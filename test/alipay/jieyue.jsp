<%
	/*
	功能：支付宝输入页面，这个页面可以集成到商户网站，实际参数的传入可以根据业务决定
	接口名称：代扣中的解除签约代扣接口
	版本：2.0
	日期：2008-12-31
	作者：支付宝公司销售部技术支持团队
	联系：0571-26888888
	版权：支付宝公司
	 */
%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>支付宝代扣--解除签约代扣</title>
		<meta http-equiv="pragma" content="no-cache">
	</head>

	<body bgcolor="#FF6600">
		<form action="alipay_jieyue.jsp" method="post" name="qianyue" target="_blank">
        	<table>
            	<tr>
                	<td colspan="2">
                    	<a href="http://www.alipay.com"><img src="images/logo.gif" border="0"/></a>
                    </td>
                </tr>
            	<tr>
                	<td>解约客户代码:</td>
                    <td><input name="customer_code" type="text" />
                    <span style="color:#FF0000;">*</span>即解约支付宝customer_code,在签约回传参数中找到
                    </td>
                </tr>
                <tr>
                	<td>
                    操作：
                    </td>
                    <td><input type="submit" name="button" id="button" value="代扣--解约" /></td>
                </tr>
                <tr>
                	<td>
                    备注：
                    </td>
                    <td>用户每签署一份type_code协议,都会得到一个唯一的客户代码，<br>如传入该参数，则忽略其他业务参数</td>
                </tr>
            </table>
		</form>
	</body>
</html>
