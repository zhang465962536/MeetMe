package com.example.meetme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.framework.helper.GlideHelper;
import com.example.framework.utils.LogUtils;
import com.example.meetme.R;
import com.example.meetme.model.AddFriendModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//多Type 的RecyclerView
public class AddFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //标题
    public static final int TYPE_TITLE = 0;
    //内容
    public static final int TYPE_CONTENT = 1;

    private Context mContext;
    private List<AddFriendModel> mList;
    private LayoutInflater inflater;

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public AddFriendAdapter(Context context, List<AddFriendModel> mList) {
        this.mContext = context;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_TITLE){
            return new TitleViewHolder(inflater.inflate(R.layout.layout_search_title_item,null));
        }else if(viewType == TYPE_CONTENT){
            return new ContentViewHolder(inflater.inflate(R.layout.layout_search_title_item,null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        AddFriendModel model = mList.get(position);
        if(model.getType()==TYPE_TITLE){
            ((TitleViewHolder)holder).tv_title.setText(model.getTitle());
        }else if(model.getType() == TYPE_CONTENT){
            //设置头像
           /* GlideHelper.loadUrl(model.getPhoto(), ((ContentViewHolder)holder).iv_photo);*/
            //设置性别
            ((ContentViewHolder)holder).iv_sex.setImageResource(model.isSex()?R.drawable.img_boy_icon:R.drawable.img_girl_icon);
            //设置昵称
            ((ContentViewHolder)holder).tv_nickname.setText(model.getNickName());
            //年龄
            ((ContentViewHolder)holder).tv_age.setText(model.getAge());
            //设置个性签名
            ((ContentViewHolder)holder).tv_desc.setText(model.getDesc());
        }

        //点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null){
                    onClickListener.OnClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //区分Type
    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    class TitleViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_title;
        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView iv_photo;
        private ImageView iv_sex;
        private TextView tv_nickname;
        private TextView tv_age;
        private TextView tv_desc;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_photo = itemView.findViewById(R.id.iv_photo);
            iv_sex = itemView.findViewById(R.id.iv_sex);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_age = itemView.findViewById(R.id.tv_age);
            tv_desc = itemView.findViewById(R.id.tv_desc);

        }
    }

    //点击Item事件
    public interface OnClickListener{
        void OnClick(int position);
    }
}
