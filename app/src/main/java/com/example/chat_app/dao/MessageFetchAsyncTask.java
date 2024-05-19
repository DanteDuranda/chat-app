package com.example.chat_app.dao;

import android.os.AsyncTask;

import com.example.chat_app.model.Message;
import com.example.chat_app.model.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MessageFetchAsyncTask extends AsyncTask<Void, Void, List<Message>> {

    private String currentUserEmail;
    private String selectedUserEmail;
    private OnFetchMessagesListener listener;
    private Exception exception;

    public MessageFetchAsyncTask(String currentUserEmail, String selectedUserEmail, OnFetchMessagesListener listener) {
        this.currentUserEmail = currentUserEmail;
        this.selectedUserEmail = selectedUserEmail;
        this.listener = listener;
    }

    @Override
    protected List<Message> doInBackground(Void... voids) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference messagesCollection = db.collection("messages");

            Task<QuerySnapshot> task1 = messagesCollection
                    .whereEqualTo("sender", currentUserEmail)
                    .whereEqualTo("recipient", selectedUserEmail)
                    .get();

            Task<QuerySnapshot> task2 = messagesCollection
                    .whereEqualTo("sender", selectedUserEmail)
                    .whereEqualTo("recipient", currentUserEmail)
                    .get();

            Tasks.await(task1);
            Tasks.await(task2);

            List<Message> messageList = new ArrayList<>();

            if (task1.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task1.getResult()) {
                    Message message = documentSnapshot.toObject(Message.class);
                    messageList.add(message);
                }
            } else {
                throw new Exception("Failed to fetch messages: " + task1.getException());
            }

            if (task2.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task2.getResult()) {
                    Message message = documentSnapshot.toObject(Message.class);
                    messageList.add(message);
                }
            } else {
                throw new Exception("Failed to fetch messages: " + task2.getException());
            }

            messageList.sort(Comparator.comparing(Message::getTime));

            return messageList;

        } catch (Exception e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Message> messageList) {
        if (messageList != null) {
            listener.onFetchSuccess(messageList);
        } else {
            listener.onFetchFailure(exception);
        }
    }

    public interface OnFetchMessagesListener {
        void onFetchSuccess(List<Message> messageList);
        void onFetchFailure(Exception e);
    }

    public void startMessageRetrieval() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new RetrieveMessagesTask(), 0, 3000); // 3000 milliseconds = 3 seconds
    }

    private class RetrieveMessagesTask extends TimerTask {
        @Override
        public void run() {
            doInBackground();
        }
    }
}
