package com.czdpzc.match;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;

public class firstGPS extends Activity{
//    static {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//    }

    private Mat targetImg = new Mat();
    private char[] numTemplate = {'6', '7', '9'};
    static private int LocOfMax;
    private Point finalLocation = new Point();
    Context context;

    public firstGPS(Mat targetImg, Context mContext) {
        this.targetImg = targetImg;
        context = mContext;
    }

    public Point getBestLoc(){
        return finalLocation;
    }

    public static double avgOfArray(double[] numArray, int length) {
        double sum = 0;
        double avg = 0;

        for (int w = 0; w <= length - 1; w++) {
            sum = sum + numArray[w];
        }
        avg = sum / (length);

        return avg;
    }

    public static double maxOfArray(double[] numArray, int length) {
        double max = 0;
        max = numArray[0];

        for (int i = 1; i <= length - 1; i++) {
            if (max < numArray[i]) {
                max = numArray[i];
                LocOfMax = i;
            }
        }
        return max;
    }

    /**
     * 求double数组中的最大值的位置
     *
     * @param numArray
     * @return
     */
    public static int maxLocOfArray(double[] numArray, int length) {
        int n = numArray.length;
        double max = 0;
        int LocOfMax;
        LocOfMax = 0;
        max = numArray[0];

        for (int i = 1; i <= length - 1; i++) {
            if (max < numArray[i]) {
                max = numArray[i];
                LocOfMax = i;
            }
        }
        return LocOfMax;
    }

    public char select2match() {
        String path1 = "Sample_czd/temp";
        String path2;
        Mat tempImg = new Mat();
        String tempPath;
        char matchedChar;
        InputStream ishelper = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        Bitmap bit2mathelper;

        Point[] matchedLocation = new Point[200];
        Point[] matchedLocation2Compare = new Point[200];


        int num1 = numTemplate.length;

        /**
         * 用来存储每一种模板的平均匹配值
         */
        double[] matchNum2Compare = new double[200];

        /**
         * 1.获取模板文件夹中照片的数量和名称
         * 2.每一个for循环结束，代表一个种类的模板全部匹配过一次
         */
        try {
            for (int i = 0; i <= num1 - 1; i++) {
                path2 = path1 + "/" + numTemplate[i];

    //            File file2 = new File(path2);
    //            String[] strArray2 = file2.list();
                String[] strArray2 = context.getAssets().list(path2);
                String[] stringTest2 = new String[1000];

                /**
                 * 模板文件夹中的模板数量 N
                 */

                int N = strArray2.length;
                int finalN = N;//用来得到正确的除数，因为采用了跳过模板的方法
                Log.d("fuck","----------------------");
                Log.d("fuck","模板文件夹中的照片数量:" + N);
                Log.d("fuck","----------------------");

                /**
                 * 用来存储每一张图片的匹配值，用来求均值，然后送给matchNum2Compare
                 */
                double[] matchNum = new double[200];

                /**
                 * 获取模板文件夹中各张照片的名称
                 */
                if (N != 0) {
                    for (int x = 0; x <= strArray2.length - 1; x++) {
                        // System.out.printf(strArray[x].toString());
                        stringTest2[x] = strArray2[x].toString();
                    }
                    /**
                     * 这里嵌套的每一个for循环结束，代表一个种类的模板下的一张模板图片...
                     *          ...完成了匹配。并且把每一次的数值存储在matchNum中
                     */
                    for (int j = 0; j <= N - 1; j++) {
                        tempPath = path2 + "/" + stringTest2[j];
                        //    System.out.println(tempPath);
    //                    tempImg = imread(tempPath);
                        ishelper = context.getClassLoader().getResourceAsStream("assets/"+tempPath);
                        bit2mathelper = BitmapFactory.decodeStream(ishelper);
                        Utils.bitmapToMat(bit2mathelper,tempImg);
                        /**
                         * 开始使用ncc函数
                         */
                        ncc process = new ncc();
    //                    imwrite("C:\\Users\\2b\\Desktop\\NCC\\beforeNCC"+(j+1)+".jpg",targetImg);
                        process.getTarget(this.targetImg);
    //                    imwrite("C:\\Users\\2b\\Desktop\\NCC\\afterNCC"+(j+1)+".jpg",targetImg);
                        process.getTemp(tempImg);
                        process.checkGrayOrNot();
                        matchNum[j] = process.nccProcess();
                        matchedLocation[j] = process.getBestLocation();
                        if (matchNum[j] == -10) {
                            Point niceHelper = new Point(0, 0);
                            matchNum[j] = 0;
                            matchedLocation[j] = niceHelper;
                            finalN--;
                        }

    //                    System.out.println("再次确认数值："+matchNum[j]);
                    }
                    Log.d("fuck","----------------------");

    //                matchNum2Compare[i] = avgOfArray(matchNum,finalN);
                    matchNum2Compare[i] = maxOfArray(matchNum, finalN);
                    matchedLocation2Compare[i] = matchedLocation[LocOfMax];

    //                System.out.println("此模板匹配均值为："+avgOfArray(matchNum,finalN));
                    Log.d("fuck","此模板匹配的最大值为：" + maxOfArray(matchNum, finalN));
                } else {
                    Log.d("fuck","此文件夹无照片");
                    Log.d("fuck","-----------------------");
                    matchNum2Compare[i] = -1;
                    Log.d("fuck","此模板匹配均值为：null");
    //                System.out.println("-----------------------");
                }
            }
            /**
             * 获取最大值位置
             */

            int LocOfMac = maxLocOfArray(matchNum2Compare,num1);
            /**
             * 获取最大值位置对应的字母，即为匹配结果
             */
            matchedChar = numTemplate[LocOfMac];
            finalLocation = matchedLocation2Compare[LocOfMac];
        } catch (IOException e) {
            e.printStackTrace();
            matchedChar = '*';
        }

        return matchedChar;
    }

    public Mat firstGPSCut(Point bestLoc){
        Rect rect = new Rect((int)(bestLoc.x)-10, (int)(bestLoc.y) -20,this.targetImg.width()*5/6+30,this.targetImg.height()*3/4+10);
//        imwrite("C:\\Users\\2b\\Desktop\\beforeRec.jpg",targetImg);
        Mat firstGpsImg = new Mat(this.targetImg,rect);
//        imwrite("C:\\Users\\2b\\Desktop\\afterRec.jpg",targetImg);
        Mat outputImg = new Mat();
        firstGpsImg.copyTo(outputImg);

//        return outputImg;
        return firstGpsImg;
    }

//    public static void main(String []args){
//        Mat i1 = new Mat();
//        i1 = imread("D:\\tempMatch\\src\\Sample_czd\\match_area\\5.jpg");
//        Point bestLoc = new Point();
//        char firstNum;
//
//        firstGPS fg = new firstGPS(i1);
//        firstNum = fg.select2match();
//        bestLoc = fg.getBestLoc();
//
////        Rect rect = new Rect((int)(bestLoc.x)-10, (int)(bestLoc.y) -20,i1.width()*5/6+30,i1.height()*3/4+10);
//
////        Mat finalImg = new Mat(i1,rect);
//
//        Mat showImg = new Mat();
//        showImg = fg.firstGPSCut(bestLoc);
//
////        finalImg.copyTo(showImg);
//
//        imwrite("C:\\Users\\2b\\Desktop\\gpsTest.jpg",showImg);
//        System.out.println("匹配的第一个数字为："+firstNum);
//
//        targetImageDiv divImg = new targetImageDiv(showImg);
//        divImg.getOne();//分割
//    }
}
