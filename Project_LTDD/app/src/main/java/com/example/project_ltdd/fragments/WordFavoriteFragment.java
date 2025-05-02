package com.example.project_ltdd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.adapters.WordFavoriteAdapter;
import com.example.project_ltdd.models.MeaningModel;
import com.example.project_ltdd.models.PhoneticModel;
import com.example.project_ltdd.models.WordModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordFavoriteFragment extends Fragment {
    private static final String ARG_FOLDER_ID = "folderId";
    private static final String ARG_FOLDER_NAME = "folderName";
    List<WordModel> listWord;
    private WordFavoriteAdapter adapter;

    private LinearLayout layoutWordActions;
    private Button btnJumpTo, btnDeleteWord, btnSelectAll, btnClearAll;
    private WordModel selectedWord = null;

    public static WordFavoriteFragment newInstance(int folderId, String folderName) {
        WordFavoriteFragment fragment = new WordFavoriteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FOLDER_ID, folderId);
        args.putString(ARG_FOLDER_NAME, folderName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_favorite_word, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        int folderId = getArguments().getInt(ARG_FOLDER_ID);
        String folderName = getArguments().getString(ARG_FOLDER_NAME);
        RecyclerView rvFavoriteWords = view.findViewById(R.id.rvFavoriteWords);
        rvFavoriteWords.setLayoutManager(new LinearLayoutManager(getContext()));

        listWord = new ArrayList<>();
        if (folderId == 1) {
            listWord.add( new WordModel(
                    1L,
                    Arrays.asList(
                            new MeaningModel(101L, "chương trình truyền hình", "Noun"),
                            new MeaningModel(102L, "việc phát sóng trên TV", "Verb")
                    ),
                    Arrays.asList(
                            new PhoneticModel("https://audio-url.com/telecast.mp3", 201L, "/ˈtelikæst/"),
                            new PhoneticModel("https://audio-url.com/telecast2.mp3", 202L, "/ˌtelɪˈkæst/")
                    ),
                    "telecast"
            ));

            listWord.add(new WordModel(
                    1L,
                    Arrays.asList(
                            new MeaningModel(1L, "con mèo", "Noun"),
                            new MeaningModel(2L, "người khó chịu", "Noun")
                    ),
                    Arrays.asList(
                            new PhoneticModel("https://api.dictionaryapi.dev/media/pronunciations/en/cat-uk.mp3", 1L, "/kæt/"),
                            new PhoneticModel("https://api.dictionaryapi.dev/media/pronunciations/en/cat-us.mp3", 1L, "/kat/")
                    ),
                    "cat"
            ));
        } else {
            listWord.add( new WordModel(
                    2L,
                    Arrays.asList(
                            new MeaningModel(103L, "máy truyền hình", "Noun"),
                            new MeaningModel(104L, "ngành truyền hình", "Noun")
                    ),
                    Arrays.asList(
                            new PhoneticModel("https://audio-url.com/television.mp3", 203L, "/ˈteləˌvɪʒən/")
                    ),
                    "television"
            ));
        }

        //         Load từ vựng từ database theo folderId

        adapter = new WordFavoriteAdapter(listWord, requireContext(), getParentFragmentManager());
        rvFavoriteWords.setAdapter(adapter);

        layoutWordActions = view.findViewById(R.id.layoutButtons);
        btnJumpTo = view.findViewById(R.id.btnJump);
        btnDeleteWord = view.findViewById(R.id.btnDelete);
        btnSelectAll = view.findViewById(R.id.btnSelectAll);
        btnClearAll = view.findViewById(R.id.btnClearAll);

        adapter.setOnItemCheckListener(new WordFavoriteAdapter.OnItemCheckListener() {
            @Override
            public void onItemChecked(WordModel word) {
                layoutWordActions.setVisibility(View.VISIBLE); // Hiện LinearLayout khi có từ được chọn
                if(adapter.selectedWords.size() == adapter.getItemCount()){
                    btnSelectAll.setVisibility(View.GONE);
                    btnClearAll.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onItemUnchecked(WordModel word) {
                // Kiểm tra nếu không còn từ nào được chọn
                if (adapter.selectedWords.isEmpty()) {
                    layoutWordActions.setVisibility(View.GONE); // Ẩn LinearLayout nếu không còn từ nào được chọn
                }
            }
        });

        // Xu ly button chon tat ca
        btnSelectAll.setOnClickListener(v -> {
            selectAllItems(true);// Gọi phương thức để chọn tất cả
            btnClearAll.setVisibility(View.VISIBLE);
            v.setVisibility(View.GONE);
        });

        btnClearAll.setOnClickListener(v -> {
            selectAllItems(false);
            btnSelectAll.setVisibility(View.VISIBLE);
            v.setVisibility(View.GONE);
        });
    }

    private void selectAllItems(boolean select) {
        if (select) {
            // Chọn tất cả từ
            adapter.selectedWords.clear();
            for (int i = 0; i < adapter.getItemCount(); i++) {
                WordModel word = adapter.getWordAt(i);
                adapter.selectedWords.add(word);
            }
        } else {
            // Bỏ chọn tất cả từ
            adapter.selectedWords.clear();
        }

        adapter.notifyDataSetChanged(); // Cập nhật toàn bộ list

        // Cập nhật layout chứa nút chức năng
        layoutWordActions.setVisibility(select ? View.VISIBLE : View.GONE);
    }

}
