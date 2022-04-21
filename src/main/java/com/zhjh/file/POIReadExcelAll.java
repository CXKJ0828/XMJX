package com.zhjh.file;

/**
 * Created by Administrator on 2018/7/8.
 */

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Map;

/**
 * @ClassName: PoiExcelToHtmlUtil
 * @Description: TODO(poi转excel为html)
 */
public class POIReadExcelAll {
    private static  Map<String, Object> map[];
    /**
     * 程序入口方法（读取指定位置的excel，将其转换成html形式的字符串，并保存成同名的html文件在相同的目录下，默认带样式）
     * @return <table>...</table> 字符串
     */
    public static String excelWriteToHtml(String sourcePath,int sheetNum){
        File sourceFile = new File(sourcePath);
        try {
            InputStream fis = new FileInputStream(sourceFile);
            String excelHtml = readExcelToHtml(fis, true,sheetNum);
            return excelHtml;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String readExcelToHtml(InputStream is,boolean isWithStyle,int sheetNum){
        String htmlExcel = "";
        try {
            Workbook wb = WorkbookFactory.create(is);
            htmlExcel = readWorkbook(wb,isWithStyle,sheetNum);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return htmlExcel;
    }

    private static String readWorkbook(Workbook wb,boolean isWithStyle,int sheetNum){
        String htmlExcel="";
        if (wb instanceof XSSFWorkbook) {
            XSSFWorkbook xWb = (XSSFWorkbook) wb;
            htmlExcel = getExcelInfo(xWb, isWithStyle,sheetNum);
        }else if(wb instanceof HSSFWorkbook){
            HSSFWorkbook hWb = (HSSFWorkbook) wb;
            htmlExcel = getExcelInfo(hWb, isWithStyle,sheetNum);
        }
        return htmlExcel;
    }


    /**
     * 读取excel成string
     * @param wb
     * @param isWithStyle
     * @return
     */
    public static String getExcelInfo(Workbook wb, boolean isWithStyle,int sheetNum){

        StringBuffer sb = new StringBuffer();
        Sheet sheet = wb.getSheetAt(sheetNum);//获取第一个Sheet的内容
        // map等待存储excel图片
        Map<String, PictureData> sheetIndexPicMap = POIReadExcel.getSheetPictrues(0, sheet, wb);
        //临时保存位置，正式环境根据部署环境存放其他位置
        try {
            if(sheetIndexPicMap != null)
                POIReadExcel.printImg(sheetIndexPicMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //读取excel拼装html
        int lastRowNum = sheet.getLastRowNum();
        map = POIReadExcel.getRowSpanColSpanMap(sheet);
        sb.append("<table style='border-collapse:collapse;width:100%;'>");
        Row row = null;        //兼容
        Cell cell = null;    //兼容

        for (int rowNum = sheet.getFirstRowNum(); rowNum <= lastRowNum; rowNum ++) {
            if(rowNum > 1000) break;
            row = sheet.getRow(rowNum);

            int lastColNum = POIReadExcel.getColsOfTable(sheet)[0];
            int rowHeight = POIReadExcel.getColsOfTable(sheet)[1];

            if(null != row) {
                lastColNum = row.getLastCellNum();
                rowHeight = row.getHeight();
            }

            if (null == row) {
                sb.append("<tr><td >  </td></tr>");
                continue;
            }else if(row.getZeroHeight()){
                continue;
            }else if(0 == rowHeight){
                continue;     //针对jxl的隐藏行（此类隐藏行只是把高度设置为0，单getZeroHeight无法识别）
            }
            sb.append("<tr>");

            for (int colNum = 0; colNum < lastColNum; colNum ++) {
                if(sheet.isColumnHidden(colNum))	continue;
                String imageRowNum = "0_" + rowNum + "_" + colNum;
                String imageHtml = "";
                cell = row.getCell(colNum);
                if ((sheetIndexPicMap != null && !sheetIndexPicMap.containsKey(imageRowNum) || sheetIndexPicMap == null) && cell == null) {    //特殊情况 空白的单元格会返回null+//判断该单元格是否包含图片，为空时也可能包含图片
                    sb.append("<td>  </td>");
                    continue;
                }
                if(sheetIndexPicMap!=null && sheetIndexPicMap.containsKey(imageRowNum)){
                    //待修改路径
                    String imagePath = "D:\\pic" + imageRowNum + ".jpeg";

                    imageHtml = "<img src='" + imagePath + "' style='height:" + rowHeight / 20 + "px;'>";
                }
                String stringValue = POIReadExcel.getCellValue(cell);
                if (map[0].containsKey(rowNum + "," + colNum)) {
                    String pointString = (String)map[0].get(rowNum + "," + colNum);
                    int bottomeRow = Integer.valueOf(pointString.split(",")[0]);
                    int bottomeCol = Integer.valueOf(pointString.split(",")[1]);
                    int rowSpan = bottomeRow - rowNum + 1;
                    int colSpan = bottomeCol - colNum + 1;
                    if(map[2].containsKey(rowNum + "," + colNum)){
                        rowSpan = rowSpan - (Integer)map[2].get(rowNum + "," + colNum);
                    }
                    sb.append("<td rowspan= '" + rowSpan + "' colspan= '"+ colSpan + "' ");
                    if(map.length > 3 && map[3].containsKey(rowNum + "," + colNum)){
                        //此类数据首行被隐藏，value为空，需使用其他方式获取值
                        stringValue = POIReadExcel.getMergedRegionValue(sheet, rowNum, colNum);
                    }
                } else if (map[1].containsKey(rowNum + "," + colNum)) {
                    map[1].remove(rowNum + "," + colNum);
                    continue;
                } else {
                    sb.append("<td ");
                }

                //判断是否需要样式
                if(isWithStyle){
                    POIReadExcel.dealExcelStyle(wb, sheet, cell, sb);//处理单元格样式
                }

                sb.append(">");
                if(sheetIndexPicMap!=null && sheetIndexPicMap.containsKey(imageRowNum)) sb.append(imageHtml);
                if (stringValue == null || "".equals(stringValue.trim())) {
                    sb.append("   ");
                } else {
                    // 将ascii码为160的空格转换为html下的空格（ ）
                    sb.append(stringValue.replace(String.valueOf((char) 160)," "));
                }
                sb.append("</td>");
            }
            sb.append("</tr>");
            continue;
        }
        sb.append("</table>");
        return sb.toString();
    }


}
