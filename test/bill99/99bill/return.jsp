<%@page contentType="text/html; charset=gb2312" language="java" %>
<%
    /**
     * @Description: 快钱快易付业务授权接口范例
     * @Copyright (c) 上海快钱信息服务有限公司
     * @version 2.0
     */

    //快钱通知结果
    String flag = request.getParameter("msg").trim();


    if (flag.equals("success!")) {
        session.setAttribute("msg", "您的申请已提交成功，请在单击确定后，连接您的打印机，选择您的申请单打印并签署姓名，连同您的证明文件复印件一同寄送给我们，审核通过后我们将与您取得联系！");
        session.setAttribute("funcdel", "window.opener.document.getElementById('print').click();pageWinClose();");
        session.setAttribute("isback", "0");
    } else if (flag.equals("success part!")) {
        session.setAttribute("msg", "您的申请已提交成功，但是申请单状态有误，请联系我们！");
        session.setAttribute("flag", "0");
    } else if (flag.equals("false!")) {
        session.setAttribute("msg", "您的申请还未完成，您在快钱的签约失败，请联系快钱！");
        session.setAttribute("flag", "0");
    } else if (flag.equals("error!")) {
        session.setAttribute("msg", "您的申请还未完成，您在快钱的签约验证失败，请联系我们！");
        session.setAttribute("flag", "0");
    }
//    System.out.println("rtnUrl = " + rtnUrl);
    response.sendRedirect("/showinfo.jsp");
%>
