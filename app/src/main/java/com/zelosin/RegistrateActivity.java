package com.zelosin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RegistrateActivity extends AppCompatActivity {

    private final static String PREF_SETTINGS = "SETTINGS_PREF";
    private final static String PREF_LOGIN = "LOGIN_PREF";
    private final static String PREF_PASSWORD = "PASSWORD_PREF";
    private final static String PREF_NEW_SER = "NEW_USER_PRED";

    private final static String INT_SUCCESS_SIGNUP = "SUCCESS_SIGNUP_INTENT";

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrate_in);

        EditText dLoginPlainText = findViewById(R.id.dLoginPlainTextRegistrate);
        EditText dPasswordPlainText = findViewById(R.id.dPasswordPlainTextRegistrate);

        SharedPreferences mLoginSettings = getSharedPreferences(PREF_SETTINGS, MODE_PRIVATE);

        findViewById(R.id.dRegistrateButton).setOnClickListener((v) -> {
            String mPotentialLogin = dLoginPlainText.getText().toString();
            String mPotentialPassword = dPasswordPlainText.getText().toString();

            if(!mPotentialLogin.isEmpty() && !mPotentialPassword.isEmpty()) {
                SharedPreferences.Editor mSettingsEditor = mLoginSettings.edit();
                mSettingsEditor.putString(PREF_LOGIN, mPotentialLogin);
                mSettingsEditor.putString(PREF_PASSWORD, mPotentialPassword);
                mSettingsEditor.putBoolean(PREF_NEW_SER, true);
                mSettingsEditor.apply();

                Intent mStartLoginActivityIntent = new Intent(this, LoginInActivity.class);
                mStartLoginActivityIntent.putExtra(INT_SUCCESS_SIGNUP, true);
                startActivity(mStartLoginActivityIntent);
            }
        });
    }
}