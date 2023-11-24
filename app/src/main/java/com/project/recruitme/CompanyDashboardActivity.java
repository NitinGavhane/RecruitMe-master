package com.project.recruitme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.project.recruitme.Utils.Constants;

public class CompanyDashboardActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    TextView NameView;
    BottomNavigationView companyNavigation;
    Button EditProfileButton;
    ListView NotificationsView;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_dashboard);

        NameView = findViewById(R.id.CompanyName);
        companyNavigation = findViewById(R.id.CompanyNavigation);


        companyNavigation.setOnItemSelectedListener(this);
        companyNavigation.setSelectedItemId(R.id.CompanyDashboard);
        NotificationsView = findViewById(R.id.CompanyNotificationList);
        EditProfileButton = findViewById(R.id.EditProfileButton);
        pref =  CompanyDashboardActivity.this.getApplicationContext().getSharedPreferences(Constants.PREFERENCE_STRING, Context.MODE_PRIVATE);

        String Name = pref.getString(Constants.USER_NAME, "");
        NameView.setText(Name);

        EditProfileButton.setOnClickListener(view -> startActivity(new Intent(CompanyDashboardActivity.this, CompanyProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.CompanyDashboard:
                return true;

            case R.id.CompanyJobs:
                startActivity(new Intent(getApplicationContext(),CompanyJobsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.CompanyProfile:
                startActivity(new Intent(getApplicationContext(),CompanyProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            default:
                return false;
        }
        overridePendingTransition(0,0);
        finish();
        return true;
    }
}