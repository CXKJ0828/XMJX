package com.zhjh.tool;

/**
 * Created by Administrator on 2018/7/18.
 */

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpMethod;
import sun.net.www.http.HttpClient;
import sun.net.www.protocol.http.HttpURLConnection;

import javax.print.DocFlavor;

/**
 * 测试POI
 * @author alex
 *
 */
public class PoiTest {

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Boolean result = false;
        int count = 0;
        while(!result) {
            try {
                Thread.sleep(1 * 1000); //设置暂停的时间 5 秒
                count ++ ;
                HttpURLConnection conn = null;
                URL realUrl = null;
                try {
                    realUrl = new URL("http://www.cxkjstu.xyz");
                    conn = (HttpURLConnection) realUrl.openConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(sdf.format(new Date()) + "--循环执行第" + count + "次");
                if (count == 100) {
                    result = true;
                    break ;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        }

    /**
     * 使用多线程进行Excel写操作，提高写入效率。
     */
    public static void multiThreadWrite() {
        String sourcePath="C://数据库.xlsx";
        File sourceFile = new File(sourcePath);
        Sheet sheet=null;
        try {
            InputStream fis = new FileInputStream(sourceFile);
            Workbook wb = WorkbookFactory.create(fis);
            if (wb instanceof XSSFWorkbook) {
                XSSFWorkbook xWb = (XSSFWorkbook) wb;
                sheet = wb.getSheetAt(0);//获取第一个Sheet的内容
            }else if(wb instanceof HSSFWorkbook){
                HSSFWorkbook hWb = (HSSFWorkbook) wb;
                sheet = wb.getSheetAt(0);//获取第一个Sheet的内容
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String content="";
        ExecutorService executors = Executors.newFixedThreadPool(10);
//        try {
//            /* 启动线程时会返回一个Future对象。
//             * 可以通过future对象获取现成的返回值。
//             * 在执行future.get()时，主线程会堵塞，直至当前future线程返回结果。
//             */
//            Future future1 = executors.submit(new ThreadWithCallback(sheet,0,sheet.getLastRowNum()/2));
//            Future future2 = executors.submit(new ThreadWithCallback(sheet,sheet.getLastRowNum()/2+1,sheet.getLastRowNum()));
////            Future future2 = executors.submit(new ThreadWithCallback(30));
//            content=content+future1.get().toString()+future2.get().toString();
////            System.out.println(future2.get());
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }finally {
//            executors.shutdown();
//        }
System.out.println(content);
    }

    /**
     * 测试基本的POI写操作
     */
    public static void poiBasicWriteTest() {
        try {
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(
                    "E:\\temp\\poiTest.xls"));
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row = sheet.createRow(0);
            HSSFCell contentCell = row.createCell(0);
            contentCell.setCellValue("abc");
            FileOutputStream os = new FileOutputStream("E:\\temp\\poiTest.xls");
            wb.write(os);
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sheet的row使用treeMap存储的，是非线程安全的，所以在创建row时需要进行同步操作。
     * @param sheet
     * @param rownum
     * @return
     */
    private static synchronized HSSFRow getRow(HSSFSheet sheet, int rownum) {
        return sheet.createRow(rownum);
    }

    /**
     * 进行sheet写操作的sheet。
     * @author alex
     *
     */
    protected static class PoiWriter implements Runnable {

        private final CountDownLatch doneSignal;

        private HSSFSheet sheet;

        private int start;

        private int end;

        public PoiWriter(CountDownLatch doneSignal, HSSFSheet sheet, int start,
                         int end) {
            this.doneSignal = doneSignal;
            this.sheet = sheet;
            this.start = start;
            this.end = end;
        }

        public void run() {
            int i = start;
            try {
                while (i <= end) {
                    HSSFRow row = getRow(sheet, i);
                    HSSFCell contentCell = row.getCell(0);
                    if (contentCell == null) {
                        contentCell = row.createCell(0);
                    }
                    contentCell.setCellValue(i + 1);
                    ++i;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                doneSignal.countDown();
                System.out.println("start: " + start + " end: " + end
                        + " Count: " + doneSignal.getCount());
            }
        }

    }

}
