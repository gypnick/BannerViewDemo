package site.site8.bannerviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import site.site8.bannerview.BannerView;

public class MainActivity extends AppCompatActivity {

    private BannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerView = (BannerView) findViewById(R.id.bannerView);
        ArrayList<Integer> images=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            images.add(R.drawable.wsx);
        }
        bannerView
                .setLoopTime(1000)//设置轮播间隔时间，默认2000毫秒
                .setGravityPoint(BannerView.BOTTOM_RIGHT)//设置轮播点的位置，默认在中间
//                .setPointImg()//设置轮播的点的图片
                //设置点击回掉
                .setBannerOnClickListener(new BannerView.BannerOnClickListener() {
                    @Override
                    public void onClick(int position) {
                        Toast.makeText(MainActivity.this, "点击了第"+position+"个banner", Toast.LENGTH_SHORT).show();
                    }
                })
                .create(images,R.mipmap.defaults);  //设置图片资源和默认图片
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bannerView.removeHandler();//移除掉handler的Callbacks
    }
}
