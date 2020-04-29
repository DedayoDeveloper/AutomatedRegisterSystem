/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digital.attendance.utils;

import com.digital.attendance.model.UserClockTime;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author oreoluwa
 */
@Service
public class GeneratePdfReport {
    
    
    private static final Logger logger = LoggerFactory.getLogger(GeneratePdfReport.class);

    public static ByteArrayInputStream userReport(List<UserClockTime> users) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            String reportHeader = "ATTENDANCE REPORT";

            PdfPTable table1 = new PdfPTable(1);
            table1.setWidthPercentage(100);
            table1.setWidths(new int[]{6});

            Font headFont1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell hcell1;

            hcell1 = new PdfPCell(new Phrase(reportHeader, headFont1));
            hcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(hcell1);


            // -------------------------------------------------------------------------------------------------------------------------------------------
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{8, 11, 8, 8, 8, 8, 8});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell hcell;
            
            hcell = new PdfPCell(new Phrase("Date", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);
            
             hcell = new PdfPCell(new Phrase("Email", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);
            
             hcell = new PdfPCell(new Phrase("Time-In", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);
            
             hcell = new PdfPCell(new Phrase("Time-Out", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);
            
             hcell = new PdfPCell(new Phrase("Location", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);
            
             hcell = new PdfPCell(new Phrase("Time-Spent", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("System-Log-Out", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            for (UserClockTime user : users) {

                PdfPCell cell;

                cell = new PdfPCell(new Phrase(String.valueOf(user.getDate())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);
                
                cell = new PdfPCell(new Phrase(String.valueOf(user.getEmail())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);
                
                cell = new PdfPCell(new Phrase(String.valueOf(user.getTimein())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);
                
                cell = new PdfPCell(new Phrase(String.valueOf(user.getTimeout())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);
                
                cell = new PdfPCell(new Phrase(String.valueOf(user.getLocation())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);
                
                
                cell = new PdfPCell(new Phrase(String.valueOf(user.getTimespent())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(user.getSystemlogout())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);
            }

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table1);
            document.add(table);

            document.close();

        } catch (DocumentException ex) {

            logger.error("Error occurred: {0}", ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
    
    
    
    
    
    
//     public static ByteArrayInputStream userReportForAttendance(List<UserClockIn> user) {
//
//        Document document = new Document();
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
////        UserClockIn user = new UserClockIn();
//        try {
//
//            PdfPTable table = new PdfPTable(8);
//            table.setWidthPercentage(100);
//            table.setWidths(new int[]{6, 6, 6, 6, 6, 6, 6, 6});
//
//            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
//
//            PdfPCell hcell;
//            
//            hcell = new PdfPCell(new Phrase("Date", headFont));
//            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(hcell);
//            
//            hcell = new PdfPCell(new Phrase("Email", headFont));
//            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(hcell);
//
//            hcell = new PdfPCell(new Phrase("Firstname", headFont));
//            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(hcell);
//            
//             hcell = new PdfPCell(new Phrase("Lastname", headFont));
//            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(hcell);
//            
//             hcell = new PdfPCell(new Phrase("Time In", headFont));
//            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(hcell);
//            
//             hcell = new PdfPCell(new Phrase("Time Out", headFont));
//            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(hcell);
//            
//             hcell = new PdfPCell(new Phrase("Location", headFont));
//            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(hcell);
//            
//             hcell = new PdfPCell(new Phrase("Hours Worked", headFont));
//            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(hcell);
//
//           
//
//                PdfPCell cell;
//
//                cell = new PdfPCell(new Phrase(String.valueOf(user.getDate())));
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                cell.setPaddingRight(5);
//                table.addCell(cell);
//                
//                cell = new PdfPCell(new Phrase(String.valueOf(user.getEmail())));
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                cell.setPaddingRight(5);
//                table.addCell(cell);
//                
//                cell = new PdfPCell(new Phrase(String.valueOf(user.getTimein())));
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                cell.setPaddingRight(5);
//                table.addCell(cell);
//                
//                cell = new PdfPCell(new Phrase(String.valueOf(user.getTimeout())));
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                cell.setPaddingRight(5);
//                table.addCell(cell);
//                
//                cell = new PdfPCell(new Phrase(String.valueOf(user.getLocation())));
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                cell.setPaddingRight(5);
//                table.addCell(cell);
//                
//                
//                cell = new PdfPCell(new Phrase(String.valueOf(user.getTimecount())));
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                cell.setPaddingRight(5);
//                table.addCell(cell);
//            
//
//            PdfWriter.getInstance(document, out);
//            document.open();
//            document.add(table);
//
//            document.close();
//
//        } catch (DocumentException ex) {
//
//            logger.error("Error occurred: {0}", ex);
//        }
//
//        return new ByteArrayInputStream(out.toByteArray());
//    }
//    
}
