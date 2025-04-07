package com.example.project_ltdd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_ltdd.R;
import com.example.project_ltdd.domain.AdminQuizzDomain;

import java.util.ArrayList;

public class AdminQuizzAdapter extends RecyclerView.Adapter<AdminQuizzAdapter.Viewholder> {
    private static final String TAG = "QuizzAdapter";
    private ArrayList<AdminQuizzDomain> mitems;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public AdminQuizzAdapter(ArrayList<AdminQuizzDomain> mitems, Context mContext) {
        this.mitems = mitems;
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public AdminQuizzAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*View inflator = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_admin_quizz_list, parent, false);*/
        View inflator = mLayoutInflater.inflate(R.layout.view_holder_admin_quizz_list, parent, false);
        return new Viewholder(inflator);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminQuizzAdapter.Viewholder holder, int position) {
        holder.title.setText(mitems.get(position).getTitle());
        holder.content.setText("$"+mitems.get(position).getContent());

        int drawableResourseId=holder.itemView.getResources().
                getIdentifier(mitems.get(position).getPicPath(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(mContext).load(drawableResourseId).into(holder.pic);

        if (position == 0) {
            holder.background_img.setImageResource(R.drawable.bg_admin_quizz_1);
            holder.layout.setBackgroundResource(R.drawable.admin_list_quizz_1);
        } else if (position == 1) {
            holder.background_img.setImageResource(R.drawable.bg_admin_quizz_2);
            holder.layout.setBackgroundResource(R.drawable.admin_list_quizz_2);
        } else if (position == 2) {
            holder.background_img.setImageResource(R.drawable.bg_admin_quizz_3);
            holder.layout.setBackgroundResource(R.drawable.admin_list_quizz_3);
        } else if (position == 3) {
            holder.background_img.setImageResource(R.drawable.bg_admin_quizz_4);
            holder.layout.setBackgroundResource(R.drawable.admin_list_quizz_4);
        } else if (position == 4) {
            holder.background_img.setImageResource(R.drawable.bg_admin_quizz_5);
            holder.layout.setBackgroundResource(R.drawable.admin_list_quizz_5);
        }
    }

    @Override
    public int getItemCount() {
        return mitems.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView title, content;
        ImageView pic, background_img;
        ConstraintLayout layout;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_title_admin_quizz);
            content = itemView.findViewById(R.id.txt_content_admin_quizz);
            pic = itemView.findViewById(R.id.pic_admin_quizz);
            background_img = itemView.findViewById(R.id.bg_img_admin_quizz);
            layout = itemView.findViewById(R.id.admin_main_quizz_layout);
        }

    }
}
