<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.SeviceProxyHttpImpl" %>
<%@ page import="zt.cms.bm.workbench.Affair" %>
<%@ page import="zt.cms.bm.workbench.db.AffairFactory" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.db.DBUtil" %>


<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.user.*" %>
<%@ page import="zt.cms.pub.*" %>
<%@ page import="zt.cmsi.pub.*" %>
<%@ page import=" zt.cmsi.biz.*" %>
<%@ page import=" java.util.*" %>

<%@ page import="zt.platform.form.control.*" %>
<%@ page import="zt.platform.form.control.*" %>
<%@ page import="java.math.*" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>
<%@ page import="zt.cmsi.pub.define.*" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.platform.form.util.*" %>
<%@ page import="zt.cms.bm.common.*" %>
<%@ page import="zt.platform.db.DatabaseConnection"%>
<%@ page import="zt.platform.db.DatabaseConnection"%>
<%@ page import="zt.cmsi.mydb.MyDB" %>
<%@ page import="zt.platform.utils.Debug" %>

<%
MyDB.getInstance().removeCurrentThreadConn("CheckResult.jsp"); //added by JGO on 2004-07-17
%>

<%
Param param=Param.getParamByCtx(request);
RequestDispatcher rd=null;
int myerrorcode = 0;
String bmNo = null;
int startActType = 0;

try
{

DatabaseConnection conn=MyDB.getInstance().apGetConn();

bmNo = BMTable.createBMTable(param.getBmTypeNo(),
    (String) param.getParam(ParamName.CLientNo),
    (String) param.getParam(ParamName.BrhID),
    SessionInfo.getLoginBrhId(session),
    SessionInfo.getLoginUserName(session));
startActType = BMType.getInstance().getStartAct(param.getBmTypeNo()).intValue();
if (bmNo == null) {
    conn.rollback();
    myerrorcode = -1;
    request.setAttribute("msg","建立业务主表失败");	
    rd=request.getRequestDispatcher("/showinfo.jsp");
    rd.forward(request,response);
}
else {


    param.addParam(ParamName.BMNo, bmNo);
    //param.addParam(ParamName.BMTransNo, new Integer(transNo));
    param.addParam("flag", "write");
    UpToDateApp data = new UpToDateApp();
    if(param.getParam(ParamName.AppAmt)!=null){
    data.appAmt = new BigDecimal( (String) param.getParam(
        ParamName.AppAmt));
    }
    data.bmTypeNo = new Integer(param.getBmTypeNo());
    data.origDueBillNo = (String) param.getParam(ParamName.
        OrigDueBillNo);
    data.origAccNo = (String) param.getParam(ParamName.
        OrigAccNo);
    data.clientNo = (String) param.getParam(ParamName.
        CLientNo);
    data.setAppEndDate( (String) param.getParam(ParamName.
        AppEndDate));
    data.setAppStartDate( (String) param.getParam(ParamName.
        AppBeginDate));
    data.origBMNo=(String)param.getParam(ParamName.OrigBMNo);
    data.finalAmt=data.appAmt;
    int ret = BMTable.updateUpToDateApp(bmNo, data);
    
    if (ret < 0) {
    	myerrorcode = -1;
        conn.rollback();
    }
    else
    {



    int transNo = BMTrans.createBMTrans(bmNo,
        param.getBmActType(),
        SessionInfo.getLoginBrhId(session),
        SessionInfo.getLoginUserName(session));
    if (transNo < 0) {
	myerrorcode = -1;
	conn.rollback();
	request.setAttribute("msg","建立业务明细表失败");	
	rd=request.getRequestDispatcher("/showinfo.jsp");
        rd.forward(request,response);
    }
    else {
    
    	param.addParam(ParamName.BMTransNo, new Integer(transNo));

        
        //写检查点数据
        if(myerrorcode >= 0)
        {
        	Vector results=(Vector)session.getAttribute("CriCheckResult");
        	AppCriteria.saveAppCriteriaData(bmNo,results);
        }
    }
  }
}

} //end of try
catch(Exception e)
{
  Debug.debug(e);
}
finally
{
  MyDB.getInstance().apReleaseConn(myerrorcode);
}




MyDB.getInstance().removeCurrentThreadConn("CheckResult(END).jsp"); //added by JGO on 2004-07-17



BMProg prog = BMRoute.getInstance().getActProg(param.getBmTypeNo(), startActType, null);
String url="";
if (prog.isForm()) {
  url= "/templates/defaultform.jsp?Plat_Form_Request_Form_ID="+prog.getProgName()+"&Plat_Form_Request_Event_ID=0&flag=write";
}else {
  //MyDB.getInstance().apReleaseConn(-1);
}

session.setAttribute("BMPARAM",param);
response.sendRedirect(url);
%>