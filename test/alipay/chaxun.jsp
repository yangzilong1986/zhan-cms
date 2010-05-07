<%
	/*
	功能：支付宝输入页面，这个页面可以集成到商户网站，实际参数的传入可以根据业务决定
	接口名称：代扣中的查询代扣结果的接口
	版本：2.0
	日期：2008-12-31
	作者：支付宝公司销售部技术支持团队
	联系：0571-26888888
	版权：支付宝公司
	 */
%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@page import="com.alipay.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>支付宝代扣</title>
		<meta http-equiv="pragma" content="no-cache">
	</head>
	<body bgcolor="#FF6600">
		<form action="alipay_chaxun.jsp" method="post" name="myform">
			<table border="0" cellspacing="1" cellpadding="0" align="left"
				valign=absmiddle width=450>
                <tr>
                	<td>
                    	<a href="http://www.alipay.com"><img src="images/logo.gif" border="0"/></a>
                    </td>
                </tr>
				<tr>
					<td align=center width=50% height=120>
						<script type="text/javascript" language="javascript"
							src="js/calendar.js"></script>
						创建时间起始时间：
						<input id="gmt_create_start" name="gmt_create_start"
							onfocus="setday(this)" />*
						<br>
						创建时间起始时间：
						<input id="gmt_create_end" name="gmt_create_end"
							onfocus="setday(this)" />*
						
						<br>
						<br>
						操作：<input name="submit" type="submit" value="代扣批量查询" />
					</td>
				</tr>
			</table>
		</form>

	</body>
</html>