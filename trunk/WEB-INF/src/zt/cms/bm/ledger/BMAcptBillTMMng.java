/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMAcptBillTMMng.java,v 1.1 2005/06/28 07:00:23 jgo Exp $
 */
package zt.cms.bm.ledger;

import zt.cmsi.pub.*;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.util.SqlField;
import java.sql.PreparedStatement;
import zt.platform.form.control.SessionContext;
import zt.platform.form.control.FormActions;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.form.util.datatype.*;
import zt.cms.pub.code.SerialNumber;
import zt.cms.bm.bill.*;
import zt.cmsi.biz.*;
import zt.cmsi.pub.define.*;
import zt.cmsi.pub.code.*;

/**
 *  ���ԣ���������������,�޸ģ�ɾ��(���ֵ)
 *
 *@author     Administrator
 *@created    2003��12��2��
 */
public class BMAcptBillTMMng
    extends FormActions {

//  private String clientno;

    private String strFlag = null;
    private boolean isAccept = false;
    private String stracptbillno = null;

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
    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                    ErrorMessages msgs,
                    EventManager manager, String parameter)
    {

        strFlag = String.valueOf(ctx.getRequestAttribute("flag"));

        if (strFlag == null || strFlag.equals("read")) {
            instance.setReadonly(true);
            System.out.println("realonly:true");
        }
        else {
            instance.setReadonly(false);
            System.out.println("realonly:false");
        }
        // System.out.println("7345"+String.valueOf(ctx.getRequestAttribute("flag")));
        stracptbillno = ctx.getParameter("ACPTBILLNO") == null ? null :
            ctx.getParameter("ACPTBILLNO").trim();
        if(stracptbillno == null)
         stracptbillno = ctx.getRequestAttribute("ACPTBILLNO") == null ? null :
              ((String)ctx.getRequestAttribute("ACPTBILLNO")).trim();
//          System.out.println("billno :"+stracptbillno);
        if (stracptbillno == null) {
            if (strFlag != null && strFlag.equals("write")) {
                trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
                        Event.BRANCH_CONTINUE_TYPE);
            }
            else {
                msgs.clear();
                msgs.add("��û���㹻��Ȩ�����Ӽ�¼��");
                return -1;
            }
        }
        else {

            this.isAccept = Function.isDataExist(conn, "BMACPTBILL", "ACPTBILLNO", stracptbillno,
                                                 Function.INTEGER_TYPE);
            System.out.println("isAccept:" + isAccept);
            if (isAccept) {
                //System.out.println("1111:"+stracptbillno);
                if (stracptbillno != null) {
                    instance.setValue("ACPTBILLNO", stracptbillno);
                }
//            if (!instance.isReadonly()) {
//                instance.setReadonly(true);
//            }
                trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                        Event.BRANCH_CONTINUE_TYPE);
            }
            else {
                msgs.clear();
                msgs.add("�Բ���û�в�ѯ���κμ�¼��");
                return -1;
            }
        }

        return 0;
    }

    public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                            ErrorMessages msgs,
                            EventManager manager)
    {

        instance.setValue("ACPTBILLNO", "0");
        instance.setFieldReadonly("ACPTBILLNO", true);
        instance.setFieldVisible("ACPTBILLNO", true);
        System.out.println("BMAcptBillTMMng.beforeInsert(line:114):" +
                           instance.getValue("ACPTBILLNO").isVisible());
        return 0;
    }

    public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         ErrorMessages msgs,
                         EventManager manager, SqlAssistor assistor)
    {

        String strNowBrhid = ctx.getParameter("BRHID");
        if (!Function.isNowBrhidRight(ctx, strNowBrhid, Function.getBrhid(ctx), "ҵ�����㲻��ά�������ڡ�",
                                      msgs)) {
            return -1;
        }

        int iacptbillno = BMAcptBill.getNextNo();
        String strAcptBillno = String.valueOf(iacptbillno);
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "ACPTBILLNO", strAcptBillno);
        return 0;
    }

    public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager)
    {
        ctx.setRequestAtrribute("title", Function.getFormTitleByInstance(instance));
        ctx.setRequestAtrribute("msg", "��������ѳɹ���");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setRequestAtrribute("isback", "0");
        ctx.setTarget("/showinfo.jsp");

        return 0;
    }

    public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                            ErrorMessages msgs,
                            EventManager manager)
    {
        ctx.setRequestAtrribute("title", Function.getFormTitleByInstance(instance));
        ctx.setRequestAtrribute("msg", "���Ӳ����ѳɹ���");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setRequestAtrribute("isback", "0");
        ctx.setTarget("/showinfo.jsp");

        return 0;
    }

    public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                       ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor)
    {
        String strNowBrhid = ctx.getParameter("BRHID");
        if (!Function.isNowBrhidRight(ctx, strNowBrhid, Function.getBrhid(ctx), "ҵ�����㲻��ά�������ڡ�",
                                      msgs)) {
            return -1;
        }
        return 0;
    }
    public int preReference(SessionContext ctx, DatabaseConnection conn,
                            FormInstance instance, ErrorMessages msgs,
                            EventManager manager, String reffldnm,
                            SqlWhereUtil sqlWhereUtil)
    {
        String strinsql = Function.getAllSubBrhIds(msgs, ctx);

        if (reffldnm.equals("BRHID")) {
            sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", strinsql,
                                       SqlWhereUtil.DataType_Sql,
                                       sqlWhereUtil.OperatorType_In);

        }
        return 0;
    }

//    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
//                           String button,
//                           ErrorMessages msgs, EventManager manager)
//    {
//        Param pm = new Param();
//        pm.addParam(ParamName.ParamName,pm);
//
//
//        if (button == null || button.equals("")) {
//            msgs.clear();
//            msgs.add("����ķ��񲻴��ڣ�");
//            return -1;
//        }
//        if(button.equals("ACPTBILLBUTTON")){
//
//            //�鿴��Ʊ�ж�
//            this.trigger(manager,"BMHPC0",null);
//        }else if (button.equals("PRINTMOPAIEDNEY")){
//////////////////////////////////////////////////////
//            //��ӡ�����
//////////////////////////////////////////////////////
//            return -1;
//        }
//
//        return 0;
//    }

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
    /* public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
             EventManager manager) {
         if(!this.isAccept){
             SystemDate sd = new SystemDate();
             String strcurrdate = sd.getSystemDate5();
             instance.setValue("CREATEDATE",strcurrdate);
             instance.setFieldReadonly("CREATEDATE",true);
             instance.setValue("BILLDISNO", strbilldisno);
             LoanLedgerMan llm = new LoanLedgerMan();
             LoanLedger ll = null;
             if(this.strbilldisno!=null){
                 ll = llm.getLoanLedger(Integer.parseInt(this.strbilldisno));
             }
             if( ll==null){
                 msgs.clear();
                 msgs.add("û��ȡ������ģ��");
                 return -1;
             }else{
                 String strfirstResp = ll.firstResp;
                 String strfirstRespPct = ll.firstRespPct.toString();
                 instance.setValue("FIRSTRESP",strfirstResp);
                 instance.setValue("FISRTRESPPCT",strfirstRespPct);
             }
         }
         return 0;
     }*/

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
    /*public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
        String strrate = ctx.getParameter("RATE");
        if(strrate!=null && !strrate.equals("")){
            if(Integer.parseInt(strrate)<=0){
                msgs.add("������\"����\"<=0��");
                return -1;
            }
        }*/
    /*if(Function.checkData(ctx,"IFADVANCED","1")){
        if(Function.checkData(ctx,"ADVAMT","")){
            msgs.add("\"�����\"������д��");
        }
        if(Function.checkData(ctx,"RATE","")){
            msgs.add("\"����\"������д��");
        }
        if(Function.checkData(ctx,"STARTDATE","")){
            msgs.add("\"��ʼ����\"������д��");
        }
        if(Function.checkData(ctx,"ENDDATE","")){
            msgs.add("\"��������\"������д��");
        }
        if(Function.checkData(ctx,"FIRSTRESP","")){
            msgs.add("\"��һ������\"������д��");
        }
        if(Function.checkData(ctx,"FISRTRESPPCT","")){
            msgs.add("\"��һ���α���\"������д��");
        }
             }*/
//        return 0;
//    }

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
    /*public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
        if(Function.checkData(ctx,"IFADVANCED","1")){
            if(Function.checkData(ctx,"ADVAMT","")){
                msgs.add("\"�����\"������д��");
            }
        }
        return 0;
         }*/

//    private  boolean checkData(SessionContext ctx,String name,String value){
//        return false;
//    }
}
