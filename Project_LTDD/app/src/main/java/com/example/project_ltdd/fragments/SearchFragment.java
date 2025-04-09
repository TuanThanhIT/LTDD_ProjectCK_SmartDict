package com.example.project_ltdd.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_ltdd.R;
import com.example.project_ltdd.adapter.VocabularyAdapter;
import com.example.project_ltdd.models.VocabularyModel;
import com.example.project_ltdd.utils.DrawingView;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {

    private ActivityResultLauncher<Intent> voiceLauncher;
    private EditText edtSearch;
    private ImageView btnClear;
    private RecyclerView rvSuggestions;
    private VocabularyAdapter adapter;
    private List<VocabularyModel> fakeData;
    private ImageView btnVoice;

    private ImageView btnWrite;

    private ImageView imgGif;

    private String currentSearchQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_search, container, false);

        edtSearch = view.findViewById(R.id.edtSearch);
        btnClear = view.findViewById(R.id.btnClear);
        rvSuggestions = view.findViewById(R.id.rvSuggestions);
        btnVoice = view.findViewById(R.id.btnVoice);
        btnWrite = view.findViewById(R.id.btnWrite);
        imgGif = view.findViewById(R.id.imgGif);

        // Chạy Gif
        Glide.with(this)
                .asGif()  // Chỉ định tải GIF
                .load(R.drawable.gi_search)  // Đường dẫn đến GIF trong drawable
                .into(imgGif);  // ImageView mà bạn muốn hiển thị GIF

        // Tạo dữ liệu giả
        fakeData = Arrays.asList(
                new VocabularyModel("telecast", "[ˈtelikæst]", "chương trình truyền hình", "Noun"),
                new VocabularyModel("television", "[ˈteləˌvɪʒən]", "tivi", "Noun"),
                new VocabularyModel("teleport", "[ˈtelɪpɔːt]", "dịch chuyển tức thời", "Noun"),
                new VocabularyModel("telecom", "[ˈtelɪkɒm]", "viễn thông", "Noun"),
                new VocabularyModel("telegraph", "[ˈtelɪɡræf]", "máy điện báo", "Noun"),
                new VocabularyModel("telephone", "[ˈtelɪfəʊn]", "điện thoại", "Noun"),
                new VocabularyModel("telepathy", "[təˈlepəθi]", "thần giao cách cảm", "Noun"),
                new VocabularyModel("telephoto", "[ˌtelɪˈfəʊtəʊ]", "ống kính tele", "Noun"),
                new VocabularyModel("telemarketer", "[ˈtelimɑːkɪtə(r)]", "nhân viên tiếp thị qua điện thoại", "Noun")
        );

        btnVoice.setOnClickListener(v -> startVoiceInput());

        adapter = new VocabularyAdapter(fakeData);
        rvSuggestions.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvSuggestions.setAdapter(adapter);
        rvSuggestions.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.fade_in_layout)
        );

        // Bắt sự kiện nhập vào ô tìm kiếm
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rvSuggestions.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                currentSearchQuery = s.toString();
                filterResults();
                btnClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Nút clear
        btnClear.setOnClickListener(v -> {
            edtSearch.setText("");
            currentSearchQuery = "";
            filterResults();
        });

        // Đăng ký launcher để nhận kết quả từ giọng nói
        voiceLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ArrayList<String> matches = result.getData()
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && !matches.isEmpty()) {
                            String spokenText = matches.get(0);
                            edtSearch.setText(spokenText);
                            currentSearchQuery = spokenText;
                            filterResults();
                        }
                    }
                }
        );

        btnWrite.setOnClickListener(v -> openDrawPopup());

        return view;
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak a word...");

        try {
            voiceLauncher.launch(intent);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Thiết bị không hỗ trợ Voice Search", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức để lọc kết quả tìm kiếm dựa trên từ người dùng nhập hoặc nói
    private void filterResults() {
        adapter.getFilter().filter(currentSearchQuery, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                rvSuggestions.scheduleLayoutAnimation(); // Kích hoạt animation
            }
        });
    }

    // Tìm kiếm bằng chu viết
    private void openDrawPopup() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View view = inflater.inflate(R.layout.popup_draw, null);

        DrawingView drawingView = view.findViewById(R.id.drawingView);
        Button btnClear = view.findViewById(R.id.btnClear);
        Button btnUndo = view.findViewById(R.id.btnUndo);
        Button btnDone = view.findViewById(R.id.btnDone);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setCancelable(true)
                .create();

        btnClear.setOnClickListener(v -> drawingView.clearCanvas());
        btnUndo.setOnClickListener(v -> drawingView.undo());

        btnDone.setOnClickListener(v -> {
            Bitmap bitmap = drawingView.getBitmap();
            recognizeTextFromBitmap(bitmap);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void recognizeTextFromBitmap(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(result -> {
                    String resultText = result.getText();
                    edtSearch.setText(resultText);
                    currentSearchQuery = resultText;
                    filterResults();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Lỗi nhận dạng", Toast.LENGTH_SHORT).show();
                });
    }
}
