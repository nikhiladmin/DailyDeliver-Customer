package com.daytoday.customer.dailydelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeScreenActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
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
                Toast.makeText(this, "To be Added soon", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


