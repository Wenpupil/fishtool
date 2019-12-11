package com.example.fishandenvironment.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.example.fishandenvironment.bean.Program;
import com.example.fishandenvironment.util.HttpUtil;
import com.example.fishandenvironment.util.LogUtil;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 程序服务组件，控制应用数据信息更新、程序版本
 * @author 方泽.Wenpupil
 * @version 1.0-2019.12.10
 */
public class ProgramService extends Service {

    private Gson gson;
    private Program program;

    class ProgramServiceCallback implements Callback{
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            LogUtil.d("ProgramService","queryService_failure");
        }
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String result = response.body().string();
            Program serviceData = gson.fromJson(result,Program.class);
            handleInfo(serviceData);
        }
    }

    public ProgramService() {
        //获取存于手机数据库sqlite3的数据与版本信息
        program = LitePal.find(Program.class, 1);
        gson = new Gson();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flag, int startId){
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        task();
        //每60秒重新创建该服务--在应用生命周期中，该服务始终存在
        int longTime = 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime()+longTime;
        Intent i = new Intent(this,ProgramService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent,flag,startId);
    }
    //该类主要任务
    private void task(){
        LogUtil.d("ProgramService","task_now");
        if(program == null){
            requestData(Program.ALL);
        }else if(!program.isFullStatus()){
            requestData(program.dataLackInfo());
        }else{
            //获取服务器的数据与版本信息
            HttpUtil.requireProgram(program, new ProgramServiceCallback());
        }
    }

    /**
     * 数据请求控制函数-控制器
     * @param mode 表示模式: 1-获取所有数据、2-获取物种数据、3-获取物种分布数据、4-获取版本数据
     */
    private void requestData(int mode){
        LogUtil.d("ProgramService","requestData: mode = " + mode);
        switch(mode){
            case Program.ALL:
                HttpUtil.requestAllData();          break;
            case Program.BIOLOGY:
                HttpUtil.requestBiologyData();      break;
            case Program.HALOBIOS:
                HttpUtil.requestHalobiosData();     break;
            case Program.PROGRAM:
                HttpUtil.requestProgramData();      break;
            default:
                LogUtil.d("ProgramService","requestData: Unknown Value");
        }
    }
    /**
     * 数据更新控制函数
     * @param mode 表示模式: 1-更新所有数据、2-更新物种数据、3-更新物种分布数据、4-更新应用信息和应用
     */
    private void updateData(int mode){
        LogUtil.d("ProgramService","updateData: mode = " + mode);
        switch(mode){
            case Program.ALL:
                HttpUtil.updateAllData();       break;
            case Program.BIOLOGY:
                HttpUtil.updateBiologyData();   break;
            case Program.HALOBIOS:
                HttpUtil.updateHalobiosData();  break;
            case Program.PROGRAM:
                HttpUtil.updateProgram();       break;
        }
    }

    /**
     * 处理版本信息类对象
     * @param serviceData 服务器的版本控制数据
     */
    private void handleInfo(Program serviceData){
        //得到版本对比信息
        int info = serviceData.compareTo(program);
        LogUtil.d("ProgramService","handleInfo_now");
        if(info != Program.EQUAL){
            updateData(info);
            program.setObjectData(info,serviceData);
            program.save();
            //递归直至版本数据一致
            handleInfo(serviceData);
        }
    }
}
