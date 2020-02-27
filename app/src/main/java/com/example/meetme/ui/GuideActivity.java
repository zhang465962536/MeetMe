package com.example.meetme.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.framework.base.BasePageAdapter;
import com.example.framework.base.BaseUIActivity;
import com.example.framework.manager.MediaPlayerManager;
import com.example.framework.utils.AnimUtils;
import com.example.meetme.R;

import java.util.ArrayList;
import java.util.List;

//引导页面

/**
 * 1.ViewPager : 适配器|帧动画播放
 * 2.小圆点的逻辑
 * 3.歌曲的播放
 * 4.属性动画旋转
 * 5.跳转
 */
public class GuideActivity extends BaseUIActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private ImageView iv_music_switch;
    private TextView tv_guide_skip;
    private ImageView iv_guide_point_1;
    private ImageView iv_guide_point_2;
    private ImageView iv_guide_point_3;

    private View view1;
    private View view2;
    private View view3;

    private List<View> mPageList = new ArrayList<>();
    private BasePageAdapter mPageAdapter;

    private ImageView iv_guide_star;
    private ImageView iv_guide_night;
    private ImageView iv_guide_smile;

    private MediaPlayerManager mGuideMusic;

    private ObjectAnimator mAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }


    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        iv_music_switch = (ImageView) findViewById(R.id.iv_music_switch);
        tv_guide_skip = (TextView) findViewById(R.id.tv_guide_skip);
        iv_guide_point_1 = (ImageView) findViewById(R.id.iv_guide_point_1);
        iv_guide_point_2 = (ImageView) findViewById(R.id.iv_guide_point_2);
        iv_guide_point_3 = (ImageView) findViewById(R.id.iv_guide_point_3);

        iv_music_switch.setOnClickListener(this);
        tv_guide_skip.setOnClickListener(this);

        view1 = View.inflate(this, R.layout.layout_pager_guide_1, null);
        view2 = View.inflate(this, R.layout.layout_pager_guide_2, null);
        view3 = View.inflate(this, R.layout.layout_pager_guide_3, null);

        mPageList.add(view1);
        mPageList.add(view2);
        mPageList.add(view3);

        //设置 缓存 预加载
        mViewPager.setOffscreenPageLimit(mPageList.size());

        mPageAdapter = new BasePageAdapter(mPageList);
        mViewPager.setAdapter(mPageAdapter);

        //帧动画
        iv_guide_star = view1.findViewById(R.id.iv_guide_star);
        iv_guide_night = view2.findViewById(R.id.iv_guide_night);
        iv_guide_smile = view3.findViewById(R.id.iv_guide_smile);

        //播放帧动画
        AnimationDrawable animStar = (AnimationDrawable) iv_guide_star.getBackground();
        animStar.start();

        AnimationDrawable animNight = (AnimationDrawable) iv_guide_night.getBackground();
        animNight.start();

        AnimationDrawable animSmile = (AnimationDrawable) iv_guide_smile.getBackground();
        animSmile.start();

        //ViewPage小圆点逻辑  监听ViewPage 滑动事件
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //播放歌曲逻辑
        startMusic();
    }

    //播放音乐
    private void startMusic() {
        mGuideMusic = new MediaPlayerManager();
        mGuideMusic.setLooping(true);
        AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.guide);
        mGuideMusic.startPlay(file);


        //旋转动画
        mAnim = AnimUtils.rotation(iv_music_switch);
        mAnim.start();
    }

    //动态选择小圆点
    private void selectPoint(int position) {
        switch (position) {
            case 0:
                iv_guide_point_1.setImageResource(R.drawable.img_guide_point_p);
                iv_guide_point_2.setImageResource(R.drawable.img_guide_point);
                iv_guide_point_3.setImageResource(R.drawable.img_guide_point);
                break;
            case 1:
                iv_guide_point_1.setImageResource(R.drawable.img_guide_point);
                iv_guide_point_2.setImageResource(R.drawable.img_guide_point_p);
                iv_guide_point_3.setImageResource(R.drawable.img_guide_point);
                break;
            case 2:
                iv_guide_point_1.setImageResource(R.drawable.img_guide_point);
                iv_guide_point_2.setImageResource(R.drawable.img_guide_point);
                iv_guide_point_3.setImageResource(R.drawable.img_guide_point_p);
            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_music_switch:
                if(mGuideMusic.MEDIA_STATUS == MediaPlayerManager.MEDIA_STATUS_PAUSE){
                    mAnim.start();
                    mGuideMusic.continuePlay();
                    iv_music_switch.setImageResource(R.drawable.img_guide_music);
                }else if(mGuideMusic.MEDIA_STATUS == MediaPlayerManager.MEDIA_STATUS_PLAY){
                    mAnim.pause();
                    mGuideMusic.pausePlay();
                    iv_music_switch.setImageResource(R.drawable.img_guide_music_off);
                }
                break;

            case R.id.tv_guide_skip:
                 startActivity(new Intent(this,LoginActivity.class));
                 finish();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGuideMusic.stopPlay();
    }
}
