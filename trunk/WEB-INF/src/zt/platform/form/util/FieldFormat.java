//Source file: e:\\java\\zt\\platform\\form\\util\\FieldFormat.java

package zt.platform.form.util;

import zt.platform.db.DBUtil;
import zt.platform.db.RecordSet;
import zt.platform.form.config.ElementBean;
import zt.platform.form.config.EnumerationBean;
import zt.platform.form.config.EnumerationType;
import zt.platform.form.control.SessionContext;
import zt.platform.utils.expression.Parser;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字段格式化输出类
 *
 * @author 王学吉
 * @version 1.0
 */
public class FieldFormat implements Serializable {

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
                if (eb.getWidth() > 6 && value.getBytes().length > width) {
                    //value = value.substring(0, width);    //lj deleted in 20090527

                    //lj addeded in 20090527 for chinese words length substring
                    int flag = -1;
                    byte[] bStr = value.getBytes();
                    String cStr = new String(bStr, width - 1, 2);
                    if (cStr.length() == 1 && value.indexOf(cStr) > 0) {
                        width += flag;
                    }
                    value = new String(bStr, 0, width);
                }

                if (eb.getDataType() == ElementBean.DATATYPE_DECIMAL) {
                    try {
                        if (eb.getDecimalDigits() >= 3) {
                            value = DBUtil.doubleToStr1WithDigits(Double.parseDouble(value), eb.getDecimalDigits());
                        } else {
                            value = DBUtil.doubleToStr1(Double.parseDouble(value));
                        }
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
                        //lj del in 20090319
                        //Integer intValue = new Integer(value);
                        //String tmpStr = (String) enum.getValue(intValue);

                        String tmpStr = (String) enumb.getValue(value);//lj add in 20090319
                        if (tmpStr != null && tmpStr.trim().length() > 0)
                            value = tmpStr.trim();
                    }
                }
            }

            //使枚举类型居中
            if (eb.getDataType() == ElementBean.DATATYPE_ENUMERATION || eb.getDataType() == ElementBean.DATATYPE_DATE) {
                value = "<div align=\"center\">" + value + "</div>";
            }


            return value;
        } catch (Exception ex) {
            return null;
        }
    }

    //解析表达式
    //$name$变量值取自RecordSet
    //$$name$$变量值取自Context
    //$$$name$$$取自实例
    //$$$$name$$$$取自请求参数
    protected final String getExpression(SessionContext ctx, String s, FormInstance fi, RecordSet values) throws Exception {
        StringBuffer sb;
        String tmpStr = s;
        try {
            //1.替换所有的请求参数
            Pattern p1 = Pattern.compile("[$]{4}[a-zA-Z]+[$]{4}");
            Matcher m1 = p1.matcher(tmpStr);
            sb = new StringBuffer();

            while (m1.find()) {
                double unit = 0;
                try {
                    String name = m1.group();
                    name = name.substring(4, name.length() - 4);
                    String value = (String) ctx.getParameter(name);
                    unit = Double.parseDouble(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                m1.appendReplacement(sb, "" + unit);
            }
            m1.appendTail(sb);
            tmpStr = sb.toString();

            //2.替换所有的实例变量
            Pattern p2 = Pattern.compile("[$]{3}[a-zA-Z]+[$]{3}");
            Matcher m2 = p2.matcher(tmpStr);
            sb = new StringBuffer();

            while (m2.find()) {
                double unit = 0;
                try {
                    String name = m2.group();
                    name = name.substring(3, name.length() - 3);
                    String value = (String) fi.getValue(name).getValueArray()[0];
                    unit = Double.parseDouble(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                m2.appendReplacement(sb, "" + unit);
            }
            m2.appendTail(sb);
            tmpStr = sb.toString();

            //3.替换所有的上下文变量
            Pattern p3 = Pattern.compile("[$]{2}[a-zA-Z]+[$]{2}");
            Matcher m3 = p3.matcher(tmpStr);
            sb = new StringBuffer();
            while (m3.find()) {
                double unit = 0;
                try {
                    String name = m3.group();
                    name = name.substring(2, name.length() - 2);
                    String value = (String) ctx.getAttribute(name);
                    unit = Double.parseDouble(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                m3.appendReplacement(sb, "" + unit);
            }
            m3.appendTail(sb);
            tmpStr = sb.toString();

            //4.替换所有的结果集变量
            Pattern p4 = Pattern.compile("[$]{1}[a-zA-Z]+[$]{1}");
            Matcher m4 = p4.matcher(tmpStr);
            sb = new StringBuffer();
            while (m4.find()) {
                double unit = 0;
                try {
                    String name = m4.group();
                    name = name.substring(1, name.length() - 1);
                    String value = (String) values.getString(name);
                    unit = Double.parseDouble(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                m4.appendReplacement(sb, "" + unit);
            }
            m4.appendTail(sb);
            tmpStr = sb.toString();
            try {
                Parser p = new Parser();
                p.parse(tmpStr);
                return "" + p.getResult();
            } catch (Exception e) {
                return e.getMessage();
            }
        } catch (Exception e) {
            sb = new StringBuffer();
            sb.append(s + "【" + e.getMessage() + "】");
        }

        return sb.toString();
    }
}
