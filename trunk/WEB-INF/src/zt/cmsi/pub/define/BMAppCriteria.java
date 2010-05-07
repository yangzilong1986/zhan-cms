package zt.cmsi.pub.define;
/**
 * ����������һ��ҵ�����͵ļ�������.
 *
 * @author zhouwei
 * $Date: 2005/06/28 07:00:39 $
 * @version 1.0
 *
 * ��Ȩ���ൺ���칫˾
 */

import zt.cms.pub.SCBranch;
import zt.cms.pub.SCUser;
import zt.cms.pub.code.SCUserRoleBranchImpl;
import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.ErrorCode;
import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.utils.Debug;

import java.util.HashMap;

public class BMAppCriteria {
    private static HashMap data = null;
    private static BMAppCriteria ptr = null;
    private static boolean isDirty = false;

    /**
     * ����BMAppCriteria���Ψһʵ��
     *
     * @roseuid 3FE3E4770039
     */
    public static BMAppCriteria getInstance() {
        if (ptr == null) {
            ptr = new BMAppCriteria();
        }
        if (isDirty) {
            ptr = new BMAppCriteria();
            isDirty = false;
        }
        return ptr;
    }

    /**
     * ���췽������ʼ������
     */
    private BMAppCriteria() {
        data = new HashMap();
        if (!loadData()) {
            Debug.debug(Debug.TYPE_ERROR, "��ȡ�����������ʧ�ܣ�����");
        }
    }

    public static void setDirty(boolean dirty) {
        isDirty = true;
    }

    /**
     * �����޸ĺ�ˢ�»�������ݣ��ֲ�ȡ�������µķ�ʽ��������һ������ʱ��ʵ�ʸ������ݡ�
     */
    public static void refresh() {
        SCUserRoleBranchImpl.setAllAppCriteriaDirty();
        isDirty = true;
        //BMAppCriteria.getInstance();//��������
    }

    /**
     * �����ݿ���װ��ҵ������
     */
    private boolean loadData() {

        try {
            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            RecordSet rs = dc.executeQuery("select * from BMAppCriteria");
            while (rs.next()) {
                BMAppCriteriaData bmappcriteriadata = initDataBean(rs);
                if (bmappcriteriadata == null) {
                    Debug.debug(Debug.TYPE_ERROR, "�������������ʧ��");
                    continue;
                }
                data.put(bmappcriteriadata.BrhID + "_" + bmappcriteriadata.CheckPoint + "_" + bmappcriteriadata.BMTypeNo, bmappcriteriadata);
            }
        }
        catch (Exception e) {
            Debug.debug(Debug.TYPE_WARNING, e.toString());
            Debug.debug(e);
            return false;
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
        }
        return true;
    }

    /**
     * ���ݱ�ṹ����ʼ��������
     */
    private BMAppCriteriaData initDataBean(RecordSet rs) {
        BMAppCriteriaData bmappcriteriadata = new BMAppCriteriaData();
        try {
            bmappcriteriadata.BrhID = rs.getString("brhid").trim();
            bmappcriteriadata.CheckPoint = rs.getInt("checkpoint");
            bmappcriteriadata.BMTypeNo = new Integer(rs.getInt("typeno"));
            bmappcriteriadata.AlertType = rs.getInt("alerttype");
        }
        catch (Exception e) {
            Debug.debug(Debug.TYPE_WARNING, e.toString());
            Debug.debug(e);
            return null;
        }

        return bmappcriteriadata;
    }

    /**
     * ����������һ��ҵ�����͵ļ�������(���̳��ϼ����������).
     */
    public int getAlertTypeofBrh(int CheckPoint, String BrhID, int BMTypeNo) {
        String brhid = BrhID.trim();
        while (true) {
            int alerttype = getDirectAlertTypeofBrh(CheckPoint, brhid, BMTypeNo);
            if (alerttype >= 0) {
                return alerttype;
            } else {
                brhid = SCBranch.getSupBrh(brhid);
                if (brhid == null) {
                    return ErrorCode.getAlertTypeofBrh_FAILED;
                }
            }
        }
    }

    /**
     * ����û�����һ��ҵ�����͵ļ�������(���̳��ϼ����������).
     */
    public int getAlertTypeofUsr(int CheckPoint, String loginName, int BMTypeNo) {
        String brhid = SCUser.getBrhId(loginName);
        if (brhid != null) {
            return getAlertTypeofBrh(CheckPoint, brhid, BMTypeNo);
        } else {
            return ErrorCode.getAlertTypeofUsr_FAILED;
        }
    }

    /**
     * ���ֱ���������һ��ҵ�����͵ļ�������(�����̳��ϼ����������).
     */
    private int getDirectAlertTypeofBrh(int CheckPoint, String BrhID, int BMTypeNo) {
        BMAppCriteriaData bcd = (BMAppCriteriaData) data.get(BrhID + "_" + CheckPoint + "_" + BMTypeNo);
        if (bcd != null) {
            return bcd.AlertType;
        } else {
            if ((bcd = (BMAppCriteriaData) data.get(BrhID + "_" +
                    CheckPoint + "_" + EnumValue.BMType_SuoYou)) != null) {
                return bcd.AlertType;
            } else {
                return -1;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(BMAppCriteria.getInstance().getAlertTypeofBrh(4, "907070160", 1));
    }
}
