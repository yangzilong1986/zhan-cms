package zt.cmsi.pub.define;
/**
 * ��BMRoute�����������壬��ʼ��ʱһ�ζ����ڴ�
 * ���õ�̬ģʽ
 *
 * @author zhouwei
 * $Date: 2005/06/28 07:00:39 $
 * @version 1.0
 *
 * ��Ȩ���ൺ���칫˾
 */

import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.ErrorCode;
import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.utils.Debug;

import java.util.HashMap;

public class BMRoute {
    private static BMRoute ptr = null;
    private static HashMap data = null;
    private static HashMap tbl_data = null; //��������ͳ�����

    /**
     * ����BMRoute���Ψһʵ��
     */
    public static BMRoute getInstance() {
        if (ptr == null) {
            System.out.println("BMRoute initing ...");
            ptr = new BMRoute();
            System.out.println("BMRoute inited ok !");
            return ptr;
        } else {
            return ptr;
        }
    }

    /**
     * ���췽������ʼ������
     */
    private BMRoute() {
        data = new HashMap();
        tbl_data = new HashMap();
        if (!loadData()) {
            Debug.debug(Debug.TYPE_ERROR, "��ȡҵ��·�ɱ�����ʧ�ܣ�����");
        }
    }

    /**
     * �����ݿ���װ��ҵ������
     */
    private boolean loadData() {
        try {
            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            RecordSet rs = dc.executeQuery("select * from bmroute");
            while (rs.next()) {
                BMRouteData bmroutedata = initDataBean(rs);
                if (bmroutedata == null) {
                    Debug.debug(Debug.TYPE_ERROR, "�������������ʧ��");
                    continue;
                }
                data.put(bmroutedata.BMTypeNo + "_" + bmroutedata.BMActType + "_" + bmroutedata.CriDefine, bmroutedata);
                tbl_data.put(bmroutedata.BMTypeNo + "_" + bmroutedata.BMActType, bmroutedata);
            }
        }
        catch (Exception e) {
            Debug.debug(Debug.TYPE_WARNING, e.toString());
            Debug.debug(e);
            return false;
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
        }
        return true;
    }

    /**
     * ���ݱ�ṹ����ʼ��������
     */
    private BMRouteData initDataBean(RecordSet rs) {
        BMRouteData bmroutedata = new BMRouteData();
        try {
            bmroutedata.BMActType = rs.getInt("bmacttype");
            bmroutedata.BMTypeNo = new Integer(rs.getInt("typeno"));
            bmroutedata.CriDefine = new Integer(rs.getInt("cridefine"));
            bmroutedata.nextActType = new Integer(rs.getInt("nextacttype"));
            bmroutedata.NextBMStatus = new Integer(rs.getInt("nextbmstatus"));
            bmroutedata.roleNo = new Integer(rs.getInt("roleno"));
            bmroutedata.progType = new Integer(rs.getInt("progtype"));
            bmroutedata.progName = rs.getString("progname");
            bmroutedata.tableName = rs.getString("tablename");
        }
        catch (Exception e) {
            Debug.debug(Debug.TYPE_WARNING, e.toString());
            Debug.debug(e);
            return null;
        }
        return bmroutedata;
    }

    /**
     * ���ҵ�����͵�ĳһ�������һ��
     * ����ֵ==NULL��ʾ���̽���
     * <p/>
     * if(BMTypeNo,BMActType,criDefine)��¼ exists
     * return nextActType
     * else if((BMTypeNoΪ��,BMActType,criDefine)��¼ exists
     * return nextactType
     * else if((BMTypeNo,BMActType,criDefineΪ��)��¼ exists
     * return nextactType
     * else if((BMTypeNoΪ��,BMActType,criDefineΪ��)��¼ exists
     * return nextactType
     * else
     * return NULL
     * end if
     *
     * @param BMTypeNo
     * @param BMActNo
     * @return Integer
     * @roseuid 3FE40BF800C7
     */
    public Integer getActNextStep(int BMTypeNo, int BMActNo, Integer criDefine) {
        BMRouteData bmroutedata = null;
        if (criDefine != null) {
            bmroutedata = (BMRouteData) data.get(BMTypeNo + "_" + BMActNo + "_" + criDefine);
            if (bmroutedata != null) {
                return bmroutedata.nextActType;
            } else {
                if ((bmroutedata = (BMRouteData) data.get("0_" + BMActNo + "_" + criDefine)) != null) {
                    return bmroutedata.nextActType;
                } else {
                    return null;
                }
            }
        } else {
            bmroutedata = (BMRouteData) data.get(BMTypeNo + "_" + BMActNo + "_0");
            if (bmroutedata != null) {
                return bmroutedata.nextActType;
            } else {
                if ((bmroutedata = (BMRouteData) data.get("0_" + BMActNo + "_0")) != null) {
                    return bmroutedata.nextActType;
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * ����ҵ�����ʹ����ҵ�������ͻ���Ƿ�ǰ�û�����ִ�б�ҵ��.
     *
     * @param UserID
     * @param BMTypeNo
     * @param BMActNo
     * @return int
     * @roseuid 3FE6E8400252
     */
    public static int hasRightToAct(int UserID, int BMTypeNo, int BMActNo) {
        UserRoleMan userroleman = UserRoleMan.getInstance();
        Integer roleNoInteger = BMRoute.getInstance().getRoleOfAct(BMTypeNo, BMActNo);
        int roleNo = roleNoInteger == null ? -1 : roleNoInteger.intValue();
        if (userroleman.ifHasRole(UserID, roleNo)) {
            return 0;
        } else {
            return ErrorCode.NO_RIGHT_OF_ACT;
        }
    }

    /**
     * @param BMTypeNo
     * @param BMActNo
     * @return Integer
     * @roseuid 3FE6E9120073
     */
    public Integer getRoleOfAct(int BMTypeNo, int BMActNo) {
        BMRouteData bmroutedata = (BMRouteData) tbl_data.get(BMTypeNo + "_" + BMActNo);
        if (bmroutedata != null) {
            return bmroutedata.roleNo;
        } else {
            if ((bmroutedata = (BMRouteData) tbl_data.get("0_" + BMActNo)) != null) {
                return bmroutedata.roleNo;
            } else {
                return null;
            }
        }
    }

    /**
     * ����ҵ�����ͺźͲ���ŵõ�������Ϣ��װ��
     *
     * @param BMTypeNo
     * @param BMActNo
     * @return zt.cmsi.pub.define.BMProg
     * @roseuid 3FE6E87A03B4
     */
    public BMProg getActProg(int BMTypeNo, int BMActNo, Integer criDefine) {
        BMRouteData bmroutedata = null;
        if (criDefine != null) {
            bmroutedata = (BMRouteData) data.get(BMTypeNo + "_" + BMActNo + "_" + criDefine);
            if (bmroutedata != null) {
                BMProg bmprog = new BMProg();
                bmprog.setProgType(bmroutedata.progType.intValue());
                bmprog.setProgName(bmroutedata.progName);
                return bmprog;
            } else {
                if ((bmroutedata = (BMRouteData) data.get("0_" + BMActNo + "_" + criDefine)) != null) {
                    BMProg bmprog = new BMProg();
                    bmprog.setProgType(bmroutedata.progType.intValue());
                    bmprog.setProgName(bmroutedata.progName);
                    return bmprog;
                } else {
                    return null;
                }
            }
        } else {
            bmroutedata = (BMRouteData) tbl_data.get(BMTypeNo + "_" + BMActNo);
            if (bmroutedata != null) {
                BMProg bmprog = new BMProg();
                bmprog.setProgType(bmroutedata.progType.intValue());
                bmprog.setProgName(bmroutedata.progName);
                return bmprog;
            } else {
                if ((bmroutedata = (BMRouteData) tbl_data.get("0_" + BMActNo)) != null) {
                    BMProg bmprog = new BMProg();
                    bmprog.setProgType(bmroutedata.progType.intValue());
                    bmprog.setProgName(bmroutedata.progName);
                    return bmprog;
                } else {
                    return null;
                }
            }
        }

    }

    /**
     * ����ҵ�����ͺźͲ���ŵõ�����
     *
     * @param BMTypeNo
     * @param BMActNo
     * @return String
     * @roseuid 3FE6E88801B5
     */
    public String getActTblName(int BMTypeNo, int BMActNo, Integer criDefine) {
        BMRouteData bmroutedata = null;
        if (criDefine != null) {
            bmroutedata = (BMRouteData) data.get(BMTypeNo + "_" + BMActNo + "_" + criDefine);
            if (bmroutedata != null) {
                return bmroutedata.tableName;
            } else {
                if ((bmroutedata = (BMRouteData) data.get("0_" + BMActNo + "_" + criDefine)) != null) {
                    return bmroutedata.tableName;
                } else {
                    return null;
                }
            }
        } else {
            bmroutedata = (BMRouteData) tbl_data.get(BMTypeNo + "_" + BMActNo);
            if (bmroutedata != null) {
                return bmroutedata.tableName;
            } else {
                if ((bmroutedata = (BMRouteData) tbl_data.get("0_" + BMActNo)) != null) {
                    return bmroutedata.tableName;
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * �����һ����ҵ��״̬����
     */
    public int getNextBMStatus(int BMTypeNo, int BMActNo, Integer criDefine) {
        BMRouteData bmroutedata = null;
        if (criDefine != null) {
            bmroutedata = (BMRouteData) data.get(BMTypeNo + "_" + BMActNo + "_" + criDefine);
            if (bmroutedata != null) {
                return bmroutedata.NextBMStatus.intValue();
            } else {
                if ((bmroutedata = (BMRouteData) data.get("0_" + BMActNo + "_" + criDefine)) != null) {
                    return bmroutedata.NextBMStatus.intValue();
                } else {
                    return ErrorCode.GET_NEXT_BM_STATUS_ERROR;
                }
            }
        } else {
            bmroutedata = (BMRouteData) data.get(BMTypeNo + "_" + BMActNo + "_0");
            if (bmroutedata != null) {
                return bmroutedata.NextBMStatus.intValue();
            } else {
                if ((bmroutedata = (BMRouteData) data.get("0_" + BMActNo + "_0")) != null) {
                    return bmroutedata.NextBMStatus.intValue();
                } else {
                    return ErrorCode.GET_NEXT_BM_STATUS_ERROR;
                }
            }
        }
    }

    public static void main(String[] args) {
        BMRoute bmroute = BMRoute.getInstance();
        //System.out.println(bmroute.getActNextStep(1,4,new Integer(2)));
        //System.out.println(bmroute.hasRightToAct(3,2,2));
        //System.out.println(bmroute.getActProg(1,2,null));
        //System.out.println(bmroute.hasRightToAct(20,EnumValue.BMType_TieXian,EnumValue.BMActType_DengJi));
        System.out.println(bmroute.hasRightToAct(9, 1, EnumValue.BMActType_DengJi));
    }
}
