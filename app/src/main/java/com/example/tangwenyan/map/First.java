package com.example.tangwenyan.map;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class First extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGHT = 1000;  //延迟2秒
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(First.this, MainActivity.class);
                First.this.startActivity(intent);
                First.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}
