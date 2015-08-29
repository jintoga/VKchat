package com.example.dat.vkchat.Model;

import java.io.Serializable;

/**
 * Created by DAT on 8/26/2015.
 */
public class Contact implements Serializable {

    private int user_id;
    private String name;
    private String avatar_url;
    private int isOnline;

    public Contact() {
    }

    public Contact(int user_id, String name, String avatar_url, int isOnline) {
        this.name = name;
        this.avatar_url = avatar_url;
        this.isOnline = isOnline;
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
