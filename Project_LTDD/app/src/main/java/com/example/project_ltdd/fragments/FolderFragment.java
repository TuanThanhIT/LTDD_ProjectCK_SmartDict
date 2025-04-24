package com.example.project_ltdd.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.adapters.FolderAdapter;
import com.example.project_ltdd.api.retrofit_client.UserRetrofitClient;
import com.example.project_ltdd.api.services.UserService;
import com.example.project_ltdd.models.FolderModel;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FolderFragment extends Fragment {

    List<FolderModel> folderList = new ArrayList<>();
    FolderAdapter adapter;

    TextView txvQuantityFolder;

    Button btnAddFolder;

    RecyclerView recyclerView;

    UserService userService = UserRetrofitClient.getClient();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fragment_folder, container, false);
        initViews(view);
        getFoldersFromApi();
        return view;
    }
    private void initViews(View view){
        txvQuantityFolder = view.findViewById(R.id.txvQuantityFolder);
        btnAddFolder = view.findViewById(R.id.btnAddFolder);
        recyclerView = view.findViewById(R.id.recyclerViewFolders);


        // Setup Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnAddFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Thêm thư mục");

                final EditText inputName = new EditText(requireContext());
                inputName.setHint("Nhập tên thư mục");
                builder.setView(inputName);

                builder.setPositiveButton("Thêm", (dialog, which)->{
                    String folderName = inputName.getText().toString().trim();
                    if (!folderName.isEmpty()) {
                        addFolder(1, folderName); // gọi hàm thêm folder
                        txvQuantityFolder.setText("("+folderList.size()+")");
                    } else {
                        Toast.makeText(requireContext(), "Tên thư mục không được để trống", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
                builder.show();
            }
        });
    }

    private void setUpAdapter(){
        // Truyền dữ liệu vào adapter
        adapter = new FolderAdapter(folderList);
        txvQuantityFolder.setText("("+folderList.size()+")");
        adapter.setOnFolderActionListener(new FolderAdapter.OnFolderActionListener() {
            @Override
            public void onEdit(FolderModel folder, int position) {
                Toast.makeText(requireContext(), "Chỉnh sửa: " + folder.getFolderName(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Chỉnh sửa thư mục");

                final EditText inputName = new EditText(requireContext());
                inputName.setText(folder.getFolderName());
                builder.setView(inputName);

                builder.setPositiveButton("Lưu", (dialog, which)->{
                    String folderName = inputName.getText().toString().trim();
                    if (!folderName.isEmpty()) {
                        updateFolder(folder.getFolderId(), folderName);
                    } else {
                        Toast.makeText(requireContext(), "Tên thư mục không được để trống", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
                builder.show();
            }

            @Override
            public void onDelete(FolderModel folder, int position) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Xóa thư mục?")
                        .setMessage("Bạn có chắc muốn xóa thư mục \"" + folder.getFolderName() + "\"?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            // Gọi API để xóa trên server
                            userService.deleteFolder(folder.getFolderId()).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        adapter.removeAt(position);
                                        txvQuantityFolder.setText("(" + folderList.size() + ")");
                                        Toast.makeText(requireContext(), "Đã xóa thư mục!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(requireContext(), "Lỗi khi xóa: " + response.code(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(requireContext(), "Lỗi kết nối khi xóa: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("Hủy", null)
                        .show();

            }

            @Override
            public void onMove(FolderModel folder, int position) {
                adapter.moveUp(position);
            }
        });
        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        recyclerView.setAdapter(adapter);
    }

    private void addFolder(int userId, String folderName){

        userService.addFolder(userId, folderName).enqueue(new Callback<FolderModel>() {
            @Override
            public void onResponse(Call<FolderModel> call, Response<FolderModel> response) {
                if (response.isSuccessful()) {
                    FolderModel  folderModel = response.body(); // Lấy nội dung thực tế từ body
                    folderList.add(folderModel); // ✅ Cập nhật danh sách ngay
                    adapter.notifyItemInserted(folderList.size() - 1);
                    txvQuantityFolder.setText("(" + folderList.size() + ")");
                    Toast.makeText(requireContext(), "Tạo thư mục thành công", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String errorJson = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorJson);
                        String errorMessage = jsonObject.optString("error", "Lỗi không xác định");
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Lỗi phản hồi máy chủ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FolderModel> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateFolder(int folderId, String folderName){
        userService.updateFolder(folderId, folderName).enqueue(new Callback<FolderModel>() {
            @Override
            public void onResponse(Call<FolderModel> call, Response<FolderModel> response) {
                if (response.isSuccessful()) {
                    FolderModel folderModel = response.body(); // Lấy nội dung thực tế từ body
                    // Tìm và cập nhật trong danh sách
                    for (int i = 0; i < folderList.size(); i++) {
                        if (folderList.get(i).getFolderId() == folderModel.getFolderId()) {
                            folderList.set(i, folderModel);
                            adapter.notifyItemChanged(i); // Cập nhật lại item tại vị trí đó
                            break;
                        }
                    }
                    Toast.makeText(requireContext(), "Chỉnh sửa thư mục thành công", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String errorJson = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorJson);
                        String errorMessage = jsonObject.optString("error", "Lỗi không xác định");
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Lỗi phản hồi máy chủ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FolderModel> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFoldersFromApi(){
        int userId = 1;
        userService.getFolders(userId).enqueue(new Callback<List<FolderModel>>() {
            @Override
            public void onResponse(Call<List<FolderModel>> call, Response<List<FolderModel>> response) {
                if(response.isSuccessful())
                {
                    folderList = response.body();
                    setUpAdapter();
                    Toast.makeText(requireContext(), "Thư mục của bạn!", Toast.LENGTH_SHORT).show();
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
