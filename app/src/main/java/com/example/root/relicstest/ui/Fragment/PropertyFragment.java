package com.example.root.relicstest.Fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.example.root.relicstest.Activity.RelicsAllActivity;
import com.example.root.relicstest.Activity.RelicsTransferActivity;
import com.example.root.relicstest.Adapter.RelicsAdapter;
import com.example.root.relicstest.Entities.Relics;
import com.example.root.relicstest.Interface.HFApiService;
import com.example.root.relicstest.R;
import com.example.root.relicstest.Utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyFragment extends BaseFragment implements RelicsAdapter.RelicsAdapterListener {
    @Override
    public int getLayoutResId() {
        return R.layout.activity_relics_all;
    }

    @BindView(R.id.rv_relicsAll)
    RecyclerView rv_relicsall;

    @BindView(R.id.sr_lly)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<Relics> relicsList = new ArrayList<>();

    private RelicsAdapter adapter;

    private static final int UPDATE_RV = 2;

    String username;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_RV:
                    relicsList = (List<Relics>) msg.obj;
                    adapter.notifyDataSetChanged();
                    adapter.setClickLitener(PropertyFragment.this);
                    rv_relicsall.setAdapter(adapter);
            }
        }
    };

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent),getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.colorPrimaryDark));
        username = getArguments().getString("username");
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rv_relicsall.setLayoutManager(layoutManager);
        adapter = new RelicsAdapter(relicsList);
        retrofit_getall();
    }

    @Override
    public void configViews() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrofit_getall();
                swipeRefreshLayout.setRefreshing(false);
                Log.d("refresh:","refresh ok!");
            }
        });
    }


    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {

        }
    }

    void retrofit_getall(){
        Call<List<Relics>> call = RetrofitUtils.getRetrofit().create(HFApiService.class).getAllRelics();
        call.enqueue(new Callback<List<Relics>>() {
            @Override
            public void onResponse(Call<List<Relics>> call, Response<List<Relics>> response) {
                if(response.isSuccessful()){
                    Message msg = new Message();
                    msg.what = UPDATE_RV;
                    msg.obj = response.body();
                    handler.sendMessage(msg);
                    Log.d("Tag",response.body().toString());
                }
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
        Intent intent = new Intent(activity,RelicsTransferActivity.class);
        if(relics.getId()!=null)
        intent.putExtra("id",relics.getId());
        intent.putExtra("poster",relics.getPoster());
        if(username != null)
        intent.putExtra("username",username);
        startActivity(intent);
    }
}
