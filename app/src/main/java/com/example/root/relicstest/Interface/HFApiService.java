package com.example.root.relicstest.Interface;

import com.example.root.relicstest.Entities.QueryParam;
import com.example.root.relicstest.Entities.Relics;
import com.example.root.relicstest.Entities.RelicsArr;
import com.example.root.relicstest.Entities.RelicsDetails;
import com.example.root.relicstest.Entities.RelicsHistory;
import com.example.root.relicstest.Entities.ResponseCode;
import com.example.root.relicstest.Entities.Transaction;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface HFApiService {

    @PUT("invoke/transfer/{id}")
    Call<ResponseCode> invokeTransfer(@Path("id")String id, @Body Transaction transaction);

    @GET("invoke/query/{id}")
    Call<RelicsDetails> query(@Path("id")String id, @Body QueryParam param);

    @GET("invoke/queryHistory/{id}")
    Call<RelicsHistory> queryHistory(@Path("id")String ID, @Body QueryParam param);

    @GET("relics/getRelics/{id}")
    Call<Relics> getRelics(@Path("id") Integer id);

    @GET("relics/getAllRelics")
    Call<List<Relics>> getAllRelics();
}
