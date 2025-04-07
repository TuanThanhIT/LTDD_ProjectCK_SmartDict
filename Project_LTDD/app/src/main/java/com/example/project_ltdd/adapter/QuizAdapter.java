package com.example.project_ltdd.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.fragments.ExamFragment;
import com.example.project_ltdd.models.QuizModel;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {
    private Context mContext;
    private List<QuizModel> mQuiz;
    private LayoutInflater mLayoutInflater;

    private FragmentManager mFragmentManager; // Thêm FragmentManager

    public QuizAdapter(Context context, List<QuizModel> datas, FragmentManager fragmentManager){
        mContext = context;
        mQuiz = datas;
        mLayoutInflater = LayoutInflater.from(context);
        mFragmentManager = fragmentManager;
    }

    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(itemView);
    }

    public void onBindViewHolder(QuizViewHolder holder, int posotion){
        QuizModel quiz = mQuiz.get(posotion);

        holder.tvQuizName.setText(quiz.getNameQuiz()); // Hiển thị tên quiz
        holder.tvQuestionCount.setText("Số câu hỏi: " + quiz.getQuestionCount());
        int time = quiz.getQuizTime();
        int minutes = time / 60;
        int seconds = time % 60;
        holder.tvQuizTime.setText("Thời gian: "+ minutes + " phút " + seconds + " giây"); // Hiển thị thời gian làm bà
        holder.btnStartQuiz.setOnClickListener(view -> {
            ExamFragment examFragment = new ExamFragment();

            // Chuyển sang ExamFragment
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, examFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

    }

    public int getItemCount(){
        return mQuiz.size();
    }

    class QuizViewHolder extends RecyclerView.ViewHolder{
        private TextView tvQuizName;
        private TextView tvQuestionCount;
        private TextView tvQuizTime;

        private Button btnStartQuiz;


        public QuizViewHolder(View itemView){
            super(itemView);
            tvQuizName = (TextView) itemView.findViewById(R.id.txvQuizTitle);
            tvQuestionCount = (TextView) itemView.findViewById(R.id.txvQuestionCount);
            tvQuizTime = (TextView) itemView.findViewById(R.id.txvQuizTime);
            btnStartQuiz = (Button) itemView.findViewById(R.id.btnStartQuiz);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    QuizModel quiz = mQuiz.get(getAdapterPosition());
                    Toast.makeText(mContext, quiz.getNameQuiz(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
