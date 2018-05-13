package com.czdpzc.photomooc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.FileNotFoundException;

/**
 * 相册选择图片
 * Created by 2b on 2018/4/1.
 */

public class choosePic extends Activity{

    private static final int REQUEST_CODE_PICK_IMAGE=1;
    private ImageView mImageview;
    private Uri uri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_layout);
//        mImageview = (ImageView) findViewById(R.id.imv);


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
                    uri = data.getData();
//                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//                        mImageview.setImageBitmap(bitmap);
                }
                else
                    Log.d("fuck","获取照片失败");

                break;

                default:
                    break;
        }
    }

    public void showImg(View view){
        Intent intent = new Intent(choosePic.this,showImgActivity.class);
        intent.putExtra("path",uri.toString());
        Log.d("path",uri.toString());
        startActivity(intent);
    }

    public void matchImg(View view){




    }

}
