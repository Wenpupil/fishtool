package com.example.fishandenvironment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mapbox.mapboxsdk.Mapbox;

public class App extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        MultiDex.install(this);
        // Mapbox令牌
        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));
    }
}
