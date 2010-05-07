package zt.cms.xf.fd.account;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-3-14
 * Time: 9:19:52
 * To change this template use File | Settings | File Templates.
 */

import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;

import zt.platform.form.util.event.*;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

import zt.platform.form.util.SessionAttributes;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.user.UserManager;
import zt.cms.xf.common.constant.XFBillStatus;
import zt.cms.xf.common.constant.XFBankCode;

public class FDIFCCBDetlListWH extends FormActions {

    public static Logger logger = Logger.getLogger("zt.cms.xf.account.XFActPayDetlListWH");

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {

        ctx.setAttribute("SUPERFORMID", instance.getFormid());

        if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            trigger(manager, "XFIFCCBDETLPAGE", null);
        }

        if (button.equals("SENDPKGBUTTON") ) {
            String[] recordnos = ctx.getParameters(SessionAttributes.REQUEST_DELETE_RANGE_NAME);

            //TODO:δѡ�д������¼������£�Ĭ��Ϊ��ȫ�������м�¼�����д���
//
//            if (recordnos == null) {
//                ctx.setRequestAtrribute("msg", "δѡ�д������¼��");
//                ctx.setRequestAtrribute("flag", "0");
//                ctx.setRequestAtrribute("isback", "0");
//                ctx.setTarget("/showinfo.jsp");
//                return -1;
//            }

//            if (recordnos == null) {
//                ctx.removeAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
//            }
            ctx.setAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME, recordnos);
            ctx.setAttribute("BUTTONNAME", button);
//            ctx.setAttribute("BANKCODE", XFBankCode.BANKCODE_CCB);
            trigger(manager, "FDDATELINK", null);

        }

//        if (button != null && button.equals("SUCCESSBUTTON")) {
//            doSuccessButton(ctx, conn, instance, msgs);
//
//            ctx.setRequestAtrribute("msg", "�趨�ۿ�ɹ���ɣ����ѯ��");
//            ctx.setRequestAtrribute("flag", "1");
//            ctx.setRequestAtrribute("isback", "0");
//            ctx.setTarget("/showinfo.jsp");
//            instance.setReadonly(true);
//        }
//        if (button != null && button.equals("FAILBUTTON")) {
//               doFailButton(ctx, conn, instance, msgs);
//
//               ctx.setRequestAtrribute("msg", "�趨�ۿ�ʧ����ɣ����ѯ��");
//               ctx.setRequestAtrribute("flag", "1");
//               ctx.setRequestAtrribute("isback", "0");
//               ctx.setTarget("/showinfo.jsp");
//               instance.setReadonly(true);
//           }

        return 0;
    }

//    private int doSuccessButton(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) {
//
//        String[] recordnos = ctx.getParameters(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
//        List results = new ArrayList();
//
////        conn.setAuto(false);
////        conn.begin();
//        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//
//        if (recordnos != null) {
//            for (int i = 0; i < recordnos.length; i++) {
//                try {
//                    if (recordnos[i] != null && recordnos[i].trim().length() > 0) {
//                        String sSql = "update xfactcutpaydetl set "
//                                + " billstatus = \'" + XFBillStatus.BILLSTATUS_CUTPAY_SUCCESS    //��Ϊ���ʳɹ�
//                                + "\', updatorid= \'" + um.getUserName()
//                                + "\', updatedate= sysdate"   //TODO: ʱ���ΪAS��ʱ��?
//                                + " where " + recordnos[i];
//
//                        logger.info(sSql);
//
//
//
//                        int thisresult = conn.executeUpdate(sSql);
//                        if (thisresult <= 0) {
//                            results.add("" + i);
//                            msgs.add("" + thisresult);
//                        }
//                    }
//                }
//                catch (Exception e) {
//                    msgs.add(e.getMessage());
//                    results.add("" + i);
//                    e.printStackTrace();
//                }
//            }
//            if (results.size() > 0) {
//                String err = "��";
//                for (int i = 0; i < results.size(); i++) {
//                    err += results.get(i);
//                    if (results.size() < results.size() - 1) {
//                        err += ",";
//                    } else {
//                        err += "��¼����ʧ��";
//                    }
//                }
//                msgs.add(err);
//            }
//        }
//
//
//        return 0;
//    }
//    private int doFailButton(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) {
//
//         String[] recordnos = ctx.getParameters(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
//         List results = new ArrayList();
//
////        conn.setAuto(false);
////        conn.begin();
//         UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//
//         if (recordnos != null) {
//             for (int i = 0; i < recordnos.length; i++) {
//                 try {
//                     if (recordnos[i] != null && recordnos[i].trim().length() > 0) {
//                         String sSql = "update xfactcutpaydetl set "
//                                 + " billstatus = \'" + XFBillStatus.BILLSTATUS_CUTPAY_FAILED    //��Ϊ����ʧ��
//                                 + "\', updatorid= \'" + um.getUserName()
//                                 + "\', updatedate= sysdate"   //TODO: ʱ���ΪAS��ʱ��?
//                                 + " where " + recordnos[i];
//
//                         logger.info(sSql);
//
//
//
//                         int thisresult = conn.executeUpdate(sSql);
//                         if (thisresult <= 0) {
//                             results.add("" + i);
//                             msgs.add("" + thisresult);
//                         }
//                     }
//                 }
//                 catch (Exception e) {
//                     msgs.add(e.getMessage());
//                     results.add("" + i);
//                     e.printStackTrace();
//                 }
//             }
//             if (results.size() > 0) {
//                 String err = "��";
//                 for (int i = 0; i < results.size(); i++) {
//                     err += results.get(i);
//                     if (results.size() < results.size() - 1) {
//                         err += ",";
//                     } else {
//                         err += "��¼����ʧ��";
//                     }
//                 }
//                 msgs.add(err);
//             }
//         }
//
//
//         return 0;
//     }
//
}