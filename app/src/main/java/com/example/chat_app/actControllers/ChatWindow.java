package com.example.chat_app.actControllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.R;
import com.example.chat_app.dao.MessageFetchAsyncTask;
import com.example.chat_app.dao.UserFetcherAsyncTask;
import com.example.chat_app.model.Message;
import com.example.chat_app.model.User;
import com.example.chat_app.utilities.MessagesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ChatWindow extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessagesAdapter messagesAdapter;
    private EditText messageField;
    private String selectedUsersEmail;
    private FirebaseFirestore db;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    List<Message> messageList = new ArrayList<>();

    private ListenerRegistration messageListenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_window);
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageField = findViewById(R.id.messageField);
        messageField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    sendMessage();
                    return true;
                }
                return false;
            }
        });

        messagesAdapter = new MessagesAdapter(messageList);
        recyclerView.setAdapter(messagesAdapter);


        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("EMAIL")) {
            selectedUsersEmail = intent.getStringExtra("EMAIL");
        }

        retrieveMessages();
        startMessageRetrieval();
    }

    private void retrieveMessages() {
        new MessageFetchAsyncTask(currentUser.getEmail(), selectedUsersEmail, new MessageFetchAsyncTask.OnFetchMessagesListener() {
            @Override
            public void onFetchSuccess(List<Message> messageList) {
                messagesAdapter = new MessagesAdapter(messageList);
                recyclerView.setAdapter(messagesAdapter);
                int lastItemPosition = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount() - 1;
                recyclerView.scrollToPosition(lastItemPosition);
            }

            @Override
            public void onFetchFailure(Exception e) {
                Toast.makeText(ChatWindow.this, "Failed to retrieve messages", Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    public void startMessageRetrieval() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new RetrieveMessagesTask(), 0, 3000); // 3 seconds
    }

    private class RetrieveMessagesTask extends TimerTask {
        @Override
        public void run() {
            if (checkRViewPos()){
                retrieveMessages();
            }
        }
    }

    public void sendMessage(){
        Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", new Locale("hu", "HU"));
        String timestampString = dateFormat.format(currentTime);
        Message newMessage = new Message(messageField.getText().toString(), currentUser.getEmail(), selectedUsersEmail,  timestampString);

        db.collection("messages").add(newMessage)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ChatWindow.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                    messageField.setText("");
                    retrieveMessages();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ChatWindow.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveMessages(); // after resume, the messages are queried again
    }

    private boolean checkRViewPos(){
        int verticalScrollOffset = recyclerView.computeVerticalScrollOffset();
        int totalHeight = recyclerView.computeVerticalScrollRange();

        int recyclerViewHeight = recyclerView.getHeight();

        if (verticalScrollOffset + recyclerViewHeight >= totalHeight) {
            return true; //scrolled to the bottom
        } else {
            return false;
        }
    }
}
