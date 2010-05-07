package zt.cms.report.notify;

/**
 * <p>Title: 信贷管理</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004 中天信息技术有限公司</p>
 * <p>Company: 中天信息技术有限公司</p>
 * @author Yusg
 * @version 1.0
 */

import java.sql.*;
import com.ming.webreport.*;
import zt.platform.db.*;
import javax.servlet.http.*;
import com.zt.util.*;
import zt.cmsi.mydb.*;
import zt.platform.utils.Debug;
import java.util.HashMap;
import java.util.Date;
/*******************************************************************
 * function : 催收通知书的批量打印
 * parameter: brhid
 *            begindate
 *            enddate
 * return   :
 ******************************************************************/
public class PNotify
{
    DatabaseConnection dbc = null; //数据库连接
    Connection conn = null;        //数据库连接
    Statement stmt = null;         //数据库连接状态

    String strBrhid=null;          //网点编号
    String strBegindate=null;      //开始日期
    String strEnddate=null;        //结束日期
    public String errMsg = null;          //错误代码
    HashMap hmap=new HashMap();    //

    public PNotify(HttpServletRequest request)
    {
      strBrhid = (String)request.getParameter("brhid");
      strBegindate = (String)request.getParameter("bdate");
      strEnddate = (String)request.getParameter("edate");
      if(strBrhid==null)
        this.errMsg="无法获得网点编号";
      if(strBegindate==null || strBegindate.trim().length()!=8)
        this.errMsg="开始日期时间格式有误";
      else
        strBegindate=DBUtil.toSqlDate(strBegindate);
      if(strEnddate==null || strEnddate.trim().length()!=8)
        this.errMsg="结束日期时间格式有误";
      else
        strEnddate=DBUtil.toSqlDate(strEnddate);
    }
    /*************************************************************
     *
     *
     ************************************************************/
    public String getRS()
    {
      String Preportstr=null;
      String strBmno=null;
      ResultSet rs = null;
      Statement st=null;
      ResultSetMetaData rsmdData=null;

      this.setGuarantor();//

      StringBuffer strBuf=new StringBuffer();
      strBuf.append("select bmno,clientname,contractamt,nowbal,paydate,nowenddate,contractno");
      strBuf.append(" from rqloanlist where brhid='");
      strBuf.append(strBrhid);
      strBuf.append("' and nowenddate between ");
      strBuf.append(strBegindate);
      strBuf.append(" and ");
      strBuf.append(strEnddate);

      String strSql=strBuf.toString();
      Debug.debug(strSql);

      try
      {
        dbc = MyDB.getInstance().apGetConn();
        conn = dbc.getConnection();
        st = conn.createStatement();
        rs = st.executeQuery(strSql);

        rsmdData=rs.getMetaData();
        MRDataSet mrds=new MRDataSet();
        while(rs.next())//
        {
           DataRecord rec = new DataRecord();
           for(int i=1;i<=7;i++)
          {
            Date date=null;
            String strValue=null;
            String strName=rsmdData.getColumnLabel(i);
            //System.out.print("strName:"+strName);
            if(i==1)
              strBmno=rs.getString(i).trim();
            if(i==5 || i==6)
            {
              date = rs.getDate(i);
            }
            else if(i==7)//处理担保人情况
            {
              strValue = (String) hmap.get(strBmno);
              hmap.remove(strBmno);
            }
            else
              strValue=DBUtil.fromDB(rs.getString(i));

            if(strValue==null)
              strValue=" ";
            else
              strValue=strValue.trim();

            if(i==5 || i==6)
            {
              rec.setValue(strName, date);
            }
            else
              rec.setValue(strName,strValue);
          }
          mrds.addRow(rec);
        }
        String strRoot=PropertyManager.getProperty("root");
        MREngine engine = new MREngine();
        engine.addMRDataSet("rs9", mrds);//绑定获得的数据
        engine.bind("MyReport",strRoot+"DueBill/PNotify.mrf");//用绝对路径绑定报表
        Preportstr = engine.createViewer("MRViewer");

      }
      catch (Exception e)
      {
        errMsg = "查询贷款信息失败！";
        Debug.debug(e);
      }
      finally
      {
        MyDB.getInstance().apReleaseConn(0);
        try
        {
          if (st != null)
            st.close();
        }
        catch (Exception e)
        {
          Debug.debug(e);
        }
      }


      return Preportstr;
    }

    /*************************************************************
     *function :获得保证人
     *parameter:bmno
     * return  :hmap 保证人的map
     ************************************************************/
    public void setGuarantor()
    {
      String strGuar = null;
      ResultSet rs = null;
      Statement st =null;

      String strBmno=getBmNO();
      StringBuffer strBuf = new StringBuffer();
      strBuf.append("select bmno,clientname from bmguarantor");
      strBuf.append(" where bmno in");
      strBuf.append(strBmno);
      strBuf.append(" order by bmno");

      String strSql = strBuf.toString();
      Debug.debug(strSql);
      try
      {
        dbc = MyDB.getInstance().apGetConn();
        conn = dbc.getConnection();
        st = conn.createStatement();
        rs = st.executeQuery(strSql);
        String Obmno="F";
        String strGr="F";
        while(rs.next())
        {
          String Nbmno=rs.getString(1);
          String clientname=DBUtil.fromDB(rs.getString(2));
          if(clientname==null || clientname.equals(""))
            clientname="";
          else
            clientname=clientname.trim()+";";

          if(!Obmno.equals(Nbmno))
          {
            hmap.put(Obmno.trim(), strGr);
            Obmno=Nbmno;
            strGr=clientname;
          }
          else
          {
            strGr += clientname;
          }
          //System.out.println(strGr);
        }
        hmap.put(Obmno.trim(), strGr);
        hmap.remove("F");//删除初始化内容
      }
      catch (Exception e)
      {
        errMsg = "查询保证人信息失败！";
        Debug.debug(e);
      }
      finally
      {
        MyDB.getInstance().apReleaseConn(0);
        try
        {
          if (st != null)
            st.close();
        }
        catch (Exception e)
        {
          Debug.debug(e);
        }
      }
    }
    /*************************************************************
     *function :获得贷款的中有担保人的贷款编号
     *parameter: brhid       全局变量
     *           begindate   全局变量
     *           enddate     全局变量
     * return  :有担保人的贷款编号的字符串
     ************************************************************/
    public String getBmNO()
    {
        ResultSet rs = null;
        Statement st=null;
        ResultSetMetaData rsmdData=null;

        StringBuffer strBufBmno=new StringBuffer();
        strBufBmno.append("(");

        StringBuffer strBuf=new StringBuffer();
        strBuf.append("select bmno ");
        strBuf.append("from rqloanlist where brhid='");
        strBuf.append(strBrhid);
        strBuf.append("' and nowenddate between ");
        strBuf.append(strBegindate);
        strBuf.append(" and ");
        strBuf.append(strEnddate);

        String strSql=strBuf.toString();
        Debug.debug(strSql);

        try
        {
          dbc = MyDB.getInstance().apGetConn();
          conn = dbc.getConnection();
          st = conn.createStatement();
          rs = st.executeQuery(strSql);
          while(rs.next())
          {
             String bmno=rs.getString(1);
             if(bmno!=null)
             {
               strBufBmno.append("'");
               strBufBmno.append(bmno.trim());
               strBufBmno.append("',");
             }
          }
        }
        catch (Exception e)
        {
          errMsg = "查询贷款担保人失败！";
          Debug.debug(e);
        }
        finally
        {
          MyDB.getInstance().apReleaseConn(0);
          try
          {
            if (st != null)
              st.close();
          }
          catch (Exception e)
          {
            Debug.debug(e);
          }
        }
        strBufBmno.append("'')");
        return strBufBmno.toString();
    }

    public String getErrMsg()
    {
      return this.errMsg;
    }
}