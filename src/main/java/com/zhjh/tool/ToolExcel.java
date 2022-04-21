package com.zhjh.tool;

/**
 * Created by Administrator on 2018/7/4.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.zhjh.bean.ExcelBean;
import com.zhjh.bean.ExcelSheetBean;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ToolExcel {


    public static void main(String[] args) {
        System.out.print(ToolProject.isHasPartent("1.1","1.1.2"));

    }

        public static String replaceBlank(String str) {
            String dest = "";
            if(str=="2.10"){
                System.out.print("aaaa");
            }
            if (str != null) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(str);
                dest = m.replaceAll("");
            }
            if(dest.indexOf("'")>0){
                dest=dest.replace("'","\"");
            }
            return dest;
        }

    /**
     * 获取sheet名字
     * @param excelUrl
     * @return
     */
    public static List<ExcelSheetBean> getSheetName(String excelUrl){
        List<ExcelSheetBean> excelSheetBeanList=new ArrayList<ExcelSheetBean>();
        String fileToBeRead = excelUrl;
        try {

            InputStream is = null;
            File sourcefile = new File(fileToBeRead);
            is = new FileInputStream(sourcefile);
            Workbook wb = WorkbookFactory.create(is);
            if (wb instanceof XSSFWorkbook) {
                XSSFWorkbook xWb = (XSSFWorkbook) wb;
                int count=xWb.getNumberOfSheets();
                for(int i=0;i<count;i++){
                    ExcelSheetBean excelSheetBean=new ExcelSheetBean(i,xWb.getSheetAt(i).getSheetName());
                    excelSheetBeanList.add(excelSheetBean);
                }
                return excelSheetBeanList;
            }else if(wb instanceof HSSFWorkbook){
                HSSFWorkbook workbook = (HSSFWorkbook) wb;
                int count=workbook.getNumberOfSheets();
                for(int i=0;i<count;i++){
                    ExcelSheetBean excelSheetBean=new ExcelSheetBean(i,workbook.getSheetAt(i).getSheetName());
                    excelSheetBeanList.add(excelSheetBean);
                }
                return excelSheetBeanList;
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return excelSheetBeanList ;
    }
    /**
     * 获取sheet名字
     * @param excelUrl
     * @param sheetNumber
     * @return
     */
    public static String getSheetName(String excelUrl,int sheetNumber){
        String fileToBeRead = excelUrl;
        try {

            InputStream is = null;
            File sourcefile = new File(fileToBeRead);
            is = new FileInputStream(sourcefile);
            Workbook wb = WorkbookFactory.create(is);
            if (wb instanceof XSSFWorkbook) {
                XSSFWorkbook xWb = (XSSFWorkbook) wb;
                int count=xWb.getNumberOfSheets();
                System.out.println(count);
                Sheet sheet = xWb.getSheetAt(sheetNumber);//获取第一个Sheet的内容
                String sheetName=sheet.getSheetName();
                return sheetName;
            }else if(wb instanceof HSSFWorkbook){
                HSSFWorkbook workbook = (HSSFWorkbook) wb;
                HSSFSheet sheet = workbook.getSheetAt(sheetNumber);
                String sheetName=sheet.getSheetName();
                return sheetName;
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "" ;
    }

    /**
     * 根据title获取列第一行的内容 比如：获取“项目号”对应的内容
     * @param excelUrl
     * @param title
     * @param sheetNumber
     * @return
     */
    public static String getContentByTitle(String excelUrl,String title,int sheetNumber){
        ExcelBean excelBean=getColNumByContent(excelUrl,title,sheetNumber);
        excelBean.setRow(excelBean.getRow()+1);
        String content=getContentByExcelBead(excelUrl,0,excelBean);
        return content;
    }

    /**
     * 根据下角标获取相应内容
     * @param excelUrl
     * @param sheetNumber
     * @param excelBean
     * @return
     */
    public static String getContentByExcelBead(String excelUrl,int sheetNumber,ExcelBean excelBean){
        String content="";
        String fileToBeRead = excelUrl;
        try {
            InputStream is = null;
            File sourcefile = new File(fileToBeRead);
            is = new FileInputStream(sourcefile);
            Workbook wb = WorkbookFactory.create(is);
            if (wb instanceof XSSFWorkbook) {
                XSSFWorkbook xWb = (XSSFWorkbook) wb;
                Sheet sheet = xWb.getSheetAt(sheetNumber);//获取第一个Sheet的内容
                Row row = sheet.getRow(excelBean.getRow());
                if(row!=null){
                    Cell cell = row.getCell(excelBean.getCol());
                    String value=getCellValue(cell);
                    return value;
                }
            }else if(wb instanceof HSSFWorkbook){
                HSSFWorkbook workbook = (HSSFWorkbook) wb;
                HSSFSheet sheet = workbook.getSheetAt(sheetNumber);
                    Row row = sheet.getRow(excelBean.getRow());
                    if(row!=null){
                            Cell cell = row.getCell(excelBean.getCol());
                            String value=getCellValue(cell);
                            return value;
                    }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 根据内容查询excel表格在第几列（下角标 从0开始计算）
     * @param excelUrl 包括excel名称
     * @param content
     * @param sheetNumber
     * @return
     */
    public static ExcelBean getColNumByContent(String excelUrl,String content,int sheetNumber){
        ExcelBean excelBean=new ExcelBean(0,0);
        String fileToBeRead = excelUrl;
        try {

            InputStream is = null;
            File sourcefile = new File(fileToBeRead);
            is = new FileInputStream(sourcefile);
            Workbook wb = WorkbookFactory.create(is);
            if (wb instanceof XSSFWorkbook) {
                XSSFWorkbook xWb = (XSSFWorkbook) wb;
                Sheet sheet = xWb.getSheetAt(sheetNumber);//获取第一个Sheet的内容
                for(int i=0;i<5;i++){
                    Row row = sheet.getRow(i);
                    if(row!=null){
                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            String value=getCellValue(cell);
                            if(value.equals(content)){
                                excelBean=new ExcelBean(i,j);
                                return excelBean;
                            }
                        }
                    }
                }
            }else if(wb instanceof HSSFWorkbook){
                HSSFWorkbook workbook = (HSSFWorkbook) wb;
                HSSFSheet sheet = workbook.getSheetAt(sheetNumber);
                String sheetName=sheet.getSheetName();
                for(int i=0;i<5;i++){
                    Row row = sheet.getRow(i);
                    if(row!=null){
                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            String value=getCellValue(cell);
                            if(value.equals(content)){
                                excelBean=new ExcelBean(i,j);
                                return excelBean;
                            }
                        }
                    }
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("执行完成");
        return excelBean ;
    }
    private static String getCellValue(Cell cell) {
                String cellValue = "";
               DataFormatter formatter = new DataFormatter();
               if (cell != null) {
                      switch (cell.getCellType()) {
                             case Cell.CELL_TYPE_NUMERIC:
                                       if (DateUtil.isCellDateFormatted(cell)) {
                                              cellValue = formatter.formatCellValue(cell);
                                         } else {
                                              double value = cell.getNumericCellValue();
                                              int intValue = (int) value;
                                                cellValue = value - intValue == 0 ? String.valueOf(intValue) : String.valueOf(value);
                                           }
                                      break;
                               case Cell.CELL_TYPE_STRING:
                                      cellValue = cell.getStringCellValue();
                                       break;
                                case Cell.CELL_TYPE_BOOLEAN:
                                      cellValue = String.valueOf(cell.getBooleanCellValue());
                                    break;
                                 case Cell.CELL_TYPE_FORMULA:
                                       cellValue = String.valueOf(cell.getCellFormula());
                                     break;
                              case Cell.CELL_TYPE_BLANK:
                                   cellValue = "";
                                        break;
                               case Cell.CELL_TYPE_ERROR:
                                     cellValue = "";
                                 break;
                             default:
                                    cellValue = cell.toString().trim();
                                 break;
                       }
               }
            return cellValue.trim();
       }

    public static List<ExcelBean> getListColNumByContent(String excelUrl,String content,int sheetNumber){
        List<ExcelBean> excelBeanList=new ArrayList<>();
        String fileToBeRead = excelUrl;
        try {

            InputStream is = null;
            File sourcefile = new File(fileToBeRead);
            is = new FileInputStream(sourcefile);
            Workbook wb = WorkbookFactory.create(is);
            if (wb instanceof XSSFWorkbook) {
                XSSFWorkbook xWb = (XSSFWorkbook) wb;
                Sheet sheet = xWb.getSheetAt(sheetNumber);//获取第一个Sheet的内容
                for(int i=0;i<sheet.getLastRowNum();i++){
                    Row row = sheet.getRow(i);
                    if(row!=null){
                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            String value=getCellValue(cell);
                            if(value.equals(content)){
                                ExcelBean excelBean=new ExcelBean(i,j);
                                excelBeanList.add(excelBean);
                            }
                        }
                    }
                }
            }else if(wb instanceof HSSFWorkbook){
                HSSFWorkbook workbook = (HSSFWorkbook) wb;
                HSSFSheet sheet = workbook.getSheetAt(sheetNumber);
                for(int i=0;i<sheet.getLastRowNum();i++){
                    Row row = sheet.getRow(i);
                    if(row!=null){
                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            String value=getCellValue(cell);
                            if(value.equals(content)){
                                ExcelBean excelBean=new ExcelBean(i,j);
                                excelBeanList.add(excelBean);
                            }
                        }
                    }
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return excelBeanList ;
    }

    /**
     * @param excelUrl 表格所在路径（包括名称）
     * @param content 修改内容
     * @param sheetNumber excel表格下侧sheet
     */
    public static void modifyExcelByExcelBean(String excelUrl,ExcelBean excelBean,String content,int sheetNumber) {
        int rowNum=excelBean.getRow();
        int colNum=excelBean.getCol();
//待转换文件路径
        String fileToBeRead = excelUrl;
        try {

            InputStream is = null;
            File sourcefile = new File(fileToBeRead);
            is = new FileInputStream(sourcefile);
            Workbook wb = WorkbookFactory.create(is);
            if (wb instanceof XSSFWorkbook) {
                XSSFWorkbook xWb = (XSSFWorkbook) wb;
                Sheet sheet = xWb.getSheetAt(sheetNumber);//获取第一个Sheet的内容
                OutputStream os = new FileOutputStream(fileToBeRead);

                XSSFRow row = (XSSFRow) sheet.getRow((short)(rowNum));
                XSSFCell cell = row.getCell((short)(colNum));
                if(cell==null){
                    cell=row.createCell(colNum);
                }
                cell.setCellValue(content);
                //保存工作薄
                xWb.write(os);
                os.flush();
                os.close();
            }else if(wb instanceof HSSFWorkbook){
                HSSFWorkbook workbook = (HSSFWorkbook) wb;
                HSSFSheet sheet = workbook.getSheetAt(sheetNumber);
                OutputStream os = new FileOutputStream(fileToBeRead);
                HSSFRow row = sheet.getRow((short)(rowNum));
                HSSFCell cell = row.getCell((short)(colNum));
                if(cell==null){
                    cell=row.createCell(colNum);
                }
                cell.setCellValue(content);
                //保存工作薄
                workbook.write(os);
                os.flush();
                os.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
