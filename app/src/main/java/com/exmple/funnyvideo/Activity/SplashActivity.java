package com.exmple.funnyvideo.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class SplashActivity extends AppCompatActivity {

    public static ArrayList<Data> randomVideoList;
    public static int totalpages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        randomVideoList = new ArrayList<>();
        getAllVideo();
    }

    private void getAllVideo() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://159.65.146.129/api/videos/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<RecyclerData> call = retrofitAPI.getAllCourses(1);

        call.enqueue(new Callback<RecyclerData>() {
            @Override
            public void onResponse(@NonNull Call<RecyclerData> call, @NonNull Response<RecyclerData> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    ArrayList<Data> templist = (ArrayList<Data>) response.body().getDataArray();
                    randomVideoList.addAll(templist);
                    totalpages = response.body().getTotalpages();

                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecyclerData> call, @NonNull Throwable t) {
                Toast.makeText(SplashActivity.this, "Fail to get data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}