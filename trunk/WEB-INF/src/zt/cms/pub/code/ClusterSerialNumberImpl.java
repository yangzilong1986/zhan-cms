package zt.cms.pub.code;
import com.caucho.hessian.server.HessianServlet;
public class ClusterSerialNumberImpl extends HessianServlet implements ClusterSerialNumber {
  public static int i = 0;
  static Object o =new Object();

  public synchronized long getNextSN(String tableName, String columnName) {
    return SerialNumber.getNextSNLocal(tableName,columnName);
  }

}
