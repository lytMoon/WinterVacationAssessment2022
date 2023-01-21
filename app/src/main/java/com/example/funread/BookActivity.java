
package com.example.funread;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class BookActivity extends AppCompatActivity {

    private static final String FILE_PATH = "TEST";
    private String mFilePath;
    private TextView mTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        if (getIntent() != null) {
            mFilePath = getIntent().getStringExtra(FILE_PATH);
        }
        Log.d("mFilePath", "(BookActivity.java:23)-->> " + mFilePath);

    }

    public static void start(Context context, String filePath) throws IOException {
        Intent intent = new Intent(context, BookActivity.class);
        intent.putExtra(FILE_PATH, filePath);
        context.startActivity(intent);
    }






}

