package com.czdpzc.match;
//import com.sun.imageio.plugins.jpeg.JPEG;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.czdpzc.photomooc.R;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
//import org.opencv.highgui.Highgui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
import java.io.*;


import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;


public class targetImageDiv extends Activity{

//    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private  Mat targetImg = new Mat();
    static int startCol = 0;

    public  targetImageDiv(Mat targetImg){
        this.targetImg = targetImg;
    }

    public void saveJpeg(Bitmap bm,String path,int num){
        String savePath = path;
        File folder = new File(savePath);
        if(!folder.exists()) //如果文件夹不存在则创建
        {
            folder.mkdir();
        }
        int dataTake = num;
        String jpegName = savePath +"/"+ dataTake +".jpg";
//        Log.i("fuck", "saveJpeg:jpegName--" + jpegName);
        //File jpegFile = new File(jpegName);
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
//            Log.i("fuck", "saveJpeg：存储完毕！");
        } catch (IOException e) {
            Log.i("fuck", "saveJpeg:存储失败！");
            e.printStackTrace();
        }
    }


    public  int stringToInt(String string){
        int j = 0;
        /**
         * substring是开始索引，indexOf是查找这个字符第一次出现的位置
         * 通过string获得int
         */
        String str = string.substring(0, string.indexOf("."));//+ string.substring(string.indexOf(".") + 1
        int intgeo = Integer.parseInt(str);
        return intgeo;
    }

    /**
     * 获取每一列像素的灰度值求和
     * @param targetImg
     * @param col
     * @return
     */
    public  double getColSum(Mat targetImg,int col){
        int height = targetImg.rows();
        double[] help = new double[height-1];
        double[] all ;
        double sum = 0;
        //sum[0] = null;
        for (int i=0;i<height-1;i++){
            all =  targetImg.get(i+1,col);
            help[i] = all[0];
        }
        for (double s:help){
            sum += s;
        }
        return sum;
    }

    /**
     * 检测宽度
     * @param targetImg
     * @param Tsum
     * @param right
     * @return
     */
    public  int cutLeft(Mat targetImg,int Tsum,int right){
        int left = 0;
        int i;

        for (i=0;i<targetImg.cols()-1;i++){
            double colVal = getColSum(targetImg,i+1);

      //      System.out.println(colVal);

            if (colVal<Tsum){
                left = i;
//                System.out.println("left的值："+left);
            //    startCol = left;
                break;
            }
        }
        /**
         * 这里的除数是为了去掉那些小于四分之一的分割距离
         * 注意，这里从左边扫描停下来的位置开始
         */
        int roiWidth = targetImg.cols()/4;//默认分割长度为图片的四分之一宽度
        for (;i<targetImg.cols();i++){
            double colVal = getColSum(targetImg,i);
            if (colVal>Tsum){
                right = i;
                if ((right-left)<(targetImg.cols()/4)) {
                  //  System.out.println("预计截图宽度：" + (right - left));
                    continue;
                }
                else{
                    roiWidth = right-left;
                    break;
                }
            }
        }

        System.out.println("分割宽度："+roiWidth);
        return roiWidth;
    }

    /**
     * 分割、存储工作
     * @param roiWidth
     * @param targetImg
     */
    public  void cutImg(int roiWidth,Mat targetImg){
        int height = targetImg.rows();
        int width = roiWidth;

        for (int j=0;j<4;j++) {
            int a = j * width ;
            int b = 0;
            Rect rect = new Rect(a, b, width, height);
            Mat roiImg = new Mat(targetImg, rect);
            Mat tmpImg = new Mat();

            roiImg.copyTo(tmpImg);

            Bitmap bitmaphelper = Bitmap.createBitmap(roiWidth,roiImg.height(),Bitmap.Config.ARGB_8888);
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()+"/TemplateMatch/div_pic";
            Utils.matToBitmap(tmpImg,bitmaphelper);
            saveJpeg(bitmaphelper,path,(j+1));


//            imwrite(".\\src\\Sample_czd\\div_pic\\" + (j + 1) + ".jpg", tmpImg);
        }
    }
    /**
     * 图像预处理+分割总步骤
     * 此函数中可调节二值化阈值和灰度检测阈值
     *
     */
    public  void getOne(){
        Mat gImg = new Mat();
        Mat histImg = new Mat();
//        System.out.println(targetImg.channels());
        Imgproc.cvtColor(targetImg,gImg,Imgproc.COLOR_RGB2GRAY);
        Imgproc.equalizeHist(gImg,histImg);
        Imgproc.threshold(gImg,gImg,35,255,Imgproc.THRESH_BINARY);
//        imwrite("C:\\Users\\2b\\Desktop\\surprise.jpg",gImg);

        System.out.println("----------------------");
        System.out.println("目标图片分割开始");
        System.out.println("----------------------");
        int psum = 0;
        for (int i=0;i<gImg.cols();i++){
            psum+=getColSum(gImg,i);
        }
        System.out.println("灰度值总和："+psum);
        System.out.println("列数："+gImg.cols());

        /**
         * 将double类型先转成string再转成int，这样比较保险
         */
        double Tsum =  0.5*(psum/gImg.cols());
        String double2str =String.valueOf(Tsum);
        int Tsum_int = stringToInt(double2str);
        System.out.println("阈值："+Tsum_int);

        int roiWidth = cutLeft(gImg,Tsum_int,0);

        cutImg(roiWidth,targetImg);
        System.out.println("-----------------------");
        System.out.println("图片分割结束");
        System.out.println("-----------------------");
      //  return roiWidth;

    }

}
