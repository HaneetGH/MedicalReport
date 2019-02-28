package com.example.utestotp.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivityViewModel extends AndroidViewModel {

    public MutableLiveData<PhoneAuthCredential> phoneAuthCredentialMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Task<AuthResult>> authResultMutableLiveData = new MutableLiveData<>();
    private Context context;
    private FirebaseAuth mAuth;
    private String code;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private Activity activity;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        context = application;
    }


    public void login(final String code, Activity activity) {
        this.activity = activity;
        verifyVerificationCode(code);
    }

    public void sendNumber(String number, Activity activity) {
        mAuth = FirebaseAuth.getInstance();
        this.activity = activity;
        sendVerificationCode(number);
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            code = phoneAuthCredential.getSmsCode();

            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                phoneAuthCredentialMutableLiveData.setValue(phoneAuthCredential);
            } else {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            mResendToken = forceResendingToken;
        }
    };
    PhoneAuthCredential credential;

    private void verifyVerificationCode(String otp) {
        //creating the credential

        if (mVerificationId != null) {
            credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            authResultMutableLiveData.setValue(task);
                        } else {
                            String message = "Somthing is wrong, we will fix it soon...";
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }


}
