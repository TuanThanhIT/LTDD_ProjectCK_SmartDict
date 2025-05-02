package com.example.project_ltdd.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.project_ltdd.R;
import com.example.project_ltdd.commons.WordCommon;
import com.example.project_ltdd.models.DefinitionModel;
import com.example.project_ltdd.models.MeaningModel;
import com.example.project_ltdd.models.PhoneticModel;
import com.example.project_ltdd.models.WordModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordDetailFragment extends Fragment {

    private TextView txvWord, txvPhonetic, txvVietNamese, txvPartOfSpeech, txvWordTitlle, txvFolder;
    private ImageView btnPlayAudioUK, btnPlayAudioUS;
    private LinearLayout containerWordDetail;
    private WordModel wordDetail;
    private Toolbar toolbar;

    private ImageView btnFavoriteMenu;

    WordCommon wordCommon = new WordCommon();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_word_details, container, false);
        initViews(view);

        // Nhận dữ liệu từ Fragment khác
        getParentFragmentManager().setFragmentResultListener("request_word", this, (key, bundle) -> {
            wordDetail = (WordModel) bundle.getSerializable("wordDetail");
            if (wordDetail == null) {
                Toast.makeText(getContext(), "Không có dữ liệu từ vựng", Toast.LENGTH_SHORT).show();
                return;
            }
            bindWordDetail(wordDetail);
        });

        return view;
    }

    private void initViews(View view) {
        txvWord = view.findViewById(R.id.txvWord);
        txvPhonetic = view.findViewById(R.id.txvPhonetic);
        txvVietNamese = view.findViewById(R.id.txvVietnameseMeaning);
        txvPartOfSpeech = view.findViewById(R.id.txvPartOfSpeech);
        btnPlayAudioUK = view.findViewById(R.id.btnPlayUk);
        btnPlayAudioUS = view.findViewById(R.id.btnPlayUs);
        containerWordDetail = view.findViewById(R.id.containerWordDetail);
        txvWordTitlle = view.findViewById(R.id.txvWordTitle);
        toolbar = view.findViewById(R.id.toolbar);
        txvFolder = view.findViewById(R.id.txvFolder);
        btnFavoriteMenu = view.findViewById(R.id.imgFavorite);

    }

    private void bindWordDetail(WordModel word) {
        txvWord.setText(word.getWord());
        txvWordTitlle.setText(word.getWord());

        btnPlayAudioUK.setOnClickListener(v -> {
            String selectedAudio = wordCommon.getAudio(true, wordDetail);
            wordCommon.playAudio(selectedAudio, v);
        });

        btnPlayAudioUS.setOnClickListener(v -> {
            String selectedAudio = wordCommon.getAudio(false, wordDetail);
            wordCommon.playAudio(selectedAudio, v);
        });

        // setup toolbar
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        setPhoneticText(word.getPhonetics());
        setMeaningInfo(word.getMeanings());
        addDefinitions(word.getMeanings());

        btnFavoriteMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(requireContext(), btnFavoriteMenu);

                // Danh sách tùy chọn truyền vào
                List<String> menuItems = new ArrayList<>();
                menuItems.add(">Thư mục [TỪ ĐÃ LƯU]");
                menuItems.add(">Thư mục [TỪ ĐANG HỌC]");
                menuItems.add("Xóa đánh dấu");

                // Thêm các mục vào menu
                for (int i = 0; i < menuItems.size(); i++) {
                    popupMenu.getMenu().add(Menu.NONE, i, i, menuItems.get(i));
                }

                // Xử lý khi chọn item
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                        int itemId = menuItem.getItemId(); // ID chính là index trong danh sách
                        String title = menuItems.get(itemId); // Lấy lại tên tương ứng
                        Toast.makeText(requireContext(), "Bạn chọn: " + title, Toast.LENGTH_SHORT).show();
                        // TODO: Xử lý thêm logic theo title hoặc itemId nếu cần
                        return true;
                    }
                });

                popupMenu.show();
            }
        });

    }

    private void setPhoneticText(List<PhoneticModel> phonetics) {
        StringBuilder phoneticText = wordCommon.setPhoneticText(phonetics);
        txvPhonetic.setText(phoneticText.toString());
    }

    private void setMeaningInfo(List<MeaningModel> meanings) {
        StringBuilder partOfSpeechText = wordCommon.setMeaningPartOfSpeech(meanings);
        StringBuilder vietNameseText = wordCommon.setMeaningVietNamese(meanings);
        txvPartOfSpeech.setText(partOfSpeechText.toString());
        txvVietNamese.setText(vietNameseText.toString());
    }

    private void addDefinitions(List<MeaningModel> meanings) {
        List<DefinitionModel> definitions = wordCommon.addDefinitions(meanings);
        int i = 0;
        for (DefinitionModel d : definitions) {
            i++;
            TextView txvDef = new TextView(getContext());
            TextView txvEx = new TextView(getContext());
            txvDef.setText("Định nghĩa "+i+": "+d.getDefiniton());
            txvDef.setTextSize(20);
            txvDef.setPadding(5, 10, 0, 0);

            if(d.getExample() != null)
            {
                txvEx.setText("→ Ví dụ: " + d.getExample());
            }
            else{
                txvEx.setText("→ Không có ví dụ cho định nghĩa này của từ ");
            }

            txvEx.setTextSize(18);
            txvEx.setPadding(10, 0, 0, 10);

            containerWordDetail.addView(txvDef);
            containerWordDetail.addView(txvEx);
        }
    }


}
