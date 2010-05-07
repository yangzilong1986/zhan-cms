package zt.cms.report.dyn;

import zt.cms.report.TableBuilder;
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import com.ming.webreport.*;
import com.zt.util.PropertyManager;
import zt.cmsi.mydb.MyDB;
import zt.platform.db.DBUtil;
import zt.cms.pub.SCUser;
import zt.cmsi.pub.define.SystemDate;
import zt.cms.pub.SCBranch;
import zt.platform.form.util.SessionAttributes;
import javax.servlet.http.HttpSession;
import zt.platform.user.UserManager;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.db.DatabaseConnection;
import java.math.BigDecimal;

/**
 * <p>Title: �Ŵ�����ϵͳ</p>
 * <p>Description: Ϋ���Ŵ�</p>
 * <p>Copyright: Copyright (c) 2003  ������Ϣ�������޹�˾</p>
 * <p>Company: ������Ϣ�������޹�˾</p>
 * @author Yusg
 * @version 1.0
 */

public class ReportBuilder
{

    public DatabaseConnection dbc = null; //���ݿ�����
    public Connection conn = null;        //���ݿ�����
    public Statement stmt = null;         //���ݿ�����״̬

    public  String strReptNo;             //������
    public  String errMsg=null;           //�������

    public  String strRepName=null;         //��̬��������
    private String strLoginName;            //��½�û���������������
    private String strLoginBrhid;           //��½�û���������
    private String strBrhId;                //����������
    private String strScbrhName;            //������������
    private String strEndDate;              //��ǰ��ҵ������
    private String strLTitle=null;          //�����
    private String strMTitle=null;          //�б���
    private String strRTitle=null;          //�ұ���
    private String strCollen=null;          //����ĸ��еĳ���
    private String strRate=null;            //���ʹ�ʽ
    private String[] blgsNo;                //�еı��ʹ�ʽ
    private String[] blNol;                 //��Ҫ���ʵ���
    int intBl=0;                            //��Ҫ���ʵ�����
    private String strTenth=null;           //�Ƿ�����Ԫ��ʾ
    public  int    intColNum=0;             //��Ч����
    public  int    intRowNum=0;             //��Ч����
    public  int    intInitNum=10;           //
    public  String strColName[]=new String[20];//������
    private String strMonth;                //ͳ���·�

    private String tmpName;                 //
    private String tmpCol[];                //
    public  String [][]tmpData=null;        //��ʱ����������
    public String tmpBranch[][]=null;       //����¼���������
    public String dataBranch[][]=null;      //�����ʾ������������
    int rs_Type = ResultSet.TYPE_SCROLL_INSENSITIVE;
    int rs_Cur = ResultSet.CONCUR_READ_ONLY;

    public ReportBuilder(HttpServletRequest request)
    {
        this.strBrhId=getBrhId(request);
        if(request.getParameter("month")!=null)
          this.strMonth=request.getParameter("month").trim();
        else
          this.strMonth="00";

        if(request.getParameter("rptno")!=null)
          this.strReptNo=request.getParameter("rptno").trim();

        if(this.strReptNo==null)
            errMsg="û�л�ñ����ţ�";
        if(this.strLoginBrhid==null)
            errMsg="��õ�½�û���������ʧ�ܣ������µ�½��";
        if(this.strBrhId==null)
            errMsg="��ñ�������ʧ�ܣ������µ�½��";
        strEndDate = SystemDate.getYesterday("-");
        try {
            dbc = MyDB.getInstance().apGetConn();
            conn = dbc.getConnection();
            stmt = conn.createStatement(this.rs_Type,this.rs_Cur);
        }
        catch (Exception e) {
            errMsg="���ݿ�����ʧ�ܣ�";
            MyDB.getInstance().apReleaseConn(1);//�ͷ����ݿ�����
        }
    }
    /********    ��õ�½�û�������    ********/
    private String getBrhId(HttpServletRequest request)
    {
        String strScbrhId = null;

        HttpSession session = (HttpSession) request.getSession(true);
        UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if (um == null) {
            this.errMsg = "������ʱ�������µ�½��";
            return strScbrhId;
        }
        else
        {
            try
            {
                String strUserName = um.getUserName();
                this.strLoginBrhid = SCUser.getBrhId(strUserName);
                this.strLoginName = SCBranch.getSName(this.strLoginBrhid);
            }
            catch (Exception e)
            {
                errMsg = "��õ�½�û���������ʧ�ܣ������µ�½��";
            }
        }

        if(request.getParameter("brhid")!=null)
        {
            String strAllSubAndSelf=SCBranch.getAllSubBrhAndSelf1(this.strLoginBrhid);
            strScbrhId= request.getParameter("brhid").trim();
            if(strAllSubAndSelf.indexOf(strScbrhId)==-1)
            {
                strScbrhId=this.strLoginBrhid;
                strScbrhName=this.strLoginName;
            }
            else
            {
                strScbrhName = SCBranch.getSName(strScbrhId);
            }
        }
        else
        {
            strScbrhId=this.strLoginBrhid;
            strScbrhName=this.strLoginName;
        }
        return strScbrhId;
    }
    /*****  �����������������������  *****/
    private void setScbranch()
    {
        ResultSet rsData=null;
        int i=0;

        String strSql=ReportSql.geBrhSql(this.strBrhId);
        System.out.println("branchSql:"+strSql);
        if(this.errMsg==null)
        {
            try
            {
                rsData=stmt.executeQuery(strSql);
                rsData.last();
                this.intRowNum=rsData.getRow();//�������
                if(this.intRowNum>10)
                    this.intInitNum=this.intRowNum;

                //System.out.println(this.intInitNum);
                tmpBranch=new String[this.intRowNum+2][2];
                dataBranch=new String[this.intInitNum][2];
                rsData.beforeFirst();
                while(rsData.next())
                {
                    dataBranch[i][0]=rsData.getString(2).trim();
                    dataBranch[i][1]=DBUtil.fromDB(rsData.getString(3)).trim();
                    tmpBranch[i][0]=rsData.getString(2).trim();
                    tmpBranch[i][1]=DBUtil.fromDB(rsData.getString(3)).trim();
                    i++;
                }
                //System.out.println("i:"+i);
                for(int m=i;m<this.intInitNum;m++)
                {
                    dataBranch[m][0]="-1";
                    dataBranch[m][1]="";
                }
                this.AddUserBrhid(this.intRowNum);
            }
            catch (SQLException se)
            {
                MyDB.getInstance().apReleaseConn(1); //�ͷ����ݿ�����
                this.errMsg = "����ͳ��ʧ�ܣ�";
            }
        }
    }
    /*****  ��������������������,�ŵ�������е�MRDataSet��   ****/
    private MRDataSet getReportData()
    {
        ResultSet rsData = null;           //
        MRDataSet mrds=new MRDataSet();    //
        String tmpMnt=null;                //
        tmpData=new String[this.intInitNum][27];
        String [][]data=new String[this.intInitNum][26];
        int i=0;

        if (this.strMonth.equals("00"))
            tmpMnt = this.strEndDate.substring(5, 7);
        else
            tmpMnt = this.strMonth;

        String strSql=ReportSql.getSql(this.strReptNo,this.strBrhId,tmpMnt);
        System.out.println("DataSql:"+strSql);
        if (this.errMsg != null)
            return null;

        try
        {
            rsData = stmt.executeQuery(strSql);
            while (rsData.next()) {
                for (int m = 0; m < 27; m++) {
                    tmpData[i][m] = DBUtil.fromDB(rsData.getString(m + 1));
                }
                i++;
            }
        }
        catch (SQLException se)
        {
            this.errMsg = "��������ͳ��ʧ�ܣ�";
        }
        finally
        {
            MyDB.getInstance().apReleaseConn(1); //�ͷ����ݿ�����
        }

        for(int n=0;n<this.intInitNum;n++)
        {
            String aaBrhid=dataBranch[n][0].trim();
            //System.out.println("aaBrhid:"+aaBrhid);
            data[n][0]=dataBranch[n][1];
            for(int h=0;h<i;h++)
            {
                String bbBrhid=tmpData[h][0].trim();
                if(aaBrhid.equals(bbBrhid))
                {
                    System.out.println("==="+bbBrhid+"=="+aaBrhid+"===");
                    for(int j=1;j<=25;j++)
                    {
                        String strRR=this.getRate(h,j);//���ʴ���
                        //System.out.println("strRR:"+strRR);

                        if(strRR.equals("-1"))
                            data[n][j] = tmpData[h][j + 1];
                        else
                            data[n][j] = strRR;
                        //System.out.println("data["+n+"]["+j+"]"+data[n][j]);
                    }
                    break;
                }
            }
        }
        for(int n=0;n<this.intInitNum;n++)
        {
           DataRecord rec = new DataRecord();
           for(int h=0;h<=25;h++)
           {
               if(h==0)
                   rec.setValue("sname",data[n][0]);
               else
               {
                   String strLabel="F"+h;
                   if(data[n][h]==null)
                       rec.setValue(strLabel,new Integer("0"));
                   else
                   {
                       int recInt=0;
                       /*=====��ʼ======�����Ƿ���Ԫ��ʾ====��ʼ===*/
                       if(strTenth!=null && !strTenth.equals(""))
                       {
                           //System.out.println("TenTh:"+this.strTenth);
                           String[] tenTh = this.strTenth.split(",");
                           //System.out.println("Lenth:"+tenTh.length);

                           int a=0;
                           for(a=0;a<tenTh.length;a++)
                           {
                               int aa = -1;
                               try
                               {
                                   aa = new Integer(tenTh[a].trim()).intValue();
                               }catch(Exception e)
                               {
                                   this.errMsg ="�Ƿ���Ԫ���д��ڷ����ֵķǷ��ַ�";
                               }
                               //System.out.println("N:"+n+"  H:"+h+"   AA:"+aa);
                               if(aa==h)
                               {
                                   recInt = new Double(data[n][h]).intValue();
                                   recInt=recInt/10000;
                                   break;
                               }
                           }

                           if(a==tenTh.length)//ѭ�����������û���ҵ�,��Ҫ����Ԫ��ʾ��
                           {
                               //System.out.println("H:"+h);
                               recInt = new Double(data[n][h]).intValue();
                           }
                           //System.out.println("recInt:"+recInt);
                       }
                       /*======����=====�����Ƿ���Ԫ��ʾ====����===*/
                       else
                       {
                           recInt = new Double(data[n][h]).intValue();
                       }
                       rec.setValue(strLabel, new Integer(recInt));
                   }
               }

           }
           mrds.addRow(rec);
        }
        return mrds;
    }


    /****   ��ñ���Ļ�����Ϣ  *****/
    private void setBasicData()
    {
        ResultSet rsData = null;
        if (this.errMsg == null)
        {
            try {
                String strSql = ReportSql.getColSql(this.strReptNo);
                System.out.println("colnum:"+strSql);
                Statement stmtBasic = conn.createStatement();
                rsData = stmtBasic.executeQuery(strSql);
                if(rsData.next())
                {
                    this.strRepName=DBUtil.fromDB(rsData.getString(1));
                    this.intColNum=rsData.getInt(2);
                    this.strLTitle=rsData.getString(3);
                    this.strMTitle=rsData.getString(4);
                    this.strRTitle=rsData.getString(5);
                    this.tmpName=DBUtil.fromDB(rsData.getString(6));
                    this.strCollen=rsData.getString(7);
                    this.strRate=rsData.getString(9);
                    this.strTenth=rsData.getString(10);
                }
            }
            catch (SQLException se) {
                MyDB.getInstance().apReleaseConn(1); //�ͷ����ݿ�����
                this.errMsg = "��������У��ʧ�ܣ�";
            }
        }
    }

    /****   Ԥ����̬����Ļ�����Ϣ  ****/
    public void setDefine()
    {
        this.setBasicData();//��ñ���Ļ�����Ϣ
        this.setRate();     //������ı��ʹ�ʽ������������

        if(this.intColNum>0 && this.errMsg==null)
        {
            this.tmpCol=this.tmpName.split(",");
            if(this.tmpCol.length!=this.intColNum)
            {
                this.errMsg="�������������������еĸ���";
            }
        }
        else if(this.intColNum==0 && this.errMsg==null)
        {
             this.errMsg="���������������";
        }

    }

    /*******  �������  *********/
    public String showReport(String id)
    {
        String reportStr =null;
        MRDataSet mrds=this.getReportData();
        if(mrds==null)
            return reportStr;

        String strRoot=PropertyManager.getProperty("root");//��ñ���ľ���·��
        System.out.println(strRoot);
        try {
            MREngine engine = new MREngine();
            engine.addMRDataSet("rs7", mrds);//�󶨻�õ�����
            engine.bind("DY0101",strRoot+"Dyn/DY0101.mrf");//�þ���·���󶨱���
            reportStr = engine.createViewer(id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return reportStr;
    }

    public String getErrMsg()//��ô�����Ϣ
    {
        return this.errMsg;
    }
    public String[][] getArry()//����¼�������
    {
        this.setScbranch();
        return this.tmpBranch;
    }

    public String getScbranchName()//��õ�ǰ���������
    {
        return this.strScbrhName;
    }
    public String getYear()
    {
        String strYear=null;

        String tmpYear=this.strEndDate.substring(0,4);
        if(this.strMonth.equals("12"))
        {
            int intYear=Integer.parseInt(tmpYear)-1;
            tmpYear=String.valueOf(intYear);
        }
        strYear=tmpYear;
        return strYear;
    }
    public String getMonth()
    {
        String strMnt=null;

        String tmpMnt=this.strEndDate.substring(5,7);
        int intMnt=Integer.parseInt(tmpMnt);
        int intMonth=Integer.parseInt(this.strMonth);
        if(intMonth>intMnt && intMonth!=12)
            tmpMnt="-1";
        else
        {
            if(!this.strMonth.equals("00"))
            tmpMnt = this.strMonth;
        }

        strMnt=tmpMnt;
        return strMnt;
    }
    /*****   ��ö��������   *****/
    public String getColName()
    {
        return this.tmpName;
    }
    /****   ��ö�����г���   ****/
    public String getColLen()
    {
        if(this.strCollen==null)
            this.strCollen="0";
        else
            this.strCollen=this.strCollen.trim();
        return this.strCollen;
    }
    /****   ��ö���ı���λ��   ****/
    public String getTitleLeft()
    {
        if(this.strMTitle==null)
            this.strMTitle="0";
        else
            this.strMTitle=this.strMTitle.trim();
        return this.strMTitle;
    }
    //��֤�¼��������������ʼ�մ��е�½�û�������
    public void AddUserBrhid(int count)
    {
        this.tmpBranch[count][0]=this.strLoginBrhid;
        this.tmpBranch[count][1]="----------------";
        this.tmpBranch[count+1][0]=this.strLoginBrhid;
        this.tmpBranch[count+1][1]=this.strLoginName;
    }

    /***************************************************
     * function :�ж��Ƿ��Ǳ����У�����Ǳ����л�ñ��ʽ��
     * parameter: rn int   �������������ݵ�����
     *            cn int   �������������ݵ�����
     * return   : -1   �����ڱ��ʹ�ʽ���ߴ��в��Ǳ�����
     ****************************************************/
    public String getRate(int rn, int cn)
    {
        String strNum = "-1";

        //System.out.println("rowNo:"+rn+"  colNo:"+cn);

        /*======��ʼ======������ʹ�ʽ======��ʼ=====*/
        if(this.blNol==null)
            return strNum;

        for (int m = 0; m < this.intBl; m++)
        {
            int mm = 0;

            if(blNol[m].equals(String.valueOf(cn)))
            {
                String strBlNor = blgsNo[m].trim().substring(2);
                //System.out.println("strBlNor:"+strBlNor);
                String blNoR[] = strBlNor.split("/");
                if (blNoR != null)
                {
                    mm = blNoR.length; //��ʽ�ұߺ��е�����
                    if (mm == 2)
                    {
                        int intFZ = new Integer(blNoR[0]).intValue();
                        int intFM = new Integer(blNoR[1]).intValue();
                        //System.out.println("intFZ:"+intFZ);
                        //System.out.println("intFM:"+intFM);
                        String strFZ = tmpData[rn][intFZ+1];
                        String strFM = tmpData[rn][intFM+1];
                        if (strFM.equals("0.00"))
                        { //�����ĸΪ0
                            strNum = "0";
                        }
                        else
                        {
                            //System.out.println("strFZ:"+strFZ);
                            //System.out.println("strFM:"+strFM);

                            float flNum=0;
                            try
                            {
                                BigDecimal bdFZ = new BigDecimal(strFZ);
                                BigDecimal bdFM = new BigDecimal(strFM);
                                BigDecimal bdJG = bdFZ.divide(bdFM, 2, BigDecimal.ROUND_HALF_UP);
                                //System.out.println("dbJG:"+bdJG.toString());
                                flNum=bdJG.floatValue();
                            }catch(Exception e)
                            {
                                System.out.println("�������ұ����ô���");
                            }

                            //float flNum = (flFZ*100)/flFM;
                            String tmpNum=String.valueOf(flNum*100);

                            int intDex=tmpNum.indexOf(".");
                            if(intDex>3)
                              strNum=tmpNum.substring(0, 3);
                            else
                              strNum=tmpNum.substring(0,intDex);
                        }
                    }
                    else
                    {
                       strNum = "0";
                    }
                }
                else
                {
                    this.errMsg = "�������ұ����ô���";
                }
            }
        }
        /*======����======���ʹ�ʽ����=====����=====*/
        return strNum;
    }

    /************������ʵĻ�����Ϣ*******/
    public void setRate()
    {
        if (this.strRate != null && !this.strRate.equals(""))
        {
            blgsNo = this.strRate.split(",");
            this.intBl = blgsNo.length;
            blNol = new String[intBl];
            for (int k = 0; k < intBl; k++)
            {
                blNol[k] = blgsNo[k].trim().substring(0, 1);
            }
        }
    }

}