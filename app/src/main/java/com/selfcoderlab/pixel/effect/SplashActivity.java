package com.selfcoderlab.pixel.effect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(3000);
                    // Util.systemUpgrade(SplashActivity.this);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intent = null;
                intent = new Intent(SplashActivity.this,
                        MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();

            }
        };
        thread.start();
    }
}
