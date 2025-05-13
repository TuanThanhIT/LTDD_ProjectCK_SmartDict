package com.example.project_ltdd.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
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
import androidx.viewpager2.widget.ViewPager2;

import com.example.project_ltdd.R;
import com.example.project_ltdd.adapters.ExamViewPager2Adapter;
import com.example.project_ltdd.api.retrofit_client.QuizRetrofitClient;
import com.example.project_ltdd.api.services.QuizService;
import com.example.project_ltdd.models.AnswerModel;
import com.example.project_ltdd.models.QuestionModel;
import com.example.project_ltdd.models.QuizModel;
import com.example.project_ltdd.models.TestModel;
import com.example.project_ltdd.utils.UserPrefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamFragment extends Fragment {
    private ViewPager2 viewPagerExam;
    private Button btnNext, btnPrev;
    private TextView txvCountdown, txvQuizTitle;
    private List<QuestionModel> questionsList = new ArrayList<>();
    private ExamViewPager2Adapter adapter;

    private int timeLeftInSeconds;// Biến lưu thời gian còn lại
    private int timeSpentInSeconds;

    private int testId;

    private ProgressBar progressBarTime;

    private CountDownTimer countDownTimer;

    private QuizService quizService = QuizRetrofitClient.getClient();

    private QuizModel quizModel;
    private Map<Integer, Integer> userAnswers = new HashMap<>();

    private UserPrefs userPrefs;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_exam, container, false);
        initViews(view);;
        addTestAndGetTestId(quizModel.getQuiz_id(), new TestIdCallback() {
            @Override
            public void onTestIdReady(int id) {
                testId = id;
                getQuestionsAndAnswers(quizModel.getQuiz_id());
            }
        });
        return view;
    }

    private void SwitchScreen(){
        ResultExamFragment resultFragment = new ResultExamFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("quizModel", quizModel); // Truyền QuizModel qua Bundle
        resultFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, resultFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initViews(View view){
        viewPagerExam = view.findViewById(R.id.viewPager2Exam);
        btnNext = view.findViewById(R.id.btnNext);
        btnPrev = view.findViewById(R.id.btnPrev);
        txvCountdown = view.findViewById(R.id.txvCountdown);
        txvQuizTitle = view.findViewById(R.id.txvQuizTitle);
        progressBarTime = view.findViewById(R.id.progressBarTime);
        userPrefs = new UserPrefs(requireContext());

        Bundle args = getArguments();
        quizModel = (QuizModel) args.getSerializable("quizModel");

        txvQuizTitle.setText(quizModel.getTitle());
        int totalTimeInSeconds = quizModel.getTime_limit();
        progressBarTime.setMax(totalTimeInSeconds);
        progressBarTime.setProgress(totalTimeInSeconds);
        timeLeftInSeconds = totalTimeInSeconds;

        countDownTimer = new CountDownTimer(totalTimeInSeconds * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInSeconds = (int) (millisUntilFinished / 1000); // Cập nhật biến
                timeSpentInSeconds = totalTimeInSeconds - timeLeftInSeconds;
                int minutes = timeLeftInSeconds / 60;
                int seconds = timeLeftInSeconds % 60;
                txvCountdown.setText(String.format("%02d:%02d", minutes, seconds));
                progressBarTime.setProgress(timeLeftInSeconds);
            }

            @Override
            public void onFinish() {
                progressBarTime.setProgress(0);
                txvCountdown.setText("00:00");
                SwitchScreen();
            }
        }.start();


        btnPrev.setOnClickListener(v -> {
            int currentItem = viewPagerExam.getCurrentItem();
            if (currentItem > 0) {
                viewPagerExam.setCurrentItem(currentItem - 1);
            }
        });

        // Xử lý ẩn/hiện nút khi chuyển câu
        viewPagerExam.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                btnPrev.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                btnNext.setText(position == questionsList.size() - 1 ? "Nộp bài" : "Tiếp tục");
            }
        });

        // Nút "Tiếp"
        btnNext.setOnClickListener(v -> {
            if (btnNext.getText().equals("Tiếp tục")) {
                int currentItem = viewPagerExam.getCurrentItem();
                if (currentItem < questionsList.size() - 1) {
                    viewPagerExam.setCurrentItem(currentItem + 1);
                }
            } else {
                // Kiểm tra đã trả lời hết chưa
                if (userAnswers.size() < questionsList.size()) {
                    Toast.makeText(requireContext(), "Vui lòng trả lời hết tất cả các câu hỏi trước khi nộp!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    updateResult();
                }
                SwitchScreen();
            }
        });
    }

    private void setUpAdater(){
        adapter = new ExamViewPager2Adapter(requireActivity(), questionsList, testId, (questionId, answerId) -> {
            userAnswers.put(questionId, answerId);
        });
        viewPagerExam.setAdapter(adapter);
    }
    private void getQuestionsAndAnswers(int quizId){
        quizService.getQuestionsAndAnswers(quizId).enqueue(new Callback<List<QuestionModel>>() {
            @Override
            public void onResponse(Call<List<QuestionModel>> call, Response<List<QuestionModel>> response) {
                if(response.isSuccessful()){
                    questionsList = response.body();
                    setUpAdater();
                }
                else {
                    Toast.makeText(requireContext(), "Không thể hiển thị câu hỏi của bài Quiz!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<QuestionModel>> call, Throwable t) {
                Toast.makeText(requireContext(), "Kết nối thất bại, thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTestAndGetTestId(int quizId, TestIdCallback callback) {
        int userId = userPrefs.getUserId();
        quizService.addTest(userId, quizId).enqueue(new Callback<TestModel>() {
            @Override
            public void onResponse(Call<TestModel> call, Response<TestModel> response) {
                if (response.isSuccessful()) {
                    // Sau khi add thành công, lấy testId
                    quizService.getTestId(userId, quizId).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.isSuccessful()) {
                                int testId = response.body();
                                callback.onTestIdReady(testId); // Gọi callback khi đã có testId
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Toast.makeText(requireContext(), "Không lấy được Test ID", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(requireContext(), "Không thể tạo bài thi mới", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TestModel> call, Throwable t) {
                Toast.makeText(requireContext(), "Kết nối thất bại, thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface TestIdCallback {
        void onTestIdReady(int testId);
    }

    private void updateResult(){
        int userId = userPrefs.getUserId();
        quizService.updateResult(userId, quizModel.getQuiz_id(), timeSpentInSeconds).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String message = response.body().string();
//                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    } catch (IOException e){
                        Toast.makeText(requireContext(), "Lỗi đọc phản hồi", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(requireContext(), "Kết nối thất bại, thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}