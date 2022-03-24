package com.example.testois;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.example.testois.databinding.ActivityReportGenerationMenuBinding;
import com.example.testois.databinding.ActivityViewOrderBinding;


public class ReportGenerationMenu extends DrawerBaseActivity {
    ActivityReportGenerationMenuBinding activityReportGenerationMenuBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReportGenerationMenuBinding = ActivityReportGenerationMenuBinding.inflate(getLayoutInflater());
        setContentView(activityReportGenerationMenuBinding.getRoot());
        allocatedActivityTitle("Generate Reports");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report,menu);

        return true;

        // Configure the search info and add any event listeners...
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.share) {
            Toast.makeText(getApplicationContext(), "You clicked share", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.about) {
            Toast.makeText(getApplicationContext(), "You clicked about", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.exit) {
            Toast.makeText(getApplicationContext(), "You clicked exit", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.search) {
            Toast.makeText(getApplicationContext(), "You clicked search", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.profile) {
            Toast.makeText(getApplicationContext(), "You clicked profile", Toast.LENGTH_SHORT).show();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}