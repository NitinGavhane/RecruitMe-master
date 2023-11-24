package com.project.recruitme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.recruitme.Models.CompanyModel;
import com.project.recruitme.Models.JobModel;
import com.project.recruitme.Models.UserModel;
import com.project.recruitme.Persistence.CompanyReference;
import com.project.recruitme.Persistence.FirebaseApplicationReference;
import com.project.recruitme.Persistence.FirebaseCompanyReference;
import com.project.recruitme.Persistence.FirebaseJobReference;
import com.project.recruitme.Persistence.FirebaseUserReference;
import com.project.recruitme.Persistence.UserReference;
import com.project.recruitme.Utils.Utils;

public class StudentJobActivity extends AppCompatActivity {

    String JobId;
    JobModel Job;
    FirebaseJobReference jobReference;
    FirebaseCompanyReference companyReference;
    FirebaseUserReference userReference;
    FirebaseApplicationReference applicationReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView JobTitleField, JobDescriptionField, SalaryRangeField, SkillsField, CompanyName, CompanyLocation, AlreadyApplied;
    Button ApplyButton;
    ImageView BackButton;
    ProgressBar ApplicationProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_job);

        JobId = getIntent().getStringExtra("jobId");

        if(JobId== null){
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            onBackPressed();
        }

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user == null){
            Toast.makeText(getApplicationContext(),"Something went wrong.",Toast.LENGTH_LONG ).show();
            Intent intent = new Intent(StudentJobActivity.this,
                    MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            StudentJobActivity.this.finish();
        }

        JobTitleField = findViewById(R.id.jobTitleField);
        JobDescriptionField = findViewById(R.id.jobDescriptionField);
        SalaryRangeField = findViewById(R.id.salaryRangeField);
        SkillsField = findViewById(R.id.skillsField);
        CompanyName = findViewById(R.id.CompanyField);
        CompanyLocation = findViewById(R.id.CompanyLocation);
        ApplyButton = findViewById(R.id.ApplyButton);
        AlreadyApplied = findViewById(R.id.AlreadyApplied);
        BackButton = findViewById(R.id.BackButton);
        ApplicationProgress = findViewById(R.id.ApplicationProgress);

        jobReference = new FirebaseJobReference();
        userReference = new FirebaseUserReference();
        companyReference = new FirebaseCompanyReference();
        applicationReference = new FirebaseApplicationReference();


        jobReference.getJob(JobId, new FirebaseJobReference.OnGetCallback() {
            @Override
            public void onSuccess(JobModel job) {
                Job =job;
                JobTitleField.setText(job.Title);
                JobDescriptionField.setText(job.Description);
                SalaryRangeField.setText(Utils.formatFloatRange(job.MinSalary, job.MaxSalary));
                SkillsField.setText(job.Skills);

                userReference.getUser(job.CompanyId, new UserReference.OnUserRetrievedCallback() {
                    @Override
                    public void onSuccess(UserModel user) {
                        CompanyName.setText(user.UserName);
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(getApplicationContext(),"Couldn't load Company",Toast.LENGTH_LONG ).show();
                    }
                });

                companyReference.getCompany(job.CompanyId, new CompanyReference.OnCompanyRetrievedCallback() {
                    @Override
                    public void onSuccess(CompanyModel company) {
                        CompanyLocation.setText(company.Location);
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(getApplicationContext(),"Couldn't load Company Location",Toast.LENGTH_LONG ).show();
                    }
                });

                applicationReference.HasApplied(JobId, user.getUid(), new FirebaseApplicationReference.OnApplicationCheckedCallback() {
                    @Override
                    public void OnSuccess(boolean HasApplied) {
                        if(HasApplied)
                        {
                            AlreadyApplied.setVisibility(View.VISIBLE);
                            ApplyButton.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void OnFailure(String error) {

                    }
                });
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });

        BackButton.setOnClickListener(view->onBackPressed());



        ApplyButton.setOnClickListener(view->{

            ApplicationProgress.setVisibility(View.VISIBLE);
            ApplyButton.setVisibility(View.GONE);

            applicationReference.AddApplication(JobId, user.getUid(), new FirebaseApplicationReference.OnApplicationSubmittedCallback() {
                @Override
                public void OnSuccess() {
                    Toast.makeText(getApplicationContext(),"Applied Successfully",Toast.LENGTH_LONG ).show();

                    ApplicationProgress.setVisibility(View.GONE);
                    AlreadyApplied.setVisibility(View.VISIBLE);
                    ApplyButton.setVisibility(View.GONE);
                }

                @Override
                public void OnFailure(String error) {
                    Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG ).show();

                    ApplicationProgress.setVisibility(View.GONE);
                    ApplyButton.setVisibility(View.VISIBLE);
                }
            });
        });
    }
}