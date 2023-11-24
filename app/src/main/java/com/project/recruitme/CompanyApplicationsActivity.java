package com.project.recruitme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.project.recruitme.Models.ApplicationsModel;
import com.project.recruitme.Models.JobModel;
import com.project.recruitme.Models.UserModel;
import com.project.recruitme.Persistence.FirebaseApplicationReference;
import com.project.recruitme.Persistence.FirebaseJobReference;
import com.project.recruitme.Persistence.FirebaseStudentReference;
import com.project.recruitme.Persistence.FirebaseUserReference;
import com.project.recruitme.Persistence.UserReference;
import com.project.recruitme.Utils.Utils;

import java.util.List;

public class CompanyApplicationsActivity extends AppCompatActivity {

    ImageView BackButton;
    TextView JobTitleField, ApplicationCountField;
    ListView AppliedStudentsList;
    String JobId;
    FirebaseApplicationReference applicationReference;
    FirebaseJobReference jobReference;
    BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_applications);
        JobId = getIntent().getStringExtra("jobId");

        if(JobId== null){
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            onBackPressed();
        }

        JobTitleField = findViewById(R.id.jobTitleField);
        ApplicationCountField = findViewById(R.id.totalApplicationsField);
        AppliedStudentsList = findViewById(R.id.AppliedStudentsList);

        applicationReference = new FirebaseApplicationReference();
        jobReference = new FirebaseJobReference();

        jobReference.getJob(JobId, new FirebaseJobReference.OnGetCallback() {
            @Override
            public void onSuccess(JobModel job) {
                JobTitleField.setText(job.Title);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        applicationReference.GetApplications(JobId, new FirebaseApplicationReference.OnApplicationsLoadedCallback() {
            @Override
            public void OnSuccess(ApplicationsModel applications) {
                String TotalApplication = "Total Applications: ";

                if(applications.StudentIds == null || applications.StudentIds.size() == 0) {
                    TotalApplication +="0";
                    return;
                }

                TotalApplication += applications.StudentIds.size();
                ApplicationCountField.setText(TotalApplication);

                FirebaseUserReference userReference = new FirebaseUserReference();
                userReference.getUsers(applications.StudentIds, new UserReference.OnUsersRetrievedCallback() {
                    @Override
                    public void onSuccess(List<UserModel> users) {

                        adapter = new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return  users.size();
                            }

                            @Override
                            public Object getItem(int i) {
                                return users.get(i);
                            }


                            @Override
                            public long getItemId(int position) {
                                return position;
                            }

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                // inflate layout for list item
                                if (convertView == null) {
                                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_job_ui, parent, false);
                                }

                                // get document snapshot for current position
                                UserModel user = users.get(position);

                                TextView JobTitle = convertView.findViewById(R.id.jobTitleTV);
                                TextView JobDescription = convertView.findViewById(R.id.jobDescriptionTV);
                                JobTitle.setText(user.UserName);
                                JobDescription.setText(user.Email);

                                // set onclick listener for list item
                                convertView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // start new activity to display full user data
                                        Intent intent = new Intent(CompanyApplicationsActivity.this, CompanyStudentActivity.class);
                                        intent.putExtra("StudentId", user.UserId);
                                        intent.putExtra("JobId", JobId);
                                        startActivity(intent);
                                    }
                                });

                                return convertView;
                            }
                        };
                        AppliedStudentsList.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(String error) {

                    }
                });

            }

            @Override
            public void OnFailure(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        });


        BackButton = findViewById(R.id.BackButton);
        BackButton.setOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
       }
}