package com.example.diary;

public class DiaryData {
    private String content;

    public DiaryData(String content, String title, String day) {
        this.content = content;
        this.title = title;
        this.day = day;
    }

    private String title;

    private String day;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }


    public String getContent() {
        return content;
    }
    public void setContent(String content) {

        this.content = content;
    }



}
