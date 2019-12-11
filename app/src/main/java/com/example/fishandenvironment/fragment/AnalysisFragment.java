package com.example.fishandenvironment.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.fishandenvironment.R;
import com.example.fishandenvironment.bean.Halobios;
import com.example.fishandenvironment.util.LogUtil;
import com.example.fishandenvironment.util.MapUtil;
import com.example.fishandenvironment.view.RulerView;
import com.githang.statusbar.StatusBarCompat;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

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
    private MapboxMap mapboxMap;
    //更多选项
    private ImageView all;
    //底部tab
    private LinearLayout mainTab;
    //时间控件
    private RulerView rulerView;
    //显示在地图上的点集链表
    private List<Halobios> halobios;
    private MapUtil mapUtil;
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
        mapUtil = new MapUtil();
        initView();
        initRulerView();
        return view;
    }
    private void initView(){
        mapView = view.findViewById(R.id.mapview);
        rulerView = view.findViewById(R.id.rulerView);

        mapUtil.initMapView(mapView);
        //设置状态栏颜色
        halobios =LitePal
                .where("time like ?","2008/1/%")
                .find(Halobios.class);
        LogUtil.d("AnalysisFragment","halobios = " + halobios.size());
        mapUtil.addGeoJsonToMapView(mapView,halobios);
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
                loadPointData(year, month);
            }
            @Override
            public void onScrollResult(String year,String month) {
                //LogUtil.d("AnalysisFragment","onScrollResult = " + year + "." + month);
            }
        });
        rulerView.computeScrollTo(2008f);
    }

    private void loadPointData(String year, String month){
        String time = year + "/" + month + "/%";
        //获取新的点集数据
        halobios =LitePal
                .where("time like ?", time)
                .find(Halobios.class);
        mapUtil.addGeoJsonToMapView(mapView,halobios);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mapViewListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        Log.d("afragment","onResume");
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        Log.d("afragment","onPause");
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onDestroy() {
        Log.d("afragment","onDestroy");
        super.onDestroy();
        mapView.onDestroy();
    }
    //关于点击MapView行为
    private void mapViewListener(){
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.addOnMoveListener(new MapboxMap.OnMoveListener() {
                    @Override
                    public void onMoveBegin(@NonNull MoveGestureDetector detector) {
                        mainTab.setVisibility(View.GONE);
                        all.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onMove(@NonNull MoveGestureDetector detector) {
                    }
                    @Override
                    public void onMoveEnd(@NonNull MoveGestureDetector detector) {
                    }
                });
                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public boolean onMapClick(@NonNull LatLng point) {
                        mainTab.setVisibility(View.GONE);
                        all.setVisibility(View.VISIBLE);
                        return false;
                    }
                });
            }
        });
    }
}
