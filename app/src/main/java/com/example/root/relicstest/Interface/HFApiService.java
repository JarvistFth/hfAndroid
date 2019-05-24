package com.example.root.relicstest.Interface;

import com.example.root.relicstest.Entities.QueryParam;
import com.example.root.relicstest.Entities.Relics;
import com.example.root.relicstest.Entities.RelicsDetails;
import com.example.root.relicstest.Entities.RelicsHistory;
import com.example.root.relicstest.Entities.ResponseCode;
import com.example.root.relicstest.Entities.Transaction;
import com.example.root.relicstest.Entities.UserDetails;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

    @POST("relics/putRelics/{username}/{orgname}")
    Call<ResponseCode> putRelics(@Path("username")String username,@Path("orgname")String orgname,@Body Relics relics);

    @POST("relics/relicsPhoto/{relicsName}")
    Call<ResponseCode> uploadPhoto(@Path("relicsName")String relicsName, @Body MultipartBody body);

    @GET("users/details/{username}")
    Call<UserDetails> getUserDetails(@Path("username")String username);
}
