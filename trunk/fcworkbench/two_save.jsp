<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="zt.cmsi.pub.define.BMFCLimit" %>
<%@ page import="zt.cmsi.pub.define.SystemDate" %>
<%@ page import="zt.cmsi.pub.cenum.EnumValue" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.DecimalFormat" %>

<%--
===============================================
Title: ��ֲ���-���������ύ
Description: ���������ύ��
 * @version  $Revision: 1.5 $  $Date: 2007/05/28 09:21:44 $
 * @author
 * <p/>�޸ģ�$Author: liuj $
===============================================
--%>

<%
    BMFCLimit bmf = BMFCLimit.getInstance();
    request.setCharacterEncoding("GBK");
    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    if (um == null) {
        response.sendRedirect("../fcworkbench/error.jsp");
    }
    ConnectionManager manager = ConnectionManager.getInstance();
    String FCNO = request.getParameter("FCNO");
    String FCSTATUS = request.getParameter("FCSTATUS");
    String FCTYPE = request.getParameter("FCTYPE");
    if (FCTYPE == null) FCTYPE = "";//lj added in 2007-04-27 for reduce error.
    String BMNO = request.getParameter("BMNO");
    String BMTYPE = request.getParameter("BMTYPE");
    String BILLNO = request.getParameter("BILLNO");
    String CLIENTNO = request.getParameter("CLIENTNO");     //�ͻ�����     lj added in 2007-05-28
    String CREATEDATE = request.getParameter("CREATEDATE"); //�������     lj added in 2007-05-28

    if (FCNO == null || FCSTATUS == null || FCTYPE == null || BMNO == null || BMTYPE == null || BILLNO == null) {
        session.setAttribute("lettermess", "û�з��ִ�����Ĳ�����");
        response.sendRedirect("../fcworkbench/lettersucces.jsp");
    }
    boolean temp;

    int LMFCCLASS = Integer.parseInt(request.getParameter("LMFCCLASS").trim());//lj added in 2007-04-25 for work flow
    int FCAUTO = Integer.parseInt(request.getParameter("FCAUTOINT").trim());   //lj added in 2007-04-25 for work flow
    int FC2 = Integer.parseInt(request.getParameter("FC2").trim());
    String BADREASON = request.getParameter("BADREASON");//�����γɲ���ԭ��                      lj added in 2007-04-27
    if (BADREASON.equals("")) BADREASON = "NULL";
    String LOANWAY = request.getParameter("LOANWAY");    //����Ͷ��                              lj added in 2007-04-27

    String CMT3;
    if (request.getParameter("CMT3") == null) {
        CMT3 = null;
    } else {
        CMT3 = request.getParameter("CMT3").trim();
    }
    String status = "";

    DecimalFormat df = new DecimalFormat();
    BigDecimal bal = new BigDecimal(df.parse(request.getParameter("BAL")).toString());

//    ******************lj delete in 2007-04-25
//    String bmno = "";
//    int loancat1 = 0;

//    String sql_fcclass = "select * from FCMAIN where FCNO='" + FCNO + "'";
//    CachedRowSet rs = manager.getRs(sql_fcclass);
//    if (rs.next()) {
//        bal = rs.getBigDecimal("bal");
//        bmno = rs.getString("bmno");
//    }
//    String sql_rqloanledger = "select loancat1 from rqloanledger where bmno='" + bmno + "'";
//    CachedRowSet vrs = manager.getRs(sql_rqloanledger);
//    if (vrs.next()) {
//        loancat1 = Integer.parseInt(vrs.getString("loancat1"));
//    }
//    ******************lj delete end

    if (FC2 == 5) {//���������϶����Ϊ����ʧ�����ύ������������ lj added comment in 2007-04-25
        status = "3";
    } else {
        if (FC2 < LMFCCLASS || FC2 < FCAUTO) {//���������϶���������ϴν������������ߺ��ڱ����Զ�������ύ������������ lj changed comment in 2007-04-25
            status = "3";
        } else {
            if (FCTYPE.equals(String.valueOf(EnumValue.FCType_ZiRanRenNongHu)) || FCTYPE.equals(String.valueOf(EnumValue.FCType_ZiRanRenQiTa))) {
                if (bmf.getLimitofBrh(SCUser.getBrhId(um.getUserName())) == null) {
                    request.setAttribute("mess", "�ٽ����δ����");
%>
<jsp:forward page="/fcworkbench/two.jsp"/>
<%
                } else {
                    if (bmf.getLimitofBrh(SCUser.getBrhId(um.getUserName())).compareTo(bal) >= 0) {
                        status = "8";
                    } else {
                        status = "3";
                    }
                }
            } else {
                status = "3";
            }
        }
    }
    String Operbrhid = SCBranch.getSupBrh(SCUser.getBrhId(um.getUserName()), EnumValue.BrhLevel_XianLianShe);//ȡ���ϼ�����


    String[] sql = new String[3];
    sql[0] = "update FCMAIN set FC2=" + FC2 + ",OPERBRHID='" + Operbrhid + "',FCSTATUS=" + status
            + ",OPERATOR='" + um.getUserName() + "',LASTMODIFIED=" + DBUtil.toSqlDate(SystemDate.getSystemDate5(""))
            + ",BADREASON=" + BADREASON + ",LOANWAY='" + LOANWAY + "'" + "";                                 //lj added BADREASON,LOANWAY in 2007-04-27
    if (status.equals(String.valueOf(EnumValue.FCStatus_WanCheng))) {
        sql[0] += ",FCCLASS=" + FC2 + ",FCOPRFLAG=1";                                                        //lj added FCOPRFLAG in 2007-05-10 ����ֱ��Ϊ�˹�";
        if (BMTYPE.equals(String.valueOf(EnumValue.BMType_ChengDuiHuiPiao))) {
            sql[1] = "update BMACPTBILL set LOANCAT1=" + FC2 + " where ACPTBILLNO=" + BILLNO + "";           //�жһ�Ʊ̨��           lj commented in 2007-04-27
        } else
        if (BMTYPE.equals(String.valueOf(EnumValue.BMType_TieXian)) || BMTYPE.equals(String.valueOf(EnumValue.BMType_ZhuanTieXian))) {
            sql[1] = "update BMBILLDIS set LOANCAT1=" + FC2 + " where BILLDISNO=" + BILLNO + "";             //ת���ֵ����       lj commented in 2007-04-27
        } else {
            sql[1] = "update RQLOANLEDGER set LOANCAT1=" + FC2 + " where BMNO='" + BMNO + "'";               //����̨�ʱ�             lj commented in 2007-04-27
        }
    }
    sql[0] += " where FCNO='" + FCNO + "'";

    String query_sql = " select * from FCCMT where FCNO='" + FCNO + "' and FCCMTTYPE=1";                     //��ȡ�������Ϊ�������  lj commented in 2007-04-27
    CachedRowSet crs = manager.getRs(query_sql);
    if (crs.next()) {
        sql[2] = "update FCCMT set CMT3='" + DBUtil.toDB(CMT3) + "' where FCCMTTYPE=1 and FCNO='" + FCNO + "'";
    } else {
        sql[2] = "insert into FCCMT(FCNO,FCCMTTYPE,CMT3) values('" + FCNO + "',1,'" + DBUtil.toDB(CMT3) + "')";
    }
    temp = manager.execBatch(sql);                                                                           //�ύ���и���           lj commented in 2007-04-27


    if (temp) {
        if (status.equals(String.valueOf(EnumValue.FCStatus_WanCheng))) {
            session.setAttribute("mess", "�ύ����ֽ�����");
        } else {
            session.setAttribute("mess", "�ύ������������");
        }
        response.sendRedirect("../fcworkbench/lettersucces.jsp");
    } else {
        request.setAttribute("mess", "�ύ��Ϣʧ��");
%>
<jsp:forward page="/fcworkbench/two.jsp"/>
<%
    }
%>