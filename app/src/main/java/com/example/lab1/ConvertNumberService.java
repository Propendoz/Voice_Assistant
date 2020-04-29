package com.example.lab1;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConvertNumberService {
     public static ConverterNumberApi getApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://htmlweb.ru") // базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) // конвертер для преобразования JSON в объекты
                .build();
        return retrofit.create(ConverterNumberApi.class);
    }
}
