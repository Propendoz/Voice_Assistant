package com.example.lab1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageEntity {

    public String text;
    public String  date;
    public int isSend;

    public MessageEntity(String text, String date, int isSend){
        this.text = text;
        this.date = date;
        this.isSend = isSend;
    }

    public MessageEntity(Message message){
        this.text = message.text;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        this.date = dateFormat.format(message.date);
        this.isSend = message.isSend ? 1 : 0;
    }
}
