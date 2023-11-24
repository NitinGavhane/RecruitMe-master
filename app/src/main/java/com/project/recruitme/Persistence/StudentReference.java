package com.project.recruitme.Persistence;

import com.project.recruitme.Models.StudentModel;
import com.project.recruitme.Models.UserModel;

import java.util.List;

public interface StudentReference {
    void addStudent(StudentModel student, StudentReference.OnStudentAddedCallback callback);
    void getStudent(String Uid, StudentReference.OnStudentRetrievedCallback callback);
    void getStudents(StudentReference.OnStudentRetrievedCallback callback);

    void updateStudent(String Uid, StudentModel student, OnStudentUpdatedCallback callback);

    interface OnStudentUpdatedCallback{
        void onSuccess();
        void onFailure(String error);
    }
    interface OnStudentAddedCallback {
        void onSuccess();
        void onFailure(String error);
    }

    interface OnStudentRetrievedCallback {
        void onSuccess(StudentModel student);
        void onSuccess(List<StudentModel> students);
        void onFailure(String error);
    }

}
