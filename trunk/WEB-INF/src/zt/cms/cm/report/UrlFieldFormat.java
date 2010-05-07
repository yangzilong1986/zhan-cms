/*
 *  Copyright (c) 2002 Email: sundj@zhongtian.biz
 *  $Id: UrlFieldFormat.java,v 1.2 2005/07/07 07:45:21 jgo Exp $
 */
package zt.cms.cm.report;

import zt.platform.form.util.FieldFormat;
import java.util.regex.*;
import zt.platform.form.control.SessionContext;
import zt.platform.db.RecordSet;
import zt.platform.form.config.*;
import zt.platform.db.DBUtil;
import zt.platform.utils.expression.*;
import zt.platform.form.util.*;

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
 *@created    2003Äê11ÔÂ24ÈÕ
 *@version    1.0
 */

public class UrlFieldFormat extends FieldFormat {
    /**
     *  Constructor for the UrlFieldFormat object
     */
    public UrlFieldFormat() { }


    /**
     *  Description of the Method
     *
     *@param  ctx     Description of the Parameter
     *@param  eb      Description of the Parameter
     *@param  fi      Description of the Parameter
     *@param  values  Description of the Parameter
     *@return         Description of the Return Value
     */
    public String format(SessionContext ctx, ElementBean eb, FormInstance fi, RecordSet values) {
        String entityNo = values.getString("ENTITYNO");
        String reportDate = values.getString("DATE");
        String reportType = values.getString("REPORTTYPE");
        String flag="read";
        if(!fi.isReadonly()){
            flag="write";
        }

        //return "<a class=\"list_edit_href\" href=\"/report/report.jsp?ENTITYNO=" + entityNo + "&REPORTDATE=" + reportDate + "&REPORTTYPE=" + reportType.trim() + "&flag="+flag+"\">ÏêÏ¸</a>";
        return "<a class=\"list_edit_href\" href=\"#\" onclick=\"window.open('/report/report.jsp?ENTITYNO=" + entityNo + "&REPORTDATE=" + reportDate + "&REPORTTYPE=" + reportType.trim() + "&flag="+flag+"','FI41','height=600,width=700,toolbar=no,scrollbars=yes,resizable=yes');\">ÏêÏ¸</a>";
    }

}
