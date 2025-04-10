package com.example.project_ltdd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project_ltdd.R;
import com.example.project_ltdd.adapter.YourWordPagerAdapter;
import com.example.project_ltdd.models.FolderModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class YourWordFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private YourWordPagerAdapter pagerAdapter;
    private ArrayList<FolderModel> folderList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fragment_yourword, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        view.findViewById(R.id.btnFolder).setOnClickListener(v -> {
            // Tạo fragment mới
            FolderFragment folderFragment = new FolderFragment();

            // Thực hiện chuyển fragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, folderFragment) // fragment_container là id của FrameLayout chứa fragment
                    .addToBackStack(null) // để quay lại được
                    .commit();
        });

        // Giả sử lấy danh sách folder từ Fragment Folder truyền qua
        // Sample data
        folderList = new ArrayList<>();
        folderList.add(new FolderModel(1, "TỪ ĐÃ LƯU"));
        folderList.add(new FolderModel(2, "TỪ ĐANG HỌC"));
        pagerAdapter = new YourWordPagerAdapter(getActivity(), folderList);
        viewPager.setAdapter(pagerAdapter);

        // Liên kết TabLayout và ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            View customTab = LayoutInflater.from(getContext()).inflate(R.layout.folder_tab, null);
            TextView tabText = customTab.findViewById(R.id.tabText);
            tabText.setText(folderList.get(position).getFolderName());
            tab.setCustomView(customTab);
        }).attach();

        return view;
    }

//    private List<FolderModel> loadFolderList() {
//        // TODO: Load từ database hoặc Bundle
//        return new ArrayList<>();
//    }
}
