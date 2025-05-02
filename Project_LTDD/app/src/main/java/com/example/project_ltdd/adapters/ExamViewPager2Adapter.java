package com.example.project_ltdd.adapters;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.project_ltdd.fragments.QuestionFragment;
import com.example.project_ltdd.models.QuestionModel;

import java.util.List;
import java.util.function.BiConsumer;

public class ExamViewPager2Adapter extends FragmentStateAdapter {
    private List<QuestionModel> questions;
    private int testId;
    private BiConsumer<Integer, Integer> answerCallback; // Câu hỏi ID và Answer ID

    public ExamViewPager2Adapter(@NonNull FragmentActivity fragmentActivity, List<QuestionModel> questions, int testId, BiConsumer<Integer, Integer> answerCallback) {
        super(fragmentActivity);
        this.questions = questions;
        this.testId = testId;
        this.answerCallback = answerCallback;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        QuestionModel question = questions.get(position);
        return QuestionFragment.newInstance(question, testId, answerCallback);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}


