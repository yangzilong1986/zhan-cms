package zt.platform.form.util;

import zt.platform.form.util.datatype.DataType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ����Where�����
 * <p/>
 * �ֶ����Ͱ������ַ�-0������-1 ����-2
 * ������:=��<��>��like��<=��>=��!=��no like
 * ��ϵ�����:and ��or
 */

public class SqlWhereUtil {
    private Logger log = Logger.getLogger("zt.platform.form.util.SqlWhereUtil");
    public static final int DataType_Character = 0;
    public static final int DataType_Number = 1;
    public static final int DataType_Date = 2;
    public static final int DataType_Sql = 3;


    private static final String Operator_No = "";
    private static final String Operator_Equals = "=";
    private static final String Operator_Lower = "<";
    private static final String Operator_Greater = ">";
    private static final String Operator_Like = " like ";
    private static final String Operator_Lower_Equals = "<=";
    private static final String Operator_Greater_Equals = ">=";
    private static final String Operator_Not_Equals = "!=";
    private static final String Operator_Not_Like = " not like ";
    private static final String Operator_In = " in ";

    public static final int OperatorType_No = -1;
    public static final int OperatorType_Equals = 0;
    public static final int OperatorType_Lower = 1;
    public static final int OperatorType_Greater = 2;
    public static final int OperatorType_Like = 3;
    public static final int OperatorType_Lower_Equals = 4;
    public static final int OperatorType_Greater_Equals = 5;
    public static final int OperatorType_Not_Equals = 6;
    public static final int OperatorType_Not_Like = 7;
    public static final int OperatorType_In = 8;

    public static final String RelationOperator_And = " and ";
    public static final String RelationOperator_Or = " or ";
    public static final String RelationOperator_NONE = " ";

    private List sqlWheres = new ArrayList();

    /**
     * ����Where�ֶ�
     *
     * @param tblname
     * @param fldname
     * @param value
     * @param datatype     type        - ����(0-�ַ� 1-���� 2-����)
     * @param operatorType operator    �� ������ (-1-�޷���,0-�Ⱥ�1-С��2-����3-like4��С�ڵ���5-���ڵ���6������7-not like)
     * @param relationType relationOpr - ��ϵ��(and��or)
     */
    public void addWhereField(String tblname, String fldname, String value, int datatype, int operatorType, String relationType) {
        if (datatype == DataType_Character) {
            if (operatorType == SqlWhereUtil.OperatorType_Like ||
                    operatorType == SqlWhereUtil.OperatorType_Not_Like) {
                /**
                 * changed by JGO on 20040804 to speed up the system performance
                 */
                //value = "'%"+value+"%'";
                value = "'" + value + "%'";
            } else
                value = "'" + value + "'";
//        } else if (datatype == DataType_Date) {   //lj changed in 20090317
//            value = "'" + toDate(value) + "'";
        } else if (datatype == DataType_Date) {  //lj changed in 20090317
            value = "to_date('" + toDate(value) + "','YYYY-MM-DD')";
        } else if (datatype == DataType_Sql) {
            value = "(" + value + ")";
        }

        sqlWheres.add(new SqlWhere(tblname, fldname, operatorType, value, DataType.STRING_TYPE, relationType));
    }
    /**
     * ����Where�ֶ�,Ĭ���ַ�����
     * @param tblname
     * @param fldname
     * @param value
     * @param operator
     * @param relationOpr
     */
//    public void addWhereField(String tblname, String fldname, String value, int operatorType,String relationOpr)
//    {
//        value = "'" + value + "'";
//        sqlWheres.add(new SqlWhere(tblname, fldname, operatorType, value, DataType.STRING_TYPE,relationOpr));
//    }
    /**
     * ����Where�ֶ�,Ĭ���ַ�����
     * @param tblname
     * @param fldname
     * @param value
     * @param operator
     */
//    public void addWhereField(String tblname, String fldname, String value, int operatorType)
//    {
//        value = "'" + value + "'";
//        sqlWheres.add(new SqlWhere(tblname, fldname, operatorType, value, DataType.STRING_TYPE,RelationOperator_And));

    //    }

    /**
     * ����Where�ֶ�
     *
     * @param tblname
     * @param fldname
     * @param value
     * @param datatype     type
     * @param operatorType operator
     */
    public void addWhereField(String tblname, String fldname, String value, int datatype, int operatorType) {
        if (datatype == DataType_Character) {
            if (operatorType == SqlWhereUtil.OperatorType_Like ||
                    operatorType == SqlWhereUtil.OperatorType_Not_Like) {
                /**
                 * changed by JGO on 20040804 to speed up the system performance
                 */
                //value = "'%"+value+"%'";
                value = "'" + value + "%'";
            } else
                value = "'" + value + "'";
        } else if (datatype == DataType_Date) {
            value = "'" + toDate(value) + "'";
        } else if (datatype == DataType_Sql) {
            value = "(" + value + ")";
        }


        sqlWheres.add(new SqlWhere(tblname, fldname, operatorType, value, DataType.STRING_TYPE, RelationOperator_And));
    }
    /**
     * ����Where�ֶ�,Ĭ���ַ�����
     * @param tblname
     * @param fldname
     * @param value
     * @param relationOpr
     */
//    public void addWhereField(String tblname, String fldname, String value, String relationType)
//    {
//        value = "'" + value + "'";
//        sqlWheres.add(new SqlWhere(tblname, fldname, OperatorType_Equals, value,DataType.STRING_TYPE, relationType));
//    }
    /**
     * ����Where�ֶ�
     * @param tblname
     * @param fldname
     * @param value
     * @param relationOpr
     * @param type
     */
//    public void addWhereField(String tblname, String fldname, String value, String relationType,int datatype)
//    {
//        if (datatype == DataType_Character) {
//            value = "'" + value + "'";
//        } else if ( datatype == DataType_Date ) {
//            value = toDate(value);
//        }
//
//
//        sqlWheres.add(new SqlWhere(tblname, fldname, OperatorType_Equals, value,DataType.STRING_TYPE, relationType));
//    }

    /**
     * ����Where�ֶ�
     *
     * @param tblname
     * @param fldname
     * @param datatype type    - ����/�ַ�/����
     * @param value
     */
    public void addWhereField(String tblname, String fldname, String value, int datatype) {
        if (datatype == DataType_Character) {
            value = "'" + value + "'";
        } else if (datatype == DataType_Date) {
            value = "'" + toDate(value) + "'";
        } else if (datatype == DataType_Sql) {
            value = "(" + value + ")";
        }


        sqlWheres.add(new SqlWhere(tblname, fldname, OperatorType_Equals, value,
                DataType.STRING_TYPE, RelationOperator_And));
    }

    /**
     * ����Where�ֶ�,Ĭ���ַ�����
     *
     * @param tblname
     * @param fldname
     * @param value
     */
    public void addWhereField(String tblname, String fldname, String value) {
        value = "'" + value + "'";
        sqlWheres.add(new SqlWhere(tblname, fldname, OperatorType_Equals, value,
                DataType.STRING_TYPE, RelationOperator_And));
    }


    public void clear() {
        sqlWheres.clear();
    }

    /**
     * ɾ��ָ���ֶ�
     *
     * @param tblname
     * @param fldname
     */
    public void removeWhereField(String tblname, String fldname) {
        SqlWhere tmpWhere = new SqlWhere(tblname, fldname, DataType.STRING_TYPE, "",
                DataType.STRING_TYPE);
        sqlWheres.remove(tmpWhere);
    }

    /**
     * �޸�ָ���ֶε�ֵ
     *
     * @param tblname
     * @param fldname
     * @param value
     */
    public void setWhereFld(String tblname, String fldname, String value) {
        SqlWhere tmpWhere = new SqlWhere(tblname, fldname, DataType.STRING_TYPE, value,
                DataType.STRING_TYPE);
        int index = sqlWheres.indexOf(tmpWhere);
        if (index >= 0) {
            tmpWhere = (SqlWhere) sqlWheres.get(index);
            tmpWhere.setValue(value);
        }
    }

    public String toSqlWhere(boolean hasWhere) {

        String sql = " ";
        if (hasWhere)
            sql += " where ";
        for (int i = 0; i < sqlWheres.size(); i++) {
            SqlWhere sqlWhere = (SqlWhere) sqlWheres.get(i);

            //����ֶ��а����������򲻼ӱ���
            if (sqlWhere.getFldname().indexOf(".") != -1) {
                sql += sqlWhere.getFldname();
            } else {
                if (sqlWhere.getTblname().equals("")) {
                    sql += sqlWhere.getFldname();
                } else {
                    sql += sqlWhere.getTblname() + "." + sqlWhere.getFldname();
                }
            }
            sql += getOperator(sqlWhere.getOperator());
            sql += sqlWhere.getValue();
            if (i < sqlWheres.size() - 1) {
                sql += " " + sqlWhere.getRelation() + " ";
            }
        }
        log.log(Level.INFO, sql);


        if (sql.trim().endsWith("where"))
            return "";

        return sql;
    }

    private String getOperator(int operator) {
        switch (operator) {
            case OperatorType_No:
                return Operator_No;
            case OperatorType_Equals:
                return Operator_Equals;
            case OperatorType_Lower:
                return Operator_Lower;
            case OperatorType_Greater:
                return Operator_Greater;
            case OperatorType_Like:
                return Operator_Like;
            case OperatorType_Lower_Equals:
                return Operator_Lower_Equals;
            case OperatorType_Greater_Equals:
                return Operator_Greater_Equals;
            case OperatorType_Not_Equals:
                return Operator_Not_Equals;
            case OperatorType_Not_Like:
                return Operator_Not_Like;
            case OperatorType_In:
                return Operator_In;
            default:
                return Operator_Equals;
        }
    }

    public String toDate(String date) {
        if (date == null || date.length() != 8) {
            return date;
        }
        return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
    }

    public static void main(String[] args) {
        SqlWhereUtil swu = new SqlWhereUtil();
//        swu.addWhereField("t1","id",SqlWhereUtil.Number_Type,"00001");
        swu.addWhereField("t1", "name", "wwwww", SqlWhereUtil.DataType_Character, SqlWhereUtil.OperatorType_Like, SqlWhereUtil.RelationOperator_And);
        swu.addWhereField("t2", "date", "20030405", SqlWhereUtil.DataType_Date, SqlWhereUtil.OperatorType_Like, SqlWhereUtil.RelationOperator_And);
        swu.addWhereField("t2", "date", "20030406", SqlWhereUtil.DataType_Date, SqlWhereUtil.OperatorType_Like, SqlWhereUtil.RelationOperator_And);
        swu.addWhereField("t2", "ccc", "select * from td", SqlWhereUtil.DataType_Sql, SqlWhereUtil.OperatorType_In, SqlWhereUtil.RelationOperator_NONE);
        swu.toSqlWhere(false);

    }
}
