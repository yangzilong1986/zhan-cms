/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMBillDisHouorMng.java,v 1.1 2005/06/28 07:00:20 jgo Exp $
 *  modified by yusg  2004/4/27   -----report print
 */
package zt.cms.bm.bill;

import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.control.SessionContext;
import zt.platform.form.control.FormActions;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cmsi.biz.*;
import zt.cmsi.pub.define.*;
import zt.cmsi.mydb.*;
import zt.cmsi.pub.*;
import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.form.config.*;
import com.zt.util.PropertyManager;
import java.util.Calendar;
import java.math.BigDecimal;
import zt.platform.utils.Debug;

/**
 *  测试：多表－－－数据添加,修改，删除(获得值)
 *
 *@author     Administrator
 *@created    2003年12月2日
 */
public class BMBillDisHouorMng
    extends FormActions {

//  private String clientno;

  private String strFlag = null;
  private boolean isreadonly = true;
  private boolean isAccept = false;
  private String strbilldisno = null;
  private String strBmNo = null;
  private String strifadvanced = null;
  private String formid = null;
  private String strUserName = null;
  private Param pm = null;

  /**
   *  Description of the Method
   *
   *@param  ctx        Description of the Parameter
   *@param  conn       Description of the Parameter
   *@param  instance   Description of the Parameter
   *@param  msgs       Description of the Parameter
   *@param  manager    Description of the Parameter
   *@param  parameter  Description of the Parameter
   *@return            Description of the Return Value
   */
  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {

    pm = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (pm == null) {
      msgs.clear();
      msgs.add("接口参数不存在.");
      return -1;
    }
    strbilldisno = (String) pm.getParam("BILLDISNO");
    strBmNo = (String) pm.getParam(ParamName.BMNo);
    strFlag = (String) pm.getParam(ParamName.Flag);
    formid = (String) pm.getParam("FORMID");

    //*formid = (String)ctx.getRequestAttribute(SessionAttributes.REQUEST_FORM_ID_NAME);
    if (formid != null && formid.equals("BMZTX1")) {
      ctx.setHead("转贴现垫款处理-详细");
    }
    //*strFlag = String.valueOf(ctx.getRequestAttribute("flag"));
     //*strBmNo = ctx.getParameter("BMNO");
      if (strFlag.equals("read") || strFlag == null) {
        instance.setReadonly(true);
        //System.out.println("realonly:true");
        this.isreadonly = true;
      }
      else {
        instance.setReadonly(false);
        //System.out.println("realonly:false");
        this.isreadonly = false;
      }
    // System.out.println("7345"+String.valueOf(ctx.getRequestAttribute("flag")));
    //*strbilldisno = ctx.getParameter("BILLDISNO");
     this.isAccept = Function.isDataExist(conn, "BMBILLDISHOUOR", "BILLDISNO",
                                          strbilldisno,
                                          Function.INTEGER_TYPE);
    System.out.println("isAccept:" + isAccept);
    if (isAccept) {
      //System.out.println("1111:"+stracptbillno);
      if (strbilldisno != null) {
        instance.setValue("BILLDISNO", strbilldisno.trim());
      }
      if (!instance.isReadonly()) {
        instance.setReadonly(true);
      }
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    else {
      if (this.isreadonly == true) {
        msgs.clear();
        msgs.add("贴现票据还未发生到期贴现汇！");
        return -1;
      }

      //System.out.println("acpt:" + stracptbillno);
      //instance.setValue("ACPTBILLNO", stracptbillno);
      trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }

    return 0;
  }

  /**
   *  Description of the Method
   *
   *@param  ctx       Description of the Parameter
   *@param  conn      Description of the Parameter
   *@param  instance  Description of the Parameter
   *@param  msgs      Description of the Parameter
   *@param  manager   Description of the Parameter
   *@return           Description of the Return Value
   */
  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    if (this.isreadonly == true) {
      msgs.clear();
      msgs.add("查看状态下不允许运行本功能！");
      return -1;
    }

    if (!this.isAccept) {

      SystemDate sd = new SystemDate();
      String strcurrdate = sd.getSystemDate5("");
      instance.setValue("CREATEDATE", strcurrdate);
      instance.setFieldReadonly("CREATEDATE", true);

      instance.setValue("BILLDISNO", strbilldisno);
      instance.setValue("DISOBJECT", "");
      UpToDateApp data = null;
      if (this.strBmNo != null) {
        data = BMTable.getUpToDateApp(this.strBmNo);
      }
      if (data == null) {
        msgs.clear();
        msgs.add("没有取到贷款台帐记录数据！");
        return -1;
      }
      else {
        String strfirstResp = data.firstResp;
        String strfirstRespPct = data.firstRespPct.toString();

        instance.setValue("FIRSTRESP", strfirstResp);
        instance.setValue("FISRTRESPPCT", strfirstRespPct);
      }
      strUserName = Function.getUserName(ctx);
      if (strUserName != null) {
        instance.setValue("OPERATOR", strUserName);
      }
      else {
        return -1;
      }
      FormBean formBean = FormBeanManager.getForm(instance.getFormid());
      ElementBean eb = formBean.getElement("CANCELOPERATION");
      if (formid != null && formid.equals("BMZTX1")) {
        instance.setValue("IFDIS", 0);

        eb.setCaption("取消转贴现");
      }
      else if (formid == null) {
        instance.setValue("IFDIS", 1);
        eb.setCaption("取消贴现");
      }

      instance.setValue("DISOBJECT", formid);

    }

    return 0;
  }

  /**
   *  Description of the Method
   *
   *@param  ctx       Description of the Parameter
   *@param  conn      Description of the Parameter
   *@param  instance  Description of the Parameter
   *@param  msgs      Description of the Parameter
   *@param  manager   Description of the Parameter
   *@param  assistor  Description of the Parameter
   *@return           Description of the Return Value
   */
  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance,
                       ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {

    strifadvanced = ctx.getParameter("IFADVANCED");
    if(strifadvanced.equals(String.valueOf(EnumValue.YesNo_Yes)))
    {
    String chgtype, frate;
    String startdate="", enddate="";
    chgtype = ctx.getParameter("CHGTYPE");
    java.util.Calendar startDate, endDate;
    startdate = ctx.getParameter("STARTDATE");
    enddate = ctx.getParameter("ENDDATE");
    frate = ctx.getParameter("FRATE");
    System.out.println("---------------------" + chgtype + frate + startdate+enddate);

    long startlong, endlong;
    Calendar c = Calendar.getInstance();
     String[] d;
     //    新加条件判断by sunzg 10-20
       if ((startdate != null && startdate.length() > 0)&&(enddate != null && enddate.length() > 0) && frate != null && frate.length() >0) {
         d = new String[3];
         d[0] = startdate.substring(0, 4);
         d[1] = startdate.substring(4, 6);
         d[2] = startdate.substring(6, 8);
         c.set(Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1,
               Integer.parseInt(d[2]));
         startDate = c;
         startlong = startDate.getTimeInMillis();

         d[0] = enddate.substring(0, 4);
         d[1] = enddate.substring(4, 6);
         d[2] = enddate.substring(6, 8);
         c.set(Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1,
               Integer.parseInt(d[2]));
         endDate = c;
         endlong = endDate.getTimeInMillis();

         int dates = 0;
         dates = (int) ( (endlong - startlong) / 1000 / 3600 / 24);


           //if (chgtype.compareToIgnoreCase("RATECHG") == 0) {
           if(true)
           {
             if (frate != null && dates != 0 && frate.length() > 0) {
               //    int appMonthInt;
               BigDecimal fratef = null;
               try {
                 //      appMonthInt = Integer.parseInt(appmonths);
                 fratef = new BigDecimal(frate);
                 //        此处修改了，精确度 modify by sdj   10-20
                 fratef = fratef.divide(new BigDecimal(100), 4,
                                        BigDecimal.ROUND_CEILING);
               }
               catch (Exception e) {
                 Debug.debug(e);
                 msgs.add("申请期限或浮动利率数据格式错误！");
                 return -1;
               }
               BMBaseInst baseint = BMBaseInst.getBaseInst(EnumValue.BMType_TieXian, dates);
               if (baseint == null) {
                 instance.setValue("BRATE", "");
                 instance.setValue("RATE", "");
                 msgs.add("未取得基准利率设置，请检查系统的基准利率设置！");
                 return -1;
               }

               System.out.println("baseint ===================" +
                                  baseint.interest);

               instance.setValue("BRATE", "" + baseint.interest);
               assistor.setSqlFieldValue(assistor.getDefaultTbl(),"BRATE","" + baseint.interest);
               BigDecimal finalrate = baseint.interest.multiply(fratef.add(new
                   BigDecimal(1)));
               instance.setValue("RATE", "" + finalrate);
               assistor.setSqlFieldValue(assistor.getDefaultTbl(),"RATE", "" + finalrate);
               msgs.add("已经获得基准利率：成功.请继续输入.");
               if(chgtype != null) return -1;
             }
             else {
               msgs.add("请继续输入...");
               return -1;
             }
           }

       }
       else {
         msgs.add("请继续输入...");
         return -1;
       }

    }

    String strrate = ctx.getParameter("RATE");
    if (strrate != null && !strrate.equals("")) {
      if (Float.parseFloat(strrate) <= 0) {
        msgs.add("不允许\"利率\"<=0。");
        return -1;
      }
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    if (button == null || button.equals("")) {
      msgs.clear();
      msgs.add("请求的服务不存在！");
      return -1;
    }
    if (button.equals("SAVEBUTTON")) {
      if (this.isreadonly == true) {
        msgs.clear();
        msgs.add("查看状态下不允许运行本功能！");
        return -1;
      }

      trigger(manager, instance, EventType.INSERT_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    else if (button.equals("CANCELOPERATION")) {
      if (this.isreadonly == true) {
        msgs.clear();
        msgs.add("查看状态下不允许运行本功能！");
        return -1;
      }

      String text = Function.getFormTitleByInstance(instance);
      strUserName = Function.getUserName(ctx);
      int a = -1;
      if (formid == null) {
        if ( (a = BMTrans.disBillRecvVoid(Integer.parseInt(strbilldisno),
                                          strUserName)) < 0) {
          ctx.setRequestAtrribute("title", text);
          ctx.setRequestAtrribute("msg", "取消操作失败！（错误：" +PropertyManager.getProperty(a+"") + "）");
          ctx.setRequestAtrribute("flag", "0");
          ctx.setRequestAtrribute("isback", "0");
          ctx.setTarget("/showinfo.jsp");
        }
        else {
          ctx.setRequestAtrribute("title", text);
          ctx.setRequestAtrribute("msg", "取消操作成功！");
          ctx.setRequestAtrribute("flag", "1");
          ctx.setRequestAtrribute("isback", "0");
          ctx.setTarget("/showinfo.jsp");
        }
      }
      else {
        text = "转贴现垫款处理-详细";
        if ( (a = BMTrans.redisBillRecvVoid(Integer.parseInt(strbilldisno),
                                            strUserName)) < 0) {
          ctx.setRequestAtrribute("title", text);
          ctx.setRequestAtrribute("msg", "取消操作失败！（错误码：" + a + "）");
          ctx.setRequestAtrribute("flag", "0");
          ctx.setRequestAtrribute("isback", "0");
          ctx.setTarget("/showinfo.jsp");
        }
        else {
          ctx.setRequestAtrribute("title", text);
          ctx.setRequestAtrribute("msg", "取消操作成功！");
          ctx.setRequestAtrribute("flag", "1");
          ctx.setRequestAtrribute("isback", "0");
          ctx.setTarget("/showinfo.jsp");
        }
      }
    }

    return 0;
  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    strifadvanced = ctx.getParameter("IFADVANCED");

    String text = Function.getFormTitleByInstance(instance);
    MyDB.getInstance().addDBConn(conn);
    int a = -1;

    if (formid == null) { //贴现垫款
      if ( (a = BMTrans.disBillRecv(Integer.parseInt(strbilldisno), strUserName)) <
          0) {
        System.out.println("a(error):" + a);
        msgs.add("错误:" + a);
        MyDB.getInstance().releaseDBConn();
        return a;
      }
      else {
        System.out.println("a(success):" + a);
      }
    }
    else { //转贴现
      text = "转贴现垫款处理-详细";
      if ( (a = BMTrans.redisBillRecv(Integer.parseInt(strbilldisno),
                                      strUserName)) < 0) {
        System.out.println("a(error):" + a);
        if (a == -30037) {
          msgs.add("错误：申请未发生垫款.");
        }
        else {
          msgs.add("错误:" + a);
        }
        MyDB.getInstance().releaseDBConn();
        return a;
      }
      else {
        System.out.println("a(success):" + a);
      }

    }

    MyDB.getInstance().releaseDBConn();
    //进入打印借据功能
    if (strifadvanced.equals(String.valueOf(EnumValue.YesNo_Yes))) {
        String strAdvBmNo=null;
        RecordSet rs=conn.executeQuery("select advbmno from bmbilldis where bmno='"+strBmNo+"'");
        try
        {
            if(rs.next())
            {
                strAdvBmNo=rs.getString(0).trim();
            }
        }
        catch(Exception e)
        {
            System.out.println("垫款业务号查询失败！");
        }
        if(strAdvBmNo==null)
        {
            ctx.setRequestAtrribute("title", "报表打印失败");
            ctx.setRequestAtrribute("msg", "垫款业务号不存在！");
            ctx.setRequestAtrribute("flag", "1");
            ctx.setRequestAtrribute("isback", "0");
            ctx.setTarget("/showinfo.jsp");
        }
        else
        {
            ctx.setRequestAtrribute("BMNO", strAdvBmNo);
            ctx.setRequestAtrribute("IFADV", "YES");
            ctx.setTarget("/jspreport/loanbiz.jsp");
        }
    }
    else {
      if (formid == null) { //贴现垫款
        BMTrans trans = new BMTrans();
        ctx.setRequestAtrribute("title", text);
        ctx.setRequestAtrribute("msg", "贴现票据到期贴现操作已成功！");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setRequestAtrribute("isback", "0");
        ctx.setTarget("/showinfo.jsp");
      }
      else {
        text = "转贴现垫款处理-详细";
        BMTrans trans = new BMTrans();
        ctx.setRequestAtrribute("title", text);
        ctx.setRequestAtrribute("msg", "转贴现垫款操作已成功！");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setRequestAtrribute("isback", "0");
        ctx.setTarget("/showinfo.jsp");
      }
    }
    return 0;
  }

  public int beforeEdit(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    FormBean formBean = FormBeanManager.getForm(instance.getFormid());
    ElementBean eb = formBean.getElement("CANCELOPERATION");

    if (formid != null && formid.equals("BMZTX1")) {
      eb.setCaption("取消转贴现");
    }
    else if (formid == null) {
      eb.setCaption("取消贴现");
    }

    instance.setValue("DISOBJECT", formid);
    return 0;
  }
  public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
      EventManager manager, String reffldnm,SqlWhereUtil sqlWhereUtil) {

      String strinsql = Function.getAllSubBrhIds(msgs, ctx);
      sqlWhereUtil.addWhereField("SCUSER", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);

  return 0;
}

}
