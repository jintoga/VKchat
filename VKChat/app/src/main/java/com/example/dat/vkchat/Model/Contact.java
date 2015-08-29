package com.example.dat.vkchat.Model;

/**
 * Created by DAT on 8/26/2015.
 */
public class Contact {

    private String name;
    private String avatar_url;
    private int isOnline;

    public Contact() {
    }

    public Contact(String name, String avatar_url, int isOnline) {
        this.name = name;
        this.avatar_url = avatar_url;
        this.isOnline = isOnline;
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
}
