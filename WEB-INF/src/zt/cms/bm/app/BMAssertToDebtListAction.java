package zt.cms.bm.app;

import zt.platform.form.control.FormActions;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.*;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SqlAssistor;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.logging.*;
import zt.platform.form.util.event.EventType;
import zt.platform.form.util.event.Event;
import zt.cms.pub.code.SerialNumber;
import zt.platform.form.util.SessionAttributes;
import zt.cms.cm.common.RightChecker;
import zt.cmsi.pub.*;
import zt.cms.bm.common.ParamFactory;
import zt.platform.form.util.*;
import zt.platform.user.*;
import zt.cms.pub.*;
import zt.cms.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.cmsi.pub.define.*;
import zt.cms.bm.common.*;
import zt.cmsi.mydb.MyDB;
import zt.cmsi.biz.*;
import java.math.*;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004年1月9日
 */
public class BMAssertToDebtListAction extends FormActions {
    /**
     *  Constructor for the BMAssertToDebtListAction object
     */
    public BMAssertToDebtListAction() { }


    /**
     *  Description of the Field
     */
    public static int ASSERT_TO_DEBT = 16;
    /**
     *  Description of the Field
     */
    public static int CANCEL_CREDIT = 17;








    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,ErrorMessages msgs,
            EventManager manager, String parameter) {
        RightChecker.checkReadonly(ctx,conn,instance);

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
    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
            ErrorMessages msgs, EventManager manager) {
         //MyDB.getInstance().addDBConn(conn);






        String but = ctx.getParameter(SessionAttributes.CLICK_COLUMN_NAME);
        if (but != null) {

            Param param = ParamFactory.getParamByCtx(ctx);
            ctx.setRequestAtrribute("BMPARAM",param);
            BMTableData tableData = BMTable.getBMTable(param.getBmNo());

            param.addParam(ParamName.BrhID,tableData.brhID);
            param.addParam(ParamName.CLientNo,tableData.clientNo);
            param.addParam(ParamName.OrigBMNo,param.getBmNo());
            param.addParam(ParamName.BrhID,tableData.brhID);
            //param.addParam(ParamName.BrhID,tableData.brhID);


            if (but.equals("editbutton1")) {

                param.addParam(ParamName.BMType, new Integer(ASSERT_TO_DEBT));
                int startActType = BMType.getInstance().getStartAct(
                    ASSERT_TO_DEBT).intValue();
                param.addParam(ParamName.BMActType, new Integer(startActType));

                //System.out.println("" + SessionInfo.getLoginUserNo(ctx) +
                //                   "-----" + ASSERT_TO_DEBT + "-----" + startActType);
                if (BMRoute.getInstance().hasRightToAct(SessionInfo.
                    getLoginUserNo(ctx), ASSERT_TO_DEBT, startActType) >= 0) {
                    Vector results = AppCriteria.checkAppCriteria(param);
                    ctx.setRequestAtrribute("results",results);
                    ctx.setRequestAtrribute("BMPARAM",param);
                    ctx.setTarget("/workbench/check.jsp");
                    //System.out.println("+++++"+param);
                    return 0;
                }else{
                    msgs.add("你没有次操作的权限！");
                    //MyDB.getInstance().releaseDBConn();
                    return -1;
                }

            } else if (but.equals("editbutton2")) {

                param.addParam(ParamName.BMType, new Integer(CANCEL_CREDIT));
                int startActType = BMType.getInstance().getStartAct(
                    ASSERT_TO_DEBT).intValue();
                param.addParam(ParamName.BMActType, new Integer(startActType));

//                System.out.println("" + SessionInfo.getLoginUserNo(ctx) +
//                                   "-----" + CANCEL_CREDIT + "-----" + startActType);
                if (BMRoute.getInstance().hasRightToAct(SessionInfo.
                    getLoginUserNo(ctx), CANCEL_CREDIT, startActType) >= 0) {


                    Vector results = AppCriteria.checkAppCriteria(param);
                    ctx.setRequestAtrribute("results",results);
                    ctx.setRequestAtrribute("BMPARAM",param);
                    ctx.setTarget("/workbench/check.jsp");
//                    System.out.println("+++++"+param);
                    return 0;

//
//                    MyDB.getInstance().addDBConn(conn);
//
//                    String bmNo = BMTable.createBMTable(param.getBmTypeNo(),
//                        (String) param.getParam(ParamName.CLientNo),
//                        (String) param.getParam(ParamName.BrhID),
//                        SessionInfo.getLoginBrhId(ctx),
//                        SessionInfo.getLoginUserName(ctx));
//                    System.out.println("bmno========================" + bmNo);
//
//                    if (bmNo == null) {
//                        conn.rollback();
//                        MyDB.getInstance().releaseDBConn();
//                        return -1;
//                    }else {
//                        int transNo = BMTrans.createBMTrans(bmNo,
//                            ((Integer)param.getParam(ParamName.BMActType)).intValue(),
//                            SessionInfo.getLoginBrhId(ctx),
//                            SessionInfo.getLoginUserName(ctx));
//                        if (transNo < 0) {
//                            conn.rollback();
//                            MyDB.getInstance().releaseDBConn();
//                            return transNo;
//                        }
//                        else {
//                                        System.out.println("new bmNo===" + bmNo +
//                                                           "---------------transno===" +
//                                                           transNo);
//                                        param.addParam(ParamName.BMNo, bmNo);
//                                        param.addParam(ParamName.BMTransNo,
//                                                       new Integer(transNo));
//                                        param.addParam("flag", "write");
//
//                                        UpToDateApp data = new UpToDateApp();
//
//                                        ////clientno,bmtype,brhid
//                                        data.bmTypeNo = new Integer(param.getBmTypeNo());
//                                        data.clientNo = (String) param.getParam(ParamName.CLientNo);
//                                        //data.
//
//                                        data.origBMNo = (String) param.getParam(ParamName.
//                                            OrigBMNo);
//                                        int ret = BMTable.updateUpToDateApp(bmNo, data);
//                                        if (ret < 0) {
//                                            conn.rollback();
//                                            MyDB.getInstance().releaseDBConn();
//                                            return ret;
//                                        }
//                                        else {
//                                            conn.commit();
//                                            BMProg prog = BMRoute.getInstance().getActProg(
//                                                param.getBmTypeNo(), startActType, null);
//                                            if (prog.isForm()) {
//                                                trigger(manager, prog.getProgName(), null);
//                                                MyDB.getInstance().releaseDBConn();
//                                                return 0;
//                                            }
//                                            else {
//                                                msgs.add("系统错误，错误的系统配置！");
//                                                MyDB.getInstance().releaseDBConn();
//                                                return -1;
//                                            }
//
//                                        }
//
//                                    }
//                                }

                }else{
                    msgs.add("你没有次操作的权限！");
                    //MyDB.getInstance().releaseDBConn();
                    return -1;
                }

            } else {
                //MyDB.getInstance().releaseDBConn();
                return -1;
            }
        } else {
            //MyDB.getInstance().releaseDBConn();
            return -1;
        }

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
    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlWhereUtil sqlWhereUtil) {
        if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
            String strinsql = SessionInfo.getLoginAllSubBrhIds(ctx);
            sqlWhereUtil.addWhereField("BMTABLE", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);
        }
        return 0;
    }

    public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
       EventManager manager, String reffldnm,SqlWhereUtil sqlWhereUtil) {

       String strinsql = SessionInfo.getLoginAllSubBrhIds(ctx);
       sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);
       return 0;
}




}
