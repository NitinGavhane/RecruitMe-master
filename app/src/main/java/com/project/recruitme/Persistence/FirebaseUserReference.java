package com.project.recruitme.Persistence;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.recruitme.Models.UserModel;
import com.project.recruitme.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUserReference implements UserReference {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference userCollectionRef = db.collection(Constants.USER_PROFILE_COLLECTION);

    @Override
    public void addUser(UserModel user, OnUserAddedCallback callback) {
        userCollectionRef.document().set(user)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    @Override
    public void getUser(String Uid, OnUserRetrievedCallback callback) {
        userCollectionRef
                .whereEqualTo("UserId", Uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    UserModel user = queryDocumentSnapshots.getDocuments().get(0).toObject(UserModel.class);
                    callback.onSuccess(user);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void getUsers(List<String> UserIds, OnUsersRetrievedCallback callback){
        if(UserIds == null|| UserIds.size() ==0 )
        {
            callback.onSuccess(new ArrayList<>());
            return;
        }

        ArrayList<UserModel> userModels = new ArrayList<>();
        userCollectionRef
                .whereIn("UserId", UserIds)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(queryDocumentSnapshots.isEmpty()){
                        callback.onSuccess(userModels);
                        return;
                    }

                    for(DocumentSnapshot doc: queryDocumentSnapshots){
                        UserModel userModel = doc.toObject(UserModel.class);
                        userModels.add(userModel);
                    }
                    callback.onSuccess(userModels);

                })
                .addOnFailureListener(error-> callback.onFailure(error.getMessage()));
    }

    @Override
    public void deleteUser(String Uid, OnUserDeletedCallback callback) {

        userCollectionRef
                .whereEqualTo("UserId", Uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    queryDocumentSnapshots.getDocuments().get(0).getReference().delete()
                            .addOnSuccessListener(aVoid->callback.onSuccess())
                            .addOnFailureListener(error->callback.onFailure(error.getMessage()));

                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }


}
