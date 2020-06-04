package com.daytoday.customer.dailydelivery.WalkThrough;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.daytoday.customer.dailydelivery.LoginActivity.LoginPage;
import com.daytoday.customer.dailydelivery.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class WalkThroughActivity extends AppCompatActivity {
    private ViewPager walk_through_Pager;
    private IntroViewPagerAdapter introViewPagerAdapter;
    private TabLayout tab_indicator_of_walkthrough;
    private Button get_started__btn;
    private Animation btn_Get_Started_Anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();


        if(restorePrefData()) {
            Intent intent = new Intent(getApplicationContext(), LoginPage.class);
            finish();
            startActivity(intent);
        }

        //Tab Inndicator
        tab_indicator_of_walkthrough = findViewById(R.id.tab_indicator_of_walkthrough);
        get_started__btn = findViewById(R.id.get_started_btn);
        btn_Get_Started_Anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.btngetstarted_animation);

        //fill the list screen
        final List<ScreenItem> list_of_items = new ArrayList<>();
        list_of_items.add(new ScreenItem(R.drawable.logo_launcher,"Experience a New Way to manage your Daily Needs"));
        list_of_items.add(new ScreenItem(R.drawable.logo_launcher,"All Your Daily Needs At your Fingertips"));


        walk_through_Pager = findViewById(R.id.Walk_through_pager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,list_of_items);
        walk_through_Pager.setAdapter(introViewPagerAdapter);


        // setup tab indicator with walkthrough
        tab_indicator_of_walkthrough.setupWithViewPager(walk_through_Pager);

        // TabLayout Change Listener

        tab_indicator_of_walkthrough.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == list_of_items.size()-1) {
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        get_started__btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(WalkThroughActivity.this, LoginPage.class);

                startActivity(intent);
                // save the data into shared preference
               savePrefData();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        double displaydp = Math.sqrt(dpHeight*dpHeight + dpWidth * dpWidth);
        Log.i("ans is","dpheight" + dpHeight);
        Log.i("ans is","dpwidth" + dpWidth);
        get_started__btn.setWidth((int)(dpWidth * 2));
        get_started__btn.setTextSize((float) (displaydp * 0.025));
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myprefs",MODE_PRIVATE);
        Boolean isIntroOpened = pref.getBoolean("isIntroOpened",false);
        return isIntroOpened;
    }

    private void savePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myprefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened",true);
        editor.commit();
    }

    private void loadLastScreen(){
        get_started__btn.setVisibility(View.VISIBLE);
        get_started__btn.setAnimation(btn_Get_Started_Anim);
    }
}
