package com.daytoday.customer.dailydelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class HomeScreenActivity extends AppCompatActivity {


    private BottomAppBar bottomAppBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        bottomAppBar = findViewById(R.id.bottomBar);
        setSupportActionBar(bottomAppBar);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bottom_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.myproducts)
        {
            Intent intent = new Intent(HomeScreenActivity.this,ProductFragment.class);
            //getSupportFragmentManager().beginTransaction().replace();
            startActivity(intent);
            finish();
            return true;
        }
        if (id==R.id.scan)
        {
            Intent intent = new Intent(HomeScreenActivity.this,ScanFragment.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id==R.id.notification)
        {
            Intent intent = new Intent(HomeScreenActivity.this,NotificationFragment.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id==R.id.myprofile)
        {
            Intent intent = new Intent(HomeScreenActivity.this,UserFragment.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


