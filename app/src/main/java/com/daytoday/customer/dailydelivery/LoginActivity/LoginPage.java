package com.daytoday.customer.dailydelivery.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


import com.daytoday.customer.dailydelivery.BlankActivity;
import com.daytoday.customer.dailydelivery.HomeScreenActivity;
import com.daytoday.customer.dailydelivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginPage extends AppCompatActivity {
    private Button phoneLogin;
    private Button googleLogin;
    private Button facebookLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);


        mAuth = FirebaseAuth.getInstance();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        

        phoneLogin = findViewById(R.id.btn1);
        googleLogin = findViewById(R.id.btn2);
        facebookLogin = findViewById(R.id.btn3);


        phoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, PhoneVerification.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Log.i("msg","here4");
            Intent loginIntent=new Intent(LoginPage.this, HomeScreenActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
        }
    }

}