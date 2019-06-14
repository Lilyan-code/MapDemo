package com.example.tangwenyan.map.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.tangwenyan.map.Adapter.InputTipsAdapter;
import com.example.tangwenyan.map.DataBase.HistroyDao;
import com.example.tangwenyan.map.DataBase.SearchHistorySQLiteHelper;
import com.example.tangwenyan.map.DataBase.Util.History;
import com.example.tangwenyan.map.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;


public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        Inputtips.InputtipsListener, OnItemClickListener {

    private boolean exist = false;
    private HashSet<String> a= new HashSet<>();
    private HashSet<Tip> b = new HashSet<>();
    private String TAG = "SearchActivity";
    private ListView mInputListView;
    private List<Tip> mCurrentTipList;
    private InputTipsAdapter mIntipAdapter;

    public String city = "北京";
    public static final int RESULT_CODE_INPUTTIPS = 101;
    public static final int REQUEST_SUC = 1000;

    private HistroyDao histroyDao;
    private SearchHistorySQLiteHelper helper = new SearchHistorySQLiteHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_input_search);
        super.onCreate(savedInstanceState);
        a= new HashSet<>();
        b = new HashSet<>();
        histroyDao = new HistroyDao(this);
        EventBus.getDefault().register(this);
        mInputListView = findViewById(R.id.inputtip_list);
        mInputListView.setOnItemClickListener(this);
        initSearchView();
        View footerView = LayoutInflater.from(this).inflate(R.layout.history_delete,null);
        mInputListView.addFooterView(footerView,null,false);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                histroyDao.delete();
                Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initSearchView() {
        SearchView searchView = findViewById(R.id.keyWord);
        searchView.setOnQueryTextListener(this);
        //设置SearchView默认为展开显示
        searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(false);
        initHistorySearch();
    }

    private void initHistorySearch() {
        LinkedList<String> stringList = histroyDao.query();
        LinkedList<Tip> tmpTipList = new LinkedList<Tip>();
        if (stringList.size() != 0) exist = true;
        stringList.forEach(s -> {
           //String ns = s.replace("point","position");
            //ns=ns.replace("id","poiid");
            String latitude1 = "", longtitude1 = "", ID = "";
            int index1 = s.indexOf("latitude\":");
            int index2 = s.indexOf("longitude\":");
            int end1 = s.indexOf(",", index1);
            int end2 = s.indexOf("}", index2);
            int index3 = s.indexOf("poiID");
            int end3 = s.indexOf(",", index3);
            Log.d(TAG, ",: " + end1 + "  " + end2);
            latitude1 = s.substring(index1 + 10, end1);
            longtitude1 =s.substring(index2 + 11, end2);
            ID = s.substring(index3 + 8, end3);
            Log.d(TAG, "ID起始: " + index3 + " " + end3);
            Log.d(TAG, "latitude1: " + latitude1);
           Log.d(TAG, "longtitude1: " + longtitude1);
            double latitude = Double.valueOf(latitude1.toString());
           double longtitude = Double.valueOf(longtitude1.toString());
           // Log.d(TAG, ": latitude" + latitude1);
          //  Log.d(TAG, "longtitude: " + longtitude1);
            Tip tip = null;
            a.add(s);
            try {
                tip = JSON.parseObject(s, Tip.class);
                tip.setPostion(new LatLonPoint(latitude, longtitude));
                tip.setID(ID);
                //tip.setPostion(new LatLonPoint());
                Log.d(TAG, "字符串: " + s);
                Log.d(TAG, "反序列化: " + tip);
                Log.d(TAG, "Point: " + tip.getPoint());
                Log.d(TAG, "tip: " + tip);
                b.add(tip);
            } catch (Exception e) {
                tip = new Tip();
                tip.setName(s);
                tip.setTypeCode("QWQ");
            }
            tmpTipList.add(tip);
        });
        mIntipAdapter = new InputTipsAdapter(getApplicationContext(), tmpTipList);
        mInputListView.setAdapter(mIntipAdapter);
        mIntipAdapter.notifyDataSetChanged();
        mInputListView.setOnItemClickListener(this::onItemClick);
    }

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

        }
    };

    /**
     * 输入提示回调
     */
    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        // 正确返回
        if (rCode == REQUEST_SUC) {
            mCurrentTipList = tipList;
            mIntipAdapter = new InputTipsAdapter(getApplicationContext(), mCurrentTipList);
            mInputListView.setAdapter(mIntipAdapter);
            mIntipAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "错误码 :" + rCode, Toast.LENGTH_SHORT).show();
        }
    }


        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (mCurrentTipList != null) {
                Tip tip = (Tip) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(SearchActivity.this, NavigationActivity.class);
                intent.putExtra("Tip_data", tip);
                EventBus.getDefault().post(tip);
                History history = new History(JSON.toJSONString(tip));
                Log.d(TAG, "序列化: " + history);
                Log.d(TAG, "判断是否存在: " + a.contains(JSON.toJSONString(tip)));
                if (!a.contains(JSON.toJSONString(tip))) {
                    histroyDao.insert(history);
                } /*else {
                    histroyDao.delete_sub(JSON.toJSONString(tip));
                    histroyDao.insert(history);
                }*/
                setResult(RESULT_CODE_INPUTTIPS, intent);
                finish();
            } else {
                //初始化历史记录
                Tip tip = (Tip) adapterView.getItemAtPosition(i);
                Log.d(TAG, "Tip: " + tip);
                if (!tip.getTypeCode().equals("QWQ")) {
                    Intent intent = new Intent();
                    intent.putExtra("Tip_data", tip);
                    EventBus.getDefault().post(tip);
                    Log.d(TAG, "onItemClick: " + tip.getPoint());
                    History history = new History(JSON.toJSONString(tip));
                    Log.d(TAG, "判断是否存在1: " + JSON.toJSONString(tip));
                    if (!b.contains(tip)) {
                        histroyDao.insert(history);
                    } /*else {
                        histroyDao.delete_sub(JSON.toJSONString(tip));
                        histroyDao.insert(history);
                    }*/
                    setResult(RESULT_CODE_INPUTTIPS, intent);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("Tip_data", tip);
                    Log.d(TAG, "onItemClick: " + tip.getPoint());
                    EventBus.getDefault().post(tip);
                    History history = new History(JSON.toJSONString(tip));
                    Log.d(TAG, "判断是否存在2: " + a.contains(JSON.toJSONString(tip)));
                    if (!b.contains(tip)) {
                        histroyDao.insert(history);
                    } /*else {
                        histroyDao.delete_sub(JSON.toJSONString(tip));
                        histroyDao.insert(history);
                    }*/
                    setResult(REQUEST_SUC, intent);
                    finish();
                }
            }
        }

    private boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select _id,name from history where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }

    /**
     * 按下确认键触发，本例为键盘回车或搜索键
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * 输入字符变化时触发
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, city);
            Inputtips inputTips = new Inputtips(SearchActivity.this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        } else {
            // 如果输入为空  则清除 listView 数据
            if (mIntipAdapter != null && mCurrentTipList != null) {
                mCurrentTipList.clear();
                mIntipAdapter.notifyDataSetChanged();
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}

