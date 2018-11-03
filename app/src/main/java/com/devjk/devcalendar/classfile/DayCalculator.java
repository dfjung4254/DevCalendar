package com.devjk.devcalendar.classfile;

import com.devjk.devcalendar.fragment.MonthlyFragment;

public class DayCalculator {

    //member variables
    private int arr_month[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public DayCalculator(){

    }
    public int getMaxDay(int year, int month){
        if(month == 2){
            if(year%4 == 0 && (year%100!=0 || (year)%400==0)){
                arr_month[2] = 29;
            }
        }
        return arr_month[month];
    }

    public void calWeek(int year, int month, int date, String[] arr){
        //주어진 arr배열에 각각 맞는 해당 주의 숫자를 집어넣는다.
        //WeeklyFragment로 해당 주의 일을 구성할때 사용.


    }

    public void calWeekNumber(int year, int month, int date){
        int ret = 0;
        if(month == 2){
            //윤년계산.
            if(year%4 == 0 && (year%100!=0 || (year)%400==0)){
                arr_month[2] = 29;
            }
        }

        int lastYear = year-1;
        int day = (lastYear + (lastYear/4)-(lastYear/100)+(lastYear/400)+1)%7;
        for(int i = 1; i < month; i++){
            day += arr_month[i];
        }
        day = day % 7;
        int index = 0;
        for(int i = -day; i < arr_month[month]; i++){
            if(i < 0){
                //해당 포함 x
            }else{
                if((i+1) == date){
                    if(index%7 == 0){
                        if(index == 0){
                            ret = 1;
                        }else{
                            
                        }
                    }else{

                    }
                    index/7 =
                }
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



    public void calMonth(int year, int month, String[] arr){
        //주어진 arr배열에 각각 맞는 해당 달의 숫자를 집어넣는다.
        //MonthlyFragment로 해당 월의 달력을 구성할때 사용.
        if(month == 2){
            if(year%4 == 0 && (year%100!=0 || (year)%400==0)){
                arr_month[2] = 29;
            }
        }

        int lastYear = year-1;
        int day = (lastYear + (lastYear/4)-(lastYear/100)+(lastYear/400)+1)%7;
        for(int i = 1; i < month; i++){
            day += arr_month[i];
        }
        day = day % 7;
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