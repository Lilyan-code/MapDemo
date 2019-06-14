package com.example.tangwenyan.map.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tangwenyan.map.MainActivity;
import com.example.tangwenyan.map.R;

import org.w3c.dom.Text;


public class Weather1 extends AppCompatActivity {

    private ImageView menu; //下面的返回按钮
    private ImageView back; //顶部导航栏返回主页面按钮
    private TextView city;  //获得当前城市名称
    private String data="桂林市";  //城市名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather1);
        Intent intent4 = getIntent();
        data = intent4.getStringExtra("extra_data");
        menu = (ImageView) findViewById(R.id.menu);
        back = (ImageView) findViewById(R.id.iv_return);
        city = (TextView) findViewById(R.id.city);
        city.setText(data);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Weather1.this, Weather.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Weather1.this, MainActivity.class);
                startActivity(intent1);
            }
        });
    }
}
