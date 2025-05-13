package com.example.project_ltdd.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.api.retrofit_client.UserRetrofitClient;
import com.example.project_ltdd.api.services.UserService;
import com.example.project_ltdd.commons.WordCommons;
import com.example.project_ltdd.fragments.WordDetailFragment;
import com.example.project_ltdd.models.WordModel;
import com.example.project_ltdd.utils.UserPrefs;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordSearchAdapter extends RecyclerView.Adapter<WordSearchAdapter.VocabViewHolder> implements Filterable {

    private List<WordModel> vocabList;
    private List<WordModel> vocabListFiltered = new ArrayList<>();
    private FragmentManager mFragmentManager;

    private Context context;

    private WordModel vocab;
    WordCommons wordCommon = new WordCommons();
    private UserPrefs userPrefs;
    public WordSearchAdapter(List<WordModel> vocabList, FragmentManager mFragmentManager, Context context) {
        this.vocabList = vocabList;
        this.mFragmentManager = mFragmentManager;
        this.context = context;
        this.userPrefs = new UserPrefs(context);
    }

    @NonNull
    @Override
    public VocabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vocabulary, parent, false);
        return new VocabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VocabViewHolder holder, int position) {
        WordModel vocab = vocabListFiltered.get(position); // Dùng biến cục bộ

        holder.txvWord.setText(vocab.getWord());

        StringBuilder phoneticText = wordCommon.setPhoneticText(vocab.getPhonetics());
        holder.txvPhonetic.setText(phoneticText.toString());

        StringBuilder partOfSpeechText = wordCommon.setMeaningPartOfSpeech(vocab.getMeanings());
        StringBuilder vietNameseText = wordCommon.setMeaningVietNamese(vocab.getMeanings());

        holder.txvPartOfSpeech.setText(partOfSpeechText.toString());
        holder.txvMeaning.setText(vietNameseText.toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WordDetailFragment detailFragment = new WordDetailFragment();

                Bundle result = new Bundle();
                result.putSerializable("wordDetail", vocab);

                mFragmentManager.setFragmentResult("request_word", result);

                postUserSearchWordFromApi(vocab); // Truyền vocab đúng tương ứng

                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, detailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
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

    private void postUserSearchWordFromApi(WordModel vocab) {
        UserService userService = UserRetrofitClient.getClient();

        Long wordId = vocab.getWordId();
        int userId = userPrefs.getUserId();

        userService.addWordSearch(userId, wordId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String message = response.body().string();
//                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(context, "Lỗi khi đọc phản hồi từ máy chủ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorJson = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorJson);
                        String errorMessage = jsonObject.optString("error", "Lỗi không xác định");
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(context, "Lỗi khi đọc lỗi từ phản hồi", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "Lỗi phản hồi máy chủ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
