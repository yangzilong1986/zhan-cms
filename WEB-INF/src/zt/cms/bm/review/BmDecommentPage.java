package zt.cms.bm.review;

/**
 * <p>Title: 信贷管理</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003 中天信息</p>
 * <p>Company: 中天信息</p>
 * @author YUSG
 * @date  2004/1/5
 * @version 1.0
 */
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SessionAttributes;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventType;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.db.DatabaseConnection;
import zt.cmsi.pub.Param;
import zt.cmsi.pub.ParamName;
import zt.platform.db.RecordSet;
import zt.cmsi.biz.BMTable;
import zt.platform.user.UserManager;
import zt.platform.db.DBUtil;
import zt.platform.form.util.SqlWhereUtil;
import zt.cms.pub.SCUser;
import zt.cms.pub.SCBranch;
/**********************************************************
 *
 * 查看审批意见详细
 *
 ************************************************************/

public class BmDecommentPage extends FormActions
{
    private String strFlag = null;      //读写标志
    public Param params = null;         //发送的变量集合
    private Param paramg = null;        //获得的变量集合
    private String strBmNo = null;      //业务号
    private String strBmTransNo = null; //业务明细号
    private String strUserName=null;    //当前登陆用户名
    private int    iCommentNo=0;        //当前的意见数
    private String strCommentNo=null;   //审批意见编号
    private String strScbrh=null;       //所在网点

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,ErrorMessages msgs,
                EventManager manager, String parameter)
    {

        paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);
        if (paramg == null) {
            msgs.add("数据不完整无法授权，请检查数据");
            return -1;
        }

        strFlag = (String) paramg.getParam(ParamName.Flag);
        strBmNo = (String) paramg.getParam(ParamName.BMNo);
        strBmTransNo = (String) paramg.getParam(ParamName.BMTransNo);
        strCommentNo=(String) paramg.getParam("commentno");


        if (strBmNo == null || strBmTransNo == null) {
            msgs.add("数据不完整无法授权，请检查数据");
            return -1;
        }
        else {
            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.
            USER_INFO_NAME);
            try
            {
               strUserName = um.getUserName();
               strScbrh = SCUser.getBrhId(strUserName);
               if (strScbrh == null || strScbrh.length() < 1) {
                   msgs.add("下属网点不存在！");
                   return -1;
               }
               else {
                   strScbrh = "'" + strScbrh + "'";
               }
             }
             catch(Exception e)
             {
                e.printStackTrace();
             }

            int iCount = 0;
            String strSql = "select max(commentno) from bmcomments where bmno='" + strBmNo;
            strSql = strSql + "' and bmtransno=" + strBmTransNo;

            RecordSet rs = conn.executeQuery(strSql);
            while (rs.next()) {
                iCount = rs.getInt(0);
            }
            iCommentNo = iCount;
            if (iCount > 0 && strCommentNo != null) {
                instance.setValue("BMNO", strBmNo);
                instance.setValue("BMTRANSNO", Integer.parseInt(strBmTransNo));
                instance.setValue("COMMENTNO", Integer.parseInt(strCommentNo));
                trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                        Event.BRANCH_CONTINUE_TYPE);
            }

            if (strFlag.equals( (String) ParamName.Flag_WRITE))
            {
                instance.setReadonly(false);
            }
            else
                instance.setReadonly(true);
            return 0;
        }
    }
    public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
           EventManager manager)
    {
        instance.setValue("OPERATOR",strUserName);
        instance.setValue("BMNO",strBmNo);
        instance.setValue("BMTRANSNO",Integer.parseInt(strBmTransNo));
        //instance.setValue("COMMENTNO",iCommentNo);

        return 0;
   }
   public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor)
    {
        iCommentNo=iCommentNo+1;
        String strClientMgr=ctx.getParameter("REVIEWEDBY").trim();
        String strWhere=" loginname='"+strClientMgr+"' and usertype<>'3'";
        String isnull=DBUtil.getCellValue(conn,"SCUSER","LOGINNAME",strWhere);
        if(isnull==null)
        {
            msgs.add("意见人不存在，请检查输入的意见人是否准确！");
            return -1;
        }
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "COMMENTNO", String.valueOf(iCommentNo));
        return 0;
    }
    public int preReference(SessionContext ctx, DatabaseConnection conn,FormInstance instance, ErrorMessages msgs,
                        EventManager manager, String reffldnm,SqlWhereUtil sqlWhereUtil)
   {
      sqlWhereUtil.addWhereField("SCUSER", "BRHID", strScbrh,
                                       SqlWhereUtil.DataType_Sql,
                                       sqlWhereUtil.OperatorType_In,
                                       sqlWhereUtil.RelationOperator_And);

       return 0;
   }



}