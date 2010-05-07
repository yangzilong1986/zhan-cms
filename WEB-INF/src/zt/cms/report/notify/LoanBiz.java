package zt.cms.report.notify;

/**
 * <p>Title: 信贷管理系统</p>
 * <p>Description: 潍坊信贷</p>
 * <p>Copyright: Copyright (c) 2003  中天信息技术有限公司</p>
 * <p>Company: 中天信息技术有限公司</p>
 * @author Yusg
 * @date   2004/4/26
*  @modify 2004/12/27   yusg     编号取合同号改为借据号
 * @version 1.0
 */

import zt.cmsi.mydb.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import zt.platform.db.DatabaseConnection;
import zt.cmsi.mydb.MyDB;
import java.sql.Connection;
import zt.cmsi.pub.define.SystemDate;
import javax.servlet.http.HttpServletRequest;
import com.zt.util.PropertyManager;
import zt.platform.utils.Debug;
import com.ming.webreport.*;
import zt.platform.db.DBUtil;
import java.util.Date;

/*******************************************
 *
 *    所有贷款种类的借款凭证的数据来源
 *        为明宇报表准备数据
 *
 *******************************************/

public class LoanBiz
{
    //DatabaseConnection dbc = null; //数据库连接
    //Connection conn = null;        //数据库连接
    Statement stmt = null;         //数据库连接状态

    String strLoanType=null;       //业务类型
    String strBmNo="";             //业务编号
    String strIfAdv="NO";          //是否发生垫款
    String[] date=null;            //获得当前的年月日
    String strYear=null;           //当前年份
    String strMonth=null;          //当前月份
    String strDate=null;           //当前日期
    String errMsg=null;            //错误代码

    //实例LoanBiz  获得数据库连接
    public LoanBiz(HttpServletRequest request)
    {
        String tmpBmNo=(String)request.getAttribute("BMNO");
        String tmpIfAdv=(String)request.getAttribute("IFADV");//承兑和贴现

        if(tmpBmNo!=null)
            this.strBmNo=tmpBmNo.trim();
        else
            errMsg="业务代码获得失败！";
        //处理承兑，贴现，转贴现的承兑和贴现
        if(tmpIfAdv!=null)
            this.strIfAdv=tmpIfAdv;
    }

    //获得凭证需要的数据(除了承兑汇票)
    public String getRS()
    {
      DatabaseConnection dc = null;
      Connection conn = null;          //
      ResultSet rs = null;             //
      ResultSetMetaData rsmdData=null; //
      String reportStr=null;
      Statement st = null;

      try
      {
        dc = MyDB.getInstance().apGetConn();
        conn = dc.getConnection();

        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select (select sname from scbranch where brhid=b.brhid),");
        strBuf.append("a.cnlno as contractno,");
        strBuf.append("b.clientname,(select address from cmclient where clientno=b.clientno),");
        strBuf.append("(select enudt from ptenuminfodetl where enuid='BMType' and enutp=b.typeno),");
        strBuf.append("a.loanpurpose,a.finalrate,a.finalamt,a.finalstartdate,a.finalenddate,a.bmno,");
        strBuf.append("(select accno from bmcontract where bmno=a.bmno) ");
        strBuf.append(" from bmtableapp a,bmtable b");
        strBuf.append(" where a.bmno=b.bmno and a.bmno='");
        strBuf.append(this.strBmNo);
        strBuf.append("'");

        String strSql = strBuf.toString();
        Debug.debug(Debug.TYPE_SQL,strSql);

        st = conn.createStatement();
        rs = st.executeQuery(strSql);
        rsmdData=rs.getMetaData();
        int iCount=rsmdData.getColumnCount();

        MRDataSet mrds=new MRDataSet();
        while(rs.next())
        {
          DataRecord rec = new DataRecord();
          //rec.setValue(label[0][m], data[i][m]);
          for(int i=1;i<=iCount;i++)
          {
            Date date=null;
            String strValue=null;
            String strName=rsmdData.getColumnLabel(i);
            if(i==9 || i==10)
            {
              date = rs.getDate(i);
            }
            else
              strValue=DBUtil.fromDB(rs.getString(i));

            if(strValue==null)
              strValue=" ";
            else
              strValue=strValue.trim();

            if(i==9 || i==10)
              rec.setValue(strName,date);
            else
              rec.setValue(strName,strValue);
          }
          mrds.addRow(rec);
        }

        String strRoot=PropertyManager.getProperty("root");
        MREngine engine = new MREngine();
        engine.addMRDataSet("rs6", mrds);//绑定获得的数据
        engine.bind("MyReport",strRoot+"DueBill/LoanBiz.mrf");//用绝对路径绑定报表
        reportStr = engine.createViewer("MRViewer");
      }
      catch(Exception e)
      {
        errMsg="发生错误！";
        Debug.debug(e);
      }
      finally
      {
        MyDB.getInstance().apReleaseConn(0);
        try
        {
          if (st != null)
            st.close();
        }catch(Exception e) {Debug.debug(e);}
      }
      return reportStr;
    }
    //承兑汇票的数据
    public String getRSPO()
    {
      DatabaseConnection dc = null;
      Connection conn = null;
      ResultSet rs = null;
      Statement st = null;
      ResultSetMetaData rsmdData=null; //
      String reportStr=null;

      try {
        dc = MyDB.getInstance().apGetConn();
        conn = dc.getConnection();

        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select (select sname from scbranch where brhid=b.brhid),b.clientname,c.appbank,c.appbankacc,");
        strBuf.append("c.payee,c.payeebank,c.payeebankacc,c.marginamt,a.finalamt,a.finalstartdate,a.finalenddate");
        strBuf.append(" from bmtableapp a,bmtable b,bmappacptbill c");
        strBuf.append(" where a.bmno=b.bmno and a.bmno=c.bmno and a.bmno='");
        strBuf.append(this.strBmNo);
        strBuf.append("'");

        String strSql = strBuf.toString();
        Debug.debug(Debug.TYPE_SQL,strSql);
        st = conn.createStatement();
        rs = st.executeQuery(strSql);

        rsmdData=rs.getMetaData();
        int iCount=rsmdData.getColumnCount();
        MRDataSet mrds=new MRDataSet();
        while(rs.next())
        {
          DataRecord rec = new DataRecord();
          //rec.setValue(label[0][m], data[i][m]);
          for(int i=1;i<=iCount;i++)
          {
            Date date=null;
            String strValue=null;
            String strName=rsmdData.getColumnLabel(i);
            if(i==10 || i==11)
            {
              date = rs.getDate(i);
            }
            else
              strValue=DBUtil.fromDB(rs.getString(i));

            if(strValue==null)
              strValue=" ";
            else
              strValue=strValue.trim();

            if(i==10 || i==11)
              rec.setValue(strName,date);
            else
              rec.setValue(strName,strValue);
          }
          mrds.addRow(rec);
        }

        String strRoot=PropertyManager.getProperty("root");
        MREngine engine = new MREngine();
        engine.addMRDataSet("rs8", mrds);//绑定获得的数据
        engine.bind("MyReport",strRoot+"DueBill/LoanPO.mrf");//用绝对路径绑定报表
        reportStr = engine.createViewer("MRViewer");

      }
      catch(Exception e)
      {
        errMsg="发生错误！";
        Debug.debug(e);
      }
      finally
      {
        MyDB.getInstance().apReleaseConn(0);
        try
        {
          if (st != null)
            st.close();
        }catch(Exception e) {Debug.debug(e);}
      }
      return reportStr;
    }

    //判断贷款类型是否承兑、贴现、转贴现三种类型，是表头打印－票据授权书
    //否表头打印－借款凭证
    public String getHead()
    {
      DatabaseConnection dc = null;
      Connection conn = null;
      ResultSet rs = null;
      Statement st = null;
      String strHead=null;
      String strLoanType=null;

      try {
        dc = MyDB.getInstance().apGetConn();
        conn = dc.getConnection();

        String strSql = "select typeno from bmtable where bmno='" + this.strBmNo +
            "'";

        st = conn.createStatement();
        rs = st.executeQuery(strSql);
        if (rs.next()) {
          strLoanType = rs.getString(1).trim();

          if (this.strIfAdv.equals("NO") &&
              (strLoanType.equals("13") || strLoanType.equals("14"))) {
            strHead = "票 据 授 权 书";
          }
          else {
            strHead = "借 款 凭 证";
          }
        }
        else
          strHead = "借 款 凭 证";
      }
      catch (Exception e) {
        Debug.debug(e);
      }
      finally {
        MyDB.getInstance().apReleaseConn(0);
        try {
          if (st != null)
            st.close();
        }
        catch (Exception e) {
          Debug.debug(e);
        }
        return strHead;
      }

    }

    //Close and release database connection
//    public void closeDB()
//    {
//        MyDB.getInstance().apReleaseConn(1);
//    }

    //Get the current system Time
    public void setYMD()
    {
        String strYMD=SystemDate.getSystemDate2();
        if(strYMD!=null)
        {
            date = strYMD.split("-");
            this.strYear=date[0];
            this.strMonth=date[1];
            this.strDate=date[2];
        }
        else
          errMsg="获得当前的打印时间失败！";
    }

    //get Year
    public String getYear()
    {
        if(this.strYear==null)
            this.setYMD();
        return this.strYear;
    }
    //get Month
    public String getMonth()
    {
        if(this.strMonth==null)
            this.setYMD();
        return this.strMonth;
    }
    //get Day
    public String getDate()
    {
        if(this.strDate==null)
            this.setYMD();
        return this.strDate;
    }
    //get Errer Message
    public String getErrMsg()
    {
        return this.errMsg;
    }
    //get Ming-report root
    public String getRoot()
    {
        String strRoot=PropertyManager.getProperty("root");//获得报表的绝对路径
        return strRoot;
    }

}
