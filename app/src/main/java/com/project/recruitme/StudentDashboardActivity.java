package com.project.recruitme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.project.recruitme.Utils.Constants;

public class StudentDashboardActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    TextView NameView;
    BottomNavigationView studentNavigation;
    Button EditProfileButton;
    ListView NotificationsView;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        NameView = findViewById(R.id.StudentName);
        studentNavigation = findViewById(R.id.StudentNavigation);


        studentNavigation.setOnItemSelectedListener(this);
        studentNavigation.setSelectedItemId(R.id.StudentDashboard);
        NotificationsView = findViewById(R.id.StudentNotificationList);
        EditProfileButton = findViewById(R.id.EditProfileButton);
        pref =  StudentDashboardActivity.this.getApplicationContext().getSharedPreferences(Constants.PREFERENCE_STRING, Context.MODE_PRIVATE);

        String Name = pref.getString(Constants.USER_NAME, "");
        NameView.setText(Name);

        EditProfileButton.setOnClickListener(view -> startActivity(new Intent(StudentDashboardActivity.this, StudentProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.StudentDashboard:
                return true;

            case R.id.StudentJobs:
                startActivity(new Intent(getApplicationContext(),StudentJobsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.StudentProfile:
                startActivity(new Intent(getApplicationContext(),StudentProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            default:
                return false;
        }
        overridePendingTransition(0,0);
        finish();
        return true;
    }
}