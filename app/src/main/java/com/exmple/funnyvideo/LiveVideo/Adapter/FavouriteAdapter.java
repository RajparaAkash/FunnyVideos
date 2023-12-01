package com.exmple.funnyvideo.LiveVideo.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.exmple.funnyvideo.LiveVideo.Common;
import com.exmple.funnyvideo.LiveVideo.Model.Data;
import com.exmple.funnyvideo.LiveVideo.database.DBHelper;
import com.exmple.funnyvideo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.RecyclerViewHolder> {

    Context contextt;
    private Clickinterface clickinterface;
    ArrayList<Data> arrayList1;

    public FavouriteAdapter(ArrayList<Data> arrayList1, Context contextt, Clickinterface clickinterface) {
        this.arrayList1 = arrayList1;
        this.contextt = contextt;
        this.clickinterface = clickinterface;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Data dat = arrayList1.get(position);

        holder.texttitle.setText(dat.getTitle());

        Glide.with(contextt)
                .load("http://159.65.146.129/EKYPBDF/f776101c/" + dat.getID() + ".webp")
                .into(holder.imageView);

        DBHelper dbHelper = new DBHelper(contextt);

        holder.delete.setOnClickListener(view -> {

            Dialog myDialog = new Dialog(contextt);
            myDialog.requestWindowFeature(1);
            myDialog.setContentView(R.layout.dialog_unfavorite_video);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            myDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            myDialog.show();

            myDialog.findViewById(R.id.close_img).setOnClickListener(v -> {
                myDialog.dismiss();
            });

            myDialog.findViewById(R.id.ok_text).setOnClickListener(v -> {
                myDialog.dismiss();
                addFavoriteItem(dat);
                dbHelper.deleteData(dat.getID());
                arrayList1.remove(arrayList1.get(position));
                notifyDataSetChanged();
                clickinterface.onClick(arrayList1.size());
            });
        });
    }

    @Override
    public int getItemCount() {
        return arrayList1.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView texttitle;
        ImageView delete;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            texttitle = itemView.findViewById(R.id.texttitle);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    void addFavoriteItem(Data item) {
        String likeListData = Common.getPreferenceString(contextt, "likeList", "");
        List<String> likeList = new ArrayList<String>();
        if (!likeListData.isEmpty()) {
            likeList.addAll(Arrays.asList(likeListData.split(",")));
        }
        if (likeList.contains(item.getID())) {
            likeList.remove(item.getID());
            DBHelper dbHelper = new DBHelper(contextt);
            dbHelper.deleteData(item.getID());

        } else {
            likeList.add(item.getID());
        }
        String str = String.join(",", likeList);
        Common.setPreferenceString(contextt, "likeList", str);
        notifyDataSetChanged();
    }


    public interface Clickinterface {
        void onClick(int pos);
    }
}
