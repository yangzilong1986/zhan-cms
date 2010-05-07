<%@ page import="zt.cms.pub.SCBranch"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="zt.cms.pub.SCUser"%>
<%@ page import="zt.platform.cachedb.ConnectionManager"%>
<%@ page import="zt.platform.db.DBUtil"%>
<%@ page import="zt.platform.form.config.SystemAttributeNames"%>
<%@ page import="zt.platform.user.UserManager"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%@ page import="zt.cms.fcsort.qydkrd.CompanyDAO" %>
<%@ page import="zt.cms.fcsort.qydkrd.CompanyInfo" %>
<%@ page errorPage="/fcsort/common/FcsortError.jsp" %>
<%@ page import="java.util.List" %>

<%--
===============================================
Title: ��ҵ�����϶�����
Description: ��ҵ�����϶�����
 * @version   $Revision: 1.8 $  $Date: 2007/06/22 00:41:46 $
 * @author   houcs
 * <p/>�޸ģ�$Author: houcs $
===============================================
--%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String currentPath = basePath + "fcsort/qydkrd/";
    pageContext.setAttribute("basePath", basePath);
    pageContext.setAttribute("currentPath", currentPath);
	request.setCharacterEncoding("GBK");
	UserManager um = (UserManager) session
			.getAttribute(SystemAttributeNames.USER_INFO_NAME);
	ConnectionManager manager = ConnectionManager.getInstance();
	//******************�������************
    String sql = "select DT from FCPRD  where INITIALIZED=1 order by SEQNO desc ";
    CachedRowSet crs = manager.getRs(sql);
   
    //********************************************
    //******************�ͻ�����***********************
    String sql2="select enutp,enudt from PTENUMINFODETL where enuid='ClientType2'";
    CachedRowSet crs2 = manager.getRs(sql2);
    //***************************************************
    //*******************��������******************
	String brhid = SCUser.getBrhId(um.getUserName().trim());
	if (request.getParameter("brhid") != null)
		brhid = request.getParameter("brhid");
	String lname3 = SCBranch.getSName(brhid);
	//***********************************************
	String lname = request.getParameter("lname2");
	if (lname == null)
		lname = lname3;
 //***********************������Ϣ**********************
   String mess = (String) request.getAttribute("mess");
	mess = (mess == null) ? "" : "<li class='error_message_li'>"
			+ mess.trim() + "</li>";
  //***************************************************
  //************************����************************
  String name=DBUtil.toDB(request.getParameter("name")==null?"":request.getParameter("name"));
  String number=request.getParameter("number")==null?"":request.getParameter("number");
  String yynumber=request.getParameter("yynumber")==null?"":request.getParameter("yynumber");
  String clienttype=request.getParameter("clienttype")==null?"":request.getParameter("clienttype");
  String clientno=request.getParameter("clientno")==null?"":request.getParameter("clientno");
  String createdate=request.getParameter("createdate");
  
  int count=Integer.parseInt(request.getParameter("count")==null?"0":request.getParameter("count"));
  
  if (createdate == null) {
        String sql3 = "select max(dt) mdt from FCPRD  where INITIALIZED=1";
        CachedRowSet crs3 = manager.getRs(sql3);
        String dt = "";
        while (crs3.next()) {
            dt = crs3.getString("mdt");
            createdate = dt;
        }
    }
  String date=createdate.substring(0,4)+"��"+createdate.substring(5,7)+"��";
  CompanyDAO dao=new CompanyDAO(request);
  dao.setBrhid(brhid);
  dao.setName(name);
  dao.setNumber(number);
  dao.setYynumber(yynumber);
  dao.setClienttype(clienttype);
  dao.setClientno(clientno);
  dao.setCreatedate(createdate);
  //*******************��ҳ���***************************************************
  if(count==0)
  {
  count=dao.getCount();//�õ���ѯ�����¼����
  dao.setMaxPage(); //���ݼ�¼��������ҳ��
  }else{
  dao.setPageCount(count);
  dao.setMaxPage();
  }
  int maxPage=dao.getMaxPage();//�õ����ҳ
  int currentPage=dao.getCurrentPage();//�õ���ǰҳ��(Ҫת����ҳ��)
  String updisabled=currentPage==1?"disabled":"";
  String downisabled=currentPage==maxPage?"disabled":"";
  int pageCount=dao.getPageCount();//������ҳ��
  int pageSize=dao.getPageSize();//ÿҳ��ʾ���� 
%>
<html>
<head>
<title>�Ŵ�����</title>
<link href="/css/platform.css" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<style type="text/css">
        .style1 {
            font-family: "����", Courier New, Arial;
            font-size: 12;
        }
        .a
        {
        text-decoration:none;
        color: #000000;
        background-color: #FFFFFF;
        }
    </style>
<script language="JavaScript" type="text/JavaScript">
  
        document.title = "��ҵ�����϶�����";
        document.focus();

        function checkLname() {
       
       
            form1.lname2.value = lname1.innerText;
            form1.submit();
        }
       
       function resets(){
      
       document.form1.name.value='';
       document.form1.number.value='';
       document.form1.yynumber.value='';
       document.form1.clienttype.value='';
     
      
       }
       function checks(a,b){
         var createdate="<%=createdate%>";
         var url="list.jsp?clientno="+a+"&brhid="+b+"&createdate="+createdate;
        
         window.open(url);
       }
        function deptrefer_click() {
            var url = "qy_view.jsp";
            var vArray;
            document.all.referValue.value = window.open(url, 0, "height=400,width=460,toolbar=no,scrollbars=yes,status");
            if (document.all.referValue.value == "undefined") {
                return;
            }
            
        }
        function linkto(a,b,c){
            window.location="list.jsp?clientno="+a+"&brhid="+b+"&createdate="+c+" target=_blank";
        }
       var menuAppear = false;
        function menuMovee() {
    if (menuAppear) {
        document.all("findDiv").style.display = "none";
        document.all("findDivHandle").src = "/images/form/button1.jpg";
        menuAppear = false;
    }
    else {
        document.all("findDiv").style.display = "";
        document.all("findDivHandle").src = "/images/form/button2.jpg";
        menuAppear = true;
    }
}
     
      function goPage(sort){
       var currentPage=document.all.currentPage.value;
       var maxPage=document.all.maxPage.value;
       var page=document.all.page.value;
         if(sort=="top"){
         document.all.currentPage.value=1;
          }
          if(sort=="bottom"){
          document.all.currentPage.value=maxPage;
          }
          if(sort=="up"){
          document.all.currentPage.value=parseInt(parseInt(currentPage)-1);
          
          }
          if(sort=="down"){
          document.all.currentPage.value=parseInt(parseInt(currentPage)+1);
          
          }
          checkLname();
          document.form1.submit();
          
     }
    </script>
<script src='/js/querybutton.js' type='text/javascript'></script>
</head>
<body background="/images/checks_02.jpg">
<form action="" name="form1" method="post" id="winform"
	onsubmit="checkLname()"><input name="referValue" type="hidden">
<input type="hidden" name="lname2">
<input type="hidden" name="count" value="<%=count%>">
<table width="100%" height="100%" border="0" cellspacing="0"
	align="center" cellpadding="0" scrolling='yes'>
	<tr>
		<td align="center" valign="middle">
		<table height="200" border="2" align="center" cellpadding="2"
			cellspacing="5" bordercolor="#006699" bgcolor="AACCEE"
			scrolling='yes'>
			<tr align="left">
				<td height="30" bgcolor="#4477AA"><img
					src="/images/form/xing1.jpg" align="absmiddle"> <font
					size="2" color="#FFFFFF"><b>��ҵ�����϶����ӡ</b></font> <img
					src="/images/form/xing1.jpg" align="absmiddle"> <font
					size="2" color="red"><%=mess%> </font></td>
			</tr>
			<tr align="center">
				<td height="100" valign="middle">
				<table width="100%" height="100%" cellspacing="0" cellpadding="0"
					border="0" scrolling='yes'>
					<tr>
						<td align="center" valign="middle">
						<table width='700' scrolling='yes'>
							<tr>
								<td>
								<table align='center' cellpadding='0' cellspacing='0' border='0'
									bgcolor='#AAAAAA' width='700'>
									<tr>
										<td height="0">
										<table id="findDiv" class='query_form_table' cellpadding='0'
											cellspacing='0' border='0' style='display:none'>
											<tr class="query_tr">
												<td class="query_td" width="80%">
												<table class='query_form_table' id='query_form_table'
													cellpadding='1' cellspacing='1' border='0'>
													<tr class="query_form_tr" nowrap>
														<td class="query_form_td" nowrap>�ͻ�����</td>
														<td class="query_form_td" nowrap><input type="text"
															name="name" 
															class="page_form_text" errInfo="�ͻ�����"> 
														</td>
														<td class="query_form_td" nowrap>֤������</td>
														<td class="query_form_td" nowrap><input type="text"
															name="number" 
															class="page_form_text" errInfo="֤������"> 
														</td>
													</tr>
													
													<tr class="query_form_tr" nowrap>
														<td class="query_form_td" nowrap>Ӫҵִ�պ�</td>
														<td class="query_form_td" nowrap><input type="text"
															name="yynumber" 
															class="page_form_text" errInfo="Ӫҵִ�պ�"> 
														</td>
														<td class="query_form_td" nowrap>��������</td>
														<td class="query_form_td" nowrap><input type="text"
															name="brhid" value="<%=brhid==null ? "":brhid %>"
															class="page_form_text" errInfo="��������"> <input
															type="button" name="nameref" value="��"
															onclick="deptrefer_click()" class="page_form_refbutton">
														</td>
													</tr>
													
													<tr class="query_form_tr" nowrap>
														<td class="query_form_td" nowrap>�ͻ�����</td>
														<td class="query_form_td" nowrap>
														<select id="clienttype" name="clienttype" 
															class="page_form_text" errInfo="�ͻ�����"> 
															<option value="0"></option>
															<%
															while(crs2.next()){
															 %>
															  <option value="<%=crs2.getString("enutp")%>">
                                                              <%=DBUtil.fromDB(crs2.getString("enudt"))%>
                                                              </option> 
															 <%
															 }
															  %>
															</select>
														</td>
														<td class="query_form_td" nowrap>��ѯʱ��</td>
														<td class="query_form_td" nowrap>
														<select id="createdate" name="createdate" 
														   
															class="page_form_text" errInfo="��ѯʱ��"> 
															<%
													 
                                                       while (crs.next()) {
                                                        %>
                                                          <option value="<%=crs.getString("DT")%>">
                                                         <%=DBUtil.fromDB(crs.getString("DT"))%>
                                                          </option>
                                                         <%
                                                         }
                                                          %>
															</select>
														</td>
													</tr>
												</table>
												</td>
												<td class="query_td" width="20%" align="center">
												<table border='0' width='80%' bgcolor='#F1F1F1'>
													<tr>
														<td nowrap valign="top"><input type="submit"
															class="query_button" name="Submit" value=" �� �� ">
														</td>
													</tr>
													<tr>
														<td nowrap valign="top"><input type="button"
															class="query_button" name="cz" onclick="resets()" value=" �� �� ">
														</td>
													</tr>
												</table>
												</td>
											</tr>
										</table>
										</td>
									</tr>
									<tr>
										<td height="0" align="center"><img id='findDivHandle'
											title='�����ѯ' onClick='menuMovee()'
											src="/images/form/button1.jpg" style='cursor:hand;'></td>
									</tr>
								</table>
								<table cellpadding='0' cellspacing='0' border='0' align="center"
									width='700'>
									<tr height="20">
										<td align="left" nowrap><span class="style1">��������:<label
											id="lname1"><%=lname == null ? "" : lname%> </label></span></td>
										<td align="right" nowrap>
										<span class="style1">��ѯʱ��:<%=date %></span>
										</td>
									</tr>
								</table>
								<table class='list_form_table' width='100%' align='center'
									id="checkTable" cellpadding='0' cellspacing='1' border='0'>
									<tr class='list_form_title_tr'>
										<td class='list_form_title_td' align="center" width="20%">�ͻ�����</td>
										<td class='list_form_title_td' align="center" width="10%">�ͻ�����</td>
										<td class='list_form_title_td' align="center" width="15%">֤������</td>
										<td class='list_form_title_td' align="center" width="10%">��������</td>
										<td class='list_form_title_td' align="center" width="15%">Ӫҵִ�պ�</td>
										<td class='list_form_title_td' align="center" width="10%">��ϸ</td>
										
									</tr>
									<%
									int i=0;
									List list=dao.getResult();
									i=list.size();
									int j=10-i;
									for(int m=0;m<list.size();m++){
									CompanyInfo info=(CompanyInfo)list.get(m);
									%>
									<tr class='list_form_tr'>
										<td nowrap class='list_form_td' align="left"><%=info.getName() %></td>
										<td nowrap class='list_form_td' align="center"><%=info.getClienttype()%></td>
										<td nowrap class='list_form_td' align="center"><%=info.getNumber() %></td>
										<td nowrap class='list_form_td' align="center"><%=info.getBrhid() %></td>
										<td nowrap class='list_form_td' align="center"><%=DBUtil.fromDB(info.getYynumber())%></td>
										<td nowrap class='list_form_td' align="center">
										<a href="#" class="a" onclick="checks(<%=info.getClientno()%>,
										<%=info.getBrhid()%>)">
										��ϸ</a></td>
									</tr>
									<%
									}
									  if(j>0){
										
											for (int k = 1; k <= j; k++) {
									%>
									<tr class='list_form_tr'>
										<td nowrap class='list_form_td' align="center"></td>
										<td nowrap class='list_form_td' align="right"></td>
										<td nowrap class='list_form_td' align="right"></td>
										<td nowrap class='list_form_td' align="right"></td>
										<td nowrap class='list_form_td' align="right"></td>
										<td nowrap class='list_form_td' align="right"></td>
										<td nowrap class='list_form_td' align="right"></td>
									</tr>
									<%
										
										}
										}
									%>
								</table>
								</td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr height="35" align="center" valign="middle">
				<td align="center">
				<table border="0" cellspacing="0" cellpadding="0" width="538">
					<tr>
						<td nowrap align="center">
						<table class='list_button_tbl'>
							<tr class="list_button_tbl_tr">
							<td class="list_form_button_td"><input type='button'
									 class='list_button_active' value='  �� ҳ ' onclick="goPage('top')"></td>
									 <td class="list_form_button_td"><input type='button'
									 class='list_button_active' value='  �� ҳ ' onclick="goPage('up')" <%=updisabled %>></td>
									 <td class="list_form_button_td"><input type='button'
									 class='list_button_active' value='  �� ҳ ' onclick="goPage('down')" <%=downisabled %>></td>
									 <td class="list_form_button_td"><input type='button'
									 class='list_button_active' value='  ĩ ҳ ' onclick="goPage('bottom')"></td>
									 <input type="hidden" name="currentPage" value="<%=currentPage %>">
									 <input type="hidden" name="maxPage" value="<%=maxPage %>">
									 <input type="hidden" name="page" value="<%=currentPage %>">
						   <td class="list_form_button_td"><input type='submit'
									name='a' class='list_button_active' value=' ˢ �� '></td>
									
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>




