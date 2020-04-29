package com.example.lab1;

import android.util.Log;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConvertNumberToString {

    public static void getConvertNumber(String number, final Consumer<String> callback) {
        ConverterNumberApi api = ConvertNumberService.getApi();
        Call<ConverterNumber> call = api.getConvertNumber(number);
        call.enqueue(new Callback<ConverterNumber>() {
            @Override
            public void onResponse(Call<ConverterNumber> call, Response<ConverterNumber> response) {
                ConverterNumber result = response.body();
                if (result!=null){
                    String answer = result.convertedStr;
                    callback.accept(answer);
                }
                else{
                    callback.accept("Не могу перевести число в строку!");
                }

            }

            @Override
            public void onFailure(Call<ConverterNumber> call, Throwable t) {
                Log.w("CONVERTNUMBER", t.getMessage());
            }
        });
    }
}
