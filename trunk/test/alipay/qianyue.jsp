
<%
	/*
	功能：支付宝输入页面，这个页面可以集成到商户网站，实际参数的传入可以根据业务决定
	接口名称：代扣接口中的签约代扣接口
	版本：2.0
	日期：2008-12-31
	作者：支付宝公司销售部技术支持团队
	联系：0571-26888888
	版权：支付宝公司
	 */
%>
<em><%@ page language="java" import="java.util.*"
		pageEncoding="GBK"%> <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
	<html>
		<head>
			<title>支付宝--签约代扣</title>
			<meta http-equiv="pragma" content="no-cache">
		</head>

		<body bgcolor="#FF6600">
			<form action="alipay_qianyue.jsp" method="post" name="qianyue"
				target="_blank">
				<table>
					<tr>
						<td colspan="2">
							<a href="http://www.alipay.com"><img src="images/logo.gif"
									border="0" />
							</a>
						</td>
					</tr>
					<tr>
						<td>
							代理业务编号：
						</td>
						<td>
							<input type="text" name="type_code" value="TEST100011000101"
								readonly />
							<span style="color: #FF0000;">*</span>type_code合作伙伴ID(必填),这个由客户经理申请，后台开通，可以从客户经理拿到
						</td>
					</tr>
					<tr>
						<td>
							签约e_mail：
						</td>
						<td>
							<input type="text" name="email" />
							<span style="color: #FF0000;">*</span>签约的账户是支付宝账户
						</td>
					</tr>
					<tr>
						<td>
							操作：
						</td>
						<td>
							<input type="submit" name="button" id="button" value="签约" />
						</td>
					</tr>
				</table>

			</form>
		</body>
	</html> </em>