package zt.cms.util;

import zt.cmsi.pub.cenum.EnumValue;
import zt.cmsi.pub.define.BMRouteBind;
import zt.cmsi.pub.define.BMRouteBindNode;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.user.UserManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Workflow {

    //    final public static String DefEndSta_XFSP_WangDianDaiMa = "11";
    //    final public static String DefEndSta_XFSP_YongHuDaiMa = "11";
    //    final public static String DefEndSta_XFSP_BuMenJiBie = "11";
    final public static String DefBeginSta_XFSP_UserType = "1";
    final public static String DefEndSta_XFSP_UserType = "11";

    public Workflow() {

    }

    public static BMRouteBindNode getRouteBindByBrhId(String bndid) {
        return BMRouteBind.getInstance().getRouteBind(EnumValue.RouteNo_XFSP, String.valueOf(EnumValue.BndType_WangDianDaiMa), bndid);
    }

    public static BMRouteBindNode getRouteBindByUserId(String bndid) {
        return BMRouteBind.getInstance().getRouteBind(EnumValue.RouteNo_XFSP, String.valueOf(EnumValue.BndType_YongHuDaiMa), bndid);
    }

    public static BMRouteBindNode getRouteBindByBrhLvl(String bndid) {
        return BMRouteBind.getInstance().getRouteBind(EnumValue.RouteNo_XFSP, String.valueOf(EnumValue.BndType_BuMenJiBie), bndid);
    }

    public static BMRouteBindNode getRouteBindByUserTp(String bndid) {
        return BMRouteBind.getInstance().getRouteBind(EnumValue.RouteNo_XFSP, String.valueOf(EnumValue.BndType_UserType), bndid);
    }

    /**
     * ������һ�����û�ӵ�в���״̬��
     * �����ǰ״̬��Ϊ�գ����¼�״̬Ϊ�գ��򷵻�""��
     * �磺�����û���Ӧ״̬Ϊ��5�����¼��û���Ӧ״̬Ϊ��5-6������ getNextStatusEnd �������ء�5-6����
     *
     * @param routeno   String
     * @param bndtype   String
     * @param status    String
     * @param condition String
     * @return NextStatus
     */
    public static String getNextStatus(String routeno, String bndtype, String status, String condition) {
        return BMRouteBind.getInstance().getNextStatus(routeno, bndtype, status, condition);
    }

    /**
     * ������һ�����û�ӵ�в���״̬��
     * �����ǰ״̬��Ϊ�գ��򷵻�""��
     * �磺�����û���Ӧ״̬Ϊ��6�����¼��û���Ӧ״̬Ϊ��4-5������ getNextStatusEnd �������ء�4-5����
     *
     * @param routeno   String
     * @param bndtype   String
     * @param status    String
     * @param condition String
     * @return BeforeStatus
     */
    public static String getBeforeStatus(String routeno, String bndtype, String status, String condition) {
        return BMRouteBind.getInstance().getBeforeStatus(routeno, bndtype, status, condition);
    }

    /**
     * ������һ���̶�Ӧ״̬��������һ�����û�ӵ�б������������̲����û��Ĳ���Ȩ�����
     * �����ǰ״̬��Ϊ�գ����¼�״̬Ϊ�գ��򷵻�""��
     * �磺�����û���Ӧ״̬Ϊ��5�����¼��û���Ӧ״̬Ϊ��5-6������ getNextStatusEnd �������ء�6����
     *
     * @param routeno   String
     * @param bndtype   String
     * @param status    String
     * @param condition String
     * @return NextStatusEnd
     */
    public static String getNextStatusEnd(String routeno, String bndtype, String status, String condition) {
        String nsStr = getNextStatus(routeno, bndtype, status, condition);
        if (nsStr.lastIndexOf(EnumValue.SPLIT_STR) > 0)
            nsStr = nsStr.substring(nsStr.lastIndexOf(EnumValue.SPLIT_STR) + 1, nsStr.length());
        return nsStr;
    }

    /**
     * ������һ���̶�Ӧ״̬��������һ�����û�ӵ�б������������̲����û��Ĳ���Ȩ�����
     * �����ǰ״̬��Ϊ�գ����ϼ�״̬Ϊ�գ��򷵻�""��
     * �磺�����û���Ӧ״̬Ϊ��6�����¼��û���Ӧ״̬Ϊ��4-5������ getBeforeStatusBegin �������ء�4����
     *
     * @param routeno   String
     * @param bndtype   String
     * @param status    String
     * @param condition String
     * @return BeforeStatusBegin
     */
    public static String getBeforeStatusBegin(String routeno, String bndtype, String status, String condition) {
        String nsStr = getBeforeStatus(routeno, bndtype, status, condition);
        if (nsStr.indexOf(EnumValue.SPLIT_STR) > 0)
            nsStr = nsStr.substring(0, nsStr.indexOf(EnumValue.SPLIT_STR));
        return nsStr;
    }

    /**
     * lj Created in 20090325
     *
     * @param bndid     String
     * @param condition String
     * @return XFStatusByBrhId
     */
    public static String getXFStatusByBrhId(String bndid, String condition) {
        return BMRouteBind.getInstance().getRouteBindStatusByLimit(EnumValue.RouteNo_XFSP,
                String.valueOf(EnumValue.BndType_WangDianDaiMa), bndid, condition);
    }

    /**
     * lj Created in 20090325
     *
     * @param bndid     String
     * @param condition String
     * @return XFStatusByUserId
     */
    public static String getXFStatusByUserId(String bndid, String condition) {
        return BMRouteBind.getInstance().getRouteBindStatusByLimit(EnumValue.RouteNo_XFSP,
                String.valueOf(EnumValue.BndType_YongHuDaiMa), bndid, condition);
    }

    /**
     * lj Created in 20090325
     *
     * @param bndid     String
     * @param condition String
     * @return XFStatusByBrhLvl
     */
    public static String getXFStatusByBrhLvl(String bndid, String condition) {
        return BMRouteBind.getInstance().getRouteBindStatusByLimit(EnumValue.RouteNo_XFSP,
                String.valueOf(EnumValue.BndType_BuMenJiBie), bndid, condition);
    }

    /**
     * lj Created in 20090325
     *
     * @param bndid     String
     * @param condition String
     * @return XFStatusByUserTp
     */
    public static String getXFStatusByUserTp(String bndid, String condition) {
        return BMRouteBind.getInstance().getRouteBindStatusByLimit(EnumValue.RouteNo_XFSP,
                String.valueOf(EnumValue.BndType_UserType), bndid, condition);
    }

    /**
     * lj Created in 20090325
     *
     * @param request   HttpServletRequest
     * @param condition String
     * @return XFStatusByLoginUser
     */
    public String getXFStatusByLoginUser(HttpServletRequest request, String condition) {
        HttpSession session = request.getSession(true);
        UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);

        return getXFStatusByUserTp(String.valueOf(um.getUser().getStatus()), condition);
    }

    /**
     * ������һ�����û�ӵ�в���״̬��
     * �����ǰ״̬��Ϊ�գ��򷵻ص�ǰ��ҵ�����̼������͵�Ĭ������״̬��
     * �磺�����û���Ӧ״̬Ϊ��5�����¼��û���Ӧ״̬Ϊ��5-6������ getNextStatusByUserTp �������ء�5-6����
     *
     * @param status    String
     * @param condition String
     * @return NextStatus
     */
    public static String getNextStatusByUserTp(String status, String condition) {
        String thisStaStr = getNextStatus(EnumValue.RouteNo_XFSP, String.valueOf(EnumValue.BndType_UserType), status, condition);
        if (status != null && !status.equals("") && thisStaStr.equals("")) thisStaStr = DefEndSta_XFSP_UserType;
        return thisStaStr;
    }

    /**
     * ������һ�����û�ӵ�в���״̬��
     * �����ǰ״̬��Ϊ�գ��򷵻ص�ǰ��ҵ�����̼������͵�Ĭ�����״̬��
     * �磺�����û���Ӧ״̬Ϊ��6�����¼��û���Ӧ״̬Ϊ��4-5������ getNextStatusByUserTp �������ء�4-5����
     *
     * @param status    String
     * @param condition String
     * @return NextStatus
     */
    public static String getBeforeStatusByUserTp(String status, String condition) {
        String thisStaStr = getBeforeStatus(EnumValue.RouteNo_XFSP, String.valueOf(EnumValue.BndType_UserType), status, condition);
        if (status != null && !status.equals("") && thisStaStr.equals("")) thisStaStr = DefBeginSta_XFSP_UserType;
        return thisStaStr;
    }

    /**
     * ������һ���̶�Ӧ״̬��������һ�����û�ӵ�б������������̲����û��Ĳ���Ȩ�����
     * �����ǰ״̬��Ϊ�գ��򷵻ص�ǰ��ҵ�����̼������͵�Ĭ������״̬��
     * �磺�����û���Ӧ״̬Ϊ��5�����¼��û���Ӧ״̬Ϊ��5-6������ getNextStatusEnd �������ء�6����
     *
     * @param status    String
     * @param condition String
     * @return NextStatusEnd
     */
    public static String getNextStatusEndByUserTp(String status, String condition) {
        String thisStaStr = getNextStatusEnd(EnumValue.RouteNo_XFSP, String.valueOf(EnumValue.BndType_UserType), status, condition);
        if (status != null && !status.equals("") && thisStaStr.equals("")) thisStaStr = DefEndSta_XFSP_UserType;
        return thisStaStr;
    }

    /**
     * ������һ���̶�Ӧ״̬��������һ�����û�ӵ�б������������̲����û��Ĳ���Ȩ�����
     * �����ǰ״̬��Ϊ�գ��򷵻ص�ǰ��ҵ�����̼������͵�Ĭ�����״̬��
     * �磺�����û���Ӧ״̬Ϊ��6�����¼��û���Ӧ״̬Ϊ��4-5������ getNextStatusEnd �������ء�4����
     *
     * @param status    String
     * @param condition String
     * @return BeforeStatusBegin
     */
    public static String getBeforeStatusBeginByUserTp(String status, String condition) {
        String thisStaStr = getBeforeStatusBegin(EnumValue.RouteNo_XFSP, String.valueOf(EnumValue.BndType_UserType), status, condition);
        if (status != null && !status.equals("") && thisStaStr.equals("")) thisStaStr = DefBeginSta_XFSP_UserType;
        return thisStaStr;
    }

    public static boolean hasWriteRoleByUserTp(String bndid, String status) {
        BMRouteBindNode dt = Workflow.getRouteBindByUserTp(bndid);
        if (dt != null) {//��õ�������Ϣ��Ϊ��
            String[] dts = dt.status.split(EnumValue.SPLIT_STR);
            for (String dt1 : dts) {
                if (status.equals(dt1)) return true;
            }
        }
        return false;
    }

    public static String statusToSql(String status) {
        if (status.indexOf(EnumValue.SPLIT_STR) > 0) status = status.replaceAll(EnumValue.SPLIT_STR, "','");
        return "'" + status + "'";
    }


}
