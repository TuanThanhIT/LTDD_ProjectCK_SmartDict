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
import com.example.project_ltdd.api.retrofit_client.UserRetrofitClient;
import com.example.project_ltdd.api.services.UserService;
import com.example.project_ltdd.commons.WordCommon;
import com.example.project_ltdd.models.DefinitionModel;
import com.example.project_ltdd.models.FavoriteWordModel;
import com.example.project_ltdd.models.FolderModel;
import com.example.project_ltdd.models.MeaningModel;
import com.example.project_ltdd.models.PhoneticModel;
import com.example.project_ltdd.models.WordModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordDetailFragment extends Fragment {

    private TextView txvWord, txvPhonetic, txvVietNamese, txvPartOfSpeech, txvWordTitlle, txvFolder;
    private ImageView btnPlayAudioUK, btnPlayAudioUS;
    private LinearLayout containerWordDetail;
    private WordModel wordDetail;
    private Toolbar toolbar;

    private ImageView btnFavoriteMenu;

    UserService userService = UserRetrofitClient.getClient();

    // Danh sách tùy chọn truyền vào
    List<FolderModel> menuItems = new ArrayList<>();

    private boolean isFolderSelected = false;

    WordCommon wordCommon = new WordCommon();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_word_details, container, false);
        initViews(view);
        getFoldersFromApi();

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
                if(isFolderSelected == false)
                {
                    PopupMenu popupMenu = new PopupMenu(requireContext(), btnFavoriteMenu);

                    // Thêm các mục vào menu
                    for (int i = 0; i < menuItems.size(); i++) {
                        popupMenu.getMenu().add(Menu.NONE, i, i,">Thư mục ["+ menuItems.get(i).getFolderName()+"]");
                    }

                    // Xử lý khi chọn item
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                            int itemId = menuItem.getItemId(); // ID chính là index trong danh sách
                            String title = menuItems.get(itemId).getFolderName(); // Lấy lại tên tương ứng
                            addFavoriteWord(itemId);
                            btnFavoriteMenu.setImageResource(R.drawable.ic_heart2);
                            txvFolder.setText(title);
                            return true;
                        }
                    });

                    popupMenu.show();
                    isFolderSelected = true;
                }
                else {
                    txvFolder.setText("TỪ ĐÃ TRA");
                    btnFavoriteMenu.setImageResource(R.drawable.ic_heart1);
                    Toast.makeText(requireContext(), "Từ này đã bị xóa khỏi danh sách Từ của bạn", Toast.LENGTH_SHORT).show();
                }

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

    private void getFoldersFromApi() {
        int userId = 1;
        userService.getFolders(userId).enqueue(new Callback<List<FolderModel>>() {
            @Override
            public void onResponse(Call<List<FolderModel>> call, Response<List<FolderModel>> response) {
                if (response.isSuccessful()) {
                    menuItems = response.body();
                } else {
                    Toast.makeText(requireContext(), "Không thể hiển thị danh sách Thư mục của bạn! Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FolderModel>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addFavoriteWord(int folderId){
        int userId = 1;
        userService.addFavoriteWord(userId, folderId, wordDetail.getWordId()).enqueue(new Callback<FavoriteWordModel>() {
            @Override
            public void onResponse(Call<FavoriteWordModel> call, Response<FavoriteWordModel> response) {
                if (response.isSuccessful()) {
                    FavoriteWordModel  favoriteWordModel = response.body(); // Lấy nội dung thực tế từ body
                    Toast.makeText(requireContext(), "Từ vựng được thêm vào thư mục thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String errorJson = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorJson);
                        String errorMessage = jsonObject.optString("error", "Lỗi không xác định");
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Lỗi phản hồi máy chủ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FavoriteWordModel> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
