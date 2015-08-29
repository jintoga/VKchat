package com.example.dat.vkchat.Model;

/**
 * Created by DAT on 8/27/2015.
 */
public class Message {
    private String body;
    private int id;
    private int user_id;
    private int from_id;

    public Message() {
    }

    public Message(String body, int id, int user_id, int from_id) {
        this.body = body;
        this.id = id;
        this.user_id = user_id;
        this.from_id = from_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFrom_id() {
        return from_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }
}
