package com.daytoday.customer.dailydelivery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class HomeScreenActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        toolbar=findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        if(savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.switchFragment, new ProductFragment()).commit();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myproducts:
                getSupportFragmentManager().beginTransaction().replace(R.id.switchFragment, new ProductFragment()).commit();
                break;
            case R.id.search:
                getSupportFragmentManager().beginTransaction().replace(R.id.switchFragment, new SearchFragment()).commit();
                break;
            case R.id.notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.switchFragment, new NotificationFragment()).commit();
                break;
            case R.id.myprofile:
                getSupportFragmentManager().beginTransaction().replace(R.id.switchFragment, new UserFragment()).commit();
                break;
        }
        return true;
    }

    //-------------------------inflating search icon on navigation bar-------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu_top, menu);
        return true;
    }
    //------------------action on search icon on navigation bar-------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan_business:
                startActivityForResult(new Intent(HomeScreenActivity.this,ScanActivity.class),1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Log.i("msg",data.getStringExtra("Answer"));
            check(data.getStringExtra("Answer"));
        }
    }

    private void check(String Uid) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Buss_Info").document(Uid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        Log.i("msg", String.valueOf(documentSnapshot.exists()));
                        if (documentSnapshot.exists())
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenActivity.this);
                            builder.setTitle("Add Business").setMessage("Do You Want To Add " + documentSnapshot.get("Name") + " Business");
                            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AddBuisness(Uid);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.show();
                        }
                        else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenActivity.this);
                            builder.setTitle("Wrong QR Code").setMessage("You Had Scanned Wrong QR-Code");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.show();
                        }
                    }
                });
    }

    private void AddBuisness(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        reference.child("Buss_Cust_Rel").child(id).child(currentuser.getUid()).setValue(true);
        reference.child("Cust_Buss_Rel").child(currentuser.getUid()).child(id).setValue(true);
    }
}


