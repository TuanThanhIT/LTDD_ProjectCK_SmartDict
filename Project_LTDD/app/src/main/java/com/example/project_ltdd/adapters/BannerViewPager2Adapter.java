package com.example.project_ltdd.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ltdd.R;
import com.example.project_ltdd.models.BannerModel;

import java.util.List;

public class BannerViewPager2Adapter extends RecyclerView.Adapter<BannerViewPager2Adapter.BannerViewHolder> {
    private List<BannerModel> bannersList;

    public BannerViewPager2Adapter(List<BannerModel> bannersList) {
        this.bannersList = bannersList;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        // Set dữ liệu lên ảnh
        BannerModel banner = bannersList.get(position);
        if (banner == null) {
            return;
        }
        holder.imageView.setImageResource(banner.getImageId());
    }

    @Override
    public int getItemCount() {
        return (bannersList != null) ? bannersList.size() : 0;
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgView);
        }
    }
}
