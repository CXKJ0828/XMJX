package com.zhjh.util;

/**
 * Created by Administrator on 2019/3/27.
 */
public class StringUtil {
    /**
     * 字符串+1方法，该方法将其结尾的整数+1,适用于任何以整数结尾的字符串,不限格式，不限分隔符。
     * @author zxcvbnmzb
     * @param string 要+1的字符串
     * @return +1后的字符串
     * @exception NumberFormatException
     */
    public static String addOne(String testStr){
        String[] strs = testStr.split("[^0-9]");//根据不是数字的字符拆分字符串
        String numStr = strs[strs.length-1];//取出最后一组数字
        if(numStr != null && numStr.length()>0){//如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常
            int n = numStr.length();//取出字符串的长度
            int num = Integer.parseInt(numStr)+1;//将该数字加一
            String added = String.valueOf(num);
            n = Math.min(n, added.length());
            //拼接字符串
            return testStr.subSequence(0, testStr.length()-n)+added;
        }else {
            throw new NumberFormatException();
        }
    }

    private static void getNextIndex(char[] chars,int c_index){
        char value = chars[c_index];
        if(value == '9'){
            chars[c_index] = '0';
            if(c_index >= 1){
                getNextIndex(chars,c_index-1);
            }
        }else{
            chars[c_index]++ ;
        }
    }
}
