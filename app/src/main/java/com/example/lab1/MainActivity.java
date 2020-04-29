package com.example.lab1;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    protected Button sendButton;
    protected EditText questionText;
    protected TextToSpeech textToSpeech;
    protected RecyclerView chatMessageList;
    protected MessageListAdapter messageListAdapter;
    protected SharedPreferences sPref;
    public static final String APP_PREFERENCES = "mysettings";
    private boolean isLight = false;
    private String THEME = "THEME";
    DBHelper dbHelper;
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sPref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        isLight = sPref.getBoolean(THEME, true);
        if(isLight == true){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        super.onCreate(savedInstanceState);
        Log.i("LOG", "onCreate");
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        sendButton=findViewById(R.id.sendButton);
        questionText=findViewById(R.id.questionField);
        chatMessageList=findViewById(R.id.chatMessageList);
        messageListAdapter = new MessageListAdapter();
        chatMessageList.setLayoutManager(new LinearLayoutManager(this));
        chatMessageList.setAdapter(messageListAdapter);

        Cursor cursor = database.query(dbHelper.TABLE_MESSAGES, null, null, null,
                null, null,null);
        if (cursor.moveToFirst()){
            int messageIndex = cursor.getColumnIndex(dbHelper.FIELD_MESSAGE);
            int dateIndex = cursor.getColumnIndex(dbHelper.FIELD_DATE);
            int sendIndex = cursor.getColumnIndex(dbHelper.FIELD_SEND);

            do{
                MessageEntity entity = new MessageEntity(cursor.getString(messageIndex),
                        cursor.getString(dateIndex), cursor.getInt(sendIndex));
                Message message = new Message();
                try {
                    message = new Message(entity);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                messageListAdapter.messageList.add(message);

            }while (cursor.moveToNext());
        }
        cursor.close();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                onSend();
            }
        });
        textToSpeech = new TextToSpeech(getApplicationContext(), new
                TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if(i != TextToSpeech.ERROR){
                            textToSpeech.setLanguage(new Locale("ru"));
                        }
                    }
                });


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onSend() {
        String text=questionText.getText().toString();
        text = AI.getStr(text);
        final String finalText = text;
        try {
            AI.getAnswer(text, new Consumer<String>() {
                @Override
                public void accept(String answer) {
                    messageListAdapter.messageList.add(new Message(finalText, Calendar.getInstance().getTime(),true));
                    messageListAdapter.messageList.add(new Message(answer, Calendar.getInstance().getTime(), false));
                    messageListAdapter.notifyDataSetChanged();

                    //chatMessageList.append("\n" + text);
                    //chatMessageList.append("\n" + answer);
                    //textToSpeech.speak(answer, TextToSpeech.QUEUE_FLUSH, null, null);

                    chatMessageList.scrollToPosition(messageListAdapter.messageList.size() - 1);
                   // chatMessageList.scrollToPosition(messageListAdapter.messageList.size());
                    questionText.setText("");
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        Log.i("LOG", "onSaveInstanceState");
        state.putSerializable("keychat", messageListAdapter);
    }
    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        Log.i("LOG", "onRestoreInstanceState");
        messageListAdapter = (MessageListAdapter)(state.getSerializable("keychat"));
        chatMessageList.setAdapter(messageListAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("LOG", "onStart");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("LOG", "onPause");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LOG", "onDestroy");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("LOG", "onRestart");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("LOG", "onStop");

        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean(THEME, isLight);
        editor.apply();
        database.delete(dbHelper.TABLE_MESSAGES, null, null);
        for (int i = 0; i < messageListAdapter.messageList.size(); i++){
            MessageEntity entity = new MessageEntity(messageListAdapter.messageList.get(i));
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.FIELD_MESSAGE, entity.text);
            contentValues.put(DBHelper.FIELD_SEND, entity.isSend);
            contentValues.put(DBHelper.FIELD_DATE, entity.date);

            database.insert(dbHelper.TABLE_MESSAGES, null, contentValues);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.day_setting:
                isLight = true;
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case R.id.night_setting:
                isLight = false;
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
                default:
                    break;

        }
        return super.onOptionsItemSelected(item);
    }
}
