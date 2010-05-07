//Source file: D:\\zt\\platform\\form\\control\\ListGenerator.java

package zt.platform.form.control;

import com.zt.util.PropertyManager;
import zt.platform.db.RecordSet;
import zt.platform.form.config.ElementBean;
import zt.platform.form.config.FormBean;
import zt.platform.form.util.FieldFormat;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.FormInstanceManager;
import zt.platform.form.util.SessionAttributes;
import zt.platform.form.util.datatype.ComponentType;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventType;
import zt.platform.utils.ErrorCode;

import java.util.List;

/**
 * @author ���滻
 * @version 1.0
 */
public class ListGenerator {

    public static final String TARGET_TEMPLATE = "/templates/defaultform.jsp";

    /**
     * 1�����FORMʵ�������
     * 2�����FORMʵ�壨event.getInstanceid()��
     * 3�����ֵ��ctx.getRequestAttribute(SessionAttributes.REQUEST_FIND_RESULT_NAME)
     * 4��ȡ������action=form.getUrlocate()
     * ����body=""
     * body += "<table class='form_list'>"
     * body += "<form method='post' action='$action$?instanceid=$instanceid$'>"
     * 5�����LISTͷ����ÿ���ֶε�TITLE
     * 6����ÿһ����¼ִ�����²���
     * <tr>
     * ��ÿһ���ֶ�ִ�����²���
     * <td width= heiht= align= ����>
     * ���������formatcls,��ʵ������ִ��format������������FieldFormat��
     * ����ֱ��ת���ɴ����
     * </td>
     * </tr>
     * 7��body += "</form>";
     * body += "</table>";
     *
     * @param ctx
     * @param event
     * @return String
     * @roseuid 3F73DED000E0
     */
    public static String[] run(SessionContext ctx, Event event,
                               ErrorMessages msgs, int result) {
        //��ȡFormInstance
        FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
        FormInstance fi = fiManager.getFormInstance(event.getId());
        if (fi == null) {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return null;
        }
        //FormBean��Ϣ
        FormBean fb = fi.getFormBean();
        String body = "";
        RecordSet rs = (RecordSet) ctx.getRequestAttribute(SessionAttributes.REQUEST_FIND_RESULT_NAME);
        String pageno = (String) ctx.getRequestAttribute(SessionAttributes.REQUEST_LIST_PAGENO_NAME);
        String pagecount = (String) ctx.getRequestAttribute(SessionAttributes.REQUEST_LIST_PAGECOUNT_NAME);
        boolean readonly = fi.isReadonly();
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
        int pagesize = fb.getRows();
        if (pagesize == 0) {
            pagesize = PropertyManager.getIntProperty(SessionAttributes.REQUEST_LIST_PAGESIZE_NAME);
        }
        String action = fb.getUrl();
        if (action == null) {
            action = PageGenerator.DEFAULT_URL_LOCATE;
        }
        if (msgs.size() > 0) {
            body += PageGenerator.getErrorString(msgs, result);
        }
        String[] fldnames = fb.getElementKeys();
        int tblwidth = 0;
        if (fldnames != null) {//edit by wxj at 040216 :each td width added
            for (int i = 0; i < fldnames.length; i++) {
                ElementBean eb = fb.getElement(fldnames[i]);
                if (!eb.isVisible()) {
                    continue;
                }
                tblwidth += eb.getWidth();
            }
        }
        if (tblwidth < fb.getWidth()) tblwidth = fb.getWidth();
        if (tblwidth != 0 && tblwidth < 300) {
            tblwidth = 300;
        }
        String sysButton = "";
        try {
            body += "<table class='list_form_table' width='" + tblwidth + "' cellpadding='1' cellspacing='1' border='0'>\n";
            body += "<form id='winform' method='post' action='" + ctx.getUrl(action) + "' target='_self'>\n";
            int row = 0;
            if (fldnames != null) {
                //1.���̧ͷ
                body += "<tr class='list_form_title_tr'>\n";
                for (int i = 0; i < fldnames.length; i++) {
                    ElementBean eb = fb.getElement(fldnames[i]);
                    if (eb == null || !eb.isVisible() || eb.getComponetTp() == ComponentType.SYS_BUTTON ||
                            eb.getDisplayType() != FormBean.DISPLAY_LIST) {
                        continue;
                    }
                    int tdwidth = eb.getWidth();
                    if (tdwidth > 0) {
                        body += "<td width='" + tdwidth + "' class='list_form_title_td' nowrap>";
                    } else {
                        body += "<td class='list_form_title_td' nowrap>";
                    }

                    String caption = eb.getCaption();
                    if (eb.getName() != null &&
                            eb.getName().toLowerCase().equals("checkbox")) {
                        caption = "<input type='checkbox' class='delete_checkbox' name='allcheck' id='all' onclick='selectAll();'>";
                    } else {
                        if (caption == null || caption.trim().length() == 0) {
                            caption = "&nbsp;";
                        }
                    }
                    body += caption.trim();
                    body += "</td>\n";
                }
                body += "</tr>\n";
                //2.�������
                if (rs != null) {
                    while (rs.next()) {
                        row++;
                        body += "<tr class='list_form_tr' onmouseover=\"overIt(this)\" onmouseout=\"outIt(this)\">\n";
                        for (int i = 0; i < fldnames.length; i++) {
                            try {
                                ElementBean eb = fb.getElement(fldnames[i]);
                                if (eb == null || !eb.isVisible() ||
                                        eb.getComponetTp() == ComponentType.SYS_BUTTON ||
                                        eb.getDisplayType() != FormBean.DISPLAY_LIST) {
                                    continue;
                                }
                                if (eb.getDataType() == ElementBean.DATATYPE_DECIMAL ||
                                        eb.getDataType() == ElementBean.DATATYPE_INTEGER) {
                                    body += "<td class='list_form_td' align='right' nowrap>";
                                } else {
                                    body += "<td class='list_form_td' nowrap>";
                                }
                                FieldFormat ff = eb.getFormatcls();
                                String content = ff.format(ctx, eb, fi, rs);
                                if (content == null || content.trim().length() <= 0) {
                                    content = "&nbsp;";
                                }
                                body += content;
                                body += "</td>\n";
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        body += "</tr>\n";
                    }
                }

                for (; /*row < 8 &&*/ row < pagesize; row++) {
                    body += "<tr class='list_form_tr'>\n";
                    for (int i = 0; i < fldnames.length; i++) {
                        ElementBean eb = fb.getElement(fldnames[i]);
                        if (eb == null || !eb.isVisible() ||
                                eb.getComponetTp() == ComponentType.SYS_BUTTON ||
                                eb.getDisplayType() != FormBean.DISPLAY_LIST) {
                            continue;
                        }
                        body += "<td class='list_form_td'>&nbsp;</td>\n";
                    }
                    body += "</tr>\n";
                }

                //afterTable
                String afterTable = "";
                String tmp1 = ctx.getAfterBody();
                //tmp1="APPBRHID=2820000,APPBRHID=2820000,MNTBRHID=2820000";
                if (tmp1 != null && tmp1.length() > 0) {
                    tmp1 = tmp1.toUpperCase();
                    String tmp2[] = tmp1.split(";");
                    afterTable += "<tr class='list_form_tr'>\n";
                    boolean isbegin = true; //��ǿ�ʼ��
                    for (int i = 0; i < fldnames.length; i++) {
                        ElementBean eb = fb.getElement(fldnames[i]);
                        if (eb == null || !eb.isVisible() ||
                                eb.getComponetTp() == ComponentType.SYS_BUTTON ||
                                eb.getDisplayType() == FormBean.DISPLAY_QUERY) {
                            continue;
                        }
                        String style = " align='right'";
                        String val = "&nbsp;";
                        if (isbegin) {
                            isbegin = false;
                            val = "�ϼ�";
                            style = " align='center' nowrap";
                        } else {
                            for (int j = 0; j < tmp2.length; j++) {
                                if (fldnames[i].trim().equals(tmp2[j].substring(0, tmp2[j].indexOf("=")))) {
                                    val = tmp2[j].substring(tmp2[j].indexOf("=") + 1, tmp2[j].length());
                                    break;
                                }
                            }
                        }
                        afterTable += "<td class='list_form_td'" + style + ">";
                        afterTable += val;
                        afterTable += "</td>\n";
                    }
                    afterTable += "</tr>\n";
                }
                body += afterTable;
            }
            //�γɲ�ѯ����
            String commText = "<input type='hidden' name='" + SessionAttributes.REQUEST_INSATNCE_ID_NAME + "' value='" + event.getId() + "'>\n";
            commText += "<input type='hidden' name='" + SessionAttributes.REQUEST_EVENT_ID_NAME + "' value='" + EventType.FIND_EVENT_TYPE + "'>\n";
            String[] names = fb.getElementKeys();
            if (names != null) {
                for (int i = 0; i < names.length; i++) {
                    String name = names[i];
                    String[] values = ctx.getParameters(name);
                    if (values != null) {
                        for (int j = 0; j < values.length; j++) {
                            if (values[j] != null) {
                                commText += "<input type='hidden' name='" + name + "' value='" + values[j] + "'>\n";
                            }
                        }
                    }
                }
            }
            body += "<script src='" + ctx.getUrl("/js/listbutton.js") + "' type='text/javascript'></script>\n";
            body += commText;
            body += "<input type='hidden' name='" + SessionAttributes.REQUEST_BUTTON_EVENT_NAME +
                    "' value='" + SessionAttributes.REQUEST_BUTTON_DELETE_NAME + "'>\n";
            body += "</form>\n";
            body += "</table>\n";
            //afterTable
//      String afterTable="";
//      String tmp=ctx.getAfterBody();
//      tmp="��Ҫ�ʲ��ϼƣ�2820,000Ԫ";
//      if(tmp!=null && tmp.length()>0){
//        afterTable  ="<table cellpadding='0' cellspacing='0' border='0'><tr><td height='2'></td></tr></table>\n";
//        afterTable += "<table class='after_body_table' width='" + (tblwidth+8) + "' align='center' cellpadding='0' cellspacing='1' border='0'>\n";
//        afterTable += "<tr class='after_body_tr'><td class='after_body_td'>"+tmp;
//        afterTable += "</td></tr>\n</table>\n";
//      }
            //body +=afterTable;
            sysButton = getSysButton(ctx, fb, event, iPageno, iPageCount, commText, readonly);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String[] rtnMsg = new String[2];
        rtnMsg[0] = body;
        rtnMsg[1] = sysButton;

        return rtnMsg;
    }

    private static String getSysButton(SessionContext ctx, FormBean fb,
                                       Event event, int iPageno, int iPageCount,
                                       String commText, boolean readonly) {
        String body = "";

        boolean useDelete = fb.isUseDelete();
        boolean useAdd = fb.isUseAdd();
//�����޼�¼ʱ����ʾ�˵�
//        if ( iPageCount == 0 && !useDelete ) {
//            return body;
//        }
        //2003-11-5
        body += "<form id='listform' method='post' action='" +
                ctx.getUrl(TARGET_TEMPLATE) + "'>";
        body += commText;
        body += "<input type='hidden' name='" +
                SessionAttributes.REQUEST_LIST_PAGENO_NAME + "' value=''>";
        body += "<input type='hidden' name='" +
                SessionAttributes.REQUEST_LIST_PAGECOUNT_NAME + "' value=''>";
        body += "</form>";
        //2003-11-5
//        body += "<table class='blank_table'></table>";
        body += "<table class='list_button_tbl'>";
        body += "<tr class='list_button_tbl_tr'>";

        List sysButton = fb.getSysButton();
        for (int i = 0; i < sysButton.size(); i++) {
            ElementBean ebt = (ElementBean) sysButton.get(i);

            body += "<td class='list_button_tbl_td'>";

            //200911 zhanrui
            if (ebt.getOnclick() == null) {
                body += "<input class='list_button_active' type='button' name='delete' value='" +
                        ebt.getCaption() + "' " +
                        " onClick='pressSelfButton(\"" + ebt.getName() + "\");'>";
            } else {
                body += "<input class='list_button_active' type='button' name='delete' value='" +
                        ebt.getCaption() + "' " +
                        " onClick='" +  ebt.getOnclick() + "'>";
            }
            body += "</td>";
        }
        //�������Ӱ�ť
        if (useAdd) {
            body += "<td class='list_button_tbl_td'>";
            if (readonly) {
                body +=
                        "<input class='list_button_disabled' type='button' name='delete' value=' ���� ' disabled='true'>";
            } else {
                body +=
                        "<input class='list_button_active' type='button' name='delete' value=' ���� ' " +
                                " onClick='pressAdd();'>";
            }
            body += "</td>";
        }
        if (useDelete) {
            body += "<td class='list_button_tbl_td'>";
            if (readonly || iPageCount < 0) {
                body +=
                        "<input class='list_button_disabled' type='button' name='delete' value=' ɾ�� ' disabled='true'>";
            } else {
                body +=
                        "<input class='list_button_active' type='button' name='delete' value=' ɾ�� ' onClick='pressDelete();'>";
            }
            body += "</td>";
        }

        //������ҳ��ť
        body += "<td class='list_button_tbl_td'>";
        if (iPageno == 0) {
            body +=
                    "<input type='button' name='submit1' class='list_button_disabled' value=' ��ҳ ' disabled='true'>";
        } else {
            body +=
                    "<input type=\"button\" name=\"submit1\" class=\"list_button_active\" value=\" ��ҳ \" onClick=\"buttonClick('0','" +
                            iPageCount + "');\">";
        }

        body += "</td>";

        //������һҳ��ť
        body += "<td class='list_button_tbl_td'>";
        if (iPageno <= 0) {
            body +=
                    "<input type='button' name='submit2' class='list_button_disabled' value='��һҳ' disabled='true'>";
        } else {
            body +=
                    "<input type=\"button\" name=\"submit2\" class=\"list_button_active\" value=\"��һҳ\" onClick=\"buttonClick('" +
                            (iPageno - 1) + "','" + iPageCount + "');\">";
        }
        body += "</td>";
        //������һҳ��ť
        body += "<td class='list_button_tbl_td'>";
        if (iPageno >= iPageCount) {
            body +=
                    "<input type='submit' name='submit3' class='list_button_disabled' value='��һҳ' disabled='true'>";
        } else {
            body +=
                    "<input type=\"button\" name=\"submit3\" class=\"list_button_active\" value=\"��һҳ\" onClick=\"buttonClick('" +
                            (iPageno + 1) + "','" + iPageCount + "');\">";
        }

        body += "</td>";
        //����βҳ��ť
        body += "<td class='list_button_tbl_td'>";
        if (iPageno >= iPageCount) {
            body +=
                    "<input type='submit' name='submit4' class='list_button_disabled' value=' βҳ ' disabled='true'>";
        } else {
            body +=
                    "<input type=\"button\" name=\"submit4\" class=\"list_button_active\" value=\" βҳ \" onClick=\"buttonClick('" +
                            iPageCount + "','" + iPageCount + "');\">";
        }

        body += "</td>";

        body += "<td class='list_button_tbl_td'>";
        body +=
                "<input type=\"button\" name=\"submit5\" class=\"list_button_active\" value=\" ˢ�� \" onClick=\"buttonClick('" +
                        iPageno + "','" + iPageCount + "');\">";
        body += "</td>";

        body += "<td class='list_button_tbl_td'>";
        body +=
                "<input type=\"button\" name=\"submit7\" class=\"list_button_active\" value=\" �ر� \" onClick=\"window.close();\">";
        body += "</td>";
        body += "</tr>";
        body += "</table>";
        return body;
    }
}
