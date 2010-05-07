//功能：产生分页的前端函数
//作者：王学吉
//日期：2002年12月3日
//******************************************内部函数：翻页计算
//计算pageCount
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
	//p_pageCatch	每页行数
	//p_rowCount	总行数
	var m_pageCount;
	m_pageCount=(p_rowCount % p_pageCatch == 0)
          ? ((p_rowCount == 0) ? 0 : p_rowCount / p_pageCatch)
          : (p_rowCount / p_pageCatch + 1);
	return parseInt(m_pageCount);
}
//首页
function createGoFirstPage(p_pn,p_url_para,p_searchType,formname){
	var m_str;
	var temp;
	if(p_pn<2){
		temp="class=button disabled=true ";
	}
	else{
		temp=" class=button";
	}
	m_str="<td class='list_form_button_td'><input type='submit' name='b1' "+temp+"  value=' 首页 ' class='button'  onClick='flippagesubmit(\""+formname+"\",\""+p_url_para+"1\")'>";

	return m_str;
}
//上一页
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
			m_str="<td class='list_form_button_td'><input type='submit' name='b2' "+temp+"  value='上一页' class='button'  onClick='flippagesubmit(\""+formname+"\",\""+p_url_para+m_pn+"\")'>";
			
		
	
	return m_str;
	
}
//下一页
function createGoNextPage(p_pn,p_pageCount,p_url_para,p_searchType,formname){
	var m_str,m_pn;
	var temp;
	//m_str="下一页";
	if(p_pn>=p_pageCount){
		temp=" class='button' disabled=true ";
	}
	else{
		temp="  class='button'";
	}
		m_pn=p_pn+1;
	
		//m_str="<a href='"+p_url_para+m_pn+"'>"+m_str+"</a>";
		//m_str="<a href='javascript:flippagesubmit(\""+formname+"\",\""+p_url_para+m_pn+"\")'>"+m_str+"</a>";
		m_str="<td class='list_form_button_td'><input type='submit' class='button' name='b3' "+temp+"   value='下一页'   onClick='flippagesubmit(\""+formname+"\",\""+p_url_para+m_pn+"\")'>";
	
	return m_str;
}
//尾页
function createGoLastPage(p_pn,p_pageCount,p_url_para,p_searchType,formname){
	var m_str,m_pn;
	var temp;
	//m_str="下一页";
	if(p_pn>=p_pageCount){
		temp=" class='button' disabled=true ";
	}
	else{
		temp="  class='button'";
	}
	m_pn=p_pageCount;
	m_str="<td class='list_form_button_td'><input type='submit' name='b4' "+temp+" class='button'  value=' 尾页 '   onClick='flippagesubmit(\""+formname+"\",\""+p_url_para+m_pn+"\")'>";
	return m_str;
}
//************************************************内部函数：翻页执行
function goPn(p_url_para,p_inputName,formname){
	m_input=document.all(p_inputName);
	
	//有效性检查
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
//复杂查询（查询要传递复杂参数，要依靠Form提交）的翻页执行
function goPage_Complex(p_urlAll){
	document.forms(0).action=p_urlAll;
	document.forms(0).submit;
}
//************************************************外部函数：产生分页
function createFlipPage(p_pn,p_pageCatch,p_rowCount,p_url,formname){
	//p_pn			当前页码
	//p_pageCatch	每页行数
	//p_rowCount	总行数
	//p_url_para	到下一页的URL和参数			
	//p_searchType	查询类型，（complex）为复杂查询，翻页时需要提交表单
	//				(simple)为简单查询，翻页时提交URL参数
	var m_str;
	var m_pageCount;
	m_pageCount=getPageCount(p_pageCatch,p_rowCount);

	//m_str ="共"+p_rowCount+"条记录&nbsp;";
	//m_str+="第"+p_pn+"/"+m_pageCount+"页&nbsp;&nbsp;";
	//m_str="<td nowrap align='left' width='40%'>&nbsp;<font size='2' color='#000066'>页码："+p_pn+"/"+m_pageCount+" 总笔数："+p_rowCount+"</font></td>";
	m_str=createGoFirstPage(p_pn,p_url,"simple",formname);
	m_str+=createGoPrevPage(p_pn,p_url,"simple",formname);
	m_str+=createGoNextPage(p_pn,m_pageCount,p_url,"simple",formname);
	m_str+=createGoLastPage(p_pn,m_pageCount,p_url,"simple",formname);
	m_str+="</tr></table><td nowrap align='left' width='40%'>&nbsp;<font size='2' color='#000066'>页码："+p_pn+"/"+m_pageCount+" 总笔数："+p_rowCount+"</font></td>";
	//m_str+="&nbsp;&nbsp;跳转到<input class='inputPage' type='text' name='pn' size='2' maxlength='3' style='height:17'>页";
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