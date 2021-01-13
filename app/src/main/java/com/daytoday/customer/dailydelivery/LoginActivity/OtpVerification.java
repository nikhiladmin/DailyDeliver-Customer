package com.daytoday.customer.dailydelivery.LoginActivity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;

import com.daytoday.customer.dailydelivery.HomeScreen.Model.AuthUser;
import com.daytoday.customer.dailydelivery.HomeScreen.View.HomeScreenActivity;
import com.daytoday.customer.dailydelivery.Network.ApiInterface;
import com.daytoday.customer.dailydelivery.Network.Client;
import com.daytoday.customer.dailydelivery.Network.Response.AuthUserResponse;
import com.daytoday.customer.dailydelivery.Network.Response.OTPSendResponse;
import com.daytoday.customer.dailydelivery.Network.Response.OTPVerifyResponse;
import com.daytoday.customer.dailydelivery.R;
import com.daytoday.customer.dailydelivery.Utilities.SaveOfflineManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerification extends AppCompatActivity {
    static final String TAG = "verification_activity";
    private PinView pinView;
    private String phone;
    private FirebaseAuth mAuth;
    private TextView textTimer;
    private ApiInterface apiInterface;
    private int time = 60;  //Time OUT resend OTP
    private String code;
    private String verification;
    private Button resend;
    private boolean isPhoneAuth;
    private String user_address;
    private TextView opt_subtile;
    private AlertDialog alertDialog;
    String email;
    String password;
    private  Button changeActionBtn;



    private final int SPLASH_SCREEN_TIME = 10000; /*This is the Splash screen time which is 3 seconds*/
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {

            Log.i(TAG, "onVerificationCompleted: " + credential.getSmsCode());

            code = credential.getSmsCode();
            if (code != null && code.length() == 6) {
                pinView.setText(code);
            }
        }


        @Override
        public void onVerificationFailed(FirebaseException e) {

            Log.w("onVerifivcation", "onVerificationFailed", e);
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG);
                Log.i(TAG, "onVerificationFailed: " + e);
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                pinView.setError("We have blocked all requests from this device due to unusual activity. Try again later.");
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG);
                Log.i(TAG, "onVerificationFailed: " + e);
            }
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            Log.i(TAG, "onCodeSent: " + verificationId);
            verification = verificationId;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_activity);

        phone = getIntent().getStringExtra("phoneNo");
        isPhoneAuth = getIntent().getBooleanExtra("isPhoneAuth", false);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
        apiInterface = Client.getClient().create(ApiInterface.class);
        mAuth = FirebaseAuth.getInstance();
        pinView = findViewById(R.id.firstPinView);
        textTimer = findViewById(R.id.timer);
        resend = findViewById(R.id.resend);
        opt_subtile = findViewById(R.id.otp_subtitle);
        changeActionBtn = findViewById(R.id.change_email_phone_input);
        resend.setEnabled(false);
        apiInterface = Client.getClient().create(ApiInterface.class);
        changeActionBtn.setEnabled(false);
        //Authentication With phoneNumber--------------
        Log.i(TAG, "onCreate: " + isPhoneAuth);

        if(isPhoneAuth){
            phoneAuthProvider();

        }else{
            opt_subtile.setText("Enter the OTP you received on your email.");
            changeActionBtn.setText("Change Email");
            email = getIntent().getStringExtra("email");
            password = getIntent().getStringExtra("password");
            sendOTP(email);
        }
        changeActionBtn.setOnClickListener(view -> {
            if(isPhoneAuth){
                Intent phoneIntent = new Intent(OtpVerification.this, PhoneVerification.class);
                finish();
                startActivity(phoneIntent);
            }else{
                Intent emailIntent = new Intent(OtpVerification.this, EmailSignup.class);
                finish();
                startActivity(emailIntent);
            }
        });
        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                code = s.toString();
                if ((code != null) && (code.length() == 6)) {
                    if(isPhoneAuth){
                        firebaseVerifyCode(code);
                    }else{
                        Log.i(TAG, "afterTextChanged: "+code);
                        emailVerifyCode(code,email,password);
                    }

                }
            }
        });

        resend.setOnClickListener(v -> {
            time = 60;
            Snackbar.make(v, "We Just Send You OTP again .Please Try Again !", Snackbar.LENGTH_LONG).show();
            if(isPhoneAuth){
                phoneAuthProvider();
            }else{
                sendOTP(email);
            }

        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            DialogBoxShow();
                            Log.i(TAG, "onComplete: "+task.getResult().getAdditionalUserInfo().isNewUser()+task.getResult().getUser().getDisplayName());
                            if (task.getResult().getAdditionalUserInfo().isNewUser() == true || task.getResult().getUser().getDisplayName() == null) {
                                alertDialog.dismiss();
                                Intent additionalInfoIntent = new Intent(OtpVerification.this, AdditionalInfo.class);
                                additionalInfoIntent.putExtra("isPhoneAuth",true);
                                additionalInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                additionalInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(additionalInfoIntent);
                                finish();
                            } else {
                                Log.i(TAG, "onComplete: login");
                                FirebaseUser user = task.getResult().getUser();
                                if (user != null) {
                                    loginUser(user.getUid());

                                }
                            }

                        } else {
                            // Sign in failed, display a message and update the com.daytoday.business.dailydelivery.MainHomeScreen.UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            alertDialog.dismiss();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                               pinView.setError("Invalid OTP");
                            }
                        }
                    }
                });
    }

    public void saveOffline(FirebaseUser currentUser, String name, String adress) {
        SaveOfflineManager.setUserName(this, name);
        SaveOfflineManager.setUserId(this, currentUser.getUid());
        SaveOfflineManager.setUserAddress(this, adress);
        SaveOfflineManager.setUserPhoneNumber(this, currentUser.getPhoneNumber());
    }

    private void firebaseVerifyCode(String code) {
        if(verification!=null){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification, code);
            signInWithPhoneAuthCredential(credential);
        }else{
            Toast.makeText(OtpVerification.this,"Verification failed",Toast.LENGTH_LONG).show();
        }


    }

    private void phoneAuthProvider() {
        Log.i(TAG, "phoneAuthProvider: " + phone);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                OtpVerification.this,               // Activity (for callback binding)
                mCallbacks);

        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                textTimer.setText("0:" + checkDigit(time));
                time--;
            }

            public void onFinish() {
                changeActionBtn.setEnabled(true);
                textTimer.setVisibility(View.INVISIBLE);
                resend.setEnabled(true);
            }
        }.start();

    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public void SendUserHomePage() {
        alertDialog.dismiss();
        Intent loginIntent = new Intent(OtpVerification.this, HomeScreenActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(loginIntent);

    }

    private void loginUser(String uid) {
        Call<AuthUserResponse> loginUserDataCalling = apiInterface.loginUser(uid);
        loginUserDataCalling.enqueue(new Callback<AuthUserResponse>() {
            @Override
            public void onResponse(Call<AuthUserResponse> call, Response<AuthUserResponse> response) {
                Log.i(TAG, "onResponse: "+response.body().getError());
                if(response.body().getError()==false) {
                    AuthUser authUser = response.body().getCustDetails().get(0);
                    Log.i("LOGIN_WITH_PHONE", "onResponse: " + authUser.getName());
                    saveOffline(mAuth.getCurrentUser(), authUser.getName(), authUser.getAddress());
                    SendUserHomePage();
                }
            }

            @Override
            public void onFailure(Call<AuthUserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendOTP(String email){
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                textTimer.setText("0:" + checkDigit(time));
                time--;
            }

            public void onFinish() {
                textTimer.setVisibility(View.INVISIBLE);
                changeActionBtn.setEnabled(true);
                textTimer.setText("Change Mobile Number");
                resend.setEnabled(true);
            }
        }.start();

        Call<OTPSendResponse> otpSendResponseCall = apiInterface.getOTPSend(email);

        otpSendResponseCall.enqueue(new Callback<OTPSendResponse>() {
            @Override
            public void onResponse(Call<OTPSendResponse> call, Response<OTPSendResponse> response) {
                if(response.body().getError()){
                    Toast.makeText(OtpVerification.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OTPSendResponse> call, Throwable t) {

            }
        });
    }

    private  void emailVerifyCode(String code,String email,String password){
        Call<OTPVerifyResponse> otpVerifyResponseCall = apiInterface.getOTPVerify(code,email);
        otpVerifyResponseCall.enqueue(new Callback<OTPVerifyResponse>() {
            @Override
            public void onResponse(Call<OTPVerifyResponse> call, Response<OTPVerifyResponse> response) {
                Log.i(TAG, "onResponse: "+response.body().getError()+" "+response.body().getStatus()+" "+response.body().getMessage()+ " "+code);
                if(response.body().getError()){
                    Toast.makeText(OtpVerification.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    pinView.setError(response.body().getMessage());
                }else{
                    if(response.body().getStatus()==0){
                        Intent sign = new Intent(OtpVerification.this, AdditionalInfo.class);
                        sign.putExtra("email",email);
                        sign.putExtra("password",password);
                        sign.putExtra("isPhoneAuth",false);
                        sign.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        sign.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(sign);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),response.body().getMessage()+". Please try again!",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OTPVerifyResponse> call, Throwable t) {

            }
        });
    }
    void DialogBoxShow(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(OtpVerification.this,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(OtpVerification.this).inflate(R.layout.custom_dialog, viewGroup, false);
        builder.setView(dialogView);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

}
