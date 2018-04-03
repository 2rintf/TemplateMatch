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
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.Buffer;

/**
 * Warning : OpenCv的导入配置和启动还没有完成
 *
 *
 * Created by 2b on 2018/4/2.
 */

public class testActivity extends Activity {

//    static{ System.loadLibrary("openCV"); }

    private ImageView mImageview;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
            Log.d("fuck","OpenCv initialization failed");
//            System.loadLibrary("jniLibs");
        }
    }


//    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
//        @Override
//        public void onManagerConnected(int status) {
//            switch (status) {
//                case LoaderCallbackInterface.SUCCESS:{
//                } break;
//                default:{
//                    super.onManagerConnected(status);
//                } break;
//            }
//        }
//    };
//    @Override
//    public void onResume(){
//        super.onResume();
//        //通过OpenCV引擎服务加载并初始化OpenCV类库，所谓OpenCV引擎服务即是
//        //OpenCV_2.4.3.2_Manager_2.4_*.apk程序包，存在于OpenCV安装包的apk目录中
//        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, this, mLoaderCallback);
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        OpenCVLoader.initDebug();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_layout);

        OpenCVLoader.initDebug();
        mImageview = (ImageView) findViewById(R.id.testPic);

        Mat i1 = new Mat();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.photo_img_btn);


        Utils.bitmapToMat(bitmap,i1);



//        i1 = Imgcodecs.imread("/res/drawable/photo_img_btn.jpg");

        Imgproc.cvtColor(i1,i1,Imgproc.COLOR_RGB2GRAY);

        Utils.matToBitmap(i1,bitmap);

//        Bitmap bitmap = BitmapFactory.decodeStream(getClass().getResourceAsStream("src/Sample_czd/match_area/1.jpg"));
//        Bitmap bitmap = BitmapFactory.decodeStream(getClass().getResourceAsStream("res/drawable/photo_img_btn.png"));
//        Drawable drawable = getResources().getDrawable(R.drawable.photo_img_btn);
//        BitmapDrawable bmpDraw = (BitmapDrawable) drawable;
//        Bitmap bmp = bmpDraw .getBitmap();
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.s1);
//        Bitmap bmp = Bitmap.createBitmap(i1.width(),i1.height(),Bitmap.Config.RGB_565);


        mImageview.setImageBitmap(bitmap);

    }
}
