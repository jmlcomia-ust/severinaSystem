package com.example.testois;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.testois.dao.Inventory;
import com.example.testois.dao.Orders;
import com.example.testois.databinding.ActivityReportGenerationMenuBinding;
import com.example.testois.utilities.GeneraatePDFReport;
import com.example.testois.utilities.ListAllReport;
import com.example.testois.utilities.severinaDB;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ReportGenerationMenu extends DrawerBaseActivity {
    ActivityReportGenerationMenuBinding activityReportGenerationMenuBinding;
    private PdfPCell cell;
    BaseColor headColor = WebColors.getRGBColor("#DEDEDE");
    BaseColor tableHeadColor = WebColors.getRGBColor("#F5ABAB");
    severinaDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReportGenerationMenuBinding = ActivityReportGenerationMenuBinding.inflate(getLayoutInflater());
        setContentView(activityReportGenerationMenuBinding.getRoot());
        allocatedActivityTitle("Generate Reports");
        ActivityCompat.requestPermissions(ReportGenerationMenu.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        Button btn_genreport = findViewById(R.id.btn_genreport);
        severinaDB db = new severinaDB(this);
        btn_genreport.setOnClickListener(v -> {
            // GeneraatePDFReport generate = new GeneraatePDFReport();
            ListAllReport report = new ListAllReport();
            report.createPDF(getApplicationContext(), "ItsYaBoiDennis");
            showPdf("ItsYaBoiDennis");
        });
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
        String mPath = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/test.pdf"; //reportName could be any name
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
}