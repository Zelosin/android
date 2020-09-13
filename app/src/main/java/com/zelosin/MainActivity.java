package com.zelosin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String[] mResourceMusicList;
    private ArrayList<String> mMusicList;

    private final static String BND_COUNTRIES = "COUNTRIES_BUNDLE";
    private final static String BND_LOCALITY = "LOCALITY_BUNDLE";
    private final static String BND_DELETE_GOAL = "DELETE_BUNDLE";

    private final static String PREF_SETTINGS = "SETTINGS_PREF";
    private final static String PREF_NEW_SER = "NEW_USER_PRED";

    private static boolean IS_NEXT_TAP_FOR_DELETE = false;
    private static boolean IS_ENG_LOCALITY = false;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(BND_COUNTRIES, mMusicList);
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

        mResourceMusicList = getResources().getStringArray(R.array.music);
        mMusicList = new ArrayList<>(Arrays.asList(mResourceMusicList));

        DBHelper mDataBaseHelper = new DBHelper(this);
        SharedPreferences mLoginSettings = getSharedPreferences(PREF_SETTINGS, MODE_PRIVATE);

        if(mLoginSettings.getBoolean(PREF_NEW_SER, true)){
            Toast.makeText(this, getString(R.string.VIEW_LOGIN_TOAST), Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor mSettingsEditor = mLoginSettings.edit();
            mSettingsEditor.putBoolean(PREF_NEW_SER, false);
            mSettingsEditor.apply();
            mDataBaseHelper.getWritableDatabase().execSQL("delete from "+ DBHelper.TABLE_DATA);
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(BND_COUNTRIES)) {
                mMusicList.clear();
                mMusicList.addAll(Objects.requireNonNull(savedInstanceState.getStringArrayList(BND_COUNTRIES)));
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

        ListView mMusicListView = (ListView) findViewById(R.id.countriesList);

        ArrayAdapter<String> mListViewAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, mMusicList);

        mMusicListView.setOnItemClickListener((parent, v, position, id) -> {
            if (IS_NEXT_TAP_FOR_DELETE) {
                mMusicList.remove(position);
                SQLiteDatabase mWritableDatabase = mDataBaseHelper.getWritableDatabase();
                mWritableDatabase.execSQL("delete from " + DBHelper.TABLE_DATA + " where " + DBHelper.KEY_ID + "=" + position);
                IS_NEXT_TAP_FOR_DELETE = !IS_NEXT_TAP_FOR_DELETE;
            }
            mListViewAdapter.notifyDataSetChanged();
        });

        mMusicListView.setAdapter(mListViewAdapter);

        SQLiteDatabase mReadableDatabase = mDataBaseHelper.getReadableDatabase();

        Cursor mDataBaseCursor = mReadableDatabase.query(DBHelper.TABLE_DATA, null, null, null, null, null, null);

        if(mDataBaseCursor.moveToFirst()){
            int mDataColumnIndex = mDataBaseCursor.getColumnIndex(DBHelper.KEY_DATA);
            mMusicList.clear();
            mListViewAdapter.notifyDataSetChanged();
            do{
                mMusicList.add(mDataBaseCursor.getString(mDataColumnIndex));
            }while (mDataBaseCursor.moveToNext());
        }

        findViewById(R.id.dAddButton).setOnClickListener((v) -> {
            Date mCurrnetDate = new Date();
            String mNewLine = mResourceMusicList[new Random().nextInt(mResourceMusicList.length)] +
                    " " + mCurrnetDate.getMinutes()+
                    ":" + mCurrnetDate.getSeconds();

            SQLiteDatabase mWritableDatabase = mDataBaseHelper.getWritableDatabase();
            ContentValues mDataBaseValues = new ContentValues();

            mDataBaseValues.put(DBHelper.KEY_DATA, mNewLine);
            mWritableDatabase.insert(DBHelper.TABLE_DATA, null, mDataBaseValues);

            mMusicList.add(mNewLine);
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