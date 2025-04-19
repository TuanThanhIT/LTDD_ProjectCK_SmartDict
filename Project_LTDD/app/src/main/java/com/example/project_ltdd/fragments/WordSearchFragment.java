package com.example.project_ltdd.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_ltdd.R;
import com.example.project_ltdd.adapters.WordSearchAdapter;
import com.example.project_ltdd.models.DefinitionModel;
import com.example.project_ltdd.models.MeaningModel;
import com.example.project_ltdd.models.PhoneticModel;
import com.example.project_ltdd.models.WordModel;
import com.example.project_ltdd.utils.DrawingView;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordSearchFragment extends Fragment {

    private ActivityResultLauncher<Intent> voiceLauncher;
    private EditText edtSearch;
    private ImageView btnClear;
    private RecyclerView rvSuggestions;
    private WordSearchAdapter adapter;
    private ImageView btnVoice;

    private ImageView btnWrite;

    private ImageView imgGif;

    private String currentSearchQuery = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_search, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        edtSearch = view.findViewById(R.id.edtSearch);
        btnClear = view.findViewById(R.id.btnClear);
        rvSuggestions = view.findViewById(R.id.rvSuggestions);
        btnVoice = view.findViewById(R.id.btnVoice);
        btnWrite = view.findViewById(R.id.btnWrite);
        imgGif = view.findViewById(R.id.imgGif);
        // Chạy Gif
        Glide.with(this)
                .asGif()  // Chỉ định tải GIF
                .load(R.drawable.gi_search)  // Đường dẫn đến GIF trong drawable
                .into(imgGif);  // ImageView mà bạn muốn hiển thị GIF

        List<WordModel> fakeData = Arrays.asList(
                new WordModel(
                        1L,
                        Arrays.asList(
                                new MeaningModel(101L, "chương trình truyền hình", "Noun") {{
                                    setDefinitions(Arrays.asList(
                                            new DefinitionModel(1001L, "The telecast of the football match was delayed.", "A broadcast of a television program."),
                                            new DefinitionModel(1002L, "Millions watched the telecast last night.", "An instance of broadcasting something by television.")
                                    ));
                                }},
                                new MeaningModel(102L, "việc phát sóng trên TV", "Verb") {{
                                    setDefinitions(Arrays.asList(
                                            new DefinitionModel(1003L, "They will telecast the event live.", "To broadcast by television.")
                                    ));
                                }}
                        ),
                        Arrays.asList(
                                new PhoneticModel("https://audio-url.com/telecast.mp3", 201L, "/ˈtelikæst/"),
                                new PhoneticModel("https://audio-url.com/telecast2.mp3", 202L, "/ˌtelɪˈkæst/")
                        ),
                        "telecast"
                ),
                new WordModel(
                        2L,
                        Arrays.asList(
                                new MeaningModel(103L, "máy truyền hình", "Noun") {{
                                    setDefinitions(Arrays.asList(
                                            new DefinitionModel(1004L, "He bought a new television.", "An electronic device for receiving television broadcasts.")
                                    ));
                                }},
                                new MeaningModel(104L, "ngành truyền hình", "Noun") {{
                                    setDefinitions(Arrays.asList(
                                            new DefinitionModel(1005L, "She works in television.", "The industry of broadcasting visual content.")
                                    ));
                                }}
                        ),
                        Arrays.asList(
                                new PhoneticModel("https://audio-url.com/television.mp3", 203L, "/ˈteləˌvɪʒən/")
                        ),
                        "television"
                ),
                new WordModel(
                        3L,
                        Arrays.asList(
                                new MeaningModel(105L, "dịch chuyển tức thời", "Verb") {{
                                    setDefinitions(Arrays.asList(
                                            new DefinitionModel(1006L, "The magician teleported across the room.", "To transport instantly from one place to another.")
                                    ));
                                }}
                        ),
                        Arrays.asList(
                                new PhoneticModel("https://audio-url.com/teleport.mp3", 204L, "/ˈtelɪpɔːt/")
                        ),
                        "teleport"
                ),
                new WordModel(
                        4L,
                        Arrays.asList(
                                new MeaningModel(106L, "viễn thông", "Noun") {{
                                    setDefinitions(Arrays.asList(
                                            new DefinitionModel(1007L, "Telecom companies are expanding rapidly.", "Short for telecommunications.")
                                    ));
                                }}
                        ),
                        Arrays.asList(
                                new PhoneticModel("https://audio-url.com/telecom.mp3", 205L, "/ˈtelɪkɒm/")
                        ),
                        "telecom"
                ),
                new WordModel(
                        5L,
                        Arrays.asList(
                                new MeaningModel(107L, "máy điện báo", "Noun") {{
                                    setDefinitions(Arrays.asList(
                                            new DefinitionModel(1008L, "The telegraph was used in the 19th century.", "A system for transmitting messages over long distances.")
                                    ));
                                }},
                                new MeaningModel(108L, "gửi điện tín", "Verb") {{
                                    setDefinitions(Arrays.asList(
                                            new DefinitionModel(1009L, "They telegraphed the news quickly.", "To send a message via telegraph.")
                                    ));
                                }}
                        ),
                        Arrays.asList(
                                new PhoneticModel("https://audio-url.com/telegraph.mp3", 206L, "/ˈtelɪɡræf/")
                        ),
                        "telegraph"
                ),
                new WordModel(
                        6L,
                        Arrays.asList(
                                new MeaningModel(109L, "điện thoại", "Noun") {{
                                    setDefinitions(Arrays.asList(
                                            new DefinitionModel(1010L, "She picked up the telephone.", "A device used for voice communication.")
                                    ));
                                }},
                                new MeaningModel(110L, "gọi điện", "Verb") {{
                                    setDefinitions(Arrays.asList(
                                            new DefinitionModel(1011L, "I telephoned her last night.", "To call someone using a telephone.")
                                    ));
                                }}
                        ),
                        Arrays.asList(
                                new PhoneticModel("https://audio-url.com/telephone.mp3", 207L, "/ˈtelɪfəʊn/"),
                                new PhoneticModel("https://audio-url.com/telephone2.mp3", 208L, "/ˈtɛlɪfəʊn/")
                        ),
                        "telephone"
                ),
                new WordModel(
                        7L,
                        Arrays.asList(
                                new MeaningModel(111L, "thần giao cách cảm", "Noun") {{
                                    setDefinitions(Arrays.asList(
                                            new DefinitionModel(1012L, "They claim to have telepathy.", "Communication of thoughts or ideas by means other than the known senses.")
                                    ));
                                }}
                        ),
                        Arrays.asList(
                                new PhoneticModel("https://audio-url.com/telepathy.mp3", 209L, "/təˈlepəθi/")
                        ),
                        "telepathy"
                ),
                new WordModel(
                        8L,
                        Arrays.asList(
                                new MeaningModel(112L, "ống kính tele", "Noun") {{
                                    setDefinitions(Arrays.asList(
                                            new DefinitionModel(1013L, "He used a telephoto lens for the shot.", "A lens with a longer focal length than standard.")
                                    ));
                                }}
                        ),
                        Arrays.asList(
                                new PhoneticModel("https://audio-url.com/telephoto.mp3", 210L, "/ˌtelɪˈfəʊtəʊ/")
                        ),
                        "telephoto"
                ),
                new WordModel(
                        9L,
                        Arrays.asList(
                                new MeaningModel(113L, "nhân viên tiếp thị qua điện thoại", "Noun") {{
                                    setDefinitions(Arrays.asList(
                                            new DefinitionModel(1014L, "The telemarketer kept calling.", "A person who sells products or services via telephone.")
                                    ));
                                }}
                        ),
                        Arrays.asList(
                                new PhoneticModel("https://audio-url.com/telemarketer.mp3", 211L, "/ˈtelimɑːkɪtə(r)/")
                        ),
                        "telemarketer"
                ),
                new WordModel(
                        10L,
                        Arrays.asList(
                                new MeaningModel(114L, "người điều khiển từ xa", "Noun") {{
                                    setDefinitions(Arrays.asList(
                                            new DefinitionModel(1015L, "The teledrive operator handled the drone.", "A person who operates a vehicle or system remotely.")
                                    ));
                                }},
                                new MeaningModel(115L, "lái từ xa", "Verb") {{
                                    setDefinitions(Arrays.asList(
                                            new DefinitionModel(1016L, "He teledrove the car through the app.", "To control or drive something remotely.")
                                    ));
                                }}
                        ),
                        Arrays.asList(
                                new PhoneticModel("https://audio-url.com/teledrive.mp3", 212L, "/ˈtɛlɪdraɪv/")
                        ),
                        "teledrive"
                ),
                new WordModel(
                        11L,
                        Arrays.asList(
                                new MeaningModel(1L, "con mèo", "Noun") {{
                                    setDefinitions(Arrays.asList(
                                            new DefinitionModel(1L, "The cat sat on the mat.", "A small domesticated carnivorous mammal."),
                                            new DefinitionModel(2L, "Cats are popular pets around the world.", "A domesticated animal kept as a pet.")
                                    ));
                                }},
                                new MeaningModel(2L, "người khó chịu", "Noun") {{
                                    setDefinitions(Arrays.asList(
                                            new DefinitionModel(3L, "He can be such a cat when things don't go his way.", "A spiteful or unpleasant person (slang).")
                                    ));
                                }}
                        ),
                        Arrays.asList(
                                new PhoneticModel("https://api.dictionaryapi.dev/media/pronunciations/en/cat-uk.mp3", 1L, "/kæt/"),
                                new PhoneticModel("https://api.dictionaryapi.dev/media/pronunciations/en/cat-us.mp3", 2L, "/kat/")
                        ),
                        "cat"
                )
        );


        btnVoice.setOnClickListener(v -> startVoiceInput());

        adapter = new WordSearchAdapter(fakeData, getParentFragmentManager());
        rvSuggestions.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvSuggestions.setAdapter(adapter);
        rvSuggestions.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.fade_in_layout)
        );

        // Bắt sự kiện nhập vào ô tìm kiếm
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rvSuggestions.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                currentSearchQuery = s.toString();
                filterResults();
                btnClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Nút clear
        btnClear.setOnClickListener(v -> {
            edtSearch.setText("");
            currentSearchQuery = "";
            filterResults();
        });

        // Đăng ký launcher để nhận kết quả từ giọng nói
        voiceLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ArrayList<String> matches = result.getData()
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && !matches.isEmpty()) {
                            String spokenText = matches.get(0);
                            edtSearch.setText(spokenText);
                            currentSearchQuery = spokenText;
                            filterResults();
                        }
                    }
                }
        );

        btnWrite.setOnClickListener(v -> openDrawPopup());
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

    // Phương thức để lọc kết quả tìm kiếm dựa trên từ người dùng nhập hoặc nói
    private void filterResults() {
        adapter.getFilter().filter(currentSearchQuery, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                rvSuggestions.scheduleLayoutAnimation(); // Kích hoạt animation
            }
        });
    }

    // Tìm kiếm bằng chu viết
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
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Lỗi nhận dạng", Toast.LENGTH_SHORT).show();
                });
    }
}
