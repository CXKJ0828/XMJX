package com.zhjh.run;

/**
 * Created by Administrator on 2018/7/18.
 */
import com.zhjh.bean.ExcelBean;
import com.zhjh.file.POIReadExcel;
import org.apache.poi.ss.usermodel.*;

import java.util.concurrent.Callable;

/**
 *  一个包含返回值的线程类
 * @author xiezd 2018-01-14 21:40
 *
 */
public class ThreadWithCallback implements Callable{
    private Sheet sheet;
    private int start;
    private int end;
    private Workbook workbook;
    private ExcelBean excelBean;

    public ThreadWithCallback(Sheet sheet) {
        this.sheet = sheet;
    }

    public ThreadWithCallback(ExcelBean excelBean, int start, int end) {
        this.sheet = excelBean.getSheet();
        this.workbook=excelBean.getWorkbook();
        this.start = start;
        this.end = end;
    }


    //相当于Thread的run方法
    @Override
    public Object call() throws Exception {
       String content=POIReadExcel.getExcelInfo(workbook,sheet,true,0,start,end);
        return content;
    }

    public Sheet getExcelInfo(Workbook wb, boolean isWithStyle,int sheetNum){

        StringBuffer sb = new StringBuffer();
        Sheet sheet = wb.getSheetAt(sheetNum);//获取第一个Sheet的内容
        return sheet;
    }
}
