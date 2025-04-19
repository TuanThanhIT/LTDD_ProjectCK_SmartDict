package com.example.project_ltdd.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project_ltdd.R;
import com.example.project_ltdd.adapters.ExamViewPager2Adapter;
import com.example.project_ltdd.models.AnswerModel;
import com.example.project_ltdd.models.QuestionModel;
import com.example.project_ltdd.models.QuizModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExamFragment extends Fragment {
    private ViewPager2 viewPagerExam;
    private Button btnNext, btnPrev;
    private TextView txvCountdown, txvQuizTitle;
    private List<QuestionModel> questionList;
    private ExamViewPager2Adapter adapter;

    private int timeLeftInSeconds;// Biến lưu thời gian còn lại
    private int timeSpentInSeconds;

    private ProgressBar progressBarTime;

    private CountDownTimer countDownTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_exam, container, false);
        initViews(view);
        return view;
    }

    private void SwitchScreen(){
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ResultExamFragment());
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

        Bundle args = getArguments();
        QuizModel quizModel = (QuizModel) args.getSerializable("quizModel");
        txvQuizTitle.setText(quizModel.getNameQuiz());
        int totalTimeInSeconds = quizModel.getQuizTime();
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

        questionList = new ArrayList<>();
        // Thêm câu hỏi và các lựa chọn câu trả lời vào danh sách
        // Câu hỏi 1
        List<AnswerModel> options1 = Arrays.asList(
                new AnswerModel(1, true, "Paris"),
                new AnswerModel(2, false, "London"),
                new AnswerModel(3, false, "Berlin"),
                new AnswerModel(4, false, "Rome")
        );
        questionList.add(new QuestionModel(1, "What is the capital of France?", options1));

        // Câu hỏi 2
        List<AnswerModel> options2 = Arrays.asList(
                new AnswerModel(1, false, "Earth"),
                new AnswerModel(2, true, "Mars"),
                new AnswerModel(3, false, "Jupiter"),
                new AnswerModel(4, false, "Venus")
        );
        questionList.add(new QuestionModel(2, "Which planet is known as the Red Planet?", options2));

        // Câu hỏi 3
        List<AnswerModel> options3 = Arrays.asList(
                new AnswerModel(1, true, "William Shakespeare"),
                new AnswerModel(2, false, "Mark Twain"),
                new AnswerModel(3, false, "Charles Dickens"),
                new AnswerModel(4, false, "J.K. Rowling")
        );
        questionList.add(new QuestionModel(3, "Who wrote 'Hamlet'?", options3));

        // Câu hỏi 4
        List<AnswerModel> options4 = Arrays.asList(
                new AnswerModel(1, true, "Pacific Ocean"),
                new AnswerModel(2, false, "Atlantic Ocean"),
                new AnswerModel(3, false, "Indian Ocean"),
                new AnswerModel(4, false, "Arctic Ocean")
        );
        questionList.add(new QuestionModel(4, "What is the largest ocean on Earth?", options4));

        // Câu hỏi 5
        List<AnswerModel> options5 = Arrays.asList(
                new AnswerModel(1, true, "Oxygen"),
                new AnswerModel(2, false, "Gold"),
                new AnswerModel(3, false, "Silver"),
                new AnswerModel(4, false, "Iron")
        );
        questionList.add(new QuestionModel(5, "Which element has the chemical symbol 'O'?", options5));

        adapter = new ExamViewPager2Adapter(requireActivity(), questionList);
        viewPagerExam.setAdapter(adapter);


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
                btnNext.setText(position == questionList.size() - 1 ? "Nộp bài" : "Tiếp tục");
            }
        });

        // Nút "Tiếp"
        btnNext.setOnClickListener(v -> {
            if (btnNext.getText().equals("Tiếp tục")) {
                int currentItem = viewPagerExam.getCurrentItem();
                if (currentItem < questionList.size() - 1) {
                    viewPagerExam.setCurrentItem(currentItem + 1);
                }
            } else {
                SwitchScreen();
            }

        });
    }
}
