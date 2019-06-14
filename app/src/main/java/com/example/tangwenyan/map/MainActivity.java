package com.example.tangwenyan.map;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.offlinemap.OfflineMapActivity;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.example.tangwenyan.map.Activity.About;
import com.example.tangwenyan.map.Activity.BuslineActivity;
import com.example.tangwenyan.map.Activity.NavigationActivity;
import com.example.tangwenyan.map.Activity.PiclocationActivity;
import com.example.tangwenyan.map.Activity.PoiSearch;
import com.example.tangwenyan.map.Activity.Settings;
import com.example.tangwenyan.map.Activity.WeatherSearchActivity;
import com.example.tangwenyan.map.Login.Login1;
import com.example.tangwenyan.map.Login.Person_center;
import com.example.tangwenyan.map.Login.User;
import com.example.tangwenyan.map.SubWay.Main;
import com.example.tangwenyan.map.Tool.Tools;
import com.example.tangwenyan.map.Util.LocationUtils;
import com.example.tangwenyan.map.Util.PermissionsUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements AMapLocationListener{
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private AMap aMap;
    private MapView mMapView;
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //判断权限是否打开
    private boolean isNeedCheck = true;
    private static final int PERMISSON_REQUESTCODE = 0;
    private MyLocationStyle myLocationStyle;
    private String cityName, AdCode, cityCode, province, poiName, street, district;
    private double latitude, longitude;
    private LatLonPoint latLonPoint;
    /**
     * 起点坐标集合[由于需要确定方向，建议设置多个起点]
     */
    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    /**
     * 保存当前算好的路线
     */

    Integer flag;
    String name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this,"8ca4d792f5c8b4a700425736be46b2a4");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        User bmobuser = BmobUser.getCurrentUser(User.class);
        if(bmobuser !=null )
        {
            name = bmobuser.getUsername();
        }


        Log.d("MainActivity",name+"helloworld!nihao");
        setSupportActionBar(toolbar);

        //引入header和menu
        navView.inflateHeaderView(R.layout.nav_header);
        navView.inflateMenu(R.menu.nav_menu);

        //获取头部布局
        View navHeaderView = navView.getHeaderView(0);
        TextView cirIViewHead = (TextView) navHeaderView.findViewById(R.id.login_in);
        TextView state = (TextView) navHeaderView.findViewById(R.id.user_name);
        CircleImageView touxiang = (CircleImageView) navHeaderView.findViewById(R.id.icon_image);
        if(name==null)
        {
            cirIViewHead.setText("未登录");
            state.setText("登录后更精彩～");
        } else if(name!=null){

            cirIViewHead.setText(name);
            state.setText("欢迎登录！");
        }

        cirIViewHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.login_in:
                        if(name == null)
                        {
                            Intent intent4 = new Intent(MainActivity.this, Login1.class);
                            startActivity(intent4);
                        }
                        else if(name != null){
                            Intent intent3 = new Intent(MainActivity.this, Person_center.class);
                            startActivity(intent3);
                        }
                        break;
                }
            }
        });

        //头像点击事件
        touxiang.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                switch (view.getId()){
                    case R.id.icon_image:
                        //Toast.makeText(MainActivity.this,"头像",Toast.LENGTH_SHORT).show();
                        if(name == null)
                        {
                            Intent intent1 = new Intent(MainActivity.this,Login1.class);
                            startActivity(intent1);
                        }

                        else if(name != null){
                            Intent intent2 = new Intent(MainActivity.this,Person_center.class);
                            startActivity(intent2);
                        }
                        break;
                }
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);  //显示导航按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu); //设置导航按钮图标
        }
        //navView.setCheckedItem(R.id.nav_bus);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //选择某一个菜单项时需要处理的逻辑写在这里面
                switch (item.getItemId()) {
                    case R.id.nav_bus:
                        //Toast.makeText(MainActivity.this, "实时公交", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, BuslineActivity.class));
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_lxdt:
                        //Toast.makeText(MainActivity.this, "离线地图", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, OfflineMapActivity.class));
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_subway:
                       // Toast.makeText(MainActivity.this, "地铁线路图", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Main.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_shoucang:
                        Toast.makeText(MainActivity.this, "收藏夹,待完善！", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_settings:
                        Intent intent10 = new Intent(MainActivity.this, Settings.class);
                        startActivity(intent10);
                        break;
                    case R.id.nav_guanyu:
                        Intent intent11 = new Intent(MainActivity.this, About.class);
                        startActivity(intent11);
                        break;
                    default:
                        mDrawerLayout.closeDrawers();
                }
                return false;
            }
        });

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        //开始定位
        initLocation();

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.floatingButton1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE));
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton)findViewById(R.id.floatingButton3);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherSearchActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton)findViewById(R.id.floatingButton2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PiclocationActivity.class);
                startActivity(intent);
            }
        });

       Button button1 = (Button)findViewById(R.id.qrcode);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });

        //顶部搜索栏点击事件
        TextView search = (TextView) findViewById(R.id.text_search) ;
        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PoiSearch.class);//拾取坐标点
                startActivity(intent);
            }
        });
    }


    private void initLocation() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(200000);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(false); //设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        //缩放级别3 -19，不支持自定义 要监听否则无效
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            }
        });
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.showIndoorMap(true);
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


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:   //HomeAsUp按钮的id永远都是android.R.id.home
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.WX:
                //Toast.makeText(this, "You clicked 卫星地图", Toast.LENGTH_SHORT).show();
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.YJ:
                //Toast.makeText(this, "You clicked 夜间模式", Toast.LENGTH_SHORT).show();
                aMap.setMapType(AMap.MAP_TYPE_NIGHT);
                break;
            case R.id.DH:
                //Toast.makeText(this, "You clicked 导航地图", Toast.LENGTH_SHORT).show();
                aMap.setMapType(AMap.MAP_TYPE_NAVI);
                break;
            case R.id.PT:
                //Toast.makeText(this, "You clicked 普通地图", Toast.LENGTH_SHORT).show();
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }



    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation.getErrorCode() == 0) {
            //更多返回看(文档：http://lbs.amap.com/api/android-location-sdk/guide/android-location/getlocation)
            latitude = amapLocation.getLatitude();//获取纬度
            longitude = amapLocation.getLongitude();//获取经度
            province = amapLocation.getProvince();//省信息
            cityName = amapLocation.getCity();//城市信息
            district = amapLocation.getDistrict();//城区信息
            street = amapLocation.getStreet();//街道信息
            cityCode = amapLocation.getCityCode();//城市编码
            AdCode = amapLocation.getAdCode();//地区编码
            poiName = amapLocation.getPoiName();//获取当前位置的POI名称
            latLonPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
            Log.e(TAG, "onLocationChanged: " + cityName);
            Log.e(TAG, "onLocationChanged: " + AdCode);
            Log.e(TAG, "onLocationChanged: " + cityCode);
            Log.e(TAG, "onLocationChanged: " + province);
            Log.e(TAG, "onLocationChanged: " + poiName);
            Log.e(TAG, "onLocationChanged: " + latitude);
            Log.e(TAG, "onLocationChanged: " + street);
            Log.e(TAG, "onLocationChanged: " + longitude);
            Log.e(TAG, "onLocationChanged: " + district);
            Log.e(TAG, "onLocationChanged: " + amapLocation.getAddress());
            Log.e(TAG, "onLocationChanged: " + amapLocation.getStreetNum());
            Log.e(TAG, "onLocationChanged: " + amapLocation.getLocationType());
            Log.e(TAG, "onLocationChanged: " + amapLocation.getLocationDetail());
            StringBuffer buffer = new StringBuffer();
            buffer.append(amapLocation.getCountry() + ""
                    + amapLocation.getProvince() + ""
                    + amapLocation.getCity() + ""
                    + amapLocation.getProvince() + ""
                    + amapLocation.getDistrict() + ""
                    + amapLocation.getStreet() + ""
                    + amapLocation.getStreetNum());
            Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
            //停止定位后，本地定位服务并不会被销毁
            mLocationClient.stopLocation();
            //销毁定位客户端，同时销毁本地定位服务。
            mLocationClient.onDestroy();
        } else {
            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
            Log.e(TAG, "定位失败, 错误码:"
                    + amapLocation.getErrorCode() + "\n"
                    + " 错误信息:"
                    + amapLocation.getErrorInfo() + "\n"
                    + "APK SHA1值为：" + Tools.getSHA1(this));
            String str = amapLocation.getErrorInfo();
            String[] split = str.split(" ");
            //截取第一个空格之前的错误日志
            Toast.makeText(this, split[0], Toast.LENGTH_SHORT).show();
            //停止定位后，本地定位服务并不会被销毁
            mLocationClient.stopLocation();
            //销毁定位客户端，同时销毁本地定位服务。
            mLocationClient.onDestroy();
        }
    }

    private long clickTime = 0; //记录第一次点击的时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - clickTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                clickTime = System.currentTimeMillis();
            } else {
                this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
