//Source file: e:\\java\\zt\\platform\\user\\MenuItemBean.java

package zt.platform.user;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * 菜单项
 *
 * @author WangHaiLei
 * @version 1.3
 *          最后修改日期：2003-10-28-13:43
 */
public class MenuItemBean
        implements Serializable {
    private static Logger logger = Logger.getLogger("zt.platform.user.MenuItemBean");
    /**
     * The nodeid for this node.
     */
    private String menuItemId = null;

    public String getMenuItemId() {
        return (this.menuItemId);
    }

    /**
     * Defines a isBranch attribute
     */

    private String isLeaf = null;

    public String getIsLeaf() {
        return (this.isLeaf);
    }

    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }

    /**
     * Defines a label attribute
     */
    private String label = null;

    public String getLabel() {
        return (this.label);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Defines a url attribute
     */
    public String url = null;

    public String getUrl() {
        return (this.url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////// Constructors

    /**
     * Constructor: Creates an empty Node object
     */
    public MenuItemBean() {
    }

    public MenuItemBean(String nodeid) {
        super();
        this.menuItemId = nodeid;
    }

    public MenuItemBean(String menuItemId, String label, String isLeaf, String url) {
        super();
        this.menuItemId = menuItemId;
        this.label = label;
        this.isLeaf = isLeaf;
        this.url = url;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Return a String representation of this object.
     */
    public String convertToString() throws Exception {

        // 下面注释掉的部分代码，是Flash Tree版本的。
//          StringBuffer sb = new StringBuffer("<node label=\"");
//          sb.append(label);
//          sb.append("\"");
//
//          if(!isLeaf.equals("")) {
//               String isBranch = null;
//               if(isLeaf.equals("0")) {
//                    isBranch = "false";
//               }
//               if(isLeaf.equals("1")) {
//                    isBranch = "true";
//               }
//               sb.append(" isBranch=\"");
//               sb.append(isBranch);
//               sb.append("\"");
//          }
//
//          sb.append(" url=\"");
//          sb.append(url);
//          sb.append("\"");
//          sb.append(">");

        // 下面的代码，是JavaScript Tree版本的。
        StringBuffer sb = new StringBuffer("<tree text=\"");
        sb.append(label);
        sb.append("\"");
        if (!url.equals("")) {
            sb.append(" action=\"");
            try {
                sb.append(specialCodeTranslate(url));
            }
            catch (Exception ex) {
                logger.severe("Wrong at MenuItemBean.convertToString : [ " + ex + " ] ");
            }
            sb.append("\"");
        }
        sb.append(">");

        return (sb.toString());
    }

    /**
     * ///////////////////////////////////////////////////////////////////////////////////////////////////////////
     */

    private String specialCodeTranslate(String codeToBeTrans) {

        String codeAfterTrans = "";

        // 先转换&；后转换其他的符号。
        codeAfterTrans = codeToBeTrans.replaceAll("&", "&amp;");

        codeAfterTrans = codeAfterTrans.replaceAll("<", "&lt;");
        codeAfterTrans = codeAfterTrans.replaceAll(">", "&gt");

        // 先转换双引号；后转换单引号。
        codeAfterTrans = codeAfterTrans.replaceAll("\"", "&quot;"); //
        codeAfterTrans = codeAfterTrans.replaceAll("'", "&apos;");

        return codeAfterTrans;
    }
}
