package com.project.recruitme.Persistence;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.recruitme.Models.CompanyModel;
import com.project.recruitme.Models.StudentModel;
import com.project.recruitme.Utils.Constants;

import java.util.List;

public class FirebaseCompanyReference implements CompanyReference {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference CompaniesCollectionRef = db.collection(Constants.COMPANY_PROFILE_COLLECTION);


    @Override
    public void addCompany(CompanyModel company, OnCompanyAddedCallback callback) {
        CompaniesCollectionRef.document().set(company)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(error ->callback.onFailure(error.getMessage()));
    }

    @Override
    public void getCompany(String Uid, OnCompanyRetrievedCallback callback) {
        CompaniesCollectionRef
                .whereEqualTo("UserId", Uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    CompanyModel company = queryDocumentSnapshots.getDocuments().get(0).toObject(CompanyModel.class);
                    callback.onSuccess(company);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    @Override
    public void getCompanies(OnCompaniesRetrievedCallback callback) {
        CompaniesCollectionRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<CompanyModel> companies = queryDocumentSnapshots.toObjects(CompanyModel.class);
                    callback.onSuccess(companies);
                })
                .addOnFailureListener(error -> callback.onFailure(error.getMessage()));

    }

    @Override
    public void updateCompany(String Uid, CompanyModel company, OnCompanyUpdatedCallback callback) {
        CompaniesCollectionRef
                .whereEqualTo("UserId", Uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty())
                    {
                        String DocumentId= queryDocumentSnapshots.getDocuments().get(0).getId();

                        CompaniesCollectionRef
                                .document(DocumentId)
                                .set(company)
                                .addOnSuccessListener(aVoid-> callback.onSuccess())
                                .addOnFailureListener(error -> callback.onFailure(error.getMessage()));
                    }
                    else callback.onFailure("Student Not Found!");
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }
}
