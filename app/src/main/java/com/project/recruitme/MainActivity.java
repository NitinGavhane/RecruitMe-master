package com.project.recruitme;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.project.recruitme.Models.UserModel;
import com.project.recruitme.Persistence.FirebaseUserReference;
import com.project.recruitme.Persistence.UserReference;
import com.project.recruitme.Utils.Constants;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText LoginEmail, LoginPassword;
    Button LoginButton;
    TextView ForgotPassword;
    ProgressBar LoginProgress;
    SharedPreferences pref;
    UserReference userReference;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginEmail = findViewById(R.id.LoginEmail);
        LoginPassword = findViewById(R.id.LoginPassword);
        LoginButton = findViewById(R.id.LoginButton);
        ForgotPassword = findViewById(R.id.ForgotPassword);
        LoginProgress = findViewById(R.id.LoginProgress);
        pref =  MainActivity.this.getApplicationContext().getSharedPreferences(Constants.PREFERENCE_STRING, Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        userReference = new FirebaseUserReference();
        if(mAuth.getCurrentUser()!=null) {
            Toast.makeText(MainActivity.this, "Signing you in!", Toast.LENGTH_LONG).show();
            LoginProgress.setVisibility(View.VISIBLE);
            LoginButton.setVisibility(View.GONE);
            RedirectUser();
        }

        LoginButton.setOnClickListener(view -> Login());
    }

    void RedirectUser(){

        userReference.getUser(mAuth.getUid(), new UserReference.OnUserRetrievedCallback() {
            @Override
            public void onSuccess(UserModel user) {
                Toast.makeText(MainActivity.this, user.UserName, Toast.LENGTH_LONG).show();
                Class DestinationClass = null;
                switch (user.Role){
                    case Constants.ADMIN_ROLE:
                        DestinationClass = AdminDashboardActivity.class;
                        break;
                    case Constants.STUDENT_ROLE:
                        DestinationClass = StudentDashboardActivity.class;
                        break;
                    case Constants.COMPANY_ROLE:
                        DestinationClass = CompanyDashboardActivity.class;
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Invalid User Role", Toast.LENGTH_LONG).show();
                        LoginProgress.setVisibility(View.GONE);
                        LoginButton.setVisibility(View.VISIBLE);
                        return;
                }

                SharedPreferences.Editor editor = pref.edit();

                editor.putString(Constants.USER_ID, user.UserId);
                editor.putString(Constants.USER_ROLE, user.Role);
                editor.putString(Constants.USER_NAME, user.UserName);
                editor.apply();

                Intent intent = new Intent(MainActivity.this, DestinationClass);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                MainActivity.this.finish();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MainActivity.this, "Failed to load user profile", Toast.LENGTH_LONG).show();
                LoginProgress.setVisibility(View.GONE);
                LoginButton.setVisibility(View.VISIBLE);
            }
        });

    }



    public void Login(){
        String Email = LoginEmail.getText().toString();
        String Password = LoginPassword.getText().toString();

        SharedPreferences.Editor editor = pref.edit();

        // Validations for input email and password
        if (TextUtils.isEmpty(Email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(Password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }


        LoginProgress.setVisibility(View.VISIBLE);
        LoginButton.setVisibility(View.GONE);

        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(getApplicationContext(),
                                    "Login successful!",
                                    Toast.LENGTH_LONG)
                            .show();
                    editor.putString(Constants.EMAIL_STORAGE_STRING, Email);
                    editor.putString(Constants.PASSWORD_STORAGE_STRING, Password);
                    editor.apply();
                    RedirectUser();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(),
                                    "Login failed!",
                                    Toast.LENGTH_LONG)
                            .show();
                    // hide the progress bar
                    LoginProgress.setVisibility(View.GONE);
                    LoginButton.setVisibility(View.VISIBLE);
                });

    }
}