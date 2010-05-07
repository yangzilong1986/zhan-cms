/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////               数据库对象类源码               //////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*自动生成数据库持久性对象
**   作者：李伟
**   生成日期：2004-2-19 16:48:00
*/
package zt.cms.report;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class RepParamObject {
    private String strSqlString;
    private String strBmNo;
    private String strBindFile;
    private String strPrintDate;
    public RepParamObject(String strSqlString1,
                          String strBmNo1,
                          String strBindFile1,
                          String strPrintDate1)
    {
        this.strSqlString=strSqlString1;
        this.strBmNo=strBmNo1;
        this.strBindFile = strBindFile1;
        this.strPrintDate = strPrintDate1;
    }
    public RepParamObject(){}

    public String getSqlString()
    {
        return strSqlString;
    }
    public String getBmNo()
    {
        return strBmNo;
    }
    public String getBindFile()
    {
        return strBindFile;
    }
    public String getPrintDate()
    {
        return strPrintDate;
    }

    public void setSqlString(String strsqlstring){
        strSqlString = strsqlstring;
    }
    public void setBmNo(String strbmno){
        strBmNo = strbmno;
    }
    public void setBindFile(String strbindfile){
        strBindFile = strbindfile;
    }
    public void setPrintDate(String strprintdate){
        strPrintDate = strprintdate;
    }

}
