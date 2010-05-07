<%
	/*
	功能：支付宝代扣流程界面，实际参数的传入可以根据业务决定
	接口名称：代扣接口
	版本：2.0
	日期：2009-01-02
	作者：支付宝公司销售部技术支持团队
	联系：0571-26888888
	版权：支付宝公司
	 */


%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="org.apache.commons.logging.Log" %>

<%
//        Log log = LogFactory.getLog("index.jsp");
        Log log = LogFactory.getLog("index.jsp");

        log.debug("alipay test index.jsp");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>支付宝代扣操作</title>
		<meta http-equiv="pragma" content="no-cache">
	</head>

	<body bgcolor="#FF6600">
		<table width="100%" border="0">
			<tr>
				<th colspan="7" scope="col">
					<a href="http://www.alipay.com" target="_blank"> <img
							src="images/logo.gif" border="0" align="left" /> </a> 支付宝代扣操作流程
				</th>
			</tr>
            <tr>
				<td colspan="7">&nbsp;</td>
			</tr>
			<tr>
				<td width="46%" align="right">
					<a href="qianyue.jsp" target="_blank"><img
							src="images/qianyue.jpg" border="0" />
					</a>
				</td>
				<td width="8%" align="center">
					<img src="images/tec.jpg" width="121" height="54" border="0" />
				</td>
				<td>
					<a href="daikou.jsp" target="_blank"><img
							src="images/daikou.jpg" border="0" />
					</a>
				</td>
				<td width="8%" align="center">
					<img src="images/tec.jpg" width="121" height="54" border="0" />
				</td>
				<td>
					<a href="chaxun.jsp" target="_blank"><img
							src="images/chaxun.jpg" border="0" />
					</a>
				</td>
				<td width="8%" align="center">
					<img src="images/tec.jpg" width="121" height="54" border="0" />
				</td>
				<td width="46%">
					<a href="jieyue.jsp" target="_blank"><img
							src="images/jieyue.jpg" border="0" />
					</a>
				</td>
			</tr>

		</table>
		<h2>
			操作流程图如下：
		</h2>
		<table width="1229" height="777">
			<tr>
				<td>
					<img src="images/daikouliucheng.gif" />
				</td>
			</tr>
		</table>

	</body>
</html>
