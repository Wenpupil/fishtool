package com.example.fishandenvironment.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.fishandenvironment.bean.Biology;
import com.example.fishandenvironment.bean.Halobios;
import com.example.fishandenvironment.bean.Program;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * http请求获取数据工具类
 * @author 方泽.Wenpupil
 * @version 1.0-2019.12.08
 */
public class HttpUtil {
    //异步查询服务器，应用程序的数据版本
    public static void requireProgram(Program program, Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("biology",program.getBiology())
                .add("halobios",program.getHalobios())
                .add("program",program.getProgram())
                .build();

        Request request = new Request.Builder()
                .url(ConfigUtil.getProgramDataUrl())
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //异步获取物种分布数据
    public static void requireHalobiosData(String time, Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("time",time)
                .build();
        Request request = new Request.Builder()
                .url(ConfigUtil.getHalobiosDataUrl())
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }
    //获取某一时段的物种分布信息，根据模式得到年，月，日数据集合 -- 同步
    public static String requireHalobiosData(String time,String mode){
        String result = "null";
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("mode",mode)
                .add("time",time)
                .build();
        Request request = new Request.Builder()
                .url(ConfigUtil.getHalobiosDataUrl())
                .post(requestBody)
                .build();
        Log.d("HttpUtil","requireHalobiosData_execute");
        try{
            Response response = client.newCall(request).execute();
            result = response.body().string();
        }catch (IOException e){
            Log.d("HttpUtil","requireHalobiosData_ioerror");
            e.printStackTrace();
        } catch (NullPointerException e){
            Log.d("HttpUtil","requireHalobiosData_nullpointerror");
            e.printStackTrace();
        }
        return result;
    }

    //TODO:获取物种分布数据 -- 需要改进
    public static void requestHalobiosData(){
        int year = 2008;
        int month = 1;
        for(int i = 0; i < 24; i++, month++) {
            if (month > 12) {
                year++;
                month = 1;
            }
            final int tyear = year;
            final int tmonth = month;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String time = tyear + "/" + tmonth + "/";
                    LogUtil.d("HttpUtil","time = " + time);
                    List<Halobios> halobios = DataUtil.getHalobiosData(time,"2");
                    saveHalobiosData(halobios);
                    halobios.clear();
                }
            }).start();
        }
    }
    private static void saveHalobiosData(List<Halobios> halobios){
        if(halobios.size() > 0)
            LogUtil.d("HttpUtil","requestHalobiosData_得到物种分布数据:"
                    + halobios.get(0).getTime());
        for(Halobios halobios1:halobios){
            halobios1.save();
        }
    }


    //TODO:获取物种信息数据
    public static void requestBiologyData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result =null;
                OkHttpClient client = new OkHttpClient();

                RequestBody requestBody = new FormBody.Builder()
                        .build();
                Request request = new Request.Builder()
                        .url(ConfigUtil.getBiologyDataUrl())
                        .post(requestBody)
                        .build();
                LogUtil.d("HttpUtil","requestBiologyData_execute");
                try{
                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                }catch (IOException e){
                    LogUtil.d("HttpUtil","requestBiologyData_io_error");
                    e.printStackTrace();
                } catch (NullPointerException e){
                    LogUtil.d("HttpUtil","requestBiologyData_null_point_error");
                    e.printStackTrace();
                }
                if(TextUtils.isEmpty(result)){
                    LogUtil.d("HttpUtil","requestBiologyData_result is null");
                }else{
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Biology>>(){}.getType();
                    List<Biology> biologies = gson.fromJson(result,type);
                    LogUtil.d("HttpUtil","program result:" + result);
                    DataUtil.saveBiologiesData(biologies,1);
                }
            }
        }).start();
    }
    //TODO:获取应用版本信息数据  -- 需要改进
    public static void requestProgramData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result =null;
                OkHttpClient client = new OkHttpClient();

                RequestBody requestBody = new FormBody.Builder()
                        .add("program","get")
                        .build();
                Request request = new Request.Builder()
                        .url(ConfigUtil.getProgramDataUrl())
                        .post(requestBody)
                        .build();
                LogUtil.d("HttpUtil","requestProgramData_execute");
                try{
                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                }catch (IOException e){
                    LogUtil.d("HttpUtil","requestProgramData_io_error");
                    e.printStackTrace();
                } catch (NullPointerException e){
                    LogUtil.d("HttpUtil","requestProgramData_null_point_error");
                    e.printStackTrace();
                }
                if(TextUtils.isEmpty(result)){
                    LogUtil.d("HttpUtil","requestProgramData_result is null");
                }else{
                    Gson gson = new Gson();
                    Program program = gson.fromJson(result,Program.class);
                    LogUtil.d("HttpUtil","program result:" + program.string());
                    program.save();
                }
            }
        }).start();
    }
    //TODO:获取所有数据
    public static void requestAllData(){
        requestProgramData();
        requestBiologyData();
        requestHalobiosData();
    }

    //TODO:更新物种分布数据
    public static void updateHalobiosData(){
    }
    //TODO:更新物种信息数据
    public static void updateBiologyData(){
    }
    //TODO:更新应用
    public static void updateProgram(){
    }
    //TODO:更新所有数据
    public static void updateAllData(){
    }
}
