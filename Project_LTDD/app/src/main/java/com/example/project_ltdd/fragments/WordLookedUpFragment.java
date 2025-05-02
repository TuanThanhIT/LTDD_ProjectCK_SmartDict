package com.example.project_ltdd.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.adapters.WordLookedUpAdapter;
import com.example.project_ltdd.api.retrofit_client.UserRetrofitClient;
import com.example.project_ltdd.api.services.UserService;
import com.example.project_ltdd.models.FavoriteWordModel;
import com.example.project_ltdd.models.FolderModel;
import com.example.project_ltdd.models.MeaningModel;
import com.example.project_ltdd.models.PhoneticModel;
import com.example.project_ltdd.models.WordModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordLookedUpFragment extends Fragment {

    private WordLookedUpAdapter adapter;

    private List<WordModel> listWord = new ArrayList<>();
    private RecyclerView rcvLookedUpWord;
    private EditText edtLookedUp;
    private ImageView btnClearLookedUp;

    private String currentSearchQuery = "";
    private LinearLayout layoutWordActions;
    private Button btnJumpTo, btnDeleteWord, btnSelectAll, btnClearAll;
    private UserService userService = UserRetrofitClient.getClient();

    private List<FolderModel> menuItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_wordlookedup, container, false);
        initViews(view);
        getWordLookedUpFromApi();
        getFoldersFromApi();
        return view;
    }

    private void initViews(View view){
        rcvLookedUpWord = view.findViewById(R.id.rcvLookedUpWord);
        edtLookedUp = view.findViewById(R.id.edtLookedUp);
        btnClearLookedUp = view.findViewById(R.id.btnClearLookUp);
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

        btnJumpTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFolderPopup();
            }
        });

        btnDeleteWord.setOnClickListener(v ->{
            handleDeleteWords();
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

    private void setUpAdapter(){
        adapter = new WordLookedUpAdapter(listWord, requireContext(), getParentFragmentManager());
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

        rcvLookedUpWord.setAdapter(adapter);
        rcvLookedUpWord.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        rcvLookedUpWord.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.fade_in_layout)
        );
    }

    private void getWordLookedUpFromApi() {
        int userId = 1;
        userService.getWordLookedUp(userId).enqueue(new Callback<List<WordModel>>() {
            @Override
            public void onResponse(Call<List<WordModel>> call, Response<List<WordModel>> response) {
                if (response.isSuccessful()) {
                    listWord = response.body();
                    setUpAdapter();
                    Toast.makeText(requireContext(), "Từ đã tra!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Không thể hiển thị danh sách Từ đã tra của bạn! Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<WordModel>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleDeleteWords(){
        List<Long> listWordsId = new ArrayList<>();
        for(WordModel wordModel: adapter.selectedWords)
        {
            Long id = wordModel.getWordId();
            listWordsId.add(id);
        }
        deleteSearchWords(listWordsId); // Gửi API xoá

        // Cập nhật dữ liệu trong danh sách
        listWord.removeIf(word -> listWordsId.contains(word.getWordId()));

        // Xóa lựa chọn và cập nhật RecyclerView
        adapter.selectedWords.clear();
        adapter = new WordLookedUpAdapter(listWord, getContext(), getParentFragmentManager());
        rcvLookedUpWord.setAdapter(adapter);
    }

    private void showFolderPopup() {
        PopupMenu popupMenu = new PopupMenu(requireContext(), btnJumpTo);
        for (int i = 0; i < menuItems.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, ">Thư mục [" + menuItems.get(i).getFolderName() + "]");
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int idx = menuItem.getItemId();
            FolderModel chosen = menuItems.get(idx);

            List<Long> listWordsId = new ArrayList<>();
            for(WordModel wordModel: adapter.selectedWords)
            {
                Long id = wordModel.getWordId();
                listWordsId.add(id);
            }
            addOrUpdateFavorWords(chosen.getFolderId(), listWordsId);
            return true;
        });
        popupMenu.show();
    }


    private void getFoldersFromApi() {
        userService.getFolders(1).enqueue(new Callback<List<FolderModel>>() {
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

    private void addOrUpdateFavorWords(int folderId, List<Long> listWords){
        int userId = 1;
        userService.addOrUpdateFavorWords(userId, folderId,listWords).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                {
                    try {
                        String message = response.body().string();
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        layoutWordActions.setVisibility(View.GONE);
                        // Xóa từ và cập nhật danh sách
                        adapter.selectedWords.clear();
                        adapter.notifyDataSetChanged();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Lỗi đọc dữ liệu!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(requireContext(), "Thêm vào thư mục thất bại!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(requireContext(), "Kết nối thất bại, thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSearchWords(List<Long> listSearchWords){
        int userId = 1;
        userService.deleteSearchWords(userId, listSearchWords).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    String message = response.body().string();
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();      // Xóa từ và cập nhật danh sách
                }
                catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Lỗi đọc dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(requireContext(), "Kết nối thất bại, thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
