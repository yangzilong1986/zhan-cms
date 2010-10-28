package zt.cms.xf.fd.account;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import zt.cms.xf.common.constant.FDBillStatus;
import zt.cms.xf.common.dao.FdcutpaydetlDao;
import zt.cms.xf.common.dto.Fdcutpaydetl;
import zt.cms.xf.common.dto.FdcutpaydetlPk;
import zt.cms.xf.common.factory.FdcutpaydetlDaoFactory;
import zt.cms.xf.newcms.controllers.T100102CTL;
import zt.cms.xf.newcms.controllers.T100104CTL;
import zt.cms.xf.newcms.domain.T100102.T100102RequestList;
import zt.cms.xf.newcms.domain.T100102.T100102RequestRecord;
import zt.cms.xf.newcms.domain.T100104.T100104RequestList;
import zt.cms.xf.newcms.domain.T100104.T100104RequestRecord;
import zt.cmsi.mydb.MyDB;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.utils.Debug;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据SBS入帐成功的扣款记录，通过DBLINK，对房贷系统的数据库进行回写
 * 回写分为两种情况：
 * 1、正常还款
 * 2、提前还款
 * User: zhanrui
 * Date: 2009-7-16
 * Time: 15:37:05
 * To change this template use File | Settings | File Templates.
 */
public class FDWriteBackManager {

//    public static Logger logger = Logger.getLogger("zt.cms.xf.fd.account.FDWriteBackManager");
    private static Log logger = LogFactory.getLog(FDWriteBackManager.class);

    /*
    20101020 单笔处理
    查询房贷系统的扣款记录表，对SBS入帐成功的记录进行回写（to 新信贷）
    返回成功处理笔数
     */

    public int processWriteBack4NewCMS_SingleRecord(Fdcutpaydetl[] cutpaydetls, DatabaseConnection conn, ErrorMessages msgs) throws Exception {

        int count = 0;

        try {
            if (cutpaydetls != null && cutpaydetls.length > 0) {
                T100102CTL t100102ctl = new T100102CTL();
                T100104CTL t100104ctl = new T100104CTL();
                FdcutpaydetlDao detlDao = FdcutpaydetlDaoFactory.create();
                FdcutpaydetlPk cutpaydetlPk = new FdcutpaydetlPk();
                for (Fdcutpaydetl detl : cutpaydetls) {
                    if (!detl.getBillstatus().equals(FDBillStatus.SBS_ACCOUNT_SUCCESS)) {
                        logger.error("状态检查失败");
                        continue;
                    }
                    boolean txResult = false;
                    if (detl.getPreflag().equals("0")) { //正常还款
                        T100102RequestRecord recordT102 = new T100102RequestRecord();
                        recordT102.setStdjjh(detl.getGthtjhHtnm());
                        recordT102.setStdqch(detl.getGthtjhJhxh());
                        recordT102.setStdjhkkr(detl.getGthtjhDate());
                        //1-成功 2-失败
                        recordT102.setStdkkjg("1");
                        T100102RequestList t100102list = new T100102RequestList();
                        t100102list.add(recordT102);
                        //单笔发送处理
                        txResult = t100102ctl.start(t100102list);
                    }else{
                        T100104RequestRecord recordT104 = new T100104RequestRecord();
                        recordT104.setStdjjh(detl.getGthtjhHtnm());
                        recordT104.setStdqch(detl.getGthtjhJhxh());
                        recordT104.setStdjhkkr(detl.getGthtjhDate());
                        //1-成功 2-失败
                        recordT104.setStdkkjg("1");
                        T100104RequestList t100104list = new T100104RequestList();
                        t100104list.add(recordT104);
                        //单笔发送处理
                        txResult = t100104ctl.start(t100104list);
                    }
                    
                    if (txResult) {
                        cutpaydetlPk.setSeqno(detl.getSeqno());
                        detl.setBillstatus(FDBillStatus.FD_WRITEBACK_SUCCESS);
                        detlDao.update(cutpaydetlPk, detl);
                        count++;
                    }
                }
            } else {
                logger.info("无符合SBS入帐条件的明细帐单记录");
            }
        } catch (Exception e) {
            logger.error(e);
            msgs.add("回写新信贷系统时出现错误。成功处理笔数:"+count);
            return -1;
        }
        return count;
    }


    /*
    20101020   批处理模式 废弃
    查询房贷系统的扣款记录表，对SBS入帐成功的记录进行回写（to 新信贷）
    返回成功处理笔数
     */

    public int processWriteBack4NewCMS(String txndate, DatabaseConnection conn, ErrorMessages msgs) throws Exception {
        int rtn = 0;

/*

        try {
            String sql = "billstatus = " + FDBillStatus.SBS_ACCOUNT_SUCCESS;

            FdcutpaydetlDao detlDao = FdcutpaydetlDaoFactory.create();
            Fdcutpaydetl[] cutpaydetls = detlDao.findByDynamicWhere(sql, null);
            FdcutpaydetlPk cutpaydetlpk = new FdcutpaydetlPk();

            int count = 0;
            if (cutpaydetls != null && cutpaydetls.length > 0) {

                T100102RequestList t100012list = new T100102RequestList();
                for (Fdcutpaydetl detl : cutpaydetls) {
                    T100102RequestRecord recordT102 = new T100102RequestRecord();
                    recordT102.setStdjjh(detl.getGthtjhHtnm());
                    recordT102.setStdqch(detl.getGthtjhJhxh());
                    recordT102.setStdjhkkr(detl.getGthtjhDate());
                    //1-成功 2-失败
                    recordT102.setStdkkjg("1");
                    t100012list.add(recordT102);
                    count++;
                }
                //发送数据
                T100102CTL t100102ctl = new T100102CTL();
                boolean txresult = false;
                try {
                    txresult = t100102ctl.start(t100012list);
                } catch (Exception e) {
                    logger.error("发送失败。");
                    String mess = "向新信贷系统提交报文失败。";
                    msgs.add(mess);
                    return -1;
                }

                if (txresult) {
                    //发送完成，返回业务成功标志 更新本地记录状态
                    conn.setAuto(false);
                    String detlSql = "update fdcutpaydetl set billstatus = " +
                            FDBillStatus.FD_WRITEBACK_SUCCESS +
                            " where billstatus = " + FDBillStatus.SBS_ACCOUNT_SUCCESS;
                    int updatertn = conn.executeUpdate(detlSql);
                    if (updatertn != count) {
                        String mess = "修改本地系统中FDCUTPAYDETL表的状态时出现错误(笔数不符)。" + "应为：" + count + " 实际： " + updatertn;
                        msgs.add(mess);
                        conn.rollback();
                        return -1;
                    }
                    conn.commit();
                }
            } else {
                logger.info("无符合SBS入帐条件的明细帐单记录");
                rtn = 0;
            }
        } catch (Exception e) {
            logger.error(e);
            conn.rollback();
            msgs.add("回写新信贷系统时出现错误。");
            return -1;
        }
*/
        return rtn;
    }

    /**
     * 批处理模式 分包  废弃
     * @param txndate
     * @param conn
     * @param msgs
     * @param numperkdg
     * @return
     * @throws Exception
     */

    public int processWriteBack4NewCMS_fenbao(String txndate, DatabaseConnection conn, ErrorMessages msgs, int numperkdg) throws Exception {

        int rtn = 0;

        try {
            String sql = "billstatus = " + FDBillStatus.SBS_ACCOUNT_SUCCESS;

            FdcutpaydetlDao detlDao = FdcutpaydetlDaoFactory.create();
            Fdcutpaydetl[] cutpaydetls = detlDao.findByDynamicWhere(sql, null);
            FdcutpaydetlPk cutpaydetlpk = new FdcutpaydetlPk();

            int count = 0;
            if (cutpaydetls != null && cutpaydetls.length > 0) {

                T100102RequestList t100012list = new T100102RequestList();
                List<String> seqnoList = new ArrayList();
                //int totalnum = cutpaydetls.length;

                String updateLocalStatus = "update fdcutpaydetl set billstatus = " +
                        FDBillStatus.FD_WRITEBACK_SUCCESS +
                        " where seqno = ? ";
                PreparedStatement ps = conn.getPreparedStatement(updateLocalStatus);

                for (Fdcutpaydetl detl : cutpaydetls) {
                    T100102RequestRecord recordT102 = new T100102RequestRecord();
                    recordT102.setStdjjh(detl.getGthtjhHtnm());
                    recordT102.setStdqch(detl.getGthtjhJhxh());
                    recordT102.setStdjhkkr(detl.getGthtjhDate());
                    //1-成功 2-失败
                    recordT102.setStdkkjg("1");
                    t100012list.add(recordT102);
                    seqnoList.add(detl.getSeqno());
                    count++;

                    if (count % numperkdg == 0) {
                        //发送数据
                        if (sendDataToNewCMS(t100012list, msgs)) { //服务器返回成功
                            //逐笔更新本地table状态
                            for (String seqno : seqnoList) {
                                ps.setString(1, seqno);
                                ps.addBatch();
                            }
                            ps.executeBatch();

                            //初始化发送包list
                            t100012list = new T100102RequestList();
                            seqnoList = new ArrayList();
                        } else {
                            logger.error("发送失败。");
                            String mess = "向新信贷系统提交报文失败。";
                            msgs.add(mess);
                            return -1;
                        }
                    }
                }
                //处理取模后余下的数据


                conn.commit();

            } else {
                logger.info("无符合SBS入帐条件的明细帐单记录");
                rtn = 0;
            }
        } catch (Exception e) {
            logger.error(e);
            conn.rollback();
            msgs.add("回写新信贷系统时出现错误。");
            return -1;
        }
        return rtn;
    }

    private boolean sendDataToNewCMS(T100102RequestList t100012list, ErrorMessages msgs) {
        //发送数据
        T100102CTL t100102ctl = new T100102CTL();
        boolean txresult = false;
        try {
            txresult = t100102ctl.start(t100012list);
            return txresult;
        } catch (Exception e) {
            logger.error("发送失败。");
            String mess = "向新信贷系统提交报文失败。";
            msgs.add(mess);
            return false;
        }

    }


    /*
   查询房贷系统的扣款记录表，对代扣成功的记录逐笔进行SBS入帐处理
   返回成功处理笔数
    */

    public int processWriteBack(String txndate, DatabaseConnection conn, ErrorMessages msgs) throws Exception {

        int rtn = 0;

        try {
            String sql = "billstatus = " + FDBillStatus.SBS_ACCOUNT_SUCCESS;

            FdcutpaydetlDao detlDao = FdcutpaydetlDaoFactory.create();
            Fdcutpaydetl[] cutpaydetls = detlDao.findByDynamicWhere(sql, null);
            FdcutpaydetlPk cutpaydetlpk = new FdcutpaydetlPk();


            long seqno = get_XDFHKX_Seqno(conn, msgs);
            if (seqno == -1) {
                return -1;
            }
            long hkpch = get_XDFHKX_HKPCH(conn, msgs);
            if (hkpch == -1) {
                return -1;
            }
            long cpnm = get_XDCPXX_CPNM(conn, msgs);
            if (hkpch == -1) {
                return -1;
            }


            if (cutpaydetls != null && cutpaydetls.length > 0) {

                conn.setAuto(false);

                String locksql = "lock table lsnbbm@haier_shengchan in  exclusive mode";
                int result = conn.executeUpdate(locksql);
                if (result < 0) {
                    msgs.add("对数据库表LSNBBM进行加锁时出现错误。");
                    rtn = -1;
                    throw new Exception();
                }

                RecordSet rs = null;

                String psSql = "INSERT INTO   XDFHKX@haier_shengchan    " +
                        "  (  XDFHKX_ID  ,  " +
                        "     XDFHKX_YWZL  ,  " +
                        "     XDFHKX_HTBH  ,  " +
                        "     XDFHKX_HTNM  ,  " +
                        "     XDFHKX_YWRQ  ,  " +
                        "     XDFHKX_JE  ,  " +
                        "     XDFHKX_LL  ,  " +
                        "     XDFHKX_ZDR  ,  " +
                        "     XDFHKX_FHR  ,  " +
                        "     XDFHKX_FKID  ,  " +
                        "     XDFHKX_DQRQ  ,  " +
                        "     XDFHKX_HKPCH  ,  " +
                        "     XDFHKX_SJRQ  ,  " +
                        "     XDFHKX_FKSPH  ,  " +
                        "     XDFHKX_BJJE  ,  " +
                        "     XDFHKX_LXJE  ,  " +
                        "     XDFHKX_JHXH  ,  " +
                        "     XDFHKX_DYJXDH  ,  " +
                        "     XDFHKX_DSSJ  )  " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement ps = conn.getPreparedStatement(psSql);

                String psCpxxSql = "insert into xdcpxx@haier_shengchan  values (?,?,?,?,?,?,?,?,?,?,?,?) ";
                PreparedStatement pscpxx = conn.getPreparedStatement(psCpxxSql);

                for (int i = 0; i < cutpaydetls.length; i++) {

                    if (cutpaydetls[i].getPreflag().equals("0")) {//正常还款
//                        rtn = processOneCutpayDetlRecord(cutpaydetls[i], conn, msgs);
//                        if (rtn == -1) {
//                            //TODO:
//                            return -1;
//                        }
//                        seqno++;
//                        hkpch++;

                        //得到FKID     业务日期     计划扣款金额
                        sql = "select gthtjh_fkid,gthtjh_date, gthtjh_jhje from  gthtjh@haier_shengchan where gthtjh_htnm="
                                + cutpaydetls[i].getGthtjhHtnm()
                                + " and gthtjh_jhxh = "
                                + cutpaydetls[i].getGthtjhJhxh();
                        //System.out.println(sql);
                        rs = conn.executeQuery(sql);

                        String fkid = null;
                        String gthtjh_date = null;
                        Double gthtjh_jhje = null;
                        if (rs.next()) {
                            fkid = rs.getString(0);
                            gthtjh_date = rs.getString(1);
                            gthtjh_jhje = rs.getDouble(2);
                        } else {
                            String mess = "获取" + cutpaydetls[i].getGthtjhHtbh() + "对应的FKID时出现错误。";
                            msgs.add(mess);
                            rtn = -1;
                            throw new Exception();
                        }

                        sql = "update gthtjh@haier_shengchan set  gthtjh_hkbz = 1, gthtjh_hkje = "
                                + gthtjh_jhje +
                                " where gthtjh_htnm="
                                + cutpaydetls[i].getGthtjhHtnm()
                                + " and gthtjh_jhxh = "
                                + cutpaydetls[i].getGthtjhJhxh();
                        //System.out.println(sql);

                        int count = conn.executeUpdate(sql);
                        if (count != 1) {
                            String mess = "修改房贷系统中GTHTJH表" + cutpaydetls[i].getGthtjhHtbh() + "对应的还款标志时出现错误。";
                            msgs.add(mess);
                            rtn = -1;
                            throw new Exception();
                        }

                        int index = 1;
                        ps.setString(index, new Long(seqno).toString());    //TODO:长度？    XDFHKX_ID:1 = '10191'
                        index++;
                        ps.setString(index, "8");                                           //XDFHKX_YWZL:2 = '8'
                        index++;
                        ps.setString(index, cutpaydetls[i].getGthtjhHtbh());     //XDFHKX_HTBH:3 = 'GZ200604O1'
                        index++;
                        ps.setString(index, cutpaydetls[i].getGthtjhHtnm());      //XDFHKX_HTNM:4 = '1182'
                        index++;
                        ps.setString(index, txndate);                                      //XDFHKX_YWRQ:5 = '20090714'
                        index++;
                        ps.setFloat(index, cutpaydetls[i].getGthtjhJhje().floatValue());    //XDFHKX_JE:6 = '10000'
                        index++;
                        ps.setFloat(index, cutpaydetls[i].getGthtjhLl());                    //XDFHKX_LL:7 = '3.47'
                        index++;
                        ps.setString(index, "AUTO");                        //XDFHKX_ZDR:8 = '姜银锡'
                        index++;
                        ps.setString(index, "AUTO");                           //XDFHKX_FHR:9 = <NULL>
                        index++;
                        ps.setString(index, fkid);                                   //XDFHKX_FKID:10 = '454'
                        index++;
                        ps.setString(index, gthtjh_date);                                   //XDFHKX_DQRQ:11 = <NULL>
                        index++;
                        ps.setString(index, StringUtils.leftPad(new Long(hkpch).toString(), 9, '0'));    //XDFHKX_HKPCH:12 = '000010039'
                        index++;
                        ps.setString(index, " ");                                      //XDFHKX_SJRQ:13 = ''
                        index++;
                        ps.setString(index, null);                                   //XDFHKX_FKSPH:14 = <NULL>
                        index++;
                        ps.setFloat(index, cutpaydetls[i].getGthtjhBjje().floatValue());    //XDFHKX_BJJE:15 = '10000'
                        index++;
                        ps.setFloat(index, cutpaydetls[i].getGthtjhLxje().floatValue());      //XDFHKX_LXJE:16 = <NULL>
                        index++;
                        ps.setString(index, cutpaydetls[i].getGthtjhJhxh());     //XDFHKX_JHXH:17 = <NULL>
                        index++;
                        ps.setString(index, " ");           //XDFHKX_DYJXDH:18 = ''
                        index++;
                        ps.setString(index, null);          //XDFHKX_DSSJ:19 = <NULL>
                        index++;

                        ps.executeUpdate();
                        //TODO:
                        seqno++;
                        hkpch++;


                        //增加本金传票信息
                        index = 1;
                        pscpxx.setString(index, new Long(cpnm).toString());
                        index++;
                        pscpxx.setString(index, cutpaydetls[i].getXdkhzdKhbh());
                        index++;
                        pscpxx.setString(index, cutpaydetls[i].getGthtjhHtbh());
                        index++;
                        pscpxx.setString(index, " ");
                        index++;
                        pscpxx.setString(index, "03");
                        index++;
                        pscpxx.setString(index, "ZXFB8");
                        index++;
                        pscpxx.setString(index, txndate);
                        index++;
                        pscpxx.setString(index, "AUTO");
                        index++;
                        pscpxx.setString(index, "801090106001041001");
                        index++;
                        pscpxx.setString(index, cutpaydetls[i].getGthtbZhbh());
                        index++;
                        pscpxx.setFloat(index, cutpaydetls[i].getGthtjhBjje().floatValue());        //本金金额
                        index++;
                        pscpxx.setString(index, "1");
                        index++;

                        pscpxx.executeUpdate();
                        cpnm++;

                        //增加利息传票信息
                        index = 1;
                        pscpxx.setString(index, new Long(cpnm).toString());
                        index++;
                        pscpxx.setString(index, cutpaydetls[i].getXdkhzdKhbh());
                        index++;
                        pscpxx.setString(index, cutpaydetls[i].getGthtjhHtbh());
                        index++;
                        pscpxx.setString(index, " ");
                        index++;
                        pscpxx.setString(index, "03");
                        index++;
                        pscpxx.setString(index, "ZXFXZXFB8");
                        index++;
                        pscpxx.setString(index, txndate);
                        index++;
                        pscpxx.setString(index, "AUTO");
                        index++;
                        pscpxx.setString(index, "801090106001041001");
                        index++;
                        pscpxx.setString(index, "801090106005014001");
                        index++;
                        pscpxx.setFloat(index, cutpaydetls[i].getGthtjhLxje().floatValue());       //利息金额
                        index++;
                        pscpxx.setString(index, "1");
                        index++;

                        pscpxx.executeUpdate();
                        cpnm++;

                        rtn++;
                        //修改本地扣款记录状态
//                        cutpaydetls[i].setBillstatus(FDBillStatus.FD_WRITEBACK_SUCCESS);
//                        cutpaydetlpk.setSeqno(cutpaydetls[i].getSeqno());
//                        detlDao.update(cutpaydetlpk, cutpaydetls[i]);

                        String detlSql = "update fdcutpaydetl set billstatus = " +
                                FDBillStatus.FD_WRITEBACK_SUCCESS +
                                " where seqno = " + cutpaydetls[i].getSeqno();
                        count = conn.executeUpdate(detlSql);
                        if (count != 1) {
                            String mess = "修改本地系统中FDCUTPAYDETL表" + cutpaydetls[i].getGthtjhHtbh() + "的状态时出现错误。";
                            msgs.add(mess);
                            rtn = -1;
                            throw new Exception();
                        }

                    } else if (cutpaydetls[i].getPreflag().equals("1")) { //提前还款

                        //修改XDFHKX表中业务种类为'6' '7' 的记录的复核人为‘AUTO'
                        sql = "update xdfhkx@haier_shengchan set  xdfhkx_fhr = 'AUTO' "
                                + " where xdfhkx_htnm='"
                                + cutpaydetls[i].getGthtjhHtnm()
                                + "' and (xdfhkx_ywzl = '6' or xdfhkx_ywzl = '7') and xdfhkx_ywrq = '"
                                + cutpaydetls[i].getGthtjhDate() + "'";
//                                + txndate +"'";

//                        System.out.println("UPDATE XDFHKX sql=" + sql);
                        logger.info("UPDATE XDFHKX sql=" + sql);
                        int count = conn.executeUpdate(sql);


                        if (count <= 0) {
                            String mess = "修改房贷系统中XDFHKX表" + cutpaydetls[i].getGthtjhHtbh() + "对应的复核人员时出现错误。";
                            logger.error(mess);
                            logger.error("update return=" + count);
                            msgs.add(mess);
                            rtn = -1;
                            throw new Exception();
                        }


                        //20090813 zhanrui
                        //增加本金传票信息
                        int index = 1;
                        pscpxx.setString(index, new Long(cpnm).toString());
                        index++;
                        pscpxx.setString(index, cutpaydetls[i].getXdkhzdKhbh());
                        index++;
                        pscpxx.setString(index, cutpaydetls[i].getGthtjhHtbh());
                        index++;
                        pscpxx.setString(index, " ");
                        index++;
                        pscpxx.setString(index, "03");
                        index++;
                        pscpxx.setString(index, "Z6");               //提前还款本金
                        index++;
                        pscpxx.setString(index, txndate);
                        index++;
                        pscpxx.setString(index, "AUTO");
                        index++;
                        pscpxx.setString(index, "801090106001041001");
                        index++;
                        pscpxx.setString(index, cutpaydetls[i].getGthtbZhbh());
                        index++;
                        pscpxx.setFloat(index, cutpaydetls[i].getGthtjhBjje().floatValue());        //本金金额
                        index++;
                        pscpxx.setString(index, "1");
                        index++;

                        pscpxx.executeUpdate();
                        cpnm++;

                        //增加利息传票信息
                        index = 1;
                        pscpxx.setString(index, new Long(cpnm).toString());
                        index++;
                        pscpxx.setString(index, cutpaydetls[i].getXdkhzdKhbh());
                        index++;
                        pscpxx.setString(index, cutpaydetls[i].getGthtjhHtbh());
                        index++;
                        pscpxx.setString(index, " ");
                        index++;
                        pscpxx.setString(index, "03");
                        index++;
                        pscpxx.setString(index, "Z7");                    //提前还款利息
                        index++;
                        pscpxx.setString(index, txndate);
                        index++;
                        pscpxx.setString(index, "AUTO");
                        index++;
                        pscpxx.setString(index, "801090106001041001");
                        index++;
                        pscpxx.setString(index, "801090106005014001");
                        index++;
                        pscpxx.setFloat(index, cutpaydetls[i].getGthtjhLxje().floatValue());       //利息金额
                        index++;
                        pscpxx.setString(index, "1");
                        index++;

                        pscpxx.executeUpdate();
                        cpnm++;

                        rtn++;

                        //修改本地扣款记录状态
                        String detlSql = "update fdcutpaydetl set billstatus = " +
                                FDBillStatus.FD_WRITEBACK_SUCCESS +
                                " where seqno = " + cutpaydetls[i].getSeqno();
                        count = conn.executeUpdate(detlSql);
                        if (count != 1) {
                            String mess = "修改本地系统中FDCUTPAYDETL表" + cutpaydetls[i].getGthtjhHtbh() + "的状态时出现错误。";
                            msgs.add(mess);
                            rtn = -1;
                            throw new Exception();
                        }

                    } else { //异常情况

                    }

                }
                seqno--;
                hkpch--;
                cpnm--;
                if (set_XDFHKX_Seqno(seqno, conn, msgs) == -1) {
                    rtn = -1;
                    throw new Exception();
                }
                if (set_XDFHKX_HKPCH(hkpch, conn, msgs) == -1) {
                    rtn = -1;
                    throw new Exception();
                }
                if (set_XDCPXX_CPNM(cpnm, conn, msgs) == -1) {
                    rtn = -1;
                    throw new Exception();
                }
            } else {
                logger.info("无符合SBS入帐条件的明细帐单记录");
                rtn = 0;
            }
        } catch (Exception e) {
            Debug.debug(e);
            conn.rollback();
            //throw new Exception(e);
            //msgs.add("回写房贷系统时出现错误。");
            return -1;
        }
        conn.commit();
        return rtn;
    }

    /*
    20090723
    对入错的房贷系统传票信息进行修改
     */

    public int processWriteBack_temp(String txndate, DatabaseConnection conn, ErrorMessages msgs) throws Exception {

        int rtn = 0;

        try {
            String sql = " billstatus = 8  and  preflag = 0";

            FdcutpaydetlDao detlDao = FdcutpaydetlDaoFactory.create();
            Fdcutpaydetl[] cutpaydetls = detlDao.findByDynamicWhere(sql, null);
            FdcutpaydetlPk cutpaydetlpk = new FdcutpaydetlPk();


            if (cutpaydetls != null && cutpaydetls.length > 0) {

                conn.setAuto(false);

                String psCpxxSql = "update  xdcpxx@haier_shengchan  set  xdcpxx_cpje = ? where XDCPXX_HTBH = ? and XDCPXX_YWZL = ? and  XDCPXX_YWRQ = '20090722'  and XDCPXX_CZRY = 'AUTO' ";
                PreparedStatement pscpxx = conn.getPreparedStatement(psCpxxSql);

                for (int i = 0; i < cutpaydetls.length; i++) {

                    if (cutpaydetls[i].getPreflag().equals("0")) {//正常还款
                        int index = 1;
                        pscpxx.setFloat(index, cutpaydetls[i].getGthtjhBjje().floatValue());
                        index++;
                        pscpxx.setString(index, cutpaydetls[i].getGthtjhHtbh());
                        index++;
                        pscpxx.setString(index, "ZXFB8");
                        int result = pscpxx.executeUpdate();
                        if (result == 1) {
                            rtn++;
                        }

                        index = 1;
                        pscpxx.setFloat(index, cutpaydetls[i].getGthtjhLxje().floatValue());
                        index++;
                        pscpxx.setString(index, cutpaydetls[i].getGthtjhHtbh());
                        index++;
                        pscpxx.setString(index, "ZXFXZXFB8");
                        result = pscpxx.executeUpdate();
                        if (result == 1) {
                            rtn++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Debug.debug(e);
            conn.rollback();
            //throw new Exception(e);
            //msgs.add("回写房贷系统时出现错误。");
            return -1;
        }
        conn.commit();
        return rtn;
    }


    private long get_XDFHKX_Seqno(DatabaseConnection conn, ErrorMessages msgs) {

        try {
            MyDB.getInstance().addDBConn(conn);

            String sql = "select lsnbbm_dqnm from lsnbbm@haier_shengchan where lsnbbm_nmbh = 'XDFHKX' for update";
            System.out.println(sql);

            RecordSet rs = conn.executeQuery(sql);


            long seqno = 0;
            if (rs.next()) {
                seqno = rs.getLong(0);
            } else {
                String msg = "读取房贷系统序号（lsnbbm_nmbh = 'XDFHKX'）时出错，请通知系统管理员。";
                msgs.add(msg);
                logger.error(msg);
                logger.error(sql);
                return -1;
            }
            return ++seqno;
        } catch (Exception e) {
            String msg = "读取房贷系统序号（lsnbbm_nmbh = 'XDFHKX'）时出现异常，请通知系统管理员。";
            msgs.add(msg);
            logger.error(msg);
            logger.error(e);
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }

    private long set_XDFHKX_Seqno(Long seqno, DatabaseConnection conn, ErrorMessages msgs) {

        try {
            MyDB.getInstance().addDBConn(conn);

            String sql = "update  lsnbbm@haier_shengchan set lsnbbm_dqnm = " + new Long(seqno).toString()
                    + " where lsnbbm_nmbh = 'XDFHKX' ";
            System.out.println(sql);

            int result = conn.executeUpdate(sql);

            if (result != 1) {
                msgs.add("修改房贷系统序号（where lsnbbm_nmbh = 'XDFHKX'）时出错！");
                return -1;
            }
        } catch (Exception e) {
            msgs.add("修改房贷系统序号（where lsnbbm_nmbh = 'XDFHKX'）时出错！");
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
        return 0;
    }


    /*
    还款自动拆分批次号
     */

    private long get_XDFHKX_HKPCH(DatabaseConnection conn, ErrorMessages msgs) {

        try {
            MyDB.getInstance().addDBConn(conn);

            String sql = "select lsnbbm_dqnm from lsnbbm@haier_shengchan where lsnbbm_nmbh = 'XDHKZDCF'  for update";
            RecordSet rs = conn.executeQuery(sql);

            long hkpch = 0;
            if (rs.next()) {
                hkpch = rs.getLong(0);
            } else {
                msgs.add("读取房贷系统序号（where lsnbbm_nmbh = 'XDHKZDCF'）时出错！");
                return -1;
            }

            return ++hkpch;
        } catch (Exception e) {
            msgs.add("读取房贷系统序号（where lsnbbm_nmbh = 'XDHKZDCF'）时出错！");
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }

    private long set_XDFHKX_HKPCH(Long hkpch, DatabaseConnection conn, ErrorMessages msgs) {

        try {
            MyDB.getInstance().addDBConn(conn);

            String sql = "update  lsnbbm@haier_shengchan set lsnbbm_dqnm = " + new Long(hkpch).toString()
                    + " where lsnbbm_nmbh = 'XDHKZDCF'";
            System.out.println(sql);

            int result = conn.executeUpdate(sql);

            if (result != 1) {
                msgs.add("修改房贷系统序号（where lsnbbm_nmbh = 'XDHKZDCF'）时出错！");
                return -1;
            }
        } catch (Exception e) {
            msgs.add("修改房贷系统序号（where lsnbbm_nmbh = 'XDHKZDCF'）时出错！");
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
        return 0;
    }

    private long get_XDCPXX_CPNM(DatabaseConnection conn, ErrorMessages msgs) {

        try {
            MyDB.getInstance().addDBConn(conn);

            String sql = "select lsnbbm_dqnm from lsnbbm@haier_shengchan where lsnbbm_nmbh = 'XDCPXXNM'  for update";
            System.out.println(sql);

            RecordSet rs = conn.executeQuery(sql);


            long cpnm = 0;
            if (rs.next()) {
                cpnm = rs.getLong(0);
            } else {
                msgs.add("读取房贷系统序号（where lsnbbm_nmbh = 'XDCPXXNM'）时出错！");
                return -1;
            }
            return ++cpnm;
        } catch (Exception e) {
            msgs.add("读取房贷系统序号（where lsnbbm_nmbh = 'XDCPXXNM'）时出错！");
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }

    private long set_XDCPXX_CPNM(Long cpnm, DatabaseConnection conn, ErrorMessages msgs) {

        try {
            MyDB.getInstance().addDBConn(conn);

            String sql = "update  lsnbbm@haier_shengchan set lsnbbm_dqnm = '"
                    + StringUtils.leftPad(new Long(cpnm).toString(), 9, "0")
                    + "' where lsnbbm_nmbh = 'XDCPXXNM'";
            System.out.println(sql);

            int result = conn.executeUpdate(sql);

            if (result != 1) {
                msgs.add("修改房贷系统序号（where lsnbbm_nmbh = 'XDCPXXNM'）时出错！");
                return -1;
            }
        } catch (Exception e) {
            msgs.add("修改房贷系统序号（where lsnbbm_nmbh = 'XDCPXXNM'）时出错！");
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
        return 0;
    }

    /*
   正常还款的记录
    */

    private int processOneCutpayDetlRecord(Fdcutpaydetl cutpaydetl, DatabaseConnection conn, ErrorMessages msgs) {

        try {
            MyDB.getInstance().addDBConn(conn);

            String sql = "select gthtjh_jhxh, gthtjh_fkid from gthtjh@haier_shengchan  where gthtjh_htnm = " + cutpaydetl.getGthtjhHtnm();
            RecordSet rs = conn.executeQuery(sql);

            String jhxh = null;
            String fkid = null;
            if (rs.next()) {
                jhxh = rs.getString(1);
                fkid = rs.getString(2);

            }

        } catch (Exception e) {
            msgs.add("读取房贷系统序号时出错！");
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
        return 0;
    }

}