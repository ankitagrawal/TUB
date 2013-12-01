package com.hk.admin.util;

import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 8/8/12
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class PdfGenerator {

    public static PdfPTable createTable(int noOfColumns, float widthOfcolumns[], float widthPercentage) throws Exception{
        PdfPTable pdfPTable = new PdfPTable(noOfColumns);
        pdfPTable.setWidths(widthOfcolumns);
        pdfPTable.setWidthPercentage(widthPercentage);

        return pdfPTable;
    }

    public static PdfPCell createCell(String paragraphContent, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.addElement(new Paragraph(paragraphContent, font));
        return cell;
    }

    public static PdfPCell createCell(String paragraphContent) {
        PdfPCell cell = new PdfPCell();
        cell.addElement(new Paragraph(paragraphContent));
        return cell;
    }
}
