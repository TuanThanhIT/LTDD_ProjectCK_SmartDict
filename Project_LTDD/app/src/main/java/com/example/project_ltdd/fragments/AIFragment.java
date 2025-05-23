package com.example.project_ltdd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.project_ltdd.R;
import com.example.project_ltdd.api.retrofit_client.MockGPTRetrofitClient;
import com.example.project_ltdd.api.services.MockGPTService;
import com.example.project_ltdd.models.GPTModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AIFragment extends Fragment {

    private EditText editTextInput;
    private Button buttonTranslate;
    private TextView textViewResult;

    public AIFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_ai, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        editTextInput = view.findViewById(R.id.editTextInput);
        buttonTranslate = view.findViewById(R.id.buttonTranslate);
        textViewResult = view.findViewById(R.id.textViewResult);


        buttonTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = editTextInput.getText().toString();

                if (!inputText.isEmpty()) {
                    fetchGptResponse(inputText);
                } else {
                    Toast.makeText(requireActivity(), "Vui lòng nhập từ cần tra.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchGptResponse(String input) {
        MockGPTService api = MockGPTRetrofitClient.getClient().create(MockGPTService.class);
        Call<List<GPTModel>> call = api.getGptResponse(input);

        call.enqueue(new Callback<List<GPTModel>>() {
            @Override
            public void onResponse(Call<List<GPTModel>> call, Response<List<GPTModel>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    GPTModel gptResponse = response.body().get(0); // lấy kết quả đầu tiên
                    textViewResult.setText(gptResponse.getResponse());
                } else {
                    textViewResult.setText("Không có kết quả.");
                }
            }

            @Override
            public void onFailure(Call<List<GPTModel>> call, Throwable t) {
                textViewResult.setText("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

}
