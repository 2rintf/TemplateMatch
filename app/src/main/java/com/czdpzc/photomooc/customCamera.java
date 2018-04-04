package com.czdpzc.photomooc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.czdpzc.photomooc.DrawImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;
import java.util.Locale;

/**
 * Created by 2b on 2018/3/31.
 */

public class customCamera extends Activity implements SurfaceHolder.Callback{

    private Camera mCamera;
    private SurfaceView mPreview;
    private DrawImageView mDrawView;
    private SurfaceHolder mHolder;
    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
//            File tempFile = new File("/sdcard/temp.png");
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());//格式化时间戳
            String rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
            Log.d("fuck rootpath",rootPath);
            File tempFile = new File(rootPath + File.separator  + "123.jpg");

            Bitmap testBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            Matrix matrix = new Matrix();
            matrix.postRotate((float)90.0);
            Bitmap rotaBitmap = Bitmap.createBitmap(testBitmap, 0, 0, testBitmap.getWidth(), testBitmap.getHeight(), matrix, false);
            if(null != rotaBitmap)
            {
                saveJpeg(rotaBitmap,rootPath+"/");
            }

            Intent intent = new Intent(customCamera.this,resultAty.class);
            intent.putExtra("picPath",tempFile.getAbsolutePath());
            startActivity(intent);
            finish();



//            try {
//                FileOutputStream fos = new FileOutputStream(tempFile);
//                try {
//                    fos.write(bytes);
//                    fos.close();
//                    Intent intent = new Intent(customCamera.this,resultAty.class);
//                    Log.d("fuck tempFile Path:",tempFile.getAbsolutePath());
//                    intent.putExtra("picPath",tempFile.getAbsolutePath());
//
//                    startActivity(intent);
////                    customCamera.this.finish();
//                    finish();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
        }
    };

    public void saveJpeg(Bitmap bm,String path){
        String savePath = path;
        File folder = new File(savePath);
        if(!folder.exists()) //如果文件夹不存在则创建
        {
            folder.mkdir();
        }
//        long dataTake = System.currentTimeMillis();
        long dataTake = 123;
        String jpegName = savePath + dataTake +".jpg";
//        Log.i(tag, "saveJpeg:jpegName--" + jpegName);
        //File jpegFile = new File(jpegName);
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);

            //			//如果需要改变大小(默认的是宽960×高1280),如改成宽600×高800
            //			Bitmap newBM = bm.createScaledBitmap(bm, 600, 800, false);

            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
//            Log.i(tag, "saveJpeg：存储完毕！");
        } catch (IOException e) {
//            Log.i(tag, "saveJpeg:存储失败！");
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_layout);

        mDrawView = (com.czdpzc.photomooc.DrawImageView) findViewById(R.id.drawview);
        mDrawView.onDraw(new Canvas());

        mPreview = (SurfaceView) findViewById(R.id.preview);
        mHolder = mPreview.getHolder();
        mHolder.addCallback(this);

        //增加点击屏幕自动对焦的功能
        mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(null);
            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera==null){
            mCamera = getCamera();
            if (mHolder!=null){
                //开始将camera和surfaceview绑定，实现预览的效果
                setStartPreview(mCamera,mHolder);
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();

    }



    public void capture(View view){
        //相机参数设置
//        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Camera.Parameters parameters = mCamera.getParameters();
//        Display display = wm.getDefaultDisplay();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);//闪光灯自动

//        parameters.setJpegQuality(85);
//        parameters.setPreviewSize(display.getWidth(),display.getHeight());

        List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
          for(int i=0; i<pictureSizes.size(); i++){
              Camera.Size size = pictureSizes.get(i);
              Log.i("fuck", "initCamera:摄像头支持的pictureSizes: width = "+size.width+"height = "+size.height);
          }
          for(int i=0; i<previewSizes.size(); i++){
              Camera.Size size = previewSizes.get(i);
              Log.i("fuck", "initCamera:摄像头支持的previewSizes: width = "+size.width+"height = "+size.height);

          }

        parameters.setPictureSize(3264,2448);
//        parameters.setPreviewSize(1280,720);


        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);//自动对焦

        mCamera.setParameters(parameters);

        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean b, Camera camera) {
                if (b){
                    mCamera.takePicture(null,null,mPictureCallback);
                }
            }
        });

    }

    /**
     * 获取系统的Camera对象
     * Camera.open()
     * @return
     */
    private Camera getCamera(){
        Camera camera;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            camera = null;
            e.printStackTrace();
        }
        return camera;

    }

    /**
     * 调用此函数来预览相机内容
     * 将camera对象与Surface对象绑定
     */
    private void setStartPreview(Camera camera,SurfaceHolder surfaceHolder){
        try {
            camera.setPreviewDisplay(surfaceHolder);
            //系统默认surface是横屏的,所以90度修改一下
            camera.setDisplayOrientation(90);
            camera.startPreview();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 因为相机资源独享，所以需要释放相机资源和surfaceHolder
     * 在销毁时调用此函数 onDestroy
     */
    private void releaseCamera(){
        if (mCamera!=null) {
            mCamera.setPreviewCallback(null);//将callback置空，取消camera和surfaceView的关联
            mCamera.stopPreview();//停止相机取景
            mCamera.release();
            mCamera = null;//camera对象置空
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        setStartPreview(mCamera,mHolder);

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        //changed时，需要重启camera，先stop，再重新setStartPreview
        mCamera.stopPreview();
        setStartPreview(mCamera,mHolder);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();

    }
}