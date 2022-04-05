package com.example.happydiary;


public class BaseDateEntity {

    /** 年 */
    public int     year;
    /** 月 */
    public int     month;
    /** 日 */
    public int     day;

    public BaseDateEntity(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public String toString() {
        return "BaseDateEntity{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }
}

