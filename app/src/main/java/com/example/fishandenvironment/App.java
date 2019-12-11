package com.example.fishandenvironment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;

public class App extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        MultiDex.install(this);
    }
}
