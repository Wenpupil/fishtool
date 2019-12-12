package com.example.fishandenvironment.activity;

import com.example.fishandenvironment.bean.Biology;
import com.facebook.drawee.view.SimpleDraweeView;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fishandenvironment.R;
import com.githang.statusbar.StatusBarCompat;

/**显示鱼类数据活动
 * @author 方泽.Wenpupil
 * @version 1.0-2019.12.11
 */
public class BiologyInfoActivity extends AppCompatActivity implements View.OnClickListener{
    // Remove the below line after defining your own ad unit ID.

    private Biology biology;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biology_info);
        initView();
    }
    //初始化界面
    private void initView(){
        TextView name = findViewById(R.id.name);
        TextView namech = findViewById(R.id.namech);
        TextView content = findViewById(R.id.content);
        ImageView close = findViewById(R.id.left_img);

        SimpleDraweeView image = findViewById(R.id.image);

        biology = (Biology) getIntent().getSerializableExtra("biology");
        name.setText(biology.getName());
        namech.setText(biology.getNamech());
        content.setText(biology.getContent());

        Uri uri = Uri.parse(biology.getImage());
        image.setImageURI(uri);

        close.setOnClickListener(this);
        StatusBarCompat.setStatusBarColor(this,
                getResources().getColor(R.color.white));
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.left_img:
                finish();
                break;
            default:
                break;
        }
    }
}
