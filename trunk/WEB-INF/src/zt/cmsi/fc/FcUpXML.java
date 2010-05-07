package zt.cmsi.fc;
/**
 * <p/>===============================================
 * <p/>Title: �弶�������ݱ���
 * <p/>===============================================
 * <p/>�������㡢�������ڵȲ�ѯ�����޸����ݣ�������xml�ĵ���������ᡣ
 * @version $Revision: 1.17 $  $Date: 2007/06/21 08:25:49 $
 * @author weiyb
 * <p/>�޸ģ�$Author: weiyb $     
 */

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import zt.cms.pub.SCBranch;
import zt.cmsi.fc.form.*;
import zt.platform.cachedb.DB2_81;
import zt.platform.cachedb.DatabaseConnection;

import javax.sql.rowset.CachedRowSet;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class FcUpXML {
    CachedRowSet rs = null;

    /**
     * ��������ҵ���������Ϣ
     *
     * @param jgdm    String �Ŵ����Ŷ�Ӧ��������������
     * @param brhlist String �¼�ʵ�����б�
     * @param date    String �������
     * @return sql
     * @throws Exception
     */
    private String getSqlQsydk(String jgdm, String brhlist, String date) throws Exception {
        //������Ҫɸѡfcmain���е����ݣ�Ȼ�����upqsydk����(ѡ�����ݶ�λNULL)
        //��������rq.LOANTYPE2,upmaping.id='LoanType2'(ʵ��Ϊ�������ޣ��ο�����������)
        //������ʽrq.loantype3,ö�٣����Զ�Ӧ
        //����չ�ڿ�ʼ����=null
        //����չ�ڵ�������=null
        //ǷϢ����Ϣ��(rqdueintrst.duebal1+duebal2+duebal3)����Ϣ�ֶ�֮��
        //��Ϣ��������=null
        //������������=fcmain.PASTDUEDAYS(��������)
        //���=FCMAIN.FCCLASS
        StringBuffer sql = new StringBuffer();
        sql.append("insert into UPqsydk(fcno,brhid,jrjgdm,fljzrq,khh,dkhthm,daikzl,daikfs,dkhtje,daikye,htsxrq,htzzrq,zqksrq,");
        sql.append("zqdqrq,htqdyt,sjdkyt,bxyqqk,qxjine,bzhrmc,dyawmc,dypgjz,dzyabl,sfdydj,yuqits,lxyqts,fleijg)");
        //sql.append(" select fc.fcno,fc.brhid,upm.upbranchid,fc.createdate,rq.clientno,rq.contractno,m_1.id,m.id,rq.contractamt,rq.nowbal,");
        sql.append(" select fc.fcno,fc.brhid,'" + jgdm + "',fc.createdate,rq.clientno,case rq.contractno when '' then fc.fcno else rtrim(fc.fcno)||rq.contractno end,m_1.id,m.id,rq.contractamt,rq.nowbal,");
        //sql.append(" ct.startdate,ct.enddate,cast(null as date),cast(null as date),cast(null as character),cast(null as character),cast(null as character)");
        sql.append(" rq.paydate,rq.enddate,cast(null as date),cast(null as date),cast(null as character),cast(null as character),cast(null as character)");
        sql.append(" ,di.duebal1+di.duebal2+di.duebal3,cast(null as character),cast(null as character),cast(null as decimal),cast(null as decimal),");
        sql.append(" case rq.loantype3 when 220 then '1' when 230 then '1' else '2' end,case when fc.PASTDUEDAYS<=0 then 0 else fc.PASTDUEDAYS end,0,cast(fc.fcclass as character)");//������������<=0����Ϊ0����Ϣ��������Ĭ��Ϊ0
        sql.append(" from fcmain fc  join rqloanlist rq on fc.bmno=rq.bmno ");
        sql.append(" left join bmcontract ct on fc.bmno=ct.bmno ");
        sql.append(" left join rqdueintrst di on fc.bmno=di.bmno");
        sql.append(" left join upmaping m on rq.loantype3=m.enutp and m.enuid='LoanType3'");
        sql.append(" left join upmaping m_1 on rq.loantype2=m_1.enutp and m_1.enuid='LoanType2'");
        //sql.append(" left join UPBRHID_MAPING upm on rq.bnkid=upm.branchid");
        //sql.append(" where fc.fctype=3 and fc.brhid='"+brhid+"' and fc.createdate='"+date+"'");
        sql.append(" where fc.fctype=3 and  fc.brhid in " + brhlist + " and fc.createdate='" + date + "'");//�޸�Ϊ�Ŵ�����
        sql.append(" and fc.FCCRTTYPE=1");//ʱ����
        //�ų�rq.clientno is null�����
        sql.append(" and rq.clientno is not null");
        //�ų� fc.FCCLASS=0�����
        sql.append(" and fc.FCCLASS>0");
        //��������
        sql.append(" and not exists(select 1 from UPqsydk where fcno=fc.fcno)");
        return sql.toString();
    }

    /**
     * ȡ����ҵ���������ʷ���ݣ����ΪĬ�����ݡ�
     *
     * @param jgdm    String �Ŵ����Ŷ�Ӧ��������������
     * @param brhlist String �¼�ʵ�����б�
     * @param date    String �������
     * @return sql
     * @throws Exception
     */
    private String getHistoryOfQsydk(String jgdm, String brhlist, String date) throws Exception {
        StringBuffer sql = new StringBuffer();
        CachedRowSet rs = DB2_81.getRs("select max(fljzrq) as maxdate from UPqsydk where jrjgdm='" + jgdm + "' and fljzrq<'" + date + "'");
        String maxdate = "";
        while (rs.next()) {
            maxdate = rs.getString("maxdate");
        }
        if (maxdate != null) {
            sql.append("update UPqsydk q set (q.daikzl,q.zqksrq,q.zqdqrq,q.htqdyt,");
            sql.append("q.sjdkyt,q.bxyqqk,q.qxjine,q.bzhrmc,q.dyawmc,q.dypgjz,q.dzyabl,q.sfdydj,q.isempty,q.dkhthm)=");
            sql.append("(select daikzl,zqksrq,zqdqrq,htqdyt,sjdkyt,bxyqqk,qxjine,bzhrmc,dyawmc,dypgjz,dzyabl,sfdydj,isempty,dkhthm ");
            sql.append(" from UPqsydk where q.khh=khh and q.jrjgdm=jrjgdm and  fljzrq='" + maxdate + "' fetch first 1 rows only) ");
            sql.append("where q.jrjgdm='" + jgdm + "' and   q.fljzrq='" + date + "' and exists(select 1 from UPqsydk  where q.khh=khh and q.jrjgdm=jrjgdm ");
            sql.append(" and fljzrq='" + maxdate + "')");
        } else {
            sql.append("delete from upqsydk where 1>2");
        }
        return sql.toString();
    }

    /**
     * ȡ��Ȼ��������΢����ҵ������Ϣ��ʷ���ݣ����ΪĬ�����ݡ�
     *
     * @param jgdm    String �Ŵ����Ŷ�Ӧ��������������
     * @param brhlist String �¼�ʵ�����б�
     * @param date    String �������
     * @return sql
     * @throws Exception
     */
    private String getHistoryOfZrrqt(String jgdm, String brhlist, String date) throws Exception {
        StringBuffer sql = new StringBuffer();
        CachedRowSet rs = DB2_81.getRs("select max(fljzrq) as maxdate from upzrrqt where jrjgdm='" + jgdm + "' and fljzrq<'" + date + "'");
        String maxdate = "";
        while (rs.next()) {
            maxdate = rs.getString("maxdate");
        }
        if (maxdate != null) {
            sql.append("update upzrrqt q set (q.fzeren,q.jinyxm,q.dk_sfaydytsydk,q.dk_dkyqqk,q.dk_dkqxqk,q.cw_gdzc,q.cw_jysr,q.cw_yjnjysr,");
            sql.append("q.cw_jsy,q.cw_yjnjsy,q.fcw_jyqksfzc,q.fcw_cpscxqqk,q.fcw_jkrhkyy,q.fcw_sfjnqk,q.db_dbhtsfyx,q.db_dbrdcnl,q.db_dzywjqjz,");
            sql.append("q.db_dzywdbxnl,q.flly,q.dcriqi,q.qtsm,q.daikzl,q.isempty,q.dkhthm)=");
            sql.append("(select fzeren,jinyxm,dk_sfaydytsydk,dk_dkyqqk,dk_dkqxqk,cw_gdzc,cw_jysr,cw_yjnjysr, ");
            sql.append("cw_jsy,cw_yjnjsy,fcw_jyqksfzc,fcw_cpscxqqk,fcw_jkrhkyy,fcw_sfjnqk,db_dbhtsfyx,db_dbrdcnl,db_dzywjqjz,");
            sql.append("db_dzywdbxnl,flly,dcriqi,qtsm,daikzl,isempty,dkhthm");
            sql.append(" from upzrrqt where q.khh=khh and q.jrjgdm=jrjgdm and  fljzrq='" + maxdate + "' fetch first 1 rows only) ");
            sql.append("where q.jrjgdm='" + jgdm + "' and   q.fljzrq='" + date + "' and exists(select 1 from upzrrqt  where q.khh=khh and q.jrjgdm=jrjgdm ");
            sql.append(" and fljzrq='" + maxdate + "')");
        } else {
            sql.append("delete from upzrrqt where 1>2");
        }
        return sql.toString();
    }

    /**
     * ȡ��Ȼ��һ��������������������Ϣ��ʷ���ݣ����ΪĬ�����ݡ�
     *
     * @param jgdm    String �Ŵ����Ŷ�Ӧ��������������
     * @param brhlist String �¼�ʵ�����б�
     * @param date    String �������
     * @return sql
     * @throws Exception
     */
    private String getHistoryOfZrrnh(String jgdm, String brhlist, String date) throws Exception {
        StringBuffer sql = new StringBuffer();
        CachedRowSet rs = DB2_81.getRs("select max(fljzrq) as maxdate from upzrrnh where jrjgdm='" + jgdm + "' and fljzrq<'" + date + "'");
        String maxdate = "";
        while (rs.next()) {
            maxdate = rs.getString("maxdate");
        }
        if (maxdate != null) {
            sql.append("update upzrrnh q set (q.dkhthm,q.daikzl,q.htsxrq,q.htzzrq,q.weiyqs,q.fenlyj,q.dbzk,q.isempty)=");
            sql.append("(select dkhthm,daikzl,htsxrq,htzzrq,weiyqs,fenlyj,dbzk,isempty");
            sql.append(" from upzrrnh where q.khh=khh and q.jrjgdm=jrjgdm and  fljzrq='" + maxdate + "' fetch first 1 rows only) ");
            sql.append("where q.jrjgdm='" + jgdm + "' and q.fljzrq='" + date + "' and exists(select 1 from upzrrnh  where q.khh=khh and q.jrjgdm=jrjgdm ");
            sql.append(" and fljzrq='" + maxdate + "')");
        } else {
            sql.append("delete from upzrrnh where 1>2");
        }
        return sql.toString();
    }

    /**
     * ������Ȼ��������΢����ҵ������Ϣ
     *
     * @param jgdm    String �Ŵ����Ŷ�Ӧ��������������
     * @param brhlist String �Ŵ������µ�ʵ�����б�
     * @param date    String �������
     * @return sql String
     * @throws Exception
     */
    private String getSqlZrrqt(String jgdm, String brhlist, String date) throws Exception {
        //���ʽ��ö�٣�ͬ������ʽ
        //��������ͬ�弶����
        //����ö�٣�û�У���Ҫ�Լ���
        //��Ӫ��Ŀ�Ƿ��ӦCMCORPCLIENT.BIZSCOPE(���˿�����Ϣ���еľ�Ӫ��Χ�ֶ�BIZSCOPE)��ͨ���ͻ��Ź���
        //�ʲ��ܶ�=CMCORPCLIENT.TOTASSETS,�ʲ��ܶ�֮�̶��ʲ�(��Ϊ��),��ծ�ܶ�=CMCORPCLIENT.TOTDEBT,
        //��ծ�ܶ�֮�Ŵ����Ž��=RQCLIENTDATA.BAL WHERE CLIENTNO=:CLIENTNO AND BRHID=:BRHID AND AVAILDT='YYMM'(���£����ֶ�Ϊ����)
        //��Ӫ����=null,Ԥ���꾭Ӫ����=null,������=null,Ԥ���꾻����=null
        //��Ӫ����Ƿ�����=null,��Ʒ�г��������=null,����˻�����Ը=null,˰�ѽ������=null,������ͬ�Ƿ���Ч=null,��֤�˴�������=null
        //�֣��ʣ�Ѻ�Ｐ���ֵ=null,�֣��ʣ�Ѻ��ı�������(ö����)=null,�������˵��=null
        //��������flly=fccmt.case when cmt4  is null then case when cmt3 is null then case when cmt2 is null then cmt1 else cmt2 end else cmt3 end else cmt4 end as cmt where fccmttype=1
        //������(ö����)=FCMAIN.FCCLASS
        //�������=RQLOANLIST.NOWBAL,��������=null,���õȼ�=m.id and enuid='CreditClass'
        //������������=fc.PASTDUEDAYS,��Ϣ��������=null
        //��������=fc.fctype,upmaping.id and upmaping.enuid='FCType'
        StringBuffer sql = new StringBuffer();
        sql.append("insert into UPzrrqt (fcno,brhid,jrjgdm,fljzrq,khh,kehumc,fzeren,jinyxm,jjixzh,dkhthm,dkhtje,daikfs,htsxrq,htzzrq,");
        sql.append("dk_sfaydytsydk,dk_dkyqqk,dk_dkqxqk,cw_zcze,cw_gdzc,cw_fzze,cw_xysjk,cw_jysr,cw_yjnjysr,cw_jsy,cw_yjnjsy,fcw_jyqksfzc,");
        sql.append("fcw_cpscxqqk,fcw_jkrhkyy,fcw_sfjnqk,db_dbhtsfyx,db_dbrdcnl,db_dzywjqjz,db_dzywdbxnl,qtsm,flly,fleijg,daikye,dcriqi,xinydj,");
        sql.append("yuqits,lxyqts,daikzl)");
        //sql.append(" select fc.fcno,fc.brhid,upm.upbranchid,fc.createdate,rq.clientno,rq.clientname,cast(null as character)");
        sql.append(" select fc.fcno,fc.brhid,'" + jgdm + "',fc.createdate,rq.clientno,rq.clientname,cast(null as character)");
        sql.append(",cast(cm.BIZSCOPE as varchar(40)),cast(null as character),case rq.contractno when '' then fc.fcno else rtrim(fc.fcno)||rq.contractno end,rq.contractamt,m.id,");
        //sql.append("ct.startdate,ct.enddate,cast(null as character),cast(null as character),cast(null as character),cm.totassets,cast(null as decimal),cm.totdebt,cd.bal,cast(null as decimal) ,cast(null as decimal),cast(null as decimal),cast(null as decimal),");
        //sql.append("ct.startdate,ct.enddate,cast(null as character),cast(null as character),cast(null as character),cm.totassets,cast(null as decimal),cm.totdebt,cast(null as decimal),cast(null as decimal) ,cast(null as decimal),cast(null as decimal),cast(null as decimal),");
        sql.append("rq.paydate,rq.enddate,cast(null as character),cast(null as character),cast(null as character),cm.totassets,cast(null as decimal),cm.totdebt,cast(null as decimal),cast(null as decimal) ,cast(null as decimal),cast(null as decimal),cast(null as decimal),");
        sql.append("cast(null as character),cast(null as character),cast(null as character),cast(null as character),cast(null as character),cast(null as character),");
        sql.append("cast(null as character),cast(null as character),cast(null as character),");
        sql.append("case when cmt.cmt4  is null then case when cmt.cmt3 is null then case when cmt.cmt2 is null then cmt.cmt1 else cmt.cmt2 end else cmt.cmt3 end else cmt.cmt4 end,");
        sql.append(" cast(fc.fcclass as character),rq.nowbal,cast(null as date),m_1.id,case when fc.PASTDUEDAYS<=0 then 0 else fc.PASTDUEDAYS end,0,m_2.id");
        sql.append(" from fcmain fc  join rqloanlist rq on fc.bmno=rq.bmno ");
        sql.append(" left join cmcorpclient cm on rq.clientno=cm.clientno");
        sql.append(" left join bmcontract ct on fc.bmno=ct.bmno ");
        //sql.append(" left join rqclientdata cd on rq.clientno=cd.clientno ");
        sql.append(" left join upmaping m on rq.loantype3=m.enutp and m.enuid='LoanType3'");
        sql.append(" left join upmaping m_1 on fc.creditclass=m_1.enutp and m_1.enuid='CreditClass'");
        sql.append(" left join upmaping m_2 on fc.fctype=m_2.enutp and m_2.enuid='FCType'");
        sql.append(" left join fccmt cmt on fc.fcno=cmt.fcno and cmt.fccmttype=1");
        //sql.append(" left join UPBRHID_MAPING upm on rq.bnkid=upm.branchid");
        sql.append(" where (fc.fctype=2 or fc.fctype=4) ");
        //sql.append(" and fc.brhid='"+brhid+"' and fc.createdate='"+date+"'");
        sql.append(" and fc.brhid in " + brhlist + " and fc.createdate='" + date + "'");//�޸�Ϊ�Ŵ�����
        sql.append(" and fc.FCCRTTYPE=1");//ʱ����
        //sql.append(" and  cd.brhid in "+brhlist+" and cd.availdt='"+DBUtil.to_Date(date).substring(0,6)+"'");
        //�ų�rq.clientno is null�����
        sql.append(" and rq.clientno is not null");
        //�ų� fc.FCCLASS=0�����
        sql.append(" and fc.FCCLASS>0");
        //��������
        sql.append(" and not exists(select 1 from UPzrrqt where fcno=fc.fcno)");
        return sql.toString();
    }

    /**
     * ���¸�ծ�ܶ�֮�Ŵ����Ž��=RQCLIENTDATA.BAL WHERE CLIENTNO=:CLIENTNO AND BRHID=:BRHID AND AVAILDT='YYMM'(���£����ֶ�Ϊ����)
     *
     * @param jgdm    String �Ŵ����Ŷ�Ӧ��������������
     * @param brhlist String �Ŵ������µ�ʵ�����б�
     * @param date    String �������
     * @return String
     * @throws Exception
     */
    private String getUpdateSqlZrrqt(String jgdm, String brhlist, String date) throws Exception {
        StringBuffer sql = new StringBuffer("update UPzrrqt q set (q.cw_xysjk)=(select bal from RQCLIENTDATA r");
        sql.append(" where r.availdt='" + DBUtil.to_Date(date).substring(0, 6) + "'");
        sql.append(" and q.khh=r.clientno and q.brhid=r.brhid) where q.jrjgdm='" + jgdm + "' and q.fljzrq='" + date + "'");
        sql.append(" and exists(select 1 from rqclientdata where q.khh=clientno and q.brhid=brhid and availdt='" + DBUtil.to_Date(date).substring(0, 6) + "')");
        return sql.toString();

    }

    /**
     * ������Ȼ������������Ϣ���ų�΢����ҵ���ʲ��ܶ�cw_zcze��CMINDVCLIENT.TOTASSETS����ծ�ܶ�cw_fzze��CMINDVCLIENT.TOTDEBT��
     *
     * @param jgdm    String �Ŵ����Ŷ�Ӧ��������������
     * @param brhlist String �Ŵ������µ�ʵ�����б�
     * @param date    String �������
     * @return String
     * @throws Exception
     */
    private String getUpdateSqlZrrqt2(String jgdm, String brhlist, String date) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("update upzrrqt q set (q.cw_zcze,q.cw_fzze,q.jinyxm,q.cw_yjnjysr)=(select  TOTASSETS*10000,TOTDEBT*10000,cast(MAINBUSINESS as varchar(40)),ANNUALINCOME*10000 from  CMINDVCLIENT where q.khh=clientno)");
        sql.append(" where q.jrjgdm='" + jgdm + "' and q.fljzrq='" + date + "'");
        sql.append(" and exists(select 1 from CMINDVCLIENT where q.khh=clientno )");
        return sql.toString();
    }

    /**
     * ������Ȼ������������Ϣ,���������
     *
     * @param jgdm    String �Ŵ����Ŷ�Ӧ��������������
     * @param brhlist String �Ŵ������µ�ʵ�����б�
     * @param date    String �������
     * @return String
     * @throws Exception
     */
    private String getUpdateSqlZrrqt3(String jgdm, String brhlist, String date) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("update upzrrqt q set (q.flly)=(select  cast(case when cmt.cmt4  is null then case when cmt.cmt3 is null then case when cmt.cmt2 is null then cmt.cmt1 else cmt.cmt2 end else cmt.cmt3 end else cmt.cmt4 end as varchar(254)) from  fccmt cmt where q.fcno=cmt.fcno and cmt.fccmttype=1)");
        sql.append(" where q.jrjgdm='" + jgdm + "' and q.fljzrq='" + date + "'");
        sql.append(" and exists(select 1 from fccmt where q.fcno=fcno and fccmttype=1 )");
        return sql.toString();
    }

    /**
     * ������Ȼ��һ�������Ϣ,���������
     *
     * @param jgdm    String �Ŵ����Ŷ�Ӧ��������������
     * @param brhlist String �Ŵ������µ�ʵ�����б�
     * @param date    String �������
     * @return String
     * @throws Exception
     */
    private String getUpdateSqlZrrnh(String jgdm, String brhlist, String date) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("update upzrrnh q set (q.fenlyj)=(select  cast(case when cmt.cmt4  is null then case when cmt.cmt3 is null then case when cmt.cmt2 is null then cmt.cmt1 else cmt.cmt2 end else cmt.cmt3 end else cmt.cmt4 end as varchar(254)) from  fccmt cmt where q.fcno=cmt.fcno and cmt.fccmttype=1)");
        sql.append(" where q.jrjgdm='" + jgdm + "' and q.fljzrq='" + date + "'");
        sql.append(" and exists(select 1 from fccmt where q.fcno=fcno and fccmttype=1 )");
        return sql.toString();
    }

    /**
     * ������Ȼ��һ��������������������Ϣ
     *
     * @param jgdm    String �Ŵ����Ŷ�Ӧ��������������
     * @param brhlist String �Ŵ������µ�ʵ�����б�
     * @param date    String �������
     * @return String
     * @throws Exception
     */
    private String getSqlZrrnh(String jgdm, String brhlist, String date) throws Exception {
        //������Ҫɸѡfcmain���е����ݣ�Ȼ�����upqsydk���С�
        //ö��ͬ��
        //������������=fcmain.PASTDUEDAYS,��Ϣ��������=null
        //���ʽ=rq.loantype3,upmaping.id
        //��������=rq.typeno,upmaping.id��Ϊfc.loanway
        //ΥԼ����=null
        StringBuffer sql = new StringBuffer();
        sql.append("insert into UPzrrnh(FCNO,brhid,jrjgdm,fljzrq,dkhthm,khh,kehumc,dkhtje,daikye,htsxrq,htzzrq,");
        sql.append("yuqits,lxyqts,daikfs,xinydj,fleijg,fenlyj,dbzk,daikzl,weiyqs)");
        //sql.append(" select  fc.fcno,fc.brhid,upm.upbranchid,fc.createdate,rq.contractno,rq.clientno,rq.clientname,");
        sql.append(" select  fc.fcno,fc.brhid,'" + jgdm + "',fc.createdate,case rq.contractno when '' then fc.fcno else rtrim(fc.fcno)||rq.contractno end,rq.clientno,rq.clientname,");
        //sql.append(" rq.contractamt,rq.nowbal,ct.startdate,ct.enddate,fc.PASTDUEDAYS,cast(null as integer),");
        sql.append(" rq.contractamt,rq.nowbal,rq.paydate,rq.enddate,case when fc.PASTDUEDAYS<=0 then 0 else fc.PASTDUEDAYS end,0,");
        sql.append(" m.id,m_1.id,m_2.id,");
        sql.append("cast(case when cmt.cmt4  is null then case when cmt.cmt3 is null then case when cmt.cmt2 is null then cmt.cmt1 else cmt.cmt2 end else cmt.cmt3 end else cmt.cmt4 end as varchar(254)),");
        //sql.append("cast(null as character),m_3.id,");
        sql.append("cast(null as character),'01',");//modified by weiyb on 20070608 Ĭ��Ϊ��Ȼ��һ��='01'
        sql.append(" 0");//ΥԼ����Ĭ��Ϊ0
        sql.append(" from fcmain fc  join rqloanlist rq on fc.bmno=rq.bmno ");
        sql.append(" left join cmcorpclient cm on rq.clientno=cm.clientno");
        sql.append(" left join bmcontract ct on fc.bmno=ct.bmno ");
        sql.append(" left join upmaping m on rq.loantype3=m.enutp and m.enuid='LoanType3'");
        sql.append(" left join upmaping m_1 on fc.creditclass=m_1.enutp and m_1.enuid='CreditClass'");
        sql.append(" left join upmaping m_2 on fc.fcclass=m_2.enutp and m_2.enuid='LoanCat1'");
        //sql.append(" left join upmaping m_3 on rq.typeno=m_3.enutp and m_3.enuid='LoanWay'");
        //FCBMAIN��Ч���Ͼ��滻Ϊ��
        sql.append(" left join upmaping m_3 on cast(fc.loanway as smallint)=m_3.enutp and m_3.enuid='LoanWay'");
        sql.append(" left join fccmt cmt on fc.fcno=cmt.fcno and cmt.fccmttype=1");//add by weiyb on 20070608
        //sql.append(" left join UPBRHID_MAPING upm on rq.bnkid=upm.branchid");
        //sql.append(" where fc.fctype=1 and fc.brhid='"+brhid+"' and fc.createdate='"+date+"'");
        sql.append(" where fc.fctype=1 and fc.brhid in " + brhlist + " and fc.createdate='" + date + "'");//�޸�Ϊ�Ŵ�����
        sql.append(" and fc.FCCRTTYPE=1");//ʱ����
        //�ų�rq.clientno is null�����
        sql.append(" and rq.clientno is not null");
        //�ų� fc.FCCLASS=0�����
        sql.append(" and fc.FCCLASS>0");
        //��������
        sql.append(" and not exists(select 1 from UPzrrnh where fcno=fc.fcno)");

        return sql.toString();
    }

    /**
     * �ж���ֽ����������Ƿ��ܹ��ˣ�
     *
     * @param dt      String ��ֽ�����
     * @param brhlist String ʵ�����б�
     * @return boolean
     * @throws Exception
     */
    public boolean isEndDate(String dt, String brhlist) throws Exception {
        //CachedRowSet rs=DB2_81.getRs("select count(*) as total from fcbmain where createdate='"+dt+"' and brhid in "+brhlist);
        CachedRowSet rs = DB2_81.getRs("select count(*) as total from fcbmain where createdate='" + dt + "'");
        int count = 0;
        while (rs.next()) {
            count = rs.getInt("total");
        }
        if (count > 0)
            return true;
        else
            return false;
    }

    /**
     * ���������������ڣ��ֱ�ɸѡ���ݲ��������ҵ���������Ϣ����Ȼ�˺�����΢����ҵ��Ϣ����Ȼ��һ�����������������Ϣ��
     *
     * @param brhid   String �Ŵ����������
     * @param jgdm    String �Ŵ����Ŷ�Ӧ����������
     * @param brhlist String �Ŵ������µ�ʵ�����б�
     * @param date    String �������
     * @return String
     * @throws Exception
     */
    public boolean impCategory(String brhid, String jgdm, String brhlist, String date) throws Exception {
        this.isFCDate(brhid, brhlist, date);
        //�����ж��Ƿ��������ӳ��
        int count = 0;
        String jrjgdm = "";
        CachedRowSet rs = DB2_81.getRs("select UPBRANCHID from upbrhid_maping where branchid='" + brhid + "'");
        while (rs.next()) {
            jrjgdm = rs.getString("UPBRANCHID");
        }
        rs.close();
        jrjgdm = jrjgdm.trim();
        if (jrjgdm.equals("")) throw new Exception("��û���������Ӧ��ϵ�����Ƚ�����Ӧ��ϵ��");

        //modified by weiyb on 2007-05-30
        //��һ�μ������ݰ���ǰ�Ĳ�����ʷ���ݴ��������Ժ��ͬһ�����ظ��������ݲ���Ҫ�ٴ�������
        boolean bQsydk = false;
        boolean bZrrqt = false;
        boolean bZrrnh = false;
        rs = DB2_81.getRs("select count(fcno) as total from upqsydk where jrjgdm='" + jgdm + "' and fljzrq='" + date + "'");
        while (rs.next()) {
            count = rs.getInt("total");
        }
        if (count > 0) bQsydk = true;
        rs = DB2_81.getRs("select count(fcno) as total from upzrrqt where jrjgdm='" + jgdm + "' and fljzrq='" + date + "'");
        while (rs.next()) {
            count = rs.getInt("total");
        }
        if (count > 0) bZrrqt = true;
        rs = DB2_81.getRs("select count(fcno) as total from upzrrnh where jrjgdm='" + jgdm + "' and fljzrq='" + date + "'");
        while (rs.next()) {
            count = rs.getInt("total");
        }
        if (count > 0) bZrrnh = true;
        String pl_sql[] = new String[10];
        pl_sql[0] = this.getSqlQsydk(jgdm, brhlist, date);
        if (!bQsydk)
            pl_sql[1] = this.getHistoryOfQsydk(jgdm, brhlist, date);//�����ǰ�������ݣ���ȡ��ǰ�����ݣ�����ÿ���ظ�¼��
        else
            pl_sql[1] = "delete from upqsydk where 1>2";
        pl_sql[2] = this.getSqlZrrqt(jgdm, brhlist, date);
        if (!bZrrqt)
            pl_sql[3] = this.getHistoryOfZrrqt(jgdm, brhlist, date);//�����ǰ�������ݣ���ȡ��ǰ�����ݣ�����ÿ���ظ�¼��
        else
            pl_sql[3] = "delete from upzrrqt where 1>2";
        pl_sql[4] = this.getUpdateSqlZrrqt(jgdm, brhlist, date);
        pl_sql[5] = this.getUpdateSqlZrrqt2(jgdm, brhlist, date);
        pl_sql[6] = this.getUpdateSqlZrrqt3(jgdm, brhlist, date);
        pl_sql[7] = this.getSqlZrrnh(jgdm, brhlist, date);
        if (!bZrrnh)
            pl_sql[8] = this.getHistoryOfZrrnh(jgdm, brhlist, date);//�����ǰ�������ݣ���ȡ��ǰ�����ݣ�����ÿ���ظ�¼��
        else
            pl_sql[8] = "delete from upzrrnh where 1>2";
        pl_sql[9] = this.getUpdateSqlZrrnh(jgdm, brhlist, date);
        return DB2_81.execBatch(pl_sql);
    }

    /**
     * �����Ŵ����ź�������ڣ��ֱ���ܵ���������硢�ͻ���Ϣ������ǰҪ�����Ŵ����ź���������ж�һ���Ƿ���ڣ���������򲻱ص��롣
     *
     * @param brnid   String �Ŵ����������
     * @param jgdm    String �Ŵ����Ŷ�Ӧ����������
     * @param brhlist String �Ŵ������µ�ʵ�����б�
     * @param date    String �������
     * @return boolean
     * @throws Exception
     */
    public boolean impDept(String brnid, String jgdm, String brhlist, String date) throws Exception {
        this.isFCDate(brnid, brhlist, date);
        //�����ж��Ƿ���ڻ���ӳ��
        String jrjgdm = "";
        CachedRowSet rs = DB2_81.getRs("select UPBRANCHID from upbrhid_maping where branchid='" + brnid + "'");
        while (rs.next()) {
            jrjgdm = rs.getString("UPBRANCHID");
        }
        rs.close();
        jrjgdm = jrjgdm.trim();
        if (jrjgdm.equals("")) throw new Exception("��û���������Ӧ��ϵ�����Ƚ�����Ӧ��ϵ��");
        //�����������£�����Ҫ�����ж��Ƿ��Ѿ�ͳ�ƹ���
        //this.impUPxlsxx(brnid,date);//����������
        this.impUPxysxx(brnid, jgdm, brhlist, date);//�����������
        this.impUPqsykh(jgdm, brhlist, date);//�����������ͻ���Ϣ
        return true;
    }

    /**
     * ������������Ϣ
     *
     * @param brhid String ��������
     * @param date  String �������
     * @throws Exception
     */
    public void impUPxlsxx(String brhid, String jgdm, String date) throws Exception {
        //�����絼�����ݵ�ʱ�����Ȳ���һ����¼����upxlsxx���С�
        //��������ǰ���ж϶�Ӧ�����㡢�����Ƿ��Ѿ����ڣ���������Ҫ��������
        //ȥ�������������Ϊ�յ��жϡ�
        CachedRowSet rs = null;
        int count = 0;
        /*
          rs=DB2_81.getRs("select count(1) as total from upzrrnh where jrjgdm = '"+jgdm+"' and fljzrq='"+date+"'");

          while (rs.next()){count=rs.getInt("total");}
          if(count<=0)
              throw new Exception("�����ʱ�������Ϊ�գ����ܵ�����");
          */
        String sExists = "select count(xx.serialno) as total from UPxlsxx xx,UPBRHID_MAPING upm,UPBRANCH ub where xx.jrjgdm=upm.UPBRANCHID and upm.UPBRANCHID=ub.branchid ";
        //sExists+=" and upm.BRANCHID like '"+brhid.substring(0,3)+"%' and ub.branchlevel=1 and xx.fljzrq='"+date+"'";
        sExists += " and upm.BRANCHID ='" + brhid + "'  and xx.fljzrq='" + date + "'";
        rs = DB2_81.getRs(sExists);
        count = 0;
        while (rs.next()) {
            count = rs.getInt("total");
        }
        rs.close();
        if (count > 0) {
            return;
        }
        StringBuffer sql = new StringBuffer();
        sql.append("insert into UPxlsxx(serialno,jrjgdm,jrjgmc,fljzrq)");
        sql.append(" select next value for S_UPXLSXX,ub.BRANCHID,ub.BRANCHNAME,'" + date + "' from UPBRHID_MAPING upm,UPBRANCH ub");
        //sql.append(" where upm.UPBRANCHID=ub.BRANCHID and upm.BRANCHID like '"+brhid.substring(0,3)+"%' and ub.branchlevel=1");
        sql.append(" where upm.UPBRANCHID=ub.BRANCHID and upm.BRANCHID ='" + brhid + "'");
        DB2_81.ExecCmd(sql.toString());
    }

    /**
     * �������������Ϣ�����Ŵ�������Ϣ����ÿ���Ŵ�����ֻͳ���Լ����Ŵ������µ���Ϣ
     *
     * @param brhid   String �Ŵ����ű��
     * @param brhlist String �Ŵ������µ�ʵ����
     * @param date    String �������
     * @return boolean
     * @throws Exception
     */
    private boolean impUPxysxx(String brhid, String jgdm, String brhlist, String date) throws Exception {
        /*
           *����ʵ��˵����
           * ����֮ǰ�ȼ�⣬�Ƿ��Ѿ�ͳ�ƹ��ˣ�ͳ������Ϊbrhid+date
           * ���Ȳ���������������Ϣ��Ȼ��ͨ����ֽ��л���ͳ���ܴ�������ʹ������ϼ���Ϣ�����Ҹ��µ��˱��С�
           * ����FCMAIN.FCTYPE(���ҵ������,����ö�ٱ�enuid='FCType')��FCMAIN.FCCLASS(�����϶����������ö�ٱ�enuid='LoanCat1')��
           * ͳ�ƻ��ܡ�
          */
        //�ж��Ƿ�ͳ�ƹ��ˣ����ͳ�ƹ��ˣ��򲻱���ͳ��
        //��ƽʽ�Ĺ����ºܶ��Ŵ����Ŷ�Ӧ����ֵ�һ���Ŵ����š�����Ҫȡ��Щ�Ŵ������µ���������ļ��ϲ��С�����ֻ��ͳ��һ���Ŵ������µ����ݡ�
        CachedRowSet rs1 = null;
        String unionbrhlist = "";
        SCBranch branch = new SCBranch();
        rs1 = DB2_81.getRs("select BRANCHID from UPBRHID_MAPING where UPBRANCHID='" + jgdm + "'");
        ArrayList list = new ArrayList();
        while (rs1.next()) {
            String s = rs1.getString("BRANCHID");
            if (s != null && s.trim().equals(""))
                continue;
            list.add(s);
        }
        for (int i = 0; i < list.size(); i++) {
            String tempbrhid = (String) list.get(i);
            unionbrhlist += branch.getAllSubBrh2(tempbrhid) + "," + tempbrhid + ",";
        }
        unionbrhlist = unionbrhlist.substring(0, unionbrhlist.length() - 1);
        unionbrhlist = "('" + unionbrhlist.replaceAll(",", "','") + "')";

        rs1 = DB2_81.getRs("select count(xx.serialno) as total from UPxysxx xx where xx.jrjgdm='" + jgdm + "' and xx.builddate='" + date + "'");
        int count = 0;
        while (rs1.next()) {
            count = rs1.getInt("total");
        }
        rs1.close();
        if (count > 0) return false;
        DatabaseConnection dbcm = new DatabaseConnection();
        java.sql.Connection conn = dbcm.getConnection();
        Statement stmt = null;
        if (conn == null) throw new Exception("���ݿ����Ӵ���");
        conn.setAutoCommit(false);
        //1.����
        StringBuffer sql = new StringBuffer();
        sql.append("insert into UPxysxx(serialno,jrjgdm,xlsjgdm,jrjgmc,builddate)");
        sql.append(" select NEXT VALUE FOR S_UPXYSXX,upm.upbranchid,ub.fbranchid,ub.branchname,'" + date + "' from scbranch sb ");
        sql.append(" left join UPBRHID_MAPING upm on sb.brhid=upm.branchid");
        sql.append(" join UPBRANCH ub on upm.upbranchid=ub.branchid");
        sql.append(" where sb.brhid in " + unionbrhlist + " fetch first 1 rows only");
        //System.out.println(sql);

        //2.ͳ��
        StringBuffer sql1 = new StringBuffer();
        sql1.append("select count(1) as total,sum(t.bal) as bal,t.fctype,t.fcclass from ");
        sql1.append(" (select fc.* from fcmain fc");
        //�ų�rq.clientno is null�����
        //sql1.append(" join rqloanlist rq on fc.bmno=rq.bmno where rq.bnkid='"+brhid+"' and rq.clientno is not null and fc.createdate='"+date+"' and fc.FCCRTTYPE=1) as t");
        sql1.append(" join rqloanlist rq on fc.bmno=rq.bmno where rq.brhid in " + unionbrhlist + " and rq.clientno is not null and fc.createdate='" + date + "' and fc.FCCRTTYPE=1) as t");
        sql1.append(" group by t.FCTYPE,t.FCCLASS");
        //System.out.println(sql1);
        //�ų�rq.clientno is null�����
        //3.ͨ��ͳ�Ƴ������ݣ��ֱ���±�����Ӧ���ֶ�,������Ȼ��������΢����ҵ����Ҫ�ϲ�(����fctype=2 or fctype=4�������)
        //ʵ�ַ������ȶ���һ�����飬�Ѽ�¼���ŵ������У�Ȼ����֯sql���ʵ�ָ��¡�
        BigDecimal[][][] info = new BigDecimal[5][4][2];
        //�����ʼ������
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                info[i][j][0] = new BigDecimal("0");
                info[i][j][1] = new BigDecimal("0");
            }
        }
        CachedRowSet rs = DB2_81.getRs(sql1.toString());
        while (rs.next()) {
            String fctype = rs.getString("fctype");
            String fcclass = rs.getString("fcclass");
            int ifctype = Integer.parseInt(fctype);
            int ifcclass = Integer.parseInt(fcclass);
            if (ifcclass == 0) continue;
            info[ifcclass - 1][ifctype - 1][0] = rs.getBigDecimal("total");
            info[ifcclass - 1][ifctype - 1][1] = rs.getBigDecimal("bal");
        }
        rs.close();
        //������Ӧ�ֶΣ���fctypeΪ2��4ʱ��Ӧ��sumһ��ֵ���ϲ�Ϊһ��ֵ����Ϊ����Ȼ��������΢����ҵ��
        info[0][1][0] = info[0][1][0].add(info[0][3][0]);
        info[0][1][1] = info[0][1][1].add(info[0][3][1]);
        info[1][1][0] = info[1][1][0].add(info[1][3][0]);
        info[1][1][1] = info[1][1][1].add(info[1][3][1]);
        info[2][1][0] = info[2][1][0].add(info[2][3][0]);
        info[2][1][1] = info[2][1][1].add(info[2][3][1]);
        info[3][1][0] = info[3][1][0].add(info[3][3][0]);
        info[3][1][1] = info[3][1][1].add(info[3][3][1]);
        info[4][1][0] = info[4][1][0].add(info[4][3][0]);
        info[4][1][1] = info[4][1][1].add(info[4][3][1]);
        /*����
           *  FCType	3	��ҵ��
           *	FCType 	2	��Ȼ������
           *	FCType 	1	��Ȼ��ũ��
           *	FCType 	4	΢����ҵ
           *
           * 	LoanCat1	1	����
           *	LoanCat1	2	��ע
           *	LoanCat1	3	�μ�
           *	LoanCat1	4	����
           *	LoanCat1	5	��ʧ
           *
           *qybs1 ����������ҵ��������ϼ�<qybs1>	0,2,0
           *qyye1	����������ҵ�������ϼ�<qyye1>	0,2,1
           *qybs2	��ע������ҵ��������ϼ�<qybs2>	1,2,0
           *qyye2	��ע������ҵ�������ϼ�<qyye2>	1,2,1
           *qybs3	�μ�������ҵ��������ϼ�<qybs3>	2,2,0
           *qyye3	�μ�������ҵ�������ϼ�<qyye3>	2,2,1
           *qybs4	����������ҵ��������ϼ�<qybs4>	3,2,0
           *qyye4	����������ҵ�������ϼ�<qyye4>	3,2,1
           *qybs5	��ʧ������ҵ��������ϼ�<qybs5>	4,2,0
           *qyye5	��ʧ������ҵ�������ϼ�<qyye5>	4,2,1
           *nhbs1	��������Ȼ��һ����������������ϼ�<nhbs1>	 0,0,0
           *nhye1	��������Ȼ��һ���������������ϼ�<nhye1>	 0,0,1
           *nhbs2	��ע����Ȼ��һ����������������ϼ�<nhbs2>	 1,0,0
           *nhye2	��ע����Ȼ��һ���������������ϼ�<nhye2>	 1,0,1
           *nhbs3	�μ�����Ȼ��һ����������������ϼ�<nhbs3>	 2,0,0
           *nhye3	�μ�����Ȼ��һ���������������ϼ�<nhye3>	 2,0,1
           *nhbs4	��������Ȼ��һ����������������ϼ�<nhbs4>	 3,0,0
           *nhye4	��������Ȼ��һ���������������ϼ�<nhye4>	 3,0,1
           *nhbs5	��ʧ����Ȼ��һ����������������ϼ�<nhbs5>	 4,0,0
           *nhye5	��ʧ����Ȼ��һ���������������ϼ�<nhye5>	 4,0,1
           *qtbs1	��������Ȼ��������΢����ҵ��������ϼ�<qtbs1>	0,(1,3),0
           *qtye1	��������Ȼ��������΢����ҵ�������ϼ�<qtye1>	0,(1,3),1
           *qtbs2	��ע����Ȼ��������΢����ҵ��������ϼ�<qtbs2>	1,(1,3),0
           *qtye2	��ע����Ȼ��������΢����ҵ�������ϼ�<qtye2>	1,(1,3),1
           *qtbs3	�μ�����Ȼ��������΢����ҵ��������ϼ�<qtbs3>	2,(1,3),0
           *qtye3	�μ�����Ȼ��������΢����ҵ�������ϼ�<qtye3>	2,(1,3),1
           *qtbs4	��������Ȼ��������΢����ҵ��������ϼ�<qtbs4>	3,(1,3),0
           *qtye4	��������Ȼ��������΢����ҵ�������ϼ�<qtye4>	3,(1,3),1
           *qtbs5	��ʧ����Ȼ��������΢����ҵ��������ϼ�<qtbs5>	4,(1,3),0
           *qtye5	��ʧ����Ȼ��������΢����ҵ�������ϼ�<qtye5>	4,(1,3),1
           * */
        //System.out.println(">>>>>>>>>>>>>>>="+info[0][2][1]);
        StringBuffer sql2 = new StringBuffer();
        sql2.append("update upxysxx xx set ");
        sql2.append("(xx.qybs1,xx.qyye1,xx.qybs2,xx.qyye2,");
        sql2.append("xx.qybs3,xx.qyye3,xx.qybs4,xx.qyye4,");
        sql2.append("xx.qybs5,xx.qyye5,");
        sql2.append("xx.nhbs1,xx.nhye1,xx.nhbs2,xx.nhye2,");
        sql2.append("xx.nhbs3,xx.nhye3,xx.nhbs4,xx.nhye4,");
        sql2.append("xx.nhbs5,xx.nhye5,");
        sql2.append("xx.qtbs1,xx.qtye1,xx.qtbs2,xx.qtye2,");
        sql2.append("xx.qtbs3,xx.qtye3,xx.qtbs4,xx.qtye4,");
        sql2.append("xx.qtbs5,xx.qtye5");
        sql2.append(")=(");
        sql2.append(info[0][2][0] + "," + info[0][2][1] + "," + info[1][2][0] + "," + info[1][2][1] + ",");
        sql2.append(info[2][2][0] + "," + info[2][2][1] + "," + info[3][2][0] + "," + info[3][2][1] + "," + info[4][2][0] + "," + info[4][2][1] + ",");
        sql2.append(info[0][0][0] + "," + info[0][0][1] + "," + info[1][0][0] + "," + info[1][0][1] + ",");
        sql2.append(info[2][0][0] + "," + info[2][0][1] + "," + info[3][0][0] + "," + info[3][0][1] + "," + info[4][0][0] + "," + info[4][0][1] + ",");
        sql2.append(info[0][1][0] + "," + info[0][1][1] + "," + info[1][1][0] + "," + info[1][1][1] + ",");
        sql2.append(info[2][1][0] + "," + info[2][1][1] + "," + info[3][1][0] + "," + info[3][1][1] + "," + info[4][1][0] + "," + info[4][1][1]);
        //sql2.append(") where xx.BUILDDATE='"+date+"' and exists(select 1 from upbrhid_maping upm where xx.jrjgdm=upm.upbranchid  and upm.branchid='"+brhid+"') ");
        //sql2.append(") where xx.BUILDDATE='"+date+"' and exists(select 1 from upbrhid_maping upm where xx.jrjgdm=upm.upbranchid  and upm.branchid in "+brhlist+") ");
        sql2.append(") where xx.BUILDDATE='" + date + "' and  xx.jrjgdm='" + jgdm + "'");

        //ִ���������
        try {
            stmt = conn.createStatement();
            if (stmt.executeUpdate(sql.toString()) < 0) {
                conn.rollback();
                stmt.close();
                return false;
            }
            System.out.println(sql2.toString());
            if (stmt.executeUpdate(sql2.toString()) < 0) {
                conn.rollback();
                stmt.close();
                return false;
            }
            return true;
        }
        catch (Exception ex) {
            conn.rollback();
            throw new Exception(ex.getMessage());
        }
        finally {
            if (conn != null) {
                conn.close();
            }

        }
    }

    /**
     * ��������ҵ����ͻ���Ϣ
     *
     * @param jgdm    String �Ŵ����Ŷ�Ӧ��������������
     * @param brhlist String �Ŵ������µ�ʵ����
     * @param date    String �������
     * @return boolean
     * @throws Exception
     */
    private boolean impUPqsykh(String jgdm, String brhlist, String date) throws Exception {
        /*
           * ����ʵ��˵����
           * ֻͳ�Ʊ��Ŵ������µ��û���
           * ͳ��֮ǰ�ȼ���Ƿ��Ѿ�ͳ�ƹ��ˣ�ͳ������Ϊbrhid��createdate
           * �ͻ���Ϣ����Ҫͨ����ֱ�FCMAIN�ʹ���̨�ʱ�RQLOANLIST(ʱ�����)���������Ŀͻ������Ȱѹ��������Ŀͻ���Ϣ���뵽����ҵ����ͻ���Ϣ�С�
           * ��Ӫҵ������=null
           * ͳ��FCMAIN.FCTYPE=3(��ҵ��)
           * �������������
           * */
        //�ж��Ƿ�ͳ�ƹ��ˣ����ͳ�ƹ��ˣ���ֱ�ӷ���
        CachedRowSet rs = null;
        /*
          CachedRowSet rs=DB2_81.getRs("select upbranchid from UPBRHID_MAPING where branchid='"+brhid+"'");
          String upbranchid=null;
          while(rs.next()){upbranchid=rs.getString("upbranchid");}
          if (upbranchid==null )throw new Exception("����ͻ���Ϣ������û�趨������Ŷ�Ӧ��ϵ�������趨��Ӧ��ϵ��");
          */
        //rs=DB2_81.getRs("select count(serialno) as total from UPqsykh where jrjgdm='"+upbranchid+"' and builddate='"+date+"'");
        /*
           * modified by weiyb on 2007-5-30
           * ȥ��ֻ������һ�����ݵ����ƣ����Զ���������ݣ��������Ŵ�ϵͳ��ȡ���ݣ�Ȼ����µ�ƫ���ϵͳ�У�
          rs=DB2_81.getRs("select count(serialno) as total from UPqsykh where jrjgdm='"+jgdm+"' and builddate='"+date+"'");
          int count=0;
          while (rs.next())
          {
              count=rs.getInt("total");
          }
          rs.close();
          if(count>0) return false;
          */
        String pl_sql[] = new String[8];
        //1.����ͻ�������Ϣ��UPqsykh��
        StringBuffer sql = new StringBuffer();
        sql.append("insert into UPqsykh(serialno,jrjgdm,khh,zhgwmc,jkrxzh,djzcrq,zczj,zyywsr,xinydj,builddate,faddbr)");
        //sql.append(" select next value for S_UPQSYKH,'"+upbranchid+"',cm.clientno,cm.name,m.id,cm.appdate,cm.CAPITALAMT,cast(null as decimal),m_1.id,'"+date+"'");
        sql.append(" select next value for S_UPQSYKH,'" + jgdm + "',cm.clientno,cm.name,m.id,cm.FOUNDDATE,cm.CAPITALAMT*10000,cast(null as decimal),m_1.id,'" + date + "',cm.LAWPERSON");
        sql.append(" from cmcorpclient cm");
        sql.append(" left join upmaping m on cm.clienttype=m.enutp ");
        sql.append(" left join upmaping m_1 on cm.CREDITCLASS=m_1.enutp ");
        sql.append(" where exists(");
        sql.append(" select rq.clientno from fcmain fc");
        sql.append(" left join rqloanlist rq on fc.bmno=rq.bmno ");
        //sql.append(" where rq.bnkid='"+brhid+"' and fc.createdate='"+date+"' and fc.FCCRTTYPE=1 and cm.clientno=rq.clientno)");
        sql.append(" where rq.brhid in " + brhlist + " and fc.createdate='" + date + "' and fc.FCCRTTYPE=1 and fc.fctype=3 and fc.FCCLASS>0 and cm.clientno=rq.clientno )");//�޸�
        sql.append(" and m.enuid='ClientType2'");
        sql.append(" and m_1.enuid='CreditClass'");
        sql.append(" and not exists(select 1 from upqsykh where cm.clientno=khh and builddate='" + date + "' and jrjgdm='" + jgdm + "')");//add by weiyb on 2007-05-30
        pl_sql[0] = sql.toString();

        //2.ͳ�ƿͻ��ʲ����,fc.fctype=3������ҵ��
        //  2.1.1 ͳ�Ƹ�������ʹ�����
        sql.delete(0, sql.length());
        sql.append("update UPqsykh q set (q.sjmj1,q.pgjz1)=(select t.area,t.estimate*10000 from (");
        sql.append("select cr.clientno, sum(cr.AREA) as area,sum(cr.ESTIMATE) as estimate from CMCORPLANDHOLDING cr");
        sql.append(" join cmcorpclient cm on cm.clientno=cr.clientno");
        //sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.bnkid='"+brhid+"' and fc.createdate='"+date+"')");
        sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.brhid in " + brhlist + " and fc.createdate='" + date + "')");
        sql.append(" group by cr.clientno)as t where t.clientno=q.khh ) where q.builddate='" + date + "'");

        sql.append(" and exists(select 1 from (select cr.clientno from CMCORPLANDHOLDING cr");
        sql.append(" join cmcorpclient cm on cm.clientno=cr.clientno");
        //sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.bnkid='"+brhid+"' and fc.createdate='"+date+"')");
        sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.brhid in " + brhlist + " and fc.createdate='" + date + "')");
        sql.append(" ) as tt where tt.clientno=q.khh)");

        pl_sql[1] = sql.toString();

        //	2.1.2 ͳ�Ƹ��������Ѱ�֤���
        sql.delete(0, sql.length());
        sql.append("update UPqsykh q set (q.ybzmj1)=(select t.area from (");
        sql.append("select cr.clientno, sum(cr.AREA) as area from CMCORPLANDHOLDING cr");
        sql.append(" join cmcorpclient cm on cm.clientno=cr.clientno");
        //sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.bnkid='"+brhid+"' and fc.createdate='"+date+"')");
        sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.brhid in " + brhlist + " and fc.createdate='" + date + "')");
        sql.append(" and cr.LANDCERTNO is not null");
        sql.append(" group by cr.clientno)as t where t.clientno=q.khh ) where q.builddate='" + date + "'");

        sql.append(" and exists(select 1 from (select cr.clientno from CMCORPLANDHOLDING cr");
        sql.append(" join cmcorpclient cm on cm.clientno=cr.clientno");
        //sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.bnkid='"+brhid+"' and fc.createdate='"+date+"')");
        sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.brhid in " + brhlist + " and fc.createdate='" + date + "')");
        sql.append(" and cr.LANDCERTNO is not null) as tt where tt.clientno=q.khh)");

        pl_sql[2] = sql.toString();
        //  2.2.1 ͳ�Ƹ��·��ز����
        sql.delete(0, sql.length());
        sql.append("update UPqsykh q set (q.sjmj2,q.pgjz2)=(select t.area,t.estimate*10000 from (");
        sql.append("select cr.clientno, sum(cr.AREA) as area,sum(cr.ESTIMATE) as estimate from CMCORPESTATE cr");
        sql.append(" join cmcorpclient cm on cm.clientno=cr.clientno");
        //sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.bnkid='"+brhid+"' and fc.createdate='"+date+"')");
        sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.brhid in " + brhlist + " and fc.createdate='" + date + "')");
        sql.append(" group by cr.clientno)as t where t.clientno=q.khh ) where q.builddate='" + date + "'");

        sql.append(" and exists(select 1 from (select cr.clientno from CMCORPESTATE cr");
        sql.append(" join cmcorpclient cm on cm.clientno=cr.clientno");
        //sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.bnkid='"+brhid+"' and fc.createdate='"+date+"')");
        sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.brhid  in " + brhlist + " and fc.createdate='" + date + "')");
        sql.append(") as tt where tt.clientno=q.khh)");

        pl_sql[3] = sql.toString();
        //	2.2.2 ͳ�Ƹ��·��ز��Ѱ�֤���
        sql.delete(0, sql.length());
        sql.append("update UPqsykh q set (q.ybzmj2)=(select t.area from (");
        sql.append("select cr.clientno, sum(cr.AREA) as area from CMCORPESTATE cr");
        sql.append(" join cmcorpclient cm on cm.clientno=cr.clientno");
        //sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.bnkid='"+brhid+"' and fc.createdate='"+date+"')");
        sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.brhid in " + brhlist + " and fc.createdate='" + date + "')");
        sql.append(" and cr.ESTATEREGNO is not null ");
        sql.append(" group by cr.clientno)as t where t.clientno=q.khh ) where q.builddate='" + date + "'");

        sql.append(" and exists(select 1 from (select cr.clientno from CMCORPESTATE cr");
        sql.append(" join cmcorpclient cm on cm.clientno=cr.clientno");
        //sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.bnkid='"+brhid+"' and fc.createdate='"+date+"')");
        sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.brhid in " + brhlist + " and fc.createdate='" + date + "')");
        sql.append(" and cr.ESTATEREGNO is not null) as tt where tt.clientno=q.khh) ");

        pl_sql[4] = sql.toString();
        //  2.3.1 ͳ�Ƹ�����ҵ�豸���
        sql.delete(0, sql.length());
        sql.append("update UPqsykh q set (q.sjmj3,q.pgjz3)=(select t.qty,t.estimate*10000 from (");
        sql.append("select cr.clientno, sum(cr.qty) as qty,sum(cr.ESTIMATE) as estimate from CMCORPEQUIPMENT cr");
        sql.append(" join cmcorpclient cm on cm.clientno=cr.clientno");
        //sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.bnkid='"+brhid+"' and fc.createdate='"+date+"')");
        sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.brhid in " + brhlist + " and fc.createdate='" + date + "')");
        sql.append(" group by cr.clientno)as t where t.clientno=q.khh ) where q.builddate='" + date + "'");

        sql.append(" and exists(select 1 from (select cr.clientno from CMCORPEQUIPMENT cr");
        sql.append(" join cmcorpclient cm on cm.clientno=cr.clientno");
        //sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.bnkid='"+brhid+"' and fc.createdate='"+date+"')");
        sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.brhid in " + brhlist + " and fc.createdate='" + date + "')");
        sql.append(" ) as tt where tt.clientno=q.khh)");

        pl_sql[5] = sql.toString();

        //	2.3.2 ͳ�Ƹ�����ҵ�豸�Ѱ�֤���
        sql.delete(0, sql.length());
        sql.append("update UPqsykh q set (q.ybzmj3)=(select t.qty from (");
        sql.append("select cr.clientno, sum(cr.qty) as qty from CMCORPEQUIPMENT cr");
        sql.append(" join cmcorpclient cm on cm.clientno=cr.clientno");
        //sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.bnkid='"+brhid+"' and fc.createdate='"+date+"')");
        sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.brhid in " + brhlist + " and fc.createdate='" + date + "')");
        sql.append(" and cr.REGNO is not null");
        sql.append(" group by cr.clientno)as t where t.clientno=q.khh) where q.builddate='" + date + "'");

        sql.append(" and exists(select 1 from (select cr.clientno from CMCORPEQUIPMENT cr");
        sql.append(" join cmcorpclient cm on cm.clientno=cr.clientno");
        //sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.bnkid='"+brhid+"' and fc.createdate='"+date+"')");
        sql.append(" where exists (select 1 from rqloanlist rq  join fcmain fc on rq.bmno=fc.bmno where cm.clientno=rq.clientno and fc.fctype=3 and rq.brhid in " + brhlist + " and fc.createdate='" + date + "')");
        sql.append(" and cr.REGNO is not null) as tt where tt.clientno=q.khh)");

        pl_sql[6] = sql.toString();


        //3.���²�����Ϣ modified by weiyb on 2007-05-21
        //select * from fcmcmt where FCCmtType=10
        //FCCmtType��10����������
        /*
          sql.delete(0, sql.length());
          sql.append("update upqsykh q set (q.cwzbrq,q.cwzb1,q.cwzb2,q.cwzb3,q.cwzb4,q.cwzb5,q.cwzb6,q.cwzb7,q.cwzb8,q.cwzb9,q.cwzb10)=");
          sql.append(" (select dt,amt1,amt2,amt9,amt10,amt3,amt4,amt5,amt6,amt7,amt8 from fcmcmt m");
          sql.append(" join fcmain fc on fc.fcno=m.fcno");
          sql.append(" join rqloanlist rq on rq.bmno=fc.bmno ");
          sql.append(" where FCCmtType=10 and rq.brhid in "+brhlist);
          sql.append(" and q.khh=rq.clientno");
          sql.append(" order by dt desc");
          sql.append(" fetch first 1 rows only)");
          sql.append(" where q.jrjgdm='"+jgdm+"' and q.builddate='"+date+"'");
          sql.append(" and exists(select 1 from fcmcmt  mt");
          sql.append(" join fcmain fc on fc.fcno=mt.fcno");
          sql.append(" join rqloanlist rq on rq.bmno=fc.bmno ");
          sql.append(" where mt.FCCmtType=10 and rq.brhid in "+brhlist+" and q.khh=rq.clientno)");
          */
        //modified by weiyb on 2007-5-28
        //��Ϊ��fcqycw���弶������ҵ����������Ϣ���л�ȡ��ز�����Ϣ��Ҫ��ͻ��š�ͳ�����ڵ��·ݸ�fcqycw�еĿͻ��š���������(���ͳ������ǰ����·�)���·���ͬ�ļ�¼
        String dt = date.substring(0, date.length() - 2) + "01";
        sql.delete(0, sql.length());
        sql.append("update upqsykh q set (q.cwzbrq,q.cwzb1,q.cwzb2,q.cwzb3,q.cwzb4,q.cwzb5,");
        sql.append("q.cwzb6,q.cwzb7,q.cwzb8,q.cwzb9,q.cwzb10,q.zyywsr,q.dbfx,q.cwfx,q.fcwfx,q.hukly1,q.hukly2,q.hukly3,q.qtsxsm)=");
        sql.append("(select dt,cuzb1*10000,cuzb2*10000,cuzb3*10000,cuzb4*10000,cuzb5,cuzb6,cuzb7,cuzb8,cuzb9,cuzb10,cuzb11,cast(dbfx as varchar(254)),cast(cwfx as varchar(254)),cast(fcwfx as varchar(254)),cast(hkly1 as varchar(100)),cast(hkly2 as varchar(100)),cast(hkly3 as varchar(100)),cast(qtsm as varchar(254))");
        sql.append(" from fcqycw w where q.khh=w.clientno  and w.dt=(select max(dt) from fcqycw where clientno=q.khh and dt<'" + dt + "'))");
        sql.append(" where q.jrjgdm='" + jgdm + "' and q.builddate='" + date + "' and exists(select 1 from fcqycw where q.khh=clientno");
        sql.append("  and dt=(select max(dt) from fcqycw where clientno=q.khh and dt<'" + dt + "') )");
        pl_sql[7] = sql.toString();
        return DB2_81.execBatch(pl_sql);

    }

    /**
     * �ж���������Ƿ��Ӧ�������,ȥ����FCMAIN
     *
     * @param brhid String �Ŵ����Ŷ�Ӧ�������
     * @param date  String �������
     * @return boolean
     * @throws Exception
     */
    public boolean isFCDate(String brhid, String brhlist, String date) throws Exception {
        //�ж���������趨���Ƿ��ڱ�����֡�
        int count = 0;
        String sql = "select count(1) as total from fcmain where createdate='" + date + "' and brhid in " + brhlist;
        CachedRowSet rs = DB2_81.getRs(sql);
        while (rs.next()) {
            count = rs.getInt("total");
        }
        rs.close();
        if (count > 0)
            return true;
        else
            throw new Exception("�������޴���ֽ������ڣ���ѡ����ʵ����ڣ�");
    }

    /**
     * ��ȡ���ʱ���б�
     *
     * @return String
     * @throws Exception
     */
    public String getFCDateList() throws Exception {
        String ret = "";
        String sql = "select dt from fcprd where initialized=1 order by dt desc";
        CachedRowSet rs = DB2_81.getRs(sql);
        while (rs.next()) {
            String dt = rs.getString("dt");
            ret += "<option value='" + dt + "'>" + dt + "</option>";
        }
        return ret;
    }

    /**
     * �жϵ�¼�����Ƿ�������
     *
     * @param brhid String ���������
     * @return boolean
     * @throws Exception
     */
    public boolean isXls(String brhid) throws Exception {
        int count = 0;
        String sql = "select count(1) as total from scbranch where brhid='" + brhid + "' and brhlevel=2 and BRHTYPE=9";
        CachedRowSet rs = DB2_81.getRs(sql);
        while (rs.next()) {
            count = rs.getInt("total");
        }
        rs.close();
        if (count > 0)
            return true;
        else
            throw new Exception("��������û���¼����ʹ�ñ����ܣ�");
    }

    /**
     * �жϵ�¼�����Ƿ��Ŵ�����
     *
     * @param brhid String �Ŵ����ű���
     * @return boolean
     * @throws Exception
     */
    public boolean isXys(String brhid) throws Exception {
        int count = 0;
        String sql = "select count(1) as total from scbranch where brhid='" + brhid + "' and brhlevel=3";
        CachedRowSet rs = DB2_81.getRs(sql);
        while (rs.next()) {
            count = rs.getInt("total");
        }
        rs.close();
        if (count > 0)
            return true;
        else
            throw new Exception("�Ŵ������û���¼����ʹ�ñ����ܣ�");
    }

/*
    public void expToXML(String src,String dest,String bnkid,String date)throws Exception
    {
        //�������ݵ�xml�ļ���srcΪxmlģ�棬destΪ������Ŀ���ļ�
        //�����Ŵ����ŵ�¼�ţ�ͨ��ӳ���ȡ�Ŵ����Ż�����
        String upbranchid="";
        CachedRowSet rs=DB2_81.getRs("select upbranchid from upbrhid_maping where branchid='"+bnkid+"'");
        while(rs.next())
        {
            upbranchid=rs.getString("upbranchid");
        }
        //xml����ģ�棬�õ�����

        String tablename="";
        //���ݱ����õ�����
        this.readData(tablename, upbranchid, date);

        //д���ݵ�XML�ļ�
    }
    */

    /**
     * ���ݱ������,��ȡö�ٱ���,�γ�optionѡ��.
     *
     * @param colName ����
     * @return String
     * @throws Exception
     */
    public String getEnumStr(String colName) throws Exception {
        String ret = "";
        String sql = "select enutp,enudt from upenuminfodetl where enuid='" + colName + "'";
        rs = DB2_81.getRs(sql);
        while (rs.next()) {
            ret += "<option value='" + rs.getString("enutp") + "'>" + rs.getString("enutp") + "--" + DBUtil.fromDB(rs.getString("enudt")) + "</option>";
        }
        return ret;
    }

    /**
     * ��ȡĳ�������µĸ����˻�ͻ�����
     *
     * @param brhid String ������
     * @return String
     * @throws Exception
     */
    public String getUserNameList(String brhid) throws Exception {
        String ret = "";
        String sql = "select LOGINNAME,USERNAME from SCUSER where brhid='" + brhid + "' and usertype<'3'";//�ų�ϵͳ����Ա
        CachedRowSet rs;
        rs = DB2_81.getRs(sql);
        while (rs.next()) {
            ret += "<option value='" + DBUtil.fromDB2(rs.getString("loginname")) + "'>" + DBUtil.fromDB2(rs.getString("username")) + "</option>";
        }
        return ret;
    }

    /**
     * ���ݱ�������ȡ��Ҫ������,bnkid���������
     *
     * @param tablename String ����
     * @param bnkid     String �������Ӧ��������������
     * @param date      String �������
     * @return CachedRowSet
     * @throws Exception
     */
    private CachedRowSet readData(String tablename, String bnkid, String date) throws Exception {
        String sql = "";
        if (tablename.equals("xlsxx"))//������
            //sql="select * from upxlsxx where jrjgdm='"+bnkid.substring(0,4)+"' and fljzrq='"+date+"'";
            sql = "select * from upxlsxx where jrjgdm='" + bnkid.substring(0, bnkid.length() - 2) + "00' and fljzrq='" + date + "'";
        else if (tablename.equals("xysxx"))//������
            sql = "select * from upxysxx where jrjgdm = '" + bnkid + "' and BUILDDATE='" + date + "'";
        else if (tablename.equals("qsykh"))//�ͻ���Ϣ
        {
            sql = "select kh.* from upqsykh kh where kh.jrjgdm = '" + bnkid + "' and kh.BUILDDATE='" + date + "' and exists(select 1 from upqsydk where kh.khh=khh";
            sql += " and jrjgdm = '" + bnkid + "' and fljzrq='" + date + "' and fleijg in ('1','2'))";
        } else if (tablename.equals("qsydk"))//����ҵ���������Ϣ
            sql = "select * from upqsydk where jrjgdm = '" + bnkid + "' and fljzrq='" + date + "' and fleijg in ('1','2')";
        else if (tablename.equals("zrrqt"))//��Ȼ��������΢����ҵ������Ϣ
            sql = "select * from upzrrqt where jrjgdm = '" + bnkid + "' and fljzrq='" + date + "' and fleijg in ('1','2')";
        else if (tablename.equals("zrrnh"))//��Ȼ��һ��������������������Ϣ
            sql = "select * from upzrrnh where jrjgdm = '" + bnkid + "' and fljzrq='" + date + "' and fleijg in ('1','2')";
        return DB2_81.getRs(sql);

    }

    /**
     * ������Excel
     *
     * @param s       OutputStream ������
     * @param jgdm    String �������Ӧ��������������
     * @param enddate String ��������
     * @throws Exception
     */
    public void exportXML(OutputStream s, String jgdm, String enddate) throws Exception {
        String filename = "test.xml";
        String xmlname = "module.xml";
        String encoding = "gb2312";
        FcUpXML test = new FcUpXML();
        Document doc = null;

        Document doc1 = DocumentHelper.createDocument();
        Element root1 = doc1.addElement("ccboot");
        doc = test.loadXML(xmlname);
        Element root = doc.getRootElement();
        test.parseXML(root, root1, jgdm, enddate);
        test.storeDoc(doc1, filename, encoding, s);
    }

    /**
     * ����XMLģ�棬������ģ��ѡ�����ݣ��γ�xml������
     *
     * @param el    Element XMLԪ��
     * @param root  Element XML��Ԫ��
     * @param bnkid bnkid ������
     * @param date  String �������
     * @throws Exception
     */
    public void parseXML(Element el, Element root, String bnkid, String date) throws Exception {
        for (Iterator i = el.elementIterator(); i.hasNext();) {
            Element element = (Element) i.next();
            Element temp = root.addElement(element.getName());

            if (element.attribute("table") != null) {
                //System.out.println(element.getName()+"====table==="+element.attributeValue("table"));
                rs = this.readData(element.attributeValue("table"), bnkid, date);
                while (rs.next()) {

                    this.invoke(element, temp, bnkid, date);
                }
            }

        }

    }

    /**
     * �ݹ����
     *
     * @param el    Element XMLԪ��
     * @param root  Element XML��Ԫ��
     * @param bnkid bnkid ������
     * @param date  String �������
     * @throws Exception
     */
    private void invoke(Element el, Element root, String bnkid, String date) throws Exception {
        for (Iterator i = el.elementIterator(); i.hasNext();) {
            Element element = (Element) i.next();
            Element temp = root.addElement(element.getName());
            if (element.attribute("type") != null) {
                String s = DBUtil.fromDB(rs.getString(element.getName()));
                //System.out.println(element.getName()+"====type==="+element.attributeValue("type"));
                String value = element.attributeValue("type");
                if (value.equals("float")) {

                    temp.addText(s == null ? "0.00" : s);
                } else if (value.equals("date")) {

                    temp.addText(s == null ? "" : DBUtil.to_Date(s));
                } else {
                    temp.addText(s == null ? "" : s);
                }


            }
            invoke(element, temp, bnkid, date);
        }
    }

    /**
     * ���XML��ĳ�豸
     *
     * @param doc Document ����
     * @throws IOException
     */
    public void printDoc(Document doc) throws IOException {
        Writer out = new OutputStreamWriter(System.out, "gb2312");
        OutputFormat format = OutputFormat.createPrettyPrint();
        //OutputFormat format =OutputFormat.createCompactFormat();
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(doc);
        out.flush();
    }

    /**
     * ����XML���ļ�
     *
     * @param doc      Document ����
     * @param filename filename �ļ���
     * @param encoding encoding ����
     * @param s        OutputStream ������
     * @throws IOException
     */
    public void storeDoc(Document doc, String filename, String encoding, OutputStream s) throws IOException {
        //Writer out = new OutputStreamWriter(new FileOutputStream(filename),encoding);
        Writer out = new OutputStreamWriter(s, encoding);
        //OutputFormat format = OutputFormat.createPrettyPrint();
        OutputFormat format = OutputFormat.createCompactFormat();
        format.setExpandEmptyElements(false);
        format.setEncoding(encoding);
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(doc);
        //printDoc(doc);
        out.close();
    }

    /**
     * ����xml�ļ�
     *
     * @param xmlfile Ҫ���ص��ļ�
     * @return Document
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public Document loadXML(String xmlfile) throws FileNotFoundException, DocumentException {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new FileInputStream(xmlfile));
        return doc;
    }
    /*
     private Document createDoc() {
         Document doc = DocumentHelper.createDocument();
         Element root = doc.addElement("root");
         Element author2 = root.addElement("author");
         author2.addAttribute("name", "Toby");
         author2.addAttribute("location","Germany");
         author2.addText("Tobias Rademacher");
         Element author1 = root.addElement("author");
         author1.addAttribute("name","James");
         author1.addAttribute("location", "UK");
         author1.addText("James Strachan");
         return doc;
     }
     */

    /**
     * ����������ȡö���б�
     *
     * @param fieldname ����
     * @return String
     * @throws Exception
     */
    public String getEnumByField(String fieldname) throws Exception {
        String ret = "";
        CachedRowSet rs = DB2_81.getRs("select * from UPENUMINFODETL where enuid='" + fieldname + "' order by enutp");
        while (rs.next()) {
            ret += "<option value='" + rs.getString("enutp").trim() + "'>" + DBUtil.fromDB2(rs.getString("enudt").trim()) + "</option>";
        }
        return ret;
    }

    /**
     * ������ҵ�ͻ���Ϣ
     *
     * @param fm FormQsykh ��ҵ�ͻ���Ϣ
     * @return boolean
     * @throws Exception
     */
    public boolean updateQsykh(FormQsykh fm) throws Exception {
        StringBuffer sql = new StringBuffer("update upqsykh set ");
        //sql.append("jrjgdm='"+fm.getJrjgdm()+"',");
        //sql.append("khh="+fm.getKhh()+",");
        sql.append("zhgwmc='" + fm.getZhgwmc() + "',");
        sql.append("jkrxzh='" + fm.getJkrxzh() + "',");
        sql.append("zhgubm='" + fm.getZhgubm() + "',");
        sql.append("faddbr='" + fm.getFaddbr() + "',");
        sql.append("djzcrq=" + DBUtil.toSqlDate(fm.getDjzcrq()) + ",");
        sql.append("tngxdz='" + fm.getTngxdz() + "',");
        sql.append("zczj=" + DBUtil.MoneytoNumber(fm.getZczj()) + ",");
        sql.append("jngyxm='" + fm.getJngyxm() + "',");
        sql.append("zyywsr=" + DBUtil.MoneytoNumber(fm.getZyywsr()) + ",");
        sql.append("zhhkhh='" + fm.getZhhkhh() + "',");
        sql.append("xinydj='" + fm.getXinydj() + "',");
        sql.append("sjmj1=" + DBUtil.MoneytoNumber(fm.getSjmj1()) + ",");
        sql.append("ybzmj1=" + DBUtil.MoneytoNumber(fm.getYbzmj1()) + ",");
        sql.append("pgjz1=" + DBUtil.MoneytoNumber(fm.getPgjz1()) + ",");
        sql.append("dyzk1='" + fm.getDyzk1() + "',");
        sql.append("sjmj2=" + DBUtil.MoneytoNumber(fm.getSjmj2()) + ",");
        sql.append("ybzmj2=" + DBUtil.MoneytoNumber(fm.getYbzmj2()) + ",");
        sql.append("pgjz2=" + DBUtil.MoneytoNumber(fm.getPgjz2()) + ",");
        sql.append("dyzk2='" + fm.getDyzk2() + "',");
        sql.append("sjmj3=" + DBUtil.MoneytoNumber(fm.getSjmj3()) + ",");
        sql.append("ybzmj3=" + DBUtil.MoneytoNumber(fm.getYbzmj3()) + ",");
        sql.append("pgjz3=" + DBUtil.MoneytoNumber(fm.getPgjz3()) + ",");
        sql.append("dyzk3='" + fm.getDyzk3() + "',");
        sql.append("hukly1='" + fm.getHukly1() + "',");
        sql.append("hukly2='" + fm.getHukly2() + "',");
        sql.append("hukly3='" + fm.getHukly3() + "',");
        sql.append("dbfx='" + fm.getDbfx() + "',");
        sql.append("cwfx='" + fm.getCwfx() + "',");
        sql.append("fcwfx='" + fm.getFcwfx() + "',");
        sql.append("cwzb1=" + DBUtil.MoneytoNumber(fm.getCwzb1()) + ",");
        sql.append("cwzb2=" + DBUtil.MoneytoNumber(fm.getCwzb2()) + ",");
        sql.append("cwzb3=" + DBUtil.MoneytoNumber(fm.getCwzb3()) + ",");
        sql.append("cwzb4=" + DBUtil.MoneytoNumber(fm.getCwzb4()) + ",");
        sql.append("cwzb5=" + DBUtil.MoneytoNumber(fm.getCwzb5()) + ",");
        sql.append("cwzb6=" + DBUtil.MoneytoNumber(fm.getCwzb6()) + ",");
        sql.append("cwzb7=" + DBUtil.MoneytoNumber(fm.getCwzb7()) + ",");
        sql.append("cwzb8=" + DBUtil.MoneytoNumber(fm.getCwzb8()) + ",");
        sql.append("cwzb9=" + DBUtil.MoneytoNumber(fm.getCwzb9()) + ",");
        sql.append("cwzb10=" + DBUtil.MoneytoNumber(fm.getCwzb10()) + ",");
        sql.append("qtsxsm='" + fm.getQtsxsm() + "',");
        sql.append("cwzbrq=" + DBUtil.toSqlDate(fm.getCwzbrq()) + ",isempty=1");
        sql.append(" where serialno=" + fm.getSerialno());
        return DB2_81.ExecCmd(sql.toString().replaceAll("'null'", "NULL"));
    }

    /**
     * ��������ҵ���������Ϣ
     *
     * @param fm FormQsydk ����ҵ���������Ϣ
     * @return boolean
     * @throws Exception
     */
    public boolean updateQsydk(FormQsydk fm) throws Exception {
        StringBuffer sql = new StringBuffer("update upqsydk set ");
        //sql.append("jrjgdm='"+fm.getJrjgdm()+"',");
        //sql.append("fljzrq="+DBUtil.toSqlDate(fm.getFljzrq())+",");
        //sql.append("khh='"+fm.getKhh()+"'");
        sql.append("dkhthm='" + fm.getDkhthm() + "',");
        sql.append("daikzl='" + fm.getDaikzl() + "',");
        sql.append("daikfs='" + fm.getDaikfs() + "',");
        sql.append("dkhtje=" + DBUtil.MoneytoNumber(fm.getDkhtje()) + ",");
        sql.append("daikye=" + DBUtil.MoneytoNumber(fm.getDaikye()) + ",");
        sql.append("htsxrq=" + DBUtil.toSqlDate(fm.getHtsxrq()) + ",");
        sql.append("htzzrq=" + DBUtil.toSqlDate(fm.getHtzzrq()) + ",");
        sql.append("zqksrq=" + DBUtil.toSqlDate(fm.getZqksrq()) + ",");
        sql.append("zqdqrq=" + DBUtil.toSqlDate(fm.getZqdqrq()) + ",");
        sql.append("htqdyt='" + fm.getHtqdyt() + "',");
        sql.append("sjdkyt='" + fm.getSjdkyt() + "',");
        sql.append("bxyqqk='" + fm.getBxyqqk() + "',");
        sql.append("qxjine=" + DBUtil.MoneytoNumber(fm.getQxjine()) + ",");
        sql.append("bzhrmc='" + fm.getBzhrmc() + "',");
        sql.append("dyawmc='" + fm.getDyawmc() + "',");
        sql.append("dypgjz=" + DBUtil.MoneytoNumber(fm.getDypgjz()) + ",");
        sql.append("dzyabl=" + DBUtil.MoneytoNumber(fm.getDzyabl()) + ",");
        sql.append("sfdydj='" + fm.getSfdydj() + "',");
        sql.append("yuqits=" + DBUtil.MoneytoNumber(fm.getYuqits()) + ",");
        sql.append("lxyqts=" + DBUtil.MoneytoNumber(fm.getLxyqts()) + ",");
        sql.append("fleijg='" + fm.getFleijg() + "',isempty=1");
        //sql.append("brhid='"+fm.getBrhid()+"'");
        sql.append(" where fcno='" + fm.getFcno() + "'");
        return DB2_81.ExecCmd(sql.toString().replaceAll("'null'", "NULL"));
    }

    /**
     * ������Ȼ��������΢����ҵ������Ϣ
     *
     * @param fm FormZrrqt ��Ȼ��������΢����ҵ������Ϣ
     * @return boolean
     * @throws Exception
     */
    public boolean updateZrrqt(FormZrrqt fm) throws Exception {
        StringBuffer sql = new StringBuffer("update upzrrqt set ");
        //sql.append("jrjgdm='"+fm.getJrjgdm()+"',");
        //sql.append("fljzrq="+DBUtil.toSqlDate(fm.getFljzrq())+",");
        //sql.append("khh='"+fm.getKhh()+"',");
        sql.append("kehumc='" + fm.getKehumc() + "',");
        sql.append("fzeren='" + fm.getFzeren() + "',");
        sql.append("jinyxm='" + fm.getJinyxm() + "',");
        sql.append("jjixzh='" + fm.getJjixzh() + "',");
        sql.append("dkhthm='" + fm.getDkhthm() + "',");
        sql.append("dkhtje=" + DBUtil.MoneytoNumber(fm.getDkhtje()) + ",");
        sql.append("daikfs='" + fm.getDaikfs() + "',");
        sql.append("htsxrq=" + DBUtil.toSqlDate(fm.getHtsxrq()) + ",");
        sql.append("htzzrq=" + DBUtil.toSqlDate(fm.getHtzzrq()) + ",");
        sql.append("dk_sfaydytsydk='" + fm.getDk_sfaydytsy() + "',");
        sql.append("dk_dkyqqk='" + fm.getDk_dkyqqk() + "',");
        sql.append("dk_dkqxqk='" + fm.getDk_dkqxqk() + "',");
        sql.append("cw_zcze=" + DBUtil.MoneytoNumber(fm.getCw_zcze()) + ",");
        sql.append("cw_gdzc=" + DBUtil.MoneytoNumber(fm.getCw_gdzc()) + ",");
        sql.append("cw_fzze=" + DBUtil.MoneytoNumber(fm.getCw_fzze()) + ",");
        sql.append("cw_xysjk=" + DBUtil.MoneytoNumber(fm.getCw_xysjk()) + ",");
        sql.append("cw_jysr=" + DBUtil.MoneytoNumber(fm.getCw_jysr()) + ",");
        sql.append("cw_yjnjysr=" + DBUtil.MoneytoNumber(fm.getCw_yjnjysr()) + ",");
        sql.append("cw_jsy=" + DBUtil.MoneytoNumber(fm.getCw_jsy()) + ",");
        sql.append("cw_yjnjsy=" + DBUtil.MoneytoNumber(fm.getCw_yjnjsy()) + ",");
        sql.append("fcw_jyqksfzc='" + fm.getFcw_jyqksfzc() + "',");
        sql.append("fcw_cpscxqqk='" + fm.getFcw_cpscxqqk() + "',");
        sql.append("fcw_jkrhkyy='" + fm.getFcw_jkrhkyy() + "',");
        sql.append("fcw_sfjnqk='" + fm.getFcw_sfjnqk() + "',");
        sql.append("db_dbhtsfyx='" + fm.getDb_dbhtsfyx() + "',");
        sql.append("db_dbrdcnl='" + fm.getDb_dbrdcnl() + "',");
        sql.append("db_dzywjqjz='" + fm.getDb_dzywjqjz() + "',");
        sql.append("db_dzywdbxnl='" + fm.getDb_dzywdbxnl() + "',");
        sql.append("qtsm='" + fm.getQtsm() + "',");
        sql.append("flly='" + fm.getFlly() + "',");
        sql.append("fleijg='" + fm.getFleijg() + "',");
        sql.append("daikye=" + DBUtil.MoneytoNumber(fm.getDaikye()) + ",");
        sql.append("dcriqi=" + DBUtil.toSqlDate(fm.getDcriqi()) + ",");
        sql.append("xinydj='" + fm.getXinydj() + "',");
        sql.append("yuqits=" + DBUtil.MoneytoNumber(fm.getYuqits()) + ",");
        sql.append("lxyqts=" + DBUtil.MoneytoNumber(fm.getLxyqts()) + ",");
        sql.append("daikzl='" + fm.getDaikzl() + "',isempty=1");
        //sql.append("brhid='"+fm.getBrhid()+"'");
        sql.append(" where fcno='" + fm.getFcno() + "'");
        return DB2_81.ExecCmd(sql.toString().replaceAll("'null'", "NULL"));

    }

    /**
     * ������Ȼ��һ��������������������Ϣ
     *
     * @param fm FormZrrnh ��Ȼ��һ��������������������Ϣ
     * @return boolean
     * @throws Exception
     */
    public boolean updateZrrnh(FormZrrnh fm) throws Exception {
        StringBuffer sql = new StringBuffer("update upzrrnh set ");
        //sql.append("jrjgdm='"+fm.getJrjgdm()+"',");
        //sql.append("fljzrq="+DBUtil.toSqlDate(fm.getFljzrq())+",");
        sql.append("dkhthm='" + fm.getDkhthm() + "',");
        //sql.append("khh='"+fm.getKhh()+"',");
        //sql.append("kehumc='"+fm.getKehumc()+"',");
        sql.append("dkhtje=" + DBUtil.MoneytoNumber(fm.getDkhtje()) + ",");
        sql.append("daikye=" + DBUtil.MoneytoNumber(fm.getDaikye()) + ",");
        sql.append("htsxrq=" + DBUtil.toSqlDate(fm.getHtsxrq()) + ",");
        sql.append("htzzrq=" + DBUtil.toSqlDate(fm.getHtzzrq()) + ",");
        sql.append("yuqits=" + DBUtil.MoneytoNumber(fm.getYuqits()) + ",");
        sql.append("lxyqts=" + DBUtil.MoneytoNumber(fm.getLxyqts()) + ",");
        sql.append("daikfs='" + fm.getDaikfs() + "',");
        sql.append("xinydj='" + fm.getXinydj() + "',");
        sql.append("fleijg='" + fm.getFleijg() + "',");
        sql.append("fenlyj='" + fm.getFenlyj() + "',");
        sql.append("dbzk='" + fm.getDbzk() + "',");
        sql.append("daikzl='" + fm.getDaikzl() + "',");
        sql.append("weiyqs=" + DBUtil.MoneytoNumber(fm.getWeiyqs()) + ",isempty=1");
        //sql.append("brhid='"+fm.getBrhid()+"'");
        sql.append(" where fcno='" + fm.getFcno() + "'");
        return DB2_81.ExecCmd(sql.toString().replaceAll("'null'", "NULL"));
    }

    /**
     * ��ȡ��������Ӧ�������Ļ�������
     *
     * @param brhid String ����
     * @return String  �����Ļ�������
     * @throws Exception
     */
    public String getJgdm(String brhid) throws Exception {
        String jgdm = "";
        CachedRowSet rs = DB2_81.getRs("select upbranchid from upbrhid_maping where branchid='" + brhid + "'");
        while (rs.next()) {
            jgdm = rs.getString("upbranchid");
        }
        rs.close();
        return jgdm;
    }

    /**
     * �����������Ż�ȡ������Ŵ�������Ϣ
     *
     * @param fjgdm ������������
     * @return String
     * @throws Exception
     */
    public String[][] getSubJgdm(String fjgdm) throws Exception {
        CachedRowSet rs = DB2_81.getRs("select branchid,branchname from upbranch  where fbranchid='" + fjgdm + "'");
        int count = rs.size();
        if (count == 0) return null;
        int i = 0;
        String ret[][] = new String[count][2];
        while (rs.next()) {
            ret[i][0] = rs.getString("branchid");
            ret[i][1] = rs.getString("branchname");
            i++;
        }
        return ret;
    }

    /**
     * ������������Ӧ�������Ļ�������
     *
     * @param brhid String ����
     * @param jgdm  String �����Ļ�������
     * @return boolean
     * @throws Exception
     */
    public boolean updateJgdm(String brhid, String jgdm) throws Exception {
        int count = 0;
        String sql = "";
        CachedRowSet rs = DB2_81.getRs("select count(1) as total from upbrhid_maping where branchid='" + brhid + "'");
        while (rs.next()) {
            count = rs.getInt("total");
        }
        rs.close();
        if (count <= 0) {
            sql = "insert into upbrhid_maping(serialno,branchid,upbranchid)values(next val for S_UPBRHID_MAPING.NEXT,";
            sql += "'" + brhid + "','" + jgdm + "')";
        } else {
            sql = "update upbrhid_maping set upbranchid='" + jgdm + "' where branchid='" + brhid + "'";
        }
        return DB2_81.ExecCmd(sql);
    }

    /**
     * ���������ѡ���Ӧ�����������б�
     *
     * @param brhid String ��¼�����
     * @return String
     * @throws Exception
     */
    public String getJgdmListByBrhid(String brhid) throws Exception {
        String item = "37" + brhid.substring(1, 3);//37����ɽ��ʡ,�������ĳ����
        String ret = "";
        String sql = "select branchid,branchname from UPBRANCH where branchid like '" + item + "%' order by branchid";
        CachedRowSet rs = DB2_81.getRs(sql);
        ret += "<option value=''></option>";
        while (rs.next()) {
            ret += "<option value='" + rs.getString("branchid").trim() + "'>" + DBUtil.fromDB2(rs.getString("branchname")) + "</option>";
        }
        return ret;
    }

    /**
     * ��ȡĳ����������������
     *
     * @param brhid ��¼�����
     * @return String
     * @throws Exception
     */
    public String getBrhidList(String brhid) throws Exception {
        String item = brhid.substring(0, 3);//37����ɽ��ʡ,�������ĳ����
        String ret = "";
        String sql = "select brhid,lname,sname from scbranch where brhid like '" + item + "%' and brhlevel<5 order by brhid,brhlevel";
        CachedRowSet rs = DB2_81.getRs(sql);
        while (rs.next()) {
            ret += "<option value='" + rs.getString("brhid").trim() + "'>" + DBUtil.fromDB2(rs.getString("lname")) + "</option>";
        }
        return ret;
    }

    /**
     * ά���������������������ձ�
     *
     * @param fm FormJGDM ��������������������
     * @return boolean
     * @throws Exception
     */
    public boolean editBranchMaping(FormJGDM fm) throws Exception {
        int count = fm.getBrhid().length;
        String sql[] = new String[count + 1];
        String itemlist = "";
        for (int i = 0; i < count; i++) {
            if (i < count - 1)
                itemlist += "'" + fm.getBrhid()[i] + "',";
            else
                itemlist += "'" + fm.getBrhid()[i] + "'";
            sql[i + 1] = "insert into UPBRHID_MAPING(serialno,BRANCHID,UPBRANCHID)values(nextval for S_UPBRHID_MAPING,'" + fm.getBrhid()[i] + "','" + fm.getUpbrhid()[i] + "')";
        }
        sql[0] = "delete from UPBRHID_MAPING where BRANCHID in (" + itemlist + ")";
        return DB2_81.execBatch(sql);
    }

    /**
     * ����ϱ�����������
     *
     * @param brhid String �Ŵ����ű��
     * @param jgdm  String ��������
     * @param date  �������
     * @return String
     * @throws Exception
     */
    public String verifyData(String brhid, String jgdm, String date) throws Exception {
        String ret = "";
        int count = 0;
        boolean bQsykh = false;
        boolean bQsydk = false;
        boolean bZrrqt = false;
        boolean bZrrnh = false;
        CachedRowSet crs = null;
        String sql = "select count(kh.serialno) as total from upqsykh kh where kh.jrjgdm='" + jgdm + "' and kh.builddate='" + date + "' and kh.isempty is null";
        sql += " and exists(select 1 from upqsydk where kh.khh=khh and fleijg in ('1','2') and jrjgdm='" + jgdm + "' and fljzrq='" + date + "' and isempty is null)";
        crs = DB2_81.getRs(sql);
        while (crs.next()) {
            count = crs.getInt("total");
        }
        if (count > 0) bQsykh = true;

        crs = DB2_81.getRs("select count(fcno) as total from upqsydk where fleijg in ('1','2') and jrjgdm='" + jgdm + "' and fljzrq='" + date + "' and isempty is null");
        while (crs.next()) {
            count = crs.getInt("total");
        }
        if (count > 0) bQsydk = true;

        crs = DB2_81.getRs("select count(fcno) as total from upzrrqt where fleijg in ('1','2') and  jrjgdm='" + jgdm + "' and fljzrq='" + date + "' and isempty is null");
        while (crs.next()) {
            count = crs.getInt("total");
        }
        if (count > 0) bZrrqt = true;

        crs = DB2_81.getRs("select count(fcno) as total from upzrrnh where  fleijg in ('1','2') and jrjgdm='" + jgdm + "' and fljzrq='" + date + "' and isempty is null");
        while (crs.next()) {
            count = crs.getInt("total");
        }
        if (count > 0) bZrrnh = true;

        if (bQsydk) {
            ret = "������ҵ���������Ϣ��д����������\\x0d";
        }
        if (bZrrqt) {
            ret += "����Ȼ��������΢����ҵ������Ϣ��д����������\\x0d";
        }
        if (bZrrnh) {
            ret += "����Ȼ��һ��������������������Ϣ��д����������\\x0d";
        }
        if (bQsykh) {
            ret += "������ҵ����ͻ���Ϣ��д����������\\x0d";
        }
        return ret;
    }

    public static void main(String args[])
	{
		//String s="907010599,907010199,907010399,907010499,907010799,907010899,907010299,907010999,907016099";
		//System.out.println("('"+s.replaceAll(",", "','")+"')");
		//System.out.println("907070000".substring(0,3));
		/*
		String s="<option value=1>1</option><option value=2>2</option>";
		String[] arr=s.split("</option>");
		for(int i=0;i<arr.length;i++)
		{
			System.out.println(arr[i]);
		}
		
		String s="37010200";
		System.out.println(s.substring(0, s.length()-2));
		String dt="2007-03-01";
		System.out.println(dt.substring(0,dt.length()-2)+"01");
		System.out.println("".trim());
		*/
		ArrayList list = new ArrayList();
		for (int i=0;i<5;i++)
		{
			list.add(String.valueOf(i));
		}
		for (int i=0;i<5;i++)
		{
			System.out.println((String)list.get(i));
		}
		String s="sdfffffffffd";
		System.out.println(s.substring(0,s.length()-1));
		
	}
	
}
