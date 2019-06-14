package com.example.tangwenyan.map.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import com.example.tangwenyan.map.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义离线地图用法
 */
public class OfflineMapMyActivity extends AppCompatActivity implements OfflineMapManager.OfflineMapDownloadListener,OfflineMapManager.OfflineLoadedListener {

    private List<OfflineMapProvince> list = new ArrayList<>();

    private OfflineMapManager amapManager;
    private static final String TAG = "OfflineMapMyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (amapManager == null) {
            //构造OfflineMapManager对象
            amapManager = new OfflineMapManager(this, this);
            amapManager.setOnOfflineLoadedListener(this);
        }
        try {
            //按照citycode下载 (城市编码去官网下载即可)
            //amapManager.downloadByCityCode("城市编码");
            //按照cityname下载
            //开始执行下载任务
            amapManager.downloadByCityName("赣州市");
            amapManager.downloadByCityName("深圳市");
            //通过updateOfflineCityByName方法判断离线地图数据是否存在更新
            //amapManager.updateOfflineCityByName("赣州市");
            //城市编码
            //amapManager.updateOfflineCityByCode("城市编码");
            //删除某一城市的离线地图包
            //注意：执行 remove 操作时，需要等待 OfflineLoadedListener 回调之后才可以，
            //否则（即使OfflineMapDownloadListener回调成功）操作将会无效
            //amapManager.remove("赣州市");

            //http://lbs.amap.com/api/android-sdk/guide/create-map/offline-map
            //获取省列表
            amapManager.getOfflineMapProvinceList();
            //获取城市列表
            amapManager.getOfflineMapCityList();
            //获取已下载城市列表
            amapManager.getDownloadOfflineMapCityList();
            //获取正在或等待下载城市列表
            amapManager.getDownloadingCityList();

            /**
             * 用法
             * 其实就是将它作为list数据源传入进去
             */
            //将省份赋值给list
            list = amapManager.getOfflineMapProvinceList();
            //自行新建listView与Adapter
            //listView.setAdapter(MyAdapter);
            Log.e(TAG, "全国省份的大小: " + list.size());
            //遍历全国省份的名称
            for (int i = 0; i < list.size(); i++) {
                Log.e(TAG, "省份的名称: " + list.get(i).getProvinceName());
                Log.e(TAG, "省份的编码: " + list.get(i).getProvinceCode());
                Log.e(TAG, "省份的拼音名称: " + list.get(i).getPinyin());
            }

        } catch (AMapException e) {
            e.printStackTrace();
        }
        //暂停地图的下载
        //amapManager.pause();
        //停止所有当前正在执行的下载，包括下载队列中等待的部分
        //amapManager.stop();
        //离线地图默认会下载到手机存储卡的“amap”目录下，也可以自定义路径：
        //通过 MapInitializer.sdcardDir 设置路径时，需要在 AMap 对象初始化之前进行，否则操作会无效
        //MapsInitializer.sdcardDir="";
    }

    /**
     * 下载任务中的回调
     *
     * @param i  状态码 0为成功 101：下载出错  4：下载完成（其他状态码暂无...）
     * @param i1 下载的进度
     * @param s  所下载的城市名
     */
    @Override
    public void onDownload(int i, int i1, String s) {
        //这里可以加载进度条，（可自行添加）这个方法在下载时会实时回调
        //注意不支持多个城市同时下载，会先执行开头下载的城市，在下载第二个城市
        Log.e(TAG, "onDownload: " + i + "\n" + i1 + "\n" + s);
    }

    /**
     * 检查城市是否有新的离线版本包
     *
     * @param b 是否有更新
     * @param s 检查更新的城市名
     */
    @Override
    public void onCheckUpdate(boolean b, String s) {
        //检查更新的回调 true+城市名
        Log.e(TAG, "onCheckUpdate: " + b + "\n" + s);
    }

    @Override
    public void onRemove(boolean b, String s, String s1) {
        Log.e(TAG, "onRemove: " + b + "\n" + s + "\n" + s1);
    }

    @Override
    public void onVerifyComplete() {
        Log.e(TAG, "onVerifyComplete: ");
    }

    /**
     * 自行实现
     */
    private BaseAdapter MyAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            //获取省的名字
            //list.get(i).getProvinceName();
            //获取省的编码
            //list.get(i).getProvinceCode();
            //获取该省的城市列表
            //list.get(i).getCityList();
            return null;
        }
    };
}
