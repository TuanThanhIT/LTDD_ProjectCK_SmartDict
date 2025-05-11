package com.example.project_ltdd.fragments;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.project_ltdd.R;
import com.example.project_ltdd.utils.ReminderReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class SettingFragment extends Fragment {
    private TimePicker timePickerReminder;
    private Button btnSaveReminder, btnFeedback;
    private TextView textReminder;
    private EditText editTextReminder;
    private ListView listViewReminders;

    private SharedPreferences sharedPreferences;
    private ArrayList<String> keyList;
    private ArrayList<String> reminderList;
    private ArrayAdapter<String> reminderAdapter;

    private static final int NOTIFICATION_PERMISSION_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_setting, container, false);

        // Thiết lập Toolbar làm ActionBar và hiển thị title

        // Ánh xạ các view
        timePickerReminder = view.findViewById(R.id.timePickerReminder);
        btnSaveReminder = view.findViewById(R.id.btnSaveReminder);
        btnFeedback = view.findViewById(R.id.btnFeedback);
        textReminder = view.findViewById(R.id.textReminder);
        editTextReminder = view.findViewById(R.id.editTextReminder);
        listViewReminders = view.findViewById(R.id.listViewReminders);

        sharedPreferences = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        keyList = new ArrayList<>();
        reminderList = new ArrayList<>();

        // Thiết lập adapter cho ListView
        reminderAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, reminderList);
        listViewReminders.setAdapter(reminderAdapter);

        createNotificationChannel();
        requestNotificationPermission();

        // Load các nhắc nhở đã lưu
        loadReminders();

        // Hiển thị TimePicker và EditText khi nhấn vào textReminder
        textReminder.setOnClickListener(v -> {
            timePickerReminder.setVisibility(View.VISIBLE);
            editTextReminder.setVisibility(View.VISIBLE);
            btnSaveReminder.setVisibility(View.VISIBLE);
        });

        // Xử lý lưu nhắc nhở
        btnSaveReminder.setOnClickListener(v -> saveAndScheduleReminder());

        // Xử lý gửi feedback
        btnFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"your_email@example.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Góp ý / Báo lỗi ứng dụng");
            startActivity(Intent.createChooser(intent, "Chọn ứng dụng gửi email"));
        });

        // Xử lý xóa nhắc nhở khi click vào item trong ListView
        listViewReminders.setOnItemClickListener((parent, view1, position, id) -> removeReminder(position));

        return view;
    }

    private void saveAndScheduleReminder() {
        String customReminder = editTextReminder.getText().toString().trim();
        if (customReminder.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập nhắc nhở", Toast.LENGTH_SHORT).show();
            return;
        }

        int hour = timePickerReminder.getHour();
        int minute = timePickerReminder.getMinute();

        // Sinh key duy nhất dựa trên timestamp
        String key = "reminder_" + System.currentTimeMillis();
        String value = customReminder + " at " + hour + ":" + String.format("%02d", minute);

        // Lưu vào SharedPreferences
        sharedPreferences.edit().putString(key, value).apply();

        // Đặt Alarm
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent intent = new Intent(requireContext(), ReminderReceiver.class);
        intent.putExtra("reminder_message", customReminder);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                requireContext(), key.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager alarmManager = (AlarmManager) requireContext()
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );

        // Cập nhật UI
        keyList.add(key);
        reminderList.add(value);
        reminderAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Đã đặt nhắc nhở " + value, Toast.LENGTH_SHORT).show();
    }

    private void loadReminders() {
        keyList.clear();
        reminderList.clear();
        Map<String, ?> all = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            if (entry.getKey().startsWith("reminder_")) {
                keyList.add(entry.getKey());
                reminderList.add(entry.getValue().toString());
            }
        }
        reminderAdapter.notifyDataSetChanged();
    }

    private void removeReminder(int position) {
        String key = keyList.get(position);
        // Hủy Alarm với cùng requestCode = key.hashCode()
        Intent intent = new Intent(requireContext(), ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                requireContext(), key.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager alarmManager = (AlarmManager) requireContext()
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        // Xóa khỏi SharedPreferences và cập nhật UI
        sharedPreferences.edit().remove(key).apply();
        keyList.remove(position);
        reminderList.remove(position);
        reminderAdapter.notifyDataSetChanged();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ReminderChannel";
            String description = "Kênh nhắc nhở học tập";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(
                    "reminder_channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = requireContext()
                    .getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE
                );
            }
        }
    }
}
