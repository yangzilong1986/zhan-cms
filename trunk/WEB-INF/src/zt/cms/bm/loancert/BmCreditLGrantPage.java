package zt.cms.bm.loancert;

/**
 * <p>Title: 信贷管理</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003 中天信息</p>
 * <p>Company: 中天信息</p>
 * @author YUSG
 * @version 1.0
 */
import java.math.*;

import zt.cmsi.client.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;

/******************************************************
 *
 *  贷款证发放详细
 *
 *****************************************************/
public class BmCreditLGrantPage extends FormActions {
  private String strFlag = null; //读写标志
  private Param paramg = null; //获得的变量集合
  private Param params = null; //发送的变量集合
  private String strClientNO = null; //客户号
  private String strUserName = null; //当前登陆用户名

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {

    paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (paramg == null) {
      msgs.add("数据不完整无法授权，请检查数据");
      return -1;
    }

    strFlag = (String) paramg.getParam(ParamName.Flag);
    strClientNO = (String) paramg.getParam(ParamName.CLientNo);

    if (strClientNO == null) {
      msgs.add("数据不完整无法授权，请检查数据");
      return -1;
    }
    else {
      UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.
        USER_INFO_NAME);
      try {
        strUserName = um.getUserName();
      }
      catch (Exception e) {
        e.printStackTrace();
      }

      int iCount = 0;
      String strSql = "select count(*) from bmcreditlimit where clientno='";
      strSql = strSql + strClientNO + "'";

      RecordSet rs = conn.executeQuery(strSql);
      if (rs.next()) {
        iCount = rs.getInt(0);
      }
      if (iCount > 0) {
        msgs.add("贷款证已经发放，请到贷款证维护模块进行维护！");
        return -1;
      }

      if (strFlag.equals(ParamName.Flag_WRITE)) {
        instance.setReadonly(false);
      }
      else {
        instance.setReadonly(true);
      }
      return 0;
    }
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    CMClient cmct = CMClientMan.getCMClient(strClientNO);
    String strName = cmct.name;

    instance.setValue("NAME", strName);
    instance.setValue("OPERATOR", strUserName);
    instance.setValue("CLIENTNO", strClientNO);
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    //BigDecimal bd = BMReviewLimit.getInstance().getReviewLimitofUsr(strUserName, EnumValue.BMType_DaiKuanZhengDaiKuan);
    BigDecimal bd = new BigDecimal(100000000);
    //above line is changed by GZL, we dont check review limit when issuing Loan Card

    String strLa = ctx.getParameter("CREDITLIMIT").trim();
    float fLa = Float.parseFloat(strLa);
    if (bd == null) {
      msgs.add("没有找到该用户所在网点的授信额度设置，请联系系统管理员！");
      return -1;
    }
    float fbd = bd.floatValue();

    if (fbd < fLa) {
      msgs.add("授信额度超过操作权限规定的最大额，请修改！");
      return -1;
    }
    return 0;
  }
  public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
        EventManager manager) {
    ctx.setRequestAtrribute("msg"," 贷款证发放成功");
    ctx.setTarget("/showinfo.jsp");

    return 0;
}

}
