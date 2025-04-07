package com.example.project_ltdd.adapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.project_ltdd.fragments.QuestionFragment;
import com.example.project_ltdd.models.QuestionModel;

import java.util.List;

public class ExamViewPager2Adapter extends FragmentStateAdapter {
    private List<QuestionModel> questionList;

    public ExamViewPager2Adapter(@NonNull FragmentActivity fragmentActivity, List<QuestionModel> questions) {
        super(fragmentActivity);
        this.questionList = questions;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return QuestionFragment.newInstance(questionList.get(position));
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }
}
