package com.project.recruitme.Persistence;

import com.google.firebase.firestore.DocumentReference;
import com.project.recruitme.Models.UserModel;

import java.util.List;

public interface UserReference {
        void addUser(UserModel user, OnUserAddedCallback callback);
        void getUser(String Uid, OnUserRetrievedCallback callback);

        void deleteUser(String Uid, OnUserDeletedCallback callback);

        interface OnUserAddedCallback {
            void onSuccess();
            void onFailure(String error);
        }

        interface OnUserDeletedCallback {
            void onSuccess();
            void onFailure(String error);
        }

        interface OnUserRetrievedCallback {
            void onSuccess(UserModel user);
            void onFailure(String error);
        }

    interface OnUsersRetrievedCallback {
        void onSuccess(List<UserModel> user);
        void onFailure(String error);
    }



}
