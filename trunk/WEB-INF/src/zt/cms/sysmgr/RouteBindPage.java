package zt.cms.sysmgr;


import zt.cms.pub.SCBranch;
import zt.cms.pub.SCUser;
import zt.cmsi.pub.cenum.EnumValue;
import zt.cmsi.pub.define.BMRouteBind;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.config.EnumerationType;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.event.EventType;

import java.util.logging.Logger;


public class RouteBindPage extends FormActions {

    private static Logger logger = Logger.getLogger("zt.cms.cm.PTEnmuDetlPage");

    private String RouteNo = null;
    private String SeqNo = null;
    private String bndtype = null;
    private String bndid = null;
    private String flag = null;

    public int load(SessionContext ctx, DatabaseConnection conn,
                    FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {
        //flag
        //    flag = ctx.getRequestAttribute("flag").toString().trim();
        //    if (flag.equals("write")) {
        //      instance.setReadonly(false);
        //    }
        //    else {
        //      instance.setReadonly(true);
        //    }
        //ENUID
        RouteNo = ctx.getParameter("ROUTENO");
//        if (RouteNo == null) {
//            msgs.add("����Ĳ�����ROUTENOΪ�գ�");
//            return -1;
//        }
        SeqNo = ctx.getParameter("SEQNO");
//        if (SeqNo == null) {
//            msgs.add("����Ĳ�����SEQNOΪ�գ�");
//            return -1;
//        }
        bndtype = ctx.getParameter("BNDTYPE");
//        if (bndtype == null) {
//            msgs.add("����Ĳ�����BNDTYPEΪ�գ�");
//            return -1;
//        }
        bndid = ctx.getParameter("BNDID");
//        if (bndid == null) {
//            msgs.add("����Ĳ�����BNDIDΪ�գ�");
//            return -1;
//        }


        if (RouteNo != null && SeqNo != null && bndtype != null && bndid != null) {
            instance.setValue("ROUTENO", RouteNo.trim());
            instance.setValue("SEQNO", SeqNo.trim());
            instance.setValue("BNDTYPE", bndtype.trim());
            instance.setValue("BNDID", bndid.trim());
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
        return 0;
    }


    public int preInsert(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, ErrorMessages msgs,
                         EventManager manager, SqlAssistor assistor) {

        bndtype = ctx.getParameter("BNDTYPE");
        bndid = ctx.getParameter("BNDID");
        String errormsg = null;

        //System.out.println("bndtype="+bndtype+"|bndid="+bndid+"============================333===");

        if (bndtype != null && bndid != null) {
            int i_bndtype = Integer.parseInt(bndtype);
            bndid = bndid.trim();

            if (i_bndtype == EnumValue.BndType_WangDianDaiMa) {
                if (SCBranch.isExist(bndid) == false) errormsg = "��������㲻����!";
            } else if (i_bndtype == EnumValue.BndType_YongHuDaiMa) {
                if (SCUser.isExist(bndid) == false) errormsg = "������û����벻����(����060501)!";
            } else if (i_bndtype == EnumValue.BndType_BuMenJiBie) {
                if (bndid.compareTo("1") < 0 || bndid.compareTo("4") > 0) errormsg = "����Ĳ��ż��𲻴���(����1)!";
            } else if (i_bndtype == EnumValue.BndType_UserType) {
                if (EnumerationType.validate("UserType", bndid) == false) errormsg = "������û����ʹ��벻����(����11)!";
            } else {
                errormsg = "Ŀǰ��֧�ִ������̰�����!";
            }

            if (errormsg != null) {
                msgs.add(errormsg);
                return -1;
            }
        }
        return 0;
    }

    public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                            FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {
        BMRouteBind.setDirty();
        return 0;
    }

    public int postDeleteOk(SessionContext ctx, DatabaseConnection conn,
                            FormInstance instance,
                            ErrorMessages msgs,
                            EventManager manager) {
        BMRouteBind.setDirty();
        return 0;
    }

    public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
        BMRouteBind.setDirty();
        return 0;
    }
}