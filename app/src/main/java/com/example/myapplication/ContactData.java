package com.example.myapplication;

import android.graphics.Bitmap;

public class ContactData {

    private Long contactId;
    private String contactName;
    private String phoneNumber;
    //private Long photoId;
    //private Bitmap contactPhoto;

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

   // public Long getPhotoId() { return photoId; }

   // public void setPhotoId(Long photoId) { this.photoId = photoId; }


    /*
    public Bitmap getContactPhoto() {
        return contactPhoto;
    }

    public void setContactPhoto(Bitmap contactPhoto) {
        this.contactPhoto = contactPhoto;
    }
*/
}
