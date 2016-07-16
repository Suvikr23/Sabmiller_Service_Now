package com.example.suvendu.sabmillerservicenow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

/**
 * Created by Suvendu on 7/10/2016.
 */
public class SplashActivity extends Activity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        Thread timer = new Thread(this);
        timer.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            /*Intent startingPoint = new Intent("com.example.suvendu.sabmillerservicenow.MainActivity");
            startActivity(Intent.createChooser(startingPoint, "check"));*/
        }
        Intent startingPoint = new Intent(this,MainActivity.class);
        startActivity(startingPoint);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
