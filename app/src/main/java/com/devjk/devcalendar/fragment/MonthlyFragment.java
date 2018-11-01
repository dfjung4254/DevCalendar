package com.devjk.devcalendar.fragment;

import android.content.Context;
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

import com.devjk.devcalendar.R;
import com.devjk.devcalendar.classfile.DayCalculator;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class MonthlyFragment extends Fragment {

    public static final int CALENDARSIZE = 42;
    Button btn_left;
    Button btn_right;
    TextView text_month;
    GridView gridView;
    DayCalculator dayCalculator = new DayCalculator();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly, container, false);

        int curYear = 2018;
        int curMonth = 11;
        int curDate = 1;

        btn_left = (Button) view.findViewById(R.id.MonthlyFragment_Button_left);
        btn_right = (Button) view.findViewById(R.id.MonthlyFragment_Button_right);
        text_month = (TextView) view.findViewById(R.id.MonthlyFragment_TextView_month);
        gridView = (GridView) view.findViewById(R.id.MonthlyFragment_GridView_gridView);

        gridView.setAdapter(new DayAdapter(getContext(), curYear, curMonth, curDate));

        return view;
    }

    private class DayAdapter extends BaseAdapter{

        //set member variables
        Context context;
        int year;
        int month;
        int date;
        String dayArr[] = new String[CALENDARSIZE];
        Boolean dayExtraArr[] = new Boolean[CALENDARSIZE];
        String scheduleArr[] = new String[CALENDARSIZE];

        public DayAdapter(Context context, int year, int month, int date){
            //constructor.
            this.context = context;
            this.year = year;
            this.month = month;
            this.date = date;
            boolean pre = true;
            int lastIndex = 1;
//            for(int i = 0; i < CALENDARSIZE; i++){
//                dayExtraArr[i] = true;
//            }
            //알고리즘을 구현해서 각각의 배열에 들어가는 숫자들을 다 설정해준다.
            dayCalculator.calMonth(year, month, dayArr);
            for(int i = 0; i < CALENDARSIZE; i++){
                //DB search and setting scheduleArr
                //dayArr setting
                //scheduleArr1 setting
                //db조회 있으면 넣고 아니면 ""
                Log.d("MYTAG______", i+"진입");
                if(dayArr[i].equals("")){
                    dayExtraArr[i] = true;
                    if(pre){
                        //앞쪽
                        dayArr[i] = String.valueOf(i+1);
                    }else{
                        //뒷쪽
                        dayArr[i] = String.valueOf(lastIndex);
                        lastIndex++;
                    }
                }else{
                    dayExtraArr[i] = false;
                    pre = false;
                }
                boolean isData = false;
                //db 조회할 공간---------------------------





                //------------------------------------------
                if(isData){
                    scheduleArr[i] = "data넣어야함";
                }else {
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
        public View getView(int pos, View view, ViewGroup parent) {

            if(view == null){
                    view = getLayoutInflater().inflate(R.layout.calendar_layout, parent, false);
            }
            TextView date = (TextView) view.findViewById(R.id.CalendarLayout_TextView_date);
            TextView schedule = (TextView) view.findViewById(R.id.CalendarLayout_TextView_schedule1);
            date.setText(dayArr[pos]);
            if(dayExtraArr[pos]){
                date.setTextColor(Color.parseColor("#6c6c6c"));
            }
            schedule.setText(scheduleArr[pos]);
            if(pos % 7 == 0){
                //일요일
                date.setTextColor(Color.parseColor("#ff0000"));
            }
            if(pos % 7 == 6){
                //토요일
                date.setTextColor(Color.parseColor("#0011ff"));
            }
            if(scheduleArr[pos].equals("")){
                schedule.setBackgroundColor(Color.parseColor("#00ffffff"));
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //여기 클릭했을때 해당 스케줄 관리 페이지로 넘어가야함


                }
            });


            return view;
        }
    }

}
