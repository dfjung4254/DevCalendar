package com.devjk.devcalendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.devjk.devcalendar.fragment.DailyFragment;
import com.devjk.devcalendar.fragment.MonthlyFragment;
import com.devjk.devcalendar.fragment.WeeklyFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    return new MonthlyFragment();
                case 1:
                    return new WeeklyFragment();
                case 2:
                    return new DailyFragment();
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
