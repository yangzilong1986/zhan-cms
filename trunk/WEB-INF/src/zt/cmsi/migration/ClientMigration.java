package zt.cmsi.migration;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import zt.cms.pub.SCUser;
import zt.cmsi.client.CMClientMan;
import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.ErrorCode;
import zt.cmsi.pub.cenum.EnumValue;
import zt.cmsi.pub.define.SystemDate;
import zt.cmsi.biz.BMTable;
import zt.platform.db.DatabaseConnection;
import zt.platform.utils.Debug;

public class ClientMigration {

    /*
public static int clientMrigWithOldClient(String initClientNo, String Operator)
{
  if(initClientNo == null) return ErrorCode.PARAM_IS_NULL;
  InitClientData data = InitClientMan.getInitClient(initClientNo);
  if(data == null) return ErrorCode.INIT_CLIENT_NOT_EXIST;

  if(data.nClientNo == null) return ErrorCode.INIT_CLIENT_WITHOUT_OLDCLIENTNO;
  return ClientMigration.clientMrig(initClientNo,data.nClientNo,Operator,data);
}
    */

    public static int clientMrig(String initClientNo, String newClientNo, String Operator) {
        int errorcode = 0;

        if (initClientNo == null || newClientNo == null) return ErrorCode.PARAM_IS_NULL;
        if (SCUser.isExist(Operator) == false) return ErrorCode.USER_NOT_FOUND;

        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s):Can not get System Date when Processing InitClient!");
            return ErrorCode.BUSI_DATE_NOT_FOUND;
        }

        InitClientData data = InitClientMan.getInitClient(initClientNo);
        if (data == null) return ErrorCode.INIT_CLIENT_NOT_EXIST;

        if (data.procStatus == null) return ErrorCode.PARAM_IS_NULL;
        if (data.procStatus.intValue() != EnumValue.ProcStatus_WeiChuLi) return ErrorCode.INIT_CLIENT_NOT_UNPROCESSED;

        if (CMClientMan.getCMClient(newClientNo) == null) return ErrorCode.CLIENT_NOT_FOUND;

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) return ErrorCode.NO_DB_CONN;

        try {
            errorcode = BMTable.chgBMTableClient(initClientNo, newClientNo, Operator);
            if (errorcode >= 0) {
                String sSql =
                        "update InitClient set Operator='" + Operator + "',LastModified='" +
                                sysdate + "',ProcStatus=" +
                                EnumValue.ProcStatus_YiChuLi + ",nClientNo='" +
                                newClientNo + "' where ClientNo='" + initClientNo + "'";

                Debug.debug(Debug.TYPE_SQL, sSql);
                int prodrtn = dc.executeUpdate(sSql);
                if (prodrtn < 0) {
                    Debug.debug(Debug.TYPE_ERROR, "failed to update InitClient!");
                    errorcode = ErrorCode.DB_UPDATE_FAILED;
                }
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) Debug.debug(e);
            Debug.debug(Debug.TYPE_ERROR, "Exception when updating InitClient!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }


    public static void main(String[] args) {
        ClientMigration clientMigration1 = new ClientMigration();
        System.out.println(ClientMigration.clientMrig("900000001", "2", "system"));
    }
}