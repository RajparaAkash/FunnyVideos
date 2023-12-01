package com.exmple.funnyvideo.Fragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exmple.funnyvideo.LiveVideo.Adapter.FavouriteAdapter;
import com.exmple.funnyvideo.LiveVideo.Model.Data;
import com.exmple.funnyvideo.LiveVideo.database.DBHelper;
import com.exmple.funnyvideo.R;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {

    private TextView noDataFoundTxt;
    private RecyclerView favouriteRV;
    FavouriteAdapter favouriteAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        noDataFoundTxt = (TextView) view.findViewById(R.id.noDataFoundTxt);
        favouriteRV = (RecyclerView) view.findViewById(R.id.favouriteRV);

        favouriteData();

        return view;
    }

    public void favouriteData() {

        DBHelper dbHelper = new DBHelper(getContext());
        Cursor cursor = dbHelper.readdata();

        ArrayList<Data> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(1);
            String title = cursor.getString(2);
            String category = cursor.getString(3);
            int views = cursor.getInt(4);
            int likes = cursor.getInt(5);

            GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
            favouriteRV.setLayoutManager(manager);
            Data dat = new Data(id, title, category, views, likes);
            arrayList.add(dat);
        }

        if (arrayList.size() > 0) {
            favouriteAdapter = new FavouriteAdapter(arrayList, getContext(), pos -> {
                if (arrayList.size() == 0) {
                    noDataFoundTxt.setVisibility(View.VISIBLE);
                } else {
                    noDataFoundTxt.setVisibility(View.GONE);
                }
            });
            favouriteRV.setAdapter(favouriteAdapter);
        } else {
            noDataFoundTxt.setVisibility(View.VISIBLE);
        }
    }
}