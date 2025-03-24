package com.example.project_ltdd.activities;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.project_ltdd.R;
import com.example.project_ltdd.fragments.HomeFragment;
import com.example.project_ltdd.fragments.QuizzFragment;
import com.example.project_ltdd.fragments.SearchFragment;
import com.example.project_ltdd.fragments.SettingFragment;
import com.example.project_ltdd.fragments.TranslationFragment;
import com.example.project_ltdd.fragments.WordLookedUpFragment;
import com.example.project_ltdd.fragments.YourWordFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View headerView;

    private ImageView btnMenuClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        headerView = navigationView.inflateHeaderView(R.layout.nav_header);
        btnMenuClose = headerView.findViewById(R.id.btn_close_nav);
        btnMenuClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Từ điển SmartDict");

        // Hiện nút mở menu trên Toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Xử lý chọn mục trong menu
        navigationView.setNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if(itemId == R.id.nav_quizz){
                selectedFragment = new QuizzFragment();
            } else if(itemId == R.id.nav_search){
                selectedFragment = new SearchFragment();
            } else if(itemId == R.id.nav_settings){
                selectedFragment = new SettingFragment();
            } else if(itemId == R.id.nav_textTranslation){
                selectedFragment = new TranslationFragment();
            } else if(itemId == R.id.nav_wordLookedUp){
                selectedFragment = new WordLookedUpFragment();
            } else {
                selectedFragment = new YourWordFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Mặc định chọn HomeFragment khi mở app
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


    }


}