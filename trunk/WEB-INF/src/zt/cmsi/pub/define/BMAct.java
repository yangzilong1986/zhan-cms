package zt.cmsi.pub.define;

/**
 * ��BMAct�����������壬��ʼ��ʱһ�ζ����ڴ�
 * ���õ�̬ģʽ
 *
 * @author zhouwei
 * $Date: 2005/06/28 07:00:39 $
 * @version 1.0
 *
 * ��Ȩ���ൺ���칫˾
 */

import zt.cmsi.mydb.MyDB;
import zt.platform.db.DBUtil;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;

import java.util.HashMap;

public class BMAct {
    private static HashMap data = null;
    private static BMAct ptr = null;

    /**
     * ����BMAct���Ψһʵ��
     *
     * @return zt.cmsi.pub.define.BMAct
     * @roseuid 3FE40E2E0157
     */
    public static BMAct getInstance() {
        if (ptr == null) {
            ptr = new BMAct();
            return ptr;
        } else {
            return ptr;
        }
    }

    /**
     * ���췽������ʼ������
     */
    private BMAct() {
        System.out.println("BMAct initing ...");
        data = new HashMap();
        loadData();
        System.out.println("BMAct inited ok !");
    }

    /**
     * �����ݿ���װ��ҵ������
     *
     * @return
     */
    private boolean loadData() {
        try {
            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            RecordSet rs = dc.executeQuery("select * from bmact");
            while (rs.next()) {
                BMActData bmactdata = initDataBean(rs);
                if (bmactdata == null) {
                    System.exit(0);
                }
                data.put(new Integer(bmactdata.BMActType), bmactdata);
            }
        }
        catch (Exception e) {
            System.out.println("�쳣��" + DBUtil.fromDB(e.getMessage()));
            System.exit(0);
            return false;
        }
        finally {
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
    private BMActData initDataBean(RecordSet rs) {
        BMActData bmactdata = new BMActData();
        try {
            bmactdata.BMActType = rs.getInt("bmacttype");
            bmactdata.BMActName = rs.getString("funcname");
        }
        catch (Exception e) {
            System.out.println("�쳣��" + DBUtil.fromDB(e.toString()));
            return null;
        }
        return bmactdata;
    }
}
