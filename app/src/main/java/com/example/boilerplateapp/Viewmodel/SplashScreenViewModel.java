package com.example.boilerplateapp.Viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.boilerplateapp.Repository.LoginSystemRepo;

public class SplashScreenViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> checkEmailValidation = new MutableLiveData<>();
    private LoginSystemRepo loginSystemRepo;


    public SplashScreenViewModel(@NonNull Application application) {
        super(application);
        loginSystemRepo = new LoginSystemRepo();
    }

    public MutableLiveData<Boolean> getCheckEmailValidation() {
        return checkEmailValidation;
    }

    public void getUserData(String email) {
        loginSystemRepo.getUserData(email);
        loginSystemRepo.getCheckEmailValidation().observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    checkEmailValidation.setValue(aBoolean);
                } else {
                    checkEmailValidation.setValue(aBoolean);
                }
            }
        });
    }


}
