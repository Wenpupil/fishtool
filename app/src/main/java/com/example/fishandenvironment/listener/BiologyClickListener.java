package com.example.fishandenvironment.listener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.fishandenvironment.activity.BiologyInfoActivity;
import com.example.fishandenvironment.adapter.BiologyRecyclerViewAdapter;
import com.example.fishandenvironment.bean.Biology;

public class BiologyClickListener implements BiologyRecyclerViewAdapter.OnBiologyClickListener {
    private Context context;
    public BiologyClickListener(Context context){
        this.context = context;
    }
    @Override
    public void onClick(Biology biology) {
        Log.d("biology",biology.getName()+"clicked");

        Intent intent = new Intent(context, BiologyInfoActivity.class);
        intent.putExtra("biology",biology);
        context.startActivity(intent);
    }
}
