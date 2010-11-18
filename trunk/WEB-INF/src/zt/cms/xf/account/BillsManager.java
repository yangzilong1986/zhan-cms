package zt.cms.xf.account;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import zt.cms.xf.common.constant.XFBillStatus;
import zt.cms.xf.common.constant.XFCommon;
import zt.cms.xf.common.constant.XFContractStatus;
import zt.cms.xf.common.dao.XfactcutpaydetlDao;
import zt.cms.xf.common.dao.XfactcutpaymainDao;
import zt.cms.xf.common.dao.XfcontractDao;
import zt.cms.xf.common.dto.*;
import zt.cms.xf.common.exceptions.XfactcutpaydetlDaoException;
import zt.cms.xf.common.factory.XfactcutpaydetlDaoFactory;
import zt.cms.xf.common.factory.XfactcutpaymainDaoFactory;
import zt.cms.xf.common.factory.XfcontractDaoFactory;
import zt.cmsi.mydb.MyDB;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.user.UserManager;
import zt.platform.utils.Debug;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-4-30
 * Time: 11:11:18
 * To change this template use File | Settings | File Templates.
 */
public class BillsManager {


    public static Log logger = LogFactory.getLog(BillsManager.class);

    private String chargeoffdate;

    public BillsManager(String chargeoffdate) {
        this.chargeoffdate = chargeoffdate;
    }


    /*
    每日日始时帐单生成处理：
    1、调用本方法前应确保前日 已出帐单 的入帐动作完成 （日终处理）
    2、根据当前业务日期逐笔过滤分期主帐单表记录（ACTCUTPAYMAIN）
             1）若本期基本帐单约定扣款日日期<=当前业务日期
                   a)若本期基本帐单未出帐，根据本期基本帐单约定还款额直接生成当日扣款记录
                   b)若本期基本帐单已出帐，且本期基本帐单未结清
                   C)若本期基本帐单已出帐，且本期基本帐单已结清
                        1、若未逾期，不做处理。
                        2、若已逾期，且本期逾期帐单已结清，不做处理
                        3、若已逾期，且本期逾期帐单未结清，且本期逾期帐单约定扣款日日期 >当前业务日期，不做处理
                        4、若已逾期，且本期逾期帐单未结清，且本期逾期帐单约定扣款日日期 <=当前业务日期，根据本期逾期帐单约定还款额直接生成当日扣款记录


                   不做处理，若未结清，查看是否逾期：
                       1、未逾期且在宽限期内，  根据约定还款额直接生成当日扣款记录
                       2、未逾期且不在宽限期内，置逾期标志，根据约定还款额生成当日扣款记录
                       3、已逾期的： （重要）
                              1、首先判断本期基本帐单是否已结清（CLOSEDCD=‘1‘？），如果未结清，根据约定还款额直接生成当日扣款记录
                              2、如果本期基本帐单已结清，再判断本期逾期帐单是否已结清 （ODB_CLOSEDCD=‘1‘？）
                                   如果逾期帐单未结清

                             2 最后还款日期不为空的： 根据 约定还款余额和已出帐的滞纳金违约金生成扣款记录

    */

    /*
    扣款帐单生成处理流程：
        对于未逾期帐单：
        1、每月13日开始生成扣款文件，生成前检查已出帐的帐单，要求关闭所有已出帐帐单（超时的需进行重发处理）。
        2、在宽限期内，可以多次生成扣款文件，生成前需关闭已出帐单。
        3、超出宽限期未扣款成功的帐单，形成逾期帐单。
                根据当前用户输入的业务日期

        对于已逾期帐单：
        1、在宽限期外的可进行主动还款，不再由系统自动代扣。
        2、还款时生成滞纳金、违约金，在下期帐单中出帐。
        3、宽限期内，如已形成滞纳金违约金，进行代扣操作，优先级高于当前期正常代扣帐单。
     */

    public int generateBills(SessionContext ctx, DatabaseConnection conn, ErrorMessages msgs) throws Exception {

        Connection sqlconn = null;

        try {

            MyDB.getInstance().addDBConn(conn);
            //conn.setAuto(false);                        //TODO
            sqlconn = conn.getConnection();
            sqlconn.setAutoCommit(false);

            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

            XfactcutpaymainDao maindao = XfactcutpaymainDaoFactory.create(sqlconn);
            XfactcutpaymainPk mainPk = new XfactcutpaymainPk();

//            String sql = " chargeoffcd = ? and to_char(paybackdate, 'yyyyMMdd') <= ?";


            String sql = "(chargeoffcd = '0' and to_char(paybackdate, 'yyyyMMdd') <= " + chargeoffdate + ")  or " +
                    "(chargeoffcd = '1' and  closedcd = '0' and to_char(paybackdate, 'yyyyMMdd') <= " + chargeoffdate + ") or " +
                    "(chargeoffcd = '1' and  closedcd = '1' and  overduecd = '1' and  odb_chargeoffcd = '0' and to_char(odb_paybackdate, 'yyyyMMdd') <= " + chargeoffdate + ") or " +
                    "(chargeoffcd = '1' and  closedcd = '1' and  overduecd = '1' and  odb_chargeoffcd = '1' and  odb_closedcd = '0' and to_char(odb_paybackdate, 'yyyyMMdd') <=" + chargeoffdate + ")";


//            Object[] sqlparams = new Object[2];
//
//            sqlparams[0] = "0";  //未出帐标志
//            sqlparams[1] = chargeoffdate;
//            Xfactcutpaymain[] xfactcutpaymain = maindao.findByDynamicWhere(sql, sqlparams);

            Xfactcutpaymain[] xfactcutpaymain = maindao.findByDynamicWhere(sql, null);
            //logger.info(Integer.toString(xfactcutpaymain.length));

            int count = 0;
            if ( xfactcutpaymain.length == 0) {
                return 0;
            }


            XfactcutpaydetlDao detldao = XfactcutpaydetlDaoFactory.create(sqlconn);
            Xfactcutpaydetl xfactcutpaydetl = new Xfactcutpaydetl();

            String journalnoSql = "select max(journalno) from xfactcutpaydetl where substr(journalno,1,8) = '" + chargeoffdate + "'";
            RecordSet rs = conn.executeQuery(journalnoSql);

            int maxno = 0;
            if (rs.next()) {
                String max = rs.getString(0);
                if (max != null) {
                    maxno = Integer.parseInt(max.substring(8));
                }
            }

            SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");

            //根据分期主帐单生成明细帐单
            for (int i = 0; i < xfactcutpaymain.length; i++) {

/*
                //根据主帐单中的合同号从合同表中取得手续费率
                XfcontractDao contractDao = XfcontractDaoFactory.create(sqlconn);
                Xfcontract contract = contractDao.findByPrimaryKey(xfactcutpaymain[i].getContractno());
                BigDecimal servicecharge = contract.getServicecharge();
*/

                //20090730  zhanrui
                //增加提前还款处理： 主帐单中已置“提前还款核准标志” 的记录不做处理
                if (xfactcutpaymain[i].getPrecutpaycd().equals("1")) {
                    continue;
                }

                PropertyUtils.copyProperties(xfactcutpaydetl, xfactcutpaymain[i]);

                //流水号生成
                maxno++;
                String journalno = chargeoffdate + StringUtils.leftPad(Integer.toString(maxno), 5, "0");
                xfactcutpaydetl.setJournalno(journalno);

                xfactcutpaydetl.setContractno(xfactcutpaymain[i].getContractno());

                xfactcutpaydetl.setBillstatus(XFBillStatus.BILLSTATUS_CHECK_PENDING);


                String curruser = StringUtils.trimToEmpty(um.getUserName());
                xfactcutpaydetl.setOperatorid(curruser);
                xfactcutpaydetl.setCreatorid(curruser);

                xfactcutpaydetl.setOperatedate(new Date());
                xfactcutpaydetl.setCreatedate(new Date());

                xfactcutpaydetl.setCheckerid(null);
                xfactcutpaydetl.setCheckdate(null);

                xfactcutpaydetl.setPaidupamt(BigDecimal.valueOf(0));
                xfactcutpaydetl.setStartdate(sFmt.parse(chargeoffdate));
                xfactcutpaydetl.setBilltype("0"); //默认为正常帐单

                if (xfactcutpaymain[i].getChargeoffcd().equals("1")
                        && xfactcutpaymain[i].getClosedcd().equals("1")
                        && xfactcutpaymain[i].getOverduecd().equals("1")) { //逾期帐单

                    xfactcutpaydetl.setPaybackdate(xfactcutpaymain[i].getOdbPaybackdate());
                    xfactcutpaydetl.setPaybackamt(xfactcutpaymain[i].getOdbPaybackamt());
                    xfactcutpaydetl.setPaybackdate(xfactcutpaymain[i].getOdbPaybackdate());
                    xfactcutpaydetl.setBilltype("1"); //逾期帐单

                    xfactcutpaydetl.setPrincipalamt(BigDecimal.valueOf(0));
                    xfactcutpaydetl.setServicechargefee(BigDecimal.valueOf(0));
                    xfactcutpaydetl.setLatefee(xfactcutpaymain[i].getOdbLatefee());
                    xfactcutpaydetl.setBreachfee(xfactcutpaymain[i].getOdbBreachfee());

                    //设置逾期帐单已出帐标志以及出帐日期
                    if ("0".equals(xfactcutpaymain[i].getOdbChargeoffcd())) {
                        xfactcutpaymain[i].setOdbChargeoffcd("1");
                        xfactcutpaymain[i].setOdbStartdate(sFmt.parse(chargeoffdate));
                    }
                } else {
                    xfactcutpaydetl.setLatefee(BigDecimal.valueOf(0));
                    xfactcutpaydetl.setBreachfee(BigDecimal.valueOf(0));
                    xfactcutpaydetl.setPrincipalamt(xfactcutpaymain[i].getPrincipalamt());
                    xfactcutpaydetl.setServicechargefee(xfactcutpaymain[i].getServicechargefee());
                    //设置基本帐单已出帐标志以及出帐日期
                    if ("0".equals(xfactcutpaymain[i].getChargeoffcd())) {
                        xfactcutpaymain[i].setChargeoffcd("1");
                        xfactcutpaymain[i].setStartdate(sFmt.parse(chargeoffdate));
                    }
                }

                //生成新的明细帐单
                detldao.insert(xfactcutpaydetl);

                mainPk.setContractno(xfactcutpaymain[i].getContractno());
                mainPk.setPoano(xfactcutpaymain[i].getPoano());
                maindao.update(mainPk, xfactcutpaymain[i]);
                count++;
            }

            sqlconn.commit();
            return count;

        } catch (Exception e) {
            if (sqlconn != null) {
                try {
                    sqlconn.rollback();
                }
                catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            msgs.add("发生异常：" + e.getMessage());
            Debug.debug(e);
            throw new Exception(e);
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }

    /*
    提前还款帐单生成
     */
    public int generatePreCutPayBills(SessionContext ctx, DatabaseConnection conn, ErrorMessages msgs) throws Exception {

        Connection sqlconn = null;

        try {

            MyDB.getInstance().addDBConn(conn);
            sqlconn = conn.getConnection();
            sqlconn.setAutoCommit(false);

            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

            XfcontractDao contractdao = XfcontractDaoFactory.create(sqlconn);

            //处理合同状态为提前还款审核通过的合同
            String contractsql = " cstatus = " + XFContractStatus.TIQIANHUANKUAN_TONGGUO;
            Xfcontract[] contracts = contractdao.findByDynamicWhere(contractsql, null);

            if (contracts.length == 0) {
                return 0;
            }

            XfactcutpaydetlDao detldao = XfactcutpaydetlDaoFactory.create(sqlconn);
            Xfactcutpaydetl xfactcutpaydetl = new Xfactcutpaydetl();

            String journalnoSql = "select max(journalno) from xfactcutpaydetl where substr(journalno,1,8) = '" + chargeoffdate + "'";
            RecordSet rs = conn.executeQuery(journalnoSql);

            long maxno = 0;
            if (rs.next()) {
                String max = rs.getString(0);
                if (max != null) {
                    maxno = Long.parseLong(max.substring(8));
                }
            }

            SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");


            XfactcutpaymainDao maindao = XfactcutpaymainDaoFactory.create(sqlconn);
            XfactcutpaymainPk mainpk = new XfactcutpaymainPk();
            String mainsql = "(chargeoffcd = '0' and to_char(precutpaydate, 'yyyyMMdd') <= " + chargeoffdate + ")  or " +
                    "(chargeoffcd = '1' and  closedcd = '0' and to_char(precutpaydate, 'yyyyMMdd') <= " + chargeoffdate + ") " +
                    " and contractno = '";

            int count = 0;
            for (Xfcontract contract : contracts) {
                String tempsql = mainsql + contract.getContractno() + "' and precutpaycd = '1'";
                Xfactcutpaymain[] xfactcutpaymain = maindao.findByDynamicWhere(tempsql, null);
                if (xfactcutpaymain.length == 0) {
                    continue;
                }

                BigDecimal totalamt = new BigDecimal(0.00);
                BigDecimal principal = new BigDecimal(0.00);
                BigDecimal servicechargefee = new BigDecimal(0.00);

                for (Xfactcutpaymain main : xfactcutpaymain) {
                    totalamt = totalamt.add(main.getPaybackamt());
                    principal = principal.add(main.getPrincipalamt());
                    servicechargefee = servicechargefee.add(main.getServicechargefee());
                    //设置基本帐单已出帐标志以及出帐日期
                    if ("0".equals(main.getChargeoffcd())) {
                        main.setChargeoffcd("1");
                        main.setStartdate(sFmt.parse(chargeoffdate));
                    }
                    mainpk.setContractno(main.getContractno());
                    mainpk.setPoano(main.getPoano());
                    maindao.update(mainpk, main);
                }

                if (!totalamt.equals(principal.add(servicechargefee))) {
                    msgs.add("生成扣款帐单时金额核对不符！");
                    throw new Exception("生成扣款帐单时金额核对不符！");
                }

                //插入一笔明细扣款记录（CUTPAYDETL表）
                PropertyUtils.copyProperties(xfactcutpaydetl, xfactcutpaymain[0]);

                //流水号生成
                String journalno = chargeoffdate + StringUtils.leftPad(Long.toString(maxno + 1), 5, "0");
                maxno++;
                xfactcutpaydetl.setJournalno(journalno);

                xfactcutpaydetl.setContractno(contract.getContractno());

                xfactcutpaydetl.setBillstatus(XFBillStatus.BILLSTATUS_CHECK_PENDING);

                String curruser = StringUtils.trimToEmpty(um.getUserName());
                xfactcutpaydetl.setOperatorid(curruser);
                xfactcutpaydetl.setCreatorid(curruser);

                xfactcutpaydetl.setOperatedate(new Date());
                xfactcutpaydetl.setCreatedate(new Date());

                xfactcutpaydetl.setCheckerid(null);
                xfactcutpaydetl.setCheckdate(null);

                xfactcutpaydetl.setPaidupamt(BigDecimal.valueOf(0));
                xfactcutpaydetl.setStartdate(sFmt.parse(chargeoffdate));

                xfactcutpaydetl.setBilltype("2"); //提前还款帐单

                xfactcutpaydetl.setPaybackamt(totalamt);
                xfactcutpaydetl.setLatefee(BigDecimal.valueOf(0));
                xfactcutpaydetl.setBreachfee(BigDecimal.valueOf(0));
                xfactcutpaydetl.setPrincipalamt(principal);
                xfactcutpaydetl.setServicechargefee(servicechargefee);

                //生成新的明细帐单
                detldao.insert(xfactcutpaydetl);

                count++;
            }
            sqlconn.commit();
            return count;
        } catch (Exception e) {
            if (sqlconn != null) {
                try {
                    sqlconn.rollback();
                }
                catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            msgs.add("发生异常：" + e.getMessage());
            Debug.debug(e);
            throw new Exception(e);
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }


    /*
    返回扣款未成功的记录集
     */
    private Xfactcutpaydetl[] getFailedPayDetl(SessionContext ctx, Connection conn)
            throws XfactcutpaydetlDaoException {

        String sql = " billstatus = " + XFBillStatus.BILLSTATUS_CUTPAY_FAILED +
                " order by journalno";

        Xfactcutpaydetl[] xfactcutpaydetl = null;

        try {
            XfactcutpaydetlDao dao = XfactcutpaydetlDaoFactory.create(conn);
            xfactcutpaydetl = dao.findByDynamicWhere(sql, null);

            logger.debug(Integer.toString(xfactcutpaydetl.length));

            return xfactcutpaydetl;
        } catch (Exception e) {
            Debug.debug(e);
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }

    }

//    /*
//    返回当前日期之前的扣款未成功的记录集
//     */
//    private Xfactcutpaydetl[] getFailedPayDetl(SessionContext ctx, Connection conn)
//            throws XfactcutpaydetlDaoException {
//
//        String sql = " billstatus = ? and to_char(startdate, 'yyyyMMdd') < ?";
//        Object[] sqlparams = new Object[2];
//
//        sqlparams[0] = XFBillStatus.BILLSTATUS_CUTPAY_FAILED;  //扣款不成功标志
//
//        //  SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");
//        //  String strDate = sFmt.format(new Date());
//
//        sqlparams[1] = ctx.getParameter("CHARGEOFFDATE");
//
//        Xfactcutpaydetl[] xfactcutpaydetl = null;
//
//        try {
//            XfactcutpaydetlDao dao = XfactcutpaydetlDaoFactory.create(conn);
//            xfactcutpaydetl = dao.findByDynamicWhere(sql, sqlparams);
//
//            logger.info(Integer.toString(xfactcutpaydetl.length));
//        } catch (Exception e) {
//            Debug.debug(e);
//            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
//        }
//        return xfactcutpaydetl;
//    }
//
//
//    private int doQueryBills(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
//                             ErrorMessages msgs) {
//
//        Connection sqlconn = null;
//
//        try {
//
//            MyDB.getInstance().addDBConn(conn);
//            sqlconn = conn.getConnection();
//
//            XfactcutpaymainDao maindao = XfactcutpaymainDaoFactory.create(sqlconn);
//            XfactcutpaymainPk pk = new XfactcutpaymainPk();
//
//            //TODO:扣款日期的处理，建行的是否需要提前一天？
//            String sql = " chargeoffcd = ? and to_char(paybackdate, 'yyyyMMdd') <= ?";
//            Object[] sqlparams = new Object[2];
//
//            sqlparams[0] = "0";  //未出帐标志
//
//            SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");
//            String strDate = sFmt.format(new Date());
//
//            sqlparams[1] = strDate;
//
//            Xfactcutpaymain[] xfactcutpaymain = maindao.findByDynamicWhere(sql, sqlparams);
//
//            return xfactcutpaymain.length;
//
//        } catch (Exception e) {
//            msgs.add("发生异常：" + e.getMessage());
//            Debug.debug(e);
//            return -1;
//        } finally {
//            MyDB.getInstance().releaseDBConn();
//        }
//    }

    /*
    统计未关闭帐单数量（未处理的已出帐单，或发生超时后未进行后续处理的帐单?）
     */

    public int accountOpenedBills(DatabaseConnection conn) throws XfactcutpaydetlDaoException {

        Connection sqlconn = null;

        try {

            MyDB.getInstance().addDBConn(conn);
            sqlconn = conn.getConnection();

            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create(sqlconn);
            //XfactcutpaymainPk pk = new XfactcutpaymainPk();

//            String sql = " billstatus <> ?  and  billstatus <> ?";
//            Object[] sqlparams = new Object[2];
//
//            sqlparams[0] = XFBillStatus.BILLSTATUS_CUTPAY_SUCCESS;  //扣款成功
//            sqlparams[1] = XFBillStatus.BILLSTATUS_CUTPAY_FAILED;  //扣款失败

            String sql = " to_number(billstatus) < " + Integer.parseInt(XFBillStatus.BILLSTATUS_CUTPAY_FAILED);

//            Object[] sqlparams = new Object[1];


            Xfactcutpaydetl[] xfactcutpaydetl = detlDao.findByDynamicWhere(sql, null);

            logger.debug(sql);
            return xfactcutpaydetl.length;

        } catch (Exception e) {
            Debug.debug(e);
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }



    public void setCutpayDetlPaidupSuccessBatch(Xfactcutpaydetl[] xfactcutpaydetls) throws XfactcutpaydetlDaoException {

        for  (Xfactcutpaydetl detl: xfactcutpaydetls) {
                setCutpayDetlPaidupSuccess(detl.getJournalno());
        }
            
    }

    /**
     * 2010/11/18  新信贷上机
     * @param journalno
     * @throws XfactcutpaydetlDaoException
     */
    public void setCutpayDetlPaidupSuccess(String journalno) throws XfactcutpaydetlDaoException {

        Connection conn = null;

        try {
            DatabaseConnection dc = new DatabaseConnection();
            conn = dc.getConnection();
            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create(conn);
            XfactcutpaydetlPk detlPk = new XfactcutpaydetlPk();
            Xfactcutpaydetl cutpaydetl = null;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = sdf.parse(chargeoffdate);

            cutpaydetl = detlDao.findByPrimaryKey(journalno);
            detlPk.setJournalno(journalno);

            cutpaydetl.setPaidupamt(cutpaydetl.getPaybackamt());
            cutpaydetl.setPaidupdate(date);
            cutpaydetl.setBillstatus(XFBillStatus.BILLSTATUS_CUTPAY_SUCCESS);

            detlDao.update(detlPk, cutpaydetl);
        } catch (Exception e) {
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }
    }


    /*
    单笔扣款成功后的处理   2010/10/21 之后 废弃
    */
    public void setCutpayDetlPaidupSuccess_20101021(String journalno) throws XfactcutpaydetlDaoException {

        Connection conn = null;

        try {
            DatabaseConnection dc = new DatabaseConnection();
            conn = dc.getConnection();
            conn.setAutoCommit(false);

            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create(conn);
            XfactcutpaydetlPk detlPk = new XfactcutpaydetlPk();
            Xfactcutpaydetl cutpaydetl = null;

            XfactcutpaymainDao mainDao = XfactcutpaymainDaoFactory.create(conn);
            XfactcutpaymainPk mainPk = new XfactcutpaymainPk();
            Xfactcutpaymain cutpaymain = null;

            XfcontractDao contractDao = XfcontractDaoFactory.create(conn);
            XfcontractPk contractPk = new XfcontractPk();
            Xfcontract contract = null;

            BigDecimal latefeerate = new BigDecimal(XFCommon.COMMON_LATEFEERATE);
            BigDecimal breachfeerate = new BigDecimal(XFCommon.COMMON_BREACHFEERATE);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = sdf.parse(chargeoffdate);

            //根据用户输入日期取得逾期帐单还款日
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, XFCommon.COMMON_CHARGEOFFDATE);
            cal.add(Calendar.MONTH, 1);
            java.sql.Date OverduePayBackDate = new java.sql.Date(cal.getTime().getTime());

            cutpaydetl = detlDao.findByPrimaryKey(journalno);
            detlPk.setJournalno(journalno);

            cutpaydetl.setPaidupamt(cutpaydetl.getPaybackamt());
            cutpaydetl.setPaidupdate(date);
            cutpaydetl.setBillstatus(XFBillStatus.BILLSTATUS_CUTPAY_SUCCESS);

            detlDao.update(detlPk, cutpaydetl);

            //更新主帐单
            mainPk.setContractno(cutpaydetl.getContractno());
            mainPk.setPoano(cutpaydetl.getPoano());
            cutpaymain = mainDao.findByPrimaryKey(mainPk);

            //TODO:事务处理 判断 cutpaymain 为NULL情况

            int terminalcd = 0;  //是否为最后一期帐单，结清标志

            if (cutpaydetl.getBilltype().equals("0")) {     //明细帐单为正常帐单
                if (cutpaymain.getClosedcd().equals("0")) {
                    cutpaymain.setClosedcd("1");
                    cutpaymain.setCloseddate(date);
                    if (cutpaymain.getOverduecd().equals("1")) { //主帐单已置逾期标志时，计算机滞纳金以及根据日期计算违约金
                        //滞纳金
                        BigDecimal latefee = cutpaydetl.getPaybackamt().multiply(latefeerate);
                        latefee = latefee.setScale(2, BigDecimal.ROUND_HALF_UP);

                        if (latefee.compareTo(new BigDecimal(10)) == -1) {
                            latefee = new BigDecimal(10);
                        }
                        cutpaymain.setOdbLatefee(latefee);

                        //违约金 按天数计算
                        long days = (date.getTime() - cutpaymain.getPaybackdate().getTime()) / (24 * 60 * 60 * 1000);
                        BigDecimal breachfee = cutpaydetl.getPaybackamt().multiply(breachfeerate);
                        breachfee = breachfee.multiply(new BigDecimal(days)).setScale(2, BigDecimal.ROUND_HALF_UP);
                        cutpaymain.setOdbBreachfee(breachfee);

                        //设置逾期后总金额
                        cutpaymain.setOdbPaybackamt(latefee.add(breachfee));
                        //设置逾期金额的还款时间
                        cutpaymain.setOdbPaybackdate(OverduePayBackDate);
                    } else{   //未逾期 检查是否为最后一期帐单
                         contract = contractDao.findByPrimaryKey(cutpaydetl.getContractno());
                        if (contract.getDuration().equals(cutpaydetl.getPoano())) {
                             terminalcd = 1;
                        }
                    }
                    mainDao.update(mainPk, cutpaymain);
                }
            } else if(cutpaydetl.getBilltype().equals("1")) {    //明细帐单为逾期帐单
                if (cutpaymain.getOdbClosedcd().equals("0")) {
                    cutpaymain.setOdbClosedcd("1");
                    cutpaymain.setOdbCloseddate(date);
                    mainDao.update(mainPk, cutpaymain);
                }
                contract = contractDao.findByPrimaryKey(cutpaydetl.getContractno());
               if (contract.getDuration().equals(cutpaydetl.getPoano())) {
                    terminalcd = 1;
               }
            } else if(cutpaydetl.getBilltype().equals("2")) {    //明细帐单为提前还款帐单
                String  sql = "contractno = '" +cutpaydetl.getContractno() + "' and  precutpaycd = '1'";
                Xfactcutpaymain[] mains =mainDao.findByDynamicWhere(sql ,null);
                if (mains.length == 0) {
                    throw new Exception("查找明细帐单对应的主帐单时出现错误");
                }

                for (Xfactcutpaymain main : mains){
                    main.setClosedcd("1");
                    main.setCloseddate(date);
                    XfactcutpaymainPk pk = new XfactcutpaymainPk();
                    pk.setContractno(main.getContractno());
                    pk.setPoano(main.getPoano());
                    mainDao.update(pk, main);
                }
                //修改合同表信息 :结清
                contract = contractDao.findByPrimaryKey(cutpaydetl.getContractno());
                contract.setTerminalcd("1");   //正常终止
                contract.setTerminalreason("1");     //结清原因为：提前还款
                contract.setTerminaldate(date);
                contract.setCstatus(XFContractStatus.HETONG_OVER_NORMAL); //合同正常终止
                contractPk.setContractno(cutpaydetl.getContractno());
                contractDao.update(contractPk, contract);
            }

            //正常还款下结清（包括逾期帐单结清）
            if (terminalcd == 1) {
                contract = contractDao.findByPrimaryKey(cutpaydetl.getContractno());
                contract.setTerminalcd("1");   //正常终止
                contract.setTerminalreason("0");     //结清原因为：正常结清
                contract.setTerminaldate(date);
                contract.setCstatus(XFContractStatus.HETONG_OVER_NORMAL); //合同正常终止
                contractPk.setContractno(cutpaydetl.getContractno());
                contractDao.update(contractPk, contract);
            }

            conn.commit();
        } catch (Exception e) {
            Debug.debug(e);
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
                Debug.debug(ex);
            }
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                Debug.debug(ex);
            }
        }
    }


    

    /*
       批量进行扣款失败后的处理，设置入帐时间 、帐单状态
     */

    public void setCutpayDetlPaidupFailBatch(Xfactcutpaydetl[] xfactcutpaydetls) throws XfactcutpaydetlDaoException {

        try {
            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();
            Xfactcutpaydetl xfactcutpaydetl = null;

            String journalno = null;
            SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");

            for (int i = 0; i < xfactcutpaydetls.length; i++) {
                journalno = xfactcutpaydetls[i].getJournalno();
                xfactcutpaydetl = detlDao.findByPrimaryKey(journalno);
                XfactcutpaydetlPk detlPk = new XfactcutpaydetlPk(journalno);

                //xfactcutpaydetl.setPaidupamt(xfactcutpaydetl.getPaybackamt());

                xfactcutpaydetl.setPaidupdate(sFmt.parse(chargeoffdate));
                xfactcutpaydetl.setBillstatus(XFBillStatus.BILLSTATUS_CUTPAY_FAILED);

                detlDao.update(detlPk, xfactcutpaydetl);

                //TODO: 判断逾期（节假日情况，增加节日表）

            }
        } catch (Exception e) {
            Debug.debug(e);
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }

    }

    /*
       进行扣款失败后的处理，设置入帐时间 、帐单状态
       */

    public void setCutpayDetlPaidupFail(String journalno) throws XfactcutpaydetlDaoException {

        try {
            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();
            Xfactcutpaydetl xfactcutpaydetl = null;

            SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");

            xfactcutpaydetl = detlDao.findByPrimaryKey(journalno);
            XfactcutpaydetlPk detlPk = new XfactcutpaydetlPk(journalno);

            xfactcutpaydetl.setPaidupdate(sFmt.parse(chargeoffdate));
            xfactcutpaydetl.setBillstatus(XFBillStatus.BILLSTATUS_CUTPAY_FAILED);

            detlDao.update(detlPk, xfactcutpaydetl);

        } catch (Exception e) {
            Debug.debug(e);
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }

    }

    public void setCutpayDetlPaidupFail(String journalno, String failureReason) throws XfactcutpaydetlDaoException {

        try {
            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();
            Xfactcutpaydetl xfactcutpaydetl = null;

            SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");

            xfactcutpaydetl = detlDao.findByPrimaryKey(journalno);
            XfactcutpaydetlPk detlPk = new XfactcutpaydetlPk(journalno);

            xfactcutpaydetl.setPaidupdate(sFmt.parse(chargeoffdate));
            xfactcutpaydetl.setBillstatus(XFBillStatus.BILLSTATUS_CUTPAY_FAILED);
            xfactcutpaydetl.setFailurereason(failureReason);

            detlDao.update(detlPk, xfactcutpaydetl);

        } catch (Exception e) {
            Debug.debug(e);
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }

    }


}
