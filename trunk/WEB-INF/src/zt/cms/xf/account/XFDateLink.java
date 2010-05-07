package zt.cms.xf.account;

import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.SessionAttributes;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.io.*;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.net.InetAddress;

import zt.platform.form.control.SessionContext;
import zt.platform.form.control.FormActions;
import zt.platform.form.util.event.*;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.utils.Debug;
import zt.platform.user.UserManager;
import zt.cms.xf.common.dao.XfactcutpaydetlDao;
import zt.cms.xf.common.dao.XfactcutpaymainDao;
import zt.cms.xf.common.dao.XfifbankdetlDao;
import zt.cms.xf.common.factory.XfactcutpaydetlDaoFactory;
import zt.cms.xf.common.factory.XfactcutpaymainDaoFactory;
import zt.cms.xf.common.factory.XfifbankdetlDaoFactory;
import zt.cms.xf.common.dto.*;
import zt.cms.xf.common.constant.*;
import zt.cms.xf.common.exceptions.XfactcutpaydetlDaoException;
//import zt.cms.xf.common.ssl.MySecureProtocolSocketFactory;
import zt.cms.xf.gateway.Bill99Manager;
import zt.cms.xf.gateway.AlipayManager;
import zt.cms.xf.gateway.CCBWLPTManager;

import zt.cmsi.mydb.MyDB;
import org.apache.commons.lang.StringUtils;
//import org.apache.commons.httpclient.*;
//import org.apache.commons.httpclient.protocol.Protocol;
//import org.apache.commons.httpclient.params.HttpMethodParams;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.NameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.BasicResponseHandler;
import org.dom4j.io.SAXReader;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.xsocket.connection.IBlockingConnection;
import org.xsocket.connection.BlockingConnection;
import com.alipay.config.AlipayConfig;
import com.alipay.util.Payment;
import com.zt.util.PropertyManager;

//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocketFactory;


/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-3-14
 * Time: 20:24:05
 * To change this template use File | Settings | File Templates.
 */
public class XFDateLink extends FormActions {

    private static Log log = LogFactory.getLog(XFDateLink.class);

    private String inputdate;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs, EventManager manager, String parameter) {

        SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");
        inputdate = sFmt.format(new Date());

        instance.setValue("INPUTDATE", inputdate);
        instance.setValue("SYSTEMDATE", inputdate);

        instance.getFormBean().getElement("FILEDOWNLOAD").setComponetTp(6);
        instance.getFormBean().getElement("DOT3BUTTON").setComponetTp(6);
        instance.getFormBean().getElement("DOCCBBUTTON").setComponetTp(6);
        instance.getFormBean().getElement("SETSUCCESSBUTTON").setComponetTp(6);
        instance.getFormBean().getElement("SETFAILBUTTON").setComponetTp(6);
        instance.getFormBean().getElement("ODBGENERATEBUTTON").setComponetTp(6);
        //200910建行新签约接口
        instance.getFormBean().getElement("DOCCBBUTTON4SIGN").setComponetTp(6);

        String lastbutton = (String) ctx.getAttribute("BUTTONNAME");
        if (lastbutton == null) {
            return -1;
        }

        if (lastbutton.equals("CCBCUTPAYBUTTON")) {
            //20090812 不再使用文件代扣形式
//            instance.getFormBean().getElement("FILEDOWNLOAD").setComponetTp(15);
            instance.getFormBean().getElement("DOCCBBUTTON").setComponetTp(15);
        } else if (lastbutton.equals("DOT3BUTTON")) {
            instance.getFormBean().getElement("DOT3BUTTON").setComponetTp(15);
        } else if (lastbutton.equals("DOCCBBUTTON")) {
            instance.getFormBean().getElement("DOCCBBUTTON").setComponetTp(15);
        } else if (lastbutton.equals("SUCCESSBUTTON")) {
            instance.getFormBean().getElement("SETSUCCESSBUTTON").setComponetTp(15);
        } else if (lastbutton.equals("FAILBUTTON")) {
            instance.getFormBean().getElement("SETFAILBUTTON").setComponetTp(15);
        } else if (lastbutton.equals("ODBGENERATEBUTTON")) {
            instance.getFormBean().getElement("ODBGENERATEBUTTON").setComponetTp(15);
        } else if (lastbutton.equals("DOCCBBUTTON4SIGN")) {
            instance.getFormBean().getElement("DOCCBBUTTON4SIGN").setComponetTp(15);
        }

        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_BREAK_TYPE);
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                           String button, ErrorMessages msgs, EventManager manager) {

        inputdate = ctx.getParameter("INPUTDATE");

        if (button.equals("FILEDOWNLOAD")) {
            try {
                if (true) {
                    //TODO:区分各银行
                    String bankcode = (String) ctx.getAttribute("BANKCODE");
                    if (bankcode == null) {
                        ctx.setRequestAtrribute("msg", "银行设定出现问题，请查询！");
                        ctx.setRequestAtrribute("flag", "1");
                        ctx.setRequestAtrribute("isback", "0");
                        ctx.setTarget("/showinfo.jsp");
                        instance.setReadonly(true);
                        return -1;
                    }
                    String[] recordnos = (String[]) ctx.getAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME);

                    String filename = bankcode + "_" + ctx.getParameter("INPUTDATE") + ".txt";

                    int rtn = 0;
                    if ((rtn = generateBankFile(conn, bankcode, filename, recordnos)) == -1) {
                        ctx.setRequestAtrribute("msg", "生成银行代扣文件出现问题，请查询！");
                        ctx.setRequestAtrribute("flag", "1");
                        ctx.setRequestAtrribute("isback", "0");
                        ctx.setTarget("/showinfo.jsp");
                        instance.setReadonly(true);
                        return -1;
                    } else if (rtn == -2) {
                        ctx.setRequestAtrribute("msg", "不存在需处理的银行代扣记录，请查询！");
                        ctx.setRequestAtrribute("flag", "1");
                        ctx.setRequestAtrribute("isback", "0");
                        ctx.setTarget("/showinfo.jsp");
                        instance.setReadonly(true);
                        return -1;
                    }

//                    setCutpayDetlStatus();

                    ctx.setRequestAtrribute("CUTPAYFILENAME", filename);
                    ctx.setTarget("/fileupdown/xfcutpaydownload.jsp");

//                    ctx.setRequestAtrribute("msg", "存在未处理完成的扣款记录，请查询！");
//                    ctx.setRequestAtrribute("flag", "1");
//                    ctx.setRequestAtrribute("isback", "0");
//                    ctx.setTarget("/showinfo.jsp");
//                    instance.setReadonly(true);
//                    return (-1);
                }
            } catch (Exception e) {
                ctx.setRequestAtrribute("msg", "系统处理出现错误，请查询！");
                ctx.setRequestAtrribute("flag", "0");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
                return (-1);
            }
        }

        if (button.equals("SETSUCCESSBUTTON")
                || button.equals("SETFAILBUTTON")) {
            String[] recordnos = (String[]) ctx.getAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
            try {
                Xfactcutpaydetl[] xfactcutpaydetls = getCutPayDetlListByRecordnos(conn, recordnos);
                BillsManager bm = new BillsManager(inputdate);
                if (button.equals("SETSUCCESSBUTTON")) {
//                    setCutpayDetlStatus(conn, xfactcutpaydetls, XFBillStatus.BILLSTATUS_CUTPAY_SUCCESS);
                    bm.setCutpayDetlPaidupSuccessBatch(xfactcutpaydetls);
                    ctx.setRequestAtrribute("msg", "设定扣款成功完成，请查询！");
                } else {
//                    setCutpayDetlStatus(conn, xfactcutpaydetls, XFBillStatus.BILLSTATUS_CUTPAY_FAILED);
                    bm.setCutpayDetlPaidupFailBatch(xfactcutpaydetls);
                    ctx.setRequestAtrribute("msg", "设定扣款失败完成，请查询！");
                }
            } catch (Exception e) {
                Debug.debug(e);
                return -1;
            } finally {
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            }
        }

/*

        if (button.equals("DOALIPAYBUTTON")) {
            doAlipayButton(ctx, conn, instance, msgs);
            ctx.setRequestAtrribute("msg", "支付宝代扣处理完成，稍后请查询处理结果！");
            ctx.setRequestAtrribute("flag", "1");
            ctx.setRequestAtrribute("isback", "0");
            ctx.setTarget("/showinfo.jsp");

        }
*/
        //建行直连代扣通讯记录生成
        if (button.equals("DOCCBBUTTON")) {
            String bankcode = (String) ctx.getAttribute("BANKCODE");
            if (bankcode == null) {
                ctx.setRequestAtrribute("msg", "银行设定出现问题，请查询！");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
                return -1;
            }
            String[] recordnos = (String[]) ctx.getAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME);

            int iSuccessCount = 0;
            try {
                iSuccessCount = doCCBDealButton(ctx, conn, instance, msgs);
                ctx.setRequestAtrribute("msg", "代扣通讯包已生成，本次处理记录数为：" + iSuccessCount + "条！");
            } catch (Exception e) {
                Debug.debug(e);
                ctx.setRequestAtrribute("msg", "建行直连代扣处理未成功完成，请查询！");
                return -1;
            } finally {
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            }
        }

        if (button.equals("DOT3BUTTON")) {
            int iSuccessCount = 0;
            //为避免长时间等待，批量处理过程中任意一笔出现异常马上退出
            try {
                iSuccessCount = doT3DealButton(ctx, conn, instance, msgs);
                ctx.setRequestAtrribute("msg", "第三方代扣处理完成,本次提交成功记录数为: " + iSuccessCount + " 条。");
            } catch (Exception e) {
                Debug.debug(e);
                ctx.setRequestAtrribute("msg", "第三方代扣平台发送处理未成功完成，请查询！");
                return -1;
            } finally {
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            }
        }

        if (button.equals("ODBGENERATEBUTTON")) {
            try {
//                 BillsManager bm = new BillsManager(chargeoffdate);
                BillsManager bm = new BillsManager(inputdate);
                if (bm.accountOpenedBills(conn) > 0) {
                    ctx.setRequestAtrribute("msg", "存在未处理完成的扣款记录，请查询！");
                    ctx.setRequestAtrribute("flag", "1");
                    ctx.setRequestAtrribute("isback", "0");
                    ctx.setTarget("/showinfo.jsp");
                    instance.setReadonly(true);
                    return (-1);
                }

                int count = doOdbGenerateButton(ctx, conn, instance, msgs);
                if (count == 0) {
                    ctx.setRequestAtrribute("msg", "无待处理的逾期帐单！");
                } else {
                    ctx.setRequestAtrribute("msg", "共生成  " + count + " 条逾期帐单，请查询处理结果！");
                }
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
            } catch (Exception e) {
                ctx.setRequestAtrribute("msg", "逾期帐单处理中发生错误，请查询处理结果！");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
            }
        }

        //200910 zhanrui 建行新的签约代扣接口处理
        if (button.equals("DOCCBBUTTON4SIGN")) {
            int iSuccessCount = 0;
            //为避免长时间等待，批量处理过程中任意一笔出现异常马上退出
            try {
                iSuccessCount = doCCB4SignButton(ctx, conn, instance, msgs);
                String msg = "处理完成,本次成功处理记录数为: " + iSuccessCount + " 条。";
                ctx.setRequestAtrribute("msg", msg);
                log.info(msg);
            } catch (Exception e) {
                Debug.debug(e);
                String msg = "建行签约代扣处理过程中出现异常，请查询！";
                msgs.add(msg);
                ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                log.error(msg);
//                return -1;
            } finally {
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            }
        }


        return 0;
    }


    private int generateBankFile(DatabaseConnection conn, String bankcode, String filename, String[] recordnos) {

        int rtn = 0;
        Xfactcutpaydetl[] xfactcutpaydetls;

        try {
            //删除已存在文件
            String filepath = PropertyManager.getProperty("BANK_CUTPAYFILE_PATH");
            try {
                File file = new File(filepath + filename);
                if (file.exists()) {
                    file.delete();
                } else {
//                    return -1;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return -1;
            }

            // 写入代扣内容

            if (recordnos == null) {
                xfactcutpaydetls = getCutPayDetlListByBank(conn, bankcode);
            } else {
                xfactcutpaydetls = getCutPayDetlListByRecordnos(conn, recordnos);
            }

            if (xfactcutpaydetls != null && xfactcutpaydetls.length > 0) {
                writeBankFile(filepath + filename, xfactcutpaydetls);
            } else {
                log.info("无明细帐单记录");
                return -2;
            }

            setCutpayDetlStatus(conn, xfactcutpaydetls, XFBillStatus.BILLSTATUS_CHARGEDOFF);

        } catch (Exception e) {
            Debug.debug(e);
            return -1;
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
     在用户没有选中需要处理的记录时，直接查出所有第三方代扣平台的记录集（待复核状态）
     目前只支持支付宝、快钱
    */
    private Xfactcutpaydetl[] getCutPayDetlListByT3Code(DatabaseConnection conn) throws XfactcutpaydetlDaoException {

        Connection sqlconn = null;
        try {

            MyDB.getInstance().addDBConn(conn);
            sqlconn = conn.getConnection();

            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create(sqlconn);

            String sql = "billstatus = " + XFBillStatus.BILLSTATUS_CHECKED +
                    " and (paybackbankid = " + XFBankCode.BANKCODE_ALIPAY +
                    " or paybackbankid = " + XFBankCode.BANKCODE_BILL99 + ")" +
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
      根据用户选中的记录数组来返回cutpaydetl记录集
     */
    private Xfactcutpaydetl[] getCutPayDetlListByRecordnos(DatabaseConnection conn,
                                                           String[] recordnos) throws XfactcutpaydetlDaoException {

        Connection sqlconn = null;
        List results = new ArrayList();
        try {

            MyDB.getInstance().addDBConn(conn);
            sqlconn = conn.getConnection();

            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create(sqlconn);

            String journalno = null;

            for (int i = 0; i < recordnos.length; i++) {
                if (recordnos[i] != null && recordnos[i].trim().length() > 0) {
                    String[] str = recordnos[i].split("=");
                    journalno = StringUtils.strip(str[1], "\'");
//                        Xfactcutpaydetl xfactcutpaydetl = detlDao.findByPrimaryKey(journalno);
                    results.add(detlDao.findByPrimaryKey(journalno));
                }
            }
            //return (Xfactcutpaydetl[]) results.toArray();
            Xfactcutpaydetl[] xfactcutpaydetls = new Xfactcutpaydetl[results.size()];
            for (int i = 0; i < results.size(); i++) {
                xfactcutpaydetls[i] = (Xfactcutpaydetl) results.get(i);
            }
            return xfactcutpaydetls;
        } catch (Exception e) {
            Debug.debug(e);
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }

    /*
     在用户没有选中需要处理的记录时，直接查出所有建设银行代扣的记录集（待复核状态）
    */
    private Xfactcutpaydetl[] getCutPayDetlListByCCB(DatabaseConnection conn) throws XfactcutpaydetlDaoException {

        Connection sqlconn = null;
        try {

            MyDB.getInstance().addDBConn(conn);
            sqlconn = conn.getConnection();

            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create(sqlconn);

            String sql = "billstatus = " + XFBillStatus.BILLSTATUS_CHECKED +
                    " and paybackbankid = " + XFBankCode.BANKCODE_CCB +
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
   写入文件内容
    */
    private void writeBankFile(String filename, Xfactcutpaydetl[] xfactcutpaydetls) {
        BufferedWriter output = null;
        File file = null;
        try {
            file = new File(filename);
            if (file.exists()) {
                System.out.println("文件存在");
            } else {
                System.out.println("文件不存在，正在创建...");
                if (file.createNewFile()) {
                    System.out.println("文件创建成功！");
                } else {
                    System.out.println("文件创建失败！");
                }
            }

//            StringBuffer str = new StringBuffer("\r\n");
            StringBuffer str = new StringBuffer();
            SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");
            DecimalFormat amtdf = new DecimalFormat("#############0.00");
            for (int i = 0; i < xfactcutpaydetls.length; i++) {
                str = str.append(StringUtils.rightPad(xfactcutpaydetls[i].getClientid(), 20, " "));
                str = str.append(StringUtils.rightPad(xfactcutpaydetls[i].getPaybackact(), 28, " "));

                String amt = amtdf.format(xfactcutpaydetls[i].getPaybackamt());
                str = str.append(StringUtils.leftPad(amt, 11, " "));
                str = str.append(xfactcutpaydetls[i].getPaybackactname() + "\r\n");

            }
            output = new BufferedWriter(new FileWriter(file));
            output.write(str.toString());
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                Debug.debug(e);
            }
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
     单笔处理帐单状态和日志
     */
    private void setCutpayDetlStatusAndLog(Xfactcutpaydetl olddetl,
                                           String billstatus, String netlog) throws XfactcutpaydetlDaoException {

        try {
            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();
//            Xfactcutpaydetl xfactcutpaydetl = null;

            String journalno = null;

            journalno = olddetl.getJournalno();
//            xfactcutpaydetl = detlDao.findByPrimaryKey(journalno);
            XfactcutpaydetlPk detlPk = new XfactcutpaydetlPk(journalno);
            Xfactcutpaydetl xfactcutpaydetl = detlDao.findByPrimaryKey(journalno);
            xfactcutpaydetl.setBillstatus(billstatus);//设置状态
            if (netlog != null && netlog.length() > 500) {
                netlog = netlog.substring(0, 500);
            }
//            xfactcutpaydetl.setFailurereason(netlog);//更新日志
            xfactcutpaydetl.setFailurereason(new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date()) + "--" + netlog);//更新日志
            detlDao.update(detlPk, xfactcutpaydetl);
        } catch (Exception e) {
            Debug.debug(e);
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }

    }

    /*
    单笔处理帐单日志
    */
    private void setCutpayDetlLog(Xfactcutpaydetl xfactcutpaydetl,
                                  String netlog) throws XfactcutpaydetlDaoException {

        try {
            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();
//            Xfactcutpaydetl xfactcutpaydetl = null;

            String journalno = null;

            journalno = xfactcutpaydetl.getJournalno();
//            xfactcutpaydetl = detlDao.findByPrimaryKey(journalno);
            XfactcutpaydetlPk detlPk = new XfactcutpaydetlPk(journalno);
            if (netlog != null && netlog.length() > 500) {
                netlog = netlog.substring(0, 500);
            }
            xfactcutpaydetl.setFailurereason(new SimpleDateFormat("yyyyMMdd hh:ss:ss").format(new Date()) + "--" + netlog);//更新日志
            detlDao.update(detlPk, xfactcutpaydetl);
        } catch (Exception e) {
            Debug.debug(e);
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }

    }


    /*
   进行第三方支付平台扣款处理
    */
    private int doT3DealButton(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) throws Exception {

        String[] recordnos = (String[]) ctx.getAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
        Xfactcutpaydetl[] xfactcutpaydetls;
        int rtn = 0;

        try {
            if (recordnos == null) {
                xfactcutpaydetls = getCutPayDetlListByT3Code(conn);
            } else {
                xfactcutpaydetls = getCutPayDetlListByRecordnos(conn, recordnos);
            }

            if (xfactcutpaydetls != null && xfactcutpaydetls.length > 0) {
                rtn = dealT3Cutpay(conn, xfactcutpaydetls);
            } else {
                log.info("无明细帐单记录");
                rtn = 0;
            }

        } catch (Exception e) {
            Debug.debug(e);
            throw new Exception();
        }

        return rtn;

    }

    /*
    200910  zhanrui  进行建行签约代扣处理
     */
    private int doCCB4SignButton(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) throws Exception {

        String[] recordnos = (String[]) ctx.getAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
        Xfactcutpaydetl[] xfactcutpaydetls;
        int rtn = 0;

        try {
            if (recordnos == null) {
                xfactcutpaydetls = getCutPayDetlListByCCB(conn);
            } else {
                xfactcutpaydetls = getCutPayDetlListByRecordnos(conn, recordnos);
            }

            if (xfactcutpaydetls != null && xfactcutpaydetls.length > 0) {
                rtn = dealCCB4SignCutpay(conn, xfactcutpaydetls, msgs);
            } else {
                log.info("无明细帐单记录");
                rtn = 0;
            }

        } catch (Exception e) {
            Debug.debug(e);
            throw new Exception();
        }

        return rtn;

    }


    /*
    进行建设银行直连扣款处理
     */
    private int doCCBDealButton(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) throws Exception {

        String[] recordnos = (String[]) ctx.getAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
        Xfactcutpaydetl[] xfactcutpaydetls;
        int rtn = 0;

        try {
            if (recordnos == null) {
                xfactcutpaydetls = getCutPayDetlListByCCB(conn);
            } else {
                xfactcutpaydetls = getCutPayDetlListByRecordnos(conn, recordnos);
            }

            if (xfactcutpaydetls != null && xfactcutpaydetls.length > 0) {
                rtn = generateBankWithholdRecord(conn, xfactcutpaydetls);
            } else {
                log.info("无明细帐单记录");
                rtn = 0;
            }

        } catch (Exception e) {
            Debug.debug(e);
            throw new Exception();
        }

        return rtn;

    }


    /*
    发起第三方扣款平台扣款处理
    返回成功处理（链接并接口成功，不包括业务处理成功）的记录数
     */
    private int dealT3Cutpay(DatabaseConnection conn, Xfactcutpaydetl[] xfactcutpaydetls) {

//        Connection sqlconn = conn.getConnection();

        XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();
        Xfactcutpaydetl xfactcutpaydetl = null;

        String journalno = null;
        int iConnectSuccessCount = 0;

        try {
            for (int i = 0; i < xfactcutpaydetls.length; i++) {
                XfactcutpaydetlPk detlPk = new XfactcutpaydetlPk(xfactcutpaydetls[i].getJournalno());
                xfactcutpaydetl = xfactcutpaydetls[i];
                if (xfactcutpaydetl.getPaybackbankid().equals(XFBankCode.BANKCODE_BILL99)) {
                    if (connect2bill99(xfactcutpaydetl) == 0) {//发起扣款成功
//                        xfactcutpaydetl.setBillstatus(XFBillStatus.BILLSTATUS_CHARGEDOFF);
//                        xfactcutpaydetl.setBillstatus(XFBillStatus.BILLSTATUS_SEND_SUCCESS);
//                        detlDao.update(detlPk, xfactcutpaydetl);
                        iConnectSuccessCount++;
                    }
                } else if (xfactcutpaydetl.getPaybackbankid().equals(XFBankCode.BANKCODE_ALIPAY)) {
                    if (connect2alipay(xfactcutpaydetl) == 0) {//发起扣款成功
//                        xfactcutpaydetl.setBillstatus(XFBillStatus.BILLSTATUS_CHARGEDOFF);
//                        xfactcutpaydetl.setBillstatus(XFBillStatus.BILLSTATUS_SEND_SUCCESS);
//                        detlDao.update(detlPk, xfactcutpaydetl);
                        iConnectSuccessCount++;
                    }
                }
            }
        } catch (Exception e) {
            Debug.debug(e);
            throw new RuntimeException();
        }
        return iConnectSuccessCount;
    }

    /*
    200910 zhanrui  根据用户的选择开始进行建行签约代扣处理
    返回成功处理（链接并接口成功，不包括业务处理成功）的记录数
     */
    private int dealCCB4SignCutpay(DatabaseConnection conn, Xfactcutpaydetl[] xfactcutpaydetls, ErrorMessages msgs) {

//        XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();
        Xfactcutpaydetl xfactcutpaydetl = null;

        int iConnectSuccessCount = 0;

        CCBWLPTManager ccbmanager = new CCBWLPTManager();

//        try {
//            ccbmanager.setConnection();
//        } catch (IOException e) {
//            msgs.add("服务器链接超时。");
//            throw new RuntimeException(e);
//        }

        BillsManager bm = new BillsManager(inputdate);

        try {

            for (int i = 0; i < xfactcutpaydetls.length; i++) {
                xfactcutpaydetl = xfactcutpaydetls[i];
                if (xfactcutpaydetl.getPaybackbankid().equals(XFBankCode.BANKCODE_CCB)) {
                    StringBuffer returnCode = new StringBuffer("返回码:");
                    StringBuffer returnMsg = new StringBuffer(" 返回信息:");

//                    if (ccbmanager.processCCBWLPT(xfactcutpaydetl, returnCode,returnMsg) == 0) {//发起扣款成功
                    if (ccbmanager.process(xfactcutpaydetl, returnCode, returnMsg) == 0) {//发起扣款成功
                        String message = returnCode.append(returnMsg).toString();
                        //在数据表中记录处理结果
//                        if ("000000".equals(returnCode)) {
                        if (returnCode.indexOf("000000") >= 0) {
                            iConnectSuccessCount++;
                            bm.setCutpayDetlPaidupSuccess(xfactcutpaydetl.getJournalno());
                            setCutpayDetlLog(xfactcutpaydetl, message);
                        } else {
                            //TODO:
                            if (returnCode.indexOf("Err") >= 0) {
                                setCutpayDetlLog(xfactcutpaydetl, message);
                            } else {
                                setCutpayDetlStatusAndLog(xfactcutpaydetl, XFBillStatus.BILLSTATUS_CUTPAY_FAILED, message);
                            }
                        }
                    } else {  //超时
                        log.error("建行代扣处理超时：" + xfactcutpaydetl.getJournalno() + xfactcutpaydetl.getClientname());
//                        setCutpayDetlStatusAndLog(xfactcutpaydetl,XFBillStatus.BILLSTATUS_CUTPAY_FAILED,"通讯连接超时，请发起查询交易查询建行处理结果。");
                        //帐单状态不变，便于发起查询交易.
                        setCutpayDetlLog(xfactcutpaydetl, "通讯连接超时，请发起查询交易查询建行处理结果。");
                        //超时后，跳出循环
                        break;
                    }
                } else {
                    //出现严重错误
                }
            }
        } catch (Exception e) {
            Debug.debug(e);
            throw new RuntimeException();
        } finally {
//            ccbmanager.closeConnection();
        }

        return iConnectSuccessCount;
    }


    /*
    生成建设银行直连代扣通讯包
    返回成功处理的记录数
     */

    private int generateBankWithholdRecord(DatabaseConnection conn, Xfactcutpaydetl[] xfactcutpaydetls) throws Exception {


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

            String bizseqnoSql = "select max(bizseqno) from xfifbankdetl where substr(bizseqno,3,8) = '" + inputdate + "' and systemtype = '1'";
            rs = conn.executeQuery(bizseqnoSql);

            int maxBizseqno = 0;
            if (rs.next()) {
                String max = rs.getString(0);
                if (max != null) {
                    maxBizseqno = Integer.parseInt(max.substring(10));
                }
            }

            String txjournalno = inputdate + StringUtils.leftPad(Integer.toString(maxno + 1), 8, "0");
            String bizseqno = "XF" + inputdate + StringUtils.leftPad(Integer.toString(maxBizseqno + 1), 8, "0");

            XfifbankdetlDao bankdetlDao = XfifbankdetlDaoFactory.create();
            Xfifbankdetl bankdetl = new Xfifbankdetl();

            StringBuffer data = new StringBuffer();
            DecimalFormat amtdf = new DecimalFormat("#############0.00");

            BigDecimal totalamt = new BigDecimal(0);
            int count = 0;
            int detlslength = xfactcutpaydetls.length;
            int i = 0;


            String updatefdsql = "update xfactcutpaydetl set billstatus = " + XFBillStatus.BILLSTATUS_CHARGEDOFF +
                    " ,txjournalno = ? where journalno = ?";
            PreparedStatement ps = conn.getPreparedStatement(updatefdsql);


            for (i = 0; i < detlslength; i++) {
                String amt = amtdf.format(xfactcutpaydetls[i].getPaybackamt());
                data = data.append(amt + "|"); //金额
                data = data.append("|"); //明细备注，一般为空
                data = data.append(xfactcutpaydetls[i].getPaybackact().trim() + "|"); //帐号
                data = data.append(xfactcutpaydetls[i].getPaybackactname().trim() + "|"); //户名
//                data = data.append(xfactcutpaydetls[i].getClientid() + "|"); //证件号
                data = data.append("|"); //证件号

                totalamt = totalamt.add(xfactcutpaydetls[i].getPaybackamt());

                //回写明细扣款记录表状态
                ps.setString(1, txjournalno);
                ps.setString(2, xfactcutpaydetls[i].getJournalno());
                ps.executeUpdate();

                count++;

                //控制包大小
//                if (data.length() > 28000) {
                if (count % 130 == 0 || data.length() > 28000) {
                    //insert  XFIFBANKDETL 表

                    bankdetl.setJournalno(txjournalno);
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
                    bankdetl.setSystemtype("1");

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
                    txjournalno = inputdate + StringUtils.leftPad(Integer.toString(maxno + 1), 8, "0");
                    bizseqno = "XF" + inputdate + StringUtils.leftPad(Integer.toString(maxBizseqno + 1), 8, "0");
                }

            }

            //insert  XFIFBANKDETL 表
            bankdetl.setJournalno(txjournalno);
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
            bankdetl.setSystemtype("1");
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


    private String getAlipayUrl_old(String journalno, String amount, String customer_code) {
        String itemUrl = "";
        String paygateway = "https://www.alipay.com/cooperate/gateway.do?"; //'支付接口（不用改）
//        String paygateway = "http://www.alipay.com/cooperate/gateway.do?"; //'支付接口（不用改）
        String t4 = "images/alipay_bwrx.gif"; //'支付宝按钮图片
        String t5 = "推荐使用支付宝付款"; //'按钮悬停说明
        String input_charset = AlipayConfig.CharSet; //页面编码（不用改）
        String service = "cae_charge_agent";//服务名称---代扣（不用改）

        String partner = AlipayConfig.partnerID; //partner合作伙伴ID(必填)
        String key = AlipayConfig.key; //partner账户对应的支付宝安全校验码(必填)
        String type_code = "TEST100011000101"; //type_code合作伙伴ID(必填),这个由销售申请，后台开通，可以从销售拿到，例如：100410000192

        String return_url = "http://218.58.70.181/test/alipay/alipay_return.jsp";
        //以上是账户信息。请改写成网站自己的
        //*****************************************************************
        String sign_type = "MD5"; //'签名方式（不用改）
        String subject = "commodityname"; //subject 商品名称“网站变量”

        SimpleDateFormat sFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sFmt.format(new Date());


        //String gmt_out_order_create = "2009-05-14 15:15:15"; //商户订单创建时间
        String gmt_out_order_create = dateStr; //商户订单创建时间

        String out_order_no = journalno; //商户网站订单（也就是外部订单号，是通过客户网站传给支付宝，不可以重复）
        //String amount = amount; //订单总价	0.01～50000.00

        //TODO:
//        String customer_code = "122850000035";

        //********************************************************************

//			itemUrl = Payment.CreateUrl(paygateway, t4, t5, service, partner,
//					sign_type, subject, gmt_out_order_create, out_order_no,
//					amount, customer_code, key, type_code, input_charset);
        itemUrl = Payment.CreateUrl_zr(paygateway, t4, t5, service, partner,
                sign_type, subject, gmt_out_order_create, out_order_no,
                amount, customer_code, key, type_code, input_charset, return_url);
        //response.sendRedirect(itemUrl);
        return itemUrl;

    }

    /*
    使用HTTPCLIENT与支付宝进行连接
     */

//    private int connect2alipay(Xfactcutpaydetl xfcutpaydetl) {
//
//        log.info("===Begin alipay:" + xfcutpaydetl.getJournalno() + "  " + xfcutpaydetl.getClientname());
//
//        if (StringUtils.isBlank(xfcutpaydetl.getCustomerCode())) {
//            return -1;
//        }
//
//        AlipayManager am = new AlipayManager();
//        //TODO:0.01
//        //构造HttpClient的实例
//        Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory(), 443);
//        Protocol.registerProtocol("https", myhttps);
//        HttpClient httpClient = new HttpClient();
//        //创建GET方法的实例
//        // TODO:0.01
//
//        //Double amt = xfcutpaydetl.getPaybackamt().doubleValue() * 100;
//        //String orderAmount = amt.toString();
//
//        String orderAmount = xfcutpaydetl.getPaybackamt().toString();
//
//        GetMethod getMethod = new GetMethod(am.getAlipayUrl(xfcutpaydetl.getJournalno(), orderAmount, xfcutpaydetl.getCustomerCode()));
//        //使用系统提供的默认的恢复策略,在发生异常时候将自动重试3次，在这里也可以设置成自定义的恢复策略
//        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
//
//        try {
//            //执行getMethod
//            int statusCode = httpClient.executeMethod(getMethod);
//            if (statusCode != HttpStatus.SC_OK) {
//                System.out.println("支付宝扣款失败 Method failed: " + getMethod.getStatusLine());
//                //TODO:在日志中记录返回码
//                return -1;
//            }
//            //读取内容
//            byte[] responseBody = getMethod.getResponseBody();
//            //处理内容
//            String body = new String(responseBody);
//            System.out.println(body);
//            log.info("===支付宝处理结果" + "[" + xfcutpaydetl.getJournalno() + "  " + xfcutpaydetl.getClientname() + ":]" + body);
//
//            if (body != null && (body.indexOf("<is_success>T</is_success>") != -1)) {
//                return 0;
//            } else {
//                return -1;
//            }
//        } catch (HttpException e) {
//            // 发生致命的异常，可能是协议不对或者返回的内容有问题
//            System.out.println("Please check your provided http address!");
//            e.printStackTrace();
//            log.error("===发生HttpException" + "Please check your provided http address!", e);
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            // 发生网络异常
//            e.printStackTrace();
//            log.error("===发生IOException", e);
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            // 发生异常
//            e.printStackTrace();
//            log.error("===发生Exception", e);
//            throw new RuntimeException(e);
//        } finally {
//            //释放连接
//            getMethod.releaseConnection();
//        }
//    }

    private int connect2alipay(Xfactcutpaydetl xfcutpaydetl) {

        int rtn = 0;
        log.info("===Begin alipay:" + xfcutpaydetl.getJournalno() + "  " + xfcutpaydetl.getClientname());
        DefaultHttpClient httpclient = null;
        KeyStore trustStore = null;
        FileInputStream instream = null;
        SSLSocketFactory socketFactory = null;

        try {
            httpclient = new DefaultHttpClient();
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            String path = System.getProperty("user.dir");
            instream = new FileInputStream(new File("d:/cms/keystore/cms.truststore.alipay"));
            trustStore.load(instream, "111111".toCharArray());

            socketFactory = new org.apache.http.conn.ssl.SSLSocketFactory(trustStore);

        } catch (Exception e) {
            log.error("===与支付宝连接出现错误：", e);
            throw new RuntimeException();
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }

        try {
            Scheme sch = new Scheme("https", socketFactory, 443);
            httpclient.getConnectionManager().getSchemeRegistry().register(sch);

            AlipayManager am = new AlipayManager();

            String journalno = xfcutpaydetl.getJournalno();
            DecimalFormat amtdf = new DecimalFormat("#############0.00");


            String amt = amtdf.format(xfcutpaydetl.getPaybackamt());
            String url = am.getAlipayUrl(journalno, amt, xfcutpaydetl.getCustomerCode());
            HttpGet httpget = new HttpGet(url);

            log.debug("executing request" + httpget.getRequestLine());

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);
            log.debug("response body:" + responseBody);

            if (responseBody != null) {
                SAXReader reader = new SAXReader();
                Document doc = reader.read(new ByteArrayInputStream(responseBody.getBytes("GBK")));
                Element root = doc.getRootElement();

                Node node = doc.selectSingleNode("//alipay/is_success");
                if ("T".equals(node.getText())) {
                    log.info("支付宝代扣发送成功" + journalno + " " + xfcutpaydetl.getClientname());
                    node = doc.selectSingleNode("//alipay/response/order/out_order_no");
                    if (journalno.equals(node.getText())) {  //核对流水号
                        node = doc.selectSingleNode("//alipay/response/order/status");
                        if ("S".equals(node.getText())) {
                            log.info("支付宝代扣款项成功" + journalno + " " + xfcutpaydetl.getClientname());
                            //修改交易状态
                            BillsManager bm = new BillsManager(inputdate);
                            bm.setCutpayDetlPaidupSuccess(journalno);
                            setCutpayDetlStatusAndLog(xfcutpaydetl, XFBillStatus.BILLSTATUS_CUTPAY_SUCCESS, responseBody);
                            rtn = 0;
                        } else {
                            log.info("支付宝代扣款项失败!" + journalno + " " + xfcutpaydetl.getClientname());
                            setCutpayDetlStatusAndLog(xfcutpaydetl, XFBillStatus.BILLSTATUS_CUTPAY_FAILED, responseBody);
                            rtn = -1;
                        }
                    } else {
                        log.info("支付宝代扣款项失败!" + journalno + " " + xfcutpaydetl.getClientname() + responseBody);
                        setCutpayDetlLog(xfcutpaydetl, "流水号不符！" + journalno + " " + responseBody);
                        rtn = -1;
                    }
                } else {
                    log.info("支付宝代扣发送成功" + journalno + " " + xfcutpaydetl.getClientname());
                    log.info("支付宝代扣扣款失败" + journalno + " " + xfcutpaydetl.getClientname());
                    setCutpayDetlStatusAndLog(xfcutpaydetl, XFBillStatus.BILLSTATUS_CUTPAY_FAILED, responseBody);
//                    setCutpayDetlLog(xfcutpaydetl, "支付宝代扣报文发送成功，接收到扣款处理失败信息。"+ " " +responseBody);
                    rtn = -1;
                }
                return rtn;
            } else {
                //TODO: log to db
                log.info("===支付宝处理结果:失败" + "[" + xfcutpaydetl.getJournalno() + "  " + xfcutpaydetl.getClientname() + ":]" + responseBody);
                //发送失败不修改状态，便于重复发送
                //另一种办法：置交易失败标志，需重新生成扣款记录

                // setCutpayDetlStatusAndLog(xfcutpaydetl, XFBillStatus.BILLSTATUS_CUTPAY_FAILED, responseBody);
                setCutpayDetlLog(xfcutpaydetl, "发送失败，未收到支付宝相应信息，请重新发送代扣请求。");
                return -1;
            }


        } catch (Exception e) {
            log.error("===与支付宝连接出现错误：", e);
            throw new RuntimeException();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }

    }


    /*
   快钱代扣发起处理：
   每笔代扣独立处理链接，待优化
    */

    private int connect2bill99(Xfactcutpaydetl xfcutpaydetl) {

        log.info("===Begin connect2bill99" + xfcutpaydetl.getJournalno() + "  " + xfcutpaydetl.getClientname());

        DefaultHttpClient httpclient = null;
        KeyStore trustStore = null;
        FileInputStream instream = null;
        SSLSocketFactory socketFactory = null;

        try {
            httpclient = new DefaultHttpClient();
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            String path = System.getProperty("user.dir");
            instream = new FileInputStream(new File("d:/cms/keystore/cms.truststore.99bill"));
            trustStore.load(instream, "111111".toCharArray());

            socketFactory = new org.apache.http.conn.ssl.SSLSocketFactory(trustStore);

        } catch (Exception e) {
            log.error("===与快钱连接出现错误：", e);
            throw new RuntimeException();
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }

        try {
            Scheme sch = new Scheme("https", socketFactory, 443);
            httpclient.getConnectionManager().getSchemeRegistry().register(sch);

//            HttpGet httpget = new HttpGet("https://www.99bill.com/bankdebit/serviceDeduction.htm");
            HttpPost httppost = new HttpPost("https://www.99bill.com/bankdebit/serviceDeduction.htm");
            // 填入各个表单域的值
            Bill99Manager bm = new Bill99Manager();
            List<NameValuePair> data = bm.getPostValues(xfcutpaydetl);
            httppost.setEntity(new UrlEncodedFormEntity(data, HTTP.UTF_8));

            log.debug("executing request" + httppost.getRequestLine());

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httppost, responseHandler);
            log.debug("response body:" + responseBody);

            if (responseBody != null && (responseBody.indexOf("success") != -1)) {
                log.info("===快钱处理结果:成功" + "[" + xfcutpaydetl.getJournalno() + "  " + xfcutpaydetl.getClientname() + ":]" + responseBody);
//                BillsManager billmgr = new BillsManager(inputdate);
//                billmgr.setCutpayDetlPaidupSuccess(xfcutpaydetl.getJournalno());
                //此处成功仅代表交易数据被快钱正常接收，业务处理是否成功待快钱发起通知。
                setCutpayDetlStatusAndLog(xfcutpaydetl, XFBillStatus.BILLSTATUS_SEND_SUCCESS, responseBody);
                return 0;
            } else {
                //TODO: log to db
                log.info("===快钱处理结果:失败" + "[" + xfcutpaydetl.getJournalno() + "  " + xfcutpaydetl.getClientname() + ":]" + responseBody);
                //发送失败不修改状态，便于重复发送
                //另一种办法：置交易失败标志，需重新生成扣款记录

                // setCutpayDetlStatusAndLog(xfcutpaydetl, XFBillStatus.BILLSTATUS_CUTPAY_FAILED, responseBody);
                setCutpayDetlLog(xfcutpaydetl, responseBody);
                return -1;
            }


        } catch (Exception e) {
            log.error("===与快钱连接出现错误：", e);
            throw new RuntimeException();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }


/*
    private int connectToAlipay() {
        //构造HttpClient的实例
        HttpClient httpClient = new HttpClient();
        //创建GET方法的实例
        GetMethod getMethod = new GetMethod("http://www.ibm.com");
        //使用系统提供的默认的恢复策略
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
        try {
            //执行getMethod
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: "
                        + getMethod.getStatusLine());
            }
            //读取内容
            byte[] responseBody = getMethod.getResponseBody();
            //处理内容
            System.out.println(new String(responseBody));
        } catch (HttpException e) {
            //发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("Please check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
            //发生网络异常
            e.printStackTrace();
        } finally {
            //释放连接
            getMethod.releaseConnection();
        }
    }
}
*/

    /*
    生成逾期帐单处理
     */

    private int doOdbGenerateButton(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) throws Exception {

//        String[] recordnos = (String[]) ctx.getAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
//
//        List results = new ArrayList();

        int count = 0;
        try {
            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

            MyDB.getInstance().addDBConn(conn);
            Connection sqlconn = conn.getConnection();

            XfactcutpaymainDao mainDao = XfactcutpaymainDaoFactory.create(sqlconn);
            XfactcutpaymainPk mainPk = new XfactcutpaymainPk();

            String sql = "chargeoffcd = '1' and  closedcd = '0'  and pbstatus <> '3'  and  to_date(" + inputdate +
                    ", 'yyyyMMdd')" + " - paybackdate >= " + XFCommon.COMMON_GRACEPERIOD;

            log.info("逾期查询SQL:" + sql);


            Xfactcutpaymain[] xfactcutpaymain = mainDao.findByDynamicWhere(sql, null);
            if ((count = xfactcutpaymain.length) == 0) {
                return count;  //无逾期记录     返回-2？
            }

            SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");

            for (int i = 0; i < count; i++) {
                xfactcutpaymain[i].setPbstatus(XFPBStatus.PBSTATUS_CHECK_PENDING);
                xfactcutpaymain[i].setOperatorid(um.getUserName());
                xfactcutpaymain[i].setOperatedate(sFmt.parse(inputdate));
                mainPk.setContractno(xfactcutpaymain[i].getContractno());
                mainPk.setPoano(xfactcutpaymain[i].getPoano());

                mainDao.update(mainPk, xfactcutpaymain[i]);
            }
            return count;
        } catch (Exception e) {
            Debug.debug(e);
            throw new Exception(e);
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }

    private int doOdbFHButton(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) {

        String[] recordnos = (String[]) ctx.getAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME);

        List results = new ArrayList();

        try {
            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

            MyDB.getInstance().addDBConn(conn);
            Connection sqlconn = conn.getConnection();

            XfactcutpaymainDao mainDao = XfactcutpaymainDaoFactory.create(sqlconn);

            String sql = "closedcd = '0' and  to_date(" + inputdate +
                    ", 'yyyyMMdd')" + " - paybackdate = " + XFCommon.COMMON_GRACEPERIOD;

            log.info("逾期查询SQL:" + sql);


            Xfactcutpaymain[] xfactcutpaymain = mainDao.findByDynamicWhere(sql, null);
            if (xfactcutpaymain.length == 0) {
                return 0;  //无逾期记录     返回-2？
            }

            SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");

            for (int i = 0; i < xfactcutpaymain.length; i++) {
                xfactcutpaymain[i].setPbstatus(XFPBStatus.PBSTATUS_CHECK_PENDING);
                xfactcutpaymain[i].setOperatorid(um.getUserName());
                xfactcutpaymain[i].setOperatedate(sFmt.parse(inputdate));
            }


        } catch (Exception e) {
            Debug.debug(e);
        } finally {
            MyDB.getInstance().releaseDBConn();
        }


        return 0;
    }


}