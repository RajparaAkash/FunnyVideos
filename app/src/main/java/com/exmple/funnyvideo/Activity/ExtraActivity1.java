package com.exmple.funnyvideo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.exmple.funnyvideo.R;

public class ExtraActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_1);

        findViewById(R.id.extra1_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExtraActivity1.this, ExtraActivity2.class));
            }
        });
    }

    private long BackPressedTime;

    @Override
    public void onBackPressed() {
        if (BackPressedTime + 2000 > System.currentTimeMillis()) {
            try {
                finishAffinity();
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        BackPressedTime = System.currentTimeMillis();
    }
}