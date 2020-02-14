package com.example.boilerplateapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.boilerplateapp.Application.GlobalApplication;
import com.example.boilerplateapp.Fragment.DashboardFragment;
import com.example.boilerplateapp.databinding.ActivityHomeBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private GoogleSignInClient mGoogleSignInClient;
    private ActivityHomeBinding activityHomeBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        setSupportActionBar(activityHomeBinding.toolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (account != null) {
            String email = account.getEmail();

            if (email != null) {
                Toast.makeText(this, "Account email" + email, Toast.LENGTH_SHORT).show();
            }

        } else if (user != null) {
            String phonenumber = user.getPhoneNumber();
            Toast.makeText(this, "Account number" + phonenumber, Toast.LENGTH_SHORT).show();
        }


        activityHomeBinding.navView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, activityHomeBinding.drawerLayout, activityHomeBinding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        activityHomeBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,
                new DashboardFragment()).commit();


    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if (activityHomeBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {

            activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Toast.makeText(this, "Home is Clicked", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,
                        new DashboardFragment()).commit();
                break;
            case R.id.nav_profile:
                Intent intent = new Intent(HomeActivity.this, MyProfile.class);
                startActivity(intent);
               /* getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,
                        new ProfileFragment()).commit();*/
                break;
            case R.id.nav_logout:
                GlobalApplication.getInstance().LogoutUser();
                GlobalApplication.getInstance().LogoutGoogleUser();
                signOutGoogle();

                break;

        }
        activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOutGoogle() {

        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(HomeActivity.this, "Sign out Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
