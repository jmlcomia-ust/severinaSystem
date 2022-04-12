package com.example.testois;

import android.Manifest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import com.example.testois.databinding.ActivityReportGenerationMenuBinding;
import com.example.testois.utilities.severinaDB;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Objects;

@SuppressLint("all")
public class ReportGenerationMenu extends DrawerBaseActivity  {
    ActivityReportGenerationMenuBinding activityReportGenerationMenuBinding;
    TableLayout tableLayout;
    severinaDB db;
    public SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
    public final String date = sdf.format(System.currentTimeMillis());

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return true;
        // Configure the search info and add any event listeners...
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) {
            Intent i = new Intent(ReportGenerationMenu.this, ReportGenerationMenu.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReportGenerationMenuBinding = ActivityReportGenerationMenuBinding.inflate(getLayoutInflater());
        setContentView(activityReportGenerationMenuBinding.getRoot());
        allocatedActivityTitle("Generate Reports");
        ActivityCompat.requestPermissions(ReportGenerationMenu.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        db = new severinaDB(this);
        tableLayout = findViewById(R.id.report_table);
        Button btn_genreport = findViewById(R.id.btn_genreport);
        BuildTable();

        btn_genreport.setOnClickListener(v -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
            String date = sdf.format(System.currentTimeMillis());
            try {
                showPdf( "SeverinaOIS-Report-For" + date);
            } catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
            }
        });
    }

    private void showPdf(String reportName) throws FileNotFoundException, DocumentException {
            String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+File.separator+"Report Logs";
            File file = new File(dir, reportName + "-"+ date +".pdf");

                if (!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }

            Cursor c1 = db.getReadableDatabase().rawQuery("SELECT * FROM db_report ", null);
            Document document = new Document();  // create the document
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Paragraph p3 = new Paragraph();
            p3.add("Your Log History for \n");
            document.add(p3);

            PdfPTable table = new PdfPTable(5);
            table.addCell("ID");
            table.addCell("DATE");
            table.addCell("INVENTORY NAME");
            table.addCell("NO. OF STOCKS LEFT");
            table.addCell("STOCKS ORDERED TODAY");

            while (c1.moveToNext()) {
                String id = c1.getString(0);
                String date = c1.getString(1);
                String name = c1.getString(2);
                String inv_qty = c1.getString(3);
                String ord_qty = c1.getString(4);

                table.addCell(id);
                table.addCell(date);
                table.addCell(name);
                table.addCell(inv_qty);
                table.addCell(ord_qty);
            }
            c1.close();

            document.add(table);
            document.addCreationDate();
            document.close();
        //REF: https://localcoder.org/couldnt-find-meta-data-for-provider-with-authority
        Uri uriToFile = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", file);

        Intent shareIntent = new Intent(Intent.ACTION_VIEW);
        shareIntent.setDataAndType(uriToFile, "application/pdf");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (shareIntent.resolveActivity(this.getPackageManager()) != null) {
            this.startActivity(shareIntent);
        }
        try {
            startActivity(shareIntent);
        } catch (Exception e) {
            Toast.makeText(this, "No Application Available to View PDF", Toast.LENGTH_LONG).show();
        }
        }

    //REF: https://github.com/ch0nch0l/AndroPDF
    public void BuildTable() {
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText = {"ID", "DATE", "ITEM NAME","STOCK LEFT","ORDERS LEFT"};

        for (String c : headerText) {

            TextView tv = new TextView(this);

            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(c);
            rowHeader.addView(tv);
        }tableLayout.addView(rowHeader);

        // Get data from sqlite database and add them to the table

        // Open the database for reading

        SQLiteDatabase sql = db. getReadableDatabase();

        try {

            String selectQuery = "select * from db_report";

            Cursor cursor = sql.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    // Read columns data
                    int i_id = cursor.getColumnIndex("joined_id");
                    String report_id = cursor.getString(i_id);
                    int i_date = cursor.getColumnIndex("joined_date");
                    String report_date = cursor.getString(i_date);
                    int i_name = cursor.getColumnIndex("joined_invname");
                    String report_name = cursor.getString(i_name);
                    int i_iord = cursor.getColumnIndex("joined_iqty");
                    String report_qty_inv = cursor.getString(i_iord);
                    int i_qord = cursor.getColumnIndex("joined_oqty");
                    String report_qty_ord = cursor.getString(i_qord);

                    TableRow row = new TableRow(this);

                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                    String[] colText = {report_id + "", report_date, report_name,report_qty_inv,report_qty_ord};

                    for (String text : colText) {
                        TextView tv = new TextView(this);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(16);
                        tv.setPadding(5, 5, 5, 5);
                        tv.setText(text);
                        row.addView(tv);
                    }
                    tableLayout.addView(row);
                }
            }
        } catch (SQLiteException e) { e.printStackTrace(); }
    }
}