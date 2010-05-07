package zt.cms.report;

/**
 * <p>Title: �Ŵ�����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003 ������Ϣ�������޹�˾</p>
 * <p>Company: ������Ϣ�������޹�˾</p>
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
 * function:���ʴ������������ѯ
 * ��Ҫ��ѯ:���������(),���������(),����Ѻ�����()
 *         �������()  ,  �������() , �������()
 *
 *********************************************************/

public class SinLoanApprove
{
    DatabaseConnection conn=null;  //���ݿ�����
    String errMsg=null;            //������Ϣ

    HashMap hmapBrhLevel=new HashMap();          //����
    HashMap hmapComTyp=new HashMap();            //�����˸�λ
    HashMap hmapResTyp=new HashMap();            //�����˽���

    int cCount=0;                  //��ǰ�����Ӻ�

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
        this.hmapBrhLevel.put("1","������");
        this.hmapBrhLevel.put("2","������");
        this.hmapBrhLevel.put("3","�Ŵ�����");
        this.hmapBrhLevel.put("4","����");

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
    private RecordSet getApp(String strbmno) throws SQLException//������������Ϣ
    {
        String    strClientNo=null;        //�ͻ�����
        String    strTableName=null;       //������
        RecordSet rsApp=null;              //������������

        StringBuffer strBuf=new StringBuffer();
        strBuf.append("select a.LoanPurpose,a.appamt,a.appmonths,b.name,");
        strClientNo=getClientNo(strbmno);  //��ÿͻ�����

        if(strClientNo!=null)
        {
            strTableName = getTableName(strClientNo); //��ñ�����(��ҵ�ͻ������˿ͻ���)
            if (strTableName.equals("cmindvclient")) {
                strBuf.append("b.id,b.currentaddress,b.phone3,");
                strBuf.append("(select d.enudt from ptenuminfodetl d where d.enuid='CreditClass' and d.enutp=b.creditclass),");
                strBuf.append("(select c.sname from scbranch c where c.brhid=b.appbrhid),");
                strBuf.append(DBUtil.toDB("'����',"));
                strBuf.append("b.clientno from bmtableapp a,cmindvclient b where a.bmno='");
            }
            else {
                strBuf.append("b.id,b.addressinlaw,");
                strBuf.append(DBUtil.toDB("'',"));
                strBuf.append("(select d.enudt from ptenuminfodetl d where d.enuid='CreditClass' and d.enutp=b.creditclass),");
                strBuf.append("(select c.sname from scbranch c where c.brhid=b.appbrhid),");
                strBuf.append(DBUtil.toDB("'��ҵ',"));
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
            this.errMsg="�ͻ���Ϣ�����ڣ�";
        }
        return rsApp;
    }
    public RecordSet getPlagSecurity(String strbmno) throws SQLException//��õ�������Ϣ
    {
        RecordSet rsClientNo=null;    //�����˴�������
        RecordSet rsPlag=null;        //��������Ϣ�����
        String strClientNo="";

        StringBuffer strBuf=new StringBuffer();
        strBuf.append("select name,id,currentaddress,phone3,creditclass,");
        strBuf.append(DBUtil.toDB("'����' ")+" from cmindvclient where clientno='");

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
        strBuf.append(DBUtil.toDB("'δ��'")+",creditclass,");
        strBuf.append(DBUtil.toDB("'��ҵ'")+" from cmcorpclient");
        strBuf.append(" where clientno ='");
        strBuf.append(strClientNo);

        strBuf.append("'");
        //System.out.println("PlagSecurity:  "+strBuf.toString());
        rsPlag=conn.executeQuery(strBuf.toString());

        return rsPlag;
    }
    public RecordSet getPlegeMort(String strbmno) throws SQLException//��õ���Ѻ�����
    {
        RecordSet rsPm=null;       //����Ѻ����������

        String strPledgeMort="select pledgename,(select enudt from ptenuminfodetl where enuid='PldgMortType' and enutp=pldgmorttype),pledgeowner,pledgeamt,EstimatePrice,PledgePrice from BMPldgMort where bmno='";
        strPledgeMort=strPledgeMort+strbmno+"'";

        //System.out.println("PledgeMort:  "+strPledgeMort);
        rsPm=conn.executeQuery(strPledgeMort);
        return rsPm;
    }
    public RecordSet getComment(String strbmno) throws SQLException//����������
    {
        RecordSet rsComment=null;   //������������

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
    public RecordSet getDecision(String strbmno) throws SQLException//��þ������
    {
        RecordSet rsDecision=null;   //������������

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
    public RecordSet getContract(String strbmno) throws SQLException //��÷������
    {
        RecordSet rsContract=null;    //������������

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
    public RecordSet getBmTable(String strbmno) throws SQLException //��õ�ǰ״̬
    {
        RecordSet rsContract=null;    //��ǰ״̬�����

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
    private String getTableName(String strclientno) throws SQLException//�жϿͻ�����ҵ�ͻ����Ǹ��˿ͻ�
    {
        String strTableName = null; //������
        int intCount = 0;           //���ڵĿͻ���¼��
        RecordSet rsCount = null;   //�ͻ���¼�������

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
    private String getClientNo(String strbmno) throws SQLException//��ÿͻ�����
    {
        String strClientNo = null; //�ͻ�����
        int intCount=-1;           //�ͻ���
        RecordSet rsClient = null; //�ͻ���������
        RecordSet rsCount=null;    //�жϿͻ��Ƿ��ǳ�ʼ��

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
            if(strType.equals("brhlvl") && this.hmapBrhLevel.containsKey(strValue))//���߲���,��������
            {
                strName=(String)this.hmapBrhLevel.get(strValue);
            }
            if(strType.equals("comment") && this.hmapComTyp.containsKey(strValue))//�����˸�λ
            {
               strName=(String)this.hmapComTyp.get(strValue);
            }
            if(strType.equals("result") && this.hmapResTyp.containsKey(strValue))//����
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
            rs = this.getApp(bmno); //�ͻ����
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
            System.out.print("�ͻ������ѯʧ��");
        }

        if(this.errMsg==null)
        {
            try {
                rs = this.getPlagSecurity(bmno); //���������
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
                System.out.println("�����������ѯʧ��");
            }

            rs.close();
            rs = null;
            try {
                rs = this.getPlegeMort(bmno); //����Ѻ�����
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
                System.out.println("����Ѻ�������ѯʧ��");
            }

            rs.close();
            rs = null;
            try {
                rs = this.getComment(bmno); //�������
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
                System.out.println("���������ѯʧ��");
            }

            rs.close();
            rs = null;
            try {
                rs = this.getDecision(bmno); //�������
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
                System.out.println("���������ѯʧ��");
            }

            rs.close();
            rs = null;
            try {
                rs = this.getContract(bmno); //�������
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
                System.out.println("���������ѯʧ��");
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
                System.out.println("ҵ��״̬��ѯʧ��");
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
    public String getErrMsg()   //��ô������
    {
        return this.errMsg;
    }
}