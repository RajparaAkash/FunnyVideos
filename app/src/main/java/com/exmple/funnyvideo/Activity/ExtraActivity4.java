package com.exmple.funnyvideo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.exmple.funnyvideo.R;

public class ExtraActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_4);

        findViewById(R.id.extra4_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExtraActivity4.this, ExtraActivity5.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}