package com.daytoday.customer.dailydelivery.HomeScreen.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.daytoday.customer.dailydelivery.Network.ApiInterface;
import com.daytoday.customer.dailydelivery.Network.Client;
import com.daytoday.customer.dailydelivery.Network.Response.BussDetailsResponse;
import com.daytoday.customer.dailydelivery.Network.Response.YesNoResponse;
import com.daytoday.customer.dailydelivery.R;
import com.daytoday.customer.dailydelivery.Utilities.AppConstants;
import com.daytoday.customer.dailydelivery.Utilities.SaveOfflineManager;
import com.daytoday.customer.dailydelivery.ViewPagerAdapter;
import com.daytoday.customer.dailydelivery.searchui.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeScreenActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, BusinessAdapter.ClickListener {

    private static final int BUSINESS_CODE = 3;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    ApiInterface apiInterface;
    ViewPager viewPager2;
    MenuItem prevMenuItem;
    ProductFragment productFragment;
    SearchFragment searchFragment;
    NotificationFragment notificationFragment;
    UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        toolbar=findViewById(R.id.toolbar_home);
        viewPager2 = findViewById(R.id.ViewPager);
        apiInterface = Client.getClient().create(ApiInterface.class);
        setSupportActionBar(toolbar);
        viewPager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager2);
    }

    public void setupViewPager(ViewPager viewPager2)
    {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        productFragment = new ProductFragment();
        searchFragment = new SearchFragment();
        notificationFragment = new NotificationFragment();
        userFragment = new UserFragment();
        viewPagerAdapter.addFragment(productFragment);
        viewPagerAdapter.addFragment(searchFragment);
        viewPagerAdapter.addFragment(notificationFragment);
        viewPagerAdapter.addFragment(userFragment);
        viewPager2.setAdapter(viewPagerAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myproducts:
                viewPager2.setCurrentItem(0);
                break;
            case R.id.search:
                viewPager2.setCurrentItem(1);
                break;
            case R.id.notification:
                viewPager2.setCurrentItem(2);
                break;
            case R.id.myprofile:
                viewPager2.setCurrentItem(3);
                break;
        }
        return false;
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
                startActivityForResult(new Intent(HomeScreenActivity.this, ScanActivity.class),BUSINESS_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3) {
            if (data != null) {
//            Log.i("msg",data.getStringExtra("Answer"));
                check(decrypt(data.getStringExtra("Answer")));
            }
        }
    }

    private String decrypt(String answer) {
        //TODO Decrypt The QrCOde Which Are Passing
        return answer;
    }

    private void check(String Uid) {
        //TODO Remove this also and don't remove until asked might contains some neededcode in it
        /*firebaseFirestore.collection("Buss_Info").document(Uid)
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
                });*/
        Call<BussDetailsResponse> bussDetailsResponseCall = apiInterface.getBussList(Uid,FirebaseAuth.getInstance().getCurrentUser().getUid());
        bussDetailsResponseCall.enqueue(new Callback<BussDetailsResponse>() {
            @Override
            public void onResponse(Call<BussDetailsResponse> call, Response<BussDetailsResponse> response) {
                if(!response.body().getError()){
                    BuisnessBottomSheet buisnessBottomSheet = new BuisnessBottomSheet(response.body().getBuss());
                    buisnessBottomSheet.show(getSupportFragmentManager(),"SelectBusiness");
                }
            }

            @Override
            public void onFailure(Call<BussDetailsResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void addBusiness(String uid) {
        Call<YesNoResponse> addBussCustDetails = apiInterface.addBussCustDetails(uid, SaveOfflineManager.getUserId(this));
        addBussCustDetails.enqueue(new Callback<YesNoResponse>() {
            @Override
            public void onResponse(Call<YesNoResponse> call, Response<YesNoResponse> response) {
                if (response.body().getError()){
                    Log.i(AppConstants.ERROR_LOG,"Some Error Occurred in HomeScreenActivity Error is : { " + response.body().getMessage() + " }");
                }
                if (!response.body().getError()) {
                    Toast.makeText(HomeScreenActivity.this, "Business Added", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<YesNoResponse> call, Throwable t) {
                Log.i(AppConstants.ERROR_LOG,"Some Error Occurred in HomeScreenActivity Error is : { " + t.getMessage() + " }");
            }
        });
    }
}


