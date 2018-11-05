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

    private TextView text_year;
    private TextView text_month;
    private TextView text_date;
    private TextView text_day;
    private TextView text_title;
    private TextView text_contents;
    private  LinearLayout linearLayout;
    private Button btn_left;
    private Button btn_right;
    private DayCalculator dayCalculator;
    private int curYear;
    private int curMonth;
    private int curDate;
    private String curDay;
    private String title;
    private String contents;
    private boolean isOnCreated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        curYear = MainActivity.currentYear;
        curMonth = MainActivity.currentMonth;
        curDate = MainActivity.currentDate;
        dayCalculator = DayCalculator.getInstance();
        curDay = dayCalculator.calDay(curYear, curMonth, curDate);
        isOnCreated = true;

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
        //2018.11.06 00:20 수정사항.
        //오류 수정부분, 기존 제출과제에서는 앱을 켜자마자 바로 Monthly탭에서
        //Schedule을 만들었을 때 NullPointer오류가 발생하면서 앱이 종료되는 버그 발견.
        //이유는 DailyFragment 탭을 열지 않았으므로 DailyFragment의 OnCreateView 메서드가 실행이 안된상태
        //에서 searchDB 메서드의 text_title에 접근하려하여 NullPointer.
        //해결책으로 DailyFragment의 전역변수로 isOnCreated를 false로 추가.
        //OnCreateView가 실행되면 true로 전환하여 단 한번도 DailyFragment가 OnCreatedView로 실행이 안되었을때
        //searchDB메서드를 실행하지 않고 바로 반환한다.
        if(isOnCreated == false){
            return;
        }
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
