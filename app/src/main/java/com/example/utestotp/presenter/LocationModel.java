package com.example.utestotp.presenter;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.utestotp.interfaces.LocationContract;
import com.example.utestotp.utils.Utils;

import static android.content.Context.LOCATION_SERVICE;

class LocationModel {
    LocationContract.View mView;
    private Context context;
    LocationManager locationManager;
    private LocationListener listener;

    public LocationModel(LocationContract.View mView, Context context) {
        this.context = context;
        this.mView = mView;
    }

    public Location generateLocation() {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mView.setLocation(location);
                //  Toast.makeText(context, "here", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //    Toast.makeText(context, "here", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProviderEnabled(String s) {
                //    Toast.makeText(context, "here", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProviderDisabled(String s) {
                //    Toast.makeText(context, "here", Toast.LENGTH_LONG).show();
            }
        };
        Location location = Utils.getLastKnownLocation(context, (Activity) mView, locationManager, listener);

        return location;
    }
}
