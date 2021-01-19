package com.daytoday.customer.dailydelivery.LoginActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import com.daytoday.customer.dailydelivery.HomeScreen.View.HomeScreenActivity;
import com.daytoday.customer.dailydelivery.Network.ApiInterface;
import com.daytoday.customer.dailydelivery.Network.Client;
import com.daytoday.customer.dailydelivery.Network.Response.GeocodingResponse;
import com.daytoday.customer.dailydelivery.Network.Response.YesNoResponse;
import com.daytoday.customer.dailydelivery.R;
import com.daytoday.customer.dailydelivery.Utilities.AppConstants;
import com.daytoday.customer.dailydelivery.Utilities.SaveOfflineManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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

public class AdditionalInfo extends AppCompatActivity {

    private static final String TAG = "ADDITIONAL_INFO";
    String ft;
    String lt;
    String userAddress;
    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private TextInputEditText addressEditText;
    private Button goHome;
    private ImageButton getLocation;
    private FirebaseAuth mAuth;
    private ApiInterface apiInterface;
    private boolean isPhoneAuth;
    private String email;
    private AlertDialog alertDialog;
    private String password;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_info);

        //Action bar hide and color change ------------------------------------------
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        //Initialize views and Firebase-------------------------
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        addressEditText = findViewById(R.id.user_address);
        goHome = findViewById(R.id.go_home);
        getLocation = findViewById(R.id.getLocation);
        mAuth = FirebaseAuth.getInstance();
        apiInterface = Client.getClient().create(ApiInterface.class);
        isPhoneAuth = getIntent().getBooleanExtra("isPhoneAuth", false);
        FirebaseUser user =mAuth.getCurrentUser();
        if(!isPhoneAuth){
            if(user!=null && user.getDisplayName() != null && user.getDisplayName().isEmpty()){
                String name[] = user.getDisplayName().split(" ");
                if(name.length==1){
                    firstName.setText(name[0]);
                }else{
                    firstName.setText(name[0]);
                    lastName.setText(name[1]);
                }
            }else{
                email = getIntent().getStringExtra("email");
                password = getIntent().getStringExtra("password");
            }
        }

        goHome.setOnClickListener(view -> {
            ft = firstName.getText().toString();
            lt = lastName.getText().toString();

            String userAddress = addressEditText.getText().toString();
            if(isInputValid(ft,lt,userAddress)) {
                if (user != null) {
                    DialogBoxShow(view);
                    if(user.getEmail()!=null&&user.getEmail().length()!=0){
                        updateUserToFirebase(ft + " " + lt, user, userAddress,2);
                    }else{
                        updateUserToFirebase(ft + " " + lt, user, userAddress,0);
                    }
                } else {
                    Log.i(TAG, "onCreate: " + email + password);
                    if (email != null && password != null) {
                        DialogBoxShow(view);
                        signupWithEmail(email, password, ft + " " + lt, userAddress);
                    }
                }
            }
        });

        getLocation.setOnClickListener(view -> {
            getLocationPermission();
        });
    }

    public void createUserProfile(String name, FirebaseUser u, String a,int provider) {

        Log.d(TAG, "createUserProfile: " + u.getUid() + name + u.getPhoneNumber() + a);
        Call<YesNoResponse> createusercall = apiInterface.addCustUserDetails(u.getUid(),name,u.getPhoneNumber(),a,u.getEmail(),provider);


        createusercall.enqueue(new Callback<YesNoResponse>() {
            @Override
            public void onResponse(Call<YesNoResponse> call, Response<YesNoResponse> response) {

                saveOffline(u, name, a);
                SendUserHomePage();
            }

            @Override
            public void onFailure(Call<YesNoResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Couldn't Login. Please Try Again", Toast.LENGTH_SHORT).show();
                //TODO logout user from here
            }
        });
    }

    public void saveOffline(FirebaseUser currentUser, String name, String adress) {
        Log.i(TAG, "saveOffline: "+adress);
        SaveOfflineManager.setUserName(this, name);
        SaveOfflineManager.setUserId(this, currentUser.getUid());
        SaveOfflineManager.setUserAddress(this, adress);
        SaveOfflineManager.setUserPhoneNumber(this, currentUser.getPhoneNumber());
    }

    public void SendUserHomePage() {
        alertDialog.dismiss();
        Intent loginIntent = new Intent(AdditionalInfo.this, HomeScreenActivity.class);
        loginIntent.putExtra("Name", ft + " " + lt);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
        finish();
    }

    private void getLocationPermission() {

        Dexter
                .withContext(getApplicationContext())
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            Log.i("LOCATION_TACK", "permisson");
                            getCurrentLocation();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(getApplicationContext(), "Please allow external storage access to save QR code", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    private void getCurrentLocation() {
        Log.i("LOCATION_TRACK","GETL");
        LocationManager locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.i("LOCATION_TRACK",isGPSEnable+" "+isNetworkEnable);
        if (isGPSEnable) {
            Log.i("LOCATION_TRACK", "getCurrentLocation: "+(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.i("LOCATION_TRACK", "getLocation: " + location.getLatitude() + " " + location.getLongitude());
                getAddress(location.getLatitude(),location.getLongitude());
                return;
            } else {
                    Toast.makeText(getApplicationContext(),"Please allow permission to find your current location",Toast.LENGTH_LONG).show();
            }
        }else if(isNetworkEnable){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Log.i("LOCATION_TRACK", "getLocation: " + location.getLatitude() + " " + location.getLongitude());
            getAddress(location.getLatitude(),location.getLongitude());
            return;
        } else {
            Toast.makeText(getApplicationContext(),"Please allow permission to find your current location",Toast.LENGTH_LONG).show();
        }
        }else{
            Toast.makeText(getApplicationContext(),"Please on your GPS or Mobile Data",Toast.LENGTH_LONG).show();
        }

    }

    private  void getAddress(double lat ,double lon){
        ApiInterface  geocodingApiInterface = Client.getGeocodingClient().create(ApiInterface.class);
        Call<GeocodingResponse> geocodingResponseCall = geocodingApiInterface.getReverseGeocoding(lat,lon,18,0,"jsonv2");

        geocodingResponseCall.enqueue(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                Log.i("LOCATION_TACK", "onResponse: "+response.body().getDisplayName());
                if(response.body().getError()!=null&&response.body().getError() == true){
                    Toast.makeText(getApplicationContext(),"Location not fonund",Toast.LENGTH_LONG).show();
                }else{
                    addressEditText.setText(response.body().getDisplayName());
                }

            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void signupWithEmail(String email,String password,String name,String addrs){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    updateUserToFirebase(name,task.getResult().getUser(),addrs,1);

                }else{
                    alertDialog.dismiss();
                        try {
                            throw task.getException();
                        } catch(FirebaseAuthWeakPasswordException e) {

                        } catch(FirebaseAuthInvalidCredentialsException e) {

                        } catch(FirebaseAuthUserCollisionException e) {
                           Toast.makeText(AdditionalInfo.this,"The email address is already in use by google account. Please login with google account",Toast.LENGTH_LONG).show();
                        } catch(Exception e) {

                        }

                }
            }
        });
    }


    private void updateUserToFirebase(String name,FirebaseUser user,String address,int provider){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");

                            createUserProfile(name, user, address,provider);

                        }else{
                             alertDialog.dismiss();
                            Toast.makeText(AdditionalInfo.this,"Something went wrong!",Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private boolean isInputValid(String ftName,String ltName,String userAddress){
        if (ft.isEmpty() || lt.isEmpty() || userAddress.isEmpty()) {
            firstName.setError("Please enter first name");
            lastName.setError("Please enter last name");
            addressEditText.setError("please enter your address");
            return  false;
        } else if (!(ft.matches("^[A-Za-z]+$") && lt.matches("^[a-zA-Z]+$"))) {
            firstName.setError("Invalid first name ");
            lastName.setError("Invalid last name");
                return  false;
        } else {
            return  true;
        }


    }
    void DialogBoxShow(View v){
        final AlertDialog.Builder builder = new AlertDialog.Builder(AdditionalInfo.this,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialog, viewGroup, false);
        builder.setView(dialogView);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }
}