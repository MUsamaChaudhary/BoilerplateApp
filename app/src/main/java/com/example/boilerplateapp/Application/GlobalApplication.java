package com.example.boilerplateapp.Application;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.boilerplateapp.HomeActivity;
import com.example.boilerplateapp.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class GlobalApplication extends Application {

    private GoogleSignInClient googleSignInClient;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private SharedPreferences google_pref;
    private SharedPreferences.Editor google_editor;

    private static final String PREF_NAME = "AndroidHivePref";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String GOOGLE_PREF_NAME = "GoogleAndroidHivePref";
    private static final String GOOGLE_IS_LOGIN = "GoogleIsLoggedIn";

    private static final String V_ID = "";
    private static GlobalApplication mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = pref.edit();

        google_pref=getApplicationContext().getSharedPreferences(GOOGLE_PREF_NAME, MODE_PRIVATE);
        google_editor = google_pref.edit();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);



    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static GlobalApplication getInstance() {
//        if (mInstance == null) {
//            mInstance = new GlobalApplication();
//        }
        return mInstance;
    }

    // this fucntion is Creating the Loggin Session For the Mobile User
    public void createLoginSession() {
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }
    public void createGoogleLoginSession() {
        google_editor.putBoolean(GOOGLE_IS_LOGIN, true);
        google_editor.commit();
    }


    //this function is checking the login session of the phone number login
    public boolean checkLogin() {
        Log.i("CheckLogin", String.valueOf(isLoggedIn()));
        if (!isLoggedIn()) {
            Intent intent = new Intent(GlobalApplication.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return false;

        } else if (isLoggedIn()) {
            Intent intent = new Intent(GlobalApplication.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return false;
    }

    public boolean checkGoogleSessionLogin() {
        Log.i("CheckLogin", String.valueOf(isLoggedIn()));
        if (!isGoogleLoggedIn()) {
            Intent intent = new Intent(GlobalApplication.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return false;

        } else if (isGoogleLoggedIn()) {
            Intent intent = new Intent(GlobalApplication.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return false;
    }




    private boolean isGoogleLoggedIn() {
        return google_pref.getBoolean(GOOGLE_IS_LOGIN, false);
    }
    private boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    /* this function loging off the mobile user by clearing the shared
    preference and redirects to main activity */
    public void LogoutUser() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(GlobalApplication.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    public void LogoutGoogleUser() {
        google_editor.clear();
        google_editor.commit();

        Intent i = new Intent(GlobalApplication.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }


    public boolean checkGoogleLogin()
    {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        if (account == null) {
            Intent intent = new Intent(GlobalApplication.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return false;

        } else if (account!=null) {
            Intent intent = new Intent(GlobalApplication.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return false;


    }


}
