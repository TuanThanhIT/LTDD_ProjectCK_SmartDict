package com.example.project_ltdd.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project_ltdd.R;
import com.example.project_ltdd.adapters.BannerViewPager2Adapter;
import com.example.project_ltdd.adapters.TopViewPager2Adapter;
import com.example.project_ltdd.api.retrofit_client.UserRetrofitClient;
import com.example.project_ltdd.api.retrofit_client.WordRetrofitClient;
import com.example.project_ltdd.api.services.UserService;
import com.example.project_ltdd.api.services.WordService;
import com.example.project_ltdd.models.BannerModel;
import com.example.project_ltdd.models.UserTopModel;
import com.example.project_ltdd.models.WordTopModel;
import com.example.project_ltdd.utils.AutoScrollViewPager2;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private ViewPager2 viewPager2Top;
    private ViewPager2 viewPager2Banner;
    private CircleIndicator3 circleIndicator33;
    private CircleIndicator3 circleIndicator3;

    private List<BannerModel> bannersList;
    private List<Fragment> fragments;

    private UserService userService = UserRetrofitClient.getClient();
    private WordService wordService = WordRetrofitClient.getClient();

    private List<UserTopModel> listUserTop;
    private List<WordTopModel> listWordsSearchTop, listWordsFavorTop;

    private List<String> topSearchedWords, topFavoriteWords, topQuizStudents;
    private int apiLoadedCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_home, container, false);
        initViews(view);
        getTopUserBest();
        getTopWordsSearch();
        getTopWordsFavor();
        return view;
    }

    private void initViews(View view){
        viewPager2Banner = view.findViewById(R.id.viewPager2Banner);
        circleIndicator33 = view.findViewById(R.id.circle_indicator33);

        bannersList = getListBanners();
        BannerViewPager2Adapter adater1 = new BannerViewPager2Adapter(bannersList);
        viewPager2Banner.setAdapter(adater1);
        circleIndicator33.setViewPager(viewPager2Banner);

        AutoScrollViewPager2 autoScrollBanner = new AutoScrollViewPager2(viewPager2Banner, 5000);
        viewPager2Banner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                autoScrollBanner.stop();
                autoScrollBanner.start();
            }
        });

        viewPager2Top = view.findViewById(R.id.viewPager2Top);
        circleIndicator3 = view.findViewById(R.id.circle_indicator3);

        Toast.makeText(requireContext(), "Trang chủ", Toast.LENGTH_SHORT).show();
    }

    private List<BannerModel> getListBanners() {
        List<BannerModel> list = new ArrayList<>();
        list.add(new BannerModel(R.drawable.img_banner1));
        list.add(new BannerModel(R.drawable.img_banner2));
        list.add(new BannerModel(R.drawable.img_banner3));
        list.add(new BannerModel(R.drawable.img_banner4));
        list.add(new BannerModel(R.drawable.img_banner5));
        return list;
    }

    private void setupFragment(){
        fragments = new ArrayList<>();
        fragments.add(TopFragment.newInstance("Top 5 từ vựng tìm kiếm nhiều nhất", topSearchedWords));
        fragments.add(TopFragment.newInstance("Top 5 từ vựng được yêu thích nhất", topFavoriteWords));
        fragments.add(TopFragment.newInstance("Top 5 người dùng đạt điểm cao nhất", topQuizStudents));

        TopViewPager2Adapter adapter2 = new TopViewPager2Adapter(requireActivity(), fragments);
        viewPager2Top.setAdapter(adapter2);
        circleIndicator3.setViewPager(viewPager2Top);

        AutoScrollViewPager2 autoScrollTop = new AutoScrollViewPager2(viewPager2Top, 5000);
        viewPager2Top.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                autoScrollTop.stop();
                autoScrollTop.start();
            }
        });
    }

    private void getTopUserBest() {
        userService.getTopUserBest().enqueue(new Callback<List<UserTopModel>>() {
            @Override
            public void onResponse(Call<List<UserTopModel>> call, Response<List<UserTopModel>> response) {
                if(response.isSuccessful() && response.body() != null){
                    listUserTop = response.body();
                    getFullNameTop();
                } else {
                    Toast.makeText(requireContext(), "Không thể hiển thị dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserTopModel>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFullNameTop(){
        topQuizStudents = new ArrayList<>();
        for(UserTopModel u: listUserTop) {
            topQuizStudents.add(u.getFullname());
        }
        checkAndSetupFragment();
    }

    private void getTopWordsSearch(){
        wordService.getTopWordsSearch().enqueue(new Callback<List<WordTopModel>>() {
            @Override
            public void onResponse(Call<List<WordTopModel>> call, Response<List<WordTopModel>> response) {
                if(response.isSuccessful() && response.body() != null){
                    listWordsSearchTop = response.body();
                    getWordsSearch();
                } else {
                    Toast.makeText(requireContext(), "Không thể hiển thị dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<WordTopModel>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getWordsSearch(){
        topSearchedWords = new ArrayList<>();
        for(WordTopModel w: listWordsSearchTop){
            topSearchedWords.add(w.getWord());
        }
        checkAndSetupFragment();
    }

    private void getTopWordsFavor(){
        wordService.getTopWordsFavor().enqueue(new Callback<List<WordTopModel>>() {
            @Override
            public void onResponse(Call<List<WordTopModel>> call, Response<List<WordTopModel>> response) {
                if(response.isSuccessful() && response.body() != null){
                    listWordsFavorTop = response.body();
                    getWordsFavor();
                } else {
                    Toast.makeText(requireContext(), "Không thể hiển thị dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<WordTopModel>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getWordsFavor(){
        topFavoriteWords = new ArrayList<>();
        for(WordTopModel w: listWordsFavorTop){
            topFavoriteWords.add(w.getWord());
        }
        checkAndSetupFragment();
    }

    private void checkAndSetupFragment() {
        apiLoadedCount++;
        if (apiLoadedCount == 3) {
            setupFragment();
        }
    }
}
