/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMAccepPoMng.java,v 1.1 2005/06/28 07:00:20 jgo Exp $
 *  modified by yusg  2004/4/27   -----report print
 */
package zt.cms.bm.bill;

import java.util.logging.*;
import com.zt.util.*;
import zt.cmsi.biz.*;
import zt.cmsi.mydb.*;
import zt.cmsi.pub.define.*;
import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import java.math.BigDecimal;
import zt.platform.utils.Debug;
import java.lang.*;
import java.util.Calendar;


public class BMAccepPoMng extends FormActions {
  private static Logger logger = Logger.getLogger("zt.cms.bm.bill.BMAccepPoMng");
  private String strFlag = null;
  private boolean isreadonly = true;
  private boolean isAccept = false;
  private String stracptbillno = null;
  private String strBmNo = null;
  private String strifadvanced = null;
  private String strUserName = null;

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {
    stracptbillno = (String) ctx.getRequestAttribute("mybillno");
    if (stracptbillno == null) {
      stracptbillno = ctx.getParameter("ACPTBILLNO");
    }
    strFlag = String.valueOf(ctx.getRequestAttribute("flag"));
    strBmNo = ctx.getParameter("BMNO");
    if (strFlag.equals("read") || strFlag == null) {
      this.isreadonly = true;
      instance.setReadonly(true);
    }
    else {
      instance.setReadonly(false);
      this.isreadonly = false;
    }
    this.isAccept = Function.isDataExist(conn, "BMACPTBILLHONOR", "ACPTBILLNO",
                                         stracptbillno,
                                         Function.INTEGER_TYPE);
    if (isAccept) {
      if (stracptbillno != null) {
        instance.setValue("ACPTBILLNO", stracptbillno.trim());
        instance.setFieldVisible("SAVEBUTTON", false); //不好使
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
        msgs.add("承兑汇票还未发生汇票承兑！");
        return -1;
      }
      trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    if (this.isreadonly == true) {
      msgs.add("查看状态下不允许运行本功能");
      return -1;
    }
    if (!this.isAccept) {
      SystemDate sd = new SystemDate();
      String strcurrdate = sd.getSystemDate5("");
      instance.setValue("CREATEDATE", strcurrdate);
      instance.setFieldReadonly("CREATEDATE", true);
      instance.setValue("ACPTBILLNO", stracptbillno);
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
        String strfirstResp = data.firstResp != null ? data.firstResp : "";
        String strfirstRespPct = data.firstRespPct != null ? data.firstRespPct.toString() : "";
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

    }

    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance,
                       ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
//    String appmonths;
    String chgtype, frate;
    String startdate="", enddate="";
    chgtype = ctx.getParameter("CHGTYPE");
    java.util.Calendar startDate, endDate;
//    appmonths = ctx.getParameter("APPMONTHS");
    String  ifadvanced=ctx.getParameter("IFADVANCED") ;
    //
    if(ifadvanced.equals("1"))
    {
      System.out.println("bmacceppomng  ifadvanced=" + ifadvanced);
      startdate = ctx.getParameter("STARTDATE");
      enddate = ctx.getParameter("ENDDATE");
      frate = ctx.getParameter("FRATE");
      System.out.println("---------------------" + chgtype + frate);

      long startlong, endlong;
      Calendar c = Calendar.getInstance();
      String[] d;
      //    新加条件判断by sunzg 10-20
      if ( (startdate != null && startdate.length() > 0) && (enddate != null && enddate.length() > 0)) {
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
        if (true) {
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
            BMBaseInst baseint = BMBaseInst.getBaseInst(EnumValue.
              BMType_ChengDuiHuiPiao, dates);
            if (baseint == null) {
              instance.setValue("BRATE", "");
              instance.setValue("RATE", "");
              msgs.add("未取得基准利率设置，请检查系统的基准利率设置！");
              return -1;
            }

            System.out.println("baseint ===================" +
                               baseint.interest);

            instance.setValue("BRATE", "" + baseint.interest);
            assistor.setSqlFieldValue(assistor.getDefaultTbl(), "BRATE", "" + baseint.interest);
            BigDecimal finalrate = baseint.interest.multiply(fratef.add(new
              BigDecimal(1)));
            instance.setValue("RATE", "" + finalrate);
            assistor.setSqlFieldValue(assistor.getDefaultTbl(), "RATE", "" + finalrate);
            msgs.add("已经获得基准利率：成功.请继续输入.");
            if (chgtype != null)return -1;
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

      if (this.isreadonly == true) {
        msgs.add("查看状态下不允许运行本功能");
        return -1;
      }
      String strrate = ctx.getParameter("RATE");
      if (strrate != null && !strrate.equals("")) {
        if (Float.parseFloat(strrate) <= 0) {
          msgs.add("不允许\"利率\"<=0。");
          return -1;
        }
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
      trigger(manager, instance, EventType.INSERT_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    else if (button.equals("CDHPSEARCH")) {
      ctx.setRequestAtrribute("flag", "read");
      ctx.setRequestAtrribute("ACPTBILLNO", stracptbillno);
      this.trigger(manager, "BMABT0", null);
    }
    else if (button.equals("CANCELOPERATION")) {
      if (this.isreadonly == true) {
        msgs.add("查看状态下不允许运行本功能");
        return -1;
      }
      strUserName = Function.getUserName(ctx);
      int a = -1;
      if ( (a = BMTrans.acptBillRecvVoid(Integer.parseInt(stracptbillno), strUserName)) < 0) {
        ctx.setRequestAtrribute("title", Function.getFormTitleByInstance(instance));
        ctx.setRequestAtrribute("msg", "承兑汇票取消操作失败(错误:" + PropertyManager.getProperty(a + "") + ")！");
        ctx.setRequestAtrribute("flag", "0");
        ctx.setRequestAtrribute("isback", "0");
        ctx.setTarget("/showinfo.jsp");
      }
      else {
        ctx.setRequestAtrribute("title", Function.getFormTitleByInstance(instance));
        ctx.setRequestAtrribute("msg", "承兑汇票取消操作成功！");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setRequestAtrribute("isback", "0");
        ctx.setTarget("/showinfo.jsp");
      }
    }
    return 0;
  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    strifadvanced = ctx.getParameter("IFADVANCED");
    MyDB.getInstance().addDBConn(conn);
    int a = -1;
    if ( (a = BMTrans.acptBillRecv(Integer.parseInt(stracptbillno),
                                     strUserName)) < 0) {
        System.out.println("a(error):" + a);
    }
    else {
       System.out.println("a(success):" + a);
    }
    MyDB.getInstance().releaseDBConn();

    //进入打印借据功能
    if (strifadvanced.equals(String.valueOf(EnumValue.YesNo_Yes))) {
      //进入打印借据功能
      ctx.setRequestAtrribute("BMNO", this.strBmNo);
      ctx.setRequestAtrribute("IFADV","YES");
      ctx.setTarget("/jspreport/loanbiz.jsp");
    }
    else {
      BMTrans trans = new BMTrans();
      ctx.setRequestAtrribute("title", Function.getFormTitleByInstance(instance));
      ctx.setRequestAtrribute("msg", "承兑汇票操作已成功！");
      ctx.setRequestAtrribute("flag", "1");
      ctx.setRequestAtrribute("isback", "0");
      ctx.setTarget("/showinfo.jsp");
    }
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm, SqlWhereUtil sqlWhereUtil) {
    String strinsql = Function.getAllSubBrhIds(msgs, ctx);
    sqlWhereUtil.addWhereField("SCUSER", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);
    return 0;
  }

}
