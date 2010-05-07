//Source file: e:\\java\\zt\\platform\\form\\config\\FieldBean.java

package zt.platform.form.config;

/**
 * �������������ݿ���ֶεĶ��� ÿ�����Եĺ�����ο�Word Document:����ƽ̨�������.doc Notes:
 * �����ÿ�����Ե�getter��setter����
 *
 * @author �����Ա���滻
 * @version 1.0
 * @created 2003��10��8��
 */
public class FieldBean implements Comparable {

    /**
     * �ֶ�����
     *
     * @author sun
     * @since 2003��10��8��
     */
    private String name;

    /**
     * �ֶε�˳��
     *
     * @author sun
     * @since 2003��10��8��
     */
    private int seqno;

    /**
     * �����������
     *
     * @author sun
     * @since 2003��10��8��
     */
    private String tblname;

    /**
     * �Ƿ������
     *
     * @author sun
     * @since 2003��10��8��
     */
    private boolean isPrimary;

    /**
     *  �ֶε�����,����ֵ����գ�DataType�࣬����ö������ʱ�� ����enutpname��ʶ�˾����ö������
     */

    /**
     * ���ֶ�������ö������ʱ��������ö����������
     *
     * @author sun
     * @since 2003��10��8��
     */
    private String enutpname;

    /**
     * �Ƿ�Search�ֶΣ������γ�Search�������
     *
     * @author sun
     * @since 2003��10��8��
     */
    private boolean isSearch;

    /**
     * �ֶγ���
     *
     * @author sun
     * @since 2003��10��8��
     */
    private int length;

    /**
     * �Ƿ�����Ϊ��
     *
     * @author sun
     * @since 2003��10��8��
     */
    private boolean isnull;

    /**
     * �Ƿ������ظ�
     *
     * @author sun
     * @since 2003��10��8��
     */
    private boolean isrepeat;

    /**
     * ������ʾ���棬title
     *
     * @author sun
     * @since 2003��10��8��
     */
    private String comment;

    /**
     * �ֶε�����
     *
     * @author sun
     * @since 2003��10��8��
     */
    private String description;

    /**
     * ȱʡֵ
     *
     * @author sun
     * @since 2003��10��8��
     */
    private String defaultValue;

    /**
     * ����
     *
     * @author sun
     * @since 2003��10��8��
     */
    private int precision;

    /**
     * С��λ����������С����
     *
     * @author sun
     * @since 2003��10��8��
     */
    private int decimals;

    /**
     * ����ο��ı���
     *
     * @author sun
     * @since 2003��10��8��
     */
    private String reftbl;

    /**
     * �ο�ע���ֶ�����
     *
     * @author sun
     * @since 2003��10��8��
     */
    private String refnamefld;

    /**
     * �ο�ֵ�ֶ�����
     *
     * @author sun
     * @since 2003��10��8��
     */
    private String refvaluefld;

    /**
     * �ο��Ŀ�ѡ��Where��
     *
     * @author sun
     * @since 2003��10��8��
     */
    private String refWhere;
    private String caption;
    private int datatype;

    /**
     * Description of the Field
     *
     * @author sun
     * @since 2003��10��11��
     */
    public final static int DATA_TYPE_STRING = 1;
    /**
     * Description of the Field
     *
     * @author sun
     * @since 2003��10��11��
     */
    public final static int DATA_TYPE_INTEGER = 2;
    /**
     * Description of the Field
     *
     * @author sun
     * @since 2003��10��11��
     */
    public final static int DATA_TYPE_DECIMAL = 3;
    /**
     * Description of the Field
     *
     * @author sun
     * @since 2003��10��11��
     */
    public final static int DATA_TYPE_BOOLEAN = 4;
    /**
     * Description of the Field
     *
     * @author sun
     * @since 2003��10��11��
     */
    public final static int DATA_TYPE_DATE = 5;
    /**
     * Description of the Field
     *
     * @author sun
     * @since 2003��10��11��
     */
    public final static int DATA_TYPE_ENUMERATION = 6;
    private boolean needEncode;


    /**
     * ���ֶε���Ž��бȽ� <0 С�� =0 ���� >0 ����
     *
     * @param o
     * @return int
     * @roseuid 3F7160100166
     */
    public int compareTo(Object o) {
        int no = this.seqno - ((FieldBean) o).seqno;
        return this.seqno - ((FieldBean) o).seqno;
    }


    /**
     * У����ֶε�ֵ�Ƿ�Ϸ� 1���Ƿ�����Ϊ�գ�isnull�� 2������У��(datatype) 3������У��(length)
     * 4��С��λ��У��(precision��decimals) 5��ȡֵ��ΧУ��(ö�����͵�ȡֵ��Χ)
     *
     * @param value
     * @return boolean
     * @roseuid 3F7262DB0254
     */
    public boolean validate(Object value) {
        return true;
    }


    /**
     * Gets the comment attribute of the FieldBean object
     *
     * @return The comment value
     */
    public String getComment() {
        return comment;
    }


    /**
     * Sets the comment attribute of the FieldBean object
     *
     * @param comment The new comment value
     */
    public void setComment(String comment) {
        this.comment = checkString(comment);
    }


    /**
     * Gets the datatype attribute of the FieldBean object
     *
     * @return The datatype value
     */
    public int getDatatype() {
        return datatype;
    }


    /**
     * Sets the datatype attribute of the FieldBean object
     *
     * @param datatype The new datatype value
     */
    public void setDatatype(int datatype) {
        this.datatype = datatype;
    }


    /**
     * Gets the decimals attribute of the FieldBean object
     *
     * @return The decimals value
     */
    public int getDecimals() {
        return decimals;
    }


    /**
     * Gets the defaultValue attribute of the FieldBean object
     *
     * @return The defaultValue value
     */
    public String getDefaultValue() {
        return checkString(defaultValue);
    }


    /**
     * Gets the description attribute of the FieldBean object
     *
     * @return The description value
     */
    public String getDescription() {
        return checkString(description);
    }


    /**
     * Gets the enutpname attribute of the FieldBean object
     *
     * @return The enutpname value
     */
    public String getEnutpname() {
        return checkString(enutpname);
    }


    /**
     * Gets the isnull attribute of the FieldBean object
     *
     * @return The isnull value
     */
    public boolean isIsnull() {
        return isnull;
    }


    /**
     * Gets the isPrimary attribute of the FieldBean object
     *
     * @return The isPrimary value
     */
    public boolean isIsPrimary() {
        return isPrimary;
    }


    /**
     * Gets the isrepeat attribute of the FieldBean object
     *
     * @return The isrepeat value
     */
    public boolean isIsrepeat() {
        return isrepeat;
    }


    /**
     * Gets the isSearch attribute of the FieldBean object
     *
     * @return The isSearch value
     */
    public boolean isIsSearch() {
        return isSearch;
    }


    /**
     * Gets the length attribute of the FieldBean object
     *
     * @return The length value
     */
    public int getLength() {
        return length;
    }


    /**
     * Gets the name attribute of the FieldBean object
     *
     * @return The name value
     */
    public String getName() {
        return checkString(name);
    }


    /**
     * Gets the precision attribute of the FieldBean object
     *
     * @return The precision value
     */
    public int getPrecision() {
        return precision;
    }


    /**
     * Gets the refnamefld attribute of the FieldBean object
     *
     * @return The refnamefld value
     */
    public String getRefnamefld() {
        return checkString(refnamefld);
    }


    /**
     * Gets the refvaluefld attribute of the FieldBean object
     *
     * @return The refvaluefld value
     */
    public String getRefvaluefld() {
        return checkString(refvaluefld);
    }


    /**
     * Gets the reftbl attribute of the FieldBean object
     *
     * @return The reftbl value
     */
    public String getReftbl() {
        return checkString(reftbl);
    }


    /**
     * Gets the refWhere attribute of the FieldBean object
     *
     * @return The refWhere value
     */
    public String getRefWhere() {
        return checkString(refWhere);
    }


    /**
     * Gets the seqno attribute of the FieldBean object
     *
     * @return The seqno value
     */
    public int getSeqno() {
        return seqno;
    }


    /**
     * Gets the tblname attribute of the FieldBean object
     *
     * @return The tblname value
     */
    public String getTblname() {
        return checkString(tblname);
    }


    /**
     * Sets the decimals attribute of the FieldBean object
     *
     * @param decimals The new decimals value
     */
    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }


    /**
     * Sets the defaultValue attribute of the FieldBean object
     *
     * @param defaultValue The new defaultValue value
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = checkString(defaultValue);
    }


    /**
     * Sets the description attribute of the FieldBean object
     *
     * @param description The new description value
     */
    public void setDescription(String description) {
        this.description = checkString(description);
    }


    /**
     * Sets the enutpname attribute of the FieldBean object
     *
     * @param enutpname The new enutpname value
     */
    public void setEnutpname(String enutpname) {
        this.enutpname = checkString(enutpname);
    }


    /**
     * Sets the isnull attribute of the FieldBean object
     *
     * @param isnull The new isnull value
     */
    public void setIsnull(boolean isnull) {
        this.isnull = isnull;
    }


    /**
     * Sets the isPrimary attribute of the FieldBean object
     *
     * @param isPrimary The new isPrimary value
     */
    public void setIsPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }


    /**
     * Sets the isrepeat attribute of the FieldBean object
     *
     * @param isrepeat The new isrepeat value
     */
    public void setIsrepeat(boolean isrepeat) {
        this.isrepeat = isrepeat;
    }


    /**
     * Sets the isSearch attribute of the FieldBean object
     *
     * @param isSearch The new isSearch value
     */
    public void setIsSearch(boolean isSearch) {
        this.isSearch = isSearch;
    }


    /**
     * Sets the length attribute of the FieldBean object
     *
     * @param length The new length value
     */
    public void setLength(int length) {
        this.length = length;
    }


    /**
     * Sets the name attribute of the FieldBean object
     *
     * @param name The new name value
     */
    public void setName(String name) {
        this.name = checkString(name);
    }


    /**
     * Sets the precision attribute of the FieldBean object
     *
     * @param precision The new precision value
     */
    public void setPrecision(int precision) {
        this.precision = precision;
    }


    /**
     * Sets the refnamefld attribute of the FieldBean object
     *
     * @param refnamefld The new refnamefld value
     */
    public void setRefnamefld(String refnamefld) {
        this.refnamefld = checkString(refnamefld);
    }


    /**
     * Sets the reftbl attribute of the FieldBean object
     *
     * @param reftbl The new reftbl value
     */
    public void setReftbl(String reftbl) {
        this.reftbl = checkString(reftbl);
    }


    /**
     * Sets the refvaluefld attribute of the FieldBean object
     *
     * @param refvaluefld The new refvaluefld value
     */
    public void setRefvaluefld(String refvaluefld) {
        this.refvaluefld = checkString(refvaluefld);
    }


    /**
     * Sets the refWhere attribute of the FieldBean object
     *
     * @param refWhere The new refWhere value
     */
    public void setRefWhere(String refWhere) {
        this.refWhere = checkString(refWhere);
    }


    /**
     * Sets the seqno attribute of the FieldBean object
     *
     * @param seqno The new seqno value
     */
    public void setSeqno(int seqno) {
        this.seqno = seqno;
    }


    /**
     * Sets the tblname attribute of the FieldBean object
     *
     * @param tblname The new tblname value
     */
    public void setTblname(String tblname) {
        this.tblname = checkString(tblname);
    }


    /**
     * Gets the caption attribute of the FieldBean object
     *
     * @return The caption value
     */
    public String getCaption() {
        return checkString(caption);
    }


    /**
     * Sets the caption attribute of the FieldBean object
     *
     * @param caption The new caption value
     */
    public void setCaption(String caption) {
        this.caption = checkString(caption);
    }


    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public String toString() {
        String text = ":" + seqno + ":" + name + ":" + this.isPrimary + ":" + datatype
                + ":" + this.isSearch + ":" + length + ":" + isnull + ":" + isrepeat + ":" + caption
                + ":" + description + ":" + this.defaultValue + ":" + precision + ":" + this.decimals + ":" +
                reftbl + ":" + this.refnamefld + ":" + this.refvaluefld + ":" + this.refWhere + "#####enuname:" + getEnutpname();
        return text;
    }

    public boolean isNeedEncode() {
        return needEncode;
    }

    public void setNeedEncode(boolean needEncode) {
        this.needEncode = needEncode;
    }

    public String checkString(String key) {
        if (key == null) {
            return "";
        } else {
            return key.trim();
        }
    }
}
