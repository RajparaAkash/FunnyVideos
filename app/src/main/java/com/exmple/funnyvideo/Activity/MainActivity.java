package com.exmple.funnyvideo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.exmple.funnyvideo.Model.AppPrefrence;
import com.exmple.funnyvideo.MyUtils.MyUtil;
import com.exmple.funnyvideo.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ViewPager pager;
    int currentPage = 0;
    int[] top_tools = {R.drawable.tool1, R.drawable.tool2, R.drawable.tool3, R.drawable.tool4};
    Timer timer;
    AppPrefrence appPrefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appPrefrence = new AppPrefrence(this);
        pager = findViewById(R.id.pager);

        findViewById(R.id.see_alll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ToolsActivity.class));
            }
        });

        pager.setAdapter(new CustomPagerAdapter());
        final Handler handler = new Handler();
        final Runnable r1 = new Runnable() {
            @Override
            public void run() {
                if (currentPage == top_tools.length) {
                    currentPage = 0;
                }
                ViewPager viewPager2 = pager;
                int i = currentPage;
                currentPage = i + 1;
                viewPager2.setCurrentItem(i, true);
            }
        };
        Timer timer = new Timer();
        this.timer = timer;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(r1);
            }
        }, 100, 4000);


        findViewById(R.id.setting_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });

        findViewById(R.id.ly_feed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FeedActivity.class));
            }
        });

        findViewById(R.id.ly_collection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CollectionActivity.class));
            }
        });
    }

    public class CustomPagerAdapter extends PagerAdapter {

        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        public Object instantiateItem(ViewGroup viewGroup, int i) {
            ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(MainActivity.this).inflate(R.layout.item_viewpager, viewGroup, false);

            ImageView imageView = (ImageView) viewGroup2.findViewById(R.id.img);
            imageView.setImageResource(top_tools[i]);
            viewGroup.addView(viewGroup2);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (i == 0) {
                        MyUtil.toolPos = 1;
                        appPrefrence.set_select("one");
                        startActivity(new Intent(MainActivity.this, VideoListActivity.class));
                    } else if (i == 1) {
                        MyUtil.toolPos = 2;
                        appPrefrence.set_select("one");
                        startActivity(new Intent(MainActivity.this, VideoListActivity.class));
                    } else if (i == 2) {
                        MyUtil.toolPos = 7;
                        appPrefrence.set_select("one");
                        startActivity(new Intent(MainActivity.this, VideoListActivity.class));
                    } else if (i == 3) {
                        MyUtil.toolPos = 4;
                        appPrefrence.set_select("one");
                        startActivity(new Intent(MainActivity.this, VideoListActivity.class));
                    }
                }
            });

            return viewGroup2;
        }

        @Override
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) obj);
        }

        @Override
        public int getCount() {
            return top_tools.length;
        }
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