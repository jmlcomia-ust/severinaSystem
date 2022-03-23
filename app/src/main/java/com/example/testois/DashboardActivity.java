package com.example.testois;

import android.os.Bundle;

import com.example.testois.databinding.ActivityDashboardBinding;

public class DashboardActivity extends DrawerBaseActivity {

    ActivityDashboardBinding activityDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());
        allocatedActivityTitle("Dashboard");
    }
}

//FIRST TIME USERS APP INTRO PAGE BETWEEN LOGIN AND DASHBOARD