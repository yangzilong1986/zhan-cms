//���ܣ�������ҳ��ǰ�˺���
//���ߣ���ѧ��
//���ڣ�2002��12��3��
//******************************************�ڲ���������ҳ����
//����pageCount
function flippagesubmit(formname, p_url)
{	
	var frm=document.all(formname);
	//document.all("b").disabled="true";
	//frm.b.disabled="true";
	//alert(frm.elements("b1").name);
	frm.elements("b1").disabled="true";
	frm.elements("b2").disabled="true";
	frm.elements("b3").disabled="true";
	frm.elements("b4").disabled="true";
	
	frm.action=p_url;
	frm.submit();	
}

function getPageCount(p_pageCatch,p_rowCount){
	//p_pageCatch	ÿҳ����
	//p_rowCount	������
	var m_pageCount;
	m_pageCount=(p_rowCount % p_pageCatch == 0)
          ? ((p_rowCount == 0) ? 0 : p_rowCount / p_pageCatch)
          : (p_rowCount / p_pageCatch + 1);
	return parseInt(m_pageCount);
}
//��ҳ
function createGoFirstPage(p_pn,p_url_para,p_searchType,formname){
	var m_str;
	var temp;
	if(p_pn<2){
		temp="class=button disabled=true ";
	}
	else{
		temp=" class=button";
	}
	m_str="<td class='list_form_button_td'><input type='submit' name='b1' "+temp+"  value=' ��ҳ ' class='button'  onClick='flippagesubmit(\""+formname+"\",\""+p_url_para+"1\")'>";

	return m_str;
}
//��һҳ
function createGoPrevPage(p_pn,p_url_para,p_searchType,formname){
	var m_str;
	var temp;
	if(p_pn<2){
		temp=" class='button' disabled=true ";
	}
	else{
		temp="  class='button'";
	}

		m_pn=p_pn-1;
	
			//m_str="<a href='"+p_url_para+m_pn+"'>"+m_str+"</a>";
			//m_str="<a href='javascript:flippagesubmit(\""+formname+"\",\""+p_url_para+m_pn+"\")'>"+m_str+"</a>";
			m_str="<td class='list_form_button_td'><input type='submit' name='b2' "+temp+"  value='��һҳ' class='button'  onClick='flippagesubmit(\""+formname+"\",\""+p_url_para+m_pn+"\")'>";
			
		
	
	return m_str;
	
}
//��һҳ
function createGoNextPage(p_pn,p_pageCount,p_url_para,p_searchType,formname){
	var m_str,m_pn;
	var temp;
	//m_str="��һҳ";
	if(p_pn>=p_pageCount){
		temp=" class='button' disabled=true ";
	}
	else{
		temp="  class='button'";
	}
		m_pn=p_pn+1;
	
		//m_str="<a href='"+p_url_para+m_pn+"'>"+m_str+"</a>";
		//m_str="<a href='javascript:flippagesubmit(\""+formname+"\",\""+p_url_para+m_pn+"\")'>"+m_str+"</a>";
		m_str="<td class='list_form_button_td'><input type='submit' class='button' name='b3' "+temp+"   value='��һҳ'   onClick='flippagesubmit(\""+formname+"\",\""+p_url_para+m_pn+"\")'>";
	
	return m_str;
}
//βҳ
function createGoLastPage(p_pn,p_pageCount,p_url_para,p_searchType,formname){
	var m_str,m_pn;
	var temp;
	//m_str="��һҳ";
	if(p_pn>=p_pageCount){
		temp=" class='button' disabled=true ";
	}
	else{
		temp="  class='button'";
	}
	m_pn=p_pageCount;
	m_str="<td class='list_form_button_td'><input type='submit' name='b4' "+temp+" class='button'  value=' βҳ '   onClick='flippagesubmit(\""+formname+"\",\""+p_url_para+m_pn+"\")'>";
	return m_str;
}
//************************************************�ڲ���������ҳִ��
function goPn(p_url_para,p_inputName,formname){
	m_input=document.all(p_inputName);
	
	//��Ч�Լ��
	if (isNaN(m_input.value)==true || m_input.value<=0){
		m_input.focus();
		m_input.value="";
		return;
	}
	
	//window.location.href=p_url_para+m_input.value;
	var frm=document.all(formname);
	frm.action=p_url_para+m_input.value;
	frm.submit();	
}
//���Ӳ�ѯ����ѯҪ���ݸ��Ӳ�����Ҫ����Form�ύ���ķ�ҳִ��
function goPage_Complex(p_urlAll){
	document.forms(0).action=p_urlAll;
	document.forms(0).submit;
}
//************************************************�ⲿ������������ҳ
function createFlipPage(p_pn,p_pageCatch,p_rowCount,p_url,formname){
	//p_pn			��ǰҳ��
	//p_pageCatch	ÿҳ����
	//p_rowCount	������
	//p_url_para	����һҳ��URL�Ͳ���			
	//p_searchType	��ѯ���ͣ���complex��Ϊ���Ӳ�ѯ����ҳʱ��Ҫ�ύ��
	//				(simple)Ϊ�򵥲�ѯ����ҳʱ�ύURL����
	var m_str;
	var m_pageCount;
	m_pageCount=getPageCount(p_pageCatch,p_rowCount);

	//m_str ="��"+p_rowCount+"����¼&nbsp;";
	//m_str+="��"+p_pn+"/"+m_pageCount+"ҳ&nbsp;&nbsp;";
	//m_str="<td nowrap align='left' width='40%'>&nbsp;<font size='2' color='#000066'>ҳ�룺"+p_pn+"/"+m_pageCount+" �ܱ�����"+p_rowCount+"</font></td>";
	m_str=createGoFirstPage(p_pn,p_url,"simple",formname);
	m_str+=createGoPrevPage(p_pn,p_url,"simple",formname);
	m_str+=createGoNextPage(p_pn,m_pageCount,p_url,"simple",formname);
	m_str+=createGoLastPage(p_pn,m_pageCount,p_url,"simple",formname);
	m_str+="</tr></table><td nowrap align='left' width='40%'>&nbsp;<font size='2' color='#000066'>ҳ�룺"+p_pn+"/"+m_pageCount+" �ܱ�����"+p_rowCount+"</font></td>";
	//m_str+="&nbsp;&nbsp;��ת��<input class='inputPage' type='text' name='pn' size='2' maxlength='3' style='height:17'>ҳ";
	//m_str+="&nbsp;<input type='button' class='list_button_active' value='GO' onClick='JavaScript:goPn(\""+p_url+"\",\"pn\",\""+formname+"\")' style='cursor:hand;height:20'>";
	document.write(m_str);
	//alert(m_str);
}

var menuAppear = false;
function menuMove(){
	if(menuAppear){
		document.all("findDiv").style.display="none";
		document.all("findDivHandle").src="../images/form/button1.jpg";
		menuAppear = false;
	} 
	else {
		document.all("findDiv").style.display="";
		document.all("findDivHandle").src="../images/form/button2.jpg";
		menuAppear = true;
	}
}