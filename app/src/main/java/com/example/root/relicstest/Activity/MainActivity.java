package com.example.root.relicstest.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.root.relicstest.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button query_btn,queryHistory_btn,buy_btn,showmore_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        setListner();
    }

    void bindView(){
        query_btn = findViewById(R.id.Query);
        queryHistory_btn = findViewById(R.id.QueryHistory);
        buy_btn = findViewById(R.id.RelicsTransfer);
        showmore_btn = findViewById(R.id.RelicsShow);
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
            case R.id.Query:
                break;
            case R.id.QueryHistory:
                break;
            case R.id.RelicsTransfer:
                toIntent(MainActivity.this,RelicsTransferActivity.class);
                break;
            case R.id.RelicsShow:
                toIntent(MainActivity.this,RelicsAllActivity.class);
                break;
        }
    }


    void toIntent(Context packageContext, Class<?> cls){
        Intent intent = new Intent(packageContext,cls);
        startActivity(intent);
    }
}
