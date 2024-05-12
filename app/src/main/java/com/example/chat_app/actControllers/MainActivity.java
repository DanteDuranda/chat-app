package com.example.chat_app.actControllers;

import static com.example.chat_app.utilities.Validator.Validation.isValidEmail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chat_app.R;
import com.example.chat_app.komoj;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();  //log tag
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 125;
    private EditText email;
    private EditText password;
    private TextView errorLabel;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private SharedPreferences preferences;

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

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        email = findViewById(R.id.registrationEmailField);
        password = findViewById(R.id.registrationPassowrdField);
        errorLabel = findViewById(R.id.loginErrorLabel);
        loginButton = findViewById(R.id.loginButton);
        mAuth = FirebaseAuth.getInstance();

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginButton.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    public void login(View view) {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        if (!areInputsValid(email, password)) return;

        Intent intent = new Intent(this, komoj.class);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Authentication was successful.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean areInputsValid(String email, String password) {
        StringBuilder validationErrorBuilder = new StringBuilder();
        errorLabel.setText("");
        if (email.isEmpty()){
            validationErrorBuilder.append(getString(R.string.empty_email_field));
        }else if(!isValidEmail(email)){
            validationErrorBuilder.append(getString(R.string.wrong_email_format));
        }
        if (password.isEmpty()){
            validationErrorBuilder.append(getString(R.string.empty_password_field));
        }
        errorLabel.setText(validationErrorBuilder.toString());
        return validationErrorBuilder.toString().isEmpty();
    }

    public void toRegistration(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }
}