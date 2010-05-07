package zt.cms.bm.postloan;
/**
 * <p>Title: 信贷管理</p>
 * <p>Description: 潍坊信贷管理系统</p>
 * <p>Copyright: Copyright (c) 2003 中天信息技术有限公司</p>
 * <p>Company: 中天信息技术有限公司</p>
 * @author     liwei
 * @Modified   Yusg    20041224
 * @version 1.0
 */

import zt.platform.db.DatabaseConnection;
import zt.platform.form.control.SessionContext;
import zt.platform.form.control.FormActions;
import zt.platform.form.util.*;
import zt.cms.bm.bill.*;
import zt.cmsi.biz.*;
import zt.cmsi.mydb.*;
import zt.platform.db.*;
import zt.cmsi.pub.code.*;
import zt.cmsi.pub.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
/********************************************************
*
*  function:风险检查
*
********************************************************/
public class BMPLCMng  extends FormActions
{
    private String strFlag = null;
    private boolean isAccept = false;
    private String strbmno = null;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         ErrorMessages msgs,
                         EventManager manager, String parameter)
         {
             if (ctx.getRequestAttribute("title") != null) {

                 instance.useCloneFormBean();
                 instance.getFormBean().setTitle("风险检查");
                 instance.getFormBean().getElement("EXPLANATION").setCaption("可偿债资产情况");
             }

             Param pm = (Param) ctx.getRequestAttribute(ParamName.ParamName);
             strFlag = (String) pm.getParam(ParamName.Flag);
             strbmno = (String) pm.getParam(ParamName.BMNo);
             strbmno = strbmno == null ? "" : strbmno.trim();

             String strbmnofromctx = ctx.getParameter("BMNO");
             strbmnofromctx = strbmnofromctx == null ? "" : strbmnofromctx.trim();
             strbmno = (strbmnofromctx.equals("")) ? strbmno : strbmnofromctx;

             if (Function.isDataExist(conn, "BMPOSTLOANCHECK", "BMNO", strbmno, Function.STRING_TYPE)) {
                 instance.setValue("BMNO", strbmno);
                 trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
             }
             else {
                 trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
             }

             return 0;
         }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                                String button,
                                ErrorMessages msgs, EventManager manager)
         {
             return 0;
         }

    public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                              ErrorMessages msgs,
                              EventManager manager, SqlAssistor assistor)
         {
             String strcheckno = String.valueOf(BMPostLoanCheck.getNextNo());
             assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CHECKNO", strcheckno);
             MyDB.getInstance().addDBConn(conn);
             InactLoanMan llm = new InactLoanMan();
             if (llm.checkLoan(Integer.parseInt(strcheckno)) < 0) {
                 msgs.clear();
                 msgs.add("出错：调用贷后检查功能(调用公共模块),必须和保存在一个事务中.");
                 return -1;
             }
             MyDB.getInstance().releaseDBConn();
             return 0;
         }

    public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                            ErrorMessages msgs,
                            EventManager manager, SqlAssistor assistor)
         {
             return 0;
         }

    public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                                 ErrorMessages msgs,
                                 EventManager manager)
         {
             instance.setValue("BMNO", strbmno);
             String strUserName = Function.getUserName(ctx);
             if (strUserName != null) {
                 instance.setValue("OPERATOR", strUserName);
             }
             else {
                 return -1;
             }
             return 0;
         }

   /***********************************************************
   * function:获得检查人
   *          弹出式按钮弹出的检查人所在网点必须与贷款业务登记网点相同
   *
   ************************************************************/
   public int preReference(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm,
                          SqlWhereUtil sqlWhereUtil)
  {
    if (reffldnm.equals("CHECKEDBY"))
    {
      String strbrh="";      //贷款登记网点

      String strSql="select brhid from bmtable where bmno='"+strbmno+"'";
      RecordSet rs=conn.executeQuery(strSql);
      while(rs.next())
      {
        strbrh=rs.getString(0);
      }

      if (strbrh!=null && strbrh.length()!=0)
      {
        sqlWhereUtil.addWhereField("SCUSER", "BRHID", strbrh,
                                   SqlWhereUtil.DataType_Character,
                                   sqlWhereUtil.OperatorType_Equals,
                                   sqlWhereUtil.RelationOperator_And);
        sqlWhereUtil.addWhereField("SCUSER", "USERTYPE", "3",
//                                   SqlWhereUtil.DataType_Number,
                                   SqlWhereUtil.DataType_Character,
                                   sqlWhereUtil.OperatorType_Not_Equals);
      }
    }
    return 0;
  }
}

