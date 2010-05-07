<%@ page import="zt.platform.form.util.SessionAttributes" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="zt.platform.form.util.*" %>

<%
    UserManager um = (UserManager)session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    if ( um == null ) {
        um = new UserManager();
        session.setAttribute(SystemAttributeNames.USER_INFO_NAME,um);
    }
    if ( !um.isLogin() ) {
%>
    <jsp:forward page="/loginfail.jsp"/>
<%  }
    String curUri     = request.getRequestURI();
    String formid     = request.getParameter(SessionAttributes.REQUEST_FORM_ID_NAME);
    String instanceid = request.getParameter(SessionAttributes.REQUEST_INSATNCE_ID_NAME);
    String eventid    = request.getParameter(SessionAttributes.REQUEST_EVENT_ID_NAME);
    String initPara   = request.getParameter(SessionAttributes.REQUEST_FORM_INIT_PARAMETERS_NAME);
    String backgroudDispatch = (String)request.getAttribute(SessionAttributes.BACKGROUND_DISPATCH);//jsp to form flag

//    if ( instanceid != null ) {
//			FormInstanceManager fiManager = (FormInstanceManager) session.getAttribute(SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
//			if (fiManager != null) {
//				FormInstance fi = fiManager.getFormInstance(instanceid);
//				if ( fi != null ){
//					formid = fi.getFormid();
//				}
//			}
//    }

    if ( formid != null && eventid != null && eventid.equals("0")) {
			String url = SessionAttributes.REQUEST_FORM_ID_NAME  + "=" + formid + "&" + SessionAttributes.REQUEST_EVENT_ID_NAME + "=0";
			if ( initPara != null ) {
	    	url += "&" + SessionAttributes.REQUEST_FORM_INIT_PARAMETERS_NAME + "=" + initPara;
			}
			if(backgroudDispatch==null && !(formid.startsWith("BMAP") || formid.equals("GLMSGPAGE"))){
      	if (!um.checkPermission(formid, 2 , url)) {
%>
    			<jsp:forward page="/permissionerror.jsp"/>
<%    	}
			}
		}
    else if(!curUri.equals("/templates/defaultform.jsp")){
    	if (!um.checkPermission(curUri, 1 , null)) {
%>
    <jsp:forward page="/permissionerror.jsp"/>
<%
    	}
		}
		else if(instanceid == null || instanceid.length()<1){
%>
    <jsp:forward page="/permissionerror.jsp"/>
<%
		}
%>