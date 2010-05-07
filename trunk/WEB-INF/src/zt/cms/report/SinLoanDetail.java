package zt.cms.report;

/**
 * <p>Title: �Ŵ�����ϵͳ</p>
 * <p>Description: Ϋ���Ŵ�</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ������Ϣ�������޹�˾</p>
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
 *  function:���ʴ�����ϸ��ѯ
 *  ��Ҫ��ѯ��1.�������   2.������Ϣ     3.�����ջ����
 *           4.�������                 5.������Ϣ
 *******************************************************/

public class SinLoanDetail
{
    DatabaseConnection conn=null;  //���ݿ�����
    String errMsg=null;            //������Ϣ
    String strBrhid=null;          //������

    public SinLoanDetail()
    {
        try
        {
            conn=MyDB.getInstance().apGetConn();
        }
        catch (Exception e)
        {
            System.out.println("DB connection error!");
            errMsg="���ݿ����ӻ��ʧ�ܣ�";
        }
    }

    private RecordSet getBmTable(String bmno)//���bmtable
    {
        RecordSet rsBmTable=null;              //bmtable�����

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
        RecordSet rsBmTableApp=null;              //bmtable�����

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
    private RecordSet getRqLoanLedger(String bmno)//��ô������
    {
        RecordSet rsLoan=null;              //������������

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

    private RecordSet getRQPayBack(String bmno)//��÷����ջ����
    {
        RecordSet rsPay=null;              //�����ջؽ����

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

    private RecordSet getBmInactLoan(String bmno)//��ò����������
    {
        RecordSet rsBad=null;              //����������������

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

    private RecordSet getClient(String clientno)//��ÿͻ���Ϣ
    {
        RecordSet rsClient=null;             //�ͻ���Ϣ�����

        String strClient="select id,phone from cmclient where clientno='";
        strClient=strClient+clientno+"'";

        rsClient=conn.executeQuery(strClient);
        return rsClient;
    }

    private String getClientNo(String strbmno) //��ÿͻ�����
    {
        String strClientNo = null; //�ͻ�����
        int intCount=-1;           //�ͻ���
        RecordSet rsClient = null; //�ͻ���������
        RecordSet rsCount=null;    //�жϿͻ��Ƿ��ǳ�ʼ��

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
    private String getBasDate()//��ò�ѯʱ��
    {
        String strBasDate="&nbsp;";
        strBasDate=ServerTime.getDbCurrentDate();
        if(strBasDate!=null)
            return strBasDate;
        else
            return "&nbsp;";
    }
    private String changeCode(String strValue)  //����Ĵ���
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
            //this.errMsg="�ͻ���Ϣ�����ڣ�";

        if(this.errMsg==null)
        {
            sldd.strDate=this.getBasDate();  //����---��ѯ����

            rs=this.getClient(StrClientNo);//CmClient��Ϣ<1 �ͻ�����>
            if(rs.next())
            {
                sldd.strId=changeCode(rs.getString(0));
                sldd.strPhone=changeCode(rs.getString(1));
            }

            rs=this.getBmTable(bmno);//BmTable��Ϣ<2 ҵ�񲿷�>
            if(rs.next())
            {
                sldd.strScbrhName=changeCode(rs.getString(0));
                sldd.strClientName=changeCode(rs.getString(1));
                sldd.strBmStatus=changeCode(rs.getString(2));
                this.strBrhid=changeCode(rs.getString(3));    //�����
            }

            rs=this.getBmTableApp(bmno);//������Ϣ
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
                  sldd.strIfResp="����������";
                }
                else
                {
                  sldd.strIfResp="��һ������";
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
            rs=this.getRQPayBack(bmno);//�����ջ����
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
            rs=this.getBmInactLoan(bmno);//����ʱ���
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
    public String getErrMsg()   //��ô������
    {
        return this.errMsg;
    }


}