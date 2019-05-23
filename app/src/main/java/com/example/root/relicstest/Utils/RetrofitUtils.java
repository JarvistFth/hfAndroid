package com.example.root.relicstest.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    public static Retrofit retrofit = new Retrofit.Builder().
            baseUrl(ConstantUtils.BaseURL).
            addConverterFactory(GsonConverterFactory.create())
            .build();

    public static Retrofit getRetrofit(){
        return retrofit;
    }
}
