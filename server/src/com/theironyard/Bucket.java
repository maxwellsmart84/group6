package com.theironyard;

/**
 * Created by DrScott on 11/5/15.
 */
public class Bucket {
    int id;
    String text;
    boolean isDone;

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public boolean isDone() {
        return isDone;
    }

    public Bucket(int id, String text, boolean isDone) {
        this.id = id;
        this.text = text;
        this.isDone = isDone;
    }
    public Bucket (){}
}
