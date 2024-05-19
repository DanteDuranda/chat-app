package com.example.chat_app.dao;

import android.os.AsyncTask;
import android.widget.TextView;

import com.example.chat_app.R;
import com.example.chat_app.databinding.NavHeaderBinding;
import com.example.chat_app.model.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserFetcherAsyncTask extends AsyncTask<Void, Void, List<User>> {

    private CollectionReference usersCollection;

    private NavHeaderBinding navBinding;
    private OnFetchUsersListener listener;
    private Exception exception;

    public UserFetcherAsyncTask(CollectionReference usersCollection, OnFetchUsersListener listener) {
        this.usersCollection = usersCollection;
        this.listener = listener;

    }

    @Override
    protected List<User> doInBackground(Void... voids) {
        try {
            Task<QuerySnapshot> task = usersCollection.orderBy("name", Query.Direction.ASCENDING).get();
            Tasks.await(task);

            if (task.isSuccessful()) {
                List<User> userList = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    User user = documentSnapshot.toObject(User.class);
                    if (!user.getEmail().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail())){
                        userList.add(user);
                    }
                }
                return userList;
            } else {
                throw new Exception("Failed to fetch users: " + task.getException());
            }
        } catch (Exception e) {
            exception = e;
            return null;
        }
    }



    @Override
    protected void onPostExecute(List<User> userList) {
        if (userList != null) {
            listener.onFetchSuccess(userList);
        } else {
            listener.onFetchFailure(exception);
        }
    }

    public interface OnFetchUsersListener {
        void onFetchSuccess(List<User> userList);
        void onFetchFailure(Exception e);
    }
}
