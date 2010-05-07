//Source file: D:\\zt\\platform\\form\\control\\PageGenerator.java

package zt.platform.form.control;

import zt.platform.form.component.AbstractFormComponent;
import zt.platform.form.config.ElementBean;
import zt.platform.form.config.FormBean;
import zt.platform.form.util.*;
import zt.platform.form.util.datatype.ComponentType;
import zt.platform.form.util.event.ErrorMessage;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventType;
import zt.platform.utils.ErrorCode;

import java.util.Iterator;
import java.util.List;

/**
 * Page页面生成器
 *
 * @author 请替换
 * @version 1.0
 */
public class PageGenerator {

    public static final String DEFAULT_URL_LOCATE = "/templates/defaultform.jsp";
    public static final String LITTLE_URL_LOCATE = "/templates/littleform.jsp";

    /**
     * 1、获得FORM实体管理器
     * 2、获得FORM实体（event.getInstanceid()）
     * 3、判断是否只读readonly
     * 添加、修改、删除成功则置readonly=true
     * 否则根据FORM定义的属性来确定
     * 4、定义formAction=Form定义中取得getUrlocate()
     * 定义body="<table class='page_tbl'>"
     * body="<form id='winform' method='post' action='$formAction$'>"
     * 5、对每一个行和列循环
     * for ( int row = 0 ; row < rows ; row++ ) {
     * body+="<tr class='page_tr'>"
     * for ( int col = 0 ; col < cols ; col++ ) {
     * body += "<td class='page_td'>"
     * if ( 得到该坐标点上的字段){
     * body += getElementScript();
     * }
     * body += "</td>"
     * }
     * body+= "</tr>"
     * }
     * 6、body+="</form>"
     * 7、body+="</table>"
     *
     * @param ctx
     * @param event
     * @param msgs
     * @param result
     * @return String
     * @roseuid 3F739BAC0185
     */
    public static String[] run(SessionContext ctx, Event event,
                               ErrorMessages msgs, int result) {
        FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(
                SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
        boolean readonly = false;
        FormInstance fi = fiManager.getFormInstance(event.getId());
        if (fi == null) {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return null;
        }
        FormBean fb = fi.getFormBean();

        //1.检查FORM的只读属性
        if (event.getType() == EventType.LOAD_EVENT_TYPE) {
            readonly = true;
        } else {
            readonly = fi.isReadonly();
        }
        //2.获得FORM的Action属性
        String actionurl = fb.getUrl();
        if (actionurl == null) {
            actionurl = DEFAULT_URL_LOCATE;
        }
        String[] body = new String[2];
        actionurl = ctx.getUrl(actionurl);
        switch (event.getType()) {
            case EventType.INSERT_VIEW_EVENT_TYPE:
                body = getForm(ctx, fiManager, actionurl, readonly, event, msgs, result);
                break;
            case EventType.INSERT_EVENT_TYPE:
                if (event.getBefore_result() < 0 || event.getResult() < 0 || event.getAfter_result() < 0) {
                    body = getForm(ctx, fiManager, actionurl, readonly, event, msgs, result);
                }
                break;
            case EventType.EDIT_VIEW_EVENT_TYPE:
                body = getForm(ctx, fiManager, actionurl, readonly, event, msgs, result);
                break;
            case EventType.EDIT_EVENT_TYPE:
                if (event.getBefore_result() < 0 || event.getResult() < 0 || event.getAfter_result() < 0) {
                    body = getForm(ctx, fiManager, actionurl, readonly, event, msgs, result);
                }
                break;
            case EventType.DELETE_VIEW_EVENT_TYPE:
                body = getForm(ctx, fiManager, actionurl, readonly, event, msgs, result);
                break;
            case EventType.DELETE_EVENT_TYPE:
                if (event.getBefore_result() < 0 || event.getResult() < 0 || event.getAfter_result() < 0) {
                    body = getForm(ctx, fiManager, actionurl, readonly, event, msgs, result);
                }
                break;
            case EventType.FIND_VIEW_EVENT_TYPE:
                body = getFindView(ctx, fiManager, actionurl, event);
                break;
            case EventType.FIND_EVENT_TYPE:
                body = getForm(ctx, fiManager, actionurl, readonly, event, msgs, result);
                break;
            case EventType.BUTTON_EVENT_TYPE:
                body = getForm(ctx, fiManager, actionurl, readonly, event, msgs, result);
                break;
            case EventType.POST_FIElD_EVENT:
                body = getXMLForm(ctx, fiManager, actionurl, readonly, event, msgs, result);
                break;
            case EventType.BEFORE_FIElD_EVENT:
                body = getXMLForm(ctx, fiManager, actionurl, readonly, event, msgs, result);
                break;
            case EventType.LOAD_EVENT_TYPE:
                break;
            case EventType.UNLOAD_EVENT_TYPE:
                break;
            default:
                break;
        }
        return body;
    }

    /**
     * @param e
     * @param value
     * @param readonly
     * @return String
     * @roseuid 3F73FED100AF
     */
    public static String getElemetScript(SessionContext ctx, String instanceid,
                                         ElementBean e, FormElementValue value,
                                         boolean readonly, boolean hasChangeEvent) {
        String html = "";
        AbstractFormComponent afc = AbstractFormComponent.getInstance(e);
        if (readonly) {
            afc.setReadonly(true);
        }
        afc.setValues(value.getValueArray());
        afc.setCtx(ctx);
        afc.setInstanceId(instanceid);
        afc.setChangeEvent(hasChangeEvent);
        if (!afc.useTd()) {
            if (value.getValueFilter() != null) {
                html = "<td class='page_form_td'>" + afc.toHTML(value.getValueFilter()) + "</td>";
            } else {
                html = "<td class='page_form_td'>" + afc.toHTML() + "</td>";
            }
        } else {
            if (e.getComponetTp() == ComponentType.BUTTON_TYPE ||
                    e.getComponetTp() == ComponentType.HIDDEN_TYPE ||
                    e.getComponetTp() == ComponentType.JAVASCIPT_TYPE ||
                    e.getComponetTp() == ComponentType.LABEL_TYPE ||
                    e.getComponetTp() == ComponentType.RESET_TYPE ||
                    e.getComponetTp() == ComponentType.SUBMIT_TYPE) {
                html = "<td class='page_form_td'>" + afc.toHTML() + "</td>";
            } else {
                if (value.getValueFilter() != null) {
                    html = afc.toHTML(value.getValueFilter());
                } else {
                    html = afc.toHTML();
                }
            }
        }
        return html;
    }

    public static String getViewElemetScript(SessionContext ctx,
                                             String instanceid, ElementBean e,
                                             FormElementValue value,
                                             boolean readonly,
                                             boolean hasChangeEvent) {
        String html = "";
        AbstractFormComponent afc = AbstractFormComponent.getInstance(e);
        if (readonly) {
            afc.setReadonly(true);
        }
        String[] values = new String[1];
        values[0] = "";
        afc.setValues(values);
        afc.setCtx(ctx);
        afc.setInstanceId(instanceid);
        afc.setChangeEvent(hasChangeEvent);
        String csrc = afc.toHTML();
        if (e.getComponetTp() == ComponentType.ENUMERATION_TYPE ||
                e.getComponetTp() == ComponentType.LIST_TYPE) {
            if (csrc != null) {
                csrc = csrc.replaceFirst("<option", "<option value=\"\"> <option");
            }
        }
        if (!afc.useTd()) {
            html = "<td class='page_form_td'>" + csrc + "</td>";
        } else {
            if (e.getComponetTp() == ComponentType.BUTTON_TYPE ||
                    e.getComponetTp() == ComponentType.HIDDEN_TYPE ||
                    e.getComponetTp() == ComponentType.JAVASCIPT_TYPE ||
                    e.getComponetTp() == ComponentType.LABEL_TYPE ||
                    e.getComponetTp() == ComponentType.RESET_TYPE ||
                    e.getComponetTp() == ComponentType.SUBMIT_TYPE) {
                html = "<td class='page_form_td'>" + csrc + "</td>";
            } else {
                html = csrc;
            }
        }

        return html;
    }

    public static String getErrorString(ErrorMessages msgs, int result) {
        String body = "";
        body += "<table class='error_message_tbl'>\n";
        body += "<tr class='error_message_tbl_tr'>\n";
        body += "<td class='error_message_tbl_td'>";
        if (msgs.size() == 0 && result < 0) {
            msgs.add("" + result);
        }
        for (int i = 0; i < msgs.size(); i++) {
            ErrorMessage msg = msgs.get(i);
            body += "<li class='error_message_li'>";
            body += MessageUtil.getMessage(msg);
            body += "</li>";
        }
        body += "</td>\n";
        body += "</tr>\n";
        body += "</table>\n";

        return body;
    }

    //引入通用的JavaScript
    private static String[] getFindView(SessionContext ctx,
                                        FormInstanceManager fiManager,
                                        String actionurl, Event event) {
        String[] rtnMsg = new String[2];
        String body = "";
        FormInstance fi = fiManager.getFormInstance(event.getId());
        if (fi == null) {
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return null;
        }
        FormBean fb = fi.getFormBean();
        String[] names = fb.getElementKeys();
        body += "<table class='page_form_table' id='page_form_table'>";
        body += "<script src='" + ctx.getUrl("/js/plat_find.js") +
                "' type='text/javascript'></script>";
        body += "<script src='" + ctx.getUrl("/js/pagebutton.js") +
                "' type='text/javascript'></script>";
        body += "<script src='" + ctx.getUrl("/js/meizzDate.js") +
                "' type='text/javascript'></script>";
        body += "<script src='" + ctx.getUrl("/js/check.js") +
                "' type='text/javascript'></script>";

        body += "<form id='winform' method='post' action='" + actionurl + "'>";
        if (names != null) {
            try {
                for (int i = 0; i < names.length; i++) {
                    ElementBean eb = fb.getElement(names[i]);
                    if (eb != null && eb.isIsSearchKey()) {
                        body += "<tr class='page_form_tr'>";
                        try {
                            FormElementValue fev = fi.getValue(eb.getName());
                            eb = (ElementBean) eb.clone();
                            if (eb.getComponetTp() == ComponentType.HIDDEN_TYPE) {
                                if (eb != null) {
                                    eb.setComponetTp(ComponentType.TEXT_TYPE);
                                } else {
                                    eb = fb.getElement(names[i]);
                                }
                            }
                            eb.setDefaultValue("");
                            eb.setIsnull(true);
                            eb.setVisible(true);

                            eb.setReadonly(false);

                            body += getViewElemetScript(ctx, fi.getInstanceid(), eb, fev, false, false);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        body += "</tr>";
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        body += "<tr><td>&nbsp;";
        body += "<input type='hidden' name='" +
                SessionAttributes.REQUEST_INSATNCE_ID_NAME + "' value='" + event.getId() +
                "'>";
        body += "<input type='hidden' name='" +
                SessionAttributes.REQUEST_EVENT_ID_NAME + "' value='" +
                EventType.FIND_EVENT_TYPE + "'>";
        body += "&nbsp;</td></tr>";
        body += "</form>";
        body += "</table>";
        rtnMsg[0] = body;
        rtnMsg[1] = getFindViewButton();

        return rtnMsg;
    }

    private static String getFindViewButton() {
        String body = "";
        body += "<table class='page_button_tbl'>";
        body += "<tr class='page_button_tbl_tr'>";
        body += "<td class='page_button_tbl_td'>";
        body += "<input type='submit' class='page_button_active' name='confirm' value=' 查 找 ' onClick='if ( checkWinform() ) winform.submit();'>";
        body += "</td></tr></table>";
        return body;
    }

    private static String getSysButton(SessionContext ctx, FormBean fb,
                                       Event event, String actionurl, int result,
                                       boolean readonly, FormInstance fi) {
        String body = "";
        body += "<table class='page_button_tbl'>";
        body += "<tr class='page_button_tbl_tr'>";
        List sysButton = fb.getSysButton();
        for (int i = 0; i < sysButton.size(); i++) {
            ElementBean ebt = (ElementBean) sysButton.get(i);

            /**
             * Following code of IF block is changed by JGO on Feb 5, 2004
             * we want to define buttons that are capable of opening a new window to run the button event
             * the Description field of PTFormInfoDetl has two parts separated by '|', the first is
             * new open window name, the optinal second one is window size(same with html syntax)
             * if no description is found, system run button event in orignal window
             */
            String[] splitstr = null;
            if (ebt.getDescription() == null || ebt.getDescription().trim().length() <= 0) {
                ebt.setOnclick("return pressSelfSysButton('" + ebt.getName() + "',this);");
            } else {
                splitstr = ebt.getDescription().split("\\|");
                StringBuffer url = new StringBuffer(200);
                url.append("return pressSelfSysButtonOpenNewWin('");
                url.append(DEFAULT_URL_LOCATE).append("?").append(SessionAttributes.
                        REQUEST_INSATNCE_ID_NAME);
                url.append("=").append(fi.getInstanceid()).append("&").append(
                        SessionAttributes.REQUEST_EVENT_ID_NAME);
                url.append("=").append(EventType.BUTTON_EVENT_TYPE).append("&").append(
                        SessionAttributes.REQUEST_EVENT_VALUE_NAME);
                url.append("=").append(ComponentType.SUBMIT_TYPE).append("&").append(
                        SessionAttributes.REQUEST_BUTTON_EVENT_NAME);
                url.append("=").append(ebt.getName()).append("','");
                url.append(splitstr[0]).append("','");
                if (splitstr.length >= 2) {
                    url.append(splitstr[1]).append("',this);");
                } else {
                    url.append(
                            "height=600,width=700,toolbar=no,scrollbars=yes,resizable=yes',this);");

                }
                ebt.setOnclick(url.toString());
            }
            String DisabledOrReadonly = "";
            if (ebt.isDisabled() || ebt.isReadonly()) DisabledOrReadonly = " disabled ";
            body += "<td class='page_button_tbl_td'>";
            body += "<input class=\"list_button_active\" type=\"button\" name=\"" +
                    ebt.getName() + "\" value=\"" +
                    ebt.getCaption() + "\" onClick=\"" + ebt.getOnclick() + "\" " + DisabledOrReadonly + ">";
            body += "</td>";
        }

        if (fb.isUseAdd()) {
            body += "<td class='page_button_tbl_td'>";
            if (readonly || event.getType() == EventType.INSERT_VIEW_EVENT_TYPE ||
                    (event.getType() == EventType.INSERT_EVENT_TYPE && result < 0)) {
                body +=
                        "<input type='button' class='page_button_disabled'  id='addbtn' name='add' value=' 增 加 ' disabled='true'>";
            } else {
                body +=
                        "<input type='button' class='page_button_active' id='addbtn' name='add' value=' 增 加 ' onClick='return pressButton(winform." +
                                SessionAttributes.REQUEST_EVENT_ID_NAME + "," +
                                EventType.INSERT_VIEW_EVENT_TYPE + ",\"增加\");'>";
            }
            body += "</td>";
        }

        if (fb.isUseEdit()) {
            body += "<td class='page_button_tbl_td'>";
            if (readonly || event.getType() == EventType.EDIT_VIEW_EVENT_TYPE ||
                    event.getType() == EventType.FIND_EVENT_TYPE) {
                body +=
                        "<input type='button' class='page_button_disabled'  id='editbtn' name='edit' value=' 修 改 ' disabled='true'>";
            } else {
                body +=
                        "<input type='button' class='page_button_active'  id='editbtn' name='edit' value=' 修 改 ' onClick='return pressButton(winform." +
                                SessionAttributes.REQUEST_EVENT_ID_NAME + "," +
                                EventType.EDIT_VIEW_EVENT_TYPE +
                                ",\"修改\");'>";
            }
            body += "</td>";
        }

        if (fb.isUseDelete()) {
            body += "<td class='page_button_tbl_td'>";
            if (readonly) {
                body +=
                        "<input type='button' class='page_button_disabled' id='deletebtn' name='delete' value=' 删 除 ' disabled='true'>";
            } else {
                if (event.getType() == EventType.EDIT_VIEW_EVENT_TYPE ||
                        event.getType() == EventType.DELETE_VIEW_EVENT_TYPE ||
                        event.getType() == EventType.FIND_EVENT_TYPE ||
                        (event.getType() == EventType.EDIT_EVENT_TYPE && result < 0) ||
                        (event.getType() == EventType.DELETE_EVENT_TYPE && result < 0)) {
                    body += "<input type='button' class='page_button_active' id='deletebtn' name='delete' value=' 删 除 ' onClick='return pressDeleteButton(winform." +
                            SessionAttributes.REQUEST_EVENT_ID_NAME + "," +
                            EventType.DELETE_EVENT_TYPE +
                            ",\"删除\");'>";
                } else {
                    body +=
                            "<input type='button' class='page_button_disabled' id='deletebtn' name='delete' value=' 删 除 ' disabled='true'>";
                }
            }
            body += "</td>";
        }

        if (fb.isUseSearch()) {
            body += "<td class='page_button_tbl_td'>";
            String curAction = actionurl + "?" +
                    SessionAttributes.REQUEST_INSATNCE_ID_NAME + "=" +
                    event.getInstanceid() + "&" +
                    SessionAttributes.REQUEST_EVENT_ID_NAME + "=" +
                    EventType.FIND_VIEW_EVENT_TYPE;
            body += "<input type=\"button\" class=\"page_button_active\" name=\"searchbtn\" value=\" 查 找 \" onClick=\"return pressSearch(winform." +
                    SessionAttributes.REQUEST_EVENT_ID_NAME + "," +
                    EventType.FIND_VIEW_EVENT_TYPE + ",'" +
                    ctx.getUrl(LITTLE_URL_LOCATE) + "');\">";
            body += "</td>";
        }

        if (fb.isUseSave()) {
            body += "<td class='page_button_tbl_td'>";
            if (readonly) {
                body +=
                        "<input type='button' class='page_button_disabled' id='savebtn' name='save' value=' 提 交 ' disabled='true' >";
            } else {
                if (event.getType() == EventType.INSERT_VIEW_EVENT_TYPE) {
                    body +=
                            "<input type='button' class='page_button_active' id='savebtn' name='save' value=' 提 交 ' onClick='return pressSaveButton(winform." +
                                    SessionAttributes.REQUEST_EVENT_ID_NAME + "," +
                                    EventType.INSERT_EVENT_TYPE +
                                    ");'>";
                } else if (event.getType() == EventType.EDIT_VIEW_EVENT_TYPE ||
                        event.getType() == EventType.FIND_EVENT_TYPE) {
                    body +=
                            "<input type='button' class='page_button_active' id='savebtn' name='save' value=' 提 交 ' onClick='return pressSaveButton(winform." +
                                    SessionAttributes.REQUEST_EVENT_ID_NAME + "," +
                                    EventType.EDIT_EVENT_TYPE +
                                    ");'>";
                } else if (event.getType() == EventType.INSERT_EVENT_TYPE &&
                        (event.getBefore_result() < 0 || event.getResult() < 0 ||
                                event.getAfter_result() < 0)) {
                    body +=
                            "<input type='button' class='page_button_active' id='savebtn' name='save' value=' 提 交 ' onClick='return pressSaveButton(winform." +
                                    SessionAttributes.REQUEST_EVENT_ID_NAME + "," +
                                    EventType.INSERT_EVENT_TYPE +
                                    ");'>";
                } else if (event.getType() == EventType.EDIT_EVENT_TYPE &&
                        (event.getBefore_result() < 0 || event.getResult() < 0 ||
                                event.getAfter_result() < 0)) {
                    body +=
                            "<input type='button' class='page_button_active' id='savebtn' name='save' value=' 提 交 ' onClick='return pressSaveButton(winform." +
                                    SessionAttributes.REQUEST_EVENT_ID_NAME + "," +
                                    EventType.EDIT_EVENT_TYPE +
                                    ");'>";
                } else {
                    body +=
                            "<input type='button' class='page_button_disabled' id='savebtn' name='save' value=' 提 交 ' disabled='true' >";
                }
            }
            body += "</td>";
        }

        if (fb.isUseAdd() || fb.isUseEdit() || fb.isUseDelete() || fb.isUseSearch() ||
                fb.isUseSave() || fb.isUseReset()) {
            body += "<td class='page_button_tbl_td'>";
//      body +=
//        "<input type='button' class='page_button_active' name='button' value=' 关 闭 ' onClick=\"parent.close();\">";
            body +=
                    "<input type='button' class='page_button_active' name='button' value=' 关 闭 ' onClick=\"pageWinClose();\">";
            body += "</td>";
        }
        body += "</tr>";
        body += "</table>";
        return body;
    }

    private static String[] getForm(SessionContext ctx,
                                    FormInstanceManager fiManager,
                                    String actionurl, boolean readonly,
                                    Event event, ErrorMessages msgs, int result) {
        String[] rtnMsg = new String[2];
        String body = "";
        FormInstance fi = fiManager.getFormInstance(event.getId());
        if (fi == null) {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return null;
        }
        //FormBean fb = FormBeanManager.getForm(fi.getFormid());
        FormBean fb = fi.getFormBean();
        if (msgs.size() > 0) {
            body += getErrorString(msgs, result);
            if (result < 0) {
                if (event.getType() != EventType.INSERT_EVENT_TYPE &&
                        event.getType() != EventType.EDIT_EVENT_TYPE &&
                        !(event.getType() == EventType.FIND_EVENT_TYPE &&
                                fb.getType() == fb.PAGE_TYPE)) {
                    rtnMsg[0] = body;
                    return rtnMsg;
                }
            }
        }
        //script file
        body += "<script src='" + ctx.getUrl("/js/check.js") + "' type='text/javascript'></script>";
        body += "<script src='" + ctx.getUrl("/js/meizzDate.js") + "' type='text/javascript'></script>";
        String scriptFile = fb.getScriptFile();
        if (scriptFile == null || scriptFile.trim().length() == 0) {
            scriptFile = "/js/template.js";
            body += "<script src='" + ctx.getUrl(scriptFile) + "' type='text/javascript'></script>";//lj changed in 20090427
        } else {//lj changed in 20090427
            String scriptFiles[] = scriptFile.split("\\|");
            for (int i = 0; i < scriptFiles.length; i++) {
                body += "<script src='" + ctx.getUrl(scriptFiles[i]) + "' type='text/javascript'></script>";
            }
        }
        //form
        body += "<form id='winform' method='post' action='" + actionurl + "' >";
        body += "<table class='page_form_table' id='page_form_table'>";
        int rows = fb.getRows();
        int cols = fb.getCols();
        try {
            for (int i = 1; i <= rows; i++) {
                body += "<tr class='page_form_tr'>";
                String rowstr = "";
                boolean isbuttonrow = false;
                boolean isFirst = true;
                for (int j = 1; j <= cols;) {
                    ElementBean eb = fb.getElement(i, j);

                    if (eb == null) {
                        rowstr += "<td class='page_form_td'>&nbsp;</td>";
                        j++;
                        continue;
                    }
                    if (isFirst && (eb.getComponetTp() == ComponentType.BUTTON_TYPE || eb.getComponetTp() == ComponentType.SUBMIT_TYPE || eb.getComponetTp() == ComponentType.RESET_TYPE)) {
                        isbuttonrow = true;
                    } else if (isbuttonrow) {
                        if (eb.getComponetTp() != ComponentType.BUTTON_TYPE && eb.getComponetTp() != ComponentType.SUBMIT_TYPE && eb.getComponetTp() != ComponentType.RESET_TYPE) {
                            isbuttonrow = false;
                        }
                    }
                    /**
                     * Following code of IF block is changed by JGO on Feb 5, 2004
                     * we want to define buttons that are capable of opening a new window to run the button event
                     * the Description field of PTFormInfoDetl has two parts separated by '|', the first is
                     * new open window name, the optinal second one is window size(same with html syntax)
                     * if no description is found, system run button event in orignal window
                     */
                    if (eb.getComponetTp() == ComponentType.BUTTON_TYPE) {
                        String[] splitstr = null;
                        if (eb.getDescription() == null ||
                                eb.getDescription().trim().length() <= 0) {
                            /**
                             * following line is added by JGO, if OnClick in ptenuminfodetil has been configurated,
                             * this config will override the default OnClick action.
                             */
                            if (eb.getOnclick() == null || eb.getOnclick().trim().length() <= 0)
                                eb.setOnclick("return pressSelfButton('" + eb.getName() + "');");
                        } else {
                            splitstr = eb.getDescription().split("\\|");
                            StringBuffer url = new StringBuffer(200);
                            url.append("return pressSelfButtonOpenNewWin('");
                            url.append(DEFAULT_URL_LOCATE).append("?").append(SessionAttributes.REQUEST_INSATNCE_ID_NAME);
                            url.append("=").append(fi.getInstanceid()).append("&").append(SessionAttributes.REQUEST_EVENT_ID_NAME);
                            url.append("=").append(EventType.BUTTON_EVENT_TYPE).append("&").append(SessionAttributes.REQUEST_EVENT_VALUE_NAME);
                            url.append("=").append(ComponentType.SUBMIT_TYPE).append("&").append(SessionAttributes.REQUEST_BUTTON_EVENT_NAME);
                            url.append("=").append(eb.getName()).append("','");
                            url.append(splitstr[0]).append("','");
                            if (splitstr.length >= 2) {
                                url.append(splitstr[1]).append("');");
                            } else {
                                url.append("height=600,width=700,toolbar=no,scrollbars=yes,resizable=yes');");
                            }
                            eb.setOnclick(url.toString());
                        }
                    }
                    if (eb.getComponetTp() == ComponentType.SUBMIT_TYPE) {
                        eb.setOnclick("return pressSelfSubmit('" + eb.getName() + "');");
                    }
                    FormElementValue fev = fi.getValue(eb.getName());
                    try {
                        boolean isReadOnly = readonly;
                        if ((eb.getComponetTp() == ComponentType.BUTTON_TYPE ||
                                eb.getComponetTp() == ComponentType.SUBMIT_TYPE ||
                                eb.getComponetTp() == ComponentType.RESET_TYPE) && readonly) {
                            isReadOnly = false;
                        }
                        if (!isReadOnly) {
                            if ((event.getType() == EventType.DELETE_EVENT_TYPE ||
                                    event.getType() == EventType.DELETE_VIEW_EVENT_TYPE ||
                                    event.getType() == EventType.EDIT_EVENT_TYPE ||
                                    event.getType() == EventType.EDIT_VIEW_EVENT_TYPE ||
                                    event.getType() == EventType.FIND_EVENT_TYPE) &&
                                    eb.isIsPrimaryKey()) {
                                isReadOnly = true;
                            } else {
                                isReadOnly = fev.isReadonly();
                            }
                        }
                        rowstr += getElemetScript(ctx, fi.getInstanceid(), eb, fev, isReadOnly, eb.isChangeEvent());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (isFirst) {
                        isFirst = false;
                    }
                    //2003-12-24增加Textarea的跨多列
                    if (eb.getComponetTp() == ComponentType.TEXTAREA_TYPE) {
                        if (!fb.hasElement(i, j)) {
                            if (AbstractFormComponent.useTd()) {
                                rowstr = rowstr.replaceAll("</td><td",
                                        "</td><td colspan=\"" + (cols - 1) +
                                                "\" nowrap");
                            } else {
                                rowstr = rowstr.replaceAll("<td",
                                        "<td colspan=\"" + (cols - 1) +
                                                "\" nowrap");
                            }
                            j = cols;
                        }
                    }
                    //2003-12-24增加Textarea的跨多列 end
                    //2009-06-11增加CHECKBOX的跨多列  lj added eg: eb.getCols() 复用 ptinfodetail中的cols
                    if (eb.getComponetTp() == ComponentType.CHECKBOX_TYPE) {
                        if (!fb.hasElement(i, j)) {
                            if (AbstractFormComponent.useTd()) {
                                rowstr = rowstr.replaceAll("</td><td",
                                        "</td><td style='word-break:break-all' colspan=\"" + (eb.getCols()) +
                                                "\"");
                            } else {
                                rowstr = rowstr.replaceAll("<td",
                                        "<td style='word-break:break-all' colspan=\"" + (eb.getCols()) +
                                                "\"");
                            }
                            j = cols;
                        }
                    }
                    //2009-06-11增加CHECKBOX的跨多列 end
                    if (AbstractFormComponent.useTd()) {
                        if (eb.getComponetTp() == ComponentType.BUTTON_TYPE ||
                                eb.getComponetTp() == ComponentType.HIDDEN_TYPE ||
                                eb.getComponetTp() == ComponentType.JAVASCIPT_TYPE ||
                                eb.getComponetTp() == ComponentType.LABEL_TYPE ||
                                eb.getComponetTp() == ComponentType.RESET_TYPE ||
                                eb.getComponetTp() == ComponentType.SUBMIT_TYPE
                                ) {
                            j++;
                        } else {
                            j += 2;
                        }
                    } else {
                        j++;
                    }
                }
                if (isbuttonrow) {
                    rowstr = rowstr.replaceAll("<td class='page_form_td'>&nbsp;</td>", "");
                    rowstr = rowstr.replaceAll("<td class='page_form_td'>", "<td>");
                    rowstr = "<td class='page_form_td' colspan='" + cols + "'><table><tr>" + rowstr + "</tr></table></td>";
                }
                body += rowstr;
                body += "</tr>";
            }
            body += "</table>";
            body += "<script src='" + ctx.getUrl("/js/pagebutton.js") + "' type='text/javascript'></script>";
            body += "<input type='hidden' name='" + SessionAttributes.REQUEST_INSATNCE_ID_NAME + "' value='" + event.getId() + "'>";
            body += "<input type='hidden' name='" + SessionAttributes.REQUEST_EVENT_ID_NAME + "' value=''>";

            if (event.getType() == EventType.INSERT_VIEW_EVENT_TYPE || event.getType() == EventType.INSERT_EVENT_TYPE) {
                body += "<input type='hidden' name='" + SessionAttributes.REQUEST_EVENT_VALUE_NAME + "' value='" + EventType.INSERT_SMALL_QUERY_EVENT_TYPE + "'>";
            } else if (event.getType() == EventType.EDIT_VIEW_EVENT_TYPE || event.getType() == EventType.EDIT_EVENT_TYPE || event.getType() == EventType.FIND_EVENT_TYPE) {
                body += "<input type='hidden' name='" + SessionAttributes.REQUEST_EVENT_VALUE_NAME + "' value='" + EventType.EDIT_SMALL_QUERY_EVENT_TYPE + "'>";
            }
            body += "<input type='hidden' name='" + SessionAttributes.REQUEST_BUTTON_EVENT_NAME + "' value=''>";
            List hiddens = fb.getHidden();
            for (int i = 0; i < hiddens.size(); i++) {
                ElementBean ebt = (ElementBean) hiddens.get(i);
                FormElementValue fev = fi.getValue(ebt.getName());
                String[] values = fev.getValueArray();
                if (values != null && values.length > 0) {
                    body += "<input type='hidden' name='" + ebt.getName() + "' value='" + values[0] + "'>";
                } else {
                    body += "<input type='hidden' name='" + ebt.getName() + "' value=''>";
                }
            }
            body += "</form>";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        rtnMsg[0] = body;
        rtnMsg[1] = getSysButton(ctx, fb, event, actionurl, result, readonly, fi);

        return rtnMsg;
    }

    private static String[] getXMLForm(SessionContext ctx,
                                       FormInstanceManager fiManager,
                                       String actionurl, boolean readonly,
                                       Event event, ErrorMessages msgs, int result) {
        String[] rtnMsg = new String[1];
        String body = "";
        FormInstance fi = fiManager.getFormInstance(event.getId());
        if (fi == null) {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return null;
        }
        //FormBean fb = FormBeanManager.getForm(fi.getFormid());
        //FormBean fb = fi.getFormBean();
        body += "<data>";

        Iterator it = fi.getHTMLFieldValue();
        if (it != null) {
            while (it.hasNext()) {
                body += "<row>";

                FormFieldValue ffv = (FormFieldValue) it.next();
                switch (ffv.getFldType()) {
                    case FormFieldValue.TYPE_READONLY:
                        body += "<type>READONLY</type>";
                        break;
                    case FormFieldValue.TYPE_DISABLE:
                        body += "<type>DISABLE</type>";
                        break;
                    case FormFieldValue.TYPE_MESSAGE:
                        body += "<type>MESSAGE</type>";
                        break;
                    case FormFieldValue.TYPE_HIDDEN:
                        body += "<type>HIDDEN</type>";
                        break;
                    case FormFieldValue.TYPE_FOCUS:
                        body += "<type>FOCUS</type>";
                        break;
                    default:
                        body += "<type>FLDVALUE</type>";
                }
                body += "<name>" + ffv.getFldName() + "</name>";
                body += "<value>" + ffv.getFldValue() + "</value>";
                body += "</row>";
            }
        }
        body += "</data>";
        fi.removeHTMLField();

        rtnMsg[0] = body;
        return rtnMsg;
    }


}
