//Source file: e:\\java\\zt\\cmsi\\pub\\define\\BMType.java

package zt.cmsi.pub.define;

import zt.cmsi.mydb.MyDB;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.utils.Debug;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 对BMType的数据作缓冲，初始化时一次读入内存 采用单态模式
 *
 * @author zhouwei $Date: 2005/06/28 07:00:39 $
 * @version 1.0 版权：青岛中天公司
 * @created 2004年1月12日
 */

public class BMType {
    private static HashMap data = null;
    private static BMType ptr = null;


    /**
     * 返回BMType类的唯一实例
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
     * 构造方法，初始化数据
     */
    private BMType() {
        data = new HashMap();
        if (!loadData()) {
            Debug.debug(Debug.TYPE_ERROR, "读取业务类型表数据失败！！！");
        }
    }


    /**
     * 从数据库中装入业务数据
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
                    Debug.debug(Debug.TYPE_ERROR, "解析结果集数据失败");
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
     * 根据表结构，初始化数据类
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
     * 根据传入的业务类型编号，获得业务名称
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
     * 根据传入的业务类型编号，判断业务类型是否存在
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
     * 根据传入的业务类型编号，判断业务类型是否已经被删除 --业务类型不存在时认为已删除
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
     * 所有的业务类型代号,此方法为修改以前的设计方法
     *
     * @return
     * @roseuid 3FE3E897013B
     */
    public Iterator getSetOfBMType() {
        return ((HashMap) data.clone()).keySet().iterator();
    }


    /**
     * 根据传入的业务类型编号，得到开始动作
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
