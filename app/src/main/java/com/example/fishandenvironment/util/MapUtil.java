package com.example.fishandenvironment.util;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.fishandenvironment.R;
import com.example.fishandenvironment.bean.Halobios;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Circle;
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager;
import com.mapbox.mapboxsdk.plugins.annotation.CircleOptions;
import com.mapbox.mapboxsdk.plugins.annotation.OnCircleClickListener;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.layers.TransitionOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import static com.mapbox.mapboxsdk.style.expressions.Expression.all;
import static com.mapbox.mapboxsdk.style.expressions.Expression.division;
import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gte;
import static com.mapbox.mapboxsdk.style.expressions.Expression.has;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.rgb;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.toNumber;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;

/**管理和设置MapView
 * @author 方泽.Wenpupil
 * @version 1.0-2019.12.11
 */
public class MapUtil {

    private MapView mapView;
    private CircleManager circleManager;
    public MapUtil(MapView mapView){
        this.mapView = mapView;
    }

    /**初始化MapView控件
     */
    public void initMapView(){
        MapboxMap map;
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        style.setTransition(new TransitionOptions(0, 0, false));
                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                -30, 125.0), 2));
                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                    }
                });
            }
        });
    }

    /**物种分布打包成GeoJson
     * @param halobiosList 物种分布集合
     * @return 生成地理点FeatureCollection
     */
    private FeatureCollection halobiosListToGeoJson(List<Halobios> halobiosList){
        List<Feature> features = new ArrayList<>();

        for(Halobios halobios:halobiosList){
            Point point = Point.fromLngLat(halobios.getLongitude(),
                    halobios.getLatitude());
            Feature feature = Feature.fromGeometry(point);
            features.add(feature);
        }
        return FeatureCollection.fromFeatures(features);
    }

    /**物种分布打包成不同特征的features集合 -- setMarkerManagerData中使用
     * @param halobiosList 物种分布集合
     * @return 生成地理点FeatureCollection
     */
    private List<CircleOptions> halobiosListToFeatures(List<Halobios> halobiosList){
        List<CircleOptions> circleOptionsList = new ArrayList<>();
        Gson gson = new Gson();
        for(Halobios halobios:halobiosList){
            LatLng latLng = new LatLng(halobios.getLatitude(),halobios.getLongitude());
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.withLatLng(latLng);

            JsonElement jsonElement = gson.toJsonTree(halobios);
            circleOptions.withData(jsonElement);
            circleOptions.withCircleColor("#FF9800");
            circleOptions.withCircleRadius(6.5f);
            circleOptionsList.add(circleOptions);
        }
        return circleOptionsList;
    }
    /**在地图上创建群集图层并初始化
     * @param halobiosList 第一次需要显示的物种分布集合
     */
    public void createClusterLayerInMap(List<Halobios> halobiosList){
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.getStyle(style -> {
                setClusterHalobiosSource(halobiosList);
                createClusteredLayer(style);
            });
        });
    }

    /**在地图上创建点集图层并初始化
     * @param halobiosList 第一次需要显示的物种分布集合
     */
    public void createMarkerLayerInMap(List<Halobios> halobiosList){
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.getStyle(style -> {
                setMarkerHalobiosSource(halobiosList);
                createMarkerLayer(style);
            });
        });
    }

    //创建物种分布群集图层
    private void createClusteredLayer(@NonNull Style style) {
        LogUtil.d("MapUtil","add source");
        //Creating a marker layer for single data points
        SymbolLayer unclustered = new SymbolLayer("custom-unclustered-points", "cluster-halobios");
        unclustered.setProperties(
                iconImage("cross-icon-id"),
                iconSize(
                        division(
                                get("mag"), literal(4.0f)
                        )
                ),
                iconColor(
                        interpolate(exponential(1), get("mag"),
                                stop(2.0, rgb(0, 255, 0)),
                                stop(4.5, rgb(0, 0, 255)),
                                stop(7.0, rgb(255, 0, 0))
                        )
                )
        );
        unclustered.setFilter(Expression.has("mag"));
        style.addLayer(unclustered);
        // Use the earthquakes GeoJSON source to create three layers: One layer for each cluster category.
        // Each point range gets a different fill color.
        int[][] layers = new int[][] {
                new int[] {150, ContextCompat.getColor(LitePalApplication.getContext(), R.color.mapboxRed)},
                new int[] {20, ContextCompat.getColor(LitePalApplication.getContext(), R.color.mapboxGreen)},
                new int[] {0, ContextCompat.getColor(LitePalApplication.getContext(), R.color.mapbox_blue)}
        };
        for (int i = 0; i < layers.length; i++) {
            //Add clusters' circles
            CircleLayer circles = new CircleLayer("custom-cluster-" + i, "cluster-halobios");
            circles.setProperties(
                    circleColor(layers[i][1]),
                    circleRadius(18f)
            );
            Expression pointCount = toNumber(get("point_count"));
            // Add a filter to the cluster layer that hides the circles based on "point_count"
            circles.setFilter(
                    i == 0
                            ? all(has("point_count"),
                            gte(pointCount, literal(layers[i][0]))
                    ) : all(has("point_count"),
                            gte(pointCount, literal(layers[i][0])),
                            lt(pointCount, literal(layers[i - 1][0]))
                    )
            );
            style.addLayer(circles);
        }
        //Add the count labels
        SymbolLayer count = new SymbolLayer("custom-count", "cluster-halobios");
        count.setProperties(
                textField(Expression.toString(get("point_count"))),
                textSize(12f),
                textColor(Color.WHITE),
                textIgnorePlacement(true),
                textAllowOverlap(true)
        );
        style.addLayer(count);
    }
    private void createMarkerLayer(@NonNull Style style){
        CircleLayer circleLayer = new CircleLayer("custom-marker-halobios", "marker-halobios");
        circleLayer.setProperties(
                circleColor(
                        LitePalApplication.getContext()
                                .getResources().getColor(R.color.haolobios_point_color)
                )
        );
        style.addLayer(circleLayer);
    }

    //TODO:添加栅格图层到MapView中
    public void addRasterLayer(){
    }

    //设置群集图层的数据源，通过改变GeoJson来改变图层显示
    public void setClusterHalobiosSource(List<Halobios> halobios){
        FeatureCollection featureCollection = halobiosListToGeoJson(halobios);
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.getStyle(style -> {
                if(style.getSource("cluster-halobios") != null){
                    GeoJsonSource geoJsonSource = style.getSourceAs("cluster-halobios");
                    geoJsonSource.setGeoJson(featureCollection);
                }else{
                    style.addSource(new GeoJsonSource("cluster-halobios",featureCollection,
                            new GeoJsonOptions()
                            .withCluster(true)
                            .withClusterMaxZoom(14)
                            .withClusterRadius(50)));
                }
            });
        });
    }
    //设置点集图层的数据于，通过改变GeoJson来改变图层的显示
    public void setMarkerHalobiosSource(List<Halobios> halobios){
        FeatureCollection featureCollection = halobiosListToGeoJson(halobios);
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.getStyle(style -> {
                if(style.getSource("marker-halobios") != null){
                    GeoJsonSource geoJsonSource = style.getSourceAs("marker-halobios");
                    geoJsonSource.setGeoJson(featureCollection);
                }else{
                    style.addSource(new GeoJsonSource("marker-halobios",featureCollection));
                }
            });
        });
    }

    //移除MapView中的所有图层
    public void removeAllLayers(){
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.getStyle(style -> {
                List<Layer> layerList = style.getLayers();
                for(Layer layer:layerList){
                    String id = layer.getId();
                    if(id.contains("custom")) {
                        //LogUtil.d("MapUtil","removed " + id);
                        style.removeLayer(layer);
                    }
                }
            });
        });
    }

    public void createMarkerManagerInMap(){
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.getStyle(style -> {
                circleManager = new CircleManager(mapView,mapboxMap,style);
                circleManager.addClickListener(new OnCircleClickListener() {
                    @Override
                    public void onAnnotationClick(Circle circle) {
                        LogUtil.d("CircleClick",circle.getData()+"");
                    }
                });
            });
        });
    }
    public void setMarkerManagerData(List<Halobios> halobiosList){
        List<CircleOptions> circleOptionsList= halobiosListToFeatures(halobiosList);
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.getStyle(style -> {
                circleManager.deleteAll();
                circleManager.create(circleOptionsList);
            });
        });
    }
    public void removeAllMarkerData(){
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.getStyle(style -> {
                circleManager.deleteAll();
            });
        });
    }
}
