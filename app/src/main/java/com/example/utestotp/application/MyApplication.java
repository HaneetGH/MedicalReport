package com.example.utestotp.application;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.example.utestotp.utils.GlobalConfiguration;
import com.example.utestotp.R;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks

{
    Activity activity;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    private static AlertDialog alert;
    private static AlertDialog alertNothing;

    public static boolean checkGPS(LocationManager locationManager) {
        //return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);       //UBICACION MEDIANTE GPS FISICO
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);     //UBICACION MEDIANTE TRIANGULACION DE ANTENAS
    }

    public static void GPSDisable(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_alert_enable_location_service, null, false);

        view.findViewById(R.id.btn_settings_enable_location_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) context).startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), GlobalConfiguration.REQUEST_GPS);
                alert.dismiss();
            }
        });

        view.findViewById(R.id.btn_cancel_enable_location_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
        builder.setView(view);
        alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityCreated(final Activity activity, Bundle bundle) {
        this.activity = activity;

        if (!checkGPS((LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE))) {
            GPSDisable(getApplicationContext());
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    GlobalConfiguration.isSystemHasLocation = true;
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                        builder1.setMessage("Please grant location Permission");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Setting",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                                        intent.setData(uri);
                                        (activity).startActivityForResult(intent, GlobalConfiguration.PERMISSIONS_REQUEST_LOCATION);
                                    }
                                });

                        builder1.setNegativeButton(
                                "Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();


                    } else {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                GlobalConfiguration.PERMISSIONS_REQUEST_LOCATION);
                    }
                } else {

                    GlobalConfiguration.isSystemHasLocation = true;

                }

            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
