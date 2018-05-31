package com.susa.ajayioluwatobi.susa;

import java.util.Date;

public class Chat {
    Date date;
    String text="",user="";

    public Date getDate(){
        return date;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Chat(String text, String user) {
        this.text = text;
        this.user = user;
        this.date = new Date();
    }
    public Chat(){
        this.date = new Date();
    }

}
