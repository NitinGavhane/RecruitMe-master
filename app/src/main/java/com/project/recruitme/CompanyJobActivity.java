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
import android.widget.TextView;
import android.widget.Toast;

import com.project.recruitme.Models.ApplicationsModel;
import com.project.recruitme.Models.JobModel;
import com.project.recruitme.Persistence.FirebaseJobReference;
import com.project.recruitme.Utils.Utils;

public class CompanyJobActivity extends AppCompatActivity {

    String JobId;
    JobModel Job;
    FirebaseJobReference jobReference;
    EditText JobDescriptionField, MinSalaryField, MaxSalaryField, SkillsField;
    TextView JobTitleField;
    Button UpdateJobButton, ApplicationsButton, DeleteJobButton;
    ImageView BackButton;
    ProgressBar UpdateJobProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_job);

        JobId = getIntent().getStringExtra("jobId");

        if(JobId== null){
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            onBackPressed();
        }

        JobTitleField = findViewById(R.id.jobTitleField);
        JobDescriptionField = findViewById(R.id.jobDescriptionField);
        MinSalaryField = findViewById(R.id.minSalary);
        MaxSalaryField = findViewById(R.id.maxSalary);
        SkillsField = findViewById(R.id.skillsField);
        jobReference = new FirebaseJobReference();
        jobReference.getJob(JobId, new FirebaseJobReference.OnGetCallback() {
            @Override
            public void onSuccess(JobModel job) {
                Job =job;
                JobTitleField.setText(job.Title);
                JobDescriptionField.setText(job.Description);
                MinSalaryField.setText(job.MinSalary +"");
                MaxSalaryField.setText(job.MaxSalary+"");
                SkillsField.setText(job.Skills);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });

        BackButton = findViewById(R.id.BackButton);
        BackButton.setOnClickListener(view->onBackPressed());

        UpdateJobButton = findViewById(R.id.UpdateJobButton);
        DeleteJobButton = findViewById(R.id.DeleteJobButton);
        ApplicationsButton = findViewById(R.id.ApplicationsButton);
        ApplicationsButton.setOnClickListener(view -> {
            Intent intent = new Intent(CompanyJobActivity.this, CompanyApplicationsActivity.class);
            intent.putExtra("jobId", JobId);
            startActivity(intent);
        });

        UpdateJobProgress = findViewById(R.id.UpdateJobProgress);

        UpdateJobButton.setOnClickListener(view->UpdateJob());
        DeleteJobButton.setOnClickListener(view -> DeleteJob());
    }

    private void DeleteJob(){

        DeleteJobButton.setVisibility(View.GONE);
        UpdateJobProgress.setVisibility(View.VISIBLE);
        jobReference.deleteJob(JobId, new FirebaseJobReference.OnDeleteCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Job Deleted", Toast.LENGTH_LONG).show();
                onBackPressed();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                DeleteJobButton.setVisibility(View.GONE);
                UpdateJobProgress.setVisibility(View.VISIBLE);
            }
        });
    }
    private void UpdateJob(){

        String JobDescription = JobDescriptionField.getText().toString().trim();
        String RequiredSkills = SkillsField.getText().toString().trim();
        String MinSalaryS = MinSalaryField.getText().toString().trim();
        String MaxSalaryS = MaxSalaryField.getText().toString().trim();

        Float MinSalary = Utils.tryParseFloat(MinSalaryS);
        Float MaxSalary = Utils.tryParseFloat(MaxSalaryS);


        if(TextUtils.isEmpty(JobDescription))
        {
            JobDescriptionField.setError("Please enter job description");
            return;
        }


        if(TextUtils.isEmpty(RequiredSkills))
        {
            SkillsField.setError("Please enter required skills");
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

        UpdateJobButton.setVisibility(View.GONE);
        UpdateJobProgress.setVisibility(View.VISIBLE);

        Job.Description = JobDescription;
        Job.MinSalary = MinSalary;
        Job.MaxSalary = MaxSalary;
        Job.Skills = RequiredSkills;

        jobReference.updateJob(JobId, Job, new FirebaseJobReference.OnUpdateCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Job Updated successfully!", Toast.LENGTH_LONG).show();
                UpdateJobButton.setVisibility(View.VISIBLE);
                UpdateJobProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                UpdateJobButton.setVisibility(View.VISIBLE);
                UpdateJobProgress.setVisibility(View.GONE);
            }
        });

    }
}