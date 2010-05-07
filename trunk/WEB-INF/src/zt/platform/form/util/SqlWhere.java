//Source file: e:\\java\\zt\\platform\\form\\util\\SqlWhere.java

package zt.platform.form.util;

import java.io.Serializable;

import zt.platform.form.util.datatype.DataType;
import zt.platform.form.util.datatype.OperatorType;

/**
 * where������
 *
 * @author ��ѧ��
 * @version 1.0
 */
public class SqlWhere implements Comparable ,Serializable {
    private String tblname;
    private String fldname;
    private int operator;
    private Object value;
    private int type;//��zt.platform.form.util.datatype.DataType��Ӧ����չֵ7��Ӧ����������ֶι�����
    private String relation;

    /**
     * ���캯��
     */
    public SqlWhere(String p_tblname, String p_fldname, int p_operator, Object p_value, int p_type, String relation) {
        this.tblname = p_tblname;
        this.fldname = p_fldname;
        this.operator = p_operator;
        this.value = p_value;
        this.type = p_type;
        this.relation = relation;
    }

    public SqlWhere(String p_tblname, String p_fldname, int p_operator, Object p_value, int p_type) {
        this.tblname = p_tblname;
        this.fldname = p_fldname;
        this.operator = p_operator;
        this.value = p_value;
        this.type = p_type;
    }

    public SqlWhere(String p_fldname, int p_operator, Object p_value, int p_type) {
        this.tblname = null;
        this.fldname = p_fldname;
        this.operator = p_operator;
        this.value = p_value;
        this.type = p_type;
    }

    public SqlWhere(String p_fldname, Object p_value, int p_type) {
        this.tblname = null;
        this.fldname = p_fldname;
        this.operator = OperatorType.NOOPERATOR_TYPE;
        this.value = p_value;
        this.type = p_type;
    }

    public SqlWhere() {
        this.tblname = null;
        this.fldname = null;
        this.operator = OperatorType.NOOPERATOR_TYPE;
        this.value = null;
        this.type = DataType.STRING_TYPE;
    }

    /**
     * @param tblname
     * @roseuid 3F7EB6140081
     */
    public void setTblname(String p_tblname) {
        this.tblname = p_tblname;
    }

    /**
     * @return String
     * @roseuid 3F7EB62502A2
     */
    public String getTblname() {
        return this.tblname;
    }

    /**
     * @param fldname
     * @roseuid 3F7EB6140081
     */
    public void setFldname(String p_fldname) {
        this.fldname = p_fldname;
    }

    /**
     * @return String
     * @roseuid 3F7EB62502A2
     */
    public String getFldname() {
        return this.fldname;
    }

    /**
     * @param operator
     * @roseuid 3F7EB6140081
     */
    public void setOperator(int p_operator) {
        this.operator = p_operator;
    }

    /**
     * @return int
     * @roseuid 3F7EB62502A2
     */
    public int getOperator() {
        return this.operator;
    }

    /**
     * @param value
     * @roseuid 3F7EB6140081
     */
    public void setValue(Object p_value) {
        this.value = p_value;
    }

    /**
     * @return Object
     * @roseuid 3F7EB62502A2
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * @param type
     * @roseuid 3F7EB6140081
     */
    public void setType(int p_type) {
        this.type = p_type;
    }

    /**
     * @return int
     * @roseuid 3F7EB62502A2
     */
    public int getType() {
        return this.type;
    }

    public boolean equals(Object o) {
        if (o instanceof SqlWhere) {
            SqlWhere tmp = (SqlWhere) o;
            if (tmp.getTblname() != null && tmp.getTblname().equals(this.tblname) &&
                    tmp.getFldname() != null && tmp.getFldname().equals(this.fldname))
                return true;
        }
        return false;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int compareTo(Object o) {
        if (o instanceof SqlWhere) {
            SqlWhere tmp = (SqlWhere) o;
            if (tmp.getTblname() != null && tmp.getTblname().equals(this.tblname) &&
                    tmp.getFldname() != null && tmp.getFldname().equals(this.fldname))
                return 0;
        }
        return -1;

    }
}
