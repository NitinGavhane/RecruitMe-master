package com.project.recruitme.Persistence;

import com.project.recruitme.Models.CompanyModel;
import com.project.recruitme.Models.StudentModel;

import java.util.List;

public interface CompanyReference {
    void addCompany(CompanyModel company, OnCompanyAddedCallback callback);
    void getCompany(String Uid, OnCompanyRetrievedCallback callback);
    void getCompanies(OnCompaniesRetrievedCallback callback);
    void updateCompany(String Uid, CompanyModel company, OnCompanyUpdatedCallback callback);

    interface OnCompanyUpdatedCallback{
        void onSuccess();
        void onFailure(String error);
    }
    interface OnCompanyAddedCallback {
        void onSuccess();
        void onFailure(String error);
    }
    interface OnCompaniesRetrievedCallback{
        void onSuccess(List<CompanyModel> companies);
        void onFailure(String error);

    }
    interface OnCompanyRetrievedCallback {
        void onSuccess(CompanyModel company);
        void onFailure(String error);
    }
}
