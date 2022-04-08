package com.example.testois;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.testois.dao.Report;
import com.example.testois.databinding.ActivityReportGenerationMenuBinding;
import com.example.testois.utilities.ListAllReport;
import com.example.testois.utilities.severinaDB;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;


public class ReportGenerationMenu extends DrawerBaseActivity{
    ActivityReportGenerationMenuBinding activityReportGenerationMenuBinding;
    TableLayout tableLayout;
    severinaDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReportGenerationMenuBinding = ActivityReportGenerationMenuBinding.inflate(getLayoutInflater());
        setContentView(activityReportGenerationMenuBinding.getRoot());
        allocatedActivityTitle("Generate Reports");
        ActivityCompat.requestPermissions(ReportGenerationMenu.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        db = new severinaDB(this);
        Button btn_genreport = findViewById(R.id.btn_genreport);

        btn_genreport.setOnClickListener(v -> {

            ListAllReport report = new ListAllReport();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
            String date = sdf.format(System.currentTimeMillis());
            BuildTable();

            report.createPDF(getApplicationContext(), "InventoryOrderReport-" + date);
            showPdf("InventoryOrderReport-" + date);
        });

        tableLayout = (TableLayout) findViewById(R.id.report_table);

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

    private void showPdf(String reportName) {
        // TODO Auto-generated method stub
        String mPath = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString(); //reportName could be any name
        //constructing the PDF file
        File pdfFile = new File(mPath, reportName + ".pdf");
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(pdfFile);
        intent.setDataAndType(uri, "application/pdf");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No Application Available to View PDF", Toast.LENGTH_LONG).show();
        }
    }

    public void BuildTable() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        String date = sdf.format(System.currentTimeMillis());
            db.open();
            Cursor c = db.readEntry(date);

            int rows = c.getCount();
            int cols = c.getColumnCount();

            c.moveToFirst();

            // outer for loop
            for (int i = 0; i < rows; i++) {

                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                // inner for loop
                for (int j = 0; j < cols; j++) {

                    TextView tv = new TextView(this);
                    tv.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    tv.setBackgroundResource(R.color.color2);
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(18);
                    tv.setPadding(0, 5, 0, 5);
                    tv.setText(c.getString(j));

                    row.addView(tv);

                }

                c.moveToNext();

                tableLayout.addView(row);

            }
            db.close();
        }


}