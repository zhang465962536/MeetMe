package com.example.framework.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//万能适配器
public class CommonAdapter<T> extends RecyclerView.Adapter<CommomViewHolder> {

    private List<T> mList;

    //声明接口
    private OnBindDataListener<T> onBindDataListener;
    private OnMoreBindDataListener<T> onMoreBindDataListener;

    //适配一般的RecyclerView
    public CommonAdapter(List<T> mList, OnBindDataListener<T> onBindDataListener) {
        this.mList = mList;
        this.onBindDataListener = onBindDataListener;
    }

    //适配多类型的RecyclerView
    public CommonAdapter(List<T> mList, OnMoreBindDataListener<T> onMoreBindDataListener) {
        this.mList = mList;
        this.onBindDataListener = onMoreBindDataListener;
        this.onMoreBindDataListener = onMoreBindDataListener;
    }

    @Override
    public int getItemViewType(int position) {
        if(onMoreBindDataListener != null){
          return   onMoreBindDataListener.getItemType(position);
        }
        return 0;
    }

    //绑定数据
    //接口 让外部实现 方法往外抛 具体让他们自己实现
    public interface OnBindDataListener<T>{
        void onBindViewHolder(T model,CommomViewHolder viewHolder,int type,int position);

        int getLayoutId(int type);
    }

    //绑定多类型的数据
    public interface OnMoreBindDataListener<T> extends OnBindDataListener<T> {
        int getItemType(int position);
    }

    @NonNull
    @Override
    public CommomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = onBindDataListener.getLayoutId(viewType);
        CommomViewHolder viewHolder = CommomViewHolder.getViewHolder(parent,layoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommomViewHolder holder, int position) {
        //将onBindViewHolder 数据绑定抛给子类具体实现
        onBindDataListener.onBindViewHolder(mList.get(position),holder,getItemViewType(position),position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}
