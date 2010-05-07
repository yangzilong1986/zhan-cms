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

    String email = request.getParameter("email");
    String out_sign_no = request.getParameter("out_sign_no");

%>
<em>
    <%@ page language="java"
             pageEncoding="GBK" %>
    <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
    <html>
    <head>
        <title>支付宝--签约代扣</title>
        <meta http-equiv="pragma" content="no-cache">
    </head>

    <body bgcolor="#FF6600">
    <form action="/alipay/alipay_qianyue.jsp" method="post" name="qianyue"
          target="_self">
        <div id="info" style="color:#ffffff; font-size:14px; font-weight:bold; font-style:normal; display:none;left:15%;top:20%;position:absolute;">正在进入支付宝网站，请稍候...</div>
        <table width="100%" id="zfb" style="display:block;">
            <tr>
                <td colspan="2"><img src="/alipay/images/logo.gif" border="0"/>
                </td>
            </tr>
            <%--<tr>--%>
            <%--<td>--%>
            <%--代理业务编号：--%>
            <%--</td>--%>
            <%--<td>--%>
            <%--<input type="hidden" name="type_code" value="XSCS100011000101" readonly/>--%>
            <%----%>
            <%--<span style="color: #FF0000;">*</span>type_code合作伙伴ID(必填),这个由客户经理申请，后台开通，可以从客户经理拿到--%>
            <%--</td>--%>
            <%--</tr>--%>
            <tr>
                <td width="30%">
                    支付宝账户：
                </td>
                <td>
                    <input type="hidden" name="out_sign_no" value="<%=out_sign_no%>" readonly/>
                    <input type="text" name="email" value="<%=email%>" readonly="true"/>
                </td>
            </tr>
            <tr>
                <td>
                    操作：
                </td>
                <td>
                    <input type="submit" name="button" id="button" value="去支付宝签约" onClick="qy()"/>
                </td>
            </tr>
        </table>

    </form>
    <script type="text/javascript">
        document.body.onload = function() {
            //document.forms[0].submit();
            //window.close();
        }


        <!--
        function qy() {
            document.getElementById("info").style.display="block";
            document.getElementById("zfb").style.display="none";
            self.moveTo(0, 0);
            self.resizeTo(screen.availWidth, screen.availHeight);
            self.outerWidth = screen.availWidth;
            self.outerHeight = screen.availHeight;
        }
        //-->
    </script>
    </body>
    </html>
</em>