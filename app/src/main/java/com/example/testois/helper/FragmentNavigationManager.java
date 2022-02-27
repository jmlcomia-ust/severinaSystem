package com.example.testois.helper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testois.BuildConfig;
import com.example.testois.DashboardInventory;
import com.example.testois.R;
import com.example.testois.fragments.FragmentContent;
import com.example.testois.theinterface.NavigationManager;

public class FragmentNavigationManager implements NavigationManager{
    private static FragmentNavigationManager mInstance;

    private FragmentManager mFragmentManager;
    private DashboardInventory dashboardInventory;

    public static FragmentNavigationManager getmInstance(DashboardInventory dashboardInventory)
    {
        if(mInstance == null)
            mInstance = new FragmentNavigationManager();
        mInstance.configure(dashboardInventory);
        return mInstance;
    }

    private void configure(DashboardInventory dashboardInventory) {
        dashboardInventory = dashboardInventory;
        mFragmentManager = dashboardInventory.getSupportFragmentManager();
    }

    @Override
    public void showFragment(String title) {

        showFragment(FragmentContent.newInstance(title), false);

    }

    private void showFragment(Fragment fragmentContent, boolean b) {
        FragmentManager fm = mFragmentManager;
        FragmentTransaction ft = fm.beginTransaction().replace(R.id.container,fragmentContent);
        ft.addToBackStack(null);
        if(b || !BuildConfig.DEBUG)
            ft.commitAllowingStateLoss();
        else
            ft.commit();
        fm.executePendingTransactions();
    }


}