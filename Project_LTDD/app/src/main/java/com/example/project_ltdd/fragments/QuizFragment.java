package com.example.project_ltdd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.adapters.QuizAdapter;
import com.example.project_ltdd.adapters.QuizHistoryAdapter;
import com.example.project_ltdd.api.retrofit_client.QuizRetrofitClient;
import com.example.project_ltdd.api.services.QuizService;
import com.example.project_ltdd.models.QuizHistoryModel;
import com.example.project_ltdd.models.QuizModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizFragment extends Fragment {

    QuizService quizService = QuizRetrofitClient.getClient();

    RecyclerView rvQuizList;

    RecyclerView rvHistoryQuizList;

    private QuizAdapter mQuizAdapter;
    private QuizHistoryAdapter mQuizHistoryAdapter;
    private List<QuizModel> quizList = new ArrayList<>();
    private List<QuizHistoryModel> quizHistoryList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_quiz, container, false);
        initViews(view);
        getQuizzes();
        getQuizesHistory();
        return view;
    }

    private void initViews(View view){
        // Hiển thị bài quiz
        rvQuizList = view.findViewById(R.id.rvQuizList);
        // Hiển thị lịch sử làm quiz
        rvHistoryQuizList = view.findViewById(R.id.rvHistoryQuizTest);
    }

    private void setUpQuizAdapter(){
        mQuizAdapter = new QuizAdapter(requireContext(), quizList, requireActivity().getSupportFragmentManager());
        rvQuizList.setAdapter(mQuizAdapter);
        rvQuizList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void setupQuizHistoryAdapter(){
        mQuizHistoryAdapter =  new QuizHistoryAdapter(requireContext(), quizHistoryList);
        rvHistoryQuizList.setAdapter(mQuizHistoryAdapter);
        rvHistoryQuizList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void getQuizzes(){
        quizService.getQuizzes().enqueue(new Callback<List<QuizModel>>() {
            @Override
            public void onResponse(Call<List<QuizModel>> call, Response<List<QuizModel>> response) {
                if(response.isSuccessful())
                {
                    quizList = response.body();
                    setUpQuizAdapter();
                    Toast.makeText(requireContext(),"Quiz",  Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(requireContext(), "Không thể hiển thị các bài quiz", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<QuizModel>> call, Throwable t) {
                Toast.makeText(requireContext(), "Kết nối thất bại, thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getQuizesHistory() {
        int userId = 1;
        quizService.getHistoryTest(userId).enqueue(new Callback<List<QuizHistoryModel>>() {
            @Override
            public void onResponse(Call<List<QuizHistoryModel>> call, Response<List<QuizHistoryModel>> response) {
                if(response.isSuccessful()){
                    quizHistoryList = response.body();
                    setupQuizHistoryAdapter();
                }
                else{
                    Toast.makeText(requireContext(), "Không thể hiển thị lịch sử làm quiz của bạn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<QuizHistoryModel>> call, Throwable t) {
                Toast.makeText(requireContext(), "Kết nối thất bại, thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
