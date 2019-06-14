package com.example.tangwenyan.map.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.example.tangwenyan.map.R;
import com.example.tangwenyan.map.Tool.Tools;
import com.example.tangwenyan.map.ViewHolder;

import java.util.List;


public class MyAdapter extends CommonAdapter<PoiItem> {

    private int selectedPosition = -1;

    public MyAdapter(Context context, List<PoiItem> datas) {
        super(context, R.layout.adapter_item, datas);
    }
    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }
    @Override
    public void convert(ViewHolder holder, PoiItem poiItem, int position) {
        //名称
        String name = poiItem.getTitle();
        TextView locationName = holder.getView(R.id.location_name);
        locationName.setText(Tools.isEmpty(name) ? "-" : name);
        //距离
        int distance = poiItem.getDistance();
        //街道
        String snippet = poiItem.getSnippet();
        TextView locationInfo = holder.getView(R.id.location_info);
        locationInfo.setText(distance + "m | " + snippet);

        if (selectedPosition == position) {
            locationName.setTextColor(Color.parseColor("#1296db"));
            locationInfo.setTextColor(Color.parseColor("#1296db"));
        } else {
            locationName.setTextColor(Color.parseColor("#444444"));
            locationInfo.setTextColor(Color.parseColor("#999999"));
        }
    }
}
