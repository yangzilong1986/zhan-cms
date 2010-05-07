//Source file: e:\\java\\zt\\platform\\form\\config\\EnumerationType.java

package zt.platform.form.config;

import zt.platform.db.ConnectionManager;
import zt.platform.db.DBUtil;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 枚举类型 负责管理所有枚举实例，在static语句块中从表sysenuinfomain和sysenuinfodetl中加载所 有的枚举类型实例定义。
 *
 * @author 请替换
 * @version 1.0
 * @created 2003年10月11日
 */
public class EnumerationType {
    private static Map types;
    static Logger logger = Logger.getLogger("zt.platform.form.config.EnumerationType");

    static {
        System.out.println("EnumerationType initing ...");
        init();
        System.out.println("EnumerationType inited ok !");
    }

    /**
     * 验证值value是否在枚举类型type的取值范围内
     *
     * @param type
     * @param value
     * @return boolean
     * @roseuid 3F71657501F8
     */
//    public static boolean validate(String type, int value) {
    public static boolean validate(String type, String value) {
        EnumerationBean eb = getEnu(type);
        if (eb == null) {
            return false;
        } else {
//            Object o = eb.getValue(new Integer(value));
            Object o = eb.getValue(value);
            if (o == null) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 得到枚举类型type的实例 可以访问每个枚举值
     *
     * @param type
     * @return zt.platform.form.config.EnumerationBean
     * @roseuid 3F7165A503D7
     */
    public static EnumerationBean getEnu(String type) {
        return (EnumerationBean) types.get(type);
    }

    /**
     * Description of the Method
     */
    public static void init() {
        types = new HashMap();
        ConnectionManager manager = ConnectionManager.getInstance();
        DatabaseConnection con = manager.getConnection();
        String str = "select * from ptenuminfomain";
        RecordSet rs = con.executeQuery(str);
        while (rs.next()) {
            String enuid = DBUtil.fromDB(rs.getString("enuid"));
            String enuDesc = rs.getString("enuDesc");
            enuid = DBUtil.fromDB(enuid.trim());
            if (enuDesc == null) {
                enuDesc = "";
            } else {
                enuDesc = DBUtil.fromDB(enuDesc.trim());
            }
            EnumerationBean eb = new EnumerationBean(enuid);
            eb.setEnudesc(enuDesc);
            types.put(enuid, eb);
        }
        Iterator enus = types.values().iterator();
        String enuBeanStr = "select * from PTENUMINFODETL where enuid=";
        while (enus.hasNext()) {
            EnumerationBean eb = (EnumerationBean) enus.next();
            RecordSet enuRs = con.executeQuery(enuBeanStr + "'" + eb.getName() + "' order by enutp");
            while (enuRs.next()) {
//                eb.add(new Integer(enuRs.getInt("enutp")), DBUtil.fromDB(enuRs.getString("enudt").trim()));
                eb.add(enuRs.getString("enutp"), DBUtil.fromDB(enuRs.getString("enudt").trim()));
            }
        }
        con.commit();
        manager.releaseConnection(con);
    }
}
