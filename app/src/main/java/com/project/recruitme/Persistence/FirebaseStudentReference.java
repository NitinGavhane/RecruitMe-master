package com.project.recruitme.Persistence;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.recruitme.Models.StudentModel;
import com.project.recruitme.Models.UserModel;
import com.project.recruitme.Utils.Constants;

import java.util.List;

public class FirebaseStudentReference implements StudentReference {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference studentCollectionRef = db.collection(Constants.STUDENT_PROFILE_COLLECTION);


    @Override
    public void addStudent(StudentModel student, OnStudentAddedCallback callback) {
        studentCollectionRef.document().set(student)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(error ->callback.onFailure(error.getMessage()));
    }

    @Override
    public void getStudent(String Uid, OnStudentRetrievedCallback callback) {
        studentCollectionRef
                .whereEqualTo("UserId", Uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    StudentModel student = queryDocumentSnapshots.getDocuments().get(0).toObject(StudentModel.class);
                    callback.onSuccess(student);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    @Override
    public void getStudents(OnStudentRetrievedCallback callback) {
        studentCollectionRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<StudentModel> students = queryDocumentSnapshots.toObjects(StudentModel.class);
                    callback.onSuccess(students);
                })
                .addOnFailureListener(error -> callback.onFailure(error.getMessage()));
    }

    @Override
    public void updateStudent(String Uid, StudentModel student, OnStudentUpdatedCallback callback) {
        studentCollectionRef
                .whereEqualTo("UserId", Uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty())
                    {

                        String DocumentId= queryDocumentSnapshots.getDocuments().get(0).getId();

                        studentCollectionRef
                                .document(DocumentId)
                                .set(student)
                                .addOnSuccessListener(aVoid-> callback.onSuccess())
                                .addOnFailureListener(error -> callback.onFailure(error.getMessage()));
                    }
                    else callback.onFailure("Student Not Found!");
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));

    }
}
