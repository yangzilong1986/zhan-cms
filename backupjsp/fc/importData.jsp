<%@ page contentType="text/html; charset=GBK" errorPage="error.jsp"%>
<%@ page import="zt.cms.report.db.*,java.util.*,javax.sql.rowset.CachedRowSet,zt.platform.user.*" %>
<%@ page import="zt.platform.form.config.*,zt.cms.pub.*,zt.platform.db.DBUtil,zt.cms.pub.SCBranch,zt.cmsi.fc.FcUpXML" %>
<%--
=============================================== 
Title: 五级分类上报数据载入
Description: 五级分类上报数据载入。
 * @version  $Revision: 1.5 $  $Date: 2007/06/20 10:28:06 $
 * @author   weiyb
 * <p/>修改：$Author: weiyb $
=============================================== 
--%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String enddate=request.getParameter("fljzrq");
	FcUpXML upxml= new FcUpXML();
	//enddate=DBUtil.toSqlDate2(enddate);
	//登录用户可能为信贷部门(包括虚、实网点),可能为实网点,根据登录的机构编号判断一下
	UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
	if(um==null){
		response.sendRedirect("error.jsp");
	}
	String	brhid = SCUser.getBrhId(um.getUserName()); //登录用户所在网点
	//去掉是否信贷部门判断
	//upxml.isXys(brhid);//判断登录网点是否信贷部门
	int count=0;
	String sql="select count(1) as total from scbranch where brhid='"+brhid+"' and brhlevel=2 and BRHTYPE=9";
	CachedRowSet crs=DB2_81.getRs(sql);
	while (crs.next())
	{
		count=crs.getInt("total");
	}
	crs.close();
	if(count>0)throw new Exception("县联社不能载入数据！");
	//ConnectionManager manager=ConnectionManager.getInstance();
	CachedRowSet rs=DB2_81.getRs("select *  from UPBRHID_MAPING where branchid='"+brhid+"'");
	String jgdm="";
	while (rs.next())
	{
		jgdm=rs.getString("upbranchid");
	}
	jgdm=jgdm.trim();
	if (jgdm.equals(""))throw new Exception ("还没建立网点对应关系，请先建立对应关系！");
	SCBranch branch=new SCBranch();
	String str=branch.getAllSubBrh2(brhid);
	String brhlist="('"+str.replaceAll(",","','")+"','"+brhid+"')";
	//载入数据;
	
	if(brhid!=null&&enddate!=null)
	{
	if (!upxml.isEndDate(enddate,brhlist)) throw new Exception("清分截至日数据为空，请在做完清分截至日后导入数据！");
	boolean b1=upxml.impCategory(brhid,jgdm,brhlist,enddate);//导入分类信息
	boolean b2=upxml.impDept(brhid,jgdm,brhlist,enddate);//导入客户信息和信贷部门统计信息
	if (b1&&b2)
	{
		out.write("<script language='javascript'>{");
		out.write("alert('数据导入完成!');}");
		out.write("</script>");
	}
		}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <link rel="stylesheet" type="text/css" href="query/setup/web.css">
    <link href="css/platform.css" rel="stylesheet" type="text/css">
    <script src='js/meizzDate.js' type='text/javascript'></script>
    <title>五级分类数据载入</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script language="javascript">
	function submitgo(val){
	document.winform.submit();
	divToDo();

 }
 function divToDo(){
	document.all.sending.style.display='';
	//document.all.over.style.display='';

}
	</script>
  </head>
  
  <body>
  <form name="winform" method="post" >
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
                          <td>清分时点:
                            <select name="fljzrq" >
                            <%=upxml.getFCDateList() %>
                            </select>
                          </td>
                          <td align="right" width="20%">
                          	<input name="button" class="query_button" type="button" onclick="submitgo(-1)" value=" 载 入 " />
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
  <td bgcolor=eeeeee align=center height=50>数据导入中…… </td>
</tr>
<tr>
  <td bgcolor=#cacaca align=center height=10>&nbsp;</td>
</tr>
</table>
</div>
</form>
  </body>
</html>
