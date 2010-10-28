package zt.cms.xf.fd.account;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import zt.cms.xf.account.SBSManager;
import zt.cms.xf.common.constant.FDBillStatus;
import zt.cms.xf.common.constant.XFBankCode;
import zt.cms.xf.common.constant.XFBillStatus;
import zt.cms.xf.common.constant.XFWithHoldStatus;
import zt.cms.xf.common.dao.FdactnoinfoDao;
import zt.cms.xf.common.dao.FdcutpaydetlDao;
import zt.cms.xf.common.dao.XfactcutpaydetlDao;
import zt.cms.xf.common.dao.XfifbankdetlDao;
import zt.cms.xf.common.dto.*;
import zt.cms.xf.common.exceptions.FdcutpaydetlDaoException;
import zt.cms.xf.common.exceptions.XfactcutpaydetlDaoException;
import zt.cms.xf.common.factory.FdactnoinfoDaoFactory;
import zt.cms.xf.common.factory.FdcutpaydetlDaoFactory;
import zt.cms.xf.common.factory.XfactcutpaydetlDaoFactory;
import zt.cms.xf.common.factory.XfifbankdetlDaoFactory;
import zt.cms.xf.newcms.controllers.BaseCTL;
import zt.cms.xf.newcms.controllers.T100101CTL;
import zt.cms.xf.newcms.controllers.T100103CTL;
import zt.cms.xf.newcms.domain.T100101.T100101ResponseRecord;
import zt.cmsi.mydb.MyDB;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.SessionAttributes;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.event.EventType;
import zt.platform.utils.Debug;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-3-14
 * Time: 20:24:05
 * To change this template use File | Settings | File Templates.
 */
public class FDDateLink extends FormActions {

    //    public static Logger logger = Logger.getLogger("zt.cms.xf.fd.account.FDDateLink");
    private static Log logger = LogFactory.getLog(FDDateLink.class);

    private String inputdate;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs, EventManager manager, String parameter) {

        SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");
        inputdate = sFmt.format(new Date());


        instance.setValue("INPUTDATE", inputdate);
        instance.setValue("SYSTEMDATE", inputdate);

        instance.getFormBean().getElement("GETFDDATABUTTON").setComponetTp(6);
        instance.getFormBean().getElement("CHECKFDDATABUTTON").setComponetTp(6);
        instance.getFormBean().getElement("GETFDPREDATABUTTON").setComponetTp(6);
        instance.getFormBean().getElement("CHECKFDPREDATABUTTON").setComponetTp(6);
        instance.getFormBean().getElement("GENERATEPKGBUTTON").setComponetTp(6);
        instance.getFormBean().getElement("SENDPKGBUTTON").setComponetTp(6);
        instance.getFormBean().getElement("XFACCOUNTBUTTON").setComponetTp(6);
        instance.getFormBean().getElement("FDACCOUNTBUTTON").setComponetTp(6);
        instance.getFormBean().getElement("FDWRITEBACKBUTTON").setComponetTp(6);
        instance.getFormBean().getElement("REGION").setComponetTp(6);

        String lastbutton = (String) ctx.getAttribute("BUTTONNAME");
        if (lastbutton == null) {
            return -1;
        }
        String superformid = (String) ctx.getAttribute("SUPERFORMID");
        if (superformid == null) {
            return -1;
        }

        if (lastbutton.equals("GETCUTPAYBUTTON")) {
            instance.getFormBean().getElement("GETFDDATABUTTON").setComponetTp(15);
//            instance.getFormBean().getElement("CHECKFDDATABUTTON").setComponetTp(15);
            instance.getFormBean().getElement("CHECKFDDATABUTTON").setComponetTp(6);
        } else if (lastbutton.equals("GENERATEPKGBUTTON")) {
            instance.getFormBean().getElement("GENERATEPKGBUTTON").setComponetTp(15);
        } else if (lastbutton.equals("SENDPKGBUTTON")) {
            instance.getFormBean().getElement("SENDPKGBUTTON").setComponetTp(15);
        } else if (lastbutton.equals("GETPRECUTPAYBUTTON")) {
            instance.getFormBean().getElement("GETFDPREDATABUTTON").setComponetTp(15);
            instance.getFormBean().getElement("CHECKFDPREDATABUTTON").setComponetTp(15);
        } else if (lastbutton.equals("FDACCOUNTBUTTON")) {
            instance.getFormBean().getElement("FDACCOUNTBUTTON").setComponetTp(15);
        } else if (lastbutton.equals("XFACCOUNTBUTTON")) {
            instance.getFormBean().getElement("XFACCOUNTBUTTON").setComponetTp(15);
        } else if (lastbutton.equals("FDWRITEBACKBUTTON")) {
            instance.getFormBean().getElement("FDWRITEBACKBUTTON").setComponetTp(15);
        }

        if (superformid.equals("FDCUTPAYDETLLIST") ||
                superformid.equals("FDPRECUTPAYDETLLIST")) {
            if (!"GENERATEPKGBUTTON".equals(lastbutton)) {
                instance.getFormBean().getElement("REGION").setComponetTp(8);
            }
        }

        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_BREAK_TYPE);
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                           String button, ErrorMessages msgs, EventManager manager) {

        inputdate = ctx.getParameter("INPUTDATE");

        //建行直连代扣通讯记录生成
        if (button.equals("GENERATEPKGBUTTON")) {
            int iSuccessCount = 0;
            try {
                iSuccessCount = doGeneratePkgButton(ctx, conn, instance, msgs);
                if (iSuccessCount == 0) {
                    ctx.setRequestAtrribute("msg", "不存在符合处理条件的数据!");
                } else {
                    ctx.setRequestAtrribute("msg", "建行直连代扣记录生成完成，处理记录数为：" + iSuccessCount + "条。");
                }
            } catch (Exception e) {
                Debug.debug(e);
                ctx.setRequestAtrribute("msg", "建行直连代扣处理未成功完成，请查询！");
                return -1;
            } finally {
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
                ctx.removeAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
            }
        }

        //利用DBLINK 获取房贷系统数据  
        if (button.equals("GETFDDATABUTTON") || button.equals("GETFDPREDATABUTTON")) {

            String regioncd = ctx.getParameter("REGION");
            String bankcd = null;

            if (regioncd == null) {
                msgs.add("未选择地区，请选择待处理的地区名称。");
                return -1;
            }

            if (regioncd.equals("0532")) {
                bankcd = XFBankCode.BANKCODE_CCB;
            } else if (regioncd.equals("0531")) {
                bankcd = XFBankCode.BANKCODE_BOC;
            } else if (regioncd.equals("023")) {
                bankcd = XFBankCode.BANKCODE_BOC;
            } else if (regioncd.equals("0351")) {
                bankcd = XFBankCode.BANKCODE_BOC;
            }
            int iSuccessCount = 0;
            try {
                if (checkFDCUTPAYDETLData(ctx, conn, instance, msgs) == -1) {
                    ctx.setRequestAtrribute("msg", "系统中存在未处理完成的代扣数据，请查询！");
                    return -1;
                }

/*
                //检查银行帐号对应情况
                if (checkFDSystemData(conn, msgs, button,regioncd,bankcd) == -1) {
                    ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                    return -1;
                }
                msgs.clear();
                iSuccessCount = getFDSystemData(conn, msgs, button, regioncd, bankcd, "fdcutpaydetl");
*/
                /*
                1、并发前处理（数据库中设置标志，本模块不允许并发） TODO
                2、清空临时表
                3、通过接口获取全部代扣数据到临时表
                4、逐笔检查临时表中数据，根据合同号与本地帐户表一一对应
                5、并发后处理
                 */
                iSuccessCount = getNewFDSystemData(conn, msgs, button, regioncd, bankcd);
//                iSuccessCount = getFDSystemData(conn, msgs, button, regioncd, bankcd, "fdcutpaydetl");


                ctx.setRequestAtrribute("msg", msgs.getAllMessages());
            } catch (Exception e) {
                Debug.debug(e);
                ctx.setRequestAtrribute("msg", "获取房贷系统数据未成功完成，请查询！");
                return -1;
            } finally {
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            }
        }

        //利用DBLINK 检查房贷系统数据
        if (button.equals("CHECKFDDATABUTTON") || button.equals("CHECKFDPREDATABUTTON")) {
            ctx.setRequestAtrribute("flag", "1");
            ctx.setRequestAtrribute("isback", "0");
            ctx.setTarget("/faces/cutpay/t100101.xhtml");
            instance.setReadonly(true);

/*

            String regioncd = ctx.getParameter("REGION");
            String bankcd = null;

            if (regioncd == null) {
                msgs.add("未选择地区，请选择待处理的地区名称。");
                ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                return 0;
            }

            if (regioncd.equals("0532")) {
                bankcd = XFBankCode.BANKCODE_CCB;
            } else if (regioncd.equals("0531")) {
                bankcd = XFBankCode.BANKCODE_BOC;
            } else if (regioncd.equals("023")) {
                bankcd = XFBankCode.BANKCODE_BOC;
            } else if (regioncd.equals("0351")) {
                bankcd = XFBankCode.BANKCODE_BOC;
            }
            int iSuccessCount = 0;
            try {
                if (checkFDCUTPAYDETLData(ctx, conn, instance, msgs) == -1) {
                    ctx.setRequestAtrribute("msg", "系统中存在未处理完成的代扣数据，请查询！");
                    return -1;
                }

                //检查银行帐号对应情况
                int recordcount = checkFDSystemData(conn, msgs, button, regioncd, bankcd);
                if (recordcount == -1) {
                    ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                    return -1;
                } else {
                    msgs.add("<br><br>房贷数据检查正常完成。");
                    ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                }

                if (recordcount > 0) {
                    msgs.clear();
                    //获取房贷系统数据到临时表，并统计数据。
                    deleteTableData_CutPatDetl(conn, msgs);
                    iSuccessCount = getFDSystemData(conn, msgs, button, regioncd, bankcd, "fdcutpaydetltemp");
                    ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                }
            } catch (Exception e) {
                Debug.debug(e);
                ctx.setRequestAtrribute("msg", "获取房贷系统数据未成功完成，请查询！");
                return -1;
            } finally {
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            }
*/
        }


        //房贷系统SBS入帐处理
        if (button.equals("FDACCOUNTBUTTON")) {
            int iSuccessCount = 0;
            try {
                iSuccessCount = doFDAccountButton(msgs);
                if (iSuccessCount == -1) {
                    msgs.add("<br>SBS系统不能连通或连接超时！");
                } else {
                    msgs.add("<br>成功入帐的记录数为： " + iSuccessCount + " 条！");
//                    iSuccessCount = doWriteBackButton(conn, msgs);
//                    if (iSuccessCount == -1) {
//                        msgs.add("<br>--回写房贷系统时出现问题！");
//                    } else {
//                        msgs.add("<br>回写房贷系统完成，处理记录数为： " + iSuccessCount + " 条！");
//                    }
                }
            } catch (Exception e) {
                Debug.debug(e);
                msgs.add("SBS帐务系统入帐处理未成功完成，请查询原因！");
                return -1;
            } finally {
                ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            }
        }

        //消费信贷系统SBS入帐处理
        if (button.equals("XFACCOUNTBUTTON")) {
            int iSuccessCount = 0;
            try {
                iSuccessCount = doXFAccountButton(msgs);
                if (iSuccessCount == -1) {
                    msgs.add("<br>SBS系统不能连通或连接超时！");
                } else {
                    msgs.add("<br>成功入帐的记录数为： " + iSuccessCount + " 条！");
                }
            } catch (Exception e) {
                Debug.debug(e);
                msgs.add("SBS帐务系统入帐处理未成功完成，请查询原因！");
                return -1;
            } finally {
                ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            }
        }


        //房贷系统回写处理
        if (button.equals("FDWRITEBACKBUTTON")) {
            int iSuccessCount = 0;
            try {
                iSuccessCount = doWriteBackButton(ctx,conn, msgs);
                if (iSuccessCount == -1) {
                    msgs.add("<br>--回写房贷系统时出现问题！");
                } else {
                    msgs.add("<br>回写房贷系统完成，处理记录数为： " + iSuccessCount + " 条！");
                }
            } catch (Exception e) {
                Debug.debug(e);
                msgs.add("<br>--房贷系统回写处理未成功完成，请查询原因！");
                return -1;
            } finally {
                ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            }
        }


        return 0;
    }


    /*
     在用户没有选中需要处理的记录时，根据用户点击的按钮，生成相应银行的记录集
    */

    private Xfactcutpaydetl[] getCutPayDetlListByBank(DatabaseConnection conn,
                                                      String bankcode) throws XfactcutpaydetlDaoException {

        Connection sqlconn = null;
        try {

            MyDB.getInstance().addDBConn(conn);
            sqlconn = conn.getConnection();

            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create(sqlconn);

            String sql = "billstatus = " +
                    XFBillStatus.BILLSTATUS_CHECKED +
                    " and paybackbankid = " + bankcode +
                    " order by journalno";

            return detlDao.findByDynamicWhere(sql, null);

        } catch (Exception e) {
            Debug.debug(e);
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }


    /*
     根据记录集设置表中数据的帐单状态
     */

    private void setCutpayDetlStatus(DatabaseConnection conn,
                                     Xfactcutpaydetl[] xfactcutpaydetls,
                                     String billstatus) throws XfactcutpaydetlDaoException {

        try {
            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create(conn.getConnection());
            Xfactcutpaydetl xfactcutpaydetl = null;

            String journalno = null;

            for (int i = 0; i < xfactcutpaydetls.length; i++) {
                journalno = xfactcutpaydetls[i].getJournalno();
                xfactcutpaydetl = detlDao.findByPrimaryKey(journalno);
                XfactcutpaydetlPk detlPk = new XfactcutpaydetlPk(journalno);
                xfactcutpaydetl.setBillstatus(billstatus);//设置状态
                detlDao.update(detlPk, xfactcutpaydetl);
            }
        } catch (Exception e) {
            Debug.debug(e);
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }

    }

    /*
   检查FDCUTPAYDETL表中数据
    */

    private int checkFDCUTPAYDETLData(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) throws Exception {
        try {
            MyDB.getInstance().addDBConn(conn);

            String sql = "select count(*) from fdcutpaydetl where billstatus <= 2";
            RecordSet rs = conn.executeQuery(sql);

            int count = 0;
            if (rs.next()) {
                count = rs.getInt(0);
            }
            rs.close();
            if (count > 0) {
                msgs.add("存在未处理的房贷代扣记录!");
                return -1;
            }
        } catch (Exception e) {
            msgs.add("发生异常：" + e.getMessage());
            Debug.debug(e);
            throw new Exception(e);
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
        return 0;
    }

    /*
    清理临时表数据
     */

    private void deleteTableData_CutPatDetl(DatabaseConnection conn, ErrorMessages msgs) throws Exception {

        MyDB.getInstance().addDBConn(conn);
        String sql = "delete from fdcutpaydetltemp";

        try {
            conn.executeUpdate(sql);
        } catch (Exception e) {
            msgs.add("发生异常：" + e.getMessage());
            Debug.debug(e);
            throw new Exception(e);
        }
    }


    /**
     * 查询房贷系统表  生成本地临时数据
     *
     * @param conn
     * @param msgs
     * @param button
     * @param regioncd
     * @param bankcd
     * @return
     * @throws Exception
     */
    private int getNewFDSystemData(DatabaseConnection conn, ErrorMessages msgs, String button,
                                   String regioncd, String bankcd) throws Exception {

        String preflag = "0";
        if (button.equals("GETFDPREDATABUTTON") || button.equals("CHECKFDPREDATABUTTON")) {
            preflag = "1";
        }

        //事务开始

        //清空临时表

        //获取新信贷数据LIST
        BaseCTL ctl;
        if (preflag.equals("0")) {
            ctl = new T100101CTL();
        }else{
            ctl = new T100103CTL();
        }
        //查询 房贷/消费信贷（1/2） 数据
        List<T100101ResponseRecord> recvList = ctl.start("1");

        //核对本地帐户表信息
        int rtn = 0;
/*
        rtn = checkNewFDSystemData(recvList, msgs);
        if (rtn == -1) {
            return -1;
        }
*/

        RecordSet rs = null;
        PreparedStatement ps = null;
        MyDB.getInstance().addDBConn(conn);

        try {
            MyDB.getInstance().addDBConn(conn);

            String sql = "select max(seqno) from fdcutpaydetl where substr(seqno,1,8) = '" + inputdate + "'";
            rs = conn.executeQuery(sql);

            int maxno = 0;
            if (rs.next()) {
                String max = rs.getString(0);
                if (max != null) {
                    maxno = Integer.parseInt(max.substring(8));
                }
            }

            conn.setAuto(false);

            sql = " delete from fdcutpaydetltemp";

            rtn = conn.executeUpdate(sql);

            sql = "insert into fdcutpaydetltemp " +
                    "(seqno,xdkhzd_khbh,xdkhzd_khmc,gthtjh_htbh,gthtjh_date,gthtjh_ll,gthtjh_jhje,gthtjh_bjje,gthtjh_lxje," +
                    "gthtb_zhbh,cutpayactno,billstatus,createtime,failreason,remark,preflag," +
                    "gthtjh_htnm,gthtjh_jhxh,journalno,regioncd,bankcd) " +
                    "values" +
                    "(?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?," +
                    "?,?,?,?,?) ";

            logger.info("sql=" + sql);
            ps = conn.getPreparedStatement(sql);

            int count = 0;
            for (T100101ResponseRecord record : recvList) {
                //TODO 地区字段为临时方案 待修正
                String tmpStr = record.getStddqh();
                String regioncdTmp,bankcdTmp,nameTmp;
                if (tmpStr == null || tmpStr.equals("null")) {
                    continue;
                } else {
                    String[] code = tmpStr.split("-");
                    regioncdTmp = code[0].trim();
                    bankcdTmp =  code[1].trim();
                }

                if (!regioncd.equals(regioncdTmp)) {
                    continue;
                }

                count++;
                ps.setString(1, inputdate + StringUtils.leftPad(String.valueOf(maxno + count), 7, '0'));
                ps.setString(2, record.getStdkhh());
                ps.setString(3, record.getStdkhmc());
                ps.setString(4, record.getStdhth());
                ps.setString(5, record.getStdjhhkr());
                if (preflag.equals("0")) {
                    ps.setDouble(6, Double.valueOf(record.getStdfxje()));  //罚息
                }else{
                    ps.setDouble(6, 0);  
                }
                ps.setDouble(7, Double.valueOf(record.getStdhkje()));
                ps.setDouble(8, Double.valueOf(record.getStdhkbj()));
                ps.setDouble(9, Double.valueOf(record.getStdhklx()));

                ps.setString(10, record.getStddkzh());
                ps.setString(11, record.getStdhkzh());
                ps.setString(12, "0");   //billstatus
                ps.setDate(13, new java.sql.Date(new Date().getTime()));
                ps.setString(14, " ");
                ps.setString(15, record.getStdryje()); //冗余金额字段  暂时放在备注里stdryje
                ps.setString(16, preflag);

                ps.setString(17, record.getStdjjh());   //借据号
                ps.setString(18, record.getStdqch());
                ps.setString(19, "0");

                //2010-10-20 对地区及银行代码进行临时处理
                //ps.setString(20, record.getStddqh());
                //ps.setString(21, record.getStdyhh());
                //begin
                ps.setString(20, regioncdTmp);
                ps.setString(21, bankcdTmp);
                //end

                ps.addBatch();
            }

            int[] results = ps.executeBatch();//执行批处理

            //处理地区信息   TODO
            sql = "insert into fdcutpaydetl select * from fdcutpaydetltemp";
            rtn = conn.executeUpdate(sql);
            if (rtn < 0) {
                msgs.add("处理临时表数据时出现问题。");
                return -1;
            }

            conn.commit();


            sql = "select count(*),sum(gthtjh_jhje) from  fdcutpaydetl " + " where billstatus = " + FDBillStatus.SEND_PENDING;
            rs = conn.executeQuery(sql);
            if (rs.next()) {
                rtn = rs.getInt(0);
                BigDecimal totalamt = new BigDecimal(rs.getDouble(1)).setScale(2, BigDecimal.ROUND_HALF_UP);
                msgs.add("<br>房贷系统记录数为：" + rtn + "条！");
                msgs.add("<br>房贷系统扣款总金额为：" + new DecimalFormat("##,###,###,###,##0.00").format(totalamt));
            }
        } catch (Exception e) {
            conn.rollback();
            msgs.add("发生异常：" + e.getMessage());
            Debug.debug(e);
            throw new Exception(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            MyDB.getInstance().releaseDBConn();
        }
        return rtn;
    }


    /*
   查询房贷系统表  生成本地临时数据
   仅处理建行代扣的记录
    */

    private int getFDSystemData(DatabaseConnection conn, ErrorMessages msgs, String button,
                                String regioncd, String bankcd, String tablename) throws Exception {

        String regionid = null;
        String regionid1 = null;
        String bankid = null;

        if ("0531".equals(regioncd)) {
            regionid = "GQ"; //济南
            regionid1 = "GSQ"; //济南
        } else if ("023".equals(regioncd)) {
            regionid = "GC"; //重庆
            regionid1 = "GSC"; //重庆
        } else if ("0351".equals(regioncd)) {
            regionid = "GT"; //太原
            regionid1 = "GST"; //太原
        } else {
            if (!"0532".equals(regioncd)) {
                throw new Exception("地区代码错误!");
            }
        }

        if (XFBankCode.BANKCODE_CCB.equals(bankcd)) {
            bankid = "801000026101041001";
        } else if (XFBankCode.BANKCODE_BOC.equals(bankcd)) {
            bankid = "801000026701041001";
        } else {
            throw new Exception("银行代码错误!");
        }

        String wherecondition = null;
        if ("0532".equals(regioncd)) {
            wherecondition = " and (b.gthtb_tyckzh = '" + bankid + "')";
        } else {//20090910 其他地区（目前济南、重庆、太原 默认为中行代扣）
            wherecondition = " and (b.gthtb_tyckzh = '" + bankid + "')"
                    + " and (" +
                    "c.xdkhzd_khbh like '" + regionid + "%'" +
                    " or c.xdkhzd_khbh like '" + regionid1 + "%'" +
                    ")";
        }


        int rtn = 0;
        RecordSet rs = null;
        try {
            MyDB.getInstance().addDBConn(conn);

            String sql = "select max(seqno) from fdcutpaydetl where substr(seqno,1,8) = '" + inputdate + "'";
            rs = conn.executeQuery(sql);

            int maxno = 0;
            if (rs.next()) {
                String max = rs.getString(0);
                if (max != null) {
                    maxno = Integer.parseInt(max.substring(8));
                }
            }

            String queryhead = " insert into " + tablename + "  (select '" + inputdate + "' || lpad(rownum +" + maxno +
                    ",7,'0') as seqno,";

            if (button.equals("GETFDDATABUTTON") || button.equals("CHECKFDDATABUTTON")) {
                sql = queryhead +
                        "       t.XDKHZD_KHBH," +
                        "       t.XDKHZD_KHMC," +
                        "       t.GTHTJH_HTBH," +
                        "       t.GTHTJH_DATE," +
                        "       t.GTHTJH_LL," +
                        "       round (t.GTHTJH_JHJE,2) as GTHTJH_JHJE, " +
                        "       round (t.GTHTJH_BJJE,2) as GTHTJH_BJJE," +
                        "       round (t.GTHTJH_LXJE,2) as GTHTJH_LXJE," +
                        "       t.GTHTB_ZHBH," +
                        "       t.cutpayactno," +
                        "       '0' as BILLSTATUS," +
                        "        sysdate as createtime," +
                        "       ' ' as failreason," +
                        "       ' ' as remark," +
                        "       '0' as preflag," +       //正常还款标志
                        "       t.GTHTJH_HTNM," +
                        "       t.GTHTJH_JHXH," +
                        "       null as journalno," +
                        " '" + regioncd + "' " + "  as regioncd, " +
                        " '" + bankcd + "' " + "  as bankcd " +
                        "  from (select c.xdkhzd_khbh," +
                        "               c.xdkhzd_khmc," +
                        "               a.gthtjh_htbh," +
                        "               a.gthtjh_date," +
                        "               a.gthtjh_ll," +
                        "               a.gthtjh_jhje," +
                        "               a.gthtjh_bjje," +
                        "               a.gthtjh_lxje," +
                        "               to_char(b.gthtb_zhbh) gthtb_zhbh," +
                        "               d.cutpayactno," +
                        "               a.GTHTJH_HTNM," +
                        "               a.GTHTJH_JHXH" +
                        "          from gthtjh@haier_shengchan a," +
                        "               gthtb@haier_shengchan  b," +
                        "               xdkhzd@haier_shengchan c," +
                        "               fdactnoinfo d" +
                        "         where a.gthtjh_htbh = b.gthtb_htbh" +
                        "           and b.gthtb_dwbh = c.xdkhzd_khbh" +
                        "           and b.gthtb_htzt <> '5'" +
                        "           and a.gthtjh_date like '" + inputdate.substring(0, 6) + "%'" +
                        "           and a.gthtjh_hkbz <> '1'" +
//                        "           and b.gthtb_tyckzh = '801000026101041001'" +
                        wherecondition +
                        "           and b.gthtb_zhbh = d.actno " +
                        "           and trim(a.gthtjh_htbh) = trim(d.contractno) " +
                        "           order by c.xdkhzd_khbh) t )";
            } else if (button.equals("GETFDPREDATABUTTON") || button.equals("CHECKFDPREDATABUTTON")) {
                sql = queryhead + " t1.XDKHZD_KHBH, " +
                        "       t1.XDKHZD_KHMC, " +
                        "       t1.GTHTJH_HTBH, " +
                        "       t1.GTHTJH_DATE, " +
                        "       t1.GTHTJH_LL, " +
//                        "       round(t1.xdfhkx_je + t2.xdfhkx_je,2) as GTHTJH_JHJE, " +
                        "       round(t1.xdfhkx_je + decode(t2.xdfhkx_je, null, 0, t2.xdfhkx_je), 2) as GTHTJH_JHJE, " +
                        "       round(t1.xdfhkx_je, 2) as GTHTJH_BJJE, " +
//                        "       round(t2.xdfhkx_je, 2) as GTHTJH_LXJE, " +
                        "       round(decode(t2.xdfhkx_je, null, 0, t2.xdfhkx_je), 2) as GTHTJH_LXJE, " +
                        "       t1.GTHTB_ZHBH, " +
                        "       t1.cutpayactno, " +
                        "       '0' as BILLSTATUS, " +
                        "        sysdate as createtime," +
                        "       ' ' as failreason," +
                        "       '提前还款' as remark," +
                        "       '1' as preflag," +                                     //提前还款标志
                        "       t1.GTHTJH_HTNM," +
                        "       t1.GTHTJH_JHXH," +
                        "       null as journalno," +
                        " '" + regioncd + "' " + "  as regioncd, " +
                        " '" + bankcd + "' " + "  as bankcd " +
                        "  from (select a.xdfhkx_id, c.xdkhzd_khbh, " +
                        "               c.xdkhzd_khmc, " +
                        "               a.xdfhkx_htbh as gthtjh_htbh, " +
                        "               a.xdfhkx_ywrq as gthtjh_date, " +
                        "               a.xdfhkx_ll as gthtjh_ll, " +
                        "               a.xdfhkx_htnm as gthtjh_htnm, " +
                        "               a.xdfhkx_jhxh as gthtjh_jhxh, " +
                        "               a.xdfhkx_ywzl, " +
                        "               round(a.xdfhkx_je, 2) xdfhkx_je, " +
                        "               to_char(b.gthtb_zhbh) gthtb_zhbh, " +
                        "               d.cutpayactno " +
                        "          from xdfhkx@haier_shengchan a, " +
                        "               gthtb@haier_shengchan  b, " +
                        "               xdkhzd@haier_shengchan c, " +
                        "               fdactnoinfo            d " +
                        "         where a.xdfhkx_htbh = b.gthtb_htbh " +
                        "           and b.gthtb_dwbh = c.xdkhzd_khbh " +
                        "           and b.gthtb_htzt <> '5' " +
                        "           and a.xdfhkx_ywrq = " + inputdate +
                        "           and b.gthtb_htlb = '03' " +
                        "           and (a.xdfhkx_ywzl = '6') " +
//                        "           and b.gthtb_zhbh = d.actno " + wherecondition + ") t1, " +

                        "           and b.gthtb_zhbh = d.actno " +
                        "           and trim(a.xdfhkx_htbh) = trim(d.contractno) " + wherecondition + ") t1, " +
                        "       (select a.xdfhkx_fkid, a.xdfhkx_htbh as gthtjh_htbh, " +
                        "               a.xdfhkx_ywzl, " +
                        "               round(a.xdfhkx_je, 2) xdfhkx_je " +
                        "          from xdfhkx@haier_shengchan a, " +
                        "               gthtb@haier_shengchan  b " +
                        "         where a.xdfhkx_htbh = b.gthtb_htbh " +
                        "           and b.gthtb_htzt <> '5' " +
                        "           and a.xdfhkx_ywrq = " + inputdate +
                        "           and b.gthtb_htlb = '03' " +
                        "           and (a.xdfhkx_ywzl = '7')) t2 " +
//                        " where t1.gthtjh_htbh = t2.gthtjh_htbh)";
                        " where t1.gthtjh_htbh = t2.gthtjh_htbh(+)  and t1.xdfhkx_id = t2.xdfhkx_fkid(+) ) ";
            } else {

            }

            logger.info("[获取房贷扣款记录SQL语句:]" + sql);
            rs = conn.executeQuery(sql);

            if (rs.next()) {
//                rtn = rsq.getInt(0);
            }

            sql = "select count(*),sum(gthtjh_jhje) from  " + tablename + " where billstatus = " + FDBillStatus.SEND_PENDING;
            rs = conn.executeQuery(sql);
            if (rs.next()) {
                rtn = rs.getInt(0);
                BigDecimal totalamt = new BigDecimal(rs.getDouble(1)).setScale(2, BigDecimal.ROUND_HALF_UP);
                msgs.add("<br>房贷系统记录数为：" + rtn + "条！");
                msgs.add("<br>房贷系统扣款总金额为：" + new DecimalFormat("##,###,###,###,##0.00").format(totalamt));
            }
        } catch (Exception e) {
            msgs.add("发生异常：" + e.getMessage());
            Debug.debug(e);
            throw new Exception(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            MyDB.getInstance().releaseDBConn();
        }
        return rtn;
    }


    /**
     * 检查新信贷系统中房贷系统数据
     */
    private int checkNewFDSystemData(List<T100101ResponseRecord> recvList, ErrorMessages msgs) throws Exception {

        logger.info("[检查房贷系统数据完整性]");

        String actno, name, contractno;
        FdactnoinfoDao actnodao = FdactnoinfoDaoFactory.create();
        Fdactnoinfo actnoinfo;
        Fdactnoinfo[] actnoinfos;


        int successflag = 0;
        int totalcount = 0;
        int failcount = 0;

        for (T100101ResponseRecord record : recvList) {
            name = record.getStdkhmc().trim();
            contractno = record.getStdhth().trim();
            actno = record.getStddkzh().trim();
            actnoinfo = actnodao.findByPrimaryKey(contractno);
            if (actnoinfo == null) {
                if (contractno.trim().length() > 10) {  //类似 GZ20080606-2的合同
                    String contractnotemp = contractno.substring(0, 10);
                    actnoinfos = actnodao.findWhereContractnoEquals(contractnotemp);
                    if (actnoinfos != null && actnoinfos.length == 1)
                        continue;
                    msgs.add("<br>===姓名：" + name + "  合同：" + contractno + "  不存在对应扣款帐号。");
                    failcount++;
                    successflag = -1;
                } else {
                    msgs.add("<br>===姓名：" + name + " 合同：" + contractno + " 不存在对应扣款帐号。");
                    logger.info("内部帐号未找到：" + name + " " + contractno + " " + actno);
                    failcount++;
                    successflag = -1;
                }
            } else {
                if (!actno.equals(actnoinfo.getActno())) {
                    msgs.add("<br>===姓名：" + name + " 合同编号：" + contractno + " 内部帐号不符：<br>信贷系统中的内部帐号："
                            + actno + "<br>扣款平台中的内部帐号：" + actnoinfo.getActno());
                    failcount++;
                }
            }

            totalcount++;
        }
        msgs.add("<br><br>共：" + totalcount + " 条待处理扣款记录。");
        msgs.add("<br><br>其中共：" + failcount + " 条扣款帐号信息不完整记录。");

        if (successflag >= 0) {
            successflag = totalcount;
        }
        return successflag;
    }

    /*
    检查房贷系统数据
     */

    private int checkFDSystemData(DatabaseConnection conn, ErrorMessages msgs, String button,
                                  String regioncd, String bankcd) throws Exception {

        logger.info("[检查房贷系统数据完整性]");

        String regionid = null;
        String regionid1 = null;
        String bankid = null;

/*    2010/2/2
        if ("0531".equals(regioncd)) {
            regionid = "GQ"; //济南
        } else if ("023".equals(regioncd)) {
            regionid = "GC"; //重庆
        } else if ("0351".equals(regioncd)) {
            regionid = "GT"; //太原
        } else {
            if (!"0532".equals(regioncd)) {
                throw new Exception("地区代码错误!");
            }
        }

        if (XFBankCode.BANKCODE_CCB.equals(bankcd)) {
            bankid = "801000026101041001";
        } else if (XFBankCode.BANKCODE_BOC.equals(bankcd)) {
            bankid = "801000026701041001";
        } else {
            throw new Exception("银行代码错误!");
        }

        String wherecondition = null;
        if ("0532".equals(regioncd)) {
            wherecondition = " and (b.gthtb_tyckzh = '" + bankid + "')";
        } else {//20090910 其他地区（目前济南、重庆、太原 默认为中行代扣）
            wherecondition = " and (b.gthtb_tyckzh = '" + bankid + "'" + " and c.xdkhzd_khbh like '" + regionid + "%')";
        }
*/

        if ("0531".equals(regioncd)) {
            regionid = "GQ"; //济南
            regionid1 = "GSQ"; //济南
        } else if ("023".equals(regioncd)) {
            regionid = "GC"; //重庆
            regionid1 = "GSC"; //重庆
        } else if ("0351".equals(regioncd)) {
            regionid = "GT"; //太原
            regionid1 = "GST"; //太原
        } else {
            if (!"0532".equals(regioncd)) {
                throw new Exception("地区代码错误!");
            }
        }

        if (XFBankCode.BANKCODE_CCB.equals(bankcd)) {
            bankid = "801000026101041001";
        } else if (XFBankCode.BANKCODE_BOC.equals(bankcd)) {
            bankid = "801000026701041001";
        } else {
            throw new Exception("银行代码错误!");
        }

        String wherecondition = null;
        if ("0532".equals(regioncd)) {
            wherecondition = " and (b.gthtb_tyckzh = '" + bankid + "')";
        } else {//20090910 其他地区（目前济南、重庆、太原 默认为中行代扣）
            wherecondition = " and (b.gthtb_tyckzh = '" + bankid + "')"
                    + " and (" +
                    "c.xdkhzd_khbh like '" + regionid + "%'" +
                    " or c.xdkhzd_khbh like '" + regionid1 + "%'" +
                    ")";
        }


        try {
            MyDB.getInstance().addDBConn(conn);
            String sql = null;
            if (button.equals("GETFDDATABUTTON") || button.equals("CHECKFDDATABUTTON")) {
                sql = "select t.XDKHZD_KHMC," +
                        "       t.GTHTJH_HTBH," +
                        "       t.GTHTB_ZHBH " +
                        "  from (select c.xdkhzd_khbh," +
                        "               c.xdkhzd_khmc," +
                        "               a.gthtjh_htbh," +
                        "               to_char(b.gthtb_zhbh) gthtb_zhbh" +
                        "          from gthtjh@haier_shengchan a," +
                        "               gthtb@haier_shengchan  b," +
                        "               xdkhzd@haier_shengchan c" +
                        "         where a.gthtjh_htbh = b.gthtb_htbh" +
                        "           and b.gthtb_dwbh = c.xdkhzd_khbh" +
                        "           and b.gthtb_htzt <> '5'" +
                        "           and a.gthtjh_date like '" + inputdate.substring(0, 6) + "%'" +
                        "           and a.gthtjh_hkbz <> '1'" +
                        wherecondition +
                        "         order by c.xdkhzd_khbh) t";
            } else if (button.equals("GETFDPREDATABUTTON") || button.equals("CHECKFDPREDATABUTTON")) {
                sql = "select c.xdkhzd_khmc,  " +
                        "       a.xdfhkx_htbh, " +
                        "       to_char(b.gthtb_zhbh) gthtb_zhbh " +
                        "  from xdfhkx@haier_shengchan a, " +
                        "       gthtb@haier_shengchan  b, " +
                        "       xdkhzd@haier_shengchan c " +
                        " where a.xdfhkx_htbh = b.gthtb_htbh " +
                        "   and b.gthtb_dwbh = c.xdkhzd_khbh " +
                        "   and b.gthtb_htzt <> '5' " +
                        "   and a.xdfhkx_ywrq = " + inputdate +
                        "   and b.gthtb_htlb = '03'" +
                        "   and (a.xdfhkx_ywzl = '6')" + wherecondition;
            } else {
                logger.info("系统处理错误。");
                throw new Exception("系统处理错误。");
            }

            logger.info("[获取房贷系统数据语句:]" + sql);
            RecordSet rs = conn.executeQuery(sql);

            String actno, name, contractno;
            FdactnoinfoDao actnodao = FdactnoinfoDaoFactory.create();
            Fdactnoinfo actnoinfo = new Fdactnoinfo();
            Fdactnoinfo[] actnoinfos;


            int successflag = 0;
            int totalcount = 0;
            int failcount = 0;
            while (rs.next()) {
                name = rs.getString(0).trim();
                contractno = rs.getString(1).trim();
                actno = rs.getString(2).trim();
                actnoinfo = actnodao.findByPrimaryKey(contractno);
                if (actnoinfo == null) {
                    if (contractno.trim().length() > 10) {  //类似 GZ20080606-2的合同
                        String contractnotemp = contractno.substring(0, 10);
                        actnoinfos = actnodao.findWhereContractnoEquals(contractnotemp);
                        if (actnoinfos != null && actnoinfos.length == 1)
                            continue;
                        msgs.add("<br>===姓名：" + name + "  合同编号：" + contractno + "  此合同号在消费信贷系统不存在。");
                        failcount++;
                        successflag = -1;
                    } else {
                        msgs.add("<br>===姓名：" + name + " 合同编号：" + contractno + " 此合同号在消费信贷系统中不存在。");
                        logger.info("内部帐号未找到：" + name + " " + contractno + " " + actno);
                        failcount++;
                        successflag = -1;
                    }
                } else {
                    if (!actno.equals(actnoinfo.getActno())) {
                        msgs.add("<br>===姓名：" + name + " 合同编号：" + contractno + " 内部帐号不符：<br>信贷系统中的内部帐号："
                                + actno + "<br>消费信贷中的内部帐号：" + actnoinfo.getActno());
                        failcount++;
                    }
                }

                totalcount++;
            }
            rs.close();
            msgs.add("<br><br>共：" + totalcount + " 条待处理扣款记录。");
            msgs.add("<br><br>其中共：" + failcount + " 条信息不完整扣款记录。");

            if (successflag >= 0) {
                successflag = totalcount;
            }
            return successflag;
        } catch (Exception e) {
            msgs.add("发生异常：" + e.getMessage());
            Debug.debug(e);
            throw new Exception(e);
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }


    /*
   进行建设银行直连扣款处理
    */

    private int doGeneratePkgButton(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) throws Exception {

        int rtn = 0;

        try {

            Fdcutpaydetl[] cutpaydetls = null;
            String[] recordnos = (String[]) ctx.getAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
            if (recordnos != null && recordnos.length != 0) {
                cutpaydetls = getCutPayDetlList(conn, recordnos);
            } else {
                String sql = "billstatus = " + FDBillStatus.SEND_PENDING;  //待提交的扣款记录
                FdcutpaydetlDao detlDao = FdcutpaydetlDaoFactory.create();
                cutpaydetls = detlDao.findByDynamicWhere(sql, null);
            }

            if (cutpaydetls != null && cutpaydetls.length > 0) {
                rtn = generateBankWithholdRecord(conn, cutpaydetls);
            } else {
                logger.info("无明细帐单记录");
                rtn = 0;
            }

        } catch (Exception e) {
            Debug.debug(e);
            throw new Exception();
        }

        return rtn;

    }

    /*
      根据用户选中的记录数组来返回cutpaydetl记录集
     */

    private Fdcutpaydetl[] getCutPayDetlList(DatabaseConnection conn, String[] recordnos) throws FdcutpaydetlDaoException {

        List results = new ArrayList();
        try {

            FdcutpaydetlDao detlDao = FdcutpaydetlDaoFactory.create();
            String seqno = null;

            for (int i = 0; i < recordnos.length; i++) {
                if (recordnos[i] != null && recordnos[i].trim().length() > 0) {
                    String[] str = recordnos[i].split("=");
                    seqno = StringUtils.strip(str[1], "\'");
                    results.add(detlDao.findByPrimaryKey(seqno));
                }
            }
            Fdcutpaydetl[] cutpaydetls = new Fdcutpaydetl[results.size()];
            for (int i = 0; i < results.size(); i++) {
                cutpaydetls[i] = (Fdcutpaydetl) results.get(i);
            }
            return cutpaydetls;
        } catch (Exception e) {
            Debug.debug(e);
            throw new FdcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }
    }


    /*
   生成建设银行直连代扣通讯包
   返回成功处理的记录数
    */

    private int generateBankWithholdRecord(DatabaseConnection conn, Fdcutpaydetl[] fdcutpaydetls) throws Exception {


        try {
            String journalnoSql = "select max(journalno) from xfifbankdetl where substr(journalno,1,8) = '" + inputdate + "'";
            RecordSet rs = conn.executeQuery(journalnoSql);

            int maxno = 0;
            if (rs.next()) {
                String max = rs.getString(0);
                if (max != null) {
                    maxno = Integer.parseInt(max.substring(8));
                }
            }

            String bizseqnoSql = "select max(bizseqno) from xfifbankdetl where substr(bizseqno,3,8) = '" + inputdate + "' and systemtype = '2'";
            rs = conn.executeQuery(bizseqnoSql);

            int maxBizseqno = 0;
            if (rs.next()) {
                String max = rs.getString(0);
                if (max != null) {
                    maxBizseqno = Integer.parseInt(max.substring(10));
                }
            }

            String journalno = inputdate + StringUtils.leftPad(Integer.toString(maxno + 1), 8, "0");
            String bizseqno = "FD" + inputdate + StringUtils.leftPad(Integer.toString(maxBizseqno + 1), 8, "0");

            XfifbankdetlDao bankdetlDao = XfifbankdetlDaoFactory.create();
            Xfifbankdetl bankdetl = new Xfifbankdetl();

            StringBuffer data = new StringBuffer();
            DecimalFormat amtdf = new DecimalFormat("#############0.00");

            BigDecimal totalamt = new BigDecimal(0);
            int count = 0;
            int detlslength = fdcutpaydetls.length;
            int i = 0;


            String updatefdsql = "update fdcutpaydetl set billstatus = " + FDBillStatus.MSG_CREATED +
                    " ,journalno = ? where seqno = ?";
            PreparedStatement ps = conn.getPreparedStatement(updatefdsql);


            for (i = 0; i < detlslength; i++) {
                //检查已选中的记录，确定为"青岛""建行"代扣
                if (!XFBankCode.BANKCODE_CCB.equals(fdcutpaydetls[i].getBankcd())) {
                    logger.info("存在非建行代扣数据" + fdcutpaydetls[i].getXdkhzdKhmc());
                    continue;
                }

                String amt = amtdf.format(fdcutpaydetls[i].getGthtjhJhje());
                data = data.append(amt + "|"); //金额
                data = data.append("|"); //明细备注，一般为空
                data = data.append(fdcutpaydetls[i].getCutpayactno().trim() + "|"); //帐号
                data = data.append(fdcutpaydetls[i].getXdkhzdKhmc().trim() + "|"); //户名
//                data = data.append(fdcutpaydetls[i].getClientid() + "|"); //证件号
                data = data.append("|"); //证件号

                totalamt = totalamt.add(fdcutpaydetls[i].getGthtjhJhje());

                //回写明细扣款记录表状态
                ps.setString(1, journalno);
                ps.setString(2, fdcutpaydetls[i].getSeqno());
                ps.executeUpdate();

                count++;

                //控制包大小
//                if (data.length() > 28000) {
                if (count % 130 == 0 || data.length() > 28000) {
                    //insert  XFIFBANKDETL 表

                    bankdetl.setJournalno(journalno);
                    bankdetl.setBizseqno(bizseqno);

                    bankdetl.setTxndate(inputdate);
                    bankdetl.setTxntype("BAW");
                    bankdetl.setTotalamt(totalamt);  //总金额
//                    bankdetl.setTotalcount(BigDecimal.valueOf(detlslength));  //总笔数
                    bankdetl.setTotalcount(BigDecimal.valueOf(count));  //总笔数
                    bankdetl.setCurrcount(BigDecimal.valueOf(count));   //本包笔数
/*
                    if (count == detlslength) {    //没有后续包
                        bankdetl.setMultiflag("0");
                    } else {
                        bankdetl.setMultiflag("1");
                    }
*/
                    bankdetl.setMultiflag("0");         //没有后续包

                    bankdetl.setTransferact("801000003012011001    "); //设置转出帐户
                    bankdetl.setFailamt(BigDecimal.valueOf(0));
                    bankdetl.setFailcount(BigDecimal.valueOf(0));
                    bankdetl.setStartdate(new Date());
                    bankdetl.setStatus(XFWithHoldStatus.SEND_PENDING); //待发送

                    //消费信贷系统标志：1   房贷系统标志 2
                    bankdetl.setSystemtype("2");

                    bankdetl.setBankid(XFBankCode.BANKCODE_CCB);

//                bankdetl.setOperatorid();
                    bankdetl.setOperatedate(new Date());

                    //按照建行测试环境要求，代扣为10000001  12位不足右补空格
//                    bankdetl.setUsage("10000001    ");
                    //按照建行生产环境要求，代扣为99999999  12位不足右补空格
                    bankdetl.setUsage("99999999    ");

                    bankdetl.setLog("【" + new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date()) + " 报文生成】");

                    //发送内容打包
                    bankdetl.setData(data.toString());

                    //数据库写入
                    bankdetlDao.insert(bankdetl);

                    //重置变量
                    data = new StringBuffer();
                    totalamt = new BigDecimal(0);
                    count = 0;
                    maxno++;
                    maxBizseqno++;
                    journalno = inputdate + StringUtils.leftPad(Integer.toString(maxno + 1), 8, "0");
                    bizseqno = "FD" + inputdate + StringUtils.leftPad(Integer.toString(maxBizseqno + 1), 8, "0");
                }

            }

            //insert  XFIFBANKDETL 表
            bankdetl.setJournalno(journalno);
            bankdetl.setBizseqno(bizseqno);

            bankdetl.setTxndate(inputdate);
            bankdetl.setTxntype("BAW");
            bankdetl.setTotalamt(totalamt);  //总金额
//            bankdetl.setTotalcount(BigDecimal.valueOf(detlslength));  //总笔数
            bankdetl.setTotalcount(BigDecimal.valueOf(count));  //总笔数
            bankdetl.setCurrcount(BigDecimal.valueOf(count));   //本包笔数
            bankdetl.setMultiflag("0");         //没有后续包

            bankdetl.setTransferact("801000003012011001    "); //设置转出帐户
            bankdetl.setFailamt(BigDecimal.valueOf(0));
            bankdetl.setFailcount(BigDecimal.valueOf(0));
            bankdetl.setStartdate(new Date());
            bankdetl.setStatus(XFWithHoldStatus.SEND_PENDING); //待发送
            //消费信贷系统标志：1   房贷系统标志 2
            bankdetl.setSystemtype("2");
            bankdetl.setBankid(XFBankCode.BANKCODE_CCB);
//                bankdetl.setOperatorid();
            bankdetl.setOperatedate(new Date());

            //按照建行测试环境要求，代扣为10000001  12位不足右补空格
//            bankdetl.setUsage("10000001    ");
            //按照建行生产环境要求，代扣为99999999  12位不足右补空格
            bankdetl.setUsage("99999999    ");

            bankdetl.setLog("【" + new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date()) + " 报文生成】");

            //发送内容打包
            bankdetl.setData(data.toString());
            //通讯表写入
            bankdetlDao.insert(bankdetl);

            return i;
        } catch (Exception e) {
            Debug.debug(e);
            System.out.println("====建行直连报文生成DEBUG====");
            throw new Exception(e);
        }
    }


    /*
   房贷处理：根据建行直连代扣的结果，对SBS进行入帐处理
    */

    private int doFDAccountButton(ErrorMessages msgs) {

        int rtn = 0;

        try {
            SBSManager sm = new SBSManager();
            rtn = sm.processFDAccount(inputdate, msgs);
        } catch (java.io.IOException e) {
            return -1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return rtn;
    }

    /*
   消费信贷处理：根据建行直连代扣的结果，对SBS进行入帐处理
    */

    private int doXFAccountButton(ErrorMessages msgs) {

        int rtn = 0;

        try {
            SBSManager sm = new SBSManager();
            rtn = sm.processXFCutPayAccount(inputdate, msgs);
        } catch (java.io.IOException e) {
            return -1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return rtn;
    }


    /*
   根据SBS入帐处理结果 对房贷系统进行回写处理
    */

    private int doWriteBackButton(SessionContext ctx,DatabaseConnection conn, ErrorMessages msgs) {

        int rtn = 0;

        try {
            Fdcutpaydetl[] cutpaydetls;
            String[] recordnos = (String[]) ctx.getAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
            if (recordnos != null && recordnos.length != 0) {
                cutpaydetls = getCutPayDetlList(conn, recordnos);
            } else {
                String sql = "billstatus = " + FDBillStatus.SBS_ACCOUNT_SUCCESS;
                FdcutpaydetlDao detlDao = FdcutpaydetlDaoFactory.create();
                cutpaydetls = detlDao.findByDynamicWhere(sql, null);
            }

            FDWriteBackManager fdw = new FDWriteBackManager();
            rtn = fdw.processWriteBack4NewCMS_SingleRecord(cutpaydetls , conn, msgs);
            //rtn = fdw.processWriteBack4NewCMS(inputdate, conn, msgs);
            //rtn = fdw.processWriteBack(inputdate, conn, msgs);
        } catch (java.io.IOException e) {
            logger.error("回写出现错误",e);
            return -1;
        } catch (Exception e) {
            logger.error("回写出现错误",e);
            throw new RuntimeException(e);
        }

        return rtn;
    }


}