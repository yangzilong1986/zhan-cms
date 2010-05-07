package zt.cms.bm.ledger.fieldformat;

import zt.platform.form.control.SessionContext;
import zt.platform.db.RecordSet;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.event.*;
import zt.platform.form.util.datatype.DataType;
import zt.platform.form.util.*;


public class EditWordFieldFormat extends FieldFormat
{
    public String format(SessionContext ctx, ElementBean eb1, FormInstance fi, RecordSet values) {
        String name    = eb1.getName();
        FormBean fb    = FormBeanManager.getForm(fi.getFormid());
        String[] names = fb.getElementKeys();
        String value   = SessionAttributes.REQUEST_INSATNCE_ID_NAME + "=" + fi.getInstanceid() + "&" +
                         SessionAttributes.REQUEST_EVENT_ID_NAME + "=" + EventType.BUTTON_EVENT_TYPE+"&"+
                         SessionAttributes.REQUEST_BUTTON_EVENT_NAME + "=" + SessionAttributes.REQUEST_EDIT_BUTTON_VALUE + "&";
        int length = value.length();
        if ( names != null ) {
            for ( int i = 0 ; i < names.length ; i++ ) {
                ElementBean eb = fb.getElement(names[i]);
                if ( eb.isIsPrimaryKey() ) {
                    if ( value.length() != 0 && !value.endsWith("&") ) {
                        value += " &";
                    }
                    String fldvalue = values.getString(eb.getName());
                    if (fldvalue != null) {
                        fldvalue = fldvalue.trim();
                    }
                    value += eb.getName() + "=" + fldvalue;
                }
            }
        }

        if ( value.length() == length ) {
            return "ʹ���޸Ĺ��ܣ��붨������";
        }
        String actionurl = "";

        try {
            String targetFormId = eb1.getDefaultValue();

            if ( targetFormId != null ) {
                FormBean fb1 = FormBeanManager.getForm(targetFormId);
                if ( fb1 != null ) {
                    if ( fb1.getUrl() != null ) {
                        actionurl = fb1.getUrl();
                    }
                }
            }
        } catch ( Exception e ) {

        }
        if ( actionurl.trim().length() > 0 ) {
            actionurl = actionurl.trim();
        } else {
            actionurl = PageGenerator.DEFAULT_URL_LOCATE;
        }
//        if ( fi.isReadonly() ) {
//            String body = "<a class='list_edit_href' href='#' >" + eb1.getCaption() +
//                "</a>";
//
//            return body;
//        } else {
            String body = "<a class=\"list_edit_href\" href=\"#\" onclick=\"window.open('" +
                ctx.getUrl(actionurl) + "?" + value +
                "','FI"+fi.getInstanceid()+"','height=600,width=800,toolbar=no,scrollbars=yes,resizable=yes');\">" + "�޸�" +
                "</a>";


            return body;
//        }
    }

}
