package com.example.project_ltdd.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.adapters.WordLookedUpAdapter;
import com.example.project_ltdd.models.MeaningModel;
import com.example.project_ltdd.models.PhoneticModel;
import com.example.project_ltdd.models.WordModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordLookedUpFragment extends Fragment {

    private WordLookedUpAdapter adapter;

    private List<WordModel> listWord;
    private RecyclerView rcvLookedUpWord;
    private EditText edtLookedUp;
    private ImageView btnClearLookedUp;

    private String currentSearchQuery = "";
    private LinearLayout layoutWordActions;
    private Button btnJumpTo, btnDeleteWord, btnSelectAll, btnClearAll;
    private WordModel selectedWord = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_wordlookedup, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        rcvLookedUpWord = view.findViewById(R.id.rcvLookedUpWord);
        edtLookedUp = view.findViewById(R.id.edtLookedUp);
        btnClearLookedUp = view.findViewById(R.id.btnClearLookUp);
        listWord = new ArrayList<>();
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

        adapter = new WordLookedUpAdapter(listWord, requireContext(), getParentFragmentManager());
        rcvLookedUpWord.setAdapter(adapter);
        rcvLookedUpWord.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        rcvLookedUpWord.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.fade_in_layout)
        );

        // Bắt sự kiện nhập vào ô tìm kiếm
        edtLookedUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString();
                filterResults();
                btnClearLookedUp.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnClearLookedUp.setOnClickListener(v -> {
            edtLookedUp.setText("");
            currentSearchQuery = "";
            filterResults();
        });

        layoutWordActions = view.findViewById(R.id.layoutButtons);
        btnJumpTo = view.findViewById(R.id.btnJump);
        btnDeleteWord = view.findViewById(R.id.btnDelete);
        btnSelectAll = view.findViewById(R.id.btnSelectAll);
        btnClearAll = view.findViewById(R.id.btnClearAll);

        adapter.setOnItemCheckListener(new WordLookedUpAdapter.OnItemCheckListener() {
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

    private void filterResults() {
        adapter.getFilter().filter(currentSearchQuery, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                rcvLookedUpWord.scheduleLayoutAnimation(); // Kích hoạt animation
            }
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
