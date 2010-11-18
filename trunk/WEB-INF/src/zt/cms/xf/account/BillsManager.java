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
    ÿ����ʼʱ�ʵ����ɴ���
    1�����ñ�����ǰӦȷ��ǰ�� �ѳ��ʵ� �����ʶ������ �����մ���
    2�����ݵ�ǰҵ��������ʹ��˷������ʵ����¼��ACTCUTPAYMAIN��
             1�������ڻ����ʵ�Լ���ۿ�������<=��ǰҵ������
                   a)�����ڻ����ʵ�δ���ʣ����ݱ��ڻ����ʵ�Լ�������ֱ�����ɵ��տۿ��¼
                   b)�����ڻ����ʵ��ѳ��ʣ��ұ��ڻ����ʵ�δ����
                   C)�����ڻ����ʵ��ѳ��ʣ��ұ��ڻ����ʵ��ѽ���
                        1����δ���ڣ���������
                        2���������ڣ��ұ��������ʵ��ѽ��壬��������
                        3���������ڣ��ұ��������ʵ�δ���壬�ұ��������ʵ�Լ���ۿ������� >��ǰҵ�����ڣ���������
                        4���������ڣ��ұ��������ʵ�δ���壬�ұ��������ʵ�Լ���ۿ������� <=��ǰҵ�����ڣ����ݱ��������ʵ�Լ�������ֱ�����ɵ��տۿ��¼


                   ����������δ���壬�鿴�Ƿ����ڣ�
                       1��δ�������ڿ������ڣ�  ����Լ�������ֱ�����ɵ��տۿ��¼
                       2��δ�����Ҳ��ڿ������ڣ������ڱ�־������Լ����������ɵ��տۿ��¼
                       3�������ڵģ� ����Ҫ��
                              1�������жϱ��ڻ����ʵ��Ƿ��ѽ��壨CLOSEDCD=��1�����������δ���壬����Լ�������ֱ�����ɵ��տۿ��¼
                              2��������ڻ����ʵ��ѽ��壬���жϱ��������ʵ��Ƿ��ѽ��� ��ODB_CLOSEDCD=��1������
                                   ��������ʵ�δ����

                             2 ��󻹿����ڲ�Ϊ�յģ� ���� Լ�����������ѳ��ʵ����ɽ�ΥԼ�����ɿۿ��¼

    */

    /*
    �ۿ��ʵ����ɴ������̣�
        ����δ�����ʵ���
        1��ÿ��13�տ�ʼ���ɿۿ��ļ�������ǰ����ѳ��ʵ��ʵ���Ҫ��ر������ѳ����ʵ�����ʱ��������ط�������
        2���ڿ������ڣ����Զ�����ɿۿ��ļ�������ǰ��ر��ѳ��ʵ���
        3������������δ�ۿ�ɹ����ʵ����γ������ʵ���
                ���ݵ�ǰ�û������ҵ������

        �����������ʵ���
        1���ڿ�������Ŀɽ����������������ϵͳ�Զ����ۡ�
        2������ʱ�������ɽ�ΥԼ���������ʵ��г��ʡ�
        3���������ڣ������γ����ɽ�ΥԼ�𣬽��д��۲��������ȼ����ڵ�ǰ�����������ʵ���
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
//            sqlparams[0] = "0";  //δ���ʱ�־
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

            //���ݷ������ʵ�������ϸ�ʵ�
            for (int i = 0; i < xfactcutpaymain.length; i++) {

/*
                //�������ʵ��еĺ�ͬ�ŴӺ�ͬ����ȡ����������
                XfcontractDao contractDao = XfcontractDaoFactory.create(sqlconn);
                Xfcontract contract = contractDao.findByPrimaryKey(xfactcutpaymain[i].getContractno());
                BigDecimal servicecharge = contract.getServicecharge();
*/

                //20090730  zhanrui
                //������ǰ����� ���ʵ������á���ǰ�����׼��־�� �ļ�¼��������
                if (xfactcutpaymain[i].getPrecutpaycd().equals("1")) {
                    continue;
                }

                PropertyUtils.copyProperties(xfactcutpaydetl, xfactcutpaymain[i]);

                //��ˮ������
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
                xfactcutpaydetl.setBilltype("0"); //Ĭ��Ϊ�����ʵ�

                if (xfactcutpaymain[i].getChargeoffcd().equals("1")
                        && xfactcutpaymain[i].getClosedcd().equals("1")
                        && xfactcutpaymain[i].getOverduecd().equals("1")) { //�����ʵ�

                    xfactcutpaydetl.setPaybackdate(xfactcutpaymain[i].getOdbPaybackdate());
                    xfactcutpaydetl.setPaybackamt(xfactcutpaymain[i].getOdbPaybackamt());
                    xfactcutpaydetl.setPaybackdate(xfactcutpaymain[i].getOdbPaybackdate());
                    xfactcutpaydetl.setBilltype("1"); //�����ʵ�

                    xfactcutpaydetl.setPrincipalamt(BigDecimal.valueOf(0));
                    xfactcutpaydetl.setServicechargefee(BigDecimal.valueOf(0));
                    xfactcutpaydetl.setLatefee(xfactcutpaymain[i].getOdbLatefee());
                    xfactcutpaydetl.setBreachfee(xfactcutpaymain[i].getOdbBreachfee());

                    //���������ʵ��ѳ��ʱ�־�Լ���������
                    if ("0".equals(xfactcutpaymain[i].getOdbChargeoffcd())) {
                        xfactcutpaymain[i].setOdbChargeoffcd("1");
                        xfactcutpaymain[i].setOdbStartdate(sFmt.parse(chargeoffdate));
                    }
                } else {
                    xfactcutpaydetl.setLatefee(BigDecimal.valueOf(0));
                    xfactcutpaydetl.setBreachfee(BigDecimal.valueOf(0));
                    xfactcutpaydetl.setPrincipalamt(xfactcutpaymain[i].getPrincipalamt());
                    xfactcutpaydetl.setServicechargefee(xfactcutpaymain[i].getServicechargefee());
                    //���û����ʵ��ѳ��ʱ�־�Լ���������
                    if ("0".equals(xfactcutpaymain[i].getChargeoffcd())) {
                        xfactcutpaymain[i].setChargeoffcd("1");
                        xfactcutpaymain[i].setStartdate(sFmt.parse(chargeoffdate));
                    }
                }

                //�����µ���ϸ�ʵ�
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
            msgs.add("�����쳣��" + e.getMessage());
            Debug.debug(e);
            throw new Exception(e);
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }

    /*
    ��ǰ�����ʵ�����
     */
    public int generatePreCutPayBills(SessionContext ctx, DatabaseConnection conn, ErrorMessages msgs) throws Exception {

        Connection sqlconn = null;

        try {

            MyDB.getInstance().addDBConn(conn);
            sqlconn = conn.getConnection();
            sqlconn.setAutoCommit(false);

            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

            XfcontractDao contractdao = XfcontractDaoFactory.create(sqlconn);

            //�����ͬ״̬Ϊ��ǰ�������ͨ���ĺ�ͬ
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
                    //���û����ʵ��ѳ��ʱ�־�Լ���������
                    if ("0".equals(main.getChargeoffcd())) {
                        main.setChargeoffcd("1");
                        main.setStartdate(sFmt.parse(chargeoffdate));
                    }
                    mainpk.setContractno(main.getContractno());
                    mainpk.setPoano(main.getPoano());
                    maindao.update(mainpk, main);
                }

                if (!totalamt.equals(principal.add(servicechargefee))) {
                    msgs.add("���ɿۿ��ʵ�ʱ���˶Բ�����");
                    throw new Exception("���ɿۿ��ʵ�ʱ���˶Բ�����");
                }

                //����һ����ϸ�ۿ��¼��CUTPAYDETL��
                PropertyUtils.copyProperties(xfactcutpaydetl, xfactcutpaymain[0]);

                //��ˮ������
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

                xfactcutpaydetl.setBilltype("2"); //��ǰ�����ʵ�

                xfactcutpaydetl.setPaybackamt(totalamt);
                xfactcutpaydetl.setLatefee(BigDecimal.valueOf(0));
                xfactcutpaydetl.setBreachfee(BigDecimal.valueOf(0));
                xfactcutpaydetl.setPrincipalamt(principal);
                xfactcutpaydetl.setServicechargefee(servicechargefee);

                //�����µ���ϸ�ʵ�
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
            msgs.add("�����쳣��" + e.getMessage());
            Debug.debug(e);
            throw new Exception(e);
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }


    /*
    ���ؿۿ�δ�ɹ��ļ�¼��
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
//    ���ص�ǰ����֮ǰ�Ŀۿ�δ�ɹ��ļ�¼��
//     */
//    private Xfactcutpaydetl[] getFailedPayDetl(SessionContext ctx, Connection conn)
//            throws XfactcutpaydetlDaoException {
//
//        String sql = " billstatus = ? and to_char(startdate, 'yyyyMMdd') < ?";
//        Object[] sqlparams = new Object[2];
//
//        sqlparams[0] = XFBillStatus.BILLSTATUS_CUTPAY_FAILED;  //�ۿ�ɹ���־
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
//            //TODO:�ۿ����ڵĴ������е��Ƿ���Ҫ��ǰһ�죿
//            String sql = " chargeoffcd = ? and to_char(paybackdate, 'yyyyMMdd') <= ?";
//            Object[] sqlparams = new Object[2];
//
//            sqlparams[0] = "0";  //δ���ʱ�־
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
//            msgs.add("�����쳣��" + e.getMessage());
//            Debug.debug(e);
//            return -1;
//        } finally {
//            MyDB.getInstance().releaseDBConn();
//        }
//    }

    /*
    ͳ��δ�ر��ʵ�������δ������ѳ��ʵ���������ʱ��δ���к���������ʵ�?��
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
//            sqlparams[0] = XFBillStatus.BILLSTATUS_CUTPAY_SUCCESS;  //�ۿ�ɹ�
//            sqlparams[1] = XFBillStatus.BILLSTATUS_CUTPAY_FAILED;  //�ۿ�ʧ��

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
     * 2010/11/18  ���Ŵ��ϻ�
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
    ���ʿۿ�ɹ���Ĵ���   2010/10/21 ֮�� ����
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

            //�����û���������ȡ�������ʵ�������
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

            //�������ʵ�
            mainPk.setContractno(cutpaydetl.getContractno());
            mainPk.setPoano(cutpaydetl.getPoano());
            cutpaymain = mainDao.findByPrimaryKey(mainPk);

            //TODO:������ �ж� cutpaymain ΪNULL���

            int terminalcd = 0;  //�Ƿ�Ϊ���һ���ʵ��������־

            if (cutpaydetl.getBilltype().equals("0")) {     //��ϸ�ʵ�Ϊ�����ʵ�
                if (cutpaymain.getClosedcd().equals("0")) {
                    cutpaymain.setClosedcd("1");
                    cutpaymain.setCloseddate(date);
                    if (cutpaymain.getOverduecd().equals("1")) { //���ʵ��������ڱ�־ʱ����������ɽ��Լ��������ڼ���ΥԼ��
                        //���ɽ�
                        BigDecimal latefee = cutpaydetl.getPaybackamt().multiply(latefeerate);
                        latefee = latefee.setScale(2, BigDecimal.ROUND_HALF_UP);

                        if (latefee.compareTo(new BigDecimal(10)) == -1) {
                            latefee = new BigDecimal(10);
                        }
                        cutpaymain.setOdbLatefee(latefee);

                        //ΥԼ�� ����������
                        long days = (date.getTime() - cutpaymain.getPaybackdate().getTime()) / (24 * 60 * 60 * 1000);
                        BigDecimal breachfee = cutpaydetl.getPaybackamt().multiply(breachfeerate);
                        breachfee = breachfee.multiply(new BigDecimal(days)).setScale(2, BigDecimal.ROUND_HALF_UP);
                        cutpaymain.setOdbBreachfee(breachfee);

                        //�������ں��ܽ��
                        cutpaymain.setOdbPaybackamt(latefee.add(breachfee));
                        //�������ڽ��Ļ���ʱ��
                        cutpaymain.setOdbPaybackdate(OverduePayBackDate);
                    } else{   //δ���� ����Ƿ�Ϊ���һ���ʵ�
                         contract = contractDao.findByPrimaryKey(cutpaydetl.getContractno());
                        if (contract.getDuration().equals(cutpaydetl.getPoano())) {
                             terminalcd = 1;
                        }
                    }
                    mainDao.update(mainPk, cutpaymain);
                }
            } else if(cutpaydetl.getBilltype().equals("1")) {    //��ϸ�ʵ�Ϊ�����ʵ�
                if (cutpaymain.getOdbClosedcd().equals("0")) {
                    cutpaymain.setOdbClosedcd("1");
                    cutpaymain.setOdbCloseddate(date);
                    mainDao.update(mainPk, cutpaymain);
                }
                contract = contractDao.findByPrimaryKey(cutpaydetl.getContractno());
               if (contract.getDuration().equals(cutpaydetl.getPoano())) {
                    terminalcd = 1;
               }
            } else if(cutpaydetl.getBilltype().equals("2")) {    //��ϸ�ʵ�Ϊ��ǰ�����ʵ�
                String  sql = "contractno = '" +cutpaydetl.getContractno() + "' and  precutpaycd = '1'";
                Xfactcutpaymain[] mains =mainDao.findByDynamicWhere(sql ,null);
                if (mains.length == 0) {
                    throw new Exception("������ϸ�ʵ���Ӧ�����ʵ�ʱ���ִ���");
                }

                for (Xfactcutpaymain main : mains){
                    main.setClosedcd("1");
                    main.setCloseddate(date);
                    XfactcutpaymainPk pk = new XfactcutpaymainPk();
                    pk.setContractno(main.getContractno());
                    pk.setPoano(main.getPoano());
                    mainDao.update(pk, main);
                }
                //�޸ĺ�ͬ����Ϣ :����
                contract = contractDao.findByPrimaryKey(cutpaydetl.getContractno());
                contract.setTerminalcd("1");   //������ֹ
                contract.setTerminalreason("1");     //����ԭ��Ϊ����ǰ����
                contract.setTerminaldate(date);
                contract.setCstatus(XFContractStatus.HETONG_OVER_NORMAL); //��ͬ������ֹ
                contractPk.setContractno(cutpaydetl.getContractno());
                contractDao.update(contractPk, contract);
            }

            //���������½��壨���������ʵ����壩
            if (terminalcd == 1) {
                contract = contractDao.findByPrimaryKey(cutpaydetl.getContractno());
                contract.setTerminalcd("1");   //������ֹ
                contract.setTerminalreason("0");     //����ԭ��Ϊ����������
                contract.setTerminaldate(date);
                contract.setCstatus(XFContractStatus.HETONG_OVER_NORMAL); //��ͬ������ֹ
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
       �������пۿ�ʧ�ܺ�Ĵ�����������ʱ�� ���ʵ�״̬
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

                //TODO: �ж����ڣ��ڼ�����������ӽ��ձ�

            }
        } catch (Exception e) {
            Debug.debug(e);
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }

    }

    /*
       ���пۿ�ʧ�ܺ�Ĵ�����������ʱ�� ���ʵ�״̬
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
