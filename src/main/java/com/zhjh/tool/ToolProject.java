package com.zhjh.tool;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/7/5.
 */
public class ToolProject {
    public static String getOrderId(){
        String name="";
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        name=df.format(new Date());// new Date()为获取当前系统时间
        return name;
    }
    public static String getPictureName(){
        String name="";
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        name=df.format(new Date());// new Date()为获取当前系统时间
        return name+".png";
    }

    public static String getAndoridAPKUrl(HttpServletRequest request){
        String path = request.getRealPath("/upload/android");
        return path;
    }

    public static String getGoodImgUrl(HttpServletRequest request){
        String path = request.getRealPath("/upload/goodimg");
        return path;
    }

    public static String getExcelUrl(HttpServletRequest request){
        String path = request.getRealPath("/WEB-INF/upload");
        return path;
    }
    public static String getImageUrl(HttpServletRequest request){
        String path = request.getRealPath("/image");
        return path;
    }
    public static String getHtmlUrl(HttpServletRequest request){
        String pathHtml = request.getRealPath("/WEB-INF/htmlExcel");
        return pathHtml;
    }

    public static boolean isHasPartent(String previous,String next  ){
        if(previous==null){
            previous="";
        }
        if(next==null){
            next="";
        }
        String str2=next;
        int pos = str2.lastIndexOf(".");
        if(pos==-1){
            return false;
        }else{
            String str3=str2.substring(0,pos);
            if(previous.equals(str3)){
                return true;
            }else{
                return false;
            }
        }
    }

    public static String getParentArrangement(String next  ){
        if(next==null){
            next="";
        }
        String str2=next;
        int pos = str2.lastIndexOf(".");
        if(pos==-1){
            return "-1";
        }else{
            String str3=str2.substring(0,pos);
           return str3;
        }
    }
}
