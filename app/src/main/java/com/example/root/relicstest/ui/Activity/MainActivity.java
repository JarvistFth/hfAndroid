package com.example.root.relicstest.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.relicstest.Adapter.HomePagerAdapter;
import com.example.root.relicstest.Entities.Relics;
import com.example.root.relicstest.Entities.UserDetails;
import com.example.root.relicstest.Fragment.MineFragment;
import com.example.root.relicstest.Fragment.PropertyFragment;
import com.example.root.relicstest.Interface.HFApiService;
import com.example.root.relicstest.R;
import com.example.root.relicstest.Utils.RetrofitUtils;
import com.example.root.relicstest.View.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.vp_home)
    NoScrollViewPager vpHome;
    @BindView(R.id.iv_mall)
    ImageView ivMall;
    @BindView(R.id.tv_mall)
    TextView tvMall;
    @BindView(R.id.lly_mall)
    LinearLayout llyMall;

    @BindView(R.id.iv_mine)
    ImageView ivMine;
    @BindView(R.id.tv_mine)
    TextView tvMine;
    @BindView(R.id.lly_mine)
    LinearLayout llyMine;


    Retrofit retrofit ;
    String username ;

    public static final int handlerMsg = 1;

    List<Fragment> fragmentList = new ArrayList<>();


    UserDetails userDetails;
    List<Relics> relicsList;

    private Button query_btn,queryHistory_btn,buy_btn,showmore_btn;

    private HomePagerAdapter homePagerAdapter;


    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case handlerMsg:
                    userDetails = (UserDetails) msg.obj;
                    PropertyFragment propertyFragment = new PropertyFragment();
                    MineFragment mineFragment = new MineFragment();
                    Bundle mineBundle = new Bundle();
                    Bundle proBundle = new Bundle();
                    if(userDetails!= null){
                        mineBundle.putSerializable("userDetails",userDetails);
                    }

                    if(userDetails != null){
                        proBundle.putString("username",userDetails.getName());
                    }


                    Log.d("handler:",userDetails.toString());
                    mineFragment.setArguments(mineBundle);
                    propertyFragment.setArguments(proBundle);
                    fragmentList.add(propertyFragment);
                    fragmentList.add(mineFragment);
                    homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), fragmentList);
                    vpHome.setAdapter(homePagerAdapter);
                    vpHome.setCurrentItem(0, false);
            }
        }
    };



    @Override
    public void BindView() {

    }

    @Override
    public void setOnclickListener() {

    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void initToolBar() {
        
    }

    @Override
    public void initDatas() {
        Log.d("MA:","initing Data..");
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        retrofit_get(username);
    }

    @Override
    public void configViews() {
        Log.d("MA:","configuing views..");
        ivMall.setSelected(true);
        tvMall.setSelected(true);

        llyMall.setOnClickListener(this);
        llyMine.setOnClickListener(this);

        vpHome.setOffscreenPageLimit(2);
    }

    void bindView(){

    }

    void setListner(){
        query_btn.setOnClickListener(this);
        queryHistory_btn.setOnClickListener(this);
        buy_btn.setOnClickListener(this);
        showmore_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        setAllUnselected();
        switch (v.getId()){
            case R.id.lly_mall:// 商场
                ivMall.setSelected(true);
                tvMall.setSelected(true);
                vpHome.setCurrentItem(0, false);
                break;
            case R.id.lly_mine:// 我的
                ivMine.setSelected(true);
                tvMine.setSelected(true);
                vpHome.setCurrentItem(1, false);
                break;

        }
    }


    void toIntent(Context packageContext, Class<?> cls){
        Intent intent = new Intent(packageContext,cls);
        startActivity(intent);
    }

    void retrofit_get(String username){
        retrofit = RetrofitUtils.getRetrofit();
        Call<UserDetails> call = retrofit.create(HFApiService.class).getUserDetails(username);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if(response.isSuccessful()){
                    userDetails = response.body();
                    Log.d("tag",userDetails.toString());
                    Message msg = new Message();
                    msg.what = handlerMsg;
                    msg.obj = userDetails;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAllUnselected() {
        ivMall.setSelected(false);
        tvMall.setSelected(false);
        ivMine.setSelected(false);
        tvMine.setSelected(false);
    }
}
