package com.ebis.ebank.format;

import java.text.*;
/**
 *  <p>
 *
 *  Title: </p> <p>
 *
 *  Description: </p> <p>
 *
 *  Copyright: Copyright (c) 2003</p> <p>
 *
 *  Company: </p>
 *
 *@author     not attributable
 *@created    2003Äê11ÔÂ12ÈÕ
 *@version    1.0
 */

public class Fomatter {
    /**
     *  Constructor for the Fomatter object
     */
    public Fomatter() { }


    /**
     *  Description of the Method
     *
     *@param  money  Description of the Parameter
     *@return        Description of the Return Value
     */
    public static String formatMoney(String money) {
        return formatMoney((double) Double.parseDouble(money));
    }


    /**
     *  Description of the Method
     *
     *@param  money  Description of the Parameter
     *@return        Description of the Return Value
     */
    public static String formatMoney(double money) {
        NumberFormat nf = new DecimalFormat("0.00");
        String pureNumber = nf.format(money);
        return fillBlank(pureNumber,11);
    }


    /**
     *  Description of the Method
     *
     *@param  money  Description of the Parameter
     *@return        Description of the Return Value
     */
    public static String formatMoney(float money) {
        return formatMoney((double) money);
    }


    /**
     *  Description of the Method
     *
     *@param  rate  Description of the Parameter
     *@return       Description of the Return Value
     */
    public static String formatRate(String rate) {
        return formatRate((double) Double.parseDouble(rate));
    }


    /**
     *  Description of the Method
     *
     *@param  rate  Description of the Parameter
     *@return       Description of the Return Value
     */
    public static String formatRate(double rate) {
        NumberFormat nf = new DecimalFormat("0.0000");
        String pureNumber = nf.format(rate);
        return fillBlank(pureNumber, 7);
    }


    /**
     *  Description of the Method
     *
     *@param  rate  Description of the Parameter
     *@return       Description of the Return Value
     */
    public static String formatRate(float rate) {
        return formatRate((double) rate);
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    private static String fillBlank(String pureNumber,int length) {
        StringBuffer sb = new StringBuffer();
        if (pureNumber.length() < length) {
            for (int i = 0; i < length - pureNumber.length(); i++) {
                sb.append(" ");
            }
            sb.append(pureNumber);
            return sb.toString();
        } else {
            return pureNumber;
        }

    }


}
