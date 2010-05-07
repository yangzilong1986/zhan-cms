//Source file: e:\\java\\zt\\platform\\form\\component\\FormJavaScript.java

package zt.platform.form.component;

import zt.platform.form.config.ElementBean;

/**
 * JavaScript�ű����
 *
 * @author ���滻
 * @version 1.0
 * @created 2003��10��11��
 */
public class FormJavaScript extends AbstractFormComponent {
    /**
     * Constructor for the FormJavaScript object
     *
     * @param element  Description of the Parameter
     */
    private ElementBean element;

    public FormJavaScript(ElementBean element) {
        super(element);
        this.element = element;
    }


    /**
     * ����value���������ַ���: <script src="" type="text/javascript" ></script>
     *
     * @return String
     * @roseuid 3F73AADB00D0
     */
    public String toHTML() {
        return "<script src=\"" + ctx.getUrl(element.getDefaultValue()) + "\" type=\"text/javascript\" ></script>";
    }
}
