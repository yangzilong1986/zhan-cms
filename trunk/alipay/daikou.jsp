<%
	/*
	功能：支付宝输入页面，这个页面可以集成到商户网站，实际参数的传入可以根据业务决定
	接口名称：代扣接口
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
		<title>支付宝服务--代扣</title>
		<meta http-equiv="pragma" content="no-cache">
	</head>

	<body bgcolor="#FF6600">
		<form action="alipay_daikou.jsp" method="post" name="qianyue" target="_blank">
        	<table>
            	<tr>
                	<td colspan="2">
                    	<a href="http://www.alipay.com"><img src="/alipay/images/logo.gif" border="0"/></a>
                    </td>
                </tr>
                <%
					UtilDate date = new UtilDate();
					%>
                <tr>
                	<td>代扣订单号：
                    </td>
                    <td><input type="text" name="orderNum" value="<%=date.getOrderNum()%>"/>
                    </td>
                </tr>
            	<tr>
                	<td>商品名称：
                    </td>
                    <td><input type="text" name="goodName" value="支付宝测试代扣接口"/>
                    </td>
                </tr>
                <tr>
                	<td>商户订单创建时间：
                    </td>
                    <td><input type="text" name="ordeCreateTime" value="<%=date.getDateFormatter()%>"/>
                    </td>
                </tr>
                <tr>
                	<td>代扣金额：
                    </td>
                    <td><input type="text" name="amount" value="0.01"/>
                    </td>
                </tr>
                <tr>
                	<td>代扣支付宝客户签约代码：
                    </td>
                    <td><input type="text" name="customer_code"/>
                    </td>
                </tr>
                <tr>
                	<td>操作：
                    </td>
                	<td>
                    	<input type="submit" name="button" id="button" value="代扣" />
                    </td>
                </tr>
            </table>
		</form>
	</body>
</html>
