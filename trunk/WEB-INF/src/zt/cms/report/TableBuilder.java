package zt.cms.report;

/**
 * <p>Title: �Ŵ�����</p>
 * <p>Description: Ϋ���Ŵ�����ϵͳ</p>
 * <p>Copyright: Copyright (c) 2003 ������Ϣ�������޹�˾</p>
 * <p>Company: ������Ϣ�������޹�˾</p>
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
 * �����������ɵ�ͨ�ó���,���ݱ���ı�ŵ��ô洢���̻�ñ��������
 * �ӹ������õ����ݵ��뵽������MRDataSet������������������
 * <p/>
 * ******************************************************************
 */
public class TableBuilder {
    public DatabaseConnection dbc = null; //���ݿ�����
    public Connection conn = null;        //���ݿ�����
    public Statement stmt = null;         //���ݿ�����״̬

    public String strReptNo;          //������
    public String errMsg = null;        //�������

    private String strLoginBrhid;      //��½�û���������
    private String strLoginName;       //��½�û���������������
    private String strBrhId;           //����������
    private String strScbrhName;       //������������
    private String strClientName;      //����ͻ�����
    private String strMonth;           //ͳ���·�
    private String strEndDate;         //��ǰ��ҵ������
    private String strFCDate;          //��ǰ���弶����ҵ������    lj added in 20050711
    private String strTenThousand = "��Ԫ"; //��λ����Ԫ��ǧԪ����Ԫ
    private int intTenThousand = 10000;     //��λ��10000,1000,100
    private int isNeedDivide = 1;           //�弶�����������Ƿ���Ҫ���е�λ���㴦��0 �� 1 �� 2 ����  lj added in 20050713
    int intBrhRowCount = 0;                 //����������
    private int intMaxRow = 10;             //���������С��10������10��
    private int flag = 1;                   //��������־   lj added in 20050905

    public String tmpBranch[][] = null;     //����¼���������
    public String dataBranch[][] = null;    //�����ʾ������������
    ResultSetMetaData rsmdBranch = null;    //������
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
            errMsg = "û�л�ñ����ţ�";
        if (this.strLoginBrhid == null)
            errMsg = "��õ�½�û���������ʧ�ܣ������µ�½��";
        if (this.strBrhId == null)
            errMsg = "��ñ�������ʧ�ܣ������µ�½��";
        if (errMsg == null) {
            try {
                dbc = MyDB.getInstance().apGetConn();
                conn = dbc.getConnection();
            } catch (Exception e) {
                errMsg = "���ݿ�����ʧ�ܣ�";
                MyDB.getInstance().apReleaseConn(1);//�ͷ����ݿ�����
            }
            strEndDate = this.getDate();////��õ�ǰ��ҵ������
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

    public String getDate()//��õ�ǰ��ҵ������
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
            this.errMsg = "��Ҫͳ�Ƶ������ѯʧ�ܣ�";
            MyDB.getInstance().apReleaseConn(1);
        }
        return strDate;
    }

    private String getBrhId(HttpServletRequest request)//��õ�½�û�������
    {
        String strScbrhId = null;

        HttpSession session = (HttpSession) request.getSession(true);
        UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if (um == null) {
            this.errMsg = "������ʱ�������µ�½��";
            return strScbrhId;
        } else {
            try {
                String strUserName = um.getUserName();
                this.strLoginBrhid = SCUser.getBrhId(strUserName);
                this.strLoginName = SCBranch.getSName(this.strLoginBrhid);
            } catch (Exception e) {
                errMsg = "��õ�½�û���������ʧ�ܣ������µ�½��";
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

    private ResultSet getBrhName() // ����¼�����ź���������
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
        } else//�����ʵ���㣬�¼�������Ϊ����
        {
            strBuf = new StringBuffer();
            strBuf.append("select brhid,sname from scbranch where brhid='");
            strBuf.append(this.strBrhId);
            strBuf.append("'");
            strSql = strBuf.toString();
            strBuf = null;
            //System.out.println("û���¼������ˣ�");
        }
        try {
            //System.out.println(strSql);
            Statement psmt1 = conn.createStatement(this.rs_Type, this.rs_Cur);
            rsBranch = psmt1.executeQuery(strSql);
        } catch (SQLException se) {
            this.errMsg = "��Ҫͳ�Ƶ������ѯʧ�ܣ�";
            MyDB.getInstance().apReleaseConn(1);
        }
        return rsBranch;
    }

    private ResultSet getData()//���ô洢���̻��ͳ�ƽ��
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
            MyDB.getInstance().apReleaseConn(1); //�ͷ����ݿ�����
            this.errMsg = "��������ͳ��ʧ�ܣ�";
        }
        return rsData;
    }

    private int getBrhGrade(String brhid)//�ж��û��ļ���
    {
        int intGrade = 0;
        //System.out.println("brhid:"+brhid);
        ScbranchLevel sbl = new ScbranchLevel(brhid);//�ж����㼶��S
        intGrade = sbl.intBrhLevel;

        if (intGrade == 1)//������
        {
            this.strTenThousand = "��Ԫ";
            this.intTenThousand = 10000;
        } else if (intGrade == 2)//������
        {
            this.strTenThousand = "ǧԪ";
            this.intTenThousand = 1000;
        } else if (intGrade == 3)//�Ŵ�����
        {
            this.strTenThousand = "��Ԫ";
            this.intTenThousand = 100;
        } else //����
        {
            intGrade = 4;
            this.strTenThousand = "��Ԫ";
            this.intTenThousand = 100;
        }
        return intGrade;
    }

    /**
     * ***************************************************
     * �������ݻ��(����Ϣ���� ����F6%��ͷ��)
     * function :������е��¼�����
     * return   :String tmpBranch[][]  �¼�����
     * ***************************************************
     */
    private String[][] getScBranch() {
        ResultSet rsBranch = null;            //�������ݼ�
        int intBrhColCount = 0;               //�������ֵ�����
        int intCount = 0;                     //�¼��������Ŀ
        int n = 0;

        rsBranch = this.getBrhName();
        if (this.errMsg != null)
            return null;
        try {
            rsmdBranch = rsBranch.getMetaData();
            intBrhColCount = rsmdBranch.getColumnCount();
            while (rsBranch.next())//����¼�������(������ʵ������)
            {
                //if(getBrhGrade(rsBranch.getString(1).trim())!=4)//ȥ��ʵ����
                intCount++;
                intBrhRowCount++;
            }
            //rsBranch.beforeFirst();
            rsBranch = this.getBrhName();

            ////���С��10������10����������,���ڵ��ڲ�����
            if (this.intBrhRowCount < this.intMaxRow) {
                this.intBrhRowCount = this.intMaxRow;
            }
            dataBranch = new String[intBrhRowCount][intBrhColCount];
            tmpBranch = new String[intCount + 2][intBrhColCount];
            //����¼�����ֻ�����ѻ��ܽ�β��(Ŀ��),������ʾ��������Ҫ�����¼�
            //��ʵ�����㣬ѭ����m=intBrhRowCount++;
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
                    strName=strName.substring(0,strName.length()-"����".length());
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
            MyDB.getInstance().apReleaseConn(1); //�ͷ����ݿ�����
            this.errMsg = "����¼�����ʧ�ܣ�";
        }
        while (n < this.intMaxRow)//���С��10������10���������
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
     * �������ݻ��(����Ϣ���� ����F6%��ͷ��)
     * function :��������������������,�ŵ�������е�MRDataSet��
     * return   :MRDataSet mrds
     * *************************************************************
     */
    public MRDataSet getReportData() {
        ResultSet rsData = null;              //�����������ݼ�
        ResultSetMetaData rsmdData = null;    //

        String data[][] = null;               //����������
        String label[][] = null;              //�����,������������
        String tmpData[][] = null;            //����ͳ����������
        int intBrhColCount = 0;               //�������ֵ�����
        int intDataColCount = 0;              //�������ݵ�����
        MRDataSet mrds = new MRDataSet();
        int intCols = 0;

        rsData = this.getData();
        if (this.errMsg != null)//����������ݲ����쳣��������null
            return null;
        try {
            rsmdData = rsData.getMetaData();

            intBrhColCount = this.rsmdBranch.getColumnCount();
            intDataColCount = rsmdData.getColumnCount();
            intCols = intDataColCount + intBrhColCount - 1;
            label = new String[1][intCols];//�������

            for (int i = 0; i < intCols; i++)//�����˵��(����F1��F2��)
            {
                if (i < intBrhColCount)
                    label[0][i] = rsmdBranch.getColumnLabel(i + 1);
                else
                    label[0][i] = rsmdData.getColumnLabel(i + 2 - intBrhColCount);
            }
            tmpData = new String[intBrhRowCount][intDataColCount];//
            int n = 0;

            while (rsData.next())//����¼��������������
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
            this.errMsg = "����ת��Ϊ����ʧ�ܣ�";
            System.out.println("����ת��Ϊ����ʧ�ܣ�");
        } finally {
            MyDB.getInstance().apReleaseConn(1);//�ͷ����ݿ�����
        }

        data = new String[intBrhRowCount][intCols];
        if (this.errMsg != null) {
            for (int i = 0; i < intBrhRowCount; i++) { //�����������������ʾ0
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
            for (int i = 0; i < intBrhRowCount; i++) { //��������
                String strBrhid = dataBranch[i][0].trim();
                int haveRec = -1;

                //�ж�����������������������ţ������ͬ��¼����������������������λ��
                //��haveRec,������������������в����ڴ��������haveRec=-1
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

                //����haveRec�Ľ��������������ֵ��ӵ�data������
                for (int m = 0; m < intCols; m++) {
                    String strData = null;
                    if (m < intBrhColCount)
                        strData = dataBranch[i][m];
                    else {
                        if (haveRec != -1) {
                            strData = tmpData[haveRec][m - intBrhColCount + 1];//��Ҫת��Ϊ��Ԫ
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
        for (int i = 0; i < intBrhRowCount; i++)//�������������͵�������MRDataSet��
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
     * �������ݻ��(��Ϣ���� ����F6%��ͷ��)
     * function :����¼�����
     * return   :String tmpBranch[][]  �¼�����
     * ************************************************************
     */
    private String[][] getScbranchRate() {
        ResultSet rsBranch = null;            //�¼���������ݼ�
        int intBrhColCount = 0;               //�������ֵ�����
        int intCount = 0;                     //�¼��������Ŀ
        int intGrade = 0;
        int n = 0;

        intGrade = this.getBrhGrade(this.strBrhId);
        if (intGrade > 2 || intGrade == 0) {
            this.errMsg = "���������������µ�����!";

            MyDB.getInstance().apReleaseConn(1); //�ͷ����ݿ�����
            return null;
        }
        rsBranch = this.getBrhName();
        if (this.errMsg != null)
            return null;
        try {
            rsmdBranch = rsBranch.getMetaData();
            intBrhColCount = rsmdBranch.getColumnCount();
            while (rsBranch.next())//����¼�������(������ʵ������)
            {
                //if(getBrhGrade(rsBranch.getString(1).trim())!=4)//ȥ��ʵ����
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
            MyDB.getInstance().apReleaseConn(1); //�ͷ����ݿ�����
            this.errMsg = "����¼�����ʧ�ܣ�";
        }
        this.AddUserBrhid(intCount);
        return this.tmpBranch;

    }

    /**
     * ************************************************************
     * �������ݻ��(�弶����)
     * function :��������������������,�ŵ�������е�MRDataSet��
     *
     * @return :MRDataSet mrds
     *         author lj
     *         Created in 20050713
     *         *************************************************************
     */
    public MRDataSet getReportFCData() {
        ResultSet rsData = null;              //�����������ݼ�
        ResultSetMetaData rsmdData = null;    //

        String data[][] = null;               //����������
        String label[][] = null;              //�����,������������
        String tmpData[][] = null;            //����ͳ����������
        int intBrhColCount = 0;               //�������ֵ�����
        int intDataColCount = 0;              //�������ݵ�����
        MRDataSet mrds = new MRDataSet();
        int intCols = 0;
        int intRettNo = Integer.parseInt(this.strReptNo.substring(2, 6));// lj added in 20050715

        rsData = this.getData();
        if (this.errMsg != null)//����������ݲ����쳣��������null
            return null;
        try {
            rsmdData = rsData.getMetaData();

            intBrhColCount = this.rsmdBranch.getColumnCount();
            intDataColCount = rsmdData.getColumnCount();
            intCols = intDataColCount + intBrhColCount - 1;
            label = new String[1][intCols];//�������

            for (int i = 0; i < intCols; i++)//�����˵��(����F1��F2��)
            {
                if (i < intBrhColCount)
                    label[0][i] = rsmdBranch.getColumnLabel(i + 1);
                else
                    label[0][i] = rsmdData.getColumnLabel(i + 2 - intBrhColCount);
            }
            tmpData = new String[intBrhRowCount][intDataColCount];//


            int n = 0;

            while (rsData.next())//����¼��������������
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
            this.errMsg = "����ת��Ϊ����ʧ�ܣ�";
            System.out.println("����ת��Ϊ����ʧ�ܣ�");
        } finally {
            MyDB.getInstance().apReleaseConn(1);//�ͷ����ݿ�����
        }

        data = new String[intBrhRowCount][intCols];
        if (this.errMsg != null) {
            for (int i = 0; i < intBrhRowCount; i++) { //�����������������ʾ0
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
            for (int i = 0; i < intBrhRowCount; i++) { //��������
                String strBrhid = dataBranch[i][0].trim();
                int haveRec = -1;

                //�ж�����������������������ţ������ͬ��¼����������������������λ��
                //��haveRec,������������������в����ڴ��������haveRec=-1
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

                //����haveRec�Ľ��������������ֵ��ӵ�data������

                for (int m = 0; m < intCols; m++) {
                    String strData = null;
                    if (m < intBrhColCount)
                        strData = dataBranch[i][m];
                    else {
                        if (haveRec != -1) {
                            strData = tmpData[haveRec][m - intBrhColCount + 1];//��Ҫת��Ϊ��Ԫ
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
        for (int i = 0; i < intBrhRowCount; i++)//�������������͵�������MRDataSet��
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
     * �������ݻ��(�弶����)�����������б���ʽ
     * function :��������������������,�ŵ�������е�MRDataSet��
     *
     * @return :MRDataSet mrds
     *         author lj
     *         Created in 20050722
     *         *************************************************************
     */
    public MRDataSet getReportFCDataNoBrh() {
        MRDataSet mrds = new MRDataSet();     //�����������ݼ�
        ResultSet rsData = null;              //�����������ݼ�
        ResultSetMetaData rsmdData = null;    //
        int intColCount = 0;                  //����
        rsData = this.getData();
        if (this.errMsg != null)//����������ݲ����쳣��������null
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

                for (int i = 0; i < n; i++) {//�������������͵�������MRDataSet��
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
                        switch (intRettNo) {//�弶����ṹ��������
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
            this.errMsg = "����ת��Ϊ�������ݼ�ʧ�ܣ�";
            System.out.println("����ת��Ϊ�������ݼ�ʧ�ܣ�");
        } finally {
            MyDB.getInstance().apReleaseConn(1);//�ͷ����ݿ�����
        }

        return mrds;
    }

    /**
     * ***********************************************************
     * �������ݻ��(��Ϣ���� ����R06%��ͷ��)
     * function :��������������������,�ŵ�������е�MRDataSet��
     * return   :MRDataSet mrds
     * ************************************************************
     */
    public MRDataSet getReportDataRate() {
        MRDataSet mrds = new MRDataSet();     //�����������ݼ�
        ResultSet rsData = null;              //�����������ݼ�
        ResultSetMetaData rsmdData = null;    //
        int intColCount = 0;                  //����

        //System.out.println("rate");
        rsData = this.getData();
        if (this.errMsg != null)//����������ݲ����쳣��������null
            return null;
        try {
            rsmdData = rsData.getMetaData();
            intColCount = rsmdData.getColumnCount();

            while (rsData.next()) {
                DataRecord rec = new DataRecord();

                for (int i = 1; i <= intColCount; i++) {
                    if (this.strReptNo.substring(0, 5).equals("R0603"))//�̶���������ֲ���
                    {
                        if (i == 1)
                            rec.setValue("sname", DBUtil.fromDB(rsData.getString(1)));
                        else {
                            String strM = String.valueOf(i - 1);
                            rec.setValue("F" + strM, new Integer(rsData.getInt(i) / 10000));
                        }
                    } else if (this.strReptNo.substring(0, 5).equals("R0602"))//��������ˮƽ��
                    {
                        if (i % 2 == 0) {
                            rec.setValue("F" + i, new Double(rsData.getDouble(i)));
                        } else {
                            rec.setValue("F" + i, new Integer(rsData.getInt(i) / 10000));
                        }
                    } else//�̶�����ˮƽ��
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
            this.errMsg = "����ת��Ϊ�������ݼ�ʧ�ܣ�";
            System.out.println("����ת��Ϊ�������ݼ�ʧ�ܣ�");
        } finally {
            MyDB.getInstance().apReleaseConn(1);//�ͷ����ݿ�����
        }

        return mrds;
    }


    /**
     * ************************************************************
     * �����ò���
     * ȷ����������ʹ�þ���·��
     * *************************************************************
     */
    public String showReport(String id) {
        String reportStr = null;
        MRDataSet mrds = null;
        MRDataSet mrds3 = null;
        MRDataSet mrds4 = null;
        int intRettNo = Integer.parseInt(this.strReptNo.substring(2, 6)); // lj added in 20050712
        String strRoot = PropertyManager.getProperty("root");//��ñ���ľ���·��
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
                        errMsg = "���ݿ�����ʧ�ܣ�";
                        MyDB.getInstance().apReleaseConn(1);//�ͷ����ݿ�����
                    }
                    mrds3 = this.getReportFCDataNoBrh();
                    this.flag = 3;
                    try {
                        dbc = MyDB.getInstance().apGetConn();
                        conn = dbc.getConnection();
                    } catch (Exception e) {
                        errMsg = "���ݿ�����ʧ�ܣ�";
                        MyDB.getInstance().apReleaseConn(1);//�ͷ����ݿ�����
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
            engine.addMRDataSet("rs2", mrds);//�󶨻�õ�����
            if (intRettNo == 7200) {
                engine.addMRDataSet("rs3", mrds3);//�󶨻�õ�����
                engine.addRelation("rs2", "rs3", "CNO=CNO");
                engine.addMRDataSet("rs4", mrds4);//�󶨻�õ�����
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
            //System.out.println("����·���󶨱���+==============================="+this.strReptNo+strRoot + rptno + ".mrf");
            engine.bind(this.strReptNo, strRoot + rptno + ".mrf");//�þ���·���󶨱���
           
            reportStr = engine.createViewer(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reportStr;
    }

    public String getTenThousand()//����Ƿ���Ԫ�ı�־
    {
        return this.strTenThousand;
    }

    public String getScbranchName()//��õ�ǰ���������
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

    public String[][] getArray()//����¼�������
    {
        if (this.strReptNo.substring(0, 3).equals("R06"))
            this.getScbranchRate();
        else
            this.getScBranch();

        return this.tmpBranch;
    }

    public String getErrMsg()  //��ô�����Ϣ
    {
        return this.errMsg;
    }

    //��֤�¼��������������ʼ�մ��е�½�û�������
    public void AddUserBrhid(int count) {
        this.tmpBranch[count][0] = this.strLoginBrhid;
        this.tmpBranch[count][1] = "----------------";
        this.tmpBranch[count + 1][0] = this.strLoginBrhid;
        this.tmpBranch[count + 1][1] = this.strLoginName;
    }

}