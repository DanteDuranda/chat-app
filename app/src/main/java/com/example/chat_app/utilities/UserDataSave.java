package com.example.chat_app.utilities;

import android.widget.TextView;

import java.lang.ref.WeakReference;

public class UserDataSave extends android.os.AsyncTask<Void, Void, String> {

    private WeakReference<String> surname;
    private WeakReference<String> forename;
    private WeakReference<String> dateOfBirth;

    public UserDataSave(WeakReference<String> surname, WeakReference<String> forename, WeakReference<String> dateOfBirth) {
        this.surname = surname;
        this.forename = forename;
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
