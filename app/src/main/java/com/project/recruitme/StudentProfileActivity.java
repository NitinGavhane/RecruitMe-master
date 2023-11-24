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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.recruitme.Models.StudentModel;
import com.project.recruitme.Persistence.FirebaseStudentReference;
import com.project.recruitme.Persistence.FirebaseUserReference;
import com.project.recruitme.Persistence.StudentReference;
import com.project.recruitme.Persistence.UserReference;
import com.project.recruitme.Utils.Constants;
import com.project.recruitme.Utils.Utils;

import java.util.List;

public class StudentProfileActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {


    BottomNavigationView studentNavigation;
    SharedPreferences pref;
    TextView StudentName, StudentEmail, StudentBranch, StudentGradYear;
    EditText CGPAField, BacklogsField, SkillsField, WorkExperienceField, ProjectsField;
    FirebaseAuth mAuth;
    StudentReference studentRef;
    StudentModel studentModel;
    Button LogoutButton, UpdateProfileButton;
    ProgressBar LogoutProgress, UpdateProfileProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        LogoutProgress = findViewById(R.id.LogoutProgress);
        LogoutButton = findViewById(R.id.AdminLogout);

        if(user == null) {
            Logout();
        }

        studentNavigation = findViewById(R.id.StudentNavigation);
        studentNavigation.setOnItemSelectedListener(this);
        studentNavigation.setSelectedItemId(R.id.StudentProfile);

        pref =  StudentProfileActivity.this.getApplicationContext().getSharedPreferences(Constants.PREFERENCE_STRING, Context.MODE_PRIVATE);
        String Name = pref.getString(Constants.USER_NAME, "");

        LogoutButton.setOnClickListener(view -> Logout());

        StudentName = findViewById(R.id.StudentName);
        StudentEmail = findViewById(R.id.StudentEmail);
        StudentBranch = findViewById(R.id.StudentBranch);
        StudentGradYear = findViewById(R.id.StudentGradYear);
        CGPAField = findViewById(R.id.CGPAField);
        BacklogsField = findViewById(R.id.BacklogsField);
        SkillsField = findViewById(R.id.SkillsField);
        WorkExperienceField = findViewById(R.id.WorkExperienceField);
        ProjectsField = findViewById(R.id.ProjectsField);
        UpdateProfileButton = findViewById(R.id.UpdateProfileButton);
        UpdateProfileProgress = findViewById(R.id.UpdateProfileProgress);
        StudentEmail.setText(user.getEmail());
        StudentName.setText(Name);

        studentRef = new FirebaseStudentReference();
        studentRef.getStudent(user.getUid(), new StudentReference.OnStudentRetrievedCallback() {
            @Override
            public void onSuccess(StudentModel student) {
                studentModel = student;
                StudentBranch.setText(student.Branch);
                StudentGradYear.setText(student.GraduationYear);
                CGPAField.setText(student.CGPA +"");
                BacklogsField.setText(student.Backlogs +"");
                SkillsField.setText(student.Skills);
                WorkExperienceField.setText(student.WorkExperience);
                ProjectsField.setText(student.Projects);
            }

            @Override
            public void onSuccess(List<StudentModel> students) {

            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(StudentProfileActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
        UpdateProfileButton.setOnClickListener(view->UpdateProfile());
    }

    public void UpdateProfile(){

        UpdateProfileButton.setVisibility(View.GONE);
        UpdateProfileProgress.setVisibility(View.VISIBLE);

        String CGPAS = CGPAField.getText().toString();
        String BacklogsS = BacklogsField.getText().toString();
        String Skills = SkillsField.getText().toString();
        String WorkExperience = WorkExperienceField.getText().toString();
        String Projects = ProjectsField.getText().toString();

        Float CGPA = Utils.tryParseFloat(CGPAS);
        Integer Backlog = Utils.tryParse(BacklogsS);

        if(CGPA == null) CGPA = 0.0f;
        if(Backlog == null) Backlog = 0;
        if(CGPA>10 || CGPA<0) {
            CGPAField.setError("CGPA must be between 0-10");
            return;
        }
        if(Backlog<0) {
            BacklogsField.setError("Backlogs can not be negative");
            return;
        }

        studentModel.CGPA = CGPA;
        studentModel.Backlogs = Backlog;
        studentModel.Skills = Skills;
        studentModel.Projects = Projects;
        studentModel.WorkExperience = WorkExperience;

        studentRef.updateStudent(studentModel.UserId, studentModel, new StudentReference.OnStudentUpdatedCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(StudentProfileActivity.this, "Profile Updated successfully", Toast.LENGTH_LONG).show();
                UpdateProfileButton.setVisibility(View.VISIBLE);
                UpdateProfileProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(StudentProfileActivity.this, error, Toast.LENGTH_LONG).show();
                UpdateProfileButton.setVisibility(View.VISIBLE);
                UpdateProfileProgress.setVisibility(View.GONE);
            }
        });

    }
    public void Logout(){
        LogoutButton.setVisibility(View.GONE);
        LogoutProgress.setVisibility(View.VISIBLE);
        mAuth.signOut();

        Intent intent = new Intent(StudentProfileActivity.this,
                MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        StudentProfileActivity.this.finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.StudentDashboard:
                startActivity(new Intent(getApplicationContext(),StudentDashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.StudentJobs:
                startActivity(new Intent(getApplicationContext(),StudentJobsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.StudentProfile:
                return true;

            default:
                return false;
        }
        overridePendingTransition(0,0);
        finish();
        return true;
    }
}