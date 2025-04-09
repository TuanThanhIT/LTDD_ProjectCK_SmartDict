package com.example.project_ltdd.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project_ltdd.R;
import com.example.project_ltdd.adapter.ExamViewPager2Adapter;
import com.example.project_ltdd.models.QuestionModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExamFragment extends Fragment {
    private ViewPager2 viewPagerExam;
    private Button btnNext, btnPrev;
    private List<QuestionModel> questionList;
    private ExamViewPager2Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_exam, container, false);

        viewPagerExam = view.findViewById(R.id.viewPager2Exam);
        btnNext = view.findViewById(R.id.btnNext);
        btnPrev = view.findViewById(R.id.btnPrev);

        questionList = new ArrayList<>();
        questionList.add(new QuestionModel("What is the capital of France?",
                Arrays.asList("Paris", "London", "Berlin", "Rome")));

        questionList.add(new QuestionModel("Which planet is known as the Red Planet?",
                Arrays.asList("Earth", "Mars", "Jupiter", "Venus")));

        questionList.add(new QuestionModel("Who wrote 'Hamlet'?",
                Arrays.asList("William Shakespeare", "Mark Twain", "Charles Dickens", "J.K. Rowling")));

        questionList.add(new QuestionModel("What is the largest ocean on Earth?",
                Arrays.asList("Pacific Ocean", "Atlantic Ocean", "Indian Ocean", "Arctic Ocean")));

        questionList.add(new QuestionModel("Which element has the chemical symbol 'O'?",
                Arrays.asList("Oxygen", "Gold", "Silver", "Iron")));

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
            if(btnNext.getText().equals("Tiếp tục"))
            {
                int currentItem = viewPagerExam.getCurrentItem();
                if (currentItem < questionList.size() - 1) {
                    viewPagerExam.setCurrentItem(currentItem + 1);
                }
            }
            else{
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new ResultExamFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }

        });

        return view;
    }
}
