package com.example.root.relicstest.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.root.relicstest.Activity.UpdatePersonInfoActivity;
import com.example.root.relicstest.Entities.UserDetails;
import com.example.root.relicstest.Interface.HFApiService;
import com.example.root.relicstest.R;
import com.example.root.relicstest.Utils.ConstantUtils;
import com.example.root.relicstest.Utils.RetrofitUtils;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MineFragment extends BaseFragment{

    @BindView(R.id.tv_setusername)
    TextView tv_setUsername;


    @BindView(R.id.tv_setAddress)
    TextView tv_setAddress;

    @BindView(R.id.tv_setOrg)
    TextView tv_setOrg;

    @BindView(R.id.tv_setSex)
    TextView tv_setSex;

    @BindView(R.id.tv_setTelephone)
    TextView tv_setTelephone;

    @BindView(R.id.tv_setValue)
    TextView tv_setValue;

    @BindView(R.id.civ_avatar)
    CircleImageView civ_avatar;

    UserDetails userDetails;


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {
        userDetails = (UserDetails)getArguments().getSerializable("userDetails");
        tv_setUsername.setText(userDetails.getName());
        tv_setAddress.setText(userDetails.getAddress());
        tv_setOrg.setText(userDetails.getOrganization());
        tv_setSex.setText(userDetails.getSex());
        tv_setValue.setText(userDetails.getBalance().toString());
        tv_setTelephone.setText(userDetails.getTelephone());
        Glide.with(this).load(ConstantUtils.BaseIMGURL + userDetails.getAvatar()).into(civ_avatar);
    }

    @Override
    public void configViews() {

    }

    @OnClick({R.id.tv_updateInfo})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_updateInfo:
                intent = new Intent(getActivity(),UpdatePersonInfoActivity.class);
        }
    }


}
