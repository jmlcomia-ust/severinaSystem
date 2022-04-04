package com.example.testois;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import com.example.testois.databinding.ActivityReportGenerationMenuBinding;
import com.example.testois.utilities.Metodos;
import com.example.testois.utilities.severinaDB;



public class ReportGenerationMenu extends DrawerBaseActivity {
    ActivityReportGenerationMenuBinding activityReportGenerationMenuBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReportGenerationMenuBinding = ActivityReportGenerationMenuBinding.inflate(getLayoutInflater());
        setContentView(activityReportGenerationMenuBinding.getRoot());
        allocatedActivityTitle("Generate Reports");
        ActivityCompat.requestPermissions(ReportGenerationMenu.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        Button btn_genreport = findViewById(R.id.btn_genreport);
        severinaDB db = new severinaDB(this);
        Inventory inventory;
        Orders orders;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);

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

    public void createPDF(View view) {
        String filename = "OrderInventoryReport_Severina";
        String filecontent = "OrderInventoryReport_Severina";
        Metodos fop = new Metodos();
        if (fop.write(filename, filecontent)) {
            Toast.makeText(getApplicationContext(),
                    filename + ".pdf created", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getApplicationContext(), "I/O error",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
