package com.example.project_ltdd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.adapters.WordFavoriteAdapter;
import com.example.project_ltdd.api.retrofit_client.UserRetrofitClient;
import com.example.project_ltdd.api.services.UserService;
import com.example.project_ltdd.models.FolderModel;
import com.example.project_ltdd.models.WordModel;
import com.example.project_ltdd.utils.UserPrefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordFavoriteFragment extends Fragment {
    private static final String ARG_FOLDER_ID = "folderId";
    private static final String ARG_FOLDER_NAME = "folderName";
    private List<WordModel> listWord = new ArrayList<>();
    private List<FolderModel> menuItems = new ArrayList<>();

    private WordFavoriteAdapter adapter;

    private LinearLayout layoutWordActions;
    private Button btnJumpTo, btnDeleteWord, btnSelectAll, btnClearAll;

    private RecyclerView rvFavoriteWords;

    private UserService userService = UserRetrofitClient.getClient();

    private UserPrefs userPrefs;

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
        getWordsFolder();
        getFoldersFromApi();
        return view;
    }

    private void initViews(View view) {
        rvFavoriteWords = view.findViewById(R.id.rvFavoriteWords);
        rvFavoriteWords.setLayoutManager(new LinearLayoutManager(getContext()));

        layoutWordActions = view.findViewById(R.id.layoutButtons);
        btnJumpTo = view.findViewById(R.id.btnJumpAll);
        btnDeleteWord = view.findViewById(R.id.btnDeleteAll);
        btnSelectAll = view.findViewById(R.id.btnSelectAll);
        btnClearAll = view.findViewById(R.id.btnClearAll);

        userPrefs = new UserPrefs(requireContext());

        // Xu ly button chon tat ca
        btnSelectAll.setOnClickListener(v -> {
            selectAllItems(true); // Gọi phương thức để chọn tất cả
            btnClearAll.setVisibility(View.VISIBLE);
            v.setVisibility(View.GONE);
        });

        btnClearAll.setOnClickListener(v -> {
            selectAllItems(false);
            btnSelectAll.setVisibility(View.VISIBLE);
            v.setVisibility(View.GONE);
        });

        btnJumpTo.setOnClickListener(v -> {
            showFolderPopup();
        });

        btnDeleteWord.setOnClickListener(v ->{
            handleDeleteAll();
        });

    }

    private void handleDeleteAll(){
        List<Long> listWordId = new ArrayList<>();
        for(WordModel wordModel: adapter.selectedWords)
        {
            Long id = wordModel.getWordId();
            listWordId.add(id);
        }
        deleteFavorWords(listWordId);
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

    private void setupAdapter() {
        // Load từ vựng từ database theo folderId
        // Trong phương thức setupAdapter()
        int currentFolderId = getArguments().getInt(ARG_FOLDER_ID);
        adapter = new WordFavoriteAdapter(listWord, requireContext(), getParentFragmentManager(), currentFolderId);
        rvFavoriteWords.setAdapter(adapter);

        adapter.setOnItemCheckListener(new WordFavoriteAdapter.OnItemCheckListener() {
            @Override
            public void onItemChecked(WordModel word) {
                layoutWordActions.setVisibility(View.VISIBLE); // Hiện LinearLayout khi có từ được chọn
                if (adapter.selectedWords.size() == adapter.getItemCount()) {
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
    }

    private void getWordsFolder() {
        int folderId = getArguments().getInt(ARG_FOLDER_ID);
        userService.getWordsFolder(folderId).enqueue(new Callback<List<WordModel>>() {
            @Override
            public void onResponse(Call<List<WordModel>> call, Response<List<WordModel>> response) {
                if (response.isSuccessful()) {
                    if (listWord == null) listWord = new ArrayList<>();
                    listWord.clear();
                    listWord.addAll(response.body());

                    if (adapter == null) {
                        setupAdapter();
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(requireContext(), "Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<WordModel>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showFolderPopup() {
        PopupMenu popupMenu = new PopupMenu(requireContext(), btnJumpTo);
        for (int i = 0; i < menuItems.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, ">Thư mục [" + menuItems.get(i).getFolderName() + "]");
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int idx = menuItem.getItemId();
            FolderModel chosen = menuItems.get(idx);
            // update
            List<Long> listWordId = new ArrayList<>();
            for(WordModel wordModel: adapter.selectedWords)
            {
                Long id = wordModel.getWordId();
                listWordId.add(id);
            }
            updateFolderWords(listWordId, chosen);
            return true;
        });
        popupMenu.show();
    }

    private void getFoldersFromApi() {
        userService.getFolders(userPrefs.getUserId()).enqueue(new Callback<List<FolderModel>>() {
            @Override
            public void onResponse(Call<List<FolderModel>> call, Response<List<FolderModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    menuItems = response.body();
                }
            }
            @Override
            public void onFailure(Call<List<FolderModel>> call, Throwable t) { }
        });
    }

    private void updateFolderWords(List<Long> listWordId, FolderModel folderSelect) {
        int userId = userPrefs.getUserId();

        userService.updateFolderWords(userId, folderSelect.getFolderId(), listWordId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String message = response.body().string();
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

                                // 1. Xóa các từ đã chuyển khỏi danh sách hiện tại
                                listWord.removeIf(word -> listWordId.contains(word.getWordId()));
                                adapter.selectedWords.clear(); // Xóa tất cả các từ đã chọn
                                adapter.notifyDataSetChanged();

                                getWordsFolder(); // Gọi lại API để cập nhật danh sách

                                // Gửi thông điệp để YourWordFragment cập nhật
                                Bundle result = new Bundle();
                                result.putBoolean("needReload", true);
                                getParentFragmentManager().setFragmentResult("request_reload_your_words", result);



                            } catch (IOException e) {
                                Toast.makeText(requireContext(), "Lỗi đọc phản hồi", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(requireContext(), "Thay đổi thư mục thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteFavorWords(List<Long> listWordId){
        int userId = userPrefs.getUserId();
        userService.deleteFavorWords(userId, listWordId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String message2 = response.body().string();
                        Toast.makeText(requireContext(), message2, Toast.LENGTH_SHORT).show();

                        // Xóa từ và cập nhật danh sách
                        listWord.removeIf(word -> listWordId.contains(word.getWordId()));
                        adapter.selectedWords.clear();
                        adapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Lỗi đọc dữ liệu!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(requireContext(), "Xóa thất bại, thử lại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(requireContext(), "Kết nối thất bại, thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}