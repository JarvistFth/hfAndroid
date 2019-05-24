package com.example.root.relicstest.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @BindView(R.id.iv_chain)
    ImageView ivChain;
    @BindView(R.id.tv_chain)
    TextView tvChain;
    @BindView(R.id.lly_chain)
    LinearLayout llyChain;

    Retrofit retrofit ;
    String username ;

    UserDetails userDetails;
    List<Relics> relicsList;

    private Button query_btn,queryHistory_btn,buy_btn,showmore_btn;

    private HomePagerAdapter homePagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        setListner();
    }

    @Override
    public void BindView() {

    }

    @Override
    public void setOnclickListener() {

    }

    @Override
    public int getLayoutID() {
        return 0;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initDatas() {
        Intent intent = getIntent();
        intent.getStringExtra("username");
        retrofit_get();
    }

    @Override
    public void configViews() {
        ivMall.setSelected(true);
        tvMall.setSelected(true);

        llyMall.setOnClickListener(this);
        llyMine.setOnClickListener(this);

        vpHome.setOffscreenPageLimit(2);
        List<Fragment> fragmentList = new ArrayList<>();

        PropertyFragment propertyFragment = new PropertyFragment();
        MineFragment mineFragment = new MineFragment();
        Bundle mineBundle = new Bundle();
        Bundle proBundle = new Bundle();
        if(userDetails!= null){
            mineBundle.putSerializable("userDetails",userDetails);
        }
        mineFragment.setArguments(mineBundle);

        fragmentList.add(propertyFragment);
//        fragmentList.add(new DiscoveryFragment());
        fragmentList.add(mineFragment);
        homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), fragmentList);
        vpHome.setAdapter(homePagerAdapter);
        vpHome.setCurrentItem(0, false);
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
        switch (v.getId()){
//            case R.id.Query:
//                Intent intent = new Intent(MainActivity.this,UploadActivity.class);
//                Bundle bundle = new Bundle();
//                UserDetails userDetails = new UserDetails();
//                userDetails.setName("jjw");
//                userDetails.setOrganization("Org1");
//                bundle.putSerializable("user",userDetails);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                break;
//            case R.id.QueryHistory:
//                break;
//            case R.id.RelicsTransfer:

//                toIntent(MainActivity.this,RelicsTransferActivity.class);

//                break;
//            case R.id.RelicsShow:
//                toIntent(MainActivity.this,RelicsAllActivity.class);
//                break;

        }
    }


    void toIntent(Context packageContext, Class<?> cls){
        Intent intent = new Intent(packageContext,cls);
        startActivity(intent);
    }

    void retrofit_get(){
        retrofit = RetrofitUtils.getRetrofit();
        Call<UserDetails> call = retrofit.create(HFApiService.class).getUserDetails(username);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if(response.isSuccessful()){
                    userDetails = response.body();
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
