//Source file: e:\\java\\zt\\platform\\form\\config\\TableBean.java

package zt.platform.form.config;

import java.util.*;

/**
 * ����Ϣ �����˱�����ơ����͡��ֶΡ���������ѯ�ֶ���Ϣ
 *
 * @author ���滻
 * @version 1.0
 * @created 2003��10��8��
 */
public class TableBean {

    /**
     * ������
     *
     * @author sun
     * @since 2003��10��8��
     */
    private String name;

    /**
     * ������ 0����̬�� 1����̬��
     *
     * @author sun
     * @since 2003��10��8��
     */
    private int type;

    /**
     * ��Ŷ�̬���¼�ı�����
     *
     * @author sun
     * @since 2003��10��8��
     */
    private String dynamictbl;

    /**
     * �ֶμ��� ���ֶ����ƣ��ֶ�ʵ��ֵ�Եķ�ʽ���
     *
     * @author sun
     * @since 2003��10��8��
     */
    private Map fields;

    /**
     * ������������ ���ʼ��ʱ�������ֶεĶ�����Ϣ��isPrimary��ֵ����ʼ��������
     *
     * @author sun
     * @since 2003��10��8��
     */
    private String[] primaryKey = new String[0];

    /**
     * ��ѯ�ֶ��������� ���ʼ��ʱ�������ֶεĶ�����Ϣ��isSearch��ֵ����ʼ��������
     *
     * @author sun
     * @since 2003��10��8��
     */
    private String[] searchKey = new String[0];

    private String description;

    /**
     * Description of the Field
     *
     * @author sun
     * @since 2003��10��8��
     */
    public final static int STATIC_TABLE = 0;
    /**
     * Description of the Field
     *
     * @author sun
     * @since 2003��10��8��
     */
    public final static int DYNAMIC_TYPE = 1;


    /**
     * @param name
     * @roseuid 3F7162780021
     */
    public TableBean(String name) {
        fields = new HashMap();
        this.name = name;
    }

    /**
     *@roseuid 3F7162560285
     */
//    public TableBean(String name, Map fields) {
//
//    }

    /**
     * ȡ�ò�ѯ���ֶ� ����ֵField[],�����ǰ����ֶ���������
     *
     * @return zt.platform.form.config.FieldBean[]
     * @roseuid 3F7161E20224
     */
    public FieldBean[] getSearchKey() {
        FieldBean[] fs = new FieldBean[searchKey.length];
        for (int i = 0; i < this.searchKey.length; i++) {
            fs[i] = (FieldBean) this.fields.get(searchKey[i]);
        }
        return fs;
    }


    /**
     * ȡ�������ֶ� ����ֵField[],�����ǰ����ֶ���������
     *
     * @return zt.platform.form.config.FieldBean[]
     * @roseuid 3F716206004F
     */
    public FieldBean[] getPrimaryKey() {
        FieldBean[] fs = new FieldBean[primaryKey.length];
        for (int i = 0; i < this.primaryKey.length; i++) {
            fs[i] = (FieldBean) this.fields.get(primaryKey[i]);
        }
        return fs;
    }


    /**
     * ȡ��ָ�����Ƶ��ֶ�
     *
     * @param name
     * @return zt.platform.form.config.FieldBean
     * @roseuid 3F7162230277
     */
    public FieldBean getField(String name) {
        return (FieldBean) this.fields.get(name);
    }


    /**
     * ȡ�����е��ֶ� ����ֵField[],�����ǰ����ֶ���������
     *
     * @return zt.platform.form.config.FieldBean[]
     * @roseuid 3F71623101AF
     */
    public FieldBean[] getFields() {
        Object[] values = this.fields.values().toArray();
        Arrays.sort(values);
        FieldBean fs[] = new FieldBean[values.length];
        for (int i = 0; i < values.length; i++) {
            fs[i] = (FieldBean) values[i];
        }
        return fs;
    }


    /**
     * @return String
     * @roseuid 3F71625001DC
     */
    public String getName() {
        return this.name;
    }


    /**
     * @return boolean
     * @roseuid 3F78FC9F0005
     */
    public boolean isDynamic() {
        if (this.getType() == this.DYNAMIC_TYPE) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * @return boolean
     * @roseuid 3F78FCAC016C
     */
    public boolean isStatic() {
        if (this.getType() == this.DYNAMIC_TYPE) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * @return String
     * @roseuid 3F7CDBA002BB
     */
    public String getDynamicTbl() {
        return this.dynamictbl;
    }


    /**
     * @param name
     * @roseuid 3F7CDBAC02FE
     */
    public void setDynamicTbl(String name) {
        this.dynamictbl = name;
    }


    /**
     * @param field
     * @roseuid 3F78FCC8020D
     */
    public void addField(FieldBean field) {
        this.fields.put(field.getName(), field);
        if (field.isIsPrimary()) {
            this.makePrimary(field);
        }
        if (field.isIsSearch()) {
            this.makeSearch(field);
        }
    }


    /**
     * Description of the Method
     *
     * @param field Description of the Parameter
     */
    private void makePrimary(FieldBean field) {
        Collection keys = new Vector();
        for (int i = 0; i < this.primaryKey.length; i++) {
            keys.add(primaryKey[i]);
        }
        keys.add(field.getName());
        this.primaryKey = this.convertToStrings(keys.toArray());
    }


    /**
     * Description of the Method
     *
     * @param field Description of the Parameter
     */
    private void makeSearch(FieldBean field) {
        Collection keys = new Vector();
        for (int i = 0; i < this.searchKey.length; i++) {
            keys.add(searchKey[i]);
        }
        keys.add(field.getName());
        this.searchKey = this.convertToStrings(keys.toArray());
    }


    /**
     * Gets the description attribute of the TableBean object
     *
     * @return The description value
     */
    public String getDescription() {
        return description;
    }


    /**
     * Sets the description attribute of the TableBean object
     *
     * @param description The new description value
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Gets the type attribute of the TableBean object
     *
     * @return The type value
     */
    public int getType() {
        return type;
    }


    /**
     * Sets the type attribute of the TableBean object
     *
     * @param type The new type value
     */
    public void setType(int type) {
        this.type = type;
    }


    /**
     * Description of the Method
     *
     * @param os Description of the Parameter
     * @return Description of the Return Value
     */
    private String[] convertToStrings(Object[] os) {
        String[] keys = new String[os.length];
        for (int i = 0; i < os.length; i++) {
            keys[i] = (String) os[i];
        }
        return keys;
    }
}
