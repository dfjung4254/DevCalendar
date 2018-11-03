package com.devjk.devcalendar.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.devjk.devcalendar.MainActivity;
import com.devjk.devcalendar.R;
import com.devjk.devcalendar.ScheduleActivity;
import com.devjk.devcalendar.classfile.DayCalculator;
import com.devjk.devcalendar.classfile.ScheduleDBHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class MonthlyFragment extends Fragment {

    public static final int CALENDARSIZE = 42;
    Button btn_left;
    Button btn_right;
    TextView text_month;
    TextView text_year;
    GridView gridView;
    DayCalculator dayCalculator = new DayCalculator();
    int curYear;
    int curMonth;
    int curDate;
    public DayAdapter dayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly, container, false);

        curYear = MainActivity.currentYear;
        curMonth = MainActivity.currentMonth;
        curDate = MainActivity.currentDate;

        btn_left = (Button) view.findViewById(R.id.MonthlyFragment_Button_left);
        btn_right = (Button) view.findViewById(R.id.MonthlyFragment_Button_right);
        text_month = (TextView) view.findViewById(R.id.MonthlyFragment_TextView_month);
        text_year = (TextView) view.findViewById(R.id.MonthlyFragment_TextView_year);
        gridView = (GridView) view.findViewById(R.id.MonthlyFragment_GridView_gridView);
        dayAdapter = new DayAdapter(getContext(), curYear, curMonth, curDate);
        gridView.setAdapter(dayAdapter);
        text_month.setText(curMonth + " 월");
        text_year.setText(String.valueOf(curYear));

        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curMonth == 1){
                    curYear--;
                    curMonth = 12;
                    text_year.setText(String.valueOf(curYear));
                }else{
                    curMonth--;
                }
                text_month.setText(curMonth + " 월");
                dayAdapter = new DayAdapter(getContext(), curYear, curMonth, curDate);
                gridView.setAdapter(dayAdapter);
            }
        });
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curMonth == 12){
                    curYear++;
                    curMonth = 1;
                    text_year.setText(String.valueOf(curYear));
                }else{
                    curMonth++;
                }
                text_month.setText(curMonth + " 월");
                dayAdapter = new DayAdapter(getContext(), curYear, curMonth, curDate);
                gridView.setAdapter(dayAdapter);
            }
        });

        return view;
    }

    public class DayAdapter extends BaseAdapter{

        //set member variables
        Context context;
        int year;
        int month;
        int date;
        String dayArr[];
        String scheduleArr[];

        public DayAdapter(Context context, int year, int month, int date){
            //constructor.
            this.context = context;
            this.year = year;
            this.month = month;
            this.date = date;
            dayArr = new String[CALENDARSIZE];
            scheduleArr = new String[CALENDARSIZE];
            //알고리즘을 구현해서 각각의 배열에 들어가는 숫자들을 다 설정해준다.
            dayCalculator.calMonth(year, month, dayArr);
            resetSchedule();

        }
        public void resetSchedule(){
            //알고리즘을 구현해서 각각의 배열에 들어가는 숫자들을 다 설정해준다.
            SQLiteDatabase db = ScheduleDBHelper.getInstance(getContext()).getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + ScheduleDBHelper.tableName + "" +
                    " WHERE year="+year+" AND month="+month+
                    " ORDER BY date ASC", null);
            for(int i = 0; i < CALENDARSIZE; i++){
                //DB search and setting scheduleArr
                //dayArr setting
                //scheduleArr1 setting
                //db조회 있으면 넣고 아니면 ""
                int curDate = Integer.parseInt(dayArr[i]);
                boolean isData = false;
                //db 조회할 공간---------------------------
                for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                    Log.d(MainActivity.MYLOG, "DB조회 : " + cursor.getInt(3) + "/" + cursor.getString(4));
                    if(!((i < 8 && curDate > 20)||(i > 32 && curDate < 12))
                            && curDate == cursor.getInt(3)){
                        //일치하는 데이터 찾음.
                        isData = true;
                        scheduleArr[i] = cursor.getString(4);
                        break;
                    }
                }
                //------------------------------------------
                if(!isData){
                    scheduleArr[i] = "";
                }
            }
        }

        @Override
        public int getCount() {
            return CALENDARSIZE;
        }

        @Override
        public Object getItem(int pos) {
            return null;
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(final int pos, View view, ViewGroup parent) {
            final int curDate = Integer.parseInt(dayArr[pos]);
            if(view == null){
                    view = getLayoutInflater().inflate(R.layout.calendar_layout, parent, false);
            }
            TextView date = (TextView) view.findViewById(R.id.CalendarLayout_TextView_date);
            TextView schedule = (TextView) view.findViewById(R.id.CalendarLayout_TextView_schedule1);
            date.setText(dayArr[pos]);
            schedule.setText(scheduleArr[pos]);
            if(pos % 7 == 0){
                //일요일
                date.setTextColor(Color.parseColor("#ff0000"));
            }
            if(pos % 7 == 6){
                //토요일
                date.setTextColor(Color.parseColor("#0011ff"));
            }
            boolean isAvailable = true;
            if((pos < 8 && curDate > 20)||(pos > 32 && curDate < 12)){
                //해당 월이 아니라 전월이나 다음월의 숫자는 회색처리하고, false처리.
                date.setTextColor(Color.parseColor("#7e7e7e"));
                isAvailable = false;
            }
            if(scheduleArr[pos].equals("")){
                //스케쥴이 없을 경우 스케쥴라인을 투명처리함.
                schedule.setBackgroundColor(Color.parseColor("#00ffffff"));
            }else{
                //스케쥴이 있을 경우 스케쥴라인을 다른색 처리함.
                Log.d(MainActivity.MYLOG, pos+" 열 에 스케쥴 존재 : " + scheduleArr[pos]);
                schedule.setBackgroundColor(Color.parseColor("#ffbd6b"));
            }

            if(year == MainActivity.currentYear && month == MainActivity.currentMonth && curDate == MainActivity.currentDate && isAvailable) {
                //만약 오늘날짜는 달력에 음영색을 더함.
                view.setBackgroundColor(Color.parseColor("#BEEFD7BA"));
            }
            if(isAvailable){
                //오늘 월에 해당하는 달력날은 스케쥴관리로 넘어갈 수 있음.
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //여기 클릭했을때 해당 스케줄 관리 페이지로 넘어가야함
                        Intent intent = new Intent(getContext(), ScheduleActivity.class);
                        intent.putExtra("year", year);
                        intent.putExtra("month", month);
                        intent.putExtra("date", curDate);
                        intent.putExtra("day", pos % 7);
                        startActivity(intent);
                    }
                });
            }


            return view;
        }
    }

}
