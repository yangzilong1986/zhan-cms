<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>

<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%@ page import="zt.cms.fcsort.swear.SwearPageobject" %>
<%@ page import="zt.cms.fcsort.swear.*" %>
<%@ page import="zt.cms.fcsort.common.FcsortUtil" %>
<%@ page import="java.text.DecimalFormat;" %>
<%--
=============================================== 
Title:�弶���಻��̨�ʰ�������ʽ��ѯ����ҳ��
 * @version  $Revision: 1.13 $  $Date: 2007/05/31 03:13:56 $
 * @author   zhengxin
 * <p/>�޸ģ�$Author: zhengx $ 
=============================================== 
--%>
<%
	String path = request.getContextPath();
	String basePath= request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String currentPath = basePath + "fcsort/swear/";
	SwearPageobject vp =new SwearPageobject();
	
	vp.setRequest(request);
	FcsortUtil sd = new FcsortUtil();
	DecimalFormat df=new DecimalFormat("###,###,###,##0.00");
	DecimalFormat df2=new DecimalFormat("###,###,###,##0");
	String scbrhtype = SCBranch.getBrhtype(vp.getBrhid()).trim();
	String brhlevel =sd.getBrhlevel(vp.getBrhid().trim()).trim();
	String bt="";
	String clientmgr="";
	if(!scbrhtype.trim().equals("9"))
	{
		clientmgr="�ͻ�����"+sd.getCLIENTMGR("CLIENTMGR","CLIENTMGR",vp.getClientmgr(),vp.getBrhid());
	}

	String selectYM = "<input type=\"hidden\" id=\"stype\" name=\"stype\" value=\"\">";//vp.getQueryid().endsWith("FCBMAIN")?"<input type=\"hidden\" id=\"stype\" name=\"stype\" value=\"\">":sd.selectType("stype","stype",vp.getStype());
		String  listTH = "���ʽ,�������,�����,��ͬ���,��Ƿ���,�μ�,����,��ʧ";


	List secondlist = vp.getSecondList();								
	List listTR = new ArrayList();
	for(int i =0;i<secondlist.size();i++)
	{	List listTd = new ArrayList();
		SecondTR tr=(SecondTR)secondlist.get(i);	
		listTd.add(tr.getTd1());
		listTd.add(tr.getTd2());
		listTd.add(tr.getTd3());
	
		listTd.add(tr.getTd8());
		
		listTd.add(tr.getTd4());
	
		listTd.add(tr.getTd5());
		
		listTd.add(tr.getTd6());
		
		listTd.add(tr.getTd7());
		listTR.add(listTd);
	}
	String[] titles= new String[]{vp.getTitle()+"����","��λ���ƣ�"+vp.getScbrhname(),vp.getMTitle(),"��λ����Ԫ"};
	String[] sum =new String[]{"�ϼ�",String.valueOf(vp.getSumA()),String.valueOf(vp.getSumB()),df.format(vp.getSumG()),df.format(vp.getSumC()),df.format(vp.getSumD()),df.format(vp.getSumE()),df.format(vp.getSumF())};
	HashMap printMap= new HashMap();
	printMap.put("titles",titles);
	
	printMap.put("listTH",listTH);
	printMap.put("listTR",listTR);
	printMap.put("sum",sum);
	request.getSession().setAttribute("printMap",printMap);
	String leibie ="���";
	if(vp.getQueryid()!=null&&vp.getQueryid().trim().equals("FCBMAIN"))
	{
		leibie="�������";
	}
	if(vp.getQueryid()!=null&&vp.getQueryid().trim().equals("BYVOUCH"))
	{
		leibie="������ʽ";
	}
	if(vp.getQueryid()!=null&&vp.getQueryid().trim().equals("BYWAY"))
	{
		leibie="����Ͷ��";
	}
	if(vp.getQueryid()!=null&&vp.getQueryid().trim().equals("BADREASON"))
	{
		leibie="�γ�ԭ��";
	}
	if(vp.getQueryid()!=null&&vp.getQueryid().trim().equals("BYTERM"))
	{
		leibie="��������";
	}

	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>�弶���಻���ʲ�</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css" href="<%=basePath%>fcsort/common/web.css">
		<script language="javascript" src="<%=basePath%>query/setup/meizzDate.js"></script>
		<script language="javascript" src="<%=basePath%>fcsort/common/ajax.js"></script>
		<script language="javascript" src="<%=basePath%>fcsort/common/common.js"></script>
		<script language="javascript" src="swear.js"></script>
		<script type="text/javascript">
			var basePath="<%=basePath %>";
			var currentPath ="<%=currentPath %>";
			function print()
			{
				var printurl=basePath+"/servlet/ExportExcel";
				window.location=printurl;
				
			}
			
		</script>
	</head>
	<body background="/images/checks_02.jpg" >
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
															<input type="hidden" id="brhid" name="brhid" value="<%=vp.getBrhid() %>">
															<input type="hidden" id="QUERYID" name="QUERYID" value="<%=vp.getQueryid() %>">
													
															&nbsp;&nbsp;&nbsp;&nbsp;
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
														<table class="table" cellSpacing=1 cellPadding=1 width=100% border=0>
																
															<tr class=head>
					
																<td>
																&nbsp;<!-- ������� -->
																<%=selectYM %>
																&nbsp;
																��ѯʱ�㣺	
																<%=sd.setcreadate("creadate","creadate",vp.getCreadate()) %>
																&nbsp;
																<%=clientmgr %>
																	
																&nbsp;&nbsp;������գ�
																<input class='input' type=text id="startdate" name="startdate"size=10 value="<%=vp.getStartdate() %>">
																<input type='button' value='��' class='button'onclick='setday(this,document.getElementById("startdate"))'>
																��
																<input class='input' type=text id="enddate" name="enddate"size=10 value="<%=vp.getEnddate() %>">
																<input type='button' value='��' class='button'onclick='setday(this,document.getElementById("enddate"))'>
																���ʴ������:
																<input type='text' size='3' name='sartbal' class='input' value="<%=vp.getSartbal() %>">
																��
																<input type='text' size='3' name='endbal' class='input' value="<%=vp.getEndbal() %>">
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
															
															<td align=center class=title >
																
															</td>
															
														</tr>
														<tr class=title>
															<td  class=title>
																�������ƣ�<%=vp.getScbrhname() %>&nbsp;
															</td>
									
															<td align=right  class=title>
																��λ����Ԫ
															</td>
														</tr>
													</table>
												</div>
												<table class=table cellSpacing=1 cellPadding=1 width=100%
													border=0 align=center>
													<TBODY>
														<tr>
															<td rowspan="2" align="center" valign="bottom"class="head">
															<%=leibie %>
															</td>
															<td rowspan="2" align="center" valign="bottom"class="head">
															�������
															
															</td>
															<td rowspan="2" align="center" valign="bottom"class="head">
															�����
															</td>
															<td rowspan="2" align="center" valign="bottom"class="head">
															��ͬ���
															</td>
															<td rowspan="2" align="center" valign="bottom"class="head">
															��Ƿ���
															</td>
															<td colspan="3" align="center" valign="bottom"class="head">
																����
															</td>
														</tr>
														<tr>
															<td align="center" valign="bottom" class="head">
																�μ�
															</td>
															<td align="center" valign="bottom" class="head">
																����
															</td>
															<td align="center" valign="bottom" class="head">
																��ʧ
															</td>
														</tr>
											
    													<%
    														
    														
    														for(int i =0;i<secondlist.size();i++)
    														{
    															SecondTR tr=(SecondTR)secondlist.get(i);
    															
    															if(brhlevel.trim().equals("1")||brhlevel.trim().equals("2"))
    															{
    																bt="A";
    															}
    															else if(scbrhtype.trim().equals("9"))
    															{
    																bt="B";
    															}
    															else
    															{
    																bt="C";
    															}
    															double[] xf = new double[7];
    														
    															xf[0]+=Double.valueOf(tr.getTd2()).doubleValue();
    															xf[1]+=Double.valueOf(tr.getTd3()).doubleValue();
    															xf[2]+=Double.valueOf(tr.getTd8()).doubleValue();
    															
    															xf[3]+=Double.valueOf(tr.getTd4()).doubleValue();
    															xf[4]+=Double.valueOf(tr.getTd5()).doubleValue();
    															xf[5]+=Double.valueOf(tr.getTd6()).doubleValue();
    															xf[6]+=Double.valueOf(tr.getTd7()).doubleValue();
    															
    															
    													%>
    													<tr bgcolor=#FFFFFF onclick="tothirdlist('<%=bt%>','<%=tr.getSqlpartname().trim() %>','<%=tr.getSqlpartvalue().trim() %>');" onmouseout="mOut(this);" onmouseover="mOvr(this);" class=data >
    															<td class=data >
																	&nbsp;<%=tr.getTd1() %>
																	
																</td>
																<td class=data align=right>
																&nbsp;<%=tr.getTd2() %>
																	
																	
																</td>
																		
																<td class=data align=right>
												
																	&nbsp;<%=tr.getTd3() %>
																	
																</td>
																<td class=data align="right">
																	&nbsp;<%=df.format(Double.valueOf(tr.getTd8()).doubleValue()) %>
																	
																
																</td>
																
																<td class=data align="right">
																		&nbsp;<%=df.format(Double.valueOf(tr.getTd4()).doubleValue()) %>
																	
																
																</td>
																<td class=data align="right">
																&nbsp;<%=df.format(Double.valueOf(tr.getTd5()).doubleValue()) %>
																	
																	
																</td>
																<td class=data align="right">
																&nbsp;<%=df.format(Double.valueOf(tr.getTd6()).doubleValue()) %>
																	
																	
																</td>
																<td class=data align="right">
																	&nbsp;<%=df.format(Double.valueOf(tr.getTd7()).doubleValue()) %>
																	
																
																</td>

															</tr>
															<%if(tr.getSqlpartname().trim().equals("LoanWay")&&(tr.getSqlpartvalue().trim().equals("199")||tr.getSqlpartvalue().trim().equals("299")||tr.getSqlpartvalue().trim().equals("399")))
															{
																String sqlvalue ="";
																if(tr.getSqlpartvalue().trim().equals("199")) sqlvalue="LOANWAY199";
																if(tr.getSqlpartvalue().trim().equals("299")) sqlvalue="LOANWAY299";
																if(tr.getSqlpartvalue().trim().equals("399")) sqlvalue="LOANWAY399";
																
																%>
																<tr class=head onclick="tothirdlist('<%=bt%>','<%=tr.getSqlpartname().trim() %>','<%=sqlvalue %>');" onmouseout="mOut(this);" onmouseover="mOvr(this);">
    															<td  >
																	<%if(tr.getSqlpartvalue().trim().equals("199")){ %>����ҵ����ϼ�<% }%>	
																	<%if(tr.getSqlpartvalue().trim().equals("299")){ %>��Ȼ��һ�����ϼ�<% }%>	
																	<%if(tr.getSqlpartvalue().trim().equals("399")){ %>��Ȼ����������ϼ�<% }%>	
																</td>
																<td  align=right>
																&nbsp;
																	<%=df2.format(xf[0]) %>	
																</td>		
																<td  align=right>
																	<%=df2.format(xf[1]) %>	
																</td>
																<td  align="right">
																	<%=df.format(xf[2]) %>
																</td>
																
																<td  align="right">
																	<%=df.format(xf[3]) %>
																</td>
																<td  align="right">
																<%=df.format(xf[4]) %>
																</td>
																<td  align="right">
																<%=df.format(xf[5]) %>
																	
																</td>
																<td  align="right">
																	<%=df.format(xf[6]) %>
																</td>

															</tr>
																<%
															} 
															%>
															
    													<% 
    													xf[0]=xf[1]=xf[2]=xf[3]=xf[4]=xf[5]=xf[6]=0.00;
    													} %>
														<tr class=head onclick="tothirdlist('<%=bt %>','sum','0');"  onmouseout="mOut(this);" onmouseover="mOvr(this);" title="�������<%=vp.getScbrhname() %>��ϸͳ��ҳ��" >
															<td align=center>
																�ϼ�
															</td>
													
															<td align=right>
																&nbsp;<%=vp.getSumA() %>
															</td>
													
															<td align=right>
																&nbsp;<%=vp.getSumB() %>
															</td>
															<td align=right>
																&nbsp;<%=df.format(vp.getSumG()) %>
															</td>
															<td align=right>
																&nbsp;<%=df.format(vp.getSumC()) %>
															</td>
													
															<td align=right>
																&nbsp;<%=df.format(vp.getSumD()) %>
															</td>
															<td align=right>
																&nbsp;<%=df.format(vp.getSumE()) %>
															</td>
													
															<td align=right>
																&nbsp;<%=df.format(vp.getSumF()) %>
															</td>
							
														</tr>
													</TBODY>
												</TABLE>
												<table cellSpacing=0 cellPadding=0 width=100% border=0 class=head>
													<tr>

													
														<td align=right>
														<input type="button" value="���س�ʼҳ" class="button" onclick="backStartList();">&nbsp;
														<input type="button" value="�����ϼ�" class="button" onclick=" backfistList();">&nbsp;
														<input type='button' value='������ҳ'onclick='history.back();' class='button'>
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
		<div id=over style="position:absolute; top:0; left:0; z-index:1; display:none;"
			width=100% height=700>
			<table width=100% height=700>
				<tr>
					<td>
					</td>
				</tr>
			</table>
		</div>
		<div id=sending style="position:absolute; top:50%; left:37%; z-index:2; display:none;" align=center>
			<table width="250" height="80" border="0" cellpadding="0"cellspacing="1">
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
