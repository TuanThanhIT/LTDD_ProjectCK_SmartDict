package com.example.project_ltdd.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.project_ltdd.R;
import com.example.project_ltdd.api.retrofit_client.QuizRetrofitClient;
import com.example.project_ltdd.api.services.QuizService;
import com.example.project_ltdd.models.QuizModel;
import com.example.project_ltdd.models.TestModel;
import com.example.project_ltdd.utils.UserPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultExamFragment extends Fragment {

    private Button btnExit;

    private TextView txvResultTitle, txvResultTime, txvResultTotalQuestions, txvResultCorrectAnswer, txvResultScore;

    ProgressBar progressBarResult;

    private QuizModel quizModel;

    private TestModel testModel;

    private QuizService quizService = QuizRetrofitClient.getClient();

    private UserPrefs userPrefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_result_exam, container, false);
        initViews(view);
        getTestResult();
        return view;
    }

    private void initViews(View view){
        if (getArguments() != null) {
            quizModel = (QuizModel) getArguments().getSerializable("quizModel");
        }

        btnExit = view.findViewById(R.id.btnResultExit);
        txvResultTitle = view.findViewById(R.id.txvResultTitle);
        txvResultTime = view.findViewById(R.id.txvResultTime);
        txvResultTotalQuestions = view.findViewById(R.id.txvResultTotalQuestions);
        txvResultCorrectAnswer = view.findViewById(R.id.txvResultCorrectAnswers);
        txvResultScore = view.findViewById(R.id.txvResultScore);
        progressBarResult = view.findViewById(R.id.progressBarResult);
        userPrefs = new UserPrefs(requireContext());

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new QuizFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        Toast.makeText(requireContext(), "Kết quả bài làm của bạn!", Toast.LENGTH_SHORT).show();
    }
    private void setupData(){
        txvResultTitle.setText(quizModel.getTitle());
        txvResultTotalQuestions.setText("Tổng số câu hỏi: "+quizModel.getTotal_question());
        int totalTime = testModel.getTest_time();
        int minutes = totalTime / 60;
        int seconds = totalTime % 60;
        txvResultTime.setText("Thời gian làm bài: "+minutes+" phút "+seconds+" giây" );
        txvResultCorrectAnswer.setText("Số câu trả lời đúng: " +testModel.getTotal_correct_answer());
        txvResultScore.setText("Đúng: "+testModel.getTotal_correct_answer()+"/"+quizModel.getTotal_question() + " câu hỏi");
        progressBarResult.setMax(quizModel.getTotal_question());
        progressBarResult.setProgress(testModel.getTotal_correct_answer());
    }

    private void getTestResult(){
        int userId = userPrefs.getUserId();
        quizService.getTestResult(userId, quizModel.getQuiz_id()).enqueue(new Callback<TestModel>() {
            @Override
            public void onResponse(Call<TestModel> call, Response<TestModel> response) {
                if(response.isSuccessful()){
                    testModel = response.body();
                    setupData();
                }
                else {
                    Toast.makeText(requireContext(), "Không thể hiển thị kết quả bài làm của bạn!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TestModel> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
