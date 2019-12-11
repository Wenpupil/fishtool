package com.example.fishandenvironment.util;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.fishandenvironment.R;
import com.example.fishandenvironment.bean.Halobios;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
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
    public MapUtil(){
    }

    /**初始化MapView控件
     * @param mapView 地图View
     */
    public void initMapView(MapView mapView){
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

    /**物种分布打包成Geo
     * @param halobiosList 物种分布集合
     * @return 生成地理点
     */
    private List<Feature> createPointListWithHalobios(List<Halobios> halobiosList){
        List<Feature> features = new ArrayList<>();

        for(Halobios halobios:halobiosList){
            Point point = Point.fromLngLat(halobios.getLongitude(),
                    halobios.getLatitude());
            Feature feature = Feature.fromGeometry(point);
            features.add(feature);
        }
        return features;
    }

    /**
     *
     * @param mapView 地图控件
     * @param halobiosList 需要显示的物种分布集合
     */
    public void addGeoJsonToMapView(MapView mapView, List<Halobios> halobiosList){
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.getStyle(new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        List<Feature> features = createPointListWithHalobios(halobiosList);
                        FeatureCollection featureCollection = FeatureCollection
                                .fromFeatures(features);

                        String result = featureCollection.toJson();
                        LogUtil.d("GeoJson","result = " + result);
                        if(style.getSource("halobios") != null){
                            GeoJsonSource geoJsonSource = style.getSourceAs("halobios");
                            geoJsonSource.setGeoJson(featureCollection);
                        }else {
                            //添加点集图层
                            addClusteredGeoJsonSource(style, featureCollection);
                        }
                    }
                });
            }
        });
    }
    //添加FreatureCollection 到图层上，并把图层叠加在MapView中
    private void addClusteredGeoJsonSource(@NonNull Style loadedMapStyle,FeatureCollection featureCollection) {
        LogUtil.d("MapUtil","add source");
        // Add a new source from the GeoJSON data and set the 'cluster' option to true.
        loadedMapStyle.addSource(
                // Point to GeoJSON data. This example visualizes all M1.0+ earthquakes from
                // 12/22/15 to 1/21/16 as logged by USGS' Earthquake hazards program.
                new GeoJsonSource("halobios",
                        featureCollection,
                        new GeoJsonOptions()
                                .withCluster(true)
                                .withClusterMaxZoom(14)
                                .withClusterRadius(50)
                )
        );
        //Creating a marker layer for single data points
        SymbolLayer unclustered = new SymbolLayer("unclustered-points", "halobios");
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
        loadedMapStyle.addLayer(unclustered);
        // Use the earthquakes GeoJSON source to create three layers: One layer for each cluster category.
        // Each point range gets a different fill color.
        int[][] layers = new int[][] {
                new int[] {150, ContextCompat.getColor(LitePalApplication.getContext(), R.color.mapboxRed)},
                new int[] {20, ContextCompat.getColor(LitePalApplication.getContext(), R.color.mapboxGreen)},
                new int[] {0, ContextCompat.getColor(LitePalApplication.getContext(), R.color.mapbox_blue)}
        };
        for (int i = 0; i < layers.length; i++) {
            //Add clusters' circles
            CircleLayer circles = new CircleLayer("cluster-" + i, "halobios");
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
            loadedMapStyle.addLayer(circles);
        }
        //Add the count labels
        SymbolLayer count = new SymbolLayer("count", "halobios");
        count.setProperties(
                textField(Expression.toString(get("point_count"))),
                textSize(12f),
                textColor(Color.WHITE),
                textIgnorePlacement(true),
                textAllowOverlap(true)
        );
        loadedMapStyle.addLayer(count);
    }
}
