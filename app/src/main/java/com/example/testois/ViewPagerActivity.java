package com.example.testois;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("all")
public class ViewPagerActivity extends AppCompatActivity {

   private OnboardingAdapter onboardingAdapter;
   private LinearLayout layoutOnboardingIndicators;
   private MaterialButton buttonOnboardingAction;
   private PrefManager prefManager;
   String prevStarted = "yes";

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(prevStarted, Boolean.TRUE);
            editor.apply();
        } else {
            moveToSecondary();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

/*
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
 */
        layoutOnboardingIndicators = findViewById(R.id.layoutOnboardingIndicators);
        buttonOnboardingAction = findViewById(R.id.buttonOnboardingAction);

        setupOnboardingItems();
        ViewPager2 onboardingViewPager = findViewById(R.id.onboardingViewPager);
        onboardingViewPager.setAdapter(onboardingAdapter);

        setupOnboardingIndicators();
        setCurrentOnboardingIndicator(0);
        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });

        buttonOnboardingAction.setOnClickListener(view -> {
            if(onboardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()){
                onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);

            }
            else{
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();

            }
        });



    }
    private void setupOnboardingItems(){

        List<OnboardingItem> onboardingItems = new ArrayList<>();
        OnboardingItem item1 = new OnboardingItem();
        item1.setTitle("SEVERINA IOS");
        item1.setDescription("Welcome to Severina AOIS your Inventory System Companion, here is a start up guide on how to use the application. ");
        item1.setImage(R.drawable.severina);

        OnboardingItem item2 = new OnboardingItem();
        item2.setTitle("User Dashboard");
        item2.setDescription("The Dashboard is your main page. clicking on the 3 lines on the upper \nleft opens the Navigation Menu, this is used to navigate through the functionalities.");
        item2.setImage(R.drawable.act_draw);

        OnboardingItem item3 = new OnboardingItem();
        item3.setTitle("Navigational Drawer");
        item3.setDescription(" Here we can see the functionalities like Inventory Management, Order Management, Viewing of Orders, Viewing of Inventory, and Report Generation.");
        item3.setImage(R.drawable.img_1);

        OnboardingItem item4 = new OnboardingItem();
        item4.setTitle("Dashboard Inventory");
        item4.setDescription("Here on Inventory Dashboard, the items could be viewed and a critical stock indicator turns red when that item is low on stock.");
        item4.setImage(R.drawable.ordvie);

        OnboardingItem item5 = new OnboardingItem();
        item5.setTitle("Dashboard Orders");
        item5.setDescription("The Order Dashboard, here the quantity, item, status of delivery, and name of the customer could be viewed.");
        item5.setImage(R.drawable.viewor);

        OnboardingItem item6 = new OnboardingItem();
        item6.setTitle("Manage Inventory");
        item6.setDescription("Here on the Manage Inventory tab we can add, update, and delete items on the inventory. There will also be push notification when an item is low on stock.");
        item6.setImage(R.drawable.manageinv);

        OnboardingItem item7 = new OnboardingItem();
        item7.setTitle("Manage Orders");
        item7.setDescription("Here on the Manage Order tabs we can add, update, and delete customer name, order quantity, order status, and the item name.");
        item7.setImage(R.drawable.manageor);

        OnboardingItem item8 = new OnboardingItem();
        item8.setTitle("Sorting Option");
        item8.setDescription("By clicking the kebab menu, there are sorting options that could be used when viewing and managing of orders. There is also a search option by clicking" +
                "the magnifying glass icon.");
        item8.setImage(R.drawable.sortop);

        onboardingItems.add(item1);
        onboardingItems.add(item2);
        onboardingItems.add(item3);
        onboardingItems.add(item4);
        onboardingItems.add(item5);
        onboardingItems.add(item6);
        onboardingItems.add(item7);
        onboardingItems.add(item8);

        onboardingAdapter = new OnboardingAdapter(onboardingItems);




    }
    private void setupOnboardingIndicators(){
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for(int i = 0; i < indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(), R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }
    }

    private void setCurrentOnboardingIndicator(int index){
        int childCount = layoutOnboardingIndicators.getChildCount();
        for (int i = 0; i < childCount; i++){
            ImageView imageView = (ImageView) layoutOnboardingIndicators.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active)
                );


            }
            else{
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive)
                );
            }
        }
        if(index == onboardingAdapter.getItemCount()-1){

            buttonOnboardingAction.setText("Start");

        }

        else{
            buttonOnboardingAction.setText("Next");

        }

    }
    public void moveToSecondary(){
        // use an intent to travel from one activity to another.
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }
}