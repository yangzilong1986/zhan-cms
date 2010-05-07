//Source file: e:\\java\\zt\\platform\\form\\util\\FormInstance.java

package zt.platform.form.util;

import zt.platform.db.DBUtil;
import zt.platform.db.RecordSet;
import zt.platform.form.config.*;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.datatype.ComponentType;
import zt.platform.form.util.datatype.DataType;
import zt.platform.form.util.datatype.OperatorType;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventType;
import zt.platform.utils.Debug;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

/**
 * FORM实例类
 *
 * @version 1.0
 *          <p/>
 *          JGO: adapt the class to accommodate postField and beforeField event of FormAction
 *          on Aug.3, 2005
 */
public class FormInstance implements Serializable {

    private static Logger logger = Logger.getLogger("zt.platform.form.util.FormInstance");

    private ArrayList pkflds = new ArrayList();
    private ArrayList skflds = new ArrayList();
    private Map fldvalue = new HashMap();
    private String instanceid;
    private String formid;
    private int state;
    private boolean readonly;
    private FormActions formAction;
    private Map values = new HashMap();
    //wxj:我于040325将FormBean实现可克隆，然后将每个FormInstance改造为可以使用单独的FormBean
    private boolean isCloneFormBean = false;
    private FormBean formBean = null;

    /**
     * 构造函数
     * <p/>
     * 1.实例化FormActions
     * 2.建立values，并放入所有元素对应的FormElementValue
     *
     * @param p_formid
     */
    public FormInstance(String p_formid, String p_instanceid) {
        this.formid = p_formid;
        this.instanceid = p_instanceid;
        FormBean fb = FormBeanManager.getForm(formid);
        //实例化FormActions
        setFormAction(fb.getProccls());
        //FormElementValue
        this.readonly = fb.isReadonly();
        String[] elementKeys = fb.getElementKeys();
        for (int i = 0; i < elementKeys.length; i++) {
            ElementBean eb = fb.getElement(elementKeys[i]);
            //实例化values
            FormElementValue fev = new FormElementValue(eb.getDataType());
            fev.setReadonly(eb.isReadonly());
            fev.setIsNull(eb.isIsnull());
            fev.setVisible(eb.isVisible());
            fev.setDisabled(eb.isDisabled());
            fev.setDecimalDigits(eb.getDecimalDigits());
            values.put(elementKeys[i], fev);
            //找出pk和sk字段
            if (eb.isIsPrimaryKey()) {
                pkflds.add(elementKeys[i]);
            }
            if (eb.isIsSearchKey()) {
                skflds.add(elementKeys[i]);
            }
        }
    }

    /**
     * 初始化values(清空FormElementValue的当前值，赋予默认值)
     * p_type：0-接收客户端数据前的清空，1-接收数据库数据前的清空
     */
    private void initValues(int p_type) {
        //取来Form中所有的元素名称
        FormBean fb = this.getFormBean();
        Object[] fldNames = fb.getElementKeys();
        for (int i = 0; i < fldNames.length; i++) {
            //单个元素名称
            String fldName = (String) fldNames[i];
            //元素数据类型
            ElementBean eb = (ElementBean) fb.getElement(fldName);
            //p_type==1时，如果是内存变量，不作清空处理。
            if (p_type == 1 && eb.getType() == eb.MEMORY_FIELD) {
                FormElementValue fev = (FormElementValue) values.get(fldName);
                if (fev != null) {
                    fev.setReadonly(eb.isReadonly());
                    fev.setIsNull(eb.isIsnull());
                    fev.setVisible(eb.isVisible());
                    fev.setDisabled(eb.isDisabled());
                    fev.setDecimalDigits(eb.getDecimalDigits());
                }
                continue;
            }
            int dataType = eb.getDataType();
            //空的FormElementValue
            FormElementValue fev = new FormElementValue(dataType);
            values.put(fldName, fev);
            fev.setReadonly(eb.isReadonly());
            fev.setIsNull(eb.isIsnull());
            fev.setVisible(eb.isVisible());
            fev.setDisabled(eb.isDisabled());
            fev.setDecimalDigits(eb.getDecimalDigits());
            //默认值
            ElementBean elementBean = fb.getElement(fldName);
            String defValue = elementBean.getDefaultValue();
            //数据形成List
            ArrayList tmpVal = (ArrayList) convertValue(defValue, dataType);
            if (tmpVal == null) {
                continue;
            }
            //List放到values中的FormElementValue里
            fev.setValue(tmpVal);
        }
    }

    public void initValues() {
        initValues(0);
    }

    /**
     * 根据dataType将value转化为list中不同类型的对象
     * 1. String 在List中是java.lang.String
     * 2. integer在List中是java.lang.Integer
     * 3. decimal在List中是java.lang.Double
     * 4. boolean在List中是java.lang.String (0-false 1-true)
     * 5. Date   在List中是java.lang.String (格式MM/DD/YYYY)
     * 6. Enumeration在List中是java.lang.Integer
     */
    private List convertValue(String[] p_values, int p_type) {
        if (p_values == null) {
            return null;
        }
        ArrayList al = new ArrayList();
        for (int i = 0; i < p_values.length; i++) {
            al.add(convertValueSingle(p_values[i], p_type));
        }
        return al;
    }

    private List convertValue(String p_value, int p_type) {
        if (p_value == null) {
            return null;
        }
        ArrayList al = new ArrayList();
        al.add(convertValueSingle(p_value, p_type));
        return al;
    }

    public static Object convertValueSingle(String p_value, int p_type) {
        if (p_value == null) {
            return null;
        }
        //整数类型处理，如果为空或异常，则处理成整数最小值
//        if (p_type == DataType.INTEGER_TYPE || p_type == DataType.ENUMERATION_TYPE) {
        if (p_type == DataType.INTEGER_TYPE) {
            try {
                if (p_value.trim().length() < 1) return null;
                return new Integer(p_value.trim());
            }
            catch (Exception ex) {
                if (!p_value.equals("自动生成")) {
                    logger.severe("整数或枚举类型转换错误，异常信息：" + ex.getMessage());
                }
                return new Integer(Integer.MAX_VALUE);
            }
        }
        //实数类型处理，如果为空或异常，则处理成实数最小值
        else if (p_type == DataType.DECIAML_TYPE) {
            try {
                if (p_value.trim().length() < 1) return null;
                return new Double(p_value.trim());
            }
            catch (Exception ex) {
                logger.severe("实数类型转换错误，异常信息：" + ex.getMessage());
                return new Double(Double.MAX_VALUE);
            }
        }
        //日期为空的情况
        //else if (p_type == DataType.DATE_TYPE && p_value.trim().equals("")) {
        //return null;
        //}
        //其他情况
        else {
            return p_value;
        }
    }

    /**
     * 根据Client传递的参数来更新实例的值
     */
    public void updateValue(SessionContext ctx) {
        initValues(0);
        //取来Form中所有的元素名称
        Object[] fldNames = values.keySet().toArray();
        for (int i = 0; i < fldNames.length; i++) {
            //元素名称
            String fldName = (String) fldNames[i];
            //元素数据类型
            int dataType = ((FormElementValue) values.get(fldName)).getType();
            //从ctx取来数据
            String[] fldValue = ctx.getParameters(fldName);
            //数据形成List
            ArrayList tmpVal = (ArrayList) convertValue(fldValue, dataType);
            if (tmpVal == null) {
                continue;
            }
            //List放到values中的FormElementValue里
            ((FormElementValue) values.get(fldName)).setValue(tmpVal);
        }
    }

    /**
     * 根据rs更新values
     *
     * @param rs
     */
    public void updateValue(RecordSet rs) {
        //initValues
        initValues(1);
        //取来Form中所有的元素名称
        Object[] fldNames = values.keySet().toArray();
        FormBean fb = this.getFormBean();
        for (int i = 0; i < fldNames.length; i++) {
            //元素名称
            String fldName = (String) fldNames[i];
            ElementBean elementBean = fb.getElement(fldName);
            //如果是内存变量，不作处理。
            if (elementBean.getType() == elementBean.MEMORY_FIELD) {
                continue;
            }
            //组件类型
            int compType = elementBean.getComponetTp();
            //元素数据类型
            int dataType = ((FormElementValue) values.get(fldName)).getType();
            //从rs取来数据，并做各种处理，rs在传进来之前就已经定位好了
            String fldValue;
            try {
                //所有数据类型，都读成String，方便处理
                fldValue = rs.getString(fldName);
                if (fldValue != null) fldValue = fldValue.trim();
            }
            catch (Exception ex) {
                fldValue = null;
            }
            //如果数据类型是double类型,去掉科学计数法
            if (dataType == DataType.DECIAML_TYPE) {
                if (fldValue != null) {
                    try {
                        double tmp = Double.parseDouble(fldValue);
                        fldValue = DBUtil.doubleToStrWithDigits(tmp, 10);
                    }
                    catch (Exception ex) {
                        fldValue = null;
                    }
                }
            }
            //如果数据类型是date类型，去掉"-"(20031127)
            if (dataType == DataType.DATE_TYPE) {
                if (fldValue != null) {
                    fldValue = fldValue.replaceAll("-", "");
                }
            }
            //如果组件类型是文本框或文本域，做html转换
            if (compType == ComponentType.TEXTAREA_TYPE || compType == ComponentType.TEXT_TYPE) {
                if (fldValue != null) {
                    fldValue = DBUtil.toHtml(fldValue);
                }
            }
            //如果数据类型是string类型，null值统统转化为""
            if (dataType == DataType.STRING_TYPE && fldValue == null) {
                fldValue = "";
            }
            //需要转码的转码在此转码
            if (elementBean.isNeedEncode()) {
                fldValue = DBUtil.fromDB(fldValue);
            }
            //数据形成List
            ArrayList tmpVal = (ArrayList) convertValue(fldValue, dataType);
            if (tmpVal == null) {
                continue;
            }
            //List放到values中的FormElementValue里
            ((FormElementValue) values.get(fldName)).setValue(tmpVal);
        }
    }

    /**
     * 清空值
     *
     * @param name
     */
    public void clearValue(String name) {
        //取来Form中所有的元素名称
        ((FormElementValue) values.get(name)).clear();
    }

    /**
     * 检查该FORM元素值的合法性
     * EventType:
     * View返回true
     * Delete － true
     * Insert、Edit - 全部 调用ElementBean的validate方法，失败则置错误信息
     * Find - true
     *
     * @param eventid
     * @param msgs
     * @return boolean
     * @roseuid 3F72628A0171
     */
    public boolean validate(int eventid, ErrorMessages msgs) {
        boolean flg = true;
        //只有Insert、Edit事件类型才检测
        if (eventid == EventType.EDIT_EVENT_TYPE ||
                eventid == EventType.INSERT_EVENT_TYPE) {
            FormBean fb = this.getFormBean();
            String[] eleNames = fb.getElementKeys();
            for (int i = 0; i < eleNames.length; i++) {
                ElementBean elementBean = fb.getElement(eleNames[i]);
                FormElementValue fev = (FormElementValue) this.values.get(eleNames[i]);
                if (elementBean.validate(fev.getValue())) {
                    flg = false;
                    msgs.add(eleNames[i] + "字段错误!");
                }
            }
        }
        return flg;
    }

    /**
     * 产生参照所用的sql
     */
    public String getRefSQL(SessionContext p_ctx, String p_fieldname) {
        if (p_ctx == null || p_fieldname == null) {
            return null;
        }
        String sql;
        String refTable;
        FieldBean[] skBeans;
        String[] skNames, skValues;
        /*1、组成select部分*/
        //定位本FormInstance所对应的TableBean
        FormBean frmbean = this.getFormBean();
        TableBean tblbean = TableBeanManager.getTable(frmbean.getTbl());
        //定位本p_fieldname所对应的FieldBean
        FieldBean fldbean = tblbean.getField(p_fieldname);
        //通过FieldBean获取refTable，Refnamefld，Refvaluefld并组成sql的select部分
        refTable = fldbean.getReftbl();
        if (refTable == null || refTable.length() < 1) {
            return null;
        }
        sql = "select " + fldbean.getRefnamefld() + "," + fldbean.getRefvaluefld() +
                " from " +
                refTable + " ";
        /*2、组成where部分*/
        //定位skBeans
        TableBean reftblbean = TableBeanManager.getTable(refTable);
        if (reftblbean == null) {
            return sql;
        }
        skBeans = reftblbean.getSearchKey();
        if (skBeans == null || skBeans.length < 1) {
            return null;
        }
        //从p_ctx中取来sk所对应的值，共同组成where条件
        sql += "where ";
        for (int i = 0; i < skBeans.length; i++) {
            String tmpVal = p_ctx.getParameter(skBeans[i].getName());
            if (tmpVal == null || tmpVal.length() < 1) {
                continue;
            }
            tmpVal = DBUtil.toDB(tmpVal.trim());
            sql += skBeans[i].getName();
            sql += " like ";
            sql +=
                    SqlAssistor.crtWhereValue(tmpVal, skBeans[i].getDatatype(),
                            OperatorType.LIKE_OPERATOR_TYPE) +
                            " and ";
        }
        if (sql.endsWith("where ")) {
            sql = sql.substring(0, sql.length() - 6);
        } else {
            sql = sql.substring(0, sql.length() - 5);
        }
        logger.info(sql);
        return sql;
    }

    /**
     * 产生参照总行数所用的sql
     */
    public String getRefCountSQL(SessionContext p_ctx, String p_fieldname) {
        if (p_ctx == null || p_fieldname == null) {
            return null;
        }
        String sql;
        String refTable;
        FieldBean[] skBeans;
        String[] skNames, skValues;
        /*1、组成select部分*/
        //定位本FormInstance所对应的TableBean
        FormBean frmbean = this.getFormBean();
        TableBean tblbean = TableBeanManager.getTable(frmbean.getTbl());
        //定位本p_fieldname所对应的FieldBean
        FieldBean fldbean = tblbean.getField(p_fieldname);
        //通过FieldBean获取refTable，Refnamefld，Refvaluefld并组成sql的select部分
        refTable = fldbean.getReftbl();
        if (refTable == null || refTable.length() < 1) {
            return null;
        }
        sql = "select count(*) from " + refTable + " ";
        /*2、组成where部分*/
        //定位skBeans
        TableBean reftblbean = TableBeanManager.getTable(refTable);
        if (reftblbean == null) {
            return sql;
        }
        skBeans = reftblbean.getSearchKey();
        if (skBeans == null || skBeans.length < 1) {
            return null;
        }
        //从p_ctx中取来sk所对应的值，共同组成where条件
        sql += "where ";
        for (int i = 0; i < skBeans.length; i++) {
            String tmpVal = p_ctx.getParameter(skBeans[i].getName());
            if (tmpVal == null || tmpVal.length() < 1) {
                continue;
            }
            tmpVal = DBUtil.toDB(tmpVal.trim());
            sql += skBeans[i].getName();
            sql += " like ";
            sql +=
                    SqlAssistor.crtWhereValue(tmpVal, skBeans[i].getDatatype(),
                            OperatorType.LIKE_OPERATOR_TYPE) +
                            " and ";
        }
        if (sql.endsWith("where ")) {
            sql = sql.substring(0, sql.length() - 6);
        } else {
            sql = sql.substring(0, sql.length() - 5);
        }
        logger.info(sql);
        return sql;
    }

    /**
     * 根据字段名称来取各种类型的数值
     *
     * @param name
     * @return
     */
    public FormElementValue getValue(String name) {
        FormElementValue fev = (FormElementValue) values.get(name);
        return fev;
    }

    public Object getObjectValue(String name) {
        return (Object) values.get(name);
    }

    public String getStringValue(String name) {
        FormElementValue fev = (FormElementValue) values.get(name);
        ArrayList al = (ArrayList) fev.getValue();
        if (al.size() > 0) {
            String temp = null;
            if (al.get(0) != null) temp = al.get(0).toString();
            if (temp == null) return temp;

            //INTEGER_TYPE,DECIAML_TYPE类型在最大值时转成""
            if (SqlAssistor.isMaxValue(temp, fev.getType())) {
                return "";
            }
            return temp;
        } else {
            return null;
        }
    }

    public int getIntValue(String name) {
        String tmp = getStringValue(name);
        if (tmp == null) {
            return 0;
        }
        return new Integer(tmp).intValue();
    }

    public double getDoubleValue(String name) {
        String tmp = getStringValue(name);
        if (tmp == null) {
            return 0;
        }
        return new Double(tmp).doubleValue();
    }

    public boolean getBooleanValue(String name) {
        String tmp = getStringValue(name);
        if (tmp == null) {
            return false;
        }
        if (tmp.equals("1")) {
            return true;
        }
        return false;
    }

    /**
     * 根据字段名称来赋各种类型的数值，赋值方式：先清空然后添加
     *
     * @param name
     * @param value
     */
    public void setValue(String name, String value) {
        FormElementValue fev = (FormElementValue) values.get(name);
        if (fev == null) return;
        fev.setValue(convertValue(value, fev.getType()));
    }

    public void setValue(String name, int value) {
        FormElementValue fev = (FormElementValue) values.get(name);
        if (fev == null) return;
        fev.setValue(convertValue(value + "", DataType.INTEGER_TYPE));
    }

    public void setValue(String name, double value) {
        FormElementValue fev = (FormElementValue) values.get(name);
        if (fev == null) return;
        fev.setValue(convertValue(value + "", DataType.DECIAML_TYPE));
    }

    public void setValue(Map p_values) {
        this.values = p_values;
    }

    public void setValue(String name, boolean value) {
        String tmp = "0";
        if (value) {
            tmp = "1";
        }
        setValue(name, tmp);
    }

    public void setValue(String name, Object value) {
        FormElementValue fev = (FormElementValue) values.get(name);
        if (fev == null) return;
        fev.setValue((List) value);
    }

    /**
     * 根据字段名称追加各种类型的数值，原来的数值保持不变
     *
     * @param name
     * @param value
     */

    public void addValue(String name, String value) {
        FormElementValue fev = (FormElementValue) values.get(name);
        fev.add(value);
    }

    public void addValue(String name, int value) {
        FormElementValue fev = (FormElementValue) values.get(name);
        fev.add(new Integer(value));
    }

    public void addValue(String name, double value) {
        FormElementValue fev = (FormElementValue) values.get(name);
        fev.add(new Double(value));
    }

    public void addValue(String name, boolean value) {
        FormElementValue fev = (FormElementValue) values.get(name);
        String tmp = "0";
        if (value) {
            tmp = "1";
        }
        fev.add(tmp);
    }

    public void addValue(String name, Object value) {
        FormElementValue fev = (FormElementValue) values.get(name);
        fev.add(value);
    }

    /**
     * @return String
     * @roseuid 3F7217070086
     */
    public String getInstanceid() {
        return this.instanceid;
    }

    /**
     * @param id
     * @roseuid 3F7217140017
     */
    public void setInstanceid(String id) {
        this.instanceid = id;
    }

    /**
     * @return String
     * @roseuid 3F721721032D
     */
    public String getFormid() {
        return this.formid;
    }

    /**
     * @param id
     * @roseuid 3F721727017D
     */
    public void setFormid(String id) {
        this.formid = id;
    }

    /**
     * @return zt.platform.form.control.FormActions
     * @roseuid 3F72172D0262
     */
    public FormActions getFormAction() {
        return this.formAction;
    }

    /**
     * @param action 为class全路径，需要实力化为对象，赋给formAction变量
     *               需要捕获ClassNotFind异常！
     * @roseuid 3F72173F0344
     */
    public void setFormAction(String action) {
        if (action == null || action.length() < 1) {
            this.formAction = null;
            return;
        }
        try {
            this.formAction = (FormActions) Class.forName(action).newInstance();
        }
        catch (Exception ex) {
            Debug.debug(ex);
            this.formAction = null;

        }
    }

    /**
     * @return int
     * @roseuid 3F72174C03B1
     */
    public int getState() {
        return this.state;
    }

    /**
     * @param state
     * @roseuid 3F721755015B
     */
    public void setState(int state) {
        this.state = state;
    }

    public boolean isFk(String p_fldName) {
        for (int i = 0; i < pkflds.size(); i++) {
            if (((String) pkflds.get(i)).equals(p_fldName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSk(String p_fldName) {
        for (int i = 0; i < skflds.size(); i++) {
            if (((String) skflds.get(i)).equals(p_fldName)) {
                return true;
            }
        }
        return false;
    }

    public boolean setFieldReadonly(String fldname, boolean readonly) {
        FormElementValue fev = (FormElementValue) values.get(fldname);
        if (fev != null) {
            fev.setReadonly(readonly);
            return true;
        } else {
            return false;
        }
    }

    public boolean setFieldIsNull(String fldname, boolean p_isnull) {
        FormElementValue fev = (FormElementValue) values.get(fldname);
        if (fev != null) {
            fev.setIsNull(p_isnull);
            return true;
        } else {
            return false;
        }
    }

    public boolean setFieldVisible(String fldname, boolean p_visible) {
        FormElementValue fev = (FormElementValue) values.get(fldname);
        if (fev != null) {
            fev.setVisible(p_visible);
            return true;
        } else {
            return false;
        }
    }

    public boolean setFieldDisabled(String fldname, boolean p_disabled) {
        FormElementValue fev = (FormElementValue) values.get(fldname);
        if (fev != null) {
            fev.setDisabled(p_disabled);
            return true;
        } else {
            return false;
        }
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public void usePublicFormBean() {
        this.formBean = FormBeanManager.getForm(formid);
        this.isCloneFormBean = false;
    }

    public void useCloneFormBean() {
        FormBean fb = FormBeanManager.getForm(formid);
        this.formBean = (FormBean) fb.clone();
        this.isCloneFormBean = true;
    }

    public FormBean getFormBean() {
        if (this.formBean == null) {
            this.usePublicFormBean();
        }
        return this.formBean;
    }

    public boolean isCloneFormBean() {
        return this.isCloneFormBean;
    }

    /**
     * following code is used to maintain the info to update HTML fields
     *
     * @param name  String
     * @param value String
     */
    public void setHTMLFieldValue(String name, String value) {
        if (name != null && value != null) {
            FormFieldValue ffv = new FormFieldValue(name, value, FormFieldValue.TYPE_VALUE);
            fldvalue.put(name, ffv);
        }
    }

    public void setHTMLFieldValue(String name, int value) {
        if (name != null) {
            FormFieldValue ffv = new FormFieldValue(name, "" + value, FormFieldValue.TYPE_VALUE);
            fldvalue.put(name, ffv);
        }
    }

    public void setHTMLFieldReadOnly(String name, boolean readonly) {
        if (name != null) {
            FormFieldValue ffv = new FormFieldValue(name, readonly == true ? "true" : "false", FormFieldValue.TYPE_READONLY);
            fldvalue.put(name, ffv);
        }
    }

    public void setHTMLFieldDisabled(String name, boolean disabled) {
        if (name != null) {
            FormFieldValue ffv = new FormFieldValue(name, disabled == true ? "true" : "false", FormFieldValue.TYPE_DISABLE);
            fldvalue.put(name, ffv);
        }
    }

    public void setHTMLFieldHidden(String name, boolean hidden) {
        if (name != null) {
            FormFieldValue ffv = new FormFieldValue(name, hidden == true ? "hidden" : "visible", FormFieldValue.TYPE_HIDDEN);
            fldvalue.put(name, ffv);
        }
    }

    public void setHTMLShowMessage(String title, String message) {
        if (message != null && title != null) {
            FormFieldValue ffv = new FormFieldValue(title, message, FormFieldValue.TYPE_MESSAGE);
            fldvalue.put(title, ffv);
        }
    }

    public void setHTMLFocus(String htmlctlname) {
        if (htmlctlname != null) {
            FormFieldValue ffv = new FormFieldValue(htmlctlname, htmlctlname, FormFieldValue.TYPE_FOCUS);
            fldvalue.put(htmlctlname + "-focus", ffv);
        }
    }

    public Iterator getHTMLFieldValue() {
        if (fldvalue == null) return null;
        else
            return fldvalue.values().iterator();
    }

    public void removeHTMLField() {
        if (this.fldvalue != null) this.fldvalue.clear();
    }
}
