package com.exmple.funnyvideo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.exmple.funnyvideo.Model.AppPrefrence;
import com.exmple.funnyvideo.MyUtils.MyUtil;
import com.exmple.funnyvideo.R;

public class ToolsActivity extends AppCompatActivity {

    ImageView crop_video;
    ImageView trim_video;
    ImageView merge_video;
    ImageView filter_video;
    ImageView compress_video;
    ImageView extrect_video;
    ImageView speed_video;
    ImageView smotion_video;
    ImageView reverse_video;
    AppPrefrence appPrefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        findViewById(R.id.back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        appPrefrence = new AppPrefrence(this);

        crop_video = findViewById(R.id.crop_video);
        trim_video = findViewById(R.id.trim_video);
        merge_video = findViewById(R.id.merge_video);
        filter_video = findViewById(R.id.filter_video);
        compress_video = findViewById(R.id.compress_video);
        extrect_video = findViewById(R.id.extrect_video);
        speed_video = findViewById(R.id.speed_video);
        smotion_video = findViewById(R.id.smotion_video);
        reverse_video = findViewById(R.id.reverse_video);


        crop_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUtil.toolPos = 1;
                appPrefrence.set_select("one");
                startActivity(new Intent(ToolsActivity.this, VideoListActivity.class));
            }
        });

        trim_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUtil.toolPos = 2;
                appPrefrence.set_select("one");
                startActivity(new Intent(ToolsActivity.this, VideoListActivity.class));
            }
        });

        merge_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUtil.toolPos = 3;
                appPrefrence.set_select("two");
                startActivity(new Intent(ToolsActivity.this, VideoListActivity.class));
            }
        });

        filter_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtil.toolPos = 4;
                appPrefrence.set_select("one");
                startActivity(new Intent(ToolsActivity.this, VideoListActivity.class));
            }
        });

        compress_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtil.toolPos = 5;
                appPrefrence.set_select("one");
                startActivity(new Intent(ToolsActivity.this, VideoListActivity.class));
            }
        });

        extrect_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtil.toolPos = 6;
                appPrefrence.set_select("one");
                startActivity(new Intent(ToolsActivity.this, VideoListActivity.class));
            }
        });

        speed_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtil.toolPos = 7;
                appPrefrence.set_select("one");
                startActivity(new Intent(ToolsActivity.this, VideoListActivity.class));
            }
        });

        smotion_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtil.toolPos = 8;
                appPrefrence.set_select("one");
                startActivity(new Intent(ToolsActivity.this, VideoListActivity.class));
            }
        });

        reverse_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtil.toolPos = 9;
                appPrefrence.set_select("one");
                startActivity(new Intent(ToolsActivity.this, VideoListActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}