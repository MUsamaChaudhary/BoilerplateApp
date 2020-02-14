package com.example.boilerplateapp.Repository;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.boilerplateapp.HomeActivity;
import com.example.boilerplateapp.model.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class ProfileAddRepo {

    private MutableLiveData<String> message = new MutableLiveData<>();
    private MutableLiveData<Profile> userprofile = new MutableLiveData<>();


    public MutableLiveData<String> getMessage() {
        return message;
    }

    public MutableLiveData<Profile> getUserprofile() {
        return userprofile;
    }

    public void insertProfile(Profile profile) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference mDocRef = firestore.collection("profile").document();

        profile.setId(mDocRef.getId());

        mDocRef.set(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("Patient records", "Addeded Successfully");
                message.setValue("Succeesfully Save Profile");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Patient records", "Not Addeded ");
                message.setValue("Failed to Save Profile");
            }
        });
    }

    public void uploadProfilePicture(final Profile profile) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        final StorageReference imageRef = storageReference.child("profile/" + UUID.randomUUID().toString());
        imageRef.putFile(Uri.parse(profile.getPicture_url())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("Profile Picture", "Addeded ");
                profile.setPicture_ref(imageRef.getPath());
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profile.setPicture_url(String.valueOf(uri));
                        insertProfile(profile);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }


    public void getUserProfileData(String email) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("profile")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                Profile userProfile = doc.toObject(Profile.class);
                                userprofile.setValue(userProfile);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Failure", "This is Fail");
            }
        });
    }

    public void getNumberUserProfileData(String phonenumber) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("profile")
                .whereEqualTo("number", phonenumber)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                Profile userProfile = doc.toObject(Profile.class);
                                userprofile.setValue(userProfile);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Failure", "This is Fail");
            }
        });
    }

    public void updatebothprofile(Profile profile, String profileid) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("profile")
                .document(profileid)
                .update(
                        "email", profile.getEmail(),
                        "name", profile.getName(),
                        "number", profile.getNumber())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Log.i("Updated Profile", "Yoooooo");

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
