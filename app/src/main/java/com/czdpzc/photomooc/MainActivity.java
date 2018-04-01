package com.czdpzc.photomooc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MainActivity extends Activity {

//    private static int REQ_1=1;
//    private static int REQ_2=2;
//    private ImageView mImageView;
//    private String mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!OpenCVLoader.initDebug()){
            //OpenCV is ok
        }

//        mImageView = (ImageView) findViewById(R.id.iv);
//        mFilePath = Environment.getExternalStorageDirectory().getPath();//获得SD卡路径
//        mFilePath = mFilePath + "/" + "temp.jpg";
    }

//    public void startCamera1(View view){
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent,REQ_1);
//    }

//    public void startCamera2(View view){
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Uri photouri = Uri.fromFile(new File(mFilePath));//将uri指向创建的文件对象temp.jpg
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, photouri);//将输出路径更改为指定的路径photouri
//        startActivityForResult(intent,REQ_2);
//    }

    public void customCamera(View view){
        startActivity(new Intent(this,customCamera.class));
    }

    public void choosePic(View view){
        startActivity(new Intent(this,choosePic.class));


    }

    public void rectPhoto(View view){
        startActivity(new Intent(this,RectPhoto.class));
    }


    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==RESULT_OK){
//            if(requestCode==REQ_1){
//                Bundle bundle = data.getExtras();
//                Bitmap bitmap = (Bitmap) bundle.get("data");
//                mImageView.setImageBitmap(bitmap);//只返回了缩略图，data中包含的只是缩略图
//            }
//            else if(requestCode==REQ_2){
//                FileInputStream fis = null;
//                try {
//                    fis = new FileInputStream(mFilePath);
//                    Bitmap bitmap = BitmapFactory.decodeStream(fis);//把文件流解析成bitmap
//                    mImageView.setImageBitmap(bitmap);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        fis.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
}