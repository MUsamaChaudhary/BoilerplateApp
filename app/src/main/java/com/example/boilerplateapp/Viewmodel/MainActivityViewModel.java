package com.example.boilerplateapp.Viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.boilerplateapp.Repository.LoginSystemRepo;

public class MainActivityViewModel extends AndroidViewModel {

    private LoginSystemRepo loginSystemRepo;

    private MutableLiveData<Boolean> checkEmailValidation = new MutableLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
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
