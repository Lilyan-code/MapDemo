package com.example.tangwenyan.map.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.tangwenyan.map.DataBase.HistroyDao;
import com.example.tangwenyan.map.DataBase.SearchHistorySQLiteHelper;
import com.example.tangwenyan.map.DataBase.Util.History;
import com.example.tangwenyan.map.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PoiSearchActivity extends AppCompatActivity implements TextWatcher, Inputtips.InputtipsListener, View.OnClickListener {
    private final static String TAG = "PoiSearchActivity";
    private BaseAdapter baseAdapter;
    private CommonAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<Tip> tips = new ArrayList<>();
    private String city = null;
    private double latitude, longitude;
    private HistroyDao histroyDao;
    private SearchHistorySQLiteHelper helper = new SearchHistorySQLiteHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);
//        getSupportActionBar().hide();
        histroyDao = new HistroyDao(this);
        EventBus.getDefault().register(this);
        AutoCompleteTextView mKeywordText = (AutoCompleteTextView)findViewById(R.id.input_edittext);
        mKeywordText.addTextChangedListener(this);
        findViewById(R.id.rl_tv_map_pick).setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.ll_rv_inputlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = getAdapter();
        mRecyclerView.setAdapter(mAdapter);
        ImageView imageView = (ImageView)findViewById(R.id.back_2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PoiSearchActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 跳转到地图地图选点Activity
     * @param v
     */
    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, PiclocationActivity.class));
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
//            tips.addAll(list);
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
                        //Toast.makeText(getApplicationContext(), tip.getName(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), tip.getDistrict(), Toast.LENGTH_LONG).show();
                        EventBus.getDefault().post(tip);
                        Log.d(TAG, "onClick: " + tip.getName());
                        History history = new History(tip.getName());
                        /*if (!hasData(tip.getName())) {
                            histroyDao.insert(history);
                        }*/
                        histroyDao.insert(history);
                        finish();
                    }
                });
            }
        };
    }

    /**
     * 检查数据库中是否已经有该条记录
     */
    private boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from history where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }
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

