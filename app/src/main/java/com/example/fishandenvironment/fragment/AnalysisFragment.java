package com.example.fishandenvironment.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.esri.arcgisruntime.data.FeatureCollection;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.example.fishandenvironment.R;
import com.example.fishandenvironment.bean.Halobios;
import com.example.fishandenvironment.util.ArcGisUtil;
import com.example.fishandenvironment.util.LogUtil;
import com.example.fishandenvironment.view.RulerView;
import com.githang.statusbar.StatusBarCompat;

import org.litepal.LitePal;

import java.util.List;

/**
 * @author 方泽.Wenpupil
 * @version 1.0-2019.12.07
 */
public class AnalysisFragment extends Fragment{

    private View view;
    //地图
    private MapView mapView;
    //更多选项
    private ImageView all;
    //底部tab
    private LinearLayout mainTab;
    //时间控件
    private RulerView rulerView;
    //ArcGis显示在地图上的特征集合管理
    private FeatureCollection featureCollection;
    //显示在地图上的点集链表
    private List<Halobios> halobios;
    private Activity activity;

    public AnalysisFragment() {
        // Required empty public constructor
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setAll(ImageView all) {
        this.all = all;
    }

    public void setMainTab(LinearLayout mainTab) {
        this.mainTab = mainTab;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_analysis,
                container, false);
        initView();
        initRulerView();
        return view;
    }
    private void initView(){
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        mapView = view.findViewById(R.id.mapview);
        rulerView = view.findViewById(R.id.rulerView);

        mapView.getGraphicsOverlays().add(graphicsOverlay);
        ArcGisUtil.setupMap(mapView);
        featureCollection = ArcGisUtil.addFeatureLayer(mapView);

        //设置状态栏颜色
        StatusBarCompat.setStatusBarColor(activity,
                activity.getResources().getColor(R.color.colorPrimaryDark));
    }
    //初始化RulerView内部接口
    private void initRulerView(){
        rulerView.setOnChooseResulterListener(new RulerView.OnChooseResulterListener() {
            @Override
            public void onEndResult(String year,String month) {
                //TODO:有问题处理图层问题
                LogUtil.d("AnalysisFragment","onEndResult = " + year + "." + month);
                //清空特征集，重新加载物种分布点集
                featureCollection.getTables().clear();
                loadPointData(year, month);
                halobios.clear();
            }

            @Override
            public void onScrollResult(String year,String month) {
                //LogUtil.d("AnalysisFragment","onScrollResult = " + year + "." + month);
            }
        });
    }

    private void loadPointData(String year, String month){
        String time = year + "/" + month + "/%";
        //获取新的点集数据
        halobios =LitePal
                .where("time like ?", time)
                .find(Halobios.class);
        //将新的点集数据 载入 特征集合图层 并且显示
        ArcGisUtil.createHalobiosPointTable(featureCollection,halobios);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        View.OnTouchListener onTouchListener = mapView.getOnTouchListener();
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mainTab.setVisibility(View.GONE);
                all.setVisibility(View.VISIBLE);
                return onTouchListener.onTouch(v,event);
            }
        });

    }
    @Override
    public void onPause() {
        Log.d("afragment","onPause");
        if (mapView != null) {
            mapView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d("afragment","onResume");
        super.onResume();
        if (mapView != null) {
            mapView.resume();
        }
    }

    @Override
    public void onDestroy() {
        Log.d("afragment","onDestroy");
        if (mapView != null) {
            mapView = null;
            //mapView.dispose();
        }
        super.onDestroy();
    }
}
