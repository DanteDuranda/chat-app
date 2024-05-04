package com.example.chat_app;

import static com.example.chat_app.Validation.isValidEmail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
    private Button loginButton;

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
        loginButton = findViewById(R.id.loginButton);

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
        if (!areInputsValid()) return;

        if (!email.getText().toString().isEmpty() && !email.getText().toString().isEmpty()){
            Log.i(LOG_TAG, "Bejelentkezett: "+email.getText().toString() + "   " + password.getText().toString()); //TODO DEBUG
        }else{
            Log.i(LOG_TAG,  "Hiányos kitoltes!!!!!"); //TODO DEBUG
        }
    }

    private boolean areInputsValid() {
        StringBuilder validationErrorBuilder = new StringBuilder();
        errorLabel.setText("");
        if (email.getText().toString().isEmpty()){
            validationErrorBuilder.append(getString(R.string.login_error_empty_email));
        }else if(!isValidEmail(email.getText().toString())){
            validationErrorBuilder.append(getString(R.string.login_error_wrong_email));
        }
        if (password.getText().toString().isEmpty()){
            validationErrorBuilder.append(getString(R.string.login_error_empty_password));
        }
        errorLabel.setText(validationErrorBuilder.toString());
        return validationErrorBuilder.toString().isEmpty();
    }

    public void toRegistration(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}