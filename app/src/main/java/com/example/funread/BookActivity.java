
package com.example.funread;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.funread.view.BookPageBezierHelper;
import com.example.funread.view.BookPageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
/**
 * description ：
 * author : lytMoon
 * email : yytds@foxmail.com
 * date : 2023/2/1
 * version: 1.0
 */

public class BookActivity extends Activity {
    private BookPageBezierHelper mHelper;
    private BookPageView mBookPageView;

    private static final String FILE_PATH = "TEST";
    private String mFilePath;
    private TextView mTextView;
    private int mWidth;
    private int mHeight;
    private Bitmap mCurrentPageBitmap;
    private Bitmap mNextPageBitmap;
    private int mCurrentLength;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        // init View
        mBookPageView = findViewById(R.id.book_page_view);
        mTextView = findViewById(R.id.progress_text_view);

        //开启全屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//开启全屏


        Log.d("mFilePath", "(BookActivity.java:23)-->> " + mFilePath);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mWidth = displayMetrics.widthPixels;
        mHeight = displayMetrics.heightPixels;
        final BookPageBezierHelper helper = new BookPageBezierHelper(mWidth,mHeight);
        mBookPageView.setBookPageBezierHelper(helper);
        mCurrentPageBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mBookPageView.setBitmaps(mCurrentPageBitmap, mNextPageBitmap);

       //设置背景
        helper.setBackground(this,R.drawable.background_read);

        //设置进度
        helper.setOnProgressChangedListener(new BookPageBezierHelper.OnProgressChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void setProgress(int currentLength, int totalLength) {
                mCurrentLength = currentLength;
                float progress = mCurrentLength * 100 / totalLength;
                mTextView.setText(String.format("%s%%", progress));
            }
        });

        if (getIntent() != null) {
            mFilePath = getIntent().getStringExtra(FILE_PATH);
            if (!TextUtils.isEmpty(mFilePath)){
                try {
                    helper.openBook(mFilePath);
                    helper.draw(new Canvas(mCurrentPageBitmap));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
//封装跳转操作
    public static void start(Context context, String filePath) throws IOException {
        Intent intent = new Intent(context, BookActivity.class);
        intent.putExtra(FILE_PATH, filePath);
        context.startActivity(intent);
    }






}

