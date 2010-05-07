package zt.platform.form.control;
/**
 * @author wdl
 * @update wxj
 */

import com.zt.util.PropertyManager;
import zt.platform.db.RecordSet;
import zt.platform.form.config.ElementBean;
import zt.platform.form.config.FormBean;
import zt.platform.form.util.*;
import zt.platform.form.util.datatype.ComponentType;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventType;
import zt.platform.utils.ErrorCode;

import java.util.List;

public class QueryGenerator {

    public static final String TARGET_TEMPLATE = "/templates/defaultform.jsp";

    /**
     * 1、获得FORM实体管理器
     * 2、获得FORM实体（event.getInstanceid()）
     * 3、获得值集ctx.getRequestAttribute(SessionAttributes.REQUEST_FIND_RESULT_NAME)
     * 4、取得请求action=form.getUrlocate()
     * 声明body=""
     * body += "<table class='form_list'>"
     * body += "<form method='post' action='$action$?instanceid=$instanceid$'>"
     * 5、输出LIST头，即每个字段的TITLE
     * 6、对每一条记录执行如下操作
     * <tr>
     * 对每一个字段执行如下操作
     * <td width= heiht= align= ……>
     * 如果定义了formatcls,则实例化并执行format方法（基类是FieldFormat）
     * 否则直接转换成串输出
     * </td>
     * </tr>
     * 7、body
     */
    public static String[] run(SessionContext ctx, Event event,
                               ErrorMessages msgs, int result) {
        //获取FormInstance
        FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
        FormInstance fi = fiManager.getFormInstance(event.getId());
        if (fi == null) {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return null;
        }
        //FormBean信息
        FormBean fb = fi.getFormBean();
        String[] fldnames = fb.getElementKeys();
        String body = "";
        String jsFiles = "";
        String errMsg = "";
        String queryTable = "";
        String listTable = "";
        String afterTable = "";
        String sysButton = "";
        boolean readonly = fi.isReadonly();
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
        String action = fb.getUrl();
        if (action == null) {
            action = PageGenerator.DEFAULT_URL_LOCATE;
        }
        //pagecount,pageno,pagesize等分页信息
        String pagecount = (String) ctx.getRequestAttribute(SessionAttributes.REQUEST_LIST_PAGECOUNT_NAME);
        String pageno = (String) ctx.getRequestAttribute(SessionAttributes.REQUEST_LIST_PAGENO_NAME);
        int pagesize = fb.getRows();
        int iPageno = 0;
        int iPageCount = 0;
        if (pagesize == 0) {
            pagesize = PropertyManager.getIntProperty(SessionAttributes.REQUEST_LIST_PAGESIZE_NAME);
        }
        try {
            iPageno = Integer.parseInt(pageno);
        }
        catch (Exception ex) {
            iPageno = 0;
        }
        try {
            iPageCount = Integer.parseInt(pagecount);
        }
        catch (Exception e) {
            iPageCount = 0;
        }

        //include js_files
        jsFiles = "<script src='" + ctx.getUrl("/js/querybutton.js") + "' type='text/javascript'></script>\n";
        jsFiles += "<script src='" + ctx.getUrl("/js/meizzDate.js") + "' type='text/javascript'></script>\n";
        //errMsg
        if (msgs.size() > 0) {
            errMsg = PageGenerator.getErrorString(msgs, result);
        }
        //queryTable
        queryTable = getQueryForm(ctx, fb, fi, tblwidth);
        //listTable
        try {
            listTable += "<table class='list_form_table' width='" + tblwidth + "' align='center' cellpadding='0' cellspacing='1' border='0'>\n";
            listTable += "<form id='querywinform' method='post' action='" + ctx.getUrl(action) + "' target='_self'>\n";
            int row = 0;
            if (fldnames != null) {
                //1.输出抬头
                listTable += "<tr class='list_form_title_tr'>\n";
                for (int i = 0; i < fldnames.length; i++) {
                    ElementBean eb = fb.getElement(fldnames[i]);
                    if (eb == null || !eb.isVisible() ||
                            eb.getComponetTp() == ComponentType.SYS_BUTTON ||
                            eb.getDisplayType() == FormBean.DISPLAY_QUERY) {
                        continue;
                    }
                    int tdwidth = eb.getWidth();
                    if (tdwidth > 0) {
                        listTable += "<td width='" + tdwidth + "' class='list_form_title_td' nowrap>";
                    } else {
                        listTable += "<td class='list_form_title_td' nowrap>";
                    }

                    String caption = eb.getCaption();
                    if (eb.getName() != null && eb.getName().toLowerCase().equals("checkbox")) {
                        caption = "<input type='checkbox' class='delete_checkbox' name='allcheck' id='all' onclick='selectAll();'>\n";
                    } else {
                        if (caption == null || caption.trim().length() == 0) {
                            caption = "&nbsp;";
                        }
                    }
                    listTable += caption.trim();

                    listTable += "</td>\n";
                }
                listTable += "</tr>\n";

                //2.输出数据
                RecordSet rs = (RecordSet) ctx.getRequestAttribute(SessionAttributes.REQUEST_FIND_RESULT_NAME);
                if (rs != null) {
                    while (rs.next()) {
                        row++;
                        listTable += "<tr class='list_form_tr' onmouseover=\"overIt(this)\" onmouseout=\"outIt(this)\">\n";
                        for (int i = 0; i < fldnames.length; i++) {
                            try {
                                ElementBean eb = fb.getElement(fldnames[i]);
                                if (eb == null || !eb.isVisible() ||
                                        eb.getComponetTp() == ComponentType.SYS_BUTTON ||
                                        eb.getDisplayType() == FormBean.DISPLAY_QUERY) {
                                    continue;
                                }
                                if (eb.getDataType() == ElementBean.DATATYPE_DECIMAL ||
                                        eb.getDataType() == ElementBean.DATATYPE_INTEGER) {
                                    listTable += "<td class='list_form_td' align='right' nowrap>";
                                } else {
                                    listTable += "<td class='list_form_td' nowrap>";
                                }
                                FieldFormat ff = eb.getFormatcls();
                                String content = ff.format(ctx, eb, fi, rs);
                                if (content == null || content.trim().length() <= 0) {
                                    content = "&nbsp;";
                                }
                                listTable += content;
                                listTable += "</td>\n";
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        listTable += "</tr>\n";
                    }
                }

                for (; /*row < 8 &&*/ row < pagesize; row++) {
                    listTable += "<tr class='list_form_tr'>\n";
                    for (int i = 0; i < fldnames.length; i++) {
                        ElementBean eb = fb.getElement(fldnames[i]);
                        if (eb == null || !eb.isVisible() ||
                                eb.getComponetTp() == ComponentType.SYS_BUTTON ||
                                eb.getDisplayType() == FormBean.DISPLAY_QUERY) {
                            continue;
                        }
                        listTable += "<td class='list_form_td'>&nbsp;</td>\n";
                    }
                    listTable += "</tr>\n";
                }
                //afterTable
                String tmp1 = ctx.getAfterBody();
                //tmp1="APPBRHID=2820000,APPBRHID=2820000,MNTBRHID=2820000";
                if (tmp1 != null && tmp1.length() > 0) {
                    tmp1 = tmp1.toUpperCase();
                    String tmp2[] = tmp1.split(";");
                    afterTable += "<tr class='list_form_tr'>\n";
                    boolean isbegin = true;//标记开始列
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
                            val = "合计";
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
                listTable += afterTable;
            }
            //hidden
            String commText = "<input type='hidden' name='" + SessionAttributes.REQUEST_INSATNCE_ID_NAME + "' value='" + event.getId() + "'>\n";
            commText += "<input type='hidden' name='" + SessionAttributes.REQUEST_EVENT_ID_NAME + "' value='" + EventType.FIND_EVENT_TYPE + "'>\n";
            String[] names = fb.getElementKeys();
            if (names != null) {
                for (int i = 0; i < names.length; i++) {
                    String name = names[i];
                    String[] values = ctx.getParameters(name);
                    String valueslarge = ctx.getParameter(name + "largelarge");
                    if (values != null) {
                        for (int j = 0; j < values.length; j++) {
                            if (values[j] != null) {
                                commText += "<input type='hidden' name='" + name + "' value='" + values[j] + "'>\n";
                            }
                        }
                    }
                    if (valueslarge != null) {
                        commText += "<input type='hidden' name='" + name + "largelarge' value='" + valueslarge + "'>\n";
                    }
                }
            }
            listTable += commText;
            listTable += "<input type='hidden' name='" + SessionAttributes.REQUEST_BUTTON_EVENT_NAME + "' value='" + SessionAttributes.REQUEST_BUTTON_DELETE_NAME + "'>";
            listTable += "</form>\n";
            listTable += "</table>\n";
            //afterTable
//      String tmp=ctx.getAfterBody();
//      tmp="重要资产合计：2820,000元";
//      if(tmp!=null && tmp.length()>0){
//        afterTable  ="<table cellpadding='0' cellspacing='0' border='0'><tr><td height='2'></td></tr></table>\n";
//        afterTable += "<table class='after_body_table' width='" + (tblwidth+8) + "' align='center' cellpadding='0' cellspacing='1' border='0'>\n";
//        afterTable += "<tr class='after_body_tr'><td class='after_body_td'>"+tmp;
//        afterTable += "</td></tr>\n</table>\n";
//      }

            //body
            body += jsFiles;
            body += errMsg;
            body += "<table>\n<tr>\n<td>\n";
            body += queryTable;
            body += "<table cellpadding='0' cellspacing='0' border='0'><tr><td height='5'></td></tr></table>\n";
            body += listTable;
            //body += afterTable;
            body += "</td>\n</tr>\n</table>\n";
            sysButton = getSysButton(ctx, fb, event, iPageno, iPageCount, commText, readonly);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //return
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

        body += "<form id='listform' method='post' action='" +
                ctx.getUrl(TARGET_TEMPLATE) + "'>\n";
        body += commText;
        body += "<input type='hidden' name='" +
                SessionAttributes.REQUEST_LIST_PAGENO_NAME + "' value=''>\n";
        body += "<input type='hidden' name='" +
                SessionAttributes.REQUEST_LIST_PAGECOUNT_NAME + "' value=''>\n";
        body += "</form>\n";

        body += "<table class='list_button_tbl'>\n";
        body += "<tr class='list_button_tbl_tr'>\n";

        List sysButton = fb.getSysButton();
        for (int i = 0; i < sysButton.size(); i++) {
            ElementBean ebt = (ElementBean) sysButton.get(i);

            body += "<td class='list_button_tbl_td'>";
/*
            body +=
                    "<input class='list_button_active' type='button' name='delete' value='" +
                            ebt.getCaption() + "' " +
                            " onClick='pressSelfButton(\"" + ebt.getName() + "\");'>";
*/
            //200911 zhanrui
            if (ebt.getOnclick().length()==0 ) {
                body += "<input class='list_button_active' type='button' name='delete' value='" +
                        ebt.getCaption() + "' " +
                        " onClick='pressSelfButton(\"" + ebt.getName() + "\");'>";
            } else {
                body += "<input class='list_button_active' type='button' name='delete' value='" +
                        ebt.getCaption() + "' " +
                        " onClick='" +  ebt.getOnclick() + "'>";
            }

            body += "</td>\n";
        }
        //生成增加按钮
        if (useAdd) {
            body += "<td class='list_button_tbl_td'>";
            if (readonly) {
                body +=
                        "<input class='list_button_disabled' type='button' name='delete' value=' 增加 ' disabled='true'>";
            } else {
                body += "<input class='list_button_active' type='button' name='delete' value=' 增加 ' " +
                        " onClick='pressAdd();'>";
            }
            body += "</td>\n";
        }
        if (useDelete) {
            body += "<td class='list_button_tbl_td'>";
            if (readonly || iPageCount < 0) {
                body +=
                        "<input class='list_button_disabled' type='button' name='delete' value=' 删除 ' disabled='true'>";
            } else {
                body += "<input class='list_button_active' type='button' name='delete' value=' 删除 ' onClick='pressDelete();'>";
            }
            body += "</td>\n";
        }

        //生成首页按钮
        body += "<td class='list_button_tbl_td'>";
        if (iPageno == 0) {
            body +=
                    "<input type='button' name='submit1' class='list_button_disabled' value=' 首页 ' disabled='true'>";
        } else {
            body += "<input type=\"button\" name=\"submit1\" class=\"list_button_active\" value=\" 首页 \" onClick=\"buttonClick('0','" +
                    iPageCount + "');\">";
        }

        body += "</td>\n";

        //生成上一页按钮
        body += "<td class='list_button_tbl_td'>";
        if (iPageno <= 0) {
            body +=
                    "<input type='button' name='submit2' class='list_button_disabled' value='上一页' disabled='true'>";
        } else {
            body += "<input type=\"button\" name=\"submit2\" class=\"list_button_active\" value=\"上一页\" onClick=\"buttonClick('" +
                    (iPageno - 1) + "','" + iPageCount + "');\">";
        }
        body += "</td>\n";
        //生成下一页按钮
        body += "<td class='list_button_tbl_td'>";
        if (iPageno >= iPageCount) {
            body +=
                    "<input type='submit' name='submit3' class='list_button_disabled' value='下一页' disabled='true'>";
        } else {
            body += "<input type=\"button\" name=\"submit3\" class=\"list_button_active\" value=\"下一页\" onClick=\"buttonClick('" +
                    (iPageno + 1) + "','" + iPageCount + "');\">";
        }

        body += "</td>\n";
        //生成尾页按钮
        body += "<td class='list_button_tbl_td'>";
        if (iPageno >= iPageCount) {
            body +=
                    "<input type='submit' name='submit4' class='list_button_disabled' value=' 尾页 ' disabled='true'>";
        } else {
            body += "<input type=\"button\" name=\"submit4\" class=\"list_button_active\" value=\" 尾页 \" onClick=\"buttonClick('" +
                    iPageCount + "','" + iPageCount + "');\">";
        }

        body += "</td>\n";

        body += "<td class='list_button_tbl_td'>";
        body += "<input type=\"button\" name=\"submit5\" class=\"list_button_active\" value=\" 刷新 \" onClick=\"buttonClick('" +
                iPageno + "','" + iPageCount + "');\">";
        body += "</td>\n";

        body += "</tr>";
        body += "</table>";

        return body;
    }

    private static String getQueryForm(SessionContext ctx, FormBean fb,
                                       FormInstance fi, int querytblwidth) {
        String queryStr = "";
        String tmpStr = "";
        String action = fb.getUrl();
        if (action == null) {
            action = PageGenerator.DEFAULT_URL_LOCATE;
        }
        try {
            tmpStr += "<table id=\"findDiv\" class=\"query_table\" cellpadding='0' cellspacing='0' border='0'  style='display:none'>";
            tmpStr += "<tr class=\"query_tr\">";
            tmpStr += "<td class=\"query_td\" width=\"80%\">";
            List queryFlds = fb.getQueryField();
            int rows = 0;

            if (queryFlds.size() > 0) {
                tmpStr +=
                        "<table class='query_form_table' id='query_form_table' cellpadding='1' cellspacing='1' border='0'>";
                int i = 0;
                for (; i < queryFlds.size(); i++) {
                    ElementBean ebt = (ElementBean) queryFlds.get(i);
                    tmpStr += "<tr class=\"query_form_tr\" nowrap>";
                    rows++;
                    try {
                        FormElementValue fev = fi.getValue(ebt.getName());
                        ebt = (ElementBean) ebt.clone();
                        if (ebt.getComponetTp() == ComponentType.HIDDEN_TYPE) {
                            if (ebt != null) {
                                ebt.setComponetTp(ComponentType.TEXT_TYPE);
                            } else {
                                ebt = fb.getElement(ebt.getName());
                            }
                        }
                        ebt.setDefaultValue("");
                        ebt.setIsnull(true);
                        ebt.setVisible(true);
                        ebt.setReadonly(false);
                        tmpStr += PageGenerator.getViewElemetScript(ctx, fi.getInstanceid(), ebt, fev, false, false);
                        if (ebt.getComponetTp() == ComponentType.TEXT_TYPE ||
                                ebt.getComponetTp() == ComponentType.DATE_TYPE ||
                                ebt.getComponetTp() == ComponentType.REFERENCE_TEXT_TYPE) {
                            //形成范围字段，在原来的字段基础上添加largelarge
                            ebt.setName(ebt.getName() + "largelarge");
                            ebt.setCaption(" 至 ");
                            tmpStr += PageGenerator.getViewElemetScript(ctx, fi.getInstanceid(), ebt, fev, false, false);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    tmpStr += "</tr>";
                }
                List queryHiddens = fb.getQueryHiddenFlds();
                for (i = 0; i < queryHiddens.size(); i++) {
                    ElementBean ebt = (ElementBean) queryHiddens.get(i);
                    FormElementValue fev = fi.getValue(ebt.getName());
                    String[] values = fev.getValueArray();
                    if (values != null && values.length > 0) {
                        tmpStr += "<input type=\"hidden\" name=\"" + ebt.getName() + "\" value=\"" + values[0] +
                                "\">";
                    } else {
                        tmpStr += "<input type=\"hidden\" name=\"" + ebt.getName() + "\" value=\"\">";
                    }
                }
                tmpStr += "<input type=\"hidden\" name=\"" + SessionAttributes.REQUEST_INSATNCE_ID_NAME +
                        "\" value=\"" + fi.getInstanceid() + "\">";
                tmpStr += "<input type=\"hidden\" name=\"" + SessionAttributes.REQUEST_EVENT_ID_NAME +
                        "\" value=\"" + EventType.FIND_EVENT_TYPE + "\">";
                tmpStr += "</table>";
                tmpStr += "</td>";
                tmpStr += "<td class=\"query_td\" width=\"20%\" align=\"center\">";
                tmpStr += "<table border='0' width='100%' bgcolor='#F1F1F1'>";
                tmpStr += "<tr>";
                tmpStr += "<td nowrap valign=\"top\">";
                tmpStr += "<input type=\"button\" class=\"query_button\" name=\"query\" value=\" 检 索 \" onclick=\"winform_on_click();\">";
                tmpStr += "</td>";
                tmpStr += "</tr>";
                tmpStr += "<tr>";
                tmpStr += "<td nowrap valign=\"top\">";
                tmpStr += "<input type=\"reset\" class=\"query_button\" name=\"reset\" value=\" 重 置 \">";
                tmpStr += "</td>";
                tmpStr += "</tr>";
                tmpStr += "</table>";
            } else {
                queryFlds = fb.getSearchKeys();
                if (queryFlds.size() > 0) {
                    tmpStr += "<table class=\"query_form_table\" id=\"query_form_table\">";
                    int i = 0;
                    for (; i < queryFlds.size(); i++) {
                        ElementBean ebt = (ElementBean) queryFlds.get(i);
                        tmpStr += "<tr class=\"query_form_tr\" nowrap>";
                        rows++;
                        try {
                            FormElementValue fev = fi.getValue(ebt.getName());
                            ebt = (ElementBean) ebt.clone();
                            ebt.setIsnull(true);
                            ebt.setVisible(true);
                            if (ebt.getComponetTp() == ComponentType.HIDDEN_TYPE) {
                                ebt.setComponetTp(ComponentType.TEXT_TYPE);
                            }
                            tmpStr +=
                                    PageGenerator.getElemetScript(ctx, fi.getInstanceid(), ebt,
                                            fev, false, false);
                            if (ebt.getComponetTp() == ComponentType.TEXT_TYPE ||
                                    ebt.getComponetTp() == ComponentType.DATE_TYPE ||
                                    ebt.getComponetTp() == ComponentType.REFERENCE_TEXT_TYPE) {
                                ebt.setName(ebt.getName() + "largelarge");
                                ebt.setCaption(" 至 ");
                                tmpStr += PageGenerator.getViewElemetScript(ctx, fi.getInstanceid(), ebt, fev, false, false);
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        tmpStr += "</tr>";
                    }
                    List queryHiddens = fb.getQueryHiddenFlds();
                    for (i = 0; i < queryHiddens.size(); i++) {
                        ElementBean ebt = (ElementBean) queryHiddens.get(i);
                        if (ebt.isIsSearchKey()) {
                            continue;
                        }
                        FormElementValue fev = fi.getValue(ebt.getName());
                        String[] values = fev.getValueArray();
                        if (values != null && values.length > 0) {
                            tmpStr += "<input type=\"hidden\" name=\"" + ebt.getName() +
                                    "\" value=\"" +
                                    values[0] +
                                    "\">";
                        } else {
                            tmpStr += "<input type=\"hidden\" name=\"" + ebt.getName() +
                                    "\" value=\"\">";
                        }
                    }
                    tmpStr += "<input type=\"hidden\" name=\"" +
                            SessionAttributes.REQUEST_INSATNCE_ID_NAME +
                            "\" value=\"" + fi.getInstanceid() + "\">";

                    tmpStr += "<input type=\"hidden\" name=\"" +
                            SessionAttributes.REQUEST_EVENT_ID_NAME +
                            "\" value=\"" + EventType.FIND_EVENT_TYPE + "\">";

                    tmpStr += "</table>";
                    tmpStr += "</td>";
                    tmpStr += "<td class=\"query_td\" valign=\"bottom\">";
                    tmpStr += "<table border='0' width='100%' bgcolor='#F1F1F1'>";
                    tmpStr += "<tr>";
                    tmpStr += "<td nowrap valign=\"top\">";
                    tmpStr += "<input type=\"button\" class=\"query_button\" name=\"query\" value=\" 检 索 \" onclick=\"winform_on_click();\">";
                    tmpStr += "</td>";
                    tmpStr += "</tr>";
                    tmpStr += "<tr>";
                    tmpStr += "<td nowrap valign=\"top\">";
                    tmpStr += "<input type=\"reset\" class=\"query_button\" name=\"reset\" value=\" 重 置 \">";
                    tmpStr += "</td>";
                    tmpStr += "</tr>";
                    tmpStr += "</table>";
                }
            }
            tmpStr += "</td>";
            tmpStr += "</tr>";
            tmpStr += "</table>";

            queryStr += "<table align='center' cellpadding='0' cellspacing='0' border='0' bgcolor='#AAAAAA' width='" + (querytblwidth + 8) + "'>\n";
            queryStr += "<form id=\"winform\" method=\"post\" action=\"" + ctx.getUrl(action) + "\">\n";
            queryStr += "<tr>\n<td height=\"0\">\n" + tmpStr + "\n</td>\n</tr>\n";
            queryStr += "<tr>\n<td height=\"0\" align=\"center\"><img id='findDivHandle' title='点击查询' onClick='menuMove()' src='" + ctx.getUrl("/images/form/button1.jpg") + "' style='cursor:hand;'></td>\n</tr>\n";
            queryStr += "</form>\n";
            queryStr += "</table>\n";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return queryStr;
    }
}
