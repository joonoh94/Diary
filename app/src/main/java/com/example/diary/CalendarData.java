package com.example.diary;

public class CalendarData {
    private String daysOfMonth;
    private String numberOfSchedule;
    private int resId;
    public CalendarData(String daysOfMonth, String numberOfSchedule, int resId) {
        this.daysOfMonth = daysOfMonth;
        this.numberOfSchedule = numberOfSchedule;
        this.resId = resId;
    }

    public String getDaysOfMonth() {
        return daysOfMonth;
    }

    public void setDaysOfMonth(String daysOfMonth) {
        this.daysOfMonth = daysOfMonth;
    }

    public String getNumberOfSchedule() {
        return numberOfSchedule;
    }

    public void setNumberOfSchedule(String numberOfSchedule) {
        this.numberOfSchedule = numberOfSchedule;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }





}
