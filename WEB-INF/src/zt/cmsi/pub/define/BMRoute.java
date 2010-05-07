package zt.cmsi.pub.define;
/**
 * 对BMRoute的数据作缓冲，初始化时一次读入内存
 * 采用单态模式
 *
 * @author zhouwei
 * $Date: 2005/06/28 07:00:39 $
 * @version 1.0
 *
 * 版权：青岛中天公司
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
    private static HashMap tbl_data = null; //处理表名和程序名

    /**
     * 返回BMRoute类的唯一实例
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
     * 构造方法，初始化数据
     */
    private BMRoute() {
        data = new HashMap();
        tbl_data = new HashMap();
        if (!loadData()) {
            Debug.debug(Debug.TYPE_ERROR, "读取业务路由表数据失败！！！");
        }
    }

    /**
     * 从数据库中装入业务数据
     */
    private boolean loadData() {
        try {
            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            RecordSet rs = dc.executeQuery("select * from bmroute");
            while (rs.next()) {
                BMRouteData bmroutedata = initDataBean(rs);
                if (bmroutedata == null) {
                    Debug.debug(Debug.TYPE_ERROR, "解析结果集数据失败");
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
     * 根据表结构，初始化数据类
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
     * 获得业务类型的某一步骤的下一步
     * 返回值==NULL表示流程结束
     * <p/>
     * if(BMTypeNo,BMActType,criDefine)记录 exists
     * return nextActType
     * else if((BMTypeNo为空,BMActType,criDefine)记录 exists
     * return nextactType
     * else if((BMTypeNo,BMActType,criDefine为空)记录 exists
     * return nextactType
     * else if((BMTypeNo为空,BMActType,criDefine为空)记录 exists
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
     * 根据业务类型代码和业务步骤类型获得是否当前用户可以执行本业务.
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
     * 根据业务类型号和步骤号得到程序信息封装类
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
     * 根据业务类型号和步骤号得到表名
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
     * 获得下一步的业务状态号码
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
