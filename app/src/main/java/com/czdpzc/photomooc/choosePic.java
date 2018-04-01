package com.czdpzc.photomooc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileNotFoundException;

/**
 * Created by 2b on 2018/4/1.
 */

public class choosePic extends Activity{

    private static final int REQUEST_CODE_PICK_IMAGE=1;
    private ImageView mImageview;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_layout);
        mImageview = (ImageView) findViewById(R.id.imv);


        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK){
                    try {
                        Uri uri = data.getData();
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        mImageview.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else
                    Log.d("fuck","获取照片失败");

                break;

                default:
                    break;
        }
    }
}
