//Source file: e:\\java\\zt\\platform\\form\\component\\FormRadio.java

package zt.platform.form.component;

import zt.platform.form.config.ElementBean;

/**
 * @author ���滻
 * @version 1.0
 */
public class FormRadio extends AbstractFormComponent {

    public FormRadio(ElementBean element) {
        super(element);
    }

    /**
     * ����FormRadio��HTML�ű�
     * <p/>
     * ����value��nameset��valueste�������½ű���
     * <td class="page_form_title_td">
     * </td>
     * <td class="page_form_td">
     * $headstr
     * <input type="radio" name="" value="" checked>$middlestr
     * <input type="radio" name="" value="" checked>$middlestr
     * <input type="radio" name="" value="" checked>$afterstr
     * </td>
     *
     * @return String
     * @roseuid 3F73AADA0382
     */
    public String toHTML() {
        StringBuffer sb = new StringBuffer();
        sb.append(getHeader());
        for (int i = 0; i < nameset.length; i++) {
            if (i != 0) {
                sb.append(element.getMiddleStr());
            }
            sb.append("<input type=\"radio\" name=\"" + element.getName() + "\" value=\"" + valueset[i] + "\" class=\"" + CSS_PAGE_FORM_RADIO + "\"" + checked(valueset[i]) + "" + otherStr() + ">" + nameset[i]);
        }
        sb.append(GetFooter());
        return sb.toString();
    }

    public String checked(String value) {
        String v[] = getValues();
        if (v[0].equals(value.trim())) {
            return " checked";
        }
        return "";
    }

    /**
     * ��ʼ��
     * <p/>
     * �㷨���£�
     * 1.super(e)
     * 2.����valuetype��valueset��ʼ������nameset��valueset
     *
     * @param e
     * @roseuid 3F7EA1480143
     */
    protected void init(ElementBean e) {

    }
}
