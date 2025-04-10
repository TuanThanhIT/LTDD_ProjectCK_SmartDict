package com.example.project_ltdd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.models.FolderModel;

import java.util.Collections;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    private List<FolderModel> folderList;
    private OnFolderActionListener listener;

    public interface OnFolderActionListener {
        void onEdit(FolderModel folder, int position);
        void onDelete(FolderModel folder, int position);
        void onMove(FolderModel folder, int position);
    }

    public FolderAdapter(List<FolderModel> folderList) {
        this.folderList = folderList;
    }

    public void setOnFolderActionListener(OnFolderActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_folder, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        FolderModel folder = folderList.get(position);
        holder.txvFolderName.setText(folder.getFolderName());

        holder.imvEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(folder, position);
        });

        holder.imvDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(folder, position);
        });

        holder.imvMove.setOnClickListener(v -> {
            if (listener != null) listener.onMove(folder, position);
        });
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    public static class FolderViewHolder extends RecyclerView.ViewHolder {
        TextView txvFolderName;
        ImageView imvEdit, imvDelete, imvMove;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            txvFolderName = itemView.findViewById(R.id.txvFolderName);
            imvEdit = itemView.findViewById(R.id.imvEdit);
            imvDelete = itemView.findViewById(R.id.imvDelete);
            imvMove = itemView.findViewById(R.id.imvMove);
        }
    }

    public void removeAt(int position) {
        folderList.remove(position);
        notifyItemRemoved(position);
    }

    public void moveUp(int position) {
        if (position > 0) {
            Collections.swap(folderList, position, position - 1);
            notifyItemMoved(position, position - 1);
        }
    }

    public void addFolder(String folderName) {
        FolderModel folder = new FolderModel(folderName);
        folderList.add(folder);
        notifyItemInserted(folderList.size() - 1); // Khi bạn dùng adapter.notifyItemInserted(...), RecyclerView sẽ tự hiển thị thêm một item mới, không cần load lại toàn bộ danh sách.
    }
}
