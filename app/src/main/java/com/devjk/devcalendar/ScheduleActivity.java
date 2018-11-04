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

    private String weeks[] = {"일", "월", "화", "수", "목", "금", "토"};
    private int curYear;
    private int curMonth;
    private int curDate;
    private int curDay;
    private int curId;
    private Button btn_back;
    private Button btn_submit;
    private Button btn_delete;
    private TextView text_year;
    private TextView text_month;
    private TextView text_date;
    private TextView text_day;
    private  EditText contents;
    private EditText title;
    private boolean isModified;
    private String strContents;
    private String strTitle;

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
        btn_delete = (Button) findViewById(R.id.ScheduleActivity_Button_delete);
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
            strContents = "";
            strTitle = "";
        }else{
            //db조회값 있음
            while(cursor.moveToNext()){
                curId = cursor.getInt(0);
                strContents = cursor.getString(5);
                strTitle = cursor.getString(4);
                contents.setText(strContents);
                title.setText(strTitle);
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

                //내용수정여부 검사.(내용을 안건드렸다면 굳이 insert나 update필요없음.
                if(strTitle.equals(title.getText()) && strContents.equals(contents.getText())){
                    //전부 처음내용이랑 같음.
                    finish();
                    return;
                }

                ContentValues values = new ContentValues();
                values.put("year", curYear);
                values.put("month", curMonth);
                values.put("date", curDate);
                values.put("title", title.getText().toString());
                values.put("contents", contents.getText().toString());
                Log.d("MYLoG++++++++++++", title.getText().toString());
                //isModified
                long isSuccess = 0;
                SQLiteDatabase db = ScheduleDBHelper.getInstance(getApplicationContext()).getWritableDatabase();
                if(isModified){
                    //update쿼리
                    if(contents.getText().toString().length() != 0 && title.getText().toString().length() != 0){
                        //기존 수정한 내용이 초기화되지 않았을때는 정보 update
                        Log.d(MainActivity.MYLOG, "쿼리 UPDATE");
                        isSuccess =  db.update(ScheduleDBHelper.tableName, values, "id=?", new String[] {String.valueOf(curId)});
                    }else{
                        //초기화로 수정했을때 db에서 아예 삭제
                        Log.d(MainActivity.MYLOG, "쿼리 DELETE");
                        isSuccess = db.delete(ScheduleDBHelper.tableName, "id=?", new String[] {String.valueOf(curId)});
                    }

                }else{
                    //insert쿼리
                    if(contents.getText().toString().length() != 0 && title.getText().toString().length() != 0){
                        //텍스트 제목과 내용이 공백이 아닐 시.
                        Log.d(MainActivity.MYLOG, "쿼리 INSERT");
                        isSuccess =  db.insert(ScheduleDBHelper.tableName, null, values);
                    }
                }
                if(isSuccess == -1){
                    //저장 실패.
                    Toast.makeText(getApplicationContext(), "저장 실패", Toast.LENGTH_SHORT).show();
                }else{
                    //저장 성공.
                    Toast.makeText(getApplicationContext(), "저장되었습니다", Toast.LENGTH_SHORT).show();
                    MainActivity.monthlyFragment.dayAdapter.resetSchedule();
                    MainActivity.monthlyFragment.dayAdapter.notifyDataSetChanged();
                    MainActivity.weeklyFragment.dayAdapter.resetSchedule();
                    MainActivity.weeklyFragment.dayAdapter.notifyDataSetChanged();
                    MainActivity.dailyFragment.searchDB();
                    finish();
                }


                //
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //기존 정보 초기화
                title.setText("");
                contents.setText("");
            }
        });




    }
}
