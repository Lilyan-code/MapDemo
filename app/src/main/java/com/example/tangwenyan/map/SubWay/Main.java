package com.example.tangwenyan.map.SubWay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.tangwenyan.map.MainActivity;
import com.example.tangwenyan.map.R;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity implements View.OnClickListener{

    private ImageView back; //返回按钮

    private List<City> cityList = new ArrayList<City>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_main);
        back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(this);
        initFruits(); // 初始化水果数据
        SubwayAdapter adapter = new SubwayAdapter(Main.this, R.layout.city_item, cityList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                City city = cityList.get(position);
                String name = city.getName();
                if(name == "北京") {
                    Intent intent = new Intent(Main.this, BeiJing.class);
                    //intent.putExtra("name", name);
                    startActivity(intent);
                } else if(name == "上海") {
                    Intent intent = new Intent(Main.this, ShangHai.class);
                    startActivity(intent);
                } else if (name == "广州") {
                    Intent intent = new Intent(Main.this, GuangZhou.class);
                    startActivity(intent);
                } else if (name == "深圳") {
                    Intent intent = new Intent(Main.this, ShengZhen.class);
                    startActivity(intent);
                } else if (name == "武汉") {
                    Intent intent = new Intent(Main.this, WuHan.class);
                    startActivity(intent);
                } else if (name == "天津") {
                    Intent intent = new Intent(Main.this, TianJing.class);
                    startActivity(intent);
                } else if (name == "南京") {
                    Intent intent = new Intent(Main.this, NanJing.class);
                    startActivity(intent);
                } else if (name == "香港") {
                    Intent intent = new Intent(Main.this, HongKong.class);
                    startActivity(intent);
                } else if (name == "重庆") {
                    Intent intent = new Intent(Main.this, ChongQing.class);
                    startActivity(intent);
                } else if (name == "杭州") {
                    Intent intent = new Intent(Main.this, HangZhou.class);
                    startActivity(intent);
                } else if (name == "沈阳") {
                    Intent intent = new Intent(Main.this, ShenYang.class);
                    startActivity(intent);
                } else if (name == "大连") {
                    Intent intent = new Intent(Main.this, DaLian.class);
                    startActivity(intent);
                } else if (name == "成都") {
                    Intent intent = new Intent(Main.this, ChengDu.class);
                    startActivity(intent);
                } else if (name == "长春") {
                    Intent intent = new Intent(Main.this, ChangChun.class);
                    startActivity(intent);
                } else if (name == "苏州") {
                    Intent intent = new Intent(Main.this, SuZhou.class);
                    startActivity(intent);
                } else if (name == "佛山") {
                    Intent intent = new Intent(Main.this, FoShan.class);
                    startActivity(intent);
                } else if (name == "昆明") {
                    Intent intent = new Intent(Main.this, KunMing.class);
                    startActivity(intent);
                } else if (name == "西安") {
                    Intent intent = new Intent(Main.this, XiAn.class);
                    startActivity(intent);
                } else if (name == "郑州") {
                    Intent intent = new Intent(Main.this, ZhengZhou.class);
                    startActivity(intent);
                } else if (name == "长沙") {
                    Intent intent = new Intent(Main.this, ChangSha.class);
                    startActivity(intent);
                } else if (name == "宁波") {
                    Intent intent = new Intent(Main.this, NingBo.class);
                    startActivity(intent);
                } else if (name == "无锡") {
                    Intent intent = new Intent(Main.this, WuXi.class);
                    startActivity(intent);
                } else if (name == "青岛") {
                    Intent intent = new Intent(Main.this, QingDao.class);
                    startActivity(intent);
                } else if (name == "南昌") {
                    Intent intent = new Intent(Main.this, NanChang.class);
                    startActivity(intent);
                } else if (name == "福州") {
                    Intent intent = new Intent(Main.this, FuZhou.class);
                    startActivity(intent);
                } else if (name == "东莞") {
                    Intent intent = new Intent(Main.this, DongGuang.class);
                    startActivity(intent);
                } else if (name == "南宁") {
                    Intent intent = new Intent(Main.this, NanNing.class);
                    startActivity(intent);
                } else if (name == "合肥") {
                    Intent intent = new Intent(Main.this, HeFei.class);
                    startActivity(intent);
                } else if (name == "哈尔滨") {
                    Intent intent = new Intent(Main.this, HaErBin.class);
                    startActivity(intent);
                } else if (name == "石家庄") {
                    Intent intent = new Intent(Main.this, ShiJiaZhuang.class);
                    startActivity(intent);
                } else if (name == "贵阳") {
                    Intent intent = new Intent(Main.this, GuiYang.class);
                    startActivity(intent);
                } else if (name == "厦门") {
                    Intent intent = new Intent(Main.this, XiaMen.class);
                    startActivity(intent);
                }
                //Toast.makeText(Main.this, city.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initFruits() {
        City beijing = new City("北京", R.drawable.hh);
        cityList.add(beijing);
        City shanghai = new City("上海", R.drawable.hh);
        cityList.add(shanghai);
        City guangzhou = new City("广州", R.drawable.hh);
        cityList.add(guangzhou);
        City shenzheng = new City("深圳", R.drawable.hh);
        cityList.add(shenzheng);
        City wuhan = new City("武汉", R.drawable.hh);
        cityList.add(wuhan);
        City tianjing = new City("天津", R.drawable.hh);
        cityList.add(tianjing);
        City nanjing = new City("南京", R.drawable.hh);
        cityList.add(nanjing);
        City hongkong = new City("香港", R.drawable.hh);
        cityList.add(hongkong);
        City chongqing = new City("重庆", R.drawable.hh);
        cityList.add(chongqing);
        City hangzhou = new City("杭州", R.drawable.hh);
        cityList.add(hangzhou);
        City shengyang = new City("沈阳", R.drawable.hh);
        cityList.add(shengyang);
        City dalian = new City("大连", R.drawable.hh);
        cityList.add(dalian);
        City chengdu = new City("成都", R.drawable.hh);
        cityList.add(chengdu);
        City changchun = new City("长春", R.drawable.hh);
        cityList.add(changchun);
        City suzhou = new City("苏州", R.drawable.hh);
        cityList.add(suzhou);
        City foshan = new City("佛山", R.drawable.hh);
        cityList.add(foshan);
        City kunming = new City("昆明", R.drawable.hh);
        cityList.add(kunming);
        City xian = new City("西安", R.drawable.hh);
        cityList.add(xian);
        City zhengzhou = new City("郑州", R.drawable.hh);
        cityList.add(zhengzhou);
        City changsha = new City("长沙", R.drawable.hh);
        cityList.add(changsha);
        City ningbo = new City("宁波", R.drawable.hh);
        cityList.add(ningbo);
        City wuxi = new City("无锡", R.drawable.hh);
        cityList.add(wuxi);
        City qingdao = new City("青岛", R.drawable.hh);
        cityList.add(qingdao);
        City nanchang = new City("南昌", R.drawable.hh);
        cityList.add(nanchang);
        City fuzhou = new City("福州", R.drawable.hh);
        cityList.add(fuzhou);
        City dongguang = new City("东莞", R.drawable.hh);
        cityList.add(dongguang);
        City nanning = new City("南宁", R.drawable.hh);
        cityList.add(nanning);
        City hefei = new City("合肥", R.drawable.hh);
        cityList.add(hefei);
        City haerbin = new City("哈尔滨", R.drawable.hh);
        cityList.add(haerbin);
        City shijiazhuang = new City("石家庄", R.drawable.hh);
        cityList.add(shijiazhuang);
        City guiyang = new City("贵阳", R.drawable.hh);
        cityList.add(guiyang);
        City xiamen = new City("厦门", R.drawable.hh);
        cityList.add(xiamen);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Intent intent = new Intent(Main.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}

