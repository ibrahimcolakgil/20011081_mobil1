package com.example.login;

import java.util.ArrayList;

public class Announcements {
    private String headline, content;
    public static int ID = 0;

    public Announcements() {

    }

    public Announcements(String headline, String content) {
        this.headline = headline;
        this.content = content;
        this.ID = ID;
        ID++;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    static public ArrayList<Announcements> setData(){
        ArrayList<Announcements> annList = new ArrayList<Announcements>();
        return annList;
    }
}
