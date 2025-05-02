package com.example.project_ltdd.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
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
import com.example.project_ltdd.commons.WordCommon;
import com.example.project_ltdd.fragments.WordDetailFragment;
import com.example.project_ltdd.models.MeaningModel;
import com.example.project_ltdd.models.PhoneticModel;
import com.example.project_ltdd.models.WordModel;

import java.util.ArrayList;
import java.util.List;

public class WordFavoriteAdapter extends RecyclerView.Adapter<WordFavoriteAdapter.WordViewHolder> {

    private final List<WordModel> wordList;
    private Context context;

    public List<WordModel> selectedWords = new ArrayList<>(); // Xu ly viec chon CheckBox

    WordCommon wordCommon = new WordCommon();

    private FragmentManager fragmentManager;

    public WordFavoriteAdapter(List<WordModel> wordList, Context context, FragmentManager fragmentManager) {
        this.wordList = wordList;
        this.context = context;
        this.fragmentManager = fragmentManager;
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
                            // TODO: Xử lý xóa từ
                            return true;
                        } else if (id == R.id.action_move_top) {
                            // TODO: Chuyển lên đầu
                            return true;
                        } else if (id == R.id.action_move_folder) {
                            // TODO: Chuyển đến thư mục khác
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


}
