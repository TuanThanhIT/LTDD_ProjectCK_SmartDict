package com.example.project_ltdd.activities.admin_activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;


import com.example.project_ltdd.R;
import com.example.project_ltdd.databinding.ActivityAdminMainBinding;
import com.example.project_ltdd.fragments.admin_fragments.AdminFabFragment;
import com.example.project_ltdd.fragments.admin_fragments.AdminHomeFragment;
import com.example.project_ltdd.fragments.admin_fragments.AdminProfileFragment;
import com.example.project_ltdd.fragments.admin_fragments.AdminQuizzFragment;
import com.example.project_ltdd.fragments.admin_fragments.AdminUsersFragment;

public class MainAdminActivity extends AppCompatActivity {
    ActivityAdminMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavigationView.setBackground(null);

        replaceFragment(new AdminHomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.admin_home) {
                replaceFragment(new AdminHomeFragment());
            } else if (item.getItemId() == R.id.quizz_list) {
                replaceFragment(new AdminQuizzFragment());
            } else if (item.getItemId() == R.id.users) {
                replaceFragment(new AdminUsersFragment());
            } else if (item.getItemId() == R.id.admin_profile) {
                replaceFragment(new AdminProfileFragment());
            } else if (item.getItemId() == R.id.fab) {
                replaceFragment(new AdminFabFragment());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}
