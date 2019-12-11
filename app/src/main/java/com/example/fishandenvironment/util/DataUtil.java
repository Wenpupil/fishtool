package com.example.fishandenvironment.util;

import com.example.fishandenvironment.bean.Biology;
import com.example.fishandenvironment.bean.Halobios;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/** 测试的数据、数据的获取
 * @author 方泽.Wenpupil
 * @version 1.0-2019.12.08
 */
public class DataUtil {

    public static List<Halobios> getHalobiosData(String time,String mode){
        final Type type = new TypeToken<List<Halobios>>() {}.getType();
        List<Halobios> halobiosList = new ArrayList<>();
        Gson gson = new Gson();

        LogUtil.d("DataUtil","getHalobiosData_正在获取的物种数据time:" + time);
        String result = HttpUtil.requireHalobiosData(time,mode);
        if(result.equals("null")){
            LogUtil.d("DataUtil","getHalobiosData_null");
        }else{
            halobiosList = gson.fromJson(result, type);
        }
        return halobiosList;
    }
    public static List<Biology> biologyData(){
        List<Biology> biologies = new ArrayList<>();
        Biology one = new Biology();
        one.setName("a");
        biologies.add(one);

        Biology two = new Biology();
        two.setName("b");
        biologies.add(two);

        return biologies;
    }

    /**
     *
     * @param biologies 需要储存在手机数据库的 数据链表
     * @param mode 模式： 1.表示清空该表再添加，2.表示追加记录
     */
    public static void saveBiologiesData(List<Biology> biologies, int mode){
        LogUtil.d("DataUtil","saveBiologiesData_now");
        if(mode == 1){
            LitePal.deleteAll(Biology.class);
        }
        for(Biology biology:biologies){
            biology.save();
        }
    }
}
