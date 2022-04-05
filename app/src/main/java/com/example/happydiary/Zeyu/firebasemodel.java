package com.example.happydiary.Zeyu;

public class firebasemodel {

    private String title;
    private String content;
    private String location;


    public firebasemodel()
    {

    }

    public firebasemodel(String title, String content,String location)
    {
        this.title=title;
        this.content=content;
        this.location=location;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
