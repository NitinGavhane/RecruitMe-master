package com.project.recruitme.Models;

import com.google.firebase.firestore.ServerTimestamp;
import com.project.recruitme.Persistence.StudentReference;

import java.util.Date;

public class StudentModel {
    public String UserId;
    public String GraduationYear;
    public String Branch;
    public float CGPA;
    public int Backlogs;
    public String Skills;
    public String WorkExperience;
    public String Projects;
    @ServerTimestamp
    public Date CreatedOn;

    public StudentModel(String UserId, String GraduationYear, String Branch){
        this.UserId = UserId;
        this.GraduationYear = GraduationYear;
        this.Branch = Branch;
        this.CGPA = 0;
        this.Backlogs = 0;
    }

    public StudentModel(){

    }

}
