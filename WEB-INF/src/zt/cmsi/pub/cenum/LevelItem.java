package zt.cmsi.pub.cenum;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class LevelItem {
    private String itemNo = null;
    private String itemLabel = null;

    public LevelItem(String itemno, String itemlabel) {
        this.itemNo = itemno;
        this.itemLabel = itemlabel;
    }

    public String getItemNo() {
        return this.itemNo;
    }

    public String getItemLabel() {
        return this.itemLabel;
    }

}
