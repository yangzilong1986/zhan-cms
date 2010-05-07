package zt.cms.xf.gateway;

import com.ibm.ctg.client.ECIRequest;
import com.ibm.ctg.client.JavaGateway;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import zt.platform.utils.Debug;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-5-10
 * Time: 20:58:47
 * To change this template use File | Settings | File Templates.
 */
public class CtgManager {

    private static Log logger = LogFactory.getLog(CtgManager.class);

    public static int iValidationFailed = 0;

    private static JavaGateway javaGatewayObject;

    private static boolean bDataConv = true;
    private static String strDataConv = "ASCII";

    private static String strProgram = "SCLMPC";
    private static String strChosenServer = "haier";

    //SBS���Ի�����ַ 192.168.91.2
    //SBS����������ַ 192.168.91.5
    private static String strUrl = "192.168.91.5";
    private static int iPort = 2006;

    private static int iCommareaSize = 32000;


    /*
    ��ͨѶ���Է�����from���� 200905
     */
    public static void processCtgTest(List list) {

        ECIRequest eciRequestObject = null;
        String buff = "";

        try {
            byte[] abytCommarea = new byte[iCommareaSize];

            javaGatewayObject = new JavaGateway(strUrl, iPort);

//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                System.out.println(e.toString());
//            }

            eciRequestObject = ECIRequest.listSystems(20);
            flowRequest(eciRequestObject);
            int x = 0;
            String zero = "010100100000000005";
            int no = 1001;

            for (int i = 1; i <= 1; i++) {
                if (eciRequestObject.SystemList.isEmpty() == true) {
                    System.out.println("No CICS servers have been defined.");
                    if (javaGatewayObject.isOpen() == true) {
                        i = i;
                        //javaGatewayObject.close();
                    }
                    //System.exit(0);
                }
                //���
                buff = "TPEIa541  010       XD01XD01"; //��ͷ���ݣ�xxxx���ף�010���㣬MPC1�նˣ�MPC1��Ա����ͷ����51���ַ�
                System.arraycopy(getBytes(buff), 0, abytCommarea, 0, buff.length()); //�����ͷ

//                 List list = new ArrayList();//�������ݣ����������ݷ���list�У��м����������add����
                no = no + 1;
                String result = zero.substring(0, 14) + "" + no;

                //�������
                setValues(list, abytCommarea);

                System.out.println("���Ͱ�����:\n" + new String(abytCommarea));

                //���Ͱ�
                eciRequestObject = new ECIRequest(ECIRequest.ECI_SYNC, //ECI call type
                        strChosenServer, //CICS server
                        "1", //CICS userid
                        "1", //CICS password
                        strProgram, //CICS program to be run
                        null, //CICS transid to be run
                        abytCommarea, //Byte array containing the
                        // COMMAREA
                        iCommareaSize, //COMMAREA length
                        ECIRequest.ECI_NO_EXTEND, //ECI extend mode
                        0);                       //ECI LUW token


                //��ȡ���ر���

                String rtnStr = new String(abytCommarea);

                if (flowRequest(eciRequestObject) == true) {
                    //��sof
                    System.out.println("����ֵ11Ϊ\n" + rtnStr);
                }
                System.out.println("����ֵ22Ϊ\n" + rtnStr);

                if (javaGatewayObject.isOpen() == true) {
                    javaGatewayObject.close();
                }


            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /*
     �����Ŵ��ۿ�ɹ������ʴ���: a541
     �����ۿ�ɹ������ʴ���: a542
     �����Ŵ�ϵͳ�ſ��aa54
      */
    public static byte[] processAccount(List list, String txncode) throws java.io.IOException {

        ECIRequest eciRequestObject = null;
        String buff = "";

        try {
            byte[] abytCommarea = new byte[iCommareaSize];

            javaGatewayObject = new JavaGateway(strUrl, iPort);

            eciRequestObject = ECIRequest.listSystems(20);
            flowRequest(eciRequestObject);
            int no = 1001;

            if (eciRequestObject.SystemList.isEmpty() == true) {
                System.out.println("No CICS servers have been defined.");
                if (javaGatewayObject.isOpen() == true) {
                    javaGatewayObject.close();
                }
            }
            //���
            buff = "TPEI" + txncode + "  010       XD01XD01"; //��ͷ���ݣ�xxxx���ף�010���㣬MPC1�նˣ�MPC1��Ա����ͷ����51���ַ�
            System.arraycopy(getBytes(buff), 0, abytCommarea, 0, buff.length()); //�����ͷ

            no = no + 1;

            //�������
            setValues(list, abytCommarea);

//            System.out.println("���Ͱ�����:\n" + new String(abytCommarea));
            logger.info("���Ͱ�����:\n" + new String(abytCommarea));

            //���Ͱ�
            eciRequestObject = new ECIRequest(ECIRequest.ECI_SYNC, //ECI call type
                    strChosenServer, //CICS server
                    "1", //CICS userid
                    "1", //CICS password
                    strProgram, //CICS program to be run
                    null, //CICS transid to be run
                    abytCommarea, //Byte array containing the
                    // COMMAREA
                    iCommareaSize, //COMMAREA length
                    ECIRequest.ECI_NO_EXTEND, //ECI extend mode
                    0);                       //ECI LUW token


            //��ȡ���ر���

            String rtnStr = new String(abytCommarea);

            if (flowRequest(eciRequestObject) == true) {
                //��sof
//                System.out.println("����ֵ11Ϊ\n" + rtnStr);
                logger.info("����ֵ11Ϊ\n" + rtnStr);
            }
//            System.out.println("����ֵ22Ϊ\n" + rtnStr);
            logger.info("����ֵ22Ϊ\n" + rtnStr);

            if (javaGatewayObject.isOpen() == true) {
                javaGatewayObject.close();
            }

            return abytCommarea;

        } catch (java.io.IOException e) {
            e.printStackTrace();
            throw new java.io.IOException("SBSϵͳ������ͨ�����ӳ�ʱ��");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

//    /*
//     ������ϵͳ���ʴ���
//      */
//    public static byte[] processFDAccount(List list) throws java.io.IOException {
//
//        ECIRequest eciRequestObject = null;
//        String buff = "";
//
//        try {
//            byte[] abytCommarea = new byte[iCommareaSize];
//
//            javaGatewayObject = new JavaGateway(strUrl, iPort);
//
//            eciRequestObject = ECIRequest.listSystems(20);
//            flowRequest(eciRequestObject);
//            int no = 1001;
//
//            if (eciRequestObject.SystemList.isEmpty() == true) {
//                System.out.println("No CICS servers have been defined.");
//                if (javaGatewayObject.isOpen() == true) {
//                    javaGatewayObject.close();
//                }
//            }
//            //���
//            buff = "TPEIa542  010       XD01XD01"; //��ͷ���ݣ�xxxx���ף�010���㣬MPC1�նˣ�MPC1��Ա����ͷ����51���ַ�
//            System.arraycopy(getBytes(buff), 0, abytCommarea, 0, buff.length()); //�����ͷ
//
//            no = no + 1;
//
//            //�������
//            setValues(list, abytCommarea);
//
//            System.out.println("���Ͱ�����:\n" + new String(abytCommarea));
//
//            //���Ͱ�
//            eciRequestObject = new ECIRequest(ECIRequest.ECI_SYNC, //ECI call type
//                    strChosenServer, //CICS server
//                    "1", //CICS userid
//                    "1", //CICS password
//                    strProgram, //CICS program to be run
//                    null, //CICS transid to be run
//                    abytCommarea, //Byte array containing the
//                    // COMMAREA
//                    iCommareaSize, //COMMAREA length
//                    ECIRequest.ECI_NO_EXTEND, //ECI extend mode
//                    0);                       //ECI LUW token
//
//
//            //��ȡ���ر���
//
//            String rtnStr = new String(abytCommarea);
//
//            if (flowRequest(eciRequestObject) == true) {
//                //��sof
//                System.out.println("����ֵ11Ϊ\n" + rtnStr);
//            }
//            System.out.println("����ֵ22Ϊ\n" + rtnStr);
//
//            if (javaGatewayObject.isOpen() == true) {
//                javaGatewayObject.close();
//            }
//
//            return abytCommarea;
//
//        } catch ( java.io.IOException e){
//            e.printStackTrace();
//            throw new  java.io.IOException("SBSϵͳ������ͨ�����ӳ�ʱ��");
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }

    /*
   �������۽��� a050
   �������۲�ѯ���� a052
   �������۲��ʽ��� n054-����
    */

    public static byte[] processBatchRequest(String TxnCode, List list) throws Exception {
        ECIRequest eciRequestObject = null;
        String buff = "";
        try {
            byte[] abytCommarea = new byte[iCommareaSize];

            javaGatewayObject = new JavaGateway(strUrl, iPort);

            eciRequestObject = ECIRequest.listSystems(20);
            flowRequest(eciRequestObject);

            if (eciRequestObject.SystemList.isEmpty() == true) {
                System.out.println("No CICS servers have been defined.");
                if (javaGatewayObject.isOpen() == true) {
                    javaGatewayObject.close();
                }
                throw new Exception("δ���� CICS ����������ȷ�ϣ�");
            }
            //���
            buff = "TPEI" + TxnCode + "  010       XD01XD01"; //��ͷ���ݣ�xxxx���ף�010���㣬MPC1�նˣ�MPC1��Ա����ͷ����51���ַ�
            System.arraycopy(getBytes(buff), 0, abytCommarea, 0, buff.length()); //�����ͷ

            //�������
            setValues(list, abytCommarea);

//            System.out.println("���Ͱ�����:\n" + new String(abytCommarea));
            logger.info("���Ͱ�����:\n" + new String(abytCommarea));

            //���Ͱ�
            eciRequestObject = new ECIRequest(ECIRequest.ECI_SYNC, //ECI call type
                    strChosenServer, //CICS server
                    "1", //CICS userid
                    "1", //CICS password
                    strProgram, //CICS program to be run
                    null, //CICS transid to be run
                    abytCommarea, //Byte array containing the
                    // COMMAREA
                    iCommareaSize, //COMMAREA length
                    ECIRequest.ECI_NO_EXTEND, //ECI extend mode
                    0);                       //ECI LUW token


            //��ȡ���ر���
            if (flowRequest(eciRequestObject) == true) {
                //��sof
//                System.out.println("CICS ��������������:" + new String(abytCommarea));
                logger.info("CICS ��������������:" + new String(abytCommarea));
            }

            String returnbuffer = new String(abytCommarea);
            String formcode = returnbuffer.substring(21, 25);
//            System.out.println("����FORM CODE:" + formcode);
            logger.info("����FORM CODE:" + formcode);
            if (javaGatewayObject.isOpen() == true) {
                javaGatewayObject.close();
            }

            return abytCommarea;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /*
    �����ۿ��ѯ���������
     */
    public static void getBatchQueryMsg(byte[] buffer, BatchQueryResult result) {
//        if
        int k = 0;
        try {
            int pos = 29;
            byte[] bSuccnt = new byte[6];
            byte[] bFalcnt = new byte[6];
            byte[] bSucamt = new byte[17];
            byte[] bFalamt = new byte[17];
            byte[] bFloflg = new byte[1];
            byte[] bCurcnt = new byte[6];
            byte[] bRemark1 = new byte[99];
            byte[] bRemark2 = new byte[99];

            System.arraycopy(buffer, pos, bSuccnt, 0, bSuccnt.length);
            pos += bSuccnt.length;
            System.arraycopy(buffer, pos, bFalcnt, 0, bFalcnt.length);
            pos += bFalcnt.length;
            System.arraycopy(buffer, pos, bSucamt, 0, bSucamt.length);
            pos += bSucamt.length;
            System.arraycopy(buffer, pos, bFalamt, 0, bFalamt.length);
            pos += bFalamt.length;

            System.arraycopy(buffer, pos, bFloflg, 0, bFloflg.length);
            pos += bFloflg.length;
            System.arraycopy(buffer, pos, bCurcnt, 0, bCurcnt.length);
            pos += bCurcnt.length;
            System.arraycopy(buffer, pos, bRemark1, 0, bRemark1.length);
            pos += bRemark1.length;
            System.arraycopy(buffer, pos, bRemark2, 0, bRemark2.length);
            pos += bRemark2.length;


            result.setSuccnt(new String(bSuccnt));
            result.setFalcnt(new String(bFalcnt));
            result.setSucamt(new String(bSucamt));
            result.setFalamt(new String(bFalamt));

            result.setFloflg(new String(bFloflg));
            result.setCurcnt(new String(bCurcnt));
            result.setRemark1(new String(bRemark1));
            result.setRemark2(new String(bRemark2));

            int curcnt = Integer.parseInt(result.getCurcnt());

            byte[] bActnum = new byte[32];
            byte[] bActnam = new byte[60];
            byte[] bReason = new byte[40];
            byte[] bTxnamt = new byte[17];


            for (k = 0; k < curcnt; k++) {
                CutpayFailRecord record = new CutpayFailRecord();
                System.arraycopy(buffer, pos, bActnum, 0, bActnum.length);
                pos += bActnum.length;
                System.arraycopy(buffer, pos, bActnam, 0, bActnam.length);
                pos += bActnam.length;
                System.arraycopy(buffer, pos, bReason, 0, bReason.length);
                pos += bReason.length;
                System.arraycopy(buffer, pos, bTxnamt, 0, bTxnamt.length);
                pos += bTxnamt.length;
                record.setActnum(new String(bActnum));
                record.setActnam(new String(bActnam));
                record.setReason(new String(bReason));
                record.setTxnamt(new String(bTxnamt));
                result.add(record);
            }
//            return result;
        } catch (Exception e) {
            System.out.println("���Ľ��ʱ�������⣺" + k);
            logger.error("���Ľ��ʱ�������⣺" + k);
            Debug.debug(e);
            throw new RuntimeException(e);
        }
    }

    static byte[] getBytes(String source) throws java.io.UnsupportedEncodingException {
        if (bDataConv) {
            return source.getBytes(strDataConv);
        } else {
            return source.getBytes();
        }
    }

    public static void setValues(List list, byte[] bb) {
        int start = 51;
        for (int i = 1; i <= list.size(); i++) {
            String value = list.get(i - 1).toString();

            String size = "";
            try {
                size = new String(value.getBytes("GBK"), "ISO-8859-1");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //  System.out.println(i+" "+start+" "+value.length()+" "+value);

//            System.out.println(i + " " + start + " " + size.length() + " " + value);
            logger.info(i + " " + start + " " + size.length() + " " + value);
            setVarData(start, value, bb); //����list�������������������������������ѭ���������
            start = start + size.length() + 2;
        }
    }

    public static void setVarData(int pos, String data, byte[] aa) {

        //˵���������Է��֣��������ݵ����ݳ��ȣ�����2�ַ���������ʮ��������ʽ����
        //���磬��һ�����������������Ϊ��111111��������Ϊ6���ַ�������ڰ�����д��"06111111"����CTG Server���޷�������ȡ�ַ����ȣ�
        //"06"������ʮ��������ʽ���ͣ���0x00 0x60
        //����������������ַ�������һ�²�����ȷȡ�������ַ��ĳ��ȣ�ʹ��UTF-8�ַ�������ȡ����ȷ�������ַ��ĳ���
        String size = "";
        try {
            size = new String(data.getBytes("GBK"), "ISO-8859-1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        short len = (short) size.length();

//		short len = (short)data.length();
        byte[] slen = new byte[2];
        slen[0] = (byte) (len >> 8);
        slen[1] = (byte) (len >> 0);
        System.arraycopy(slen, 0, aa, pos, 2);
        System.arraycopy(data.getBytes(), 0, aa, pos + 2, len);
    }


    private static boolean flowRequest(ECIRequest requestObject) throws Exception {
        int iRc = javaGatewayObject.flow(requestObject);
        String msg = null;
        switch (requestObject.getCicsRc()) {
            case ECIRequest.ECI_NO_ERROR:
                if (iRc == 0) {
                    return true;
                } else {
                    if (javaGatewayObject.isOpen() == true) {
                        javaGatewayObject.close();
                    }
                    throw new Exception("SBS Gateway ���ִ���("
                            + requestObject.getRcString()
                            + "), �����ԭ�����·�����");
                }
            case ECIRequest.ECI_ERR_SECURITY_ERROR:
                msg = "SBS CICS: �û������������";
                break;
            case ECIRequest.ECI_ERR_TRANSACTION_ABEND:
                msg = "SBS CICS : û��Ȩ�����д˱�CICS����";
                break;
            default:
                msg = "SBS CICS : ���ִ��������ԭ��" + requestObject.getCicsRcString();
        }
//        System.out.println("ECI returned: " + requestObject.getCicsRcString());
        logger.info("ECI returned: " + requestObject.getCicsRcString());
//        System.out.println("Abend code was " + requestObject.Abend_Code + " ");
        logger.info("Abend code was " + requestObject.Abend_Code + " ");
        if (javaGatewayObject.isOpen() == true) {
            javaGatewayObject.close();
        }
        throw new Exception(msg);
    }

    public static String getStrUrl() {
        return strUrl;
    }

    public static void setStrUrl(String strUrl) {
        CtgManager.strUrl = strUrl;
    }
}
