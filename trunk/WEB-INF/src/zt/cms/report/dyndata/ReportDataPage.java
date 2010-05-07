package zt.cms.report.dyndata;

/**
 * <p>Title: 信贷管理系统</p>
 * <p>Description: 潍坊信贷</p>
 * <p>Copyright: Copyright (c) 2003  中天信息技术有限公司</p>
 * <p>Company: 中天信息技术有限公司</p>
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
 *    动态表数据录入  详细页面
 *
 *******************************************/

public class ReportDataPage extends FormActions
{
    String strFlag=null;             //读写标志
    private Param paramg = null;     //获得的变量集合
    private String strRptNo=null;    //项目编号
    private String strMonth=null;    //月份
    private String strBrhid=null;    //网点号
    String[] name;                   //列名
    String[] dpNoL;                  //需要打平的列
    String[] blNol;                  //需要比率的列
    String[] dpgsNo;                 //列的打平公式
    String[] blgsNo;                 //列的比率公式
    int intDp=0;                     //需要打平的列数
    int intBl=0;                     //需要比率的列数

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,ErrorMessages msgs,
                EventManager manager, String parameter)
    {
        paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);

        if (paramg == null)
        {
            msgs.add("数据不完整！");
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
        String dpgs=null;       //定义表中定义的打平公式内容
        String blgs=null;       //定义表中定义的比率公式内容
        if(rs.next())
        {
            colnum=rs.getInt(1);                     //获得总列数
            colname=DBUtil.fromDB(rs.getString(5));  //获得列明
            dpgs=rs.getString(7);                    //获得打平公式
            blgs=rs.getString(8);                    //获得比率公式
        }

        if(colname==null || colname=="")
        {
            msgs.add("列名数据获得失败！");
            return -1;
        }
        else
            name=colname.split(",");
        if(dpgs!=null)//打平列的处理
        {

            try
            {
                EquitionPool pool = EquitionPool.buildEquitionPool(dpgs.trim());
                dpgs = pool.generateOut();
            }catch(IllegalArgumentException e)
            {
                msgs.add("打平公式格式有误！");
                return -1;
            }
            //System.out.println("dpgs:"+dpgs);
            dpgsNo=dpgs.split(",");
            intDp=dpgsNo.length;               //需要打平的列数
            //System.out.println("intDp:"+intDp);
            if(intDp<1)
            {
               msgs.add("打平列左边设置错误！");
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
                eb2.setCaption("无效数据项");
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

        for(int i=0;i<this.intDp;i++)//处理打平公式数据自动合计
        {
            int mm=0;
            int dpSum=0;//打平结果

            String strDpNor=dpgsNo[i].trim().substring(2);//比率等式右边
            //System.out.println("strDpNor:"+strDpNor);
            String dpNoR[]=strDpNor.split("\\+");//拆分比率公式右边的列
            if(dpNoR==null)
            {
               msgs.add("打平列右边设置错误！");
               return -1;
            }
            else
            {
                mm = dpNoR.length; //等式右边含有的列数
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
            msgs.add("当前网点当前月份数据存在！");
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

        for(int i=0;i<this.intDp;i++)//处理打平公式数据自动合计
        {
            int mm=0;
            int dpSum=0;//打平结果

            String strDpNor=dpgsNo[i].trim().substring(2);
            //System.out.println("strDpNor:"+strDpNor);
            String dpNoR[]=strDpNor.split("\\+");
            if(dpNoR==null)
            {
               msgs.add("打平列右边设置错误！");
               return -1;
            }
            else
            {
                mm = dpNoR.length; //等式右边含有的列数
            }
           for(int m=0;m<mm;m++)
           {
                String str=ctx.getParameter("F"+dpNoR[m]);
                if(str!=null && !str.equals(""))
                {
                    dpSum+=new Double(str).intValue();

                }
           }

           //打平公式列数据的置入
           String strColNo="F"+dpNoL[i];
           System.out.println("DPSUM:"+dpSum);
           System.out.println("STRCOLNO:"+strColNo);
           if(strColNo!=null)
             assistor.setSqlFieldValue(assistor.getDefaultTbl(), strColNo, String.valueOf(dpSum));
        }
        return 0;
    }

}