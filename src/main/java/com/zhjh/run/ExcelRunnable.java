package com.zhjh.run;

import com.zhjh.file.POIReadExcel;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2018/7/18.
 */
public class ExcelRunnable implements Runnable {
    private String path;
    private int sheetNum;
    private String content;
    private CountDownLatch downLatch;
    @Override
    public void run() {
        content= POIReadExcel.excelWriteToHtml(path,0,downLatch);
//        downLatch.countDown();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSheetNum() {
        return sheetNum;
    }

    public void setSheetNum(int sheetNum) {
        this.sheetNum = sheetNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CountDownLatch getDownLatch() {
        return downLatch;
    }

    public void setDownLatch(CountDownLatch downLatch) {
        this.downLatch = downLatch;
    }

    public ExcelRunnable() {
    }

    public ExcelRunnable(String path, int sheetNum, String content, CountDownLatch downLatch) {
        this.path = path;
        this.sheetNum = sheetNum;
        this.content = content;
        this.downLatch = downLatch;
    }
}
