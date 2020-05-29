package com.daytoday.customer.dailydelivery.LoginActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daytoday.customer.dailydelivery.BlankActivity;
import com.daytoday.customer.dailydelivery.HomeScreenActivity;
import com.daytoday.customer.dailydelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpVerification extends AppCompatActivity {
    EditText e1, e2, e3, e4, e5, e6;
    static final String TAG="verification_activity";
    private String phone;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private TextView textTimer;
    private int time=60;  //Time OUT resend OTP
    private String code;
    private String name;

    private int SPLASH_SCREEN_TIME = 10000; /*This is the Splash screen time which is 3 seconds*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_activity);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();


        e1 = findViewById(R.id.edittext1);
        e2 = findViewById(R.id.edittext2);
        e3 = findViewById(R.id.edittext3);
        e4 = findViewById(R.id.edittext4);

        e5 = findViewById(R.id.edittext5);
        e6 = findViewById(R.id.edittext6);

        textTimer = findViewById(R.id.timer);

        phone=getIntent().getStringExtra("phoneNo");
        name=getIntent().getStringExtra("Name");

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.w("onVerifivcation", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    Log.i(TAG, "onVerificationFailed: "+e);
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.i(TAG, "onVerificationFailed: "+e);
                }
            }
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

               // PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
               // signInWithPhoneAuthCredential(credential);
            }
        };

          phoneAuthProvider();


       // e5 = findViewById(R.id.edittext5);
        e5=findViewById(R.id.edittext5);
        e6 = findViewById(R.id.edittext6);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(verification_activity.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_TIME);*/

        e1.addTextChangedListener(new OTPTextWatcher(e1));
        e2.addTextChangedListener(new OTPTextWatcher(e2));
        e3.addTextChangedListener(new OTPTextWatcher(e3));
        e4.addTextChangedListener(new OTPTextWatcher(e4));
        e5.addTextChangedListener(new OTPTextWatcher(e5));
        e6.addTextChangedListener(new OTPTextWatcher(e6));
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            SendUserHomePage();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
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
            SendUserHomePage();
        }
    }

    public void phoneAuthProvider(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                OtpVerification.this,               // Activity (for callback binding)
                mCallbacks);

        new CountDownTimer(60000, 1000) {
           public void onTick(long millisUntilFinished) {
                textTimer.setText("0:"+checkDigit(time));
                time--;
            }
            public void onFinish() {
                textTimer.setText("try again");
            }
        }.start();

  }
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public void SendUserHomePage(){
        Log.i("msg","this");
        Intent loginIntent=new Intent(OtpVerification.this, HomeScreenActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
        finish();
    }

    //-------------------------OTP WATCHER CLASS--------------------------------------//
    public class OTPTextWatcher implements TextWatcher {
        private View view;

        public OTPTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String otp = s.toString();
            switch (view.getId()) {
                case R.id.edittext1:
                    if (otp.length() == 1) {
                        e2.requestFocus();
                    } else if (otp.length() == 0)
                        e1.requestFocus();
                    break;
                case R.id.edittext2:
                    if (otp.length() == 1) {
                        e3.requestFocus();
                    } else if (otp.length() == 0)
                       e2.requestFocus();
                    break;
                case R.id.edittext3:
                    if (otp.length() == 1) {
                        e4.requestFocus();
                    } else if (otp.length() == 0)
                        e3.requestFocus();
                    break;
                case R.id.edittext4:
                    if (otp.length() == 1) {
                        e5.requestFocus();
                    } else if (otp.length() == 0)
                        e4.requestFocus();
                    break;
                case R.id.edittext5:
                    if (otp.length() == 1) {
                        e6.requestFocus();
                    }else {
                        e2.requestFocus();
                    }
                    break;
                case R.id.edittext6:
                    if (otp.length() == 1) {

//                      code=e1.getText().toString()+e2.getText().toString()+e3.getText().toString()
//                      +e4.getText().toString()+e5.getText().toString()+e6.getText().toString();
//                      phoneAuthProvider();
                    }else{
                        e5.requestFocus();
                        String tempotp = "1111";
                        Log.e(TAG, "afterTextChanged: "+ e3 );
                        Log.i("msg","here1");
                        Intent intent = new Intent(OtpVerification.this, HomeScreenActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                default:
                    Log.i("msg","here2");
                    Intent intent = new Intent(OtpVerification.this, HomeScreenActivity.class);
                    startActivity(intent);
                    finish();
            }

        }
    }
    //-------------------------OTP watcher class ends here-------------------------------------------------------//
}
