package com.example.root.relicstest.Activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.root.relicstest.Entities.Relics;
import com.example.root.relicstest.Entities.ResponseCode;
import com.example.root.relicstest.Entities.UserDetails;
import com.example.root.relicstest.Interface.HFApiService;
import com.example.root.relicstest.R;
import com.example.root.relicstest.Utils.RetrofitUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UploadActivity extends BaseActivity {

    private EditText et_name,et_id,et_details,et_price;
    private ImageView iv_uploadPhoto;
    private Button btn_submit,btn_choosePhoto;
    Retrofit retrofit ;
    private UserDetails userDetails;
    private static final int CHOOSEPHOTO = 4;
    File file;

    @Override
    public int getLayoutID() {
        return R.layout.activity_upload;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initDatas() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userDetails = (UserDetails)bundle.getSerializable("user");
    }

    @Override
    public void configViews() {

    }



    @Override
    public void BindView() {
        et_name = findViewById(R.id.et_name);
        et_id = findViewById(R.id.et_id);
        et_details = findViewById(R.id.et_details);
        et_price = findViewById(R.id.et_price);
        iv_uploadPhoto = findViewById(R.id.iv_upload);
        btn_choosePhoto = findViewById(R.id.btn_upload);
        btn_submit = findViewById(R.id.btn_submit);
    }

    @Override
    public void setOnclickListener() {
        btn_submit.setOnClickListener(this);
        btn_choosePhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_upload:
                checkpermission();
                break;
            case R.id.btn_submit:
                if(file != null && file.exists()) {
                    retrofit_to();
                    retrofit_file(file);
                }
                break;
        }
    }

    void checkpermission(){
        int presult = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if(presult != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            openAlbum();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case CHOOSEPHOTO:
                if(resultCode == RESULT_OK){
                   handleImageOnKitKat(data);
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults){
        switch (requestCode){
            case 1: if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openAlbum();
            }
            else {
                Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
            }
                break;
            default:
                break;
        }
    }

    void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSEPHOTO);
    }

    void retrofit_to(){
        retrofit =  RetrofitUtils.getRetrofit();
        Relics relics = setRelics();
        Call<ResponseCode> call = retrofit.create(HFApiService.class).
                putRelics(userDetails.getName(),userDetails.getOrganization(),relics);
        call.enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UploadActivity.this,response.body().getCode() + response.body().getMsg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Toast.makeText(UploadActivity.this,"文物上架请求失败！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    void retrofit_file(File file){
        retrofit = RetrofitUtils.getRetrofit();
        if(!file.exists() || file == null){
            Log.d("tag", "retrofit_file: file not exist!!!");
            return;
        }
        Relics relics = setRelics();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        RequestBody rqFile = RequestBody.create(MediaType.parse("image/jpeg"),file);
        builder.addFormDataPart("file",file.getName(),rqFile);
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        Call<ResponseCode> uploadCall = retrofit.create(HFApiService.class).uploadPhoto(relics.getName(),multipartBody);
        uploadCall.enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                Log.d("tag","start call...");
                if (response.isSuccessful()){
                    Log.d("tag",response.body().toString());
                    if(response.body().getCode().equals("200")){
                        Toast.makeText(UploadActivity.this,"上传成功图片！",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(UploadActivity.this,response.body().getMsg(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.d("tag","response failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Toast.makeText(UploadActivity.this,"上传请求发送失败！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    Relics setRelics(){
        Relics relics = new Relics();
        if(et_price.getText()!=null && et_name.getText()!=null
                && et_details.getText()!=null && et_id.getText()!=null
                ){
            relics.setId(Integer.parseInt(et_id.getText().toString()));
            relics.setName(et_name.getText().toString());
            relics.setDetails(et_details.getText().toString());
            relics.setPoster(userDetails.getName());
            relics.setValue(Integer.parseInt(et_price.getText().toString()));
        }else{
            Toast.makeText(UploadActivity.this,"请输入全部信息",Toast.LENGTH_SHORT).show();
        }

        return relics;
    }



    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docID = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority()))
            {
                String id = docID.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" +id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }
            else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docID));
                imagePath = getImagePath(contentUri,null);
            }
        }
        else if("content".equalsIgnoreCase(uri.getScheme()))
        {
            imagePath = getImagePath(uri,null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
        file = new File(imagePath);
    }

    private String getImagePath(Uri uri, String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null && cursor.moveToFirst()){
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        cursor.close();
        return path;

    }


    private void displayImage(String imagePath){
        if(imagePath != null){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath,options);
            iv_uploadPhoto.setImageBitmap(bitmap);
        }
        else{
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }
}
