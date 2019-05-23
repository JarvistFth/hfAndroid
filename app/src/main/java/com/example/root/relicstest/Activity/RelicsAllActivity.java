package com.example.root.relicstest.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.root.relicstest.Adapter.RelicsAdapter;
import com.example.root.relicstest.Entities.Relics;
import com.example.root.relicstest.Entities.RelicsArr;
import com.example.root.relicstest.Interface.HFApiService;
import com.example.root.relicstest.R;
import com.example.root.relicstest.Utils.ConstantUtils;
import com.example.root.relicstest.Utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelicsAllActivity extends BaseActivity {
    RecyclerView rv_relicsall;

    private List<Relics> relicsList = new ArrayList<>();

    private RelicsAdapter adapter;

    private static final int UPDATE_RV = 2;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_RV:
                    relicsList = (List<Relics>) msg.obj;
                    adapter = new RelicsAdapter(relicsList);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relics_all);
        BindView();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rv_relicsall.setLayoutManager(layoutManager);
        retrofit_getall();

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
}
