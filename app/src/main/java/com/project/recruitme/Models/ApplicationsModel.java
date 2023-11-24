package com.project.recruitme.Models;
import java.util.ArrayList;

public class ApplicationsModel {
    public String JobId;
    public ArrayList<String> StudentIds;

    public ApplicationsModel(){

    }
    public ApplicationsModel(String JobId, ArrayList<String> StudentIds){
        this.JobId = JobId;
        this.StudentIds = StudentIds;
    }
}
