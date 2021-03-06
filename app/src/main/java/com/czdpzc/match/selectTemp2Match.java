package com.czdpzc.match;

//import com.sun.imageio.plugins.jpeg.JPEG;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.czdpzc.photomooc.testActivity;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
//import org.opencv.highgui.Highgui;
import org.opencv.imgcodecs.Imgcodecs;

//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
import java.io.*;


import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;

/**
 * @function 主要的模板选择，然后用 ncc() 匹配
 * @input 被分割成四份的目标图片（逐一送入函数）
 * @output 1.最后的匹配值 2.匹配字符的输出
 * @author czd
 *
 * @note 1.将ncc（）函数单独写好后，在这个函数中import
 *       2.后面补充对ncc()计算结果的的比较，比较谁为最大值，而不是均值！！
 *
 */

public class selectTemp2Match extends Activity{

//    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private Mat targetImg = new Mat();
    Context context;
    private char[] charTemplate = {'A','F','L','P','R'};


    /**
     * 此处的context传入暂时玄学，不是很清楚T.T
     * @param targetImg
     * @param mContext
     */
    public selectTemp2Match(Mat targetImg , Context mContext){
        this.targetImg = targetImg;
        context = mContext;
    }
    /**
     * 获取目标图片
     * @param targetImg
     */
    public void getImage(Mat targetImg){
        this.targetImg = targetImg;
    }

    /**
     * 求double数组的均值
     * @param numArray
     * @return
     */
    public static double avgOfArray(double[] numArray,int length){
        double sum = 0;
        double avg = 0;

        for (int w=0;w<=length-1;w++){
            sum = sum + numArray[w];
        }
        avg = sum/(length);

        return avg;
    }

    public static double maxOfArray(double[] numArray,int length){
        double max = 0;
        max = numArray[0];

        for (int i=1;i<=length-1;i++){
            if (max < numArray[i]){
                max = numArray[i];
              //  LocOfMax = i;
            }
        }
        return max;
    }

    /**
     * 求double数组中的最大值的位置
     * @param numArray
     * @return
     */
    public static int maxLocOfArray(double[] numArray,int length){
        int n=numArray.length;
        double max = 0;
        int LocOfMax;
        LocOfMax = 0;
        max = numArray[0];

        for (int i=1;i<=length-1;i++){
            if (max < numArray[i]){
                max = numArray[i];
                LocOfMax = i;
            }
        }
        return LocOfMax;
    }


    /**
     * 选择模板进行匹配的过程，返回匹配到的字符
     * @return
     */
    public char select2match(){
        String path1 = "Sample_czd/temp";
        String path2 ;
        String stringTest = new String();
        char charArray[];
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
        /**
         * 用来存储每一种模板的平均匹配值
         */
        double[] matchNum2Compare = new double[200];
        /**
         * 用来存储每一张图片的匹配值，用来求均值，然后送给matchNum2Compare
         */
        double[] matchNum = new double[200];

        try {

            int num1 = charTemplate.length;
//            /**
//             * 模板文件夹数量的获取
//             */
//            String[] strArray = context.getAssets().list(path1);
//            int num1 = strArray.length;
//            System.out.println("----------------------");
//            System.out.println("模板种类数量：" + num1);
//
//            /**
//             * 模板文件夹种类的获取
//             */
//            for (int x=0;x<=strArray.length-1;x++) {
//                // System.out.printf(strArray[x].toString());
//                stringTest = stringTest + strArray[x].toString();
//            }
//            System.out.println("");
//
//            /**
//             * 若要获取种类，使用charArray[]
//             */
//            charArray = stringTest.toCharArray();
//            System.out.println("模板种类：");
//            System.out.print(charArray);
//
//            System.out.println("");
//            System.out.println("----------------------");
//
//            /**
//             * 若要获取种类，使用charArray[]
//             */
//            charArray = stringTest.toCharArray();
//            System.out.println("模板种类：");
//            System.out.print(charArray);
//
//            System.out.println("");
//            System.out.println("----------------------");

            /**
             * 1.获取模板文件夹中照片的数量和名称
             * 2.每一个for循环结束，代表一个种类的模板全部匹配过一次
             */
            for (int i=0;i<=num1-1;i++){
                path2 = path1+"/"+charTemplate[i];
                //    System.out.println("模板文件夹位置："+path2);

//                File file2 = new File(path2);
//                String[] strArray = context.getAssets().list(path1);
//                String[] strArray2 = file2.list();
                String[] strArray2 = context.getAssets().list(path2);
                String[] stringTest2 = new String[200];

                /**
                 * 模板文件夹中的模板数量 N
                 */
                int N=strArray2.length;
                int finalN = N;//用来得到正确的除数，因为采用了跳过模板的方法
                Log.d("fuck","----------------------");
                Log.d("fuck","模板文件夹中的照片数量:"+N);
                Log.d("fuck","----------------------");

//                /**
//                 * 用来存储每一张图片的匹配值，用来求均值，然后送给matchNum2Compare
//                 */
//                double[] matchNum = new double[200];

                /**
                 * 获取模板文件夹中各张照片的名称
                 */
                if (N!=0) {
                    for (int x = 0; x <= strArray2.length - 1; x++) {
                        // System.out.printf(strArray[x].toString());
                        stringTest2[x] = strArray2[x].toString();
                    }
                    /**
                     * 这里嵌套的每一个for循环结束，代表一个种类的模板下的一张模板图片...
                     *          ...完成了匹配。并且把每一次的数值存储在matchNum中
                     */
                    for (int j=0;j<=N-1;j++){
                        tempPath = path2+"/"+stringTest2[j];
                        //    System.out.println(tempPath);
//                        tempImg = imread(tempPath);
                        ishelper = context.getClassLoader().getResourceAsStream("assets/"+tempPath);
                        bit2mathelper = BitmapFactory.decodeStream(ishelper);
                        Utils.bitmapToMat(bit2mathelper,tempImg);

                        /**
                         * 开始使用ncc函数
                         */
                        ncc process = new ncc();
                        process.getTarget(this.targetImg);
                        process.getTemp(tempImg);
                        process.checkGrayOrNot();
                        matchNum[j] = process.nccProcess();
                        if (matchNum[j] == -10){
                            matchNum[j] = 0;
                            finalN--;
                        }

//                    System.out.println("再次确认数值："+matchNum[j]);
                    }
                    Log.d("fuck","----------------------");

                    matchNum2Compare[i] = avgOfArray(matchNum,finalN);
//                matchNum2Compare[i] = maxOfArray(matchNum,finalN);

                    Log.d("fuck","此模板匹配均值为："+avgOfArray(matchNum,finalN));
//                System.out.println("此模板匹配的最大值为："+maxOfArray(matchNum,finalN));
                }

                else {
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
            matchedChar = charTemplate[LocOfMac];
            //  System.out.println("匹配结果："+matchedChar);
            //  System.out.println("-----------------------------");

        } catch (IOException e) {
            e.printStackTrace();
            matchedChar = '*';
        }
        return matchedChar;
    }
}
