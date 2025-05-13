package com.example.project_ltdd.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.api.retrofit_client.UserRetrofitClient;
import com.example.project_ltdd.api.services.UserService;
import com.example.project_ltdd.commons.WordCommons;
import com.example.project_ltdd.fragments.WordDetailFragment;
import com.example.project_ltdd.models.FavoriteWordModel;
import com.example.project_ltdd.models.FolderModel;
import com.example.project_ltdd.models.WordModel;
import com.example.project_ltdd.utils.UserPrefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordLookedUpAdapter extends RecyclerView.Adapter<WordLookedUpAdapter.WordViewHolder> implements Filterable{

    private List<WordModel> wordList;
    private List<WordModel> wordListFiltered;
    private Context context;
    private FragmentManager fragmentManager;

    private WordCommons wordCommon = new WordCommons();

    public List<WordModel> selectedWords = new ArrayList<>(); // Xu ly viec chon CheckBox

    private UserService userService = UserRetrofitClient.getClient();

    private List<FolderModel> menuItems = new ArrayList<>();

    private Map<Long, Boolean> favoriteStatus = new HashMap<>();
    private UserPrefs userPrefs;

    public WordLookedUpAdapter(List<WordModel> wordList, Context context, FragmentManager fragmentManager) {
        this.wordList = wordList;
        this.context = context;
        this.wordListFiltered = new ArrayList<>(wordList);
        this.fragmentManager = fragmentManager;
        this.userPrefs = new UserPrefs(context);
        getFoldersFromApi();
    }


    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lookedup_word, parent, false);
        return new WordViewHolder(view);
    }

    // Xu ly cho check bõ
    public interface OnItemCheckListener {
        void onItemChecked(WordModel word);
        void onItemUnchecked(WordModel word); // Optional
    }

    private OnItemCheckListener listener;

    public void setOnItemCheckListener(OnItemCheckListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        WordModel word = wordListFiltered.get(position);

        handleFavorite(word, holder.btnFavorite);

        holder.txvWord.setText(word.getWord());

        StringBuilder phoneticText = wordCommon.setPhoneticText(word.getPhonetics());
        holder.txvPhonetic.setText(phoneticText.toString());

        StringBuilder partOfSpeechText = wordCommon.setMeaningPartOfSpeech(word.getMeanings());
        StringBuilder vietNameseText = wordCommon.setMeaningVietNamese(word.getMeanings());
        holder.txvPartOfSpeech.setText(partOfSpeechText.toString());
        holder.txvMeaning.setText(vietNameseText.toString());

        holder.btnPlayAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedAudio = wordCommon.getAudioLookedUp(word.getPhonetics());
                wordCommon.playAudio(selectedAudio, view);
            }
        });

        holder.btnFavorite.setOnClickListener(view -> {
            Boolean isFavorite = favoriteStatus.get(word.getWordId());
            if (isFavorite == null) isFavorite = false; // Nếu chưa có thì mặc định chưa yêu thích

            if (!isFavorite) {
                PopupMenu popupMenu = new PopupMenu(context, holder.btnFavorite);
                for (int i = 0; i < menuItems.size(); i++) {
                    popupMenu.getMenu().add(Menu.NONE, i, i, ">Thư mục [" + menuItems.get(i).getFolderName() + "]");
                }
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    int idx = menuItem.getItemId();
                    FolderModel chosen = menuItems.get(idx);
                    addFavoriteWord(chosen.getFolderId(), word);
                    return true;
                });
                popupMenu.show();
            } else {
                deleteWordFavor(word);
            }
        });

        holder.chbSelect.setOnCheckedChangeListener(null);
        holder.chbSelect.setChecked(selectedWords.contains(word));

        holder.chbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedWords.contains(word)) {
                    selectedWords.add(word);
                }
                if (listener != null) listener.onItemChecked(word);
            } else {
                selectedWords.remove(word);
                if (listener != null) listener.onItemUnchecked(word);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WordDetailFragment detailFragment = new WordDetailFragment();

                Bundle result = new Bundle();
                result.putSerializable("wordDetail", word);

                fragmentManager.setFragmentResult("request_word", result);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, detailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordListFiltered.size();
    }

    public WordModel getWordAt(int position) {
        return wordListFiltered.get(position);
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView txvWord, txvPhonetic, txvMeaning, txvPartOfSpeech;
        ImageView btnPlayAudio, btnFavorite;

        CheckBox chbSelect;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            txvWord = itemView.findViewById(R.id.txvWord);
            txvPhonetic = itemView.findViewById(R.id.txvPhonetic);
            txvMeaning = itemView.findViewById(R.id.txvMeaning);
            txvPartOfSpeech = itemView.findViewById(R.id.txvPartOfSpeech);
            btnPlayAudio = itemView.findViewById(R.id.btnSound);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            chbSelect = itemView.findViewById(R.id.chbLookedUp);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<WordModel> filtered = new ArrayList<>();
                String keyword = constraint.toString().toLowerCase().trim();

                // Kiểm tra xem từ khóa có hợp lệ không
                if (keyword.isEmpty()) {
                    filtered.addAll(wordList); // Nếu không có từ khóa, hiển thị tất cả
                } else {
                    for (WordModel v : wordList) {
                        // Kiểm tra từ khóa xuất hiện ở bất kỳ vị trí nào trong từ (dùng contains thay vì startsWith)
                        if (v.getWord().toLowerCase().startsWith(keyword)) {
                            filtered.add(v);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                wordListFiltered.clear(); // Xóa các kết quả trước đó
                wordListFiltered.addAll((List<WordModel>) results.values); // Thêm kết quả mới
                notifyDataSetChanged();
            }
        };
    }

    private void getFoldersFromApi() {
        int userId = userPrefs.getUserId();
        userService.getFolders(userId).enqueue(new Callback<List<FolderModel>>() {
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


    private void addFavoriteWord(int folderId, WordModel word) {
        int userId = userPrefs.getUserId();
        userService.addFavoriteWord(userId, folderId, word.getWordId()).enqueue(new Callback<FavoriteWordModel>() {
            @Override
            public void onResponse(Call<FavoriteWordModel> call, Response<FavoriteWordModel> response) {
                favoriteStatus.put(word.getWordId(), true);
                notifyDataSetChanged();
                Toast.makeText(context, "Từ vựng được thêm vào thư mục thành công!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<FavoriteWordModel> call, Throwable t) { }
        });
    }

    private void deleteWordFavor(WordModel word) {
        int userId = userPrefs.getUserId();
        userService.deleteWordFavor(userId, word.getWordId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                favoriteStatus.put(word.getWordId(), false);
                notifyDataSetChanged();
                Toast.makeText(context, "Từ này đã bị xóa khỏi thư mục!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) { }
        });
    }


    private void handleFavorite(WordModel word, ImageView btnFavorite) {
        int userId = userPrefs.getUserId();
        userService.checkFavorite(userId, word.getWordId()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isFavorite = response.body();
                    favoriteStatus.put(word.getWordId(), isFavorite);
                    btnFavorite.setImageResource(isFavorite ? R.drawable.ic_heart2 : R.drawable.ic_heart1);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(context, "Lỗi kiểm tra yêu thích", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
