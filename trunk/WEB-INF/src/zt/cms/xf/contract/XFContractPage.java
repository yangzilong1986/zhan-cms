package zt.cms.xf.contract;

import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.util.SqlField;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Map;
import java.util.Calendar;
import java.util.logging.Logger;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import zt.platform.form.control.SessionContext;
import zt.platform.form.control.FormActions;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.form.util.datatype.*;
import zt.platform.form.config.FormBean;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.utils.Debug;
import zt.platform.user.UserManager;
import zt.cms.pub.code.SerialNumber;
import zt.cms.xf.account.Airth;
import zt.cms.xf.common.dto.*;
import zt.cms.xf.common.dao.XfcontractpaysDao;
import zt.cms.xf.common.dao.XfactcutpaymainDao;
import zt.cms.xf.common.dao.XfcontractDao;
import zt.cms.xf.common.factory.XfcontractpaysDaoFactory;
import zt.cms.xf.common.factory.XfactcutpaymainDaoFactory;
import zt.cms.xf.common.factory.XfcontractDaoFactory;
import zt.cms.xf.common.constant.XFContractStatus;
import zt.cms.xf.common.constant.XFBankCode;
import zt.cms.xf.common.constant.XFCommon;
import zt.cmsi.mydb.MyDB;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-3-14
 * Time: 20:24:05
 * To change this template use File | Settings | File Templates.
 */
public class XFContractPage extends FormActions {

    public static Logger logger = Logger.getLogger("zt.cms.xf.contract");
    private boolean isNewPaySchedule = false;
    private String superformid = null;
    private boolean isFHStatus = false;        //����״̬
    private boolean isCXStatus = false;        //��ѯ״̬
    private boolean isJBStatus = false;        //����¼��״̬��������ͬ���޸Ĳ��غ�ͬ��

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {

        boolean isNewRecord = false;
        superformid = (String) ctx.getAttribute("SUPERFORMID");

        instance.setReadonly(true);
        instance.getFormBean().getElement("ACCOUNTOPER").setComponetTp(6); //���÷ſťΪ����
        instance.getFormBean().getElement("CREDITISSUELISTFH").setComponetTp(6);  //���÷ſ��б�鿴��ťΪ����
        instance.getFormBean().getElement("CREDITISSUEFH").setComponetTp(6);  //���÷ſ�˰�ťΪ����
        instance.getFormBean().getElement("CREDITISSUEFHDISMISSAL").setComponetTp(6);  //���÷ſ�˰�ťΪ����
        instance.getFormBean().getElement("CONFIRMBUTTON").setComponetTp(6); //���ø��˰�ťΪ����
        instance.getFormBean().getElement("PAYSCHEDULE").setComponetTp(6); //���÷ſ�ƻ���ťΪ����
        instance.getFormBean().getElement("DISMISSALBUTTON").setComponetTp(6); //���÷ſ�ƻ���ťΪ����

//        //���÷��ڸ������ڲ���˾�������ڲ��ʺ���λ����ʾ
//        instance.getFormBean().getElement("CLIENTACT").setComponetTp(4);

        if (superformid != null) {
            if (superformid.equals("XFCONTRACTLINK")) {
                ctx.setAttribute("SUPERFORMID", "XFCONTRACTPAGE");
                isNewRecord = true;
                instance.setReadonly(false);
                if (getAppInfo(ctx, conn, instance, msgs) == -1) {
                    return -1;
                }
                isNewPaySchedule = true;

            } else if (superformid.equals("XFCREDITISSUELIST")) {     //����ž��촦��
                instance.getFormBean().getElement("ACCOUNTOPER").setComponetTp(15);
            } else if (superformid.equals("XFCREDITISSUELISTWH")) {     //�����ά������
                instance.getFormBean().getElement("ACCOUNTOPER").setComponetTp(15);
            } else if (superformid.equals("XFCREDITISSUELISTFH")) {     //����Ÿ��˴���
                instance.getFormBean().getElement("CREDITISSUELISTFH").setComponetTp(15);
                instance.getFormBean().getElement("CREDITISSUEFH").setComponetTp(15);
                instance.getFormBean().getElement("CREDITISSUEFHDISMISSAL").setComponetTp(15);
            } else if (superformid.equals("XFCONTRACTLISTFH")) {
                isFHStatus = true;
//                UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//                instance.getFormBean().getElement("CHECKERID").setDefaultValue(um.getUserName());
//                instance.setValue("CHECKERID",um.getUserName());
//                instance.getFormBean().getElement("UPDATORID").setDefaultValue(um.getUserName());

                instance.getFormBean().getElement("CONFIRMBUTTON").setComponetTp(15); //���ø��˰�ťΪϵͳ��ť
                instance.getFormBean().getElement("DISMISSALBUTTON").setComponetTp(15); //���ò��ذ�ťΪϵͳ��ť

            } else if (superformid.equals("XFCONTRACTLISTCX")
                    || superformid.equals("XFCONTRACTLISTDY")
                    || superformid.equals("XFAPPQUERYLIST")) {
                isCXStatus = true;
                //���ð�ťΪ����
                instance.getFormBean().getElement("CONFIRMBUTTON").setComponetTp(6);
                instance.getFormBean().getElement("DISMISSALBUTTON").setComponetTp(6);
                instance.getFormBean().getElement("PAYSCHEDULE").setComponetTp(6);

//                //���÷��ڸ������ڲ���˾�������ڲ��ʺ���λ����ʾ
//                instance.getFormBean().getElement("CLIENTACT").setComponetTp(4);

            } else if (superformid.equals("XFCONTRACTLIST")) {
                isJBStatus = true;
                instance.setReadonly(false);
            }
        }

        //ctx.setAttribute("SUPERFORMID", null);


        if (isNewRecord) {
            trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        } else {
            String contractno = ctx.getParameter("CONTRACTNO");
            if (contractno == null) {
                contractno = (String) ctx.getAttribute("CONTRACTNO");
            }

            if (contractno != null && contractno.trim().length() != 0) {
                instance.setValue("CONTRACTNO", contractno);
                trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
            } else {
                trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
            }
        }
        return 0;

    }

    public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager, SqlAssistor assistor) {

        if (isFHStatus) {
            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
            //instance.getFormBean().getElement("CHECKERID").setDefaultValue(um.getUserName());
            instance.setValue("CHECKERID", um.getUserName());
            instance.setValue("UPDATORID", um.getUserName());
            //instance.getFormBean().getElement("UPDATORID").setDefaultValue(um.getUserName());
            //TODO ����DB���������ֶν��и���...
        }

        return 0;
    }

    public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {


        ctx.setRequestAtrribute("msg", "��ͬ��Ϣ���ӳɹ���");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setRequestAtrribute("isback", "0");

        ctx.setTarget("/showinfo.jsp");
        instance.setReadonly(true);

        if (generateNewPaySchedule(ctx, conn, instance, msgs) < 0) {
            //TODO:
        }

        return 0;
    }

    public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                         EventManager manager, SqlAssistor assistor) {
        instance.setValue("OPERATEDATE", "#SYSDATE#");
        instance.setValue("CREATEDATE", "#SYSDATE#");
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATEDATE", "#SYSDATE#");
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CREATEDATE", "#SYSDATE#");

//        String str = instance.getFormBean().getElement("CONTRACTAMT").getDefaultValue();
        String str = instance.getStringValue("CONTRACTAMT");

        instance.setValue("PRINCIPALAMT", str);
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "PRINCIPALAMT", str);

        return 0;
    }

    public int preUpdate(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                         EventManager manager, SqlAssistor assistor) {

        //? no use?
        instance.setValue("CHECKDATE", "#SYSDATE#");
        instance.setValue("UPDATEDATE", "#SYSDATE#");
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CHECKDATE", "#SYSDATE#");
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "UPDATEDATE", "#SYSDATE#");

        if (isJBStatus) {
            instance.setValue("CSTATUS", "1");
            assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CSTATUS", "1");
        }
//        //�������ԱID
//        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//        instance.getFormBean().getElement("CHECKERID").setDefaultValue(um.getUserName());
//        instance.getFormBean().getElement("UPDATORID").setDefaultValue(um.getUserName());
//        instance.getFormBean().getElement("UPDATEFORM").setDefaultValue("XFCONTRACTPAGE");

        return 0;
    }

    public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
        instance.setValue("CHECKDATE", "#SYSDATE#");
        instance.setValue("UPDATEDATE", "#SYSDATE#");
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CHECKDATE", "#SYSDATE#");
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "UPDATEDATE", "#SYSDATE#");

        if (isJBStatus) {
            instance.setValue("CSTATUS", "1");
            assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CSTATUS", "1");
            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
            assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATORID", um.getUserName());
            assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATORDATE", "#SYSDATE#");
        }

        return 0;
    }

    public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
        ctx.setRequestAtrribute("msg", "��ͬ��Ϣ�޸ĳɹ���");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setRequestAtrribute("isback", "0");
        ctx.setTarget("/showinfo.jsp");
        instance.setReadonly(true);

        return 0;
    }


    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {

        if (button == null) {
            return -1;
        }
        if (button.equals("PAYSCHEDULE")) {
//                trigger(manager, "XFCONTRACTPAYLIST", null);
        } else if (button.equals("CONFIRMBUTTON")) {
            if (isFHStatus) {
                if (doConfirm(ctx, conn, instance, msgs) == 0) {
                    ctx.setRequestAtrribute("msg", "��ͬ�����ͨ����");
                    ctx.setRequestAtrribute("flag", "1");
                    ctx.setRequestAtrribute("isback", "0");
                    ctx.setTarget("/showinfo.jsp");
                    instance.setReadonly(true);
                } else {
                    return -1;
                }
            }
        } else if (button.equals("DISMISSALBUTTON")) {
            if (isFHStatus) {
                if (doDismissal(ctx, conn, instance, msgs) == 0) {
                    ctx.setRequestAtrribute("msg", "��ͬ�Ѳ��أ�");
                    ctx.setRequestAtrribute("flag", "1");
                    ctx.setRequestAtrribute("isback", "0");
                    ctx.setTarget("/showinfo.jsp");
                    instance.setReadonly(true);

                } else {
                    return -1;
                }
            }
        } else if (button.equals("ACCOUNTOPER")) { //����Ŵ��������ʺ�
            trigger(manager, "XFCLIENTACT", null);
        } else if (button.equals("CREDITISSUELISTFH")) {  //������б�鿴����
            trigger(manager, "XFACTPAYDETLLISTFH", null);
        } else if (button.equals("CREDITISSUEFH")) {  //�������˴���
            if (doConfirmForCreditIssue(ctx, conn, instance, msgs) == 0) {
                ctx.setRequestAtrribute("msg", "��ͬ�ſ������ͨ����");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            } else {
                ctx.setRequestAtrribute("msg", "��ͬ�ſ���˳������⣬���ѯ��");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
                return -1;
            }
        } else if (button.equals("CREDITISSUEFHDISMISSAL")) {  //�������˲��ش���
            if (doDismissalForCreditIssue(ctx, conn, instance, msgs) == 0) {
                ctx.setRequestAtrribute("msg", "��ͬ�ſ��Ѳ��أ�");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            } else {
                ctx.setRequestAtrribute("msg", "��ͬ�ſ�س������⣬���ѯ��");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
                return -1;
            }
        }
        return 0;
    }


    private int getAppInfo(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) {

        try {
            MyDB.getInstance().addDBConn(conn);

            String appno = ctx.getParameter("APPNO");
            String id = "";
            String idtype = "";
            String name = "";

            //TODO: zt.cmsi.pub.cenum.EnumValue
            //String sql = "select idtype,id, name from xfvappinfo where appno='" + appno + "'";
            String sql = "select * from xfvappinfo where appno='" + appno + "'";
            RecordSet rs = conn.executeQuery(sql);
            int count = 0;
            if (rs.next()) {

                //TODO: PRODUCT���ʹ��
//               String productsql = "select * from product where productid= " + appno +
//                       " and  duration = " + appno +
//                       " and clientid = " + ;

                FormBean fb = instance.getFormBean();

                fb.getElement("APPNO").setDefaultValue(appno);
                fb.getElement("CONTRACTNO").setDefaultValue(ctx.getParameter("CONTRACTNO"));
                fb.getElement("APPTYPE").setDefaultValue(rs.getString("APPTYPE"));

                fb.getElement("INTERESTTYPE").setDefaultValue("1");

                //����ǩ������Ϊ��ǰ����
                fb.getElement("STARTDATE").setDefaultValue(rs.getString("STARTDATE"));


                fb.getElement("CSTATUS").setDefaultValue("1");
                fb.getElement("CONTRACTTYPE").setDefaultValue("1");
                fb.getElement("CURNO").setDefaultValue("1");

                fb.getElement("DURATION").setDefaultValue(rs.getString("DURATION"));

                fb.getElement("CONTRACTAMT").setDefaultValue(String.valueOf(new Double(rs.getDouble("CONTRACTAMT"))));
                fb.getElement("RECEIVEAMT").setDefaultValue(String.valueOf(new Double(rs.getDouble("RECEIVEAMT"))));
                fb.getElement("COMMAMT").setDefaultValue(String.valueOf(new Double(rs.getDouble("COMMAMT"))));

//                Double receiveamt = new Double(rs.getDouble("RECEIVEAMT"));
//                fb.getElement("RECEIVEAMT").setDefaultValue(String.valueOf(receiveamt));
//                Double commamt = new Double(rs.getDouble("COMMAMT"));
//                fb.getElement("COMMAMT").setDefaultValue(String.valueOf(commamt));


                Double dTemp = new Double(rs.getDouble("SERVICECHARGE"));
                fb.getElement("SERVICECHARGE").setDefaultValue(String.valueOf(dTemp));
//                dTemp = new Double(rs.getDouble("LATEFEERATE"));
                dTemp = new Double(0);
                fb.getElement("LATEFEERATE").setDefaultValue(String.valueOf(dTemp));
//                dTemp = new Double(rs.getDouble("BREACHFEERATE"));
                fb.getElement("BREACHFEERATE").setDefaultValue(String.valueOf(dTemp));
//                dTemp = new Double(rs.getDouble("LOWESTLATEFEE"));
                fb.getElement("LOWESTLATEFEE").setDefaultValue(String.valueOf(dTemp));
//                dTemp = new Double(rs.getDouble("MANAGERFEERATE"));
                fb.getElement("MANAGERFEERATE").setDefaultValue(String.valueOf(dTemp));

                fb.getElement("PARTNERACT").setDefaultValue(rs.getString("PARTNERACT"));
                fb.getElement("PARTNERNAME").setDefaultValue(rs.getString("PARTNERNAME"));

//               fb.getElement("COMMNAME").setDefaultValue(rs.getString("COMMNAME"));
//               fb.getElement("COMMTYPE").setDefaultValue(rs.getString("COMMTYPE"));
//               fb.getElement("COMMNUM").setDefaultValue(rs.getString("COMMNUM"));

                fb.getElement("CLIENTID").setDefaultValue(rs.getString("CLIENTID"));
                fb.getElement("CLIENTIDTYPE").setDefaultValue(rs.getString("CLIENTIDTYPE"));
                fb.getElement("CLIENTNAME").setDefaultValue(rs.getString("CLIENTNAME"));
                fb.getElement("CLIENTNO").setDefaultValue(rs.getString("CLIENTNO"));
                fb.getElement("PAYBACKACT").setDefaultValue(rs.getString("PAYBACKACT"));

                fb.getElement("CUSTOMER_CODE").setDefaultValue(rs.getString("CUSTOMER_CODE"));
                fb.getElement("SIGN_ACCOUNT_NO").setDefaultValue(rs.getString("SIGN_ACCOUNT_NO"));

                String bankid = rs.getString("PAYBACKBANKID");
                fb.getElement("PAYBACKBANKID").setDefaultValue(bankid);

                //��ͬǩԼ��Ĭ��Ϊ�ൺ
                fb.getElement("PLACE").setDefaultValue("�ൺ");
                //���ÿͻ�������Ϊ���е�Ĭ�ϴ��ۻ���
                if (bankid.equals(XFBankCode.BANKCODE_CCB)) {
                    fb.getElement("WITHHOLDBANKNAME").setDefaultValue("�й����������ൺ�к���·֧��");
                } else if (bankid.equals(XFBankCode.BANKCODE_ALIPAY)) {
                    fb.getElement("WITHHOLDBANKNAME").setDefaultValue("֧�������й������缼�����޹�˾");
                } else if (bankid.equals(XFBankCode.BANKCODE_BILL99)) {
                    fb.getElement("WITHHOLDBANKNAME").setDefaultValue("�Ϻ���Ǯ��Ϣ�������޹�˾");
                } else {
                    //null
                }
                //�ʺ�����Ĭ��ʹ�ÿͻ�����
                fb.getElement("PAYBACKACTNAME").setDefaultValue(rs.getString("CLIENTNAME"));

                UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
                fb.getElement("OPERATORID").setDefaultValue(um.getUserName());
                fb.getElement("CREATORID").setDefaultValue(um.getUserName());
                //fb.getElement("CREATORFORM").setDefaultValue("XFCONTRACTPAGE");

//               idtype = rs.getString("IDTYPE");
//               id = rs.getString("ID");
//               name = rs.getString("NAME");  //todo: DB null�ж�
                count = 1;
            }
            rs.close();
            if (count == 0) {
                msgs.add("�����뵥������!");
                msgs.add("���뵥���:  " + appno);
                return -1;
            }

        } catch (Exception e) {
            msgs.add("�����쳣��" + e.getMessage());
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
        return 0;
    }


    /*
    ���ܣ��Զ����ɻ���ƻ�
    �����������Զ���������ƻ����ں�ͬ������Զ�����
    ��XFcontracgpayPage�еķ����������ƣ����㷽��δ�鹲ͨ
     */

    private int generateNewPaySchedule(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                                       ErrorMessages msgs) {

        try {
            MyDB.getInstance().addDBConn(conn);
            String contractno = ctx.getParameter("CONTRACTNO");

            String sql = "select count(*)  from xfcontractpays where contractno='" + contractno + "'";
            RecordSet rs = conn.executeQuery(sql);
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(0);
            }
            if (count > 0) {
                String sqlDelete = "delete from xfcontractpays where contractno='" + contractno.trim() + "'";
                conn.executeUpdate(sqlDelete);
                //conn.commit();
            }


            String sSql = "insert into xfcontractpays " +
                    "(contractno,cpaamt, poano, startdate,PAYBACKACT,PAYBACKACTNAME,PAYBACKBANKID,PRINCIPALAMT,SERVICECHARGEFEE) " +
                    "values " + "('" + contractno + "',?,?,?,?,?,?,?,?)";


            PreparedStatement ps = conn.getPreparedStatement(sSql);


            double contractamt = Double.parseDouble(ctx.getParameter("CONTRACTAMT"));
            double servicecharge = Double.parseDouble(ctx.getParameter("SERVICECHARGE"));
            int duration = Integer.parseInt(ctx.getParameter("DURATION"));

            double interest = Airth.mul(Airth.mul(contractamt, duration), servicecharge);
            interest = Airth.round(interest, 2);    //����Ϣ

            double totalamt = Airth.add(contractamt, interest);
            totalamt = Airth.round(totalamt, 2);    //�ܿۿ����ͬ���+��Ϣ��


            String paybackact = ctx.getParameter("PAYBACKACT");
            String paybackactname = ctx.getParameter("PAYBACKACTNAME");
            String paybackbankid = ctx.getParameter("PAYBACKBANKID");
            //TODO: ����������

//            double cpaamt = Airth.div(totalamt, duration, 2);   //ÿ�»����ܽ��
//            double cpaprincipal = Airth.div(contractamt, duration, 2);   //ÿ�±���
//            double cpainterest = cpaamt-cpaprincipal;  //ÿ��������
            double cpaamt = Airth.div(totalamt, duration, 2);   //ÿ�»����ܽ��
            double cpainterest = Airth.mul(contractamt, servicecharge);  //ÿ��������
            cpainterest = Airth.round(cpainterest, 2);  //ÿ��������
            double cpaprincipal = cpaamt - cpainterest;   //ÿ�±���


            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");

            //��õ�ǰ�û����������
            String startdate = ctx.getParameter("STARTDATE");
            cal.setTime(sFmt.parse(startdate));

            cal.set(Calendar.DAY_OF_MONTH, XFCommon.COMMON_CHARGEOFFDATE);


            for (int i = 1; i <= duration; i++) {
                if (i == duration) {
                    cpaamt = cpaamt * 100;
                    ps.setDouble(1, totalamt - (cpaamt * (duration - 1)) / 100);

                    cpaprincipal = cpaprincipal * 100;
                    ps.setDouble(7, contractamt - (cpaprincipal * (duration - 1)) / 100);

                    cpainterest = cpainterest * 100;
                    ps.setDouble(8, interest - (cpainterest * (duration - 1)) / 100);

                } else {
                    ps.setDouble(1, cpaamt);
                    ps.setDouble(7, cpaprincipal);
                    ps.setDouble(8, cpainterest);
                }

                ps.setInt(2, i);

                cal.add(Calendar.MONTH, 1);
                java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
                ps.setDate(3, sqlDate);

                ps.setString(4, paybackact);
                ps.setString(5, paybackactname);
                ps.setString(6, paybackbankid);

                ps.executeUpdate();
            }
            rs.close();

        } catch (Exception e) {
            msgs.add("�����쳣��" + e.getMessage());
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
        return 0;
    }

    private int doConfirm(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) {

        try {
            //TODO: Ƕ��������
            //conn.setAuto(false);
            MyDB.getInstance().addDBConn(conn);
            String contractno = ctx.getParameter("CONTRACTNO");

            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

//            String sql = "update xfcontract set cstatus = '3', checkerid = " +
//                    "'" + um.getUserName() + "'" +
//                    ", updatorid = " +
//                    "'" + um.getUserName() + "'" +
//                    ",checkdate = sysdate, updatedate = sysdate  " +
//                    "where contractno='" + contractno + "'";

            String sql = "update xfcontract set cstatus = " + XFContractStatus.QIANDING_TONGGUO +
                    ", checkerid = " +
                    "'" + um.getUserName() + "'" +
                    ", updatorid = " +
                    "'" + um.getUserName() + "'" +
                    ",checkdate = sysdate, updatedate = sysdate  " +
                    "where contractno='" + contractno + "'";

            logger.info(sql);

//            if (doGenMainBills(ctx,conn,instance,msgs) == -1) {
//                msgs.add("�ڸ��ݱ���ͬ�ķ��ڻ���ƻ����ɿۿ��ʵ�ʱ�������󣬺�ͬ����ʧ�ܣ�");
//                return -1;
//            }
            if (conn.executeUpdate(sql) < 0) {
                msgs.add("��ͬ����ʧ�ܣ�");
                return -1;
            }

            msgs.add("�˺�ͬ��ȷ��ͨ����");

            //���ݻ���ƻ�����ÿ�ڻ������ʵ�


            return 0;
        } catch (Exception e) {
            msgs.add("�����쳣��" + e.getMessage());
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }

    private int doGenMainBills(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) {

        try {
//            MyDB.getInstance().addDBConn(conn);
            String contractno = ctx.getParameter("CONTRACTNO");

            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

            XfcontractpaysDao paysdao = XfcontractpaysDaoFactory.create(conn.getConnection());
            Xfcontractpays[] pays = paysdao.findWhereContractnoEquals(contractno);

            XfactcutpaymainDao cutpaydao = XfactcutpaymainDaoFactory.create(conn.getConnection());
            Xfactcutpaymain xfactcutpaymain = new Xfactcutpaymain();


            if (pays.length == 0) {
                return -1;
            }

            for (int i = 0; i < pays.length; i++) {
                //BeanUtils.copyProperties(pays[i], xfactcutpaymain);
                //PropertyUtils.copyProperties(pays[i], xfactcutpaymain);
                xfactcutpaymain.setContractno(pays[i].getContractno());
                xfactcutpaymain.setPoano(pays[i].getPoano());
                xfactcutpaymain.setPaybackamt(pays[i].getCpaamt());
                xfactcutpaymain.setPaybackdate(pays[i].getStartdate());
                xfactcutpaymain.setPaybackact(pays[i].getPaybackact());
                xfactcutpaymain.setPaybackbankid(pays[i].getPaybackbankid());
                xfactcutpaymain.setPaybackbankno(pays[i].getPaybackbankno());
                xfactcutpaymain.setPaybackactname(pays[i].getPaybackactname());

                //���ñ��������������
                xfactcutpaymain.setPrincipalamt(pays[i].getPrincipalamt());
                xfactcutpaymain.setServicechargefee(pays[i].getServicechargefee());


                xfactcutpaymain.setClientname(ctx.getParameter("CLIENTNAME"));
                xfactcutpaymain.setClientact(ctx.getParameter("CLIENTACT"));
                xfactcutpaymain.setClientno(ctx.getParameter("CLIENTNO"));
                xfactcutpaymain.setClientidtype(ctx.getParameter("CLIENTIDTYPE"));
                xfactcutpaymain.setClientid(ctx.getParameter("CLIENTID"));

                xfactcutpaymain.setCustomerCode(ctx.getParameter("CUSTOMER_CODE"));
                xfactcutpaymain.setSignAccountNo(ctx.getParameter("SIGN_ACCOUNT_NO"));

                //���÷����ʵ�״̬
                xfactcutpaymain.setPbstatus("0");  //TODO: ��ȥ��


                xfactcutpaymain.setOperatorid(um.getUserName());
                xfactcutpaymain.setCreatorid(um.getUserName());
                xfactcutpaymain.setOperatedate(new Date());
                xfactcutpaymain.setCreatedate(new Date());

                xfactcutpaymain.setClosedcd("0");    //����δ�����־
                xfactcutpaymain.setChargeoffcd("0");    //����δ���ʱ�־
                xfactcutpaymain.setOverduecd("0");    //����δ���ڱ�־

                xfactcutpaymain.setOdbClosedcd("0");    //���������ʵ�δ�����־
                xfactcutpaymain.setOdbChargeoffcd("0");    //��������δ���ʱ�־

                xfactcutpaymain.setOdbPaybackamt(BigDecimal.valueOf(0));
                xfactcutpaymain.setOdbBreachfee(BigDecimal.valueOf(0));
                xfactcutpaymain.setOdbLatefee(BigDecimal.valueOf(0));

                //������ǰ������Ϣ 20090803 zhanrui
                xfactcutpaymain.setPrecutpaycd("0");
                xfactcutpaymain.setPrecutpaydate(null);

                //TODO:ȫ��δ���г��ʴ���Ŀۿ�ƻ�����ɾ��������
                //String sqlDelete = "delete from xfactcutpaymail where contractno='" + contractno.trim() + "'";
                //conn.executeUpdate(sqlDelete);

                cutpaydao.insert(xfactcutpaymain);
            }


        } catch (Exception e) {
            msgs.add("�����쳣��" + e.getMessage());
            Debug.debug(e);
            return -1;
        }
//        } finally {
//            MyDB.getInstance().releaseDBConn();
//        }
        return 0;
    }

    private int doDismissal(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) {

        try {
            MyDB.getInstance().addDBConn(conn);
            String contractno = ctx.getParameter("CONTRACTNO");

            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

            String sql = "update xfcontract set cstatus = " + XFContractStatus.QIANDING_BOHUI +
                    ", checkerid = " +
                    "'" + um.getUserName() + "'" +
                    ", updatorid = " +
                    "'" + um.getUserName() + "'" +
                    ",checkdate = sysdate, updatedate = sysdate  " +
                    "where contractno='" + contractno + "'";

            logger.info(sql);

            if (conn.executeUpdate(sql) < 0) {
                msgs.add("��ͬ����ʧ�ܣ�");
                return -1;
            }
            msgs.add("�˺�ͬ�Ѳ��أ�");
            return 0;
        } catch (Exception e) {
            msgs.add("�����쳣��" + e.getMessage());
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }


    private int doConfirmForCreditIssue(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) {

        try {
            //TODO: Ƕ��������
            //conn.setAuto(false);
//            MyDB.getInstance().addDBConn(conn);
            String contractno = ctx.getParameter("CONTRACTNO");

            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

            if (doGenMainBills(ctx, conn, instance, msgs) == -1) {
                logger.info("�ڸ��ݱ���ͬ�ķ��ڻ���ƻ����ɿۿ��ʵ�ʱ�������󣬺�ͬ����ʧ�ܣ�");
                return -1;
            }


            String sql = "update xfcontract set cstatus = " + XFContractStatus.FANGKUAN_TONGGUO +
                    " where contractno='" + contractno + "'";

            logger.info(sql);

            if (conn.executeUpdate(sql) < 0) {
                msgs.add("��ͬ�ſ��ʧ�ܣ�");
                return -1;
            }
            msgs.add("�˺�ͬ�ſ���ȷ��ͨ����");
            return 0;
        } catch (Exception e) {
            msgs.add("�����쳣��" + e.getMessage());
            Debug.debug(e);
            return -1;
        }
//        } finally {
//            MyDB.getInstance().releaseDBConn();
//        }
    }

    private int doDismissalForCreditIssue(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) {

        try {
            MyDB.getInstance().addDBConn(conn);
            String contractno = ctx.getParameter("CONTRACTNO");

            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

            String sql = "update xfcontract set cstatus = " + XFContractStatus.FANGKUAN_BOHUI +
                    " where contractno='" + contractno + "'";

            logger.info(sql);

            if (conn.executeUpdate(sql) < 0) {
                return -1;
            }
            return 0;
        } catch (Exception e) {
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }


}
