//Source file: e:\\java\\zt\\platform\\form\\component\\FormLabel.java

package zt.platform.form.component;

import zt.platform.form.config.ElementBean;

/**
 * Label���
 *
 * @author ���滻
 * @version 1.0
 */
public class FormLabel
        extends AbstractFormComponent {
    public FormLabel(ElementBean element) {
        super(element);
    }

    /**
     * �γ����µ��ַ���
     * <p/>
     * <label>value</label>
     *
     * @return String
     * @roseuid 3F73AADB003A
     */
    public String toHTML() {
        return "<label class=\"" + AbstractFormComponent.CSS_PAGE_FORM_LABEL + "\">" + element.getCaption() + "</label>";
    }
}
