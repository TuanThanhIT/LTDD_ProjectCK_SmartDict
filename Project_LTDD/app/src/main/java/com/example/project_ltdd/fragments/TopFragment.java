package com.example.project_ltdd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.adapter.TopAdapter;

import java.util.ArrayList;
import java.util.List;

public class TopFragment extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_DATA = "data";

    public static TopFragment newInstance(String title, List<String> data) {
        TopFragment fragment = new TopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putStringArrayList(ARG_DATA, new ArrayList<>(data));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_top, container, false);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        // Nhận dữ liệu từ arguments
        String title = getArguments() != null ? getArguments().getString(ARG_TITLE) : "Top List";
        List<String> data = getArguments() != null ? getArguments().getStringArrayList(ARG_DATA) : new ArrayList<>();

        txtTitle.setText(title);

        // Cấu hình RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new TopAdapter(data));

        return view;
    }
}
//👉 Nhận dữ liệu: Dùng Bundle để truyền tiêu đề và danh sách.
//👉 Hiển thị RecyclerView: Hiển thị danh sách dữ liệu trong từng Fragment.
