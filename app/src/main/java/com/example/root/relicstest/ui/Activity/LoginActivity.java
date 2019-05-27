package com.example.root.relicstest.Activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.relicstest.Entities.ResponseCode;
import com.example.root.relicstest.Entities.UserLogin;
import com.example.root.relicstest.Interface.HFApiService;
import com.example.root.relicstest.R;
import com.example.root.relicstest.Utils.RetrofitUtils;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.tv_register)
    TextView tv_regsiter;

    @BindView(R.id.bt_login)
    Button bt_login;

    @BindView(R.id.et_username)
    EditText et_username;

    @BindView(R.id.et_password)
    EditText et_password;

    Retrofit retrofit;

    boolean loginok = false;

    @OnClick({R.id.tv_register,R.id.bt_login})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bt_login:
                retrofit_login();
                break;
        }
    }

    @Override
    public void BindView() {

    }

    @Override
    public void setOnclickListener() {

    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {

    }

    boolean retrofit_login(){
        Log.d("tag","retrofit start...");
        if(et_username.getText()==null && et_password == null){
            Toast.makeText(this,"please ensure username and password is not null",Toast.LENGTH_SHORT).show();
            return false;
        }
        retrofit = RetrofitUtils.getRetrofit();
        UserLogin userLogin = new UserLogin();
        userLogin.setName(et_username.getText().toString());
        userLogin.setPassword(et_password.getText().toString());
        Call<ResponseCode> call  =retrofit.create(HFApiService.class).userLogin(userLogin);
        call.enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if(response.isSuccessful()){
                    Log.d("tag:",response.body().getCode());
                    if (response.body().getCode().equals("200")){
                        Intent intent2 = new Intent(LoginActivity.this,MainActivity.class);
                        intent2.putExtra("username",et_username.getText().toString());
                        Log.d("tag",et_username.getText().toString());
                        startActivity(intent2);
                        finish();                    }
                    Toast.makeText(LoginActivity.this,response.body().getMsg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                loginok = false;
                Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        return loginok;
    }
}
