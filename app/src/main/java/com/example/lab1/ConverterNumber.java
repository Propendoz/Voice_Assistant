package com.example.lab1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConverterNumber implements Serializable {

        @SerializedName("status")
        @Expose

        public String status;

        @SerializedName("str")
        @Expose

        public String convertedStr;

}
