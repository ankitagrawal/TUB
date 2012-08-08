package com.hk.admin.util;

import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.DocumentException;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 8/8/12
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class PdfGenerator {

    PdfPTable pdfPTable;
    public PdfGenerator(){

    }

    public void generateTable(List<String> columnNames ){

    }

    public PdfGenerator addBottomComment(String comment){

        return this;
    }

    public void addHeading(){

    }

    public void createTable(int noOfColumns, float widthOfcolumns[], float widthPercentage) {
        PdfPTable pdfPTable = new PdfPTable(noOfColumns);
        try{
            pdfPTable.setWidths(widthOfcolumns);
            pdfPTable.setWidthPercentage(widthPercentage);

        }catch (DocumentException de) {

        }

    }

    public void createCell(PdfPTable pdfPTable, java.util.List<Map<String, Font>> columnContentWithFontList) {
        PdfPCell cell = new PdfPCell();
        for (Map<String, Font> columnContentWithFont : columnContentWithFontList) {
            for(String columnContent : columnContentWithFont.keySet()) {
                Font font = columnContentWithFont.get(columnContent);
            }
        }
        cell.addElement(new Paragraph("S.No.", new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD)));
        pdfPTable.addCell(cell);

    }
}
