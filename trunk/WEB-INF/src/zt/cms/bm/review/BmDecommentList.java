package zt.cms.bm.review;

/**
 * <p>Title: �Ŵ�����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003  ������Ϣ</p>
 * <p>Company: ������Ϣ</p>
 * @author YUSG
 * @date   2004/01/05  created
 * @version 1.0
 */
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SessionAttributes;
import zt.cmsi.pub.Param;
import zt.cmsi.pub.ParamName;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**************************************************
 *
 * �鿴��������б�
 *
 ***************************************************/

public class BmDecommentList extends FormActions
{
    private String strFlag=null;  //��д��־
    public  Param  params=null;   //���͵ı�������
    private Param  paramg=null;   //��õı�������
    private String strBmTransNo=null;  //ҵ����ϸ��
    private String strBmNo=null;  //ҵ���

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,ErrorMessages msgs,
                EventManager manager, String parameter)
    {

        paramg=(Param)ctx.getRequestAttribute(ParamName.ParamName);
            if(paramg==null)
            {
                msgs.add("�����������ʧ�ܣ�");
                return -1;
            }
            strFlag=paramg.getParam(ParamName.Flag).toString() ;
            strBmNo=paramg.getParam(ParamName.BMNo).toString() ;
            strBmTransNo=paramg.getParam(ParamName.BMTransNo).toString() ;


            if(strBmTransNo==null || strBmNo==null)
            {
                msgs.add("�����������ʧ�ܣ�");
                return -1;
            }
            else
            {
                if(strFlag.equals(ParamName.Flag_WRITE))
                {
                    instance.setReadonly(false);
                }
                else
                {
                    instance.setReadonly(true);
                }
            }
            return 0;
    }
    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                EventManager manager, PreparedStatement ps, PreparedStatement countps)
    {
            try
            {
                ps.setString(1, strBmNo);
                ps.setInt(2, Integer.parseInt(strBmTransNo));

                countps.setString(1, strBmNo);
                countps.setInt(2, Integer.parseInt(strBmTransNo));

                return 0;
            }
            catch(SQLException se)
            {
                return -1;
            }
       }
       public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                ErrorMessages msgs, EventManager manager)
        {
            String strCommentNo=ctx.getParameter("COMMENTNO");
            params=new Param();
            params.addParam(ParamName.Flag,strFlag);
            params.addParam(ParamName.BMNo,strBmNo);
            params.addParam(ParamName.BMTransNo,strBmTransNo);
            params.addParam("commentno",strCommentNo);

            ctx.setRequestAtrribute(ParamName.ParamName,params);

            if ( button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE) )
            {
                trigger(manager,"BMCOMMENTSPAGE",null);
            }
            if ( button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME) )
            {
                trigger(manager,"BMCOMMENTSPAGE",null);
            }
            return 0;
        }
}