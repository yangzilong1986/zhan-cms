//Source file: e:\\java\\zt\\platform\\form\\config\\EnumerationBean.java

package zt.platform.form.config;

import java.util.*;

/**
 * ö��
 *
 * @author ���滻
 * @version 1.0
 */
public class EnumerationBean {

    /**
     * ö����Ԫ��
     * Map��key-value�ṹ
     * key  ����Ӧö�ٵ�ֵ
     * value����Ӧö�ٵ�ע��
     */
    private Map enums;

    private Vector keys = new Vector();

    /**
     * ö�ٵ�����
     */
    private String name;
    private String enudesc;

    /**
     * @param name
     * @param enu
     * @roseuid 3F7CF5DA020B
     */
//    public EnumerationBean(String name, Map enu) {
//
//    }

    /**
     * @param name
     * @roseuid 3F71680F0368
     */
    public EnumerationBean(String name) {
        enums = new HashMap();
        this.name = name;
    }

    /**
     * ����ö����Ԫ��
     * @param enu
     * @roseuid 3F7167FF0094
     */
//    public void setEnu(Map enu) {
//
//    }

    /**
     * �õ�ö�ٵ�ֵ��
     *
     * @return Map
     * @roseuid 3F716723026E
     */
    public Map getEnu() {
        return enums;
    }

    /**
     * ����һ��ö����Ԫ
     *
     * @param key
     * @param value
     * @roseuid 3F7167C80135
     */
    public void add(Object key, Object value) {
        this.keys.add(key);
        this.enums.put(key, value);
    }

    /**
     * �õ�ö�ٵ�����
     *
     * @return String
     * @roseuid 3F71686A0065
     */
    public String getName() {
        return this.name;
    }

    public String getEnudesc() {
        return enudesc;
    }

    public void setEnudesc(String enudesc) {
        this.enudesc = enudesc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue(Object key) {
        return enums.get(key);
    }

    public String toString() {
        String text = ":" + this.getName() + ":" + this.getEnudesc() + ":?";
        Iterator keys = this.enums.keySet().iterator();
        while (keys.hasNext()) {
            Object key = keys.next();
            text = text + "?" + key + "?" + this.enums.get(key) + "?";
        }
        return text;
    }

    public Collection getKeys() {
        return keys;
    }

}
