package com.example.testois.utilities;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Metodos {
    public Boolean write(String fname, String fcontent) {
        try {
            String fpath = "/sdcard/" + fname + ".pdf";
            File file = new File(fpath);

            if (!file.exists()) {
                file.createNewFile();
            }

            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);


            Document document = new Document();

            PdfWriter.getInstance(document,
                    new FileOutputStream(file.getAbsoluteFile()));
            document.open();
            document.add(new Paragraph("Sigueme en Twitter!"));

            document.add(new Paragraph("@DavidHackro"));

            PdfPTable table = new PdfPTable(3);
            PdfPCell cell = new PdfPCell(new Phrase("Cell with colspan 3"));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
            cell.setRowspan(2);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            table.addCell("Cell 1.1");
            cell = new PdfPCell();
            cell.addElement(new Phrase("Cell 1.2"));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Cell 2.1"));
            cell.setPadding(5);
            cell.setUseAscender(true);
            cell.setUseDescender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setPadding(5);
            cell.setUseAscender(true);
            cell.setUseDescender(true);
            Paragraph p = new Paragraph("Cell 2.2");
            p.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p);
            table.addCell(cell);
            document.add(table);
            document.close();
            document.close();

            return true;
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            return false;
        } // TODO Auto-generated catch block

    }}

