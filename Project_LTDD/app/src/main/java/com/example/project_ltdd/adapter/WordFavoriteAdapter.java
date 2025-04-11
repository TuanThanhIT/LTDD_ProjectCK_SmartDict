package com.example.project_ltdd.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.models.MeaningModel;
import com.example.project_ltdd.models.PhoneticModel;
import com.example.project_ltdd.models.WordModel;

import java.util.List;

public class WordFavoriteAdapter extends RecyclerView.Adapter<WordFavoriteAdapter.WordViewHolder> {

    private final List<WordModel> wordList;
    private Context context;

    public WordFavoriteAdapter(List<WordModel> wordList, Context context) {
        this.wordList = wordList;
        this.context = context;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        WordModel word = wordList.get(position);
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
    }

        @Override
    public int getItemCount() {
        return wordList.size();
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView txvWord, txvPhonetic, txvMeaning, txvPartOfSpeech;
        ImageView btnPlayAudio, btnMenu;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            txvWord = itemView.findViewById(R.id.txvWord);
            txvPhonetic = itemView.findViewById(R.id.txvPhonetic);
            txvMeaning = itemView.findViewById(R.id.txvMeaning);
            txvPartOfSpeech = itemView.findViewById(R.id.txvPartOfSpeech);
            btnPlayAudio = itemView.findViewById(R.id.btnSound);
            btnMenu = itemView.findViewById(R.id.btnMenu);
        }
    }


}
