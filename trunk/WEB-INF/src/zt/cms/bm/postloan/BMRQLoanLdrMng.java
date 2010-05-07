/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMRQLoanLdrMng.java,v 1.1 2005/06/28 07:00:26 jgo Exp $
 */
package zt.cms.bm.postloan;

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
import zt.cmsi.mydb.*;
import zt.platform.db.*;
import zt.cmsi.pub.*;
import zt.cms.bm.bill.*;

/**
 *  ���ԣ���������������,�޸ģ�ɾ��(���ֵ)
 *
 *@author     Administrator
 *@created    2003��12��2��
 */
public class BMRQLoanLdrMng
    extends FormActions {

//  private String clientno;

    private boolean isAccept = false;
    private String strFlag = null;
    private String strBmNo = null;
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
    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                    ErrorMessages msgs,
                    EventManager manager, String parameter)
    {
        pm = (Param)ctx.getRequestAttribute(ParamName.ParamName);
        if(pm!=null){
            strFlag = (String) pm.getParam(ParamName.Flag);
            strBmNo = (String) pm.getParam(ParamName.BMNo);
            //System.out.println("strFlag:"+strFlag+"\nstrBmNo:"+strBmNo);
        }
        if(strBmNo==null || strBmNo.equals("")){
            msgs.clear();
            msgs.add("��ҵ��Ÿ�ʽ����ȷ��");
            return -1;
        }


        if (strFlag.equals("read") || strFlag == null) {
            instance.setReadonly(true);
        }else{
            //ȡĬ��ֵ
        }


        /*if ( strFlag == null|| strFlag.equals("read")) {
            instance.setReadonly(true);
            System.out.println("realonly:true");
                 } else {
            instance.setReadonly(false);
            System.out.println("realonly:false");
                 }*/
        // System.out.println("7345"+String.valueOf(ctx.getRequestAttribute("flag")));
//        strcnlno = ctx.getParameter("CNLNO") == null ? null : ctx.getParameter("CNLNO").trim();
        //System.out.println("strbmno:"+strbmno);
        if (Function.isDataExist(conn,"RQLOANLEDGER","BMNO",strBmNo,Function.STRING_TYPE)) {
            instance.setValue("BMNO", strBmNo);
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
        else {
            msgs.clear();
            msgs.add("�Բ���û�в�ѯ���κμ�¼��");
            return -1;
        }

        return 0;
    }
//    public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
//            EventManager manager) {
//        if(!this.isAccept && strFlag.equals(strFlag)){
//            SystemDate sd = new SystemDate();
//            String strcurrdate = sd.getSystemDate5("");
//            instance.setValue("CREATEDATE",strcurrdate);
//            instance.setFieldReadonly("CREATEDATE",true);
//
//            instance.setValue("BMNO", strBmNo);
//            LoanLedgerMan llm = new LoanLedgerMan();
//            LoanLedger ll = null;
//            if(strBmNo!=null){
//                ll = llm.getLoanLedger(strBmNo);
//            }
//            if( ll==null){
//                msgs.clear();
//                msgs.add("û��ȡ������ģ��");
//                return -1;
//            }else{
//                String strfirstResp = ll.firstResp;
//                String strfirstRespPct = ll.firstRespPct.toString();
//
//                instance.setValue("OLDRESP",strfirstResp);
//                instance.setValue("OLDPCT",strfirstRespPct);
//            }
//
//            //�������ά����
//        }
//
//        return 0;
//    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                           String button,
                           ErrorMessages msgs, EventManager manager)
    {
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
