package com.example.project_ltdd.fragments;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project_ltdd.R;
import com.example.project_ltdd.api.retrofit_client.QuizRetrofitClient;
import com.example.project_ltdd.api.services.QuizService;
import com.example.project_ltdd.models.AnswerModel;
import com.example.project_ltdd.models.QuestionModel;
import com.example.project_ltdd.models.UserAnswerModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.function.BiConsumer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionFragment extends Fragment {
    private static final String ARG_QUESTION = "question";
    private QuestionModel question;

    private QuizService quizService = QuizRetrofitClient.getClient();

    private int testId;

    private BiConsumer<Integer, Integer> answerCallback;

    public void setAnswerCallback(BiConsumer<Integer, Integer> callback) {
        this.answerCallback = callback;
    }


    public static QuestionFragment newInstance(QuestionModel question, int testId, BiConsumer<Integer, Integer> answerCallback) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable("question", question);
        args.putInt("testId", testId);
        fragment.setArguments(args);
        fragment.setAnswerCallback(answerCallback); // Truyền callback
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = (QuestionModel) getArguments().getSerializable(ARG_QUESTION);
            testId = getArguments().getInt("testId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_exam, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        TextView tvQuestion = view.findViewById(R.id.txvQuestion);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);

        tvQuestion.setText(question.getQuestion_test());

        // Tạo danh sách đáp án
        for (AnswerModel option : question.getListAnswers()) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(option.getAnswer_text());
            radioGroup.addView(radioButton);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selected = view.findViewById(checkedId);
            int selectedAnswerId = question.getListAnswers()
                    .stream()
                    .filter(a -> a.getAnswer_text().equals(selected.getText()))
                    .findFirst()
                    .get()
                    .getAnswer_id();
            if (answerCallback != null) {
                answerCallback.accept(question.getQuestion_id(), selectedAnswerId);
            }
            saveUserAnswer(testId, selectedAnswerId);
        });

    }

    private void saveUserAnswer(int testId, int selectedAnswerId) {
        UserAnswerModel answer = new UserAnswerModel(testId, question.getQuestion_id(), selectedAnswerId);

//        // Log dữ liệu JSON gửi đi
//        Gson gson = new Gson();
//        String json = gson.toJson(answer);
//        Log.d("DEBUG_JSON_SENT", json);

        quizService.saveUserAnswer(answer).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String message = response.body().string();
                       // Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

                    } catch (IOException e){
                        Toast.makeText(requireContext(), "Lỗi đọc phản hồi", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(requireContext(), "Câu trả lời của bạn chưa được lưu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
//                Log.e("DEBUG_API_FAIL", t.getMessage(), t);
            }
        });
    }


    public interface OnAnswerSelectedListener {
        void onAnswerSelected(int questionId, int answerId);
    }

}
