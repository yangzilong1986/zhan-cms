<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="zt.cms.report.query.IndexData" %>
<%@ page import="zt.cms.report.query.Index" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>

<HTML>
<HEAD>
<TITLE>信贷管理</TITLE>
<META http-equiv=Content-Type content="text/html; charset=gb2312">

<link type="text/css"  href="web.css" rel="stylesheet">
<style type="text/css">
<!--
body {
	background-image: url(../images/checks_02.jpg);
}

.menutitle{
cursor:pointer;
margin-bottom: 5px;
background-color:#ECECFF;
color:#000000;
width:120px;
padding:2px;
text-align:center;
font-weight:bold;
/*/*/border:1px solid #000000;/* */
}

.submenu{
    display:none;
	margin-bottom: 0.1em;
	font-size:12px;
}

-->
</style>
<script type="text/javascript">

/***********************************************
* Switch Menu script- by Martial B of http://getElementById.com/
* Modified by Dynamic Drive for format & NS4/IE4 compatibility
* Visit http://www.dynamicdrive.com/ for full source code
***********************************************/

if (document.getElementById){ //DynamicDrive.com change
document.write('<style type="text/css">\n')
document.write('.submenu{display: none;}\n')
document.write('</style>\n')
}

function SwitchMenu(obj){
	if(document.getElementById){
	var el = document.getElementById(obj);
	var ar = document.getElementById("masterdiv").getElementsByTagName("table"); //DynamicDrive.com change
		if(el.style.display != "block"){ //DynamicDrive.com change
			for (var i=0; i<ar.length; i++){
				if (ar[i].className=="submenu") //DynamicDrive.com change
				ar[i].style.display = "none";
			}
			el.style.display = "block";
		}else{
			el.style.display = "none";
		}
	}
}

function open(str){
    var st="status=yes,toolbar=yes,menubar=no,location=no,scrollbars=yes,resizable=yes";
    if(str.substr(0,2)=="DY")
        top.window.open("common/dynreport.jsp?rptno="+str,"_blank",st).focus();
    else if(str.substr(1,2)=="07"||str.substr(1,2)=="08"){ //lj added in 20050712 edit by zhengxin in 20070527
        if (str=="R07030"||str=="R07031")
            top.window.open("common/query.jsp?qryNo="+str,"_blank",st).focus();
        else if (str=="R07190"||str=="R07200")
            top.window.open("common/FCreport1.jsp?rptno="+str,"_blank",st).focus();

		else if (str=="R07220")
            top.window.open("/fcsort/xdzcfx/list.jsp","_blank",st).focus();//add by houcs
        else if (str=="R07240")
            top.window.open("/fcsort/hangyecx/list.jsp","_blank",st).focus();//add by houcs
        else
            top.window.open("common/FCreport.jsp?rptno="+str,"_blank",st).focus();
    }
    else
        top.window.open("common/report.jsp?rptno="+str,"_blank",st).focus();
}

</script>
</head>


<%
ArrayList arrList=new ArrayList();
Index index=new Index();

arrList=index.getReport();

%>
<BODY background=贷后管理_files/checks_02.jpg>
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0>
  <TR>
    <TD vAlign="center" align="middle">
      <TABLE borderColor=#006699 height=325 cellSpacing=2 cellPadding=2
      align=center bgColor=#aaccee border=2>
        <TR align=left>
          <TD bgColor=#4477aa height=30><IMG src="../images/form/xing1.jpg" width="13" height="13"
            align=absMiddle> <FONT color=#ffffff size=2><B>报表</B></FONT> <IMG
            src="../images/form/xing1.jpg" width="13" height="13" align="absMiddle"></TD>
        </TR>
        <TR align="middle">
          <TD vAlign="center">
            <TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%"
            border=0>
              <TR>
                <TD width=20>&nbsp;</TD>
                <TD vAlign="center" align="middle">
                  <SCRIPT src="贷后管理_files/querybutton.js"
                  type=text/javascript></SCRIPT>

                  <SCRIPT src="贷后管理_files/meizzDate.js"
                  type=text/javascript></SCRIPT>

                  <TABLE>
                    <TR>
                      <TD>
                        <TABLE cellSpacing=0 cellPadding=0 width=538 border=0>
                          <TR>
                            <TD align="middle" height=0>&nbsp;</TD>
                          </TR>
					    </TABLE>
                        <div id="masterdiv"><br>
						  <table width=480  border=0 align='center' cellpadding=1 cellspacing=1 class='list_form_table' >
							<%
							 StringBuffer strBuf=new StringBuffer();
							 int intClass=0;

							 for(int i=0;i<arrList.size();i++)
							 {
								 IndexData iData=(IndexData)arrList.get(i);
								 int rptClass=iData.rptClass;

								 if(rptClass!=intClass && intClass!=0)
								 {
								    strBuf.append(" </table></td></tr> ");
								 }

								 if(rptClass!=intClass)
								 {
									 strBuf.append(" <tr align='center' class='list_form_title_tr'> ");
									 strBuf.append(" <td align='center' class='table_list1'><span class='style6'>");
									 strBuf.append("<input name='button2' type=button class='button' ");
									 strBuf.append("value='"+iData.listItmNo+"' height='14' onclick=\"SwitchMenu('tb");
									 strBuf.append( i+"')\"> </span></td> </tr>");

									 strBuf.append("<tr class=list_form_tr>");
								     strBuf.append("<td align='center' class='table_list' height='0'>");
								     strBuf.append("<table class='submenu' width='70%'  border='0' "); strBuf.append(" cellpadding='2' cellspacing='0' id='tb"+i+"'>");
								 }
								
								strBuf.append("<tr> <td class='table_list'> ");
								strBuf.append("<a href=\"javascript:open('"+iData.rptForm+"')\" >");
								strBuf.append(iData.rptName);
								strBuf.append("</a></td></tr>");

								
								intClass=rptClass;
							 }
							strBuf.append("</table></td></tr> </table>");
							out.print(strBuf.toString());
							%>
					</table>
					</div>
					</TD>
                    </TR>
					</TABLE>
					</TD>
				</TR>
				</TABLE>
				</TD>
				</TR>
		<TR vAlign="center" align="middle" height="35">
             <TD>&nbsp;          </TD>
        </TR>
		</TABLE>
	</TD>
	</TR>
</TABLE>

</BODY>
</HTML>