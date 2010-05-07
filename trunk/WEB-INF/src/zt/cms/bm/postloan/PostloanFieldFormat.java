package zt.cms.bm.postloan;

import com.zt.util.setup.SetupManager;
import zt.cmsi.pub.confitem;
import zt.cmsi.pub.define.SystemDate;
import zt.platform.db.DBUtil;
import zt.platform.db.RecordSet;
import zt.platform.form.config.ElementBean;
import zt.platform.form.config.EnumerationBean;
import zt.platform.form.config.EnumerationType;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FieldFormat;
import zt.platform.form.util.FormInstance;

import java.util.Calendar;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class PostloanFieldFormat extends FieldFormat {
    public static final int CHARACTOR_PIXEL = 6;

    /**
     * @param ctx
     * @param eb     name
     * @param fi     formInstance
     * @param values
     * @return String
     * @roseuid 3F7F4BAE0070
     */
    public String format(SessionContext ctx, ElementBean eb, FormInstance fi, RecordSet values) {
        try {
            String value = "";
            if (eb.getExpression() != null && eb.getExpression().trim().length() > 0) {
                value = getExpression(ctx, eb.getExpression(), fi, values);
                try {
                    value = DBUtil.doubleToStr1(Double.parseDouble(value));
                }
                catch (Exception e) {

                }
            } else {
                value = values.getString(eb.getName());
                if (value != null) {
                    value = value.trim();
                } else {
                    value = "";
                }
//                if ( eb.isNeedEncode() ) {
                value = DBUtil.fromDB(value);
//                }
                int width = eb.getWidth();
                width /= CHARACTOR_PIXEL;
                if (eb.getWidth() > 6 && value.length() > width) {
                    value = value.substring(0, width);
                }

                if (eb.getDataType() == ElementBean.DATATYPE_DECIMAL) {
                    try {
                        value = DBUtil.doubleToStr1(Double.parseDouble(value));
                    }
                    catch (Exception e) {

                    }
                } else if (eb.getDataType() == ElementBean.DATATYPE_INTEGER) {
                    try {
                        value = DBUtil.intToStr(Integer.parseInt(value));
                    }
                    catch (Exception e) {

                    }
                } else if (eb.getDataType() == ElementBean.DATATYPE_ENUMERATION) {
                    String enumname = eb.getEnutpname();
                    if (enumname != null) {
                        EnumerationBean enumb = EnumerationType.getEnu(enumname);
                        Integer intValue = new Integer(value);
                        String tmpStr = (String) enumb.getValue(intValue);
                        if (tmpStr != null && tmpStr.trim().length() > 0)
                            value = tmpStr.trim();
                    }
                }
            }


            //使枚举类型居中
            String style = "";
            String align = "";

            if (eb.getDataType() == ElementBean.DATATYPE_ENUMERATION) {
                align = " align=\"center\"";
            }
            if (hasEnterTheAlertDay(fi, values)) {
                style = " class=\"list_form_td_unviewed\"";
            }
            value = "<div " + align + style + ">" + value + "</div>";

            return value;
        } catch (Exception ex) {
            return null;
        }
    }


    boolean hasEnterTheAlertDay(FormInstance fi, RecordSet values) {
        Calendar currentDate = SystemDate.getSystemDate1();
        Calendar payDate = values.getCalendar("NOWENDDATE");
        int alertDays = -10;
        alertDays = SetupManager.getIntProperty(confitem.POST_LOAN_ALERT_DAYS);
        //System.out.println("alertDays="+alertDays);

        currentDate.add(Calendar.DATE, alertDays);
        //System.out.println("currentdate"+currentDate);
        //System.out.println("payDate"+payDate);
        if (currentDate.after(payDate)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        Calendar a = Calendar.getInstance();
        Calendar b = Calendar.getInstance();
        b.add(Calendar.DATE, -10);
        boolean ret = a.after(b);
        System.out.println("days" + ret);
    }


}
