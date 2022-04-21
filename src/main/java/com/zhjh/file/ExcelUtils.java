package com.zhjh.file;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.UnsupportedEncodingException;

/**
 * Created by 88888888 on 2020/8/19.
 */
public class ExcelUtils {
    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String excelTitle,String sheetName, String []title, String [][]values, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }



        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        HSSFRow row1=sheet.createRow(0); //在sheet里创建第一行，参数为行索引

        HSSFCell cellTtitle=row1.createCell(0); //创建单元格

        HSSFCellStyle styleTitle = wb.createCellStyle();
        styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        HSSFFont font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 16);//设置字体大小
       styleTitle.setFont(font);
        cellTtitle.setCellValue(excelTitle);        //设置单元格内容
        cellTtitle.setCellStyle(styleTitle);

        //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列

        sheet.addMergedRegion(new CellRangeAddress(0,0,0,title.length-1));

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(1);


        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        // 第四步，创建单元格，并设置值表头 设置表头居中


        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 2);
            for(int j=0;j<values[i].length;j++){
                //将内容按顺序赋给对应的列对象
                String content=values[i][j];
                if(content==null||content.equals("null")){
                    content="";
                }
                row.createCell(j).setCellValue(content);
            }
        }
        return wb;
    }
    /**
     * 发送响应流方法
     */
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
