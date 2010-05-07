//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////               数据库对象类源码               //////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*自动生成数据库持久性对象
**   作者：李伟
**   生成日期：2004-2-6 20:03:10
*/
package zt.cms.report.util;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class InactInfoObject {
    private String strTransferDate;
    private String strAdminedBy;
    private String strLastNotifyDate;
    private String strPenaltyDate;
    private String strPenaltyRule;
    private String strPenalty;
    public InactInfoObject(String strTransferDate1,
                          String strAdminedBy1,
                          String strLastNotifyDate1,
                          String strPenaltyDate1,
                          String strPenaltyRule1,
                          String strPenalty1)
    {
        this.strTransferDate=strTransferDate1;
        this.strAdminedBy=strAdminedBy1;
        this.strLastNotifyDate=strLastNotifyDate1;
        this.strPenaltyDate=strPenaltyDate1;
        this.strPenaltyRule=strPenaltyRule1;
        this.strPenalty=strPenalty1;
    }
    public String getTransferDate()
    {
        return strTransferDate;
    }
    public String getAdminedBy()
    {
        return strAdminedBy;
    }
    public String getLastNotifyDate()
    {
        return strLastNotifyDate;
    }
    public String getPenaltyDate()
    {
        return strPenaltyDate;
    }
    public String getPenaltyRule()
    {
        return strPenaltyRule;
    }
    public String getPenalty()
    {
        return strPenalty;
    }
}
