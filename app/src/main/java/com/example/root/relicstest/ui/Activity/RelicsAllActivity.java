package com.example.root.relicstest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.example.root.relicstest.Adapter.RelicsAdapter;
import com.example.root.relicstest.Entities.Relics;
import com.example.root.relicstest.Interface.HFApiService;
import com.example.root.relicstest.R;
import com.example.root.relicstest.Utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelicsAllActivity extends BaseActivity implements RelicsAdapter.RelicsAdapterListener {
    RecyclerView rv_relicsall;

    private List<Relics> relicsList = new ArrayList<>();

    private RelicsAdapter adapter;

    @Override
    public int getLayoutID() {
        return R.layout.activity_relics_all;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initDatas() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rv_relicsall.setLayoutManager(layoutManager);
        retrofit_getall();
    }

    @Override
    public void configViews() {

    }

    private static final int UPDATE_RV = 2;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_RV:
                    relicsList = (List<Relics>) msg.obj;
                    adapter = new RelicsAdapter(relicsList);
                    adapter.setClickLitener(RelicsAllActivity.this);
                    rv_relicsall.setAdapter(adapter);
            }
        }
    };


    @Override
    public void BindView() {
        rv_relicsall = findViewById(R.id.rv_relicsAll);
    }

    @Override
    public void setOnclickListener() {

    }

    @Override
    public void onClick(View v) {

    }


    void retrofit_getall(){
        Call<List<Relics>> call = RetrofitUtils.getRetrofit().create(HFApiService.class).getAllRelics();
        call.enqueue(new Callback<List<Relics>>() {
            @Override
            public void onResponse(Call<List<Relics>> call, Response<List<Relics>> response) {
                Message msg = new Message();
                msg.what = UPDATE_RV;
                msg.obj = response.body();
                handler.sendMessage(msg);
                Log.d("Tag",response.body().toString());
            }

            @Override
            public void onFailure(Call<List<Relics>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onRelicsClick(View v) {
        int pos = (Integer)v.getTag();
        Relics relics = relicsList.get(pos);
        Intent intent = new Intent(RelicsAllActivity.this,RelicsTransferActivity.class);
        intent.putExtra("id",relics.getId());
        startActivity(intent);
    }
}
