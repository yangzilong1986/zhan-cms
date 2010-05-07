package zt.cms.report;

/**
 * <p>Title: �Ŵ�����</p>
 * <p>Description: Ϋ���Ŵ�����ϵͳ</p>
 * <p>Copyright: Copyright (c) 2003 ������Ϣ�������޹�˾</p>
 * <p>Company: ������Ϣ�������޹�˾</p>
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
 * ������ѯ�������
 * *****************************************************
 */
public class Query {
    DatabaseConnection dbc = null; //���ݿ�����
    Connection conn = null; //���ݿ�����

    private HashMap mapDefVal = new HashMap(); //ȱʡֵ
    private HashMap mapUserVal = new HashMap(); //�û�ֵ
    private HashMap mapTblVal = new HashMap(); //��ֵ

    private String data[][] = null; //��¼��
    public int rows = 0; //���ݼ���¼��
    public int cols = 0; //���ݼ�����
    public String errMsg = null; //������Ϣ

    private int isCall = 0; //�Ƿ�Ϊ�洢���̵��� 0-�� 1-��
    private HttpServletRequest request = null; //request����

    int rs_Type = ResultSet.TYPE_SCROLL_INSENSITIVE;
    int rs_Cur = ResultSet.CONCUR_READ_ONLY;
    int rs_Update = ResultSet.CONCUR_UPDATABLE;

    /**
     * ****************************************************
     * function:������,������ݿ�����
     * *****************************************************
     */
    public Query() {
        try {
            dbc = MyDB.getInstance().apGetConn();
            conn = dbc.getConnection();
        } catch (Exception e) {
            MyDB.getInstance().apReleaseConn(1);
            System.out.println("���ݿ�����ʧ�ܣ�");
        }
    }

    /**
     * ****************************************************
     * function: �ر����ݿ�����
     * ****************************************************
     */
    public void closeDB() throws Exception {
        MyDB.getInstance().apReleaseConn(1);
    }

    /**
     * ****************************************************
     * function:  ��ʼ��������û�������ţ��������ƣ���÷������ĵ�ǰ
     * ʱ�䣬����õ�ֵ�ŵ�ȱʡ��map��
     * <p/>
     * ****************************************************
     */
    private void init() {
        String strScbrhId = null; //������
        String strScbrhName = null; //��������

        HttpSession session = request.getSession(true);
        UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.
                USER_INFO_NAME);
        try {
            String strUserName = um.getUserName();
            strScbrhId = SCUser.getBrhId(strUserName);
            strScbrhName = SCBranch.getSName(strScbrhId);
        } catch (Exception e) {
            errMsg = "�������ź���������ʧ�ܣ�";
            e.printStackTrace();
        }
        String strEndDate = SystemDate.getYesterday("-");

        String strBeginDate = strEndDate.substring(0, 8) + "01";
        String strDispMode = "0";

        if (strScbrhId == null || strScbrhName == null || strBeginDate == null ||
                strEndDate == null) {
            errMsg = "��ʼ����ѯ����ʧ�ܣ�";
        } else {
            //System.out.println("==================init() brhid=" + strScbrhId +"scbrhid=" + strScbrhId);
            mapDefVal.put("brhid", strScbrhId);
            mapDefVal.put("scbrhid", strScbrhId);
            mapDefVal.put("scbrhname", strScbrhName);
            mapDefVal.put("begindate", strBeginDate);
            mapDefVal.put("enddate", strEndDate);
            mapDefVal.put("dispmode", strDispMode);
            mapDefVal.put("dispname", strScbrhName);
            //���±���Ϊ���ò�ѯȱʡֵ�� ,��:����������������Χ
            mapDefVal.put("intdata_0", "0");
            mapDefVal.put("intdata_a", "1");
            mapDefVal.put("intdata_b", "10");
            mapDefVal.put("intdata_c", "100");
            mapDefVal.put("strdata_a", "");
            mapDefVal.put("strdata_b", "");
            mapDefVal.put("strdata_c", "");
            mapDefVal.put("mgrdata", "%"); //ȱʡ���ÿͻ�����
            //ȱʡֵ������  ʹ��selectѡ��
            mapDefVal.put("status", "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,20,25");
            mapDefVal.put("stt", "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,20,25");
            mapDefVal.put("stuu", "100,110,120,130,140,210,220,230,240");
        }
    }

    /**
     * *************************************************
     * �����ض��Ĳ�ѯ�ţ�����ô˲�ѯ���ڱ��еĶ�������
     * ����������ݷŵ�map��
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
                if (strColumnName == null) { //����û�У�����
                    continue;
                }
                String strVal = ""; //ȱʡ��ֵ
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
            errMsg = errMsg + "�����ݿ�ȡ������ʧ�ܣ�";
            se.printStackTrace();
        }
    }

    /**
     * ********************************************************
     * ��������б�����
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

        ScbranchLevel sbl = new ScbranchLevel(upBrh); //�ж����㼶��
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

            //����������
            if (level > 0 && level < 4) {
                strSql = "select brhid,sname from scbranch where upbrh='" + upBrh +
                        "' order by brhid";
                rs = stmt.executeQuery(strSql);

                //status=��Ʊ״̬|select|1+����;3 selected+���;6+�ջ�
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
            errMsg = errMsg + "�����ݿ�ȡ������ʧ�ܣ�";
            se.printStackTrace();
        }
        return brhList;
    }

    /**
     * ***********************************************************
     * �ӱ����map�и��ݹؼ��������ֵ
     * ***********************************************************
     */
    public String getTblValue(String strName) {
        String strValue = null; //ֵ
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
     * parameter:strName(String)�ؼ���
     * function :���map�е�Stringֵ
     * return   :strValue(String)   ����ؼ��ֲ�����     ����null
     * ����ؼ���ֵNull     ���ؿ�
     * note     :��ȡ����ʱ���ȴ��û�������ȡ��
     * ��˿ɱ�֤ͬ���ı����û���ֵ���ȣ�
     * �����Ƕ��壬
     * ���ʹ��ϵͳȱʡ
     * ***********************************************************
     */
    public String getStrValue(String strName) {
        String strValue = null; //ֵ

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
     * parameter:strName(String)�ؼ���
     * function :���map�е�intֵ������ؼ��ֲ�����        ����-1
     * ������ؼ��ֵ���������   ����-1
     * return   :intValue int
     * note     :��ȡ����ʱ���ȴ��û�������ȡ��
     * ��˿ɱ�֤ͬ���ı����û���ֵ���ȣ�
     * �����Ƕ��壬
     * ���ʹ��ϵͳȱʡ
     * ***********************************************************
     */
    public int getIntValue(String strName) {
        String strKey = null; //map�еĹؼ���
        String strValue = null; //ֵ
        int intValue = -1; //ֵ
        strKey = strName.trim().toLowerCase();

        strValue = (String) this.mapUserVal.get(strKey); //�û����
        if (strValue == null) {
            strValue = (String) this.mapTblVal.get(strKey); //������
        }
        if (strValue == null) {
            strValue = (String) this.mapDefVal.get(strKey); //ȱʡֵ���
        }

        if (strValue != null) { //����ҵ�ֵ
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
     * ʵ���û��������� ,
     * ���˱���׷�ӵ��û������С�
     * ��ȡ����ʱ���ȴ��û�������ȡ��
     * ��˿ɱ�֤ͬ���ı����û���ֵ���ȣ������Ƕ��壬���ʹ��ϵͳȱʡ
     *
     * @param KeyName  String
     * @param KeyValue String
     *                 ***********************************************************
     */
    public void setUserValues(String KeyName, String KeyValue) {
        //ע��:Ӧ���������null,��ʾ�˱���������
        mapUserVal.put(KeyName.toLowerCase(), KeyValue);
    }

    /**
     * **********************************************************
     * ʵ���û��������� ,
     * ���˱���׷�ӵ��û������С�
     * ��ȡ����ʱ���ȴ��û�������ȡ��
     * ��˿ɱ�֤ͬ���ı����û���ֵ���ȣ������Ƕ��壬���ʹ��ϵͳȱʡ
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
     * ������
     *
     * @param qryNo
     * @return
     * @throws java.lang.Exception ****************************************************************
     */
    public String buildData(String qryNo) throws Exception { //�������

        String strSql = null;
        String brhId = null;

        this.init(); //��ʼ��ȱʡֵ
        if (errMsg == null) {
            int nvBar = getIntValue("nvbar");
            if (nvBar == -1) { //��һ�ε���
                brhId = getStrValue("upbrh");
                if (brhId != null) {
                    this.setUserValues("brhid", brhId);
                }
            }

            this.setCurrentBrhId(); //���õ�ǰ����
            int dispMode = getIntValue("dispMode"); //��һ�ε�����init()�ж���
            int brhLevel = getIntValue("brhLevel");

            if (brhLevel == 4) {
                if (getStrValue("brhid").equals(getStrValue("scbrhid"))
                        && (nvBar == -1)) {
                    //���û�Ϊʵ�����㲢�ҵ�һ��ʹ��ʱ��ӦΪͳ��
                    //���Բ����κβ���
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
                    //���û�Ϊʵ�����㲢�ҵ�һ��ʹ��ʱ��ӦΪͳ��
                    //���Բ����κβ���
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
                    errMsg = "ѡ�����嵥���򣬵�ûָ���嵥������";
                } else {
                    qryNo = listNo;
                    this.setUserValues("qryNo", qryNo);
                    this.setUserValues("listNo", null);

                }
            }
        }
        if (errMsg == null) {
            //System.out.println("================================step 1==========================");
            this.getTableDefine(qryNo); //�ӱ�ȡ������Ϣ
            //���ݱ�Ķ�������dispMode    0--ͳ��     1--�嵥
            this.setUserValues("dispMode", getIntValue("qryIsList"));
        }
        if (errMsg == null) { //���sql���
            strSql = getSQL();
            this.setUserValues("userSql", strSql);
            //System.out.println("====usersql=" + strSql);
        }
        if (errMsg == null) {
            try {
                if (isCall == 0) {
                    data = getRecords(strSql); //��ñ�������
                } else {
                    data = getCallRecords(strSql); //�洢���̵���
                }
            } catch (Exception e) {
                errMsg = "���SQL:" + strSql + "\n�������ݿⷵ�����´���:" +
                        DBUtil.fromDB(e.getMessage());
            }
        }
        if (errMsg != null) {
            return errMsg;
        }
        this.buildTitle(); //�����ͷ

        return "ok";
    }

    /**
     * ***********************************************************
     * ����ʱ ֱ��ȡ����
     *
     * @param qryNo
     * @return
     * @throws java.lang.Exception ************************************************************
     */
    public String getNvData(String qryNo, String strSql) throws Exception {
        this.init(); //��ʼ��ȱʡֵ

        if (strSql == null) {
            errMsg = "sql��� û����";
        }
        if (errMsg == null) {
            this.getTableDefine(qryNo); //�ӱ�ȡ������Ϣ
        }

        if (errMsg == null) {
            //��õ���������������Ϊͳ���޵���������Ҳû�д洢���̵���
            try {
                data = getRecords(strSql);
            } catch (Exception e) {
                errMsg = "���SQL:" + strSql + "\n�������ݿⷵ�����´���:" +
                        DBUtil.fromDB(e.getMessage());
            }
        }
        if (errMsg == null) {
            this.buildTitle(); //�����ͷ
        }
        if (errMsg != null) {
            return errMsg;
        }
        return "ok";
    }

    /**
     * *****************************************************************
     * ��request�л�����еĲ��������������ȡ�����뵽mapUserVal ��
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
     * ����SQL�������ݿ���ȡ������
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
            errMsg += "SQL���û����";
            return rec;
        }
        if (sql.trim().equals("")) {
            errMsg += "û��SQL���";
            return rec;
            //jjj
        }
        stmt = conn.createStatement(rs_Type, rs_Cur);
        int nvBar = getIntValue("nvBar"); //�Ƿ��ǵ������� ,1--����
        int total = (getIntValue("total") < 0) ? 0 : getIntValue("total");
        int pageSize = (getIntValue("pageSize") < 1) ? 1 : getIntValue("pageSize");
        int pageNo = (getIntValue("pageNo") < 1) ? 1 : getIntValue("pageNo");

        int pages = 0;
        rows = pageSize;
        //System.out.println("nvBar:"+nvBar);
        if (nvBar != 1) { //��һ�ε���
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

        String linkKey = rsmd.getColumnName(1); //�õ�����������
        this.setUserValues("linkKey", linkKey);
        int linkCol = getIntValue("linkCol");
        String colNames = getStrValue("colnames");
        String caption = getStrValue("caption");

        //����ͷû�� ʱ��ʹ���ֶ�������ͷ
        if (colNames.equals("")) {
            for (int i = linkCol + 1; i <= cols; i++) {
                colNames = colNames + rsmd.getColumnName(i) + ",";
            }
            if (colNames.endsWith(",")) {
                colNames.substring(0, colNames.length() - 1);
            }
            this.setUserValues("colnames", colNames);
        }
        //������û��ʱ��ʹ�ñ���(DB�����񲻺���)
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
     * ����SQL�������ݿ�洢������ȡ������
     * DB2 �洢���̲�֧�ֶ�̬��꣬��ˣ�������ֻӦ����ͳ��
     *
     * @param sql
     * @param Param
     * @return
     * @throws java.lang.Exception ***************************************************************
     */
    private String[][] getCallRecords(String strSql) throws Exception {
        String rec[][] = null;

        if (strSql == null) {
            errMsg += "SQL���û����";
            return rec;
        }
        if (strSql.trim().equals("")) {
            errMsg += "û��SQL���";
            return rec;
        }
        //�滻�ؼ���
        //���������ؼ��֣����ݿⱣ����Ƿ�����ؼ���

        String linkKey = getStrValue("linkKey");
        String linkKey1 = getStrValue("linkKey1");
        String linkKeyValue = getStrValue("linkKeyValue");
        String linkKey1Value = getStrValue("linkKey1Value");
        String linkKey1Name = getStrValue("linkKey1Name");
        if (linkKey1Name == null) {
            linkKey1Name = "";
        }
        String dispName = getStrValue("dispName");

        if (linkKey1 == null) { //��һ�ε���
            linkKey1 = getStrValue("sqlKey");
            linkKey1Value = "0";
            linkKey1Name = "ȫ��";
            linkKey = "brhid";
            linkKeyValue = getStrValue("scbrhid");
        } else {

            String scbrhid = getStrValue("brhid");

            int nvBar = getIntValue("nvbar");
            if (nvBar != -1) { //��������Ŀ�鿴�¼�����
                if (!linkKey.trim().equals("brhid")) { //�Է������ϴ�����

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
                linkKey1Name = "ȫ��";
                linkKey = "brhid";
            }
        }
        linkKey = "brhid";

        strSql = strSql.replaceAll(":" + linkKey.toLowerCase(), linkKeyValue);
        strSql = strSql.replaceAll(":" + linkKey1.toLowerCase(), linkKey1Value);

        this.setUserValues("linkKey", "brhid");
        this.setUserValues("linkKeyValue", linkKeyValue);
        this.setUserValues("linkKey1", linkKey1); //���������ؼ���
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

            //�õ�����������
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

        String brhId = getStrValue("brhid"); //�õ���ǰ����

        String linkKeyValue = getStrValue("linkKeyValue");
        //System.out.println("========================================linekeyvalue=" +linkKeyValue);
        if (linkKeyValue == null) { //��һ�ε���
            this.setUserValues("linkKeyValue", brhId);
        } else { //�ǵ�һ�ε���,Ҫ�ж����㼶��,���������
            String linkKey = getStrValue("linkKey");

            /************************************************
             * ˵�������⴦��  ��¼ʵ������Ա��嵥ʹ��
             * ���� replaceSql�й���linkKey1Value�����⴦��
             ************************************************/
            ScbranchLevel sbl1 = new ScbranchLevel(linkKeyValue); //�ж����㼶��
            int level = sbl1.intBrhLevel;

            if (linkKey != null && linkKeyValue != null &&
                    linkKey.equalsIgnoreCase("brhid") && level >= 4) {
                this.setUserValues("linkKey2", "brhid"); //��¼ʵ���� ��־
                this.setUserValues("linkKey2Value", linkKeyValue); //��¼ʵ���� ֵ
            }
            //System.out.println("linkKey:"+linkKey);
            //System.out.println("linkKeyValue:"+linkKeyValue);
            //System.out.println("linkKey1Value:"+getStrValue("linkkey1value"));

            if (linkKey != null &&
                    (linkKey.equalsIgnoreCase("itmclass") ||
                    linkKey.equalsIgnoreCase("accno"))) {
                //���ô洢���̣���getcallrecords ����
            } else {
                brhId = getStrValue("upbrh");
                if (brhId == null) {
                    brhId = linkKeyValue;
                }
                this.setUserValues("linkKey", "brhid");
            }
        }
        /*************�Ƿ��Ǳ������������¼�����***************/
        brhId = brhId.trim();
        String strLoginBrhid = this.getStrValue("scbrhid");
        String strAllSubAndSelf = SCBranch.getAllSubBrhAndSelf1(strLoginBrhid);
        if (strAllSubAndSelf.indexOf(brhId) == -1) {
            brhId = strLoginBrhid;
        }

        /*****************����ּ� ��������*****************/
        ScbranchLevel sbl = new ScbranchLevel(brhId); //�ж����㼶��
        brhLevel = sbl.intBrhLevel;

        if (brhLevel == 1) { // ������
            sqlupBrh = "BnkId";
            sqlbrh = "BnkId";
        } else if (brhLevel == 2) { //����
            sqlupBrh = "BnkId";
            sqlbrh = "ChgId";
        } else if (brhLevel == 3) { //�Ŵ�����
            sqlupBrh = "ChgId";
            sqlbrh = "BrhId";
        } else { //����(ʵ����)
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
     * function : ���������sql��ѯ���
     * param    : ��(���������ȡ��)
     * return   : String  strSql  sql��ѯ���������
     * ***********************************************************
     */
    private String getSQL() {
        String strSql = null;

        strSql = getStrValue("userSql"); //���SQL���쿴���������Ƿ���sql���
        if (strSql != null) {
            return strSql;
        }
        //���У�ֱ�ӷ��أ��������û�����Ļ����Ǳ�����session ��Ҫ��ҳ�ģ�
        String sqlSelect = getStrValue("sqlSelect");

        //���ޣ��ӱ������л��sqlSelect
        if (sqlSelect == null || sqlSelect.trim().length() == 0) {
            errMsg += "��������ˣ�";
            return strSql;
        }

        if (sqlSelect.startsWith("{")) { //�洢����
            isCall = 1; //sql����Ǵ洢����
            strSql = sqlSelect; //����������,�����������⴦��
        } else {
            isCall = 0; //sql�������ͨ��sql��䲻�Ǵ洢����

            /****************************************************
             ˵����  sqlSelect
             ��select��with��ͷ sql���ֱ�Ӹ�ֵ
             �������buildSql���� ���sql���
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
     * ����:���sql
     * ˵�� :��ģ��Ŀ���ǰ�ָ���ؼ��ֵ�SQL����滻Ϊûָ���ؼ��ֵġ�
     * ���ָ���˹ؼ���(sqlKey=brhid, ע�� ����������ı�������ӱ��� a):
     * SELECT :<brhid>,sum(b.nowbal)
     * FROM: rqloanledger a
     * WHERE: a.loancat2=2
     * WHERE1: <UPBRH>=��:brhId��
     * GROUP : <brhid>
     * ORDER:  <brhid>
     * ���û��ָ���ؼ��֣�
     * SELECT : k.<brhid>,sum(b.nowbal)
     * FROM:  qloanledger a  , scbranch k
     * WHERE: a.brhid=k.brhid and a.loancat2=2
     * WHERE1: k.<UPBRH>=��:brhId��
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
        String sqlKey = getStrValue("sqlKey"); //ʹ��sqlKey �����ͳ��תΪ�嵥ʱ����

        if (sqlKey.equalsIgnoreCase("brhid")) {
            //��Ҫ��������е�λ�����г�ʱ��������������.����getbinddata
            sSelect = sSelect.replaceAll("<BRHID>", "<brhid>");
            sSelect = sSelect.replaceFirst("<brhid>",
                    "<brhid>,(SELECT sName FROM SCBranch SNM WHERE SNM.brhId=<brhid>)");
            sFrom = sFrom + ",SCBranch S ";
            if (sWhere.equals("")) {
                sWhere = "a.BrhId = S.BrhId  ";
            } else {
                sWhere += " AND a.BrhId=S.BrhId";
            }
            //�ڽ��滻����������ӱ���
            String brh = "S." + getStrValue("sqlbrh");
            String upBrh = "S." + getStrValue("sqlupBrh");
            setUserValues("sqlbrh", brh);
            setUserValues("sqlupBrh", upBrh);
        }

        int brhLevel = getIntValue("brhLevel");
        sql = " SELECT " + sSelect + " FROM " + sFrom;
        String str = "";
        if ((brhLevel != 1) && (sWhere1.length() > 0)) { // ��������
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
     * ��SQL����еĹؼ��ֺ�����ֵ�滻
     *
     * @param strSql
     * @return strSql
     *         *********************************************************************
     */
    private String replaceSql(String strSql) { //����sqlֵ

        if (strSql == null) {
            errMsg += "SQL������ ";
            return errMsg;
        }

        strSql = strSql.replaceAll("<BRHID>", "<brhid>");
        strSql = strSql.replaceAll("<UPBRH>", "<upbrh>");
        strSql = strSql.replaceAll(":BRHID", ":brhid");

        String sqlbrh = getStrValue("sqlbrh");
        String sqlupBrh = getStrValue("sqlupBrh");
        String sqlbrhid = getStrValue("brhid");
        strSql = strSql.replaceAll("<brhid>", sqlbrh); //�����ֶ�
        strSql = strSql.replaceAll("<upbrh>", sqlupBrh); //�ϼ������ֶ�

        strSql = strSql.replaceAll(":brhid", sqlbrhid);

        //�ؼ���Ϊ�����㣬����
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

        //����SQL�еı��� ��SQL�б�����ð��ǰ׺���� :������
        //Ҫ�������ȫ��Сд��������б���qryParams ��
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
                 ����SQL�еı��� ��SQL�б�����ð��ǰ׺���� :������
                 Ҫ�������ȫ��Сд��������б���qryParams ��
                 ***********************************************/
                if (sName.equals("strdata_a") || sName.equals("strdata_b") ||
                        sName.equals("strdata_c")) { //���Ϊ������Ҫת��
                    sVal = DBUtil.toDB(sVal);

                }
                if (sName.equals("status") || sName.equals("stt") || sName.equals("stuu")) { //ö�������ֶ�
                    if (sVal.equals("-1")) {
                        sVal = "( " + (String) this.mapDefVal.get(sName) + " )";
                    } else {
                        sVal = "( " + sVal + " )";
                    }
                }
                if (sName.equals("mgrdata")) { //�ͻ�������û��Ŵ���
                    sVal = "'" + sVal + "'";
                }
                if (sName.equalsIgnoreCase("linkkey2value")) { //���⴦���嵥����Ҫʵ����ŵ�
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
     * ȡ�����в�ѯ�����б�
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
            errMsg = errMsg + "�����ݿ�ȡ���ж���ʧ�ܣ�";
            se.printStackTrace();
        }
        return strList;
    }

    /**
     * ***************************************************************
     * function :���ɲ�ѯ�ı�ͷ�ṹ
     * caption    ��ͷ  ��    ltitle    ����⣻
     * mtitle     �б��⣻    rtitle    �ұ��⣻
     * qryParams  ����
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

        if (!qryParams.trim().equals("")) { //����ȱʡֵ
            qryParams = "," + qryParams + ",";

            if (dispMode == 1) {
                qryParams = qryParams.replaceAll(",dispmode,",
                        ",dispmode=ͳ�Ʒ�ʽ|radio|0+ͳ��;1 checked+�嵥,");
            } else {
                qryParams = qryParams.replaceAll(",dispmode,",
                        ",dispmode=ͳ�Ʒ�ʽ|radio|0 checked+ͳ��;1+�嵥,");
            }
            qryParams = qryParams.replaceAll(",begindate,",
                    ",begindate=��ʼ����|text|:begindate,");
            qryParams = qryParams.replaceAll(",enddate,",
                    ",enddate=��������|text|:enddate,");

            //status=��Ʊ״̬|select|1+����;3 selected+���;6+�ջ�
            if (qryParams.indexOf("status") > 0) {
                String sVal = getStrValue("status");

                int pos0 = qryParams.indexOf("status");
                String s0 = qryParams.substring(0, pos0); //status��ǰ����

                String s1 = qryParams.substring(pos0);
                int pos1 = s1.indexOf(",");
                String s2 = s1.substring(0, pos1); //status����
                String s3 = s1.substring(pos1); //status�Ժ󲿷�

                //s2=s2.replaceAll(sVal,sVal+" selected");
                s2 = s2.replaceAll(";" + sVal + "+", ";" + sVal + " selected");
                qryParams = s0 + s2 + s3;
            }
            //stt=��Ʊ״̬|select|1+����;3 selected+���;6+�ջ�
            if (qryParams.indexOf("stt") > 0) {
                String sVal = getStrValue("stt");

                int pos0 = qryParams.indexOf("stt");
                String s0 = qryParams.substring(0, pos0); //stt��ǰ����

                String s1 = qryParams.substring(pos0);
                int pos1 = s1.indexOf(",");
                String s2 = s1.substring(0, pos1); //stt����
                String s3 = s1.substring(pos1); //stt�Ժ󲿷�

                s2 = s2.replaceAll(sVal, sVal + " selected");
                qryParams = s0 + s2 + s3;
            }
            //status=��Ʊ״̬|select|1+����;3 selected+���;6+�ջ�
            if (qryParams.indexOf("stuu") > 0) {
                String sVal = getStrValue("stuu");

                int pos0 = qryParams.indexOf("stuu");
                String s0 = qryParams.substring(0, pos0); //status��ǰ����

                String s1 = qryParams.substring(pos0);
                int pos1 = s1.indexOf(",");
                String s2 = s1.substring(0, pos1); //status����
                String s3 = s1.substring(pos1); //status�Ժ󲿷�

                //s2=s2.replaceAll(sVal,sVal+" selected");
                s2 = s2.replaceAll(";" + sVal + "+", ";" + sVal + " selected");
                qryParams = s0 + s2 + s3;
            }
            if (qryParams.indexOf("mgrdata") > 0) { //�ͻ�����ĵ����⴦��ֻ���嵥����
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
                        ",upbrh=��ѡ������|select|" + brhList);
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
//����ϵͳĬ�ϱ���
        if (linkKey1Name != null) {

            if (!linkKey1Name.equals("null") && !linkKey1Name.equals(dispName)) {
                dispName = dispName + "(" + linkKey1Name + ")";
            }
        }
        caption = caption.replaceAll(":dispname", dispName);
        lTitle = lTitle.replaceAll(":dispname", dispName);
        mTitle = mTitle.replaceAll(":dispname", dispName);

        if (lTitle.trim().equals("")) {
            lTitle = "��λ����:" + dispName;
        }

        if (rTitle.trim().length() == 0) {
            if (dataDispWan == 0) {
                rTitle = "��λ��Ԫ";
            } else {
                rTitle = "��λ����Ԫ";
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
     * @param :brhid String     ��������(ʵ����)
     * @return :strCM    String     ���ɵĿͻ������select�ַ���
     *         *********************************************************
     * @function :ȡ�ÿͻ���������
     */
    public String getClientManager(String brhid) {
        String strCM = ",mgrdata=�ͻ�����|select|%+����"; //���صĿͻ�������ֶ�
        String strLName = ""; //�ͻ������½��
        String strUName = ""; //�ͻ���������

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
            errMsg = errMsg + "�����ݿ�ȡ�ͻ�����ʧ�ܣ�";
            se.printStackTrace();
        }
        strCM = strCM + ",";

        return strCM;
    }

    /**
     * *********************************************************
     *
     * @param :brhid String ��������
     * @return :void
     * @function :����Ҫ��ʾ����������
     * *********************************************************
     */
    public void setDispName(String brhid) {
        String dispName = ""; //��������

        String strSql = "select sname from scbranch where brhid='" + brhid + "'";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(strSql);
            if (rs.next()) {
                dispName = DBUtil.fromDB(rs.getString(1));
                this.setUserValues("dispName", dispName);
            }
        } catch (SQLException se) {
            errMsg = errMsg + "�����ݿ�ȡ��������ʧ�ܣ�";
            se.printStackTrace();
        }
    }

    /**
     * ******************************************************************
     * ��ṹ��brhid, tblitmno,itmclass,itmcount,itmcount1,itmcount2,itmdata,itmdata1,itmdata2
     * ��  ʽ������,��Ŀ��,����,�³����������·��ű�������ǰ����,�³������·��Ŷ��ǰ���
     * �������Ŀ��:
     * "Q00001","��ҵ������",BMType,
     * "Q00002","��ռ����̬",LoanCat2,
     * "Q00003","��������ʽ",LoanType3,
     * "Q00004","����������",LoanType2,
     * "Q00005","�����ŷ�ʽ",LoanType5,
     * "Q00006","��������;",LoanCat3,
     * "Q00007","���ͻ�����",ClientType,
     * "Q00008","�����ò���",EcomDeptType,
     * "Q00009","����ҵ����",SectorCat1,
     * "Q00010","����ҵ��ģ",EtpScopType,
     * "Q00011","����������",EcomType,
     * "Q00012","���弶����",LoanCat1,
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

        if (callSql.indexOf("Q10010") > 0) { //�������Ŀ��ѯ�������
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
        ScbranchLevel sbl = new ScbranchLevel(brhId); //�ж����㼶��
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
        ScbranchLevel sbl = new ScbranchLevel(upbrh); //�ж����㼶��
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

    /*************************�����ǲ�ѯ���ù����õ��ķ���************************/
    /**
     * *******************************************************************
     * ������Ŀ����
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
                        strRtn = "��¼ɾ���ɹ�";
                    } else {
                        strRtn = "��¼ɾ��ʧ��";
                    }
                    cmdOk = -1;
                } else {
                    if (cmd.equals("cmdAdd")) { //������ ,����¼�Ѿ����ڣ�Ϊ��ֹ��ã�����������
                        strRtn = "��¼�Ѿ�����:" + qryNo;
                        cmdOk = -1;
                    }
                }
            } else {
                if (cmd.equals("cmdAdd")) {
                    //�����¼�¼
                    cmdOk = 1;
                } else {
                    cmdOk = -1; // ���޸ģ�����¼������
                    strRtn = "��¼������";
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
                    //���ڱ���ֻ��smallint ��CHAR���ֶΣ�����������д������������
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
                    strRtn = "��¼�޸ĳɹ�";
                } else {
                    strSql = "insert into scqrydef values (" + strVal + ")";
                    stmt.executeUpdate(strSql);
                    strRtn = "��¼���ӳɹ�";
                }
                this.getTableDefine(qryNo);
            }
        } catch (SQLException se) {
            errMsg = errMsg + "�������ݿ������ʧ�ܣ�";
            se.printStackTrace();
        }
        return strRtn;
    }

    /**
     * ***********************************************************
     * ����������SQL�����
     *
     * @return ************************************************************
     */
    public String getTestSql() {
        this.init();
        /******������******/
        String rtnSql = null;
        setUserValues("dispmode", 0);
        setUserValues("brhid", "907999999");
        setCurrentBrhId();
        rtnSql = getSQL();
        /******����******/
        setUserValues("brhid", "907069999");
        setUserValues("linkKey", "bnkid");
        setCurrentBrhId();
        rtnSql = rtnSql + "!" + getSQL();
        /******�Ŵ�����******/
        setUserValues("brhid", "907060199");
        setUserValues("linkKey", "chgid");
        setCurrentBrhId();
        rtnSql = rtnSql + "!" + getSQL();

        return rtnSql;
    }

}
