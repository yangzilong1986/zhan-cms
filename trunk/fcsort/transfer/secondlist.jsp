<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ page errorPage="/fcsort/common/FcsortError.jsp" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="zt.cms.fcsort.transfer.TransferPageobject" %>
<%@ page import="zt.cms.fcsort.transfer.ThirdTr"%>
<%@ page import="zt.cms.fcsort.common.FcsortUtil" %>
<%@ page import="java.text.DecimalFormat;" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String currentPath = basePath + "fcsort/transfer/";
	TransferPageobject vp = new TransferPageobject();
	vp.setRequest(request);
	FcsortUtil sd = new FcsortUtil();
	DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
	String title = vp.getTitle();

	//****************************��ҳ���****************************
	vp.setPagecount();//��������¼��
	vp.setMaxpage();//�������ҳ��
	int pagecount = vp.getPagecount();//ҳ���¼��
	int pagesize = vp.getPagesize();// ҳ���С
	int maxpage = vp.getMaxpage();// ���ҳ
	int currpage = vp.getCurrpage();// ��ǰҳ
	String updisabled =currpage==1?"disabled":"";
	String downdisabled =currpage==maxpage?"disabled":"";
	String selectYM = "<input type=\"hidden\" id=\"stype\" name=\"stype\" value=\"\">";//:sd.selectType("stype","stype",vp.getStype());
	String clientmgr="";//�ͻ�����
	String bt="";//�Ƿ�ϼ�
	String scbrhtype = SCBranch.getBrhtype(vp.getBrhid()).trim();
	String brhlevel =sd.getBrhlevel(vp.getBrhid().trim()).trim();
	if(!scbrhtype.trim().equals("9"))
	{
		clientmgr="�ͻ�����"+sd.getCLIENTMGR("CLIENTMGR","CLIENTMGR",vp.getClientmgr(),vp.getBrhid());
	}
	String  listTH = "���������,������;,��ͬ���,��Ƿ���,����,����,������,������,�ļ���̬,�弶����,��һ������,�γ�ԭ��";
	

																	
			
	List list = vp.getThirdList();								
	List listTR = new ArrayList();
	for(int i =0;i<list.size();i++)
	{	List listTd = new ArrayList();
		
		ThirdTr tr = (ThirdTr) list.get(i);	
		listTd.add(tr.getTd1());
		listTd.add(tr.getTd2());
		listTd.add(tr.getContractamt());
		listTd.add(tr.getTd5());
		
		listTd.add(tr.getTd3());
	
		listTd.add(tr.getTd4());
		
		listTd.add(tr.getTd6());
		
		listTd.add(tr.getTd7());
		listTd.add(tr.getTdF());
		
		listTd.add(tr.getTd8());
		listTd.add(tr.getTd9());	
		listTd.add(tr.getTd10());
		listTR.add(listTd);

	}
	String[] titles= new String[]{vp.getTitle()+"��ϸ","��λ���ƣ�"+vp.getScbrhname(),vp.getMTitle(),"��λ��Ԫ"};
	String[] sum =new String[]{"�ϼ�","",String.valueOf(df.format(vp.getSumD())),df.format(vp.getSumD()),"","","","","","","",""};
	HashMap printMap= new HashMap();
	printMap.put("titles",titles);
	
	printMap.put("listTH",listTH);
	printMap.put("listTR",listTR);
	printMap.put("sum",sum);
	request.getSession().setAttribute("printMap",printMap);
%>
<%--
=============================================== 
Title:�弶����Ǩ�������ѯ����ҳ��
 * @version  $Revision: 1.7 $  $Date: 2007/05/28 11:46:35 $
 * @author   zhengxin
 * <p/>�޸ģ�$Author: zhengx $   dfd
=============================================== 
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>�弶���಻���ʲ�̨��</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">

		<link rel="stylesheet" type="text/css" href="<%=basePath%>fcsort/common/web.css">
		<script language="javascript" src="<%=basePath%>query/setup/meizzDate.js"></script>
		<script language="javascript" src="<%=basePath%>fcsort/common/ajax.js"></script>
		<script language="javascript" src="<%=basePath%>fcsort/common/common.js"></script>
		<script language="javascript" src="transfer.js"></script>
		<script type="text/javascript">
			var basePath="<%=basePath %>";
			var currentPath ="<%=currentPath %>";
			function print()
			{
				var printurl=basePath+"/servlet/ExportExcel";
				window.location=printurl;
				
			}
			function print()
			{
				var printurl=basePath+"/servlet/ExportExcel";
				window.location=printurl;
				
			}
		</script>
	</head>
	<body background="/images/checks_02.jpg">
		<form action="<%=currentPath%>secondlist.jsp" name="listfrom">
			<div id=aaaa align="center">
				<TABLE borderColor=#999999 cellSpacing=0 cellPadding=0 align=center
					border=1 width=100% bgcolor=#f1f1f1>
					<TBODY>
						<TR Align=center>
							<TD align="center">
								<TABLE width=95%>
									<TBODY>
										<TR align="center">
											<TD align="center">
												<table width=100% border=0 align="center" cellPadding=0
													cellSpacing=0>
													<TR>
														<TD height=2>
															&nbsp;

														</TD>
													</TR>
													<TR>
														<td id=detailTab align="center">
															<div class=head align="right">
																<!-- ����ҳ����� -->
																<input type="hidden" name="sqlpartname" value="<%=vp.getSqlpartname() %>">
																<input type="hidden" name="sqlpartvalue" value="<%=vp.getSqlpartvalue() %>">
																<input type="hidden" id="brhid" name="brhid" value="<%=vp.getBrhid() %>">
																<input type="hidden" id="QUERYID" name="QUERYID" value="<%=vp.getQueryid() %>">
																<input type="button" value=" �� �� " class="button"onclick="document.listfrom.submit();">
																&nbsp;&nbsp;
																<input type="reset" value=" �� �� " class="button" >
																&nbsp;&nbsp;
																<input type=button class=button value=" �� ӡ " onclick="print();">
																&nbsp;&nbsp;
																<input type="button" class="button" value=" �� �� "onclick="self.close()">
																&nbsp;
															</div>
															<br>
															<table class="table" cellSpacing=1 cellPadding=1
																width=100% border=0>
																<tr class=head>
																	<td>
																		
																		��λ����:<%=vp.getScbrhname() %>
																
																		&nbsp; ��ѯʱ�㣺
																		<%=sd.setcreadate("creadate", "creadate", vp.getCreadate())%>
																		&nbsp;
																				<%=clientmgr %>	
																		&nbsp;&nbsp;������գ�
																		<input class='input' type=text id="startdate"
																			name="startdate" size=10
																			value="<%=vp.getStartdate() %>">
																		<input type='button' value='��' class='button'
																			onclick='setday(this,document.getElementById("startdate"))'>
																		��
																		<input class='input' type=text id="enddate"
																			name="enddate" size=10 value="<%=vp.getEnddate() %>">
																		<input type='button' value='��' class='button'
																			onclick='setday(this,document.getElementById("enddate"))'>
																		���ʴ������:
																		<input type='text' size='3' name='sartbal'
																			class='input' value="<%=vp.getSartbal() %>">
																		��
																		<input type='text' size='3' name='endbal'
																			class='input' value="<%=vp.getEndbal() %>">
																		��Ԫ
																	</td>
																</tr>
															</table>
														</td>
													</TR>
													<TR>
														<TD colspan=2 height=2>
															&nbsp;
														</TD>
													</TR>
											</TABLE><font size="4"><%=vp.getTitle() %></font>
											<div id='showTable' align=center width=100%>
												<div class=caption align=center>
													
													<font class="title"><%=vp.getMTitle() %></font>
												</div>
													<div align=center>
														<table cellSpacing=0 cellPadding=0 width=100% border=0>
															<tr class=title align="center">

																<td align=center class=title>

																</td>

															</tr>
															<tr class=title>
																<td class=title>
																	Ǩ������:<%=vp.getLefttitle() %>
																	
																</td>

																<td align=right class=title>
																	��λ��Ԫ
																</td>
															</tr>
														</table>
													</div>
													<table class=table cellSpacing=1 cellPadding=1 width=100%
														border=0 align=center>
														<TBODY>
															<tr>
																<td valign="bottom" class="head">

																	���������
																</td>
																<td align="center" valign="bottom" class="head">
																	������;
																</td>
																<td align="center" valign="bottom" class="head">
																	��ͬ���
																</td>
																<td align="center" valign="bottom" class="head">
																	��Ƿ���
																</td>
																<td align="center" valign="bottom" class="head">
																	����
																</td>
																<td align="center" valign="bottom" class="head">
																	����
																</td>
																<td align="center" valign="bottom" class="head">
																	������
																</td>
																<td align="center" valign="bottom" class="head">
																	������
																</td>
																<td align="center" valign="bottom" class="head">
																	�ļ���̬
																</td>
																<td align="center" valign="bottom" class="head">
																	���弶����
																</td>
																<td align="center" valign="bottom" class="head">
																	��һ������
																</td>
																<td align="center" valign="bottom" class="head">
																	�γ�ԭ��
																</td>
															</tr>

															<%
																
																int margin= pagesize-list.size();
																for (int i = 0; i < list.size(); i++) {
																	ThirdTr tr = (ThirdTr) list.get(i);
															%>
															<tr bgcolor=#FFFFFF
																onclick="showLoanInfo('<%=tr.getTd0() %>');"
																onmouseout="mOut(this);" onmouseover="mOvr(this);"
																class=data>
																<td class=data>
																	<%=tr.getTd1()%>

																</td>
																<td class=data align=right>
																	<%=tr.getTd2()%>


																</td>
																<td class=data align=right>
																	<%=tr.getContractamt()%>


																</td>
																<td class=data align="right">
																	<%=tr.getTd5()%>


																</td>
																<td class=data align=center>

																	<%=tr.getTd3()%>

																</td>

																<td class=data align="right">
																	<%=tr.getTd4()%>


																</td>

																<td class=data align="center">
																	<%=tr.getTd6()%>


																</td>
																<td class=data align="center">
																	<%=tr.getTd7()%>


																</td>
																<td class=data align="center">
																	<%=tr.getTdF()%>


																</td>
																<td class=data align="center">
																<%=tr.getTd8()%>
																</td>
																<td class=data>
																	<%=tr.getTd9()%>
																</td>
																<td class=data>
																	<%=tr.getTd10()%>


																</td>

															</tr>
															<%
															}
															%>
															
															<%
															if(margin>0)
															{
																for(int i=0;i<margin;i++)
																{
																	 %>
																	 <tr bgcolor=#FFFFFF class=data>
																	 <%
																	 for(int j=0;j<12;j++)
																	 {
																	 %>
																	 <td class=data>&nbsp;</td>
																	 <% 
																	 }
																	  %>
																	 </tr>
																	 <% 
																}
															}
																
															 %>
															<tr class=head>
																<td align=center>
																	�ϼ�
																</td>
																<td align=right>
																	&nbsp;
																</td>
																<td align=right>
																	&nbsp;
																
																<%=df.format(vp.getSumD()) %>
																</td>
																<td align=right>
																	&nbsp;
																
																<%=df.format(vp.getSumE()) %>
																</td>
																<td align=right>
																	&nbsp;
																
																</td>
																<td align=right>
																	&nbsp;
																</td>
																<td align=right>
																	&nbsp;
																</td>
																<td align=right>
																	&nbsp;
																</td>
																<td align=right>
																	&nbsp;
																</td>
																<td align=right>
																	&nbsp;
																</td>
																<td align=right>
																	&nbsp;
																</td>
																<td align=right>
																	&nbsp;
																</td>

															</tr>
														</TBODY>
													</TABLE>
													<table cellSpacing=0 cellPadding=0 width=100% border=0
														class=head>
														<tr>
															<td>
																ÿҳ
																<input type='text' name="pagesize" size='3'value="<%=pagesize %>" class='input'>
																�� | ��<%=maxpage %>ҳ<%=pagecount%>����¼ &nbsp;
																<input type='button' value='�� ҳ'onClick="goPage('top');" class='button'>
																&nbsp;
																<input type='button' value='�� ҳ' onClick='goPage("up");' class='button' <%=updisabled %> />
																&nbsp;
																<input type='button' value='�� ҳ' onClick='goPage("down");' class='button' <%=downdisabled %> />
																	
																&nbsp;
																<input type='button' value='ĩ ҳ' onClick='goPage("bottom");' class='button'>
																<input type="hidden" name="maxpage" value="<%=maxpage %>">	
																&nbsp; ��
																<input type="text" name="currpage" value="<%=currpage %>" class="input" size="3">
																<input type="hidden" name="pageer"  value="<%=currpage %>">
																ҳ<input type='button' value='ȷ��' onclick='goPage("gopage");' class='button'>
															</td>
															<td align=right>
																<input type="button" value="���س�ʼҳ" class="button" onclick="backStartList();">	&nbsp;
																<input type="button" value="�����ϼ�" class="button" onclick=" backfistList();">&nbsp;
																<input type='button' value='������ҳ' onclick='history.back();' class='button'>
																&nbsp;
															</td>
														</tr>
													</table>

												</div>
											</TD>
										</TR>
									</TBODY>
								</TABLE>
								<br>
							</TD>
						</TR>
					</TBODY>
				</TABLE>

			</div>
			<div id=bbbb>
			</div>
			<div id=over
				style="position:absolute; top:0; left:0; z-index:1; display:none;"
				width=100% height=700>
				<table width=100% height=700>
					<tr>
						<td>
						</td>
					</tr>
				</table>
			</div>
			<div id=sending
				style="position:absolute; top:50%; left:37%; z-index:2; display:none;"
				align=center>
				<table width="250" height="80" border="0" cellpadding="0"
					cellspacing="1">
					<tr>
						<td bgcolor=#999999 align=center height=20 width=100>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td bgcolor=eeeeee align=center height=50>
							���ڴ����С���
						</td>
					</tr>
					<tr>
						<td bgcolor=#cacaca align=center height=10>
							&nbsp;
						</td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>
