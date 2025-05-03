package com.example.project_ltdd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.models.QuizHistoryModel;

import java.util.List;

public class QuizHistoryAdapter extends RecyclerView.Adapter<QuizHistoryAdapter.QuizHistoryViewHolder> {
    private Context mContext;

    private List<QuizHistoryModel> mQuizHistoryList;

    private LayoutInflater mLayoutInflater;

    public QuizHistoryAdapter(Context context, List<QuizHistoryModel> datas){
        this.mContext = context;
        this.mQuizHistoryList = datas;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public QuizHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_history_quiz, parent, false);
        return new QuizHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(QuizHistoryViewHolder holder, int position) {
        QuizHistoryModel quizHistory = mQuizHistoryList.get(position);

        holder.txvQuizName.setText(quizHistory.getQuizTitle());
        int time = quizHistory.getTestTime();
        int minutes = time / 60;
        int seconds = time % 60;
        holder.txvQuizTime.setText(minutes + " phút " + seconds + " giây");
        holder.txvQuizPoint.setText("Đúng: "+quizHistory.getTotalCorrectAnswer() + "/"+ quizHistory.getTotalQuestion());
        holder.txvQuizAttempt.setText("Lần: " + quizHistory.getAttempt());
    }

    public int getItemCount(){
        return mQuizHistoryList.size();
    }

    class QuizHistoryViewHolder extends RecyclerView.ViewHolder{
        private TextView txvQuizName;
        private TextView txvQuizTime;
        private TextView txvQuizPoint;
        private TextView txvQuizAttempt;

        public QuizHistoryViewHolder(View itemView){
            super(itemView);
            txvQuizName = (TextView) itemView.findViewById(R.id.txvQuizTest);
            txvQuizTime = (TextView) itemView.findViewById(R.id.txvQuizTime);
            txvQuizPoint = (TextView) itemView.findViewById(R.id.txvQuizPoint);
            txvQuizAttempt = (TextView) itemView.findViewById(R.id.txvQuizAttempt);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    QuizHistoryModel quizHistory = mQuizHistoryList.get(getAdapterPosition());
                    Toast.makeText(mContext, quizHistory.getQuizTitle(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
