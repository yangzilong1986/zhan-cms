package zt.cms.report.dyndata;

/**
 * <p>Title: �Ŵ�����ϵͳ</p>
 * <p>Description: Ϋ���Ŵ�</p>
 * <p>Copyright: Copyright (c) 2003  ������Ϣ�������޹�˾</p>
 * <p>Company: ������Ϣ�������޹�˾</p>
 * @author Yusg
 * @version 1.0
 */
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SessionAttributes;
import zt.cmsi.pub.ParamName;
import zt.cmsi.pub.Param;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.util.event.EventType;
import zt.platform.form.util.event.Event;
import zt.platform.form.config.FormBean;
import zt.platform.form.config.ElementBean;
import zt.cms.report.dyn.ReportSql;
import zt.platform.form.util.SqlWhereUtil;
import zt.platform.user.UserManager;
import zt.cms.pub.SCUser;
import zt.platform.form.config.SystemAttributeNames;
import zt.cms.pub.SCBranch;

/********************************************
 *
 *    ��̬������¼��  ��ϸҳ��
 *
 *******************************************/

public class ReportDataPage1 extends FormActions
{
    String strFlag=null;             //��д��־
    public  Param  params=null;     //���͵ı�������
    private Param paramg = null;     //��õı�������

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,ErrorMessages msgs,
                    EventManager manager, String parameter)
    {
        paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);

        if (paramg == null)
        {
            msgs.add("���ݲ�������");
            return -1;
        }
        strFlag = (String) paramg.getParam(ParamName.Flag);

        if (strFlag.equals(ParamName.Flag_WRITE))
        {
            instance.setReadonly(false);
        }
        else
        {
            instance.setReadonly(true);
        }

        return 0;
    }
    public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, String reffldnm,SqlWhereUtil sqlWhereUtil)
    {

        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if (reffldnm.equals("BRHID")) {
            //BRHID���û����㣩
            String BRHID = SCUser.getBrhId(um.getUserName());
            if (BRHID == null || BRHID.length() < 1) {
                return -1;
            }
            //APPBRHIDs���û������µ�����ʵ���㣬�����Լ���
            String SUBBRHIDs = SCBranch.getSubBrh2(BRHID);
            if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
                msgs.add("����ʵ���㲻���ڣ�");
                return -1;
            }
            else {
                SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
            }
            //sqlWhereUtil
            if (reffldnm.equals("BRHID")) {
                sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", SUBBRHIDs,
                                           SqlWhereUtil.DataType_Sql,
                                           sqlWhereUtil.OperatorType_In);
            }
        }
        return 0;
    }

   public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
            ErrorMessages msgs, EventManager manager)
    {
        String strRptNo=ctx.getParameter("REPNO");
        String strMonth=ctx.getParameter("MONTH");
        String strBrhid=ctx.getParameter("BRHID");

        params=new Param();

        params.addParam("REPNO",strRptNo);
        params.addParam("MONTH",strMonth);
        params.addParam("BRHID",strBrhid);
        params.addParam("OPER","ADD");
        params.addParam(ParamName.Flag,strFlag);

        ctx.setRequestAtrribute(ParamName.ParamName,params);

        if (button != null && button.equals("goon"))
        {
            trigger(manager, "REPORTDATAPAGE", null);
        }
        return 0;
    }


}