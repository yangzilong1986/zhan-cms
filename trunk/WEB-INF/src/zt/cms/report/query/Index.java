package zt.cms.report.query;

/**
 * <p>Title: �Ŵ�����ϵͳ</p>
 * <p>Description: ������Ϣ�������޹�˾Ϋ��ũ��</p>
 * <p>Copyright: ������Ϣ�������޹�˾ Copyright (c) 2003</p>
 * <p>Company: ������Ϣ�������޹�˾</p>
 * @author Yusg
 * @version 1.0
 */
import zt.platform.db.DatabaseConnection;
import zt.cmsi.mydb.MyDB;
import zt.platform.db.DBUtil;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
/*********************************************
 *
 *   ���ɲ�ѯ��query.jsp��report.jsp����������
 *
 ********************************************/

public class Index
{
    DatabaseConnection dbc = null;         //���ݿ�����
    Connection conn = null;                //���ݿ�����
    Statement stmt = null;                 //���ݿ�����״̬

    private String errMsg=null;            //�������
    private String strType=null;           //��������  1--��ѯ    2--����
    public Index()
    {
        try {
            dbc = MyDB.getInstance().apGetConn();
            conn = dbc.getConnection();
        }
        catch (Exception e) {
            errMsg="���ݿ�����ʧ�ܣ�";
        }
    }

    public ArrayList getQuery()//��ò�ѯ������  1
    {
        this.strType="0";
        return this.getRS();
    }

    public ArrayList getReport()//��ñ��������  2
    {
        this.strType="1";
        return this.getRS();
    }

    public ArrayList getRS()//ͨ��--������ݵķ���
    {
        ArrayList arrList=new ArrayList();    //���ݼ���

        StringBuffer strBuf=new StringBuffer();
        strBuf.append("select rptname,rptclass,rptprog,rptform,listitmno");
        strBuf.append(" from sctblname ");
        if(this.strType.equals("0"))
            strBuf.append(" where rpttype=0");
        else
            strBuf.append("where rpttype > 0                ");
        strBuf.append(" order by rptclass,dispseq");
        System.out.println(strBuf.toString());
        try{
            stmt=conn.createStatement();
            ResultSet rs = stmt.executeQuery(strBuf.toString());
            while (rs.next())
            {
                IndexData iData=new IndexData();      //ֵ����
                iData.rptName = DBUtil.fromDB(rs.getString(1));
                iData.rptClass = rs.getInt(2);
                iData.rptProg = rs.getString(3);
                iData.rptForm = rs.getString(4).trim();
                iData.listItmNo = DBUtil.fromDB(rs.getString(5));

                arrList.add(iData);
            }
        }
        catch(SQLException se)
        {
            this.errMsg="��ѯ��������ʧ�ܣ�";
        }
        finally
        {
            MyDB.getInstance().apReleaseConn(1);
        }
        return arrList;
    }
    public String getErrMsg()
    {
        return this.errMsg;
    }
}