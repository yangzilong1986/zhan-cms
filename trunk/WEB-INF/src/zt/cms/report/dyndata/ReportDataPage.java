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
import zt.platform.db.DBUtil;
import zt.cms.report.pub.equition.EquitionPool;

/********************************************
 *
 *    ��̬������¼��  ��ϸҳ��
 *
 *******************************************/

public class ReportDataPage extends FormActions
{
    String strFlag=null;             //��д��־
    private Param paramg = null;     //��õı�������
    private String strRptNo=null;    //��Ŀ���
    private String strMonth=null;    //�·�
    private String strBrhid=null;    //�����
    String[] name;                   //����
    String[] dpNoL;                  //��Ҫ��ƽ����
    String[] blNol;                  //��Ҫ���ʵ���
    String[] dpgsNo;                 //�еĴ�ƽ��ʽ
    String[] blgsNo;                 //�еı��ʹ�ʽ
    int intDp=0;                     //��Ҫ��ƽ������
    int intBl=0;                     //��Ҫ���ʵ�����

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
        strRptNo = (String) paramg.getParam("REPNO");
        strMonth=(String) paramg.getParam("MONTH");
        strBrhid=(String) paramg.getParam("BRHID");
        System.out.println("BRHID:"+strBrhid);
        System.out.println("REPNO:"+strRptNo);
        System.out.println("MONTH:"+strMonth);
        String strOper=(String) paramg.getParam("OPER");

        if (strFlag.equals(ParamName.Flag_WRITE))
        {
            instance.setReadonly(false);
        }
        else
        {
            instance.setReadonly(true);
        }

        String strSql = ReportSql.getColSql(strRptNo);
        //System.out.print(strSql);
        RecordSet rs=conn.executeQuery(strSql);

        int colnum=0;
        String colname="";
        String dpgs=null;       //������ж���Ĵ�ƽ��ʽ����
        String blgs=null;       //������ж���ı��ʹ�ʽ����
        if(rs.next())
        {
            colnum=rs.getInt(1);                     //���������
            colname=DBUtil.fromDB(rs.getString(5));  //�������
            dpgs=rs.getString(7);                    //��ô�ƽ��ʽ
            blgs=rs.getString(8);                    //��ñ��ʹ�ʽ
        }

        if(colname==null || colname=="")
        {
            msgs.add("�������ݻ��ʧ�ܣ�");
            return -1;
        }
        else
            name=colname.split(",");
        if(dpgs!=null)//��ƽ�еĴ���
        {

            try
            {
                EquitionPool pool = EquitionPool.buildEquitionPool(dpgs.trim());
                dpgs = pool.generateOut();
            }catch(IllegalArgumentException e)
            {
                msgs.add("��ƽ��ʽ��ʽ����");
                return -1;
            }
            //System.out.println("dpgs:"+dpgs);
            dpgsNo=dpgs.split(",");
            intDp=dpgsNo.length;               //��Ҫ��ƽ������
            //System.out.println("intDp:"+intDp);
            if(intDp<1)
            {
               msgs.add("��ƽ��������ô���");
               return -1;
            }
            dpNoL=new String[intDp];
            for(int i=0;i<intDp;i++)
            {
                dpNoL[i]=dpgsNo[i].trim().substring(0,1);
            }
        }
        if(blgs!=null)
        {
            blgsNo=blgs.trim().split(",");
            intBl=blgsNo.length;
            blNol=new String[intBl];
            for(int i=0;i<intBl;i++)
            {
                blNol[i]=blgsNo[i].trim().substring(0,1);
            }
        }

        instance.useCloneFormBean();
        FormBean fb=instance.getFormBean();
        ElementBean[] eb = new  ElementBean[20];
        for(int i=1;i<=25;i++)
        {
            if(i<=name.length)
            {
                eb[i-1] = fb.getElement("F"+i);
                eb[i-1].setCaption(name[i-1]);
                for(int m=0;m<intDp;m++)
                {
                    if(dpNoL[m].equals(String.valueOf(i)))
                    {
                        eb[i - 1].setReadonly(true);
                        //eb[i - 1].setCaption("<font color=\"red\">"+eb[i-1].getCaption()+"</font>");
                    }
                }
                for(int n=0;n<intBl;n++)
                {
                    if(blNol[n].equals(String.valueOf(i)))
                        eb[i - 1].setReadonly(true);
                }
            }
            else
            {
                ElementBean eb2 = fb.getElement("F" + i);
                eb2.setCaption("��Ч������");
                //eb2.setVisible(false);
                eb2.setComponetTp(6);
                eb2.setReadonly(true);
            }
            fb.setRows(colnum/2+3);
        }

        if ( strOper==null)
        {
            instance.setValue("REPNO",strRptNo);
            instance.setValue("BRHID",strBrhid);
            instance.setValue("MONTH",strMonth);
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
        return 0;
    }
    public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                            ErrorMessages msgs,
                            EventManager manager)
    {
        instance.setValue("REPNO",strRptNo);
        instance.setValue("MONTH",strMonth);
        instance.setValue("BRHID",strBrhid);

        return 0;
    }
    public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         ErrorMessages msgs,
                         EventManager manager, SqlAssistor assistor)
    {
        int tmpRptNo=0;
        String brhid=ctx.getParameter("BRHID").trim();
        String month=ctx.getParameter("MONTH").trim();

        for(int i=0;i<this.intDp;i++)//�����ƽ��ʽ�����Զ��ϼ�
        {
            int mm=0;
            int dpSum=0;//��ƽ���

            String strDpNor=dpgsNo[i].trim().substring(2);//���ʵ�ʽ�ұ�
            //System.out.println("strDpNor:"+strDpNor);
            String dpNoR[]=strDpNor.split("\\+");//��ֱ��ʹ�ʽ�ұߵ���
            if(dpNoR==null)
            {
               msgs.add("��ƽ���ұ����ô���");
               return -1;
            }
            else
            {
                mm = dpNoR.length; //��ʽ�ұߺ��е�����
            }
           for(int m=0;m<mm;m++)
           {
                String str=ctx.getParameter("F"+dpNoR[m]);
                if(str!=null && !str.equals(""))
                {
                    dpSum+=new Double(str).intValue();
                }
           }

           String strColNo="F"+dpNoL[i];
           System.out.println("DPSUM:"+dpSum);
           System.out.println("STRCOLNO:"+strColNo);
           if(strColNo!=null)
             assistor.setSqlFieldValue(assistor.getDefaultTbl(), strColNo, String.valueOf(dpSum));
        }

        StringBuffer strBuf=new StringBuffer();
        strBuf.append("select count(*) from reportdef where repno='");
        strBuf.append(this.strRptNo);
        strBuf.append("' and month='");
        strBuf.append(month);
        strBuf.append("' and brhid='");
        strBuf.append(brhid);
        strBuf.append("'");

        RecordSet rs=conn.executeQuery(strBuf.toString());
        if(rs.next())
        {
            tmpRptNo=rs.getInt(0);
        }
        if (tmpRptNo>0)
        {
            msgs.add("��ǰ���㵱ǰ�·����ݴ��ڣ�");
            return -1;
        }
        else
          return 0;
    }
    public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                       ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor)
    {
        int tmpRptNo=0;
        String brhid=ctx.getParameter("BRHID").trim();
        String month=ctx.getParameter("MONTH").trim();

        for(int i=0;i<this.intDp;i++)//�����ƽ��ʽ�����Զ��ϼ�
        {
            int mm=0;
            int dpSum=0;//��ƽ���

            String strDpNor=dpgsNo[i].trim().substring(2);
            //System.out.println("strDpNor:"+strDpNor);
            String dpNoR[]=strDpNor.split("\\+");
            if(dpNoR==null)
            {
               msgs.add("��ƽ���ұ����ô���");
               return -1;
            }
            else
            {
                mm = dpNoR.length; //��ʽ�ұߺ��е�����
            }
           for(int m=0;m<mm;m++)
           {
                String str=ctx.getParameter("F"+dpNoR[m]);
                if(str!=null && !str.equals(""))
                {
                    dpSum+=new Double(str).intValue();

                }
           }

           //��ƽ��ʽ�����ݵ�����
           String strColNo="F"+dpNoL[i];
           System.out.println("DPSUM:"+dpSum);
           System.out.println("STRCOLNO:"+strColNo);
           if(strColNo!=null)
             assistor.setSqlFieldValue(assistor.getDefaultTbl(), strColNo, String.valueOf(dpSum));
        }
        return 0;
    }

}