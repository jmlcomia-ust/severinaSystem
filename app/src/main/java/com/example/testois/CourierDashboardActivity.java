package com.example.testois;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.testois.databinding.ActivityCourierDashboardBinding;
@SuppressLint("all")
public class CourierDashboardActivity extends CourierDrawerBaseActivity {

    ActivityCourierDashboardBinding activityCourierDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCourierDashboardBinding = ActivityCourierDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityCourierDashboardBinding.getRoot());
        allocatedActivityTitle("CourierDashboard");
    }
}