package com.zhjh.tool;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.zhjh.bean.FileBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2018/7/2.
 */
public class ToolCommon {
    public static void writeToFile(String data){
        byte[] sourceByte = data.getBytes();
        String path = "D:/";
        String fileName = "cxkjexort.txt";
        if(null != sourceByte){
            try {
                File file = new File(path+fileName);//文件路径（路径+文件名）
                if (!file.exists()) {   //文件不存在则创建文件，先创建目录
                    File dir = new File(file.getParent());
                    dir.mkdirs();
                    file.createNewFile();
                }
                FileOutputStream outStream = new FileOutputStream(file); //文件输出流将数据写入文件
                outStream.write(sourceByte);
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                // do something
            } finally {
                // do something
            }
        }
    }


    public static String excelUrl="";
    /**
     * 获取当前时间
     * @return
     */
    public static String getNowTime(){
        String nowTime="";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        nowTime=df.format(new Date());// new Date()为获取当前系统时间
        return nowTime;
    }

    public static String getNowTimeNoDay(){
        String nowTime="";
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        nowTime=df.format(new Date());// new Date()为获取当前系统时间
        return nowTime;
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    public static FileBean uploadFileNotChangeName(String path,MultipartFile file){
        Integer width=0;
        Integer height=0;
        try{
           BufferedImage bufferedImage =ImageIO.read(file.getInputStream()); // 通过MultipartFile得到InputStream，从而得到BufferedImage
           if (bufferedImage == null) {
               // 证明上传的文件不是图片，获取图片流失败，不进行下面的操作
           }
           width = bufferedImage.getWidth();
           height = bufferedImage.getHeight();
       }catch (Exception e){

       }
        // 文件不为空
        if(!file.isEmpty()) {
            File filesave = new File(path);
            //判断上传文件的保存目录是否存在
            if (!filesave.exists() && !filesave.isDirectory()) {
//				System.out.println(savePath+"目录不存在，需要创建");
                //创建目录
                filesave.mkdirs();
            }

            String origFilename = file.getOriginalFilename(); // 图片名
            File dest = new File(path + origFilename); // 保存位置
            String name =file.getOriginalFilename();


            File destFile = new File(path,name);
            excelUrl=destFile.getPath();
            // 转存文件
            try {
                file.transferTo(destFile);
                String urlExcel=path+File.separator+name;
                FileBean fileBean=new FileBean(name,urlExcel);
                zipWidthHeightImageFile(urlExcel,urlExcel,width,height,0.25f);
                return fileBean;
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    public static String getNowDay(){
        String nowTime="";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        nowTime=df.format(new Date());// new Date()为获取当前系统时间
        return nowTime;
    }

    public static String computeEstimateCompleteTime(String startTime,String overTimeLimit){
        String estimateCompleteTime="";
        String workStartTime=" 08:00:00";
        String workEndTime=ToolCommon.getNowDay()+" 18:00:00";
        int overTimeLimitInt=Integer.parseInt(overTimeLimit)*60;//分钟
        String endTime=ToolCommon.addMinute(startTime,overTimeLimitInt);
        if(workEndTime.compareTo(endTime)>0){
            estimateCompleteTime=endTime;
        }else{
            int minuteDvalue=ToolCommon.getMinuteTimeDistance(startTime,workEndTime);//当天使用分钟
            int surpluMinute=overTimeLimitInt-minuteDvalue;//剩余分钟
            double days=surpluMinute/(10*60);
            double minute=surpluMinute%(10*60);
            int daysInt=new Double(days).intValue();
            int minuteInt=new Double(minute).intValue();
            if(days<1){
                estimateCompleteTime=ToolCommon.addMinute(ToolCommon.addDay(ToolCommon.getNowDay(),1)+" "+workStartTime,surpluMinute);
            }else{
                estimateCompleteTime=ToolCommon.addMinute(ToolCommon.addDay(ToolCommon.getNowDay(),1+daysInt)+" "+workStartTime,minuteInt);
            }
        }
        return estimateCompleteTime;
    }

    public static String getNowMonth(){
        String nowTime="";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");//设置日期格式
        nowTime=df.format(new Date());// new Date()为获取当前系统时间
        return nowTime;
    }

    public static FileBean uploadExcelFile(MultipartFile file, HttpServletRequest request){
// 文件不为空
        if(!file.isEmpty()) {
            String path = ToolProject.getExcelUrl(request);
            File filesave = new File(path);
            //判断上传文件的保存目录是否存在
            if (!filesave.exists() && !filesave.isDirectory()) {
//				System.out.println(savePath+"目录不存在，需要创建");
                //创建目录
                filesave.mkdir();
            }

//			// 文件存放路径
//			String path = request.getServletContext().getRealPath("/")+"\order\";
            // 文件名称
            String name = String.valueOf(new Date().getTime()+"_"+file.getOriginalFilename());
            // 访问的url
//            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath() + "/" + name;
            File destFile = new File(path,name);
            excelUrl=destFile.getPath();
            // 转存文件
            try {
                file.transferTo(destFile);
                String urlExcel=path+File.separator+name;
                FileBean fileBean=new FileBean(name,urlExcel);
                return fileBean;
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    /**
     * base64字符串转换成图片
     * @param imgStr		base64字符串
     * @param imgFilePath	图片存放路径
     * @return
     *
     * @author ZHANGJL
     * @dateTime 2018-02-23 14:42:17
     */
    public static boolean Base64ToImage(String imgStr,String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片

        if (imgStr==null||imgStr.equals("")) // 图像数据为空
            return false;

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }

            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();

            return true;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 获取时间段差值
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getTimeDistance(String startTime,String endTime){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = null;
        String distance="";
        try {
            now = df.parse(endTime);
            Date date=df.parse(startTime);
            long l=now.getTime()-date.getTime();
            long day=l/(24*60*60*1000);
            long hour=(l/(60*60*1000)-day*24);
            long min=((l/(60*1000))-day*24*60-hour*60);
            long s=(l/1000-day*24*60*60-hour*60*60-min*60);
            if(day>0){
                distance=distance+day+"天";
            }
            if(hour>0){
                distance=distance+hour+"小时";
            }
            if(min>0){
                distance=distance+min+"分";
            }
            if(s>0){
                distance=distance+s+"秒";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return distance;
    }
    /**
     * 对比time1与time2大小 time1大为true time2小为false
     * @param time1
     * @param time2
     * @return
     */
    public static boolean compareTimeSize(String time1,String time2){
        if(time1.compareTo(time2)>0){
            return true;
        }else{
            return false;
        }
    }
    public static String getHourTimeDistance(String startTime,String endTime){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = null;
        String distance="";
        try {
            now = df.parse(endTime);
            Date date=df.parse(startTime);
            long l=now.getTime()-date.getTime();
            long day=l/(24*60*60*1000);
            long hour=(l/(60*60*1000)-day*24);
            long min=((l/(60*1000))-day*24*60-hour*60);
            long s=(l/1000-day*24*60*60-hour*60*60-min*60);
            double hourSum=day*24+hour+min*(1/60.0)+s*(1/3600.0);
            distance= String.format("%.1f",hourSum);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return distance;
    }

    public static int getMinuteTimeDistance(String startTime,String endTime){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = null;
        int distance=0;
        try {
            now = df.parse(endTime);
            Date date=df.parse(startTime);
            long l=now.getTime()-date.getTime();
            long day=l/(24*60*60*1000);
            long hour=(l/(60*60*1000)-day*24);
            long min=((l/(60*1000))-day*24*60-hour*60);
            long s=(l/1000-day*24*60*60-hour*60*60-min*60);
            double minuteSum=day*24*60+hour*60+min+s*60;
            distance= (int)minuteSum;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return distance;
    }

    /***
     * 压缩制定大小图片
     *
     * @param oldPath  临时图片路径
     * @param copyPath 压缩图片保存路径
     * @param width    宽度
     * @param height   高度
     * @param quality  高清度
     * @return
     * @throws Exception
     */
    private static Boolean zipWidthHeightImageFile(String oldPath, String copyPath, int width, int height,
                                            float quality) {
        Boolean sta = false;
        File oldFile = new File(oldPath);
        File newFile = new File(copyPath);
        if (oldFile == null) {
            return null;
        }
        String newImage = null;
        try {
            /** 对服务器上的临时文件进行处理 */
            Image srcFile = ImageIO.read(oldFile);
            int w = srcFile.getWidth(null);
            System.out.println(w);
            int h = srcFile.getHeight(null);
            System.out.println(h);

            /** 宽,高设定 */
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(srcFile, 0, 0, width, height, null);
            //String filePrex = oldFile.substring(0, oldFile.indexOf('.'));
            /** 压缩后的文件名 */
            //newImage = filePrex + smallIcon+ oldFile.substring(filePrex.length());

            /** 压缩之后临时存放位置 */
            FileOutputStream out = new FileOutputStream(newFile);

            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
            /** 压缩质量 */
            jep.setQuality(quality, true);
            encoder.encode(tag, jep);
            out.close();
            sta = true;
        } catch (Exception e) {
            e.printStackTrace();
            sta = false;
        }
        return sta;
    }

    public static String getDayDistance(String startTime,String endTime){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = null;
        String distance="";
        try {
            now = df.parse(endTime);
            Date date=df.parse(startTime);
            long l=now.getTime()-date.getTime();
            long day=l/(24*60*60*1000);
            long hour=(l/(60*60*1000)-day*24);
            long min=((l/(60*1000))-day*24*60-hour*60);
            long s=(l/1000-day*24*60*60-hour*60*60-min*60);
            if(day>0){
                distance=distance+day+"天";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return distance;
    }

    /**
     * 字符串转成整型
     * @param content
     * @return
     */
    public static int StringToInteger(String content){
        int result=0;
        if(content!=null&&!content.equals("")){
            if(NumberValidationUtils.isRealNumber(content)){
                if(content.contains(".")){
                    String[] contents=content.split("\\.");
                    result=Integer.parseInt(contents[0]);
                }else{
                    result=Integer.parseInt(content);
                }
            }
        }
        return result;
    }

    /**
     * 对字节数组字符串进行Base64解码并生成图片
     * @param imgStr
     * @param imgFilePath
     * @return
     */
    public static boolean GenerateImage(String imgStr, String imgFilePath,String name) {// 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null){// 图像数据为空
            return false;
        }else{
            String path = imgFilePath;
            File filesave = new File(path);
            //判断上传文件的保存目录是否存在
            if (!filesave.exists() && !filesave.isDirectory()) {
//				System.out.println(savePath+"目录不存在，需要创建");
                //创建目录
                filesave.mkdir();
            }
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                // Base64解码
                byte[] bytes = decoder.decodeBuffer(imgStr);
                for (int i = 0; i < bytes.length; ++i) {
                    if (bytes[i] < 0) {// 调整异常数据
                        bytes[i] += 256;
                    }
                }
                // 生成jpeg图片
                OutputStream out = new FileOutputStream(imgFilePath+"/"+name);
                out.write(bytes);
                out.flush();
                out.close();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
    //返回 list 对象数组  字符串JSON要添加[]
    public static Object json2ObjectList(String strJson, @SuppressWarnings("rawtypes")Class beanClass) {
        return JSONArray.toCollection(JSONArray.fromObject(strJson), beanClass);
    }

    //返回 list 对象数组  字符串JSON要添加[]
    public static Object json2Object(String strJson, @SuppressWarnings("rawtypes")Class beanClass) {
        JSONObject jsonObject=JSONObject.fromObject(strJson);
        return JSONObject.toBean(jsonObject, beanClass);
    }

    //返回 list 对象数组  字符串JSON要添加[]
    public static JSONObject  json2Object(String strJson) {
          JSONObject jsonObject = JSONObject.fromObject(strJson);
          return jsonObject;
    }

    /**
     * 解决HTML用ajax传递中文汉字乱码
     * @param content
     * @return
     */
    public static String changeContentFromHtmlMessyCode(String content){
        if(!content.equals("")){
            try {
                content= new String(content.getBytes("iso-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    public static String TimeLongToStringTime(long timeLong){
        String distance="";
        long day=timeLong/(24*60*60*1000);
        long hour=(timeLong/(60*60*1000)-day*24);
        long min=((timeLong/(60*1000))-day*24*60-hour*60);
        long s=(timeLong/1000-day*24*60*60-hour*60*60-min*60);
        if(day>0){
            distance=distance+day+"天";
        }
        if(hour>0){
            distance=distance+hour+"小时";
        }
        if(min>0){
            distance=distance+min+"分";
        }
        if(s>0){
            distance=distance+s+"秒";
        }
        System.out.print(distance);
        return distance;
    }

    /**
     * 下载文件
     * @param file
     * @param name 导出文件名称
     * @param response
     */
    public static void exportFile(File file,String name,HttpServletResponse response){
            if(file.exists()){
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment; filename="+name);
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(file);
                    byte[] by  = new byte[fileInputStream.available()];
                    fileInputStream.read(by);
                    OutputStream outputStream = response.getOutputStream();
                    outputStream.write(by);
                    fileInputStream.close();
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
    }

    /**
     * 将字符串日期加一天
     * @param s ：为时间字符串（例如：“2019-05-30”）
     * @param n ：如果日期加一天，那么n传1
     * @return
     */
    public static String addMonth(String s,int n){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");  //构造格式化日期的格式
        Calendar cd = Calendar.getInstance();
        try {
            cd.setTime(sdf.parse(s));
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        cd.add(Calendar.DATE,n); //当n=1代表是增加一天
        //cd.add(Calendar.YEAR, 1);//增加一年
        //cd.add(Calendar.DATE, -10);//减10天
        cd.add(Calendar.MONTH, n);//n=1代表增加一个月
        return sdf.format(cd.getTime());  //format(Date date)方法：将制定的日期对象格式，化为指定格式的字符串（例如：“yyyy-MM-dd”）
    }

    /**
     * 将字符串日期加一天
     * @param s ：为时间字符串（例如：“2019-05-30”）
     * @param n ：如果日期加一天，那么n传1
     * @return
     */
    public static String addDay(String s,int n){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  //构造格式化日期的格式
        Calendar cd = Calendar.getInstance();
        try {
            cd.setTime(sdf.parse(s));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cd.add(Calendar.DATE,n); //当n=1代表是增加一天
        //cd.add(Calendar.YEAR, 1);//增加一年
        //cd.add(Calendar.DATE, -10);//减10天
//        cd.add(Calendar.MONTH, n);//n=1代表增加一个月
        return sdf.format(cd.getTime());  //format(Date date)方法：将制定的日期对象格式，化为指定格式的字符串（例如：“yyyy-MM-dd”）
    }

    public static String addHour(String s,int n){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //构造格式化日期的格式
        Calendar cd = Calendar.getInstance();
        try {
            cd.setTime(sdf.parse(s));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cd.add(Calendar.HOUR,n); //当n=1代表是增加一天
        return sdf.format(cd.getTime());  //format(Date date)方法：将制定的日期对象格式，化为指定格式的字符串（例如：“yyyy-MM-dd”）
    }

    public static String addMinute(String s,int n){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //构造格式化日期的格式
        Calendar cd = Calendar.getInstance();
        try {
            cd.setTime(sdf.parse(s));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cd.add(Calendar.MINUTE,n); //当n=1代表是增加一分钟
        return sdf.format(cd.getTime());  //format(Date date)方法：将制定的日期对象格式，化为指定格式的字符串（例如：“yyyy-MM-dd”）
    }

    /**
     * float转换成money float
     * @param content
     * @return
     */
    public static float FloatToMoney(float content){
        DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p=decimalFormat.format(content);//format 返回的是字符串
        return StringToFloat(p);
    }

    public static float FloatTo4Float(float content){
        DecimalFormat decimalFormat=new DecimalFormat(".0000");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p=decimalFormat.format(content);//format 返回的是字符串
        return StringToFloat(p);
    }

    /**
     * 字符串转换成float
     * @param content
     * @return
     */
    public static float StringToFloat(String content){
        float result=0;
        if(content!=null&&!content.equals("")) {
            content=content.replace(",","");
            content=content.replace(" ","");
            if(NumberValidationUtils.isRealNumber(content)){
                try {
                    result=Float.parseFloat(content);
                }catch (Exception e){
                    result=0;
                }
            }

        }
        return result;
    }

    public static double StringToDouble(String content){
        double result=0;
        if(content!=null&&!content.equals("")) {
            content=content.replace(",","");
            content=content.replace(" ","");
            if(NumberValidationUtils.isRealNumber(content)){
                try {
                    result=Double.parseDouble(content);
                }catch (Exception e){
                    result=0;
                }
            }

        }
        return result;
    }

    /**
     * double类型数字保留两位小数 四舍五入
     * @param content
     * @return
     */
    public static double Double2(double content){
        BigDecimal bg = new BigDecimal(content);
        content = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return content;
    }
    /**
     * double类型数字保留两位小数 不四舍五入
     * @param content
     * @return
     */
    public static String Double2NotIn(double content){
        DecimalFormat formater = new DecimalFormat("#0.##");
        formater.setRoundingMode(RoundingMode.FLOOR);
        return formater.format(content);
    }

    /**
     * double类型数字保留四位小数 四舍五入
     * @param content
     * @return
     */
    public static double Double4(double content){
        BigDecimal bg = new BigDecimal(content);
        content = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        return content;
    }

    /**
     * 上传文件
     * @param file
     * @param request
     * @return
     */
    public static FileBean uploadFile(MultipartFile file, HttpServletRequest request){
// 文件不为空
        if(!file.isEmpty()) {
            String path = ToolProject.getExcelUrl(request);
            File filesave = new File(path);
            //判断上传文件的保存目录是否存在
            if (!filesave.exists() && !filesave.isDirectory()) {
//				System.out.println(savePath+"目录不存在，需要创建");
                //创建目录
                filesave.mkdir();
            }

//			// 文件存放路径
//			String path = request.getServletContext().getRealPath("/")+"\order\";
            // 文件名称
            String name = String.valueOf(new Date().getTime()+"_"+file.getOriginalFilename());
            // 访问的url
//            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath() + "/" + name;
            File destFile = new File(path,name);
            excelUrl=destFile.getPath();
            // 转存文件
            try {
                file.transferTo(destFile);
                String urlExcel=path+File.separator+name;
                FileBean fileBean=new FileBean(name,urlExcel);
                return fileBean;
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public static FileBean uploadPicture(MultipartFile file, HttpServletRequest request){
// 文件不为空
        if(!file.isEmpty()) {
            String path = ToolProject.getImageUrl(request);
            File filesave = new File(path);
            //判断上传文件的保存目录是否存在
            if (!filesave.exists() && !filesave.isDirectory()) {
//				System.out.println(savePath+"目录不存在，需要创建");
                //创建目录
                filesave.mkdir();
            }

//			// 文件存放路径
//			String path = request.getServletContext().getRealPath("/")+"\order\";
            // 文件名称
            String name = String.valueOf(new Date().getTime()+"_"+file.getOriginalFilename());
            // 访问的url
//            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath() + "/" + name;
            File destFile = new File(path,name);
            excelUrl=destFile.getPath();
            // 转存文件
            try {
                file.transferTo(destFile);
                String urlExcel=path+File.separator+name;
                FileBean fileBean=new FileBean(name,urlExcel);
                return fileBean;
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
    /**
     * 移除字符串第一个符号
     * @param content
     * @param appoint
     * @return
     */
    public static String removeContentFirstAppoint(String content,String appoint){
        String result="";
        int location=content.indexOf(appoint);
        result=content.substring(0,location);
        return result;
    }

    public static boolean isContain(String str,String content){
        if(str.contains(content)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public static boolean isNull(String str){
        if(str==null||str.equals("")||str.equals("null")){
            return true;
        }else{
            return false;
        }
    }

    public static String setContentToRed(String content){
        content="<span style=color:#ff0030;font-weight:bold>"+content+"</span>";
        return content;
    }

    public static String setContentToGreen(String content){
        content="<span style='color:#048133;font-weight:bold;'>"+content+"</span>";
        return content;
    }

    public static String setContentToRedFontBig(String content){
        content="<span style=color:#ff0030;font-weight:bold;font-size:18px>"+content+"</span>";
        return content;
    }

    public static String setContentToGreenFontBig(String content){
        content="<span style='color:#048133;font-weight:bold;font-size:18px'>"+content+"</span>";
        return content;
    }

    public static void main(String[] args) {

    }
}
