package com.daytoday.customer.dailydelivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class HomeScreenActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager pager;
    FloatingActionButton floatingActionButton;
    private int[] tabIcons = {
            R.drawable.ic_product_black_24dp,
            R.drawable.ic_center_focus_strong_black_24dp,
            R.drawable.ic_add_alert_black_24dp,
            R.drawable.ic_profile_black_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        tabLayout = findViewById(R.id.bottomTabLayout);
        pager = findViewById(R.id.switchFragment);
        floatingActionButton = findViewById(R.id.fab);
        BottomTabAdapter adapter = new BottomTabAdapter(getSupportFragmentManager(),this,tabLayout.getTabCount());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() != 2)
                    pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(2);
            }
        });
        setupTabIcons();
    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(3).setIcon(tabIcons[2]);
        tabLayout.getTabAt(4).setIcon(tabIcons[3]);
    }

}
