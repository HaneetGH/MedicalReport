package com.example.utestotp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.utestotp.R;
import com.example.utestotp.databinding.ActivityMainBinding;
import com.example.utestotp.viewmodel.LoginActivityViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;


public class LoginActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    private LoginActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        attachModel();
        // ButterKnife.bind(this);
    }

    private void attachModel() {
        binding.setHandler(new ClickHandler());
        viewModel = ViewModelProviders.of(this).get(LoginActivityViewModel.class);
        viewModel.phoneAuthCredentialMutableLiveData.observe(this, new Observer<PhoneAuthCredential>() {
            @Override
            public void onChanged(@Nullable PhoneAuthCredential phoneAuthCredential) {
                if (phoneAuthCredential.getSmsCode() != null)
                    binding.etOTP.setText(phoneAuthCredential.getSmsCode());
            }
        });
        viewModel.authResultMutableLiveData.observe(this, new Observer<Task<AuthResult>>() {
            @Override
            public void onChanged(@Nullable Task<AuthResult> authResultTask) {

                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    public class ClickHandler {

        public void login(final String empcode) {
            if (TextUtils.isEmpty(empcode) || TextUtils.isEmpty(binding.etPhoneNo.getText()) || binding.etPhoneNo.getText().length() < 10) {
                FirebaseAuth.getInstance().signOut();
                Snackbar.make(findViewById(android.R.id.content), "Please enter otp", Snackbar.LENGTH_SHORT).show();
                binding.etOTP.setVisibility(View.GONE);
                binding.btnNext.setVisibility(View.VISIBLE);
                binding.btnLogin.setVisibility(View.GONE);
                return;
            } else {
                viewModel.login(binding.etOTP.getText().toString(), LoginActivity.this);
            }


        }

        public void sendPhone(final String empNumber) {
            if (TextUtils.isEmpty(empNumber)) {
                FirebaseAuth.getInstance().signOut();
                binding.etOTP.setVisibility(View.GONE);
                binding.btnNext.setVisibility(View.VISIBLE);
                binding.btnLogin.setVisibility(View.GONE);
                return;
            } else {
                binding.etOTP.setVisibility(View.VISIBLE);
                binding.btnNext.setVisibility(View.GONE);
                binding.btnLogin.setVisibility(View.VISIBLE);
                viewModel.sendNumber(empNumber, LoginActivity.this);
            }


        }

    }


}
