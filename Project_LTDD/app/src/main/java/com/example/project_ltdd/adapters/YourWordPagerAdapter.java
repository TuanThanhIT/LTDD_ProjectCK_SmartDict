package com.example.project_ltdd.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.project_ltdd.models.FolderModel;
import com.example.project_ltdd.fragments.WordFavoriteFragment;

import java.util.List;

public class YourWordPagerAdapter extends FragmentStateAdapter {
    private final List<FolderModel> folderList;

    public YourWordPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<FolderModel> folderList) {
        super(fragmentActivity);
        this.folderList = folderList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        FolderModel folder = folderList.get(position);
        return WordFavoriteFragment.newInstance(folder.getFolderId(), folder.getFolderName()); // Truyền id folder để load từ vựng
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }
}
