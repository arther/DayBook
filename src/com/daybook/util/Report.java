package com.daybook.util;

import android.os.Environment;
import com.daybook.model.Bill;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Report{
    private static SimpleDateFormat simpleDateFormat;

    private String fileName;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    public Report(){

        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        fileName = Environment.getExternalStorageDirectory() + File.separator + "Report-"
                + simpleDateFormat.format(new Date()) + ".pdf";
    }
    public void generateReport(java.util.List<Bill> bills) {

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            addTitle(document);
            addContent(document, bills);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTitle(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();

        preface.add(new Paragraph("Daily Report", catFont));

        preface.add(new Paragraph("Date : " + new Date(), smallBold));

        addEmptyLine(preface, 1);

        document.add(preface);
    }

    private static void addContent(Document document, java.util.List<Bill> bills) throws DocumentException {
        document.add(createTable(bills));

    }

    private static PdfPTable createTable(java.util.List<Bill> bills)
            throws BadElementException {

        PdfPTable table = new PdfPTable(2);
        int total = 0;
        int amount;

        PdfPCell c1 = new PdfPCell(new Phrase("Bill Number"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Bill Amount"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);

        for(Bill bill : bills) {
            amount = bill.getAmount();
            total += amount;
            table.addCell(String.valueOf(bill.getNumber()));
            table.addCell(String.valueOf(amount));
        }

        table.addCell("Total");
        table.addCell(String.valueOf(total));

        return table;

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}