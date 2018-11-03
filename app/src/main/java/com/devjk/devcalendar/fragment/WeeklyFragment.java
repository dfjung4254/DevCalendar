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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devjk.devcalendar.MainActivity;
import com.devjk.devcalendar.R;
import com.devjk.devcalendar.ScheduleActivity;
import com.devjk.devcalendar.classfile.DayCalculator;
import com.devjk.devcalendar.classfile.ScheduleDBHelper;

import java.util.HashMap;

public class WeeklyFragment extends Fragment {

    public static final int WEEKSIZE = 7;
    String weeks[] = {"첫째 주", "둘째 주", "셋째 주", "넷째 주", "다섯째 주", "여섯째 주"};
    TextView text_year;
    TextView text_month;
    TextView text_weekNumber;
    Button btn_left;
    Button btn_right;
    GridView gridView;
    DayCalculator dayCalculator = new DayCalculator();
    public DayAdapter dayAdapter;
    int curYear;
    int curMonth;
    int curDate;
    int curWeekIndex;
    int curWeekMaxIndex;

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

        text_year.setText(String.valueOf(curYear));
        text_month.setText(String.valueOf(curMonth));
        curWeekIndex = dayCalculator.calWeekNumber(curYear, curMonth, curDate);
        curWeekMaxIndex = dayCalculator.calWeekMaxIndex(curYear, curMonth);
        text_weekNumber.setText(weeks[curWeekIndex]);

        dayAdapter = new DayAdapter(getContext(), curYear, curMonth, curWeekIndex);
        gridView.setAdapter(dayAdapter);

        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curWeekIndex == 0){
                    if(curMonth == 1){
                        curYear--;
                        text_year.setText(String.valueOf(curYear));
                        curMonth = 12;
                    }else{
                        curMonth--;
                    }
                    text_month.setText(String.valueOf(curMonth));
                    curWeekMaxIndex = dayCalculator.calWeekMaxIndex(curYear, curMonth);
                    curWeekIndex = curWeekMaxIndex;
                }else{
                    curWeekIndex--;
                }
                text_weekNumber.setText(weeks[curWeekIndex]);
                dayAdapter = new DayAdapter(getContext(), curYear, curMonth, curWeekIndex);
                gridView.setAdapter(dayAdapter);
            }
        });
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curWeekIndex == curWeekMaxIndex){
                    if(curMonth == 12){
                        curYear++;
                        text_year.setText(String.valueOf(curYear));
                        curMonth = 1;
                    }else{
                        curMonth++;
                    }
                    text_month.setText(String.valueOf(curMonth));
                    curWeekMaxIndex = dayCalculator.calWeekMaxIndex(curYear, curMonth);
                    curWeekIndex = 0;
                }else{
                    curWeekIndex++;
                }
                text_weekNumber.setText(weeks[curWeekIndex]);
                dayAdapter = new DayAdapter(getContext(), curYear, curMonth, curWeekIndex);
                gridView.setAdapter(dayAdapter);
            }
        });

        return view;
    }

    public class DayAdapter extends BaseAdapter{

        Context context;
        int year;
        int month;
        int weekIndex;
        int weekMaxIndex;
        int targetDate[] = new int[2]; // 0 : 한 주의 시작날짜, 1 : 한 주의 마지막 날짜.
        int size;
        String dateArray[];
        String dayArray[];
        String titleArray[];
        String contentsArray[];

        public DayAdapter(Context context, int year, int month, int weekIndex){
            //초기화.
            this.context = context;
            this.year = year;
            this.month = month;
            this.weekIndex = weekIndex;
            this.weekMaxIndex = dayCalculator.calWeekMaxIndex(year, month);
            size = getCount();
            dateArray = new String[size];
            dayArray = new String[size];
            titleArray = new String[size];
            contentsArray = new String[size];

            //해당 주에 맞는 날짜를 계산해서 입력해야 한다.
            dayCalculator.calWeek(year, month, weekIndex, dateArray, dayArray, targetDate);

            //DB조회
            resetSchedule();
        }

        public void resetSchedule(){
            //DB조회해서 해당 주에 해당하는 날짜들의 title과 contents 배열에 입력한다.
            SQLiteDatabase db = ScheduleDBHelper.getInstance(getContext()).getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + ScheduleDBHelper.tableName + "" +
                    " WHERE year="+year+" AND month="+month+
                    " AND date>="+targetDate[0]+" AND date<="+targetDate[1]+
                    " ORDER BY date ASC", null);
            for(int i = 0; i < size; i++){
                //DB search and setting scheduleArr
                //dayArr setting
                //scheduleArr1 setting
                //db조회 있으면 넣고 아니면 ""
                int curDate = Integer.parseInt(dateArray[i]);
                boolean isData = false;
                //db 조회할 공간---------------------------
                for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                    Log.d(MainActivity.MYLOG, "DB조회 : " + cursor.getInt(3) + "/" + cursor.getString(4));
                    if(curDate == cursor.getInt(3)){
                        //일치하는 데이터 찾음.
                        isData = true;
                        titleArray[i] = cursor.getString(4);
                        contentsArray[i] = cursor.getString(5);
                        break;
                    }
                }
                //------------------------------------------
                if(!isData){
                    titleArray[i] = "";
                    contentsArray[i] = "";
                }
            }
        }


        @Override
        public int getCount() {
            if(weekIndex == 0){
                return dayCalculator.calFirstWeekDaysNum(year, month);
            }else if(weekIndex == weekMaxIndex){
                return dayCalculator.calLastWeekDaysNum(year, month);
            }else{
                return WEEKSIZE;
            }
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
        public View getView(final int pos, View view, ViewGroup parent) {
            Log.d(MainActivity.MYLOG, "curDate : "+dateArray[pos]+" pos : "+pos);
            final int curDate = Integer.parseInt(dateArray[pos]);
            if(view == null){
                view = getLayoutInflater().inflate(R.layout.week_layout, parent, false);
            }
            TextView date = view.findViewById(R.id.WeekLayout_TextView_date);
            TextView day = view.findViewById(R.id.WeekLayout_TextView_day);
            TextView title = view.findViewById(R.id.WeekLayout_TextView_title);
            TextView contents = view.findViewById(R.id.WeekLayout_TextView_contents);
            LinearLayout topBar = view.findViewById(R.id.WeekLayout_LinearLayout_topBar);

            date.setText(dateArray[pos]);
            day.setText(dayArray[pos]);
            title.setText(titleArray[pos]);
            contents.setText(contentsArray[pos]);

            if(dayArray[pos].equals("일")){
                //일요일
                date.setTextColor(Color.parseColor("#ff0000"));
                day.setTextColor(Color.parseColor("#ff0000"));
            }
            if(dayArray[pos].equals("토")){
                //토요일
                date.setTextColor(Color.parseColor("#0011ff"));
                day.setTextColor(Color.parseColor("#0011ff"));
            }
            if(year == MainActivity.currentYear && month == MainActivity.currentMonth && curDate == MainActivity.currentDate) {
                //만약 오늘날짜는 달력에 음영색을 더함.
                view.setBackgroundColor(Color.parseColor("#BEEFD7BA"));
            }
            if(!(titleArray[pos].equals("") && contentsArray[pos].equals(""))){
                topBar.setBackgroundColor(Color.parseColor("#ffbd6b"));
            }else{
                topBar.setBackgroundColor(Color.parseColor("#00ffffff"));
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //여기 클릭했을때 해당 스케줄 관리 페이지로 넘어가야함
                    Intent intent = new Intent(getContext(), ScheduleActivity.class);
                    intent.putExtra("year", year);
                    intent.putExtra("month", month);
                    intent.putExtra("date", curDate);
                    intent.putExtra("day", MainActivity.weekMap.get(dayArray[pos]));
                    startActivity(intent);
                }
            });

            return view;
        }
    }

}