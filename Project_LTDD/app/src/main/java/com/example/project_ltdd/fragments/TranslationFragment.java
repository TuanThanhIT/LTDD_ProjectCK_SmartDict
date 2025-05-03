package com.example.project_ltdd.fragments;

//Sử dụng Google ML Kit Translation (On-Device, Miễn phí)

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.project_ltdd.R;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class TranslationFragment extends Fragment {

    // Khai báo các thành phần UI
    private EditText editTextInput;
    private RadioGroup radioGroup;
    private RadioButton radioEnToVi, radioViToEn;
    private Button buttonTranslate;
    private TextView textViewResult;
    private Translator translator;     // Biến xử lý dịch thuật
    private String sourceLang = TranslateLanguage.ENGLISH;
    private String targetLang = TranslateLanguage.VIETNAMESE;
    private ProgressDialog progressDialog;

    // Phương thức khởi tạo giao diện
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_translation, container, false);
        initViews(view); // Khởi tạo các thành phần UI
        setupInputField(); // Thiết lập sự kiện bàn phím
        checkInternetConnection(); // Kiểm tra kết nối mạng
        setupTranslator(); // Khởi tạo công cụ dịch
        return view;
    }

    // Xử lý sau khi view được tạo
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupInputField(); // Đảm bảo xử lý bàn phím được thiết lập
    }

    // Xử lý khi Fragment tạm dừng
    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss(); // Đảm bảo dialog không bị treo
        }
    }

    // ===================================================================
    // CÁC PHƯƠNG THỨC XỬ LÝ CHỨC NĂNG CHÍNH
    // ===================================================================

    /** Thiết lập sự kiện cho ô nhập liệu */
    private void setupInputField() {
        // Xử lý khi nhấn nút Done trên bàn phím
        editTextInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performTranslation(); // Thực hiện dịch
                hideKeyboard(); // Ẩn bàn phím
                return true;
            }
            return false;
        });
    }

    /** Ẩn bàn phím ảo */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextInput.getWindowToken(), 0);
    }

    /** Kiểm tra kết nối Internet */
    private void checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnected()) {
            showToast("Vui lòng kết nối Internet để tải model dịch");
        }
    }

    // ===================================================================
    // PHƯƠNG THỨC KHỞI TẠO VÀ XỬ LÝ GIAO DIỆN
    // ===================================================================

    /** Khởi tạo và ánh xạ các thành phần UI */
    private void initViews(View view) {
        // Ánh xạ các view từ XML
        editTextInput = view.findViewById(R.id.editTextInput);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioEnToVi = view.findViewById(R.id.radioEnToVi);
        radioViToEn = view.findViewById(R.id.radioViToEn);
        buttonTranslate = view.findViewById(R.id.buttonTranslate);
        textViewResult = view.findViewById(R.id.textViewResult);

        // Xử lý thay đổi ngôn ngữ dịch
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioEnToVi) {
                updateLanguagePair(TranslateLanguage.ENGLISH, TranslateLanguage.VIETNAMESE);
            } else {
                updateLanguagePair(TranslateLanguage.VIETNAMESE, TranslateLanguage.ENGLISH);
            }
        });

        // Xử lý sự kiện nhấn nút dịch
        buttonTranslate.setOnClickListener(v -> performTranslation());

        // Theo dõi thay đổi văn bản nhập liệu
        editTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Reset kết quả khi input trống
                textViewResult.postDelayed(() -> {
                    if (s.toString().trim().isEmpty()) {
                        textViewResult.setText("Kết quả sẽ hiển thị ở đây");
                        textViewResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                    }
                }, 100); // Độ trễ 100ms để tránh lag
            }
        });
    }

    // ===================================================================
    // CÁC PHƯƠNG THỨC XỬ LÝ DỊCH THUẬT
    // ===================================================================

    /** Khởi tạo công cụ dịch */
    private void setupTranslator() {
        // Cấu hình ngôn ngữ nguồn và đích
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLang)
                .setTargetLanguage(targetLang)
                .build();

        translator = Translation.getClient(options);
        downloadModel(); // Tải model dịch
    }

    /** Cập nhật cặp ngôn ngữ dịch */
    private void updateLanguagePair(String newSource, String newTarget) {
        if (translator != null) {
            translator.close(); // Giải phóng tài nguyên cũ
        }
        sourceLang = newSource;
        targetLang = newTarget;
        setupTranslator(); // Khởi tạo lại với ngôn ngữ mới
    }

    /** Tải model dịch về thiết bị */
    private void downloadModel() {
        checkInternetConnection(); // Kiểm tra mạng trước khi tải
        translator.downloadModelIfNeeded()
                .addOnSuccessListener(unused -> showToast("sẵn sàng dịch"))
                .addOnFailureListener(e -> showToast("Lỗi tải model: " + e.getMessage()));
    }

    /** Thực hiện quá trình dịch */
    private void performTranslation() {
        String input = editTextInput.getText().toString().trim();
        if (input.isEmpty()) {
            showToast("Vui lòng nhập văn bản");
            return;
        }

        showProgressDialog(); // Hiển thị loading

        // Xử lý dịch và hiển thị kết quả
        translator.translate(input)
                .addOnSuccessListener(translatedText -> {
                    textViewResult.setText(translatedText);
                    dismissProgressDialog();
                })
                .addOnFailureListener(e -> {
                    showToast("Lỗi dịch: " + e.getMessage());
                    dismissProgressDialog();
                });
    }

    // ===================================================================
    // CÁC PHƯƠNG THỨC HỖ TRỢ
    // ===================================================================

    /** Hiển thị dialog loading */
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang dịch...");
        progressDialog.setCancelable(false); // Không cho phép hủy bằng back button
        progressDialog.show();
    }

    /** Ẩn dialog loading */
    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /** Hiển thị thông báo ngắn */
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    // ===================================================================
    // XỬ LÝ VÒNG ĐỜI FRAGMENT
    // ===================================================================

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (translator != null) {
            translator.close(); // Dọn dẹp tài nguyên khi Fragment bị hủy
        }
    }
}