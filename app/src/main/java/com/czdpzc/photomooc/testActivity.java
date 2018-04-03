package com.czdpzc.photomooc;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * Warning : OpenCv的导入配置和启动还没有完成
 *
 *
 * Created by 2b on 2018/4/2.
 */

public class testActivity extends Activity {

//    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private ImageView mImageview;

    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:{
                } break;
                default:{
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    @Override
    public void onResume(){
        super.onResume();
        //通过OpenCV引擎服务加载并初始化OpenCV类库，所谓OpenCV引擎服务即是
        //OpenCV_2.4.3.2_Manager_2.4_*.apk程序包，存在于OpenCV安装包的apk目录中
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, this, mLoaderCallback);
    }

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
            Log.d("fuck","OpenCv initialization failed");
            OpenCVLoader.initDebug();
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        OpenCVLoader.initDebug();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_layout);

        mImageview = (ImageView) findViewById(R.id.testPic);
        Mat i1 = new Mat();
        i1 = Imgcodecs.imread("./src/Sample_czd/match_area/1.jpg");

//        Bitmap bitmap = BitmapFactory.decodeStream(getClass().getResourceAsStream("src/Sample_czd/match_area/1.jpg"));
//        Bitmap bitmap = BitmapFactory.decodeStream(getClass().getResourceAsStream("res/drawable/photo_img_btn.png"));
//        Drawable drawable = getResources().getDrawable(R.drawable.photo_img_btn);
//        BitmapDrawable bmpDraw = (BitmapDrawable) drawable;
//        Bitmap bmp = bmpDraw .getBitmap();
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.s1);
        Bitmap bmp = Bitmap.createBitmap(i1.width(),i1.height(),Bitmap.Config.RGB_565);


        mImageview.setImageBitmap(bmp);

    }
}
