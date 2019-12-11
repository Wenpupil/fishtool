package com.example.fishandenvironment.listener;

import android.util.Log;

import com.example.fishandenvironment.adapter.BiologyRecyclerViewAdapter;
import com.example.fishandenvironment.bean.Biology;

public class BiologyClickListener implements BiologyRecyclerViewAdapter.OnBiologyClickListener {
    @Override
    public void onClick(Biology biology) {
        Log.d("biology",biology.getName()+"clicked");
    }
}
