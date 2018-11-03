package com.devjk.devcalendar.classfile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScheduleDBHelper extends SQLiteOpenHelper{

    private static ScheduleDBHelper instance;

    private static int version = 1;
    private static String name = "ScheduleDB";
    public static String tableName = "Schedule";

    //constructor
    public ScheduleDBHelper(Context context) {
        super(context, name, null, version);
    }

    public static ScheduleDBHelper getInstance(Context context){
        //하나의 dbHelper 클래스만 사용하여 모두 공유. getInstance를 사용해 호출.
        if(instance == null){
            instance = new ScheduleDBHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //최초 DB 테이블생성.
        sqLiteDatabase.execSQL("CREATE TABLE " + tableName + " ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "year INTEGER," +
                "month INTEGER," +
                "date INTEGER," +
                "title TEXT," +
                "contents TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
