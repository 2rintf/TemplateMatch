package com.czdpzc.photomooc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import java.io.FileNotFoundException;

/**
 * 展示选择图片
 */


public class showImgActivity extends Activity {
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_img);
        mImageView = (ImageView) findViewById(R.id.imv);
        Bitmap bitmap;

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        Uri uri = Uri.parse(path);

//        bitmap = BitmapFactory.decodeFile();
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            mImageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



    }
}
