package com.example.tangwenyan.map.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tangwenyan.map.MainActivity;
import com.example.tangwenyan.map.R;
import com.example.tangwenyan.map.Activity.Weather1;

public class Weather extends AppCompatActivity {

    private ImageView back; //菜单按钮
    private ImageView back1;  //顶部导航栏返回按钮
    private Button beijing;
    private Button shanghai;
    private Button guangzhou;
    private Button shenzhen;
    private Button tianjin;
    private Button chengdu;
    private Button chongqing;
    private Button qingdao;
    private Button nanjing;
    private Button dalian;
    private Button wuhan;
    private Button zhenzhou;
    private Button current;
    private TextView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        search = (EditText) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = search.getText().toString();
                Intent intent14 = new Intent(Weather.this, Weather1.class);
                intent14.putExtra("extra_data", data);
                //startActivity(intent14);
            }
        });
        back = (ImageView) findViewById(R.id.iv_return);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Weather.this, MainActivity.class);
                startActivity(intent1);
            }
        });
        back1 = (ImageView) findViewById(R.id.iv_return1);
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Weather.this, Weather1.class);
                startActivity(intent);
            }
        });
        current = (Button) findViewById(R.id.current_city);
        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent0 = new Intent(Weather.this, Weather1.class);
                intent0.putExtra("extra_data", "桂林市");
                startActivity(intent0);
            }
        });
        beijing = (Button) findViewById(R.id.beijing);
        beijing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Weather.this, Weather1.class);
                intent2.putExtra("extra_data", "北京市");
                startActivity(intent2);
            }
        });
        shanghai = (Button) findViewById(R.id.shanghai);
        shanghai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(Weather.this, Weather1.class);
                intent3.putExtra("extra_data", "上海市");
                startActivity(intent3);
            }
        });
        guangzhou = (Button) findViewById(R.id.guangzhou);
        guangzhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(Weather.this, Weather1.class);
                intent4.putExtra("extra_data", "广州市");
                startActivity(intent4);
            }
        });
        shenzhen = (Button) findViewById(R.id.shenzhen);
        shenzhen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(Weather.this, Weather1.class);
                intent5.putExtra("extra_data", "深圳市");
                startActivity(intent5);
            }
        });
        tianjin = (Button) findViewById(R.id.tianjin);
        tianjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(Weather.this, Weather1.class);
                intent6.putExtra("extra_data", "天津市");
                startActivity(intent6);
            }
        });
        chengdu = (Button) findViewById(R.id.chengdu);
        chengdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent7 = new Intent(Weather.this, Weather1.class);
                intent7.putExtra("extra_data", "成都市");
                startActivity(intent7);
            }
        });
        chongqing = (Button) findViewById(R.id.chongqing);
        chongqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent8 = new Intent(Weather.this, Weather1.class);
                intent8.putExtra("extra_data", "重庆市");
                startActivity(intent8);
            }
        });
        qingdao = (Button) findViewById(R.id.qingdao);
        qingdao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent9 = new Intent(Weather.this, Weather1.class);
                intent9.putExtra("extra_data", "青岛市");
                startActivity(intent9);
            }
        });
        nanjing = (Button) findViewById(R.id.nanjing);
        nanjing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent10 = new Intent(Weather.this, Weather1.class);
                intent10.putExtra("extra_data", "南京市");
                startActivity(intent10);
            }
        });
        dalian = (Button) findViewById(R.id.dalian);
        dalian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent11 = new Intent(Weather.this, Weather1.class);
                intent11.putExtra("extra_data", "大连市");
                startActivity(intent11);
            }
        });
        wuhan = (Button) findViewById(R.id.wuhan);
        wuhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent12 = new Intent(Weather.this, Weather1.class);
                intent12.putExtra("extra_data", "武汉市");
                startActivity(intent12);
            }
        });
        zhenzhou = (Button) findViewById(R.id.zhenzhou);
        zhenzhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent13 = new Intent(Weather.this, Weather1.class);
                intent13.putExtra("extra_data", "郑州市");
                startActivity(intent13);
            }
        });
    }
}
