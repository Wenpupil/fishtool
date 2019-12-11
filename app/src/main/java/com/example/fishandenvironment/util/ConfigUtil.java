package com.example.fishandenvironment.util;

import com.example.fishandenvironment.R;

import org.litepal.LitePalApplication;

/*
 * @Author 方泽.Wenpupil
 * @Time 2019.12.08
 * @Description 默认配置信息
 */
public class ConfigUtil {
    //获取物种分布数据集 地址
    public static String getHalobiosDataUrl(){
        return LitePalApplication.getContext()
                .getString(R.string.http_data_halobios_url);
    }
    //获取版本控制，信息控制数据 地址
    public static String getProgramDataUrl(){
        return LitePalApplication.getContext()
                .getResources().getString(R.string.http_data_program_url);
    }
    //获取物种信息数据集 地址
    public static String getBiologyDataUrl(){
        return LitePalApplication.getContext()
                .getResources().getString(R.string.http_data_biology_url);
    }
}
