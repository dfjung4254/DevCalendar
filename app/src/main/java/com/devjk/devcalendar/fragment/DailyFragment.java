package com.devjk.devcalendar.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devjk.devcalendar.MainActivity;
import com.devjk.devcalendar.R;
import com.devjk.devcalendar.ScheduleActivity;
import com.devjk.devcalendar.classfile.DayCalculator;
import com.devjk.devcalendar.classfile.ScheduleDBHelper;

import java.util.HashMap;

public class DailyFragment extends Fragment {

    TextView text_year;
    TextView text_month;
    TextView text_date;
    TextView text_day;
    TextView text_title;
    TextView text_contents;
    LinearLayout linearLayout;
    Button btn_left;
    Button btn_right;
    DayCalculator dayCalculator = new DayCalculator();
    int curYear;
    int curMonth;
    int curDate;
    String curDay;
    String title;
    String contents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        curYear = MainActivity.currentYear;
        curMonth = MainActivity.currentMonth;
        curDate = MainActivity.currentDate;
        curDay = dayCalculator.calDay(curYear, curMonth, curDate);

        text_year = (TextView) view.findViewById(R.id.DailyFragment_TextView_year);
        text_month = (TextView) view.findViewById(R.id.DailyFragment_TextView_month);
        text_date = (TextView) view.findViewById(R.id.DailyFragment_TextView_date);
        text_day = (TextView) view.findViewById(R.id.DailyFragment_TextView_day);
        text_title = (TextView) view.findViewById(R.id.DailyFragment_TextView_title);
        text_contents = (TextView) view.findViewById(R.id.DailyFragment_TextView_contents);
        linearLayout = (LinearLayout) view.findViewById(R.id.DailyFragment_LinearLayout_linearLayout); //onclicklistener달아야함.
        btn_left = (Button) view.findViewById(R.id.DailyFragment_Button_left);
        btn_right = (Button) view.findViewById(R.id.DailyFragment_Button_right);


        searchDB();
        render();

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ScheduleActivity.class);
                intent.putExtra("year", curYear);
                intent.putExtra("month", curMonth);
                intent.putExtra("date", curDate);
                intent.putExtra("day", MainActivity.weekMap.get(curDay));
                startActivity(intent);
            }
        });

        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curDate == 1){
                    if(curMonth == 1){
                        curYear--;
                        curMonth = 12;
                    }else{
                        curMonth--;
                    }
                    dayCalculator.setFeb(curYear, curMonth);
                    curDate = dayCalculator.arr_month[curMonth];
                }else{
                    curDate--;
                }
                searchDB();
                render();
            }
        });

        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayCalculator.setFeb(curYear, curMonth);
                if(curDate == dayCalculator.arr_month[curMonth]){
                    if(curMonth == 12){
                        curYear++;
                        curMonth = 1;
                    }else{
                        curMonth++;
                    }
                    curDate = 1;
                }else{
                    curDate++;
                }
                searchDB();
                render();
            }
        });

        return view;
    }

    public void render(){
        text_year.setText(String.valueOf(curYear));
        text_month.setText(String.valueOf(curMonth));
        text_date.setText(String.valueOf(curDate));
        text_day.setText(curDay);

    }

    public void searchDB(){
        //DB검색 해서 title과 contents값 갱신
        SQLiteDatabase db = ScheduleDBHelper.getInstance(getContext()).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ScheduleDBHelper.tableName + "" +
                " WHERE year="+curYear+" AND month="+curMonth+" AND date="+curDate, null);
        if(cursor.getCount() == 0){
            //검색결과 없음
            title = "새 일정을 등록하세요.";
            contents = "";
        }else{
            //검색결과값 대입.
            while(cursor.moveToNext()){
                title = cursor.getString(4);
                contents = cursor.getString(5);
            }
        }
        //render
        text_title.setText(title);
        text_contents.setText(contents);
    }

}
