package zt.cms.report.notify;

/**
 * <p>Title: �����������֪ͨ����Ϊ�����׼������</p>
 * <p>Description: Ϋ���Ŵ�������Ŀ</p>
 * <p>Copyright: ������Ϣ�������޹�˾ Copyright (c) 2003</p>
 * <p>Company: ������Ϣ�������޹�˾</p>
 * @author YUSG
 * @version 1.0
 */

import java.sql.*;
import javax.servlet.http.*;

import com.zt.util.*;
import zt.cmsi.mydb.*;
import zt.cmsi.pub.define.*;
import zt.platform.db.*;
import zt.platform.utils.Debug;
import com.ming.webreport.*;
import java.util.Date;


public class Notify {
  DatabaseConnection dbc = null; //���ݿ�����
  Connection conn = null; //���ݿ�����
  Statement stmt = null; //���ݿ�����״̬

  String strBmNo = ""; //ҵ����
  String strNotifyNo = ""; //˳���
  String[] date = null; //��õ�ǰ��������
  String strYear = null; //��ǰ���
  String strMonth = null; //��ǰ�·�
  String strDate = null; //��ǰ����
  String strGT1 = " "; //������1
  String strGT2 = " "; //������2
  String errMsg = null; //�������

  public Notify(HttpServletRequest request) {
    String tmpBmNo = (String) request.getAttribute("BMNO");
    String tmpNotifyNo = (String) request.getAttribute("NOTIFNO");
    if (tmpBmNo != null) {
      strBmNo = tmpBmNo.trim();
    }
    if (tmpNotifyNo != null) {
      strNotifyNo = tmpNotifyNo.trim();
    }
  }

  public String getRs() {
    ResultSet rs = null;
    Statement st=null;
    ResultSetMetaData rsmdData=null;
    String reportStr=null;

    StringBuffer strBuf = new StringBuffer();
    if (strNotifyNo == null || strNotifyNo.trim().length() < 1) {
      strBuf.append("select b.bmno,c.contractno,b.clientno,b.clientname,d.contractamt,d.nowbal,d.paydate,d.nowenddate,'1900-01-01' as \"DATE\"");
      strBuf.append(" from bmtable b,bmtableapp c,rqloanlist d ");
      strBuf.append(" where b.bmno=c.bmno and b.bmno=d.bmno and b.bmno='");
      strBuf.append(this.strBmNo);
      strBuf.append("'");
    }
    else {
      strBuf.append("select a.bmno,c.contractno,b.clientno,b.clientname,d.contractamt,d.nowbal,d.paydate,d.nowenddate,a.\"DATE\"");
      strBuf.append(" from bmilnotifi a,bmtable b,bmtableapp c,rqloanlist d ");
      strBuf.append(" where a.bmno=b.bmno and a.bmno=c.bmno and a.bmno=d.bmno and a.bmno='");
      strBuf.append(this.strBmNo);
      strBuf.append("' ");
      strBuf.append(" and a.notifno=");
      strBuf.append(this.strNotifyNo);
    }
    String strSql = strBuf.toString();
    Debug.debug(strSql);

    try {
      dbc = MyDB.getInstance().apGetConn();
      conn = dbc.getConnection();
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
            if(i==9 || i==8 || i==7)
            {
              date = rs.getDate(i);
            }
            else
              strValue=DBUtil.fromDB(rs.getString(i));

            if(strValue==null)
              strValue=" ";
            else
              strValue=strValue.trim();

            if(i==9 || i==8 || i==7)
              rec.setValue(strName,date);
            else
              rec.setValue(strName,strValue);
          }
          mrds.addRow(rec);
        }
        String strRoot=PropertyManager.getProperty("root");
        MREngine engine = new MREngine();
        engine.addMRDataSet("rs5", mrds);//�󶨻�õ�����
        engine.bind("MyReport",strRoot+"DueBill/Notify.mrf");//�þ���·���󶨱���
        reportStr = engine.createViewer("MRViewer");
    }
    catch (Exception e)
    {
      errMsg = "��ѯ������Ϣʧ�ܣ�";
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

  public void getGuarantor() {
    String strGuar = null;
    ResultSet rs = null;
    Statement st =null;
    StringBuffer strBuf = new StringBuffer();
    strBuf.append("select clientname from bmguarantor");
    strBuf.append(" where bmno='");
    strBuf.append(this.strBmNo);
    strBuf.append("'");
    String strSql = strBuf.toString();
    Debug.debug(strSql);

    try {
      dbc = MyDB.getInstance().apGetConn();
      conn = dbc.getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(strSql);
      while (rs.next()) {
        String strGuarName = DBUtil.fromDB(rs.getString(1));
        //�жϵ����˵����ƣ����Ϊ�ղ�����
        if (strGuarName != null) {
          strGuarName = strGuarName.trim();
          //�жϵ����˵�һ�еĳ��ȣ����̫���ڵڶ���д
          if (this.strGT1.length() < 30) {
            this.strGT1 += strGuarName;
            this.strGT1 += ";";
          }
          else {
            this.strGT2 += strGuarName;
            this.strGT2 += ";";
          }
        }
      }
    }
    catch (Exception e) {
      errMsg = "��ѯ������ʧ�ܣ�";
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
  }

  public String getGT1() {
    return this.strGT1;
  }

  public String getGT2() {
    return this.strGT2;
  }

  public void closeDB() {
    MyDB.getInstance().apReleaseConn(1);
  }

  public void setYMD() {
    String strYMD = SystemDate.getSystemDate2();
    if (strYMD != null) {
      date = strYMD.split("-");
      this.strYear = date[0];
      this.strMonth = date[1];
      this.strDate = date[2];
    }
    else {
      errMsg = "��õ�ǰ�Ĵ�ӡʱ��ʧ�ܣ�";
    }
  }

  public String getYear() {
    if (this.strYear == null) {
      this.setYMD();
    }
    return this.strYear;
  }

  public String getMonth() {
    if (this.strMonth == null) {
      this.setYMD();
    }
    return this.strMonth;
  }

  public String getDate() {
    if (this.strDate == null) {
      this.setYMD();
    }
    return this.strDate;
  }

  public String getErrMsg() {
    return this.errMsg;
  }

  public String getRoot() {
    String strRoot = PropertyManager.getProperty("root"); //��ñ���ľ���·��
    return strRoot;
  }
}
