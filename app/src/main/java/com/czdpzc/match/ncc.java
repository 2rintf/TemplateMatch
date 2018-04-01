package com.czdpzc.match;


import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @input image1为目标图片，image2为模板图片
 * @output ncc计算后的最大值
 * @author czd
 *
 */

public class ncc {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private Mat image1 = new Mat();//目标图片
    private Mat image2 = new Mat();//模板图片

    public void getTarget(Mat image1){
        this.image1 = image1;
    }

    public void getTemp(Mat image2){
        this.image2 = image2;
    }

    /**
     * 将字符串写入.txt文件，测试用
     * @param str
     * @throws Exception
     */
    public static void  write2Txt(String str) throws Exception{
        FileWriter fw = null;
        String path = "C:\\Users\\2b\\Desktop\\match_log.txt";
        File f = new File(path);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            fw = new FileWriter(f);
            BufferedWriter out = new BufferedWriter(fw);
            // FileOutputStream fos = new FileOutputStream(f);
            // OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
            out.write(str.toString());
            out.close();
            System.out.println("===========写入文本成功========");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测图像是否已经灰度化，若没有进行灰度化
     */
    public void checkGrayOrNot(){
        int check1 = image1.channels();
        int check2 = image2.channels();

        if (check1==3){
            Imgproc.cvtColor(image1,image1,Imgproc.COLOR_RGB2GRAY);
        }

        if (check2==3){
            Imgproc.cvtColor(image2,image2,Imgproc.COLOR_RGB2GRAY);
        }

//        System.out.println("------------------------------");
//        System.out.println("....已保证图像灰度化");
//        System.out.println("------------------------------");
    }

    /**
     * 利用归一化相关系数进行匹配
     */
    public double nccProcess(){
        double bestValue;

        double r1 = image1.height();//列数
        double c1 = image1.width();//行数

        double r2 = image2.height();//列数
        double c2 = image2.width();//行数

        int result_rows=(int) (r1-r2+1);
        int result_cols=(int) (c1-c2+1);

        if (result_rows<=0 || result_cols<=0){
            System.out.println("-------------------------");
            System.out.println("跳过一张模板");
//            System.out.println("-------------------------");
            bestValue = -10;
        }
        else {
//
//            System.out.println("-------------------------------");
//            System.out.println("       NCC计算开始....");
//            System.out.println("-------------------------------");

            Mat g_result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
            //归一化相关系数数学方法实现
            Imgproc.matchTemplate(image1, image2, g_result,
                    Imgproc.TM_CCOEFF_NORMED);
            //对结果进行归一化
            //     Core.normalize(g_result,g_result,0,1,Core.NORM_MINMAX,-1,new Mat());

//        //将匹配结果矩阵写为.txt文件，测试用
//        try {
//            write2Txt(g_result.dump());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
            /**
             * g_result存储的是每一次匹配完的数值，
             * 当匹配结束后，这个矩阵中各个数值的位置，就是实际上每一个模板位于原图的像素坐标
             * 通过读取g_result矩阵中匹配值最大的坐标位置，就可以画出匹配的位置
             * 当然，这个矩阵的值就是NCC后的值
             */
            Point matchLocation = new Point();
            Core.MinMaxLocResult mmlr = Core.minMaxLoc(g_result);
            bestValue = mmlr.maxVal;//NCC最佳匹配值，用于return
//
//        System.out.println("--------------------------------");
//        System.out.println("获得NCC最佳匹配位置："+mmlr.maxLoc);
//        System.out.println("NCC最佳匹配值为："+mmlr.maxVal);
//        System.out.println("--------------------------------");


//        /**
//         * 画出匹配到的区域，测试用
//         */
//        System.out.println("==============================");
//        System.out.println("绘图中...");
//        System.out.println("==============================");
//        matchLocation = mmlr.maxLoc;
//        Imgproc.rectangle(image1,matchLocation,new Point(matchLocation.x + image2.cols(),matchLocation.y + image2.rows()),
//                new Scalar(0,0,0,0));
//        Imgcodecs.imwrite("C:\\Users\\2b\\Desktop\\final.jpg",image1);
//        System.out.println("==============================");
//        System.out.println("绘图结束");
//        System.out.println("==============================");
        }
        return bestValue;
    }



//    public static void main(String []args){
//
//        Mat i1 = new Mat();//目标
//        Mat i2 = new Mat();//模板
//
//        i1 = Imgcodecs.imread("D:\\tempMatch\\src\\Sample_czd\\match_area\\1_div.jpg");
//        i2 = Imgcodecs.imread("D:\\tempMatch\\src\\Sample_czd\\temp\\9\\5.jpg");
//
//        double num = 0;
//
//        ncc Process = new ncc();
//        Process.getTarget(i1);
//        Process.getTemp(i2);
//        Process.checkGrayOrNot();
//        num = Process.nccProcess();
//
//        System.out.println(num);
//    }
}
