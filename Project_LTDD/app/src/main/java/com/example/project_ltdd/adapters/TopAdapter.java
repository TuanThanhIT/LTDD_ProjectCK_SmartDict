package com.example.project_ltdd.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;

import java.util.List;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.TopViewHolder> {
    private final List<String> itemList;

    public TopAdapter(List<String> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public TopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top, parent, false);
        return new TopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopViewHolder holder, int position) {
        holder.txtItem.setText(itemList.get(position));

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class TopViewHolder extends RecyclerView.ViewHolder {
        TextView txtItem;

        public TopViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItem = itemView.findViewById(R.id.txtItem);
        }
    }
}

//👉 TopAdapter nhận danh sách dữ liệu, tạo ViewHolder cho từng item trong RecyclerView.
//👉 Hiển thị đơn giản: Chỉ cần truyền danh sách chuỗi (List<String>) vào là xong!