package com.example.root.relicstest.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.root.relicstest.R;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public Toolbar mCommonToolBar;
    protected Context mContext;

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        mContext = this;
        ImmersionBar.with(this).init();
        unbinder = ButterKnife.bind(this);

        mCommonToolBar = findViewById(R.id.common_toolbar);
        if(mCommonToolBar != null){
            ImmersionBar.with(this)
                    .titleBar(mCommonToolBar, false)
                    .transparentStatusBar()
                    .statusBarDarkFont(true, 1f)
                    .navigationBarColor(R.color.white)
                    .init();
            initToolBar();
            setSupportActionBar(mCommonToolBar);
        }
        BindView();
        setOnclickListener();
        initDatas();
        configViews();

    }

    public abstract void BindView();

    public abstract void setOnclickListener();

    public abstract int getLayoutID();

    public abstract void initToolBar();

    public abstract void initDatas();

    /**
     * 对各种控件进行设置、适配、填充数据
     */
    public abstract void configViews();


}
