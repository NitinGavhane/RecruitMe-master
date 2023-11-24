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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.project.recruitme.Models.CompanyModel;
import com.project.recruitme.Models.StudentModel;
import com.project.recruitme.Models.UserModel;
import com.project.recruitme.Persistence.CompanyReference;
import com.project.recruitme.Persistence.FirebaseCompanyReference;
import com.project.recruitme.Persistence.FirebaseStudentReference;
import com.project.recruitme.Persistence.FirebaseUserReference;
import com.project.recruitme.Persistence.StudentReference;
import com.project.recruitme.Persistence.UserReference;
import com.project.recruitme.Utils.Constants;

public class AdminAddCompanyActivity extends AppCompatActivity {

    EditText nameField, emailField, phoneField, locationField;
    ImageView backButton;
    ProgressBar progressBar;
    Button addButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_company);

        mAuth = FirebaseAuth.getInstance();

        backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(view -> onBackPressed());

        nameField = findViewById(R.id.nameField);
        emailField = findViewById(R.id.emailField);
        phoneField = findViewById(R.id.phoneField);
        locationField = findViewById(R.id.locationField);

        progressBar= findViewById(R.id.AddCompanyProgress);
        addButton = findViewById(R.id.AddCompanyButton);

        addButton.setOnClickListener(view->AddCompany());

    }

    public void AddCompany(){

        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();
        String location = locationField.getText().toString().trim();

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
        if (location.isEmpty()) {
            locationField.setError("Name is required");
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.GONE);
        mAuth.createUserWithEmailAndPassword(email, Constants.DEFAULT_COMPANY_PASSWORD)
                .addOnSuccessListener(authResult -> {
                    String UserId= authResult.getUser().getUid();
                    UserModel user = new UserModel(name, Constants.COMPANY_ROLE, UserId, phone, email);
                    UserReference userReference = new FirebaseUserReference();
                    userReference.addUser(user, new UserReference.OnUserAddedCallback(){

                        @Override
                        public void onSuccess() {
                            CompanyReference companyReference = new FirebaseCompanyReference();
                            CompanyModel company = new CompanyModel(UserId,location );
                            companyReference.addCompany(company, new CompanyReference.OnCompanyAddedCallback() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(AdminAddCompanyActivity.this, "Company Added successfully.", Toast.LENGTH_LONG).show();
                                    signAdminBackIn();
                                    onBackPressed();
                                }

                                @Override
                                public void onFailure(String error) {
                                    Toast.makeText(AdminAddCompanyActivity.this, error, Toast.LENGTH_LONG).show();
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
                            Toast.makeText(AdminAddCompanyActivity.this, error, Toast.LENGTH_LONG).show();
                            mAuth.getCurrentUser().delete();
                            signAdminBackIn();
                        }
                    });

                })
                .addOnFailureListener(authResult->{
                    Toast.makeText(AdminAddCompanyActivity.this, authResult.getMessage(), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(AdminAddCompanyActivity.this, "Something went wrong, Please sign in again", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AdminAddCompanyActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                });
    }

}