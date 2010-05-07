package zt.cms.report;

/**
 * <p>Title: 信贷管理系统</p>
 * <p>Description: 潍坊信贷</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 中天信息技术有限公司</p>
 * @author Yusg
 * @version 1.0
 */
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.SQLException;
import zt.platform.db.RecordSet;
import zt.cmsi.mydb.MyDB;
import zt.platform.db.DBUtil;
import zt.platform.db.DatabaseConnection;
import zt.cms.pub.ServerTime;
/********************************************************
 *  function:单笔贷款明细查询
 *  需要查询：1.基本情况   2.贷款信息     3.发放收回情况
 *           4.不良情况                 5.其他信息
 *******************************************************/

public class SinLoanDetail
{
    DatabaseConnection conn=null;  //数据库连接
    String errMsg=null;            //错误信息
    String strBrhid=null;          //网点编号

    public SinLoanDetail()
    {
        try
        {
            conn=MyDB.getInstance().apGetConn();
        }
        catch (Exception e)
        {
            System.out.println("DB connection error!");
            errMsg="数据库连接获得失败！";
        }
    }

    private RecordSet getBmTable(String bmno)//获得bmtable
    {
        RecordSet rsBmTable=null;              //bmtable结果集

        StringBuffer strBuf=new StringBuffer();
        strBuf.append("select (select sname from scbranch where brhid=a.brhid),a.clientname,");
        strBuf.append("(select enudt from ptenuminfodetl where enuid='BMStatus' and enutp=a.bmstatus),");
        strBuf.append(" a.brhid from bmtable a");
        strBuf.append(" where a.bmno='");
        strBuf.append(bmno+"'");
        //System.out.println("bmtable:"+strBuf.toString());
        rsBmTable=conn.executeQuery(strBuf.toString());
        return rsBmTable;
    }
    private RecordSet getBmTableApp(String bmno)
    {
        RecordSet rsBmTableApp=null;              //bmtable结果集

       StringBuffer strBuf=new StringBuffer();
       strBuf.append("select a.contractno,a.scontractno,a.loanpurpose,");
       strBuf.append("(select enudt from ptenuminfodetl where enuid='BMType' and enutp=a.typeno),");
       strBuf.append("(select username from scuser where loginname=a.clientmgr),");
       strBuf.append("(select username from scuser where loginname=a.firstresp),");
       strBuf.append("a.fisrtresppct, ");
       strBuf.append("(select enudt from ptenuminfodetl where enuid='LoanType3' and enutp=a.loantype3),");
       strBuf.append("(select enudt from ptenuminfodetl where enuid='LoanType5' and enutp=a.loantype5),");
       strBuf.append("a.ifresploan");
       strBuf.append(" from bmtableapp a ");
       strBuf.append(" where a.bmno='");
       strBuf.append(bmno+"'");
       //System.out.println("bmtableapp:"+strBuf.toString());
       rsBmTableApp=conn.executeQuery(strBuf.toString());
       return rsBmTableApp;
    }
    private RecordSet getRqLoanLedger(String bmno)//获得贷款情况
    {
        RecordSet rsLoan=null;              //贷款情况结果集

        StringBuffer strBuf=new StringBuffer();
        strBuf.append("select a.actno,a.cnlno,a.accno,a.crtrate,a.enddate,a.nowenddate,");
        strBuf.append("a.perimon,a.contractamt,a.paydate,a.nowbal,");
        strBuf.append("(select enudt from ptenuminfodetl where enuid='LoanCat2' and enutp=a.loancat2),");
        strBuf.append("(select sum(paybal) from btdueintrst where bmno=a.bmno)");
        strBuf.append(" from RQLoanLedger a ");
        strBuf.append(" where a.bmno='");
        strBuf.append(bmno+"'");
        //System.out.println("rqloanled:"+strBuf.toString());
        rsLoan=conn.executeQuery(strBuf.toString());
        return rsLoan;
    }

    private RecordSet getRQPayBack(String bmno)//获得发放收回情况
    {
        RecordSet rsPay=null;              //发放收回结果集

        StringBuffer strBuf=new StringBuffer();
        strBuf.append("select actno,cnlno,txndate,PayCrBal,PayDbBal,Dtlbal,");
        strBuf.append("(select enudt from ptenuminfodetl where enuid='LoanCat2' and enutp=loancat2)");
        strBuf.append(" from rqpayback");
        strBuf.append(" where bmno ='");
        strBuf.append(bmno+"' and brhid='");
        strBuf.append(this.strBrhid+"' order by txndate");
        //System.out.println("rqpayback:"+strBuf.toString());
        rsPay=conn.executeQuery(strBuf.toString());
        return rsPay;
    }

    private RecordSet getBmInactLoan(String bmno)//获得不良贷款情况
    {
        RecordSet rsBad=null;              //不良贷款情况结果集

        StringBuffer strBuf=new StringBuffer();
        strBuf.append("select transferdate,lastnotifydate,penaltydate,");
        strBuf.append("(select username from scuser where loginname=adminedby),");
        strBuf.append("(select username from scuser where loginname=reviewedby),");
        strBuf.append("penaltyrule,penalty from bminactloan where bmno='");
        strBuf.append(bmno+"'");
        //System.out.println("bminactloan:"+strBuf.toString());
        rsBad=conn.executeQuery(strBuf.toString());
        return rsBad;
    }

    private RecordSet getClient(String clientno)//获得客户信息
    {
        RecordSet rsClient=null;             //客户信息结果集

        String strClient="select id,phone from cmclient where clientno='";
        strClient=strClient+clientno+"'";

        rsClient=conn.executeQuery(strClient);
        return rsClient;
    }

    private String getClientNo(String strbmno) //获得客户代码
    {
        String strClientNo = null; //客户代码
        int intCount=-1;           //客户数
        RecordSet rsClient = null; //客户代码结果集
        RecordSet rsCount=null;    //判断客户是否是初始化

        String strSqlBmTable = "select a.clientno from bmtable a where a.bmno='"+strbmno+"'";
        //System.out.println("SqlBmTable:"+strSqlBmTable);
        rsClient = conn.executeQuery(strSqlBmTable);
        if (rsClient.next())
        {
           strClientNo = rsClient.getString(0).trim();
        }

        rsClient.close();
        rsClient=null;

        return strClientNo;
    }
    private String getBasDate()//获得查询时间
    {
        String strBasDate="&nbsp;";
        strBasDate=ServerTime.getDbCurrentDate();
        if(strBasDate!=null)
            return strBasDate;
        else
            return "&nbsp;";
    }
    private String changeCode(String strValue)  //编码的处理
    {
        String strCode="&nbsp;";
        if(strValue!=null)
        {
            strValue=strValue.trim();
            strCode = DBUtil.fromDB(strValue.trim());
        }
        return strCode;
    }
    public SinLoanDetData getLoanDetail(String bmno)
    {
        RecordSet rs = null;
        SinLoanDetData sldd=new SinLoanDetData();

        String StrClientNo=this.getClientNo(bmno);
        //if(StrClientNo==null)
            //this.errMsg="客户信息不存在！";

        if(this.errMsg==null)
        {
            sldd.strDate=this.getBasDate();  //其他---查询日期

            rs=this.getClient(StrClientNo);//CmClient信息<1 客户部分>
            if(rs.next())
            {
                sldd.strId=changeCode(rs.getString(0));
                sldd.strPhone=changeCode(rs.getString(1));
            }

            rs=this.getBmTable(bmno);//BmTable信息<2 业务部分>
            if(rs.next())
            {
                sldd.strScbrhName=changeCode(rs.getString(0));
                sldd.strClientName=changeCode(rs.getString(1));
                sldd.strBmStatus=changeCode(rs.getString(2));
                this.strBrhid=changeCode(rs.getString(3));    //网点号
            }

            rs=this.getBmTableApp(bmno);//贷款信息
            if(rs.next())
            {
                sldd.strContractNo=changeCode(rs.getString(0));
                sldd.strSContractNo=changeCode(rs.getString(1));
                sldd.strLoanPurpose=changeCode(rs.getString(2));
                sldd.strLoanType=changeCode(rs.getString(3));
                sldd.strClientMgr=changeCode(rs.getString(4));
                sldd.strFirstResp=changeCode(rs.getString(5));
                sldd.strFisrtRespPct=changeCode(rs.getString(6));
                sldd.strLoanType3=changeCode(rs.getString(7));
                sldd.strLoanType5=changeCode(rs.getString(8));
                String strIfResp=rs.getString(9);
                if(strIfResp==null || !strIfResp.equals("1"))
                {
                  sldd.strIfResp="管理责任人";
                }
                else
                {
                  sldd.strIfResp="第一责任人";
                }

            }
            rs=this.getRqLoanLedger(bmno);
            if(rs.next())
            {
                sldd.strActNo=changeCode(rs.getString(0));
                sldd.strCnlNo=changeCode(rs.getString(1));
                sldd.strAccNo=changeCode(rs.getString(2));
                sldd.strCrtRate=changeCode(rs.getString(3));
                sldd.strEndDate=changeCode(rs.getString(4));
                sldd.strNowEndDate=changeCode(rs.getString(5));
                sldd.strPerimon=changeCode(rs.getString(6));
                sldd.strContractAmt=changeCode(rs.getString(7));
                sldd.strPayDate=changeCode(rs.getString(8));
                sldd.strNowBal=changeCode(rs.getString(9));
                sldd.strLoanCat2=changeCode(rs.getString(10));
                sldd.strEndRate=changeCode(rs.getString(11));
            }
            rs=this.getRQPayBack(bmno);//发放收回情况
            while(rs.next())
            {
                String strPActNo=changeCode(rs.getString(0));
                String strPCnlNo=changeCode(rs.getString(1));
                String strPTxnDate=changeCode(rs.getString(2));
                String strPayCrBal=changeCode(rs.getString(3));
                String strPayDbBal=changeCode(rs.getString(4));
                String strPDtlBal=changeCode(rs.getString(5));
                String strPLoanCat2=changeCode(rs.getString(6));

                ArrayList arrayList = new ArrayList();
                arrayList.add(strPActNo);
                arrayList.add(strPCnlNo);
                arrayList.add(strPTxnDate);
                arrayList.add(strPayDbBal);
                arrayList.add(strPayCrBal);
                arrayList.add(strPDtlBal);
                arrayList.add(strPLoanCat2);

                sldd.alPay.add(arrayList);
            }
            rs=this.getBmInactLoan(bmno);//不良时情况
            if(rs.next())
            {
                sldd.strTransDate=changeCode(rs.getString(0));
                sldd.strLastNotifyDate=changeCode(rs.getString(1));
                sldd.strPenaltyDate=changeCode(rs.getString(2));
                sldd.strAdminedBy=changeCode(rs.getString(3));
                sldd.strReviewedBy=changeCode(rs.getString(4));
                sldd.strPenaltyRule=changeCode(rs.getString(5));
                sldd.strPenalty=changeCode(rs.getString(6));
            }
        }
        MyDB.getInstance().apReleaseConn(1);
        conn = null;
        return sldd;
    }
    public String getErrMsg()   //获得错误代码
    {
        return this.errMsg;
    }


}