package com.example.c3451748.shoppinglistapp;

import java.util.Date;
import java.util.UUID;

/**
 * Created by c3451748 on 17/11/2017.
 */

public class Shopping {


    private UUID mId;
    private String mTitle;
    private String mDetail;
    private Date mDate;
    private boolean mIsComplete;

    public Shopping() {
        mId = UUID.randomUUID();
        mDate = new Date();
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

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean isComplete() {
        return mIsComplete;
    }

    public void setComplete(boolean mIsComplete) {
        this.mIsComplete = mIsComplete;
    }
}
