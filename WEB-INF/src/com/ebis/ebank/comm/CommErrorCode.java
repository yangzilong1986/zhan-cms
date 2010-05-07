package com.ebis.ebank.comm;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class CommErrorCode {
   static final int COMM_OK                              = 0;
   static final int CSM_AGENT_NOT_ENOUGH_NODE				= -1;
   static final int CSM_AGENT_NOT_ENOUGH_SPACE				= -2;
   static final int CSM_AGENT_INIT_FAILED						= -3;
   static final int CSM_AGENT_WRITE_ERROR						= -4;
   static final int CSM_AGENT_PARAM_ERROR						= -5;
   static final int CSM_AGENT_NOT_FOUND_SEQ_NO				= -6;
   static final int CSM_AGENT_READ_ERROR						= -7;
   static final int CSM_AGENT_RESPONSE_EMPTY					= -8;
   static final int CSM_AGENT_REQUEST_EMPTY					= -9;
   static final int CSM_AGENT_LOCK_TIME_OUT					= -10;
   static final int COMM_INIFILE_OPEN_FAILED					= -100;
   static final int COMM_CREATE_OBJECT_FAILED				= -101;
   static final int COMM_SERVER_NOT_RUNNING					= -102;
   static final int COMM_PARAMETER_ERROR						= -103;
   static final int COMM_INIFILE_HOSTID_SETTING_ERROR		= -104;
   static final int COMM_INTFILE_TIMEOUT_SETTING_ERROR	= -105;
   static final int COMM_INTFILE_HOSTSERVICE_SETTING_ERROR=	-106;
   static final int COMM_JNI_ERROR                       = -107;
   static final int COMM_LIBRARY_LOAD_ERROR              = -201;
   static final int COMM_ROUTER_PARM_ERROR               = -202;
   static final int COMM_ROUTER_TIMEOUT                  = -203;

}