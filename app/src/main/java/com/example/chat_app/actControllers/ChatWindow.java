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
import com.example.chat_app.model.Message;
import com.example.chat_app.utilities.MessagesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatWindow extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessagesAdapter messagesAdapter;
    private EditText messageField;
    private String selectedUsersEmail;
    private FirebaseFirestore db;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_window);
        db = FirebaseFirestore.getInstance();
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize messageField and set focus change listener
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

        List<Message> messageList = new ArrayList<>();


        // Initialize and set adapter for RecyclerView
        messagesAdapter = new MessagesAdapter(messageList);
        recyclerView.setAdapter(messagesAdapter);


        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("EMAIL")) {
            selectedUsersEmail = intent.getStringExtra("EMAIL");
        }

        retrieveMessages();

        // Initialize and set adapter for RecyclerView
        messagesAdapter = new MessagesAdapter(messageList);
        recyclerView.setAdapter(messagesAdapter);
    }

    private void retrieveMessages() {
        db.collection("messages")
                .whereEqualTo("sender", currentUser.getEmail())
                .whereEqualTo("recipient", selectedUsersEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Message> messageList = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Message message = documentSnapshot.toObject(Message.class);
                        messageList.add(message);
                    }
                    // Update RecyclerView adapter with retrieved messages
                    messagesAdapter = new MessagesAdapter(messageList);
                    recyclerView.setAdapter(messagesAdapter);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(ChatWindow.this, "Failed to retrieve messages", Toast.LENGTH_SHORT).show();
                });
    }

    public void sendMessage(){
        Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timestampString = dateFormat.format(currentTime);
        Message newMessage = new Message(messageField.getText().toString(), currentUser.getEmail(), selectedUsersEmail,  timestampString);

        db.collection("messages").add(newMessage)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ChatWindow.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                    messageField.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ChatWindow.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                });
    }
}
