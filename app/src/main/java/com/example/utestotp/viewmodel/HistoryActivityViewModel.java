package com.example.utestotp.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.utestotp.database.DatabaseHelper;
import com.example.utestotp.models.UserModel;

import org.json.JSONException;

import java.util.ArrayList;

public class HistoryActivityViewModel extends AndroidViewModel {
    public MutableLiveData<ArrayList<UserModel>> bundleMutableLiveData = new MutableLiveData<>();
    Application application;
    DatabaseHelper mydb;

    public HistoryActivityViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        mydb = new DatabaseHelper(application.getApplicationContext());
    }

    public void fetchInformationFromDB() {

        dBCall();
    }

    private void dBCall() {

        try {
            bundleMutableLiveData.setValue(mydb.getData());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
