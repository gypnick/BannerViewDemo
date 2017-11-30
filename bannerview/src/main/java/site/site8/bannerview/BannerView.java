package site.site8.bannerview;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by g on 2016/10/20.
 */

public class BannerView extends FrameLayout implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private BannerAdapter adapter;
    private Handler handler;
    private LinearLayout points;
    private List<ImageView> tips;
    private Runnable runnable;
    //设置指示点的位置,
    public static final int BOTTOM_LEFT = Gravity.LEFT;
    public static final int BOTTOM_CENTER = Gravity.CENTER;
    public static final int BOTTOM_RIGHT = Gravity.RIGHT;


    /**
     * 可设置的参数
     */
    //数据源
    private ArrayList<?> dataList;
    //默认轮播时间
    private int loopTime = 2000;



    //是否正向循环
    private boolean isPositive = true;
    //设置指示点的位置
    private int gravityPoint = BOTTOM_CENTER;
    //轮播点的图片id
    private int pointsFocused = R.drawable.page_indicator_focused;
    private int pointsUnFocused = R.drawable.page_indicator_unfocused;
    //默认图片
    private int defaultPic = 0;
    //点击banner回掉
    private BannerOnClickListener bannerOnClickListener;
    //设置是否无限轮询
    private boolean isLoop = false;


    public BannerView(Context context) {
        super(context);
        init(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewPager = new ViewPager(context);
        viewPager.setLayoutParams(params);
        viewPager.addOnPageChangeListener(this);

        addView(viewPager);
        handler = new Handler();

        points = new LinearLayout(context);


    }

    /**
     * 设置轮播点
     *
     * @param number
     */
    private void setPoint(int number) {
        //设置points的LayoutParams
        LinearLayout.LayoutParams pLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        points.setLayoutParams(pLayoutParams);
        points.setOrientation(LinearLayout.HORIZONTAL);
        //这里很重要 设置  layout_gravity  相对于父布局的位置
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(points.getLayoutParams());
        layoutParams.gravity = (Gravity.BOTTOM | gravityPoint);  //设置轮播点靠右下角展示
        layoutParams.bottomMargin = dip2px(getContext(), 10);
        layoutParams.rightMargin = dip2px(getContext(), 10);
        points.setLayoutParams(layoutParams);
        addView(points);
        for (int i = 0; i < number; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(dip2px(getContext(), 10), dip2px(getContext(), 10)));
            tips.add(imageView);
            if (i == 0) {
                tips.get(i).setBackgroundResource(pointsFocused);
            } else {
                tips.get(i).setBackgroundResource(pointsUnFocused);
            }
            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            mLayoutParams.leftMargin = dip2px(getContext(), 2);//轮播点左右间距
            mLayoutParams.rightMargin = dip2px(getContext(), 2);//轮播点左右间距
            points.addView(imageView, mLayoutParams);
        }


    }


    //设置banner点击监听
    public interface BannerOnClickListener {
        void onClick(int position);
    }

    private BannerView autoLoop(final int time) {

        runnable = new Runnable() {
            @Override
            public void run() {
                int item = viewPager.getCurrentItem();

                if (item >= adapter.getCount() - 1) {  //判断循环到哪里了
                    isPositive = false;
                } else if (item <= 0) {
                    isPositive = true;
                }
                if (isPositive) {
                    item++;
                } else if (!isPositive) {
                    item--;
                }
                viewPager.setCurrentItem(item);
                handler.postDelayed(this, time);
            }
        };
        handler.postDelayed(runnable, time);
        return this;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        position = position % dataList.size();
        for (int i = 0; i < tips.size(); i++) {
            if (i == position) {
                tips.get(i).setBackgroundResource(pointsFocused);
            } else {
                tips.get(i).setBackgroundResource(pointsUnFocused);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class BannerAdapter extends PagerAdapter {
        private ArrayList<?> list;

        public BannerAdapter(ArrayList<?> url) {
            list = url;
        }

        @Override
        public int getCount() {
            if (!isLoop) return list.size();//不循环

            if (list.size() == 1) {  //循环
                return 1;
            } else {
                return 500 * list.size();
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % dataList.size();
            ImageView imageView = new ImageView(getContext());
            imageView.setBackground(getResources().getDrawable(defaultPic));
            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //此处用Glide加载图片，如果用的其他的，可以再这里更换
            Glide.with(getContext()).load(list.get(position)).error(defaultPic).centerCrop().into(imageView);//Glide加载图片
            container.addView(imageView);
            final int finalPosition = position;
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bannerOnClickListener != null)
                        bannerOnClickListener.onClick(finalPosition);//点击事件的回调
                }
            });
            imageView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        handler.removeCallbacks(runnable);  //手指触碰banner条后，停止循环
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        handler.removeCallbacks(runnable);
                        handler.postDelayed(runnable, 2000); //手指抬起后2秒后继续循环
                    }
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        handler.removeCallbacks(runnable);
                        handler.postDelayed(runnable, 2000); //手指抬起后2秒后继续循环
                    }
                    return false;
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
//========================================对外暴露方法==================================================

    /**
     * 移除掉handler
     */
    public void removeHandler() {
        handler.removeCallbacks(runnable);
        handler = null;
    }

    //设置banner点击监听
    public BannerView setBannerOnClickListener(BannerOnClickListener bannerOnClickListener) {
        this.bannerOnClickListener = bannerOnClickListener;
        return this;
    }


    /**
     *
     * @param list  图片数据，可以为本地图片，可以为网络图片
     * @param defaultPic 默认图片
     */
    public void create(ArrayList<?> list, int defaultPic) {
        if (list == null) return;
        dataList = list;
        this.defaultPic = defaultPic;//默认图片
        adapter = new BannerAdapter(list);
        viewPager.setAdapter(adapter);
        //设置指示点
        tips = new ArrayList<>();
        if (list.size() > 1) {
            setPoint(list.size());
        }
        //轮询时间小于100毫秒就不进行轮播
        if (loopTime >= 100) autoLoop(loopTime);

    }

    /**
     * 设置指示点的图片id
     *
     * @param pointsUnFocused
     * @param pointsFocused
     * @return
     */
    public BannerView setPointImg(int pointsUnFocused, int pointsFocused) {
        this.pointsUnFocused = pointsUnFocused;
        this.pointsFocused = pointsFocused;
        return this;
    }

    /**
     * 设置指示点的位置
     * @param gravityPoint
     * @return
     */
    public BannerView setGravityPoint(int gravityPoint) {
        switch (gravityPoint) {
            case BOTTOM_CENTER:
                this.gravityPoint = gravityPoint;
                break;
            case BOTTOM_LEFT:
                this.gravityPoint = gravityPoint;
                break;
            case BOTTOM_RIGHT:
                this.gravityPoint = gravityPoint;
                break;
            default:
                this.gravityPoint = BOTTOM_CENTER;
                break;
        }
        return this;
    }

    /**
     * 设置轮播间隔时间
     * @param loopTime   轮询时间小于100毫秒就不进行轮播
     * @return
     */
    public BannerView setLoopTime(int loopTime){
        this.loopTime=loopTime;
        return this;
    }

}
