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
     * �ṩ��ȷ�ļӷ����㡣
     *
     * @param v1 ������
     * @param v2 ����
     * @return ���������ĺ�
     */

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();

    }

    /**
     * �ṩ��ȷ�ļ������㡣
     *
     * @param v1 ������
     * @param v2 ����
     * @return ���������Ĳ�
     */

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }


    /**
     * �ṩ��ȷ�ĳ˷����㡣
     * @param v1 ������
     * @param v2 ����
     * @return ���������Ļ�
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * �ṩ����ԣ���ȷ�ĳ������㣬�����������������ʱ����ȷ��
     * С�����Ժ�10λ���Ժ�������������롣
     * @param v1 ������
     * @param v2 ����
     * @return ������������
     */

    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }


    /**
     * �ṩ����ԣ���ȷ�ĳ������㡣�����������������ʱ����scale����ָ
     * �����ȣ��Ժ�������������롣
     * @param v1    ������
     * @param v2    ����
     * @param scale ��ʾ��ʾ��Ҫ��ȷ��С�����Ժ�λ��
     * @return ������������
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
     * �ṩ��ȷ��С��λ�������봦��
     * @param v     ��Ҫ�������������
     * @param scale С���������λ
     * @return ���������Ľ��
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
     * ��һ��double����ֵ��������
     * @param dSource
     * @return
     */
    public int getRound(double dSource) {
        int iRound;
        // BigDecimal�Ĺ��캯������������double
        BigDecimal deSource = new BigDecimal(dSource);
        // deSource.setScale(0,BigDecimal.ROUND_HALF_UP) ����ֵ���� BigDecimal
        // intValue() ������BigDecimalת��Ϊint
        iRound = deSource.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        return iRound;

    }

     public static String NumToChn(String input){  
         String s1="��Ҽ��������½��ƾ�";
         String s4="�ֽ���Ԫʰ��Ǫ��ʰ��Ǫ��ʰ��Ǫ";
         String temp="";
         String result="";
         if (input==null) return "������ִ��������ִ�ֻ�ܰ��������ַ���'0'~'9','.'),�����ִ����ֻ�ܾ�ȷ��Ǫ�ڣ�С����ֻ����λ��";
         temp=input.trim();
         float f;
         try{
             f=Float.parseFloat(temp);
         }catch(Exception e){
             return "������ִ��������ִ�ֻ�ܰ��������ַ���'0'~'9','.'),�����ִ����ֻ�ܾ�ȷ��Ǫ�ڣ�С����ֻ����λ��";
         }
         int len=0;
         if(temp.indexOf(".")==-1) len=temp.length();
         else len=temp.indexOf(".");
         if(len>s4.length()-3) return("�����ִ����ֻ�ܾ�ȷ��Ǫ�ڣ�С����ֻ����λ��");
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
         if((len==temp.length())||(len==temp.length()-1)) result=result.concat("��");
         if(len==temp.length()-2) result=result.concat("���");
         return result;
     }


}
