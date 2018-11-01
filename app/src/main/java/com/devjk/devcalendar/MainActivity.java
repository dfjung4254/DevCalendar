package com.devjk.devcalendar;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.devjk.devcalendar.fragment.DailyFragment;
import com.devjk.devcalendar.fragment.MonthlyFragment;
import com.devjk.devcalendar.fragment.WeeklyFragment;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    MonthlyFragment monthlyFragment;
    WeeklyFragment weeklyFragment;
    DailyFragment dailyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        monthlyFragment = new MonthlyFragment();
        weeklyFragment = new WeeklyFragment();
        dailyFragment = new DailyFragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.MainActivity_Toolbar_toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.MainActivity_TabLayout_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.MainActivity_ViewPager_viewPager);

        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem((tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter{

        private int tabCount;

        public TabPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return monthlyFragment;
                case 1:
                    return weeklyFragment;
                case 2:
                    return dailyFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

}
