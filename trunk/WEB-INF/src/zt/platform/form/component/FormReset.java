//Source file: e:\\java\\zt\\platform\\form\\component\\FormReset.java

package zt.platform.form.component;

import zt.platform.form.config.ElementBean;

/**
 * ���ð�ť���
 *
 * @author ���滻
 * @version 1.0
 */
public class FormReset
        extends AbstractFormComponent {

    public FormReset(ElementBean element) {
        super(element);
    }

    /**
     * �γ������ַ�����
     * <input type="reset" name="" value="" ����>
     *
     * @return String
     * @roseuid 3F73AADB02CF
     */
    public String toHTML() {
        return "<input type=\"reset\" name=\"" + element.getName() + "\" value=\"" + element.getCaption() + "\" class=\"" + AbstractFormComponent.CSS_PAGE_FORM_RESET + "\">";
    }
}
