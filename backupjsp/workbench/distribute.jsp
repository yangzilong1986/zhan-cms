<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.SeviceProxyHttpImpl" %>
<%@ page import="zt.cmsi.pub.*" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.platform.form.util.*" %>
<%@ page import="zt.cms.bm.common.*" %>

<%@ page import="zt.cmsi.pub.define.*" %>
<%@ page import="zt.cmsi.biz.*" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>

<%
MyDB.getInstance().removeCurrentThreadConn("distribute.jsp"); //added by JGO on 2004-07-17
%>

<%
request.setCharacterEncoding("GB2312");

Param param = new Param();
param.addParam(ParamName.BMNo, request.getParameter(ParamName.BMNo).trim());
param.addParam(ParamName.BMTransNo, new Integer(request.getParameter(ParamName.BMTransNo).trim()));
param.addParam(ParamName.ClientName, request.getParameter(ParamName.ClientName).trim());
param.addParam(ParamName.Flag, request.getParameter(ParamName.Flag).trim());
param.addParam(ParamName.BMActType, new Integer(request.getParameter(ParamName.BMActType).trim()));
param.addParam(ParamName.BrhID, request.getParameter(ParamName.BrhID).trim());

param.addParam(ParamName.BMType, new Integer(request.getParameter(ParamName.BMType).trim()));
//System.out.println(param);
request.setAttribute("BMPARAM",param);
request.setAttribute(SessionAttributes.BACKGROUND_DISPATCH,"ok");



//System.out.println(SessionInfo.getLoginUserNo(session)+"++++"+param.getBmTypeNo()+"+++++"+param.getBmActType());
int ret=BMRoute.getInstance().hasRightToAct(SessionInfo.getLoginUserNo(session),param.getBmTypeNo(),param.getBmActType());
//System.out.println("ret**********"+ret);

RequestDispatcher rd=null;
if(ret>=0){
    BMTrans.viewTrans(param.getBmNo(),param.getBmTransNo());
    BMProg prog = BMRoute.getInstance().getActProg(param.getBmTypeNo(),param.getBmActType(),null);
    //System.out.println("**************PROG:*****************"+prog);
    String url= "";

	if(prog.isForm()){
           url= "/templates/defaultform.jsp?Plat_Form_Request_Form_ID="+prog.getProgName()+"&Plat_Form_Request_Event_ID=0&flag=write";
	}else{
           url=prog.getProgName();
        }
    //System.out.println(url);
	//System.out.println(param);
rd=request.getRequestDispatcher(url);
}else{
    request.setAttribute("msg","该用户没有权限");	
    rd=request.getRequestDispatcher("/showinfo.jsp");
}

rd.forward(request,response);

%>