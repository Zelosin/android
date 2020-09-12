package com.zelosin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LoginInActivity extends AppCompatActivity {

    private final static String PREF_SETTINGS = "SETTINGS_PREF";
    private final static String PREF_LOGIN = "LOGIN_PREF";
    private final static String PREF_PASSWORD = "PASSWORD_PREF";

    private final static String INT_SUCCESS_SIGNUP = "SUCCESS_SIGNUP_INTENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);

        EditText dLoginPlainText = findViewById(R.id.dLoginPlainText);
        EditText dPasswordPlainText = findViewById(R.id.dPasswordPlainText);

        SharedPreferences mLoginSettings = getSharedPreferences(PREF_SETTINGS, MODE_PRIVATE);

        findViewById(R.id.dSignUpButton).setOnClickListener((v) -> {
            startActivity(new Intent(this, RegistrateActivity.class));
        });

        findViewById(R.id.dSignInButton).setOnClickListener((v) -> {
            if (dLoginPlainText.getText().toString()
                    .equals(mLoginSettings.getString(PREF_LOGIN, "")) &&
                dPasswordPlainText.getText().toString()
                    .equals(mLoginSettings.getString(PREF_PASSWORD, ""))) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                dPasswordPlainText.getText().clear();
                Toast.makeText(this, getString(R.string.VIEW_REJECT_LOGIN_TOAST), Toast.LENGTH_SHORT).show();
            }
        });

        Intent mIntentFromRegistration = getIntent();
        if (mIntentFromRegistration != null) {
            if (mIntentFromRegistration.getBooleanExtra(INT_SUCCESS_SIGNUP, false))
                Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.VIEW_REGISTRATION_SUCCESS_TOAST),
                        Toast.LENGTH_SHORT)
                        .show();
        }


    }
}