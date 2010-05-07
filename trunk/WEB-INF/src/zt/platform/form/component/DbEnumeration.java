//Source file: e:\\java\\zt\\platform\\form\\component\\DbEnumeration.java

package zt.platform.form.component;

import zt.platform.form.config.ElementBean;
import zt.platform.form.config.EnumerationBean;
import zt.platform.form.config.EnumerationType;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Enumeration����List�ı��֣���������ֵ���Ǹ���enuname��EnumerationType�и���enuna
 * me��ȡ
 *
 * @author ���滻
 * @version 1.0
 */
public class DbEnumeration extends AbstractFormComponent {
    Logger logger = Logger.getLogger("zt.platform.form.component.DbEnumeration");

    private String enuname;

    public DbEnumeration(ElementBean element) {
        super(element);
    }


    /**
     * ����enutpname��EnumerationType�л��ö��ʵ�����γ�
     * <td class="page_form_title_td"></td>
     * <td class="page_form_td">
     * <select����>
     * ����
     * <option value="">name</option>
     * <option value="" selected >name</option>
     * ����
     * </select>
     * <td>
     * ���ַ���
     *
     * @return String
     * @roseuid 3F73AADA0101
     */
    public String toHTML() {
        EnumerationBean enu = EnumerationType.getEnu(element.getEnutpname());
        StringBuffer sb = new StringBuffer();

        sb.append(getHeader());

        //zhanrui
        String cssStr = null;
        if (isReadonly()) {
            cssStr = CSS_PAGE_FORM_SELECT_READONLY;
        } else {
            cssStr = CSS_PAGE_FORM_SELECT;
        }

        sb.append("<select name=\"" + element.getName() + "\" class=\"" + cssStr +
                "\"" + otherStr() + ">");
        if (enu != null) {
            for (Iterator iter = enu.getKeys().iterator(); iter.hasNext();) {
                Object key = (Object) iter.next();
                Object value = enu.getValue(key);
                if (isReadonly()) {
                    if (isSelected(key + "")) {
                        sb.append("<option value=\"" + key + "\" selected >" + value +
                                "</option>");
                    }
                } else
                    sb.append("<option value=\"" + key + "\"" + this.selected(key + "") + ">" + value +
                            "</option>");
            }
        } else {
            logger.severe("The enumeration type with name " + element.getEnutpname() + " is null");
        }
        sb.append("</select>");
        sb.append(GetFooter());
        return sb.toString();
    }

    public String toHTML(Collection filters) {
        EnumerationBean enu = EnumerationType.getEnu(element.getEnutpname());
        StringBuffer sb = new StringBuffer();

        //zhanrui
        String cssStr = null;
        if (isReadonly()) {
            cssStr = CSS_PAGE_FORM_SELECT_READONLY;
        } else {
            cssStr = CSS_PAGE_FORM_SELECT;
        }


        sb.append(getHeader());
        sb.append("<select name=\"" + element.getName() + "\" class=\"" + cssStr +
                "\"" + otherStr() + ">");
        if (enu != null) {
            for (Iterator iter = enu.getKeys().iterator(); iter.hasNext();) {
                Object key = (Object) iter.next();
                if (filters.contains(key)) {
                    Object value = enu.getValue(key);
                    if (isReadonly()) {
                        if (isSelected(key + "")) {
                            sb.append("<option value=\"" + key +
                                    "\" selected >" + value +
                                    "</option>");
                        }
                    } else
                        sb.append("<option value=\"" + key + "\"" +
                                this.selected(key + "") + ">" + value +
                                "</option>");
                }
            }
        } else {
            logger.severe("The enumeration type with name " + element.getEnutpname() + " is null");
        }
        sb.append("</select>");
        sb.append(GetFooter());
        return sb.toString();
    }


    private String selected(String value) {
        String[] v = getValues();
        if (v != null) {
            for (int i = 0; i < v.length; i++) {
                if (v[i] != null) {
                    if (v[i].equals(value)) {
                        return " selected";
                    }
                }
            }
            return "";
        } else {
            return "";
        }
    }

    private boolean isSelected(String value) {
        String[] v = getValues();
        if (v != null) {
            for (int i = 0; i < v.length; i++) {
                if (v[i] != null) {
                    if (v[i].equals(value)) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

}
