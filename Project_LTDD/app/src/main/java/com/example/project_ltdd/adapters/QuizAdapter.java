package com.example.project_ltdd.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

        holder.tvQuizName.setText(quiz.getTitle()); // Hiển thị tên quiz
        holder.tvQuestionCount.setText("Số câu hỏi: " + quiz.getTotal_question());
        holder.tvQuizDescription.setText("Mô tả: "+quiz.getDescription());
        int time = quiz.getTime_limit();
        int minutes = time / 60;
        int seconds = time % 60;
        holder.tvQuizTime.setText("Thời gian: "+ minutes + " phút " + seconds + " giây"); // Hiển thị thời gian làm bà
        // Ví dụ trong adapter hoặc activity/fragment:
        Glide.with(mContext)
                .load(quiz.getImage())
                .placeholder(R.drawable.sample_image) // ảnh hiển thị trong lúc chờ load
                .error(R.drawable.error_image)   // ảnh nếu load lỗi
                .into(holder.imgQuiz);
        holder.btnStartQuiz.setOnClickListener(view -> {
            ExamFragment examFragment = new ExamFragment();

            // Gói dữ liệu vào Bundle
            Bundle bundle = new Bundle();
            bundle.putSerializable("quizModel", quiz);// key: "timeSpent"
            examFragment.setArguments(bundle);


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

        private TextView tvQuizDescription;

        private Button btnStartQuiz;

        private ImageView imgQuiz;


        public QuizViewHolder(View itemView){
            super(itemView);
            tvQuizName = (TextView) itemView.findViewById(R.id.txvQuizTitle);
            tvQuestionCount = (TextView) itemView.findViewById(R.id.txvQuestionCount);
            tvQuizTime = (TextView) itemView.findViewById(R.id.txvQuizTime);
            tvQuizDescription = (TextView) itemView.findViewById(R.id.txvQuizDescription);
            imgQuiz = (ImageView) itemView.findViewById(R.id.imgQuiz);
            btnStartQuiz = (Button) itemView.findViewById(R.id.btnStartQuiz);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    QuizModel quiz = mQuiz.get(getAdapterPosition());
                    Toast.makeText(mContext, quiz.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
