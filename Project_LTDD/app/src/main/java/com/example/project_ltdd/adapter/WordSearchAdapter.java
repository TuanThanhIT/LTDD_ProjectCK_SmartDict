package com.example.project_ltdd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.models.MeaningModel;
import com.example.project_ltdd.models.PhoneticModel;
import com.example.project_ltdd.models.WordModel;

import java.util.ArrayList;
import java.util.List;

public class WordSearchAdapter extends RecyclerView.Adapter<WordSearchAdapter.VocabViewHolder> implements Filterable {

    private List<WordModel> vocabList;
    private List<WordModel> vocabListFiltered = new ArrayList<>();

    public WordSearchAdapter(List<WordModel> vocabList) {
        this.vocabList = vocabList;
    }

    @NonNull
    @Override
    public VocabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vocabulary, parent, false);
        return new VocabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VocabViewHolder holder, int position) {
        WordModel vocab = vocabListFiltered.get(position);
        holder.txvWord.setText(vocab.getWord());

        StringBuilder phoneticText = new StringBuilder("[");
        if (vocab.getPhonetics() != null && !vocab.getPhonetics().isEmpty()) {
            for (int i = 0; i < vocab.getPhonetics().size(); i++) {
                PhoneticModel p = vocab.getPhonetics().get(i);
                phoneticText.append(p.getText());
                if (i < vocab.getPhonetics().size() - 1) {
                    phoneticText.append(", ");
                }
            }
        }
        phoneticText.append("]");
        holder.txvPhonetic.setText(phoneticText.toString());

        StringBuilder partOfSpeechText = new StringBuilder("[");
        StringBuilder vietNameseText = new StringBuilder();
        if (vocab.getMeanings() != null && !vocab.getMeanings().isEmpty()) {
            for (int i = 0; i < vocab.getMeanings().size(); i++) {
                MeaningModel m = vocab.getMeanings().get(i);
                partOfSpeechText.append(m.getPartOfSpeech());
                vietNameseText.append(m.getVietnameseMeaning());
                if (i < vocab.getMeanings().size() - 1) {
                    partOfSpeechText.append(", ");
                    vietNameseText.append(", ");
                }
            }
        }
        partOfSpeechText.append("]");
        holder.txvPartOfSpeech.setText(partOfSpeechText.toString());
        holder.txvMeaning.setText(vietNameseText.toString());
    }

    @Override
    public int getItemCount() {
        return vocabListFiltered.size();
    }

    public class VocabViewHolder extends RecyclerView.ViewHolder {
        TextView txvWord, txvPhonetic, txvMeaning, txvPartOfSpeech;

        public VocabViewHolder(@NonNull View itemView) {
            super(itemView);
            txvWord = itemView.findViewById(R.id.txvWord);
            txvPhonetic = itemView.findViewById(R.id.txvPhonetic);
            txvMeaning = itemView.findViewById(R.id.txvMeaning);
            txvPartOfSpeech = itemView.findViewById(R.id.txvPartOfSpeech);
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
                    filtered.addAll(vocabList); // Nếu không có từ khóa, hiển thị tất cả
                } else {
                    for (WordModel v : vocabList) {
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
                vocabListFiltered.clear(); // Xóa các kết quả trước đó
                vocabListFiltered.addAll((List<WordModel>) results.values); // Thêm kết quả mới
                notifyDataSetChanged();
            }
        };
    }
}
