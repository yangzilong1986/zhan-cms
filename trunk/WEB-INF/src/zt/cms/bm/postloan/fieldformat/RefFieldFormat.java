package zt.cms.bm.postloan.fieldformat;

import zt.platform.form.control.SessionContext;
import zt.platform.db.RecordSet;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.event.*;
import zt.platform.form.util.datatype.DataType;
import zt.platform.form.util.*;


public class RefFieldFormat extends FieldFormat
{
    public String format(SessionContext ctx, ElementBean eb1, FormInstance fi, RecordSet values) {
           String fldvalue = values.getString("CLIENTNO").trim();
           String body = "<a href=\"#\" onClick=\"opener.document.getElementById('winform').CLIENTNO.value='" + fldvalue + "';window.close();\">" + fldvalue + "</a>";
           //String body = "<a href=\"#\" onClick=\"opener.getElementById('winform').CLIENTNO=" + fldvalue + ";window.close();\">" + fldvalue + "</a>";
           return body;
//        }
    }

}