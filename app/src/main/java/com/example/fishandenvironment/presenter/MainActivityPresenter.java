package com.example.fishandenvironment.presenter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fishandenvironment.R;

public class MainActivityPresenter {
    private static int lastSelect;

    private ImageView ivTabLeft;
    private ImageView ivTabMiddle;
    private ImageView ivTabRight;

    private TextView tvTabLeft;
    private TextView tvTabMiddle;
    private TextView tvTabRight;

    private Context context;

    public MainActivityPresenter(Context context){
        this.context = context;
    }

    //底部tab图标初始化
    public void initIvView(ImageView tab1, ImageView tab2, ImageView tab3){
        ivTabLeft = tab1;
        ivTabMiddle = tab2;
        ivTabRight = tab3;
    }
    //底部tab文字初始化
    public void initTvView(TextView tab1, TextView tab2, TextView tab3){
        tvTabLeft = tab1;
        tvTabMiddle = tab2;
        tvTabRight = tab3;
    }
    //底部tab切换效果逻辑
    public void select(int i){
        //取消上次选中
        unSelect(lastSelect);
        switch(i){
            case 0:{
                ivTabLeft.setImageResource(R.mipmap.main_tab_left_in);
                tvTabLeft.setTextColor(context.getResources().getColor(
                        R.color.main_tab_textColor_in
                ));
                break;
            }
            case 1:{
                ivTabMiddle.setImageResource(R.mipmap.main_tab_middle_in);
                tvTabMiddle.setTextColor(context.getResources().getColor(
                        R.color.main_tab_textColor_in
                ));
                break;
            }
            case 2:{
                ivTabRight.setImageResource(R.mipmap.main_tab_right_in);
                tvTabRight.setTextColor(context.getResources().getColor(
                        R.color.main_tab_textColor_in
                ));
                break;
            }
        }
        lastSelect = i;
    }
    private void unSelect(int i){
        switch (i){
            case 0:{
                ivTabLeft.setImageResource(R.mipmap.main_tab_left);
                tvTabLeft.setTextColor(context.getResources().getColor(
                        R.color.main_tab_textColor
                ));
                break;
            }
            case 1:{
                ivTabMiddle.setImageResource(R.mipmap.main_tab_middle);
                tvTabMiddle.setTextColor(context.getResources().getColor(
                        R.color.main_tab_textColor
                ));
                break;
            }
            case 2:{
                ivTabRight.setImageResource(R.mipmap.main_tab_right);
                tvTabRight.setTextColor(context.getResources().getColor(
                        R.color.main_tab_textColor
                ));
                break;
            }
        }
    }
}
