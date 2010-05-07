package zt.cms.bm.review;

/**
 * <p>Title: ��Ȩ����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003  ������Ϣ</p>
 * <p>Company: ������Ϣ</p>
 * @author YUSG
 * @modified by wxj at 20040509  1��Ȩ�Ľ��ȡ���������Ľ������������� 2��ťֻ������ 3���̵�����
*  @modifide by yusg at 20041227 ���ʵ�ծ����ӡƾ֤
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
  private String strFlag = null; //��д��־
  public Param params = null; //���͵ı�������
  private Param paramg = null; //��õı�������
  private String strBmNo = null; //ҵ���
  private String strBmTransNo = null; //ҵ����ϸ��
  private String strUserName = null; //��ǰ��½�û���
  private String strBmActType = null; //��������
  private String strBmType = null; //��������
  private boolean isFromList=false;

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {

    paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (paramg == null) {
      msgs.add("�������󣬲������󲻴��ڣ�");
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
    //strBmNo��strBmTransNo
    strBmNo = paramg.getParam(ParamName.BMNo).toString();
    strBmTransNo = paramg.getParam(ParamName.BMTransNo).toString();
    if (strBmNo == null || strBmTransNo == null) {
      msgs.add("��������ҵ��Ż�ҵ����ϸ�Ų����ڣ�");
      return -1;
    }
    //strBmActType��strBmType
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
      msgs.add("ϵͳ�����޷�ȡ������������");
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
    //�鿴��ʷ������¼
    if (button.equals("BmTrans")) {
      trigger(manager, "BMTRANSLIST", null);
      return 0;
    }
    //����
    else if (button.equals("put"))
    {
      if(isFromList){
        ctx.setRequestAtrribute("title", "������Ȩ");
        ctx.setRequestAtrribute("msg", "ֻ��Ȩ���޷�������Ȩ��");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
        return 0;
      }
      int iFlag = BMTrans.compltTrans(strBmNo, Integer.parseInt(strBmTransNo), strUserName);
      if (iFlag >= 0) {
        ctx.setRequestAtrribute("BMNO", this.strBmNo);
        if (strBmType.trim().equals("11") || strBmType.trim().equals("16")) {
          //����,���ʵ�ծ���� ��Ȩ����ӡ
          ctx.setRequestAtrribute("msg", "��Ȩ�ɹ���");
          ctx.setRequestAtrribute("flag", "1");
          ctx.setTarget("/showinfo.jsp");
          return 0;
        }
        else if (strBmType.trim().equals("12")) {
          //�жһ�Ʊ��ӡ����ǩ�����гжһ�Ʊ֪ͨ��
          ctx.setTarget("/jspreport/loanbizpo.jsp");
        }
        else if (strBmType.trim().equals("17")) {
          //�������������Ȩ����ӡ��
          ctx.setRequestAtrribute("title", "����������Ȩ");
          ctx.setRequestAtrribute("msg", "��Ȩ�ɹ���");
          ctx.setRequestAtrribute("flag", "1");
          ctx.setTarget("/showinfo.jsp");
        }
        else {
          //BMType(13,14)                 Ʊ����Ȩ��
          //BMType(��11,12,13,14,16,17��)     ���ƾ֤
          ctx.setTarget("/jspreport/loanbiz.jsp");
        }
        return 0;
      }
      return iFlag;
    }
    //ȡ����Ȩ
    else if (button.equals("cancel")) {
      if(isFromList){
        ctx.setRequestAtrribute("title", "ȡ����Ȩ");
        ctx.setRequestAtrribute("msg", "ֻ��Ȩ���޷�ȡ����Ȩ��");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
        return 0;
      }
      int iFlag = BMTrans.cancelBMTrans(strBmNo, Integer.parseInt(strBmTransNo), strUserName);
      if (iFlag >= 0) {
        ctx.setRequestAtrribute("title", "ȡ����Ȩ");
        ctx.setRequestAtrribute("msg", "��Ȩȡ���ɹ���");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
        return 0;
      }
      return iFlag;
    }
    return 0;
  }
}
