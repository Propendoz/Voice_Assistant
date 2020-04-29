package com.example.lab1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Message {

    public String text;
    public Date date;
    public Boolean isSend;

    public Message(String text, Date date, Boolean isSend) {
        this.text = text;
        this.date = date;
        this.isSend = isSend;
    }

    public Message(MessageEntity entity) throws ParseException {

        text = entity.text;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        date = dateFormat.parse(entity.date);
        isSend = intToBoolean(entity.isSend);
    }

    public Message() {

    }

    private boolean intToBoolean(int input) {
        if((input==0)||(input==1)) {
            return input!=0;
        }else {
            throw new IllegalArgumentException("Входное значение может быть равно только 0 или 1 !");
        }
    }
}
