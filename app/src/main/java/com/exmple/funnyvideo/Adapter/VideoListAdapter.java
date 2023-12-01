package com.exmple.funnyvideo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.exmple.funnyvideo.Model.MediaPojo;
import com.exmple.funnyvideo.R;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MediaPojo> files;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private OnVideoItemSelectedListener onVideoItemSelectedListener;


    public interface OnVideoItemSelectedListener {
        void onVideoSelected(int position);
    }

    public VideoListAdapter(Context context, ArrayList<MediaPojo> files, OnVideoItemSelectedListener listener) {
        this.context = context;
        this.files = files;
        this.onVideoItemSelectedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.items_file_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        MediaPojo fileItem = files.get(position);
        try {
            Glide.with(context)
                    .load(fileItem.getPath())
                    .placeholder(R.drawable.logo_squre)
                    .into(viewHolder.imageView);
        } catch (Exception ex) {
        }

        viewHolder.itemView.setOnClickListener(v -> {
            if (selectedPosition != position) {
                selectedPosition = position;
                notifyDataSetChanged();
                if (onVideoItemSelectedListener != null) {
                    onVideoItemSelectedListener.onVideoSelected(position);
                }
            }
        });


        boolean isSelected = selectedPosition == position;
        viewHolder.itemView.setSelected(isSelected);
        viewHolder.rightLayout.setVisibility(isSelected ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return files == null ? 0 : files.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        LinearLayout rightLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.rightLayout = (LinearLayout) itemView.findViewById(R.id.rightLayout);
        }
    }
}