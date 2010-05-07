<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/Ming.tld" prefix="ming" %>
<%@ page import="java.sql.*"%>
<%@ page import="com.ming.webreport.*"%>
<%@ page import="zt.platform.db.*"%>
<%@ page import="zt.cmsi.mydb.*"%>
<%@ page import="zt.cms.report.*"%>
<%@ include file="include.inc"%>
<%
	RepParamObject rpobj = (RepParamObject)request.getAttribute("REPPARAMOBJECT");
	String strSqlString = rpobj.getSqlString();
	String strBmNo = rpobj.getBmNo();
	String strBindFile = rpobj.getBindFile();
	String strPrintDate = rpobj.getPrintDate();
	String year = "";
	String month = "";
	String day = "";
    if(strPrintDate!=null){
    	strPrintDate = strPrintDate.trim();
		if (!strPrintDate.equals("")){
			String[] date = strPrintDate.split("-");
			year = date[0];
			month = date[1];
			day = date[2];
		}
	}



	System.out.println("1:"+strSqlString);
	System.out.println("2:"+strBmNo);
	System.out.println("3:"+strBindFile);
	System.out.println("year:"+year+"\nmonth:"+month+"\nday:"+day);
%>
<%
  Connection conn = null;
  PreparedStatement prepStmtNew =null;
  try
  {
  	  DatabaseConnection dconn= MyDB.getInstance().apGetConn();
  	  if(dconn!=null){
  	  	conn = dconn.getConnection();
  	  }

  	  if(conn!=null){
	  	prepStmtNew=conn.prepareStatement(strSqlString);
		prepStmtNew.setString(1,strBmNo);
	  }
	  if(prepStmtNew!=null){
	  	ResultSet rs = prepStmtNew.executeQuery();
        //ResultSetMetaData rsmd = rs.getMetaData();
		//int iColumnCount = rsmd.getColumnCount();
		//if(!rs.next()){

			//ResultSetMetaData rsmd = rs.getMetaData();
			//int iColumnCount = rsmd.getColumnCount();
            /*rs.moveToInsertRow();
			for(int i=1;i<=iColumnCount ;i++){
       			rs.updateString(i," ");
			}
            rs.insertRow();
            rs.moveToCurrentRow();*/
		//}
		/*ifor(int i=1;i<=iColumnCount ;i++){
        	System.out.println(i+"s:"+rs.getString(1));
		}*/
	  	MREngine engine = new MREngine(pageContext);
	  	engine.setRootPath((String)request.getAttribute("ROOTPATH"));

	  	engine.addDataSet("rs3",rs,"8859_1","GB2312");
	  	engine.bind(strBindFile);
	  }
  }
  finally
  {
  	prepStmtNew.close();
	MyDB.getInstance().apReleaseConn(0);
  }
%>
<HTML>

<HEAD>

<TITLE> 潍坊信贷报表打印 </TITLE>

</HEAD>

<BODY style="margin: 0">

<table height="100%" width="100%" border=0 cellpadding=0 cellspacing=0>

  <tr height="95%">

    <td valign="top">

      <ming:MRViewer id="MRViewer" shownow="true"  width="100%"  height="100%"/>

    </td>

  </tr>
  <tr height="5%" bgColor="lightskyblue">
	<td align=center>
		<input type="button" name="submit6" class="list_button_active" value="上一步" onClick="history.go(-1);">
		<input type=button value="关闭" onclick="window.close()	">
	</td>
  </tr>
</table>
<script language="JavaScript">
var year= '<%=year%>';
var month='<%=month%>';
var day='<%=day%>';
var obj = document.getElementById('MRViewer');
 obj.setVariable('year',year);
 obj.setVariable('month',month);
 obj.setVariable('day',day);
 obj.preview();
</script>
</BODY>

</HTML>
