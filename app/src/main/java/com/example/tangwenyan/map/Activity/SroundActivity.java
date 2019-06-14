package com.example.tangwenyan.map.Activity;
//
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.tangwenyan.map.Adapter.MyAdapter;
import com.example.tangwenyan.map.MainActivity;
import com.example.tangwenyan.map.R;
import com.example.tangwenyan.map.Tool.Tools;
import com.example.tangwenyan.map.Util.LocationUtils;
import com.example.tangwenyan.map.Util.PermissionsUtils;
import com.example.tangwenyan.map.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class SroundActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener,
        AMapLocationListener {

    private static final String TAG = "SroundActivity";
    private List<Tip> tips = new ArrayList<>();
    private MapView mMapView;
    private ListView listView;
    private TabLayout tabLayout;

    private AMap aMap;
    //判断权限是否打开
    private boolean isNeedCheck = true;
    private MyLocationStyle myLocationStyle;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private static final int PERMISSON_REQUESTCODE = 0;
    private MyAdapter adapter;
    /**
     * 查询的类型
     */
    private String BANK = "银行";
    private String DINING_ROOM = "餐厅";
    private String PLOT = "小区";
    private String OFFICE_BUILDINGS = "写字楼";
    private String BUS_STATION = "公交站";
    private String SUBWAY_STATION = "地铁站";
    private String LAVATORY = "厕所";
    private String SUPERMARKET = "超市";

    /**
     * 纬度、经度、城市名
     */
    private double latitude, longitude;
    private String cityName;
    private ProgressDialog progressDialog;
    EditText inputText;
    Tip tip = new Tip();
    Tip tip0 = new Tip();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sround);
       // EventBus.getDefault().register(this);
        mMapView = (MapView) this.findViewById(R.id.map);
        listView = (ListView) this.findViewById(R.id.listview);
        tabLayout = (TabLayout) this.findViewById(R.id.tablayout);

        // 此方法须重写，虚拟机需要在很多情况下保存地图绘制的当前状态
        mMapView.onCreate(savedInstanceState);

        ininview();
        initLocation();
        inputText = (EditText)findViewById(R.id.et_search);
        ImageView imageView = (ImageView)findViewById(R.id.iv_return);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SroundActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView1 = (ImageView)findViewById(R.id.iv_btn_search);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SroundActivity.this, Poi_navi.class);
               // EventBus.getDefault().post(tip);
                intent.putExtra("Tip_end", tip);
                intent.putExtra("Tip_start", tip0);
                startActivity(intent);
            }
        });
    }

    private void ininview() {
        tabLayout.addTab(tabLayout.newTab().setText(DINING_ROOM));
        tabLayout.addTab(tabLayout.newTab().setText(BANK));
        tabLayout.addTab(tabLayout.newTab().setText(PLOT));
        tabLayout.addTab(tabLayout.newTab().setText(OFFICE_BUILDINGS));
        tabLayout.addTab(tabLayout.newTab().setText(BUS_STATION));
        tabLayout.addTab(tabLayout.newTab().setText(SUBWAY_STATION));
        tabLayout.addTab(tabLayout.newTab().setText(LAVATORY));
        tabLayout.addTab(tabLayout.newTab().setText(SUPERMARKET));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.layout_divider_vertical));
        linearLayout.setDividerPadding(30);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selPosition = tab.getPosition();
                if (selPosition == 0) {
                    seartchPoiStart(DINING_ROOM);
                } else if (selPosition == 1) {
                    seartchPoiStart(BANK);
                } else if (selPosition == 2) {
                    seartchPoiStart(PLOT);
                } else if (selPosition == 3) {
                    seartchPoiStart(OFFICE_BUILDINGS);
                } else if (selPosition == 4) {
                    seartchPoiStart(BUS_STATION);
                } else if (selPosition == 5) {
                    seartchPoiStart(SUBWAY_STATION);
                } else if (selPosition == 6) {
                    seartchPoiStart(LAVATORY);
                } else {
                    seartchPoiStart(SUPERMARKET);
                }
                Log.e(TAG, "onTabSelected: " + selPosition);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initLocation() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            }
        });
        //指南针
        aMap.getUiSettings().setCompassEnabled(false);
        //显示默认的定位按钮
        aMap.getUiSettings().setMyLocationButtonEnabled(false);

        //显示实时交通状况(默认地图)
        aMap.setTrafficEnabled(true);
        //地图模式-标准地图：MAP_TYPE_NORMAL、卫星地图：MAP_TYPE_SATELLITE
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        myLocationStyle = new MyLocationStyle();
        //设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
        //定位一次，且将视角移动到地图中心点。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
        //动态设置权限
        if (isNeedCheck) {
            if (PermissionsUtils.checkPermissions(this, PERMISSON_REQUESTCODE, PermissionsUtils.locationPermissions)) {
                initaion();
            }
        } else {
            initaion();
        }
    }

    private void initaion() {
        //基本的定位参数
        mLocationOption = LocationUtils.getDefaultOption();
        mLocationClient = LocationUtils.initLocation(this, mLocationOption, this);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!PermissionsUtils.verifyPermissions(grantResults)) {
                Tools.startAppSettings(this);
                isNeedCheck = false;
            } else {
                initaion();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //重新绘制加载地图
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //暂停地图的绘制
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            //停止定位后，本地定位服务并不会被销毁
            mLocationClient.stopLocation();
            //销毁定位客户端，同时销毁本地定位服务。
            mLocationClient.onDestroy();
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 开始默认查询附近地点
     */
    private void seartchPoiStart(String key) {
        //创建搜索对象
        PoiSearch.Query query = new PoiSearch.Query(key, "", cityName);
        //设置每页最多返回多少条poiitem
        query.setPageSize(30);
        //设置查询页码
        query.setPageNum(1);
        //构造 PoiSearch 对象，并设置监听。
        PoiSearch search = new PoiSearch(this, query);
        //设置周边搜索的中心点以及区域 5000米-5公里
        search.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude), 3000));
        search.setOnPoiSearchListener(this);
        //开始搜索
        search.searchPOIAsyn();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在查询，请稍后...");
        progressDialog.show();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
       // LatLonPoint latLonPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
       // tip0.setPostion(latLonPoint);
        if (amapLocation.getErrorCode() == 0) {
            //停止定位后，本地定位服务并不会被销毁
            mLocationClient.stopLocation();
            //销毁定位客户端，同时销毁本地定位服务。
            mLocationClient.onDestroy();
            //更多返回看(文档：http://lbs.amap.com/api/android-location-sdk/guide/android-location/getlocation)
            latitude = amapLocation.getLatitude();//获取纬度
            longitude = amapLocation.getLongitude();//获取经度
            cityName = amapLocation.getCity();//城市信息
            //定位成功在请求附近point  默认餐厅
            seartchPoiStart(DINING_ROOM);
        } else {
            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
            String str = amapLocation.getErrorInfo();
            String[] split = str.split(" ");
            //截取第一个空格之前的错误日志
            Toast.makeText(this, "定位失败，" + split[0], Toast.LENGTH_LONG).show();
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void onPoiSearched(final PoiResult poiResult, int i) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        //1000为成功，其他失败http://lbs.amap.com/api/android-location-sdk/guide/utilities/errorcode/
        if (i == 1000) {
            if (poiResult.getPois().size() > 0) {
                adapter = new MyAdapter(this, poiResult.getPois());
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //根据当前选中的postion 设置高亮
                        adapter.setSelectedPosition(position);
                        adapter.notifyDataSetInvalidated();
                        //设置点击的标记
                        setMarkerOptions(poiResult.getPois().get(position).getTitle(), poiResult.getPois().get
                                (position).getDistance(), poiResult.getPois().get(position).getLatLonPoint()
                                .getLatitude(), poiResult.getPois().get(position).getLatLonPoint().getLongitude());
                      // Toast.makeText(getApplicationContext(), poiResult.getPois().get(position).getAdName(), Toast.LENGTH_LONG).show();
                        inputText.setText(poiResult.getPois().get(position).getTitle());
                        tip.setName(poiResult.getPois().get(position).getTitle());
                        Log.d(TAG, "onItemClick: " + tip.getName());
                        //tip.setDistrict(poiResult.getPois().get(position).getAdName());
                        tip.setPostion(poiResult.getPois().get(position).getLatLonPoint());
                        Log.d(TAG, "onItemClick: " + tip.getPoint());
                        tip.setDistrict(poiResult.getPois().get(position).getProvinceName() + poiResult.getPois().get(position).getCityName() +
                                poiResult.getPois().get(position).getAdName());
                        /*
                        *poi_field_id,tip.getName());
                holder.setText(R.id.poi_value_id,tip.getDistrict());
                         */
                    }
                });


            } else {
                Toast.makeText(this, "暂无结果", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "搜索失败", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 设置标记
     *
     * @param name      标记的名称
     * @param distance  距离
     * @param latitude  纬度
     * @param longitude 经度
     */
    private void setMarkerOptions(String name, int distance, double latitude, double longitude) {
        LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
        tip0.setPostion(latLonPoint);
        //在地图上添加一个marker，并将地图中移动至此处
        MarkerOptions mk = new MarkerOptions();
        //设置定位的图片 (默认)
        //本地图片BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable
        // .location_marker))
        mk.icon(BitmapDescriptorFactory.defaultMarker());
        //设置点击的名称
        mk.title(name);
        //点标记的内容
        mk.snippet("距离" + distance + "m");
        //点标记是否可拖拽
        //mk.draggable(true);
        //点标记的锚点
        mk.anchor(1.5f, 3.5f);
        //点的透明度
        //mk.alpha(0.7f);
        //设置marker平贴地图效果
        mk.setFlat(true);
        //设置纬度和经度
        LatLng ll = new LatLng(latitude, longitude);
        mk.position(ll);
        //清除所有marker等，保留自身
        aMap.clear(true);
        CameraUpdate cu = CameraUpdateFactory.newLatLng(ll);
        aMap.animateCamera(cu);
        aMap.addMarker(mk);
    }
}

