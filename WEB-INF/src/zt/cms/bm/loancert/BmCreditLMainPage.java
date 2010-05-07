package zt.cms.bm.loancert;

/**
 * <p>Title: 贷款证维护详细</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003 中天信息</p>
 * <p>Company: 中天信息</p>
 * @author YUSG
 * @version 1.0
 */
import java.math.*;

import zt.cmsi.biz.*;
import zt.cmsi.pub.*;
import zt.cmsi.pub.define.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.cmsi.client.*;
import com.ebis.encrypt.*;

public class BmCreditLMainPage
    extends FormActions {
  private String strFlag = null; //读写标志
  private Param paramg = null; ///获得的变量集合
  private String strClientNO = null; //客户号
  private String strTypeNo = null; //业务类型代码
  private String strUserName = null; //当前登陆用户名
  private CMClient cmclient = null; //客户资料

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //Edit by wxj at 20040223
    strFlag=(String) ctx.getRequestAttribute("flag");
    strClientNO=(String) ctx.getRequestAttribute("CLIENTNO");
    if(strFlag==null || strClientNO==null)
    {
        paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);
        if (paramg == null) {
            msgs.add("参数错误，参数对象不存在！");
            return -1;
        }
        //flag
        strFlag = paramg.getParam(ParamName.Flag).toString();
        if (strFlag == null) {
            msgs.add("参数错误，窗体读写标志不存在！");
            return -1;
        }
        if (strFlag.equals(ParamName.Flag_WRITE)) {
            instance.setReadonly(false);
        }
        else {
            instance.setReadonly(true);
        }
        //strClientNO
        strClientNO = (String) paramg.getParam(ParamName.CLientNo);
        if (strClientNO == null) {
            msgs.add("参数错误，客户号不存在！");
            return -1;
        }
        //strTypeNo
        Object tmp = paramg.getParam(ParamName.BMType);
        strTypeNo = (tmp == null ? null : tmp.toString().trim());
    }
    else
    {
        instance.setReadonly(true);
        strTypeNo = "1";
    }
    //strUserName
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    try {
      strUserName = um.getUserName();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    //cmclient
    cmclient=CMClientMan.getCMClient(strClientNO);
    instance.setValue("CLIENTNO", strClientNO);
    instance.setValue("TYPENO", strTypeNo);
    instance.setValue("OPERATOR", strUserName);
    instance.setValue("CLIENTNAME",cmclient.name);

    //按照传入的客户号和业务类型代码查找记录,如果找到,则进入修改状态,没有则进入增加状态。
    if (strTypeNo != null && strTypeNo.length() > 0) {
      String formid = instance.getFormid();
      FormBean formBean = FormBeanManager.getForm(formid);
      String tblName = formBean.getTbl().trim();
      if (DBUtil.getCellValue(conn, tblName, "CLIENTNO", "CLIENTNO='" + strClientNO + "' and TYPENO=" + strTypeNo) != null) {
        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                Event.BRANCH_CONTINUE_TYPE);
      }
      else{
        ctx.setRequestAtrribute("title", "贷款证查询");
        ctx.setRequestAtrribute("msg", "对不起，该客户的贷款证不存在！");
        ctx.setRequestAtrribute("flag", "0");
        ctx.setTarget("/showinfo.jsp");
      }
    }
    return 0;
  }

  public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    //Edit by wxj at 20040223
    String str = "";
    CreditLimitData cldata = CreditLimit.getCreditLimit(strClientNO, Integer.parseInt(strTypeNo));
    if (cldata != null) {
      String strKyjeInt = null; //可用金额的整数部分
      String strKyjeDec = null; //可用金额的小数部分
      float fKyje = cldata.creditLimit.floatValue() - cldata.loanBal.floatValue();
      String strKyje = String.valueOf(fKyje);

      int iLength = strKyje.length();
      int iDot = strKyje.indexOf(".");
      if (iLength > iDot + 2) {
        strKyjeInt = strKyje.substring(0, iDot);
        strKyjeDec = strKyje.substring(iDot + 1, iDot + 3);
      }
      else if (iLength > iDot + 1) {
        strKyjeInt = strKyje.substring(0, iDot);
        strKyjeDec = strKyje.substring(iDot + 1, iDot + 2);
        strKyjeDec = strKyjeDec + "0";
      }
      else {
        strKyjeInt = strKyje.substring(0, iDot);
        strKyjeDec = "00";
      }
      str = strKyjeInt + "." + strKyjeDec;
    }
    String strDate = SystemDate.getSystemDate5(null);
    instance.setValue("LASTMODIFIED", strDate);
    instance.setValue("KYJE", str);
    return 0;
  }

  public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    //BigDecimal bd = BMReviewLimit.getInstance().getReviewLimitofUsr(strUserName, EnumValue.BMType_DaiKuanZhengDaiKuan);
    BigDecimal bd = new BigDecimal(100000000);
    //above line is changed by GZL, we dont check review limit when issuing Loan Card

    String strLa = ctx.getParameter("CREDITLIMIT").trim();
    String passwd = ctx.getParameter("PASSWD").trim();
    if(passwd.length() <= 0) passwd = null;

    if(passwd != null && passwd.length() < 6 )
    {
      msgs.add("密码长度必须为6位！");
      return -1;
    }

    if(passwd != null && passwd.length() <= 10)
    {
      EncryptData ed = new EncryptData();
      String encrypted = new String(ed.enPasswd(passwd.getBytes()));
      assistor.setSqlFieldValue(assistor.getDefaultTbl(), "PASSWD",encrypted);
    }


    float fLa = Float.parseFloat(strLa);
    float fbd = bd.floatValue();

    if (fbd < fLa) {
      msgs.add("授信额度超过操作权限规定的最大额，请修改！");
      return -1;
    }
    return 0;
  }
}
