package zt.cmsi.fc;
/**
 * <p/>===============================================
 * <p/>Title: 五级分类数据报送
 * <p/>===============================================
 * <p/>根据网点、截至日期等查询数据修改数据，并输入xml文档报送银监会。
 * @version $Revision: 1.17 $  $Date: 2007/06/21 08:25:49 $
 * @author weiyb
 * <p/>修改：$Author: weiyb $     
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
     * 载入企事业贷款分类信息
     *
     * @param jgdm    String 信贷部门对应的银监会机构编码
     * @param brhlist String 下级实网点列表
     * @param date    String 清分日期
     * @return sql
     * @throws Exception
     */
    private String getSqlQsydk(String jgdm, String brhlist, String date) throws Exception {
        //首先需要筛选fcmain表中的数据，然后插入upqsydk表中(选填项暂定位NULL)
        //贷款种类rq.LOANTYPE2,upmaping.id='LoanType2'(实际为贷款期限，参考黑龙江程序)
        //担保方式rq.loantype3,枚举，可以对应
        //贷款展期开始日期=null
        //贷款展期到期日期=null
        //欠息金额＝利息表(rqdueintrst.duebal1+duebal2+duebal3)中利息字段之和
        //利息逾期天数=null
        //本金逾期天数=fcmain.PASTDUEDAYS(逾期天数)
        //结果=FCMAIN.FCCLASS
        StringBuffer sql = new StringBuffer();
        sql.append("insert into UPqsydk(fcno,brhid,jrjgdm,fljzrq,khh,dkhthm,daikzl,daikfs,dkhtje,daikye,htsxrq,htzzrq,zqksrq,");
        sql.append("zqdqrq,htqdyt,sjdkyt,bxyqqk,qxjine,bzhrmc,dyawmc,dypgjz,dzyabl,sfdydj,yuqits,lxyqts,fleijg)");
        //sql.append(" select fc.fcno,fc.brhid,upm.upbranchid,fc.createdate,rq.clientno,rq.contractno,m_1.id,m.id,rq.contractamt,rq.nowbal,");
        sql.append(" select fc.fcno,fc.brhid,'" + jgdm + "',fc.createdate,rq.clientno,case rq.contractno when '' then fc.fcno else rtrim(fc.fcno)||rq.contractno end,m_1.id,m.id,rq.contractamt,rq.nowbal,");
        //sql.append(" ct.startdate,ct.enddate,cast(null as date),cast(null as date),cast(null as character),cast(null as character),cast(null as character)");
        sql.append(" rq.paydate,rq.enddate,cast(null as date),cast(null as date),cast(null as character),cast(null as character),cast(null as character)");
        sql.append(" ,di.duebal1+di.duebal2+di.duebal3,cast(null as character),cast(null as character),cast(null as decimal),cast(null as decimal),");
        sql.append(" case rq.loantype3 when 220 then '1' when 230 then '1' else '2' end,case when fc.PASTDUEDAYS<=0 then 0 else fc.PASTDUEDAYS end,0,cast(fc.fcclass as character)");//本金逾期天数<=0，则为0，利息逾期天数默认为0
        sql.append(" from fcmain fc  join rqloanlist rq on fc.bmno=rq.bmno ");
        sql.append(" left join bmcontract ct on fc.bmno=ct.bmno ");
        sql.append(" left join rqdueintrst di on fc.bmno=di.bmno");
        sql.append(" left join upmaping m on rq.loantype3=m.enutp and m.enuid='LoanType3'");
        sql.append(" left join upmaping m_1 on rq.loantype2=m_1.enutp and m_1.enuid='LoanType2'");
        //sql.append(" left join UPBRHID_MAPING upm on rq.bnkid=upm.branchid");
        //sql.append(" where fc.fctype=3 and fc.brhid='"+brhid+"' and fc.createdate='"+date+"'");
        sql.append(" where fc.fctype=3 and  fc.brhid in " + brhlist + " and fc.createdate='" + date + "'");//修改为信贷部门
        sql.append(" and fc.FCCRTTYPE=1");//时点型
        //排除rq.clientno is null的情况
        sql.append(" and rq.clientno is not null");
        //排除 fc.FCCLASS=0的情况
        sql.append(" and fc.FCCLASS>0");
        //增量更新
        sql.append(" and not exists(select 1 from UPqsydk where fcno=fc.fcno)");
        return sql.toString();
    }

    /**
     * 取企事业贷款分类历史数据，填充为默认数据。
     *
     * @param jgdm    String 信贷部门对应的银监会机构编码
     * @param brhlist String 下级实网点列表
     * @param date    String 清分日期
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
     * 取自然人其他和微型企业分类信息历史数据，填充为默认数据。
     *
     * @param jgdm    String 信贷部门对应的银监会机构编码
     * @param brhlist String 下级实网点列表
     * @param date    String 清分日期
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
     * 取自然人一般贷款和消费类贷款分类信息历史数据，填充为默认数据。
     *
     * @param jgdm    String 信贷部门对应的银监会机构编码
     * @param brhlist String 下级实网点列表
     * @param date    String 清分日期
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
     * 载入自然人其他和微型企业分类信息
     *
     * @param jgdm    String 信贷部门对应的银监会机构编码
     * @param brhlist String 信贷部门下的实网点列表
     * @param date    String 清分日期
     * @return sql String
     * @throws Exception
     */
    private String getSqlZrrqt(String jgdm, String brhlist, String date) throws Exception {
        //贷款方式，枚举，同担保方式
        //分类结果，同五级分类
        //其他枚举，没有，需要自己填
        //经营项目是否对应CMCORPCLIENT.BIZSCOPE(法人可以信息表中的经营范围字段BIZSCOPE)，通过客户号关联
        //资产总额=CMCORPCLIENT.TOTASSETS,资产总额之固定资产(暂为空),负债总额=CMCORPCLIENT.TOTDEBT,
        //负债总额之信贷部门借款=RQCLIENTDATA.BAL WHERE CLIENTNO=:CLIENTNO AND BRHID=:BRHID AND AVAILDT='YYMM'(年月，此字段为新增)
        //经营收入=null,预计年经营收入=null,净收益=null,预计年净收益=null
        //经营情况是否正常=null,产品市场需求情况=null,借款人还款意愿=null,税费缴纳情况=null,担保合同是否有效=null,保证人代偿能力=null
        //抵（质）押物及其价值=null,抵（质）押物的变现能力(枚举型)=null,其它情况说明=null
        //分类理由flly=fccmt.case when cmt4  is null then case when cmt3 is null then case when cmt2 is null then cmt1 else cmt2 end else cmt3 end else cmt4 end as cmt where fccmttype=1
        //分类结果(枚举型)=FCMAIN.FCCLASS
        //贷款余额=RQLOANLIST.NOWBAL,调查日期=null,信用等级=m.id and enuid='CreditClass'
        //本金逾期天数=fc.PASTDUEDAYS,利息逾期天数=null
        //贷款种类=fc.fctype,upmaping.id and upmaping.enuid='FCType'
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
        sql.append(" and fc.brhid in " + brhlist + " and fc.createdate='" + date + "'");//修改为信贷部门
        sql.append(" and fc.FCCRTTYPE=1");//时点型
        //sql.append(" and  cd.brhid in "+brhlist+" and cd.availdt='"+DBUtil.to_Date(date).substring(0,6)+"'");
        //排除rq.clientno is null的情况
        sql.append(" and rq.clientno is not null");
        //排除 fc.FCCLASS=0的情况
        sql.append(" and fc.FCCLASS>0");
        //增量更新
        sql.append(" and not exists(select 1 from UPzrrqt where fcno=fc.fcno)");
        return sql.toString();
    }

    /**
     * 更新负债总额之信贷部门借款=RQCLIENTDATA.BAL WHERE CLIENTNO=:CLIENTNO AND BRHID=:BRHID AND AVAILDT='YYMM'(年月，此字段为新增)
     *
     * @param jgdm    String 信贷部门对应的银监会机构编码
     * @param brhlist String 信贷部门下的实网点列表
     * @param date    String 清分日期
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
     * 更新自然人其他分类信息，排除微型企业，资产总额cw_zcze＝CMINDVCLIENT.TOTASSETS，负债总额cw_fzze＝CMINDVCLIENT.TOTDEBT，
     *
     * @param jgdm    String 信贷部门对应的银监会机构编码
     * @param brhlist String 信贷部门下的实网点列表
     * @param date    String 清分日期
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
     * 更新自然人其他分类信息,分类人意见
     *
     * @param jgdm    String 信贷部门对应的银监会机构编码
     * @param brhlist String 信贷部门下的实网点列表
     * @param date    String 清分日期
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
     * 更新自然人一般分类信息,分类人意见
     *
     * @param jgdm    String 信贷部门对应的银监会机构编码
     * @param brhlist String 信贷部门下的实网点列表
     * @param date    String 清分日期
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
     * 载入自然人一般贷款和消费类贷款分类信息
     *
     * @param jgdm    String 信贷部门对应的银监会机构编码
     * @param brhlist String 信贷部门下的实网点列表
     * @param date    String 清分日期
     * @return String
     * @throws Exception
     */
    private String getSqlZrrnh(String jgdm, String brhlist, String date) throws Exception {
        //首先需要筛选fcmain表中的数据，然后插入upqsydk表中。
        //枚举同上
        //本金逾期天数=fcmain.PASTDUEDAYS,利息逾期天数=null
        //贷款方式=rq.loantype3,upmaping.id
        //贷款种类=rq.typeno,upmaping.id改为fc.loanway
        //违约期数=null
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
        sql.append("cast(null as character),'01',");//modified by weiyb on 20070608 默认为自然人一般='01'
        sql.append(" 0");//违约期数默认为0
        sql.append(" from fcmain fc  join rqloanlist rq on fc.bmno=rq.bmno ");
        sql.append(" left join cmcorpclient cm on rq.clientno=cm.clientno");
        sql.append(" left join bmcontract ct on fc.bmno=ct.bmno ");
        sql.append(" left join upmaping m on rq.loantype3=m.enutp and m.enuid='LoanType3'");
        sql.append(" left join upmaping m_1 on fc.creditclass=m_1.enutp and m_1.enuid='CreditClass'");
        sql.append(" left join upmaping m_2 on fc.fcclass=m_2.enutp and m_2.enuid='LoanCat1'");
        //sql.append(" left join upmaping m_3 on rq.typeno=m_3.enutp and m_3.enuid='LoanWay'");
        //FCBMAIN生效后，上句替换为：
        sql.append(" left join upmaping m_3 on cast(fc.loanway as smallint)=m_3.enutp and m_3.enuid='LoanWay'");
        sql.append(" left join fccmt cmt on fc.fcno=cmt.fcno and cmt.fccmttype=1");//add by weiyb on 20070608
        //sql.append(" left join UPBRHID_MAPING upm on rq.bnkid=upm.branchid");
        //sql.append(" where fc.fctype=1 and fc.brhid='"+brhid+"' and fc.createdate='"+date+"'");
        sql.append(" where fc.fctype=1 and fc.brhid in " + brhlist + " and fc.createdate='" + date + "'");//修改为信贷部门
        sql.append(" and fc.FCCRTTYPE=1");//时点型
        //排除rq.clientno is null的情况
        sql.append(" and rq.clientno is not null");
        //排除 fc.FCCLASS=0的情况
        sql.append(" and fc.FCCLASS>0");
        //增量更新
        sql.append(" and not exists(select 1 from UPzrrnh where fcno=fc.fcno)");

        return sql.toString();
    }

    /**
     * 判断清分截至日批量是否跑过了？
     *
     * @param dt      String 清分截至日
     * @param brhlist String 实网点列表
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
     * 根据网点和清分日期，分别筛选数据并填充企事业贷款分类信息、自然人和其他微型企业信息、自然人一般贷款和消费类贷款信息。
     *
     * @param brhid   String 信贷部门网点号
     * @param jgdm    String 信贷部门对应的银监会编码
     * @param brhlist String 信贷部门下的实网点列表
     * @param date    String 清分日期
     * @return String
     * @throws Exception
     */
    public boolean impCategory(String brhid, String jgdm, String brhlist, String date) throws Exception {
        this.isFCDate(brhid, brhlist, date);
        //首先判断是否存在网点映射
        int count = 0;
        String jrjgdm = "";
        CachedRowSet rs = DB2_81.getRs("select UPBRANCHID from upbrhid_maping where branchid='" + brhid + "'");
        while (rs.next()) {
            jrjgdm = rs.getString("UPBRANCHID");
        }
        rs.close();
        jrjgdm = jrjgdm.trim();
        if (jrjgdm.equals("")) throw new Exception("还没建立网点对应关系，请先建立对应关系！");

        //modified by weiyb on 2007-05-30
        //第一次加载数据把以前的部分历史数据带过来。以后对同一网点重复加载数据不需要再带过来。
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
            pl_sql[1] = this.getHistoryOfQsydk(jgdm, brhlist, date);//如果以前存在数据，则取以前的数据，避免每次重复录入
        else
            pl_sql[1] = "delete from upqsydk where 1>2";
        pl_sql[2] = this.getSqlZrrqt(jgdm, brhlist, date);
        if (!bZrrqt)
            pl_sql[3] = this.getHistoryOfZrrqt(jgdm, brhlist, date);//如果以前存在数据，则取以前的数据，避免每次重复录入
        else
            pl_sql[3] = "delete from upzrrqt where 1>2";
        pl_sql[4] = this.getUpdateSqlZrrqt(jgdm, brhlist, date);
        pl_sql[5] = this.getUpdateSqlZrrqt2(jgdm, brhlist, date);
        pl_sql[6] = this.getUpdateSqlZrrqt3(jgdm, brhlist, date);
        pl_sql[7] = this.getSqlZrrnh(jgdm, brhlist, date);
        if (!bZrrnh)
            pl_sql[8] = this.getHistoryOfZrrnh(jgdm, brhlist, date);//如果以前存在数据，则取以前的数据，避免每次重复录入
        else
            pl_sql[8] = "delete from upzrrnh where 1>2";
        pl_sql[9] = this.getUpdateSqlZrrnh(jgdm, brhlist, date);
        return DB2_81.execBatch(pl_sql);
    }

    /**
     * 根据信贷部门和清分日期，分别汇总导入基层联社、客户信息。导入前要根据信贷部门和清分日期判断一下是否存在，如果存在则不必导入。
     *
     * @param brnid   String 信贷部门网点号
     * @param jgdm    String 信贷部门对应的银监会编码
     * @param brhlist String 信贷部门下的实网点列表
     * @param date    String 清分日期
     * @return boolean
     * @throws Exception
     */
    public boolean impDept(String brnid, String jgdm, String brhlist, String date) throws Exception {
        this.isFCDate(brnid, brhlist, date);
        //首先判断是否存在机构映射
        String jrjgdm = "";
        CachedRowSet rs = DB2_81.getRs("select UPBRANCHID from upbrhid_maping where branchid='" + brnid + "'");
        while (rs.next()) {
            jrjgdm = rs.getString("UPBRANCHID");
        }
        rs.close();
        jrjgdm = jrjgdm.trim();
        if (jrjgdm.equals("")) throw new Exception("还没建立网点对应关系，请先建立对应关系！");
        //采用增量更新，不需要另外判断是否已经统计过。
        //this.impUPxlsxx(brnid,date);//导入县联社
        this.impUPxysxx(brnid, jgdm, brhlist, date);//导入基层联社
        this.impUPqsykh(jgdm, brhlist, date);//导入基层联社客户信息
        return true;
    }

    /**
     * 载入县联社信息
     *
     * @param brhid String 县联社编号
     * @param date  String 清分日期
     * @throws Exception
     */
    public void impUPxlsxx(String brhid, String jgdm, String date) throws Exception {
        //县联社导出数据的时候，首先插入一条记录到到upxlsxx表中。
        //导入数据前先判断对应的网点、日期是否已经存在，存在则不需要导入数据
        //去掉导出表的数据为空的判断。
        CachedRowSet rs = null;
        int count = 0;
        /*
          rs=DB2_81.getRs("select count(1) as total from upzrrnh where jrjgdm = '"+jgdm+"' and fljzrq='"+date+"'");

          while (rs.next()){count=rs.getInt("total");}
          if(count<=0)
              throw new Exception("此清分时点的数据为空，不能导出！");
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
     * 载入基层联社信息（即信贷部门信息），每个信贷部门只统计自己的信贷部门下的信息
     *
     * @param brhid   String 信贷部门编号
     * @param brhlist String 信贷部门下的实网点
     * @param date    String 清分日期
     * @return boolean
     * @throws Exception
     */
    private boolean impUPxysxx(String brhid, String jgdm, String brhlist, String date) throws Exception {
        /*
           *功能实现说明：
           * 插入之前先检测，是否已经统计过了，统计依据为brhid+date
           * 首先插入基层联社代码信息，然后通过清分进行汇总统计总贷款笔数和贷款余额合计信息，并且更新到此表中。
           * 根据FCMAIN.FCTYPE(清分业务类型,关联枚举表enuid='FCType')和FCMAIN.FCCLASS(最终认定结果，关联枚举表enuid='LoanCat1')来
           * 统计汇总。
          */
        //判断是否统计过了，如果统计过了，则不必再统计
        //扁平式的管理导致很多信贷部门对应银监局的一个信贷部门。所以要取这些信贷部门下的所有网点的集合才行。否则只能统计一个信贷部门下的数据。
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
        if (conn == null) throw new Exception("数据库连接错误！");
        conn.setAutoCommit(false);
        //1.新增
        StringBuffer sql = new StringBuffer();
        sql.append("insert into UPxysxx(serialno,jrjgdm,xlsjgdm,jrjgmc,builddate)");
        sql.append(" select NEXT VALUE FOR S_UPXYSXX,upm.upbranchid,ub.fbranchid,ub.branchname,'" + date + "' from scbranch sb ");
        sql.append(" left join UPBRHID_MAPING upm on sb.brhid=upm.branchid");
        sql.append(" join UPBRANCH ub on upm.upbranchid=ub.branchid");
        sql.append(" where sb.brhid in " + unionbrhlist + " fetch first 1 rows only");
        //System.out.println(sql);

        //2.统计
        StringBuffer sql1 = new StringBuffer();
        sql1.append("select count(1) as total,sum(t.bal) as bal,t.fctype,t.fcclass from ");
        sql1.append(" (select fc.* from fcmain fc");
        //排除rq.clientno is null的情况
        //sql1.append(" join rqloanlist rq on fc.bmno=rq.bmno where rq.bnkid='"+brhid+"' and rq.clientno is not null and fc.createdate='"+date+"' and fc.FCCRTTYPE=1) as t");
        sql1.append(" join rqloanlist rq on fc.bmno=rq.bmno where rq.brhid in " + unionbrhlist + " and rq.clientno is not null and fc.createdate='" + date + "' and fc.FCCRTTYPE=1) as t");
        sql1.append(" group by t.FCTYPE,t.FCCLASS");
        //System.out.println(sql1);
        //排除rq.clientno is null的情况
        //3.通过统计出的数据，分别更新本表相应的字段,其中自然人其他、微型企业的需要合并(即：fctype=2 or fctype=4两种情况)
        //实现方法：先定义一个数组，把记录集放到数组中，然后组织sql语句实现更新。
        BigDecimal[][][] info = new BigDecimal[5][4][2];
        //用零初始化数组
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
        //更新相应字段，当fctype为2或4时，应该sum一下值，合并为一个值，作为“自然人其他和微型企业”
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
        /*矩阵
           *  FCType	3	企业类
           *	FCType 	2	自然人其他
           *	FCType 	1	自然人农户
           *	FCType 	4	微型企业
           *
           * 	LoanCat1	1	正常
           *	LoanCat1	2	关注
           *	LoanCat1	3	次级
           *	LoanCat1	4	可疑
           *	LoanCat1	5	损失
           *
           *qybs1 正常类企事业贷款笔数合计<qybs1>	0,2,0
           *qyye1	正常类企事业贷款余额合计<qyye1>	0,2,1
           *qybs2	关注类企事业贷款笔数合计<qybs2>	1,2,0
           *qyye2	关注类企事业贷款余额合计<qyye2>	1,2,1
           *qybs3	次级类企事业贷款笔数合计<qybs3>	2,2,0
           *qyye3	次级类企事业贷款余额合计<qyye3>	2,2,1
           *qybs4	可疑类企事业贷款笔数合计<qybs4>	3,2,0
           *qyye4	可疑类企事业贷款余额合计<qyye4>	3,2,1
           *qybs5	损失类企事业贷款笔数合计<qybs5>	4,2,0
           *qyye5	损失类企事业贷款余额合计<qyye5>	4,2,1
           *nhbs1	正常类自然人一般和消费类贷款笔数合计<nhbs1>	 0,0,0
           *nhye1	正常类自然人一般和消费类贷款余额合计<nhye1>	 0,0,1
           *nhbs2	关注类自然人一般和消费类贷款笔数合计<nhbs2>	 1,0,0
           *nhye2	关注类自然人一般和消费类贷款余额合计<nhye2>	 1,0,1
           *nhbs3	次级类自然人一般和消费类贷款笔数合计<nhbs3>	 2,0,0
           *nhye3	次级类自然人一般和消费类贷款余额合计<nhye3>	 2,0,1
           *nhbs4	可疑类自然人一般和消费类贷款笔数合计<nhbs4>	 3,0,0
           *nhye4	可疑类自然人一般和消费类贷款余额合计<nhye4>	 3,0,1
           *nhbs5	损失类自然人一般和消费类贷款笔数合计<nhbs5>	 4,0,0
           *nhye5	损失类自然人一般和消费类贷款余额合计<nhye5>	 4,0,1
           *qtbs1	正常类自然人其他和微型企业贷款笔数合计<qtbs1>	0,(1,3),0
           *qtye1	正常类自然人其他和微型企业贷款余额合计<qtye1>	0,(1,3),1
           *qtbs2	关注类自然人其他和微型企业贷款笔数合计<qtbs2>	1,(1,3),0
           *qtye2	关注类自然人其他和微型企业贷款余额合计<qtye2>	1,(1,3),1
           *qtbs3	次级类自然人其他和微型企业贷款笔数合计<qtbs3>	2,(1,3),0
           *qtye3	次级类自然人其他和微型企业贷款余额合计<qtye3>	2,(1,3),1
           *qtbs4	可疑类自然人其他和微型企业贷款笔数合计<qtbs4>	3,(1,3),0
           *qtye4	可疑类自然人其他和微型企业贷款余额合计<qtye4>	3,(1,3),1
           *qtbs5	损失类自然人其他和微型企业贷款笔数合计<qtbs5>	4,(1,3),0
           *qtye5	损失类自然人其他和微型企业贷款余额合计<qtye5>	4,(1,3),1
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

        //执行事务控制
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
     * 载入企事业贷款客户信息
     *
     * @param jgdm    String 信贷部门对应的银监会机构编码
     * @param brhlist String 信贷部门下的实网点
     * @param date    String 清分日期
     * @return boolean
     * @throws Exception
     */
    private boolean impUPqsykh(String jgdm, String brhlist, String date) throws Exception {
        /*
           * 功能实现说明：
           * 只统计本信贷部门下的用户。
           * 统计之前先检测是否已经统计过了，统计依据为brhid和createdate
           * 客户信息＝需要通过清分表FCMAIN和贷款台帐表RQLOANLIST(时点清分)关联出来的客户，首先把关联出来的客户信息插入到企事业贷款客户信息中。
           * 主营业务收入=null
           * 统计FCMAIN.FCTYPE=3(企业类)
           * 采用批处理更新
           * */
        //判断是否统计过了，如果统计过了，则直接返回
        CachedRowSet rs = null;
        /*
          CachedRowSet rs=DB2_81.getRs("select upbranchid from UPBRHID_MAPING where branchid='"+brhid+"'");
          String upbranchid=null;
          while(rs.next()){upbranchid=rs.getString("upbranchid");}
          if (upbranchid==null )throw new Exception("导入客户信息出错，还没设定机构编号对应关系，请先设定对应关系！");
          */
        //rs=DB2_81.getRs("select count(serialno) as total from UPqsykh where jrjgdm='"+upbranchid+"' and builddate='"+date+"'");
        /*
           * modified by weiyb on 2007-5-30
           * 去掉只能载入一次数据的限制，可以多次载入数据（便于在信贷系统中取数据，然后更新到偏离度系统中）
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
        //1.插入客户基本信息到UPqsykh表
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
        sql.append(" where rq.brhid in " + brhlist + " and fc.createdate='" + date + "' and fc.FCCRTTYPE=1 and fc.fctype=3 and fc.FCCLASS>0 and cm.clientno=rq.clientno )");//修改
        sql.append(" and m.enuid='ClientType2'");
        sql.append(" and m_1.enuid='CreditClass'");
        sql.append(" and not exists(select 1 from upqsykh where cm.clientno=khh and builddate='" + date + "' and jrjgdm='" + jgdm + "')");//add by weiyb on 2007-05-30
        pl_sql[0] = sql.toString();

        //2.统计客户资产情况,fc.fctype=3代表企业类
        //  2.1.1 统计更新土地使用情况
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

        //	2.1.2 统计更新土地已办证面积
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
        //  2.2.1 统计更新房地产情况
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
        //	2.2.2 统计更新房地产已办证情况
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
        //  2.3.1 统计更新企业设备情况
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

        //	2.3.2 统计更新企业设备已办证情况
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


        //3.更新财务信息 modified by weiyb on 2007-05-21
        //select * from fcmcmt where FCCmtType=10
        //FCCmtType＝10代表财务参数
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
        //改为从fcqycw（五级分类企业基本财务信息）中获取相关财务信息，要求客户号、统计日期的月份跟fcqycw中的客户号、调查日期(最靠近统计日期前面的月份)的月份相同的记录
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
     * 判断这个日期是否对应清分日期,去查找FCMAIN
     *
     * @param brhid String 信贷部门对应的网点号
     * @param date  String 清分日期
     * @return boolean
     * @throws Exception
     */
    public boolean isFCDate(String brhid, String brhlist, String date) throws Exception {
        //判断清分日期设定表，是否在本日清分。
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
            throw new Exception("网点下无此清分截至日期，请选择合适的日期！");
    }

    /**
     * 获取清分时点列表
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
     * 判断登录网点是否县联社
     *
     * @param brhid String 县联社编码
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
            throw new Exception("县联社的用户登录才能使用本功能！");
    }

    /**
     * 判断登录网点是否信贷部门
     *
     * @param brhid String 信贷部门编码
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
            throw new Exception("信贷部门用户登录才能使用本功能！");
    }

/*
    public void expToXML(String src,String dest,String bnkid,String date)throws Exception
    {
        //导出数据到xml文件，src为xml模版，dest为导出的目标文件
        //根据信贷部门登录号，通过映射获取信贷部门机构号
        String upbranchid="";
        CachedRowSet rs=DB2_81.getRs("select upbranchid from upbrhid_maping where branchid='"+bnkid+"'");
        while(rs.next())
        {
            upbranchid=rs.getString("upbranchid");
        }
        //xml解析模版，得到表名

        String tablename="";
        //根据表名得到数据
        this.readData(tablename, upbranchid, date);

        //写数据到XML文件
    }
    */

    /**
     * 根据表的列名,获取枚举变量,形成option选项.
     *
     * @param colName 列名
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
     * 获取某个网点下的负责人或客户经理
     *
     * @param brhid String 网点编号
     * @return String
     * @throws Exception
     */
    public String getUserNameList(String brhid) throws Exception {
        String ret = "";
        String sql = "select LOGINNAME,USERNAME from SCUSER where brhid='" + brhid + "' and usertype<'3'";//排除系统管理员
        CachedRowSet rs;
        rs = DB2_81.getRs(sql);
        while (rs.next()) {
            ret += "<option value='" + DBUtil.fromDB2(rs.getString("loginname")) + "'>" + DBUtil.fromDB2(rs.getString("username")) + "</option>";
        }
        return ret;
    }

    /**
     * 根据表名，获取需要的数据,bnkid县联社代码
     *
     * @param tablename String 表名
     * @param bnkid     String 县联社对应的银监会机构编码
     * @param date      String 清分日期
     * @return CachedRowSet
     * @throws Exception
     */
    private CachedRowSet readData(String tablename, String bnkid, String date) throws Exception {
        String sql = "";
        if (tablename.equals("xlsxx"))//县联社
            //sql="select * from upxlsxx where jrjgdm='"+bnkid.substring(0,4)+"' and fljzrq='"+date+"'";
            sql = "select * from upxlsxx where jrjgdm='" + bnkid.substring(0, bnkid.length() - 2) + "00' and fljzrq='" + date + "'";
        else if (tablename.equals("xysxx"))//基层社
            sql = "select * from upxysxx where jrjgdm = '" + bnkid + "' and BUILDDATE='" + date + "'";
        else if (tablename.equals("qsykh"))//客户信息
        {
            sql = "select kh.* from upqsykh kh where kh.jrjgdm = '" + bnkid + "' and kh.BUILDDATE='" + date + "' and exists(select 1 from upqsydk where kh.khh=khh";
            sql += " and jrjgdm = '" + bnkid + "' and fljzrq='" + date + "' and fleijg in ('1','2'))";
        } else if (tablename.equals("qsydk"))//企事业贷款分类信息
            sql = "select * from upqsydk where jrjgdm = '" + bnkid + "' and fljzrq='" + date + "' and fleijg in ('1','2')";
        else if (tablename.equals("zrrqt"))//自然人其他和微型企业分类信息
            sql = "select * from upzrrqt where jrjgdm = '" + bnkid + "' and fljzrq='" + date + "' and fleijg in ('1','2')";
        else if (tablename.equals("zrrnh"))//自然人一般贷款和消费类贷款分类信息
            sql = "select * from upzrrnh where jrjgdm = '" + bnkid + "' and fljzrq='" + date + "' and fleijg in ('1','2')";
        return DB2_81.getRs(sql);

    }

    /**
     * 导出到Excel
     *
     * @param s       OutputStream 数据流
     * @param jgdm    String 县联社对应的银监会机构编码
     * @param enddate String 截至日期
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
     * 解析XML模版，并根据模版选择数据，形成xml数据流
     *
     * @param el    Element XML元素
     * @param root  Element XML根元素
     * @param bnkid bnkid 网点编号
     * @param date  String 清分日期
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
     * 递归调用
     *
     * @param el    Element XML元素
     * @param root  Element XML根元素
     * @param bnkid bnkid 网点编号
     * @param date  String 清分日期
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
     * 输出XML到某设备
     *
     * @param doc Document 对象
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
     * 保存XML到文件
     *
     * @param doc      Document 对象
     * @param filename filename 文件名
     * @param encoding encoding 编码
     * @param s        OutputStream 数据流
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
     * 加载xml文件
     *
     * @param xmlfile 要加载的文件
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
     * 根据列名获取枚举列表
     *
     * @param fieldname 列名
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
     * 更新企业客户信息
     *
     * @param fm FormQsykh 企业客户信息
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
     * 更新企事业贷款分类信息
     *
     * @param fm FormQsydk 企事业贷款分类信息
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
     * 更新自然人其他和微型企业分类信息
     *
     * @param fm FormZrrqt 自然人其他和微型企业分类信息
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
     * 更新自然人一般贷款和消费类贷款分类信息
     *
     * @param fm FormZrrnh 自然人一般贷款和消费类贷款分类信息
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
     * 获取网点所对应的银监会的机构代码
     *
     * @param brhid String 网点
     * @return String  银监会的机构代码
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
     * 根据县联社编号获取下面的信贷部门信息
     *
     * @param fjgdm 县联社机构编号
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
     * 更新网点所对应的银监会的机构代码
     *
     * @param brhid String 网点
     * @param jgdm  String 银监会的机构代码
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
     * 根据网点号选择对应的银监会机构列表
     *
     * @param brhid String 登录网点号
     * @return String
     * @throws Exception
     */
    public String getJgdmListByBrhid(String brhid) throws Exception {
        String item = "37" + brhid.substring(1, 3);//37代表山东省,后面代表某个市
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
     * 获取某个市下面所有网点
     *
     * @param brhid 登录网点号
     * @return String
     * @throws Exception
     */
    public String getBrhidList(String brhid) throws Exception {
        String item = brhid.substring(0, 3);//37代表山东省,后面代表某个市
        String ret = "";
        String sql = "select brhid,lname,sname from scbranch where brhid like '" + item + "%' and brhlevel<5 order by brhid,brhlevel";
        CachedRowSet rs = DB2_81.getRs(sql);
        while (rs.next()) {
            ret += "<option value='" + rs.getString("brhid").trim() + "'>" + DBUtil.fromDB2(rs.getString("lname")) + "</option>";
        }
        return ret;
    }

    /**
     * 维护网点和银监会机构代码对照表
     *
     * @param fm FormJGDM 网点和银监会机构代码对照
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
     * 检查上报数据完整性
     *
     * @param brhid String 信贷部门编号
     * @param jgdm  String 机构编码
     * @param date  清分日期
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
            ret = "【企事业贷款分类信息填写不完整！】\\x0d";
        }
        if (bZrrqt) {
            ret += "【自然人其他和微型企业分类信息填写不完整！】\\x0d";
        }
        if (bZrrnh) {
            ret += "【自然人一般贷款和消费类贷款分类信息填写不完整！】\\x0d";
        }
        if (bQsykh) {
            ret += "【企事业贷款客户信息填写不完整！】\\x0d";
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
