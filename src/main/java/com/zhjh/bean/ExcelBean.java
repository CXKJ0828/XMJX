package com.zhjh.bean;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Created by Administrator on 2018/7/4.
 */
public class ExcelBean {
    public int id;//行数 相当于tr的id
    public String content;//Excel表格解析成HTML内容
    public int row;//行
    public int col;//列
    public Sheet sheet;
    public Workbook workbook;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public ExcelBean(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public ExcelBean() {
    }

    public ExcelBean(Sheet sheet, Workbook workbook) {
        this.sheet = sheet;
        this.workbook = workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }
}
