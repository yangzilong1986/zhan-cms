/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMAccepPoDyn.java,v 1.3 2005/07/14 13:07:35 jgo Exp $
 */
package zt.cms.bm.bill;

import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.control.SessionContext;
import zt.platform.form.control.FormActions;
import zt.platform.form.util.*;
import zt.cmsi.pub.define.BMRoute;
import zt.cmsi.pub.cenum.EnumValue;

/**
 *  测试：多表－－－数据添加,修改，删除(获得值)
 *
 *@author     Administrator
 *@created    2003年12月2日
 */
public class BMAccepPoDyn
         extends FormActions {

    /**
     *  Description of the Method
     *
     *@author            Administrator
     *@since             2004年1月5日
     *@param  ctx        Description of the Parameter
     *@param  conn       Description of the Parameter
     *@param  instance   Description of the Parameter
     *@param  msgs       Description of the Parameter
     *@param  manager    Description of the Parameter
     *@param  parameter  Description of the Parameter
     *@return            Description of the Return Value
     */

    String strFlag = null;


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
    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, String parameter) {

        String strUserId = Function.getUserId(ctx);
        if(strUserId==null){
            msgs.clear();
            msgs.add("用户标示符不存在。");
            return -1;
        }
        BMRoute bmroute = BMRoute.getInstance();
        int a=-1;
        if((a=bmroute.hasRightToAct(Integer.parseInt(strUserId), EnumValue.BMType_ChengDuiHuiPiao,EnumValue.BMActType_DengJi)) < 0){
            ctx.setRequestAtrribute("title",Function.getFormTitleByInstance(instance));
            ctx.setRequestAtrribute("msg","您没有权限访问该页面。");
            ctx.setRequestAtrribute("flag","0");
            ctx.setRequestAtrribute("isback","0");
            ctx.setTarget("/showinfo.jsp");
            return -1;
        }
        strFlag = ctx.getParameter("flag").trim();
        return 0;
    }


    /**
     *  Description of the Method
     *
     *@param  ctx           Description of the Parameter
     *@param  conn          Description of the Parameter
     *@param  instance      Description of the Parameter
     *@param  msgs          Description of the Parameter
     *@param  manager       Description of the Parameter
     *@param  sqlWhereUtil  Description of the Parameter
     *@return               Description of the Return Value
     */
//    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
//            EventManager manager, PreparedStatement ps, PreparedStatement countps) {
//        //System.out.println("strFlag:"+strFlag);
//        System.out.println("222222222222222222222222222");
//        if (strFlag.equals("read") || strFlag == null) {
//            instance.setReadonly(true);
//        } else {
//            instance.setReadonly(false);
//        }
//        return 0;
//
//    }

    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlWhereUtil sqlWhereUtil) {

        if (strFlag.equals("read") || strFlag == null) {
            instance.setReadonly(true);
        } else {
            instance.setReadonly(false);
        }

        if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
            //System.out.println("111111111111111111111111");
            // sqlWhereUtil.addWhereField("BMTABLE","BMNO","2");
            //sqlWhereUtil.addWhereField("BMTABLE","BMNO","BMTABLE.BMNO in ('1','2')",3);
            String strinsql = Function.getAllSubBrhIds(msgs, ctx);
            System.out.println("insql:"+strinsql);
            sqlWhereUtil.addWhereField("BMACPTBILL", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);
        }
        return 0;
    }


    /**
     *  Description of the Method
     *
     *@param  ctx       Description of the Parameter
     *@param  conn      Description of the Parameter
     *@param  instance  Description of the Parameter
     *@param  button    Description of the Parameter
     *@param  msgs      Description of the Parameter
     *@param  manager   Description of the Parameter
     *@return           Description of the Return Value
     */
    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
            String button,
            ErrorMessages msgs, EventManager manager) {

        ctx.setRequestAtrribute("flag", strFlag);
//        Param pm = new Param();
//        pm.addParam(ParamName.BMNo,)

        String strbilldisno = ctx.getParameter("ACPTBILLNO");
        String strbmno = ctx.getParameter("BMNO");

        String col = ctx.getParameter(SessionAttributes.CLICK_COLUMN_NAME);
        if(col != null && col.compareToIgnoreCase("fc") == 0)
        {
          //ctx.setRequestAtrribute("ENTITYNO",this.entityNo);
          ctx.setTarget("/fcworkbench/createbillfcmain.jsp?confirmed=0&billno="+strbilldisno);
          return 0;
        }

        if ((button != null) && (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)
                || button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE))) {
            this.trigger(manager, "BMHPC0", null);
        }
        //
        return 0;
    }
//    public int preDelete(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
//            EventManager manager, SqlAssistor assistor) {
//        String strmenuid = ctx.getParameter("MENUID");
//        System.out.println("MENUID:"+strmenuid);
//        String strsql = null;
//        boolean isPIDUsed = false;
//
//        if(strmenuid!=null){
//            strsql = "select * from ptmenu where parentid='"+strmenuid+"'";
//        }
//        if(strsql!=null){
//            RecordSet rs = null;
//            try {
//                rs = conn.executeQuery(strsql);
//            }
//            catch (Exception ex) {
//                ex.printStackTrace();
//                return -1;
//            }
//            if(rs.next()){
//                isPIDUsed = true;
//            }
//        }
//        if( isPIDUsed ){
//            msgs.clear();
//            msgs.add("该菜单已有其他子菜单使用，具体删除步骤请阅读帮助文件。");
//            return -1;
//        }
//        return 0;
//    }
    public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
       EventManager manager, String reffldnm,SqlWhereUtil sqlWhereUtil) {

        String strinsql = Function.getAllSubBrhIds(msgs, ctx);
        System.out.println("insql:"+strinsql);
        sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);
        return 0;
}

}
