package com.example.utestotp.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.utestotp.R;
import com.example.utestotp.databinding.HistoryActivityBinding;
import com.example.utestotp.databinding.HistoryCellBinding;
import com.example.utestotp.interfaces.RecyclerViewClickListener;
import com.example.utestotp.models.UserModel;

import java.util.ArrayList;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.MyViewHolder> {

    private ArrayList<UserModel> material;
    private RecyclerViewClickListener mListener;

    public HistoryListAdapter(ArrayList<UserModel> material, RecyclerViewClickListener mListener) {
        this.material = material;
        this.mListener = mListener;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private HistoryCellBinding binding;

        public MyViewHolder(HistoryCellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HistoryCellBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.history_cell, parent, false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.binding.setModel(material.get(position));
        holder.binding.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onClick(v, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return material.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
