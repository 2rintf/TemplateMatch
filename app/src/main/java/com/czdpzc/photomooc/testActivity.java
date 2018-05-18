package com.czdpzc.photomooc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.czdpzc.match.*;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        TextView textView = (TextView) findViewById(R.id.text2show);
        mImageview = (ImageView) findViewById(R.id.testPic);
        Context context = this.getApplicationContext();



        InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        FileInputStream fis = null;
        Mat targetImg = new Mat();
        Mat imgDived = new Mat();
        char[] matchedChar = new char[4];
        String path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()+"/TemplateMatch/div_pic";
        String path2;
        char firstNum;
        Point bestLoc = new Point();


        is = context.getClassLoader().getResourceAsStream("assets/Sample_czd/match_area/"+"8.jpg");
        Bitmap bitmap1 = BitmapFactory.decodeStream(is);
        Utils.bitmapToMat(bitmap1,targetImg);

//        Imgproc.cvtColor(targetImg,targetImg,Imgproc.COLOR_RGB2GRAY);
//        Bitmap bitmap2 = Bitmap.createBitmap(bitmap1.getWidth(),bitmap1.getHeight(),Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(targetImg,bitmap2);

        Log.d("fuck","--------------");
        Log.d("fuck","开始匹配");
        Log.d("fuck","--------------");
        long startTime = System.nanoTime();

        firstGPS fg = new firstGPS(targetImg,context);
        firstNum = fg.select2match();
        bestLoc = fg.getBestLoc();
        Mat showImg = fg.firstGPSCut(bestLoc);



        targetImageDiv divImg = new targetImageDiv(showImg);
        divImg.getOne();//分割
        //不匹配第一个数字，直接从第二个字符开始
        for (int j=2; j<=4; j++){
            path2 = path1 +"/"+ j+ ".jpg";
            try {
                fis = new FileInputStream(path2);
                Bitmap bitmap2 = BitmapFactory.decodeStream(fis);
                Utils.bitmapToMat(bitmap2,imgDived);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("fuck","匹配过程读取分割后的字符图片出错");
            }

            selectTemp2Match selectTemp2Match = new selectTemp2Match(imgDived,context);
            matchedChar[j-1] = selectTemp2Match.select2match();
        }

        matchedChar[0] = firstNum;
        textView.setText(matchedChar,0,4);
        long consumingTime = System.nanoTime()-startTime;
        Log.d("fuck",consumingTime/1000000+"ms");

        mImageview.setImageBitmap(bitmap1);








//        String path = "/Sample_czd/temp";
//        File file = new File(path);
//        try {
//            //一定要有context上下文，不然读不出文件夹数量
//            String[] strArray= context.getAssets().list("Sample_czd/temp");
//            int num = strArray.length;
//            Log.d("fuck","模板种类数量："+num);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }




//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.photo_img_btn);
//        Utils.bitmapToMat(bitmap,i1);
//        i1 = Imgcodecs.imread("/res/drawable/photo_img_btn.jpg");
//        Imgproc.cvtColor(i1,i1,Imgproc.COLOR_RGB2GRAY);
//        Utils.matToBitmap(i1,bitmap);

//        Bitmap bitmap = BitmapFactory.decodeStream(getClass().getResourceAsStream("src/Sample_czd/match_area/1.jpg"));
//        Bitmap bitmap = BitmapFactory.decodeStream(getClass().getResourceAsStream("res/drawable/photo_img_btn.png"));
//        Drawable drawable = getResources().getDrawable(R.drawable.photo_img_btn);
//        BitmapDrawable bmpDraw = (BitmapDrawable) drawable;
//        Bitmap bmp = bmpDraw .getBitmap();
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.s1);
//        Bitmap bmp = Bitmap.createBitmap(i1.width(),i1.height(),Bitmap.Config.RGB_565);


//        mImageview.setImageBitmap(bitmap2);

    }
}
