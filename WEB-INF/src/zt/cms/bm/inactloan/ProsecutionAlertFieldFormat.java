package zt.cms.bm.inactloan;

import zt.cmsi.pub.define.SystemDate;
import zt.platform.db.DBUtil;
import zt.platform.db.RecordSet;
import zt.platform.form.config.ElementBean;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FieldFormat;
import zt.platform.form.util.FormInstance;

public class ProsecutionAlertFieldFormat
        extends FieldFormat {
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

        String APPDATE = SystemDate.getSystemDate2();
        APPDATE = "cast('" + APPDATE + "' as \"DATE\")-23 MONTH";
        String bmno = values.getString("BMNO");
        String value = values.getString(eb.getName());
        value = DBUtil.fromDB(value);
        //String sql="select max(RESPDATE) from BMILNOTIFI where BMNO=?";
        boolean ifRed = false;
        //String respdate=DBUtil.getCellValue("BMILNOTIFI","max(RESPDATE)","BMNO='"+bmno+"' and RESPDATE>="+APPDATE);
        String respdate = DBUtil.getCellValue("BMILNOTIFI", "max(\"DATE\")", "BMNO='" + bmno + "' and \"DATE\">=" + APPDATE);
        if (respdate == null || respdate.length() < 1) {
            ifRed = true;
        }

        StringBuffer tdBody = new StringBuffer("");
        tdBody.append("<div title=\"");
        //tdBody.append(respdate);
        tdBody.append("\">");
        tdBody.append("<font size=2");
        if (ifRed) {
            tdBody.append(" color=\"red\"");
        }
        tdBody.append(">");
        tdBody.append(value);
        tdBody.append("</font>");
        tdBody.append("</div>");
        return tdBody.toString();
    }
}
