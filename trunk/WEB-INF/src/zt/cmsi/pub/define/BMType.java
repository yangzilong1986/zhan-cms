//Source file: e:\\java\\zt\\cmsi\\pub\\define\\BMType.java

package zt.cmsi.pub.define;

import zt.cmsi.mydb.MyDB;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.utils.Debug;

import java.util.HashMap;
import java.util.Iterator;

/**
 * ��BMType�����������壬��ʼ��ʱһ�ζ����ڴ� ���õ�̬ģʽ
 *
 * @author zhouwei $Date: 2005/06/28 07:00:39 $
 * @version 1.0 ��Ȩ���ൺ���칫˾
 * @created 2004��1��12��
 */

public class BMType {
    private static HashMap data = null;
    private static BMType ptr = null;


    /**
     * ����BMType���Ψһʵ��
     *
     * @return zt.cmsi.pub.define.BMType
     * @roseuid 3FE3E4770039
     */
    public static BMType getInstance() {
        if (ptr == null) {
            ptr = new BMType();
        }
        return ptr;
    }


    /**
     * ���췽������ʼ������
     */
    private BMType() {
        data = new HashMap();
        if (!loadData()) {
            Debug.debug(Debug.TYPE_ERROR, "��ȡҵ�����ͱ�����ʧ�ܣ�����");
        }
    }


    /**
     * �����ݿ���װ��ҵ������
     *
     * @return
     */
    private boolean loadData() {

        try {
            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            RecordSet rs = dc.executeQuery("select * from bmtype order by typeno");
            while (rs.next()) {
                BMTypeData bmtypedata = initDataBean(rs);
                if (bmtypedata == null) {
                    Debug.debug(Debug.TYPE_ERROR, "�������������ʧ��");
                    continue;
                }
                data.put(new Integer(bmtypedata.TypeNo), bmtypedata);
            }
        } catch (Exception e) {
            Debug.debug(Debug.TYPE_WARNING, e.toString());
            Debug.debug(e);
            return false;
        } finally {
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
    private BMTypeData initDataBean(RecordSet rs) {
        BMTypeData bmtypedata = new BMTypeData();
        try {
            bmtypedata.TypeNo = rs.getInt("typeno");
            bmtypedata.TypeName = rs.getString("typename");
            bmtypedata.Deleted = rs.getBoolean("deleted");
            bmtypedata.StartAct = new Integer(rs.getInt("startbmact"));
        } catch (Exception e) {
            Debug.debug(Debug.TYPE_WARNING, e.toString());
            Debug.debug(e);
            return null;
        }

        return bmtypedata;
    }


    /**
     * ���ݴ����ҵ�����ͱ�ţ����ҵ������
     *
     * @param TypeNo
     * @return String
     * @roseuid 3FE3E5AE0267
     */
    public String getTypeName(int TypeNo) {
        BMTypeData bmtypedata = (BMTypeData) data.get(new Integer(TypeNo));
        return (bmtypedata != null) ? bmtypedata.TypeName : null;
    }


    /**
     * ���ݴ����ҵ�����ͱ�ţ��ж�ҵ�������Ƿ����
     *
     * @param TypeNo
     * @return boolean
     * @roseuid 3FE3E73F012D
     */
    public boolean ifExist(int TypeNo) {
        BMTypeData bmtypedata = (BMTypeData) data.get(new Integer(TypeNo));
        return (bmtypedata != null) ? true : false;
    }


    /**
     * ���ݴ����ҵ�����ͱ�ţ��ж�ҵ�������Ƿ��Ѿ���ɾ�� --ҵ�����Ͳ�����ʱ��Ϊ��ɾ��
     *
     * @param TypeNo
     * @return boolean
     * @roseuid 3FE3E74C0171
     */
    public boolean ifDeleted(int TypeNo) {
        BMTypeData bmtypedata = (BMTypeData) data.get(new Integer(TypeNo));
        return (bmtypedata != null) ? bmtypedata.Deleted : true;
    }


    /**
     * ���е�ҵ�����ʹ���,�˷���Ϊ�޸���ǰ����Ʒ���
     *
     * @return
     * @roseuid 3FE3E897013B
     */
    public Iterator getSetOfBMType() {
        return ((HashMap) data.clone()).keySet().iterator();
    }


    /**
     * ���ݴ����ҵ�����ͱ�ţ��õ���ʼ����
     *
     * @param TypeNo
     * @return Integer
     * @roseuid 3FE6BE2A02C4
     */
    public Integer getStartAct(int TypeNo) {
        BMTypeData bmtypedata = (BMTypeData) data.get(new Integer(TypeNo));
        return (bmtypedata != null) ? bmtypedata.StartAct : null;
    }


    /**
     * The main program for the BMType class
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        BMType bmtype = BMType.getInstance();
        System.out.println(bmtype.getTypeName(1));

//     HashMap hm = new HashMap();
//     hm.put("1","aa");
//     hm.put("2","bb");
//     hm.put("3","cc");
//     hm.put("4","dd");
//
//     System.out.println(hm);
//     Iterator it = ((HashMap)(hm.clone())).keySet().iterator();
//     it.next();
//     it.remove();
//     System.out.println(hm);

    }
}
