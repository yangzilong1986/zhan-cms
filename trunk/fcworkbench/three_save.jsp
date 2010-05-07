<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cmsi.pub.define.SystemDate" %>
<%@ page import="zt.cmsi.pub.cenum.EnumValue" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>

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
    request.setCharacterEncoding("GBK");
    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    if (um == null) {
        response.sendRedirect("../fcworkbench/error.jsp");
    }
    ConnectionManager manager = ConnectionManager.getInstance();
    String FCNO = request.getParameter("FCNO");
    String FCSTATUS = request.getParameter("FCSTATUS");
    String FCTYPE = request.getParameter("FCTYPE");
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
    String FC3 = (request.getParameter("FC3") == null) ? null : request.getParameter("FC3").trim();
    String BADREASON = request.getParameter("BADREASON");//�����γɲ���ԭ��                      lj added in 2007-04-28
    if (BADREASON.equals("")) BADREASON = "NULL";
    String LOANWAY = request.getParameter("LOANWAY");    //����Ͷ��                              lj added in 2007-04-28


    String CMT4 = (request.getParameter("CMT4") == null) ? null : request.getParameter("CMT4").trim();

    String[] sql = new String[3];
    String query_sql = " select * from FCCMT where FCNO='" + FCNO + "' and FCCMTTYPE=1";
    sql[0] = "update FCMAIN set FC3=" + FC3 + ",FCSTATUS=" + 8 + ",FCCLASS=" + FC3 + ",OPERATOR='" + um.getUserName()
            + "',LASTMODIFIED=" + DBUtil.toSqlDate(SystemDate.getSystemDate5(""))
            + ",FCOPRFLAG=1"                                                                                         //lj added FCOPRFLAG in 2007-05-10 ����ֱ��Ϊ�˹�
            + ",BADREASON=" + BADREASON + ",LOANWAY='" + LOANWAY + "'"                                               //lj added BADREASON,LOANWAY in 2007-04-28
            + " where FCNO='" + FCNO + "'";
    if (BMTYPE.equals(String.valueOf(EnumValue.BMType_ChengDuiHuiPiao))) {
        sql[1] = "update BMACPTBILL set LOANCAT1=" + FC3 + " where ACPTBILLNO=" + BILLNO + "";
    } else
    if (BMTYPE.equals(String.valueOf(EnumValue.BMType_TieXian)) || BMTYPE.equals(String.valueOf(EnumValue.BMType_ZhuanTieXian))) {
        sql[1] = "update BMBILLDIS set LOANCAT1=" + FC3 + " where BILLDISNO=" + BILLNO + "";
    } else {
        sql[1] = "update RQLOANLEDGER set LOANCAT1=" + FC3 + " where BMNO='" + BMNO + "'";
    }

    CachedRowSet crs = manager.getRs(query_sql);
    if (crs.next()) {
        sql[2] = "update FCCMT set CMT4='" + DBUtil.toDB(CMT4) + "' where FCCMTTYPE=1 and FCNO='" + FCNO + "'";
    } else {
        sql[2] = "insert into FCCMT(FCNO,FCCMTTYPE,CMT4) values('" + FCNO + "',1,'" + DBUtil.toDB(CMT4) + "')";
    }
    temp = manager.execBatch(sql);

    if (temp) {
        session.setAttribute("mess", "�ύ����ֽ�����");
        response.sendRedirect("../fcworkbench/lettersucces.jsp");
    } else {
        request.setAttribute("mess", "�ύ��Ϣʧ��");
%>
<jsp:forward page="/fcworkbench/three.jsp"/>
<%
    }
%>