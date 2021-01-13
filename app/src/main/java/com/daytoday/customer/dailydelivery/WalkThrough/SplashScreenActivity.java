package com.daytoday.customer.dailydelivery.WalkThrough;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daytoday.customer.dailydelivery.R;
import com.daytoday.customer.dailydelivery.Utilities.SaveOfflineManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME = 3000; /*This is the Splash screen time which is 3 seconds*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        Log.i("Firebase","Token is " + task.getResult());
                        SaveOfflineManager.setFireBaseToken(SplashScreenActivity.this,task.getResult());
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, WalkThroughActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN_TIME);  /*This is the Code For Holding the Screen For 3 Seconds */
    }
}
