package com.project.recruitme.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class JobModel {

    public String DocId;
    public String CompanyId;
    public String Title;
    public String Description;
    public float MinSalary;
    public float MaxSalary;
    public String Skills;

    @ServerTimestamp
    public Date CreatedOn;
    public JobModel(){

    }

    public JobModel(String Title, String Description, String Skills, float MinSalary, float MaxSalary, String CompanyId){
        this.Title = Title;
        this.Description = Description;
        this.Skills = Skills;
        this.MaxSalary = MaxSalary;
        this.MinSalary = MinSalary;
        this.CompanyId = CompanyId;
    }
}
