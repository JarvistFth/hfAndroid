package com.example.root.relicstest.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.root.relicstest.R;

import butterknife.OnClick;

public class PropertyFragment extends BaseFragment {
    @Override
    public int getLayoutResId() {
        return R.layout.activity_relics_all;
    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {
    }

    @OnClick({R.id.lly_wallet_manage, R.id.lly_msg_center
            , R.id.lly_system_setting, R.id.lly_trade_recode})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {

        }
    }
}
