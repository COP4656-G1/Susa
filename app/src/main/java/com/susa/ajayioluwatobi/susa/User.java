package com.susa.ajayioluwatobi.susa;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by wjl13 on 4/10/2018.
 */

@IgnoreExtraProperties
public class User {
    public String name;
    public String email;

    public User(){

    }

    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

}