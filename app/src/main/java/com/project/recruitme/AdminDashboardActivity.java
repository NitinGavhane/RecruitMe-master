package com.project.recruitme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminDashboardActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    BottomNavigationView adminNavigation;
    LinearLayout adminStudent, adminCompany;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        adminNavigation = findViewById(R.id.AdminNavigation);
        adminStudent = findViewById(R.id.AdminStudent);

        adminCompany = findViewById(R.id.AdminCompany);

        adminStudent.setOnClickListener(view -> {
            Intent intent= new Intent(AdminDashboardActivity.this, AdminStudentListActivity.class);
            startActivity(intent);
        });

        adminCompany.setOnClickListener(view -> {
            Intent intent= new Intent(AdminDashboardActivity.this, AdminCompaniesActivity.class);
            startActivity(intent);
        });

        adminNavigation.setOnItemSelectedListener(this);
        adminNavigation.setSelectedItemId(R.id.adminDashboard);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.adminDashboard:
                return true;

            case R.id.adminProfile:
                startActivity(new Intent(getApplicationContext(),AdminProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            default:
                return false;
        }
        overridePendingTransition(0,0);
        finish();
        return true;
    }
}