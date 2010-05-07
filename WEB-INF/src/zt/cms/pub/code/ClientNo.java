package zt.cms.pub.code;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ClientNo {
  /**
   * 获取下一个序列号
   */
  static long sn=-1;
  public static synchronized long getNextSN() {
    if(sn>=0){
      return ++sn;
    }
    else{
      sn=getCurSN();
      return ++sn;
    }
  }
  /**
   * 获取当前序列号
   */
  public static synchronized long getCurSN() {
    if(sn>=0){
      return sn;
    }
    else{
      long sn1=SerialNumber.getCurSN("cmcorpclient","clientno");
      long sn2=SerialNumber.getCurSN("cmindvclient","clientno");
      if(sn1<sn2){
        sn=sn2;
      }
      else{
        sn=sn1;
      }
      return sn;
    }
  }
}