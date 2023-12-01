package com.exmple.funnyvideo.Activity;

import static com.exmple.funnyvideo.Activity.SplashActivity.randomVideoList;
import static com.exmple.funnyvideo.Activity.SplashActivity.totalpages;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.exmple.funnyvideo.LiveVideo.Adapter.RecyclerViewAdapter;
import com.exmple.funnyvideo.LiveVideo.EndlessRecyclerViewScrollListener;
import com.exmple.funnyvideo.LiveVideo.Model.Data;
import com.exmple.funnyvideo.LiveVideo.Model.RecyclerData;
import com.exmple.funnyvideo.LiveVideo.RetrofitAPI;
import com.exmple.funnyvideo.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedActivity extends AppCompatActivity {

    private RecyclerViewAdapter recyclerViewAdapter;
    private ProgressBar progressBar;
    RecyclerView randomListRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        randomListRV = findViewById(R.id.randomListRV);
        progressBar = findViewById(R.id.progressBar);

        LinearLayoutManager manager = new LinearLayoutManager(FeedActivity.this);

        randomListRV.setLayoutManager(manager);
        recyclerViewAdapter = new RecyclerViewAdapter(randomVideoList, FeedActivity.this);
        randomListRV.setAdapter(recyclerViewAdapter);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(randomListRV);

        randomListRV.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onFirstVisibleItem(int i) {
            }

            @Override
            public void onLoadMore(int i) {
                if (totalpages >= i) {
                    getAllVideo(i);
                }
            }
        });
    }

    private void getAllVideo(int i) {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://159.65.146.129/api/videos/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<RecyclerData> call = retrofitAPI.getAllCourses(i);

        call.enqueue(new Callback<RecyclerData>() {
            @Override
            public void onResponse(@NonNull Call<RecyclerData> call, @NonNull Response<RecyclerData> response) {

                Log.e("Akash", "onResponse: " + response);
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    assert response.body() != null;
                    ArrayList<Data> templist = (ArrayList<Data>) response.body().getDataArray();
                    randomVideoList.addAll(templist);
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecyclerData> call, @NonNull Throwable t) {
                Toast.makeText(FeedActivity.this, "Fail to get data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}