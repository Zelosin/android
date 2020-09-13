package com.zelosin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class ActionActivity extends AppCompatActivity {
    Uri gmmIntentUri = Uri.parse("geo:55.754283,37.62002");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        findViewById(R.id.dOpenMapButton).setOnClickListener((v) -> {
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            startActivity(mapIntent);
        });
    }
}