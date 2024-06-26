package com.example.chat_app.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static class Validation {
        private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[0-9]).{8,}$");

        public static boolean isValidEmail(String email) {
            Matcher matcher = EMAIL_PATTERN.matcher(email);
            return matcher.matches();
        }

        public static boolean isValidPassword(String password) {
            Matcher matcher = PASSWORD_PATTERN.matcher(password);
            return matcher.matches();
        }

    }
}
