<%
    /*
     ���ܣ�֧��������ҳ�棬���ҳ����Լ��ɵ��̻���վ��ʵ�ʲ����Ĵ�����Ը���ҵ�����
     �ӿ����ƣ����۽ӿ��е�ǩԼ���۽ӿ�
     �汾��2.0
     ���ڣ�2008-12-31
     ���ߣ�֧������˾���۲�����֧���Ŷ�
     ��ϵ��0571-26888888
     ��Ȩ��֧������˾
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
        <title>֧����--ǩԼ����</title>
        <meta http-equiv="pragma" content="no-cache">
    </head>

    <body bgcolor="#FF6600">
    <form action="/alipay/alipay_qianyue.jsp" method="post" name="qianyue"
          target="_self">
        <div id="info" style="color:#ffffff; font-size:14px; font-weight:bold; font-style:normal; display:none;left:15%;top:20%;position:absolute;">���ڽ���֧������վ�����Ժ�...</div>
        <table width="100%" id="zfb" style="display:block;">
            <tr>
                <td colspan="2"><img src="/alipay/images/logo.gif" border="0"/>
                </td>
            </tr>
            <%--<tr>--%>
            <%--<td>--%>
            <%--����ҵ���ţ�--%>
            <%--</td>--%>
            <%--<td>--%>
            <%--<input type="hidden" name="type_code" value="XSCS100011000101" readonly/>--%>
            <%----%>
            <%--<span style="color: #FF0000;">*</span>type_code�������ID(����),����ɿͻ��������룬��̨��ͨ�����Դӿͻ������õ�--%>
            <%--</td>--%>
            <%--</tr>--%>
            <tr>
                <td width="30%">
                    ֧�����˻���
                </td>
                <td>
                    <input type="hidden" name="out_sign_no" value="<%=out_sign_no%>" readonly/>
                    <input type="text" name="email" value="<%=email%>" readonly="true"/>
                </td>
            </tr>
            <tr>
                <td>
                    ������
                </td>
                <td>
                    <input type="submit" name="button" id="button" value="ȥ֧����ǩԼ" onClick="qy()"/>
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