package zt.platform.form.control;

/**
 * �ο�ҳ��������
 *
 * @author ���滻
 * @version 1.0
 */

import zt.platform.db.DBUtil;
import zt.platform.db.RecordSet;
import zt.platform.form.component.AbstractFormComponent;
import zt.platform.form.config.*;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.FormInstanceManager;
import zt.platform.form.util.SessionAttributes;
import zt.platform.form.util.datatype.ComponentType;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventType;
import zt.platform.utils.ErrorCode;

import java.util.StringTokenizer;

public class ReferenceGenerator {
    public static final String TARGET_TEMPLATE = "/templates/ref.jsp";
    public static final String TARGET_JAVASCRIPT = "/js/ref.js";

    public static String[] run(SessionContext ctx, Event event, ErrorMessages msgs, int result) {
        RecordSet rs = (RecordSet) ctx.getRequestAttribute(SessionAttributes.REQUEST_REF_RESULT_NAME);
        String pageno = (String) ctx.getRequestAttribute(SessionAttributes.REQUEST_LIST_PAGENO_NAME);
        String pagecount = (String) ctx.getRequestAttribute(SessionAttributes.REQUEST_LIST_PAGECOUNT_NAME);
        int iPageno = 0;
        int iPageCount = 0;
        try {
            iPageno = Integer.parseInt(pageno);
        }
        catch (Exception e) {
            iPageno = 0;
        }
        try {
            iPageCount = Integer.parseInt(pagecount);
        }
        catch (Exception e) {
            iPageCount = 0;
        }

        String body = "";
        String sysButton = "";
        String[] rtnMsg = new String[2];

//    if (event.getBefore_result() < 0 || rs == null) {
//      rtnMsg[0] = PageGenerator.getErrorString(msgs, result);
//      return rtnMsg;
//    }

        if (result < 0) body += PageGenerator.getErrorString(msgs, result);
        if (false) {

        } else {
            //1.��òο���Ͳο���λ
            body += "<script src='" + ctx.getUrl(TARGET_JAVASCRIPT) + "' type='text/javascript'></script>";
            body += "<script src='" + ctx.getUrl("/js/meizzDate.js") + "' type='text/javascript'></script>";
            try {
                FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(
                        SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
                String instanceid = event.getId();
                String reffldname = ctx.getParameter(SessionAttributes.REQUEST_REFERENCE_FIELD_NAME);
                String oldreffldnm = reffldname;
                //�����QueryForm�Ĺ��˲ο����ֶΣ���ص�largelarge(��QueryGenerator����ӵ�)
                if (reffldname != null && reffldname.endsWith("largelarge")) {
                    reffldname = reffldname.substring(0, reffldname.length() - "largelarge".length());
                }

                FormInstance fi = fiManager.getFormInstance(instanceid);
                if (fi == null) {
                    msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
                    event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
                    return null;
                }
                FormBean fb = fi.getFormBean();
                String tblname = fb.getTbl();
                TableBean tb = TableBeanManager.getTable(tblname);
                FieldBean field = tb.getField(reffldname);
                String reftbl = field.getReftbl();
                String refname = field.getRefnamefld();
                String[] refnames;
                try {
                    StringTokenizer st = new StringTokenizer(refname, ",");
                    int refnamecount = st.countTokens();
                    refnames = new String[refnamecount];
                    for (int i = 0; i < refnamecount && st.hasMoreTokens(); i++) {
                        refnames[i] = st.nextToken();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    refnames = new String[1];
                    refnames[0] = refname;
                }

                String refvalue = field.getRefvaluefld();
                //2.��òο��ֶ�

                TableBean tb1 = TableBeanManager.getTable(reftbl);
                if (tb1 == null) {
                    rtnMsg[0] = "�ο��������[" + reftbl + "]";
                    return rtnMsg;
                }
                FieldBean[] fb1s = tb1.getSearchKey();
                FieldBean fbvalue = tb1.getField(refvalue);
                if (fbvalue == null) {
                    rtnMsg[0] = "�ο��ֶζ������[" + refname + "��" + refvalue + "]";
                    return rtnMsg;
                }
                //����������Ͳ�ѯ�������
                String hiddentxt = "";
                String searchtxt = "";
                if (fb1s != null) {
                    for (int i = 0; i < fb1s.length; i++) {
                        FieldBean fbt = fb1s[i];
                        String name = fbt.getName();
                        String[] value = ctx.getParameters(name);
                        if (value != null) {
                            for (int j = 0; j < value.length; j++) {
                                hiddentxt += "<input type='hidden' name='" + name + "' value='" + value[j] + "'>";
                            }
                        }
                        try {
                            ElementBean e = new ElementBean(fbt);
                            if (e.getComponetTp() == ComponentType.HIDDEN_TYPE) {
                                e.setComponetTp(ComponentType.TEXT_TYPE);
                            }

                            e.setDefaultValue("");
                            e.setIsnull(true);
                            e.setVisible(true);
                            e.setReadonly(false);

                            AbstractFormComponent afc = AbstractFormComponent.getInstance(e);
                            if (value != null) {
                                for (int j = 0; j < value.length; j++) {
                                    if (value[j] == null) {
                                        value[j] = "";
                                    }
                                }
                                afc.setValues(value);
                            }
                            afc.setCtx(ctx);
                            searchtxt += "<tr class='filter_table_tr'>";
                            String csrc = afc.toHTML();
                            if (csrc != null) {
                                csrc = csrc.replaceFirst("<option", "<option value=\"\"> <option");
                            }
                            if (!afc.useTd()) {
                                searchtxt += "<td class='filter_table_td'>" + csrc + "</td>";
                            } else {
                                String tmp = csrc;
                                if (tmp != null) {
                                    tmp = tmp.replaceAll("<td class=\"page_form_title_td\">",
                                            "<td class='filter_table_title_td'>");
                                    tmp = tmp.replaceAll("<td class=\"page_form_td\">",
                                            "<td class='filter_table_td'>");
                                }
                                searchtxt += tmp;
                            }
                            searchtxt += "</tr>";
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                body += "<table class='reference_tbl'>";
                //1.�γɱ�ͷ
                body += "<tr class='reference_tbl_head_tr'>";
                body += "<td class='reference_tbl_head_value_td'>";
                body += fbvalue.getCaption();
                body += "</td>";
                for (int i = 0; i < refnames.length; i++) {
                    FieldBean fbname = tb1.getField(refnames[i].replaceAll("\"", ""));
                    body += "<td class='reference_tbl_head_desc_td'>";
                    body += fbname.getCaption();
                    body += "</td>";
                }
                body += "</tr>";
                //2.�������
                try {
                    if (rs != null) {
                        while (rs.next()) {
                            body += "<tr class='reference_tbl_content_tr'>";
                            body += "<td class='reference_tbl_content_value_td'>";
                            String value = DBUtil.fromDB(rs.getString(refvalue));
                            String value_d = DBUtil.fromDB(rs.getString(refnames[0].replaceAll("\"", "")));

                            body +=
                                    "<a href=\"#\" onClick=\"return refselect(opener.winform." +
                                            oldreffldnm + ",'" + value + "',opener.winform." +
                                            oldreffldnm + "_D,'" + value_d + "')\">" + value + "</a>";
                            body += "</td>";
                            String head = "";
                            head +=
                                    "<a href=\"#\" onClick=\"return refselect(opener.winform." +
                                            oldreffldnm + ",'" + value + "',opener.winform." +
                                            oldreffldnm + "_D,'" + value_d + "')\">";
                            for (int i = 0; i < refnames.length; i++) {
                                body += "<td class='reference_tbl_content_desc_td'>";
                                //���������Ϣ
                                body += head;
                                body += DBUtil.fromDB(rs.getString(refnames[i].replaceAll("\"", "")));
                                body += "</a></td>";
                            }
                            body += "</tr>";
                        }
                    }
                }
                catch (Exception e) {

                }
                body += "</table>";

                //3.���칫������
                String commPara = "<input type='hidden' name='" +
                        SessionAttributes.REQUEST_EVENT_ID_NAME + "' value='" +
                        EventType.REFERENCE_FIELD_EVENT_TYPE + "'>";
                commPara += "<input type='hidden' name='" +
                        SessionAttributes.REQUEST_INSATNCE_ID_NAME + "' value='" + event.getId() + "'>";
                commPara += "<input type='hidden' name='" +
                        SessionAttributes.REQUEST_REFERENCE_FIELD_NAME + "' value='" + oldreffldnm +
                        "'>";
                String commText = commPara + hiddentxt;
                String searchPara = commPara + searchtxt;
                body += "<table class='blank_table'></table>";
                //�����11��17
                String filter = ctx.getParameter(SessionAttributes.REQUEST_REFERENCE_TEXT_NAME);
                body += "<form id='winform' method='post' action='" + ctx.getUrl(TARGET_TEMPLATE) +
                        "'>";
                body += commText;
                if (filter != null) {
                    body += "<input type='hidden' name='" +
                            SessionAttributes.REQUEST_REFERENCE_TEXT_NAME + "' value='" + filter + "'>";
                }
                body += "<input type='hidden' name='" + SessionAttributes.REQUEST_LIST_PAGENO_NAME +
                        "' value=''>";
                body += "<input type='hidden' name='" +
                        SessionAttributes.REQUEST_LIST_PAGECOUNT_NAME + "' value=''>";

                body += "</form>";
                sysButton = getReferenceButton(ctx, searchPara, iPageno, iPageCount, fb1s);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            //3.������ư�ť
            rtnMsg[0] = body;
            rtnMsg[1] = sysButton;
        }

        return rtnMsg;
    }

    private static String getReferenceButton(SessionContext ctx, String searchPara, int iPageno, int iPageCount, FieldBean[] fb1s) {
        String body = "";
        body += "<table class='reference_button_tbl'>";
        body += "<tr class='reference_button_tbl_tr'>";

        if (fb1s != null) {
            body += "<td class='reference_button_tbl_td'>";
            String onclick = "if ( filter.style.visibility == 'hidden' ) { filter.style.visibility ='';} else { filter.style.visibility = 'hidden';}";
            body +=
                    "<input class='reference_button_active' type='button' name='submit' value=' �� �� ' onclick=\"" +
                            onclick + "\">";
            body += "</td>";
        }
        //�����ҳ
        body += "<td class='reference_button_tbl_td'>";
        if (iPageno == 0) {
            body += "<input class='reference_button_disabled' type='submit' name='submit' value=' �� ҳ ' disabled='true'>";
        } else {
            body +=
                    "<input class=\"reference_button_active\" type=\"button\" name=\"submit\" value=\" �� ҳ \" onClick=\"buttonClick('0','" +
                            iPageCount + "');\">";
        }
        body += "</td>";

        //������һҳ��ť
        body += "<td class='reference_button_tbl_td'>";
        if (iPageno <= 0) {
            body += "<input class='reference_button_disabled' type='submit' name='submit' value=' ��һҳ ' disabled='true'>";
        } else {
            body += "<input type=\"button\" name=\"submit\" class=\"reference_button_active\" value=\" ��һҳ \" onClick=\"buttonClick('" +
                    (iPageno - 1) + "','" + iPageCount + "');\">";
        }
        body += "</td>";
        //������һҳ��ť
        body += "<td class='reference_button_tbl_td'>";
        if (iPageno >= iPageCount) {
            body += "<input class='reference_button_disabled' type='submit' name='submit' value=' ��һҳ ' disabled='true'>";
        } else {
            body += "<input type=\"button\" name=\"submit\" class=\"reference_button_active\" value=\" ��һҳ \" onClick=\"buttonClick('" +
                    (iPageno + 1) + "','" + iPageCount + "');\">";
        }
        body += "</td>";
        //����βҳ��ť
        body += "<td class='reference_button_tbl_td'>";
        if (iPageno >= iPageCount) {
            body += "<input class='reference_button_disabled' type='submit' name='submit' value=' β ҳ ' disabled='true'>";
        } else {
            body += "<input type=\"button\" name=\"submit\" class=\"reference_button_active\" value=\" β ҳ \" onClick=\"buttonClick('" +
                    iPageCount + "','" + iPageCount + "');\">";
        }
        body += "</td>";
        //���ɹرհ�ť
        body += "<td class='reference_button_tbl_td'>";
        body += "<input type=\"button\" name=\"submit\" class=\"reference_button_active\" value=\" �� �� \" onClick=\"window.close();\">";
        body += "</td>";
        body += "</tr>";
        body += "</table>";
        body += "<table class='blank_table'></table>";
        if (fb1s != null) {
            //�γ�������
            body += "<div id='filter' style='visibility:hidden'>";
            body += "<table class='filter_table'>";
            body += "<form id='winform7' method='post' action='" + ctx.getUrl(TARGET_TEMPLATE) + "'>";
            body += searchPara;
            body += "</form>";
            body += "</table>";
            body += "<table class='filter_button_table'>";
            body += "<tr class='filter_button_table_tr'>";
            body += "<td class='filter_button_table_td'>";
            body += "<input type='submit' class='filter_button_active' name='submit' value=' ȷ �� ' onClick='return winform7.submit();'>";
            body += "<input type='reset' class='filter_button_active' name='reset' value='������д' onClick='return winform7.reset();'>";
            body += "</td>";
            body += "</tr>";
            body += "</table>";
            body += "</div>";
        }
        return body;
    }
}
