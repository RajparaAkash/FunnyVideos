package com.exmple.funnyvideo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.exmple.funnyvideo.R;

public class ExtraActivity5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_5);

        findViewById(R.id.extra5_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExtraActivity5.this, LanguageActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}