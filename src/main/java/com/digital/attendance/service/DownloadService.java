/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digital.attendance.service;

import com.digital.attendance.model.UserClockTime;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

/**
 *
 * @author oreoluwa
 */
@Service
public class DownloadService {
    
    public static ByteArrayInputStream usersAttendanceToExcel(List<UserClockTime> users) throws IOException {
    String[] COLUMNs = {"date", "email", "timein", "timeout","location","timespent","System-Log-Out"};
    try(
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
    ){
      CreationHelper createHelper = workbook.getCreationHelper();
   
      Sheet sheet = workbook.createSheet("Users");
   
      Font headerFont = workbook.createFont();
      headerFont.setBold(true);
      headerFont.setColor(IndexedColors.BLUE.getIndex());
   
      CellStyle headerCellStyle = workbook.createCellStyle();
      headerCellStyle.setFont(headerFont);
   
      // Row for Header
      Row headerRow = sheet.createRow(0);
   
      // Header
      for (int col = 0; col < COLUMNs.length; col++) {
        Cell cell = headerRow.createCell(col);
        cell.setCellValue(COLUMNs[col]);
        cell.setCellStyle(headerCellStyle);
      }
   
      // CellStyle for Age
      CellStyle ageCellStyle = workbook.createCellStyle();
      ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
   
      int rowIdx = 1;
      for (UserClockTime user : users) {
        Row row = sheet.createRow(rowIdx++);
   
        
        row.createCell(0).setCellValue(user.getDate());
        row.createCell(1).setCellValue(user.getEmail());
        row.createCell(2).setCellValue(user.getTimein());
        row.createCell(3).setCellValue(user.getTimeout());
        row.createCell(4).setCellValue(user.getLocation());
        row.createCell(5).setCellValue(user.getTimespent());
        row.createCell(6).setCellValue(user.getSystemlogout());

       
   
//        Cell ageCell = row.createCell(3);
//        ageCell.setCellValue(customer.getAge());
//        ageCell.setCellStyle(ageCellStyle);
      }
   
      workbook.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    }
  }
    
    
    
    
    
    
    public static void writeObjectToCSV(PrintWriter writer,List<UserClockTime> users) {
    try (
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                      .withHeader("Date", "Email", "Timein","Timeout","Location","Timespent","System-Log-Out"));
    ) {
      for (UserClockTime user : users) {
        List<String> data = Arrays.asList(
            user.getDate().toString(),
            user.getEmail(),
            user.getTimein().toString(),
            user.getTimeout().toString(),
            user.getLocation(),
            user.getTimespent(),
                user.getSystemlogout()
          );
        
        csvPrinter.printRecord(data);
      }
      csvPrinter.flush();
    } catch (Exception e) {
      System.out.println("Writing CSV error!");
      e.printStackTrace();
    }
  }
    
    
    
        
    public static void downloadEachUserAttendanceToCSV(PrintWriter writer, UserClockTime user) {
    try (
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                      .withHeader("Date", "Email", "Timein","Timeout","Location","Timespent"));
    ) {
     
        List<String> data = Arrays.asList(
            user.getDate().toString(),
            user.getEmail(),
            user.getTimein().toString(),
            user.getTimeout().toString(),
            user.getLocation(),
            user.getTimespent(),
                user.getSystemlogout()
          );
        
        csvPrinter.printRecord(data);
      
      csvPrinter.flush();
    } catch (Exception e) {
      System.out.println("Writing CSV error!");
      e.printStackTrace();
    }
  }
    
    
    
    
}
