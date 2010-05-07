package zt.platform.form.util;

import java.io.Serializable;

import zt.cms.pub.SCBranch;
import zt.platform.db.RecordSet;
import zt.platform.form.config.ElementBean;
import zt.platform.form.control.SessionContext;

public class BRHIDFieldFormat
        extends FieldFormat implements Serializable{
    public String format(SessionContext ctx, ElementBean eb, FormInstance fi, RecordSet values) {
        String value = "";
        String BRHName = "";
        StringBuffer tdBody = new StringBuffer("");
        value = values.getString(eb.getName());
        if (value != null) {
            value = value.trim();
        } else {
            value = "";
        }
        BRHName = SCBranch.getLName(value);
        tdBody.append("<div title=\"");
        tdBody.append(BRHName);
        tdBody.append("\">");
        tdBody.append(value);
        tdBody.append("</div>");
        return tdBody.toString();
    }
}
