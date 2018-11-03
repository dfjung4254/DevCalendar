package com.devjk.devcalendar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devjk.devcalendar.classfile.ScheduleDBHelper;

import java.util.Calendar;

public class ScheduleActivity extends AppCompatActivity {

    String weeks[] = {"일", "월", "화", "수", "목", "금", "토"};
    int curYear;
    int curMonth;
    int curDate;
    int curDay;
    int curId;
    Button btn_back;
    Button btn_submit;
    TextView text_year;
    TextView text_month;
    TextView text_date;
    TextView text_day;
    EditText contents;
    EditText title;
    boolean isModified;

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
        contents = (EditText) findViewById(R.id.ScheduleActivity_EditText_contents);
        title = (EditText) findViewById(R.id.ScheduleActivity_EditText_title);

        //DB에서 해당 년,월,일에 해당하는 스케쥴을 검색하고
        //해당하는 스케쥴이 있으면 String에 담아 editText에 입력시켜야함.
        //또한 변형된것인지 완전 새것인건지 판단해야함.

        //DB조회 한 후 isModified의 값을 수정.
        SQLiteDatabase db = ScheduleDBHelper.getInstance(this).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ScheduleDBHelper.tableName + "" +
                " WHERE year="+curYear+" AND month="+curMonth+" AND date="+curDate, null);
        isModified = true;
        if(cursor.getCount() == 0){
            //db조회값 없음
            isModified = false;
        }else{
            //db조회값 있음
            while(cursor.moveToNext()){
                curId = cursor.getInt(0);
                contents.setText(cursor.getString(5));
                title.setText(cursor.getString(4));
            }
        }

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
                ContentValues values = new ContentValues();
                values.put("year", curYear);
                values.put("month", curMonth);
                values.put("date", curDate);
                values.put("title", title.getText().toString());
                values.put("contents", contents.getText().toString());
                Log.d("MYLoG++++++++++++", title.getText().toString());
                //isModified
                long isSuccess = 0;
                if(isModified){
                    //update쿼리
                    Log.d("MYLOG+++++++++++", "쿼리 UPDATE");
                    SQLiteDatabase db = ScheduleDBHelper.getInstance(getApplicationContext()).getWritableDatabase();
                    isSuccess =  db.update(ScheduleDBHelper.tableName, values, "id=?", new String[] {String.valueOf(curId)});

                }else{
                    //insert쿼리
                    if(contents.getText().toString().length() != 0 && title.getText().toString().length() != 0){
                        //텍스트 제목과 내용이 공백이 아닐 시.
                        Log.d("MYLOG+++++++++++", "쿼리 INSERT");
                        SQLiteDatabase db = ScheduleDBHelper.getInstance(getApplicationContext()).getWritableDatabase();
                        isSuccess =  db.insert(ScheduleDBHelper.tableName, null, values);
                    }
                }
                if(isSuccess == -1){
                    //저장 실패.
                    Toast.makeText(getApplicationContext(), "저장 실패", Toast.LENGTH_SHORT).show();
                }else{
                    //저장 성공.
                    Toast.makeText(getApplicationContext(), "저장 성공", Toast.LENGTH_SHORT).show();
                    MainActivity.monthlyFragment.dayAdapter.resetSchedule();
                    MainActivity.monthlyFragment.dayAdapter.notifyDataSetChanged();
                    //주간, 일간도 다 reset?
                    finish();
                }


                //
            }
        });




    }
}
