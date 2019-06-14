package com.example.tangwenyan.map.Adapter;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.services.help.Tip;
import com.example.tangwenyan.map.R;
import com.example.tangwenyan.map.ViewHolder;

import java.util.List;

public class TipAdapter extends CommonAdapter<Tip> {

    public TipAdapter(Context context, List<Tip> mDatas) {
        super(context, R.layout.list_tip_item, mDatas);
    }

    @Override
    public void convert(ViewHolder holder, Tip item, int postion) {
        //名称
        String name = item.getName();
        holder.setText(R.id.tip_item_name, TextUtils.isEmpty(name) ? "" : name);
        //地址
        String address = item.getAddress();
        holder.setText(R.id.tip_item_address, TextUtils.isEmpty(address) ? "" : address);
    }
}
