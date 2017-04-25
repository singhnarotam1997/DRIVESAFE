package com.example.drivesafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.os.Handler;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Camera mCamera;
    Activity current=this;
    Context context=this;
    private static final int camera_req=1;
    private CameraView mCameraView;
    predict obj;
    Context cont;
    Intent tt;
    boolean status=false;
    LinearLayout preview;
    private int mInterval = 1000; // 1 seconds by default, can be changed later
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        obj=new predict();
        Log.d("Looping","Inside MainActivity");

        mCamera = getCameraInstance(Camera.CameraInfo.CAMERA_FACING_FRONT);
        cont=this;
        mCamera.setDisplayOrientation(90);
        Camera.Parameters params = mCamera.getParameters();
        List sizes = params.getSupportedPictureSizes();
        params.setPictureSize(640,480);
        //params.setPreviewSize(480,320);
        params.setRotation(270);
        params.setColorEffect(Camera.Parameters.EFFECT_MONO);
        Log.d("Value","is RGB"+PixelFormat.RGB_888 + " NY "+PixelFormat.YCbCr_420_SP);
        params.setPictureFormat(ImageFormat.JPEG);

        Log.d("size", params.getJpegThumbnailSize().toString());
        Camera.Size result = null;
        for (int i = 0; i < sizes.size(); i++) {
            result = (Camera.Size) sizes.get(i);
            Log.d("PictureSize", "Supported Size. Width: " + result.width + "height : " + result.height);
        }           // Create an instance of Camera
        mHandler = new Handler(Looper.getMainLooper());
        mCamera.setParameters(params);
        mCameraView = new CameraView(context, mCamera);

        // Create our Preview view and set it as the content of our activity.

        TableLayout preview = (TableLayout) findViewById(R.id.lay);
        preview.addView(mCameraView);
        mHandler = new Handler(Looper.getMainLooper());


// Add a listener to the Capture button
//        tt = new Intent();
  //      tt.setClass(this, Main2Activity.class);
        Button captureButton = (Button) findViewById(R.id.Capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!status) {
                            startRepeatingTask();
                            status=true;
                        }
                    }
                }
        );

        Button pau = (Button) findViewById(R.id.Pause);
        pau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=false;
                stopRepeatingTask();
            }
        });
        Button res = (Button) findViewById(R.id.Resume);
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!status) {
                    startRepeatingTask();
                    status = true;
                }
            }
        });


    }


    private Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }
    private File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpeg");

        } else {
            return null;
        }
        return mediaFile;
    }

    public static Camera getCameraInstance(int i) {
        Camera c = null;
        try {
            c = Camera.open(i); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }
    Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

/*            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                                if (pictureFile == null){
                                    Log.d("TAG", "Error creating media file, check storage permissions: ");
                                    return;
                                }


                        Log.d("TAG5", "Error creating media file, check storage permissions: ");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(pictureFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fos.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }



            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
  */
            obj.prediction(data,cont);
            camera.startPreview();

        }
                        // get an image from the camera
    };
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            Log.e("Running","Looping....");
            try {
                mCamera.takePicture(null, null, mPicture);
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

}