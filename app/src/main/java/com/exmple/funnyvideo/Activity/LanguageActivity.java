package com.exmple.funnyvideo.Activity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.POST_NOTIFICATIONS;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VIDEO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.exmple.funnyvideo.R;

public class LanguageActivity extends AppCompatActivity {

    private final int BELOW_ANDROID_13 = 101;
    private final int ABOVE_ANDROID_13 = 102;

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
        setContentView(R.layout.activity_language);

        idBinding();

        findViewById(R.id.save_language).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissionGranted()) {
                    nextScreen();
                } else {
                    takePermission();
                }
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

    public boolean checkPermissionGranted() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {

            int read_external_storage = ContextCompat.checkSelfPermission(LanguageActivity.this, READ_EXTERNAL_STORAGE);

            return read_external_storage == PackageManager.PERMISSION_GRANTED;

        } else {

            int read_media_video = ContextCompat.checkSelfPermission(LanguageActivity.this, READ_MEDIA_VIDEO);
            int post_notifications = ContextCompat.checkSelfPermission(LanguageActivity.this, POST_NOTIFICATIONS);

            return read_media_video == PackageManager.PERMISSION_GRANTED
                    && post_notifications == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case BELOW_ANDROID_13:
                boolean gotPermission1 = grantResults.length > 0;

                for (int result : grantResults) {
                    gotPermission1 &= result == PackageManager.PERMISSION_GRANTED;
                }

                if (gotPermission1) {
                    nextScreen();
                } else {
                    Toast.makeText(LanguageActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;

            case ABOVE_ANDROID_13:
                boolean gotPermission2 = grantResults.length > 0;

                for (int result : grantResults) {
                    gotPermission2 &= result == PackageManager.PERMISSION_GRANTED;
                }

                if (gotPermission2) {
                    nextScreen();
                } else {
                    Toast.makeText(LanguageActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void nextScreen() {
        startActivity(new Intent(LanguageActivity.this, MainActivity.class));
    }

    private void takePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {

            ActivityCompat.requestPermissions(LanguageActivity.this,
                    new String[]{
                            READ_EXTERNAL_STORAGE}, BELOW_ANDROID_13);
        } else {

            ActivityCompat.requestPermissions(LanguageActivity.this,
                    new String[]{
                            READ_MEDIA_VIDEO,
                            POST_NOTIFICATIONS}, ABOVE_ANDROID_13);
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}