package zt.cms.xf.fd.account;

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
//import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.math.BigDecimal;

import zt.platform.form.control.SessionContext;
import zt.platform.form.control.FormActions;
import zt.platform.form.util.event.*;
import zt.platform.utils.Debug;
import zt.cms.xf.common.dao.*;
import zt.cms.xf.common.factory.*;
import zt.cms.xf.common.dto.*;
import zt.cms.xf.common.constant.*;
import zt.cms.xf.common.exceptions.XfactcutpaydetlDaoException;
import zt.cms.xf.common.exceptions.FdcutpaydetlDaoException;
import zt.cms.xf.account.SBSManager;

import zt.cmsi.mydb.MyDB;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


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
            instance.getFormBean().getElement("CHECKFDDATABUTTON").setComponetTp(15);
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

        //����ֱ������ͨѶ��¼����
        if (button.equals("GENERATEPKGBUTTON")) {
            int iSuccessCount = 0;
            try {
                iSuccessCount = doGeneratePkgButton(ctx, conn, instance, msgs);
                if (iSuccessCount == 0) {
                    ctx.setRequestAtrribute("msg", "�����ڷ��ϴ�������������!");
                } else {
                    ctx.setRequestAtrribute("msg", "����ֱ�����ۼ�¼������ɣ������¼��Ϊ��" + iSuccessCount + "����");
                }
            } catch (Exception e) {
                Debug.debug(e);
                ctx.setRequestAtrribute("msg", "����ֱ�����۴���δ�ɹ���ɣ����ѯ��");
                return -1;
            } finally {
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
                ctx.removeAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
            }
        }

        //����DBLINK ��ȡ����ϵͳ����  
        if (button.equals("GETFDDATABUTTON") || button.equals("GETFDPREDATABUTTON")) {

            String regioncd = ctx.getParameter("REGION");
            String bankcd = null;

            if (regioncd == null) {
                msgs.add("δѡ���������ѡ�������ĵ������ơ�");
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
                    ctx.setRequestAtrribute("msg", "ϵͳ�д���δ������ɵĴ������ݣ����ѯ��");
                    return -1;
                }

                //��������ʺŶ�Ӧ���
                if (checkFDSystemData(conn, msgs, button,regioncd,bankcd) == -1) {
                    ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                    return -1;
                }

                msgs.clear();
                iSuccessCount = getFDSystemData(conn, msgs, button, regioncd, bankcd, "fdcutpaydetl");
                ctx.setRequestAtrribute("msg", msgs.getAllMessages());
            } catch (Exception e) {
                Debug.debug(e);
                ctx.setRequestAtrribute("msg", "��ȡ����ϵͳ����δ�ɹ���ɣ����ѯ��");
                return -1;
            } finally {
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            }
        }

        //����DBLINK ��鷿��ϵͳ����
        if (button.equals("CHECKFDDATABUTTON") || button.equals("CHECKFDPREDATABUTTON")) {

            String regioncd = ctx.getParameter("REGION");
            String bankcd = null;

            if (regioncd == null) {
                msgs.add("δѡ���������ѡ�������ĵ������ơ�");
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
                    ctx.setRequestAtrribute("msg", "ϵͳ�д���δ������ɵĴ������ݣ����ѯ��");
                    return -1;
                }

                //��������ʺŶ�Ӧ���
                int recordcount = checkFDSystemData(conn, msgs, button, regioncd, bankcd);
                if (recordcount == -1) {
                    ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                    return -1;
                } else {
                    msgs.add("<br><br>�������ݼ��������ɡ�");
                    ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                }

                if (recordcount > 0) {
                    msgs.clear();
                    //��ȡ����ϵͳ���ݵ���ʱ����ͳ�����ݡ�
                    deleteTableData_CutPatDetl(conn, msgs);
                    iSuccessCount = getFDSystemData(conn, msgs, button, regioncd, bankcd, "fdcutpaydetltemp");
                    ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                }
            } catch (Exception e) {
                Debug.debug(e);
                ctx.setRequestAtrribute("msg", "��ȡ����ϵͳ����δ�ɹ���ɣ����ѯ��");
                return -1;
            } finally {
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            }
        }


        //����ϵͳSBS���ʴ���
        if (button.equals("FDACCOUNTBUTTON")) {
            int iSuccessCount = 0;
            try {
                iSuccessCount = doFDAccountButton(msgs);
                if (iSuccessCount == -1) {
                    msgs.add("<br>SBSϵͳ������ͨ�����ӳ�ʱ��");
                } else {
                    msgs.add("<br>�ɹ����ʵļ�¼��Ϊ�� " + iSuccessCount + " ����");
//                    iSuccessCount = doWriteBackButton(conn, msgs);
//                    if (iSuccessCount == -1) {
//                        msgs.add("<br>--��д����ϵͳʱ�������⣡");
//                    } else {
//                        msgs.add("<br>��д����ϵͳ��ɣ������¼��Ϊ�� " + iSuccessCount + " ����");
//                    }
                }
            } catch (Exception e) {
                Debug.debug(e);
                msgs.add("SBS����ϵͳ���ʴ���δ�ɹ���ɣ����ѯԭ��");
                return -1;
            } finally {
                ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            }
        }

        //�����Ŵ�ϵͳSBS���ʴ���
        if (button.equals("XFACCOUNTBUTTON")) {
            int iSuccessCount = 0;
            try {
                iSuccessCount = doXFAccountButton(msgs);
                if (iSuccessCount == -1) {
                    msgs.add("<br>SBSϵͳ������ͨ�����ӳ�ʱ��");
                } else {
                    msgs.add("<br>�ɹ����ʵļ�¼��Ϊ�� " + iSuccessCount + " ����");
                }
            } catch (Exception e) {
                Debug.debug(e);
                msgs.add("SBS����ϵͳ���ʴ���δ�ɹ���ɣ����ѯԭ��");
                return -1;
            } finally {
                ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            }
        }


        //����ϵͳ��д����
        if (button.equals("FDWRITEBACKBUTTON")) {
            int iSuccessCount = 0;
            try {
                iSuccessCount = doWriteBackButton(conn, msgs);
                if (iSuccessCount == -1) {
                    msgs.add("<br>--��д����ϵͳʱ�������⣡");
                } else {
                    msgs.add("<br>��д����ϵͳ��ɣ������¼��Ϊ�� " + iSuccessCount + " ����");
                }
            } catch (Exception e) {
                Debug.debug(e);
                msgs.add("<br>--����ϵͳ��д����δ�ɹ���ɣ����ѯԭ��");
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
   ���FDCUTPAYDETL��������
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
                msgs.add("����δ����ķ������ۼ�¼!");
                return -1;
            }
        } catch (Exception e) {
            msgs.add("�����쳣��" + e.getMessage());
            Debug.debug(e);
            throw new Exception(e);
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
        return 0;
    }

    /*
    ������ʱ������
     */
    private void deleteTableData_CutPatDetl(DatabaseConnection conn, ErrorMessages msgs) throws Exception {

        MyDB.getInstance().addDBConn(conn);
        String sql = "delete from fdcutpaydetltemp";

        try {
            conn.executeUpdate(sql);
        } catch (Exception e) {
            msgs.add("�����쳣��" + e.getMessage());
            Debug.debug(e);
            throw new Exception(e);
        }
    }

    /*
   ��ѯ����ϵͳ��  ���ɱ�����ʱ����
   �������д��۵ļ�¼
    */
    private int getFDSystemData(DatabaseConnection conn, ErrorMessages msgs, String button,
                                String regioncd, String bankcd, String tablename) throws Exception {

        String regionid = null;
        String regionid1 = null;
        String bankid = null;

        if ("0531".equals(regioncd)) {
            regionid = "GQ"; //����
            regionid1 = "GSQ"; //����
        } else if ("023".equals(regioncd)) {
            regionid = "GC"; //����
            regionid1 = "GSC"; //����
        } else if ("0351".equals(regioncd)) {
            regionid = "GT"; //̫ԭ
            regionid1 = "GST"; //̫ԭ
        } else {
            if (!"0532".equals(regioncd)) {
                throw new Exception("�����������!");
            }
        }

        if (XFBankCode.BANKCODE_CCB.equals(bankcd)) {
            bankid = "801000026101041001";
        } else if (XFBankCode.BANKCODE_BOC.equals(bankcd)) {
            bankid = "801000026701041001";
        } else {
            throw new Exception("���д������!");
        }

        String wherecondition = null;
        if ("0532".equals(regioncd)) {
            wherecondition = " and (b.gthtb_tyckzh = '" + bankid + "')";
        } else {//20090910 ����������Ŀǰ���ϡ����졢̫ԭ Ĭ��Ϊ���д��ۣ�
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
                        "       '0' as preflag," +       //���������־
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
                        "       '��ǰ����' as remark," +
                        "       '1' as preflag," +                                     //��ǰ�����־
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

            logger.info("[��ȡ�����ۿ��¼SQL���:]" + sql);
            rs = conn.executeQuery(sql);

            if (rs.next()) {
//                rtn = rsq.getInt(0);
            }

            sql = "select count(*),sum(gthtjh_jhje) from  " + tablename + " where billstatus = " + FDBillStatus.SEND_PENDING;
            rs = conn.executeQuery(sql);
            if (rs.next()) {
                rtn = rs.getInt(0);
                BigDecimal totalamt = new BigDecimal(rs.getDouble(1)).setScale(2, BigDecimal.ROUND_HALF_UP);
                msgs.add("<br>����ϵͳ��¼��Ϊ��" + rtn + "����");
                msgs.add("<br>����ϵͳ�ۿ��ܽ��Ϊ��" + new DecimalFormat("##,###,###,###,##0.00").format(totalamt));
            }
        } catch (Exception e) {
            msgs.add("�����쳣��" + e.getMessage());
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


    /*
    ����
   ��ѯ����ϵͳ��  ���ɱ�����ʱ����
   �������д��۵ļ�¼
    */
    private int getFDSystemDataForCCB(DatabaseConnection conn, ErrorMessages msgs, String button) throws Exception {

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

            String queryhead = " insert into fdcutpaydetl  (select '" + inputdate + "' || lpad(rownum +" + maxno +
                    ",7,'0') as seqno,";

            if (button.equals("GETFDDATABUTTON")) {
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
                        "       '0' as preflag," +       //���������־
                        "       t.GTHTJH_HTNM," +
                        "       t.GTHTJH_JHXH," +
                        "       '0532' as regioncd," +
                        "       '105' as bankcd," +
                        "       null as journalno" +
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
                        "           and b.gthtb_tyckzh = '801000026101041001'" +
                        "           and b.gthtb_zhbh = d.actno" +
                        "           order by c.xdkhzd_khbh) t )";
            } else {
                sql = queryhead + " t1.XDKHZD_KHBH, " +
                        "       t1.XDKHZD_KHMC, " +
                        "       t1.GTHTJH_HTBH, " +
                        "       t1.GTHTJH_DATE, " +
                        "       t1.GTHTJH_LL, " +
//                        "       round(t1.xdfhkx_je + t2.xdfhkx_je,2) as GTHTJH_JHJE, " +
                        "       round(t1.xdfhkx_je + decode(t2.xdfhkx_je, null, 0), 2) as GTHTJH_JHJE, " +
                        "       round(t1.xdfhkx_je, 2) as GTHTJH_BJJE, " +
//                        "       round(t2.xdfhkx_je, 2) as GTHTJH_LXJE, " +
                        "       round(decode(t2.xdfhkx_je, null, 0), 2) as GTHTJH_LXJE, " +
                        "       t1.GTHTB_ZHBH, " +
                        "       t1.cutpayactno, " +
                        "       '0' as BILLSTATUS, " +
                        "        sysdate as createtime," +
                        "       ' ' as failreason," +
                        "       '��ǰ����' as remark," +
                        "       '1' as preflag," +                                     //��ǰ�����־
                        "       t1.GTHTJH_HTNM," +
                        "       t1.GTHTJH_JHXH," +
                        "       '0532' as regioncd," +
                        "       '105' as bankcd," +
                        "       null as journalno" +
                        "  from (select c.xdkhzd_khbh, " +
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
                        "           and b.gthtb_zhbh = d.actno) t1, " +
                        "       (select a.xdfhkx_htbh as gthtjh_htbh, " +
                        "               a.xdfhkx_ywzl, " +
                        "               round(a.xdfhkx_je, 2) xdfhkx_je " +
                        "          from xdfhkx@haier_shengchan a, " +
                        "               gthtb@haier_shengchan  b " +
                        "         where a.xdfhkx_htbh = b.gthtb_htbh " +
                        "           and b.gthtb_htzt <> '5' " +
                        "           and a.xdfhkx_ywrq = " + inputdate +
                        "           and b.gthtb_htlb = '03' " +
                        "           and (a.xdfhkx_ywzl = '7')) t2 " +
                        " where t1.gthtjh_htbh = t2.gthtjh_htbh(+))";
            }

            logger.info("[��ȡ�����ۿ��¼SQL���:]" + sql);
            rs = conn.executeQuery(sql);

            if (rs.next()) {
//                rtn = rsq.getInt(0);
            }

            sql = "select count(*),sum(gthtjh_jhje) from fdcutpaydetl where billstatus = " + FDBillStatus.SEND_PENDING;
            rs = conn.executeQuery(sql);
            if (rs.next()) {
                rtn = rs.getInt(0);
//                Double totalamt = rs.getDouble(1);
                BigDecimal totalamt = new BigDecimal(rs.getDouble(1)).setScale(2, BigDecimal.ROUND_HALF_UP);
                msgs.add("<br>��ȡ����ϵͳ��¼��Ϊ��" + rtn + "����");
                msgs.add("<br>��ȡ����ϵͳ�ۿ��ܽ��Ϊ��" + new DecimalFormat("##,###,###,###,##0.00").format(totalamt));
            }
        } catch (Exception e) {
            msgs.add("�����쳣��" + e.getMessage());
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

    /*
   ����
   ��ѯ����ϵͳ��,���ɱ�����ʱ����
   ���������������
    */
    private int getFDSystemDataForOtherBank(DatabaseConnection conn, ErrorMessages msgs, String button,
                                            String regioncd, String bankcd) throws Exception {

        String regionid = null;
        String bankid = null;

        if ("0531".equals(regioncd)) {
            regionid = "GQ"; //����
        } else if ("023".equals(regioncd)) {
            regionid = "GC"; //����
        } else {
            throw new Exception("�����������!");
        }

        if (XFBankCode.BANKCODE_BOC.equals(regioncd)) {
            bankid = "801000026701041001";
        } else {
            throw new Exception("���д������!");
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

            String queryhead = " insert into fdcutpaydetl  (select '" + inputdate + "' || lpad(rownum +" + maxno +
                    ",7,'0') as seqno,";

            if (button.equals("GETFDDATABUTTON")) {
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
                        "       ' ' as cutpayactno," +
                        "       '0' as BILLSTATUS," +
                        "        sysdate as createtime," +
                        "       ' ' as failreason," +
                        "       ' ' as remark," +
                        "       '0' as preflag," +       //���������־
                        "       t.GTHTJH_HTNM," +
                        "       t.GTHTJH_JHXH," +

                        " '" + regioncd + "' " + "  as regioncd," +
                        " '" + bankcd + "' " + "  as bankcd," +

                        "       null as journalno" +
                        "  from (select c.xdkhzd_khbh," +
                        "               c.xdkhzd_khmc," +
                        "               a.gthtjh_htbh," +
                        "               a.gthtjh_date," +
                        "               a.gthtjh_ll," +
                        "               a.gthtjh_jhje," +
                        "               a.gthtjh_bjje," +
                        "               a.gthtjh_lxje," +
                        "               to_char(b.gthtb_zhbh) gthtb_zhbh," +
                        "               a.GTHTJH_HTNM," +
                        "               a.GTHTJH_JHXH" +
                        "          from gthtjh@haier_shengchan a," +
                        "               gthtb@haier_shengchan  b," +
                        "               xdkhzd@haier_shengchan c" +
                        "         where a.gthtjh_htbh = b.gthtb_htbh" +
                        "           and b.gthtb_dwbh = c.xdkhzd_khbh" +
                        "           and b.gthtb_htzt <> '5'" +
                        "           and a.gthtjh_date like '" + inputdate.substring(0, 6) + "%'" +
                        "           and a.gthtjh_hkbz <> '1'" +
                        "           and ( b.gthtb_tyckzh = '" + bankid + "'" + " and c.xdkhzd_khbh like '" + regionid + "%')" +
                        "           order by c.xdkhzd_khbh) t )";
            } else {
                sql = queryhead + " t1.XDKHZD_KHBH, " +
                        "       t1.XDKHZD_KHMC, " +
                        "       t1.GTHTJH_HTBH, " +
                        "       t1.GTHTJH_DATE, " +
                        "       t1.GTHTJH_LL, " +
                        "       round(t1.xdfhkx_je + t2.xdfhkx_je,2) as GTHTJH_JHJE, " +
                        "       round(t1.xdfhkx_je, 2) as GTHTJH_BJJE, " +
                        "       round(t2.xdfhkx_je, 2) as GTHTJH_LXJE, " +
                        "       t1.GTHTB_ZHBH, " +
                        "       ' ' as  cutpayactno, " +
                        "       '0' as BILLSTATUS, " +
                        "        sysdate as createtime," +
                        "       ' ' as failreason," +
                        "       '��ǰ����' as remark," +
                        "       '1' as preflag," +                                     //��ǰ�����־
                        "       t1.GTHTJH_HTNM," +
                        "       t1.GTHTJH_JHXH," +
                        " '" + regioncd + "' " + "  as regioncd," +
                        " '" + bankcd + "' " + "  as bankcd," +
                        "       null as journalno" +
                        "  from (select c.xdkhzd_khbh, " +
                        "               c.xdkhzd_khmc, " +
                        "               a.xdfhkx_htbh as gthtjh_htbh, " +
                        "               a.xdfhkx_ywrq as gthtjh_date, " +
                        "               a.xdfhkx_ll as gthtjh_ll, " +
                        "               a.xdfhkx_htnm as gthtjh_htnm, " +
                        "               a.xdfhkx_jhxh as gthtjh_jhxh, " +
                        "               a.xdfhkx_ywzl, " +
                        "               round(a.xdfhkx_je, 2) xdfhkx_je, " +
                        "               to_char(b.gthtb_zhbh) gthtb_zhbh " +
                        "          from xdfhkx@haier_shengchan a, " +
                        "               gthtb@haier_shengchan  b, " +
                        "               xdkhzd@haier_shengchan c " +
                        "         where a.xdfhkx_htbh = b.gthtb_htbh " +
                        "           and b.gthtb_dwbh = c.xdkhzd_khbh " +
                        "           and b.gthtb_htzt <> '5' " +
                        "           and a.xdfhkx_ywrq = " + inputdate +
                        "           and b.gthtb_htlb = '03' " +
                        "           and (a.xdfhkx_ywzl = '6') " +
                        "           and ( b.gthtb_tyckzh = '" + bankid + "'" + " and c.xdkhzd_khbh like '" + regionid + "%')" +
                        "           ) t1, " +
                        "       (select a.xdfhkx_htbh as gthtjh_htbh, " +
                        "               a.xdfhkx_ywzl, " +
                        "               round(a.xdfhkx_je, 2) xdfhkx_je " +
                        "          from xdfhkx@haier_shengchan a, " +
                        "               gthtb@haier_shengchan  b " +
                        "         where a.xdfhkx_htbh = b.gthtb_htbh " +
                        "           and b.gthtb_htzt <> '5' " +
                        "           and a.xdfhkx_ywrq = " + inputdate +
                        "           and b.gthtb_htlb = '03' " +
                        "           and (a.xdfhkx_ywzl = '7')) t2 " +
                        " where t1.gthtjh_htbh = t2.gthtjh_htbh)";
            }

            logger.info("[��ȡ�����ۿ��¼SQL���:]" + sql);
            rs = conn.executeQuery(sql);

            if (rs.next()) {
//                rtn = rsq.getInt(0);
            }

            sql = "select count(*),sum(gthtjh_jhje) from fdcutpaydetl where billstatus = " + FDBillStatus.SEND_PENDING;
            rs = conn.executeQuery(sql);
            if (rs.next()) {
                rtn = rs.getInt(0);
//                Double totalamt = rs.getDouble(1);
                BigDecimal totalamt = new BigDecimal(rs.getDouble(1)).setScale(2, BigDecimal.ROUND_HALF_UP);
                msgs.add("<br>��ȡ����ϵͳ��¼��Ϊ��" + rtn + "����");
                msgs.add("<br>��ȡ����ϵͳ�ۿ��ܽ��Ϊ��" + new DecimalFormat("##,###,###,###,##0.00").format(totalamt));
            }
        } catch (Exception e) {
            msgs.add("�����쳣��" + e.getMessage());
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


    /*
    ��鷿��ϵͳ����
     */
    private int checkFDSystemData(DatabaseConnection conn, ErrorMessages msgs, String button,
                                  String regioncd, String bankcd) throws Exception {

        logger.info("[��鷿��ϵͳ����������]");

        String regionid = null;
        String regionid1 = null;
        String bankid = null;

/*    2010/2/2
        if ("0531".equals(regioncd)) {
            regionid = "GQ"; //����
        } else if ("023".equals(regioncd)) {
            regionid = "GC"; //����
        } else if ("0351".equals(regioncd)) {
            regionid = "GT"; //̫ԭ
        } else {
            if (!"0532".equals(regioncd)) {
                throw new Exception("�����������!");
            }
        }

        if (XFBankCode.BANKCODE_CCB.equals(bankcd)) {
            bankid = "801000026101041001";
        } else if (XFBankCode.BANKCODE_BOC.equals(bankcd)) {
            bankid = "801000026701041001";
        } else {
            throw new Exception("���д������!");
        }

        String wherecondition = null;
        if ("0532".equals(regioncd)) {
            wherecondition = " and (b.gthtb_tyckzh = '" + bankid + "')";
        } else {//20090910 ����������Ŀǰ���ϡ����졢̫ԭ Ĭ��Ϊ���д��ۣ�
            wherecondition = " and (b.gthtb_tyckzh = '" + bankid + "'" + " and c.xdkhzd_khbh like '" + regionid + "%')";
        }
*/

        if ("0531".equals(regioncd)) {
            regionid = "GQ"; //����
            regionid1 = "GSQ"; //����
        } else if ("023".equals(regioncd)) {
            regionid = "GC"; //����
            regionid1 = "GSC"; //����
        } else if ("0351".equals(regioncd)) {
            regionid = "GT"; //̫ԭ
            regionid1 = "GST"; //̫ԭ
        } else {
            if (!"0532".equals(regioncd)) {
                throw new Exception("�����������!");
            }
        }

        if (XFBankCode.BANKCODE_CCB.equals(bankcd)) {
            bankid = "801000026101041001";
        } else if (XFBankCode.BANKCODE_BOC.equals(bankcd)) {
            bankid = "801000026701041001";
        } else {
            throw new Exception("���д������!");
        }

        String wherecondition = null;
        if ("0532".equals(regioncd)) {
            wherecondition = " and (b.gthtb_tyckzh = '" + bankid + "')";
        } else {//20090910 ����������Ŀǰ���ϡ����졢̫ԭ Ĭ��Ϊ���д��ۣ�
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
                logger.info("ϵͳ�������");
                throw new Exception("ϵͳ�������");
            }

            logger.info("[��ȡ����ϵͳ�������:]" + sql);
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
                    if (contractno.trim().length() > 10) {  //���� GZ20080606-2�ĺ�ͬ
                        String contractnotemp = contractno.substring(0, 10);
                        actnoinfos = actnodao.findWhereContractnoEquals(contractnotemp);
                        if (actnoinfos != null && actnoinfos.length == 1)
                            continue;
                        msgs.add("<br>===������" + name + "  ��ͬ��ţ�" + contractno + "  �˺�ͬ���������Ŵ�ϵͳ�����ڡ�");
                        failcount++;
                        successflag = -1;
                    } else {
                        msgs.add("<br>===������" + name + " ��ͬ��ţ�" + contractno + " �˺�ͬ���������Ŵ�ϵͳ�в����ڡ�");
                        logger.info("�ڲ��ʺ�δ�ҵ���" + name + " " + contractno + " " + actno);
                        failcount++;
                        successflag = -1;
                    }
                } else {
                    if (!actno.equals(actnoinfo.getActno())) {
                        msgs.add("<br>===������" + name + " ��ͬ��ţ�" + contractno + " �ڲ��ʺŲ�����<br>�Ŵ�ϵͳ�е��ڲ��ʺţ�"
                                + actno + "<br>�����Ŵ��е��ڲ��ʺţ�" +actnoinfo.getActno());
                        failcount++;
                    }
                }

                totalcount++;
            }
            rs.close();
            msgs.add("<br><br>����" + totalcount + " ��������ۿ��¼��");
            msgs.add("<br><br>���й���" + failcount + " ����Ϣ�������ۿ��¼��");

            if (successflag >= 0) {
                successflag = totalcount;
            }
            return successflag;
        } catch (Exception e) {
            msgs.add("�����쳣��" + e.getMessage());
            Debug.debug(e);
            throw new Exception(e);
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }


    /*
   ���н�������ֱ���ۿ��
    */

    private int doGeneratePkgButton(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) throws Exception {

        int rtn = 0;

        try {

            Fdcutpaydetl[] cutpaydetls = null;
            String[] recordnos = (String[]) ctx.getAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
            if (recordnos != null && recordnos.length != 0) {
                cutpaydetls = getCutPayDetlList(conn, recordnos);
            } else {
                String sql = "billstatus = " + FDBillStatus.SEND_PENDING;  //���ύ�Ŀۿ��¼
                FdcutpaydetlDao detlDao = FdcutpaydetlDaoFactory.create();
                cutpaydetls = detlDao.findByDynamicWhere(sql, null);
            }

            if (cutpaydetls != null && cutpaydetls.length > 0) {
                rtn = generateBankWithholdRecord(conn, cutpaydetls);
            } else {
                logger.info("����ϸ�ʵ���¼");
                rtn = 0;
            }

        } catch (Exception e) {
            Debug.debug(e);
            throw new Exception();
        }

        return rtn;

    }

    /*
      �����û�ѡ�еļ�¼����������cutpaydetl��¼��
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
   ���ɽ�������ֱ������ͨѶ��
   ���سɹ�����ļ�¼��
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
                //�����ѡ�еļ�¼��ȷ��Ϊ"�ൺ""����"����
                if (!XFBankCode.BANKCODE_CCB.equals(fdcutpaydetls[i].getBankcd())) {
                    logger.info("���ڷǽ��д�������"+ fdcutpaydetls[i].getXdkhzdKhmc());
                    continue;
                }

                String amt = amtdf.format(fdcutpaydetls[i].getGthtjhJhje());
                data = data.append(amt + "|"); //���
                data = data.append("|"); //��ϸ��ע��һ��Ϊ��
                data = data.append(fdcutpaydetls[i].getCutpayactno().trim() + "|"); //�ʺ�
                data = data.append(fdcutpaydetls[i].getXdkhzdKhmc().trim() + "|"); //����
//                data = data.append(fdcutpaydetls[i].getClientid() + "|"); //֤����
                data = data.append("|"); //֤����

                totalamt = totalamt.add(fdcutpaydetls[i].getGthtjhJhje());

                //��д��ϸ�ۿ��¼��״̬
                ps.setString(1, journalno);
                ps.setString(2, fdcutpaydetls[i].getSeqno());
                ps.executeUpdate();

                count++;

                //���ư���С
//                if (data.length() > 28000) {
                if (count % 130 == 0 || data.length() > 28000) {
                    //insert  XFIFBANKDETL ��

                    bankdetl.setJournalno(journalno);
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
                    bankdetl.setSystemtype("2");

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
                    journalno = inputdate + StringUtils.leftPad(Integer.toString(maxno + 1), 8, "0");
                    bizseqno = "FD" + inputdate + StringUtils.leftPad(Integer.toString(maxBizseqno + 1), 8, "0");
                }

            }

            //insert  XFIFBANKDETL ��
            bankdetl.setJournalno(journalno);
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
            bankdetl.setSystemtype("2");
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


    /*
   �����������ݽ���ֱ�����۵Ľ������SBS�������ʴ���
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
   �����Ŵ��������ݽ���ֱ�����۵Ľ������SBS�������ʴ���
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
   ����SBS���ʴ����� �Է���ϵͳ���л�д����
    */
    private int doWriteBackButton(DatabaseConnection conn, ErrorMessages msgs) {

        int rtn = 0;

        try {
            FDWriteBackManager fdw = new FDWriteBackManager();
            rtn = fdw.processWriteBack(inputdate, conn, msgs);
//            rtn = fdw.processWriteBack_temp(inputdate, conn, msgs);
        } catch (java.io.IOException e) {
            return -1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return rtn;
    }


}