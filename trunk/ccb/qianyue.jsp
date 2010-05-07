<%
    /*
     功能：建设银行签约接口
     接口名称：代扣接口中的签约代扣接口
     版本：
      */

//    String email = request.getParameter("email");
    String email = "abc@abc.com";
//    String out_sign_no = request.getParameter("out_sign_no");
    String out_sign_no = "ccb2010042300001";

%>
<em>
    <%@ page language="java"
             pageEncoding="GBK" %>
    <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
    <html>
    <head>
        <title>建设银行--签约代扣</title>
        <meta http-equiv="pragma" content="no-cache">
    </head>

    <body>
    <form action="/ccb/ccb_qianyue.jsp" method="post" name="qianyue"
          target="_self">
        <div id="info"
             style="color:#ffffff; font-size:14px; font-weight:bold; font-style:normal; display:none;left:15%;top:20%;position:absolute;">
            正在进入建行网银网站，请稍候...
        </div>
        <table width="100%" id="ccb" style="display:block;">

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
                <td width="15%">
                    建行个人银行账户：
                </td>
                <td>
                    <input type="hidden" name="out_sign_no" value="<%=out_sign_no%>" readonly/>
                    <input type="text" name="email" value="<%=email%>" readonly="true"/>
                </td>
            </tr>
            <tr>
                <td>
                    
                </td>
                <td>
                    <input type="submit" name="button" id="button" value="建行网银签约" onClick="qy()"/>
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
            document.getElementById("info").style.display = "block";
            document.getElementById("ccb").style.display = "none";
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