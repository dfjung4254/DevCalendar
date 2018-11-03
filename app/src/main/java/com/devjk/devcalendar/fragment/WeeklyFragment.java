package com.devjk.devcalendar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.devjk.devcalendar.MainActivity;
import com.devjk.devcalendar.R;
import com.devjk.devcalendar.classfile.DayCalculator;

import org.w3c.dom.Text;

public class WeeklyFragment extends Fragment {

    public static final int WEEKSIZE = 7;
    TextView text_year;
    TextView text_month;
    TextView text_weekNumber;
    Button btn_left;
    Button btn_right;
    GridView gridView;
    DayCalculator dayCalculator = new DayCalculator();
    DayAdapter dayAdapter;
    int curYear;
    int curMonth;
    int curDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly, container, false);

        curYear = MainActivity.currentYear;
        curMonth = MainActivity.currentMonth;
        curDate = MainActivity.currentDate;

        text_year = (TextView) view.findViewById(R.id.WeeklyFragment_TextView_year);
        text_month = (TextView) view.findViewById(R.id.WeeklyFragment_TextView_month);
        text_weekNumber = (TextView) view.findViewById(R.id.WeeklyFragment_TextView_weekNumber);
        btn_left = (Button) view.findViewById(R.id.WeeklyFragment_Button_left);
        btn_right = (Button) view.findViewById(R.id.WeeklyFragment_Button_right);
        gridView = (GridView) view.findViewById(R.id.WeeklyFragment_GridView_gridView);
        dayAdapter = new DayAdapter(getContext(), curYear, curMonth, curDate);

        text_year.setText(curYear);
        text_month.setText(curMonth);
        text_weekNumber


        return view;
    }

    public class DayAdapter extends BaseAdapter{

        Context context;
        int year;
        int month;
        int date;

        public DayAdapter(Context context, int year, int month, int date){
            this.context = context;
            this.year = year;
            this.month = month;
            this.date = date;

            //해당 주에 맞는 날짜를 계산해서 입력해야 한다.

        }

        @Override
        public int getCount() {
            return WEEKSIZE;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int pos, View view, ViewGroup parent) {

            if(view == null){
                view = getLayoutInflater().inflate(R.layout.week_layout, parent, false);
            }
            TextView date = view.findViewById(R.id.WeekLayout_TextView_date);
            TextView day = view.findViewById(R.id.WeekLayout_TextView_day);
            TextView title = view.findViewById(R.id.WeekLayout_TextView_title);
            TextView contents = view.findViewById(R.id.WeekLayout_TextView_contents);

            return null;
        }
    }

}