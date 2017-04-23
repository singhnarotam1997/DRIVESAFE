package com.example.drivesafe;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.HOGDescriptor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.opencv.core.*;
/**
 * Created by Narotam on 01-04-2017.
 */
public class predict {

    HOGDescriptor hog;
  MatOfFloat ders;
    MatOfPoint points;
    int last=0;
    int count=0;
    public static double[][] Theta2;
    public static double[][] Theta1;
    static {
        System.loadLibrary("opencv_java3");
    }
    static void AddFile(Context context, Main2Activity.LoadViewTask LoadingTask) {

        try {

            BufferedReader file1;
            BufferedReader file2;
            file1 = new BufferedReader(new InputStreamReader(context.getAssets().open("o1.txt")));
            file2 = new BufferedReader(new InputStreamReader(context.getAssets().open("o2.txt")));
            Theta1 = new double[50][720 + 1];
            Theta2 = new double[10][50 + 1];
            String[] Data;
            //Initialize an integer (that will act as a counter) to zero
            //While the counter is smaller than four
            //Wait 850 milliseconds
            //Increment the counter
            //Set the current progress.
            //This value is going to be passed to the onProgressUpdate() method.
            for (int i = 0; i < 50; i++) {
                Log.e("Looping", "" + i);
                Data = file1.readLine().split(" ");
                for (int j = 0; j < 721; j++)
                    Theta1[i][j] = Double.parseDouble(Data[j]);
                LoadingTask.onProgressUpdate(i);
            }
            for (int i = 0; i < 10; i++) {
                Log.e("Looping", "" + i);
                Data = file2.readLine().split(" ");
                for (int j = 0; j < 51; j++)
                    Theta2[i][j] = Double.parseDouble(Data[j]);
                LoadingTask.onProgressUpdate(i*10);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public boolean prediction(byte[] arr, Context context) {
        Log.d("Looping","Predict Start");
        Bitmap mp = BitmapFactory.decodeByteArray(arr, 0, arr.length);

        // Copy pixel data from the Bitmap into the 'intArray' array
        //mp = Bitmap.createScaledBitmap(mp, 50, 50, true);
        Bitmap mp1 = JPEGtoRGB888(mp);

 //load opencv_java lib
//            System.loadLibrary("native_sample");

        //Bitmap mp1 = mp.copy(Bitmap.Config.ARGB_8888,true);
       // Log.d("Looping","After JPEGtoRGB"+mp1.getWidth()+" Height : "+mp1.getHeight());
        Mat MAT = new Mat(mp1.getWidth(),mp1.getHeight(),CvType.CV_8UC1);
        Utils.bitmapToMat(mp1,MAT);
        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root.getPath() + File.separator +
                    "output" + ".txt");

        Mat forHOGim = new Mat();
        org.opencv.core.Size sz = new org.opencv.core.Size(480,640);
        Imgproc.resize( MAT, MAT, sz );
        Imgproc.cvtColor(MAT,forHOGim,Imgproc.COLOR_RGB2GRAY);
        /*FileWriter writer = null;
        try {
            writer = new FileWriter(gpxfile, true);
            for (int i = 0; i < forHOGim.width(); i++) {
                //Log.d("Looping",i+" ");
                for (int j = 0; j < forHOGim.height(); j++) {
                    writer.append(forHOGim.get(j,i)[0]+" ");
                }
                writer.append("\n");
                }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        gpxfile = new File(root.getPath() + File.separator +
                "output1" + ".txt");
/*        try {
            writer = new FileWriter(gpxfile, true);
            for (int i = 0; i < forHOGim.height(); i++) {
                for (int j = 0; j < forHOGim.width(); j++) {
                    writer.append(forHOGim.get(i,j)[0] + " ");
                }
                writer.append("\n");
            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
      */ /* MatOfInt matInt=new MatOfInt();
            matInt.fromArray(Imgcodecs.CV_IMWRITE_PNG_COMPRESSION, 0);
            Imgcodecs.imwrite(root.getPath() + File.separator +
                    "output" + ".png", MAT, matInt);
            matInt.fromArray(Imgcodecs.CV_IMWRITE_JPEG_QUALITY, 0);
            Imgcodecs.imwrite(root.getPath() + File.separator +
                    "output1" + ".jpeg", MAT, matInt);
            */

        ders=new MatOfFloat();
            points=new MatOfPoint();

            Log.d("Looping","Mat created"+forHOGim.width()+" "+forHOGim.height());
            //hog=new HOGDescriptor();
            hog = new HOGDescriptor(sz,new org.opencv.core.Size(480,640),new org.opencv.core.Size(8,8),new org.opencv.core.Size(48,80),9);
      //      Log.d("Looping","bitmapToMAT");
            hog.compute(forHOGim , ders, new org.opencv.core.Size(480,640), new Size(0,0), points);
        //    Log.d("Looping","hog.compute");



      /*  int[] pix = new int[mp1.getWidth() * mp1.getHeight()];
        mp1.getPixels(pix, 0, mp1.getWidth(), 0, 0, mp1.getWidth(), mp1.getHeight());
*/
/*        if (arr.length < 144 * 176) {
            Log.e("Running", "Returned False" + arr.length);
            return false;
        }*//*
        byte[] grey = new byte[pix.length];
        for (int i = 0; i < pix.length; i++) {
            grey[i] = (byte)(pix[i]&0xFF);
        }
        if(pix.length<2500)
            return false;*/
        float[] pix = ders.toArray();
        Log.d("Looping","ders to array"+pix.length);
        if (pix.length < 720)
            return false;

        double[] Hidden = new double[51];
        Hidden[0] = 1;
//        Log.d("Looping","Hidden");
        for (int i = 0; i < 50; i++) {// Hidden[i+1]=t0+t1x0+t2x1+...
            Hidden[i + 1] = Theta1[i][0];
  //          Log.e("Running", "Loop1 " + i);
            for (int j = 0; j < 720; j++) {
                Hidden[i + 1] += Theta1[i][j + 1] * pix[j];
            }
            Hidden[i + 1] = 1 / (1 + Math.exp(-Hidden[i + 1]));
        }
       // Log.e("Running", "After Loop1");
        double[] Ans = new double[10];
        for (int i = 0; i < 10; i++) {// Hidden[i+1]=t0+t1x0+t2x1+...
            Ans[i] = Theta2[i][0];
         //   Log.e("Running", "Loop1 " + i);
            for (int j = 0; j < 50; j++) {
                Ans[i] += Theta2[i][j + 1] * Hidden[j];
            }
            Ans[i] = 1 / (1 + Math.exp(-Hidden[i + 1]));
        }
     //   Log.e("Running", "After Loop2");
        double maxAns = 0;
        int index = 0;
        double avg=0;
        for (int i = 0; i < 10; i++) {
//            Log.d("FinalState",i+":"+Ans[i]);
            avg+=Ans[i]/10;
            if (maxAns < Ans[i]) {
                maxAns = Ans[i];
                index = i;
            }
        }
        if(maxAns<avg*1.2)
            index=0;
        MediaPlayer mpp;
        if(last==index)
            count++;
        else{
            count=0;
            last=index;
        }
        switch (index) {
            case 0:
                Log.e("Looping", "Normal");
                break;
            case 1:
                Log.e("Looping", "Chatting with left hand");
                break;
            case 2:
                Log.e("Looping", "Talking on phone with left hand");
                break;
            case 3:
                Log.e("Looping", "Chatting with right hand");
                break;
            case 4:
                Log.e("Looping", "Talking on phone with right hand");
                break;
            case 5:
                Log.e("Looping", "switching Radio");
                break;
            case 6:
                Log.e("Looping", "Drinking");
                break;
            case 7:
                Log.e("Looping", "Taking something from back");
                break;
            case 8:
                Log.e("Looping", "Makeup");
                break;
            case 9:
                Log.e("Looping", "Talking with passenger");
                break;
        }
        if(index!=0 && count==3){
            count--;
            mpp = MediaPlayer.create(context,R.raw.o);
            mpp.start();
            return true;
        }
        else
        return false;
    }


    private Bitmap JPEGtoRGB888(Bitmap img) {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }
}

