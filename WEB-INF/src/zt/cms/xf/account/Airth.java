package zt.cms.xf.account;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-4-2
 * Time: 14:26:04
 * To change this template use File | Settings | File Templates.
 */

import java.math.BigDecimal;

public class Airth {
    private static final int DEF_DIV_SCALE = 10;

    private void Arith() {
    }


    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();

    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }


    /**
     * 提供精确的乘法运算。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */

    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }


    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

    }


    /**
     * 提供精确的小数位四舍五入处理。
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将一个double的数值四舍五入
     * @param dSource
     * @return
     */
    public int getRound(double dSource) {
        int iRound;
        // BigDecimal的构造函数参数类型是double
        BigDecimal deSource = new BigDecimal(dSource);
        // deSource.setScale(0,BigDecimal.ROUND_HALF_UP) 返回值类型 BigDecimal
        // intValue() 方法将BigDecimal转化为int
        iRound = deSource.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        return iRound;

    }

     public static String NumToChn(String input){  
         String s1="零壹贰叁肆伍陆柒捌玖";
         String s4="分角整元拾佰仟万拾佰仟亿拾佰仟";
         String temp="";
         String result="";
         if (input==null) return "输入的字串不是数字串只能包括以下字符（'0'~'9','.'),输入字串最大只能精确到仟亿，小数点只能两位！";
         temp=input.trim();
         float f;
         try{
             f=Float.parseFloat(temp);
         }catch(Exception e){
             return "输入的字串不是数字串只能包括以下字符（'0'~'9','.'),输入字串最大只能精确到仟亿，小数点只能两位！";
         }
         int len=0;
         if(temp.indexOf(".")==-1) len=temp.length();
         else len=temp.indexOf(".");
         if(len>s4.length()-3) return("输入字串最大只能精确到仟亿，小数点只能两位！");
         int n1=0;
         String num="";
         String unit="";
         for(int i=0;i<temp.length();i++){
             if(i>len+2){break;}
             if(i==len) {continue;}
             n1=Integer.parseInt(String.valueOf(temp.charAt(i)));
             num=s1.substring(n1,n1+1);
             n1=len-i+2;
             unit=s4.substring(n1,n1+1);
             result=result.concat(num).concat(unit);
         }
         if((len==temp.length())||(len==temp.length()-1)) result=result.concat("整");
         if(len==temp.length()-2) result=result.concat("零分");
         return result;
     }


}
