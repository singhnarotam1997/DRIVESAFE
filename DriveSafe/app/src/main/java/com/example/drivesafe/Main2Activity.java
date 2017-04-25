package com.example.drivesafe;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
public class Main2Activity extends AppCompatActivity {
    Intent i;
    Activity current=this;
    private static final int camera_req=1;
        /*super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Thread t=new Thread(){
            @Override
            public void run(){
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(getApplicationContext(),
                            MainActivity.class);
                    startActivity(i);
            }
        };
        t.start();*/

        //A ProgressDialog object
        public ProgressDialog progressDialog;

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            if (ContextCompat.checkSelfPermission(current,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(current,Manifest.permission.CAMERA)) {
                        Log.d("Permission_Granted","Inside if");
                    ActivityCompat.requestPermissions(current,new String[]{Manifest.permission.CAMERA},camera_req);
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {
                    Log.d("Permission_Granted","Inside else");
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(current,new String[]{Manifest.permission.CAMERA},camera_req);
                }

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                //
                }
            Log.d("Permission_Granted"," "+ContextCompat.checkSelfPermission(current,Manifest.permission.CAMERA));
            if (ContextCompat.checkSelfPermission(current,Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission_Granted","True Rechecked");
                new LoadViewTask().execute();
            }

        }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case camera_req: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission_Granted","True");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    new LoadViewTask().execute();

                } else {
                    Log.d("Permission_Granted","False");
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
        //To use the AsyncTask, it must be subclassed
        public class LoadViewTask extends AsyncTask<Void, Integer, Void>
        {
            //Before running code in separate thread
            @Override
            protected void onPreExecute()
            {
                //Create a new progress dialog
                progressDialog = new ProgressDialog(Main2Activity.this);
                //Set the progress dialog to display a horizontal progress bar
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                //Set the dialog title to 'Loading...'
                progressDialog.setTitle("Loading...");
                //Set the dialog message to 'Loading application View, please wait...'
                progressDialog.setMessage("Loading application Data, please wait...");
                //This dialog can't be canceled by pressing the back key
                progressDialog.setCancelable(false);
                //This dialog isn't indeterminate
                progressDialog.setIndeterminate(false);
                //The maximum number of items is 100
                progressDialog.setMax(100);
                //Set the current progress to zero
                progressDialog.setProgress(0);
                //Display the progress dialog
                progressDialog.show();
            }

            //The code to be executed in a background thread.
            @Override
            protected Void doInBackground(Void... params)
            {
            /* This is just a code that delays the thread execution 4 times,
             * during 850 milliseconds and updates the current progress. This
             * is where the code that is going to be executed on a background
             * thread must be placed.
             */

                Log.d("Looping","Loading..");
                predict.AddFile(getApplicationContext(),this);
                    i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                return null;
            }

            //Update the progress
            @Override
            public void onProgressUpdate(Integer... values)
            {
                //set the current progress of the progress dialog
                progressDialog.setProgress(values[0]);
            }

            //after executing the code in the thread
            @Override
            protected void onPostExecute(Void result)
            {
                //close the progress dialog
                progressDialog.dismiss();
                //initialize the View
                setContentView(R.layout.activity_main);
            }
        }
}