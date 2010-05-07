package zt.cms.report.query;

/**
 * <p>Title: 信贷管理系统</p>
 * <p>Description: 中天信息技术有限公司潍坊农信</p>
 * <p>Copyright: 中天信息技术有限公司 Copyright (c) 2003</p>
 * <p>Company: 中天信息技术有限公司</p>
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
 *   生成查询的query.jsp和report.jsp的所需数据
 *
 ********************************************/

public class Index
{
    DatabaseConnection dbc = null;         //数据库连接
    Connection conn = null;                //数据库连接
    Statement stmt = null;                 //数据库连接状态

    private String errMsg=null;            //错误代码
    private String strType=null;           //程序类型  1--查询    2--报表
    public Index()
    {
        try {
            dbc = MyDB.getInstance().apGetConn();
            conn = dbc.getConnection();
        }
        catch (Exception e) {
            errMsg="数据库连接失败！";
        }
    }

    public ArrayList getQuery()//获得查询的数据  1
    {
        this.strType="0";
        return this.getRS();
    }

    public ArrayList getReport()//获得报表的数据  2
    {
        this.strType="1";
        return this.getRS();
    }

    public ArrayList getRS()//通用--获得数据的方法
    {
        ArrayList arrList=new ArrayList();    //数据集合

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
                IndexData iData=new IndexData();      //值对象
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
            this.errMsg="查询配置数据失败！";
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