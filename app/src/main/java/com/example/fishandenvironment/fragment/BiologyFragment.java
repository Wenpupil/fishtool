package com.example.fishandenvironment.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fishandenvironment.R;
import com.example.fishandenvironment.adapter.BiologyRecyclerViewAdapter;
import com.example.fishandenvironment.bean.Biology;
import com.example.fishandenvironment.listener.BiologyClickListener;
import com.example.fishandenvironment.util.DataUtil;
import com.githang.statusbar.StatusBarCompat;

import org.litepal.LitePal;

import java.util.List;

public class BiologyFragment extends Fragment {

    private Activity activity;
    private BiologyRecyclerViewAdapter.OnBiologyClickListener mListener;

    public BiologyFragment() {
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_biology_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        List<Biology> biologies = LitePal.findAll(Biology.class);
        recyclerView.setAdapter(new BiologyRecyclerViewAdapter(biologies, mListener));
        return view;
    }

    private void init(){
        StatusBarCompat.setStatusBarColor(activity, Color.WHITE);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BiologyRecyclerViewAdapter.OnBiologyClickListener) {
            mListener = (BiologyRecyclerViewAdapter.OnBiologyClickListener) context;
        } else {
            mListener = new BiologyClickListener();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
