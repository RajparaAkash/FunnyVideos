package com.exmple.funnyvideo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.exmple.funnyvideo.R;

public class LanguageSelectActivity extends AppCompatActivity {

    LinearLayout lan_layout_1;
    LinearLayout lan_layout_2;
    LinearLayout lan_layout_3;
    LinearLayout lan_layout_4;
    LinearLayout lan_layout_5;
    LinearLayout lan_layout_6;
    LinearLayout lan_layout_7;

    ImageView lan_select_img_1;
    ImageView lan_select_img_2;
    ImageView lan_select_img_3;
    ImageView lan_select_img_4;
    ImageView lan_select_img_5;
    ImageView lan_select_img_6;
    ImageView lan_select_img_7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);

        idBinding();

        findViewById(R.id.back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.save_language).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        lan_layout_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAll();
                lan_select_img_1.setImageResource(R.drawable.language_selected);
            }
        });

        lan_layout_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAll();
                lan_select_img_2.setImageResource(R.drawable.language_selected);
            }
        });

        lan_layout_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAll();
                lan_select_img_3.setImageResource(R.drawable.language_selected);
            }
        });

        lan_layout_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAll();
                lan_select_img_4.setImageResource(R.drawable.language_selected);
            }
        });

        lan_layout_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAll();
                lan_select_img_5.setImageResource(R.drawable.language_selected);
            }
        });

        lan_layout_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAll();
                lan_select_img_6.setImageResource(R.drawable.language_selected);
            }
        });

        lan_layout_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAll();
                lan_select_img_7.setImageResource(R.drawable.language_selected);
            }
        });
    }

    private void unSelectAll() {
        lan_select_img_1.setImageResource(R.drawable.language_unselected);
        lan_select_img_2.setImageResource(R.drawable.language_unselected);
        lan_select_img_3.setImageResource(R.drawable.language_unselected);
        lan_select_img_4.setImageResource(R.drawable.language_unselected);
        lan_select_img_5.setImageResource(R.drawable.language_unselected);
        lan_select_img_6.setImageResource(R.drawable.language_unselected);
        lan_select_img_7.setImageResource(R.drawable.language_unselected);
    }

    private void idBinding() {
        lan_layout_1 = (LinearLayout) findViewById(R.id.lan_layout_1);
        lan_layout_2 = (LinearLayout) findViewById(R.id.lan_layout_2);
        lan_layout_3 = (LinearLayout) findViewById(R.id.lan_layout_3);
        lan_layout_4 = (LinearLayout) findViewById(R.id.lan_layout_4);
        lan_layout_5 = (LinearLayout) findViewById(R.id.lan_layout_5);
        lan_layout_6 = (LinearLayout) findViewById(R.id.lan_layout_6);
        lan_layout_7 = (LinearLayout) findViewById(R.id.lan_layout_7);

        lan_select_img_1 = (ImageView) findViewById(R.id.lan_select_img_1);
        lan_select_img_2 = (ImageView) findViewById(R.id.lan_select_img_2);
        lan_select_img_3 = (ImageView) findViewById(R.id.lan_select_img_3);
        lan_select_img_4 = (ImageView) findViewById(R.id.lan_select_img_4);
        lan_select_img_5 = (ImageView) findViewById(R.id.lan_select_img_5);
        lan_select_img_6 = (ImageView) findViewById(R.id.lan_select_img_6);
        lan_select_img_7 = (ImageView) findViewById(R.id.lan_select_img_7);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}