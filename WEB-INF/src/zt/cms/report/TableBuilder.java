package zt.cms.report;

/**
 * <p>Title: 信贷管理</p>
 * <p>Description: 潍坊信贷管理系统</p>
 * <p>Copyright: Copyright (c) 2003 中天信息技术有限公司</p>
 * <p>Company: 中天信息技术有限公司</p>
 * @author YUSG
 * @version 1.0
 */

import com.ming.webreport.DataRecord;
import com.ming.webreport.MRDataSet;
import com.ming.webreport.MREngine;
import com.zt.util.PropertyManager;
import zt.cms.pub.SCBranch;
import zt.cms.pub.SCUser;
import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.define.SystemDate;
import zt.platform.db.DBUtil;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.user.UserManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.*;
import zt.cms.fcsort.fcreport.FcRrportDao;

/**
 * ****************************************************************
 * function :
 * 报表数据生成的通用程序,根据报表的编号调用存储过程获得报表的数据
 * 加工处理获得的数据导入到明宇报表的MRDataSet，利用明宇报表产生报表
 * <p/>
 * ******************************************************************
 */
public class TableBuilder {
    public DatabaseConnection dbc = null; //数据库连接
    public Connection conn = null;        //数据库连接
    public Statement stmt = null;         //数据库连接状态

    public String strReptNo;          //报表编号
    public String errMsg = null;        //错误代码

    private String strLoginBrhid;      //登陆用户的网点编号
    private String strLoginName;       //登陆用户的所在网点名称
    private String strBrhId;           //报表网点编号
    private String strScbrhName;       //报表网点名称
    private String strClientName;      //报表客户名称
    private String strMonth;           //统计月份
    private String strEndDate;         //当前的业务日期
    private String strFCDate;          //当前的五级分类业务日期    lj added in 20050711
    private String strTenThousand = "万元"; //金额单位：万元，千元，百元
    private int intTenThousand = 10000;     //金额单位：10000,1000,100
    private int isNeedDivide = 1;           //五级分类数据中是否需要进行单位换算处理：0 否 1 是 2 部分  lj added in 20050713
    int intBrhRowCount = 0;                 //数据网点数
    private int intMaxRow = 10;             //如果网点数小于10，补齐10行
    private int flag = 1;                   //多结果集标志   lj added in 20050905

    public String tmpBranch[][] = null;     //获得下级网点数组
    public String dataBranch[][] = null;    //获得显示数据网点数组
    ResultSetMetaData rsmdBranch = null;    //网点列
    int rs_Type = ResultSet.TYPE_SCROLL_INSENSITIVE;
    int rs_Cur = ResultSet.CONCUR_READ_ONLY;
    StringBuffer strBuf = new StringBuffer();

    public TableBuilder(HttpServletRequest request) {
        this.strBrhId = getBrhId(request);
        if (request.getParameter("clientname") != null) {
            this.strClientName = request.getParameter("clientname").trim();
        } else
            this.strClientName = "";

        if (request.getParameter("rptno") != null)
            this.strReptNo = request.getParameter("rptno").trim();

        if (this.strReptNo == null)
            errMsg = "没有获得报表编号！";
        if (this.strLoginBrhid == null)
            errMsg = "获得登陆用户所在网点失败，请重新登陆！";
        if (this.strBrhId == null)
            errMsg = "获得报表网点失败，请重新登陆！";
        if (errMsg == null) {
            try {
                dbc = MyDB.getInstance().apGetConn();
                conn = dbc.getConnection();
            } catch (Exception e) {
                errMsg = "数据库连接失败！";
                MyDB.getInstance().apReleaseConn(1);//释放数据库连接
            }
            strEndDate = this.getDate();////获得当前的业务日期
        }
        if (request.getParameter("month") != null) {
            this.strMonth = request.getParameter("month").trim().substring(0, 4)+"-"+request.getParameter("month").trim().substring(4, 6);
        } else
            this.strMonth = "00";
        if (request.getParameter("date") != null) {
            this.strFCDate = request.getParameter("date").trim();
        } else
            this.strFCDate = SystemDate.getYesterday("-");
    }

    public String getDate()//获得当前的业务日期
    {
        String strDate = null;
        ResultSet rsDate = null;

        String strSql = "select rtrim(property_value) from setup_property where module_name='SYSDATE' and property_name='TODAY'";
        try {
            //System.out.println(strSql);
            Statement psmt1 = conn.createStatement(this.rs_Type, this.rs_Cur);
            rsDate = psmt1.executeQuery(strSql);
            if (rsDate.next()) {
                strDate = rsDate.getString(1);
            }
        } catch (SQLException se) {
            this.errMsg = "需要统计的网点查询失败！";
            MyDB.getInstance().apReleaseConn(1);
        }
        return strDate;
    }

    private String getBrhId(HttpServletRequest request)//获得登陆用户的网点
    {
        String strScbrhId = null;

        HttpSession session = (HttpSession) request.getSession(true);
        UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if (um == null) {
            this.errMsg = "操作超时，请重新登陆！";
            return strScbrhId;
        } else {
            try {
                String strUserName = um.getUserName();
                this.strLoginBrhid = SCUser.getBrhId(strUserName);
                this.strLoginName = SCBranch.getSName(this.strLoginBrhid);
            } catch (Exception e) {
                errMsg = "获得登陆用户所在网点失败，请重新登陆！";
            }
        }

        if (request.getParameter("brhid") != null) {
            String strAllSubAndSelf = SCBranch.getAllSubBrhAndSelf1(this.strLoginBrhid);
            strScbrhId = request.getParameter("brhid").trim();
            if (strAllSubAndSelf.indexOf(strScbrhId) == -1) {
                strScbrhId = this.strLoginBrhid;
                strScbrhName = this.strLoginName;
            } else {
                strScbrhName = SCBranch.getSName(strScbrhId);
            }
        } else {
            strScbrhId = this.strLoginBrhid;
            strScbrhName = this.strLoginName;
        }
        return strScbrhId;
    }

    private ResultSet getBrhName() // 获得下级网点号和网点名称
    {
        ResultSet rsBranch = null;
        String strSql = "";
        int intGrade = getBrhGrade(this.strBrhId);

        if (this.errMsg != null)
            return null;

        /**
         * specially deal with the situation of Yingyebu
         */
        String yingyebrhid = this.strBrhId;
        String subbrhall = SCBranch.getAllSubBrh1(yingyebrhid);
        if (subbrhall != null && subbrhall.trim().length() <= 0) {
            subbrhall = null;
        }
        if (yingyebrhid != null && intGrade == 3 && subbrhall == null) {
            intGrade = 4;
        }

        if (intGrade < 4) {
            strBuf = new StringBuffer();
            strBuf.append("select brhid,sname from scbranch where upbrh='");
            strBuf.append(this.strBrhId);
            strBuf.append("' order by brhid ");
            strSql = strBuf.toString();
            strBuf = null;
        } else//如果是实网点，下级网点设为自身
        {
            strBuf = new StringBuffer();
            strBuf.append("select brhid,sname from scbranch where brhid='");
            strBuf.append(this.strBrhId);
            strBuf.append("'");
            strSql = strBuf.toString();
            strBuf = null;
            //System.out.println("没有下级网点了！");
        }
        try {
            //System.out.println(strSql);
            Statement psmt1 = conn.createStatement(this.rs_Type, this.rs_Cur);
            rsBranch = psmt1.executeQuery(strSql);
        } catch (SQLException se) {
            this.errMsg = "需要统计的网点查询失败！";
            MyDB.getInstance().apReleaseConn(1);
        }
        return rsBranch;
    }

    private ResultSet getData()//调用存储过程获得统计结果
    {
        ResultSet rsData = null;
        String strSql = null;

        if (this.errMsg != null) {
            return null;
        }
        try {
            System.out.println("rptno:" + this.strReptNo + "   brhid:" + this.strBrhId);
            //String strSql = "{call report('"+this.strReptNo+"','" + this.strBrhId + "','"+this.strMonth+"')};";

            if (this.strReptNo.substring(0, 3).equals("R06")) {
                strSql = ReportSql.getRateSql(this.strReptNo, this.strBrhId, this.strMonth);
            } else if (this.strReptNo.substring(0, 3).equals("R07")) {//lj added in 20050710
                if (this.strReptNo.equals("R07190") || this.strReptNo.equals("R07200"))
                    strSql = ReportSql.getFCSql1(this.strReptNo, this.strBrhId, this.strFCDate, this.strClientName, this.flag);
                else
                	
                    strSql = ReportSql.getFCSql(this.strReptNo, this.strBrhId, this.strFCDate);
                
            } else
                strSql = ReportSql.getSql(this.strReptNo, this.strBrhId, this.strMonth);
           System.out.println(strSql);
            stmt = conn.createStatement(this.rs_Type, this.rs_Cur);
            rsData = stmt.executeQuery(strSql);
        } catch (SQLException se) {
            MyDB.getInstance().apReleaseConn(1); //释放数据库连接
            this.errMsg = "报表数据统计失败！";
        }
        return rsData;
    }

    private int getBrhGrade(String brhid)//判断用户的级别
    {
        int intGrade = 0;
        //System.out.println("brhid:"+brhid);
        ScbranchLevel sbl = new ScbranchLevel(brhid);//判断网点级别S
        intGrade = sbl.intBrhLevel;

        if (intGrade == 1)//市联社
        {
            this.strTenThousand = "万元";
            this.intTenThousand = 10000;
        } else if (intGrade == 2)//县联社
        {
            this.strTenThousand = "千元";
            this.intTenThousand = 1000;
        } else if (intGrade == 3)//信贷部门
        {
            this.strTenThousand = "百元";
            this.intTenThousand = 100;
        } else //分社
        {
            intGrade = 4;
            this.strTenThousand = "百元";
            this.intTenThousand = 100;
        }
        return intGrade;
    }

    /**
     * ***************************************************
     * 报表数据获得(除利息表部分 即以F6%开头的)
     * function :获得所有的下级网点
     * return   :String tmpBranch[][]  下级网点
     * ***************************************************
     */
    private String[][] getScBranch() {
        ResultSet rsBranch = null;            //网点数据集
        int intBrhColCount = 0;               //网点数字的列数
        int intCount = 0;                     //下级网点的数目
        int n = 0;

        rsBranch = this.getBrhName();
        if (this.errMsg != null)
            return null;
        try {
            rsmdBranch = rsBranch.getMetaData();
            intBrhColCount = rsmdBranch.getColumnCount();
            while (rsBranch.next())//获得下级网点数(包括了实际网点)
            {
                //if(getBrhGrade(rsBranch.getString(1).trim())!=4)//去掉实网点
                intCount++;
                intBrhRowCount++;
            }
            //rsBranch.beforeFirst();
            rsBranch = this.getBrhName();

            ////如果小于10个补齐10行设置数组,大于等于不处理
            if (this.intBrhRowCount < this.intMaxRow) {
                this.intBrhRowCount = this.intMaxRow;
            }
            dataBranch = new String[intBrhRowCount][intBrhColCount];
            tmpBranch = new String[intCount + 2][intBrhColCount];
            //获得下级网点只处理已汇总结尾的(目的),但是显示的数据需要包括下级
            //的实际网点，循环后m=intBrhRowCount++;
            while (rsBranch.next()) {
                String strId = rsBranch.getString(1).trim();
                String strName = DBUtil.fromDB(rsBranch.getString(2)).trim();
                tmpBranch[n][0] = strId;
                dataBranch[n][0] = strId;
                tmpBranch[n][1] = strName;
                dataBranch[n][1] = strName;
                n++;

                /*
                if(getBrhGrade(strId)!=4)
                {
                    strName=strName.substring(0,strName.length()-"汇总".length());
                    tmpBranch[n][0]=strId;
                    dataBranch[m][0]=strId;
                    tmpBranch[n][1]=strName;
                    dataBranch[m][1]=strName;
                    n++;
                    m++;
                }
                else
                {
                    dataBranch[m][0]=strId;
                    dataBranch[m][1]=strName;
                    m++;
                }
                */
            }
        } catch (SQLException se) {
            MyDB.getInstance().apReleaseConn(1); //释放数据库连接
            this.errMsg = "获得下级网点失败！";
        }
        while (n < this.intMaxRow)//如果小于10个补齐10行填充数组
        {
            dataBranch[n][0] = "-1";
            dataBranch[n][1] = "";
            n++;
        }
        this.AddUserBrhid(intCount);
        return this.tmpBranch;
    }

    /**
     * ************************************************************
     * 报表数据获得(除利息表部分 即以F6%开头的)
     * function :将报表数据整理到数组中,放到明宇报表中的MRDataSet中
     * return   :MRDataSet mrds
     * *************************************************************
     */
    public MRDataSet getReportData() {
        ResultSet rsData = null;              //网点数据数据集
        ResultSetMetaData rsmdData = null;    //

        String data[][] = null;               //整理后的数组
        String label[][] = null;              //网点号,网点名称数组
        String tmpData[][] = null;            //网点统计数据数组
        int intBrhColCount = 0;               //网点数字的列数
        int intDataColCount = 0;              //网点数据的列数
        MRDataSet mrds = new MRDataSet();
        int intCols = 0;

        rsData = this.getData();
        if (this.errMsg != null)//获得网点数据产生异常，返回了null
            return null;
        try {
            rsmdData = rsData.getMetaData();

            intBrhColCount = this.rsmdBranch.getColumnCount();
            intDataColCount = rsmdData.getColumnCount();
            intCols = intDataColCount + intBrhColCount - 1;
            label = new String[1][intCols];//获得列名

            for (int i = 0; i < intCols; i++)//获得列说明(例如F1，F2等)
            {
                if (i < intBrhColCount)
                    label[0][i] = rsmdBranch.getColumnLabel(i + 1);
                else
                    label[0][i] = rsmdData.getColumnLabel(i + 2 - intBrhColCount);
            }
            tmpData = new String[intBrhRowCount][intDataColCount];//
            int n = 0;

            while (rsData.next())//获得下级管理网点的数据
            {
                for (int i = 0; i < intDataColCount; i++) {
                    String strFN = rsData.getString(i + 1);
                    String tmpFN = "";
                    if (i != 0) {
                        int intFN = strFN.indexOf(".");
                        if (intFN != -1)
                            tmpFN = strFN.substring(0, intFN);
                        else
                            tmpFN = strFN;
                    } else {
                        tmpFN = strFN;
                    }
                    tmpData[n][i] = tmpFN;
                }
                n++;
            }

            if (this.errMsg != null)
                stmt.close();

        } catch (Exception e) {
            this.errMsg = "数据转化为数组失败！";
            System.out.println("数据转化为数组失败！");
        } finally {
            MyDB.getInstance().apReleaseConn(1);//释放数据库连接
        }

        data = new String[intBrhRowCount][intCols];
        if (this.errMsg != null) {
            for (int i = 0; i < intBrhRowCount; i++) { //如果不存在数据则显示0
                String strData = null;
                for (int m = 0; m < intCols; m++) {
                    if (m < intBrhColCount) {
                        strData = dataBranch[i][m];
                    } else {
                        strData = "0";
                    }
                    data[i][m] = strData;
                }
            }
        } else {
            for (int i = 0; i < intBrhRowCount; i++) { //整理数据
                String strBrhid = dataBranch[i][0].trim();
                int haveRec = -1;

                //判断网点和网点数据数组的网点号，如果相同记录本网点号在网点数据数组的位置
                //即haveRec,如果在网点数据数组中不存在此网点号则haveRec=-1
                for (int n = 0; n < intBrhRowCount; n++) {
                    String strTmpBrhid = tmpData[n][0];
                    if (strTmpBrhid != null) {
                        strTmpBrhid = strTmpBrhid.trim();
                        //System.out.println("brhid:"+strBrhid+"  dataID:"+strTmpBrhid+"   "+strBrhid.equals(strTmpBrhid));
                        if (strBrhid.equals(strTmpBrhid)) {
                            haveRec = n;
                            break;
                        }
                    } else
                        break;
                }

                //根据haveRec的将网点数据数组的值添加到data数组中
                for (int m = 0; m < intCols; m++) {
                    String strData = null;
                    if (m < intBrhColCount)
                        strData = dataBranch[i][m];
                    else {
                        if (haveRec != -1) {
                            strData = tmpData[haveRec][m - intBrhColCount + 1];//需要转化为万元
                        } else
                            strData = "0";
                        if (strData == null || strData.equals("")) {
                            strData = "0";
                        } else {
                            long longData = Long.parseLong(strData.trim());
                            strData = String.valueOf(longData / this.intTenThousand);
                        }
                    }
                    data[i][m] = strData;
                }
            }
        }
        for (int i = 0; i < intBrhRowCount; i++)//将整理到的数据送到明宇报表的MRDataSet中
        {
            DataRecord rec = new DataRecord();
            for (int m = 0; m < intCols; m++) {
                //System.out.println(label[0][m]+":"+data[i][m]);
                if (m > 1) {
                    rec.setValue(label[0][m], new Integer(data[i][m]));
                } else {
                    rec.setValue(label[0][m], data[i][m]);
                }
            }
            mrds.addRow(rec);
        }
        return mrds;
    }

    /**
     * ***********************************************************
     * 报表数据获得(利息表部分 即以F6%开头的)
     * function :获得下级网点
     * return   :String tmpBranch[][]  下级网点
     * ************************************************************
     */
    private String[][] getScbranchRate() {
        ResultSet rsBranch = null;            //下级网点的数据集
        int intBrhColCount = 0;               //网点数字的列数
        int intCount = 0;                     //下级网点的数目
        int intGrade = 0;
        int n = 0;

        intGrade = this.getBrhGrade(this.strBrhId);
        if (intGrade > 2 || intGrade == 0) {
            this.errMsg = "不包含县联社以下的数据!";

            MyDB.getInstance().apReleaseConn(1); //释放数据库连接
            return null;
        }
        rsBranch = this.getBrhName();
        if (this.errMsg != null)
            return null;
        try {
            rsmdBranch = rsBranch.getMetaData();
            intBrhColCount = rsmdBranch.getColumnCount();
            while (rsBranch.next())//获得下级网点数(包括了实际网点)
            {
                //if(getBrhGrade(rsBranch.getString(1).trim())!=4)//去掉实网点
                intCount++;
                intBrhRowCount++;
            }
            //rsBranch.beforeFirst();
            rsBranch = this.getBrhName();
            this.tmpBranch = new String[intCount + 2][intBrhColCount];
            while (rsBranch.next()) {
                String strId = rsBranch.getString(1).trim();
                String strName = DBUtil.fromDB(rsBranch.getString(2)).trim();
                tmpBranch[n][0] = strId;
                tmpBranch[n][1] = strName;
                n++;
            }
        } catch (SQLException se) {
            MyDB.getInstance().apReleaseConn(1); //释放数据库连接
            this.errMsg = "获得下级网点失败！";
        }
        this.AddUserBrhid(intCount);
        return this.tmpBranch;

    }

    /**
     * ************************************************************
     * 报表数据获得(五级分类)
     * function :将报表数据整理到数组中,放到明宇报表中的MRDataSet中
     *
     * @return :MRDataSet mrds
     *         author lj
     *         Created in 20050713
     *         *************************************************************
     */
    public MRDataSet getReportFCData() {
        ResultSet rsData = null;              //网点数据数据集
        ResultSetMetaData rsmdData = null;    //

        String data[][] = null;               //整理后的数组
        String label[][] = null;              //网点号,网点名称数组
        String tmpData[][] = null;            //网点统计数据数组
        int intBrhColCount = 0;               //网点数字的列数
        int intDataColCount = 0;              //网点数据的列数
        MRDataSet mrds = new MRDataSet();
        int intCols = 0;
        int intRettNo = Integer.parseInt(this.strReptNo.substring(2, 6));// lj added in 20050715

        rsData = this.getData();
        if (this.errMsg != null)//获得网点数据产生异常，返回了null
            return null;
        try {
            rsmdData = rsData.getMetaData();

            intBrhColCount = this.rsmdBranch.getColumnCount();
            intDataColCount = rsmdData.getColumnCount();
            intCols = intDataColCount + intBrhColCount - 1;
            label = new String[1][intCols];//获得列名

            for (int i = 0; i < intCols; i++)//获得列说明(例如F1，F2等)
            {
                if (i < intBrhColCount)
                    label[0][i] = rsmdBranch.getColumnLabel(i + 1);
                else
                    label[0][i] = rsmdData.getColumnLabel(i + 2 - intBrhColCount);
            }
            tmpData = new String[intBrhRowCount][intDataColCount];//


            int n = 0;

            while (rsData.next())//获得下级管理网点的数据
            {
                for (int i = 0; i < intDataColCount; i++) {
                    String strFN = rsData.getString(i + 1);
                    strFN = (strFN == null) ? "" : strFN; // lj added in 20050712
                    String tmpFN = "";
                    if (i != 0) {
                        int intFN = 0;
                        switch (intRettNo) {
                            case 7050:
                            case 7140:
                                if (i < 21) intFN = strFN.indexOf(".");
                                break;
                            default:
                                intFN = strFN.indexOf(".");
                        }
                        if (intFN != -1 && intFN != 0)
                            tmpFN = strFN.substring(0, intFN);
                        else
                            tmpFN = strFN;
                    } else {
                        tmpFN = strFN;
                    }
                    tmpData[n][i] = tmpFN;
                }
                n++;
            }

            if (this.errMsg != null)
                stmt.close();

        } catch (Exception e) {
            this.errMsg = "数据转化为数组失败！";
            System.out.println("数据转化为数组失败！");
        } finally {
            MyDB.getInstance().apReleaseConn(1);//释放数据库连接
        }

        data = new String[intBrhRowCount][intCols];
        if (this.errMsg != null) {
            for (int i = 0; i < intBrhRowCount; i++) { //如果不存在数据则显示0
                String strData = null;
                for (int m = 0; m < intCols; m++) {
                    if (m < intBrhColCount) {
                        strData = dataBranch[i][m];
                    } else {
                        strData = "0";
                    }
                    data[i][m] = strData;
                }
            }
        } else {
            for (int i = 0; i < intBrhRowCount; i++) { //整理数据
                String strBrhid = dataBranch[i][0].trim();
                int haveRec = -1;

                //判断网点和网点数据数组的网点号，如果相同记录本网点号在网点数据数组的位置
                //即haveRec,如果在网点数据数组中不存在此网点号则haveRec=-1
                for (int n = 0; n < intBrhRowCount; n++) {
                    String strTmpBrhid = tmpData[n][0];
                    if (strTmpBrhid != null) {
                        strTmpBrhid = strTmpBrhid.trim();
                        if (strBrhid.equals(strTmpBrhid)) {
                            haveRec = n;
                            break;
                        }
                    } else
                        break;
                }

                //根据haveRec的将网点数据数组的值添加到data数组中

                for (int m = 0; m < intCols; m++) {
                    String strData = null;
                    if (m < intBrhColCount)
                        strData = dataBranch[i][m];
                    else {
                        if (haveRec != -1) {
                            strData = tmpData[haveRec][m - intBrhColCount + 1];//需要转化为万元
                        } else
                            strData = "0";
                        if (strData == null || strData.equals("")) {
                            strData = "0";
                        } else {
                            long longData = Long.parseLong("0");
                            long longTenThousand = Long.parseLong(String.valueOf(this.intTenThousand));
                            switch (isNeedDivide) {
                                case 1:
                                    longData = Long.parseLong(strData.trim());
                                    strData = String.valueOf(BigDecimal.valueOf(longData).divide(BigDecimal.valueOf(longTenThousand), BigDecimal.ROUND_HALF_UP));
                                    break;
                                case 2:
                                    switch (intRettNo) {
                                        case 7020:
                                        case 7040:
                                        case 7070:
                                        case 7120:
                                        case 7130:
                                        case 7160:
                                     
                                            //System.out.println("m  = " + m +"||strData="+strData);
                                            if (m % 2 == 0) {
                                                longData = Long.parseLong(strData.trim());
                                                strData = String.valueOf(BigDecimal.valueOf(longData).divide(BigDecimal.valueOf(longTenThousand), BigDecimal.ROUND_HALF_UP));
                                            } else
                                                strData = strData.trim();
                                            break;
                                        case 7050:
                                        case 7140:
                                            if (m < 21) {
                                                longData = Long.parseLong(strData.trim());
                                                strData = String.valueOf(BigDecimal.valueOf(longData).divide(BigDecimal.valueOf(longTenThousand), BigDecimal.ROUND_HALF_UP));
                                            } else
                                                strData = strData.trim();
                                            break;
                                        default:
                                            strData = strData.trim();
                                    }
                                default:
                                    strData = strData.trim();
                            }
                        }
                    }
                    data[i][m] = strData;
                }
            }
        }
        for (int i = 0; i < intBrhRowCount; i++)//将整理到的数据送到明宇报表的MRDataSet中
        {
            DataRecord rec = new DataRecord();
            int limit = intCols;
            switch (intRettNo) {
                case 7050:
                case 7140:
                    limit = intCols - 5;
                default:
            }
            for (int m = 0; m < intCols; m++) {
                if (m > 1 && m < limit) {
                    rec.setValue(label[0][m], new Integer(data[i][m]));
                } else {
                    rec.setValue(label[0][m], data[i][m]);
                }
            }
            mrds.addRow(rec);
        }
        return mrds;
    }

    /**
     * ************************************************************
     * 报表数据获得(五级分类)——无网点列表形式
     * function :将报表数据整理到数组中,放到明宇报表中的MRDataSet中
     *
     * @return :MRDataSet mrds
     *         author lj
     *         Created in 20050722
     *         *************************************************************
     */
    public MRDataSet getReportFCDataNoBrh() {
        MRDataSet mrds = new MRDataSet();     //明宇网点数据集
        ResultSet rsData = null;              //网点数据数据集
        ResultSetMetaData rsmdData = null;    //
        int intColCount = 0;                  //列数
        rsData = this.getData();
        if (this.errMsg != null)//获得网点数据产生异常，返回了null
            return null;
        try {
            rsmdData = rsData.getMetaData();
            intColCount = rsmdData.getColumnCount();
            int intRettNo = Integer.parseInt(this.strReptNo.substring(2, 6));
            long longData = Long.parseLong("0");
            long longTenThousand = Long.parseLong(String.valueOf(this.intTenThousand));
            String strData = null;
            if (this.flag == 2 && intRettNo == 7200) {
                int n = 0;
                while (rsData.next()) {
                    n++;
                }
                if (n < 6) n = 6;
                rsData.beforeFirst();
                String Sdata[][] = new String[n][intColCount];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < intColCount; j++) {
                        Sdata[i][j] = "";
                    }
                }
                int j = 0;
                while (rsData.next()) {
                    for (int i = 1; i <= intColCount; i++) {
                        String strFN = rsData.getString(i);
                        strFN = (strFN == null) ? "" : strFN; // lj added in 20050712
                        Sdata[j++][i - 1] = DBUtil.fromDB(strFN.trim());
                        //System.out.println("Sdata[" + j + "][" + (i - 1) + "] = " + Sdata[j++][i - 1]);
                    }
                }

                for (int i = 0; i < n; i++) {//将整理到的数据送到明宇报表的MRDataSet中
                    DataRecord rec = new DataRecord();
                    for (int m = 0; m < intColCount; m++) {
                        rec.setValue(rsmdData.getColumnLabel(m + 1), Sdata[i][m]);
                        //System.out.println(" Sdata["+i+"]["+m+"] = " +  Sdata[i][m]);
                    }
                    mrds.addRow(rec);
                }
            } else
                while (rsData.next()) {
                    DataRecord rec = new DataRecord();

                    for (int i = 1; i <= intColCount; i++) {
                        String strFN = rsData.getString(i);
                        strFN = (strFN == null) ? "" : strFN; // lj added in 20050712
                        String tmpFN = "";
                        int intFN = intFN = strFN.indexOf(".");
                        if (intFN != -1 && intFN != 0)
                            tmpFN = strFN.substring(0, intFN);
                        else
                            tmpFN = strFN;
                        strData = tmpFN;
                        if (strData == null || strData.equals("")) {
                            strData = "0";
                        }
                        switch (intRettNo) {//五级分类结构分析表三
                            case 7080:
                            case 7090:
                            case 7170:
                            case 7180:
                            case 7230:
                            case 7210:
                          
                                strData = strData.trim();
                                if (i == 1) {
                                    if (strData.equals("0")) strData = "";
                                    rec.setValue(rsmdData.getColumnLabel(i), DBUtil.fromDB(strData));
                                } else {
                                    if (i % 2 == 0) {
                                        longData = Long.parseLong(strData.trim());
                                        strData = String.valueOf(BigDecimal.valueOf(longData).divide(BigDecimal.valueOf(longTenThousand), BigDecimal.ROUND_HALF_UP));
                                    }
                                    rec.setValue(rsmdData.getColumnLabel(i), new Integer(strData));
                                }
                                break;
                            case 7100:
                                strData = DBUtil.fromDB(strData.trim());
                                if (i == 14 && strData.equals("0")) strData = "";
                                rec.setValue(rsmdData.getColumnLabel(i), strData);
                                break;
                            case 7190:
                            case 7200:
                                //System.out.println(intRettNo+"(" + i + ") = " + rsmdData.getColumnLabel(i) + "||strData=" + strData);
                                strData = DBUtil.fromDB(strData.trim());
                                if (strData.equals("0")) strData = "";
                                rec.setValue(rsmdData.getColumnLabel(i), strData);
                                break;
                            default:
                        }
                    }
                    mrds.addRow(rec);
                }
        } catch (Exception e) {
            this.errMsg = "数据转化为明宇数据集失败！";
            System.out.println("数据转化为明宇数据集失败！");
        } finally {
            MyDB.getInstance().apReleaseConn(1);//释放数据库连接
        }

        return mrds;
    }

    /**
     * ***********************************************************
     * 报表数据获得(利息表部分 即以R06%开头的)
     * function :将报表数据整理到数组中,放到明宇报表中的MRDataSet中
     * return   :MRDataSet mrds
     * ************************************************************
     */
    public MRDataSet getReportDataRate() {
        MRDataSet mrds = new MRDataSet();     //明宇网点数据集
        ResultSet rsData = null;              //网点数据数据集
        ResultSetMetaData rsmdData = null;    //
        int intColCount = 0;                  //列数

        //System.out.println("rate");
        rsData = this.getData();
        if (this.errMsg != null)//获得网点数据产生异常，返回了null
            return null;
        try {
            rsmdData = rsData.getMetaData();
            intColCount = rsmdData.getColumnCount();

            while (rsData.next()) {
                DataRecord rec = new DataRecord();

                for (int i = 1; i <= intColCount; i++) {
                    if (this.strReptNo.substring(0, 5).equals("R0603"))//固定利率区间分布表
                    {
                        if (i == 1)
                            rec.setValue("sname", DBUtil.fromDB(rsData.getString(1)));
                        else {
                            String strM = String.valueOf(i - 1);
                            rec.setValue("F" + strM, new Integer(rsData.getInt(i) / 10000));
                        }
                    } else if (this.strReptNo.substring(0, 5).equals("R0602"))//贴现利率水平表
                    {
                        if (i % 2 == 0) {
                            rec.setValue("F" + i, new Double(rsData.getDouble(i)));
                        } else {
                            rec.setValue("F" + i, new Integer(rsData.getInt(i) / 10000));
                        }
                    } else//固定利率水平表
                    {
                        if (i == 1)
                            rec.setValue("sname", DBUtil.fromDB(rsData.getString(1)));
                        else {
                            int intM = i - 1;
                            String strM = String.valueOf(intM);
                            if (i % 2 == 1) {
                                rec.setValue("F" + strM, new Double(rsData.getDouble(i)));
                            } else {
                                rec.setValue("F" + strM, new Integer(rsData.getInt(i) / 10000));
                            }
                        }
                    }
                }
                mrds.addRow(rec);
            }
        } catch (Exception e) {
            this.errMsg = "数据转化为明宇数据集失败！";
            System.out.println("数据转化为明宇数据集失败！");
        } finally {
            MyDB.getInstance().apReleaseConn(1);//释放数据库连接
        }

        return mrds;
    }


    /**
     * ************************************************************
     * 报表公用部分
     * 确认明宇报表必须使用绝对路径
     * *************************************************************
     */
    public String showReport(String id) {
        String reportStr = null;
        MRDataSet mrds = null;
        MRDataSet mrds3 = null;
        MRDataSet mrds4 = null;
        int intRettNo = Integer.parseInt(this.strReptNo.substring(2, 6)); // lj added in 20050712
        String strRoot = PropertyManager.getProperty("root");//获得报表的绝对路径
        if (this.strReptNo.substring(0, 3).equals("R06")) {
            mrds = this.getReportDataRate();
            strRoot = strRoot + "rate\\";
        } else if (this.strReptNo.substring(0, 3).equals("R07")) {// lj added in 20050712
        	
            switch (intRettNo) {
                case 7020:
                case 7040:
                case 7050:
                case 7070:
                case 7120:
                case 7130:
                case 7140:
                case 7160:
                    isNeedDivide = 2;
                    mrds = this.getReportFCData();
                    break;
                case 7080:
                case 7090:
                case 7100:
                case 7170:
                case 7180:
                case 7190:
                case 7210:
                case 7230:
                    mrds = this.getReportFCDataNoBrh();
                    break;
                case 7200:
                    mrds = this.getReportFCDataNoBrh();
                    this.flag = 2;
                    try {
                        dbc = MyDB.getInstance().apGetConn();
                        conn = dbc.getConnection();
                    } catch (Exception e) {
                        errMsg = "数据库连接失败！";
                        MyDB.getInstance().apReleaseConn(1);//释放数据库连接
                    }
                    mrds3 = this.getReportFCDataNoBrh();
                    this.flag = 3;
                    try {
                        dbc = MyDB.getInstance().apGetConn();
                        conn = dbc.getConnection();
                    } catch (Exception e) {
                        errMsg = "数据库连接失败！";
                        MyDB.getInstance().apReleaseConn(1);//释放数据库连接
                    }
                    mrds4 = this.getReportFCDataNoBrh();
                    this.flag = 1;
                    break;
                default:
                    isNeedDivide = 1;
                    mrds = this.getReportFCData();
            }
            strRoot = strRoot + "FC\\";
        }else if(this.strReptNo.substring(0, 3).equals("F08"))
        {
        	FcRrportDao dao = new FcRrportDao();
        	mrds = dao.getF08010Data(this.strBrhId,this.strFCDate);
        	strRoot = strRoot + "FC\\";
        
        }
        else
            mrds = this.getReportData();
        if (mrds == null)
            return reportStr;

        
        try {
            MREngine engine = new MREngine();
            engine.addMRDataSet("rs2", mrds);//绑定获得的数据
            if (intRettNo == 7200) {
                engine.addMRDataSet("rs3", mrds3);//绑定获得的数据
                engine.addRelation("rs2", "rs3", "CNO=CNO");
                engine.addMRDataSet("rs4", mrds4);//绑定获得的数据
                engine.addRelation("rs2", "rs4", "CNO=CNO");
            }
            String rptno = this.strReptNo;
            if (rptno.substring(0, 3).equals("R07")){
                switch (intRettNo) {
                    case 7110:
                        rptno = "R07010";
                        break;
                    case 7120:
                        rptno = "R07020";
                        break;
                    case 7130:
                        rptno = "R07040";
                        break;
                    case 7140:
                        rptno = "R07050";
                        break;
                    case 7150:
                        rptno = "R07060";
                        break;
                    case 7160:
                        rptno = "R07070";
                        break;
                    case 7170:
                        rptno = "R07080";
                        break;
                    case 7180:
                        rptno = "R07090";
                        break;
                    case 7190:
                        rptno = "R07110";
                        break;
                    case 7200:
                        rptno = "R07120";
                        break;
                }
            }
            if(rptno.substring(0, 3).equals("F08"))
            {
            	rptno="F08010";
            }
            //System.out.println("绝对路径绑定报表+==============================="+this.strReptNo+strRoot + rptno + ".mrf");
            engine.bind(this.strReptNo, strRoot + rptno + ".mrf");//用绝对路径绑定报表
           
            reportStr = engine.createViewer(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reportStr;
    }

    public String getTenThousand()//获得是否万元的标志
    {
        return this.strTenThousand;
    }

    public String getScbranchName()//获得当前网点的名称
    {
        return this.strScbrhName;
    }

    public String getYear() {
        String strYear = null;

        String tmpYear = this.strEndDate.substring(0, 4);
        if (this.strMonth.equals("12")) {
            int intYear = Integer.parseInt(tmpYear) - 1;
            tmpYear = String.valueOf(intYear);
        }
        strYear = tmpYear;
        return strYear;
    }

    public String getMonth() {
        String strMnt = null;
        int intMonth = 0;

        String tmpMnt = this.strEndDate.substring(5, 7);
        int intMnt = Integer.parseInt(tmpMnt);
        if (this.strMonth.length() > 2)
            intMonth = Integer.parseInt(this.strMonth.substring(5));
        else
            intMonth = Integer.parseInt(this.strMonth);

        if (intMonth > intMnt && intMonth != 12)
            tmpMnt = "-1";
        else {
            if (!this.strMonth.equals("00"))
                tmpMnt = String.valueOf(intMonth);
            else
                tmpMnt = String.valueOf(intMnt - 1);
        }

        strMnt = tmpMnt;
        return strMnt;
    }

    public String[][] getArray()//获得下级的网点
    {
        if (this.strReptNo.substring(0, 3).equals("R06"))
            this.getScbranchRate();
        else
            this.getScBranch();

        return this.tmpBranch;
    }

    public String getErrMsg()  //获得错误信息
    {
        return this.errMsg;
    }

    //保证下级网点的下拉窗口始终带有登陆用户的网点
    public void AddUserBrhid(int count) {
        this.tmpBranch[count][0] = this.strLoginBrhid;
        this.tmpBranch[count][1] = "----------------";
        this.tmpBranch[count + 1][0] = this.strLoginBrhid;
        this.tmpBranch[count + 1][1] = this.strLoginName;
    }

}