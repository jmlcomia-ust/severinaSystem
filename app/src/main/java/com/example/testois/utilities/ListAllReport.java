package com.example.testois.utilities;

//REFERENCE: https://github.com/shohrabuddin/PdfReportInAndroid
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.testois.ReportGenerationMenu;
import com.example.testois.adapter.CustomViewAdapInv;
import com.example.testois.adapter.CustomViewAdapOrd;
import com.example.testois.dao.Inventory;
import com.example.testois.dao.Orders;
import com.example.testois.dao.Report;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ListAllReport extends Application {
    public SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
    public final String date = sdf.format(System.currentTimeMillis());
    private FileProvider provider = new FileProvider();

        //creating a PdfWriter variable. PdfWriter class is available at com.itextpdf.text.pdf.PdfWriter
        private PdfWriter pdfWriter;
        severinaDB db;

        //we will add some products to arrayListRProductModel to show in the PDF document
        private static ArrayList<Report> arrayListReport = new ArrayList<Report>();

        public boolean createPDF(Context context, String reportName) {

            try {
                //creating a directory in SD card
                String mPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/external_files"; //reportName could be any name
                //constructing the PDF file
                File pdfFile = new File(mPath, reportName+ "-"+ date+".pdf");
                if (!pdfFile.getParentFile().exists()) {
                    pdfFile.getParentFile().mkdirs();
                    pdfFile.createNewFile();
                }


                FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
                //Creating a Document with size A4. Document class is available at  com.itextpdf.text.Document
                Document document = new Document(PageSize.A4);

                //assigning a PdfWriter instance to pdfWriter
                pdfWriter = PdfWriter.getInstance(document, fileOutputStream);

                //PageFooter is an inner class of this class which is responsible to create Header and Footer
                PageHeaderFooter event = new PageHeaderFooter();
                pdfWriter.setPageEvent((PdfPageEvent) event);

                //Before writing anything to a document it should be opened first
                document.open();

                //Adding meta-data to the document
                addMetaData(document);
                //Adding Title(s) of the document
                addTitlePage(document);
                //Adding main contents of the document
                addContent(document);
                //Closing the document
                document.close();

                String authority = context.getApplicationContext().getPackageName() + ".fileprovider";
                Uri uriToFile = FileProvider.getUriForFile(context, authority, pdfFile);

                Intent shareIntent = new Intent(Intent.ACTION_VIEW);
                shareIntent.setDataAndType(uriToFile, "application/pdf");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(shareIntent);
                } else {
                    Uri uri = Uri.fromFile(pdfFile);
                    provider.getContext().grantUriPermission("package com.example.testois", uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.setDataAndType(uri, "application/pdf");
                }
                try {
                    startActivity(shareIntent);
                } catch (Exception e) {
                    Toast.makeText(this, "No Application Available to View PDF", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();

                return false;
            }
            return true;
        }


        /**
         *  iText allows to add metadata to the PDF which can be viewed in your Adobe Reader. If you right click
         *  on the file and to to properties then you can see all these information.
         * @param document
         */
        private static void addMetaData(Document document) {
            document.addTitle("Severina OIS Report for "+ document.addCreationDate());
            document.addSubject("none");
            document.addKeywords("Java, PDF, iText");
            document.addAuthor("YABOI DENNIS");
        }

        /**
         * In this method title(s) of the document is added.
         * @param document
         * @throws DocumentException
         */
        private static void addTitlePage(Document document)
                throws DocumentException {
            Paragraph paragraph = new Paragraph();

            // Adding several title of the document. Paragraph class is available in  com.itextpdf.text.Paragraph
            Paragraph childParagraph = new Paragraph("Severina LPG Company"); //public static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD);
            childParagraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add(childParagraph);

            childParagraph = new Paragraph("Inventory and Order Report"); //public static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
            childParagraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add(childParagraph);

            childParagraph = new Paragraph("Report generated on: " + document.addCreationDate());
            childParagraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add(childParagraph);

            addEmptyLine(paragraph, 2);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            //End of adding several titles

        }

        /**
         * In this method the main contents of the documents are added
         * @param document
         * @throws DocumentException
         */

        private void addContent(Document document) throws DocumentException {

            Paragraph reportBody = new Paragraph();
            reportBody.setFont(new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL)); //public static Font FONT_BODY = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);

            // Creating a table
            createTable(reportBody);

            // now add all this to the document
            document.add(reportBody);

        }

        /**
         * This method is responsible to add table using iText
         * @param reportBody
         * @throws BadElementException
         */
        private void createTable(Paragraph reportBody)
                throws BadElementException {

            float[] columnWidths = {2,5,5,5,5}; //total 4 columns and their width. The first three columns will take the same width and the fourth one will be 5/2.
            PdfPTable table = new PdfPTable(columnWidths);

            table.setWidthPercentage(100); //set table with 100% (full page)
            table.getDefaultCell().setUseAscender(true);


            //Adding table headers
            PdfPCell cell = new PdfPCell(new Phrase("ID", //Table Header
                    new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD))); //Public static Font FONT_TABLE_HEADER = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER); //alignment
            cell.setBackgroundColor(new GrayColor(0.75f)); //cell background color
            cell.setFixedHeight(30); //cell height
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("ORDER DATE",
                    new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new GrayColor(0.75f));
            cell.setFixedHeight(30);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("ITEM NAME",
                    new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new GrayColor(0.75f));
            cell.setFixedHeight(30);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("QTY STOCKS LEFT",
                    new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new GrayColor(0.75f));
            cell.setFixedHeight(30);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("STOCKS DELIVERED FOR THIS WEEK",
                    new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new GrayColor(0.75f));
            cell.setFixedHeight(30);
            table.addCell(cell);


            //End of adding table headers

            //This method will generate some static data for the table
            List<Report> reportList = db.getReportList();

            //Adding data into table
            for (int i = 0; i < reportList.size(); i++) { //
                cell = new PdfPCell(new Phrase(reportList.get(i).getOrd_id()));
                cell.setFixedHeight(28);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(reportList.get(i).getOrd_date()));
                cell.setFixedHeight(28);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(reportList.get(i).getInv_name()));
                cell.setFixedHeight(28);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(reportList.get(i).getInv_quantity()));
                cell.setFixedHeight(28);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(reportList.get(i).getOrd_quantity()));
                cell.setFixedHeight(28);
                table.addCell(cell);
            }

            reportBody.add(table);

        }

        /**
         * This method is used to add empty lines in the document
         * @param paragraph
         * @param number
         */
        private static void addEmptyLine(Paragraph paragraph, int number) {
            for (int i = 0; i < number; i++) {
                paragraph.add(new Paragraph(" "));
            }
        }



    /**
         * This is an inner class which is used to create header and footer
         * @author XYZ
         *
         */

        class PageHeaderFooter extends PdfPageEventHelper {
            Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);

            public void onEndPage(PdfWriter writer, Document document) {

                /**
                 * PdfContentByte is an object containing the user positioned text and graphic contents
                 * of a page. It knows how to apply the proper font encoding.
                 */
                PdfContentByte cb = writer.getDirectContent();

                /**
                 * In iText a Phrase is a series of Chunks.
                 * A chunk is the smallest significant part of text that can be added to a document.
                 *  Most elements can be divided in one or more Chunks. A chunk is a String with a certain Font
                 */
                Phrase footer_poweredBy = new Phrase("Powered By SIAS ERP", new Font(Font.FontFamily.UNDEFINED, 7, Font.ITALIC)); //public static Font FONT_HEADER_FOOTER = new Font(Font.FontFamily.UNDEFINED, 7, Font.ITALIC);
                Phrase footer_pageNumber = new Phrase("Page " + document.getPageNumber(), new Font(Font.FontFamily.UNDEFINED, 7, Font.ITALIC));

                // Header
                // ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, header,
                // (document.getPageSize().getWidth()-10),
                // document.top() + 10, 0);

                // footer: show page number in the bottom right corner of each age
                ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                        footer_pageNumber,
                        (document.getPageSize().getWidth() - 10),
                        document.bottom() - 10, 0);
//			// footer: show page number in the bottom right corner of each age
                ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                        footer_poweredBy, (document.right() - document.left()) / 2
                                + document.leftMargin(), document.bottom() - 10, 0);
            }
        }

}
