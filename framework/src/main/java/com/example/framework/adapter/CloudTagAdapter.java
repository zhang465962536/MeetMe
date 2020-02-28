package com.example.framework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.framework.R;
import com.moxun.tagcloudlib.view.TagsAdapter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//3D星球适配器
public class CloudTagAdapter extends TagsAdapter {

    private Context mContext;
    private List<String> mList;
    private LayoutInflater inflater;

    public CloudTagAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(Context context, int position, ViewGroup parent) {
        //初始化View
        View view = inflater.inflate(R.layout.layout_star_view_item,null);
        //初始化控件
        CircleImageView iv_star_icon = view.findViewById(R.id.iv_star_icon);
        TextView tv_star_name = view.findViewById(R.id.tv_star_name);
        //赋值
        tv_star_name.setText(mList.get(position));
        iv_star_icon.setImageResource(R.drawable.img_bg);

        return view;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return 0;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {

    }
}
