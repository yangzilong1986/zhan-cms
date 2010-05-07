//Source file: e:\\java\\zt\\platform\\form\\component\\FormHidden.java

package zt.platform.form.component;

import zt.platform.form.config.ElementBean;

/**
 * Hidden���
 *
 * @author ���滻
 * @version 1.0
 */
public class FormHidden extends AbstractFormComponent {

    public FormHidden(ElementBean element) {
        super(element);
    }

    /**
     * ���������ַ�����
     *
     * @return String
     * @roseuid 3F73AADA024B
     */
    public String toHTML() {
        return "<input type=\"hidden\" name=\"" + element.getName() + "\" value=\"" + getValues()[0] + "\" />";
    }
}
