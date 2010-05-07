package zt.cms.xf.report;

import com.zt.util.PropertyManager;
import zt.cms.cm.common.RightChecker;
import zt.platform.cachedb.ConnectionManager;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;

import javax.sql.rowset.CachedRowSet;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class XFReport extends FormActions {
//    public static Logger logger = Logger.getLogger("zt.cms.xf.XFConfirmPage10");
    private static Log logger = LogFactory.getLog(XFReport.class);


    //private String flag = null;  //窗体是否可读
    private String FORMID = null; //申请单号

    public int load(SessionContext ctx, DatabaseConnection conn,
                    FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {

        RightChecker.checkReadonly(ctx, conn, instance);
        FORMID = instance.getFormid();

        return 0;
    }


    public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                           FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {

        if (button.equals("savebtn")) {
            String clientnm = ctx.getParameter("CLIENTNM");             //客户姓名
            String contractno = ctx.getParameter("CONTRACTNO");        //合同号
            String contfromdate = ctx.getParameter("CONTFROMDATE");    //合同签署开始日期
            String conttodate = ctx.getParameter("CONTTODATE");        //合同签署结束日期
            String payfromdate = ctx.getParameter("PAYFROMDATE");      //还款开始日期
            String paytodate = ctx.getParameter("PAYTODATE");          //还款结束日期
            String appfromdate = ctx.getParameter("APPFROMDATE");      //申请开始日期
            String apptodate = ctx.getParameter("APPTODATE");          //申请结束日期
            String searchdate = ctx.getParameter("SEARCHDATE");        //查询日期
            String overduecd = ctx.getParameter("OVERDUECD");          //是否逾期
            String gender = ctx.getParameter("GENDER");                 //性别
            String marriagestatus = ctx.getParameter("MARRIAGESTATUS");//婚姻状况
            String residenceadr = ctx.getParameter("RESIDENCEADR");    //户籍所在地
            String edulevel = ctx.getParameter("EDULEVEL");             //教育程度
            String commtype = ctx.getParameter("COMMTYPE");             //产品种类

            String datestr = "";

            clientnm = (clientnm == null) ? "" : clientnm.trim();
            contractno = (contractno == null) ? "" : contractno.trim();
            contfromdate = (contfromdate == null) ? "" : contfromdate.trim();
            conttodate = (conttodate == null) ? "" : conttodate.trim();
            payfromdate = (payfromdate == null) ? "" : payfromdate.trim();
            paytodate = (paytodate == null) ? "" : paytodate.trim();
            appfromdate = (appfromdate == null) ? "" : appfromdate.trim();
            apptodate = (apptodate == null) ? "" : apptodate.trim();
            searchdate = (searchdate == null) ? "" : searchdate.trim();
            overduecd = (overduecd == null) ? "" : overduecd.trim();
            gender = (gender == null) ? "" : gender.trim();
            marriagestatus = (marriagestatus == null) ? "" : marriagestatus.trim();
            residenceadr = (residenceadr == null) ? "" : residenceadr.trim();
            edulevel = (edulevel == null) ? "" : edulevel.trim();
            commtype = (commtype == null) ? "" : commtype.trim();

            String poiexceltemp = PropertyManager.getProperty("POI_EXCEl_PATH");
            HashMap<String, Object> excelMap = new HashMap<String, Object>();
            String sql = "";

            if (FORMID.equals("XFREPORT01")) {
                sql = "select row_number() over (order by b.startdate),b.appno,b.clientname,b.CLIENTACT,b.CONTRACTNO," +
                        "REPLACE(trim(c.COMMNAME),chr(10),''),b.COMMAMT," +
                        "REPLACE(trim(c.CHANNEL),chr(10),''),b.RECEIVEAMT,b.PRINCIPALAMT," +
                        "p.PAYDATE,(select enudt from ptenuminfodetl where enuid='Bank' and enutp=b.PAYBACKBANKID) PAYBACKBANK,b.PAYBACKACT," +
                        "to_char(p.PAYBACKDATEFROM,'YYYY-MM-DD') || '  ' || to_char(p.PAYBACKDATETO,'YYYY-MM-DD') qj," +
                        "b.DURATION,b.SERVICECHARGE,(p.SSERVICECHARGEFEE + p.SODB_BREACHFEE+ p.SODB_LATEFEE) REVENUE,SPRINCIPALAMT,SSERVICECHARGEFEE,SCLOSEDCD,SOVERDUECD,SKX,SZC " +
                        "from XFAPPCOMM c,XFPOA p,XFCONTRACT b " +
                        "where b.appno=c.appno and b.CONTRACTNO =p.CONTRACTNO  ";

                if (!clientnm.equals("")) sql += "and b.CLIENTNAME like '%" + clientnm + "%' ";
                if (!contractno.equals("")) sql += "and b.CONTRACTNO='" + contractno + "' ";
                if (!contfromdate.equals("")) sql += "and b.STARTDATE>=to_date('" + contfromdate + "','YYYYMMDD') ";
                if (!conttodate.equals("")) sql += "and b.STARTDATE<=to_date('" + conttodate + "','YYYYMMDD') ";
                if (!payfromdate.equals("")) sql += "and PAYBACKDATEFROM>=to_date('" + payfromdate + "','YYYYMMDD') ";
                if (!paytodate.equals("")) sql += "and PAYBACKDATETO<=to_date('" + paytodate + "','YYYYMMDD') ";
                if (!overduecd.equals("")) {
                    if (overduecd.equals("0")) sql += "and SOVERDUECD=0 ";
                    else if (overduecd.equals("1")) sql += "and SOVERDUECD>0 ";
                }

                sql+=" order by b.startdate ";
                logger.debug("还款明细统计表SQL="+sql);

                excelMap.put("filenm", poiexceltemp + "\\个人消费贷款还款明细表.xls");
                if (!contfromdate.equals("")) datestr += " 自" + contfromdate;
                if (!conttodate.equals("")) datestr += " 至" + conttodate;

            } else if (FORMID.equals("XFREPORT02")) {                   //五级分类表
                sql = "select row_number() over (order by b.startdate),b.clientname,b.CONTRACTNO," +
                        "REPLACE(trim(c.COMMNAME),chr(10),''),to_char(p.paydate,'YYYY-MM-DD') PAYDATE,to_char(p.paybackdateto,'YYYY-MM-DD') PAYBACKDATETO," +
                        "b.CONTRACTAMT,(b.CONTRACTAMT-p.sprincipalamt) NOWBAL," +
                        "CASE WHEN p.soverduecd > 0 THEN '有' ELSE '无' END,'','' " +
                        "from XFPOA p,XFAPPCOMM c,XFCONTRACT b " +
                        "where b.appno=c.appno and p.CONTRACTNO=b.CONTRACTNO ";

                if (!clientnm.equals("")) sql += "and b.CLIENTNAME like '%" + clientnm + "%' ";
                if (!contractno.equals("")) sql += "and b.CONTRACTNO='" + contractno + "' ";
                if (!contfromdate.equals("")) sql += "and b.STARTDATE>=to_date('" + contfromdate + "','YYYYMMDD') ";
                if (!conttodate.equals("")) sql += "and b.STARTDATE<=to_date('" + conttodate + "','YYYYMMDD') ";
                if (!payfromdate.equals("")) sql += "and PAYBACKDATEFROM>=to_date('" + payfromdate + "','YYYYMMDD') ";
                if (!paytodate.equals("")) sql += "and PAYBACKDATETO<=to_date('" + paytodate + "','YYYYMMDD') ";
                if (!overduecd.equals("")) {
                    if (overduecd.equals("0")) sql += "and SOVERDUECD=0 ";
                    else if (overduecd.equals("1")) sql += "and SOVERDUECD>0 ";
                }

                sql+=" order by b.startdate ";
                logger.debug("五级分类统计表SQL="+sql);
                
                excelMap.put("filenm", poiexceltemp + "\\个人消费贷款五级分类汇总表.xls");
                if (!contfromdate.equals("")) datestr += " 自" + contfromdate;
                if (!conttodate.equals("")) datestr += " 至" + conttodate;

            } else if (FORMID.equals("XFREPORT03")) {
                if (searchdate.equals("")) searchdate = "20000101";

                sql = "with t1 as (select CONTRACTNO,POANO,count(CONTRACTNO) ct,max(BILLSTATUS) bs from xfactcutpaydetl t where BILLTYPE='0' and BillStatus>=3 group by CONTRACTNO,POANO" +
                        "),t2 as (select a.CONTRACTNO,a.POANO,a.PAYBACKDATE,a.STARTDATE,a.CLOSEDDATE,t1.ct,t1.bs,a.CLOSEDCD,a.OVERDUECD from XFACTCUTPAYMAIN a left outer join t1 on a.CONTRACTNO=t1.CONTRACTNO and a.POANO=t1.POANO" +
                        "),t3 as (SELECT CONTRACTNO," +
                        "       to_char(PAYBACKDATE, 'YYYY') PAYBACKYEAR," +
                        "       MIN(to_char(PAYBACKDATE, 'YYYYMM')) STAPAYBACKDATE," +
                        "       SUM(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'YYYYMMDD') <= to_char(PAYBACKDATE, 'YYYY') || '01' || to_char(PAYBACKDATE, 'DD') THEN" +
                        "              OVERDUECD" +
                        "             ELSE" +
                        "              '0'" +
                        "           END) OD01," +
                        "       MAX(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'MM') = '01' THEN" +
                        "              CASE" +
                        "             WHEN CLOSEDCD = 1 THEN" +
                        "              CASE WHEN ct='1' THEN 'N' ELSE 'N/'||ct END " +
                        "             WHEN OVERDUECD = 1 THEN" +
                        "              '1'" +
                        "             WHEN bs=5 THEN" +
                        "              CASE WHEN ct='1' THEN '*' ELSE '*/'||ct END" +
                        "           END END) D01," +
                        "       SUM(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'YYYYMMDD') <= to_char(PAYBACKDATE, 'YYYY') || '02' || to_char(PAYBACKDATE, 'DD') THEN" +
                        "              OVERDUECD" +
                        "             ELSE" +
                        "              '0'" +
                        "           END) OD02," +
                        "       MAX(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'MM') = '02' THEN" +
                        "              CASE" +
                        "             WHEN CLOSEDCD = 1 THEN" +
                        "              CASE WHEN ct='1' THEN 'N' ELSE 'N/'||ct END" +
                        "             WHEN OVERDUECD = 1 THEN" +
                        "              '1'" +
                        "             WHEN bs=5 THEN" +
                        "              CASE WHEN ct='1' THEN '*' ELSE '*/'||ct END" +
                        "           END END) D02," +
                        "       SUM(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'YYYYMMDD') <= to_char(PAYBACKDATE, 'YYYY') || '03' || to_char(PAYBACKDATE, 'DD') THEN" +
                        "              OVERDUECD" +
                        "             ELSE" +
                        "              '0'" +
                        "           END) OD03," +
                        "       MAX(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'MM') = '03' THEN" +
                        "              CASE" +
                        "             WHEN CLOSEDCD = 1 THEN" +
                        "              CASE WHEN ct='1' THEN 'N' ELSE 'N/'||ct END" +
                        "             WHEN OVERDUECD = 1 THEN" +
                        "              '1'" +
                        "             WHEN bs=5 THEN" +
                        "              CASE WHEN ct='1' THEN '*' ELSE '*/'||ct END" +
                        "           END END) D03," +
                        "       SUM(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'YYYYMMDD') <= to_char(PAYBACKDATE, 'YYYY') || '04' || to_char(PAYBACKDATE, 'DD') THEN" +
                        "              OVERDUECD" +
                        "             ELSE" +
                        "              '0'" +
                        "           END) OD04," +
                        "       MAX(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'MM') = '04' THEN" +
                        "              CASE" +
                        "             WHEN CLOSEDCD = 1 THEN" +
                        "              CASE WHEN ct='1' THEN 'N' ELSE 'N/'||ct END" +
                        "             WHEN OVERDUECD = 1 THEN" +
                        "              '1'" +
                        "             WHEN bs=5 THEN" +
                        "              CASE WHEN ct='1' THEN '*' ELSE '*/'||ct END" +
                        "           END END) D04," +
                        "       SUM(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'YYYYMMDD') <= to_char(PAYBACKDATE, 'YYYY') || '05' || to_char(PAYBACKDATE, 'DD') THEN" +
                        "              OVERDUECD" +
                        "             ELSE" +
                        "              '0'" +
                        "           END) OD05," +
                        "       MAX(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'MM') = '05' THEN" +
                        "              CASE" +
                        "             WHEN CLOSEDCD = 1 THEN" +
                        "              CASE WHEN ct='1' THEN 'N' ELSE 'N/'||ct END" +
                        "             WHEN OVERDUECD = 1 THEN" +
                        "              '1'" +
                        "             WHEN bs=5 THEN" +
                        "              CASE WHEN ct='1' THEN '*' ELSE '*/'||ct END" +
                        "           END END) D05," +
                        "       SUM(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'YYYYMMDD') <= to_char(PAYBACKDATE, 'YYYY') || '06' || to_char(PAYBACKDATE, 'DD') THEN" +
                        "              OVERDUECD" +
                        "             ELSE" +
                        "              '0'" +
                        "           END) OD06," +
                        "       MAX(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'MM') = '06' THEN" +
                        "              CASE" +
                        "             WHEN CLOSEDCD = 1 THEN" +
                        "              CASE WHEN ct='1' THEN 'N' ELSE 'N/'||ct END" +
                        "             WHEN OVERDUECD = 1 THEN" +
                        "              '1'" +
                        "             WHEN bs=5 THEN" +
                        "              CASE WHEN ct='1' THEN '*' ELSE '*/'||ct END" +
                        "           END END) D06," +
                        "       SUM(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'YYYYMMDD') <= to_char(PAYBACKDATE, 'YYYY') || '07' || to_char(PAYBACKDATE, 'DD') THEN" +
                        "              OVERDUECD" +
                        "             ELSE" +
                        "              '0'" +
                        "           END) OD07," +
                        "       MAX(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'MM') = '07' THEN" +
                        "              CASE" +
                        "             WHEN CLOSEDCD = 1 THEN" +
                        "              CASE WHEN ct='1' THEN 'N' ELSE 'N/'||ct END" +
                        "             WHEN OVERDUECD = 1 THEN" +
                        "              '1'" +
                        "             WHEN bs=5 THEN" +
                        "              CASE WHEN ct='1' THEN '*' ELSE '*/'||ct END" +
                        "           END END) D07," +
                        "       SUM(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'YYYYMMDD') <= to_char(PAYBACKDATE, 'YYYY') || '08' || to_char(PAYBACKDATE, 'DD') THEN" +
                        "              OVERDUECD" +
                        "             ELSE" +
                        "              '0'" +
                        "           END) OD08," +
                        "       MAX(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'MM') = '08' THEN" +
                        "              CASE" +
                        "             WHEN CLOSEDCD = 1 THEN" +
                        "              CASE WHEN ct='1' THEN 'N' ELSE 'N/'||ct END" +
                        "             WHEN OVERDUECD = 1 THEN" +
                        "              '1'" +
                        "             WHEN bs=5 THEN" +
                        "              CASE WHEN ct='1' THEN '*' ELSE '*/'||ct END" +
                        "           END END) D08," +
                        "       SUM(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'YYYYMMDD') <= to_char(PAYBACKDATE, 'YYYY') || '09' || to_char(PAYBACKDATE, 'DD') THEN" +
                        "              OVERDUECD" +
                        "             ELSE" +
                        "              '0'" +
                        "           END) OD09," +
                        "       MAX(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'MM') = '09' THEN" +
                        "              CASE" +
                        "             WHEN CLOSEDCD = 1 THEN" +
                        "              CASE WHEN ct='1' THEN 'N' ELSE 'N/'||ct END" +
                        "             WHEN OVERDUECD = 1 THEN" +
                        "              '1'" +
                        "             WHEN bs=5 THEN" +
                        "              CASE WHEN ct='1' THEN '*' ELSE '*/'||ct END" +
                        "           END END) D09," +
                        "       SUM(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'YYYYMMDD') <= to_char(PAYBACKDATE, 'YYYY') || '10' || to_char(PAYBACKDATE, 'DD') THEN" +
                        "              OVERDUECD" +
                        "             ELSE" +
                        "              '0'" +
                        "           END) OD10," +
                        "       MAX(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'MM') = '10' THEN" +
                        "              CASE" +
                        "             WHEN CLOSEDCD = 1 THEN" +
                        "              CASE WHEN ct='1' THEN 'N' ELSE 'N/'||ct END" +
                        "             WHEN OVERDUECD = 1 THEN" +
                        "              '1'" +
                        "             WHEN bs=5 THEN" +
                        "              CASE WHEN ct='1' THEN '*' ELSE '*/'||ct END" +
                        "           END END) D10," +
                        "       SUM(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'YYYYMMDD') <= to_char(PAYBACKDATE, 'YYYY') || '11' || to_char(PAYBACKDATE, 'DD') THEN" +
                        "              OVERDUECD" +
                        "             ELSE" +
                        "              '0'" +
                        "           END) OD11," +
                        "       MAX(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'MM') = '11' THEN" +
                        "              CASE" +
                        "             WHEN CLOSEDCD = 1 THEN" +
                        "              CASE WHEN ct='1' THEN 'N' ELSE 'N/'||ct END" +
                        "             WHEN OVERDUECD = 1 THEN" +
                        "              '1'" +
                        "             WHEN bs=5 THEN" +
                        "              CASE WHEN ct='1' THEN '*' ELSE '*/'||ct END" +
                        "           END END) D11," +
                        "       SUM(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'YYYYMMDD') <= to_char(PAYBACKDATE, 'YYYY') || '12' || to_char(PAYBACKDATE, 'DD') THEN" +
                        "              OVERDUECD" +
                        "             ELSE" +
                        "              '0'" +
                        "           END) OD12," +
                        "       MAX(CASE" +
                        "             WHEN to_char(PAYBACKDATE, 'MM') = '12' THEN" +
                        "              CASE" +
                        "             WHEN CLOSEDCD = 1 THEN" +
                        "              CASE WHEN ct='1' THEN 'N' ELSE 'N/'||ct END" +
                        "             WHEN OVERDUECD = 1 THEN" +
                        "              '1'" +
                        "             WHEN bs=5 THEN" +
                        "              CASE WHEN ct='1' THEN '*' ELSE '*/'||ct END" +
                        "           END END) D12" +
                        "  FROM t2 ";
                if (!searchdate.equals(""))
                    sql += "WHERE PAYBACKDATE<=to_date('" + searchdate + "','YYYYMMDD') ";
                sql += "   GROUP BY CONTRACTNO,to_char(PAYBACKDATE, 'YYYY')) " +
                        " " +
                        "SELECT rownum,a.* FROM " +
                        "(select c.CONTRACTNO,c.CLIENTNAME,x.PRINCIPALAMT,c.PRINCIPALAMT cPRINCIPALAMT,c.PRINCIPALAMT-p.SPRINCIPALAMT,ROUND(p.SPRINCIPALAMT/c.PRINCIPALAMT,2) PAYPERCENT,STAPAYBACKDATE,c.CLIENTACT," +
                        "  CASE WHEN D12 IS NOT NULL THEN CASE WHEN D12='1' AND OD12!='0' THEN to_char(OD12) ELSE D12 END ELSE '/' END D12," +
                        "  CASE WHEN D11 IS NOT NULL THEN CASE WHEN D11='1' AND OD11!='0' THEN to_char(OD11) ELSE D11 END ELSE '/' END D11," +
                        "  CASE WHEN D10 IS NOT NULL THEN CASE WHEN D10='1' AND OD10!='0' THEN to_char(OD10) ELSE D10 END ELSE '/' END D10," +
                        "  CASE WHEN D09 IS NOT NULL THEN CASE WHEN D09='1' AND OD09!='0' THEN to_char(OD09) ELSE D09 END ELSE '/' END D09," +
                        "  CASE WHEN D08 IS NOT NULL THEN CASE WHEN D08='1' AND OD08!='0' THEN to_char(OD08) ELSE D08 END ELSE '/' END D08," +
                        "  CASE WHEN D07 IS NOT NULL THEN CASE WHEN D07='1' AND OD07!='0' THEN to_char(OD07) ELSE D07 END ELSE '/' END D07," +
                        "  CASE WHEN D06 IS NOT NULL THEN CASE WHEN D06='1' AND OD06!='0' THEN to_char(OD06) ELSE D06 END ELSE '/' END D06," +
                        "  CASE WHEN D05 IS NOT NULL THEN CASE WHEN D05='1' AND OD05!='0' THEN to_char(OD05) ELSE D05 END ELSE '/' END D05," +
                        "  CASE WHEN D04 IS NOT NULL THEN CASE WHEN D04='1' AND OD04!='0' THEN to_char(OD04) ELSE D04 END ELSE '/' END D04," +
                        "  CASE WHEN D03 IS NOT NULL THEN CASE WHEN D03='1' AND OD03!='0' THEN to_char(OD03) ELSE D03 END ELSE '/' END D03," +
                        "  CASE WHEN D02 IS NOT NULL THEN CASE WHEN D02='1' AND OD02!='0' THEN to_char(OD02) ELSE D02 END ELSE '/' END D02," +
                        "  CASE WHEN D01 IS NOT NULL THEN CASE WHEN D01='1' AND OD01!='0' THEN to_char(OD01) ELSE D01 END ELSE '/' END D01 " +
                        "  " +
                        "FROM XFPOA p,XFCONTRACT c,t3 left outer join XFACTCUTPAYMAIN x on x.CONTRACTNO=t3.CONTRACTNO " +
                        "and to_char(x.PAYBACKDATE, 'YYYYMM') ='" + searchdate.substring(0, 6) + "' " +
                        "WHERE p.CONTRACTNO=c.CONTRACTNO AND c.CONTRACTNO=t3.CONTRACTNO " +
                        "and PAYBACKYEAR ='" + searchdate.substring(0, 4) + "' AND to_char(p.PAYBACKDATEFROM, 'YYYY') ='" + searchdate.substring(0, 4) + "' ";

                if (!clientnm.equals("")) sql += "and c.CLIENTNAME like '%" + clientnm + "%' ";
                if (!contractno.equals("")) sql += "and c.CONTRACTNO='" + contractno + "' ";
                if (!contfromdate.equals("")) sql += "and c.STARTDATE>=to_date('" + contfromdate + "','YYYYMMDD') ";
                if (!conttodate.equals("")) sql += "and c.STARTDATE<=to_date('" + conttodate + "','YYYYMMDD') ";
                if (!payfromdate.equals("")) sql += "and p.PAYBACKDATEFROM>=to_date('" + payfromdate + "','YYYYMMDD') ";
                if (!paytodate.equals("")) sql += "and p.PAYBACKDATETO<=to_date('" + paytodate + "','YYYYMMDD') ";
                if (!overduecd.equals("")) {
                    if (overduecd.equals("0")) sql += "and SOVERDUECD=0 ";
                    else if (overduecd.equals("1")) sql += "and SOVERDUECD>0 ";
                }
                sql += " ORDER BY PAYBACKYEAR,c.STARTDATE) a ";

                logger.debug("收本收息统计表SQL="+sql);
                excelMap.put("filenm", poiexceltemp + "\\个人消费贷款收本收息行为统计表.xls");
                if (!searchdate.equals("")) datestr += " " + searchdate;

            } else if (FORMID.equals("XFREPORT04")) {
                sql = "WITH t1 AS (SELECT a.CONTRACTNO,to_char(a.PAYBACKDATE, 'YYYY\"年\"MM\"月\"') PAYBACKDATE," +
                        "to_char(a.ODB_PAYBACKDATE, 'YYYY\"年\"MM\"月\"') ODB_PAYBACKDATE," +
                        "to_char(a.CLOSEDDATE, 'YYYY\"年\"MM\"月\"') CLOSEDDATE," +
                        "to_char(a.ODB_CLOSEDDATE, 'YYYY\"年\"MM\"月\"') ODB_CLOSEDDATE," +
                        "to_char(c.STARTDATE, 'YYYY\"年\"MM\"月\"') STARTDATE," +
                        "TERMINALREASON," +
                        "PAYBACKAMT,a.PRINCIPALAMT,SERVICECHARGEFEE,ODB_PAYBACKAMT,(DURATION-a.POANO) RD,TERMINALCD " +
                        "FROM XFACTCUTPAYMAIN a LEFT OUTER JOIN XFCONTRACT c ON c.CONTRACTNO=a.CONTRACTNO" +
                        "),  " +
                        "t2 AS (SELECT STARTDATE," +
                        "to_char(add_months(to_date(STARTDATE||'01','YYYY\"年\"MM\"月\"DD'),1), 'YYYY\"年\"MM\"月\"') STARTDATE2," +
                        "SUM(PAYBACKAMT) PAYBACKAMT,SUM(PRINCIPALAMT) PRINCIPALAMT,SUM(SERVICECHARGEFEE) SERVICECHARGEFEE," +
                        "SUM(decode(ODB_PAYBACKAMT,null,0,ODB_PAYBACKAMT)) ODB_PAYBACKAMT,COUNT(distinct CONTRACTNO) NEWCONTNUM " +
                        "FROM t1 GROUP BY STARTDATE ORDER  BY STARTDATE" +
                        ")," +
                        "t3 AS (SELECT PAYBACKDATE,SUM(PAYBACKAMT) PAYBACKAMT,round(AVG(RD),2) RD " +
                        "FROM t1 GROUP BY PAYBACKDATE" +
                        ")," +
                        "t4 AS (SELECT ODB_PAYBACKDATE,SUM(decode(ODB_PAYBACKAMT,null,0,ODB_PAYBACKAMT)) ODB_PAYBACKAMT " +
                        "FROM t1 GROUP BY ODB_PAYBACKDATE ORDER  BY ODB_PAYBACKDATE" +
                        ")," +
                        "t5 AS (SELECT CLOSEDDATE,SUM(PAYBACKAMT) PAYBACKAMT,SUM(PRINCIPALAMT) PRINCIPALAMT,SUM(SERVICECHARGEFEE) SERVICECHARGEFEE " +
                        "FROM t1 GROUP BY CLOSEDDATE ORDER  BY CLOSEDDATE" +
                        ")," +
                        "t6 AS (SELECT ODB_CLOSEDDATE,SUM(decode(ODB_PAYBACKAMT,null,0,ODB_PAYBACKAMT)) ODB_PAYBACKAMT " +
                        "FROM t1 GROUP BY ODB_CLOSEDDATE ORDER  BY ODB_CLOSEDDATE" +
                        ")," +
                        "t7 AS (SELECT to_char(TERMINALDATE, 'YYYY\"年\"MM\"月\"') TERMINALDATE," +
                        "SUM(CASE WHEN TERMINALREASON='0' THEN 1 ELSE 0 END) CT0," +
                        "SUM(CASE WHEN TERMINALREASON='1' THEN 1 ELSE 0 END) CT1," +
                        "SUM(CASE WHEN TERMINALREASON='2' THEN 1 ELSE 0 END) CT2,COUNT(CONTRACTNO) CLOSECONTNUM " +
                        "FROM XFCONTRACT GROUP BY to_char(TERMINALDATE, 'YYYY\"年\"MM\"月\"') ORDER  BY TERMINALDATE" +
                        ")," +
                        "t8 AS (SELECT TERMINALDATE," +
                        "decode(TERMINALDATE,null,null,to_char(add_months(to_date(TERMINALDATE||'01','YYYY\"年\"MM\"月\"DD'),1), 'YYYY\"年\"MM\"月\"')) TERMINALDATE2," +
                        "CLOSECONTNUM FROM t7" +
                        ")," +
                        "" +
                        "b1 AS (SELECT STARTDATE,STARTDATE2,SUM(t2.PAYBACKAMT+t2.ODB_PAYBACKAMT) over (ORDER BY STARTDATE) AMT1," +
                        "(t3.PAYBACKAMT+decode(t4.ODB_PAYBACKAMT,NULL,0,t4.ODB_PAYBACKAMT)) BILLAMT,t3.PAYBACKAMT AMT2," +
                        "t4.ODB_PAYBACKAMT AMT3,t5.PAYBACKAMT AMT4,t5.PRINCIPALAMT AMT5,t5.SERVICECHARGEFEE AMT6,t6.ODB_PAYBACKAMT AMT7," +
                        "SUM(NEWCONTNUM) over (ORDER BY STARTDATE) CONTNUM,CT0,CT1,CT2,RD FROM t2 " +
                        "LEFT OUTER JOIN t3 ON t2.STARTDATE2=t3.PAYBACKDATE " +
                        "LEFT OUTER JOIN t4 ON t2.STARTDATE2=t4.ODB_PAYBACKDATE " +
                        "LEFT OUTER JOIN t5 ON t2.STARTDATE2=t5.CLOSEDDATE " +
                        "LEFT OUTER JOIN t6 ON t2.STARTDATE2=t6.ODB_CLOSEDDATE " +
                        "LEFT OUTER JOIN t7 ON t2.STARTDATE2=t7.TERMINALDATE" +
                        ")," +
                        "b2 AS (SELECT STARTDATE,STARTDATE2," +
                        "SUM(t5.PAYBACKAMT+decode(t6.ODB_PAYBACKAMT,NULL,0,t6.ODB_PAYBACKAMT)) over (ORDER BY STARTDATE) CLOSEAMT FROM t2 " +
                        "LEFT OUTER JOIN t5 ON t2.STARTDATE=t5.CLOSEDDATE " +
                        "LEFT OUTER JOIN t6 ON t2.STARTDATE=t6.ODB_CLOSEDDATE" +
                        ")," +
                        "b3 AS (SELECT STARTDATE,STARTDATE2,t2.PRINCIPALAMT AMT8,t2.SERVICECHARGEFEE AMT9,t2.NEWCONTNUM FROM t2)," +
                        "b4 AS (SELECT TERMINALDATE2,SUM(CLOSECONTNUM) over (ORDER BY TERMINALDATE2) CLOSECONTNUM FROM t8)," +
                        "b5 AS (SELECT b1.STARTDATE2,AMT1,decode(CLOSEAMT,NULL,0,CLOSEAMT) CLOSEAMT,BILLAMT,AMT2,AMT3," +
                        "decode(AMT4,NULL,0,AMT4) AMT4,decode(AMT5,NULL,0,AMT5) AMT5,decode(AMT6,NULL,0,AMT6) AMT6," +
                        "decode(AMT7,NULL,0,AMT7) AMT7,decode(b3.AMT8,NULL,0,b3.AMT8) AMT8,decode(b3.AMT9,NULL,0,b3.AMT9) AMT9," +
                        "CONTNUM,decode(CT0,NULL,0,CT0) CT0,decode(CT1,NULL,0,CT1) CT1,decode(CT2,NULL,0,CT2) CT2," +
                        "decode(b3.NEWCONTNUM,NULL,0,b3.NEWCONTNUM) NEWCONTNUM,RD " +
                        "FROM b1 INNER JOIN b2 ON b1.STARTDATE2=b2.STARTDATE2 LEFT OUTER JOIN b3 ON b2.STARTDATE2=b3.STARTDATE" +
                        ")," +
                        "b6 AS (SELECT STARTDATE2,AMT1,CLOSEAMT,BILLAMT,AMT2,AMT3,AMT4,AMT5,AMT6,AMT7,AMT8,AMT9,CONTNUM," +
                        "decode(CLOSECONTNUM,NULL,0,CLOSECONTNUM) CLOSECONTNUM,CT0,CT1,CT2,NEWCONTNUM,RD FROM b5 " +
                        "LEFT OUTER JOIN b4 ON b5.STARTDATE2=b4.TERMINALDATE2" +
                        ")" +
                        "" +
                        "SELECT STARTDATE2,(AMT1-CLOSEAMT) BEGINMONAMT,BILLAMT,(AMT4+AMT7) CLOSEMONAMT,AMT5,AMT6,AMT7," +
                        "round((BILLAMT-AMT4-AMT7)/BILLAMT,2),AMT8,AMT9,(AMT1-CLOSEAMT+AMT8+AMT9-AMT4-AMT7) ENDMONAMT,'' KK," +
                        "CONTNUM-CLOSECONTNUM,CT0,CT1,CT2,NEWCONTNUM,CONTNUM-CLOSECONTNUM+NEWCONTNUM-CT0-CT1-CT2,RD " +
                        "FROM b6 where 1=1 ";


                if (!contfromdate.equals("")) sql += "and STARTDATE2>=to_char(to_date('" + contfromdate + "','YYYYMMDD'),'YYYY\"年\"MM\"月\"') ";
                if (!conttodate.equals("")) sql += "and STARTDATE2<=to_char(to_date('" + conttodate + "','YYYYMMDD'),'YYYY\"年\"MM\"月\"') ";

                sql += " ORDER BY STARTDATE2";

                excelMap.put("filenm", poiexceltemp + "\\消费信贷组合趋势分析表.xls");
                if (!contfromdate.equals("")) datestr += " 自" + contfromdate;
                if (!conttodate.equals("")) datestr += " 至" + conttodate;

            }else if (FORMID.equals("XFREPORT10")) {     //客户基本信息表
                sql = "SELECT rownum,T0.* from (" +
                        "SELECT c.appno, c.NAME,x.GRADE,m.APPAMT,m.DIVID, c.ID," +
                        "trunc(Months_between(a.APPDATE,BIRTHDAY)/12) AGE," +
                        "(SELECT enudt FROM ptenuminfodetl  WHERE enutp=gender AND ENUID='Gender') gender," +
                        "(SELECT enudt FROM ptenuminfodetl  WHERE enutp=residenceadr AND ENUID='ResidenceADR') residenceadr," +
                        "(SELECT enudt FROM ptenuminfodetl  WHERE enutp=marriagestatus AND ENUID='MarriageStatus') marriagestatus," +
                        "(SELECT enudt FROM ptenuminfodetl  WHERE enutp=healthstatus AND ENUID='HealthStatus') healthstatus," +
                        "(SELECT enudt FROM ptenuminfodetl  WHERE enutp=edulevel AND ENUID='EduLevel') edulevel," +
                        "(SELECT enudt FROM ptenuminfodetl  WHERE enutp=qualification AND ENUID='Qualification') qualification," +
                        "currentaddress,phone2,pc,phone1,email,company,phone3,compc,comaddr," +
                        "(SELECT enudt FROM ptenuminfodetl  WHERE enutp=c.CLIENTTYPE AND ENUID='ClientType1') CLIENTTYPE," +
                        "CASE WHEN g.sort6='1' THEN '受雇' ELSE '自雇' END SORT6," +
                        "empno," +
                        "(SELECT enudt FROM ptenuminfodetl  WHERE enutp=title AND ENUID='Title') title,servfrom||'年'," +
                        "CONFMONPAY,REPLACE(trim(m.COMMNAME),chr(10),'') COMMNAME,m.NUM," +
                        "(SELECT enudt FROM ptenuminfodetl  WHERE enutp=c.actopeningbank AND ENUID='Bank') actopeningbank,resdaddr,LINKMAN," +
                        "(SELECT enudt FROM ptenuminfodetl  WHERE enutp=APPRELATION AND ENUID='AppRelation') APPRELATION,LINKMANPHONE1," +
                        "CASE WHEN APPSTATUS='11' THEN '通过' WHEN APPSTATUS='99' THEN '未通过' ELSE '审批中' END APPSTATUSNM " +
                        "FROM xfclient c,XFAPPCOMM m,XFAPPADD d,XFAPP a " +
                        "LEFT OUTER JOIN XFOPINION x on x.appno=a.appno AND OPINIONNO=3 " +
                        "LEFT OUTER JOIN XFCONTRACT t on t.appno=a.appno " +
                        "LEFT OUTER JOIN XFAPPGRADE g on g.appno=a.appno " +
                        "WHERE c.appno=a.appno AND c.XFCLTP=1 and m.appno=a.appno " +
                        "AND d.appno=a.appno AND APPSTATUS NOT IN ('0','0.1','0.2') ";

                if (!clientnm.equals("")) sql += "and c.NAME like '%" + clientnm + "%' ";
                if (!contractno.equals("")) sql += "and t.CONTRACTNO='" + contractno + "' ";
                if (!gender.equals("")) sql += "and c.GENDER='" + gender + "' ";
                if (!marriagestatus.equals("")) sql += "and c.MARRIAGESTATUS='" + marriagestatus + "' ";
                if (!residenceadr.equals("")) sql += "and c.RESIDENCEADR='" + residenceadr + "' ";
                if (edulevel.equals("0")) sql += "and c.EDULEVEL<'" + edulevel + "' ";
                if (!commtype.equals("")) sql += "and t.CONTRACTNO like '" + commtype + "%' ";
                if (!contfromdate.equals("")) sql += "and t.STARTDATE>=to_date('" + contfromdate + "','YYYYMMDD') ";
                if (!conttodate.equals("")) sql += "and t.STARTDATE<=to_date('" + conttodate + "','YYYYMMDD') ";
                if (!appfromdate.equals("")) sql += "and a.APPDATE>=to_date('" + appfromdate + "','YYYYMMDD') ";
                if (!apptodate.equals("")) sql += "and a.APPDATE<=to_date('" + apptodate + "','YYYYMMDD') ";

                sql += " ORDER BY APPSTATUSNM,a.APPDATE desc) T0";

                excelMap.put("filenm", poiexceltemp + "\\客户基本信息.xls");
                if (!contfromdate.equals("")) datestr += " 自" + contfromdate;
                if (!conttodate.equals("")) datestr += " 至" + conttodate;

            } else if (FORMID.equals("XFREPORT11")) {
                sql = "SELECT ROWNUM, CONTRACTNO, CLIENTNAME, CLIENTACT,PAIDUPDATE, PRINCIPALAMT, SERVICECHARGEFEE, LATEFEE, BREACHFEE,PAIDUPAMT " +
                        "FROM XFACTCUTPAYDETL WHERE BillStatus>=6 ";
                if (!searchdate.equals("")) sql += "and PAIDUPDATE=to_date('" + searchdate + "','YYYYMMDD') ";

                excelMap.put("filenm", poiexceltemp + "\\收本收息表.xls");
                if (!searchdate.equals("")) datestr += " " + searchdate;
            }


            try {
                ConnectionManager cmanager = ConnectionManager.getInstance();
                CachedRowSet crs = cmanager.getRs(sql);

                if (crs.size() == 0) {
                    instance.getFormBean().getElement("savebtn").setDescription("newwin");
                    ctx.setRequestAtrribute("msg", "没有符合条件的数据！");
                    ctx.setRequestAtrribute("isback", "0");
                    ctx.setRequestAtrribute("funcdel", "history.go(-1)");
                    ctx.setTarget("/showinfo.jsp");
                    return -1;
                }

                excelMap.put("date", datestr);
                excelMap.put("cellname", "dataArea");
                excelMap.put("crs", crs);

                ctx.getRequest().setCharacterEncoding("iso8859-1");
                ctx.getRequest().getSession().setAttribute("excelMap", excelMap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ctx.setTarget("/excelByTmp");
        }
        return 0;
    }
}
