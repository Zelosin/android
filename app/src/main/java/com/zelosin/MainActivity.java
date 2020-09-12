package com.zelosin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    List<String> mCountriesList = new ArrayList<>(Arrays.asList("Бразилия", "Аргентина", "Колумбия", "Чили", "Уругвай"));

    private final static String BND_COUNTRIES = "COUNTRIES_BUNDLE";
    private final static String BND_LOCALITY = "LOCALITY_BUNDLE";
    private final static String BND_DELETE_GOAL = "DELETE_BUNDLE";

    private static boolean IS_NEXT_TAP_FOR_DELETE = false;
    private static boolean IS_ENG_LOCALITY = false;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(BND_COUNTRIES, (ArrayList<String>) mCountriesList);
        outState.putBoolean(BND_LOCALITY, IS_ENG_LOCALITY);
        outState.putBoolean(BND_DELETE_GOAL, IS_NEXT_TAP_FOR_DELETE);
    }

    private void showDeleteToast(){
        Toast.makeText(
                getApplicationContext(),
                getString(R.string.VIEW_DELETE_TOAST), Toast.LENGTH_SHORT
        )
                .show();
    }

    private void applyLocality(String newLocality){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(newLocality.toLowerCase()));
        } else {
            config.locale = new Locale(newLocality.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }

    private String switchApplicationLocality(){
        return IS_ENG_LOCALITY ? "en" : "ru";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(BND_COUNTRIES)) {
                mCountriesList.clear();
                mCountriesList.addAll(Objects.requireNonNull(savedInstanceState.getStringArrayList(BND_COUNTRIES)));
            }
            if (savedInstanceState.containsKey(BND_LOCALITY)) {
                IS_ENG_LOCALITY = savedInstanceState.getBoolean(BND_LOCALITY);
            }
            if (savedInstanceState.containsKey(BND_DELETE_GOAL)) {
                IS_NEXT_TAP_FOR_DELETE = savedInstanceState.getBoolean(BND_DELETE_GOAL);
                if(IS_NEXT_TAP_FOR_DELETE)
                    showDeleteToast();
            }
        }

        applyLocality(switchApplicationLocality());

        ListView mCountriesListView = (ListView) findViewById(R.id.countriesList);

        ArrayAdapter<String> mListViewAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, mCountriesList);

        mCountriesListView.setOnItemClickListener((parent, v, position, id) -> {
            if (IS_NEXT_TAP_FOR_DELETE) {
                mCountriesList.remove(position);
                IS_NEXT_TAP_FOR_DELETE = !IS_NEXT_TAP_FOR_DELETE;
            }
            mListViewAdapter.notifyDataSetChanged();
        });

        mCountriesListView.setAdapter(mListViewAdapter);

        findViewById(R.id.dAddButton).setOnClickListener((v) -> {
            mCountriesList.add(new Date().toString());
            mListViewAdapter.notifyDataSetChanged();
        });

        findViewById(R.id.dDeleteButton).setOnClickListener((v) -> {
            showDeleteToast();
            IS_NEXT_TAP_FOR_DELETE = true;
        });

        findViewById(R.id.dChangeLocalityButton).setOnClickListener((v) -> {
            IS_ENG_LOCALITY = !IS_ENG_LOCALITY;
            recreate();
        });
    }
}