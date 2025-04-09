package com.example.project_ltdd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project_ltdd.R;
import com.example.project_ltdd.adapter.BannerViewPager2Adapter;
import com.example.project_ltdd.adapter.TopViewPager2Adapter;
import com.example.project_ltdd.models.BannerModel;
import com.example.project_ltdd.utils.AutoScrollViewPager2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment {
    private ViewPager2 viewPager2Top;
    private ViewPager2 viewPager2Banner;
    private CircleIndicator3 circleIndicator33;

    private CircleIndicator3 circleIndicator3;
    private List<BannerModel> bannersList;

    private List<Fragment> fragments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_home, container, false);

        // Banner quảng cáo
        viewPager2Banner = view.findViewById(R.id.viewPager2Banner);
        circleIndicator33 = view.findViewById(R.id.circle_indicator33);

        bannersList = getListBanners();

        BannerViewPager2Adapter adater1 = new BannerViewPager2Adapter(bannersList);
        viewPager2Banner.setAdapter(adater1);

        // Liên kết ViewPager2 với CircleIndicator3
        circleIndicator33.setViewPager(viewPager2Banner);

        AutoScrollViewPager2 autoScrollBanner = new AutoScrollViewPager2(viewPager2Banner, 5000);

        // Lắng nghe sự kiện khi ViewPager2 chuyển trang
        viewPager2Banner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                autoScrollBanner.stop();
                autoScrollBanner.start();// Chạy auto slide sau 5 giây
            }
        });


        // Hiển thì top
        viewPager2Top = view.findViewById(R.id.viewPager2Top);
        circleIndicator3 = view.findViewById(R.id.circle_indicator3);

        List<String> topSearchedWords = Arrays.asList("Hello", "Love", "Computer", "Music", "Friend");         // Dữ liệu giả
        List<String> topFavoriteWords = Arrays.asList("Sunshine", "Adventure", "Happiness", "Dream", "Success");
        List<String> topQuizStudents = Arrays.asList("Nguyễn Văn A", "Trần Thị B", "Lê Văn C", "Hoàng Minh D", "Phạm Hồng E");

        fragments = new ArrayList<>();         // Tạo danh sách fragment
        fragments.add(TopFragment.newInstance("Top 5 từ vựng tìm kiếm nhiều nhất", topSearchedWords));
        fragments.add(TopFragment.newInstance("Top 5 từ vựng được yêu thích nhất", topFavoriteWords));
        fragments.add(TopFragment.newInstance("Top 5 người dùng đạt điểm cao nhất", topQuizStudents));

        TopViewPager2Adapter adapter2 = new TopViewPager2Adapter(requireActivity(), fragments);    // Thiết lập ViewPager2
        viewPager2Top.setAdapter(adapter2);

        circleIndicator3.setViewPager(viewPager2Top);// Liên kết ViewPager2 với CircleIndicator3


        AutoScrollViewPager2 autoScrollTop = new AutoScrollViewPager2(viewPager2Top, 5000);

        viewPager2Top.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {   // Đăng ký một trình lắng nghe (OnPageChangeCallback) cho ViewPager2. Mỗi khi người dùng vuốt qua một trang mới, callback này sẽ được gọi.
                super.onPageSelected(position); // Được gọi khi một trang mới được chọn (người dùng lướt sang trang khác).
                autoScrollTop.stop(); //Dừng runnable2 nếu nó đang chạy.
                autoScrollTop.start(); //Đặt lại bộ đếm thời gian để tự động chuyển trang sau 3 giây.
            }
        });
        return view;
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

}
