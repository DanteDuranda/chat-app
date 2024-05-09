package com.example.chat_app;

import static com.example.chat_app.Validation.isValidEmail;
import static com.example.chat_app.Validation.isValidPassword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;//TODO RAJONNI EZEK MIK
    private FirebaseAuth mAuth;
    private EditText emailField;
    private TextView registrationErrorLabel;
    private EditText password;
    private EditText passwordAgain;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);
        if (secret_key != 99) {
            finish();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        emailField = findViewById(R.id.registrationEmailField);
        password = findViewById(R.id.registrationPasswordField);
        passwordAgain = findViewById(R.id.registrationPasswordAgainField);
        registrationErrorLabel = findViewById(R.id.registrationErrorLabel);
        mAuth = FirebaseAuth.getInstance();
    }

    public void register(Bundle saveInstanceState) {
        String email = this.emailField.getText().toString();
        String password = this.password.getText().toString();
        String passwordAgain = this.passwordAgain.getText().toString();
        if (!areInputsValid(email, password, passwordAgain)) return;
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    succesfulRegistration();
                }else{
                    //TODO
                }
            }
        });
    }

    private boolean areInputsValid(String email, String password, String passwordAgain) {
        StringBuilder validationErrorBuilder = new StringBuilder();
        if (email.isEmpty()){
            validationErrorBuilder.append(getString(R.string.empty_email_field));
        } else if (!isValidEmail(email)) {
            validationErrorBuilder.append(getString(R.string.wrong_email_format));
        }
        if (password.isEmpty()) {
            validationErrorBuilder.append(getString(R.string.empty_password_field));
        }else if(passwordAgain.isEmpty()) {
            validationErrorBuilder.append(getString(R.string.password_again_missing));
        }else if(!password.equalsIgnoreCase(passwordAgain)) {
            validationErrorBuilder.append(getString(R.string.password_match_failure));
        }else if (!isValidPassword(password)) {
            validationErrorBuilder.append(getString(R.string.wrong_password));
        }
        registrationErrorLabel.setText(validationErrorBuilder.toString());
        return validationErrorBuilder.toString().isEmpty();
    }

    private void succesfulRegistration() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void backToLogin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}