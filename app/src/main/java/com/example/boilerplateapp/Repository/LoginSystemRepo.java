package com.example.boilerplateapp.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginSystemRepo {

    private MutableLiveData<Boolean> checkEmailValidation = new MutableLiveData<>();
    private MutableLiveData<Boolean> checkphoneNumberValidation = new MutableLiveData<>();


    public MutableLiveData<Boolean> getCheckEmailValidation() {
        return checkEmailValidation;
    }

    public MutableLiveData<Boolean> getCheckphoneNumberValidation() {
        return checkphoneNumberValidation;
    }


    public void getUserData(String email) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("profile")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            checkEmailValidation.setValue(false);

                        } else {
                            checkEmailValidation.setValue(true);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Failure", "This is Fail");
            }
        });
    }

    public void getUserPhoneData(String phoneNumber) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("profile")
                .whereEqualTo("number", phoneNumber)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            checkphoneNumberValidation.setValue(false);

                        } else {
                            checkphoneNumberValidation.setValue(true);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Failure", "This is Fail");
            }
        });
    }
}
