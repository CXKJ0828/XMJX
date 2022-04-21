package com.zhjh.util;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 导出Excel文档工具类
 * @date 2014-8-6
 * */
public class ExcelUtil {

    /**
     * 创建excel文档，
     * @param list 数据
     * */
    public static Workbook createWorkBook(List<Map<String, Object>> listMap,List<?> list,String sheetName) {
            // 创建excel工作簿
            Workbook wb = new HSSFWorkbook();
            // 创建第一个sheet（页），并命名
            Sheet sheet = wb.createSheet(sheetName);
            // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
           /* for(int i=0;i<keys.length;i++){
                sheet.setColumnWidth((short) i, (short) (35.7 * 150));
            }*/

            // 创建第一行
            Row row = sheet.createRow((short) 0);

            // 创建两种单元格格式
            CellStyle cs = wb.createCellStyle();
            CellStyle cs2 = wb.createCellStyle();

            // 创建两种字体
            Font f = wb.createFont();
            Font f2 = wb.createFont();

            // 创建第一种字体样式（用于列名）
            f.setFontHeightInPoints((short) 10);
            f.setColor(IndexedColors.BLACK.getIndex());
            f.setBoldweight(Font.BOLDWEIGHT_BOLD);

            // 创建第二种字体样式（用于值）
            f2.setFontHeightInPoints((short) 10);
            f2.setColor(IndexedColors.BLACK.getIndex());

//            Font f3=wb.createFont();
//            f3.setFontHeightInPoints((short) 10);
//            f3.setColor(IndexedColors.RED.getIndex());

            // 设置第一种单元格的样式（用于列名）
            cs.setFont(f);
            cs.setBorderLeft(CellStyle.BORDER_THIN);
            cs.setBorderRight(CellStyle.BORDER_THIN);
            cs.setBorderTop(CellStyle.BORDER_THIN);
            cs.setBorderBottom(CellStyle.BORDER_THIN);
            cs.setAlignment(CellStyle.ALIGN_CENTER);

            // 设置第二种单元格的样式（用于值）
            cs2.setFont(f2);
            cs2.setBorderLeft(CellStyle.BORDER_THIN);
            cs2.setBorderRight(CellStyle.BORDER_THIN);
            cs2.setBorderTop(CellStyle.BORDER_THIN);
            cs2.setBorderBottom(CellStyle.BORDER_THIN);
            cs2.setAlignment(CellStyle.ALIGN_CENTER);
            for(int i=0;i<listMap.size();i++){
            	if(Boolean.parseBoolean(listMap.get(i).get("hide")+"")){
            		listMap.remove(listMap.get(i));
            	}
            }
            //设置列名
            for(int i=0;i<listMap.size();i++){
                Cell cell = row.createCell(i);
                cell.setCellValue(listMap.get(i).get("name")+"");
                cell.setCellStyle(cs);
            }
            //设置每行每列的值
            for (int i = 0; i < list.size(); i++) {
                // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
                // 创建一行，在页sheet上
                Row row1 = sheet.createRow(i+1);
                // 在row行上创建一个方格

                for(int j=0;j<listMap.size();j++){
                    Cell cell = row1.createCell(j);
                    Map<String, Object> map =(Map<String, Object>) list.get(i);
                    cell.setCellValue(map.get(listMap.get(j).get("colkey")) == null?" ": map.get(listMap.get(j).get("colkey")).toString());
                    cell.setCellStyle(cs2);
                }
            }
            return wb;    		

    }

    public static Sheet readRowsAndColums(String url) {
        File sourceFile = new File(url);
        InputStream fis = null;
        try {
            fis = new FileInputStream(sourceFile);
            //1:创建workbook
            Workbook workbook = WorkbookFactory.create(fis);
            if (workbook instanceof XSSFWorkbook) {
                XSSFWorkbook xWb = (XSSFWorkbook) workbook;
                Sheet sheet = xWb.getSheetAt(0);//获取第一个Sheet的内容
                return sheet;
            }else if(workbook instanceof HSSFWorkbook){
                HSSFWorkbook hWb = (HSSFWorkbook) workbook;
                Sheet sheet = hWb.getSheetAt(0);//获取第一个Sheet的内容
                return sheet;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Sheet readRowsAndColumsSheet1(String url) {
        File sourceFile = new File(url);
        InputStream fis = null;
        try {
            fis = new FileInputStream(sourceFile);
            //1:创建workbook
            Workbook workbook = WorkbookFactory.create(fis);
            if (workbook instanceof XSSFWorkbook) {
                XSSFWorkbook xWb = (XSSFWorkbook) workbook;
                Sheet sheet = xWb.getSheetAt(0);//获取第一个Sheet的内容
                return sheet;
            }else if(workbook instanceof HSSFWorkbook){
                HSSFWorkbook hWb = (HSSFWorkbook) workbook;
                Sheet sheet = hWb.getSheetAt(0);//获取第一个Sheet的内容
                return sheet;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Sheet readRowsAndColumsSheet2(String url) {
        File sourceFile = new File(url);
        InputStream fis = null;
        try {
            fis = new FileInputStream(sourceFile);
            //1:创建workbook
            Workbook workbook = WorkbookFactory.create(fis);
            if (workbook instanceof XSSFWorkbook) {
                XSSFWorkbook xWb = (XSSFWorkbook) workbook;
                Sheet sheet = xWb.getSheetAt(1);//获取第一个Sheet的内容
                return sheet;
            }else if(workbook instanceof HSSFWorkbook){
                HSSFWorkbook hWb = (HSSFWorkbook) workbook;
                Sheet sheet = hWb.getSheetAt(1);//获取第一个Sheet的内容
                return sheet;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Sheet readRowsAndColumsSheet3(String url) {
        File sourceFile = new File(url);
        InputStream fis = null;
        try {
            fis = new FileInputStream(sourceFile);
            //1:创建workbook
            Workbook workbook = WorkbookFactory.create(fis);
            if (workbook instanceof XSSFWorkbook) {
                XSSFWorkbook xWb = (XSSFWorkbook) workbook;
                Sheet sheet = xWb.getSheetAt(2);//获取第一个Sheet的内容
                return sheet;
            }else if(workbook instanceof HSSFWorkbook){
                HSSFWorkbook hWb = (HSSFWorkbook) workbook;
                Sheet sheet = hWb.getSheetAt(2);//获取第一个Sheet的内容
                return sheet;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Sheet readRowsAndColumsSheet4(String url) {
        File sourceFile = new File(url);
        InputStream fis = null;
        try {
            fis = new FileInputStream(sourceFile);
            //1:创建workbook
            Workbook workbook = WorkbookFactory.create(fis);
            if (workbook instanceof XSSFWorkbook) {
                XSSFWorkbook xWb = (XSSFWorkbook) workbook;
                Sheet sheet = xWb.getSheetAt(3);//获取第一个Sheet的内容
                return sheet;
            }else if(workbook instanceof HSSFWorkbook){
                HSSFWorkbook hWb = (HSSFWorkbook) workbook;
                Sheet sheet = hWb.getSheetAt(3);//获取第一个Sheet的内容
                return sheet;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取表格单元格Cell内容
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        String result = new String();
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:// 数字类型
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {// 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    Date date = cell.getDateCellValue();
                    result = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 58) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = cell.getNumericCellValue();
                    Date date = DateUtil
                            .getJavaDate(value);
                    result = sdf.format(date);
                } else {
                    double value = cell.getNumericCellValue();
                    CellStyle style = cell.getCellStyle();
                    DecimalFormat format = new DecimalFormat();
                    String temp = style.getDataFormatString();
//                    // 单元格设置成常规
//                    if (temp.equals("General")) {
//                        format.applyPattern("#");
//                    }
                    result = format.format(value);
                }
                break;
            case Cell.CELL_TYPE_STRING:// String类型
                result = cell.getRichStringCellValue().toString();
                break;
            case Cell.CELL_TYPE_BLANK:
                result = "";
                break;
            default:
                result = cell.toString();
                break;
        }
        return result;
    }

    public static int[] getColsOfTable(Sheet sheet) {

        int[] data = {0, 0};
        for(int i = sheet.getFirstRowNum(); i < sheet.getLastRowNum(); i ++) {
            if(null != sheet.getRow(i)) {
                data[0] = sheet.getRow(i).getLastCellNum();
                data[1] = sheet.getRow(i).getHeight();
            }else
                continue;
        }
        return data;
    }

    public static void main(String[] args) {
        String url="C:/客户模板.xlsx";
        Sheet sheet=readRowsAndColumsSheet1(url);
        int lastColNum=0;
        for(int i=1;i<=sheet.getLastRowNum();i++){
            Row row = null;
            row = sheet.getRow(i);
            lastColNum = row.getLastCellNum();
            for(int j=0;j<lastColNum;j++){
                Cell cell=row.getCell(j);
                String value=getCellValue(cell);
                System.out.print(value);
            }
        }
    }
}