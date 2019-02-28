package com.example.utestotp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.utestotp.utils.GlobalConfiguration;
import com.example.utestotp.base.BaseActivity;

import com.example.utestotp.application.MyApplication;
import com.example.utestotp.R;
import com.example.utestotp.database.DatabaseHelper;
import com.example.utestotp.databinding.HomeActivityBinding;
import com.example.utestotp.interfaces.LocationContract;
import com.example.utestotp.presenter.LocationPresenter;
import com.example.utestotp.utils.Utils;
import com.example.utestotp.viewmodel.HomeActivityViewModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.Locale;

import static com.example.utestotp.utils.GlobalConfiguration.REQ_CODE_SPEECH_INPUT;

public class HomeActivity extends BaseActivity implements LocationContract.View {
    private HomeActivityBinding binding;
    private HomeActivityViewModel viewModel;

    private String isFor;
    LocationPresenter mPresenter;
    private LocationManager mLocationManager;
    DatabaseHelper mydb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity);
        mydb = new DatabaseHelper(this);
        Toast.makeText(getApplicationContext(), mydb.numberOfRows() + "", Toast.LENGTH_LONG).show();

        attachModel();
        //mydb.getData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        if(mPresenter!=null)
        mPresenter.unbind();
    }

    private void attachModel() {
        binding.setHandler(new ClickHandler());
        viewModel = ViewModelProviders.of(this).get(HomeActivityViewModel.class);
        viewModel.bundleMutableLiveData.observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(@Nullable ArrayList<String> strings) {
                Log.e("Resut", strings.get(0) + "");
            }
        });

    }

    @Override
    public void setLocation(Location message) {
        if (message != null)
            binding.idForGPS.setText(message.getLatitude() + " " + message.getLongitude());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public class ClickHandler {

        public void createPatient() {

            binding.llOption.setVisibility(View.GONE);
            binding.llDetail.setVisibility(View.VISIBLE);
        }

        public void getPatient() {

            Intent intent = new Intent(HomeActivity.this, PatientHistory.class);
            startActivity(intent);
        }

        public void fetchDateTime() {
            binding.idForTimeDate.setText(Calendar.getInstance().getTimeInMillis() + "");
        }

        public void saveData() {

            if (binding.idForTimeDate.getText().toString().isEmpty() || binding.idForGPS.getText().toString().isEmpty() || binding.idEtForMedication.getText().toString().isEmpty() || binding.idEtForDiagnosis.getText().toString().isEmpty() || binding.idEtForAsessment.getText().toString().isEmpty() || binding.idForName.toString().isEmpty() || binding.idForNumber.toString().isEmpty()) {
                return;
            } else {
                Toast.makeText(getApplicationContext(), "Save", Toast.LENGTH_LONG).show();
                mydb.insertUser(binding.idForName.getText().toString(), binding.idForNumber.getText().toString(), binding.idForTimeDate.getText().toString(), binding.idForGPS.getText().toString(), binding.idEtForAsessment.getText().toString(), binding.idEtForDiagnosis.getText().toString(), binding.idEtForMedication.getText().toString());
            }
        }

        public void fetchGPS() {
            MyApplication nMyApplication = (MyApplication) getApplication();
            nMyApplication.onActivityCreated(HomeActivity.this, null);
            if (GlobalConfiguration.isSystemHasLocation) {
                mPresenter = new LocationPresenter(HomeActivity.this, getApplicationContext());
                mPresenter.bind();
            }


        }

        public void speechToText(String gps) {
            // viewModel.speechToText("", HomeActivity.this);
            isFor = gps;
            Utils.promptSpeechInput(gps, HomeActivity.this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    final ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);


                    if (result.get(0) != null) {
                        if (isFor.equals("asessment")) {
                            binding.idEtForAsessment.setText(result.get(0));
                        } else if (isFor.equals("diagnosis")) {
                            binding.idEtForDiagnosis.setText(result.get(0));
                        } else if (isFor.equals("medication")) {
                            binding.idEtForMedication.setText(result.get(0));
                        }
                        break;
                    }

                }

            }
        }
    }
}
