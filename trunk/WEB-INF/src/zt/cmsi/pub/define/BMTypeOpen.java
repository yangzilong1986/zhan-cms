package zt.cmsi.pub.define;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import zt.cms.pub.SCBranch;
import zt.cms.pub.SCUser;
import zt.cms.pub.code.SCUserRoleBranchImpl;
import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.db.DatabaseConnection;
import zt.platform.utils.Debug;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class BMTypeOpen {
    private static HashMap data = null;
    private static BMTypeOpen ptr = null;
    private static boolean isDirty = false;

    /**
     * ����BMReviewLimit���Ψһʵ��
     *
     * @return zt.cmsi.pub.define.BMReviewLimit
     * @roseuid 3FE3F4F00390
     */
    public static BMTypeOpen getInstance() {
        if (ptr == null) {
            ptr = new BMTypeOpen();
        }
        if (isDirty) {
            ptr = new BMTypeOpen();
            isDirty = false;
        }
        return ptr;
    }

    /**
     * ���췽������ʼ������
     */
    private BMTypeOpen() {
        data = new HashMap();
        if (!loadData()) {
            Debug.debug(Debug.TYPE_ERROR, "��ȡҵ�񿪿��ױ�����ʧ�ܣ�����");
        }
    }

    public static void setDirty(boolean dirty) {
        isDirty = true;
    }


    /**
     * �����޸ĺ�ˢ�»�������ݣ��ֲ�ȡ�������µķ�ʽ��������һ������ʱ��ʵ�ʸ������ݡ�
     */
    public static void refresh() {
        SCUserRoleBranchImpl.setAllTypeOpenDirty();
        isDirty = true;
        //BMTypeOpen.getInstance();//��������
    }

    /**
     * �����ݿ���װ��ҵ������
     *
     * @return
     */
    private boolean loadData() {
        Connection con = null;
        Statement st = null;
        try {
            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            con = dc.getConnection();
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from BMTypeOpen");
            while (rs.next()) {
                BMTypeOpenData bmreviewdata = initDataBean(rs);
                if (bmreviewdata == null) {
                    Debug.debug(Debug.TYPE_ERROR, "�������������ʧ��");
                    continue;
                }
                data.put("" + bmreviewdata.brhid + "_" + bmreviewdata.brhLevel + "_" + bmreviewdata.typeNo, bmreviewdata);
            }
        }
        catch (Exception e) {
            Debug.debug(Debug.TYPE_WARNING, e.toString());
            Debug.debug(e);
            return false;
        }
        finally {
            if (st != null) {
                try {
                    st.close();
                }
                catch (Exception e) {
                    Debug.debug(Debug.TYPE_WARNING, "Close Statement Failed!!!");
                    if (Debug.isDebugMode == true) {
                        Debug.debug(e);
                    }
                }
            }
            MyDB.getInstance().apReleaseConn(0);
        }
        return true;
    }

    /**
     * ���ݱ�ṹ����ʼ��������
     *
     * @param rs
     * @return
     */
    private BMTypeOpenData initDataBean(ResultSet rs) {
        BMTypeOpenData bmreviewdata = new BMTypeOpenData();
        try {
            bmreviewdata.brhid = rs.getString("brhid") == null ? null : rs.getString("brhid").trim();
            bmreviewdata.typeNo = rs.getInt("typeno");
            bmreviewdata.ifOpen = new Boolean(rs.getBoolean("open"));
            bmreviewdata.brhLevel = rs.getInt("BrhLevel");
        }
        catch (Exception e) {
            Debug.debug(Debug.TYPE_WARNING, e.toString());
            Debug.debug(e);
            return null;
        }

        return bmreviewdata;
    }

    /**
     * ����������һ��ҵ�������Ƿ񿪰�(���̳��ϼ����������).
     */
    public boolean ifBizOpenofBrh(String BrhID, int BMTypeNo) {
        String brhid = BrhID.trim();
        int brhlevel = 0;

        while (true) {
            try {
                brhlevel = Integer.parseInt(SCBranch.getBrhlevel(brhid));
            }
            catch (Exception e) {
            }
            Boolean isopen = ifDirectBizOpenofBrh(brhid, brhlevel, BMTypeNo);
            //System.out.println(brhid+"_"+brhlevel+"_"+BMTypeNo+":"+isopen);
            if (isopen != null) {
                return isopen.booleanValue();
            }
            //�ϼ�
            brhid = SCBranch.getSupBrh(brhid);
            if (brhid == null) {
                if (brhlevel > 0) {
                    brhlevel = 0;
                } else {
                    return false;
                }
            }
        }
    }

    /**
     * @param BrhID
     * @param BMTypeNo
     * @return boolean
     * @roseuid 3FE3F590019B
     */
    public boolean ifBizOpenofUsr(String loginName, int BMTypeNo) {
        String brhid = SCUser.getBrhId(loginName);
        if (brhid != null) {
            return ifBizOpenofBrh(brhid, BMTypeNo);
        } else {
            return false;
        }
    }

    /**
     * ���ֱ���������һ��ҵ�������Ƿ񿪰�(�����̳��ϼ����������).
     */
    private Boolean ifDirectBizOpenofBrh(String BrhID, int brhlevel, int BMTypeNo) {
        BMTypeOpenData bmreviewdata = null;
        //ĳһ����(���ֵȼ�)ĳһ����
        bmreviewdata = (BMTypeOpenData) data.get(BrhID + "_" + EnumValue.BrhLevel_SuoYou + "_" + BMTypeNo);
        if (bmreviewdata != null) {
            return bmreviewdata.ifOpen;
        }
        //ĳһ����(���ֵȼ�)��������
        bmreviewdata = (BMTypeOpenData) data.get(BrhID + "_" + EnumValue.BrhLevel_SuoYou + "_" + EnumValue.BMType_SuoYou);
        if (bmreviewdata != null) {
            return bmreviewdata.ifOpen;
        }
        //��������_ĳһ�ȼ�_ĳһ����
        bmreviewdata = (BMTypeOpenData) data.get("null_" + brhlevel + "_" + BMTypeNo);
        if (bmreviewdata != null) {
            return bmreviewdata.ifOpen;
        }
        //��������_ĳһ�ȼ�_��������
        bmreviewdata = (BMTypeOpenData) data.get("null_" + brhlevel + "_" + EnumValue.BMType_SuoYou);
        if (bmreviewdata != null) {
            return bmreviewdata.ifOpen;
        }
        //��������_���еȼ�_��������
        bmreviewdata = (BMTypeOpenData) data.get("null_" + EnumValue.BrhLevel_SuoYou + "_" + EnumValue.BMType_SuoYou);
        if (bmreviewdata != null) {
            return bmreviewdata.ifOpen;
        }
        return null;
    }

    public static void main(String[] args) {
        SCBranch.getBrhlevel("907020599");
        System.out.println(BMTypeOpen.getInstance().ifBizOpenofBrh("907060500", 1));
    }
}
