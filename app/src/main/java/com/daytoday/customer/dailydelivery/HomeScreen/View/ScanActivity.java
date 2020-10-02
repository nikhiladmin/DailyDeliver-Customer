package com.daytoday.customer.dailydelivery.HomeScreen.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.daytoday.customer.dailydelivery.LoginActivity.LoginPage;
import com.daytoday.customer.dailydelivery.LoginActivity.PhoneVerification;
import com.daytoday.customer.dailydelivery.R;
import com.google.android.gms.vision.barcode.Barcode;

import java.security.Policy;
import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class ScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageButton goBack = findViewById(R.id.goBack);
        ImageButton flash_on = findViewById(R.id.flash_on);
        ImageButton flash_off = findViewById(R.id.flash_off);

        goBack.setOnClickListener(view -> {
            Intent intent = new Intent(ScanActivity.this, HomeScreenActivity.class);
            startActivity(intent);
        });


        flash_off.setOnClickListener(v -> {
                FlashLight(true);
                flash_off.setVisibility(View.INVISIBLE);
                flash_on.setVisibility(View.VISIBLE);
        });

        flash_on.setOnClickListener(v -> {
            FlashLight(false);
            flash_on.setVisibility(View.INVISIBLE);
            flash_off.setVisibility(View.VISIBLE);
        });
    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onScanned(Barcode barcode) {
        Log.i("msg",barcode.displayValue);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Answer", barcode.displayValue);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {

    }


    public void FlashLight(boolean status) {
        CameraManager mCameraManager;
        try {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                mCameraManager =(CameraManager) getSystemService(Context.CAMERA_SERVICE);
                String cameraId = mCameraManager.getCameraIdList()[0];
                mCameraManager.setTorchMode(cameraId,status);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("FLASH_L", "FlashLight: "+e.getMessage());
            Toast.makeText(getBaseContext(), "Exception throws in turning on flashlight."+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}