package com.project.recruitme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class AdminProfileActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    BottomNavigationView adminNavigation;
    Button LogoutButton, UpdatePassword;
    TextView AdminEmail;
    FirebaseAuth mAuth;
    ProgressBar LogoutProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        mAuth = FirebaseAuth.getInstance();

        adminNavigation = findViewById(R.id.AdminNavigation);
        adminNavigation.setOnItemSelectedListener(this);
        adminNavigation.setSelectedItemId(R.id.adminProfile);
        LogoutButton = findViewById(R.id.AdminLogout);
        UpdatePassword = findViewById(R.id.AdminPasswordReset);
        AdminEmail = findViewById(R.id.AdminProfileEmail);
        LogoutProgress = findViewById(R.id.LogoutProgress);

        AdminEmail.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());

        LogoutButton.setOnClickListener(view -> Logout());

        UpdatePassword.setOnClickListener(view -> UpdatePassword());


    }

    public void UpdatePassword(){

        LogoutProgress.setVisibility(View.VISIBLE);
        UpdatePassword.setVisibility(View.GONE);

        if(mAuth.getCurrentUser()!=null)
        {
            mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail())
                    .addOnCompleteListener(task -> {
                        Toast.makeText(AdminProfileActivity.this, "Password reset email sent", Toast.LENGTH_LONG).show();
                        LogoutProgress.setVisibility(View.GONE);
                        UpdatePassword.setVisibility(View.VISIBLE);
                    });
        }
    }

    public void Logout(){
        LogoutButton.setVisibility(View.GONE);
        LogoutProgress.setVisibility(View.VISIBLE);
        mAuth.signOut();

        Intent intent = new Intent(AdminProfileActivity.this,
                MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        AdminProfileActivity.this.finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.adminProfile:
                return true;

            case R.id.adminDashboard:
                startActivity(new Intent(getApplicationContext(),AdminDashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            default:
                return false;
        }
        overridePendingTransition(0,0);
        finish();
        return true;
    }
}