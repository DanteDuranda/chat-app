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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chat_app.R;

public class RegUserData extends AppCompatActivity {
    TextView surnameField;
    TextView forenameField;
    Button dateOfBirthButton;
    private String selectedDate;
    TextView userDataErrorLabel;
    private boolean dateIsValid = false;
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
                        RegUserData.this,
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
        Toast.makeText(RegUserData.this, "Registration successful!\nNow you can login.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}