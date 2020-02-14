package com.example.boilerplateapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.boilerplateapp.Application.GlobalApplication;
import com.example.boilerplateapp.Viewmodel.VerifyPhoneActivityViewModel;
import com.example.boilerplateapp.databinding.ActivityVerifyPhoneBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    private String verificationId;
    private String forceResendingToken;
    private FirebaseAuth mAuth;
    private String mobile_Number;
    private ActivityVerifyPhoneBinding activityVerifyPhoneBinding;
    private VerifyPhoneActivityClickHandler verifyPhoneActivityClickHandler;
    private VerifyPhoneActivityViewModel verifyPhoneActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        activityVerifyPhoneBinding = DataBindingUtil.setContentView(this, R.layout.activity_verify_phone);
        verifyPhoneActivityClickHandler = new VerifyPhoneActivityClickHandler(this);
        activityVerifyPhoneBinding.setClickHandler(verifyPhoneActivityClickHandler);
        verifyPhoneActivityViewModel = ViewModelProviders.of(this).get(VerifyPhoneActivityViewModel.class);
        mAuth = FirebaseAuth.getInstance();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            verificationId = extras.getString("userId");
            mobile_Number = extras.getString("mobileNumb");
            forceResendingToken= extras.getString("resendToken");
            activityVerifyPhoneBinding.phoneNumber.setText(mobile_Number);
        }

    }

    public class VerifyPhoneActivityClickHandler {
        Context context;

        public VerifyPhoneActivityClickHandler(Context context) {
            this.context = context;
        }

        public void ContinueCheckCodeButton(View view) {
            String code = activityVerifyPhoneBinding.editTextCode.getText().toString();

            if (code.isEmpty() || code.length() < 6) {
                activityVerifyPhoneBinding.editTextCode.setError("Enter Code");
                activityVerifyPhoneBinding.editTextCode.requestFocus();
                return;
            }
            verifyCode(code);
        }


    }




    private void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        activityVerifyPhoneBinding.progressbar.setVisibility(View.VISIBLE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            activityVerifyPhoneBinding.progressbar.setVisibility(View.GONE);

                            if (mobile_Number != null) {
                                verifyPhoneActivityViewModel.getUserData(mobile_Number);

                                verifyPhoneActivityViewModel.getCheckphoneNumberValidation().observeForever(new Observer<Boolean>() {
                                    @Override
                                    public void onChanged(Boolean aBoolean) {

                                        if (aBoolean) {
                                            Toast.makeText(VerifyPhoneActivity.this, aBoolean.toString(), Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(VerifyPhoneActivity.this, ProfileAddInfo.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.putExtra("mobileNumber", mobile_Number);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(VerifyPhoneActivity.this, aBoolean.toString(), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(VerifyPhoneActivity.this, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.putExtra("mobileNumber", mobile_Number);
                                            startActivity(intent);
                                            GlobalApplication.getInstance().createLoginSession();

                                        }

                                    }
                                });


                            }


                        } else {
                            activityVerifyPhoneBinding.progressbar.setVisibility(View.GONE);
                            Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}
