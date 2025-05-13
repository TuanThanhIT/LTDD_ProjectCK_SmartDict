package com.example.project_ltdd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
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

import com.example.project_ltdd.R;
import com.example.project_ltdd.api.retrofit_client.UserRetrofitClient;
import com.example.project_ltdd.api.services.UserService;
import com.example.project_ltdd.commons.WordCommons;
import com.example.project_ltdd.models.DefinitionModel;
import com.example.project_ltdd.models.FavoriteWordModel;
import com.example.project_ltdd.models.FolderModel;
import com.example.project_ltdd.models.MeaningModel;
import com.example.project_ltdd.models.PhoneticModel;
import com.example.project_ltdd.models.WordModel;
import com.example.project_ltdd.utils.UserPrefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordDetailFragment extends Fragment {

    private TextView txvWord, txvPhonetic, txvVietNamese, txvPartOfSpeech, txvWordTitle, txvFolder;
    private ImageView btnPlayAudioUK, btnPlayAudioUS, btnFavoriteMenu;
    private LinearLayout containerWordDetail;
    private Toolbar toolbar;
    private UserService userService = UserRetrofitClient.getClient();

    private List<FolderModel> menuItems = new ArrayList<>();
    private WordModel wordDetail;
    private FolderModel folder;
    private boolean isFolderSelected = false;

    private UserPrefs userPrefs;
    private WordCommons wordCommon = new WordCommons();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_word_details, container, false);
        initViews(view);
        getFoldersFromApi();

        getParentFragmentManager().setFragmentResultListener("request_word", this, (key, bundle) -> {
            wordDetail = (WordModel) bundle.getSerializable("wordDetail");
            if (wordDetail == null) {
                Toast.makeText(getContext(), "Không có dữ liệu từ vựng", Toast.LENGTH_SHORT).show();
                return;
            }
            // Đầu tiên fetch folder liên quan đến từ
            fetchFolderThenBind();
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
        txvWordTitle = view.findViewById(R.id.txvWordTitle);
        toolbar = view.findViewById(R.id.toolbar);
        txvFolder = view.findViewById(R.id.txvFolder);
        btnFavoriteMenu = view.findViewById(R.id.imgFavorite);
        userPrefs = new UserPrefs(requireContext());
    }

    private void fetchFolderThenBind() {
        userService.getFolderByWord(wordDetail.getWordId(), userPrefs.getUserId()).enqueue(new Callback<FolderModel>() {
            @Override
            public void onResponse(Call<FolderModel> call, Response<FolderModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    folder = response.body();
                } else {
                    folder = new FolderModel(-1, "TỪ ĐÃ TRA");
                }
                bindWordDetail();
            }

            @Override
            public void onFailure(Call<FolderModel> call, Throwable t) {
                folder = new FolderModel(-1, "TỪ ĐÃ TRA");
                bindWordDetail();
            }
        });
    }

    private void bindWordDetail() {
        // Thiết lập toolbar
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // Dữ liệu từ vựng
        txvWord.setText(wordDetail.getWord());
        txvWordTitle.setText(wordDetail.getWord());
        setPhoneticText(wordDetail.getPhonetics());
        setMeaningInfo(wordDetail.getMeanings());
        addDefinitions(wordDetail.getMeanings());

        // Dữ liệu folder
        txvFolder.setText(folder.getFolderName());
        if (folder.getFolderId() == -1) {
            isFolderSelected = false;
            btnFavoriteMenu.setImageResource(R.drawable.ic_heart1);
        } else {
            isFolderSelected = true;
            btnFavoriteMenu.setImageResource(R.drawable.ic_heart2);
        }

        // Nút play audio
        btnPlayAudioUK.setOnClickListener(v -> {
            String selectedAudio = wordCommon.getAudio(true, wordDetail);
            wordCommon.playAudio(selectedAudio, v);
        });
        btnPlayAudioUS.setOnClickListener(v -> {
            String selectedAudio = wordCommon.getAudio(false, wordDetail);
            wordCommon.playAudio(selectedAudio, v);
        });

        // Xử lý favorite menu
        btnFavoriteMenu.setOnClickListener(view -> {
            if (!isFolderSelected) {
                showFolderPopup();
            } else {
                deleteWordFavor();
                txvFolder.setText("TỪ ĐÃ TRA");
                btnFavoriteMenu.setImageResource(R.drawable.ic_heart1);
                isFolderSelected = false;
            }
        });
    }

    private void showFolderPopup() {
        PopupMenu popupMenu = new PopupMenu(requireContext(), btnFavoriteMenu);
        for (int i = 0; i < menuItems.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, ">Thư mục [" + menuItems.get(i).getFolderName() + "]");
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int idx = menuItem.getItemId();
            FolderModel chosen = menuItems.get(idx);
            addFavoriteWord(chosen.getFolderId(), chosen.getFolderName());
            return true;
        });
        popupMenu.show();
    }

    private void setPhoneticText(List<PhoneticModel> phonetics) {
        txvPhonetic.setText(wordCommon.setPhoneticText(phonetics).toString());
    }

    private void setMeaningInfo(List<MeaningModel> meanings) {
        txvPartOfSpeech.setText(wordCommon.setMeaningPartOfSpeech(meanings).toString());
        txvVietNamese.setText(wordCommon.setMeaningVietNamese(meanings).toString());
    }

    private void addDefinitions(List<MeaningModel> meanings) {
        List<DefinitionModel> definitions = wordCommon.addDefinitions(meanings);
        for (int i = 0; i < definitions.size(); i++) {
            DefinitionModel d = definitions.get(i);
            TextView txvDef = new TextView(getContext());
            txvDef.setText("- Định nghĩa " + (i+1) + ": " + d.getDefiniton());
            txvDef.setTextSize(20);
            txvDef.setPadding(5, 15, 10, 5);
            containerWordDetail.addView(txvDef);
            TextView txvEx = new TextView(getContext());
            txvEx.setText(d.getExample() != null ? "→ Ví dụ: " + d.getExample() : "→ Không có ví dụ");
            txvEx.setTextSize(18);
            txvEx.setPadding(5, 5, 10, 5);
            containerWordDetail.addView(txvEx);
        }
    }

    private void getFoldersFromApi() {
        userService.getFolders(userPrefs.getUserId()).enqueue(new Callback<List<FolderModel>>() {
            @Override
            public void onResponse(Call<List<FolderModel>> call, Response<List<FolderModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    menuItems = response.body();
                }
            }
            @Override
            public void onFailure(Call<List<FolderModel>> call, Throwable t) { }
        });
    }

    private void addFavoriteWord(int folderId, String folderName) {
        userService.addFavoriteWord(userPrefs.getUserId(), folderId, wordDetail.getWordId()).enqueue(new Callback<FavoriteWordModel>() {
            @Override
            public void onResponse(Call<FavoriteWordModel> call, Response<FavoriteWordModel> response) {
                Toast.makeText(requireContext(), "Từ vựng được thêm vào thư mục thành công!", Toast.LENGTH_SHORT).show();
                txvFolder.setText(folderName);
                btnFavoriteMenu.setImageResource(R.drawable.ic_heart2);
                isFolderSelected = true;
            }
            @Override
            public void onFailure(Call<FavoriteWordModel> call, Throwable t) { }
        });
    }

    private void deleteWordFavor() {
        int userId = userPrefs.getUserId();
        userService.deleteWordFavor(userId, wordDetail.getWordId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(requireContext(), "Từ này đã bị xóa khỏi thư mục", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) { }
        });
    }
}
