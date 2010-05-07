//Source file: e:\\java\\zt\\platform\\form\\component\\FormTextArea.java

package zt.platform.form.component;

import zt.platform.form.config.ElementBean;
import zt.platform.form.config.FieldBean;

/**
 * TextArea���
 *
 * @author ���滻
 * @version 1.0
 * @created 2003��10��11��
 */
public class FormTextArea extends AbstractFormComponent {
    /**
     * Constructor for the FormTextArea object
     *
     * @param element Description of the Parameter
     */
    public FormTextArea(ElementBean element) {
        super(element);
    }


    /**
     * ���������ַ�����
     * <td class="page_form_title_td">
     * <p/>
     * </td>
     * <p/>
     * <td class="page_form_td">
     * <input type="textarea" name="" value="" ����>
     *
     * @return String
     * @roseuid 3F73AADA01B5
     */
    public String toHTML() {
        String cols = "";
        String rows = "";
        if (element.getCols() <= 0) {
        } else {
            cols = " cols=\"" + element.getCols() + "\"";
        }

        if (element.getRows() <= 0) {
        } else {
            rows = " rows=\"" + element.getRows() + "\"";
        }
        return getHeader() + "<textarea name=\"" + element.getName() + "\"" + cols + rows + " class=\"" + CSS_PAGE_FORM_TEXTFIELD + "\"" + otherStr() + conditions() + maxLength() + ">" + getValues()[0] + "</textarea>" + GetFooter();
    }


    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public String conditions() {
        String mayNull = "";
        String minLength = "";
        String dataType = "";
        String errInfo = "";
        String precision = "";
        String decimalDigits = "";
        if (element.isIsnull()) {
            mayNull = " mayNull=\"1\"";
        } else {
            mayNull = " mayNull=\"0\"";
            if (element.getMinLength() != 0) {
                minLength = " minLength=\"" + element.getMinLength() + "\"";
            }
        }

        dataType = " dataType=\"" + element.getDataType() + "\"";
        errInfo = " errInfo=\"" + element.getCaption() + "\"";

        if (element.getDataType() == FieldBean.DATA_TYPE_DECIMAL) {
            precision = " precision=\"" + element.getPrecision() + "\"";
            decimalDigits = " decimalDigits=\"" + element.getDecimalDigits() + "\"";
        }

        return mayNull + minLength + dataType + errInfo + precision + decimalDigits;
    }


    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public String maxLength() {
        String maxLength = "";
        if (element.getMaxLength() != 0) {
            maxLength = " maxlength=\"" + element.getMaxLength() + "\"";
        }
        return maxLength;
    }

}
