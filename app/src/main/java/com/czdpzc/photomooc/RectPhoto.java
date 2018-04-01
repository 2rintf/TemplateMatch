package com.czdpzc.photomooc;

/**
 * Created by 2b on 2018/4/1.
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class RectPhoto extends Activity implements SurfaceHolder.Callback{
    private static final String tag="yan";
    private boolean isPreview = false;
    private SurfaceView mPreviewSV = null; //预览SurfaceView
    private SurfaceHolder mySurfaceHolder = null;
    private ImageButton mPhotoImgBtn = null;
    private Camera myCamera = null;
    private Bitmap mBitmap = null;
    private AutoFocusCallback myAutoFocusCallback = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window myWindow = this.getWindow();
        myWindow.setFlags(flag, flag);

        setContentView(R.layout.activity_rect_photo);

        //初始化SurfaceView
        mPreviewSV = (SurfaceView)findViewById(R.id.previewSV);
        mySurfaceHolder = mPreviewSV.getHolder();
        mySurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);//translucent半透明 transparent透明
        mySurfaceHolder.addCallback(this);
        mySurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //自动聚焦变量回调
        myAutoFocusCallback = new AutoFocusCallback() {

            public void onAutoFocus(boolean success, Camera camera) {
                // TODO Auto-generated method stub
                if(success)//success表示对焦成功
                {
                    Log.i(tag, "myAutoFocusCallback: success...");
                    //myCamera.setOneShotPreviewCallback(null);

                }
                else
                {
                    //未对焦成功
                    Log.i(tag, "myAutoFocusCallback: 失败了...");

                }


            }
        };

        mPhotoImgBtn = (ImageButton)findViewById(R.id.photoImgBtn);
        //手动设置拍照ImageButton的大小为120×120,原图片大小是64×64
        LayoutParams lp = mPhotoImgBtn.getLayoutParams();
        lp.width = 120;
        lp.height = 120;
        mPhotoImgBtn.setLayoutParams(lp);
        mPhotoImgBtn.setOnClickListener(new PhotoOnClickListener());
        mPhotoImgBtn.setOnTouchListener(new MyOnTouchListener());


    }


    /*下面三个是SurfaceHolder.Callback创建的回调函数*/
    public void surfaceChanged(SurfaceHolder holder, int format, int width,int height)
    // 当SurfaceView/预览界面的格式和大小发生改变时，该方法被调用
    {

        Log.i(tag, "SurfaceHolder.Callback:surfaceChanged!");
        initCamera();

    }


    public void surfaceCreated(SurfaceHolder holder)
    // SurfaceView启动时/初次实例化，预览界面被创建时，该方法被调用。
    {

        myCamera = Camera.open();
        try {
            myCamera.setPreviewDisplay(mySurfaceHolder);
            Log.i(tag, "SurfaceHolder.Callback: surfaceCreated!");
        } catch (IOException e) {

            if(null != myCamera){
                myCamera.release();
                myCamera = null;
            }
            e.printStackTrace();
        }



    }


    public void surfaceDestroyed(SurfaceHolder holder)
    //销毁时被调用
    {

        Log.i(tag, "SurfaceHolder.Callback：Surface Destroyed");
        if(null != myCamera)
        {
            myCamera.setPreviewCallback(null); /*在启动PreviewCallback时这个必须在前不然退出出错。
			这里实际上注释掉也没关系*/

            myCamera.stopPreview();
            isPreview = false;
            myCamera.release();
            myCamera = null;
        }

    }

    //初始化相机
    public void initCamera(){
        if(isPreview){
            myCamera.stopPreview();
        }
        if(null != myCamera){
            Camera.Parameters myParam = myCamera.getParameters();
            //			//查询屏幕的宽和高
            //			WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
            //			Display display = wm.getDefaultDisplay();
            //			Log.i(tag, "屏幕宽度："+display.getWidth()+" 屏幕高度:"+display.getHeight());

            myParam.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式

            //			//查询camera支持的picturesize和previewsize
            //			List<Size> pictureSizes = myParam.getSupportedPictureSizes();
            //			List<Size> previewSizes = myParam.getSupportedPreviewSizes();
            //			for(int i=0; i<pictureSizes.size(); i++){
            //				Size size = pictureSizes.get(i);
            //				Log.i(tag, "initCamera:摄像头支持的pictureSizes: width = "+size.width+"height = "+size.height);
            //			}
            //			for(int i=0; i<previewSizes.size(); i++){
            //				Size size = previewSizes.get(i);
            //				Log.i(tag, "initCamera:摄像头支持的previewSizes: width = "+size.width+"height = "+size.height);
            //
            //			}


            //设置大小和方向等参数
            myParam.setPictureSize(4128,3096);
            myParam.setPreviewSize(1280,720);
            //myParam.set("rotation", 90);
            myCamera.setDisplayOrientation(90);
            myParam.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            myCamera.setParameters(myParam);
            myCamera.startPreview();
            myCamera.autoFocus(myAutoFocusCallback);
            isPreview = true;
        }
    }

    /*为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量*/
    ShutterCallback myShutterCallback = new ShutterCallback()
            //快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
    {

        public void onShutter() {
            Log.i(tag, "myShutterCallback:onShutter...");

        }
    };
    PictureCallback myRawCallback = new PictureCallback()
            // 拍摄的未压缩原数据的回调,可以为null
    {

        public void onPictureTaken(byte[] data, Camera camera) {
            Log.i(tag, "myRawCallback:onPictureTaken...");

        }
    };
    PictureCallback myJpegCallback = new PictureCallback()
            //对jpeg图像数据的回调,最重要的一个回调
    {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.i(tag, "myJpegCallback:onPictureTaken...");
            if(null != data){
                mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
                myCamera.stopPreview();
                isPreview = false;
            }
            //设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。图片竟然不能旋转了，故这里要旋转下
            Matrix matrix = new Matrix();
            matrix.postRotate((float)90.0);
            Bitmap rotaBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, false);
            //保存图片到sdcard
            if(null != rotaBitmap)
            {
                saveJpeg(rotaBitmap);
            }

            //再次进入预览
            myCamera.startPreview();
            isPreview = true;
        }
    };
    //拍照按键的监听
    public class PhotoOnClickListener implements OnClickListener{

        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(isPreview && myCamera!=null){
                myCamera.takePicture(myShutterCallback, null, myJpegCallback);
            }

        }

    }
    /*给定一个Bitmap，进行保存*/
    public void saveJpeg(Bitmap bm){
        String savePath = "/mnt/sdcard/rectPhoto/";
        File folder = new File(savePath);
        if(!folder.exists()) //如果文件夹不存在则创建
        {
            folder.mkdir();
        }
        long dataTake = System.currentTimeMillis();
        String jpegName = savePath + dataTake +".jpg";
        Log.i(tag, "saveJpeg:jpegName--" + jpegName);
        //File jpegFile = new File(jpegName);
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);

            //			//如果需要改变大小(默认的是宽960×高1280),如改成宽600×高800
            //			Bitmap newBM = bm.createScaledBitmap(bm, 600, 800, false);

            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Log.i(tag, "saveJpeg：存储完毕！");
        } catch (IOException e) {
            Log.i(tag, "saveJpeg:存储失败！");
            e.printStackTrace();
        }
    }

    /*为了使图片按钮按下和弹起状态不同，采用过滤颜色的方法.按下的时候让图片颜色变淡*/
    public class MyOnTouchListener implements OnTouchListener{

        public final  float[] BT_SELECTED=new float[]
                      { 2, 0, 0, 0, 2,
                        0, 2, 0, 0, 2,
                        0, 0, 2, 0, 2,
                        0, 0, 0, 1, 0 };

        public final float[] BT_NOT_SELECTED=new float[]
                      { 1, 0, 0, 0, 0,
                        0, 1, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 1, 0 };
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction() == MotionEvent.ACTION_DOWN){
                v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
                v.setBackgroundDrawable(v.getBackground());
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
                v.setBackgroundDrawable(v.getBackground());

            }
            return false;
        }

    }

    @Override
    public void onBackPressed()
    //无意中按返回键时要释放内存
    {
        super.onBackPressed();
        RectPhoto.this.finish();
    }
}
