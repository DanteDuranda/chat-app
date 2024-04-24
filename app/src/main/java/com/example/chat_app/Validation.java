package com.example.chat_app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    private static final Pattern patternEmail = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    private static final Pattern patternPassword = Pattern.compile("^(?=.*[A-Z])(?=.*\\d).*$");

    public static boolean isValidEmail(String email) {
        Matcher matcher = patternEmail.matcher(email);
        return matcher.matches();
    }

    public static String isValidPassword(String password) {
        StringBuilder errorBuilder = new StringBuilder("Hiba!\n");

        if (password.isEmpty()){
            errorBuilder.append("Nem adtál meg jelszót!");
            return errorBuilder.toString();
        }
        if (password.length() < 8){
            errorBuilder.append("A megadott jelszónak legal'bb 8 karakter hosszúnak kell lennie.");
        }
        Matcher matcher = patternPassword.matcher(password);
        if (!matcher.matches()){
            errorBuilder.append("A jelszónak tartalmazia kell legalább egy nagybetut és legalább egy számot.");
        }

        return errorBuilder.toString();
    }
}
