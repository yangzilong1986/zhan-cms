//Source file: e:\\java\\zt\\platform\\form\\component\\FormCheckBox.java

package zt.platform.form.component;

import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.form.config.ElementBean;

/**
 * @author ���滻
 * @version 1.0
 * @created 2003��10��11��
 */
public class FormCheckBox extends AbstractFormComponent {

    /**
     * Constructor for the FormCheckBox object
     *
     * @param element Description of the Parameter
     */
    public FormCheckBox(ElementBean element) {
        super(element);
    }


    /**
     * ����FormCheckBox��HTML�ű� ����value��nameset��valueste�������½ű���
     * <td class="page_form_title_td">
     * <p/>
     * </td>
     * <p/>
     * <td class="page_form_td">
     * $headstr <input type="checkbox" name="" value="" checked>$middlestr
     * <input type="checkbox" name="" value="" checked>$middlestr <input
     * type="checkbox" name="" value="" checked>$afterstr
     * </td>
     *
     * @return String
     * @roseuid 3F73AADA02EC
     */
    public String toHTML() {
        StringBuffer sb = new StringBuffer();
        sb.append(getHeader());
        for (int i = 0; i < nameset.length; i++) {
            if (i != 0) {
                sb.append(element.getMiddleStr());
            }
            sb.append(nameset[i] + "<input type=\"checkbox\" name=\"" + element.getName() + "\" value=\"" + valueset[i] + "\" class=\"" + AbstractFormComponent.CSS_PAGE_FORM_CHECKBOX + "\"" + checked(valueset[i]) + otherStr() + ">");
        }
        sb.append(GetFooter());
        return sb.toString();
    }

    private String checked(String value) {
        String[] v = getValues();

        //lj added multicheck in 20090611
        boolean multiflag = false;
        if (v.length > 0)
            if (v[0].indexOf(EnumValue.SPLIT_STR) > 0) multiflag = true;

        for (int i = 0; i < v.length; i++) {
            if (multiflag) {
                String[] vv = v[i].split(EnumValue.SPLIT_STR);
                for (int j = 0; j < vv.length; j++) {
                    if (vv[j].equals(value.trim())) {
                        return " checked";
                    }
                }
            } else if (v[i].equals(value.trim())) {
                return " checked";
            }
        }
        return "";
    }


    /**
     * ��ʼ�� �㷨���£� 1.super(e) 2.����valuetype��valueset��ʼ������nameset��valueset
     *
     * @param e
     * @roseuid 3F7EA09E01E9
     */
    protected void init(ElementBean e) {
    }
}
