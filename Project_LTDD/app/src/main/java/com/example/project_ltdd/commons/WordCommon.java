package com.example.project_ltdd.commons;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.project_ltdd.models.DefinitionModel;
import com.example.project_ltdd.models.MeaningModel;
import com.example.project_ltdd.models.PhoneticModel;
import com.example.project_ltdd.models.WordModel;

import java.util.ArrayList;
import java.util.List;

public class WordCommon {

    public WordCommon() {
    }

    public StringBuilder setPhoneticText(List<PhoneticModel> phonetics) {
        StringBuilder phoneticText = new StringBuilder("[");
        if (phonetics != null && !phonetics.isEmpty()) {
            for (int i = 0; i < phonetics.size(); i++) {
                phoneticText.append(phonetics.get(i).getText());
                if (i < phonetics.size() - 1) {
                    phoneticText.append(", ");
                }
            }
        }
        phoneticText.append("]");
        return phoneticText;
    }

    public StringBuilder setMeaningPartOfSpeech(List<MeaningModel> meanings) {
        StringBuilder partOfSpeechText = new StringBuilder("[");

        if (meanings != null && !meanings.isEmpty()) {
            for (int i = 0; i < meanings.size(); i++) {
                partOfSpeechText.append(meanings.get(i).getPartOfSpeech());
                if (i < meanings.size() - 1) {
                    partOfSpeechText.append(", ");
                }
            }
        }
        partOfSpeechText.append("]");

        return partOfSpeechText;
    }

    public StringBuilder setMeaningVietNamese(List<MeaningModel> meanings) {
        StringBuilder vietNameseText = new StringBuilder();

        if (meanings != null && !meanings.isEmpty()) {
            for (int i = 0; i < meanings.size(); i++) {
                vietNameseText.append(meanings.get(i).getVietnameseMeaning());
                vietNameseText.append(", ");
            }
        }
        return vietNameseText;
    }

    public List<DefinitionModel> addDefinitions(List<MeaningModel> meanings) {
        List<DefinitionModel> definitions = new ArrayList<>();
        if (meanings != null) {
            for (MeaningModel m : meanings) {
                if (m.getDefinitions() != null) {
                    definitions.addAll(m.getDefinitions());
                }
            }
        }
        return definitions;
    }

    public void playAudio(String url, View view) {
        if (url == null || url.isEmpty()) {
            Toast.makeText(view.getContext(), "Không có audio", Toast.LENGTH_SHORT).show();
            return;
        }

        view.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).withEndAction(() -> {
            view.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
        }).start();

        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
        } catch (Exception e) {
            Log.e("AUDIO_ERROR", "Không thể phát audio: " + e.getMessage());
            Toast.makeText(view.getContext(), "Lỗi phát âm thanh", Toast.LENGTH_SHORT).show();
        }
    }

    public String getAudio(boolean isUK, WordModel wordDetail) {
        String ukAudio = null;
        String usAudio = null;

        if (wordDetail.getPhonetics() != null) {
            for (PhoneticModel p : wordDetail.getPhonetics()) {
                String audio = p.getAudio();
                if (audio != null && !audio.isEmpty()) {
                    if (audio.contains("-uk.mp3")) {
                        ukAudio = audio;
                    } else {
                        usAudio = audio;
                    }
                }
            }
        }

        return isUK ? ukAudio : usAudio;
    }

    public String getAudioLookedUp(List<PhoneticModel> phonetics) {
        String selectedAudio = null;
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
            selectedAudio = ukAudio != null ? ukAudio : usAudio;
        }
        return selectedAudio;
    }
}

