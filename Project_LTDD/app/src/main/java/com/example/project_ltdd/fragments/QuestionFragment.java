package com.example.project_ltdd.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project_ltdd.R;
import com.example.project_ltdd.models.AnswerModel;
import com.example.project_ltdd.models.QuestionModel;

public class QuestionFragment extends Fragment {
    private static final String ARG_QUESTION = "question";
    private QuestionModel question;

    public static QuestionFragment newInstance(QuestionModel question) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUESTION, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = (QuestionModel) getArguments().getSerializable(ARG_QUESTION);
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

        tvQuestion.setText(question.getQuestionText());

        // Tạo danh sách đáp án
        for (AnswerModel option : question.getOptions()) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(option.getAnswerText());
            radioGroup.addView(radioButton);
        }

    }
}
