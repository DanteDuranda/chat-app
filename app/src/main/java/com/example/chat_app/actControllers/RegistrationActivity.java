package com.example.chat_app.actControllers;

import static com.example.chat_app.utilities.Validator.Validation.isValidEmail;
import static com.example.chat_app.utilities.Validator.Validation.isValidPassword;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chat_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    private static final int SECRET_KEY = 126;
    private FirebaseAuth mAuth;
    private EditText emailField;
    private TextView registrationErrorLabel;
    private EditText password;
    private EditText passwordAgain;
    private Button registrationButton;
    private SharedPreferences preferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);
        System.err.println("Secret key = "+secret_key);
        if (secret_key != 125) {
            Log.d(LOG_TAG, "Violation!");
            finish();
        }else {
            Log.d(LOG_TAG, "Success!");
        }

        /*preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        if(preferences != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        }*/

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        emailField = findViewById(R.id.registrationEmailField);
        password = findViewById(R.id.registrationPassowrdField);
        passwordAgain = findViewById(R.id.registrationPasswordAgainField);
        registrationErrorLabel = findViewById(R.id.registrationErrorLabel);
        mAuth = FirebaseAuth.getInstance();
        registrationButton = findViewById(R.id.regrButton);

        passwordAgain.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registrationButton.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    public void register(View view) {
        String email = this.emailField.getText().toString();
        String password = this.password.getText().toString();
        String passwordAgain = this.passwordAgain.getText().toString();
        if (!areInputsValid(email, password, passwordAgain)) return;
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(LOG_TAG, "User created successfully");
                    succesfulRegistration();
                }else{
                    Log.d(LOG_TAG, "Error during the registration process!");
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
        Intent intent = new Intent(this, RegUserData.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void backToLogin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}