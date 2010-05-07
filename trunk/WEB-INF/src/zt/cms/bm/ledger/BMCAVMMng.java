/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMCAVMMng.java,v 1.1 2005/06/28 07:00:23 jgo Exp $
 */
package zt.cms.bm.ledger;

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

/**
 *  ���ԣ���������������,�޸ģ�ɾ��(���ֵ)
 *
 *@author     Administrator
 *@created    2003��12��2��
 */
public class BMCAVMMng
         extends FormActions {

//  private String clientno;

    private String strFlag = null;
    private boolean isAccept = false;
    private String strbmno = null;


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

        strFlag = String.valueOf(ctx.getRequestAttribute("flag"));

        if ( strFlag == null|| strFlag.equals("read")) {
            instance.setReadonly(true);
            System.out.println("realonly:true");
        }else {
            instance.setReadonly(false);
            System.out.println("realonly:false");
        }
        // System.out.println("7345"+String.valueOf(ctx.getRequestAttribute("flag")));
        strbmno = ctx.getParameter("BMNO")==null?null:ctx.getParameter("BMNO").trim();
        if(strbmno==null){
            if(strFlag!=null&&strFlag.equals("write")){
                trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
                        Event.BRANCH_CONTINUE_TYPE);
            }else{
                msgs.clear();
                msgs.add("��û���㹻��Ȩ�����Ӽ�¼��");
                return -1;
            }
        }else{

        this.isAccept = Function.isDataExist(conn, "BMLOANCAV", "BMNO", strbmno,Function.STRING_TYPE);
        System.out.println("isAccept:"+isAccept);
        if (isAccept) {
            //System.out.println("1111:"+stracptbillno);
            if (strbmno != null) {
                instance.setValue("BMNO", strbmno);
            }
//            if (!instance.isReadonly()) {
//                instance.setReadonly(true);
//            }
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        } else{
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

//        instance.setValue("BILLDISNO", "0");
//        instance.setFieldReadonly("BILLDISNO", true);
//        instance.setFieldVisible("BILLDISNO", true);
//        System.out.println("BMAcptBillTMMng.beforeInsert(line:114):"+instance.getValue("BILLDISNO").isVisible());
        return 0;
    }

    public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         ErrorMessages msgs,
                         EventManager manager, SqlAssistor assistor)
    {
//        int iacptbillno = BMBILLDIS.getNextNo();
//        String strBillDisno = String.valueOf(iacptbillno);
//        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "BILLDISNO", strBillDisno);
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
    public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager) {
        ctx.setRequestAtrribute("title", Function.getFormTitleByInstance(instance));
        ctx.setRequestAtrribute("msg", "���Ӳ����ѳɹ���");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setRequestAtrribute("isback", "0");
        ctx.setTarget("/showinfo.jsp");

        return 0;
    }
    public int preReference(SessionContext ctx, DatabaseConnection conn,
                            FormInstance instance, ErrorMessages msgs,
                            EventManager manager, String reffldnm,
                            SqlWhereUtil sqlWhereUtil) {
        String strinsql = Function.getAllSubBrhIds(msgs, ctx);
        if(reffldnm.equals("BRHID")){
            sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", strinsql,
                                       SqlWhereUtil.DataType_Sql,
                                       sqlWhereUtil.OperatorType_In);

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
