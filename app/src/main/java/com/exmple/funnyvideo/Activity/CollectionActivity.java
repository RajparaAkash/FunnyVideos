package com.exmple.funnyvideo.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exmple.funnyvideo.Fragment.FavoriteFragment;
import com.exmple.funnyvideo.Fragment.MyDownloadFragment;
import com.exmple.funnyvideo.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {

    TabLayout collectionTabLayout;
    ViewPager collectionViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        findViewById(R.id.back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        idBinding();
        setViewPagerMethod();
    }

    public void setViewPagerMethod() {
        setupViewPager(collectionViewpager);
        collectionTabLayout.setupWithViewPager(collectionViewpager);

        collectionTabLayout.getTabAt(0).setCustomView(getHeaderView());
        collectionTabLayout.getTabAt(1).setCustomView(getHeaderView());

        for (int i = 0; i < collectionTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = collectionTabLayout.getTabAt(i);

            TextView tabTxt = tab.getCustomView().findViewById(R.id.tabTxt);
            LinearLayout tabLay = tab.getCustomView().findViewById(R.id.tabLay);

            if (i == 0) {
                tabTxt.setTextColor(getResources().getColor(R.color.white));
                tabLay.setBackgroundResource(R.drawable.tab_bg_selected);
                tabTxt.setText("My Download");
            } else {
                tabTxt.setTextColor(getResources().getColor(R.color.tab_unselect));
                tabTxt.setText("Favorite");
                tabLay.setBackgroundResource(R.drawable.tab_bg_unselected);
            }
        }

        collectionTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                TextView tabTxt = tab.getCustomView().findViewById(R.id.tabTxt);
                tabTxt.setTextColor(getResources().getColor(R.color.white));

                LinearLayout tabLay = tab.getCustomView().findViewById(R.id.tabLay);
                tabLay.setBackgroundResource(R.drawable.tab_bg_selected);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tabTxt = tab.getCustomView().findViewById(R.id.tabTxt);
                tabTxt.setTextColor(getResources().getColor(R.color.tab_unselect));
                tabTxt.setVisibility(View.VISIBLE);

                LinearLayout tabLay = tab.getCustomView().findViewById(R.id.tabLay);
                tabLay.setBackgroundResource(R.drawable.tab_bg_unselected);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private View getHeaderView() {
        return ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.layout_custom_tab, null, false);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MyDownloadFragment(), "My Download");
        adapter.addFragment(new FavoriteFragment(), "Favorite");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @org.jetbrains.annotations.Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void idBinding() {
        collectionTabLayout = (TabLayout) findViewById(R.id.collectionTabLayout);
        collectionViewpager = (ViewPager) findViewById(R.id.collectionViewpager);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}