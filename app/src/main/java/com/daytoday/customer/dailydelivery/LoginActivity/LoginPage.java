package com.daytoday.customer.dailydelivery.LoginActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.daytoday.customer.dailydelivery.HomeScreen.Model.AuthUser;
import com.daytoday.customer.dailydelivery.HomeScreen.View.HomeScreenActivity;
import com.daytoday.customer.dailydelivery.Network.ApiInterface;
import com.daytoday.customer.dailydelivery.Network.Client;
import com.daytoday.customer.dailydelivery.Network.Response.AuthUserCheckResponse;
import com.daytoday.customer.dailydelivery.Network.Response.AuthUserResponse;
import com.daytoday.customer.dailydelivery.R;
import com.daytoday.customer.dailydelivery.Utilities.SaveOfflineManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity {
    private int RC_SIGN_IN = 1;
    private Button phoneLogin;
    private Button googleLogin;
    private FirebaseAuth mAuth;
    private AlertDialog alertDialog;

    //for Google Login
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
//    private CallBack;
    //Dialog box
    private ProgressBar loading;
    private TextView heading;
    private TextView subheading;
    private  Button signup;
    private  Button signin;
    private TextInputEditText signinEmail;
    private TextInputEditText signinPassword;
    private Object FirebaseAuthUserCollisionException;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);


        mAuth = FirebaseAuth.getInstance();

//        mCallbackManager = CallbackManager.Factory.create();
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        phoneLogin = findViewById(R.id.btn1);
        googleLogin = findViewById(R.id.btn2);
        signup = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);
        signinEmail = findViewById(R.id.signin_email);
        signinPassword = findViewById(R.id.signin_password);
        //dialog Box
        loading = findViewById(R.id.loading);
        heading =findViewById(R.id.heading);
        subheading =findViewById(R.id.subHeading);
    //==============================Signin======================================================

        signin.setOnClickListener(view -> {
            if(inputValidation(signinEmail.getText().toString(),signinPassword.getText().toString())) {
                DialogBoxShow(view);
                signInWithEmail(signinEmail.getText().toString(), signinPassword.getText().toString());
            }
        });

        //==========================Signup======================================================
        signup.setOnClickListener(view -> {
            Intent signup = new Intent(LoginPage.this, EmailSignup.class);
            startActivity(signup);
        });
        //==========================Phone Login==================================================
        phoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginPage.this, PhoneVerification.class);
                intent.putExtra("isPhoneAuth",true);
                startActivity(intent);
            }
        });


        //============================Google Login=======================================

         //================= configuration =================
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogBoxShow(v);
            googleLogin.setEnabled(false);
            GoogleSignIn();
        }
    });

    }

    private boolean inputValidation(String e, String p) {
        if(e.length()==0){
            signinEmail.setError("Please enter email .");
            return  false;
        }else if(p.length()==0){
            signinPassword.setError("Please enter password");
            return  false;
        }else{
            return  true;
        }
    }

    private void GoogleSignIn() {
        Log.w("GoogleLogin", "signInWithCredentialIntent");
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        Log.w("GoogleLogin", "signInWithCredential");
            startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = task.getResult().getUser();
                            if(user!=null){
                                Log.i("LOGIN_USER", "onResponse: API"+task.getResult().getAdditionalUserInfo().isNewUser());
                                if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                    alertDialog.dismiss();
                                    Intent loginIntent = new Intent(LoginPage.this, AdditionalInfo.class);
                                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    loginIntent.putExtra("isPhoneAuth",false);
                                    startActivity(loginIntent);
                                    finish();

                                }else{
                                    loginUser(user.getUid());
                                }
                            }


                        } else {
                            googleLogin.setEnabled(true);
                            alertDialog.dismiss();
                            loading.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"Login failed Please try again",Toast.LENGTH_LONG);
                            Log.w("GoogleLogin", "signInWithCredential:failure", task.getException());
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {

                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(LoginPage.this,"Invalid Login Credentials",Toast.LENGTH_LONG);
                            } catch(com.google.firebase.auth.FirebaseAuthUserCollisionException e) {
                                Toast.makeText(LoginPage.this,"The email address is already in use by google account. Please login with google account",Toast.LENGTH_LONG);
                            } catch(Exception e) {

                            }
                        }
                        Log.i("GoogleLogin", "onComplete: "+task.getResult());
                    }


                });
    }

//=====================show Custom Dialog Box================================

    void DialogBoxShow(View v){
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialog, viewGroup, false);
        builder.setView(dialogView);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.i("GoogleLogin", "onActivityResult: "+requestCode+" "+data+" "+resultCode);
        if(requestCode ==RC_SIGN_IN &&data!=null&&resultCode!=0){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("GoogleLogin", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update com.daytoday.business.dailydelivery.MainHomeScreen.UI appropriately
                googleLogin.setEnabled(true);
                loading.setVisibility(View.INVISIBLE);
                Log.w("GoogleLogin", "Google sign in failed", e);
                // ...
            }
        }else{
            googleLogin.setEnabled(true);
            alertDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Please choose one of your google account",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update com.daytoday.business.dailydelivery.MainHomeScreen.UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Log.i("AUTHENT_US", "onStart: "+SaveOfflineManager.getUserAddress(LoginPage.this));
            if(SaveOfflineManager.getUserAddress(LoginPage.this)==null||SaveOfflineManager.getUserAddress(LoginPage.this).length()==0){
                Intent signupIntent = new Intent(LoginPage.this, AdditionalInfo.class);
                if(currentUser.getPhoneNumber()!=null&&currentUser.getPhoneNumber().length()!=0){
                    signupIntent.putExtra("isPhoneAuth",true);
                }else if(currentUser.getEmail()!=null&&currentUser.getEmail().length()!=0){
                    signupIntent.putExtra("isPhoneAuth",false);
                }
                signupIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                signupIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(signupIntent);
                finish();
            }else{
                Intent loginIntent = new Intent(LoginPage.this, HomeScreenActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
                finish();
            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    private void loginUser(String uid) {
        ApiInterface apiInterface= Client.getClient().create(ApiInterface.class);

        Call<AuthUserResponse> loginUserDataCalling = apiInterface.loginUser(uid);
        loginUserDataCalling.enqueue(new Callback<AuthUserResponse>() {
            @Override
            public void onResponse(Call<AuthUserResponse> call, Response<AuthUserResponse> response) {
                alertDialog.dismiss();
                Log.i("LOGIN_USER", "onResponse: "+response.body().getError());
                if(response.body().getError()){
                    Intent loginIntent = new Intent(LoginPage.this, AdditionalInfo.class);
                    loginIntent.putExtra("isPhoneAuth",false);
                    startActivity(loginIntent);
                    finish();
                }else{
                    Log.i("LOGIN_USER", "onResponse: "+response.body().getCustDetails());
                    AuthUser authUser = response.body().getCustDetails().get(0);
                    Log.i("LOGIN_USER", "onResponse: "+authUser.getName());
                    saveOffline(mAuth.getCurrentUser(), authUser.getName(), authUser.getAddress());
                    SendUserHomePage();
                }


            }

            @Override
            public void onFailure(Call<AuthUserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Somting went wrong", Toast.LENGTH_LONG);
            }
        });
    }

    public void SendUserHomePage() {
        Intent loginIntent = new Intent(LoginPage.this, HomeScreenActivity.class);

        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
        finish();
    }

    public void saveOffline(FirebaseUser currentUser, String name, String adress) {
        SaveOfflineManager.setUserName(this, name);
        SaveOfflineManager.setUserId(this, currentUser.getUid());
        SaveOfflineManager.setUserAddress(this, adress);
        SaveOfflineManager.setUserPhoneNumber(this, currentUser.getPhoneNumber());
    }

    private void signInWithEmail(String email,String password){

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i("EMAIL_PASS_AUTH", "onComplete: "+task.getResult().getUser());
                    loginUser(task.getResult().getUser().getUid());
                }else{
                    alertDialog.dismiss();
                    Log.i("EMAIL_PASS_AUTH", "onComplete: "+task.getException());
                    try{
                        throw task.getException();
                    } catch(FirebaseAuthInvalidUserException e) {
                        signinEmail.setError("Invalid email");
                        signinPassword.setError("Invalid password");
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        signinPassword.setError("Invalid login Credentials or You may Sign-in with google");
                    }
                    catch (Exception e){

                    }
                }
            }
        });
    };
    private void  isUserExist(String email,String uid){
        Call<AuthUserCheckResponse> authUserCheckResponseCall = Client.getClient().create(ApiInterface.class).isRegisteredUser(email);
        authUserCheckResponseCall.enqueue(new Callback<AuthUserCheckResponse>() {
            @Override
            public void onResponse(Call<AuthUserCheckResponse> call, Response<AuthUserCheckResponse> response) {
                if (response.body().getError()) {
                    Intent signupIntent = new Intent(LoginPage.this, AdditionalInfo.class);
                    signupIntent.putExtra("isPhoneAuth",false);
                    signupIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    signupIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(signupIntent);
                    finish();
                }else{
                    loginUser(uid);
                }
            }
                @Override
                public void onFailure(Call<AuthUserCheckResponse> call, Throwable t) {

                }
            });
        }
}

