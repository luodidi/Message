package com.example.myapplication;


import android.widget.ImageView;
import android.widget.TextView;

public class MessageHolder {
    private ImageView ivImage;//联系人头像
    private TextView tvTitle;//发件人
    private TextView tvDesc;//短信内容
    private TextView tvCount;//来自同一个联系人的信息条数
    private TextView tvTime;//收件时间

    public ImageView getIvImage() {
        return ivImage;
    }

    public void setIvImage(ImageView ivImage) {
        this.ivImage = ivImage;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public TextView getTvDesc() {
        return tvDesc;
    }

    public void setTvDesc(TextView tvDesc) {
        this.tvDesc = tvDesc;
    }

    public TextView getTvCount() {
        return tvCount;
    }

    public void setTvCount(TextView tvCount) {
        this.tvCount = tvCount;
    }

    public TextView getTvTime() {
        return tvTime;
    }

    public void setTvTime(TextView tvTime) {
        this.tvTime = tvTime;
    }

}
