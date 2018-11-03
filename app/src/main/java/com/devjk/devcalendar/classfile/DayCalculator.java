package com.devjk.devcalendar.classfile;

import android.util.Log;

import com.devjk.devcalendar.MainActivity;
import com.devjk.devcalendar.fragment.MonthlyFragment;

public class DayCalculator {

    //member variables
    public int arr_month[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private String arr_day[] = {"일","월","화","수","목","금","토"};

    //constructor
    public DayCalculator(){

    }

    //methods
    public void setFeb(int year, int month){
        if(month == 2){
            if(year%4 == 0 && (year%100!=0 || (year)%400==0)){
                arr_month[2] = 29;
            }
        }
    }

    public int getDay(int year, int month){
        setFeb(year, month);
        int lastYear = year-1;
        int day = (lastYear + (lastYear/4)-(lastYear/100)+(lastYear/400)+1)%7;
        for(int i = 1; i < month; i++){
            day += arr_month[i];
        }
        day = day % 7;
        return day;
    }

    public String calDay(int year, int month, int date){
        //요일 계산.
        int day = getDay(year, month);
        int index = 0;
        for(int i = -day; i < arr_month[month]; i++){
            if(i+1 == date){
                if(index == 0){
                    return arr_day[0];
                }else{
                    return arr_day[index%7];
                }
            }
            index++;
        }
        return "";
    }

    public void calWeek(int year, int month, int weekNumber, String[] dateArray, String[] dayArray, int[] targetDate){
        //주어진 arr배열에 각각 맞는 해당 주의 숫자를 집어넣는다.
        //WeeklyFragment로 해당 주의 일을 구성할때 사용.
        int day = getDay(year, month);
        int index = 0;
        int arr_index = 0;
        for(int i = -day; i < arr_month[month]; i++){
            boolean setData = true;
            if(i > 0 && index/7 == weekNumber){
                Log.d(MainActivity.MYLOG, "dateArray 에 "+(i+1)+" 넣음 arr_index["+arr_index+"] "+weekNumber+"주차임.");
                dateArray[arr_index] = String.valueOf(i+1);
                dayArray[arr_index] = arr_day[index%7];
                arr_index++;
            }else if(i == 0){
                if(index == 0 && weekNumber == 0){
                    Log.d(MainActivity.MYLOG, "dateArray 에 "+(i+1)+" 넣음 arr_index["+arr_index+"] "+weekNumber+"주차임.");
                    dateArray[arr_index] = String.valueOf(i+1);
                    dayArray[arr_index] = arr_day[0];
                    arr_index++;
                }else if(index != 0 && index/7 == weekNumber){
                    Log.d(MainActivity.MYLOG, "dateArray 에 "+(i+1)+" 넣음 arr_index["+arr_index+"] "+weekNumber+"주차임.");
                    dateArray[arr_index] = String.valueOf(i+1);
                    dayArray[arr_index] = arr_day[index%7];
                    arr_index++;
                }else{
                    setData = false;
                }
            }else{
                setData = false;
            }
            if(setData){
                //dateArray에 값을 집어넣은 타이밍임.
                if(arr_index == 1){
                    //처음 arr_index 0번지에 i+1 넣고 arr_index++ 되었을때,
                    //해당 주의 첫 날짜
                    targetDate[0] = i+1;
                }
                //마지막 날짜.
                targetDate[1] = i+1;
            }
            index++;
        }
    }

    public int calFirstWeekDaysNum(int year, int month){
        int day = getDay(year, month);
        return 7-day;
    }

    public int calLastWeekDaysNum(int year, int month){
        int day = getDay(year, month);
        return (arr_month[month] + (day-1))%7+1;
    }

    public int calWeekMaxIndex(int year, int month){
        int day = getDay(year, month);
        return (arr_month[month] + (day-1))/7;
    }

    public int calWeekNumber(int year, int month, int date){
        int ret = 0;
        int day = getDay(year, month);
        int index = 0;
        for(int i = -day; i < arr_month[month]; i++){
            if(i < 0){
                //해당 포함 x
            }else{
                if((i+1) == date){
                    if(index == 0){
                        ret = 0;
                    }else{
                        ret = index/7;
                    }
                    break;
                }
            }
            index++;
        }
        return ret;
    }

    public void calMonth(int year, int month, String[] arr){
        //주어진 arr배열에 각각 맞는 해당 달의 숫자를 집어넣는다.
        //MonthlyFragment로 해당 월의 달력을 구성할때 사용.
        int day = getDay(year, month);
        int index = 0;
        for(int i = -day; i < arr_month[month]; i++){
            if(i < 0){
                //arr[index] = "";
                arr[index] = String.valueOf(arr_month[month-1] + i+1);
            }else{
                arr[index] = String.valueOf(i+1);
            }
            index++;
        }
        int num = 1;
        for(; index < MonthlyFragment.CALENDARSIZE; index++){
            arr[index] = String.valueOf(num);
            num++;
        }
    }

}