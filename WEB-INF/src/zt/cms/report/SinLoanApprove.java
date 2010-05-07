package zt.cms.report;

/**
 * <p>Title: 信贷管理</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003 中天信息技术有限公司</p>
 * <p>Company: 中天信息技术有限公司</p>
 * @author YUSG
 * @version 1.0
 */

import zt.cmsi.mydb.MyDB;
import java.sql.ResultSet;
import java.lang.StringBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.SQLException;
import zt.platform.db.RecordSet;
import zt.platform.db.DBUtil;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.ConnectionManager;
import zt.platform.db.NoAvailableResourceException;
import zt.cms.pub.ServerTime;
/********************************************************
 * function:单笔贷款审批情况查询
 * 需要查询:申请人情况(),担保人情况(),抵质押物情况()
 *         审批情况()  ,  决策情况() , 发放情况()
 *
 *********************************************************/

public class SinLoanApprove
{
    DatabaseConnection conn=null;  //数据库连接
    String errMsg=null;            //错误信息

    HashMap hmapBrhLevel=new HashMap();          //部门
    HashMap hmapComTyp=new HashMap();            //处理人岗位
    HashMap hmapResTyp=new HashMap();            //处理人结论

    int cCount=0;                  //当前的连接号

    public SinLoanApprove()
    {
        try {
            conn=MyDB.getInstance().apGetConn();
        }
        catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }
    private void init()
    {
        this.hmapBrhLevel.put("1","市联社");
        this.hmapBrhLevel.put("2","县联社");
        this.hmapBrhLevel.put("3","信贷部门");
        this.hmapBrhLevel.put("4","分社");

         RecordSet rs1 = conn.executeQuery(
                "select enutp,enudt from ptenuminfodetl where enuid='CommentType'");
        while (rs1.next())
        {
             String strKey = rs1.getString(0);
             String strName=DBUtil.fromDB(rs1.getString(1));
             if(strKey!=null && strName!=null)
             {
                 this.hmapComTyp.put(strKey,strName);
             }
        }
        rs1=null;
        RecordSet rs2 = conn.executeQuery(
                "select enutp,enudt from ptenuminfodetl where enuid='ResultType'");
        while (rs2.next())
        {
           String strKey = rs2.getString(0);
           String strName=DBUtil.fromDB(rs2.getString(1));
           if(strKey!=null && strName!=null)
           {
              this.hmapResTyp.put(strKey,strName);
           }
        }
        rs2=null;
    }
    private RecordSet getApp(String strbmno) throws SQLException//获得审批情况信息
    {
        String    strClientNo=null;        //客户代码
        String    strTableName=null;       //表名称
        RecordSet rsApp=null;              //申请情况结果集

        StringBuffer strBuf=new StringBuffer();
        strBuf.append("select a.LoanPurpose,a.appamt,a.appmonths,b.name,");
        strClientNo=getClientNo(strbmno);  //获得客户代码

        if(strClientNo!=null)
        {
            strTableName = getTableName(strClientNo); //获得表名称(企业客户表或个人客户表)
            if (strTableName.equals("cmindvclient")) {
                strBuf.append("b.id,b.currentaddress,b.phone3,");
                strBuf.append("(select d.enudt from ptenuminfodetl d where d.enuid='CreditClass' and d.enutp=b.creditclass),");
                strBuf.append("(select c.sname from scbranch c where c.brhid=b.appbrhid),");
                strBuf.append(DBUtil.toDB("'个人',"));
                strBuf.append("b.clientno from bmtableapp a,cmindvclient b where a.bmno='");
            }
            else {
                strBuf.append("b.id,b.addressinlaw,");
                strBuf.append(DBUtil.toDB("'',"));
                strBuf.append("(select d.enudt from ptenuminfodetl d where d.enuid='CreditClass' and d.enutp=b.creditclass),");
                strBuf.append("(select c.sname from scbranch c where c.brhid=b.appbrhid),");
                strBuf.append(DBUtil.toDB("'企业',"));
                strBuf.append("b.clientno from bmtableapp a,cmcorpclient b where a.bmno='");
            }
            strBuf.append(strbmno);
            strBuf.append("' and b.clientno='");
            strBuf.append(strClientNo);
            strBuf.append("'");
            //System.out.println("APP:  "+strBuf.toString());
            rsApp = conn.executeQuery(strBuf.toString());
        }
        else
        {
            this.errMsg="客户信息不存在！";
        }
        return rsApp;
    }
    public RecordSet getPlagSecurity(String strbmno) throws SQLException//获得担保人信息
    {
        RecordSet rsClientNo=null;    //担保人代码结果集
        RecordSet rsPlag=null;        //担保人信息结果集
        String strClientNo="";

        StringBuffer strBuf=new StringBuffer();
        strBuf.append("select name,id,currentaddress,phone3,creditclass,");
        strBuf.append(DBUtil.toDB("'个人' ")+" from cmindvclient where clientno='");

        String strPlagSecur="select clientno from bmpldgsecurity where bmno='";
        rsClientNo=conn.executeQuery(strPlagSecur+strbmno+"'");
        if(rsClientNo.next())
        {
            strClientNo=rsClientNo.getString(0);
            if(strClientNo!=null)
               strClientNo=strClientNo.trim();
        }
        strBuf.append(strClientNo);
        strBuf.append("' union");
        strBuf.append(" select name,id,addressinlaw,");
        strBuf.append(DBUtil.toDB("'未填'")+",creditclass,");
        strBuf.append(DBUtil.toDB("'企业'")+" from cmcorpclient");
        strBuf.append(" where clientno ='");
        strBuf.append(strClientNo);

        strBuf.append("'");
        //System.out.println("PlagSecurity:  "+strBuf.toString());
        rsPlag=conn.executeQuery(strBuf.toString());

        return rsPlag;
    }
    public RecordSet getPlegeMort(String strbmno) throws SQLException//获得抵质押物情况
    {
        RecordSet rsPm=null;       //抵质押物情况结果集

        String strPledgeMort="select pledgename,(select enudt from ptenuminfodetl where enuid='PldgMortType' and enutp=pldgmorttype),pledgeowner,pledgeamt,EstimatePrice,PledgePrice from BMPldgMort where bmno='";
        strPledgeMort=strPledgeMort+strbmno+"'";

        //System.out.println("PledgeMort:  "+strPledgeMort);
        rsPm=conn.executeQuery(strPledgeMort);
        return rsPm;
    }
    public RecordSet getComment(String strbmno) throws SQLException//获得审批情况
    {
        RecordSet rsComment=null;   //审批情况结果集

        StringBuffer strBuf=new StringBuffer();
        strBuf.append("select a.commenttype,b.username,a.remark,a.resulttype,c.brhlevel");
        strBuf.append(" from bmcomments a,scuser b,scbranch c ");
        strBuf.append("where b.loginname=a.reviewedby and b.brhid=c.brhid and a.bmno='");
        strBuf.append(strbmno);
        strBuf.append("' order by c.brhlevel,a.commenttype");
        //System.out.println("Comment:  "+strBuf.toString());
        rsComment=conn.executeQuery(strBuf.toString());
        return rsComment;

    }
    public RecordSet getDecision(String strbmno) throws SQLException//获得决策情况
    {
        RecordSet rsDecision=null;   //决策情况结果集

        StringBuffer strBuf=new StringBuffer();
        strBuf.append("select b.username,a.amt,a.rate,a.months,a.resulttype,c.brhlevel ");
        strBuf.append(" from bmdecision a,scuser b,scbranch c ");
        strBuf.append("where b.loginname=a.decidedby and b.brhid=c.brhid and a.bmno='");
        strBuf.append(strbmno);
        strBuf.append("' order by c.brhlevel");
        //System.out.println("Decision:  "+strBuf.toString());
        rsDecision=conn.executeQuery(strBuf.toString());
        return rsDecision;
    }
    public RecordSet getContract(String strbmno) throws SQLException //获得发放情况
    {
        RecordSet rsContract=null;    //发放情况结果集

        StringBuffer strBuf=new StringBuffer();
        strBuf.append("select a.contractno,a.startdate,a.enddate,b.contractamt,");
        strBuf.append("b.perimon,b.crtrate,b.accno from bmcontract a,rqloanledger b");
        strBuf.append(" where a.bmno=b.bmno and a.bmno='");
        strBuf.append(strbmno);
        strBuf.append("'");
        //System.out.println("Contract:  "+strBuf.toString());
        rsContract=conn.executeQuery(strBuf.toString());
        return rsContract;
    }
    public RecordSet getBmTable(String strbmno) throws SQLException //获得当前状态
    {
        RecordSet rsContract=null;    //当前状态结果集

        StringBuffer strBuf=new StringBuffer();
        strBuf.append("select bmstatus,");
        strBuf.append("(select d.enudt from ptenuminfodetl d where d.enuid='BMStatus' and d.enutp=bmstatus)");
        strBuf.append(" from bmtable where bmno='");
        strBuf.append(strbmno);
        strBuf.append("'");
        //System.out.println("Bmtalbe:  "+strBuf.toString());
        rsContract=conn.executeQuery(strBuf.toString());

        return rsContract;
    }
    private String getTableName(String strclientno) throws SQLException//判断客户是企业客户还是个人客户
    {
        String strTableName = null; //表名称
        int intCount = 0;           //存在的客户记录数
        RecordSet rsCount = null;   //客户记录数结果集

        String strSqlClient = "select count(*) from cmcorpclient where clientno='"+strclientno + "'";
        //System.out.println("SqlClient:"+strSqlClient);
        rsCount = conn.executeQuery(strSqlClient);
        if(rsCount.next()) {
            intCount = rsCount.getInt(0);
        }
        if (intCount < 1) {
            strTableName = "cmindvclient";
        }
        else {
            strTableName = "cmcorpclient";
        }
        //System.out.println("TableName:"+strTableName);
        return strTableName;
    }
    private String getClientNo(String strbmno) throws SQLException//获得客户代码
    {
        String strClientNo = null; //客户代码
        int intCount=-1;           //客户数
        RecordSet rsClient = null; //客户代码结果集
        RecordSet rsCount=null;    //判断客户是否是初始化

        String strSqlBmTable = "select a.clientno from bmtable a where a.bmno='"+strbmno+"' and exists (select * from cmclient where clientno=a.clientno)";
        //System.out.println("SqlBmTable:"+strSqlBmTable);
        rsClient = conn.executeQuery(strSqlBmTable);
        if(rsClient.next()) {
            strClientNo = rsClient.getString(0).trim();
        }
        rsClient.close();
        rsClient=null;

        return strClientNo;
    }
    private String changeCode(String strValue)
    {
        String strCode="&nbsp;";
        if(strValue!=null)
        {
            strValue=strValue.trim();
            strCode = DBUtil.fromDB(strValue.trim());
        }
        return strCode;
    }
    private String valToName(String strValue,String strType)//
    {
        String strName="&nbsp;";
        if(strValue!=null)
        {
            strValue=strValue.trim();
            if(strType.equals("brhlvl") && this.hmapBrhLevel.containsKey(strValue))//决策部门,审批部门
            {
                strName=(String)this.hmapBrhLevel.get(strValue);
            }
            if(strType.equals("comment") && this.hmapComTyp.containsKey(strValue))//处理人岗位
            {
               strName=(String)this.hmapComTyp.get(strValue);
            }
            if(strType.equals("result") && this.hmapResTyp.containsKey(strValue))//结论
            {
                strName=(String)this.hmapResTyp.get(strValue);
            }
        }
        if(strName==null)
            strName="&nbsp;";
        return strName;
    }

    public SinLoanAppData getSinLoanApp(String bmno)
    {
        SinLoanAppData slad = new SinLoanAppData();
        RecordSet rs = null;
        slad.basDate=this.getBasDate();
        this.init();

        try {
            rs = this.getApp(bmno); //客户情况
            if(this.errMsg==null)
            {
                if (rs.next()) {
                    slad.appLoanCat = changeCode(rs.getString(0));
                    slad.appAmt = changeCode(rs.getString(1));
                    slad.appMonths = changeCode(rs.getString(2));

                    slad.appClientName = changeCode(rs.getString(3));
                    slad.appID = changeCode(rs.getString(4));
                    slad.appAddress = changeCode(rs.getString(5));
                    slad.appPhone = changeCode(rs.getString(6));
                    slad.appCreditClass = changeCode(rs.getString(7));
                    slad.basBrhName=changeCode(rs.getString(8));
                    slad.appClientType = changeCode(rs.getString(9));
                    slad.appClientNo = changeCode(rs.getString(10));
                }
            }
        }
        catch (SQLException e) {
            System.out.print("客户情况查询失败");
        }

        if(this.errMsg==null)
        {
            try {
                rs = this.getPlagSecurity(bmno); //担保人情况
                if (rs.next()) {
                    slad.secClientName = changeCode(rs.getString(0));
                    slad.secID = changeCode(rs.getString(1));
                    slad.secAddress = changeCode(rs.getString(2));
                    slad.secPhone = changeCode(rs.getString(3));
                    slad.secCreditClass = changeCode(rs.getString(4));
                    slad.secClientType = changeCode(rs.getString(5));
                }
            }
            catch (SQLException e) {
                System.out.println("担保人情况查询失败");
            }

            rs.close();
            rs = null;
            try {
                rs = this.getPlegeMort(bmno); //抵质押物情况
                while (rs.next()) {
                    slad.pleName = changeCode(rs.getString(0));
                    slad.pleType = changeCode(rs.getString(1));
                    slad.pleOwner = changeCode(rs.getString(2));
                    slad.pleAmt = changeCode(rs.getString(3));
                    slad.pleEstimate = changeCode(rs.getString(4));
                    slad.plePrice = changeCode(rs.getString(5));

                    ArrayList arrayList = new ArrayList();
                    arrayList.add(slad.pleName);
                    arrayList.add(slad.pleType);
                    arrayList.add(slad.pleOwner);
                    arrayList.add(slad.pleAmt);
                    arrayList.add(slad.pleEstimate);
                    arrayList.add(slad.plePrice);

                    slad.alPlege.add(arrayList);
                    arrayList = null;
                }
            }
            catch (SQLException e) {
                System.out.println("抵质押物情况查询失败");
            }

            rs.close();
            rs = null;
            try {
                rs = this.getComment(bmno); //审批情况
                while (rs.next()) {
                    slad.comType = valToName(rs.getString(0), "comment");
                    slad.comReviewd = changeCode(rs.getString(1));
                    slad.comRemark = changeCode(rs.getString(2));
                    slad.comResultType = valToName(rs.getString(3), "result");
                    slad.comBrhLevel = valToName(rs.getString(4), "brhlvl");

                    ArrayList arrayList = new ArrayList();
                    arrayList.add(slad.comBrhLevel);
                    arrayList.add(slad.comType);
                    arrayList.add(slad.comReviewd);
                    arrayList.add(slad.comRemark);
                    arrayList.add(slad.comResultType);

                    slad.alComment.add(arrayList);
                    arrayList = null;
                }
            }
            catch (SQLException e) {
                System.out.println("审批情况查询失败");
            }

            rs.close();
            rs = null;
            try {
                rs = this.getDecision(bmno); //决策情况
                while (rs.next()) {
                    slad.decDecided = changeCode(rs.getString(0));
                    slad.decAmt = changeCode(rs.getString(1));
                    slad.decRate = changeCode(rs.getString(2));
                    slad.decMonths = changeCode(rs.getString(3));
                    slad.decResultType = valToName(rs.getString(4), "result");
                    slad.decRrhLevel = valToName(rs.getString(5), "brhlvl");

                    ArrayList arrayList = new ArrayList();
                    arrayList.add(slad.decRrhLevel);
                    arrayList.add(slad.decDecided);
                    arrayList.add(slad.decAmt);
                    arrayList.add(slad.decRate);
                    arrayList.add(slad.decMonths);
                    arrayList.add(slad.decResultType);

                    slad.alDecision.add(arrayList);
                    arrayList = null;
                }
            }
            catch (SQLException e) {
                System.out.println("决策情况查询失败");
            }

            rs.close();
            rs = null;
            try {
                rs = this.getContract(bmno); //发放情况
                if (rs.next()) {
                    slad.conNo = changeCode(rs.getString(0));
                    slad.conBeginDate = changeCode(rs.getString(1));
                    slad.conEndDate = changeCode(rs.getString(2));
                    slad.conLoanAmt = changeCode(rs.getString(3));
                    slad.conAmt = changeCode(rs.getString(3));
                    slad.conMonths = changeCode(rs.getString(4));
                    slad.conRate = changeCode(rs.getString(5));
                    slad.conAccNo = changeCode(rs.getString(6));
                }
            }
            catch (SQLException e) {
                System.out.println("发放情况查询失败");
            }

            rs.close();
            rs = null;
            try
            {
                rs=this.getBmTable(bmno);
                if(rs.next())
                {
                    slad.basStatus=changeCode(rs.getString(0));
                    slad.basStatusName=changeCode(rs.getString(1));
                }
            }
            catch(SQLException e)
            {
                System.out.println("业务状态查询失败");
            }
        }

        MyDB.getInstance().apReleaseConn(1);
        conn = null;
        return slad;
    }
    public String getBasDate()
    {
        String strBasDate="&nbsp;";
        strBasDate=ServerTime.getDbCurrentDate();
        if(strBasDate!=null)
            return strBasDate;
        else
            return "&nbsp;";
    }
    public String getErrMsg()   //获得错误代码
    {
        return this.errMsg;
    }
}