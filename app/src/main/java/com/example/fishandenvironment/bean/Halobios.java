package com.example.fishandenvironment.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
/**
 * 海洋生物分布数据
 * @author 方泽.Wenpupil
 * @version 1.0-2019.12.07
 * @serial this
 */
public class Halobios extends LitePalSupport implements Serializable {
    private String hid;          //生物编号
    private String name;        //生物俗名
    private String time;        //时间
    private double latitude;    //纬度
    private double longitude;   //经度
    private double radius;       //范围
    private int count;          //频率

    public void setHid(String hid) {
        this.hid = hid;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getHid() {
        return hid;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRadius() {
        return radius;
    }

    public int getCount() {
        return count;
    }
}
