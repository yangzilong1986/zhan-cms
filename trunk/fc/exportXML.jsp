<%@ page contentType="text/html; charset=GBK" errorPage="error.jsp"%>
<%@ page import="zt.cms.report.db.*,java.util.*,javax.sql.rowset.CachedRowSet,zt.platform.user.*" %>
<%@ page import="zt.platform.form.config.*,zt.cms.pub.*,zt.cmsi.fc.DBUtil,zt.cms.pub.SCBranch,zt.cmsi.fc.FcUpXML" %>
<%--
=============================================== 
Title: 导出xml
Description: 导出xml。
 * @version  $Revision: 1.4 $  $Date: 2007/05/31 02:29:40 $
 * @author   weiyb
 * <p/>修改：$Author: weiyb $
=============================================== 
--%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String enddate=request.getParameter("fljzrq");
	FcUpXML upxml= new FcUpXML();
	//登录用户为县联社用户
	UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
	if(um==null){
		response.sendRedirect("error.jsp");
	}
	String	brhid = SCUser.getBrhId(um.getUserName()); //登录用户所在网点
	upxml.isXls(brhid);//判断登录网点是否县联社
	//ConnectionManager manager=ConnectionManager.getInstance();
	CachedRowSet rs=DB2_81.getRs("select *  from UPBRHID_MAPING where branchid='"+brhid+"'");
	String jgdm="";
	while (rs.next())
	{
		jgdm=rs.getString("upbranchid");
	}
	String xys[][]=upxml.getSubJgdm(jgdm);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <link rel="stylesheet" type="text/css" href="query/setup/web.css">
    <link href="css/platform.css" rel="stylesheet" type="text/css">
    <script src='js/meizzDate.js' type='text/javascript'></script>
    <title>五级分类数据导出XML</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script language="javascript">
	function submitgo(val){
	var obj=document.getElementsByName('jgdm');
	var b=false;
	for(var i=0;i<obj.length;i++)
	{
		if (obj[i].checked)
		{
			b=true;
			break;
		}
	}
	if(!b)
	{
		alert('请选择信贷部门!');
		return;
	}
	document.winform.submit();
	//divToDo();

 }
 function divToDo(){
	document.all.sending.style.display='';
	//document.all.over.style.display='';

}
	</script>
  </head>
  
  <body>
<form name="winform" method="post" action="fc/to_download.jsp">
<input name="brhid" value="<%=brhid %>" type="hidden">
<div id="aaaa" align="center">
  <table bordercolor="#999999" cellspacing="0" cellpadding="0" width="100%" align="center" bgcolor="#f1f1f1" border="1">
    <tbody>
      <tr align="middle">
        <td align="middle"><table width="95%">
          <tbody>
            <tr align="middle">
              <td align="middle"><table cellspacing="0" cellpadding="0" width="100%" align="center" border="0">
                <tbody>
                  <tr>
                    <td height="2"> </td>
                  </tr>
                  <tr>
                    <td id="detailTab" align="middle"><table cellspacing="1" cellpadding="1" width="100%" border="0">
                      <tbody>
                       <tr>
                        <td colspan=2>信贷部门选择:</td>
                        </tr>
                        <%if(xys!=null) {
                        for(int i=0;i<xys.length;i++){
                        %>
                        
                        <tr>
                        <td colspan=2>
                        <input type="radio" name="jgdm" value="<%=xys[i][0] %>" /><%=DBUtil.fromDB2(xys[i][1]) %>
                        </td>
                        </tr>
                        <%}}%>
                        <tr>
                          <td>截止日期:
                             <select name="fljzrq" >
                            <%=upxml.getFCDateList() %>
                            </select>
                          </td>
                          <td align="right" width="20%">
                          	<input name="button" class="query_button" type="button" onclick="submitgo(-1)" value=" 导 出 " />
                            <input name="button" class="query_button" type="button" onclick="self.close()" value=" 关 闭 " />
                          </td>
                        </tr>
                      </tbody>
                    </table></td>
                  </tr>
              
                </tbody>
              </table>
            </td>
            </tr>
          </tbody>
        </table>
            </td>
      </tr>
    </tbody>
  </table>
</div>
<div id=sending style="position:absolute; top:50%; left:37%; z-index:2; display:none;" align=center>
<table width="250" height="80" border="0" cellpadding="0" cellspacing="1">
<tr>
<td bgcolor=#999999 align=center height=20 width=100>&nbsp;
</td>
</tr>
<tr>
  <td bgcolor=eeeeee align=center height=50>正在处理中…… </td>
</tr>
<tr>
  <td bgcolor=#cacaca align=center height=10>&nbsp;</td>
</tr>
</table>
</div>
</form>
  </body>
</html>
