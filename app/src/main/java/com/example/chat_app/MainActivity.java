package com.example.chat_app;

import static com.example.chat_app.Validation.isValidEmail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();  //log tag

    private EditText email;
    private EditText password;
    private TextView errorLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = findViewById(R.id.registrationEmailField);
        password = findViewById(R.id.registrationPasswordField);
        errorLabel = findViewById(R.id.loginErrorLabel);
    }

    public void login(View view) {
        errorLabel.setText("");

        if (email.getText().toString().isEmpty()){
            errorLabel.setText(getString(R.string.login_error_empty_email));
        }

        if (!email.getText().toString().isEmpty() && !email.getText().toString().isEmpty()){
            Log.i(LOG_TAG, "Bejelentkezett: "+email.getText().toString() + "   " + password.getText().toString()); //TODO DEBUG
        }else{
            Log.i(LOG_TAG,  "Hiányos kitoltes!!!!!"); //TODO DEBUG
        }

        if (isValidEmail(email.getText().toString())){
            Log.i(LOG_TAG, "Valid Email: " + email.getText().toString());
        }else{
            errorLabel.setText(getString(R.string.login_error_wrong_email)); //TODO ekezet
        }
    }

    public void toRegistration(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}