package com.example.project_ltdd.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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

public class WordFavoriteAdapter extends RecyclerView.Adapter<WordFavoriteAdapter.WordViewHolder> {

    private final List<WordModel> wordList;
    private Context context;

    private int currentFolderId; // Thêm thuộc tính này

    public List<WordModel> selectedWords = new ArrayList<>(); // Xu ly viec chon CheckBox

    private List<FolderModel> listFolderEx = new ArrayList<>();

    private final WordCommons wordCommon = new WordCommons();

    private FragmentManager fragmentManager;

    private UserService userService = UserRetrofitClient.getClient();

    private UserPrefs userPrefs;

    public WordFavoriteAdapter(List<WordModel> wordList, Context context, FragmentManager fragmentManager, int currentFolderId) {
        this.wordList = wordList;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.currentFolderId = currentFolderId;
        this.userPrefs = new UserPrefs(context); // <-- thêm dòng này
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_word, parent, false);
        return new WordViewHolder(view);
    }

    // Xu ly cho check bõ
    public interface OnItemCheckListener {
        void onItemChecked(WordModel word);
        void onItemUnchecked(WordModel word); // Optional
    }

    private WordFavoriteAdapter.OnItemCheckListener listener;

    public void setOnItemCheckListener(WordFavoriteAdapter.OnItemCheckListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        WordModel word = wordList.get(position);
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
                wordCommon.playAudio(selectedAudio,view);
            }
        });

        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.btnMenu);
                popupMenu.getMenuInflater().inflate(R.menu.favorite_word_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.action_remove) {
                            deleteWordFavor(word);
                            return true;
                        } else if (id == R.id.action_move_top) {
                            // TODO: Chuyển lên đầu
                            moveWordToTop(word);
                            return true;
                        } else if (id == R.id.action_move_folder) {
                            // TODO: Chuyển đến thư mục khác
                            getFoldersExcept(word);
                            return true;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });

        holder.chbSelect.setOnCheckedChangeListener(null); // Ngắt listener cũ nếu có
        // Cập nhật trạng thái CheckBox
        holder.chbSelect.setChecked(selectedWords.contains(word));

        holder.chbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Thêm từ vào danh sách selectedWords khi được chọn
                if (!selectedWords.contains(word)) {
                    selectedWords.add(word);
                }
                if (listener != null) listener.onItemChecked(word); // Gửi callback
            } else {
                // Loại bỏ từ khỏi danh sách selectedWords khi bị bỏ chọn
                selectedWords.remove(word);
                if (listener != null) listener.onItemUnchecked(word); // Gửi callback
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
        return wordList.size();
    }

    public WordModel getWordAt(int position) {
        return wordList.get(position);
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView txvWord, txvPhonetic, txvMeaning, txvPartOfSpeech;
        ImageView btnPlayAudio, btnMenu;
        CheckBox chbSelect;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            txvWord = itemView.findViewById(R.id.txvWord);
            txvPhonetic = itemView.findViewById(R.id.txvPhonetic);
            txvMeaning = itemView.findViewById(R.id.txvMeaning);
            txvPartOfSpeech = itemView.findViewById(R.id.txvPartOfSpeech);
            btnPlayAudio = itemView.findViewById(R.id.btnSound);
            btnMenu = itemView.findViewById(R.id.btnMenu);
            chbSelect = itemView.findViewById(R.id.cbFavorite);
        }
    }
    private void moveWordToTop(WordModel word) {
        int currentIndex = wordList.indexOf(word);
        if (currentIndex > 0) {
            wordList.remove(currentIndex);
            wordList.add(0, word);
            notifyItemMoved(currentIndex, 0);
            Toast.makeText(context, "Đã chuyển từ lên đầu", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Từ đã nằm ở đầu danh sách", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteWordFavor(WordModel wordFavor) {
        int userId = userPrefs.getUserId();
        userService.deleteWordFavor(userId, wordFavor.getWordId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    int position = wordList.indexOf(wordFavor);
                    if (position != -1) {
                        wordList.remove(position);
                        selectedWords.remove(wordFavor); // Nếu từ đó đang được chọn thì cũng xóa khỏi danh sách selectedWords
                        notifyItemRemoved(position);
                    }
                    Toast.makeText(context, "Từ này đã bị xóa khỏi thư mục", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Xóa thất bại, thử lại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Kết nối thất bại, thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showMoveFolderDialog(WordModel wordEx) {
        // Dùng danh sách thực từ server
        String[] folderNames = new String[listFolderEx.size()];
        for (int i = 0; i < listFolderEx.size(); i++) {
            folderNames[i] = listFolderEx.get(i).getFolderName(); // Tùy getter bạn đặt tên
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn thư mục")
                .setItems(folderNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FolderModel selectedFolder = listFolderEx.get(which);
                        updateFolderWord(wordEx, selectedFolder);
                    }
                })
                .show();
    }


    private void getFoldersExcept(WordModel wordEx) {
        int userId = userPrefs.getUserId(); // Cố định hoặc lấy user đang login

        userService.getFoldersExcept(userId, wordEx.getWordId()).enqueue(new Callback<List<FolderModel>>() {
            @Override
            public void onResponse(Call<List<FolderModel>> call, Response<List<FolderModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listFolderEx = response.body();

                    if (listFolderEx.isEmpty()) {
                        Toast.makeText(context, "Không còn thư mục để chuyển.", Toast.LENGTH_SHORT).show();
                    } else {
                        // ✅ Show dialog sau khi có dữ liệu
                        showMoveFolderDialog(wordEx);
                    }
                } else {
                    Toast.makeText(context, "Lấy danh sách thư mục thất bại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FolderModel>> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateFolderWord(WordModel wordEx, FolderModel folderSelect) {
        int userId = userPrefs.getUserId();

        userService.updateFolderWord(userId, wordEx.getWordId(), folderSelect.getFolderId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String message = response.body().string();
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                // Xóa từ khỏi danh sách hiện tại và cập nhật UI
                                int position = wordList.indexOf(wordEx);
                                if (position != -1) {
                                    wordList.remove(position);
                                    selectedWords.remove(wordEx); // Xóa khỏi danh sách chọn nếu có
                                    notifyDataSetChanged();  // Cập nhật RecyclerView hiện tại
                                }

                                Bundle result = new Bundle();
                                result.putBoolean("needReload", true);
                                fragmentManager.setFragmentResult("request_reload_your_words", result);


                            } catch (IOException e) {
                                Toast.makeText(context, "Lỗi đọc phản hồi", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Thay đổi thư mục thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
