package com.example.framework.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.helper.GlideHelper;

import java.io.File;

//万能的ViewHolder
public class CommomViewHolder extends RecyclerView.ViewHolder {

    //子View集合 SparseArray 高性能存储View
    private SparseArray<View> mViews;
    //布局
    private View mContentView;

    public CommomViewHolder(@NonNull View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
        //通过它即可 findviewById
        mContentView = itemView;
    }

    //实现itemView  获取CommomViewHolder实体
    public static CommomViewHolder getViewHolder(ViewGroup parent,int layoutId){
        return new CommomViewHolder(View.inflate(parent.getContext(),layoutId,null));
    }

    //提供给外部访问View 的方法
    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if(view == null){
            view = mContentView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }

    //设置文本
    public CommomViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    //设置图片链接
    public CommomViewHolder setImageUrl(Context mContext, int viewId, String url) {
        ImageView iv = getView(viewId);
        GlideHelper.loadUrl(mContext, url, iv);
        return this;
    }

    //压缩
    public CommomViewHolder setImageUrl(Context mContext, int viewId, String url, int w, int h) {
        ImageView iv = getView(viewId);
       // GlideHelper.loadSmollUrl(mContext, url, w, h, iv);
        return this;
    }

    //设置图片文件
    public CommomViewHolder setImageFile(Context mContext, int viewId, File file) {
        ImageView iv = getView(viewId);
       // GlideHelper.loadFile(mContext, file, iv);
        return this;
    }

    //设置图片
    public CommomViewHolder setImageResource(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    //设置背景颜色
    public CommomViewHolder setBackgroundColor(int viewId, int color) {
        ImageView iv = getView(viewId);
        iv.setBackgroundColor(color);
        return this;
    }

    //设置文本颜色
    public CommomViewHolder setTextColor(int viewId, int color) {
        TextView tv = getView(viewId);
        tv.setTextColor(color);
        return this;
    }

    //设置控件的显示隐藏
    public CommomViewHolder setVisibility(int viewId, int visibility) {
        TextView tv = getView(viewId);
        tv.setVisibility(visibility);
        return this;
    }
}
