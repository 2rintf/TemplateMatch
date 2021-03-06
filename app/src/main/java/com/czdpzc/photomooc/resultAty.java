package com.czdpzc.photomooc;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**匹配结果
 * Created by 2b on 2018/3/31.
 */

public class resultAty extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultaty_layout);

        String path = getIntent().getStringExtra("picPath");
        ImageView imageView =(ImageView) findViewById(R.id.pic);

//        Log.d("fuck bitmap path",path);
////        ImageView imageView =(ImageView) findViewById(R.id.pic);
//        Bitmap bitmap = BitmapFactory.decodeFile(path);
//        Bitmap scaled = Bitmap.createScaledBitmap(bitmap,800 , 600, true);
//
//
////        imageView.setImageBitmap(bitmap);
//
//        Matrix matrix = new Matrix();
//        matrix.setRotate(90);//旋转90度矩阵
//        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        imageView.setImageBitmap(bitmap);

        FileInputStream fis = null;
                try {
                    fis = new FileInputStream(path);
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);//把文件流解析成bitmap
//                    Bitmap scaled = Bitmap.createScaledBitmap(bitmap,1280 , 720, true);

//                    Matrix matrix = new Matrix();
//                    matrix.setRotate(90);//旋转90度矩阵
//                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    /**
                     * 根据取景框大小，对原拍摄图片进行剪裁。
                     * 坐标值需要修正。
                     */
                    Rect rect = new Rect(220,450,440,600);
                    Bitmap cutImg = Bitmap.createBitmap(bitmap,870,1290,600,450);


                    /**
                     * 显示由取景框裁剪的图片
                     */
                    imageView.setImageBitmap(cutImg);

//                    imageView.setImageBitmap(scaled);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
    }
}
