package com.example.fishandenvironment.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 物种
 * @author 方泽.Wenpupil
 * @version 1.0-2019.12.08
 * @serial this
 */
public class Biology extends LitePalSupport implements Serializable {
    private String name;    //物种名字
    private String namech;  //中文名字
    private String image;   //物种图片url
    private String content; //物种介绍

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getImage(){
        return image;
    }

    public String getNamech() {
        return namech;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(String image){
        this.image = image;
    }

    public void setNamech(String namech) {
        this.namech = namech;
    }
}
