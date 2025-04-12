package com.example.project_ltdd.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.models.MeaningModel;
import com.example.project_ltdd.models.PhoneticModel;
import com.example.project_ltdd.models.WordModel;

import java.util.ArrayList;
import java.util.List;

public class WordLookedUpAdapter extends RecyclerView.Adapter<WordLookedUpAdapter.WordViewHolder> implements Filterable{

    private List<WordModel> wordList;
    private List<WordModel> wordListFiltered;
    private Context context;

    Boolean checkFavorite = false;

    public List<WordModel> selectedWords = new ArrayList<>(); // Xu ly viec chon CheckBox

    public WordLookedUpAdapter(List<WordModel> wordList, Context context) {
        this.wordList = wordList;
        this.context = context;
        this.wordListFiltered = new ArrayList<>(wordList);
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
        StringBuilder phoneticText = new StringBuilder("[");
        if (word.getPhonetics() != null && !word.getPhonetics().isEmpty()) {
            for (int i = 0; i < word.getPhonetics().size(); i++) {
                PhoneticModel p = word.getPhonetics().get(i);
                phoneticText.append(p.getText());
                if (i < word.getPhonetics().size() - 1) {
                    phoneticText.append(", ");
                }
            }
        }
        phoneticText.append("]");
        holder.txvPhonetic.setText(phoneticText.toString());

        StringBuilder partOfSpeechText = new StringBuilder("[");
        StringBuilder vietNameseText = new StringBuilder();
        if (word.getMeanings() != null && !word.getMeanings().isEmpty()) {
            for (int i = 0; i < word.getMeanings().size(); i++) {
                MeaningModel m = word.getMeanings().get(i);
                partOfSpeechText.append(m.getPartOfSpeech());
                vietNameseText.append(m.getVietnameseMeaning());
                if (i < word.getMeanings().size() - 1) {
                    partOfSpeechText.append(", ");
                    vietNameseText.append(", ");
                }
            }
        }
        partOfSpeechText.append("]");
        holder.txvPartOfSpeech.setText(partOfSpeechText.toString());
        holder.txvMeaning.setText(vietNameseText.toString());

        holder.btnPlayAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<PhoneticModel> phonetics = word.getPhonetics();
                if (phonetics != null && !phonetics.isEmpty()) {
                    String ukAudio = null;
                    String usAudio = null;

                    for (PhoneticModel p : phonetics) {
                        String audio = p.getAudio();
                        if (audio != null && !audio.isEmpty()) {
                            if (audio.contains("-uk.mp3")) {
                                ukAudio = audio;
                                break; // Ưu tiên UK, có là dừng luôn
                            } else if (audio.contains("-us.mp3")) {
                                usAudio = audio;
                            }
                        }
                    }

                    String selectedAudio = ukAudio != null ? ukAudio : usAudio;

                    view.animate()
                            .scaleX(1.2f)
                            .scaleY(1.2f)
                            .setDuration(100)
                            .withEndAction(()->{
                                view.animate()
                                        .scaleX(1.0f)
                                        .scaleY(1.0f)
                                        .setDuration(100)
                                        .start();
                            })
                            .start();

                    if (selectedAudio != null) {
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(selectedAudio);
                            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
                            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
                            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                                mp.release();
                                Toast.makeText(view.getContext(), "Lỗi phát âm thanh", Toast.LENGTH_SHORT).show();
                                return true;
                            });
                            mediaPlayer.prepareAsync();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(view.getContext(), "Không thể phát âm thanh", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(view.getContext(), "Không có audio UK hoặc US", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "Không có dữ liệu phát âm", Toast.LENGTH_SHORT).show();
                }
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
