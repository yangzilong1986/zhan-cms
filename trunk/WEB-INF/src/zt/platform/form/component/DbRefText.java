package zt.platform.form.component;

/**
 *  用来表示带有外键参考的字段
 *
 *@author 请替换
 *@created 2003年11月21日
 *@version 1.0
 */

import zt.cms.pub.SCBranch;
import zt.cms.pub.SCUser;
import zt.platform.form.config.ElementBean;
import zt.platform.form.config.FieldBean;
import zt.platform.form.util.SessionAttributes;
import zt.platform.form.util.event.EventType;

public class DbRefText extends AbstractFormComponent {

    public final static String REF_FILE_NAME = "/templates/ref.jsp";
    private String ref;

    public DbRefText(ElementBean element) {
        super(element);
    }

    public String toHTML() {
        StringBuffer sb = new StringBuffer();
        sb.append(getHeader());
        String refTbl = element.getRefTbl();
        if (refTbl == null) return "";
        refTbl = refTbl.trim().toLowerCase();
        if (refTbl.equals("scuser") || refTbl.equals("scbranch")) {
            //if(refTbl.equals("scbranch")){
            sb.append("<input type=\"text\" name=\"" + element.getName() + "_D\" value=\"" + getRefValue_D(refTbl, getValues()[0]) + "\" class=\"" + AbstractFormComponent.CSS_PAGE_FORM_TEXT + "\" " + getConditions_D());
            sb.append("<input type=\"hidden\" name=\"" + element.getName() + "\" value=\"" + getValues()[0] + "\" class=\"" + AbstractFormComponent.CSS_PAGE_FORM_TEXT + "\"" + sizeAndMaxLength() + otherStr() + "" + conditions() + ">");
        }
//    else if(refTbl.equals("scuser"))
//    {
//      sb.append("<input type=\"hidden\" name=\"" + element.getName() + "_D\" value=\"\" minLength=\"0\" maxlength=\"10000\" mayNull=\"1\" dataType=\"1\" readonly=\"true\">");
//      sb.append("<input type=\"text\" name=\"" + element.getName() + "\" title=\""+ getRefValue_D(refTbl,getValues()[0]) + "\" value=\"" + getValues()[0] + "\" class=\"" + AbstractFormComponent.CSS_PAGE_FORM_TEXT + "\"" + sizeAndMaxLength() + otherStr() + "" + conditions() + ">");
//    }
        else {
            sb.append("<input type=\"hidden\" name=\"" + element.getName() + "_D\" value=\"\" minLength=\"0\" maxlength=\"10000\" mayNull=\"1\" dataType=\"1\" readonly=\"true\">");
            sb.append("<input type=\"text\" name=\"" + element.getName() + "\" value=\"" + getValues()[0] + "\" class=\"" + AbstractFormComponent.CSS_PAGE_FORM_TEXT + "\"" + sizeAndMaxLength() + otherStr() + "" + conditions() + ">");
        }
        sb.append(element.getMiddleStr());
        sb.append("<input type=\"button\" name=\"nameref\" value=\"…\" onclick=\"window.open(&quot;" +
                getCtx().getUrl(REF_FILE_NAME) + "?" + SessionAttributes.REQUEST_INSATNCE_ID_NAME +
                "=" + getInstanceId() + "&" + SessionAttributes.REQUEST_EVENT_ID_NAME +
                "=" + EventType.REFERENCE_FIELD_EVENT_TYPE +
                "&reference_field=" + element.getName() + "&quot;,&quot;FIREF" + element.getName() + getInstanceId() + "&quot;,&quot;height=350,width=460,toolbar=no,scrollbars=yes&quot;);\" class=\"" +
                AbstractFormComponent.CSS_PAGE_FORM_REFBUTTON + "\"" + disabled() + ">");
        sb.append(GetFooter());
        return sb.toString();
    }

    public String getConditions_D() {
        String size = "";
        if (element.getSize() != 0) {
            size = " size=\"" + element.getSize() + "\"";
        }
        size += " minLength=\"0\"";
        size += " maxlength=\"10000\"";
        if (element.isIsnull()) {
            size += " mayNull=\"1\"";
        } else {
            size += " mayNull=\"0\"";
        }
        size += " dataType=\"1\"";
        size += " errInfo=\"" + element.getCaption() + "\"";
        size += " readonly=\"true\">";
        return size;
    }

    public String sizeAndMaxLength() {
        String size = "";
        String maxLength = "";
        if (element.getSize() != 0) {
            size = " size=\"" + element.getSize() + "\"";
        }
        if (element.getMaxLength() != 0) {
            maxLength = " maxlength=\"" + element.getMaxLength() + "\"";
        }
        return size + maxLength;
    }

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

    private String disabled() {
        if (isReadonly() || element.isReadonly()) {
            return " disabled";
        }
        return "";
    }

    private String getRefValue_D(String p_refTbl, String p_value) {
        if (p_value == null || p_value.trim().length() < 1) return "";
        if (p_refTbl.equals("scuser")) {
            return SCUser.getName(p_value);
        }
        if (p_refTbl.equals("scbranch")) {
            return SCBranch.getLName(p_value);
        }
        return "";
    }
}
