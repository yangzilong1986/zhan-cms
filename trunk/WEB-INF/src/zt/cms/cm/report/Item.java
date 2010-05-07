/*
 *  Copyright (c) 2002 Email: sundj@zhongtian.biz
 *  $Id: Item.java,v 1.1 2005/06/28 07:00:31 jgo Exp $
 */
package zt.cms.cm.report;

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
 *@created    2003Äê12ÔÂ2ÈÕ
 *@version    1.0
 */

public class Item implements Comparable {
    private int itemNo;
    private int printNo;
    private Report report;
    private double itemValue;
    private String itemName;


    /**
     *  Constructor for the Item object
     */
    public Item() { }


    /**
     *  Gets the itemNo attribute of the Item object
     *
     *@return    The itemNo value
     */
    public int getItemNo() {
        return itemNo;
    }


    /**
     *  Sets the itemNo attribute of the Item object
     *
     *@param  itemNo  The new itemNo value
     */
    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }


    /**
     *  Gets the itemName attribute of the Item object
     *
     *@return    The itemName value
     */
    public String getItemName() {
        String returnValue = itemName.replaceAll("~", "¡¡¡¡");
        return returnValue;
    }


    /**
     *  Gets the clearItemName attribute of the Item object
     *
     *@return    The clearItemName value
     */
    public String getClearItemName() {
        String returnValue = itemName.replaceAll("~", "");
        return returnValue;
    }



    /**
     *  Sets the itemName attribute of the Item object
     *
     *@param  itemName  The new itemName value
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    /**
     *  Gets the printNo attribute of the Item object
     *
     *@return    The printNo value
     */
    public int getPrintNo() {
        return printNo;
    }


    /**
     *  Sets the printNo attribute of the Item object
     *
     *@param  printNo  The new printNo value
     */
    public void setPrintNo(int printNo) {
        this.printNo = printNo;
    }


    /**
     *  Gets the report attribute of the Item object
     *
     *@return    The report value
     */
    public Report getReport() {
        return report;
    }


    /**
     *  Sets the report attribute of the Item object
     *
     *@param  report  The new report value
     */
    public void setReport(Report report) {
        this.report = report;
    }


    /**
     *  Gets the itemValue attribute of the Item object
     *
     *@return    The itemValue value
     */
    public double getItemValue() {
        return itemValue;
    }


    /**
     *  Sets the itemValue attribute of the Item object
     *
     *@param  itemValue  The new itemValue value
     */
    public void setItemValue(double itemValue) {
        this.itemValue = itemValue;
    }


    /**
     *  Description of the Method
     *
     *@param  o  Description of the Parameter
     *@return    Description of the Return Value
     */
    public int compareTo(Object o) {
        return getPrintNo() - ((Item) o).getPrintNo();
    }

}
