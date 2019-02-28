package com.example.utestotp.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.utestotp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class HomeActivityViewModel extends AndroidViewModel {
    public MutableLiveData<ArrayList<String>> bundleMutableLiveData = new MutableLiveData<>();
    Activity activity;
    RecognitionListener recognitionListener;
    Context context;
    Intent mSpeechRecognizerIntent;
    SpeechRecognizer mSpeechRecognizer;
    final int REQ_CODE_SPEECH_INPUT = 1;

    public HomeActivityViewModel(@NonNull Application application) {
        super(application);
        context = application;
    }

    public void speechToText(final String code, Activity activity) {
        this.activity = activity;
        startAlgo();
    }

    private void startAlgo() {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        mSpeechRecognizer.setRecognitionListener(recognitionListener);

        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());

        recognitionListener = new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.d("Value", params.toString());
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d("Value", "");
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                Log.d("Value", "");
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                Log.d("Value", buffer.toString());
            }

            @Override
            public void onEndOfSpeech() {
                Log.d("Value", "");
            }

            @Override
            public void onError(int error) {
                Log.d("Value", "");
            }

            @Override
            public void onResults(Bundle results) {
                Log.d("Value", results.toString());
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                Log.d("Value", partialResults.toString());
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                Log.d("Value", params.toString());
            }
        };
        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
    }

}