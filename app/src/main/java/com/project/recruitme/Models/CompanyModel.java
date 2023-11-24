package com.project.recruitme.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class CompanyModel {
    public String UserId;
    public String Location;
    public String Industry;
    public String Website;
    public String Description;

    @ServerTimestamp public Date CreatedOn;

    public CompanyModel(String UserId, String Location){
        this.UserId = UserId;
        this.Location = Location;
    }

    public CompanyModel(){

    }
}
