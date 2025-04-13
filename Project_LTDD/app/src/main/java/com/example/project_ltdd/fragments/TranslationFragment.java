package com.example.project_ltdd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project_ltdd.R;
import com.example.project_ltdd.api.requests.TranslateRequest;
import com.example.project_ltdd.api.responses.TranslateResponse;
import com.example.project_ltdd.api.services.TranslateService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TranslationFragment extends Fragment {

    EditText editTextInput;
    RadioButton radioEnToVi, radioViToEn;
    Button buttonTranslate;
    TextView textViewResult;
    TranslateService translateService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_translation, container, false);
        editTextInput = view.findViewById(R.id.editTextInput);
        radioEnToVi = view.findViewById(R.id.radioEnToVi);
        radioViToEn = view.findViewById(R.id.radioViToEn);
        buttonTranslate = view.findViewById(R.id.buttonTranslate);
        textViewResult = view.findViewById(R.id.textViewResult);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://libretranslate.com/") // Đúng
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        translateService = retrofit.create(TranslateService.class);

        buttonTranslate.setOnClickListener(v -> {
            String text = editTextInput.getText().toString();
            if (text.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập văn bản", Toast.LENGTH_SHORT).show();
                return;
            }

            String source = radioEnToVi.isChecked() ? "en" : "vi";
            String target = radioEnToVi.isChecked() ? "vi" : "en";

            TranslateRequest request = new TranslateRequest(text, source, target);


            translateService.translate(request).enqueue(new Callback<TranslateResponse>() {
                @Override
                public void onResponse(Call<TranslateResponse> call, Response<TranslateResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        textViewResult.setText(response.body().getTranslatedText());
                    } else {
                        textViewResult.setText("Lỗi dịch: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<TranslateResponse> call, Throwable t) {
                    textViewResult.setText("Lỗi kết nối: " + t.getMessage());
                }
            });
        });
        return view;
    }
}
