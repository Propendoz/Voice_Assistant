package com.example.lab1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "messageDb";
    public static final String TABLE_MESSAGES = "messages";

    public static final String FIELD_ID = "id";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_SEND = "send";
    public static final String FIELD_DATE = "date";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("LOG", "onCreateDatabase");
        db.execSQL("CREATE TABLE [" + TABLE_MESSAGES + "] (\n" +
                " [" + FIELD_ID + "] INTEGER primary key, " +
                " [" + FIELD_MESSAGE + "] TEXT, " +
                " [" + FIELD_SEND + "] INTEGER, " +
                " [" + FIELD_DATE + "] TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("LOG", "onUpdateDatabase");
        db.execSQL("drop table if exists " + TABLE_MESSAGES);
        onCreate(db);
    }
}
