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
        //200910������ǩԼ�ӿ�
        instance.getFormBean().getElement("DOCCBBUTTON4SIGN").setComponetTp(6);

        String lastbutton = (String) ctx.getAttribute("BUTTONNAME");
        if (lastbutton == null) {
            return -1;
        }

        if (lastbutton.equals("CCBCUTPAYBUTTON")) {
            //20090812 ����ʹ���ļ�������ʽ
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
                    //TODO:���ָ�����
                    String bankcode = (String) ctx.getAttribute("BANKCODE");
                    if (bankcode == null) {
                        ctx.setRequestAtrribute("msg", "�����趨�������⣬���ѯ��");
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
                        ctx.setRequestAtrribute("msg", "�������д����ļ��������⣬���ѯ��");
                        ctx.setRequestAtrribute("flag", "1");
                        ctx.setRequestAtrribute("isback", "0");
                        ctx.setTarget("/showinfo.jsp");
                        instance.setReadonly(true);
                        return -1;
                    } else if (rtn == -2) {
                        ctx.setRequestAtrribute("msg", "�������账������д��ۼ�¼�����ѯ��");
                        ctx.setRequestAtrribute("flag", "1");
                        ctx.setRequestAtrribute("isback", "0");
                        ctx.setTarget("/showinfo.jsp");
                        instance.setReadonly(true);
                        return -1;
                    }

//                    setCutpayDetlStatus();

                    ctx.setRequestAtrribute("CUTPAYFILENAME", filename);
                    ctx.setTarget("/fileupdown/xfcutpaydownload.jsp");

//                    ctx.setRequestAtrribute("msg", "����δ������ɵĿۿ��¼�����ѯ��");
//                    ctx.setRequestAtrribute("flag", "1");
//                    ctx.setRequestAtrribute("isback", "0");
//                    ctx.setTarget("/showinfo.jsp");
//                    instance.setReadonly(true);
//                    return (-1);
                }
            } catch (Exception e) {
                ctx.setRequestAtrribute("msg", "ϵͳ������ִ������ѯ��");
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
                    ctx.setRequestAtrribute("msg", "�趨�ۿ�ɹ���ɣ����ѯ��");
                } else {
//                    setCutpayDetlStatus(conn, xfactcutpaydetls, XFBillStatus.BILLSTATUS_CUTPAY_FAILED);
                    bm.setCutpayDetlPaidupFailBatch(xfactcutpaydetls);
                    ctx.setRequestAtrribute("msg", "�趨�ۿ�ʧ����ɣ����ѯ��");
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
            ctx.setRequestAtrribute("msg", "֧�������۴�����ɣ��Ժ����ѯ��������");
            ctx.setRequestAtrribute("flag", "1");
            ctx.setRequestAtrribute("isback", "0");
            ctx.setTarget("/showinfo.jsp");

        }
*/
        //����ֱ������ͨѶ��¼����
        if (button.equals("DOCCBBUTTON")) {
            String bankcode = (String) ctx.getAttribute("BANKCODE");
            if (bankcode == null) {
                ctx.setRequestAtrribute("msg", "�����趨�������⣬���ѯ��");
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
                ctx.setRequestAtrribute("msg", "����ͨѶ�������ɣ����δ����¼��Ϊ��" + iSuccessCount + "����");
            } catch (Exception e) {
                Debug.debug(e);
                ctx.setRequestAtrribute("msg", "����ֱ�����۴���δ�ɹ���ɣ����ѯ��");
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
            //Ϊ���ⳤʱ��ȴ��������������������һ�ʳ����쳣�����˳�
            try {
                iSuccessCount = doT3DealButton(ctx, conn, instance, msgs);
                ctx.setRequestAtrribute("msg", "���������۴������,�����ύ�ɹ���¼��Ϊ: " + iSuccessCount + " ����");
            } catch (Exception e) {
                Debug.debug(e);
                ctx.setRequestAtrribute("msg", "����������ƽ̨���ʹ���δ�ɹ���ɣ����ѯ��");
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
                    ctx.setRequestAtrribute("msg", "����δ������ɵĿۿ��¼�����ѯ��");
                    ctx.setRequestAtrribute("flag", "1");
                    ctx.setRequestAtrribute("isback", "0");
                    ctx.setTarget("/showinfo.jsp");
                    instance.setReadonly(true);
                    return (-1);
                }

                int count = doOdbGenerateButton(ctx, conn, instance, msgs);
                if (count == 0) {
                    ctx.setRequestAtrribute("msg", "�޴�����������ʵ���");
                } else {
                    ctx.setRequestAtrribute("msg", "������  " + count + " �������ʵ������ѯ��������");
                }
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
            } catch (Exception e) {
                ctx.setRequestAtrribute("msg", "�����ʵ������з����������ѯ��������");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
            }
        }

        //200910 zhanrui �����µ�ǩԼ���۽ӿڴ���
        if (button.equals("DOCCBBUTTON4SIGN")) {
            int iSuccessCount = 0;
            //Ϊ���ⳤʱ��ȴ��������������������һ�ʳ����쳣�����˳�
            try {
                iSuccessCount = doCCB4SignButton(ctx, conn, instance, msgs);
                String msg = "�������,���γɹ������¼��Ϊ: " + iSuccessCount + " ����";
                ctx.setRequestAtrribute("msg", msg);
                log.info(msg);
            } catch (Exception e) {
                Debug.debug(e);
                String msg = "����ǩԼ���۴�������г����쳣�����ѯ��";
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
            //ɾ���Ѵ����ļ�
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

            // д���������

            if (recordnos == null) {
                xfactcutpaydetls = getCutPayDetlListByBank(conn, bankcode);
            } else {
                xfactcutpaydetls = getCutPayDetlListByRecordnos(conn, recordnos);
            }

            if (xfactcutpaydetls != null && xfactcutpaydetls.length > 0) {
                writeBankFile(filepath + filename, xfactcutpaydetls);
            } else {
                log.info("����ϸ�ʵ���¼");
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
     ���û�û��ѡ����Ҫ����ļ�¼ʱ�������û�����İ�ť��������Ӧ���еļ�¼��
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
     ���û�û��ѡ����Ҫ����ļ�¼ʱ��ֱ�Ӳ�����е���������ƽ̨�ļ�¼����������״̬��
     Ŀǰֻ֧��֧��������Ǯ
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
      �����û�ѡ�еļ�¼����������cutpaydetl��¼��
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
     ���û�û��ѡ����Ҫ����ļ�¼ʱ��ֱ�Ӳ�����н������д��۵ļ�¼����������״̬��
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
   д���ļ�����
    */
    private void writeBankFile(String filename, Xfactcutpaydetl[] xfactcutpaydetls) {
        BufferedWriter output = null;
        File file = null;
        try {
            file = new File(filename);
            if (file.exists()) {
                System.out.println("�ļ�����");
            } else {
                System.out.println("�ļ������ڣ����ڴ���...");
                if (file.createNewFile()) {
                    System.out.println("�ļ������ɹ���");
                } else {
                    System.out.println("�ļ�����ʧ�ܣ�");
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
     ���ݼ�¼�����ñ������ݵ��ʵ�״̬
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
                xfactcutpaydetl.setBillstatus(billstatus);//����״̬
                detlDao.update(detlPk, xfactcutpaydetl);
            }
        } catch (Exception e) {
            Debug.debug(e);
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }

    }

    /*
     ���ʴ����ʵ�״̬����־
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
            xfactcutpaydetl.setBillstatus(billstatus);//����״̬
            if (netlog != null && netlog.length() > 500) {
                netlog = netlog.substring(0, 500);
            }
//            xfactcutpaydetl.setFailurereason(netlog);//������־
            xfactcutpaydetl.setFailurereason(new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date()) + "--" + netlog);//������־
            detlDao.update(detlPk, xfactcutpaydetl);
        } catch (Exception e) {
            Debug.debug(e);
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }

    }

    /*
    ���ʴ����ʵ���־
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
            xfactcutpaydetl.setFailurereason(new SimpleDateFormat("yyyyMMdd hh:ss:ss").format(new Date()) + "--" + netlog);//������־
            detlDao.update(detlPk, xfactcutpaydetl);
        } catch (Exception e) {
            Debug.debug(e);
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }

    }


    /*
   ���е�����֧��ƽ̨�ۿ��
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
                log.info("����ϸ�ʵ���¼");
                rtn = 0;
            }

        } catch (Exception e) {
            Debug.debug(e);
            throw new Exception();
        }

        return rtn;

    }

    /*
    200910  zhanrui  ���н���ǩԼ���۴���
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
                log.info("����ϸ�ʵ���¼");
                rtn = 0;
            }

        } catch (Exception e) {
            Debug.debug(e);
            throw new Exception();
        }

        return rtn;

    }


    /*
    ���н�������ֱ���ۿ��
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
                log.info("����ϸ�ʵ���¼");
                rtn = 0;
            }

        } catch (Exception e) {
            Debug.debug(e);
            throw new Exception();
        }

        return rtn;

    }


    /*
    ����������ۿ�ƽ̨�ۿ��
    ���سɹ��������Ӳ��ӿڳɹ���������ҵ����ɹ����ļ�¼��
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
                    if (connect2bill99(xfactcutpaydetl) == 0) {//����ۿ�ɹ�
//                        xfactcutpaydetl.setBillstatus(XFBillStatus.BILLSTATUS_CHARGEDOFF);
//                        xfactcutpaydetl.setBillstatus(XFBillStatus.BILLSTATUS_SEND_SUCCESS);
//                        detlDao.update(detlPk, xfactcutpaydetl);
                        iConnectSuccessCount++;
                    }
                } else if (xfactcutpaydetl.getPaybackbankid().equals(XFBankCode.BANKCODE_ALIPAY)) {
                    if (connect2alipay(xfactcutpaydetl) == 0) {//����ۿ�ɹ�
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
    200910 zhanrui  �����û���ѡ��ʼ���н���ǩԼ���۴���
    ���سɹ��������Ӳ��ӿڳɹ���������ҵ����ɹ����ļ�¼��
     */
    private int dealCCB4SignCutpay(DatabaseConnection conn, Xfactcutpaydetl[] xfactcutpaydetls, ErrorMessages msgs) {

//        XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();
        Xfactcutpaydetl xfactcutpaydetl = null;

        int iConnectSuccessCount = 0;

        CCBWLPTManager ccbmanager = new CCBWLPTManager();

//        try {
//            ccbmanager.setConnection();
//        } catch (IOException e) {
//            msgs.add("���������ӳ�ʱ��");
//            throw new RuntimeException(e);
//        }

        BillsManager bm = new BillsManager(inputdate);

        try {

            for (int i = 0; i < xfactcutpaydetls.length; i++) {
                xfactcutpaydetl = xfactcutpaydetls[i];
                if (xfactcutpaydetl.getPaybackbankid().equals(XFBankCode.BANKCODE_CCB)) {
                    StringBuffer returnCode = new StringBuffer("������:");
                    StringBuffer returnMsg = new StringBuffer(" ������Ϣ:");

//                    if (ccbmanager.processCCBWLPT(xfactcutpaydetl, returnCode,returnMsg) == 0) {//����ۿ�ɹ�
                    if (ccbmanager.process(xfactcutpaydetl, returnCode, returnMsg) == 0) {//����ۿ�ɹ�
                        String message = returnCode.append(returnMsg).toString();
                        //�����ݱ��м�¼������
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
                    } else {  //��ʱ
                        log.error("���д��۴���ʱ��" + xfactcutpaydetl.getJournalno() + xfactcutpaydetl.getClientname());
//                        setCutpayDetlStatusAndLog(xfactcutpaydetl,XFBillStatus.BILLSTATUS_CUTPAY_FAILED,"ͨѶ���ӳ�ʱ���뷢���ѯ���ײ�ѯ���д�������");
                        //�ʵ�״̬���䣬���ڷ����ѯ����.
                        setCutpayDetlLog(xfactcutpaydetl, "ͨѶ���ӳ�ʱ���뷢���ѯ���ײ�ѯ���д�������");
                        //��ʱ������ѭ��
                        break;
                    }
                } else {
                    //�������ش���
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
    ���ɽ�������ֱ������ͨѶ��
    ���سɹ�����ļ�¼��
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
                data = data.append(amt + "|"); //���
                data = data.append("|"); //��ϸ��ע��һ��Ϊ��
                data = data.append(xfactcutpaydetls[i].getPaybackact().trim() + "|"); //�ʺ�
                data = data.append(xfactcutpaydetls[i].getPaybackactname().trim() + "|"); //����
//                data = data.append(xfactcutpaydetls[i].getClientid() + "|"); //֤����
                data = data.append("|"); //֤����

                totalamt = totalamt.add(xfactcutpaydetls[i].getPaybackamt());

                //��д��ϸ�ۿ��¼��״̬
                ps.setString(1, txjournalno);
                ps.setString(2, xfactcutpaydetls[i].getJournalno());
                ps.executeUpdate();

                count++;

                //���ư���С
//                if (data.length() > 28000) {
                if (count % 130 == 0 || data.length() > 28000) {
                    //insert  XFIFBANKDETL ��

                    bankdetl.setJournalno(txjournalno);
                    bankdetl.setBizseqno(bizseqno);

                    bankdetl.setTxndate(inputdate);
                    bankdetl.setTxntype("BAW");
                    bankdetl.setTotalamt(totalamt);  //�ܽ��
//                    bankdetl.setTotalcount(BigDecimal.valueOf(detlslength));  //�ܱ���
                    bankdetl.setTotalcount(BigDecimal.valueOf(count));  //�ܱ���
                    bankdetl.setCurrcount(BigDecimal.valueOf(count));   //��������
/*
                    if (count == detlslength) {    //û�к�����
                        bankdetl.setMultiflag("0");
                    } else {
                        bankdetl.setMultiflag("1");
                    }
*/
                    bankdetl.setMultiflag("0");         //û�к�����

                    bankdetl.setTransferact("801000003012011001    "); //����ת���ʻ�
                    bankdetl.setFailamt(BigDecimal.valueOf(0));
                    bankdetl.setFailcount(BigDecimal.valueOf(0));
                    bankdetl.setStartdate(new Date());
                    bankdetl.setStatus(XFWithHoldStatus.SEND_PENDING); //������

                    //�����Ŵ�ϵͳ��־��1   ����ϵͳ��־ 2
                    bankdetl.setSystemtype("1");

                    bankdetl.setBankid(XFBankCode.BANKCODE_CCB);

//                bankdetl.setOperatorid();
                    bankdetl.setOperatedate(new Date());

                    //���ս��в��Ի���Ҫ�󣬴���Ϊ10000001  12λ�����Ҳ��ո�
//                    bankdetl.setUsage("10000001    ");
                    //���ս�����������Ҫ�󣬴���Ϊ99999999  12λ�����Ҳ��ո�
                    bankdetl.setUsage("99999999    ");

                    bankdetl.setLog("��" + new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date()) + " �������ɡ�");

                    //�������ݴ��
                    bankdetl.setData(data.toString());

                    //���ݿ�д��
                    bankdetlDao.insert(bankdetl);

                    //���ñ���
                    data = new StringBuffer();
                    totalamt = new BigDecimal(0);
                    count = 0;
                    maxno++;
                    maxBizseqno++;
                    txjournalno = inputdate + StringUtils.leftPad(Integer.toString(maxno + 1), 8, "0");
                    bizseqno = "XF" + inputdate + StringUtils.leftPad(Integer.toString(maxBizseqno + 1), 8, "0");
                }

            }

            //insert  XFIFBANKDETL ��
            bankdetl.setJournalno(txjournalno);
            bankdetl.setBizseqno(bizseqno);

            bankdetl.setTxndate(inputdate);
            bankdetl.setTxntype("BAW");
            bankdetl.setTotalamt(totalamt);  //�ܽ��
//            bankdetl.setTotalcount(BigDecimal.valueOf(detlslength));  //�ܱ���
            bankdetl.setTotalcount(BigDecimal.valueOf(count));  //�ܱ���
            bankdetl.setCurrcount(BigDecimal.valueOf(count));   //��������
            bankdetl.setMultiflag("0");         //û�к�����

            bankdetl.setTransferact("801000003012011001    "); //����ת���ʻ�
            bankdetl.setFailamt(BigDecimal.valueOf(0));
            bankdetl.setFailcount(BigDecimal.valueOf(0));
            bankdetl.setStartdate(new Date());
            bankdetl.setStatus(XFWithHoldStatus.SEND_PENDING); //������
            //�����Ŵ�ϵͳ��־��1   ����ϵͳ��־ 2
            bankdetl.setSystemtype("1");
            bankdetl.setBankid(XFBankCode.BANKCODE_CCB);
//                bankdetl.setOperatorid();
            bankdetl.setOperatedate(new Date());

            //���ս��в��Ի���Ҫ�󣬴���Ϊ10000001  12λ�����Ҳ��ո�
//            bankdetl.setUsage("10000001    ");
            //���ս�����������Ҫ�󣬴���Ϊ99999999  12λ�����Ҳ��ո�
            bankdetl.setUsage("99999999    ");

            bankdetl.setLog("��" + new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date()) + " �������ɡ�");

            //�������ݴ��
            bankdetl.setData(data.toString());
            //ͨѶ��д��
            bankdetlDao.insert(bankdetl);

            return i;
        } catch (Exception e) {
            Debug.debug(e);
            System.out.println("====����ֱ����������DEBUG====");
            throw new Exception(e);
        }
    }


    private String getAlipayUrl_old(String journalno, String amount, String customer_code) {
        String itemUrl = "";
        String paygateway = "https://www.alipay.com/cooperate/gateway.do?"; //'֧���ӿڣ����øģ�
//        String paygateway = "http://www.alipay.com/cooperate/gateway.do?"; //'֧���ӿڣ����øģ�
        String t4 = "images/alipay_bwrx.gif"; //'֧������ťͼƬ
        String t5 = "�Ƽ�ʹ��֧��������"; //'��ť��ͣ˵��
        String input_charset = AlipayConfig.CharSet; //ҳ����루���øģ�
        String service = "cae_charge_agent";//��������---���ۣ����øģ�

        String partner = AlipayConfig.partnerID; //partner�������ID(����)
        String key = AlipayConfig.key; //partner�˻���Ӧ��֧������ȫУ����(����)
        String type_code = "TEST100011000101"; //type_code�������ID(����),������������룬��̨��ͨ�����Դ������õ������磺100410000192

        String return_url = "http://218.58.70.181/test/alipay/alipay_return.jsp";
        //�������˻���Ϣ�����д����վ�Լ���
        //*****************************************************************
        String sign_type = "MD5"; //'ǩ����ʽ�����øģ�
        String subject = "commodityname"; //subject ��Ʒ���ơ���վ������

        SimpleDateFormat sFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sFmt.format(new Date());


        //String gmt_out_order_create = "2009-05-14 15:15:15"; //�̻���������ʱ��
        String gmt_out_order_create = dateStr; //�̻���������ʱ��

        String out_order_no = journalno; //�̻���վ������Ҳ�����ⲿ�����ţ���ͨ���ͻ���վ����֧�������������ظ���
        //String amount = amount; //�����ܼ�	0.01��50000.00

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
    ʹ��HTTPCLIENT��֧������������
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
//        //����HttpClient��ʵ��
//        Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory(), 443);
//        Protocol.registerProtocol("https", myhttps);
//        HttpClient httpClient = new HttpClient();
//        //����GET������ʵ��
//        // TODO:0.01
//
//        //Double amt = xfcutpaydetl.getPaybackamt().doubleValue() * 100;
//        //String orderAmount = amt.toString();
//
//        String orderAmount = xfcutpaydetl.getPaybackamt().toString();
//
//        GetMethod getMethod = new GetMethod(am.getAlipayUrl(xfcutpaydetl.getJournalno(), orderAmount, xfcutpaydetl.getCustomerCode()));
//        //ʹ��ϵͳ�ṩ��Ĭ�ϵĻָ�����,�ڷ����쳣ʱ���Զ�����3�Σ�������Ҳ�������ó��Զ���Ļָ�����
//        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
//
//        try {
//            //ִ��getMethod
//            int statusCode = httpClient.executeMethod(getMethod);
//            if (statusCode != HttpStatus.SC_OK) {
//                System.out.println("֧�����ۿ�ʧ�� Method failed: " + getMethod.getStatusLine());
//                //TODO:����־�м�¼������
//                return -1;
//            }
//            //��ȡ����
//            byte[] responseBody = getMethod.getResponseBody();
//            //��������
//            String body = new String(responseBody);
//            System.out.println(body);
//            log.info("===֧����������" + "[" + xfcutpaydetl.getJournalno() + "  " + xfcutpaydetl.getClientname() + ":]" + body);
//
//            if (body != null && (body.indexOf("<is_success>T</is_success>") != -1)) {
//                return 0;
//            } else {
//                return -1;
//            }
//        } catch (HttpException e) {
//            // �����������쳣��������Э�鲻�Ի��߷��ص�����������
//            System.out.println("Please check your provided http address!");
//            e.printStackTrace();
//            log.error("===����HttpException" + "Please check your provided http address!", e);
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            // ���������쳣
//            e.printStackTrace();
//            log.error("===����IOException", e);
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            // �����쳣
//            e.printStackTrace();
//            log.error("===����Exception", e);
//            throw new RuntimeException(e);
//        } finally {
//            //�ͷ�����
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
            log.error("===��֧�������ӳ��ִ���", e);
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
                    log.info("֧�������۷��ͳɹ�" + journalno + " " + xfcutpaydetl.getClientname());
                    node = doc.selectSingleNode("//alipay/response/order/out_order_no");
                    if (journalno.equals(node.getText())) {  //�˶���ˮ��
                        node = doc.selectSingleNode("//alipay/response/order/status");
                        if ("S".equals(node.getText())) {
                            log.info("֧�������ۿ���ɹ�" + journalno + " " + xfcutpaydetl.getClientname());
                            //�޸Ľ���״̬
                            BillsManager bm = new BillsManager(inputdate);
                            bm.setCutpayDetlPaidupSuccess(journalno);
                            setCutpayDetlStatusAndLog(xfcutpaydetl, XFBillStatus.BILLSTATUS_CUTPAY_SUCCESS, responseBody);
                            rtn = 0;
                        } else {
                            log.info("֧�������ۿ���ʧ��!" + journalno + " " + xfcutpaydetl.getClientname());
                            setCutpayDetlStatusAndLog(xfcutpaydetl, XFBillStatus.BILLSTATUS_CUTPAY_FAILED, responseBody);
                            rtn = -1;
                        }
                    } else {
                        log.info("֧�������ۿ���ʧ��!" + journalno + " " + xfcutpaydetl.getClientname() + responseBody);
                        setCutpayDetlLog(xfcutpaydetl, "��ˮ�Ų�����" + journalno + " " + responseBody);
                        rtn = -1;
                    }
                } else {
                    log.info("֧�������۷��ͳɹ�" + journalno + " " + xfcutpaydetl.getClientname());
                    log.info("֧�������ۿۿ�ʧ��" + journalno + " " + xfcutpaydetl.getClientname());
                    setCutpayDetlStatusAndLog(xfcutpaydetl, XFBillStatus.BILLSTATUS_CUTPAY_FAILED, responseBody);
//                    setCutpayDetlLog(xfcutpaydetl, "֧�������۱��ķ��ͳɹ������յ��ۿ��ʧ����Ϣ��"+ " " +responseBody);
                    rtn = -1;
                }
                return rtn;
            } else {
                //TODO: log to db
                log.info("===֧����������:ʧ��" + "[" + xfcutpaydetl.getJournalno() + "  " + xfcutpaydetl.getClientname() + ":]" + responseBody);
                //����ʧ�ܲ��޸�״̬�������ظ�����
                //��һ�ְ취���ý���ʧ�ܱ�־�����������ɿۿ��¼

                // setCutpayDetlStatusAndLog(xfcutpaydetl, XFBillStatus.BILLSTATUS_CUTPAY_FAILED, responseBody);
                setCutpayDetlLog(xfcutpaydetl, "����ʧ�ܣ�δ�յ�֧������Ӧ��Ϣ�������·��ʹ�������");
                return -1;
            }


        } catch (Exception e) {
            log.error("===��֧�������ӳ��ִ���", e);
            throw new RuntimeException();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }

    }


    /*
   ��Ǯ���۷�����
   ÿ�ʴ��۶����������ӣ����Ż�
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
            log.error("===���Ǯ���ӳ��ִ���", e);
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
            // ������������ֵ
            Bill99Manager bm = new Bill99Manager();
            List<NameValuePair> data = bm.getPostValues(xfcutpaydetl);
            httppost.setEntity(new UrlEncodedFormEntity(data, HTTP.UTF_8));

            log.debug("executing request" + httppost.getRequestLine());

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httppost, responseHandler);
            log.debug("response body:" + responseBody);

            if (responseBody != null && (responseBody.indexOf("success") != -1)) {
                log.info("===��Ǯ������:�ɹ�" + "[" + xfcutpaydetl.getJournalno() + "  " + xfcutpaydetl.getClientname() + ":]" + responseBody);
//                BillsManager billmgr = new BillsManager(inputdate);
//                billmgr.setCutpayDetlPaidupSuccess(xfcutpaydetl.getJournalno());
                //�˴��ɹ������������ݱ���Ǯ�������գ�ҵ�����Ƿ�ɹ�����Ǯ����֪ͨ��
                setCutpayDetlStatusAndLog(xfcutpaydetl, XFBillStatus.BILLSTATUS_SEND_SUCCESS, responseBody);
                return 0;
            } else {
                //TODO: log to db
                log.info("===��Ǯ������:ʧ��" + "[" + xfcutpaydetl.getJournalno() + "  " + xfcutpaydetl.getClientname() + ":]" + responseBody);
                //����ʧ�ܲ��޸�״̬�������ظ�����
                //��һ�ְ취���ý���ʧ�ܱ�־�����������ɿۿ��¼

                // setCutpayDetlStatusAndLog(xfcutpaydetl, XFBillStatus.BILLSTATUS_CUTPAY_FAILED, responseBody);
                setCutpayDetlLog(xfcutpaydetl, responseBody);
                return -1;
            }


        } catch (Exception e) {
            log.error("===���Ǯ���ӳ��ִ���", e);
            throw new RuntimeException();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }


/*
    private int connectToAlipay() {
        //����HttpClient��ʵ��
        HttpClient httpClient = new HttpClient();
        //����GET������ʵ��
        GetMethod getMethod = new GetMethod("http://www.ibm.com");
        //ʹ��ϵͳ�ṩ��Ĭ�ϵĻָ�����
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
        try {
            //ִ��getMethod
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: "
                        + getMethod.getStatusLine());
            }
            //��ȡ����
            byte[] responseBody = getMethod.getResponseBody();
            //��������
            System.out.println(new String(responseBody));
        } catch (HttpException e) {
            //�����������쳣��������Э�鲻�Ի��߷��ص�����������
            System.out.println("Please check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
            //���������쳣
            e.printStackTrace();
        } finally {
            //�ͷ�����
            getMethod.releaseConnection();
        }
    }
}
*/

    /*
    ���������ʵ�����
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

            log.info("���ڲ�ѯSQL:" + sql);


            Xfactcutpaymain[] xfactcutpaymain = mainDao.findByDynamicWhere(sql, null);
            if ((count = xfactcutpaymain.length) == 0) {
                return count;  //�����ڼ�¼     ����-2��
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

            log.info("���ڲ�ѯSQL:" + sql);


            Xfactcutpaymain[] xfactcutpaymain = mainDao.findByDynamicWhere(sql, null);
            if (xfactcutpaymain.length == 0) {
                return 0;  //�����ڼ�¼     ����-2��
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