package com.example.fishandenvironment.util;

import android.util.Log;

/*
 * @Author Wenpupil
 * @Description 日志工具类
 */
public class LogUtil {
    private static final int DEBUG=1;
    private static final int INFO=2;
    private static final int WARN=3;
    private static final int ERROR=4;
    private static final int LEVEL=1;

    public static void d(String tag,String msg){
        if(DEBUG<=LEVEL){
            Log.d(tag,msg);
        }
    }
    public static void i(String tag,String msg){
        if(INFO<=LEVEL){
            Log.i(tag,msg);
        }
    }
    public static void w(String tag,String msg){
        if(WARN<=LEVEL){
            Log.w(tag,msg);
        }
    }
    public static void e(String tag,String msg){
        if(ERROR<=LEVEL){
            Log.e(tag,msg);
        }
    }
}
