package com.project.recruitme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.project.recruitme.Models.JobModel;
import com.project.recruitme.Persistence.FirebaseJobReference;
import com.project.recruitme.Utils.Utils;

import java.util.List;

public class StudentJobsActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    ListView JobsList;
    BottomNavigationView studentNavigation;
    FirebaseJobReference jobReference;
    BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_jobs);

        studentNavigation = findViewById(R.id.StudentNavigation);
        studentNavigation.setOnItemSelectedListener(this);
        studentNavigation.setSelectedItemId(R.id.StudentJobs);
        JobsList = findViewById(R.id.JobsList);

        jobReference = new FirebaseJobReference();
    }

    @Override
    protected void onResume() {
        super.onResume();
        jobReference.getJobs(new FirebaseJobReference.OnGetAllCallback() {
            @Override
            public void onSuccess(List<JobModel> jobs) {
                adapter = new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return jobs.size();
                    }

                    @Override
                    public Object getItem(int i) {
                        return jobs.get(i);
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
                        JobModel job = jobs.get(position);

                        // set values for textviews
                        TextView JobTitle = convertView.findViewById(R.id.jobTitleTV);
                        TextView JobDescription = convertView.findViewById(R.id.jobDescriptionTV);
                        TextView JobSalary = convertView.findViewById(R.id.jobSalaryTV);
                        JobTitle.setText(job.Title);
                        JobDescription.setText(Utils.truncateString(job.Description));
                        JobSalary.setText(Utils.formatFloatRange(job.MinSalary, job.MaxSalary));

                        // set onclick listener for list item
                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // get document id for clicked item
                                String docId = job.DocId;
                                // start new activity to display full user data
                                Intent intent = new Intent(StudentJobsActivity.this, StudentJobActivity.class);
                                intent.putExtra("jobId", docId);
                                startActivity(intent);
                            }
                        });

                        return convertView;
                    }
                };
                JobsList.setAdapter(adapter);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), "Failed to load Jobs", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.StudentDashboard:
                startActivity(new Intent(getApplicationContext(),StudentDashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.StudentJobs:
                return true;

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