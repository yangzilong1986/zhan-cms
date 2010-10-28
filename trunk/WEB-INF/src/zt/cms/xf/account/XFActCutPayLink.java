package zt.cms.xf.account;

import org.apache.commons.lang.StringUtils;
import zt.cms.xf.common.constant.XFBillStatus;
import zt.cms.xf.common.dao.XfactcutpaydetlDao;
import zt.cms.xf.common.dao.XfactcutpaymainDao;
import zt.cms.xf.common.dto.Xfactcutpaydetl;
import zt.cms.xf.common.dto.Xfactcutpaymain;
import zt.cms.xf.common.factory.XfactcutpaydetlDaoFactory;
import zt.cms.xf.common.factory.XfactcutpaymainDaoFactory;
import zt.cms.xf.newcms.controllers.T100101CTL;
import zt.cms.xf.newcms.domain.T100101.T100101ResponseRecord;
import zt.cmsi.mydb.MyDB;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.event.EventType;
import zt.platform.user.UserManager;
import zt.platform.utils.Debug;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-3-14
 * Time: 20:24:05
 * To change this template use File | Settings | File Templates.
 */
public class XFActCutPayLink extends FormActions {

    public static Logger logger = Logger.getLogger("zt.cms.xf.account.XFActPayLink");

    private String chargeoffdate;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs, EventManager manager, String parameter) {

        SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");
        chargeoffdate = sFmt.format(new Date());


        instance.setValue("CHARGEOFFDATE", chargeoffdate);

        int num = countWaitChargeoffBills(msgs);

        if (num >= 0) {
            instance.setValue("CHARGEOFFNUM", num);
        }

        //trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_BREAK_TYPE);
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                           String button, ErrorMessages msgs, EventManager manager) {

        chargeoffdate = ctx.getParameter("CHARGEOFFDATE");
        int count = 0;
        if (button.equals("NEXT")) {
            BillsManager bm = new BillsManager(chargeoffdate);
            try {
                if (bm.accountOpenedBills(conn) > 0) {
                    ctx.setRequestAtrribute("msg", "����δ������ɵĿۿ��¼�����ѯ��");
                    ctx.setRequestAtrribute("flag", "1");
                    ctx.setRequestAtrribute("isback", "0");
                    ctx.setTarget("/showinfo.jsp");
                    instance.setReadonly(true);
                    return (-1);
                }

                //20101018 ���Ŵ��ӿڸ��� ���α��δ���
                if ((count = bm.generateBills(ctx, conn, msgs)) == 0) {
                    ctx.setRequestAtrribute("msg", "�����޴����ʿۿ��¼�����ѯ��");
                } else {
                    ctx.setRequestAtrribute("msg", "�ʵ����ɳɹ��������� " + count + " ���¿ۿ��¼�����ѯ��");
                }

                
/*

                //20101018 ���Ŵ��ӿڸ���
                if ((count = generateNewCmsBills(ctx, conn, msgs)) == 0) {
                    ctx.setRequestAtrribute("msg", "�����޴����ʿۿ��¼�����ѯ��");
                } else {
                    ctx.setRequestAtrribute("msg", "�ʵ����ɳɹ��������� " + count + " ���¿ۿ��¼�����ѯ��");
                }

*/

                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            } catch (Exception e) {
                ctx.setRequestAtrribute("msg", "ϵͳ������ִ������ѯ��");
                ctx.setRequestAtrribute("flag", "0");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
                return (-1);
            }

        }
        return 0;
    }

    /**
     * 20101018  ���Ŵ��ӿ�
     *
     * @param ctx
     * @param conn
     * @param msgs
     * @return
     * @throws Exception
     */
    public int generateNewCmsBills(SessionContext ctx, DatabaseConnection conn, ErrorMessages msgs) throws Exception {

        Connection sqlconn = null;

        try {

            MyDB.getInstance().addDBConn(conn);
            //conn.setAuto(false);                        //TODO
            sqlconn = conn.getConnection();
            sqlconn.setAutoCommit(false);

            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);


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

            //��ȡ���Ŵ�����LIST
            T100101CTL t100101 = new T100101CTL();
            //��ѯ ����/�����Ŵ���1/2�� ����
            List<T100101ResponseRecord> recvList = t100101.start("2");

            int count = 0;
            for (T100101ResponseRecord recvRecord : recvList) {

                //��ˮ������
                maxno++;
                String journalno = chargeoffdate + StringUtils.leftPad(Integer.toString(maxno), 5, "0");
                xfactcutpaydetl.setJournalno(journalno);

                xfactcutpaydetl.setContractno(recvRecord.getStdhth());
                xfactcutpaydetl.setClientno(recvRecord.getStdkhh());
                xfactcutpaydetl.setClientname(recvRecord.getStdkhmc());
                xfactcutpaydetl.setPoano(new BigDecimal(recvRecord.getStdqch()));
                xfactcutpaydetl.setClientact(recvRecord.getStddkzh());
                xfactcutpaydetl.setPaybackact(recvRecord.getStdhkzh());

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

                xfactcutpaydetl.setPaybackamt(new BigDecimal(recvRecord.getStdhkje()));
                xfactcutpaydetl.setPrincipalamt(new BigDecimal(recvRecord.getStdhkbj()));
                xfactcutpaydetl.setServicechargefee(new BigDecimal(recvRecord.getStdhklx()));

                //�����µ���ϸ�ʵ�
                detldao.insert(xfactcutpaydetl);

                count++;
            }

            sqlconn.commit();
            return count;

        }

        catch (Exception e) {
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


    private int countWaitChargeoffBills(ErrorMessages msgs) {

        try {

            XfactcutpaymainDao maindao = XfactcutpaymainDaoFactory.create();

            SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");
            String strDate = sFmt.format(new Date());

            String sql = "precutpaycd = '0' and ((chargeoffcd = '0' and to_char(paybackdate, 'yyyyMMdd') <= " + strDate + ")  or " +
                    "(chargeoffcd = '1' and  closedcd = '0' and to_char(paybackdate, 'yyyyMMdd') <= " + strDate + ") or " +
                    "(chargeoffcd = '1' and  closedcd = '1' and  overduecd = '1' and  odb_chargeoffcd = '0' and to_char(odb_paybackdate, 'yyyyMMdd') <= " + strDate + ") or " +
                    "(chargeoffcd = '1' and  closedcd = '1' and  overduecd = '1' and  odb_chargeoffcd = '1' and  odb_closedcd = '0' and to_char(odb_paybackdate, 'yyyyMMdd') <=" + strDate + "))";

            Xfactcutpaymain[] xfactcutpaymain = maindao.findByDynamicWhere(sql, null);

            return xfactcutpaymain.length;

        } catch (Exception e) {
            msgs.add("�����쳣��" + e.getMessage());
            Debug.debug(e);
            return -1;
        }
    }


}