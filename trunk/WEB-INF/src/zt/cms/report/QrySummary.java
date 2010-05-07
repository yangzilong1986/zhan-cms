/**
 * <p/>===============================================
 * <p/>Title: ����̨���ۺϲ�ѯ����
 * <p/>===============================================
 * <p/>Description: ����̨���ۺϲ�ѯ��
 * @version  $Revision: 1.3 $  $Date: 2007/05/28 09:30:08 $
 * @author   weiyb
 * <p/>�޸ģ�$Author: weiyb $     
*/

package zt.cms.report;

import javax.sql.rowset.CachedRowSet;
import zt.platform.cachedb.DB2_81;
import  zt.platform.db.DBUtil;

public class QrySummary {
    /**
     * ����弶������̬LoanCat1
     * @return String
     * @throws Exception
     */
    public String getLoanCat1()throws Exception
    {
    	CachedRowSet rs = null; 
        StringBuffer strBuf = new StringBuffer();
        String ret="";
        strBuf.append("select enutp,enudt from ptenuminfodetl where enuid='LoanCat1'");
        rs = DB2_81.getRs(strBuf.toString());
        while (rs.next())
        {
        	ret+="<option value='"+rs.getString("enutp")+"'>"+DBUtil.fromDB(rs.getString("enudt"))+"</option>";
        }
        return ret;
    }
    /**
     * ����ļ�������̬LoanCat2
     * @return String
     * @throws Exception
     */
    public String getLoanCat2()throws Exception
    {
        CachedRowSet rs = null;  //ptenuminfodetl �����

        StringBuffer strBuf = new StringBuffer();
        String ret="";
        strBuf.append("select enutp,enudt from ptenuminfodetl where enuid='LoanCat2'");
        rs = DB2_81.getRs(strBuf.toString());
        while(rs.next())
        {
        	ret+="<option value='"+rs.getString("enutp")+"'>"+DBUtil.fromDB(rs.getString("enudt"))+"</option>";
        }
        return ret;
    }
    /**
     * ��ȡ��Ŀ�б�
     * @return String
     * @throws Exception
     */
    public String getAccList()throws Exception
    {
    	String sql="select AccNo,AccName from SCHostAcc where ACCSTATUS='0' and acctp='3' and BALTYPE='1' order by AccNo";
        CachedRowSet rs = null;              //ptenuminfodetl �����

        String ret="";
        rs = DB2_81.getRs(sql);
        while(rs.next())
        {
        	String accNo=rs.getString("AccNo").trim();
        	if (accNo.length()==4)
        		accNo+="---";
        	else if(accNo.length()==3)
        		accNo+="----";
        	else
        		accNo+="--";
        	ret+="<option value='"+rs.getString("AccNo").trim()+"'>"+accNo+DBUtil.fromDB(rs.getString("AccName"))+"</option>";
        }
        return ret;
    }
    /**
     * ��ȡҵ�������б�
     * @return String
     * @throws Exception
     */
    public String getType()throws Exception
    {
    	String sql="select typeno,typename from BMType";
        CachedRowSet rs = null;              //ptenuminfodetl �����

        String ret="";
        rs = DB2_81.getRs(sql);
        while(rs.next())
        {
        	ret+="<option value='"+rs.getString("typeno")+"'>"+DBUtil.fromDB(rs.getString("typename"))+"</option>";
        }
        return ret;
    }
    /**
     * ��ȡ������;�б�
     * @return String
     * @throws Exception
     */
    public String getLoanCat3()throws Exception
    {
    	String sql="select enutp,enudt from ptenuminfodetl where enuid='LoanCat3'";
        CachedRowSet rs = null;              //ptenuminfodetl �����

        String ret="";
        rs = DB2_81.getRs(sql);
        while(rs.next())
        {
        	ret+="<option value='"+rs.getString("enutp")+"'>"+DBUtil.fromDB(rs.getString("enudt"))+"</option>";
        }
        return ret;
    }
    public static void main(String args[])
    {
    	System.out.println("1234".substring(0, "1234".indexOf("34")+"34".length()));
    }

    
}
