package com.project.recruitme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

public class AdminStudentListActivity extends AppCompatActivity {

    ImageView backButton, addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_list);

        backButton = findViewById(R.id.BackButton);
        addButton = findViewById(R.id.AddButton);

        backButton.setOnClickListener(view -> onBackPressed());

        addButton.setOnClickListener(view -> startActivity(new Intent(AdminStudentListActivity.this, AdminStudentAddActivity.class)));
    }
}