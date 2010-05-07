package zt.cms.report.dyn;

/**
 * <p>Title: �Ŵ�����ϵͳ</p>
 * <p>Description: Ϋ���Ŵ�</p>
 * <p>Copyright: Copyright (c) 2003  ������Ϣ�������޹�˾</p>
 * <p>Company: ������Ϣ�������޹�˾</p>
 * @author Yusg
 * @version 1.0
 */
import zt.cms.report.ScbranchLevel;
/************************************************
 *
 * ��̬����Ĳ�ѯ���
 *
 ***********************************************/

public class ReportSql
{

    /************************************************
     * function :��ñ������Ϣ��sql���
     * parameter:strRptNo   String     ����
     * return   :strSql     String     sql���
     ************************************************/
    public static String getColSql(String strRptNo)
    {
        String strSql;
        strSql="select repname,colnum,ltitle,mtitle,rtitle,colname,collen,planish,rate,istenth from reportdef where repno='"+strRptNo+"'";
        return strSql;
    }

    /************************************************
     * function :��������ļ��������ݵĲ�ѯsql���
     * parameter:strRptNo  String    ������
     *           upbrh     String    ��ѯ��������
     *           month     String    �·�
     * return   :sql       String     sql���
     ************************************************/
    public static String getSql(String strRptNo,String upbrh,String month)
    {
        String v_brhid,sql,strBrhid,strSName;//��������ĵȼ��ж�
        int level=-1;

        ScbranchLevel sbl=new ScbranchLevel(upbrh);
        level=sbl.intBrhLevel;

        if(level==1)
            {
                v_brhid="s.bnkid";
                strSName=",(select sname from scbranch where brhid=s.bnkid),";
                strBrhid = " s.brhid=a.brhid ";
            }
            else if(level==2)
            {
                v_brhid="s.chgid";
                strSName=",(select sname from scbranch where brhid=s.chgid),";
                strBrhid = "s.bnkid='"+upbrh+"' and s.brhid=a.brhid";
            }
            else if (level==3)
            {
                v_brhid="s.brhid";
                strSName=",(select sname from scbranch where brhid=s.brhid),";
                strBrhid = "s.chgid='"+upbrh+"' and s.brhid=a.brhid";
            }
            else
            {
                v_brhid="s.brhid";
                strSName=",(select sname from scbranch where brhid=s.brhid),";
                strBrhid = "s.brhid='"+upbrh+"' and s.brhid=a.brhid";
            }

            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select ");
            strBuf.append(v_brhid);//�������㼶�𣬻��bnkid,chgid,brhid
            strBuf.append(strSName);//�������㼶�𣬻����������
            strBuf.append("sum(a.F1),sum(a.F2),sum(a.F3),sum(a.F4),sum(a.F5),sum(a.F6),sum(a.F7),sum(a.F8),");
            strBuf.append("sum(a.F9),sum(a.F10),sum(a.F11),sum(a.F12),sum(a.F13),sum(a.F14),sum(a.F15),sum(a.F16),sum(a.F17),");
            strBuf.append("sum(a.F18),sum(a.F19),sum(a.F20),sum(a.F21),sum(a.F22),sum(a.F23),sum(a.F24),sum(a.F25) ");
            strBuf.append("from scbranch s,reportdata a where ");
            strBuf.append(strBrhid);
            strBuf.append(" and a.month='");
            strBuf.append(month);
            strBuf.append("' and repno='");
            strBuf.append(strRptNo);
            strBuf.append("' group by ");
            strBuf.append(v_brhid);
            strBuf.append(" order by ");
            strBuf.append(v_brhid);

            sql=strBuf.toString();
            return sql;
    }

    /************************************************
     * function :��������ļ������¼�����
     * parameter:upbrh  String  ��ѯ��������
     * return   :sql    String  sql���
     ************************************************/
    public static String geBrhSql(String upbrh)
    {
        String v_brhid,sql,strBrhid;//��������ĵȼ��ж�
        int level=-1;
        ScbranchLevel sbl=new ScbranchLevel(upbrh);
        level=sbl.intBrhLevel;

        if(level==1)
            {
                v_brhid="s.bnkid";
                strBrhid = "s.upbrh='";
            }
            else if(level==2)
            {
                v_brhid="s.chgid";
                strBrhid = "s.upbrh='";
            }
            else if (level==3)
            {
                v_brhid="s.brhid";
                strBrhid = "s.upbrh='";
            }
            else
            {
                v_brhid="s.brhid";
                strBrhid = "s.brhid='";
            }

            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select ");
            strBuf.append(v_brhid);
            strBuf.append(",s.brhid,(select sname from scbranch where brhid=s.brhid)");
            strBuf.append(" from scbranch s where ");
            strBuf.append(strBrhid + upbrh);
            strBuf.append("' order by ");
            strBuf.append(v_brhid);

            sql=strBuf.toString();
            return sql;
    }
}
