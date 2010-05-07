<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="com.zt.util.PropertyManager" %>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFWorkbook" %>
<%@ page import="zt.cms.util.poiutil.POIUtil" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.util.HashMap" %>


<%
    ConnectionManager manager = ConnectionManager.getInstance();
    String sql = "with e as (SELECT " +
            "to_char(MIN(e.PAYBACKDATE),'YYYY-MM-DD') || '  ' || to_char(MAX(e.PAYBACKDATE),'YYYY-MM-DD') qj," +
            "SUM(e.SERVICECHARGEFEE + e.ODB_PAYBACKAMT) AMT," +
            "SUM(e.PRINCIPALAMT) SPRINCIPALAMT," +
            "SUM(e.SERVICECHARGEFEE) SSERVICECHARGEFEE," +
            "SUM(e.CLOSEDCD) SCLOSEDCD," +
            "SUM(e.OVERDUECD) SOVERDUECD," +
            "SUM(CASE WHEN (e.CLOSEDDATE - e.PAYBACKDATE) > 7 THEN 1 ELSE 0 END) SKX," +
            "MAX(e.CLOSEDDATE - e.PAYBACKDATE) SZC,CONTRACTNO " +
            "FROM XFACTCUTPAYMAIN e GROUP BY CONTRACTNO) " +

            "select rownum,a.appno,a.name,b.CLIENTACT,b.CONTRACTNO,REPLACE(trim(c.COMMNAME),chr(10),''),b.COMMAMT,REPLACE(trim(c.CHANNEL),chr(10),'')," +
            "b.RECEIVEAMT,b.CONTRACTAMT,d.PAYDATE,b.PAYBACKBANKID,b.PAYBACKACT,qj,b.DURATION," +
            "b.SERVICECHARGE,e.AMT,SPRINCIPALAMT,SSERVICECHARGEFEE,SCLOSEDCD,SOVERDUECD,SKX,SZC " +
            "from XFAPP a,XFAPPCOMM c,e,XFCONTRACT b " +
            "left outer join XFACTPAYDETL d on b.CONTRACTNO=d.CONTRACTNO and d.PayStatus ='4' " +
            "where a.appno=b.appno and a.appno=c.appno and b.CONTRACTNO =e.CONTRACTNO";

    CachedRowSet crs = null;
    try {
        crs = manager.getRs(sql);
        out.println("crs.size() = " + crs.size());
    } catch (Exception e) {
        e.printStackTrace();
    }
    HashMap<String, Object> excelMap = new HashMap<String, Object>();

    String poiexceltemp = PropertyManager.getProperty("POI_EXCEl_PATH");
    excelMap.put("filenm", poiexceltemp + "\\个人消费贷款还款明细表.xls");

    excelMap.put("date", "2009-06-12");
    excelMap.put("cellname", "dataArea");
    excelMap.put("crs", crs);
    request.setCharacterEncoding("iso8859-1");
    request.getSession().setAttribute("excelMap", excelMap);

    POIUtil poi = new POIUtil();
    HSSFWorkbook wb = poi.readExcel(poiexceltemp + "\\个人消费贷款还款明细表.xls");
%>
<br>
<%=poi.getFirstDataRow(wb).getRowNum()%>
<a href="/excelByTmp">点击</a>