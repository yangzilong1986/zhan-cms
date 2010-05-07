<%@ page import="zt.platform.db.DatabaseConnection" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.Statement" %>
<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
    <title>测试附件</title>
    <BASE target=_self>
    <link href="../css/platform.css" rel="stylesheet" type="text/css">
    <style type="text/css">
        <!--
        body {
            margin: 15px;
        }

        -->
    </style>
    <style type="text/css">
        /*附件 开始*/
        a.files {
            overflow: hidden;
            display: -moz-inline-box;
            display: inline-block;
            background: url(/images/F1.gif);
        }

        span.del_file {
            overflow: hidden;
            display: -moz-inline-box;
            display: inline-block;
            background: url(/images/F2.gif);
        }

        a.files {
            width: 70px;
            height: 18px;
        #vertical-align : middle;
        }

        a.files:hover {
            background-position: 0 -1px;
        }

        a.files input {
            margin-left: -152px;
            filter: alpha(opacity = 0);
            opacity: 0;
        }

        span.del_file {
            width: 16px;
            height: 14px;
            background-position: 0 -51px;
            vertical-align: middle;
        #vertical-align : sub;
            margin-left: 2px;
            cursor: pointer;
        }

        /*附件 结束*/
    </style>
    <script type="text/javascript">
        var File = {
            /*
             模拟126的附件添加特效
             -------------------------------------------------------
             **num
             为元素的下标
             **count
             为元素的个数
             **name
             为元素的名字和ID的前段（元素的实际名称是前段加下标）
             **status
             为状态的ID
             **form
             为表单的ID
             -------------------------------------------------------
             */
            num : 1, count : 0 ,maxfile : 8

            , name : 'file', status : 'file', form : 'form'

            , urls : {}

            , add : function (file) {
                //添加附件
                if (this.urls[file.value]) {
                    alert('此文件已存在');
                    return false;
                }
                if (this.count - (-document.getElementById("hasaddfiles").value) >= this.maxfile) {
                    alert('您最多只能上传'+this.maxfile+'个附件');
                    return false;
                }
                if (File.size(file) > (2 * 1024 * 1024)) {
                    alert("单个文件大小不能超过2M！");
                    return false;
                }


                var a = file.parentNode, status = document.getElementById(this.status);

                this.urls[file.value] = 1;

                document.getElementById(this.form).appendChild(file);

                if (/Firefox/.test(window.navigator.userAgent)) {
                    //中转一下，否则FF里有很NB的错误会出现。。。
                    var b = a, a = a.cloneNode(true);
                    b.parentNode.replaceChild(a, b);
                    b = null;
                }

                file.style.display = 'none';

                a.innerHTML = '<input id="' + this.name + (this.num + 1) + '" name="' + this.name + (this.num + 1) + '" onchange="File.add(this)" type="file" \/>';

                status.innerHTML += ' <span>' + (/[^\\]+$/.exec(file.value) || '') + '<span class="del_file" onclick="File.del(this, ' + this.num + ')"><\/span>;<br><\/span>'


                this.count ++,this.num ++,a = file = null;
            }

            , del : function (span, num) {
                //删除附件
                var file = document.getElementById(this.name + num);
                delete this.urls[file.value];

                document.getElementById(this.form).removeChild(file);
                span.parentNode.parentNode.removeChild(span.parentNode);
                this.count --,span = num = null;
            }
            , size : function (file) {
                var filePath = file.value;
                var image = new Image();
                try {
                    image.dynsrc = filePath;
                    return image.fileSize;
                } catch (e) {
                    return 0;
                }

            }
        };


    </script>
    <script>window.name = "fileup";</script>
</head>
<%
    request.setCharacterEncoding("GBK");

    String FILETP = request.getParameter("FILETP");              //文件相关的业务类别
    String OPNO = request.getParameter("OPNO");                  //对象业务号
    String APPSTATUS = request.getParameter("APPSTATUS");       //业务状态

    if (FILETP == null || OPNO == null) {
        session.setAttribute("msg", "上传功能出错，请联系我们！");
        session.setAttribute("isback", "0");
        session.setAttribute("closeSelf", "1");
        response.sendRedirect("../showinfo.jsp");
    }
    if (APPSTATUS == null || APPSTATUS.trim().equals("")) APPSTATUS = "0";
%>
<body>
<a href="javascript:void(0);" class="files"><input id="file1" name="file1" onchange="File.add(this);" type="file"/></a>
<br><br>

<div id="file"></div>
<hr/>
<form id="form" action="/fileupdown/handle.jsp?FILETP=<%=FILETP%>&OPNO=<%=OPNO%>" method="post"
      enctype="multipart/form-data" target="fileup">
    <input type="hidden" id="hasaddfiles" value="0"/>
    <input type="submit" value=" 上 传 "/>
</form>
<br>
<span id="info" style="color:#6600CC; font-size:14px; font-weight:bold; font-style:normal; display:block;">请将本人相关资料上传；如果有共同还款人，还需将共同还款人的相关资料上传。</span>
<br>

<form id="delform" action="/fileupdown/handle.jsp?FILETP=<%=FILETP%>&OPNO=<%=OPNO%>" method="post"
      enctype="multipart/form-data" target="fileup">
    <table class='page_button_tbl' width="100%" style="font-weight:normal; font-size: 12px; color:#000000;font-family:'微软雅黑';">
        <tr class='page_button_tbl_tr'>
            <td class='page_button_tbl_td' width="100%" style=" word-break : break-all;">
                已提交附件：
            </td>
        </tr>

        <%
            zt.platform.db.ConnectionManager dbmanager = zt.platform.db.ConnectionManager.getInstance();
            DatabaseConnection dCon = dbmanager.getConnection();
            Connection con = dCon.getConnection();
            try {
                Statement st = con.createStatement();
                String filestr = "select objid,filenm from fileblob where FILETP='" + FILETP + "' and OPNO='" + OPNO + "' order by objid";
                ResultSet rs = st.executeQuery(filestr);
                int i = 0;
                while (rs.next()) {
                    i++;
        %>
        <tr class='page_button_tbl_tr'>
            <td class='page_button_tbl_td' width="100%" style=" word-break : break-all;">
                <%if (Integer.parseInt(APPSTATUS) < 2) {%>
                <input type="button" value="删除"
                       onclick="document.all.delform.action=document.all.delform.action+'&OBJID='+<%=rs.getString("objid")%>;document.all.delform.submit()"/>
                &nbsp;<%}%>
                <a href="/file?objid=<%=rs.getString("objid")%>" target="_blank"><%=rs.getString("filenm")%>
                </a>
            </td>
        </tr>
        <%
            }
        %>
        <script type="text/javascript">document.getElementById("hasaddfiles").value = <%=i%></script>
        <%
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
            finally {
                dbmanager.releaseConnection(dCon);
            }
        %>
    </table>
</form>
</body>
</html>