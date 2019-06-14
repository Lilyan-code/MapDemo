package com.example.tangwenyan.map.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tangwenyan.map.ViewHolder;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {

    //关联的布局
    protected LayoutInflater mInflater;
    //上下文
    protected Context mContext;
    //数据源
    protected List<T> mDatas;
    protected final int mItemLayoutId;

    public CommonAdapter(Context context, int itemLayoutId, List<T> mDatas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
        convert(viewHolder, getItem(position),position);
        return viewHolder.getConvertView();

    }

    public abstract void convert(ViewHolder holder, T item,int postion);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
    }
}


