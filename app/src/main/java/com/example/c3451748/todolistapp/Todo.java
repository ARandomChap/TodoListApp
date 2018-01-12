package com.example.c3451748.todolistapp;

import java.util.Date;
import java.util.UUID;
import java.text.*;

/**
 * Created by c3451748 on 17/11/2017.
 */

public class Todo {


    private UUID mId;
    private String mTitle;
    private String mDetail;
    private String mDate;
    private boolean mIsComplete;

    public Todo() {
        mId = UUID.randomUUID();
        Date date = new Date();

        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        mDate = "Created at: " + f.format(date);
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String mDetail) {
        this.mDetail = mDetail;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public boolean isComplete() {
        return mIsComplete;
    }

    public void setComplete(boolean mIsComplete) {
        this.mIsComplete = mIsComplete;
    }
}
