package com.example.fishandenvironment.listener;

import android.content.Context;
import android.widget.Toast;

import com.example.fishandenvironment.bean.Halobios;
import com.example.fishandenvironment.util.LogUtil;
import com.example.fishandenvironment.view.HalobiosInfoDialog;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mapbox.mapboxsdk.plugins.annotation.Circle;
import com.mapbox.mapboxsdk.plugins.annotation.OnCircleClickListener;

/**自定义的CircleClickListener
 * @author 方泽.Wenpupil
 * @version 2019.12.14
 */
public class MyOnCircleClickListener implements OnCircleClickListener {

    private Context context;
    private Gson gson;
    private Halobios halobios;

    /**初始化
     * @param context 父类的环境变量
     */
    public MyOnCircleClickListener(Context context){
        this.context = context;
        gson = new Gson();
    }

    /**
     * 重写父类接口
     * @param circle 得到的Circle对象
     */
    @Override
    public void onAnnotationClick(Circle circle){
        //Circle标签带的json对象内容
        JsonElement result = circle.getData();
        LogUtil.d("CircleClick",result+"");
        halobios = gson.fromJson(result,Halobios.class);
        new HalobiosInfoDialog(context,halobios).show();
        //Toast.makeText(context, halobios.getName() + "," + halobios.getTime(), Toast.LENGTH_SHORT).show();
    }
}
