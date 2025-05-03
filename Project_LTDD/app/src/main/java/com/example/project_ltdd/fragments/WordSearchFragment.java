package com.example.project_ltdd.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_ltdd.R;
import com.example.project_ltdd.adapters.WordSearchAdapter;
import com.example.project_ltdd.api.retrofit_client.WordRetrofitClient;
import com.example.project_ltdd.api.services.WordService;
import com.example.project_ltdd.models.WordModel;
import com.example.project_ltdd.utils.DrawingView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordSearchFragment extends Fragment {

    private ActivityResultLauncher<Intent> voiceLauncher;
    private EditText edtSearch;
    private ImageView btnClear, btnVoice, btnWrite, imgGif;
    private RecyclerView rvSuggestions;
    private WordSearchAdapter adapter;
    private List<WordModel> words = new ArrayList<>();
    private String currentSearchQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_search, container, false);
        initViews(view);
        fetchWordsFromApi();
        return view;
    }

    private void initViews(View view) {
        edtSearch = view.findViewById(R.id.edtSearch);
        btnClear = view.findViewById(R.id.btnClear);
        btnVoice = view.findViewById(R.id.btnVoice);
        btnWrite = view.findViewById(R.id.btnWrite);
        imgGif = view.findViewById(R.id.imgGif);
        rvSuggestions = view.findViewById(R.id.rvSuggestions);

        Glide.with(this)
                .asGif()
                .load(R.drawable.gi_search)
                .into(imgGif);

        btnVoice.setOnClickListener(v -> startVoiceInput());
        btnClear.setOnClickListener(v -> {
            edtSearch.setText("");
            currentSearchQuery = "";
            filterResults();
        });
        btnWrite.setOnClickListener(v -> openDrawPopup());

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                rvSuggestions.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                currentSearchQuery = s.toString();
                filterResults();
                btnClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        voiceLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ArrayList<String> matches = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && !matches.isEmpty()) {
                            String spokenText = matches.get(0);
                            edtSearch.setText(spokenText);
                            currentSearchQuery = spokenText;
                            filterResults();
                        }
                    }
                }
        );
    }

    private void setupAdapter() {
        adapter = new WordSearchAdapter(words, getParentFragmentManager(), requireContext());
        rvSuggestions.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvSuggestions.setAdapter(adapter);
        rvSuggestions.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.fade_in_layout));
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak a word...");

        try {
            voiceLauncher.launch(intent);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Thiết bị không hỗ trợ Voice Search", Toast.LENGTH_SHORT).show();
        }
    }

    private void filterResults() {
        if (adapter != null) {
            adapter.getFilter().filter(currentSearchQuery, count -> rvSuggestions.scheduleLayoutAnimation());
        }
    }

    private void openDrawPopup() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View view = inflater.inflate(R.layout.popup_draw, null);

        DrawingView drawingView = view.findViewById(R.id.drawingView);
        Button btnClear = view.findViewById(R.id.btnClear);
        Button btnUndo = view.findViewById(R.id.btnUndo);
        Button btnDone = view.findViewById(R.id.btnDone);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setCancelable(true)
                .create();

        btnClear.setOnClickListener(v -> drawingView.clearCanvas());
        btnUndo.setOnClickListener(v -> drawingView.undo());
        btnDone.setOnClickListener(v -> {
            Bitmap bitmap = drawingView.getBitmap();
            recognizeTextFromBitmap(bitmap);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void recognizeTextFromBitmap(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(result -> {
                    String resultText = result.getText();
                    edtSearch.setText(resultText);
                    currentSearchQuery = resultText;
                    filterResults();
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Lỗi nhận dạng", Toast.LENGTH_SHORT).show());
    }

    private void fetchWordsFromApi() {
        WordService api = WordRetrofitClient.getClient();

        api.getAllWords().enqueue(new Callback<List<WordModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<WordModel>> call, @NonNull Response<List<WordModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    words = response.body();
                    setupAdapter(); // Gọi adapter sau khi có dữ liệu
                } else {
                    Toast.makeText(requireContext(), "Lỗi: "+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<WordModel>> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

