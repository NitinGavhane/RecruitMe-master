package com.project.recruitme.Persistence;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.project.recruitme.Models.JobModel;
import com.project.recruitme.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class FirebaseJobReference {

    public interface OnAddedCallback{
        void onSuccess();
        void onFailure(String error);
    }
    public interface OnGetCallback{
        void onSuccess(JobModel job);
        void onFailure(String error);
    }
    public interface OnGetAllCallback{
        void onSuccess(List<JobModel> job);
        void onFailure(String error);
    }
    public interface  OnUpdateCallback{
        void onSuccess();
        void onFailure(String error);
    }

    public interface  OnDeleteCallback{
        void onSuccess();
        void onFailure(String error);
    }


    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference JobReference = db.collection(Constants.JOB_COLLECTION);

    public void addJob(JobModel jobModel, OnAddedCallback callback){
        JobReference.document()
                .set(jobModel)
                .addOnSuccessListener(aVoid->callback.onSuccess())
                .addOnFailureListener(error->callback.onFailure(error.getMessage()));
    }
    public void getJobs(OnGetAllCallback callback){
        JobReference.orderBy("CreatedOn", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<JobModel> jobs = new ArrayList<>();
                    for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                        JobModel job = doc.toObject(JobModel.class);
                        job.DocId = doc.getId();
                        jobs.add(job);
                    }
                    callback.onSuccess(jobs);
                })
                .addOnFailureListener(error->callback.onFailure(error.getMessage()));
    }


    public void deleteJob(String JobId, OnDeleteCallback callback){
        JobReference.document(JobId)
                .delete()
                .addOnSuccessListener(aVoid->callback.onSuccess())
                .addOnFailureListener(error-> callback.onFailure(error.getMessage()));
    }

    public void getJob(String JobId, OnGetCallback callback)
    {
        JobReference.document(JobId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(!documentSnapshot.exists()) callback.onFailure("Job not found");
                    JobModel jobModel = documentSnapshot.toObject(JobModel.class);
                    callback.onSuccess(jobModel);
                })
                .addOnFailureListener(error-> callback.onFailure(error.getMessage()));
    }

    public void updateJob(String DocId, JobModel jobModel, OnUpdateCallback callback){
        JobReference.document(DocId)
                .set(jobModel, SetOptions.merge())
                .addOnSuccessListener(avoid -> callback.onSuccess())
                .addOnFailureListener(error -> callback.onFailure(error.getMessage()));
    }
}
