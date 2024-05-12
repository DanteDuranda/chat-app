package com.example.chat_app.actControllers;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.chat_app.R;

import java.util.HashMap;
import java.util.Map;

public class RegUserData extends AppCompatActivity {
    TextView surnameField;
    TextView forenameField;
    Button dateOfBirthButton;
    private String selectedDate;
    TextView userDataErrorLabel;
    private boolean dateIsValid = false;

    private CollectionReference usersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reg_user_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("users");

        surnameField = findViewById(R.id.surnameField);
        forenameField = findViewById(R.id.foreNameField);
        dateOfBirthButton = findViewById(R.id.dateOfBirthReg);
        userDataErrorLabel = findViewById(R.id.userDataErrorLabel);

        dateOfBirthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog and show it
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        com.example.chat_app.actControllers.RegUserData.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                                if (selectedYear < 1920){
                                    dateOfBirthButton.setText(R.string.invalid_year);
                                }else {
                                    dateIsValid = true;
                                }
                                selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                                dateOfBirthButton.setText(selectedDate);
                            }
                        },
                        year, month, day);

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });
    }

    public void finishRegistration(View view) {
        if (surnameField.getText().toString().isEmpty() || forenameField.getText().toString().isEmpty()){
            userDataErrorLabel.setText(R.string.missing_names);
            return;
        }
        if (areNameValid() && dateIsValid) {
            String name = forenameField.getText() + " " +surnameField.getText();
            saveUserDataToFirestore(name, selectedDate);
            goToLogin();
        }else{
            userDataErrorLabel.setText(R.string.wrongNameOrDate);
        }
    }

    private boolean areNameValid(){
        return Character.isUpperCase(surnameField.getText().charAt(0)) &&
                Character.isUpperCase(forenameField.getText().charAt(0));
    }

    private void goToLogin(){
        Toast.makeText(com.example.chat_app.actControllers.RegUserData.this, "Registration successful!\nNow you can login.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    // Other activity methods...

    // Method to save user data to Firestore on a background thread
    private void saveUserDataToFirestore(String name, String dateOfBirth) {
        new SaveUserDataAsyncTask(name, dateOfBirth).execute();
    }

    private void showToast(String message) {
        // Display toast message on the main UI thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegUserData.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // AsyncTask to save user data to Firestore
    private class SaveUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        private String name;
        private String dateOfBirth;

        public SaveUserDataAsyncTask(String name, String dateOfBirth) {
            this.name = name;
            this.dateOfBirth = dateOfBirth;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Create a Map representing the user data
            Map<String, Object> userData = new HashMap<>();
            userData.put("name", name);
            userData.put("dateOfBirth", dateOfBirth);

            // Add the user data to Firestore
            usersCollection.add(userData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Document successfully added
                            showToast("User data saved to Firestore");
                            goToLogin();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle errors
                            showToast("Error saving user data: " + e.getMessage());
                        }
                    });
            return null;
        }

        private void showToast(String message) {
            // Display toast message on the main UI thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RegUserData.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}