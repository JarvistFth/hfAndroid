package com.example.root.relicstest.Activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public abstract void BindView();

    public abstract void setOnclickListener();
}
