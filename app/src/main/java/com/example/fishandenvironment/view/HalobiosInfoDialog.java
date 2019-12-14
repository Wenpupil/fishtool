package com.example.fishandenvironment.view;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fishandenvironment.R;
import com.example.fishandenvironment.bean.Biology;
import com.example.fishandenvironment.bean.Halobios;
import com.facebook.drawee.view.SimpleDraweeView;

import org.litepal.LitePal;

/**点击Circle弹出的Dialog框,关于物种的介绍
 * @author 方泽.Wenpupil
 * @version 1.0-2019.12.14
 */
public class HalobiosInfoDialog extends Dialog {

    private Halobios halobios;

    public HalobiosInfoDialog(Context context, Halobios halobios){
        super(context, R.style.dialog);
        this.halobios = halobios;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halobios_info_dialog);
        initView();
    }
    private void initView(){
        TextView name = findViewById(R.id.name);
        TextView namech = findViewById(R.id.namech);
        TextView latitude = findViewById(R.id.latitude);
        TextView longitude = findViewById(R.id.longitude);

        SimpleDraweeView image = findViewById(R.id.image);

        Biology biology = LitePal.where("name = ?",halobios.getName())
                .findFirst(Biology.class);
        //设置名字
        name.setText(biology.getName());
        namech.setText(biology.getNamech());
        //设置图片
        Uri uri = Uri.parse(biology.getImage());
        image.setImageURI(uri);
        //设置经纬度
        latitude.setText(String.valueOf(halobios.getLatitude()));
        longitude.setText(String.valueOf(halobios.getLongitude()));
    }
}
