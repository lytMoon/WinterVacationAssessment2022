package com.example.funread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;
/**
 * description ：
 * author : lytMoon
 * email : yytds@foxmail.com
 * date : 2023/1/19
 * version: 1.0
 */
public class SplashActivity extends Activity {

    public static final int CODE = 1001;
    public static final int TOTAL_TIME = 3000;
    public static final int INTERVAL_TIME = 1000;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iniFind();

        MyHandler handler = new MyHandler(this);
        Message message = Message.obtain();
        message.what = CODE;
        message.arg1= TOTAL_TIME;//message构造好了
        handler.sendMessage(message);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo:设置点击跳过
                //直接用类来调用，方便
                BookListActivity.start(SplashActivity.this);
                SplashActivity.this.finish();
                handler.removeMessages(CODE);

            }
        });

    }

    private void iniFind() {
        mTextView = findViewById(R.id.time_text_view);
    }

    /**
     * TODO:这里设置静态的类，防止内存泄漏。（之前的handler持有activity的引用，延迟触发时会释放不掉）
     * 利用handler制作倒计时
     */
   public static class MyHandler extends Handler{

       public final WeakReference<SplashActivity> mWeakReference;
       public MyHandler(SplashActivity activity){
           mWeakReference = new WeakReference<>(activity);
       }

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            SplashActivity activity = mWeakReference.get();//得到SplashActivity的弱引用
            if (msg.what == CODE){
                if(activity!=null){
                    //设置文字更新UI
                    int time = msg.arg1;
                    activity.mTextView.setText(time/INTERVAL_TIME+"秒，点击跳过");
                    //发送倒计时
                    Message message =Message.obtain();
                    message.what = CODE;
                    message.arg1 = time - INTERVAL_TIME;
                    if (time > 0){
                        sendMessageDelayed(message,INTERVAL_TIME);//设置间隔时间
                    }
                    else {
                        //todo:时间到了，开始跳到下一个页面
                        BookListActivity.start(activity);
                        activity.finish();


                    }

                }
            }


        }
    }
}