package com.example.dat.vkchat.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.TimerTask;

/**
 * Created by DAT on 8/30/2015.
 */
public class NotifyIncomingMsg extends AsyncTask {


    @Override
    protected Object doInBackground(Object[] params) {
        Log.d("DoINBackGround", "On doInBackground...");
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
                Log.d("DoINBackGround", "On doInBackground...");
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        }

        return null;
    }

    protected void onPreExecute() {
        Log.d("PreExceute", "On pre Exceute......");
    }


    protected void onProgressUpdate(Integer... a) {
        Log.d("onProgressUpdate", a[0].toString());
    }

    protected void onPostExecute(String result) {
        Log.d("onPostExecute", result);
    }

}
