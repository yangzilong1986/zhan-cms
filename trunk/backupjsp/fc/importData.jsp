<%@ page contentType="text/html; charset=GBK" errorPage="error.jsp"%>
<%@ page import="zt.cms.report.db.*,java.util.*,javax.sql.rowset.CachedRowSet,zt.platform.user.*" %>
<%@ page import="zt.platform.form.config.*,zt.cms.pub.*,zt.platform.db.DBUtil,zt.cms.pub.SCBranch,zt.cmsi.fc.FcUpXML" %>
<%--
=============================================== 
Title: �弶�����ϱ���������
Description: �弶�����ϱ��������롣
 * @version  $Revision: 1.5 $  $Date: 2007/06/20 10:28:06 $
 * @author   weiyb
 * <p/>�޸ģ�$Author: weiyb $
=============================================== 
--%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String enddate=request.getParameter("fljzrq");
	FcUpXML upxml= new FcUpXML();
	//enddate=DBUtil.toSqlDate2(enddate);
	//��¼�û�����Ϊ�Ŵ�����(�����顢ʵ����),����Ϊʵ����,���ݵ�¼�Ļ�������ж�һ��
	UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
	if(um==null){
		response.sendRedirect("error.jsp");
	}
	String	brhid = SCUser.getBrhId(um.getUserName()); //��¼�û���������
	//ȥ���Ƿ��Ŵ������ж�
	//upxml.isXys(brhid);//�жϵ�¼�����Ƿ��Ŵ�����
	int count=0;
	String sql="select count(1) as total from scbranch where brhid='"+brhid+"' and brhlevel=2 and BRHTYPE=9";
	CachedRowSet crs=DB2_81.getRs(sql);
	while (crs.next())
	{
		count=crs.getInt("total");
	}
	crs.close();
	if(count>0)throw new Exception("�����粻���������ݣ�");
	//ConnectionManager manager=ConnectionManager.getInstance();
	CachedRowSet rs=DB2_81.getRs("select *  from UPBRHID_MAPING where branchid='"+brhid+"'");
	String jgdm="";
	while (rs.next())
	{
		jgdm=rs.getString("upbranchid");
	}
	jgdm=jgdm.trim();
	if (jgdm.equals(""))throw new Exception ("��û���������Ӧ��ϵ�����Ƚ�����Ӧ��ϵ��");
	SCBranch branch=new SCBranch();
	String str=branch.getAllSubBrh2(brhid);
	String brhlist="('"+str.replaceAll(",","','")+"','"+brhid+"')";
	//��������;
	
	if(brhid!=null&&enddate!=null)
	{
	if (!upxml.isEndDate(enddate,brhlist)) throw new Exception("��ֽ���������Ϊ�գ�����������ֽ����պ������ݣ�");
	boolean b1=upxml.impCategory(brhid,jgdm,brhlist,enddate);//���������Ϣ
	boolean b2=upxml.impDept(brhid,jgdm,brhlist,enddate);//����ͻ���Ϣ���Ŵ�����ͳ����Ϣ
	if (b1&&b2)
	{
		out.write("<script language='javascript'>{");
		out.write("alert('���ݵ������!');}");
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
    <title>�弶������������</title>
    
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
                          <td>���ʱ��:
                            <select name="fljzrq" >
                            <%=upxml.getFCDateList() %>
                            </select>
                          </td>
                          <td align="right" width="20%">
                          	<input name="button" class="query_button" type="button" onclick="submitgo(-1)" value=" �� �� " />
                            <input name="button" class="query_button" type="button" onclick="self.close()" value=" �� �� " />
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
  <td bgcolor=eeeeee align=center height=50>���ݵ����С��� </td>
</tr>
<tr>
  <td bgcolor=#cacaca align=center height=10>&nbsp;</td>
</tr>
</table>
</div>
</form>
  </body>
</html>
