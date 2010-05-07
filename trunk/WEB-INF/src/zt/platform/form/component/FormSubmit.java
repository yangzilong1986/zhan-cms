//Source file: e:\\java\\zt\\platform\\form\\component\\FormSubmit.java

package zt.platform.form.component;

import zt.platform.form.config.ElementBean;

/**
 * �ύ��ť���
 *
 * @author ���滻
 * @version 1.0
 */
public class FormSubmit
        extends AbstractFormComponent {
    public FormSubmit(ElementBean element) {
        super(element);
    }

    /**
     * ���������ַ���:
     * <p/>
     * <input type="submit" name="" value="" ����>
     *
     * @return String
     * @roseuid 3F73AADB0211
     */
    public String toHTML() {
        if (isReadonly() || element.isDisabled()) {
            return "<input type=\"button\" name=\"" + element.getName() +
                    "\" value=\"" + element.getCaption() + "\" onclick=\"" + element.getOnclick() + "\" class=\"" +
                    AbstractFormComponent.CSS_PAGE_FORM_SUBMIT + "\" disabled>";
        } else {
            return "<input type=\"button\" name=\"" + element.getName() +
                    "\" value=\"" + element.getCaption() + "\" onclick=\"" + element.getOnclick() + "\" class=\"" +
                    AbstractFormComponent.CSS_PAGE_FORM_SUBMIT + "\">";
        }
    }
}