package com.devjk.devcalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class ScheduleActivity extends AppCompatActivity {

    String weeks[] = {"일", "월", "화", "수", "목", "금", "토"};
    int curYear;
    int curMonth;
    int curDate;
    int curDay;
    Button btn_back;
    Button btn_submit;
    TextView text_year;
    TextView text_month;
    TextView text_date;
    TextView text_day;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Intent intent = getIntent();
        Calendar cal = Calendar.getInstance();
        curYear = intent.getIntExtra("year", cal.get(cal.YEAR));
        curMonth = intent.getIntExtra("month", cal.get(cal.MONTH));
        curDate = intent.getIntExtra("date", cal.get(cal.DATE));
        curDay = intent.getIntExtra("day", cal.get(cal.DAY_OF_WEEK)-1);
        btn_back = (Button) findViewById(R.id.ScheduleActivity_Button_back);
        btn_submit = (Button) findViewById(R.id.ScheduleActivity_Button_submit);
        text_year = (TextView) findViewById(R.id.ScheduleActivity_TextView_year);
        text_month = (TextView) findViewById(R.id.ScheduleActivity_TextView_month);
        text_date = (TextView) findViewById(R.id.ScheduleActivity_TextView_date);
        text_day = (TextView) findViewById(R.id.ScheduleActivity_TextView_day);
        editText = (EditText) findViewById(R.id.ScheduleActivity_EditText_editText);

        text_year.setText(curYear + " ");
        text_month.setText(curMonth + " ");
        text_date.setText(curDate + " ");
        text_day.setText("(" + weeks[curDay] + ")");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //여기에서 editText의 내용을 DB에 저장시켜야함.


                //
            }
        });

        //DB에서 해당 년,월,일에 해당하는 스케쥴을 검색하고
        //해당하는 스케쥴이 있으면 String에 담아 editText에 입력시켜야함.
        String tmpEdit = "임시입력값";
        editText.setText(tmpEdit);


    }
}
