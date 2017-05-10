package com.example.geoff.ranchat;

import java.util.Date;

/*
 * The Class Conversation represents a single chat conversation message.
 */
public class Conversation
{


    public static final int STATUS_SENDING = 0;
    public static final int STATUS_SENT = 1;
    public static final int STATUS_FAILED = 2;
    private String msg;
    private int status = STATUS_SENT;
    private Date date;
    private String sender;
    private String receiver;
    private String photoUrl;

    public Conversation(String msg, Date date, String uid, String sender, String receiver) {
        this.msg = msg;
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
        this.photoUrl = photoUrl;

    }


    public String getMsg()
    {
        return msg;
    }


    public void setMsg(String msg)
    {
        this.msg = msg;
    }


    public boolean isSent()
    {
        return UserList.user.getId().contentEquals(sender);
    }


    public Date getDate() {

        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }


    public String getReceiver()
    {
        return receiver;
    }


    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getPhotoUrl() { return this.photoUrl; }

}
