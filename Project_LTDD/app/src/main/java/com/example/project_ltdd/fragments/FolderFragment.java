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
import com.example.project_ltdd.adapter.FolderAdapter;
import com.example.project_ltdd.models.FolderModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FolderFragment extends Fragment {

    List<FolderModel> folderList = new ArrayList<>();
    FolderAdapter adapter;

    TextView txvQuantityFolder;

    Button btnAddFolder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fragment_folder, container, false);

        txvQuantityFolder = view.findViewById(R.id.txvQuantityFolder);
        btnAddFolder = view.findViewById(R.id.btnAddFolder);

        // Setup Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFolders);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Sample data
        folderList.add(new FolderModel(1, "TỪ ĐÃ LƯU"));
        folderList.add(new FolderModel(2, "TỪ ĐANG HỌC"));

        // Truyền dữ liệu vào adapter
        adapter = new FolderAdapter(folderList);
        adapter.setOnFolderActionListener(new FolderAdapter.OnFolderActionListener() {
            @Override
            public void onEdit(FolderModel folder, int position) {
                Toast.makeText(requireContext(), "Chỉnh sửa: " + folder.getFolderName(), Toast.LENGTH_SHORT).show();
                // TODO: Mở dialog chỉnh sửa thư mục
            }

            @Override
            public void onDelete(FolderModel folder, int position) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Xóa thư mục?")
                        .setMessage("Bạn có chắc muốn xóa thư mục \"" + folder.getFolderName() + "\"?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            adapter.removeAt(position);
                            txvQuantityFolder.setText("("+folderList.size()+")");
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }

            @Override
            public void onMove(FolderModel folder, int position) {
                adapter.moveUp(position);
            }
        });

        recyclerView.setAdapter(adapter);
        txvQuantityFolder.setText("("+folderList.size()+")");

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
                        adapter.addFolder(folderName); // gọi hàm thêm folder
                        txvQuantityFolder.setText("("+folderList.size()+")");
                    } else {
                        Toast.makeText(requireContext(), "Tên thư mục không được để trống", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
                builder.show();
            }
        });
        return view;
    }
}
