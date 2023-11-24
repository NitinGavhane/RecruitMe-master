package com.project.recruitme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.recruitme.Models.JobModel;
import com.project.recruitme.Models.StudentModel;
import com.project.recruitme.Models.UserModel;
import com.project.recruitme.Persistence.FirebaseJobReference;
import com.project.recruitme.Persistence.FirebaseStudentReference;
import com.project.recruitme.Persistence.FirebaseUserReference;
import com.project.recruitme.Persistence.StudentReference;
import com.project.recruitme.Persistence.UserReference;

import java.util.List;

public class CompanyStudentActivity extends AppCompatActivity {

    String StudentId, JobId;
    FirebaseStudentReference studentReference;
    FirebaseUserReference userReference;
    FirebaseJobReference jobReference;

    ImageView BackButton;
    TextView JobTitle, StudentName, StudentEmail, StudentBranch, StudentCGPA,StudentGradYear, StudentBacklogs, StudentWorkExperience, StudentSkills, StudentProjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_student);

        JobId = getIntent().getStringExtra("JobId");

        if(JobId== null){
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            onBackPressed();
        }

        StudentId = getIntent().getStringExtra("StudentId");

        if(StudentId== null){
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            onBackPressed();
        }

        JobTitle = findViewById(R.id.jobTitleTV);
        StudentName = findViewById(R.id.StudentName);
        StudentEmail = findViewById(R.id.StudentEmail);
        StudentBranch= findViewById(R.id.StudentBranch);
        StudentCGPA = findViewById(R.id.StudentCGPA);
        StudentGradYear = findViewById(R.id.StudentGradYear);
        StudentBacklogs = findViewById(R.id.StudentBacklogs);
        StudentWorkExperience = findViewById(R.id.StudentWorkExperience);
        StudentSkills = findViewById(R.id.StudentSkills);
        StudentProjects = findViewById(R.id.StudentProjects);


        jobReference = new FirebaseJobReference();
        userReference = new FirebaseUserReference();
        studentReference = new FirebaseStudentReference();

        jobReference.getJob(JobId, new FirebaseJobReference.OnGetCallback() {
            @Override
            public void onSuccess(JobModel job) {
                JobTitle.setText(job.Title);
            }

            @Override
            public void onFailure(String error) {

            }
        });

        userReference.getUser(StudentId, new UserReference.OnUserRetrievedCallback() {
            @Override
            public void onSuccess(UserModel user) {
                StudentName.setText(user.UserName);
                StudentEmail.setText(user.Email);
            }

            @Override
            public void onFailure(String error) {

            }
        });

        studentReference.getStudent(StudentId, new StudentReference.OnStudentRetrievedCallback() {
            @Override
            public void onSuccess(StudentModel student) {
                StudentBranch.setText(student.Branch);
                StudentGradYear.setText(student.GraduationYear);
                StudentCGPA.setText(student.CGPA +"");
                StudentWorkExperience.setText(student.WorkExperience);
                StudentProjects.setText(student.Projects);
                StudentSkills.setText(student.Skills);
                StudentBacklogs.setText(student.Backlogs+"");
            }

            @Override
            public void onSuccess(List<StudentModel> students) {

            }

            @Override
            public void onFailure(String error) {

            }
        });



        BackButton = findViewById(R.id.BackButton);
        BackButton.setOnClickListener(view -> onBackPressed());

    }
}