package com.example.fishandenvironment.activity;

import com.example.fishandenvironment.bean.Biology;
import com.facebook.drawee.view.SimpleDraweeView;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.fishandenvironment.R;
import com.githang.statusbar.StatusBarCompat;

/**显示鱼类数据活动
 * @author 方泽.Wenpupil
 * @version 1.0-2019.12.11
 */
public class BiologyInfoActivity extends AppCompatActivity {
    // Remove the below line after defining your own ad unit ID.

    private TextView name;
    private TextView namech;
    private TextView content;
    private SimpleDraweeView image;
    private Biology biology;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biology_info);
        initView();
    }

    private void initView(){
        name = findViewById(R.id.name);
        namech = findViewById(R.id.namech);
        content = findViewById(R.id.content);
        image = findViewById(R.id.image);

        biology = (Biology) getIntent().getSerializableExtra("biology");
        name.setText(biology.getName());
        namech.setText(biology.getNamech());
        content.setText(biology.getContent());

        Uri uri = Uri.parse(biology.getImage());
        image.setImageURI(uri);

        StatusBarCompat.setStatusBarColor(this,
                getResources().getColor(R.color.white));
    }
}
