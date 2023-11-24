package com.project.recruitme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.recruitme.Models.StudentModel;
import com.project.recruitme.Models.UserModel;
import com.project.recruitme.Persistence.FirebaseStudentReference;
import com.project.recruitme.Persistence.FirebaseUserReference;
import com.project.recruitme.Persistence.StudentReference;
import com.project.recruitme.Persistence.UserReference;
import com.project.recruitme.Utils.Constants;

public class AdminStudentAddActivity extends AppCompatActivity {

    EditText nameField, emailField, phoneField, gradYearField, branchField;
    ImageView backButton;
    ProgressBar progressBar;
    Button addButton;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_add);


        backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(view -> onBackPressed());

        nameField = findViewById(R.id.nameField);
        emailField = findViewById(R.id.emailField);
        phoneField = findViewById(R.id.phoneField);
        gradYearField = findViewById(R.id.gradYearField);
        branchField = findViewById(R.id.branchField);

        progressBar= findViewById(R.id.AddStudentProgress);
        addButton = findViewById(R.id.AddStudentButton);

        addButton.setOnClickListener(view->AddStudent());
    }

    void AddStudent(){
        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();
        String gradYear = gradYearField.getText().toString().trim();
        String branch = branchField.getText().toString().trim();

        // Validate input
        if (name.isEmpty()) {
            nameField.setError("Name is required");
            return;
        }
        if (email.isEmpty()) {
            emailField.setError("Email is required");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Invalid email format");
            return;
        }
        if (phone.isEmpty()) {
            phoneField.setError("Phone number is required");
            return;
        }
        if (!Patterns.PHONE.matcher(phone).matches()) {
            phoneField.setError("Invalid phone number format");
            return;
        }
        if (gradYear.isEmpty()) {
            gradYearField.setError("Graduation year is required");
            return;
        }
        if (branch.isEmpty()) {
            branchField.setError("Branch is required");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.GONE);

        mAuth.createUserWithEmailAndPassword(email, Constants.DEFAULT_STUDENT_PASSWORD)
                .addOnSuccessListener(authResult -> {
                    String UserId= authResult.getUser().getUid();

                    UserModel user = new UserModel(name, Constants.STUDENT_ROLE, UserId, phone, email);

                    UserReference userReference = new FirebaseUserReference();
                    userReference.addUser(user, new UserReference.OnUserAddedCallback(){

                        @Override
                        public void onSuccess() {
                            StudentReference studentReference = new FirebaseStudentReference();
                            StudentModel student = new StudentModel(UserId, gradYear, branch);
                            studentReference.addStudent(student,new StudentReference.OnStudentAddedCallback() {
                                @Override
                                public void onSuccess(){
                                    Toast.makeText(AdminStudentAddActivity.this, "Student Added successfully.", Toast.LENGTH_LONG).show();
                                    signAdminBackIn();
                                    onBackPressed();
                                }

                                @Override
                                public void onFailure(String error) {
                                    Toast.makeText(AdminStudentAddActivity.this, error, Toast.LENGTH_LONG).show();
                                    mAuth.getCurrentUser().delete();
                                    userReference.deleteUser(UserId, new UserReference.OnUserDeletedCallback() {
                                        @Override
                                        public void onSuccess() {
                                        }

                                        @Override
                                        public void onFailure(String error) {
                                        }
                                    });
                                    signAdminBackIn();
                                }
                            });
                        }

                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(AdminStudentAddActivity.this, error, Toast.LENGTH_LONG).show();
                            mAuth.getCurrentUser().delete();
                            signAdminBackIn();
                        }
                    });

                })
                .addOnFailureListener(authResult->{
                    Toast.makeText(AdminStudentAddActivity.this, authResult.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    addButton.setVisibility(View.VISIBLE);
                    signAdminBackIn();
                });

    }

    private void signAdminBackIn() {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCE_STRING, Context.MODE_PRIVATE);

        String email = prefs.getString(Constants.EMAIL_STORAGE_STRING, "");
        String password = prefs.getString(Constants.PASSWORD_STORAGE_STRING, "");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnFailureListener(e -> {
                    // Handle error
                    Toast.makeText(AdminStudentAddActivity.this, "Something went wrong, Please sign in again", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AdminStudentAddActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                });
    }
}