package com.example.testois;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class CourierDrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout courierDrawerLayout;

    @Override
    public void setContentView(View view) {
        courierDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_courier_drawer_base, null);
        FrameLayout courierContainer = courierDrawerLayout.findViewById(R.id.courierActivityContainer);
        courierContainer.addView(view);
        super.setContentView(courierDrawerLayout);

        Toolbar courierToolbar = courierDrawerLayout.findViewById(R.id.courierToolBar);
        setSupportActionBar(courierToolbar);

        NavigationView courierNavigationView = courierDrawerLayout.findViewById(R.id.nav_courierView);
        courierNavigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle courierToggle = new ActionBarDrawerToggle(this, courierDrawerLayout, courierToolbar, R.string.menu_drawer_open, R.string.menu_drawer_close);
        courierDrawerLayout.addDrawerListener(courierToggle);
        courierToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        courierDrawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()) {
            case R.id.nav_couriervorder:
                startActivity(new Intent(this, CourierDashboardOrder.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_couriermorder:
                startActivity(new Intent(this, CourierViewOrder.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_courierlogout:
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(0, 0);
                break;
            default:
                startActivity(new Intent(this, CourierDashboardActivity.class));
                overridePendingTransition(0,0);
        }

        return false;
    }

    protected void allocatedActivityTitle(String titleString) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }
}