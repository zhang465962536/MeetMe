package com.example.framework.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

//头部的拉伸View
public class HeadZoomScrollView extends ScrollView {
    //头部View
    private View mZoomView;
    private int mZoomViewWidth;
    private int mZoomViewHeight;

    //是否在滑动
    private boolean isScrolling = false;
    //第一次按下的坐标
    private float firstPosition;
    //滑动系数
    private float mScrollRate = 0.3f;
    //回弹系数
    private float mReplyRate = 0.5f;

    public HeadZoomScrollView(Context context) {
        super(context);
    }

    public HeadZoomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadZoomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 1.获取头部的View(操控的View)
     * 2.滑动事件的处理
     */
    //控件加载完成之后 获取缩放View
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildAt(0) != null) {
            ViewGroup vg = (ViewGroup) getChildAt(0);
            if (vg.getChildAt(0) != null) {
                mZoomView = vg.getChildAt(0);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //获取View的宽高
        if (mZoomViewWidth <= 0 || mZoomViewHeight <= 0) {
            mZoomViewWidth = mZoomView.getMeasuredWidth();
            mZoomViewHeight = mZoomView.getMeasuredHeight();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (!isScrolling) {
                    //如果没有滑动
                    if (getScrollY() == 0) {
                        //没有滑动 即第一次滑动 记录 按下的坐标
                        firstPosition = ev.getY();
                    } else {
                        break;
                    }
                }

                //计算缩放值 滑动距离 = 当前的位置 - 第一次按下的位置
                //公式：(当前的位置 - 第一次按下的位置) * 缩放系数
                int distance = (int) ((ev.getY() - firstPosition) * mScrollRate);
                if (distance < 0) {
                    break;
                }
                isScrolling = true;
                setZoomView(distance);
                break;

            case MotionEvent.ACTION_UP:
                isScrolling = false;
                //回弹
                replayZoomView();
                break;
        }

        return true;

    }

    //回弹动画
    private void replayZoomView() {
        //计算下拉的缩放值再让属性动画根据这个值复原
        int distance = mZoomView.getMeasuredWidth() - mZoomViewWidth;
        //属性动画回弹
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(distance,0).setDuration((long) (distance * mReplyRate));
        //属性动画监听
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setZoomView((Float) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    //缩放View
    private void setZoomView(float zoom) {
        if (mZoomViewWidth <= 0 || mZoomViewHeight <= 0) {
            return;
        }

        ViewGroup.LayoutParams lp = mZoomView.getLayoutParams();
        //原来的宽加上缩放值
        lp.width = (int) (mZoomViewWidth + zoom);
        //现在的宽 / 原来的宽 得到 缩放比例  * 原本的高 得到缩放的高
        lp.height = (int) (mZoomViewHeight * ((mZoomViewWidth + zoom) / mZoomViewWidth));


        //设置间距 否则会向右下偏移
        //公式 间距 现在的宽(lp.width) - 原本的宽(mZoomViewWidth) / 2 得到的值是一个负数
        ((MarginLayoutParams) lp).setMargins((lp.width - mZoomViewWidth) / 2, 0, 0, 0);
        mZoomView.setLayoutParams(lp);


    }
}
