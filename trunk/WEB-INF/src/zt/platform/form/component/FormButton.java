//Source file: e:\\java\\zt\\platform\\form\\component\\FormButton.java

package zt.platform.form.component;

import zt.platform.form.config.ElementBean;

/**
 * ��ť���
 *
 * @author ���滻
 * @version 1.0
 * @created 2003��10��11��
 */
public class FormButton extends AbstractFormComponent {
    /**
     * Constructor for the FormButton object
     *
     * @param element Description of the Parameter
     */
    public FormButton(ElementBean element) {
        super(element);
    }


    /**
     * �������µ��ַ��� <input type="button" name="" value="" onclick="" ����>
     *
     * @return String
     * @roseuid 3F73AADB0166
     */
    public String toHTML() {
        if (isReadonly() || element.isDisabled()) {
            return "<input type=\"button\" name=\"" + element.getName() +
                    "\" value=\"" + element.getCaption() + "\" onclick=\"" +
                    element.getOnclick() + "\" class=\"" +
                    AbstractFormComponent.CSS_PAGE_FORM_BUTTON + "\" disabled>";
        } else {
            return "<input type=\"button\" name=\"" + element.getName() +
                    "\" value=\"" + element.getCaption() + "\" onclick=\"" +
                    element.getOnclick() + "\" class=\"" +
                    AbstractFormComponent.CSS_PAGE_FORM_BUTTON + "\">";
        }
    }
}
