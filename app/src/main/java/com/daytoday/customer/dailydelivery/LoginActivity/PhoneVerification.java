package com.daytoday.customer.dailydelivery.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.daytoday.customer.dailydelivery.HomeScreenActivity;
import com.daytoday.customer.dailydelivery.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;


public class PhoneVerification extends AppCompatActivity {

    CountryCodePicker ccp;
    Button send_otp;

    EditText phoneNo;
    EditText first;
    EditText last;
    String ft,lt,fullname;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();


        ccp=findViewById(R.id.ccp);
        send_otp=findViewById(R.id.send_otp);
        phoneNo=findViewById(R.id.editText_carrierNumber);
        first =findViewById(R.id.firstName);
        last  =findViewById(R.id.lastName);

        mAuth = FirebaseAuth.getInstance();

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft=first.getText().toString().trim();
                lt=last.getText().toString().trim();
                String completePhoneNo=ccp.getFullNumberWithPlus()+phoneNo.getText().toString();
                if(ft.isEmpty()||lt.isEmpty()){
                    Snackbar.make(v,"Fill the First and Last Name", Snackbar.LENGTH_LONG).show();
                }else if(!(ft.matches("^[A-Za-z]+$")&&lt.matches("^[a-zA-Z]+$"))){
                    Snackbar.make(v,"Only Alphabets allowed in First and Last Name", Snackbar.LENGTH_LONG).show();
                }else if(phoneNo.getText().toString().isEmpty()){
                    Snackbar.make(v,"Enter Phone Number", Snackbar.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(PhoneVerification.this, OtpVerification.class);
                    intent.putExtra("phoneNo",completePhoneNo);
                    intent.putExtra("Name",ft+" "+lt);
                    PhoneVerification.this.startActivity(intent);
                }

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Log.i("msg","here3");
            Intent loginIntent=new Intent(PhoneVerification.this, HomeScreenActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
        }
    }
    //country code picker function starts
    //country code picker function ends

}
