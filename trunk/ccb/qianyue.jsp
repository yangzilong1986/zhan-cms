<%
    /*
     ���ܣ���������ǩԼ�ӿ�
     �ӿ����ƣ����۽ӿ��е�ǩԼ���۽ӿ�
     �汾��
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
        <title>��������--ǩԼ����</title>
        <meta http-equiv="pragma" content="no-cache">
    </head>

    <body>
    <form action="/ccb/ccb_qianyue.jsp" method="post" name="qianyue"
          target="_self">
        <div id="info"
             style="color:#ffffff; font-size:14px; font-weight:bold; font-style:normal; display:none;left:15%;top:20%;position:absolute;">
            ���ڽ��뽨��������վ�����Ժ�...
        </div>
        <table width="100%" id="ccb" style="display:block;">

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
                <td width="15%">
                    ���и��������˻���
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
                    <input type="submit" name="button" id="button" value="��������ǩԼ" onClick="qy()"/>
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