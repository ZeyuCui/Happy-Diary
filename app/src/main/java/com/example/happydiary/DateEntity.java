package com.example.happydiary;

import java.io.Serializable;


public class DateEntity extends BaseDateEntity implements Serializable {

    /**
     * 日期
     */
    public int date;

    /**
     * 星期
     */
    public int weekDay;

    /**
     * 是否为当前日期
     */
    public boolean isNowDate;

    /**
     * 是否为本月日期
     */
    public boolean isSelfMonthDate;

    /**
     * 是否有记录
     */
    public boolean hasRecord = false;

    public DateEntity(int year, int month, int day) {
        super(year, month, day);
    }
}