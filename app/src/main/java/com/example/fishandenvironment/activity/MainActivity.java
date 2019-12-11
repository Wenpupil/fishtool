package com.example.fishandenvironment.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fishandenvironment.App;
import com.example.fishandenvironment.R;
import com.example.fishandenvironment.fragment.AnalysisFragment;
import com.example.fishandenvironment.fragment.BiologyFragment;
import com.example.fishandenvironment.presenter.MainActivityPresenter;
import com.example.fishandenvironment.service.ProgramService;

public class MainActivity extends App implements View.OnClickListener{

    private MainActivityPresenter presenter;
    private LinearLayout tab;
    private ImageView all;

    private AnalysisFragment analysisFragment;
    private BiologyFragment biologyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPresenter();
        initView();
        initService();
    }
    private void initService(){
        Intent intent = new Intent(this, ProgramService.class);
        startService(intent);
    }
    private void initPresenter(){
        presenter = new MainActivityPresenter(this);
    }
    private void initView(){
        LinearLayout tab_left = findViewById(R.id.ll_tab1);
        LinearLayout tab_middle = findViewById(R.id.ll_tab2);
        LinearLayout tab_right = findViewById(R.id.ll_tab3);

        ImageView tab1 = findViewById(R.id.iv_tab1);
        ImageView tab2 = findViewById(R.id.iv_tab2);
        ImageView tab3 = findViewById(R.id.iv_tab3);

        TextView tvTabLeft = findViewById(R.id.tv_tab1);
        TextView tvTabMiddle = findViewById(R.id.tv_tab2);
        TextView tvTabRight = findViewById(R.id.tv_tab3);

        tab = findViewById(R.id.main_tab);
        all = findViewById(R.id.iv_all);

        all.setOnClickListener(this);
        tab_left.setOnClickListener(this);
        tab_middle.setOnClickListener(this);
        tab_right.setOnClickListener(this);

        //设置状态栏透明
        //StatusBarCompat.setStatusBarColor(this, Color.TRANSPARENT);
        presenter.initIvView(tab1,tab2,tab3);
        presenter.initTvView(tvTabLeft,tvTabMiddle,tvTabRight);
        select(0);
    }

    private void select(int i){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch(i){
            case 0:{
                if(analysisFragment == null){
                    analysisFragment = new AnalysisFragment();
                }
                analysisFragment.setActivity(this);
                analysisFragment.setAll(all);
                analysisFragment.setMainTab(tab);
                transaction.replace(R.id.fl_main,analysisFragment);
                break;
            }
            case 1:{
                if(biologyFragment == null){
                    biologyFragment = new BiologyFragment();
                }
                biologyFragment.setActivity(this);
                transaction.replace(R.id.fl_main,biologyFragment);
                break;
            }
            case 2:{
                break;
            }
        }
        presenter.select(i);
        transaction.commit();
    }
    public void onClick(View view){
        switch(view.getId()){
            case R.id.iv_all:{
                tab.setVisibility(View.VISIBLE);
                all.setVisibility(View.GONE);
                break;
            }
            case R.id.ll_tab1:{
                select(0);
                break;
            }
            case R.id.ll_tab2:{
                select(1);
                break;
            }
            case R.id.ll_tab3:{
                select(2);
                break;
            }
        }
    }
    @Override
    public void onDestroy(){
        Intent intent = new Intent(this,ProgramService.class);
        stopService(intent);
        super.onDestroy();
    }
}
