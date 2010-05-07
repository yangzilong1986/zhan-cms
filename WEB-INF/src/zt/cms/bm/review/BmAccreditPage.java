package zt.cms.bm.review;

/**
 * <p>Title: 授权发放</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003  中天信息</p>
 * <p>Company: 中天信息</p>
 * @author YUSG
 * @modified by wxj at 20040509  1授权的金额取最终审批的金额而不是申请金额 2按钮只读处理 3流程调整简化
*  @modifide by yusg at 20041227 以资抵债不打印凭证
 * @version 1.0
 */
import zt.cmsi.biz.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;

public class BmAccreditPage extends FormActions {
  private String strFlag = null; //读写标志
  public Param params = null; //发送的变量集合
  private Param paramg = null; //获得的变量集合
  private String strBmNo = null; //业务号
  private String strBmTransNo = null; //业务明细号
  private String strUserName = null; //当前登陆用户名
  private String strBmActType = null; //审批级别
  private String strBmType = null; //贷款类型
  private boolean isFromList=false;

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {

    paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (paramg == null) {
      msgs.add("参数错误，参数对象不存在！");
      return -1;
    }
    //readonly?
    strFlag = paramg.getParam(ParamName.Flag).toString();
    if (strFlag.equals( (String) ParamName.Flag_WRITE)) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
      instance.useCloneFormBean();
      FormBean fb = instance.getFormBean();
      ElementBean eb1 = fb.getElement("cancel");
      ElementBean eb2 = fb.getElement("put");
      eb1.setReadonly(true);
      eb2.setReadonly(true);
    }
    //strBmNo，strBmTransNo
    strBmNo = paramg.getParam(ParamName.BMNo).toString();
    strBmTransNo = paramg.getParam(ParamName.BMTransNo).toString();
    if (strBmNo == null || strBmTransNo == null) {
      msgs.add("参数错误，业务号或业务明细号不存在！");
      return -1;
    }
    //strBmActType，strBmType
    strBmActType = paramg.getParam(ParamName.BMActType).toString();
    strBmType = paramg.getParam(ParamName.BMType).toString();
    //fromList
    if(ctx.getRequestAttribute("fromList") != null){
      isFromList=true;
    }
    instance.setValue("BMNO", strBmNo);
    trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    return 0;
  }

  public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    UpToDateApp data = BMTable.getUpToDateApp(this.strBmNo);
    String finalAmt = null;
    if (data != null) {
      if (data.finalAmt != null) {
        finalAmt = data.finalAmt.toString();
      }
    }
    if (finalAmt == null) {
      msgs.add("系统错误，无法取到最终审批金额！");
      return -1;
    }
    instance.setValue("APPAMT", data.finalAmt.toString());
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    strUserName = um.getUserName();
    params = new Param();
    params.addParam(ParamName.Flag, strFlag);
    params.addParam(ParamName.BMNo, strBmNo);
    params.addParam(ParamName.BMTransNo, strBmTransNo);
    params.addParam(ParamName.BMType, strBmType);
    params.addParam(ParamName.BMActType, strBmActType);
    ctx.setRequestAtrribute(ParamName.ParamName, params);
    //查看历史审批记录
    if (button.equals("BmTrans")) {
      trigger(manager, "BMTRANSLIST", null);
      return 0;
    }
    //发放
    else if (button.equals("put"))
    {
      if(isFromList){
        ctx.setRequestAtrribute("title", "发放授权");
        ctx.setRequestAtrribute("msg", "只读权限无法发放授权！");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
        return 0;
      }
      int iFlag = BMTrans.compltTrans(strBmNo, Integer.parseInt(strBmTransNo), strUserName);
      if (iFlag >= 0) {
        ctx.setRequestAtrribute("BMNO", this.strBmNo);
        if (strBmType.trim().equals("11") || strBmType.trim().equals("16")) {
          //授信,以资抵债类型 授权不打印
          ctx.setRequestAtrribute("msg", "授权成功！");
          ctx.setRequestAtrribute("flag", "1");
          ctx.setTarget("/showinfo.jsp");
          return 0;
        }
        else if (strBmType.trim().equals("12")) {
          //承兑汇票打印－－签发银行承兑汇票通知书
          ctx.setTarget("/jspreport/loanbizpo.jsp");
        }
        else if (strBmType.trim().equals("17")) {
          //不良贷款核销授权不打印；
          ctx.setRequestAtrribute("title", "不良贷款授权");
          ctx.setRequestAtrribute("msg", "授权成功！");
          ctx.setRequestAtrribute("flag", "1");
          ctx.setTarget("/showinfo.jsp");
        }
        else {
          //BMType(13,14)                 票据授权书
          //BMType(除11,12,13,14,16,17外)     借款凭证
          ctx.setTarget("/jspreport/loanbiz.jsp");
        }
        return 0;
      }
      return iFlag;
    }
    //取消授权
    else if (button.equals("cancel")) {
      if(isFromList){
        ctx.setRequestAtrribute("title", "取消授权");
        ctx.setRequestAtrribute("msg", "只读权限无法取消授权！");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
        return 0;
      }
      int iFlag = BMTrans.cancelBMTrans(strBmNo, Integer.parseInt(strBmTransNo), strUserName);
      if (iFlag >= 0) {
        ctx.setRequestAtrribute("title", "取消授权");
        ctx.setRequestAtrribute("msg", "授权取消成功！");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
        return 0;
      }
      return iFlag;
    }
    return 0;
  }
}
