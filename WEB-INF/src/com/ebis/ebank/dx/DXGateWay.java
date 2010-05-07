//Source file: c:\\src\\com\\ebis\\ebank\\dx\\DXGateWay.java

package com.ebis.ebank.dx;

import com.ebis.ebank.ap.ApDataPoint;
import com.ebis.ebank.defines.*;

public class DXGateWay
{

   /**
   @roseuid 3DEB599602BE
    */
   public DXGateWay()
   {

   }

   /**
   @param apData
   @param transID
   @return long
   @roseuid 3DEAE8A900A1
    */
   public static long clientSendTrans(ApDataPoint apData, String transID) throws Exception
   {
       if ( apData == null )
           return DXErrorCode.AP_DATA_ERROR;
       try {
           ControlCenter control = get(transID);
           return control.send(apData, transID);
       } catch ( Exception e ) {
           return DXErrorCode.TRANS_DEFINE_ERROR;
       }
   }

   /**
   @param apData
   @param transSeriesID
   @param timeout
   @return int
   @roseuid 3DEAE932010C
    */
   public static int clientRecvTrans(ApDataPoint apData, long transSeries, long timeout) throws Exception
   {
       if ( apData == null )
           return DXErrorCode.AP_DATA_ERROR;
       LuManager luManager = LuManager.getInstance();
       String transSeriesID = SeriesID.format((int)transSeries);
       if ( !luManager.contain(""+transSeriesID) )
           return DXErrorCode.NO_THIS_LU;
       try {
           SeriesSource source = (SeriesSource) luManager.getTrans("" +
               transSeriesID);
           String transID = source.getTransID();
           ControlCenter control = get(transID);

           control.receive(apData, transSeries, timeout);

           return 0;
       } catch ( Exception e ) {
           return DXErrorCode.TRANS_DEFINE_ERROR;
       }
   }
   private static ControlCenter get(String transID) throws Exception {
       ConfigManager config = ConfigManager.getInstance();
       Transaction trans = config.getTransaction(transID);
       if ( trans == null )
           throw new IllegalDefinitionException("交易["+transID+"]定义故障！");
       Gateway gateway = config.getGateway(trans.getGateWayID());
       if ( gateway == null || gateway.getGatewayID() == null || gateway.getClassHandler() == null || gateway.getHostName() == null)
           throw new IllegalDefinitionException("交易["+transID+"]的网关定义故障！");

       try {
           ClassLoader loader = DXGateWay.class.getClassLoader();
           Class classHandler = loader.loadClass(gateway.getClassHandler());
           if ( classHandler == null )
               throw new IllegalDefinitionException("交易["+transID+"]网关处理类载入失败！");
           ControlCenter control = (ControlCenter)classHandler.newInstance();
           return control;
       } catch ( Exception e ) {
           throw new IllegalDefinitionException(e.getMessage());
       }
   }

   /**
   @param apData
   @param transSeriesID
   @return int
   @roseuid 3DEAE97602AE
    */
   public static int serverSendTrans(ApDataPoint apData, long transSeriesID)  throws Exception
   {
       throw new UnsupportedOperationException("Method serverSendTrans() not yet implemented.");
   }

   /**
   @param apData
   @param transID
   @param timeout
   @return long
   @roseuid 3DEAE99203D1
    */
   public static long serverRecvTrans(ApDataPoint apData, String transID, long timeout) throws Exception
   {
       throw new UnsupportedOperationException("Method serverRecvTrans() not yet implemented!");
   }
}
