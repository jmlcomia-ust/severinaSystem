package com.example.testois;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.testois.dao.User;
import com.google.android.material.navigation.NavigationView;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    User user;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_drawer_open, R.string.menu_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        TextView header_one = navigationView.findViewById(R.id.userName);   //change TextViews in Drawer Header
        //header_one.setText(R.string.header_name);
        TextView header_two = navigationView.findViewById(R.id.userRole);
        //header_two.setText(R.string.header_role);
        TextView header_three = navigationView.findViewById(R.id.userMail);
        //header_three.setText(user.getName());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()) {
            case R.id.nav_dinventory:
                startActivity(new Intent(this, DashboardInventory.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.nav_dorder:
                startActivity(new Intent(this, DashboardOrders.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.nav_vinventory:
                startActivity(new Intent(this, ViewInventory.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.nav_vorder:
                startActivity(new Intent(this, ViewOrder.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.nav_gen_report:
                startActivity(new Intent(this, ReportGenerationMenu.class)); //change to gen report, do same edit on other acts for gen report
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.nav_instruc:
                startActivity(new Intent(this, UserTutorial.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_logout:
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            default:
                startActivity(new Intent(this, DashboardActivity.class));
                overridePendingTransition(0,0);
        }

        return false;
    }

    protected void allocatedActivityTitle(String titleString) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
    }
}