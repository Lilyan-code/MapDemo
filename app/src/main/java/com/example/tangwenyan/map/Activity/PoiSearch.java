package com.example.tangwenyan.map.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.tangwenyan.map.MainActivity;
import com.example.tangwenyan.map.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class PoiSearch extends AppCompatActivity implements TextWatcher, Inputtips.InputtipsListener, View.OnClickListener {
    private static final String TAG = "PoiSearch";
    private CommonAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<Tip> tips = new ArrayList<>();
    private String city = null;
    private double latitude, longitude;
    private int weizhi = 0;
    AutoCompleteTextView mKeywordText;
    AutoCompleteTextView mKeywordText1;
    Tip tip0, tip1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poi_search_top);
//        getSupportActionBar().hide();
        EventBus.getDefault().register(this);
        mKeywordText = (AutoCompleteTextView)findViewById(R.id.input_edittext);
        mKeywordText1 = (AutoCompleteTextView)findViewById(R.id.input_edittext1);
        mKeywordText.addTextChangedListener(this);
        mKeywordText1.addTextChangedListener(this);
        findViewById(R.id.rl_tv_map_pick).setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.ll_rv_inputlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = getAdapter();
        mRecyclerView.setAdapter(mAdapter);
        ImageView imageView = (ImageView) findViewById(R.id.back_1);
        mKeywordText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                weizhi = 0;
                return false;
            }
        });
        mKeywordText1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                weizhi = 1;
                return false;
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PoiSearch.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView1 = (ImageView)findViewById(R.id.chaxun);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PoiSearch.this, RouteActivity.class);
                intent.putExtra("Tipdata0", tip0);
                intent.putExtra("Tipdata1", tip1);
                startActivity(intent);
            }
        });

       ImageView imageView2 = (ImageView)findViewById(R.id.jiaohuan);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tip tmp = tip0;
                tip0 = tip1;
                tip1 = tmp;
                mKeywordText.setText(tip0.getName());
                mKeywordText1.setText(tip1.getName());
            }
        });
    }

    /**
     * 跳转到地图地图选点Activity
     * @param v
     */
    @Override
    public void onClick(View v) {

        startActivity(new Intent(this, SroundActivity.class));
    }

    /**
     * 选择了地点，关闭当前Activity
     * @param tip
     */
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = false,priority = 2)
    public void close(Tip tip) {
        finish();
    };

    /**
     * 设置当前城市
     * @param amapLocation
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void setCity(AMapLocation amapLocation) {
        if(amapLocation!=null){
            this.city = amapLocation.getCity();
            this.latitude = amapLocation.getLatitude();
            this.longitude = amapLocation.getLongitude();
        }
    };
    /**
     * 文本变化监听事件
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        InputtipsQuery inputquery = new InputtipsQuery(newText, city);
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips(this, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();

    }

    /**
     * 输入自动提示结果
     * @param list
     * @param i
     */
    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        if(i == AMapException.CODE_AMAP_SUCCESS){
            if(tips.size()>0)
                tips.clear();
            for (Tip tip:list) {
                if(tip.getPoint()!=null){
                    tips.add(tip);
                }
            }
//           tips.addAll(list);
           // Toast.makeText(this, "点击", Toast.LENGTH_SHORT).show();
            mAdapter.notifyDataSetChanged();
        }
    }
    private CommonAdapter getAdapter() {
        return new CommonAdapter<Tip>(this,R.layout.item_layout,tips) {

            @Override
            protected void convert(ViewHolder holder, final Tip tip, int position) {

                holder.setText(R.id.poi_field_id,tip.getName());
                holder.setText(R.id.poi_value_id,tip.getDistrict());

                holder.getView(R.id.item_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (weizhi == 0) {
                            mKeywordText.setText(tip.getName());
                            tip0 = tip;
                        } else {
                            mKeywordText1.setText(tip.getName());
                            tip1 = tip;
                        }
                       /* if (weizhi == 0) {
                            mKeywordText.setText(tip.getName());
                        } else {
                            mKeywordText1.setText(tip.getName());
                        }*/
                        //EventBus.getDefault().post(tip);
                        //Intent intent = new Intent(PoiSearch.this,RouteActivity.class);
                        //intent.putExtra("Tip_data", tip);
                        //startActivity(intent);
                        //finish();
                    }
                });
            }
        };
    }

    /*Intent intent = new Intent(PoiSearch.this,RouteActivity.class);
                        intent.putExtra("longitude", longitude);
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("city", city);
                        startActivity(intent);*/

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

