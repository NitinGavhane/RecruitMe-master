package com.project.recruitme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class AdminCompaniesActivity extends AppCompatActivity {

    ImageView backButton, addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_companies);


        backButton = findViewById(R.id.BackButton);
        addButton = findViewById(R.id.AddButton);
        backButton.setOnClickListener(view -> onBackPressed());
        addButton.setOnClickListener(view -> startActivity(new Intent(AdminCompaniesActivity.this, AdminAddCompanyActivity.class)));

    }
}