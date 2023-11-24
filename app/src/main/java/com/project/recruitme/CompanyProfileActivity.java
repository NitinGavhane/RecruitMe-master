package com.project.recruitme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.recruitme.Models.CompanyModel;
import com.project.recruitme.Models.StudentModel;
import com.project.recruitme.Models.UserModel;
import com.project.recruitme.Persistence.CompanyReference;
import com.project.recruitme.Persistence.FirebaseCompanyReference;
import com.project.recruitme.Persistence.FirebaseUserReference;
import com.project.recruitme.Persistence.StudentReference;
import com.project.recruitme.Persistence.UserReference;
import com.project.recruitme.Utils.Constants;

import java.util.regex.Pattern;

public class CompanyProfileActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView companyNavigation;
    TextView CompanyName, CompanyEmail, CompanyPhone, CompanyLocation;
    EditText WebsiteField, IndustryField, DescriptionField;
    FirebaseAuth mAuth;
    CompanyReference companyRef;
    UserReference userRef;
    CompanyModel companyModel;
    Button LogoutButton, UpdateProfileButton, UpdatePasswordButton;
    ProgressBar LogoutProgress, UpdateProfileProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        LogoutProgress = findViewById(R.id.LogoutProgress);
        LogoutButton = findViewById(R.id.CompanyLogout);

        if(user == null) {
            Logout();
        }

        companyNavigation = findViewById(R.id.CompanyNavigation);
        companyNavigation.setOnItemSelectedListener(this);
        companyNavigation.setSelectedItemId(R.id.CompanyProfile);

        CompanyName = findViewById(R.id.CompanyName);
        CompanyEmail = findViewById(R.id.CompanyEmail);
        CompanyPhone = findViewById(R.id.CompanyPhone);
        CompanyLocation = findViewById(R.id.CompanyLocation);
        WebsiteField = findViewById(R.id.CompanyWebsiteField);
        IndustryField = findViewById(R.id.IndustryField);
        DescriptionField = findViewById(R.id.DescriptionField);
        UpdateProfileButton = findViewById(R.id.UpdateProfileButton);
        UpdateProfileProgress = findViewById(R.id.UpdateProfileProgress);
        UpdatePasswordButton = findViewById(R.id.CompanyPasswordUpdate);
        CompanyEmail.setText(user.getEmail());
        LogoutButton.setOnClickListener(view -> Logout());

        companyRef = new FirebaseCompanyReference();
        userRef = new FirebaseUserReference();

        userRef.getUser(user.getUid(), new UserReference.OnUserRetrievedCallback() {
            @Override
            public void onSuccess(UserModel user) {
                CompanyName.setText(user.UserName);
                CompanyPhone.setText(user.Phone);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(CompanyProfileActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
        companyRef.getCompany(user.getUid(), new CompanyReference.OnCompanyRetrievedCallback() {
            @Override
            public void onSuccess(CompanyModel company) {
                companyModel = company;
                CompanyLocation.setText(company.Location);
                IndustryField.setText(company.Industry);
                WebsiteField.setText(company.Website);
                DescriptionField.setText(company.Description);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(CompanyProfileActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
        UpdateProfileButton.setOnClickListener(view->UpdateProfile());
        UpdatePasswordButton.setOnClickListener(view-> UpdatePassword());
    }
    public void UpdatePassword(){

        LogoutProgress.setVisibility(View.VISIBLE);
        UpdatePasswordButton.setVisibility(View.GONE);

        if(mAuth.getCurrentUser()!=null)
        {
            mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail())
                    .addOnCompleteListener(task -> {
                        Toast.makeText(CompanyProfileActivity.this, "Password reset email sent", Toast.LENGTH_LONG).show();
                        LogoutProgress.setVisibility(View.GONE);
                        UpdatePasswordButton.setVisibility(View.VISIBLE);
                    });
        }
    }


    public void Logout(){
        LogoutButton.setVisibility(View.GONE);
        LogoutProgress.setVisibility(View.VISIBLE);
        mAuth.signOut();

        Intent intent = new Intent(CompanyProfileActivity.this,
                MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CompanyProfileActivity.this.finish();
    }

    public void UpdateProfile(){
        String Website = WebsiteField.getText().toString().trim();
        String Industry = IndustryField.getText().toString().trim();
        String Description = DescriptionField.getText().toString().trim();

        if(TextUtils.isEmpty(Website) || !Patterns.WEB_URL.matcher(Website).matches()){
            WebsiteField.setError("Please provide a valid website Url");
            return;
        }

        if(TextUtils.isEmpty(Industry)){

            IndustryField.setError("Industry is required");
            return;
        }
        if(TextUtils.isEmpty(Description) || Description.length()>120){

            DescriptionField.setError("Please a provide short description, less than 120 characters so it's easy to read.");
            return;
        }

        UpdateProfileButton.setVisibility(View.GONE);
        UpdateProfileProgress.setVisibility(View.VISIBLE);

        companyModel.Website = Website;
        companyModel.Industry = Industry;
        companyModel.Description = Description;

        companyRef.updateCompany(companyModel.UserId, companyModel, new CompanyReference.OnCompanyUpdatedCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(CompanyProfileActivity.this, "Profile Updated successfully!", Toast.LENGTH_LONG).show();
                UpdateProfileButton.setVisibility(View.VISIBLE);
                UpdateProfileProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String error) {

                Toast.makeText(CompanyProfileActivity.this, error, Toast.LENGTH_LONG).show();

                UpdateProfileButton.setVisibility(View.VISIBLE);
                UpdateProfileProgress.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.CompanyDashboard:
                startActivity(new Intent(getApplicationContext(),CompanyDashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.CompanyJobs:
                startActivity(new Intent(getApplicationContext(),CompanyJobsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.CompanyProfile:
                return true;


            default:
                return false;
        }
        overridePendingTransition(0,0);
        finish();
        return true;
    }
}