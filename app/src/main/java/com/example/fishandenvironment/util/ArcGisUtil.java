package com.example.fishandenvironment.util;

import android.graphics.Color;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureCollection;
import com.esri.arcgisruntime.data.FeatureCollectionTable;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureCollectionLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.BackgroundGrid;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.example.fishandenvironment.bean.Halobios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**关于ArcGis操作封装
 * @author 方泽.Wenpupil
 * @version 1.0-2019.12.05
 */
public class ArcGisUtil {


    //设置ArcGis底图
    public static void setupMap(MapView mapView){
        setLicense();
        if(mapView != null){
            Basemap.Type basemapType = Basemap.Type.STREETS;
            double latitude = -20.0;
            double longitude = 130.0;
            int levelOfDetail = 3;
            ArcGISMap arcGISMap = new ArcGISMap(basemapType, latitude, longitude, levelOfDetail);
            mapView.setMap(arcGISMap);
            deleteBottomLabel(mapView);
            deleteGrid(mapView);
        }
    }

    //去除网格
    private static void deleteGrid(MapView mapView){
        BackgroundGrid backgroundGrid = new BackgroundGrid();
        backgroundGrid.setColor(0xf6f6f6f6);
        backgroundGrid.setGridLineWidth(0);
        mapView.setBackgroundGrid(backgroundGrid);
    }

    //设置ArcGis许可
    private static void setLicense(){
        ArcGISRuntimeEnvironment
                .setLicense("runtimelite,1000,rud4781252065,none,RP5X0H4AH44SJ9HSX152");
    }

    //去除底部水印
    private static void deleteBottomLabel(MapView mapView){
        mapView.setAttributionTextVisible(false);
    }

    //加载底图
    public static void loadBottomMap(MapView mapView, String url){
        ArcGISMap arcGISMap = new ArcGISMap();
        //底图地址
        TileCache mainTileCache = new TileCache(url);
        ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(mainTileCache);
        Basemap basemap = new Basemap(tiledLayer);
        arcGISMap.setBasemap(basemap);
        mapView.setMap(arcGISMap);
    }

    //添加特征集
    public static FeatureCollection addFeatureLayer(MapView mapView){
        FeatureCollection featureCollection = new FeatureCollection();
        FeatureCollectionLayer featureCollectionLayer = new FeatureCollectionLayer(featureCollection);
        mapView.getMap().getOperationalLayers().add(featureCollectionLayer);
        return featureCollection;
    }

    //创建海洋生物特征集
    public static void createHalobiosPointTable(FeatureCollection featureCollection,
                                                List<Halobios> halobios){
        List<Feature> features = new ArrayList<>();
        List<Field> pointFields = new ArrayList<>();

        //创建点模式
        pointFields.add(Field.createString("name", "count", 50));
        FeatureCollectionTable pointsTable = new FeatureCollectionTable(pointFields,
                GeometryType.POINT, SpatialReferences.getWgs84());
        //设置点的属性：形状，颜色，大小
        SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(
                SimpleMarkerSymbol.Style.CIRCLE, 0xFFCF512F, 3);
        SimpleRenderer renderer = new SimpleRenderer(simpleMarkerSymbol);
        pointsTable.setRenderer(renderer);
        featureCollection.getTables().add(pointsTable);

        for(Halobios one : halobios){
            Map<String, Object> attributes = new HashMap<>();
            attributes.put(pointFields.get(0).getName(), one.getName());
            attributes.put(pointFields.get(0).getAlias(), one.getCount());
            Point point = new Point(one.getLongitude(), one.getLatitude(),
                    SpatialReferences.getWgs84());
            features.add(pointsTable.createFeature(attributes, point));
        }
        //将点集异步显示在图层上
        pointsTable.addFeaturesAsync(features);
    }
    public static void createPointGraphics(GraphicsOverlay mGraphicsOverlay,Halobios one) {
        Point point = new Point(one.getLongitude(), one.getLatitude(),
                SpatialReferences.getWgs84());
        SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol
                (SimpleMarkerSymbol.Style.CIRCLE,
                        Color.rgb(255,152,0), 4.0f);
        pointSymbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID,
                Color.rgb(255,152,0), (float)one.getRadius()*4000));
        Graphic pointGraphic = new Graphic(point, pointSymbol);
        mGraphicsOverlay.getGraphics().add(pointGraphic);
    }
}
