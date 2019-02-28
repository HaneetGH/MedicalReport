package com.example.utestotp.presenter;

import android.content.Context;

import com.example.utestotp.interfaces.LocationContract;

public class LocationPresenter implements LocationContract.Presenter {
    LocationModel mModel;
    LocationContract.View mView;
    private Context context;


    public LocationPresenter(LocationContract.View view, Context applicationContext) {
        this.mView = view;
        this.context = applicationContext;

    }

    @Override
    public void bind() {
        mModel = new LocationModel(mView, context);
        mView.setLocation(mModel.generateLocation());

    }

    @Override
    public void unbind() {
        mModel = null;
        mView = null;
    }


}
