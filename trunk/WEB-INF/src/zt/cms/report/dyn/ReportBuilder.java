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
 * <p>Title: 信贷管理系统</p>
 * <p>Description: 潍坊信贷</p>
 * <p>Copyright: Copyright (c) 2003  中天信息技术有限公司</p>
 * <p>Company: 中天信息技术有限公司</p>
 * @author Yusg
 * @version 1.0
 */

public class ReportBuilder
{

    public DatabaseConnection dbc = null; //数据库连接
    public Connection conn = null;        //数据库连接
    public Statement stmt = null;         //数据库连接状态

    public  String strReptNo;             //报表编号
    public  String errMsg=null;           //错误代码

    public  String strRepName=null;         //动态报表名称
    private String strLoginName;            //登陆用户的所在网点名称
    private String strLoginBrhid;           //登陆用户的网点编号
    private String strBrhId;                //报表网点编号
    private String strScbrhName;            //报表网点名称
    private String strEndDate;              //当前的业务日期
    private String strLTitle=null;          //左标题
    private String strMTitle=null;          //中标题
    private String strRTitle=null;          //右标题
    private String strCollen=null;          //定义的各列的长度
    private String strRate=null;            //比率公式
    private String[] blgsNo;                //列的比率公式
    private String[] blNol;                 //需要比率的列
    int intBl=0;                            //需要比率的列数
    private String strTenth=null;           //是否是万元显示
    public  int    intColNum=0;             //有效列数
    public  int    intRowNum=0;             //有效行数
    public  int    intInitNum=10;           //
    public  String strColName[]=new String[20];//列名称
    private String strMonth;                //统计月份

    private String tmpName;                 //
    private String tmpCol[];                //
    public  String [][]tmpData=null;        //临时的网点数据
    public String tmpBranch[][]=null;       //获得下级网点数组
    public String dataBranch[][]=null;      //获得显示数据网点数组
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
            errMsg="没有获得报表编号！";
        if(this.strLoginBrhid==null)
            errMsg="获得登陆用户所在网点失败，请重新登陆！";
        if(this.strBrhId==null)
            errMsg="获得报表网点失败，请重新登陆！";
        strEndDate = SystemDate.getYesterday("-");
        try {
            dbc = MyDB.getInstance().apGetConn();
            conn = dbc.getConnection();
            stmt = conn.createStatement(this.rs_Type,this.rs_Cur);
        }
        catch (Exception e) {
            errMsg="数据库连接失败！";
            MyDB.getInstance().apReleaseConn(1);//释放数据库连接
        }
    }
    /********    获得登陆用户的网点    ********/
    private String getBrhId(HttpServletRequest request)
    {
        String strScbrhId = null;

        HttpSession session = (HttpSession) request.getSession(true);
        UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if (um == null) {
            this.errMsg = "操作超时，请重新登陆！";
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
                errMsg = "获得登陆用户所在网点失败，请重新登陆！";
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
    /*****  将所包括的网点放入数组中  *****/
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
                this.intRowNum=rsData.getRow();//获得行数
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
                MyDB.getInstance().apReleaseConn(1); //释放数据库连接
                this.errMsg = "网点统计失败！";
            }
        }
    }
    /*****  将报表数据整理到数组中,放到明宇报表中的MRDataSet中   ****/
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
            this.errMsg = "报表数据统计失败！";
        }
        finally
        {
            MyDB.getInstance().apReleaseConn(1); //释放数据库连接
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
                        String strRR=this.getRate(h,j);//比率处理
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
                       /*=====开始======处理是否万元显示====开始===*/
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
                                   this.errMsg ="是否万元列中存在非数字的非法字符";
                               }
                               //System.out.println("N:"+n+"  H:"+h+"   AA:"+aa);
                               if(aa==h)
                               {
                                   recInt = new Double(data[n][h]).intValue();
                                   recInt=recInt/10000;
                                   break;
                               }
                           }

                           if(a==tenTh.length)//循环到数组结束没有找到,不要求万元显示列
                           {
                               //System.out.println("H:"+h);
                               recInt = new Double(data[n][h]).intValue();
                           }
                           //System.out.println("recInt:"+recInt);
                       }
                       /*======结束=====处理是否万元显示====结束===*/
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


    /****   获得报表的基本信息  *****/
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
                MyDB.getInstance().apReleaseConn(1); //释放数据库连接
                this.errMsg = "报表数据校验失败！";
            }
        }
    }

    /****   预处理动态报表的基本信息  ****/
    public void setDefine()
    {
        this.setBasicData();//获得报表的基本信息
        this.setRate();     //处理报表的比率公式，放入数组中

        if(this.intColNum>0 && this.errMsg==null)
        {
            this.tmpCol=this.tmpName.split(",");
            if(this.tmpCol.length!=this.intColNum)
            {
                this.errMsg="报表列数不等于列名中的个数";
            }
        }
        else if(this.intColNum==0 && this.errMsg==null)
        {
             this.errMsg="报表列数定义错误";
        }

    }

    /*******  绑定明宇报表  *********/
    public String showReport(String id)
    {
        String reportStr =null;
        MRDataSet mrds=this.getReportData();
        if(mrds==null)
            return reportStr;

        String strRoot=PropertyManager.getProperty("root");//获得报表的绝对路径
        System.out.println(strRoot);
        try {
            MREngine engine = new MREngine();
            engine.addMRDataSet("rs7", mrds);//绑定获得的数据
            engine.bind("DY0101",strRoot+"Dyn/DY0101.mrf");//用绝对路径绑定报表
            reportStr = engine.createViewer(id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return reportStr;
    }

    public String getErrMsg()//获得错误信息
    {
        return this.errMsg;
    }
    public String[][] getArry()//获得下级的网点
    {
        this.setScbranch();
        return this.tmpBranch;
    }

    public String getScbranchName()//获得当前网点的名称
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
    /*****   获得定义的列名   *****/
    public String getColName()
    {
        return this.tmpName;
    }
    /****   获得定义的列长度   ****/
    public String getColLen()
    {
        if(this.strCollen==null)
            this.strCollen="0";
        else
            this.strCollen=this.strCollen.trim();
        return this.strCollen;
    }
    /****   获得定义的标题位置   ****/
    public String getTitleLeft()
    {
        if(this.strMTitle==null)
            this.strMTitle="0";
        else
            this.strMTitle=this.strMTitle.trim();
        return this.strMTitle;
    }
    //保证下级网点的下拉窗口始终带有登陆用户的网点
    public void AddUserBrhid(int count)
    {
        this.tmpBranch[count][0]=this.strLoginBrhid;
        this.tmpBranch[count][1]="----------------";
        this.tmpBranch[count+1][0]=this.strLoginBrhid;
        this.tmpBranch[count+1][1]=this.strLoginName;
    }

    /***************************************************
     * function :判断是否是比率列，如果是比率列获得比率结果
     * parameter: rn int   网点数组中数据的行数
     *            cn int   网点数组中数据的列数
     * return   : -1   不存在比率公式或者此列不是比率列
     ****************************************************/
    public String getRate(int rn, int cn)
    {
        String strNum = "-1";

        //System.out.println("rowNo:"+rn+"  colNo:"+cn);

        /*======开始======处理比率公式======开始=====*/
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
                    mm = blNoR.length; //等式右边含有的列数
                    if (mm == 2)
                    {
                        int intFZ = new Integer(blNoR[0]).intValue();
                        int intFM = new Integer(blNoR[1]).intValue();
                        //System.out.println("intFZ:"+intFZ);
                        //System.out.println("intFM:"+intFM);
                        String strFZ = tmpData[rn][intFZ+1];
                        String strFM = tmpData[rn][intFM+1];
                        if (strFM.equals("0.00"))
                        { //如果分母为0
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
                                System.out.println("比率列右边设置错误！");
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
                    this.errMsg = "比率列右边设置错误！";
                }
            }
        }
        /*======结束======比率公式处理=====结束=====*/
        return strNum;
    }

    /************定义比率的基本信息*******/
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