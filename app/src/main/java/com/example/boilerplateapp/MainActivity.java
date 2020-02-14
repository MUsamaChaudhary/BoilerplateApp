package com.example.boilerplateapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.boilerplateapp.Application.GlobalApplication;
import com.example.boilerplateapp.Viewmodel.MainActivityViewModel;
import com.example.boilerplateapp.databinding.ActivityMainBinding;
import com.example.boilerplateapp.model.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private MainActivityClickListner mainActivityClickListner;
    private MainActivityViewModel mainActivityViewModel;
    private String mobile_number;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainActivityClickListner = new MainActivityClickListner(this);
        activityMainBinding.setClickHandler(mainActivityClickListner);
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

    }

    public class MainActivityClickListner {

        Context context;

        public MainActivityClickListner(Context context) {
            this.context = context;
        }

        public void phoneNumberNextButton(View view) {
            mobile_number = activityMainBinding.editTextMobile.getText().toString();
            if (isConnectedtoNetwork()) {

                if (!mobile_number.isEmpty()) {
                    sendVerificationCode(mobile_number);
                } else {
                    Toast.makeText(MainActivity.this, "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(MainActivity.this, "You are not Connected to Internet Plz Connect First", Toast.LENGTH_SHORT).show();
            }

        }


        public void loginWithGoogleButton(View view) {
            if (isConnectedtoNetwork()) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            } else {
                Toast.makeText(MainActivity.this, "You are not Connected to Internet Plz Connect First", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.

            final String email = account.getEmail();

            if (email != null) {
                mainActivityViewModel.getUserData(email);

                mainActivityViewModel.getCheckEmailValidation().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {

                        if (aBoolean) {
                            Intent intent = new Intent(MainActivity.this, ProfileAddInfo.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("emailafterlogin", email);
                            startActivity(intent);
                            GlobalApplication.getInstance().createGoogleLoginSession();
                        }
                    }
                });

            }


        } catch (ApiException e) {

            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Failed ", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendVerificationCode(String number) {

        activityMainBinding.mainProgreebar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);

                        activityMainBinding.mainProgreebar.setVisibility(View.GONE);
                        Log.e("Code sent", s);
                        Intent intent = new Intent(MainActivity.this, VerifyPhoneActivity.class);
                        intent.putExtra("userId", s);
                        intent.putExtra("mobileNumb", mobile_number);
                        intent.putExtra("resendToken", forceResendingToken);
                        startActivity(intent);

                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        Log.e("OnVerificationCompleted", String.valueOf(phoneAuthCredential));
                        activityMainBinding.mainProgreebar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Log.i("Verification Failed", e.getMessage());
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        activityMainBinding.mainProgreebar.setVisibility(View.GONE);

                    }
                }
        );

    }

    protected boolean isConnectedtoNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        } else {
            return false;
        }
    }

}






