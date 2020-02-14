package com.example.boilerplateapp.Splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.boilerplateapp.Application.GlobalApplication;
import com.example.boilerplateapp.HomeActivity;
import com.example.boilerplateapp.MainActivity;
import com.example.boilerplateapp.ProfileAddInfo;
import com.example.boilerplateapp.R;
import com.example.boilerplateapp.Viewmodel.SplashScreenViewModel;
import com.example.boilerplateapp.model.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SplashScreen extends AppCompatActivity {

    private GoogleSignInClient googleSignInClient;
  //  private SplashScreenViewModel splashScreenViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
       // splashScreenViewModel = ViewModelProviders.of(this).get(SplashScreenViewModel.class);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (GlobalApplication.getInstance().checkLogin()) {

                    /* This Function Demostrate the Mobile Login True and False,
                      If true go to App Main screen If False go to Login Screen */
                }
                else if (GlobalApplication.getInstance().checkGoogleSessionLogin())
                {

                    /* This Function Demostrate the Mobile Login True and False,
                      If true go to App Main screen If False go to Login Screen */

                }


/*
                else {
                    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

                    if (account != null) {
                        String accountEmail = account.getEmail();
                        if (accountEmail != null) {
                            splashScreenViewModel.getUserData(accountEmail);
                            splashScreenViewModel.getCheckEmailValidation().observeForever(new Observer<Boolean>() {
                                @Override
                                public void onChanged(Boolean aBoolean) {
                                    if (aBoolean) {
                                        Toast.makeText(SplashScreen.this, "No Account", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SplashScreen.this, ProfileAddInfo.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(SplashScreen.this, "Yes Account", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }

                                }
                            });
                        }
                    } else {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                }
*/

            }
        }, 2500);


    }


}
