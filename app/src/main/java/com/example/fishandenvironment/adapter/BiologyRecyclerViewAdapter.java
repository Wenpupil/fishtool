package com.example.fishandenvironment.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fishandenvironment.R;
import com.example.fishandenvironment.bean.Biology;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class BiologyRecyclerViewAdapter extends RecyclerView.Adapter<BiologyRecyclerViewAdapter.ViewHolder> {

    private final List<Biology> biologies;
    private final OnBiologyClickListener mListener;

    public BiologyRecyclerViewAdapter(List<Biology> biologies, OnBiologyClickListener listener) {
        this.biologies = biologies;
        mListener = listener;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_biology, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Biology biology = biologies.get(position);
        //holder.mImageView.setImageURI();
        holder.mNameView.setText(biology.getName());
        Uri uri = Uri.parse(biology.getImage());
        holder.mImageView.setImageURI(uri);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onClick(biology);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return biologies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final SimpleDraweeView mImageView;
        final TextView mNameView;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.image);
            mNameView = view.findViewById(R.id.name);
        }
    }

    public interface OnBiologyClickListener {
        void onClick(Biology biology);
    }
}
