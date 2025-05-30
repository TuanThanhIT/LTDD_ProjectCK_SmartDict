package com.example.project_ltdd.activities;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.project_ltdd.R;
import com.example.project_ltdd.fragments.HomeFragment;
import com.example.project_ltdd.fragments.AIFragment;
import com.example.project_ltdd.fragments.QuizFragment;
import com.example.project_ltdd.fragments.WordFavoriteFragment;
import com.example.project_ltdd.fragments.WordSearchFragment;
import com.example.project_ltdd.fragments.SecurityPolicyFragment;
import com.example.project_ltdd.fragments.SettingFragment;
import com.example.project_ltdd.fragments.TermFragment;
import com.example.project_ltdd.fragments.TranslationFragment;
import com.example.project_ltdd.fragments.WordLookedUpFragment;
import com.example.project_ltdd.fragments.YourWordFragment;
import com.example.project_ltdd.utils.UserPrefs;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private NavigationView navigationUserView;

    private BottomNavigationView navigationBottom;
    private View headerView;

    private ImageView btnMenuClose;

    private ImageView btnUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_menuView);
        navigationUserView = findViewById(R.id.navigation_userView);
        navigationBottom = findViewById(R.id.bottom_navigation);
        headerView = navigationView.inflateHeaderView(R.layout.nav_header);
        btnMenuClose = headerView.findViewById(R.id.btn_close_nav);
        btnUser = findViewById(R.id.btn_user);

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
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                // Khi mở menu -> Đóng navigationUserView nếu đang mở
                if (navigationUserView.getVisibility() == View.VISIBLE) {
                    navigationUserView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        toggle.syncState();

        // Xử lý chọn mục trong menu chính
        navigationView.setNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                switchFragment(new HomeFragment());
            } else if(itemId == R.id.nav_quizz){
                switchFragmentIfLoggedIn(new QuizFragment());
            } else if(itemId == R.id.nav_search){
                switchFragmentIfLoggedIn(new WordSearchFragment());
            } else if(itemId == R.id.nav_settings){
                switchFragment(new SettingFragment());
            } else if(itemId == R.id.nav_textTranslation){
                switchFragmentIfLoggedIn(new TranslationFragment());
            } else if(itemId == R.id.nav_wordLookedUp){
                switchFragmentIfLoggedIn(new WordLookedUpFragment());
            } else if(itemId == R.id.nav_yourWord){
                switchFragmentIfLoggedIn(new YourWordFragment());
            } else if(itemId == R.id.nav_term) {
                switchFragment(new TermFragment());
            } else if(itemId == R.id.nav_facebook){
                openFacebookPage(MainActivity.this);
            } else {
                switchFragment(new SecurityPolicyFragment());
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
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

        // Xử lý menu user:


        // Xử lý bottom navigation
        navigationBottom.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            View view = navigationBottom.findViewById(itemId);
            if (view != null) {
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.nav_item_scale);
                view.startAnimation(animation);
            }
            if(itemId == R.id.nav_bottomHome){
                switchFragment(new HomeFragment());
            } else if(itemId == R.id.nav_bottomSearch){
                switchFragmentIfLoggedIn(new WordSearchFragment());
            } else if(itemId == R.id.nav_bottomYourWord) {
                switchFragmentIfLoggedIn(new YourWordFragment());
            } else if(itemId == R.id.nav_bottomAI){
                switchFragmentIfLoggedIn(new AIFragment());
            } else {
                moveTaskToBack(true);
                return true;
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out) // Hiệu ứng Fade
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }

            return false;
        });

        btnUser.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

    }

    private void openFacebookPage(Context context) {
        String facebookUrl = "https://www.facebook.com/share/164Z1MWcDM/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
        context.startActivity(intent);
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void switchFragmentIfLoggedIn(Fragment fragment) {
        UserPrefs userPrefs = new UserPrefs(this);
        if (!userPrefs.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            switchFragment(fragment);
        }
    }


}