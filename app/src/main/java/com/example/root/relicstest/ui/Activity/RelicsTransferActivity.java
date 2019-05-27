package com.example.root.relicstest.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.root.relicstest.Adapter.RelicsAdapter;
import com.example.root.relicstest.Entities.Relics;
import com.example.root.relicstest.Entities.ResponseCode;
import com.example.root.relicstest.Entities.Transaction;
import com.example.root.relicstest.Interface.HFApiService;
import com.example.root.relicstest.R;
import com.example.root.relicstest.Utils.ConstantUtils;
import com.example.root.relicstest.Utils.RetrofitUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RelicsTransferActivity extends BaseActivity {
    private TextView tv_id,tv_name,tv_poster,tv_value,tv_details,tv_datetime;

    private ImageView iv_relics;

    private Retrofit retrofit = RetrofitUtils.getRetrofit();

    private Integer relicsId;

    private Button buy_btn;

    String username,postername;



    private static final int UPDATE_RELICS = 1;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_RELICS:
                    Relics ret = (Relics)msg.obj;
                    tv_id.setText(ret.getId().toString());
                    tv_datetime.setText(ret.getOnsalestime().toString());
                    tv_details.setText(ret.getDetails());
                    tv_name.setText(ret.getName());
                    tv_poster.setText(ret.getPoster());
                    tv_value.setText(ret.getValue().toString());
                    Glide.with(RelicsTransferActivity.this).load(ConstantUtils.BaseIMGURL + ret.getPhoto()).into(iv_relics);
            }
        }
    };

    @Override
    public void BindView() {
        tv_datetime = findViewById(R.id.tv_datetime);
        tv_details = findViewById(R.id.tv_details);
        tv_id = findViewById(R.id.tv_id);
        tv_name = findViewById(R.id.tv_name);
        tv_poster = findViewById(R.id.tv_poster);
        tv_value = findViewById(R.id.tv_value);
        iv_relics = findViewById(R.id.iv_relics);
        buy_btn = findViewById(R.id.buy_btn);
    }

    @Override
    public void setOnclickListener() {
        buy_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buy_btn:
                rt_transfer();
                break;

        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_relics_transfer;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initDatas() {
        Intent intent = getIntent();
        relicsId = intent.getIntExtra("id",0);
        username = intent.getStringExtra("username");
        postername = intent.getStringExtra("poster");
        getDetails();

    }

    @Override
    public void configViews() {

    }

    void getDetails(){
        HFApiService hfApiService = retrofit.create(HFApiService.class);
        Call<Relics> call = hfApiService.getRelics(relicsId);
        call.enqueue(new Callback<Relics>() {
            @Override
            public void onResponse(Call<Relics> call, Response<Relics> response) {
                if(response.isSuccessful()){
                    Relics ret = response.body();
                    Message msg = new Message();
                    msg.what = UPDATE_RELICS;
                    msg.obj = ret;
                    handler.sendMessage(msg);
                }
            }
            @Override
            public void onFailure(Call<Relics> call, Throwable t) {

            }
        });
    }

    void rt_transfer(){
        Transaction transaction = new Transaction();
        transaction.setNewOwnerName(username);
        transaction.setOwnerName(postername);
        transaction.setOrgName("Org1");
        HFApiService hfApiService = retrofit.create(HFApiService.class);
        Call<ResponseCode> call = hfApiService.invokeTransfer(tv_id.getText().toString(),transaction);
        call.enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if(response.isSuccessful()){
                    Log.e("transfer:",response.body().getMsg());
                    if(response.body().getCode().equals("200")){
                        Toast.makeText(RelicsTransferActivity.this,"交易成功！",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RelicsTransferActivity.this,response.body().getMsg(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.e("transfer","call failure");
            }
        });
    }


}
