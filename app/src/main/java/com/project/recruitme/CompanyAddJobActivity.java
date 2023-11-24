package com.project.recruitme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.recruitme.Models.JobModel;
import com.project.recruitme.Persistence.FirebaseJobReference;
import com.project.recruitme.Utils.Utils;

public class CompanyAddJobActivity extends AppCompatActivity {

    EditText JobTitleField, JobDescriptionField, RequiredSkillsField, MinSalaryField, MaxSalaryField;
    Button AddJobButton;
    ProgressBar AddJobProgress;
    ImageView BackButton;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_add_job);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user == null){
            Toast.makeText(getApplicationContext(), "Something went wrong, please login again to continue.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(CompanyAddJobActivity.this,
                    MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CompanyAddJobActivity.this.finish();
        }


        JobTitleField = findViewById(R.id.jobTitleField);
        JobDescriptionField = findViewById(R.id.jobDescriptionField);
        RequiredSkillsField = findViewById(R.id.skillsField);
        MinSalaryField = findViewById(R.id.minSalary);
        MaxSalaryField= findViewById(R.id.maxSalary);

        AddJobButton = findViewById(R.id.AddJobButton);
        AddJobProgress = findViewById(R.id.AddJobProgress);

        BackButton = findViewById(R.id.BackButton);
        
        BackButton.setOnClickListener(view -> onBackPressed());
        AddJobButton.setOnClickListener(view -> addJob());
    }

    private void addJob() {
        String JobTitle = JobTitleField.getText().toString().trim();
        String JobDescription = JobDescriptionField.getText().toString().trim();
        String RequiredSkills = RequiredSkillsField.getText().toString().trim();
        String MinSalaryS = MinSalaryField.getText().toString().trim();
        String MaxSalaryS = MaxSalaryField.getText().toString().trim();

        Float MinSalary = Utils.tryParseFloat(MinSalaryS);
        Float MaxSalary = Utils.tryParseFloat(MaxSalaryS);

        if(TextUtils.isEmpty(JobTitle))
        {
            JobTitleField.setError("Please enter a job title");
            return;
        }


        if(TextUtils.isEmpty(JobDescription))
        {
            JobDescriptionField.setError("Please enter job description");
            return;
        }


        if(TextUtils.isEmpty(RequiredSkills))
        {
            RequiredSkillsField.setError("Please enter required skills");
            return;
        }

        if(MinSalary == null) {
            MinSalaryField.setError("Please enter a valid LPA");
            return;
        }


        if(MaxSalary == null) {
            MaxSalaryField.setError("Please enter a valid LPA");
            return;
        }

        AddJobButton.setVisibility(View.GONE);
        AddJobProgress.setVisibility(View.VISIBLE);

        JobModel jobModel = new JobModel(JobTitle, JobDescription, RequiredSkills, MinSalary, MaxSalary, user.getUid());

        FirebaseJobReference jobReference = new FirebaseJobReference();
        jobReference.addJob(jobModel, new FirebaseJobReference.OnAddedCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Job added Successfully!", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                AddJobButton.setVisibility(View.VISIBLE);
                AddJobProgress.setVisibility(View.GONE);
            }
        });

    }
}