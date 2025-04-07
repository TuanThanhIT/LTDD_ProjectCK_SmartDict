package com.example.project_ltdd.fragments.admin_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.adapters.AdminQuizzAdapter;
import com.example.project_ltdd.domain.AdminQuizzDomain;

import java.util.ArrayList;

public class AdminQuizzFragment extends Fragment {
    /*private RecyclerView.Adapter adapterQuizzList;
    private RecyclerView recycleViewQuizzList;*/
    private RecyclerView recycleViewQuizzList;
    private AdminQuizzAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_admin_quizz, container, false);
        initRecyclerVew(view);
        return view;
    }

    private void initRecyclerVew(View view) {
        recycleViewQuizzList = view.findViewById(R.id.rcv_quizz_list);

        ArrayList<AdminQuizzDomain> items = new ArrayList<>();
        items.add(new AdminQuizzDomain("Quizz list 1","Content 1", "avartar1"));
        items.add(new AdminQuizzDomain("Quizz list 2", "Content 1","avartar1"));
        items.add(new AdminQuizzDomain("Quizz list 3", "Content 1","avartar1"));
        items.add(new AdminQuizzDomain("Quizz list 4","Content 1", "avartar1"));
        items.add(new AdminQuizzDomain("Quizz list 5", "Content 1","avartar1"));
        mAdapter = new AdminQuizzAdapter(items, requireContext());
        recycleViewQuizzList.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recycleViewQuizzList.setLayoutManager(linearLayoutManager);

        /*recycleViewQuizzList = view.findViewById(R.id.rcv_quizz_list);


        recycleViewQuizzList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));


        adapterQuizzList = new AdminQuizzAdapter(items, requireContext());
        recycleViewQuizzList.setAdapter(adapterQuizzList);*/
    }
}
