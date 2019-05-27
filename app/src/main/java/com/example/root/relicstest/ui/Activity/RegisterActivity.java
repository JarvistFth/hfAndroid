package com.example.root.relicstest.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.root.relicstest.Entities.ResponseCode;
import com.example.root.relicstest.Entities.UserRegister;
import com.example.root.relicstest.Interface.HFApiService;
import com.example.root.relicstest.R;
import com.example.root.relicstest.Utils.PhotoUtils;
import com.example.root.relicstest.Utils.RetrofitUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends BaseActivity {

    public static final int OPEN_ALBUM = 5;

    @BindView(R.id.et_upname)
    EditText et_upname;

    @BindView(R.id.et_uporg)
    EditText et_uporg;

    @BindView(R.id.et_upsex)
    EditText et_upsex;

    @BindView(R.id.et_upaddr)
    EditText et_upaddr;

    @BindView(R.id.et_uppassword)
    EditText et_uppassword;

    @BindView(R.id.et_uptel)
    EditText et_uptel;

    @BindView(R.id.civ_avatar_upload)
    CircleImageView civ_avatar_upload;

    @BindView(R.id.bt_register)
    Button bt_register;

    File file;
    String path;

    Retrofit retrofit;
    boolean registerok = false;
    UserRegister userRegister = new UserRegister();

    private static final int REGISTER_MSG = 1;


    @Override
    public void BindView() {

    }

    @Override
    public void setOnclickListener() {

    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_register;
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

    void checkpermission(){
        int presult = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if(presult != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            PhotoUtils.openAlbum(this,OPEN_ALBUM);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case OPEN_ALBUM:
                if(resultCode == RESULT_OK){
                    String path =  PhotoUtils.handleImageOnKitKat(data,this);
                    Log.d("path:",path);
                    PhotoUtils.displayImage(path,civ_avatar_upload,this);
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults){
        switch (requestCode){
            case 1: if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                PhotoUtils.openAlbum(this,OPEN_ALBUM);
            }
            else {
                Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
            }
                break;
            default:
                break;
        }
    }



    @OnClick({R.id.bt_register,R.id.civ_avatar_upload})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_register:
                retrofit_register();
                break;
            case R.id.civ_avatar_upload:
                checkpermission();
//                file = new File(path);
//                PhotoUtils.displayImage(path,civ_avatar_upload,this);
                break;
        }
    }


    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case REGISTER_MSG:
                    boolean ok = (boolean)msg.obj;
                    if(ok){
                        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                        intent.putExtra("username",userRegister.getName());
                        startActivity(intent);
                    }

            }
        }
    };

    private void retrofit_register(){
        userRegister.setAddress(et_upaddr.getText().toString());
        userRegister.setName(et_upname.getText().toString());
        userRegister.setOrganization(et_uporg.getText().toString());
        userRegister.setSex(et_upsex.getText().toString());
        userRegister.setPassword(et_uppassword.getText().toString());
        userRegister.setTelephone(et_uptel.getText().toString());
        retrofit = RetrofitUtils.getRetrofit();
        Call<ResponseCode> call = retrofit.create(HFApiService.class).userRegister(userRegister);
        call.enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if(response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,response.body().getMsg(),Toast.LENGTH_SHORT).show();
                    Message message = new Message();
                    registerok = true;
                    message.what = REGISTER_MSG;
                    message.obj = registerok;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                Message message = new Message();
                registerok = false;
                message.what = REGISTER_MSG;
                message.obj = registerok;
                handler.sendMessage(message);
            }
        });
    }
}
