<%@ page contentType="text/html; charset=GBK" %>
<html>
<head>
<title>
sctblacc
</title>
<link rel="stylesheet" type="text/css" href="web.css">
</head>
<jsp:useBean id="b1" scope="request" class="zt.cms.report.SCTblAcc" />
<jsp:setProperty name="b1" property="*" />
<body bgcolor=#99a7bf   topmargin="3" >
<%

b1.init();

int ProcFlag=b1.getProcFlag();
String p1="";
String p2="";
if (ProcFlag==3) {
	 p2="Selected";  //��Ŀ�ϼ�
}else{
	 p1="selected";  //��Ŀ�ϼ�
}
%>
<Form name="userform" Method="Post">
<table width="99%" border=0   align=center class='tb' >
<tr><td align=center class='caption'>��ѡ����Ŀ:
<select name='selectId' size='1' onchange="chgItm();">
<%=b1.getDspItmList()%>
</select>
</td></tr>
</table>
<table width="99%" border=0 bgcolor=#dddddd align=center>
 <tr>
	<td align=center  width="45%" class='title'> ��Ŀһ���� </td>

	<td rowspan=2 width="10%" align=center>
	<input type="button" value="��� ��" onClick="addConfirmer(); return false;" class='button'><BR><br>
	<input type="button" value="�� ɾ��" onClick="removeConfirmer(); return false;" class='button'>
	</td>
	<td align=center   width="45%" class='title'> ��ѡ��Ŀ�б� </td>

</tr>
<tr>
	<td>
	<select name="selectableIDList"  multiple size="18"  style="width:100%;" ondblClick="addConfirmer(); return false;">
	<%=b1.getDspAccList()%>
	</select>
	</td>
	<td>
	<select name="selectedIDList" multiple size="18" style="width:100%;" ondblClick="removeConfirmer(); return false;">

	</select>
	</td>

</tr>
</table>
</form>

<form name='f2' method='post' onsubmit="return ChkData();">
<table width="90%" border=0   align=center >
<tr> ��Ŀ�б�:<input type="text" name='accList' value="<%=b1.getAccList()%>" size=80 class=pt readonly>  </td></tr>
<tr> ��Ŀ����:<input type='text' name='newItmId' value='<%=b1.getNewItmId()%>' size=10 maxlength=6 class=pt>
 ����Ŀ���ƣ� <input type='text' name='itmName' value='<%=b1.getItmName()%>' size=20 maxlength=20 class=pt>
 ������ʽ��<select name="procFlag" size=1>
<option value=1 <%=p1%>>��Ŀ�ϼ�</option>
<option value=3 <%=p2%>>��Ŀ�ϼ�</option>
</select>
</td>
</tr>

<tr class='title'>
<td   >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<input TYPE="submit" value="������" name="cmdAdd"  class="button" onclick="getAccList(1);">&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;
<INPUT TYPE="submit" value="�ޡ���" name="cmdChg"  class="button" onclick="getAccList(2);">&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;
 <INPUT TYPE="submit" value="ɾ����" name="cmdDel"  class="button" >
</td>
</tr>

</table>

<input type="hidden" name="itmId"  value='<%=b1.getItmId()%>'>
</form>

</body>
</html>

<script language="javascript">

if ("<%=b1.getErrMsg()%>" !=""){
  alert("<%=b1.getErrMsg()%> ");
}

addFlag=0;

alist=document.userform.selectableIDList ;
blist=document.userform.selectedIDList;

s=document.f2.accList.value;

s1=s.split(",");
for (i=0;i<s1.length;i++){
	for (j=0;j<alist.length ;j++){
		if (s1[i] == alist.options[j].value){
			n=document.createElement("option");
			blist.add(n);
			n.value=s1[i];
			n.innerText=alist.options[j].innerText;
		}
	}

}

function addConfirmer() {

    aList=document.userform.selectableIDList;
    bList=document.userform.selectedIDList;
	if ((document.f2.procFlag.item(document.f2.procFlag.selectedIndex).value ==3 )  &&(aList.item(aList.selectedIndex).value == document.f2.newItmId.value)){
		alert("��Ŀ�ϼ�ʱ�����ܰ����Լ�!");
		return ;
      }


    for (i = 0; i < aList.options.length; i++) {
      if (aList.options(i).selected == true) {
	    j = bList.options.length - 1;
	    k=0;
	    for (; j >= 0; j--) {
		    if (aList.item(i).value == bList.item(j).value) {
			break;
		    }
		    if (aList.item(i).value >= bList.item(j).value) {
		      k=i;
		    }
	    }
	    if (j < 0) {
		newOpt = new Option(aList.item(i).text, aList.item(i).value);
		bList.add(newOpt,k);
	    }
	 }
    }
    getAccList(0)
}

function removeConfirmer() {
    aList=document.userform.selectableIDList;
    bList=document.userform.selectedIDList;
    myindex = 0;
    for (i = bList.options.length - 1; i >=  myindex; i--) {
	if (bList.options(i).selected == true) {
	    bList.remove(i);
		}
    }
    getAccList(0)
}



function getAccList(val)
{
	aList=document.userform.selectableIDList;
	bList=document.userform.selectedIDList;
	tmpid=""
	tmpname=""
	myindex=0;
	for (i =0; i< bList.options.length;i++) {
	tmpid=tmpid  + bList.options[i].value +","

	}

	document.f2.accList.value=tmpid;
	 if (val ==0 ) addFlag=0;
	if (val ==1 ) addFlag=1;
	if (val ==2 ) addFlag=2;



}
function jtrim(s)
{
var i,b=0,e=s.length;
for(i=0;i<s.length;i++) if(s.charAt(i)!=' '){b=i;break;}
if(i==s.length) return "";
for(i=s.length-1;i>b;i--) if(s.charAt(i)!=' '){e=i;break;}
return s.substring(b,e+1);
}
function ChkData()
{

	if(jtrim(document.f2.newItmId.value)=="")
	{
		alert("��������Ŀ���롣")
		document.f2.newItmId.focus();
		return false;
	}
	if(jtrim(document.f2.itmName.value)=="")
	{
		alert("��������Ŀ���ơ�")
		document.f2.itmName.focus();
		return false;
	}

	if (addFlag==1 && document.f2.itmId.value == document.f2.newItmId.value){
		alert("��Ŀ������ͬ����������:"+document.f2.itmId.value);
		addFlag=0;
		return false;
	}
	if (addFlag==2 && document.f2.itmId.value != document.f2.newItmId.value){
		addFlag=0;
		return confirm("��Ŀ����һ���޸�? ȷ����?");

	}


	return true;
}
function chgItm(){
	document.f2.itmId.value=document.userform.selectId.value;
	document.f2.submit();
}


</script>
