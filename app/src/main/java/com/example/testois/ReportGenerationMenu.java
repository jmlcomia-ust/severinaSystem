package com.example.testois;

import android.Manifest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.testois.dao.Report;
import com.example.testois.databinding.ActivityReportGenerationMenuBinding;
import com.example.testois.utilities.severinaDB;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@SuppressLint("all")
public class ReportGenerationMenu extends DrawerBaseActivity  {
    ActivityReportGenerationMenuBinding activityReportGenerationMenuBinding;
    TableLayout tableLayout;
    severinaDB db;
    public SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
    public SimpleDateFormat sdflong = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
    public final String date = sdf.format(System.currentTimeMillis());
    public final String datelong = sdflong.format(System.currentTimeMillis());
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return true;
        // Configure the search info and add any event listeners...
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) {
            if(!db.checkExistingData("db_order", "ord_id", "'1'")){
                Log.e("Existing Data Check", " no data in ORDERS TABLE. CLEARING REPORTS. ");
                db.deleteReports();
                Intent i = new Intent(ReportGenerationMenu.this, ReportGenerationMenu.class);
                startActivity(i);
                finish();
                return true;
            }
            else{
                Intent i = new Intent(ReportGenerationMenu.this, ReportGenerationMenu.class);
                startActivity(i);
                finish();
                return true;
            }
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
        List<Report> reportList = db.getReportList();
        BuildTable();

        btn_genreport.setOnClickListener(v -> {
            if(reportList.size() == 0){ Toast.makeText(getApplicationContext(), "Sorry. There are no data to report.", Toast.LENGTH_SHORT).show();}
            else{
                try {
                    showPdf( "SeverinaOIS-Report-For" + datelong);
                } catch (DocumentException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showPdf(String reportName) throws IOException, DocumentException {
            String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+File.separator+"Report Logs";
            File file = new File(dir, reportName + "-"+ datelong +".pdf");

                if (!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }

            Cursor c1 = db.getReadableDatabase().rawQuery("SELECT * FROM db_report ", null);
            Document document = new Document();  // create the document
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            //insert severina logo
            Drawable logo = ReportGenerationMenu.this.getResources().getDrawable(R.drawable.severina);
            Bitmap bitmap = ((BitmapDrawable) logo).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapLogo = stream.toByteArray();

            PdfPTable header = new PdfPTable(3);
            header.setWidthPercentage(100);
            float[] fl = new float[]{20, 60, 20};
            header.setWidths(fl);
            PdfPCell cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            Image imgReportLogo = Image.getInstance(bitmapLogo);
            imgReportLogo.setAbsolutePosition(330f, 642f);
            imgReportLogo.setScaleToFitHeight(false);
            cell.addElement(imgReportLogo);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            header.addCell(cell);

            cell = new PdfPCell();
            // Heading
            //BaseFont font = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.BOLD, BaseColor.BLACK);

            //Creating Chunk
            Chunk titleChunk = new Chunk("Severina Order and Inventory System", titleFont);
            //Paragraph
            Paragraph titleParagraph = new Paragraph(titleChunk);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
                Paragraph subtitleParagraph = new Paragraph("Inventory and Order Report");
                subtitleParagraph.setAlignment(Element.ALIGN_CENTER);
                    Paragraph subsubtitleParagraph = new Paragraph("Date: "+date);
                    subsubtitleParagraph.setAlignment(Element.ALIGN_CENTER);

            cell.addElement(titleParagraph);
            cell.addElement(subtitleParagraph);
            cell.addElement(subsubtitleParagraph);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setPadding(5);
            header.addCell(cell);

            cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setPadding(2);
            header.addCell(cell);

            //creates space between header and report table
            PdfPTable space = new PdfPTable(1);
            space.setWidthPercentage(100);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(1);
            cell.addElement(new Paragraph(" "));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setPadding(5);
            space.addCell(cell);

            //column text
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

        Paragraph col_id_par = new Paragraph("ID");
        col_id_par.setAlignment(Element.ALIGN_CENTER);
        Paragraph col_date_par = new Paragraph("DELIVERY DATE");
        col_date_par.setAlignment(Element.ALIGN_CENTER);
        Paragraph col_name_par = new Paragraph("INVENTORY NAME");
        col_name_par.setAlignment(Element.ALIGN_CENTER);
        Paragraph col_invqty_par = new Paragraph("STOCK/S LEFT");
        col_invqty_par.setAlignment(Element.ALIGN_CENTER);
        Paragraph col_ordqty_par = new Paragraph("STOCK/S ORDERED");
        col_ordqty_par.setAlignment(Element.ALIGN_CENTER);

            cell = new PdfPCell();
            cell.addElement(col_id_par);
            cell.setBorder(Rectangle.BOX);
            cell.setColspan(1);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.addElement(col_date_par);
            cell.setBorder(Rectangle.BOX);
            cell.setColspan(1);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setPadding(5);

            table.addCell(cell);
            cell = new PdfPCell();
            cell.addElement(col_name_par);
            cell.setBorder(Rectangle.BOX);
            cell.setColspan(1);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.addElement(col_invqty_par);
            cell.setBorder(Rectangle.BOX);
            cell.setColspan(1);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.addElement(col_ordqty_par);
            cell.setBorder(Rectangle.BOX);
            cell.setColspan(1);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);

            while (c1.moveToNext()) {
                String id = c1.getString(0);
                String date = c1.getString(1);
                String name = c1.getString(2);
                String inv_qty = c1.getString(3);
                String ord_qty = c1.getString(4);

                Paragraph id_par = new Paragraph(id);
                id_par.setAlignment(Element.ALIGN_CENTER);
                Paragraph date_par = new Paragraph(date);
                date_par.setAlignment(Element.ALIGN_CENTER);
                Paragraph name_par = new Paragraph(name);
                name_par.setAlignment(Element.ALIGN_CENTER);
                Paragraph invqty_par = new Paragraph(inv_qty);
                invqty_par.setAlignment(Element.ALIGN_CENTER);
                Paragraph ordqty_par = new Paragraph(ord_qty);
                ordqty_par.setAlignment(Element.ALIGN_CENTER);


                cell = new PdfPCell();
                cell.addElement(id_par);
                cell.setBorder(Rectangle.BOX);
                cell.setColspan(1);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.addElement(new Paragraph(date_par));
                cell.setBorder(Rectangle.BOX);
                cell.setColspan(1);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.addElement(new Paragraph(name_par));
                cell.setBorder(Rectangle.BOX);
                cell.setColspan(1);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.addElement(new Paragraph(invqty_par));
                cell.setBorder(Rectangle.BOX);
                cell.setColspan(1);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.addElement(ordqty_par);
                cell.setBorder(Rectangle.BOX);
                cell.setColspan(1);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);

               // table.addCell(id);
               // table.addCell(date);
               // table.addCell(name);
               // table.addCell(inv_qty);
               // table.addCell(ord_qty);

            }
            c1.close();
            document.add(header);
            document.add(space);
            document.add(space);
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
            Toast.makeText(this, "PDF created. Check on "+file+" for more details.", Toast.LENGTH_LONG).show();
        }
        }

    //REF: https://github.com/ch0nch0l/AndroPDF
    public void BuildTable() {
        TableRow rowHeader = new TableRow(this);
        rowHeader.setDividerPadding(View.VISIBLE);
        rowHeader.setGravity(Gravity.CENTER_VERTICAL);
        rowHeader.setMinimumHeight(10);
        rowHeader.setBackgroundColor(Color.parseColor("#598F70"));
        rowHeader.setBackgroundResource(R.drawable.border);
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText = {"ID", "DELIVERY DATE", "ITEM NAME","STOCK/S LEFT","STOCK/S ORDERED"};

        for (String c : headerText) {

            TextView tv = new TextView(this);

            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundColor(Color.parseColor("#598F70"));
            tv.setTextSize(20);
            tv.setMaxWidth(100);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(c);


            rowHeader.addView(tv);
            rowHeader.setBackgroundColor(Color.parseColor("#598F70"));
        }
        tableLayout.addView(rowHeader);
        tableLayout.setStretchAllColumns(true);

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

                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                    String[] colText = {report_id + "", report_date, report_name,report_qty_inv,report_qty_ord};

                    for (String text : colText) {
                        TextView tv = new TextView(this);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tv.setGravity(Gravity.CENTER_HORIZONTAL);
                        tv.setTextSize(18);
                        tv.setPadding(5, 5, 5, 5);
                        tv.setText(text);
                        tv.setMaxWidth(100);
                        tv.setBackgroundResource(R.drawable.border);
                        row.setMinimumWidth(10);
                        row.addView(tv);
                    }
                    tableLayout.addView(row);
                }
                cursor.close();
                tableLayout.setStretchAllColumns(true);
                //tableLayout.setShrinkAllColumns(true);
            }
        } catch (SQLiteException e) { e.printStackTrace(); }
    }
}
