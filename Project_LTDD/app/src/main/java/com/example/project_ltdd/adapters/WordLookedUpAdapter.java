package com.example.project_ltdd.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
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

public class WordLookedUpAdapter extends RecyclerView.Adapter<WordLookedUpAdapter.WordViewHolder> implements Filterable{

    private List<WordModel> wordList;
    private List<WordModel> wordListFiltered;
    private Context context;
    private FragmentManager fragmentManager;

    WordCommon wordCommon = new WordCommon();
    Boolean checkFavorite = false;

    public List<WordModel> selectedWords = new ArrayList<>(); // Xu ly viec chon CheckBox

    public WordLookedUpAdapter(List<WordModel> wordList, Context context, FragmentManager fragmentManager) {
        this.wordList = wordList;
        this.context = context;
        this.wordListFiltered = new ArrayList<>(wordList);
        this.fragmentManager = fragmentManager;
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

        holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkFavorite){
                    // Xu ly them vao tu cua ban
                    holder.btnFavorite.setImageResource(R.drawable.ic_heart2);
                    Toast.makeText(context," Da them tu vung nay vao Tu cua ban", Toast.LENGTH_SHORT).show();
                    checkFavorite = true;
                }
                else{
                    holder.btnFavorite.setImageResource(R.drawable.ic_heart1);
                    Toast.makeText(context,"Tu nay da bi xoa khoi Tu cua ban", Toast.LENGTH_SHORT).show();
                }


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
}
