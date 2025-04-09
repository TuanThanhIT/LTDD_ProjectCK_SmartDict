package com.example.project_ltdd.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class TopViewPager2Adapter extends FragmentStateAdapter {
    private final List<Fragment> fragmentList;

    public TopViewPager2Adapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragments) {
        super(fragmentActivity);
        this.fragmentList = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}

//👉 Chứa danh sách các Fragment và quản lý việc hiển thị từng Fragment trong ViewPager2.
