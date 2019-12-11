package com.example.fishandenvironment.bean;

import android.text.TextUtils;

import org.litepal.crud.LitePalSupport;
import java.io.Serializable;

/**
 * 程序数据信息，版本控制对像
 * @author 方泽.Wenpupil
 * @version 1.0-2019.12.10
 * @serial this
 */
public class Program extends LitePalSupport implements Serializable {

    //所有数据
    public final static int ALL = 1;
    //物种分布数据
    public final static int BIOLOGY = 2;
    //物种数据
    public final static int HALOBIOS = 3;
    //应用版本
    public final static int PROGRAM = 4;
    //都相同
    public final static int EQUAL = 5;
    //拥有所有数据
    public final static int FULL = 5;


    private String biology;
    private String halobios;
    private String program;

    public String getBiology() {
        return biology;
    }

    public String getHalobios() {
        return halobios;
    }

    public String getProgram() {
        return program;
    }

    public void setBiology(String biology) {
        this.biology = biology;
    }

    public void setHalobios(String halobios) {
        this.halobios = halobios;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    /**
     * @param programs 同类对象比较
     * @return 1-所有数据不同、2-物种数据不同
     *         3-物种分布数据不同、4-版本数据不同、5-都相同
     */
    public int compareTo(Program programs){
        if(!biology.equals(programs.getBiology()) && !halobios.equals(programs.getHalobios()) &&
                !program.equals(programs.getProgram())){
            return ALL;
        }
        if(!biology.equals(programs.getBiology())){
            return BIOLOGY;
        }

        if(!halobios.equals(programs.getHalobios())){
            return HALOBIOS;
        }

        if(!program.equals(programs.getProgram())){
            return PROGRAM;
        }
        return EQUAL;
    }

    /**
     *
     * @param mode 模式代表要修改的属性
     * @param pg 属性值
     */
    public void setObjectData(int mode, Program pg){
        switch (mode){
            case ALL:
                biology = pg.getBiology();
                halobios = pg.getHalobios();
                program = pg.getProgram();
                break;
            case BIOLOGY:
                biology = pg.getBiology();
                break;
            case HALOBIOS:
                halobios = pg.getHalobios();
                break;
            case PROGRAM:
                program = pg.getProgram();
                break;
        }
    }

    /**查找数据缺少
     * @return 1-缺少所有数据、2-缺少物种数据
     *         3-缺少物种分布数据、4-缺少版本数据、5-不缺数据
     */
    public int dataLackInfo(){
        if(TextUtils.isEmpty(biology) && TextUtils.isEmpty(halobios) && TextUtils.isEmpty(program)){
            return ALL;
        }
        if(TextUtils.isEmpty(biology)){
            return BIOLOGY;
        }
        if(TextUtils.isEmpty(halobios)){
            return HALOBIOS;
        }
        if(TextUtils.isEmpty(program)){
            return PROGRAM;
        }
        return FULL;
    }

    /**
     * @return 关于对象数据是否都有信息
     */
    public boolean isFullStatus(){
        return dataLackInfo() == FULL;
    }

    public String string(){
        return "\nbiology = " + biology + "\nhalobios = " + halobios + "\nprogram = " + program;
    }
}
