package com.example.boilerplateapp.Viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.boilerplateapp.Repository.LoginSystemRepo;

public class VerifyPhoneActivityViewModel extends AndroidViewModel {

    private LoginSystemRepo loginSystemRepo;

    private MutableLiveData<Boolean> checkphoneNumberValidation = new MutableLiveData<>();

    public MutableLiveData<Boolean> getCheckphoneNumberValidation() {
        return checkphoneNumberValidation;
    }

    public VerifyPhoneActivityViewModel(@NonNull Application application) {
        super(application);
        loginSystemRepo = new LoginSystemRepo();
    }

    public void getUserData(String phoneNumber) {
        loginSystemRepo.getUserPhoneData(phoneNumber);
        loginSystemRepo.getCheckphoneNumberValidation().observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    checkphoneNumberValidation.setValue(aBoolean);
                } else {
                    checkphoneNumberValidation.setValue(aBoolean);
                }
            }
        });

    }

}
