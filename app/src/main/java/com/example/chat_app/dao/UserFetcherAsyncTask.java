package com.example.chat_app.dao;

import android.os.AsyncTask;

import com.example.chat_app.model.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class UserFetcherAsyncTask extends AsyncTask<Void, Void, List<User>> {

    private CollectionReference usersCollection;
    private OnFetchUsersListener listener;
    private Exception exception;

    public UserFetcherAsyncTask(CollectionReference usersCollection, OnFetchUsersListener listener) {
        this.usersCollection = usersCollection;
        this.listener = listener;
    }

    @Override
    protected List<User> doInBackground(Void... voids) {
        try {
            // Fetch user data asynchronously
            Task<QuerySnapshot> task = usersCollection.get();
            Tasks.await(task); // Wait for the task to complete

            if (task.isSuccessful()) {
                List<User> userList = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    User user = documentSnapshot.toObject(User.class);
                    userList.add(user);
                }
                return userList;
            } else {
                throw new Exception("Failed to fetch users: " + task.getException());
            }
        } catch (Exception e) {
            // Store the exception for later retrieval
            exception = e;
            return null;
        }
    }


    @Override
    protected void onPostExecute(List<User> userList) {
        if (userList != null) {
            // Notify listener with the fetched user list
            listener.onFetchSuccess(userList);
        } else {
            // Notify listener about failure
            listener.onFetchFailure(exception);
        }
    }

    // Interface for callbacks
    public interface OnFetchUsersListener {
        void onFetchSuccess(List<User> userList);
        void onFetchFailure(Exception e);
    }
}
