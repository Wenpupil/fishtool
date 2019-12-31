package com.example.fishandenvironment.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.example.fishandenvironment.R;
import com.example.fishandenvironment.bean.Biology;
import com.example.fishandenvironment.bean.Halobios;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;


/**显示直方图对话框
 * @author 方泽.Wenpupil
 * @version 1.0-2019.12.12
 */

//TODO:需要大的修改
public class BarLineDialog extends Dialog {

    private BarChart barChart;
    private BarData barData;
    private Biology biology;

    public BarLineDialog(Context context,Biology biology){
        super(context,R.style.dialog);
        this.biology = biology;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.barline_dialog);
        initView();
        initData();
    }
    private void initView(){
        barChart = findViewById(R.id.chart);

        //条形直方图 设置值在条形上
        barChart.setDrawValueAboveBar(true);
        //设置x,y缩放
        barChart.setPinchZoom(false);
        //不画网格线
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置x不绘制格线
        xAxis.setDrawGridLines(false);
        //设置间隔
        xAxis.setGranularity(1f);
        //设置绘制标签
        xAxis.setDrawLabels(true);
        //自定义的标签
        final String[] quarters = new String[] {"一月", "二月", "三月", "四月","五月", "六月",
                "七月", "八月", "九月", "十月", "十一月","十二月"};
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return quarters[(int) value - 1];
            }
        };
        xAxis.setValueFormatter(formatter);
        //设置标签数量
        xAxis.setLabelCount(12);
        //设置标签文本字体大小
        xAxis.setTextSize(7f);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawGridLines(false);
        //设置最大值与顶部的高度 百分比
        leftAxis.setSpaceTop(15f);
        leftAxis.setTextSize(8f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        //禁用此轴
        rightAxis.setEnabled(false);

    }
    private void initData(){
        String name = biology.getName();
        List<BarEntry> barEntries = new ArrayList<>();
        for(int i = 1; i <= 12; i++){
            String time = "2008/"+ i +"/%";

            List<Halobios> halobiosList = LitePal
                    .where("time like ? and name = ?",time,name)
                    .order("time")
                    .find(Halobios.class);
            int total = 0;
            for(Halobios halobios:halobiosList){
                total += halobios.getCount();
            }
            //LogUtil.d("BarLineDialog","total = " + total + ",time = " + time);
            BarEntry barEntry = new BarEntry(i,total);
            barEntries.add(barEntry);
        }
        BarDataSet barDataSet = new BarDataSet(barEntries,biology.getNamech());
        int[] colors = new int[]{R.color.color_1,R.color.color_2,R.color.color_3,R.color.color_4,
            R.color.color_5,R.color.color_6,R.color.color_7,R.color.color_8,
            R.color.color_9,R.color.color_10,R.color.color_11,R.color.color_12};
        barDataSet.setColors(colors,LitePalApplication.getContext());
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        barChart.setData(barData);
        barChart.invalidate();
    }
}
