package com.project.recruitme.Persistence;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.project.recruitme.Models.ApplicationsModel;
import com.project.recruitme.Utils.Constants;

import java.util.ArrayList;

public class FirebaseApplicationReference {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference ApplicationsCollectionRef = db.collection(Constants.APPLICATION_COLLECTION);

    public interface OnApplicationsLoadedCallback {
        void OnSuccess(ApplicationsModel applications);
        void OnFailure(String error);
    }

    public interface OnApplicationSubmittedCallback{
        void OnSuccess();
        void OnFailure(String error);
    }

    public interface OnApplicationCheckedCallback{
        void OnSuccess(boolean HasApplied);
        void OnFailure(String error);
    }

    public void HasApplied(String JobId, String StudentId, OnApplicationCheckedCallback callback){
        ApplicationsCollectionRef
                .document(JobId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        ApplicationsModel applicationsModel = documentSnapshot.toObject(ApplicationsModel.class);
                        if(applicationsModel.StudentIds!=null && applicationsModel.StudentIds.contains(StudentId)){
                            callback.OnSuccess(true);
                        } else callback.OnSuccess(false);
                    } else callback.OnSuccess(false);
                })
                .addOnFailureListener(error-> callback.OnFailure(error.getMessage()));
    }

    public void AddApplication(String JobId, String StudentId, OnApplicationSubmittedCallback callback ){
        ApplicationsCollectionRef
                .document(JobId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        ApplicationsModel applicationsModel = documentSnapshot.toObject(ApplicationsModel.class);
                        if(applicationsModel.StudentIds == null)
                            applicationsModel.StudentIds = new ArrayList<>();
                        applicationsModel.StudentIds.add(StudentId);

                        ApplicationsCollectionRef
                                .document(JobId)
                                .set(applicationsModel, SetOptions.merge())
                                .addOnSuccessListener(aVoid->callback.OnSuccess())
                                .addOnFailureListener(error->callback.OnFailure(error.getMessage()));


                    } else {
                        ApplicationsModel applicationsModel = new ApplicationsModel(JobId, new ArrayList<>());
                        applicationsModel.StudentIds.add(StudentId);
                        ApplicationsCollectionRef
                                .document(JobId)
                                .set(applicationsModel, SetOptions.merge())
                                .addOnSuccessListener(aVoid->callback.OnSuccess())
                                .addOnFailureListener(error->callback.OnFailure(error.getMessage()));
                    }
                })
                .addOnFailureListener(error-> callback.OnFailure(error.getMessage()));
    }

    public void GetApplications(String JobId, OnApplicationsLoadedCallback callback){
        ApplicationsCollectionRef
                .document(JobId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        ApplicationsModel applicationsModel = documentSnapshot.toObject(ApplicationsModel.class);
                        callback.OnSuccess(applicationsModel);
                    } else {
                        callback.OnFailure("No Applications yet");
                    }
                })
                .addOnFailureListener(error-> callback.OnFailure(error.getMessage()));
    }
}
