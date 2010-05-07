package zt.cms.bm.review;

/**
 * <p>Title: �Ŵ�����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003  ������Ϣ</p>
 * <p>Company: ������Ϣ</p>
 * @author YUSGe
 * @date  2004/1/2
 * @modified wxj  2004/04/20 �������̺��ж�
 * @modified yusg 2004/04/26
*  @modified yusg 2004/12/27 ��ݺŸ��ݴ��������Ƿ���ʾ
 * @version 1.0
 */
import zt.cms.pub.*;
import zt.cmsi.biz.*;
import zt.cmsi.mydb.*;
import zt.cmsi.pub.*;
import zt.cmsi.pub.define.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import java.util.Calendar;

/****************************************************
 *
 * ��ͬ
 *
 ****************************************************/

public class BmContractPage  extends FormActions {
  private String strFlag = null; //��д��־
  private Param params = null; //���͵ı�������
  private Param paramg = null; //��õı�������
  private String strBmNo = null; //ҵ���
  private String strBmTransNo = null; //ҵ����ϸ��
  private String strBmType = null; //ҵ������
  private String strUserName = null; //��ǰ��½�û���
  private String strOthers = "0"; //�Ƿ����������־
  private String strScbrh = null; //��������

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //mod by wxj at 040210 �������̺��ж�
    //mod by yusg 2004/04/26
    paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (paramg == null) {
      msgs.add("�������󣬲������󲻴��ڣ�");
      return -1;
    }
    strFlag = paramg.getParam(ParamName.Flag).toString();
    strBmNo = (String) paramg.getParam(ParamName.BMNo);
    strBmTransNo = (String) paramg.getParam(ParamName.BMTransNo).toString();
    strBmType = (String) paramg.getParam(ParamName.BMType).toString();
    //System.out.println("bmType:" + strBmType);
    if (strBmType == null) {
      strBmType = "";
    }
    if (strBmNo == null) {
      msgs.add("��������ҵ��Ų����ڣ�");
      return -1;
    }
    //strUserName
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    strUserName = um.getUserName();
    if(strUserName==null || strUserName.length()<1){
      msgs.add("ϵͳ�����޷�ȡ������Ա��Ϣ��");
      return -1;
    }
    System.out.println("strUserName:" + strUserName);
    //strScbrh
    strScbrh = SCUser.getBrhId(strUserName);
    if(strScbrh==null || strScbrh.length()<1){
      msgs.add("ϵͳ�����޷�ȡ��ά��������Ϣ��");
      return -1;
    }
    //readonly?
    if (strFlag.equals( (String) ParamName.Flag_WRITE)) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }
    //System.out.println("strScbrh:" + strScbrh);
    //if BMNO is exist ... goto edit event
    if (DBUtil.getCellValue(conn, "bmcontract", "BMNO", "BMNO='" + strBmNo + "'") != null) {
      instance.setValue("BMNO", strBmNo);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    //�жһ�Ʊ�����֣�ת���ֲ���ʾ��Ϣ��ʽ
    //�жһ�Ʊ�����֣�ת���ֲ���ʾ��ݺ�
    if(strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ChengDuiHuiPiao+"")|| strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_TieXian+"") || strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ZhuanTieXian+"")){
      instance.useCloneFormBean();
      FormBean fb = instance.getFormBean();
      ElementBean eb1 = fb.getElement("INTERESTTYPE");
      ElementBean eb3=fb.getElement("CNLNO");
      eb1.setComponetTp(6);
      eb3.setComponetTp(6);
    }
    //if the bmtype is not acceptance bill, then hide the BILLNO field.
    if(!strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ChengDuiHuiPiao+""))
    {
      instance.useCloneFormBean();
      FormBean fb = instance.getFormBean();
      ElementBean eb1 = fb.getElement("BANKNOTENO");
      eb1.setComponetTp(6);
    }
    //���֣�ת���ֲ���ʾ��ͬ��������
    if(strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_TieXian+"") || strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ZhuanTieXian+"")){
      FormBean fb = instance.getFormBean();
      ElementBean eb2 = fb.getElement("ENDDATE");
      eb2.setComponetTp(6);
    }
    //����,���ʵ�ծ,����������� ����ʾ��ݺ�
    if(strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ShouXin+"") || strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_YiZiDiZhai+"") || strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_BuLiangDaiKuanHeXiao+""))
    {
      FormBean fb=instance.getFormBean();
      ElementBean eb4=fb.getElement("CNLNO");
      eb4.setComponetTp(6);
    }

    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    String strStartDate = null; //���߷ſ���
    String strEndDate = null;   //���߻�����
    String strContrSum=null;    //��ͬ���

    String strDate = SystemDate.getSystemDate5(null);
    UpToDateApp utda = BMTable.getUpToDateApp(strBmNo);

    strStartDate = util.calToString(utda.finalStartDate); //��þ��߷ſ���
    strEndDate = util.calToString(utda.finalEndDate);     //��þ��߻�����
    strContrSum= DBUtil.doubleToStr(utda.finalAmt.doubleValue());//��ú�ͬ���




    //���֣�ת���ֺ�ͬ��������ȥ�������һ���ܴ��Ĭ��ֵ
    if(strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_TieXian+"") || strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ZhuanTieXian+"")){
      instance.setValue("ENDDATE", "30010101");
    }
    else{
      instance.setValue("ENDDATE", strDate); //��һҵ����ľ��߻�����
    }

    if (this.strBmType.equals("11")) {
        //System.out.println("TTTTTTTTTTTTTTTTTTTT");
        instance.setValue("STARTDATE", strStartDate); //��һҵ����ľ��߷ſ���
        instance.setValue("ENDDATE", strEndDate); //��һҵ����ľ��߻�����
    }

    instance.setValue("CREATEDATE", strDate);
    instance.setValue("OPERATOR", strUserName);
    instance.setValue("BMNO", strBmNo);
    instance.setValue("BmType", strBmType);

    instance.setValue("contractsum", strContrSum);
    return 0;
  }

  public int beforeEdit(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {

    instance.setValue("BmType", strBmType);

    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    //�жһ�Ʊ�����֣�ת����ȥ����Ϣ��ʽ�����Ĭ��ֵ
    if(strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ChengDuiHuiPiao+"")|| strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_TieXian+"") || strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ZhuanTieXian+"")){
      assistor.setSqlFieldValue(assistor.getDefaultTbl(), "INTERESTTYPE", "1");
    }
    strOthers = ctx.getParameter("finish"); //�����ɺ�ͬ��־
    return 0;
  }

  public int preEdit(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    //�жһ�Ʊ�����֣�ת����ȥ����Ϣ��ʽ�����Ĭ��ֵ
    if(strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ChengDuiHuiPiao+"")|| strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_TieXian+"") || strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ZhuanTieXian+"")){
      assistor.setSqlFieldValue(assistor.getDefaultTbl(), "INTERESTTYPE", "1");
    }
    strOthers = ctx.getParameter("finish"); //�����ɺ�ͬ��־
    return 0;

  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {

    try {

      if(! strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_TieXian+"") && ! strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ZhuanTieXian+"")
         && ! strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ChengDuiHuiPiao+""))
      {

        String date1 = ctx.getParameter("STARTDATE");
        String date2 = ctx.getParameter("ENDDATE");

        Calendar d1 = util.stringToCal(date1);
        Calendar d2 = util.stringToCal(date2);

        UpToDateApp utda = BMTable.getUpToDateApp(strBmNo);

        if(utda.appDate != null)
        {
          if (util.daysBetweenCals(util.stringToCal("2005-10-14"), utda.appDate) <
              0)
          {
            //System.out.println("------------------------date1="+date1+"date2="+date2 +"decdate="+util.calToString(utda.finalStartDate));

            if (utda != null && utda.finalStartDate != null && utda.finalMonths != null) {
              //(utda.finalStartDate.add(Calendar.DAY_OF_MONTH,utda.finalMonths)
              if (util.daysBetweenCals(d1, utda.finalStartDate) < 0) {
                msgs.add("��ͬ��ʼ���ڱ������ھ�������(��)!");
                return -1;
              }

              if( strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ShouXin+""))
              {
                if (util.daysBetweenCals(d1, utda.finalStartDate) < 0) {
                  msgs.add("��ͬ��ʼ���ڱ������ھ�������(��)!");
                  return -1;
                }

                int days = 0;
                if(utda.finalStartDate != null && utda.finalEndDate != null)
                {
                  days = util.daysBetweenCals(utda.finalEndDate, utda.finalStartDate);
                  if (util.daysBetweenCals(d1, utda.finalStartDate) > days) {
                    msgs.add("��ͬ�������ھ�������+��������!");
                    return -1;
                  }

                  if (util.daysBetweenCals(d2, d1) > days) {
                    msgs.add("��ͬ���޲��ܴ��ھ�������!");
                    return -1;
                  }
                }
              }
              else
              {
                if (util.daysBetweenCals(d1, utda.finalStartDate) >
                    (utda.finalMonths.intValue() * 30)) {
                  msgs.add("��ͬ�������ھ�������+��������!");
                  return -1;
                }

                if (util.daysBetweenCals(d2, d1) >
                    (utda.finalMonths.intValue() * 30)) {
                  msgs.add("��ͬ���޲��ܴ��ھ�������!");
                  return -1;
                }
              }
            }
            else {
              System.out.println("BMTABLEAPP is null!");
            }
          }
        }
      }

//       if(true)
//       {
//         msgs.add("test");
//         return -1;
//       }


      int iflag = -1;
      MyDB.getInstance().addDBConn(conn);
      if (strOthers.equals("1")) {
        iflag = BMTrans.compltTrans(strBmNo, Integer.parseInt(strBmTransNo),
                                    strUserName); //��ɺ�ͬ
      }
      if (iflag >= 0) {
        ctx.setRequestAtrribute("title", "����ҵ������");
        ctx.setRequestAtrribute("msg", "��Ϣ����ɹ���");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");

      }
      else {
        return iflag;
      }
    }
    catch (Exception ex) {
      msgs.add("δ֪ԭ���µ�ϵͳ����");
      return -1;
    }
    finally {
      MyDB.getInstance().releaseDBConn();
    }
    return 0;
  }

  public int postEditOk(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager) {
    try {

      if(! strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_TieXian+"") && ! strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ZhuanTieXian+"")
         && ! strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ChengDuiHuiPiao+""))
      {

        String date1 = ctx.getParameter("STARTDATE");
        String date2 = ctx.getParameter("ENDDATE");

        Calendar d1 = util.stringToCal(date1);
        Calendar d2 = util.stringToCal(date2);

        UpToDateApp utda = BMTable.getUpToDateApp(strBmNo);

        //System.out.println("date1="+date1+"date2="+date2 +"decdate="+util.calToString(utda.finalStartDate));
        if(utda.appDate != null)
        {
          if (util.daysBetweenCals(util.stringToCal("2005-10-14"), utda.appDate) <
              0) {
            //System.out.println("date1="+date1+"date2="+date2 +"decdate="+util.calToString(utda.finalStartDate));

            if (utda != null && utda.finalStartDate != null && utda.finalMonths != null) {
              //(utda.finalStartDate.add(Calendar.DAY_OF_MONTH,utda.finalMonths)
              if (util.daysBetweenCals(d1, utda.finalStartDate) < 0) {
                msgs.add("��ͬ��ʼ���ڱ������ھ�������(��)!");
                return -1;
              }

              if( strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ShouXin+""))
              {
                if (util.daysBetweenCals(d1, utda.finalStartDate) <= 0) {
                  msgs.add("��ͬ��ʼ���ڱ������ھ�������(����)!");
                  return -1;
                }

                int days = 0;
                if(utda.finalStartDate != null && utda.finalEndDate != null)
                {
                  days = util.daysBetweenCals(utda.finalEndDate, utda.finalStartDate);
                  if (util.daysBetweenCals(d1, utda.finalStartDate) > days) {
                    msgs.add("��ͬ�������ھ�������+��������!");
                    return -1;
                  }

                  if (util.daysBetweenCals(d2, d1) > days) {
                    msgs.add("��ͬ���޲��ܴ��ھ�������!");
                    return -1;
                  }
                }
              }
              else
              {
                if (util.daysBetweenCals(d1, utda.finalStartDate) >
                    (utda.finalMonths.intValue() * 30)) {
                  msgs.add("��ͬ�������ھ�������+��������!");
                  return -1;
                }

                if (util.daysBetweenCals(d2, d1) >
                    (utda.finalMonths.intValue() * 30)) {
                  msgs.add("��ͬ���޲��ܴ��ھ�������!");
                  return -1;
                }
              }
            }
            else {
              System.out.println("BMTABLEAPP is null!");
            }
          }
        }
      }

//      if(true)
//      {
//        msgs.add("test");
//        return -1;
//      }

      int iflag = -1;
      MyDB.getInstance().addDBConn(conn);
      System.out.println("strOthers:" + strOthers);
      if (strOthers.equals("1")) {
        iflag = BMTrans.compltTrans(strBmNo, Integer.parseInt(strBmTransNo),
                                    strUserName); //��ɺ�ͬ
      }
      if (iflag >= 0) {
        ctx.setRequestAtrribute("title", "����ҵ������");
        ctx.setRequestAtrribute("msg", "��Ϣ����ɹ���");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
      }
      else {
        return iflag;
      }
    }
    catch (Exception ex) {
      msgs.add("δ֪ԭ���µ�ϵͳ����");
      return -1;
    }
    finally {
      MyDB.getInstance().releaseDBConn();
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    params = new Param();
    params.addParam(ParamName.Flag, strFlag);
    params.addParam(ParamName.BMNo, strBmNo);
    params.addParam(ParamName.BMTransNo, strBmTransNo);

    ctx.setRequestAtrribute(ParamName.ParamName, params);
    if (button.equals("BmTrans")) {
      trigger(manager, "BMTRANSLIST", null);
      return 0;
    }
    else {
      return -1;
    }
  }
}
