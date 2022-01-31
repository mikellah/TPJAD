package com.example.tpjad.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.tpjad.R;
import com.example.tpjad.adapters.VPAdapter2;
import com.google.android.material.tabs.TabLayout;
import com.example.tpjad.fragments.*;

public class MainActivity_Dispatcher extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dispatcher);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);

        VPAdapter2 vpAdapter = new VPAdapter2(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        vpAdapter.addFragment(new Dispatcher_WelcomeScreen(), "Home");
        vpAdapter.addFragment(new Dispatcher_MissionScreen(), "Missions");
        vpAdapter.addFragment(new Dispatcher_TeamsScreen(),"Teams");

        viewPager.setAdapter(vpAdapter);



    }
}