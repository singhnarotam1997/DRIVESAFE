package com.example.drivesafe;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class Main2Activity extends AppCompatActivity {
    Intent i;
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

            //Initialize a LoadViewTask object and call the execute() method
            new LoadViewTask().execute();

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