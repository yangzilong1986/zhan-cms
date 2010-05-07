<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.db.*" %>
<%@ page import="zt.cmsi.mydb.MyDB,zt.platform.utils.Debug,zt.cmsi.pub.code.*" %>

<!--jsp:include page="/checkpermission.jsp"/-->

<%

//Debug.debug(Debug.TYPE_MESSAGE,"CURR SN:"+zt.cmsi.pub.code.SNBusinessID.getCurNo());
//Debug.debug(Debug.TYPE_MESSAGE,"CURR SN:"+zt.cmsi.pub.code.BMPDTrans.getCurNo());

MyDB.getInstance().removeCurrentThreadConn("workbench.check.jsp"); //added by JGO on 2004-07-17

DatabaseConnection dc = null;
dc = MyDB.getInstance().apGetConn();

//Thread.sleep(20000);
System.out.println("before----updat branch");
dc.executeUpdate("update scbranch set sname='test' where brhid='907070160'");
System.out.println("Finished----update brannch");


System.out.println("before----select * from scrole");
dc.executeQuery("select * from scrole");
System.out.println("Finished----select * from scrole");

System.out.println("before----update scuser");
dc.executeUpdate("update scuser set username='test' where loginname='060501'");
System.out.println("Finished----update scuser");

Thread.sleep(60000);

System.out.println("before----commit");
//db.commit();


MyDB.getInstance().apReleaseConn(0);

System.out.println("after----commit");

//ConnectionManager.getInstance().releaseConnection(dc);
System.out.println("OK");
 
%>