package com.example.utestotp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.utestotp.R;
import com.example.utestotp.adapter.HistoryListAdapter;
import com.example.utestotp.base.BaseActivity;
import com.example.utestotp.database.DatabaseHelper;
import com.example.utestotp.databinding.HistoryActivityBinding;
import com.example.utestotp.interfaces.RecyclerViewClickListener;
import com.example.utestotp.models.UserModel;
import com.example.utestotp.viewmodel.HistoryActivityViewModel;

import java.util.ArrayList;


public class PatientHistory extends BaseActivity implements RecyclerViewClickListener {
    private HistoryActivityBinding binding;
    private HistoryActivityViewModel viewModel;
    private HistoryListAdapter adapter;
    ArrayList<UserModel> userModelss;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.history_activity);
        attachViewModel();

    }

    private void attachViewModel() {
        viewModel = ViewModelProviders.of(this).get(HistoryActivityViewModel.class);

        viewModel.bundleMutableLiveData.observe(this, new Observer<ArrayList<UserModel>>() {
            @Override
            public void onChanged(@Nullable ArrayList<UserModel> userModels) {
                userModelss = userModels;
                adapter = new HistoryListAdapter(userModels, PatientHistory.this);
                binding.setAdapter(adapter);
            }
        });

        viewModel.fetchInformationFromDB();
    }

    @Override
    public void onClick(View view, int position) {
        binding.list.setVisibility(View.GONE);
        binding.llDetail.setVisibility(View.VISIBLE);
        binding.idForName.setText(userModelss.get(position).getName());
        binding.idForNumber.setText(userModelss.get(position).getPhone());
        binding.idForTimeDate.setText(userModelss.get(position).getTime());
        binding.idForGPS.setText(userModelss.get(position).getGps());
        binding.idEtForDiagnosis.setText(userModelss.get(position).getDiagnosis());
        binding.idEtForAsessment.setText(userModelss.get(position).getAsessment());
        binding.idEtForMedication.setText(userModelss.get(position).getMedication());


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {

        if (binding.llDetail.getVisibility() == View.VISIBLE) {
            binding.list.setVisibility(View.VISIBLE);
            binding.llDetail.setVisibility(View.GONE);

        } else
            super.onBackPressed();
    }
}
