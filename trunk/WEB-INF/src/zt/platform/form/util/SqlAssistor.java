//Source file: e:\\java\\zt\\platform\\form\\util\\SqlAssistor.java

package zt.platform.form.util;

import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.db.DBUtil;
import zt.platform.form.config.ElementBean;
import zt.platform.form.config.FormBean;
import zt.platform.form.util.datatype.ComponentType;
import zt.platform.form.util.datatype.DataType;
import zt.platform.form.util.datatype.OperatorType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sql���������
 *
 * @author ��ѧ��
 * @version 1.0
 */
public class SqlAssistor {
    private Logger logger = Logger.getLogger("zt.platform.form.util.SqlAssistor");
    private List sqlFields = new ArrayList();
    private List sqlWheres = new ArrayList();
    private String defaultTbl;
    private List tbls = new ArrayList();
    private FormInstance formInstance;
    public static final int INSERT_SQL_TYPE = 0;
    public static final int UPDATE_SQL_TYPE = 1;
    public static final int DELETE_SQL_TYPE = 2;
    public static final int SELECT_SQL_TYPE = 3;

    public static final int SEARCH_KEY_TYPE = 0;
    public static final int PRIMARY_KEY_TYPE = 1;

    /**
     * ����SQL��������ȡFORM�е�DB���͵��ֶΣ����뵽sqlField�У����������뵽tbls�У�
     * ��defaultTbl��Ϊ��FormInstance�ı�����type��ֵ��
     * type=0ʱ����SearchKey���ֶμ��뵽sqlWhere�У�
     * type=1ʱ����primaryKey���ֶμ��뵽sqlWhere��
     *
     * @param formInstance
     * @param type
     * @roseuid 3F729B920175
     */
    public SqlAssistor(FormInstance formInstance, int type) {
        //��������ϵ���Ч�ԣ���������ֶ���ֱ��ȡ
        //FormInstance�ж�Ӧ��value������ȡ�ֶε�defaultvalue����nullֵ
        //һ��FormBeanֻ��Ӧһ���������еĶ��壩
        this.formInstance = formInstance;
        FormBean formBean = formInstance.getFormBean();
        String[] elementKeys = formBean.getElementKeys();
        //
        this.defaultTbl = formBean.getTbl().trim();
        //
        tbls.add(this.defaultTbl);
        for (int i = 0; i < elementKeys.length; i++) {
            ElementBean elementBean = formBean.getElement(elementKeys[i]);
            //������ڴ��������������
            if (elementBean.getType() == elementBean.MEMORY_FIELD) {
                continue;
            }
            //ȡFORM�е�DB���͵��ֶΣ����뵽sqlField��
            SqlField sf = new SqlField();
            sf.setTblname(this.defaultTbl);
            sf.setFldname(elementKeys[i]);
            sf.setType(elementBean.getDataType());
            //����value
            FormElementValue fev = (FormElementValue) formInstance.getValue(elementKeys[i]);
            Object value = null;
            if (fev.getValue().size() > 0) {
                //lj added in 20090611
                if (fev.getValue().size() == 1) {
                    value = fev.getValue().get(0);    //lj changed in 20090611
                    //*********************lj added in 20090611 begin
                } else {
                    String values = "";
                    for (int vsi = 0; vsi < fev.getValue().size(); vsi++) {
                        values += EnumValue.SPLIT_STR + fev.getValue().get(vsi);
                    }
                    value = values.replaceFirst(EnumValue.SPLIT_STR, "");
                }
                //*********************lj added in 20090611 end
            }
            //��TEXTAREA_TYPE�����������toSql��toHtmlת��
            if ((elementBean.getComponetTp() == ComponentType.TEXTAREA_TYPE || elementBean.getComponetTp() == ComponentType.TEXT_TYPE) && value != null) {
                value = zt.platform.db.DBUtil.toSql(value.toString());
                //value = zt.platform.db.DBUtil.toHtml(value.toString());
            }

            //������ַ�����valueֵ��Ҫת����ڴ�ת��
            if (sf.getType() == DataType.STRING_TYPE) {
//        if (elementBean.isNeedEncode() & value != null) {
//          value = zt.platform.db.DBUtil.toDB(value.toString());
//        }
                if (value != null) {
                    value = zt.platform.db.DBUtil.toDB(value.toString());
                }
            }
            //��������ڣ���Ҫ����"-"
            if (sf.getType() == DataType.DATE_TYPE) {
                if (value != null) {
                    String temp = value.toString();
                    if (temp.length() == 8) {
                        temp = temp.substring(0, 4) + "-" + temp.substring(4, 6) + "-" + temp.substring(6, 8);
                        value = temp;
                    } else if (temp.equals("#SYSDATE#")) {//lj added in 20090324 for oracle sysdate
                        value = temp;
                    } else {
                        value = null;
                    }
                }
            }
            sf.setValue(value);
            addSqlField((sf));
            //type=0
            if (type == 0) {
                if (elementBean.isIsSearchKey()) {
                    SqlWhere sw = new SqlWhere();
                    sw.setTblname(sf.getTblname());
                    sw.setFldname(sf.getFldname());
                    sw.setType(sf.getType());
                    sw.setOperator(OperatorType.NOOPERATOR_TYPE);
                    sw.setValue(sf.getValue());
                    addSqlWhere(sw);
                }
            } else {
                if (elementBean.isIsPrimaryKey()) {
                    SqlWhere sw = new SqlWhere();
                    sw.setTblname(sf.getTblname());
                    sw.setFldname(sf.getFldname());
                    sw.setType(sf.getType());
                    sw.setOperator(OperatorType.NOOPERATOR_TYPE);
                    sw.setValue(sf.getValue());
                    addSqlWhere(sw);
                }
            }
        }
    }

    /**
     * @param p_defaultTbl defaultTbl
     * @roseuid 3F7EB6140081
     */
    public void setDefaultTbl(String p_defaultTbl) {
        this.defaultTbl = p_defaultTbl;
    }

    /**
     * @return String
     * @roseuid 3F7EB62502A2
     */
    public String getDefaultTbl() {
        return this.defaultTbl;
    }

    /**
     * @param sqlField �˷�����forminstance��
     */
    public void addSqlField(SqlField sqlField) {
        //��Ч�Լ��
        String tblname;
        //Object value;
        tblname = sqlField.getTblname();
        if (tblname == null || tblname.length() < 1) {
            tblname = this.defaultTbl;
        }
        sqlField.setTblname(tblname);
        //value=sqlField.getValue();
        //if(value==null) return;
        this.sqlFields.add(sqlField);
        //��sqlFields�����е�tblname�ϲ�ͬ����
        int j = tbls.size();
        for (int i = 0; i < j; i++) {
            if (!((String) tbls.get(i)).equals(tblname)) {
                tbls.add(tblname);
            }
        }
    }

    /**
     * ����table��ĳһ�ֶε�ֵ
     * �˷�����Ӧ����Ա��
     */
    public void setSqlFieldValue(String p_tblName, String p_fldName,
                                 String p_value) {
        //��Ч�Լ��
        SqlField sf = getSqlField(p_tblName, p_fldName);
        if (sf != null) {
            int type = sf.getType();
            sf.setValue(FormInstance.convertValueSingle(DBUtil.toSql(p_value), type));
        }
    }

    /**
     * ����idx����SqlField
     */
    public SqlField getSqlField(int idx) {
        return (SqlField) this.sqlFields.get(idx);
    }

    /**
     * ����tblName��fldName����SqlField
     */
    public SqlField getSqlField(String p_tblName, String p_fldName) {
        //��⴦��
        if (p_tblName == null || p_fldName == null) {
            return null;
        }
        String tblName1 = p_tblName.trim().toLowerCase();
        String fldName1 = p_fldName.trim().toLowerCase();
        //��������
        for (int i = 0; i < sqlFields.size(); i++) {
            SqlField sf = (SqlField) sqlFields.get(i);
            String tblName2 = sf.getTblname().trim().toLowerCase();
            String fldName2 = sf.getFldname().trim().toLowerCase();
            if (tblName2.equals(tblName1) && fldName2.equals(fldName1)) {
                return sf;
            }
        }
        return null;
    }

    /**
     * @return List
     * @roseuid 3F72987202AB
     */
    public List getSqlFields() {
        return (List) this.sqlFields;
    }

    /**
     * @param sqlField
     * @roseuid 3F7298500125
     */
    public void removeSqlField(SqlField sqlField) {
        this.sqlFields.remove(sqlField);
    }

    /**
     * @param idx
     * @roseuid 3F7298F4012B
     */
    public void removeSqlField(int idx) {
        this.sqlFields.remove(idx);
    }

    /**
     * @param sqlWhere
     * @roseuid 3F7E0F4F0123
     */
    public void addSqlWhere(SqlWhere sqlWhere) {
        //��Ч�Լ��
        String tblname;
        Object value;
        tblname = sqlWhere.getTblname();
        if (tblname == null || tblname.length() < 1) {
            tblname = this.defaultTbl;
        }
        sqlWhere.setTblname(tblname);
        value = sqlWhere.getValue();
        this.sqlWheres.add(sqlWhere);
        //��sqlWheres�����е�tblname�ϲ�ͬ����
        int j = tbls.size();
        for (int i = 0; i < j; i++) {
            if (!((String) tbls.get(i)).equals(sqlWhere.getTblname())) {
                tbls.add(tblname);
            }
        }
    }

    /**
     * @param idx
     * @return zt.platform.form.util.SqlWhere
     * @roseuid 3F7298D302B4
     */
    public SqlWhere getSqlWhere(int idx) {
        return (SqlWhere) this.sqlWheres.get(idx);
    }

    /**
     * @return List
     * @roseuid 3F7298E6003B
     */
    public List getSqlWheres() {
        return (List) this.sqlWheres;
    }

    /**
     * @param sqlWhere
     * @roseuid 3F7298B003E0
     */
    public void removeSqlWhere(SqlWhere sqlWhere) {
        sqlWheres.remove(sqlWhere);
    }

    /**
     * @param idx
     * @roseuid 3F7298CB02D1
     */
    public void removeSqlWhere(int idx) {
        sqlWheres.remove(idx);
    }

    /**
     * ����SQL��䴮
     * type:
     * 0-����insert���
     * 1-����update���
     * 2-����delete���
     * 3-����select���
     *
     * @param type
     * @return String
     * @roseuid 3F72990C0266
     */
    public String toSql(int type) {
        //���sqlFieldsû�����ݻ�defaultTblΪ�գ�����
        int j = this.sqlFields.size();
        if (j < 1 || defaultTbl == null || defaultTbl.length() < 1) {
            return null;
        }

        //������ʱ�ַ���temp���INSERT_SQL_TYPE���͵�sql������������о����������γɵ�
        //sqlFields���ǺϷ���,ͬʱ���������û��Լ��Ӵ�����sqlFieldsҲ���ǺϷ��ġ�
        if (type == this.INSERT_SQL_TYPE) {
            String temp = "insert into " + defaultTbl + " (";
            //�ֶ���ϣ�ֻ���tblname��defaultTbl��ȵ��ֶΣ�
            for (int i = 0; i < j; i++) {
                SqlField sf = (SqlField) sqlFields.get(i);
                if (sf.getTblname().equals(defaultTbl) && sf.getValue() != null) {
                    temp += "\"" + sf.getFldname() + "\",";                       //lj changed in 200903
                }
            }
            if (temp.endsWith(" (")) {
                return null;
            }
            temp = temp.substring(0, temp.length() - 1);
            temp += ") values(";
            //��ֵ��ϣ�ֻ���tblname��defaultTbl��ȵ��ֶΣ�
            for (int i = 0; i < j; i++) {
                SqlField sf = (SqlField) sqlFields.get(i);
                if (sf.getTblname().equals(defaultTbl) && sf.getValue() != null) {
                    temp += crtFieldValue(sf.getValue().toString(), sf.getType()) + ",";
                }
            }
            temp = temp.substring(0, temp.length() - 1);
            temp += ")";
            logger.info(temp);
            return temp;
        }

        //���������update,delete,select����sql��Ҫ���ĺϷ��Լ��
        //û��where���������˳�
        int k = this.sqlWheres.size();
        if (k < 1) {
            return null;
        }
        //where��û��defaultTbl��Ҳ�˳�
        boolean flg = false;
        for (int i = 0; i < k; i++) {
            if (((SqlWhere) sqlWheres.get(i)).getTblname().equals(defaultTbl)) {
                flg = true;
                break;
            }
        }
        if (!flg) {
            return null;
        }

        //������ʱ�ַ���temp���UPDATE_SQL_TYPE���͵�sql
        if (type == this.UPDATE_SQL_TYPE) {
            //��ֵ��ϣ�ֻ���tblname��defaultTbl��ȵ��ֶΣ��ҹؼ��ֶβ����£�
            String temp = "update " + defaultTbl + " set ";
            for (int i = 0; i < j; i++) {
                SqlField sf = (SqlField) sqlFields.get(i);
                if (sf.getTblname().equals(defaultTbl) && !formInstance.isFk(sf.getFldname())) {
                    temp += "\"" + sf.getFldname();                                 //lj changed in 200903
                    temp += "\"=";                                                  //lj changed in 200903
                    temp += crtFieldValue(sf.getValue(), sf.getType());
                    temp += ",";
                }
            }
            if (temp.endsWith(" set ")) {
                return null;
            }
            temp = temp.substring(0, temp.length() - 1);
            //������ϣ�ֻ���tblname��defaultTbl��ȵ��ֶΣ�,����������ϵ����and
            temp += " where ";
            for (int i = 0; i < k; i++) {
                SqlWhere sw = (SqlWhere) sqlWheres.get(i);
                if (sw.getTblname().equals(defaultTbl)) {
                    if (sw.getValue() == null) {
                        temp += "\"" + sw.getFldname() + "\" is null and ";        //lj changed in 200903
                    } else if (isMaxValue(sw.getValue().toString(), sw.getType())) {
                        continue;
                    } else {
                        temp += "\"" + sw.getFldname() + "\" ";                      //lj changed in 200903
                        temp += (new OperatorType()).getOperator(sw.getOperator()) + " ";
                        temp += crtWhereValue(sw.getValue().toString(), sw.getType(), sw.getOperator());
                        temp += " and ";
                    }
                }
            }
            temp = temp.substring(0, temp.length() - 5);
            logger.info(temp);
            return temp;
        }

        //������ʱ�ַ���temp���DELETE_SQL_TYPE���͵�sql
        if (type == this.DELETE_SQL_TYPE) {
            //������ϣ�ֻ���tblname��defaultTbl��ȵ��ֶΣ�
            String temp = "delete from " + defaultTbl + " where ";
            for (int i = 0; i < k; i++) {
                SqlWhere sw = (SqlWhere) sqlWheres.get(i);
                if (sw.getTblname().equals(defaultTbl)) {
                    if (sw.getValue() == null) {
                        temp += "\"" + sw.getFldname() + "\" is null and ";         //lj changed in 200903
                    } else if (isMaxValue(sw.getValue().toString(), sw.getType())) {
                        continue;
                    } else {
                        temp += "\"" + sw.getFldname() + "\" ";                       //lj changed in 200903
                        temp += (new OperatorType()).getOperator(sw.getOperator()) + " ";
                        temp += crtWhereValue(sw.getValue().toString(), sw.getType(), sw.getOperator());
                        temp += " and ";
                    }
                }
            }
            temp = temp.substring(0, temp.length() - 5);
            logger.info(temp);
            return temp;
        }

        //������ʱ�ַ���temp���SELECT_SQL_TYPE���͵�sql
        if (type == this.SELECT_SQL_TYPE) {
            //�ֶ����
            String temp = "select ";
            for (int i = 0; i < j; i++) {
                SqlField sf = (SqlField) sqlFields.get(i);
                temp += sf.getTblname() + ".\"" + sf.getFldname() + "\"";            //lj changed in 200903
                if (sf.getAliaseName() != null && sf.getAliaseName().length() > 0) {
                    temp += " as ";
                    temp += sf.getAliaseName();
                }
                temp += ",";
            }
            temp = temp.substring(0, temp.length() - 1);
            //from���
            temp += " from ";
            for (int i = 0; i < tbls.size(); i++) {
                temp += tbls.get(i) + ",";
            }
            temp = temp.substring(0, temp.length() - 1);
            //�������
            temp += " where ";
            for (int i = 0; i < k; i++) {
                SqlWhere sw = (SqlWhere) sqlWheres.get(i);
                if (sw.getValue() == null || (sw.getValue().toString()).length() < 1) {
                    continue;
                }
                if (isMaxValue(sw.getValue().toString(), sw.getType())) {
                    continue;
                }
                temp += sw.getTblname() + ".\"" + sw.getFldname() + "\" ";         //lj changed in 200903
                //��û�����ò�������sw���������������String����ô��������ΪLIKE_OPERATOR_TYPE����
                if (sw.getType() == DataType.STRING_TYPE &&
                        sw.getOperator() == OperatorType.NOOPERATOR_TYPE) {
//          sw.setOperator(OperatorType.LIKE_OPERATOR_TYPE);
                    sw.setOperator(OperatorType.EQUALS_OPERATOR_TYPE);
                }
                temp += (new OperatorType()).getOperator(sw.getOperator()) + " ";
                temp += crtWhereValue(sw.getValue().toString(), sw.getType(), sw.getOperator());
                temp += " and ";
            }
            //temp = temp.substring(0, temp.length() - 5);                      //lj changed in 200903
            if (temp.endsWith(" where "))                                      //lj changed in 200903
                temp = temp.substring(0, temp.length() - 7);
            else
                temp = temp.substring(0, temp.length() - 5);

            logger.info(temp);
            return temp;
        } else {
            logger.log(Level.WARNING, "ƽ̨�γ�SQL���ΪNULL");
            return null;
        }
    }

    /**
     * ���p_value�ǲ��������͵����ֵ����Ϊƽ̨�����ֵ����ʾIE��
     * δ����������������null����ʾIE��Form��û����Ӧ�������
     */
    public static boolean isMaxValue(String p_value, int p_tp) {
        //INTEGER_TYPE,DECIAML_TYPE��MAX_VALUEת��ΪNULL
//        if (p_tp == DataType.INTEGER_TYPE || p_tp == DataType.ENUMERATION_TYPE) {
        if (p_tp == DataType.INTEGER_TYPE) {
            if (Integer.parseInt(p_value) == Integer.MAX_VALUE) {
                return true;
            }
        } else if (p_tp == DataType.DECIAML_TYPE) {
            if (Double.parseDouble(p_value) == Double.MAX_VALUE) {
                return true;
            }
        }
        return false;
    }

    /**
     * ���ɣ�insert update deleteʱ��SQL��䴮��"set field=value"�е�value���֣���Ϊ�е���Ҫ��"'"
     *
     * @param p_value type
     * @param p_tp
     * @return String
     */
    public static String crtFieldValue(Object p_value, int p_tp) {
        if (p_value == null) {
            return "NULL";
        }
        return crtFieldValue(p_value.toString(), p_tp);
    }

    public static String crtFieldValue(String p_value, int p_tp) {
        //null��''ֵ����
        if (p_value == null || p_value.length() < 1) {
            return "NULL";
        }
        //��Ҫ��'��ֵ���
//        if (p_tp == DataType.STRING_TYPE || p_tp == DataType.BOOLEAN_TYPE || p_tp == DataType.DATE_TYPE) {//lj changed in 20090226
        if (p_tp == DataType.STRING_TYPE || p_tp == DataType.BOOLEAN_TYPE) {  //lj changed in 20090226
            return "'" + p_value + "'";
        } else if (p_tp == DataType.DATE_TYPE) {                               //lj changed in 20090226
            if (p_value.equals("#SYSDATE#")) return "SYSDATE";
            else return "to_date('" + p_value + "','YYYY-MM-DD')";
        }
        //INTEGER_TYPE,DECIAML_TYPE��MAX_VALUEת��ΪNULL
        else if (isMaxValue(p_value, p_tp)) {
            return "NULL";
        }
        //�����'��ֵ��ϣ�����DataType�ж�������������⣬��������Ϊ��ֵ7��
        else {
            return p_value;
        }
    }

    /**
     * ���ɣ�insert update deleteʱ��SQL��䴮��"set field=value"�е�value���֣���Ϊ�е���Ҫ��"'"
     *
     * @param p_value type
     * @return String
     */
    public static String crtWhereValue(String p_value, int p_tp, int p_opt) {
        //��Ҫ��'��ֵ���
        String operator = new OperatorType().getOperator(p_opt);
        if (p_tp == DataType.STRING_TYPE &&
                (p_opt == OperatorType.LIKE_OPERATOR_TYPE ||
                        p_opt == OperatorType.NOT_LIKE_OPERATOR_TYPE)) {
            return "'%" + p_value + "%'";
//        } else if (p_tp == DataType.STRING_TYPE || p_tp == DataType.DATE_TYPE ||      //lj changed in 20090226
//                p_tp == DataType.BOOLEAN_TYPE) {                                      //lj changed in 20090226
        } else if (p_tp == DataType.STRING_TYPE || p_tp == DataType.BOOLEAN_TYPE) { //lj changed in 20090226
            return "'" + p_value + "'";
        } else if (p_tp == DataType.DATE_TYPE) {                                       //lj changed in 20090226
            if (p_value.equals("#SYSDATE#")) return "SYSDATE";                       //lj changed in 20090226
            else return "to_date('" + p_value + "','YYYY-MM-DD')";                  //lj changed in 20090226
        }
        //�����'��ֵ��ϣ�����DataType�ж�������������⣬��������Ϊ��ֵ7��
        else {
            return p_value;
        }
    }
}
