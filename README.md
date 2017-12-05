# BannerViewDemo
### android bannerView  广告条 无限轮播
![image](https://github.com/gypnick/BannerViewDemo/blob/master/bannerView.gif)
## 1.导入到工程
####    Step 1.Add it in your root build.gradle at the end of repositories:
  ```Java
  allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```
####   Step 2. Add the dependency
  ```Java
  dependencies {
	         compile 'com.github.gypnick:BannerViewDemo:V1.0'
	}
  ```
##  2.使用
####
	<site.site8.bannerview.BannerView
 	   android:id="@+id/bannerView"
    	   android:layout_width="match_parent"
    	   android:layout_height="200dp">
	</site.site8.bannerview.BannerView>

####
             bannerView
                     .setLoopTime(1000)//设置轮播间隔时间，默认2000毫秒
                     .setGravityPoint(BannerView.BOTTOM_RIGHT)//设置轮播点的位置，默认在中间
                   //.setPointImg()//设置轮播的点的图片
                     //设置点击回掉
                     .setBannerOnClickListener(new BannerView.BannerOnClickListener() {
                         @Override
                         public void onClick(int position) {
                             Toast.makeText(MainActivity.this, "点击了第"+position+"个banner", Toast.LENGTH_SHORT).show();
                         }
                     })
                     .create(images,R.mipmap.defaults);  //设置图片资源和默认图片

  
  
  
