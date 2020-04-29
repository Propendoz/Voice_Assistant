package com.example.lab1;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ConverterNumberApi {
    @GET("/json/convert/num2str")
    Call<ConverterNumber> getConvertNumber(@Query("num") String number);
}
