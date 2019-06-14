package com.example.tangwenyan.map.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.example.tangwenyan.map.Adapter.BusResultListAdapter;
import com.example.tangwenyan.map.Overlay.DrivingRouteOverlay;
import com.example.tangwenyan.map.R;
import com.example.tangwenyan.map.Util.ToastUtil;
import com.example.tangwenyan.map.Tool.Tools;
import com.example.tangwenyan.map.Util.AMapUtil;
import com.example.tangwenyan.map.Util.LocationUtils;
import com.example.tangwenyan.map.Util.PermissionsUtils;
import com.example.tangwenyan.map.Overlay.WalkRouteOverlay;


/**
 * Route路径规划: 驾车规划、公交规划、步行规划
 */
public class RouteActivity extends Activity implements OnMapClickListener,
        OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter, OnRouteSearchListener, AMapLocationListener {
    private static final String TAG = "RouteAcitivity";
    private AMap aMap;
    private MapView mapView;
    private MyLocationStyle myLocationStyle;
    private boolean isNeedCheck = true;
    private static final int PERMISSON_REQUESTCODE = 0;
    public AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mLocationClient = null;
    private Context mContext;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private BusRouteResult mBusRouteResult;
    private WalkRouteResult mWalkRouteResult;
    private LatLonPoint mStartPoint = new LatLonPoint(25.06504605, 110.30068517);//起点，116.335891,39.942295
    private LatLonPoint mEndPoint = new LatLonPoint(25.26425799, 110.32814026);//终点，116.481288,39.995576
    private LatLonPoint mStartPoint_bus = new LatLonPoint(40.818311, 111.670801);//起点，111.670801,40.818311
    private LatLonPoint mEndPoint_bus = new LatLonPoint(39.91776787, 116.39705658);//终点，
    private String mCurrentCityName = "桂林";
    private final int ROUTE_TYPE_BUS = 1;
    private final int ROUTE_TYPE_DRIVE = 2;
    private final int ROUTE_TYPE_WALK = 3;
    private final int ROUTE_TYPE_CROSSTOWN = 4;
    private TextView tvStart, tvEnd;
    private LinearLayout mBusResultLayout;
    private RelativeLayout mBottomLayout;
    private TextView mRotueTimeDes, mRouteDetailDes;
    private ImageView mBus;
    private ImageView mDrive;
    private ImageView mWalk;
    private ListView mBusResultList;
    private ProgressDialog progDialog = null;// 搜索时进度条
    private String cityName, AdCode, cityCode, province, poiName, street, district;
    private double latitude, longitude;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.route_activity);
        Tip tip0 = (Tip)getIntent().getParcelableExtra("Tipdata0");
        Tip tip1 = (Tip)getIntent().getParcelableExtra("Tipdata1");
        LatLonPoint startLp = tip0.getPoint();
        mStartPoint = new LatLonPoint(startLp.getLatitude(), startLp.getLongitude());
        mStartPoint_bus = new LatLonPoint(startLp.getLatitude(), startLp.getLongitude());
        LatLonPoint endLp = tip1.getPoint();
        mEndPoint = new LatLonPoint(endLp.getLatitude(), endLp.getLongitude());
        mEndPoint_bus = new LatLonPoint(endLp.getLatitude(), endLp.getLongitude());
        //EventBus.getDefault().register(this);
        //Intent intent = getIntent();
        //mStartPoint = new LatLonPoint(intent.getDoubleExtra("longitude", 0.0), intent.getDoubleExtra("latitude",0.0));
       // mEndPoint = new LatLonPoint(110.425538, 25.322186);
        //mCurrentCityName = intent.getStringExtra("city");
        mContext = this.getApplicationContext();
        mapView = (MapView) findViewById(R.id.route_map);
        mapView.onCreate(bundle);// 此方法必须重写
        initLocation();
        init();
        setfromandtoMarker();
    }

    /**
     * 获取终点信息
     *
     * @param
     */
   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Tip tip) {
        tvEnd.setText("到     " + tip.getDistrict());
        LatLonPoint endLp = tip.getPoint();
        mEndPoint = new LatLonPoint(endLp.getLatitude(), endLp.getLongitude());
    }
    public void onEventMainThread(Tip tip) {
        LatLonPoint endLp = tip.getPoint();
        mEndPoint = new LatLonPoint(endLp.getLatitude(), endLp.getLongitude());
    }*/

    private void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
    }

    private void initLocation() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(200000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            }
        });
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

    /**
     * 初始化AMap对象
     */
    private void init() {
        registerListener();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mBusResultLayout = (LinearLayout) findViewById(R.id.bus_result);
        mRotueTimeDes = (TextView) findViewById(R.id.firstline);
        mRouteDetailDes = (TextView) findViewById(R.id.secondline);
        mDrive = (ImageView)findViewById(R.id.route_drive);
        mBus = (ImageView)findViewById(R.id.route_bus);
        mWalk = (ImageView)findViewById(R.id.route_walk);
        mBusResultList = (ListView) findViewById(R.id.bus_result_list);
    }

    /**
     * 注册监听
     */
    private void registerListener() {
        aMap.setOnMapClickListener(RouteActivity.this);
        aMap.setOnMarkerClickListener(RouteActivity.this);
        aMap.setOnInfoWindowClickListener(RouteActivity.this);
        aMap.setInfoWindowAdapter(RouteActivity.this);

    }

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
           // mStartPoint = new LatLonPoint(latitude, longitude);
           // mStartPoint_bus = new LatLonPoint(latitude, longitude);
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

    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * 公交路线搜索
     */
    public void onBusClick(View view) {
        searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
        mDrive.setImageResource(R.drawable.route_drive_normal);
        mBus.setImageResource(R.drawable.route_bus_select);
        mWalk.setImageResource(R.drawable.route_walk_normal);
        mapView.setVisibility(View.GONE);
        mBusResultLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 驾车路线搜索
     */
    public void onDriveClick(View view) {
        searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
        mDrive.setImageResource(R.drawable.route_drive_select);
        mBus.setImageResource(R.drawable.route_bus_normal);
        mWalk.setImageResource(R.drawable.route_walk_normal);
        mapView.setVisibility(View.VISIBLE);
        mBusResultLayout.setVisibility(View.GONE);
    }

    /**
     * 步行路线搜索
     */
    public void onWalkClick(View view) {
        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
        mDrive.setImageResource(R.drawable.route_drive_normal);
        mBus.setImageResource(R.drawable.route_bus_normal);
        mWalk.setImageResource(R.drawable.route_walk_select);
        mapView.setVisibility(View.VISIBLE);
        mBusResultLayout.setVisibility(View.GONE);
    }

    /**
     * 跨城公交路线搜索
     */
    public void onCrosstownBusClick(View view) {
        searchRouteResult(ROUTE_TYPE_CROSSTOWN, RouteSearch.BusDefault);
        mDrive.setImageResource(R.drawable.route_drive_normal);
        mBus.setImageResource(R.drawable.route_bus_normal);
        mWalk.setImageResource(R.drawable.route_walk_normal);
        mapView.setVisibility(View.GONE);
        mBusResultLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            ToastUtil.show(mContext, "起点未设置");
            return;
        }
        if (mEndPoint == null) {
            ToastUtil.show(mContext, "终点未设置");
        }
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_BUS) {// 公交路径规划
            BusRouteQuery query = new BusRouteQuery(fromAndTo, mode,
                    mCurrentCityName, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        } else if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            DriveRouteQuery query = new DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        } else if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            WalkRouteQuery query = new WalkRouteQuery(fromAndTo);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        } else if (routeType == ROUTE_TYPE_CROSSTOWN) {
            RouteSearch.FromAndTo fromAndTo_bus = new RouteSearch.FromAndTo(
                    mStartPoint_bus, mEndPoint_bus);
            BusRouteQuery query = new BusRouteQuery(fromAndTo_bus, mode,
                    "呼和浩特市", 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            query.setCityd("农安县");
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        }
    }

    /**
     * 公交路线搜索结果方法回调
     */
    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        dissmissProgressDialog();
        mBottomLayout.setVisibility(View.GONE);
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mBusRouteResult = result;
                    BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(mContext, mBusRouteResult);
                    mBusResultList.setAdapter(mBusResultListAdapter);
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }
            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }

    /**
     * 驾车路线搜索结果方法回调
     */
    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    if(drivePath == null) {
                        return;
                    }
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            mContext, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.VISIBLE);
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    mRouteDetailDes.setText("打车约"+taxiCost+"元");
                    mBottomLayout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext,
                                    DriveRouteDetailActivity.class);
                            intent.putExtra("drive_path", drivePath);
                            intent.putExtra("drive_result",
                                    mDriveRouteResult);
                            startActivity(intent);
                        }
                    });
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }


    }

    /**
     * 步行路线搜索结果方法回调
     */
    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    if(walkPath == null) {
                        return;
                    }
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.GONE);
                    mBottomLayout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext,
                                    WalkRouteDetailActivity.class);
                            intent.putExtra("walk_path", walkPath);
                            intent.putExtra("walk_result",
                                    mWalkRouteResult);
                            startActivity(intent);
                        }
                    });
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }


    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null) {
            progDialog = new ProgressDialog(this);
        }
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        //EventBus.getDefault().unregister(this);
    }

    /**
     * 骑行路线搜索结果方法回调
     */
    @Override
    public void onRideRouteSearched(RideRouteResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }

}

