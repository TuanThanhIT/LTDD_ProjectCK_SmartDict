package com.example.project_ltdd.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.adapter.QuizAdapter;
import com.example.project_ltdd.adapter.QuizHistoryAdapter;
import com.example.project_ltdd.models.QuizHistoryModel;
import com.example.project_ltdd.models.QuizModel;

import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment {

    RecyclerView rvQuizList;

    RecyclerView rvHistoryQuizList;

    private QuizAdapter mQuizAdapter;
    private QuizHistoryAdapter mQuizHistoryAdapter;
    private List<QuizModel> quizList;
    private List<QuizHistoryModel> quizHistoryList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_quiz, container, false);

        // Hiển thị bài quiz
        rvQuizList = view.findViewById(R.id.rvQuizList);
        quizList = new ArrayList<>();
        quizList.add(new QuizModel("Quiz số 1", 600, 10));
        quizList.add(new QuizModel("Quiz số 2", 600, 15));
        quizList.add(new QuizModel("Quiz số 3", 1200, 20));

        mQuizAdapter = new QuizAdapter(requireContext(), quizList, requireActivity().getSupportFragmentManager());
        rvQuizList.setAdapter(mQuizAdapter);

        rvQuizList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        // Hiển thị lịch sử làm quiz
        rvHistoryQuizList = view.findViewById(R.id.rvHistoryQuizTest);
        quizHistoryList = new ArrayList<>();
        quizHistoryList.add(new QuizHistoryModel("Quiz số 1", 1, 50, 500));
        quizHistoryList.add(new QuizHistoryModel("Quiz số 2", 1, 80, 1300));
        quizHistoryList.add(new QuizHistoryModel("Quiz số 3", 1, 100, 1000));
        quizHistoryList.add(new QuizHistoryModel("Quiz số 4", 1, 50, 500));
        quizHistoryList.add(new QuizHistoryModel("Quiz số 2", 2, 80, 1300));
        quizHistoryList.add(new QuizHistoryModel("Quiz số 3", 2, 100, 1000));
        quizHistoryList.add(new QuizHistoryModel("Quiz số 5", 1, 50, 500));
        quizHistoryList.add(new QuizHistoryModel("Quiz số 6", 1, 80, 1300));
        quizHistoryList.add(new QuizHistoryModel("Quiz số 7", 1, 100, 1000));

        mQuizHistoryAdapter =  new QuizHistoryAdapter(requireContext(), quizHistoryList);
        rvHistoryQuizList.setAdapter(mQuizHistoryAdapter);
        rvHistoryQuizList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        return view;
    }
}
