package com.example.funread;

import static android.content.ContentValues.TAG;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * description ：
 * author : lytMoon
 * email : yytds@foxmail.com
 * date : 2023/2/1
 * version: 2.0
 */
public class BookListActivity extends AppCompatActivity {

    private List<BookListResult.Book> mBooks = new ArrayList<>();//进行初始化，防止空指针
    private String url = "http://www.imooc.com/api/teacher?type=10";
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        mListView = findViewById(R.id.book_list_view);
        getAsync();


    }


    //跳转时封装一个静态的方法，不用到处写Intent
    public static void start(Context context) {
        Intent intent = new Intent(context, BookListActivity.class);
        context.startActivity(intent);
    }

    public void getAsync() { //get请求方式的异步请求
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {  //异步请求需要调用enqueue方法,通过回调的方式接受结果
            //            异步请求不需要主动创建线程
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    Gson gson = new Gson();
                    BookListResult bookListResult = gson.fromJson(result, BookListResult.class);
                    mBooks = bookListResult.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListView.setAdapter(new BookListAdapter());
                        }//这里应该在UI线程中，不在UI线程中刷新不了UI。之前测试了几次均失败
                    });

                }


            }
        });

    }

    private class BookListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mBooks.size();
        }

        @Override
        public Object getItem(int position) {
            return mBooks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            BookListResult.Book book = mBooks.get(position);
            ViewHoder viewHoder = new ViewHoder();
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.item_book_list, null);
                viewHoder.mNameTextView = view.findViewById(R.id.name_text_view);
                viewHoder.mButton = view.findViewById(R.id.book_button);
                view.setTag(viewHoder);
            } else {
                viewHoder = (ViewHoder) view.getTag();
            }
            viewHoder.mNameTextView.setText(book.getBookname());
            viewHoder.mButton.setText("点击下载");
            ViewHoder finalViewHoder2 = viewHoder;
            final String path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();//设置下载的路径
            Log.d("pathname","(BookListActivity.java:152)-->> "+path);
            final File file = new File(path, book.getBookname()+ ".txt");
            final String filePath = file.getPath();
            viewHoder.mButton.setText(file.exists() ? "点击打开" : "点击下载");
            viewHoder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (file.exists()) {
                        try {
                            BookActivity.start(BookListActivity.this, file.getPath());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder().url(book.getBookfile()).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {


                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.i("myTag", "下载失败");
                        }

                        @Override
                        public void onResponse(@NonNull Call call, Response response) throws IOException {
                            finalViewHoder2.mButton.setText("点击打开");
                            InputStream is = null;
                            FileOutputStream fos = null;//调用输入输出流
                            is = response.body().byteStream();

                            try {
                                fos = new FileOutputStream(file);
                                byte[] bytes = new byte[2048];
                                int len = 0;
                                //获取下载的文件的大小
                                long fileSize = response.body().contentLength();
                                long sum = 0;
                                int porSize = 0;
                                while ((len = is.read(bytes)) != -1) {
                                    fos.write(bytes);
                                    sum += len;
                                 //这里本来用来计算下载的进度   porSize = (int) ((sum * 1.0f / fileSize) * 100);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (is != null) {
                                        is.close();
                                    }
                                    if (fos != null) {
                                        fos.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.i("myTag", "下载状态：" + file.exists() );


                        }
                    });


                }


            });


            return view;
        }
    }


    class ViewHoder {
        public TextView mNameTextView;
        public Button mButton;
    }

}

