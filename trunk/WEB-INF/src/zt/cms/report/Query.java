package zt.cms.report;

/**
 * <p>Title: 信贷管理</p>
 * <p>Description: 潍坊信贷管理系统</p>
 * <p>Copyright: Copyright (c) 2003 中天信息技术有限公司</p>
 * <p>Company: 中天信息技术有限公司</p>
 * @author
 * @version 1.0
 */

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
import java.sql.*;
import java.util.HashMap;

/**
 * *****************************************************
 * 构建查询输出程序
 * *****************************************************
 */
public class Query {
    DatabaseConnection dbc = null; //数据库连接
    Connection conn = null; //数据库连接

    private HashMap mapDefVal = new HashMap(); //缺省值
    private HashMap mapUserVal = new HashMap(); //用户值
    private HashMap mapTblVal = new HashMap(); //表值

    private String data[][] = null; //记录集
    public int rows = 0; //数据集记录数
    public int cols = 0; //数据集列数
    public String errMsg = null; //错误信息

    private int isCall = 0; //是否为存储过程调用 0-否 1-是
    private HttpServletRequest request = null; //request对象

    int rs_Type = ResultSet.TYPE_SCROLL_INSENSITIVE;
    int rs_Cur = ResultSet.CONCUR_READ_ONLY;
    int rs_Update = ResultSet.CONCUR_UPDATABLE;

    /**
     * ****************************************************
     * function:构造器,获得数据库连接
     * *****************************************************
     */
    public Query() {
        try {
            dbc = MyDB.getInstance().apGetConn();
            conn = dbc.getConnection();
        } catch (Exception e) {
            MyDB.getInstance().apReleaseConn(1);
            System.out.println("数据库连接失败！");
        }
    }

    /**
     * ****************************************************
     * function: 关闭数据库连接
     * ****************************************************
     */
    public void closeDB() throws Exception {
        MyDB.getInstance().apReleaseConn(1);
    }

    /**
     * ****************************************************
     * function:  初始化，获得用户的网点号，网点名称，获得服务器的当前
     * 时间，将获得的值放到缺省的map中
     * <p/>
     * ****************************************************
     */
    private void init() {
        String strScbrhId = null; //网点编号
        String strScbrhName = null; //网点名称

        HttpSession session = request.getSession(true);
        UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.
                USER_INFO_NAME);
        try {
            String strUserName = um.getUserName();
            strScbrhId = SCUser.getBrhId(strUserName);
            strScbrhName = SCBranch.getSName(strScbrhId);
        } catch (Exception e) {
            errMsg = "获得网点号和网点名称失败！";
            e.printStackTrace();
        }
        String strEndDate = SystemDate.getYesterday("-");

        String strBeginDate = strEndDate.substring(0, 8) + "01";
        String strDispMode = "0";

        if (strScbrhId == null || strScbrhName == null || strBeginDate == null ||
                strEndDate == null) {
            errMsg = "初始化查询参数失败！";
        } else {
            //System.out.println("==================init() brhid=" + strScbrhId +"scbrhid=" + strScbrhId);
            mapDefVal.put("brhid", strScbrhId);
            mapDefVal.put("scbrhid", strScbrhId);
            mapDefVal.put("scbrhname", strScbrhName);
            mapDefVal.put("begindate", strBeginDate);
            mapDefVal.put("enddate", strEndDate);
            mapDefVal.put("dispmode", strDispMode);
            mapDefVal.put("dispname", strScbrhName);
            //以下变量为设置查询缺省值用 ,如:逾期天数，贷款余额范围
            mapDefVal.put("intdata_0", "0");
            mapDefVal.put("intdata_a", "1");
            mapDefVal.put("intdata_b", "10");
            mapDefVal.put("intdata_c", "100");
            mapDefVal.put("strdata_a", "");
            mapDefVal.put("strdata_b", "");
            mapDefVal.put("strdata_c", "");
            mapDefVal.put("mgrdata", "%"); //缺省设置客户经理
            //缺省值的设置  使用select选择
            mapDefVal.put("status", "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,20,25");
            mapDefVal.put("stt", "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,20,25");
            mapDefVal.put("stuu", "100,110,120,130,140,210,220,230,240");
        }
    }

    /**
     * *************************************************
     * 根据特定的查询号，来获得此查询的在表中的定义数据
     * 将定义的数据放到map中
     * **************************************************
     */
    public void getTableDefine(String qryNo) {
        String strSql = "select * from scqrydef where qryno='" + qryNo + "'";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(strSql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int hasRec = 0;
            if (rs.next()) {
                hasRec = 1;
            }
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String strColumnName = rsmd.getColumnName(i).trim().toLowerCase();
                if (strColumnName == null) { //名字没有，继续
                    continue;
                }
                String strVal = ""; //缺省空值
                if (hasRec == 1) {
                    String strTmp = DBUtil.fromDB(rs.getString(i));
                    if (strTmp != null) {
                        strVal = strTmp.trim();
                    } else {
                        strVal = "";
                    }
                }
                mapTblVal.put(strColumnName, new String(strVal));
            }
        } catch (SQLException se) {
            errMsg = errMsg + "从数据库取表数据失败！";
            se.printStackTrace();
        }
    }

    /**
     * ********************************************************
     * 获得网点列表数据
     * *********************************************************
     */
    private String getBrhList(String upBrh) {
        int level = -1;
        String strSql = "";
        String brhList = "";
        String sbrhid = "";
        String sname = "";
        String cbrhid = getStrValue("scbrhid");
        String cbrhname = getStrValue("scbrhname");

        ScbranchLevel sbl = new ScbranchLevel(upBrh); //判断网点级别
        level = sbl.intBrhLevel;

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = null;
            if (level > 0 && level < 4) {
                strSql = "select brhid,sname from scbranch where brhid='" + upBrh + "'";
            } else {
                strSql = "select upbrh,sname from scbranch where brhid='" + upBrh + "'";
            }
            rs = stmt.executeQuery(strSql);
            if (rs.next()) {
                sbrhid = DBUtil.fromDB(rs.getString(1));
                sname = DBUtil.fromDB(rs.getString(2));
                this.setUserValues("dispname", sname);
            } else {
                return brhList;
            }

            //以下有疑问
            if (level > 0 && level < 4) {
                strSql = "select brhid,sname from scbranch where upbrh='" + upBrh +
                        "' order by brhid";
                rs = stmt.executeQuery(strSql);

                //status=汇票状态|select|1+正常;3 selected+垫款;6+收回
                while (rs.next()) {
                    sbrhid = DBUtil.fromDB(rs.getString(1));
                    sname = DBUtil.fromDB(rs.getString(2));
                    if (sbrhid.trim().equals(upBrh)) {
                        sbrhid = sbrhid + " selected";
                    }
                    brhList += sbrhid + "+" + sname + ";";
                }
            }
            brhList += cbrhid + "+------------------------------;";
            brhList += cbrhid + "+" + cbrhname;
        } catch (SQLException se) {
            errMsg = errMsg + "从数据库取表数据失败！";
            se.printStackTrace();
        }
        return brhList;
    }

    /**
     * ***********************************************************
     * 从表定义的map中根据关键字来获得值
     * ***********************************************************
     */
    public String getTblValue(String strName) {
        String strValue = null; //值
        String strKey = strName.trim().toLowerCase();
        strValue = (String) this.mapTblVal.get(strKey);
        if (strValue == null) {
            strValue = "";
        } else {
            strValue = strValue.trim();
        }
        return strValue;
    }

    /**
     * **********************************************************
     * name     :getStrValue(String strName)
     * parameter:strName(String)关键字
     * function :获得map中的String值
     * return   :strValue(String)   如果关键字不存在     返回null
     * 如果关键字值Null     返回空
     * note     :读取数据时，先从用户数据中取，
     * 如此可保证同样的变量用户的值优先，
     * 而后是定义，
     * 最后使用系统缺省
     * ***********************************************************
     */
    public String getStrValue(String strName) {
        String strValue = null; //值

        String strKey = strName.trim().toLowerCase();
        strValue = (String) this.mapUserVal.get(strKey);
        if (strValue == null) {
            strValue = (String) this.mapTblVal.get(strKey);
        }
        if (strValue == null) {
            strValue = (String) this.mapDefVal.get(strKey);
        }
        return strValue;
    }

    /**
     * **********************************************************
     * name     :getIntValue(String strName)
     * parameter:strName(String)关键字
     * function :获得map中的int值，如果关键字不存在        返回-1
     * 如果本关键字的数据类型   返回-1
     * return   :intValue int
     * note     :读取数据时，先从用户数据中取，
     * 如此可保证同样的变量用户的值优先，
     * 而后是定义，
     * 最后使用系统缺省
     * ***********************************************************
     */
    public int getIntValue(String strName) {
        String strKey = null; //map中的关键字
        String strValue = null; //值
        int intValue = -1; //值
        strKey = strName.trim().toLowerCase();

        strValue = (String) this.mapUserVal.get(strKey); //用户表查
        if (strValue == null) {
            strValue = (String) this.mapTblVal.get(strKey); //定义表查
        }
        if (strValue == null) {
            strValue = (String) this.mapDefVal.get(strKey); //缺省值表查
        }

        if (strValue != null) { //如果找到值
            try {
                intValue = Integer.parseInt(strValue);
            } catch (Exception e) {
                intValue = -1;
            }
        }
        return intValue;
    }

    /**
     * **********************************************************
     * 实现用户设置数据 ,
     * 将此变量追加到用户数据中。
     * 读取数据时，先从用户数据中取，
     * 如此可保证同样的变量用户的值优先，而后是定义，最后使用系统缺省
     *
     * @param KeyName  String
     * @param KeyValue String
     *                 ***********************************************************
     */
    public void setUserValues(String KeyName, String KeyValue) {
        //注意:应该允许插入null,表示此变量不存在
        mapUserVal.put(KeyName.toLowerCase(), KeyValue);
    }

    /**
     * **********************************************************
     * 实现用户设置数据 ,
     * 将此变量追加到用户数据中。
     * 读取数据时，先从用户数据中取，
     * 如此可保证同样的变量用户的值优先，而后是定义，最后使用系统缺省
     *
     * @param KeyName  String
     * @param KeyValue int
     *                 ***********************************************************
     */

    public void setUserValues(String KeyName, int KeyValue) {

        mapUserVal.put(KeyName.toLowerCase(), String.valueOf(KeyValue));
    }

    public String[][] getData() {
        return data;
    }

    /**
     * ***************************************************************
     * 主程序
     *
     * @param qryNo
     * @return
     * @throws java.lang.Exception ****************************************************************
     */
    public String buildData(String qryNo) throws Exception { //获得数据

        String strSql = null;
        String brhId = null;

        this.init(); //初始化缺省值
        if (errMsg == null) {
            int nvBar = getIntValue("nvbar");
            if (nvBar == -1) { //第一次调用
                brhId = getStrValue("upbrh");
                if (brhId != null) {
                    this.setUserValues("brhid", brhId);
                }
            }

            this.setCurrentBrhId(); //设置当前网点
            int dispMode = getIntValue("dispMode"); //第一次调用在init()中定义
            int brhLevel = getIntValue("brhLevel");

            if (brhLevel == 4) {
                if (getStrValue("brhid").equals(getStrValue("scbrhid"))
                        && (nvBar == -1)) {
                    //当用户为实际网点并且第一次使用时，应为统计
                    //所以不做任何操作
                } else {
                    if (nvBar != -1) {
                        dispMode = 1;
                        setUserValues("dispMode", dispMode);
                    }
                }

            }

            String yingyebrhid = getStrValue("brhid");
            String subbrhall = SCBranch.getAllSubBrh1(yingyebrhid);
            //System.out.println("====================all subbrh=" + subbrhall);
            if (subbrhall != null && subbrhall.trim().length() <= 0) {
                subbrhall = null;
            }
            if (yingyebrhid != null && brhLevel == 3 && subbrhall == null) {
                if (getStrValue("brhid").equals(getStrValue("scbrhid"))
                        && (nvBar == -1)) {
                    //当用户为实际网点并且第一次使用时，应为统计
                    //所以不做任何操作
                } else {
                    if (nvBar != -1) {
                        //System.out.println("====================set dispmode to 1===============");
                        this.setUserValues("brhLevel", 4);
                        dispMode = 1;
                        setUserValues("dispMode", dispMode);
                        this.setUserValues("sqlbrh", "BrhId");
                        this.setUserValues("sqlupbrh", "BrhId");

                    }
                }

            }

            String listNo = getStrValue("listNo");
            //System.out.println("================================listno is =====" + listNo + "dispMode=" + dispMode);
            if (dispMode > 0) {
                if (listNo != null && listNo.trim().length() == 0) {
                    errMsg = "选择了清单程序，但没指定清单程序编号";
                } else {
                    qryNo = listNo;
                    this.setUserValues("qryNo", qryNo);
                    this.setUserValues("listNo", null);

                }
            }
        }
        if (errMsg == null) {
            //System.out.println("================================step 1==========================");
            this.getTableDefine(qryNo); //从表取定义信息
            //根据表的定义设置dispMode    0--统计     1--清单
            this.setUserValues("dispMode", getIntValue("qryIsList"));
        }
        if (errMsg == null) { //获得sql语句
            strSql = getSQL();
            this.setUserValues("userSql", strSql);
            //System.out.println("====usersql=" + strSql);
        }
        if (errMsg == null) {
            try {
                if (isCall == 0) {
                    data = getRecords(strSql); //获得报表结果集
                } else {
                    data = getCallRecords(strSql); //存储过程调用
                }
            } catch (Exception e) {
                errMsg = "你的SQL:" + strSql + "\n导致数据库返回如下错误:" +
                        DBUtil.fromDB(e.getMessage());
            }
        }
        if (errMsg != null) {
            return errMsg;
        }
        this.buildTitle(); //处理表头

        return "ok";
    }

    /**
     * ***********************************************************
     * 导航时 直接取数据
     *
     * @param qryNo
     * @return
     * @throws java.lang.Exception ************************************************************
     */
    public String getNvData(String qryNo, String strSql) throws Exception {
        this.init(); //初始化缺省值

        if (strSql == null) {
            errMsg = "sql语句 没定义";
        }
        if (errMsg == null) {
            this.getTableDefine(qryNo); //从表取定义信息
        }

        if (errMsg == null) {
            //获得导航报表结果集，因为统计无导航，所以也没有存储过程调用
            try {
                data = getRecords(strSql);
            } catch (Exception e) {
                errMsg = "你的SQL:" + strSql + "\n导致数据库返回如下错误:" +
                        DBUtil.fromDB(e.getMessage());
            }
        }
        if (errMsg == null) {
            this.buildTitle(); //处理表头
        }
        if (errMsg != null) {
            return errMsg;
        }
        return "ok";
    }

    /**
     * *****************************************************************
     * 从request中获得所有的参数，将参数逐个取出放入到mapUserVal 中
     *
     * @param HttpServletRequest request
     *                           retrun   void
     *                           ******************************************************************
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
        java.util.Enumeration e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String stemp = (String) e.nextElement();
            mapUserVal.put(stemp.toLowerCase(),
                    DBUtil.fromDB(request.getParameter(stemp)));
            //mapUserVal.put(stemp.toLowerCase(), request.getParameter(stemp));
        }
    }

    /**
     * ******************************************************************
     * 根据SQL语句从数据库中取得数据
     *
     * @param sql
     * @param Param
     * @return
     * @throws java.lang.Exception *******************************************************************
     */
    private String[][] getRecords(String sql) throws Exception {
        //System.out.println("SQL:"+sql);
        String rec[][] = null;
        Statement stmt = null;
        ResultSet rs = null;

        if (sql == null) {
            errMsg += "SQL语句没定义";
            return rec;
        }
        if (sql.trim().equals("")) {
            errMsg += "没有SQL语句";
            return rec;
            //jjj
        }
        stmt = conn.createStatement(rs_Type, rs_Cur);
        int nvBar = getIntValue("nvBar"); //是否是导航数据 ,1--导航
        int total = (getIntValue("total") < 0) ? 0 : getIntValue("total");
        int pageSize = (getIntValue("pageSize") < 1) ? 1 : getIntValue("pageSize");
        int pageNo = (getIntValue("pageNo") < 1) ? 1 : getIntValue("pageNo");

        int pages = 0;
        rows = pageSize;
        //System.out.println("nvBar:"+nvBar);
        if (nvBar != 1) { //第一次调用
            rs = stmt.executeQuery(sql);
            rs.last();
            total = rs.getRow();
            pageNo = 1;
            if (getIntValue("dispmode") == 0 || total < rows) {
                rows = total;
            }
            pages = (total % pageSize == 0) ? total / pageSize : total / pageSize + 1;
            rs.first();
        } else {
            pages = (total % pageSize == 0) ? total / pageSize : total / pageSize + 1;
            pageNo = (pageNo > pages) ? pages : pageNo;

            int beginIndex = pageSize * (pageNo - 1) + 1;
            if (beginIndex > total) {
                beginIndex = total - pageSize;
            }
            if ((total - beginIndex) < pageSize) {
                rows = total - beginIndex + 1;
            }

            stmt.setMaxRows(beginIndex + rows);
            rs = stmt.executeQuery(sql);
            rs.absolute(beginIndex);
        }

        ResultSetMetaData rsmd = rs.getMetaData();
        cols = rsmd.getColumnCount();
        rec = new String[rows][cols];

        String linkKey = rsmd.getColumnName(1); //得到参数变量名
        this.setUserValues("linkKey", linkKey);
        int linkCol = getIntValue("linkCol");
        String colNames = getStrValue("colnames");
        String caption = getStrValue("caption");

        //当表头没有 时，使用字段名作表头
        if (colNames.equals("")) {
            for (int i = linkCol + 1; i <= cols; i++) {
                colNames = colNames + rsmd.getColumnName(i) + ",";
            }
            if (colNames.endsWith(",")) {
                colNames.substring(0, colNames.length() - 1);
            }
            this.setUserValues("colnames", colNames);
        }
        //当标题没有时，使用表名(DB２好像不好用)
        if (caption.equals("")) {
            caption = rsmd.getTableName(1);
            this.setUserValues("caption", caption);
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 1; j <= cols; j++) {
                String tmp = DBUtil.fromDB(rs.getString(j));
                if (tmp == null) {
                    tmp = "";
                } else {
                    tmp = tmp.trim();
                }
                rec[i][j - 1] = tmp;
            }
            if (!rs.next()) {
                break;
            }
        }
        stmt.close();

        this.setUserValues("pagesize", pageSize);
        this.setUserValues("total", total);
        this.setUserValues("pageNo", pageNo);
        return rec;
    }

    /**
     * *************************************************************
     * 根据SQL语句从数据库存储过程中取得数据
     * DB2 存储过程不支持动态光标，因此，本程序只应用于统计
     *
     * @param sql
     * @param Param
     * @return
     * @throws java.lang.Exception ***************************************************************
     */
    private String[][] getCallRecords(String strSql) throws Exception {
        String rec[][] = null;

        if (strSql == null) {
            errMsg += "SQL语句没定义";
            return rec;
        }
        if (strSql.trim().equals("")) {
            errMsg += "没有SQL语句";
            return rec;
        }
        //替换关键字
        //共有两个关键字，数据库保存的是非网点关键字

        String linkKey = getStrValue("linkKey");
        String linkKey1 = getStrValue("linkKey1");
        String linkKeyValue = getStrValue("linkKeyValue");
        String linkKey1Value = getStrValue("linkKey1Value");
        String linkKey1Name = getStrValue("linkKey1Name");
        if (linkKey1Name == null) {
            linkKey1Name = "";
        }
        String dispName = getStrValue("dispName");

        if (linkKey1 == null) { //第一次调用
            linkKey1 = getStrValue("sqlKey");
            linkKey1Value = "0";
            linkKey1Name = "全部";
            linkKey = "brhid";
            linkKeyValue = getStrValue("scbrhid");
        } else {

            String scbrhid = getStrValue("brhid");

            int nvBar = getIntValue("nvbar");
            if (nvBar != -1) { //按单个科目查看下级网点
                if (!linkKey.trim().equals("brhid")) { //以非网点上传数据

                    linkKey1 = linkKey;
                    linkKey1Value = linkKeyValue;
                    linkKey = "brhid";
                    linkKey1Name = dispName;
                    this.setDispName(linkKey1);
                    //linkKeyValue = scbrhid;
                    linkKeyValue = getStrValue("saveBrh");
                }
            } else {
                linkKeyValue = scbrhid;
                linkKey1 = getStrValue("sqlKey");
                linkKey1Value = "0";
                linkKey1Name = "全部";
                linkKey = "brhid";
            }
        }
        linkKey = "brhid";

        strSql = strSql.replaceAll(":" + linkKey.toLowerCase(), linkKeyValue);
        strSql = strSql.replaceAll(":" + linkKey1.toLowerCase(), linkKey1Value);

        this.setUserValues("linkKey", "brhid");
        this.setUserValues("linkKeyValue", linkKeyValue);
        this.setUserValues("linkKey1", linkKey1); //保存非网点关键字
        this.setUserValues("linkKey1Value", linkKey1Value);
        this.setUserValues("linkKey1Name", linkKey1Name);

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();
            strSql = this.pQuery(strSql);
            rs = stmt.executeQuery(strSql);
            ResultSetMetaData rsmd = rs.getMetaData();
            cols = rsmd.getColumnCount();

            //得到参数变量名
            linkKey = rsmd.getColumnName(1);
            this.setUserValues("linkKey", linkKey);

            rows = 0;
            String s = "";
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    s += DBUtil.fromDB(rs.getString(i)) + ",";
                }
                s += "!";
            }

            if (s.length() > 0) {
                String[] s1 = s.split("!");
                rows = s1.length;

                rec = new String[rows][cols];
                for (int i = 0; i < rows; i++) {
                    String[] s2 = s1[i].split(",");
                    for (int j = 0; j < cols; j++) {
                        rec[i][j] = s2[j];
                    }
                }
            } else {
                rec = new String[0][cols];
            }

            stmt.close();

            this.setUserValues("total", rows);
            this.setUserValues("pageNo", 1);
            if (getIntValue("pageSize") < rows) {
                this.setUserValues("pageSize", rows);
            }
        } catch (Exception ex) {
            errMsg = "db2 Err:" + DBUtil.fromDB(ex.getMessage());
        }
        this.setUserValues("userSql", strSql);
        return rec;

    }

    /**
     * ********************************************************
     * <p/>
     * ********************************************************
     */
    private void setCurrentBrhId() {
        String sqlupBrh = "";
        String sqlbrh = "";
        int brhLevel = 0;

        String brhId = getStrValue("brhid"); //得到当前网点

        String linkKeyValue = getStrValue("linkKeyValue");
        //System.out.println("========================================linekeyvalue=" +linkKeyValue);
        if (linkKeyValue == null) { //第一次调用
            this.setUserValues("linkKeyValue", brhId);
        } else { //非第一次调用,要判断网点级别,更新网点号
            String linkKey = getStrValue("linkKey");

            /************************************************
             * 说明：特殊处理  记录实网点号以备清单使用
             * 方法 replaceSql中关于linkKey1Value的特殊处理
             ************************************************/
            ScbranchLevel sbl1 = new ScbranchLevel(linkKeyValue); //判断网点级别
            int level = sbl1.intBrhLevel;

            if (linkKey != null && linkKeyValue != null &&
                    linkKey.equalsIgnoreCase("brhid") && level >= 4) {
                this.setUserValues("linkKey2", "brhid"); //记录实网点 标志
                this.setUserValues("linkKey2Value", linkKeyValue); //记录实网点 值
            }
            //System.out.println("linkKey:"+linkKey);
            //System.out.println("linkKeyValue:"+linkKeyValue);
            //System.out.println("linkKey1Value:"+getStrValue("linkkey1value"));

            if (linkKey != null &&
                    (linkKey.equalsIgnoreCase("itmclass") ||
                    linkKey.equalsIgnoreCase("accno"))) {
                //调用存储过程，由getcallrecords 处理
            } else {
                brhId = getStrValue("upbrh");
                if (brhId == null) {
                    brhId = linkKeyValue;
                }
                this.setUserValues("linkKey", "brhid");
            }
        }
        /*************是否是本级及所属的下级网点***************/
        brhId = brhId.trim();
        String strLoginBrhid = this.getStrValue("scbrhid");
        String strAllSubAndSelf = SCBranch.getAllSubBrhAndSelf1(strLoginBrhid);
        if (strAllSubAndSelf.indexOf(brhId) == -1) {
            brhId = strLoginBrhid;
        }

        /*****************网点分级 分析网点*****************/
        ScbranchLevel sbl = new ScbranchLevel(brhId); //判断网点级别
        brhLevel = sbl.intBrhLevel;

        if (brhLevel == 1) { // 市联社
            sqlupBrh = "BnkId";
            sqlbrh = "BnkId";
        } else if (brhLevel == 2) { //联社
            sqlupBrh = "BnkId";
            sqlbrh = "ChgId";
        } else if (brhLevel == 3) { //信贷部门
            sqlupBrh = "ChgId";
            sqlbrh = "BrhId";
        } else { //分社(实网点)
            sqlupBrh = "BrhId";
            sqlbrh = "BrhId";
            brhLevel = 4;
        }

        //System.out.println("=================SetCurrentBranch=brhid=" + brhId + "scbrhid=" + strLoginBrhid + "BrhLevel=" + brhLevel);

        this.setUserValues("brhid", brhId);
        this.setUserValues("sqlbrh", sqlbrh);
        this.setUserValues("sqlupbrh", sqlupBrh);
        this.setUserValues("brhLevel", brhLevel);
    }

    /**
     * **********************************************************
     * function : 获得完整的sql查询语句
     * param    : 无(从类变量中取得)
     * return   : String  strSql  sql查询的完整语句
     * ***********************************************************
     */
    private String getSQL() {
        String strSql = null;

        strSql = getStrValue("userSql"); //获得SQL。察看变量表中是否有sql语句
        if (strSql != null) {
            return strSql;
        }
        //如有，直接返回（可能是用户自设的或者是保存在session 需要翻页的）
        String sqlSelect = getStrValue("sqlSelect");

        //如无，从变量表中获得sqlSelect
        if (sqlSelect == null || sqlSelect.trim().length() == 0) {
            errMsg += "程序出错了！";
            return strSql;
        }

        if (sqlSelect.startsWith("{")) { //存储过程
            isCall = 1; //sql语句是存储过程
            strSql = sqlSelect; //不做语句更新,程序做了特殊处理
        } else {
            isCall = 0; //sql语句是普通的sql语句不是存储过程

            /****************************************************
             说明：  sqlSelect
             以select或with开头 sql语句直接赋值
             否则调有buildSql方法 组合sql语句
             ****************************************************/
            if (sqlSelect.startsWith("select") || sqlSelect.startsWith("with")) {
                strSql = sqlSelect;
            } else {
                strSql = buildSql();
            }
            strSql = this.replaceSql(strSql);
        }

        return strSql;
    }

    /**
     * *****************************************************************
     * 功能:组合sql
     * 说明 :本模块目的是把指定关键字的SQL语句替换为没指定关键字的。
     * 如果指定了关键字(sqlKey=brhid, 注意 和网点关联的表名必须加别名 a):
     * SELECT :<brhid>,sum(b.nowbal)
     * FROM: rqloanledger a
     * WHERE: a.loancat2=2
     * WHERE1: <UPBRH>=’:brhId’
     * GROUP : <brhid>
     * ORDER:  <brhid>
     * 如果没有指定关键字：
     * SELECT : k.<brhid>,sum(b.nowbal)
     * FROM:  qloanledger a  , scbranch k
     * WHERE: a.brhid=k.brhid and a.loancat2=2
     * WHERE1: k.<UPBRH>=’:brhId’
     * GROUP : k.<brhid>
     * ORDER: k.<brhid>
     * ******************************************************************
     */
    private String buildSql() {
        String sql = null;

        String sSelect = getStrValue("sqlselect").trim();
        String sWhere1 = getStrValue("sqlwhere1").trim();
        String sWhere = getStrValue("sqlwhere").trim();
        String sFrom = getStrValue("sqlfrom").trim();
        String sGroup = getStrValue("sqlgroup").trim();
        String sOrder = getStrValue("sqlorder").trim();
        String sqlKey = getStrValue("sqlKey"); //使用sqlKey 否则从统计转为清单时出错

        if (sqlKey.equalsIgnoreCase("brhid")) {
            //当要求必须所有单位都先列出时，屏蔽下面两行.调用getbinddata
            sSelect = sSelect.replaceAll("<BRHID>", "<brhid>");
            sSelect = sSelect.replaceFirst("<brhid>",
                    "<brhid>,(SELECT sName FROM SCBranch SNM WHERE SNM.brhId=<brhid>)");
            sFrom = sFrom + ",SCBranch S ";
            if (sWhere.equals("")) {
                sWhere = "a.BrhId = S.BrhId  ";
            } else {
                sWhere += " AND a.BrhId=S.BrhId";
            }
            //在将替换的名字中添加别名
            String brh = "S." + getStrValue("sqlbrh");
            String upBrh = "S." + getStrValue("sqlupBrh");
            setUserValues("sqlbrh", brh);
            setUserValues("sqlupBrh", upBrh);
        }

        int brhLevel = getIntValue("brhLevel");
        sql = " SELECT " + sSelect + " FROM " + sFrom;
        String str = "";
        if ((brhLevel != 1) && (sWhere1.length() > 0)) { // 非市联社
            str = " WHERE " + sWhere1;
        }
        if (sWhere.length() > 0) {
            if (str.trim().startsWith("WHERE")) {
                str = str + " AND " + sWhere;
            } else {
                str = str + " WHERE " + sWhere;
            }
        }
        sql = sql + str;
        if (sGroup.length() > 0) {
            sql = sql + " GROUP BY " + sGroup;
        }
        if (sOrder.length() > 0) {
            sql = sql + " ORDER BY " + sOrder;
        }
        return sql;
    }

    /**
     * *******************************************************************
     * 将SQL语句中的关键字和数据值替换
     *
     * @param strSql
     * @return strSql
     *         *********************************************************************
     */
    private String replaceSql(String strSql) { //更新sql值

        if (strSql == null) {
            errMsg += "SQL不存在 ";
            return errMsg;
        }

        strSql = strSql.replaceAll("<BRHID>", "<brhid>");
        strSql = strSql.replaceAll("<UPBRH>", "<upbrh>");
        strSql = strSql.replaceAll(":BRHID", ":brhid");

        String sqlbrh = getStrValue("sqlbrh");
        String sqlupBrh = getStrValue("sqlupBrh");
        String sqlbrhid = getStrValue("brhid");
        strSql = strSql.replaceAll("<brhid>", sqlbrh); //网点字段
        strSql = strSql.replaceAll("<upbrh>", sqlupBrh); //上级网点字段

        strSql = strSql.replaceAll(":brhid", sqlbrhid);

        //关键字为非网点，更新
        String linkKey = getStrValue("linkKey");
        if (linkKey != null && (!linkKey.trim().toLowerCase().equals("brhid"))) {
            strSql = strSql.replaceAll(":" + linkKey.toLowerCase(),
                    getStrValue("linkKeyValue"));
        }

        String linkKey1 = getStrValue("linkKey1");
        if (linkKey1 != null && (!linkKey1.trim().toLowerCase().equals("brhid"))) {
            strSql = strSql.replaceAll(":" + linkKey1.toLowerCase(),
                    getStrValue("linkKey1Value"));
        }

        //更新SQL中的变量 在SQL中变量用冒号前缀，如 :变量名
        //要求变量名全部小写，其变量列表在qryParams 中
        String qryParams = getStrValue("qryParams");
        if (qryParams == null) {
            qryParams = "";
        }
        if (qryParams.trim().length() > 0) {
            String[] tmp = qryParams.split(",");
            for (int i = 0; i < tmp.length; i++) {
                String[] tmp1 = tmp[i].split("=");
                String sName = tmp1[0];
                String sVal = getStrValue(sName);
                //System.out.println(sName+":"+sVal);

                /***********************************************
                 更新SQL中的变量 在SQL中变量用冒号前缀，如 :变量名
                 要求变量名全部小写，其变量列表在qryParams 中
                 ***********************************************/
                if (sName.equals("strdata_a") || sName.equals("strdata_b") ||
                        sName.equals("strdata_c")) { //如果为汉字需要转码
                    sVal = DBUtil.toDB(sVal);

                }
                if (sName.equals("status") || sName.equals("stt") || sName.equals("stuu")) { //枚举类型字段
                    if (sVal.equals("-1")) {
                        sVal = "( " + (String) this.mapDefVal.get(sName) + " )";
                    } else {
                        sVal = "( " + sVal + " )";
                    }
                }
                if (sName.equals("mgrdata")) { //客户经理的用户号处理
                    sVal = "'" + sVal + "'";
                }
                if (sName.equalsIgnoreCase("linkkey2value")) { //特殊处理，清单中需要实网点号的
                    sVal = getStrValue("linkkey2value");

                    //System.out.println("------------------");
                    //System.out.println("linkkey2value:"+sVal);
                    //System.out.println("------------------");
                }

                if (sVal == null) {
                    sVal = "";
                }
                strSql = strSql.replaceAll(":" + sName, sVal);
            }
        }
        return strSql;
    }

    /**
     * ***************************************************************
     * 取得所有查询定义列表
     *
     * @return
     * @throws java.lang.Exception
     * *****************************************************************
     */
    public String getQryList() {
        String strList = "";
        String strSql = "select qryNo,qryName from scqrydef order by qryNo";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(strSql);
            while (rs.next()) {
                strList += rs.getString(1) + "=" + DBUtil.fromDB(rs.getString(2)) + ",";
            }
            if (strList.endsWith(",")) {
                strList = strList.substring(0, strList.length() - 1);
            }
        } catch (SQLException se) {
            errMsg = errMsg + "从数据库取所有定义失败！";
            se.printStackTrace();
        }
        return strList;
    }

    /**
     * ***************************************************************
     * function :生成查询的表头结构
     * caption    表头  ；    ltitle    左标题；
     * mtitle     中标题；    rtitle    右标题；
     * qryParams  参数
     * ***************************************************************
     */
    private void buildTitle() {
        String caption = getStrValue("caption");
        String lTitle = getStrValue("lTitle");
        String mTitle = getStrValue("mTitle");
        String rTitle = getStrValue("rTitle");
        int dataDispWan = getIntValue("dataDispWan");
        String qryParams = getStrValue("qryParams");
        String linkKey1Name = getStrValue("linkKey1Name");
        int dispMode = getIntValue("dispMode");

        if (!qryParams.trim().equals("")) { //处理缺省值
            qryParams = "," + qryParams + ",";

            if (dispMode == 1) {
                qryParams = qryParams.replaceAll(",dispmode,",
                        ",dispmode=统计方式|radio|0+统计;1 checked+清单,");
            } else {
                qryParams = qryParams.replaceAll(",dispmode,",
                        ",dispmode=统计方式|radio|0 checked+统计;1+清单,");
            }
            qryParams = qryParams.replaceAll(",begindate,",
                    ",begindate=开始日期|text|:begindate,");
            qryParams = qryParams.replaceAll(",enddate,",
                    ",enddate=结束日期|text|:enddate,");

            //status=汇票状态|select|1+正常;3 selected+垫款;6+收回
            if (qryParams.indexOf("status") > 0) {
                String sVal = getStrValue("status");

                int pos0 = qryParams.indexOf("status");
                String s0 = qryParams.substring(0, pos0); //status以前部分

                String s1 = qryParams.substring(pos0);
                int pos1 = s1.indexOf(",");
                String s2 = s1.substring(0, pos1); //status部分
                String s3 = s1.substring(pos1); //status以后部分

                //s2=s2.replaceAll(sVal,sVal+" selected");
                s2 = s2.replaceAll(";" + sVal + "+", ";" + sVal + " selected");
                qryParams = s0 + s2 + s3;
            }
            //stt=汇票状态|select|1+正常;3 selected+垫款;6+收回
            if (qryParams.indexOf("stt") > 0) {
                String sVal = getStrValue("stt");

                int pos0 = qryParams.indexOf("stt");
                String s0 = qryParams.substring(0, pos0); //stt以前部分

                String s1 = qryParams.substring(pos0);
                int pos1 = s1.indexOf(",");
                String s2 = s1.substring(0, pos1); //stt部分
                String s3 = s1.substring(pos1); //stt以后部分

                s2 = s2.replaceAll(sVal, sVal + " selected");
                qryParams = s0 + s2 + s3;
            }
            //status=汇票状态|select|1+正常;3 selected+垫款;6+收回
            if (qryParams.indexOf("stuu") > 0) {
                String sVal = getStrValue("stuu");

                int pos0 = qryParams.indexOf("stuu");
                String s0 = qryParams.substring(0, pos0); //status以前部分

                String s1 = qryParams.substring(pos0);
                int pos1 = s1.indexOf(",");
                String s2 = s1.substring(0, pos1); //status部分
                String s3 = s1.substring(pos1); //status以后部分

                //s2=s2.replaceAll(sVal,sVal+" selected");
                s2 = s2.replaceAll(";" + sVal + "+", ";" + sVal + " selected");
                qryParams = s0 + s2 + s3;
            }
            if (qryParams.indexOf("mgrdata") > 0) { //客户经理的的特殊处理只有清单可用
                String strCM = this.getClientManager(getStrValue("brhid"));
                String sVal = getStrValue("mgrdata");

                strCM = strCM.replaceAll(";" + sVal + "+", ";" + sVal + " selected");
                qryParams = qryParams.replaceAll(",mgrdata,", strCM);
            }

            if (qryParams.indexOf("upbrh") > 0) {
                String upBrh = getStrValue("upbrh");

                if (upBrh == null || getIntValue("nvBar") != -1) { //first use default
                    upBrh = getStrValue("brhid");
                }

                String brhList = this.getBrhList(upBrh);
                qryParams = qryParams.replaceAll(",upbrh,",
                        ",upbrh=请选择网点|select|" + brhList);
            }

            if (qryParams.startsWith(",")) {
                qryParams = qryParams.substring(1);
            }
            if (qryParams.endsWith(",")) {
                qryParams = qryParams.substring(0, qryParams.length() - 1);
            }

            String tmp[] = qryParams.split(",");
            for (int i = 0; i < tmp.length; i++) {

                String tmp1[] = tmp[i].split("=");
                String s = tmp1[0];

                String s1 = getStrValue(s);
                caption = caption.replaceAll(":" + s, s1);
                lTitle = lTitle.replaceAll(":" + s, s1);
                mTitle = mTitle.replaceAll(":" + s, s1);
                qryParams = qryParams.replaceAll(":" + s, s1);
            }
        }

        String dispName = getStrValue("dispName");
//更新系统默认变量
        if (linkKey1Name != null) {

            if (!linkKey1Name.equals("null") && !linkKey1Name.equals(dispName)) {
                dispName = dispName + "(" + linkKey1Name + ")";
            }
        }
        caption = caption.replaceAll(":dispname", dispName);
        lTitle = lTitle.replaceAll(":dispname", dispName);
        mTitle = mTitle.replaceAll(":dispname", dispName);

        if (lTitle.trim().equals("")) {
            lTitle = "单位名称:" + dispName;
        }

        if (rTitle.trim().length() == 0) {
            if (dataDispWan == 0) {
                rTitle = "单位：元";
            } else {
                rTitle = "单位：万元";
            }
        }

        this.setUserValues("caption", caption);
        this.setUserValues("lTitle", lTitle);
        this.setUserValues("mTitle", mTitle);
        this.setUserValues("rTitle", rTitle);
        this.setUserValues("qryParams", qryParams);
    }

    /**
     * *********************************************************
     *
     * @param :brhid String     网点名称(实网点)
     * @return :strCM    String     生成的客户经理的select字符串
     *         *********************************************************
     * @function :取得客户经理名称
     */
    public String getClientManager(String brhid) {
        String strCM = ",mgrdata=客户经理|select|%+所有"; //返回的客户经理的字段
        String strLName = ""; //客户经理登陆名
        String strUName = ""; //客户经理名称

        String strSql = "select loginname,username from scuser where brhid='" +
                brhid + "' and usertype='1'";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(strSql);
            while (rs.next()) {
                strLName = rs.getString(1);
                strUName = DBUtil.fromDB(rs.getString(2));
                if (strLName != null && strUName != null) {
                    strCM = strCM + ";" + strLName + "+" + strUName;
                }
            }
        } catch (SQLException se) {
            errMsg = errMsg + "从数据库取客户经理失败！";
            se.printStackTrace();
        }
        strCM = strCM + ",";

        return strCM;
    }

    /**
     * *********************************************************
     *
     * @param :brhid String 网点名称
     * @return :void
     * @function :设置要显示的网点名称
     * *********************************************************
     */
    public void setDispName(String brhid) {
        String dispName = ""; //网点名称

        String strSql = "select sname from scbranch where brhid='" + brhid + "'";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(strSql);
            if (rs.next()) {
                dispName = DBUtil.fromDB(rs.getString(1));
                this.setUserValues("dispName", dispName);
            }
        } catch (SQLException se) {
            errMsg = errMsg + "从数据库取网点名称失败！";
            se.printStackTrace();
        }
    }

    /**
     * ******************************************************************
     * 表结构：brhid, tblitmno,itmclass,itmcount,itmcount1,itmcount2,itmdata,itmdata1,itmdata2
     * 格  式：网点,项目号,分类,月初笔数，当月发放笔数，当前笔数,月初余额，当月发放额，当前余额
     * 处理的项目号:
     * "Q00001","按业务种类",BMType,
     * "Q00002","按占用形态",LoanCat2,
     * "Q00003","按担保方式",LoanType3,
     * "Q00004","按贷款期限",LoanType2,
     * "Q00005","按发放方式",LoanType5,
     * "Q00006","按贷款用途",LoanCat3,
     * "Q00007","按客户性质",ClientType,
     * "Q00008","按经济部门",EcomDeptType,
     * "Q00009","按行业分类",SectorCat1,
     * "Q00010","按企业规模",EtpScopType,
     * "Q00011","按经济类型",EcomType,
     * "Q00012","按五级分类",LoanCat1,
     * *******************************************************************
     */
    private String pQuery(String callSql) {
        int val = 0;
        String keyName = "";
        String sUpName;
        String sql;
        String sSelect;
        String sFields;
        String sFrom;
        String sWhere;
        String sOrder;
        int level = -1;

        if (callSql.indexOf("Q10010") > 0) { //按贷款科目查询网点余额
            return this.p10010(callSql);
        }

        val = callSql.indexOf("(");
        if (val <= 0) {
            return callSql;
        }

        callSql = callSql.substring(val + 1);
        val = callSql.indexOf(")");
        callSql = callSql.substring(0, val);
        callSql = callSql.replaceAll("'", "");
        String tmp[] = callSql.split(",");

        String keyItm = tmp[0];
        int keyValue = Integer.parseInt(tmp[1]);
        String brhId = tmp[2];

        if (brhId == null || brhId.trim().length() < 9) {
            brhId = (String) this.mapDefVal.get("brhid");
        }
        //System.out.println("brhId:"+brhId);
        val = Integer.parseInt(keyItm.substring(2));

        switch (val) {
            case 1:
                keyName = "BMType";
                break;
            case 2:
                keyName = "LoanCat2";
                break;
            case 3:
                keyName = "LoanType3";
                break;
            case 4:
                keyName = "LoanType2";
                break;
            case 5:
                keyName = "LoanType5";
                break;
            case 6:
                keyName = "LoanCat3";
                break;
            case 7:
                keyName = "ClientType";
                break;
            case 8:
                keyName = "EcomDeptType";
                break;
            case 9:
                keyName = "SectorCat1";
                break;
            case 10:
                keyName = "EtpScopType";
                break;
            case 11:
                keyName = "EcomType";
                break;
            case 12:
                keyName = "LoanCat1";
                break;

            default:
                keyName = "";
        }
        ScbranchLevel sbl = new ScbranchLevel(brhId); //判断网点级别
        level = sbl.intBrhLevel;

        if (keyValue == 0) {
            sSelect =
                    "select itmClass,(select enudt from ptenuminfodetl where enuid='"
                    + keyName + "' and enutp=rqtbldata.itmClass), ";
            if (level == 1) {
                sWhere = " where brhid in (select brhid from scbranch where upbrh='" +
                        brhId + "')";
            } else {
                sWhere = " where brhid='" + brhId + "'";
            }
            sWhere += " and tblItmNo='" + keyItm + "'";
            sOrder = "  group by itmclass  order by itmclass ";

        } else {
            if (level < 4 && level > 0) {
                sUpName = "upbrh";
            } else {
                sUpName = "brhid";
            }
            sSelect =
                    "select brhid,(select sname from scbranch where brhid=rqtbldata.brhid), ";
            sWhere = " where tblItmNo='" + keyItm + "' and  itmclass=" +
                    String.valueOf(keyValue)
                    + " and brhid in (select brhid from scbranch where " + sUpName + "='" +
                    brhId + "')";
            sOrder = "  group by brhid  order by brhid ";
        }

        sFields = " coalesce(sum(itmcount),0),coalesce(sum(itmcount1),0),coalesce(sum(itmdata),0),"
                + " coalesce(sum(itmdata1),0),coalesce(sum(itmdata2),0) ";
        sFrom = " from rqtbldata  ";
        sql = sSelect + sFields + sFrom + sWhere + sOrder;

        return sql;
    }

    /**
     * ****************************************************
     * <p/>
     * {call  Q10010(':accno',':brhid') }
     * <p/>
     * *****************************************************
     */
    private String p10010(String callSql) {
        int val = 0;
        int level = -1;
        String sql;

        val = callSql.indexOf("(");
        if (val <= 0) {
            return callSql;
        }

        callSql = callSql.substring(val + 1);
        val = callSql.indexOf(")");
        callSql = callSql.substring(0, val);
        callSql = callSql.replaceAll("'", "");
        String tmp[] = callSql.split(",");

        String accno = tmp[0];
        String upbrh = tmp[1];
        if (upbrh == null || upbrh.trim().length() < 9) {
            upbrh = (String) this.mapDefVal.get("brhid");
        }
        //System.out.println("brhid:"+upbrh);
        ScbranchLevel sbl = new ScbranchLevel(upbrh); //判断网点级别
        level = sbl.intBrhLevel;

        String sField = "";
        sField += "b.daydbbal + b.daycrbal,";
        sField += "b.mondbamt,";
        sField += "b.moncramt,";
        sField += "b.lastmondbbal + b.lastmoncrbal,";
        sField += "b.lastyeardbbal + b.lastyearcrbal,";
        sField += "b.yeardbamt,";
        sField += "b.yearcramt";

        int whichQuery = -1;
        if (accno != null && accno.trim().equals("0")) {
            whichQuery = 0;
        } else {
            if (upbrh != null && level > 0 && level < 4) {
                whichQuery = 1;
            } else {
                whichQuery = 0;
            }
        }

        switch (whichQuery) {
            case 0: //one branch  ,see self all accno
                sql = "select a.accno,a.accname,a.accno," + sField;
                sql += " from schostacc a,btaccbal b  ";
                sql += "where b.brhid='" + upbrh +
                        "' and a.accclass>0   and a.accno=b.accno  order by a.accno;";
                break;
            case 1: // one accno ,see next level  branch
                sql = " select b.brhid,a.sname,b.brhid," + sField;
                sql += " from scbranch a,btaccbal b  ";
                sql += " where a.upbrh='" + upbrh + "'  and b.accno='" + accno +
                        "' and a.brhid=b.brhid  order by a.brhid";
                break;
            default:
                sql = "";
        }
        return sql;
    }

    /*************************以下是查询配置工具用到的方法************************/
    /**
     * *******************************************************************
     * 保存项目定义
     *
     * @param cmd *******************************************************************
     */
    public String saveDefine(String cmd) {
        String strRtn = null;
        String qryNo = getStrValue("qryNo");
        String strSql = "select * from scqrydef where qryno='" + qryNo + "'";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(strSql);
            ResultSetMetaData rsmd = rs.getMetaData();

            int cmdOk = 0;
            if (rs.next()) {
                if (cmd.equals("cmdDel")) {
                    strSql = "delete from scqrydef where qryno='" + qryNo + "'";
                    if (stmt.executeUpdate(strSql) > 0) {
                        strRtn = "纪录删除成功";
                    } else {
                        strRtn = "纪录删除失败";
                    }
                    cmdOk = -1;
                } else {
                    if (cmd.equals("cmdAdd")) { //想增加 ,但纪录已经存在，为防止误该，不做操作。
                        strRtn = "纪录已经存在:" + qryNo;
                        cmdOk = -1;
                    }
                }
            } else {
                if (cmd.equals("cmdAdd")) {
                    //增加新纪录
                    cmdOk = 1;
                } else {
                    cmdOk = -1; // 想修改，但纪录不存在
                    strRtn = "纪录不存在";
                }
            }

            if (cmdOk >= 0) {
                String strName = "";
                String strVal = "";
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String strColName = rsmd.getColumnName(i).trim().toLowerCase();
                    strName += strColName + ",";
                    int intTmp = 0;
                    String strTmp = "";
                    //由于本表只有smallint 型CHAR型字段，本程序可如此写，但其他不可
                    if (rsmd.getColumnType(i) == Types.SMALLINT) {
                        intTmp = getIntValue(strColName);
                        if (intTmp == -1) {
                            strVal += null + ",";
                        } else {
                            strVal += intTmp + ",";
                        }
                    } else {
                        strTmp = DBUtil.toDB(getStrValue(strColName));
                        //strTmp = getStrValue(strColName);

                        strTmp = DBUtil.toSql(strTmp);
                        if (strTmp.equals("")) {
                            strTmp = null;
                            strVal += strTmp + ",";
                        } else {
                            strVal += "'" + strTmp + "',";
                        }
                    }

                }
                if (strName.endsWith(",")) {
                    strName = strName.substring(0, strName.length() - 1);
                }
                if (strVal.endsWith(",")) {
                    strVal = strVal.substring(0, strVal.length() - 1);
                }
                if (cmdOk == 0) {
                    strSql = "update scqrydef set (" + strName + ")=(" + strVal +
                            ") where qryno='" + qryNo + "'";
                    stmt.executeUpdate(strSql);
                    strRtn = "纪录修改成功";
                } else {
                    strSql = "insert into scqrydef values (" + strVal + ")";
                    stmt.executeUpdate(strSql);
                    strRtn = "纪录增加成功";
                }
                this.getTableDefine(qryNo);
            }
        } catch (SQLException se) {
            errMsg = errMsg + "保存数据库表数据失败！";
            se.printStackTrace();
        }
        return strRtn;
    }

    /**
     * ***********************************************************
     * 定义程序测试SQL语句用
     *
     * @return ************************************************************
     */
    public String getTestSql() {
        this.init();
        /******市联社******/
        String rtnSql = null;
        setUserValues("dispmode", 0);
        setUserValues("brhid", "907999999");
        setCurrentBrhId();
        rtnSql = getSQL();
        /******联社******/
        setUserValues("brhid", "907069999");
        setUserValues("linkKey", "bnkid");
        setCurrentBrhId();
        rtnSql = rtnSql + "!" + getSQL();
        /******信贷部门******/
        setUserValues("brhid", "907060199");
        setUserValues("linkKey", "chgid");
        setCurrentBrhId();
        rtnSql = rtnSql + "!" + getSQL();

        return rtnSql;
    }

}
