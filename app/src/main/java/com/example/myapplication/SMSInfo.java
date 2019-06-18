package com.example.myapplication;

import android.graphics.Bitmap;

public class SMSInfo {
    /**
     * 短信内容
     */
    private String smsbody;
    /**
     * * 发送短信的日期和时间
     */
    private String date;
    /**
     * 短信类型1是接收到的，通过SQLiteExpertPers查看发出信息类型为6
     */
    private String type;
    /**
     * 与同一个联系人的会话包含的消息数
     */
    private String messageCout;
    /**
     * 会话人的信息，属于联系人则为名称，否则为其号码
     */
    private String contactMes;
    /**
     * 该会话对应的联系人头像
     */
    private Bitmap contactPhoto;

    public String getContactMes() {
        return contactMes;
    }

    public void setContactMes(String contactMes) {
        this.contactMes = contactMes;
    }

    public Bitmap getContactPhoto() {
        return contactPhoto;
    }

    public void setContactPhoto(Bitmap contactPhoto) {
        this.contactPhoto = contactPhoto;
    }

    public String getMessageCout() {
        return messageCout;
    }

    public void setMessageCout(String messageCout) {
        this.messageCout = messageCout;
    }

    public String getSmsbody() {
        return smsbody;
    }

    public void setSmsbody(String smsbody) {
        this.smsbody = smsbody;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
