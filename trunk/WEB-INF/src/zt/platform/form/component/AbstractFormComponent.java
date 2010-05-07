package zt.platform.form.component;
/**
 *  FORM组件的抽象类 Notes: 1.请添加所有属性的setter和getter方法
 *
 *@author 请替换
 *@created 2003年10月10日
 *@version 1.0
 */

import com.zt.util.PropertyManager;
import zt.platform.db.ConnectionManager;
import zt.platform.db.DBUtil;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.config.ElementBean;
import zt.platform.form.config.EnumerationBean;
import zt.platform.form.config.EnumerationType;
import zt.platform.form.util.datatype.ComponentType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

public abstract class AbstractFormComponent {
    public static Logger logger = Logger.getLogger("zt.platform.form.component.AbstractComponent");
    public final static String CSS_PAGE_FORM_BUTTON = "page_form_button_active";
    public final static String CSS_PAGE_FORM_REFBUTTON = "page_form_refbutton";
    public final static String CSS_PAGE_FORM_SELECT_READONLY = "page_form_select_readonly"; //zhanrui
    public final static String CSS_PAGE_FORM_SELECT = "page_form_select";
    public final static String CSS_PAGE_FORM_RADIO = "page_form_radio";
    public final static String CSS_PAGE_FORM_CHECKBOX = "page_form_checkbox";
    public final static String CSS_PAGE_FORM_LABEL = "page_form_label";
    public final static String CSS_PAGE_FORM_TEXT_READONLY = "page_form_text_readonly";   //zhanrui
    public final static String CSS_PAGE_FORM_TEXT = "page_form_text";
    public final static String CSS_PAGE_FORM_TEXTFIELD = "page_form_textfield";
    public final static String CSS_PAGE_FORM_PASSWORD = "page_form_password";
    public final static String CSS_PAGE_FORM_SUBMIT = "page_form_button_active";
    public final static String CSS_PAGE_FORM_RESET = "page_form_button_active";
    public final static String CSS_PAGE_FORM_TABLE = "page_form_table";
    public final static String CSS_PAGE_FORM_TABLE_TR = "page_form_tr";
    public final static String CSS_PAGE_FORM_TABLE_TD = "page_form_td";
    public final static String CSS_PAGE_FORM_TABLE_TITLE_TD = "page_form_title_td";
    public final static String CSS_PAGE_FORM_BUTTON_TABLE = "page_form_button_table";
    public final static String CSS_PAGE_FORM_BUTTON_TR = "page_form_button_tr";
    public final static String CSS_LIST_FORM_TABLE = "list_form_table";
    public final static String CSS_LIST_FORM_TD = "list_form_td";
    public final static String CSS_LIST_FORM_TITLE_TD = "list_form_title_td";
    public final static String CSS_LIST_FORM_TR = "list_form_tr";

    protected String[] nameset;
    protected String[] valueset;
    private String[] value;
    protected boolean readonly = false;
    protected zt.platform.form.control.SessionContext ctx;
    private String instanceId;
    private boolean changeEvent;
    public ElementBean element = null;
    private static Map components;

    static {
        components = new HashMap();
        components.put(new Integer(ComponentType.BOOLEAN_TYPE), "zt.platform.form.component.DbBoolean");
        components.put(new Integer(ComponentType.ENUMERATION_TYPE), "zt.platform.form.component.DbEnumeration");
        components.put(new Integer(ComponentType.REFERENCE_TEXT_TYPE), "zt.platform.form.component.DbRefText");
        components.put(new Integer(ComponentType.TEXT_TYPE), "zt.platform.form.component.DbText");
        components.put(new Integer(ComponentType.BUTTON_TYPE), "zt.platform.form.component.FormButton");
        components.put(new Integer(ComponentType.CHECKBOX_TYPE), "zt.platform.form.component.FormCheckBox");
        components.put(new Integer(ComponentType.HIDDEN_TYPE), "zt.platform.form.component.FormHidden");
        components.put(new Integer(ComponentType.JAVASCIPT_TYPE), "zt.platform.form.component.FormJavaScript");
        components.put(new Integer(ComponentType.LABEL_TYPE), "zt.platform.form.component.FormLabel");
        components.put(new Integer(ComponentType.LIST_TYPE), "zt.platform.form.component.FormList");
        components.put(new Integer(ComponentType.PASSWORD_TYPE), "zt.platform.form.component.FormPassword");
        components.put(new Integer(ComponentType.RADIO_TYPE), "zt.platform.form.component.FormRadio");
        components.put(new Integer(ComponentType.RESET_TYPE), "zt.platform.form.component.FormReset");
        components.put(new Integer(ComponentType.SUBMIT_TYPE), "zt.platform.form.component.FormSubmit");
        components.put(new Integer(ComponentType.TEXTAREA_TYPE), "zt.platform.form.component.FormTextArea");
        components.put(new Integer(ComponentType.DATE_TYPE), "zt.platform.form.component.FormDate");
    }

    /**
     * Constructor for the AbstractFormComponent object
     *
     * @param element Description of the Parameter
     */
    public AbstractFormComponent(ElementBean element) {
        this.element = element;
        int type = element.getComponetTp();
        if (type == ComponentType.CHECKBOX_TYPE ||
                type == ComponentType.LIST_TYPE ||
                type == ComponentType.RADIO_TYPE) {
            this.initValues();
        }
    }

    /**
     * Description of the Method
     */
    private void initValues() {

        //nameset标示显示内容，valueset标示存储值
        logger.info("Init values because the list,radio,checkbox");
        int valueSetType = this.element.getValuesettype();
        String keyAndValueStr = element.getValueset();
        logger.info("keyAndValueStr is " + keyAndValueStr);
        if (valueSetType == 1) {
            String keyAndValues[] = keyAndValueStr.split("\\|");

            this.nameset = new String[keyAndValues.length];
            this.valueset = new String[keyAndValues.length];
            for (int i = 0; i < keyAndValues.length; i++) {
                logger.info("keyAndValues[" + i + "] is :" + keyAndValues[i]);
                String keyAndValue[] = keyAndValues[i].split(":");
                valueset[i] = keyAndValue[0];
                nameset[i] = keyAndValue[1];
            }
        } else if (valueSetType == 2) {
            String[] tableAndField = keyAndValueStr.split(":");
            String sql = "select " + tableAndField[1] + "," + tableAndField[2] + " from " +
                    tableAndField[0] + " ";
            logger.info("sql is : " + sql);
            ConnectionManager manager = ConnectionManager.getInstance();
            DatabaseConnection con = manager.getConnection();
            RecordSet rs = con.executeQuery(sql);
            this.nameset = new String[rs.getRecordCount()];
            this.valueset = new String[rs.getRecordCount()];
            logger.info("RecordSet count is : " + rs.getRecordCount());
            int i = 0;
            while (rs.next()) {
                Object o1;
                Object o2;
                o1 = rs.getObject(0);
                o2 = rs.getObject(1);
                if (o1 == null || o2 == null) {
                    logger.warning("Null name or value ");
                } else {
                    valueset[i] = o1.toString().trim();
                    nameset[i] = DBUtil.fromDB(o2.toString().trim());
                }
                i++;
            }
            con.commit();
            manager.releaseConnection(con);
        } else if (valueSetType == 3) {
            ConnectionManager manager = ConnectionManager.getInstance();
            DatabaseConnection con = manager.getConnection();
            RecordSet rs = con.executeQuery(keyAndValueStr);
            this.nameset = new String[rs.getRecordCount()];
            this.valueset = new String[rs.getRecordCount()];
            int i = 0;
            while (rs.next()) {
                Object o1;
                Object o2;
                o1 = rs.getObject(0);
                o2 = rs.getObject(1);
                if (o1 == null || o2 == null) {
                    logger.warning("Null name or value ");
                } else {
                    valueset[i] = o1.toString().trim();
                    nameset[i] = DBUtil.fromDB(o2.toString().trim());
                }

                i++;
            }
            con.commit();
            manager.releaseConnection(con);
        } else if (valueSetType == 4) {
//            String enuname = element.getEnutpname();
            String enuname = keyAndValueStr;
            if (enuname == null || enuname.equals("")) {
                logger.severe("Please set the " + element.getName() + "'s enum enuname");
            } else {
                EnumerationBean enu = EnumerationType.getEnu(enuname);
                if (enu == null) {
                    logger.severe("Please config the " + element.getName() +
                            "'s enum with enuname of " + enuname);
                } else {
                    Map enus = enu.getEnu();
                    this.nameset = new String[enus.size()];
                    this.valueset = new String[enus.size()];
                    int i = 0;
                    for (Iterator iter = enus.keySet().iterator(); iter.hasNext();) {
                        Object key = (Object) iter.next();
                        Object value = enu.getValue(key);
                        valueset[i] = key.toString();
                        nameset[i] = value.toString();
                        i++;
                    }

                }
            }
        } else {
            logger.severe("values set type is unknown " + valueSetType);
        }
    }

    /**
     * @return String
     * @roseuid 3F738709032D
     */
    public String toHTML() {
        return "error";
    }

    public String toHTML(Collection filter) {
        return "error";
    }

    /**
     * 初始化 根据e的相关属性初始化类属性
     *
     * @param e
     * @roseuid 3F7EA0DD0366
     */
    protected void init(ElementBean e) {
    }

    /**
     * 根据ElementBean的实例e构造一个AbstarctFormComponent实例，它的具体类型包括：
     * 枚举（enumration）、
     * 参考性文本框(reference text)、
     * 布尔类型(boolean)
     * 文本框（text）、
     * 文本区域(textarea)、
     * 隐藏域(hidden)、
     * 复选框(checkbox)、
     * 单选按钮(radio)、
     * 下拉式列表(list)、
     * 文本标签(label)、
     * 脚本(js)、
     * 按钮（button）、
     * 提交（submit）、
     * 重置（reset）
     * 组件类型在ComponentType中已经定义。
     */
    public static AbstractFormComponent getInstance(ElementBean e) {
        int type = e.getComponetTp();
        String tmp = (String) components.get(new Integer(type));
        if (tmp == null) tmp = (String) components.get(new Integer(ComponentType.TEXT_TYPE));
        Class c = null;
        try {
            c = Class.forName(tmp);
            try {
                Object o[] = {e};
                AbstractFormComponent com = (AbstractFormComponent) c.getConstructors()[0].newInstance(o);
                return com;
            }
            catch (Exception ex1) {
                ex1.printStackTrace();
            }
        }
        catch (ClassNotFoundException ex) {
            //ex.printStackTrace();
            logger.info("错误的组件类型：" + e.getName() + "=" + e.getComponetTp());
            return null;
        }
        return null;
    }

    /**
     * Gets the value attribute of the AbstractFormComponent object
     *
     * @return The value value
     */
    public String[] getValues() {
        if (this.value == null) {
            String v[] = {
                    ""};
            return v;
        }
        for (int i = 0; i < value.length; i++) {
            if (value[i] == null) {
                value[i] = "";

            }
        }
        return value;
    }

    /**
     * Sets the value attribute of the AbstractFormComponent object
     *
     * @param value The new value value
     */
    public void setValues(String[] value) {
        this.value = value;
    }

    /**
     * Gets the valueset attribute of the AbstractFormComponent object
     *
     * @return The valueset value
     */
    public String[] getValueset() {
        return valueset;
    }

    /**
     * Sets the valueset attribute of the AbstractFormComponent object
     *
     * @param valueset The new valueset value
     */
    public void setValueset(String[] valueset) {
        this.valueset = valueset;
    }

    /**
     * Gets the nameset attribute of the AbstractFormComponent object
     *
     * @return The nameset value
     */
    public String[] getNameset() {
        return nameset;
    }

    /**
     * Sets the nameset attribute of the AbstractFormComponent object
     *
     * @param nameset The new nameset value
     */
    public void setNameset(String[] nameset) {
        this.nameset = nameset;
    }

    /**
     * Gets the readonly attribute of the AbstractFormComponent object
     *
     * @return The readonly value
     */
    public boolean isReadonly() {
        return readonly;
    }

    /**
     * Sets the readonly attribute of the AbstractFormComponent object
     *
     * @param readonly The new readonly value
     */
    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public static boolean useTd() {
        if (PropertyManager.getProperty("zt.platform.component.TdSeparatorIsUsed") == null) {
            return true;
        } else {
            return PropertyManager.getProperty("zt.platform.component.TdSeparatorIsUsed").equals("true");
        }
    }

    /**
     * Gets the header attribute of the AbstractFormComponent object
     *
     * @return The header value
     */
    protected String getHeader() {
        String tmp = "";
        if (!element.isIsnull()) {
            tmp = "*";
        }
        if (useTd()) {
            String header = "<td class=\"" + CSS_PAGE_FORM_TABLE_TITLE_TD + "\" nowrap>" +
                    element.getCaption() + tmp +
                    "</td><td class=\"" + CSS_PAGE_FORM_TABLE_TD + "\" >" + element.getHeadstr();
            return header;
        } else {
            String header = "<td class=\"" + CSS_PAGE_FORM_TABLE_TD + "\" nowrap>" + element.getCaption() +
                    tmp + " " +
                    element.getHeadstr();
            return header;
        }
    }

    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    protected String GetFooter() {
        String footer = element.getAfterstr() + "</td>";
        return footer;
    }

    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public String otherStr() {
        String onClick = "";
        String onChange = "";
        String readOnly = "";
        if (element.getOnclick() != null && element.getOnclick().trim().toUpperCase().equals("BEFOREFIELD")) {
            onClick = " onFocus=\"BeforeFieldEvent(this);\"";
        } else if (element.getOnclick() != null && !element.getOnclick().equals("")) {
            onClick = " onclick=\"" + element.getOnclick() + "\"";
        }

        String changeEvent = "";
        if (isChangeEvent()) {
            changeEvent = "changeFunc()";
        }
        if (element.getOnchange() != null && element.getOnchange().trim().toUpperCase().equals("POSTFIELD")) {
            onChange = " onchange=\"PostFieldEvent(this);\"";
        } else if (element.getOnchange() != null && !element.getOnchange().equals("")) {
            onChange = " onchange=\"" + element.getOnchange() + ";" + changeEvent + "\"";
        } else if (!changeEvent.equals("")) {
            onChange = " onchange=\"" + changeEvent + "\"";
        }
        //if (isReadonly() || (!isReadonly() && element.isReadonly())) {
        if (isReadonly()) {
            readOnly = " readonly ";
        }

        String others = element.getOthers();
        String otherStr = "";
        if (others != null && !others.equals("")) {
            String other[] = others.split("\\|");
            if (other.length != 0) {
                for (int i = 0; i < other.length; i++) {
                    String[] nameAndValue = other[i].split(":");
                    if (nameAndValue.length != 2) {
                        logger.warning("Invalid name and value of " + other[i] + ",ignored! others str");
                    } else {
                        otherStr = otherStr + " " + nameAndValue[0] + "=" + "\"" + nameAndValue[1] + "\"";
                    }
                }
            }
        }
        return onClick + onChange + readOnly + otherStr;
    }

    /**
     * Gets the ctx attribute of the AbstractFormComponent object
     *
     * @return The ctx value
     */
    public zt.platform.form.control.SessionContext getCtx() {
        return ctx;
    }

    /**
     * Sets the ctx attribute of the AbstractFormComponent object
     *
     * @param ctx The new ctx value
     */
    public void setCtx(zt.platform.form.control.SessionContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Gets the instanceId attribute of the AbstractFormComponent object
     *
     * @return The instanceId value
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * Sets the instanceId attribute of the AbstractFormComponent object
     *
     * @param instanceId The new instanceId value
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * Gets the changeEvent attribute of the AbstractFormComponent object
     *
     * @return The changeEvent value
     */
    public boolean isChangeEvent() {
        return changeEvent;
    }

    /**
     * Sets the changeEvent attribute of the AbstractFormComponent object
     *
     * @param changeEvent The new changeEvent value
     */
    public void setChangeEvent(boolean changeEvent) {
        this.changeEvent = changeEvent;
    }
}
