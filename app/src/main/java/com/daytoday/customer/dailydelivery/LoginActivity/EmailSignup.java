package com.daytoday.customer.dailydelivery.LoginActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import com.daytoday.customer.dailydelivery.HomeScreen.View.HomeScreenActivity;
import com.daytoday.customer.dailydelivery.Network.ApiInterface;
import com.daytoday.customer.dailydelivery.Network.Client;
import com.daytoday.customer.dailydelivery.Network.Response.AuthUserCheckResponse;
import com.daytoday.customer.dailydelivery.R;
import com.daytoday.customer.dailydelivery.Utilities.SaveOfflineManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailSignup extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button  signupBtn;
    private CheckBox privacyPolicyCheck;
    private Button privacyPolicyBtn;
    private Button signinNow;
    private AlertDialog alertDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_signup);

        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        emailEditText = findViewById(R.id.signup_email);
        passwordEditText = findViewById(R.id.signup_password);
        confirmPasswordEditText = findViewById(R.id.signup_confirm_password);
        signupBtn = findViewById(R.id.signup_btn);
        privacyPolicyCheck = findViewById(R.id.privacy_policy_checkBox);
        privacyPolicyBtn = findViewById(R.id.privacy_policy);
        signinNow = findViewById(R.id.signin_now);
        mAuth = FirebaseAuth.getInstance();

        signinNow.setOnClickListener(view -> {
            Intent signin = new Intent(EmailSignup.this, LoginPage.class);
            startActivity(signin);
        });

        signupBtn.setOnClickListener(view -> {
   if(privacyPolicyCheck.isChecked()) {
    if (isEmailValid(emailEditText.getText().toString())) {
        isUserExist(emailEditText.getText().toString());
    }
}else{
    Snackbar.make(view,"Please read and accept the privacy and policy.",Snackbar.LENGTH_LONG).show();
}
        });
    }

    private boolean isEmailValid(String e) {
        if(e==null||e.length()==0){
            emailEditText.setError("Please enter email");
            return false;
        }else {
            return true;
        }
    }


    private  boolean passwordMatch(String password,String confirmPassword){
        return password.equals(confirmPassword);
    }


    private void  isUserExist(String email){
        Call<AuthUserCheckResponse> authUserCheckResponseCall = Client.getClient().create(ApiInterface.class).isRegisteredUser(email);
            authUserCheckResponseCall.enqueue(new Callback<AuthUserCheckResponse>() {
                @Override
                public void onResponse(Call<AuthUserCheckResponse> call, Response<AuthUserCheckResponse> response) {
                    if(response.body().getError()){
                        String pass = passwordEditText.getText().toString();
                        String em = emailEditText.getText().toString();
                        String con_pass = confirmPasswordEditText.getText().toString();
                        if(passwordMatch(pass,con_pass)){
                            Intent signup = new Intent(EmailSignup.this, OtpVerification.class);
                            signup.putExtra("email",em);
                            signup.putExtra("password",pass);
                            signup.putExtra("isPhoneAuth",false);
                            startActivity(signup);
                        }else{
                            confirmPasswordEditText.setError("Password not match");
                        }
                    }else{
                        if(response.body().getProvider().equals("1")){
                            emailEditText.setError("User already exist Please! login");
                        }else if(response.body().getProvider().equals("2")){
                            emailEditText.setError("User already exist With google Sign-in");
                        }else{
                            emailEditText.setError("User already exist Please! login");
                        }
                    }
                }

                @Override
                public void onFailure(Call<AuthUserCheckResponse> call, Throwable t) {

                }
            });
    }

    void DialogBoxShow(View v){
        final AlertDialog.Builder builder = new AlertDialog.Builder(EmailSignup.this,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialog, viewGroup, false);
        builder.setView(dialogView);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();
    }
}