/**
 * Copyright 2003 ZhongTian, Inc. All rights reserved.
 *
 * Zhongtian PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * $Id: RecordSet.java,v 1.1 2007/04/28 14:08:36 liuj Exp $
 * File:RecordSet.java
 * Date Author Changes
 * March 5 2003 wangdeliang Created
 */

package zt.platform.db;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

//import com.zt.trace.Debug;

/**
 * �����
 *
 * @author <a href="mailto:wangdl@zhongtian.biz">WangDeLiang</a>
 * @version $Revision: 1.1 $ $Date: 2007/04/28 14:08:36 $
 */

public class RecordSet {
    private List records = new ArrayList();
    private HashMap metadata = new HashMap();
    private List fieldNames = new ArrayList();
    private int currentNo = -1;
    private int recordCount = 0;
    private int fieldCount = 0;

    /**
     * @param rs
     * @roseuid 3E5D7C00033C
     */
    public RecordSet(java.sql.ResultSet rs) {
        init(rs);
    }

    public RecordSet(java.sql.ResultSet rs, int no) {
        init(rs, no);
    }


    /**
     * @roseuid 3E5D7BF502FA
     */
    public RecordSet() {
    }

    /**
     * @return boolean
     * @roseuid 3E5D86C703E3
     */
    public boolean isFirst() {
        return currentNo == 0;
    }

    /**
     * @roseuid 3E5D7C20008F
     */
    public void first() {
        currentNo = 0;
    }

    public void beforeFirst() {
        currentNo = -1;
    }

    /**
     * @return boolean
     * @roseuid 3E5D86D10365
     */
    public boolean isLast() {
        return currentNo == recordCount;
    }

    /**
     * @roseuid 3E5D7CA3008D
     */
    public void last() {
        currentNo = recordCount;
    }

    /**
     * @return boolean
     * @roseuid 3E5D7CAA01D7
     */
    public boolean next() {
        if (currentNo < recordCount - 1) {
            currentNo++;
            return true;
        }
        return false;
    }

    /**
     * @return boolean
     * @roseuid 3E5D7CB200D5
     */
    public boolean previous() {
        if (currentNo > 0) {
            currentNo--;
            return true;
        }
        return false;
    }

    /**
     * @param idx
     * @roseuid 3E5D7CC1000E
     */
    public void absolute(int idx) {
        if (idx >= 0 && idx < recordCount) {
            currentNo = idx;
        }
    }

    /**
     * @param idx
     * @roseuid 3E5D870601D1
     */
    public void relative(int idx) {
        currentNo += idx;
        if (currentNo < 0)
            currentNo = 0;
        if (currentNo > recordCount)
            currentNo = recordCount - 1;
    }

    /**
     * @param record
     * @roseuid 3E5D7CDF033C
     */
    public void add(Record record) {
        records.add(record);
    }

    /**
     * @param record
     * @roseuid 3E5D7D89030F
     */
    public void delete(Record record) {
        records.remove(record);
    }

    /**
     * @param list
     * @roseuid 3E5D7DA30384
     */
    public void merge(java.util.List list) {
        if (list == null)
            return;
        for (Iterator it = list.iterator(); it.hasNext();) {
            Object obj = it.next();
            if (obj instanceof Record)
                records.add(obj);
        }
    }

    /**
     * @param rs
     * @roseuid 3E5D7DC700DD
     */
    public void merge(java.sql.ResultSet rs) throws Exception {
        throw new Exception("�÷�����δʵ��");
    }

    /**
     * @param idx
     * @param type
     * @return boolean
     * @roseuid 3E5D7DD403CB
     */
    public boolean insertItem(int idx, String type) throws Exception {
        throw new Exception("�÷�����δʵ��");
    }

    /**
     * @param idx
     * @return boolean
     * @roseuid 3E5D7E3F0374
     */
    public boolean deleteItem(int idx) throws Exception {
        throw new Exception("�÷�����δʵ��");
    }

    /**
     * @param idx
     * @return com.zt.db.RecordMetaData
     * @roseuid 3E5D800400FF
     */
    public RecordMetaData getMetaData(int idx) {
        Collection cl = metadata.values();
        for (Iterator it = cl.iterator(); it.hasNext();) {
            RecordMetaData rmd = (RecordMetaData) it.next();
            if (rmd.getSeqno() == idx)
                return rmd;
        }
        return null;
    }

    /**
     * @param name
     * @return
     */
    public RecordMetaData getMetaData(String name) {
        return (RecordMetaData) metadata.get(name);
    }


    /**
     * ֻ��String��Boolean���ͽ���ת�����������;�����false
     *
     * @param columnIdx
     * @return boolean
     * @roseuid 3E5D883200BA
     */
    public boolean getBoolean(int columnIdx) {
        if (columnIdx < 0 || columnIdx > fieldCount || currentNo < 0 || currentNo > recordCount)
            return false;

        String name = (String) fieldNames.get(columnIdx);
        RecordMetaData rmd = (RecordMetaData) metadata.get(name);

        if (rmd.getType() == MetaDataTypes.STRING_TP) {
            Record record = (Record) records.get(currentNo);
            String value = (String) record.get(columnIdx);
            if (value == null || value.equals("0"))
                return false;
            return true;
        }
        if (rmd.getType() == MetaDataTypes.BOOLEAN_TP) {
            Record record = (Record) records.get(currentNo);
            Boolean value = (Boolean) record.get(columnIdx);
            return value.booleanValue();
        } else {
            return false;
        }
    }

    /**
     * @param columnIdx
     * @return byte[]
     * @roseuid 3E5D885A02FC
     */
    public byte[] getByte(int columnIdx) throws Exception {
        throw new Exception("�÷�����δʵ��!");
    }

    /**
     * @param columnIdx
     * @return short
     * @roseuid 3E5D88780133
     */
    public short getShort(int columnIdx) {
        if (columnIdx < 0 || columnIdx > fieldCount || currentNo < 0 || currentNo > recordCount)
            return 0;

        String name = (String) fieldNames.get(columnIdx);
        RecordMetaData rmd = (RecordMetaData) metadata.get(name);
        Record record = (Record) records.get(currentNo);
        Object value = record.get(columnIdx);
        if (value == null)
            return 0;

        switch (rmd.getType()) {
            case MetaDataTypes.BIGDECIMAL_TP:
                return ((BigDecimal) value).shortValue();
            case MetaDataTypes.BIGINTEGER_TP:
                return ((BigInteger) value).shortValue();
            case MetaDataTypes.BOOLEAN_TP:
                Boolean b = (Boolean) value;
                if (b.booleanValue())
                    return 1;
                else
                    return 0;
            case MetaDataTypes.DOUBLE_TP:
                return ((Double) value).shortValue();
            case MetaDataTypes.FLOAT_TP:
                return ((Float) value).shortValue();
            case MetaDataTypes.INTEGER_TP:
                return ((Integer) value).shortValue();
            case MetaDataTypes.LONG_TP:
                return ((Long) value).shortValue();
            case MetaDataTypes.SHORT_TP:
                return ((Short) value).shortValue();
            case MetaDataTypes.STRING_TP:
                try {
                    return Short.parseShort((String) value);
                } catch (Exception e) {
                    return 0;
                }
            default:
                return 0;
        } //switch
    }

    /**
     * @param columnIdx
     * @return int
     * @roseuid 3E5D888D0269
     */
    public int getInt(int columnIdx) {
        if (columnIdx < 0 || columnIdx > fieldCount || currentNo < 0 || currentNo > recordCount)
            return 0;

        String name = (String) fieldNames.get(columnIdx);
        RecordMetaData rmd = (RecordMetaData) metadata.get(name);
        Record record = (Record) records.get(currentNo);
        Object value = record.get(columnIdx);
        if (value == null)
            return 0;

        switch (rmd.getType()) {
            case MetaDataTypes.BIGDECIMAL_TP:
                return ((BigDecimal) value).intValue();
            case MetaDataTypes.BIGINTEGER_TP:
                return ((BigInteger) value).intValue();
            case MetaDataTypes.BOOLEAN_TP:
                Boolean b = (Boolean) value;
                if (b.booleanValue())
                    return 1;
                else
                    return 0;
            case MetaDataTypes.DOUBLE_TP:
                return ((Double) value).intValue();
            case MetaDataTypes.FLOAT_TP:
                return ((Float) value).intValue();
            case MetaDataTypes.INTEGER_TP:
                return ((Integer) value).intValue();
            case MetaDataTypes.LONG_TP:
                return ((Long) value).intValue();
            case MetaDataTypes.SHORT_TP:
                return ((Short) value).intValue();
            case MetaDataTypes.STRING_TP:
                try {
                    return Integer.parseInt((String) value);
                } catch (Exception e) {
                    return 0;
                }
            default:
                return 0;
        } //switch
    }

    /**
     * @param columnIdx
     * @return long
     * @roseuid 3E5D889C0301
     */
    public long getLong(int columnIdx) {
        if (columnIdx < 0 || columnIdx > fieldCount || currentNo < 0 || currentNo > recordCount)
            return 0;

        String name = (String) fieldNames.get(columnIdx);
        RecordMetaData rmd = (RecordMetaData) metadata.get(name);
        Record record = (Record) records.get(currentNo);
        Object value = record.get(columnIdx);
        if (value == null)
            return 0;

        switch (rmd.getType()) {
            case MetaDataTypes.BIGDECIMAL_TP:
                return ((BigDecimal) value).longValue();
            case MetaDataTypes.BIGINTEGER_TP:
                return ((BigInteger) value).longValue();
            case MetaDataTypes.BOOLEAN_TP:
                Boolean b = (Boolean) value;
                if (b.booleanValue())
                    return 1;
                else
                    return 0;
            case MetaDataTypes.DOUBLE_TP:
                return ((Double) value).longValue();
            case MetaDataTypes.FLOAT_TP:
                return ((Float) value).longValue();
            case MetaDataTypes.INTEGER_TP:
                return ((Integer) value).longValue();
            case MetaDataTypes.LONG_TP:
                return ((Long) value).longValue();
            case MetaDataTypes.SHORT_TP:
                return ((Short) value).longValue();
            case MetaDataTypes.STRING_TP:
                try {
                    return Long.parseLong((String) value);
                } catch (Exception e) {
                    return 0;
                }
            default:
                return 0;
        } //switch
    }

    /**
     * @param columnIdx
     * @return float
     * @roseuid 3E5D88AF00C4
     */
    public float getFloat(int columnIdx) {
        if (columnIdx < 0 || columnIdx > fieldCount || currentNo < 0 || currentNo > recordCount)
            return 0;

        String name = (String) fieldNames.get(columnIdx);
        RecordMetaData rmd = (RecordMetaData) metadata.get(name);
        Record record = (Record) records.get(currentNo);
        Object value = record.get(columnIdx);
        if (value == null)
            return 0;

        switch (rmd.getType()) {
            case MetaDataTypes.BIGDECIMAL_TP:
                return ((BigDecimal) value).floatValue();
            case MetaDataTypes.BIGINTEGER_TP:
                return ((BigInteger) value).floatValue();
            case MetaDataTypes.BOOLEAN_TP:
                Boolean b = (Boolean) value;
                if (b.booleanValue())
                    return 1;
                else
                    return 0;
            case MetaDataTypes.DOUBLE_TP:
                return ((Double) value).floatValue();
            case MetaDataTypes.FLOAT_TP:
                return ((Float) value).floatValue();
            case MetaDataTypes.INTEGER_TP:
                return ((Integer) value).floatValue();
            case MetaDataTypes.LONG_TP:
                return ((Long) value).floatValue();
            case MetaDataTypes.SHORT_TP:
                return ((Short) value).floatValue();
            case MetaDataTypes.STRING_TP:
                try {
                    return Float.parseFloat((String) value);
                } catch (Exception e) {
                    return 0;
                }
            default:
                return 0;
        } //switch
    }

    /**
     * @param columnIdx
     * @return double
     * @roseuid 3E5D88C701AE
     */
    public double getDouble(int columnIdx) {
        if (columnIdx < 0 || columnIdx > fieldCount || currentNo < 0 || currentNo > recordCount)
            return 0;

        String name = (String) fieldNames.get(columnIdx);
        RecordMetaData rmd = (RecordMetaData) metadata.get(name);
        Record record = (Record) records.get(currentNo);
        Object value = record.get(columnIdx);
        if (value == null)
            return 0;

        switch (rmd.getType()) {
            case MetaDataTypes.BIGDECIMAL_TP:
                return ((BigDecimal) value).doubleValue();
            case MetaDataTypes.BIGINTEGER_TP:
                return ((BigInteger) value).doubleValue();
            case MetaDataTypes.BOOLEAN_TP:
                Boolean b = (Boolean) value;
                if (b.booleanValue())
                    return 1;
                else
                    return 0;
            case MetaDataTypes.DOUBLE_TP:
                return ((Double) value).doubleValue();
            case MetaDataTypes.FLOAT_TP:
                return ((Float) value).doubleValue();
            case MetaDataTypes.INTEGER_TP:
                return ((Integer) value).doubleValue();
            case MetaDataTypes.LONG_TP:
                return ((Long) value).doubleValue();
            case MetaDataTypes.SHORT_TP:
                return ((Short) value).doubleValue();
            case MetaDataTypes.STRING_TP:
                try {
                    return Double.parseDouble((String) value);
                } catch (Exception e) {
                    return 0;
                }
            default:
                return 0;
        } //switch
    }

    /**
     * @param columnIdx
     * @return char
     * @roseuid 3E5D88DE0085
     */
    public char getChar(int columnIdx) throws Exception {
        throw new Exception("�÷�����δʵ�֣�");
    }

    /**
     * @param columnIdx
     * @return String
     * @roseuid 3E5D88F000F9
     */
    public String getString(int columnIdx) {
        if (columnIdx < 0 || columnIdx > fieldCount || currentNo < 0 || currentNo > recordCount)
            return null;

        String name = (String) fieldNames.get(columnIdx);
        RecordMetaData rmd = (RecordMetaData) metadata.get(name);
        Record record = (Record) records.get(currentNo);
        Object value = record.get(columnIdx);
        if (value == null)
            return null;

        return value.toString();
    }

    /**
     * @param columnIdx
     * @return java.util.Calendar
     * @roseuid 3E5D8903036D
     */
    public Calendar getCalendar(int columnIdx) {
        if (columnIdx < 0 || columnIdx > fieldCount || currentNo < 0 || currentNo > recordCount)
            return null;

        String name = (String) fieldNames.get(columnIdx);
        RecordMetaData rmd = (RecordMetaData) metadata.get(name);
        Record record = (Record) records.get(currentNo);
        Object value = record.get(columnIdx);
        if (value == null) {
            return null;
        } else {
        }


//        if (rmd.getType() == MetaDataTypes.CALENDAR_TP) {
        if (rmd.getType() == MetaDataTypes.CALENDAR_TP || rmd.getType() == MetaDataTypes.ORACALENDAR_TP) {//lj changed in 20090227
            java.sql.Date d = (java.sql.Date) value;
            Calendar c = Calendar.getInstance();
            c.set(1900 + d.getYear(), d.getMonth(), d.getDate());
            return c;
        } else {
            return null;
        }
    }

    /**
     * @param columnIdx
     * @return Object
     * @roseuid 3E5D894200B1
     */
    public Object getObject(int columnIdx) {
        if (columnIdx < 0 || columnIdx > fieldCount || currentNo < 0 || currentNo > recordCount)
            return null;

        String name = (String) fieldNames.get(columnIdx);
        RecordMetaData rmd = (RecordMetaData) metadata.get(name);
        Record record = (Record) records.get(currentNo);
        Object value = record.get(columnIdx);
        return value;
    }

    /**
     * @param columnIdx
     * @param type
     * @return Object
     * @roseuid 3E5D8954037E
     */
    public Object getObject(int columnIdx, String type) throws Exception {
        throw new Exception("�÷�����δʵ��!");
    }

    /**
     * @param columnName
     * @return boolean
     * @roseuid 3E5D8A760186
     */
    public boolean getBoolean(String columnName) {
        if (columnName == null)
            return false;
        int columnIdx = fieldNames.indexOf(columnName.toLowerCase());
        return getBoolean(columnIdx);
    }

    /**
     * @param columnName
     * @return byte[]
     * @roseuid 3E5D8A9202F9
     */
    public byte[] getByte(String columnName) throws Exception {
        throw new Exception("�÷�����δʵ��");
    }

    /**
     * @param columnName
     * @return short
     * @roseuid 3E5D8AA50089
     */
    public short getShort(String columnName) {
        if (columnName == null)
            return 0;
        int columnIdx = fieldNames.indexOf(columnName.toLowerCase());
        return getShort(columnIdx);
    }

    /**
     * @param columnName
     * @return int
     * @roseuid 3E5D8AB803C6
     */
    public int getInt(String columnName) {
        if (columnName == null)
            return 0;
        int columnIdx = fieldNames.indexOf(columnName.toLowerCase());
        return getInt(columnIdx);
    }

    /**
     * @param columnName
     * @return long
     * @roseuid 3E5D8AD60210
     */
    public long getLong(String columnName) {
        if (columnName == null)
            return 0;
        int columnIdx = fieldNames.indexOf(columnName.toLowerCase());
        return getLong(columnIdx);
    }

    /**
     * @param columnName
     * @return float
     * @roseuid 3E5D8AF00286
     */
    public float getFloat(String columnName) {
        if (columnName == null)
            return 0;
        int columnIdx = fieldNames.indexOf(columnName.toLowerCase());
        return getFloat(columnIdx);
    }

    /**
     * @param columnName
     * @return double
     * @roseuid 3E5D8B0300C1
     */
    public double getDouble(String columnName) {
        if (columnName == null)
            return 0;
        int columnIdx = fieldNames.indexOf(columnName.toLowerCase());
        return getDouble(columnIdx);
    }

    /**
     * @param columnName
     * @return char
     * @roseuid 3E5D8B110053
     */
    public char getChar(String columnName) throws Exception {
        if (columnName == null)
            return 0;
        int columnIdx = fieldNames.indexOf(columnName.toLowerCase());
        return getChar(columnIdx);
    }

    /**
     * @param columnName
     * @return String
     * @roseuid 3E5D8B1F0189
     */
    public String getString(String columnName) {
        if (columnName == null)
            return "";
        if (columnName.indexOf(".") != -1) {
            columnName = columnName.split("\\.")[1];
        }
        int columnIdx = fieldNames.indexOf(columnName.toLowerCase());
        return getString(columnIdx);
    }

    /**
     * @param columnName
     * @return java.util.Calendar
     * @roseuid 3E5D8B310121
     */
    public Calendar getCalendar(String columnName) {
        if (columnName == null)
            return null;
        int columnIdx = fieldNames.indexOf(columnName.toLowerCase());
        return getCalendar(columnIdx);
    }

    /**
     * @param columnName
     * @return Object
     * @roseuid 3E5D8B4601A3
     */
    public Object getObject(String columnName) {
        if (columnName == null)
            return null;
        int columnIdx = fieldNames.indexOf(columnName.toLowerCase());
        return getObject(columnIdx);
    }

    /**
     * @param columnName
     * @param type
     * @return Object
     * @roseuid 3E5D8B55035D
     */
    public Object getObject(String columnName, String type) throws Exception {
        if (columnName == null)
            return null;
        int columnIdx = fieldNames.indexOf(columnName.toLowerCase());
        return getObject(columnIdx, type);
    }

    /**
     * @param columnIdx
     * @param obj
     * @roseuid 3E5D8981013E
     */
    public void setObject(int columnIdx, Object obj) {
        if (columnIdx < 0 || columnIdx > fieldCount || currentNo < 0 || currentNo > recordCount)
            return;

        String name = (String) fieldNames.get(columnIdx);
        RecordMetaData rmd = (RecordMetaData) metadata.get(name);
        Record record = (Record) records.get(currentNo);
        record.set(columnIdx, obj);
    }

    /**
     * @param columnName
     * @param obj
     * @roseuid 3E5D8981013E
     */
    public void setObject(String columnName, Object obj) {
        if (columnName == null)
            return;
        int columnIdx = fieldNames.indexOf(columnName.toLowerCase());
        setObject(columnIdx, obj);
    }

    /**
     * ��ü�¼��
     *
     * @return
     */
    public int getRecordCount() {
        return recordCount;
    }

    /**
     * ��õ�ǰ��¼��
     *
     * @return
     */
    public int getCurrentNo() {
        return currentNo;
    }

    /**
     * �����λ����
     *
     * @return
     */
    public int getfieldCount() {
        return fieldCount;
    }

    /**
     * �����ƶ���λ�����
     *
     * @param name
     * @return int
     * @roseuid 3E5D8A2B00D4
     */
    public int findColumn(String name) {
        RecordMetaData rmd = (RecordMetaData) metadata.get(name);
        return rmd.getSeqno();
    }

    /**
     * @roseuid 3E5EABDE02D3
     */
    public void close() {
        records.clear();
        metadata.clear();
        records = null;
        metadata = null;
    }

    private void init(ResultSet rs) {
        if (rs == null)
            return;

        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            for (int i = 1; i <= count; i++) {
                String name = rsmd.getColumnName(i);
                if (name != null)
                    name = name.toLowerCase();
                String typeName = rsmd.getColumnClassName(i);
                String caption = rsmd.getColumnLabel(i);
                RecordMetaData rmd = new RecordMetaData(name, typeName, caption, i);
                metadata.put(name, rmd);
                fieldNames.add(name);
//Debug.debug(name+"="+typeName+"="+caption);
            }
            fieldCount = count;
//Debug.debug(""+rs.getRow());
            while (rs.next()) {
                ArrayList record = new ArrayList();
                for (int i = 1; i <= count; i++) {
                    String name = (String) fieldNames.get(i - 1);
                    RecordMetaData rmd = (RecordMetaData) metadata.get(name);

                    switch (rmd.getType()) {
                        case MetaDataTypes.BIGDECIMAL_TP:
                        case MetaDataTypes.BIGINTEGER_TP:
                            record.add(rs.getBigDecimal(i));
                            break;
                        case MetaDataTypes.BOOLEAN_TP:
                            record.add(new Boolean(rs.getBoolean(i)));
                            break;
                        case MetaDataTypes.BYTE_TP:
                            record.add(new Byte(rs.getByte(i)));
                            break;
                        case MetaDataTypes.CALENDAR_TP:
                            java.sql.Date d = rs.getDate(i);
                            record.add(d);
                            break;
                        case MetaDataTypes.ORACALENDAR_TP://lj add in 20090227
                            java.sql.Date d1 = rs.getDate(i);
                            record.add(d1);
                            break;
                        case MetaDataTypes.CHARACTER_TP:
                            break;
                        case MetaDataTypes.DOUBLE_TP:
                            record.add(new Double(rs.getDouble(i)));
                            break;
                        case MetaDataTypes.FLOAT_TP:
                            record.add(new Float(rs.getFloat(i)));
                            break;
                        case MetaDataTypes.INTEGER_TP:
                            record.add(new Integer(rs.getInt(i)));
                            break;
                        case MetaDataTypes.LONG_TP:
                            record.add(new Long(rs.getLong(i)));
                            break;
                        case MetaDataTypes.SHORT_TP:
                            record.add(new Short(rs.getShort(i)));
                            break;
                        case MetaDataTypes.STRING_TP:
                            record.add(rs.getString(i));
                            break;
                        default:
                            InputStream is = rs.getBinaryStream(i);
                            record.add(is);
                    } //switch
                } // for
//Debug.println("");
                records.add(new Record(record));
                recordCount++;
            } // while
//Debug.debug("ת�����");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            //Debug.debug(sqle.getMessage());
        }
    }

    private void init(ResultSet rs, int resultNo) {
        if (rs == null)
            return;

        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            for (int i = 1; i <= count; i++) {
                String name = rsmd.getColumnName(i);
                if (name != null)
                    name = name.toLowerCase();
                String typeName = rsmd.getColumnClassName(i);
                //System.out.println(name + " ||typeName = " + typeName);
                String caption = rsmd.getColumnLabel(i);
                RecordMetaData rmd = new RecordMetaData(name, typeName, caption, i);
                metadata.put(name, rmd);
                fieldNames.add(name);
            }
            fieldCount = count;

            while (rs.next()) {
                ArrayList record = new ArrayList();
                for (int i = 1; i <= count; i++) {
                    String name = (String) fieldNames.get(i - 1);
                    RecordMetaData rmd = (RecordMetaData) metadata.get(name);
                    //System.out.println(name + " ||rmd.getType() = " + rmd.getType());

                    switch (rmd.getType()) {
                        case MetaDataTypes.BIGDECIMAL_TP:
                        case MetaDataTypes.BIGINTEGER_TP:
                            record.add(rs.getBigDecimal(i));
                            break;
                        case MetaDataTypes.BOOLEAN_TP:
                            record.add(new Boolean(rs.getBoolean(i)));
                            break;
                        case MetaDataTypes.BYTE_TP:
                            record.add(new Byte(rs.getByte(i)));
                            break;
                        case MetaDataTypes.CALENDAR_TP:
                            java.sql.Date d = rs.getDate(i);
                            record.add(d);
                            break;
                        case MetaDataTypes.ORACALENDAR_TP://lj add in 20090227
                            java.sql.Date d1 = rs.getDate(i);
                            record.add(d1);
                            break;
                        case MetaDataTypes.CHARACTER_TP:
                            break;
                        case MetaDataTypes.DOUBLE_TP:
                            record.add(new Double(rs.getDouble(i)));
                            break;
                        case MetaDataTypes.FLOAT_TP:
                            record.add(new Float(rs.getFloat(i)));
                            break;
                        case MetaDataTypes.INTEGER_TP:
                            record.add(new Integer(rs.getInt(i)));
                            break;
                        case MetaDataTypes.LONG_TP:
                            record.add(new Long(rs.getLong(i)));
                            break;
                        case MetaDataTypes.SHORT_TP:
                            record.add(new Short(rs.getShort(i)));
                            break;
                        case MetaDataTypes.STRING_TP:
                            record.add(rs.getString(i));
                            break;
                        default:
                            InputStream is = rs.getBinaryStream(i);
                            record.add(is);
                    } //switch
                } // for

                records.add(new Record(record));
                recordCount++;
                if (recordCount == resultNo) {
                    break;
                }
            } // while
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

}
