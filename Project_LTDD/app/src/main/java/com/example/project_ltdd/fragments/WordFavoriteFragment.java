package com.example.project_ltdd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.adapter.WordAdapter;
import com.example.project_ltdd.models.WordModel;

import java.util.ArrayList;
import java.util.List;

public class WordFavoriteFragment extends Fragment {
    private static final String ARG_FOLDER_ID = "folderId";
    List<WordModel> listWord;

    public static WordFavoriteFragment newInstance(int folderId, String folderName) {
        WordFavoriteFragment fragment = new WordFavoriteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FOLDER_ID, folderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_favorite_word, container, false);

        int folderId = getArguments().getInt(ARG_FOLDER_ID);
        RecyclerView rvFavoriteWords = view.findViewById(R.id.rvFavoriteWords);
        rvFavoriteWords.setLayoutManager(new LinearLayoutManager(getContext()));

        listWord = new ArrayList<>();
        if(folderId == 1){
            listWord.add(new WordModel("telecast", "[ˈtelikæst]", "chương trình truyền hình", "Noun"));
        } else {
            listWord.add( new WordModel("teleport", "[ˈtelɪpɔːt]", "dịch chuyển tức thời", "Noun"));
        }

        //         Load từ vựng từ database theo folderId

        WordAdapter adapter = new WordAdapter(listWord);
        rvFavoriteWords.setAdapter(adapter);
        return view;
    }

}
