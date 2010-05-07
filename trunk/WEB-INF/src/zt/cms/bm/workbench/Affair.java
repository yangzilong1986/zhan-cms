package zt.cms.bm.workbench;

/**
 *  Title: </p> <p>
 *@author     not attributable
 *@created    2003年12月30日
 *@version    1.0
 */

import java.text.*;
import java.util.*;

import zt.platform.form.config.*;

public class Affair {
  private int typeNo;
  private java.util.Calendar createDate;
  private String brhId;
  private String clientName;
  private int actStatus;
  private String operator;
  private int bmActType;
  private boolean viewed;
  private int bmTransNo;
  private String bmNo;

  public static int TRANS_STATUS_EXECUTING = 1;
  public static int TRANS_STATUS_COMPLETED = 2;
  public static int TRANS_STATUS_CANCELED = 3;
  public static int BM_STATUS_GRANTED = 7;
  private int bmStatusNo;
  private double appAmt;

  public Affair() {}

  public int getTypeNo() {
    return typeNo;
  }

  public void setTypeNo(int typeNo) {
    this.typeNo = typeNo;
  }

  public java.util.Calendar getCreateDate() {
    return createDate;
  }

  public void setCreateDate(java.util.Calendar createDate) {
    this.createDate = createDate;
  }

  public String getBrhId() {
    return brhId;
  }

  public void setBrhId(String brhId) {
    this.brhId = brhId;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public int getActStatus() {
    return actStatus;
  }

  public void setActStatus(int actStatus) {
    this.actStatus = actStatus;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public int getBmActType() {
    return bmActType;
  }

  public void setBmActType(int bmActType) {
    this.bmActType = bmActType;
  }

  public String getTypeName() {
    return (String) EnumerationType.getEnu("BMType").getValue(new Integer(getTypeNo()));
  }

  public String getBmActTypeName() {
    return (String) EnumerationType.getEnu("BMActType").getValue(new Integer(getBmActType()));
  }

  public String getActStatusName() {
    return (String) EnumerationType.getEnu("TransStatus").getValue(new Integer(getActStatus()));
  }

  public String getCreateDateName() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    return format.format(new Date(this.getCreateDate().getTime().getTime()));
  }

  public static int executing() {
    return 1;
  }

  public static int granted() {
    return 7;
  }

  public boolean isViewed() {
    return viewed;
  }

  public void setViewed(boolean viewed) {
    this.viewed = viewed;
  }

  public String getBmNo() {
    return bmNo;
  }

  public void setBmNo(String bmNo) {
    this.bmNo = bmNo;
  }

  public int getBmTransNo() {
    return bmTransNo;
  }

  public void setBmTransNo(int bmTransNo) {
    this.bmTransNo = bmTransNo;
  }

  public int getBmStatusNo() {
    return bmStatusNo;
  }

  public String getBmStatusName() {
    return (String) EnumerationType.getEnu("BMStatus").getValue(new Integer(getBmStatusNo()));
  }

  public void setBmStatusNo(int bmStatusNo) {
    this.bmStatusNo = bmStatusNo;
  }

  public double getAppAmt() {
    return appAmt;
  }

  public void setAppAmt(double appAmt) {
    this.appAmt = appAmt;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("---------------------------");
    sb.append("\n业务类型(typeno)：" + this.getTypeNo());
    sb.append("\n业务步骤类型(bmacttype)：" + this.getBmActType());
    sb.append("\n建立日期(createdate)：" + this.getCreateDate());
    sb.append("\n业务网点(BrhId)：" + this.getBrhId());
    sb.append("\n客户姓名(clientname)：" + this.getClientName());
    sb.append("\n业务状态(actStatus)：" + this.getActStatus());
    sb.append("\n最后维护人员(operator):" + this.getOperator());
    sb.append("\n察看过(viewed)" + this.isViewed());

    sb.append("\n+++++++");
    sb.append("\n业务步骤类型名称:" + this.getBmActTypeName());
    sb.append("\n业务类型名称:" + this.getTypeName());
    sb.append("\n业务状态名称:" + this.getActStatusName());
    sb.append("\n建立日期:" + this.getCreateDateName());

    sb.append("\n+++++++");
    sb.append("\nbmno:" + this.getBmNo());
    sb.append("\nbmtransno:" + this.getBmTransNo());
    sb.append("\nAppAmt:" + this.getAppAmt());

    sb.append("\n---------------------------");
    return sb.toString();
  }

}
