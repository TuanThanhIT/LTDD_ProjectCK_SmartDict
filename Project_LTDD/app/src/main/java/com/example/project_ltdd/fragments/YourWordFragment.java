package com.example.project_ltdd.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project_ltdd.R;
import com.example.project_ltdd.adapters.YourWordPagerAdapter;
import com.example.project_ltdd.api.retrofit_client.UserRetrofitClient;
import com.example.project_ltdd.api.services.UserService;
import com.example.project_ltdd.models.FolderModel;
import com.example.project_ltdd.models.WordModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourWordFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private YourWordPagerAdapter pagerAdapter;
    private List<FolderModel> folderList = new ArrayList<>();

    private ImageView btnMenu;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fragment_yourword, container, false);
        initViews(view);
        getFoldersFromApi();
        return view;
    }

    private void initViews(View view){
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        btnMenu = view.findViewById(R.id.btnMenu);

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

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(requireContext(), btnMenu);
                popupMenu.getMenuInflater().inflate(R.menu.favorite_above_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.action_add) {
                            // Xử lý thêm tu
                            return true;
                        } else if (id == R.id.action_arrangeAZ) {
                            // TODO:Sắp xếp A->Z
                            return true;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });

    }

    private void setUpAdapter()
    {
        pagerAdapter = new YourWordPagerAdapter(getActivity(), folderList);
        viewPager.setAdapter(pagerAdapter);

        // Liên kết TabLayout và ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            View customTab = LayoutInflater.from(getContext()).inflate(R.layout.item_tab, null);
            TextView tabText = customTab.findViewById(R.id.tabText);
            tabText.setText(folderList.get(position).getFolderName());
            tab.setCustomView(customTab);
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Get the tab text view
                TextView tabText = tab.getCustomView().findViewById(R.id.tabText);

                // Hiệu ứng rõ dần (fade in) khi chọn tab
                tabText.animate().alpha(1f).setDuration(250).start();

                // Thay đổi màu chữ thành trắng khi tab được chọn
                tabText.setTextColor(Color.WHITE);

                // Đánh dấu tab đã được chọn (để drawable selector thay đổi màu nền)
                tabText.setSelected(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Get the tab text view
                TextView tabText = tab.getCustomView().findViewById(R.id.tabText);

                // Hiệu ứng mờ dần (fade out) khi bỏ chọn tab
                tabText.animate().alpha(0.7f).setDuration(250).start();

                // Thay đổi màu chữ thành đen khi tab không được chọn
                tabText.setTextColor(Color.BLACK);

                // Đánh dấu tab đã bị bỏ chọn
                tabText.setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Hiệu ứng có thể thêm ở đây, ví dụ như khi tab được chọn lại
                TextView tabText = tab.getCustomView().findViewById(R.id.tabText);
                tabText.animate().alpha(1f).setDuration(200).start();
            }
        });
    }
    private void getFoldersFromApi(){
        UserService userService = UserRetrofitClient.getClient();
        int userId = 1;
        userService.getFolders(userId).enqueue(new Callback<List<FolderModel>>() {
            @Override
            public void onResponse(Call<List<FolderModel>> call, Response<List<FolderModel>> response) {
                if(response.isSuccessful())
                {
                    folderList = response.body();
                    setUpAdapter();
                    Toast.makeText(requireContext(), "Từ của bạn!", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(requireContext(), "Không thể hiển thị danh sách Thư mục của bạn! Lỗi: "+response.code(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FolderModel>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
