package com.daytoday.customer.dailydelivery.LoginActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.daytoday.customer.dailydelivery.Network.ApiInterface;
import com.daytoday.customer.dailydelivery.Network.Client;
import com.daytoday.customer.dailydelivery.Network.Response.GeocodingResponse;
import com.daytoday.customer.dailydelivery.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PhoneVerification extends AppCompatActivity {

    CountryCodePicker ccp;
    Button send_otp;

    private TextInputEditText phoneNo;
    private boolean isPhoneAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        ccp = findViewById(R.id.ccp);
        send_otp = findViewById(R.id.send_otp);
        phoneNo = findViewById(R.id.editText_carrierNumber);


        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNo.getText().toString()!=null&&phoneNo.getText().toString().length() != 0) {
                    String completePhoneNo = ccp.getFullNumberWithPlus() + phoneNo.getText().toString();
                    Intent intent = new Intent(PhoneVerification.this, OtpVerification.class);
                    intent.putExtra("isPhoneAuth", true);
                    intent.putExtra("phoneNo", completePhoneNo);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();

                    startActivity(intent);
                }else{
                    phoneNo.setError("Enter phone number");
                }
            }
        });
    }
}
