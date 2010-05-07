//Source file: e:\\java\\zt\\cmsi\\pub\\Param.java

package zt.cmsi.pub;

import zt.platform.form.control.SessionContext;
import zt.platform.utils.Debug;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Description of the Class
 *
 * @author Administrator
 * @created 2004年1月6日
 */
public class Param implements Serializable {
    private HashMap data = null;


    /**
     * Constructor for the Param object
     */
    public Param() {
        data = new HashMap(10);
    }


    /**
     * @param name
     * @return Object
     * @roseuid 3FE50F4902BB 删除子对象到全局参数对象中
     */
    public boolean deleteParam(String name) {
        if (name == null) {
            return false;
        }
        try {
            this.data.remove(name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * @param name
     * @return Object
     * @roseuid 3FE50F4902BB 获得子对象在全局参数对象中
     */
    public Object getParam(String name) {
        if (name == null) {
            return null;
        }
        try {
            return this.data.get(name);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * @param name
     * @param obj
     * @return boolean
     * @roseuid 3FE50F7B00AA 添加子对象到全局参数对象中
     */
    public boolean addParam(String name, Object obj) {
        if (name == null || obj == null) {
            return false;
        }
        try {
            this.data.put(name, obj);
            return true;
        } catch (Exception e) {
            Debug.debug(e);
            return false;
        }
    }


    /**
     * @param name
     * @return boolean
     * @roseuid 3FE50FA9038B 判断子对象在全局参数对象中是否存在
     */
    public boolean ifExist(String name) {
        if (name == null) {
            return false;
        }
        try {
            return this.data.containsKey(name);
        } catch (Exception e) {
            return false;
        }

    }


    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("");
        for (Iterator iter = data.keySet().iterator(); iter.hasNext();) {
            String item = (String) iter.next();
            sb.append("\n" + item + ":" + data.get(item));
        }
        return sb.toString();
    }


    /**
     * Gets the bmNo attribute of the Param object
     *
     * @return The bmNo value
     */
    public String getBmNo() {
        if (this.data.get(ParamName.BMNo) != null) {
            return (String) this.data.get(ParamName.BMNo);
        } else {
            return null;
        }
    }


    /**
     * Gets the bmTypeNo attribute of the Param object
     *
     * @return The bmTypeNo value
     */
    public int getBmTypeNo() {
        Object o = this.data.get(ParamName.BMType);
        if (o != null) {
            if (o instanceof String) {
                return Integer.parseInt(((String) o).trim());
            } else if (o instanceof Integer) {
                return ((Integer) o).intValue();
            } else {
                return -1;
            }
        } else {
            return -1;
        }

    }


    /**
     * Gets the bmActNo attribute of the Param object
     *
     * @return The bmActNo value
     */
    public int getBmActType() {
        Object o = this.data.get(ParamName.BMActType);
        if (o != null) {
            if (o instanceof String) {
                return Integer.parseInt(((String) o).trim());
            } else if (o instanceof Integer) {
                return ((Integer) o).intValue();
            } else {
                return -1;
            }
        } else {
            return -1;
        }

    }


    /**
     * Gets the bmTransNo attribute of the Param object
     *
     * @return The bmTransNo value
     */
    public int getBmTransNo() {
        Object o = this.data.get(ParamName.BMTransNo);
        if (o != null) {
            if (o instanceof String) {
                return Integer.parseInt(((String) o).trim());
            } else if (o instanceof Integer) {
                return ((Integer) o).intValue();
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }


    public static Param getParamByCtx(SessionContext ctx) {
        Param param = new Param();
        param.addParam(ParamName.BMNo, ctx.getParameter(ParamName.BMNo));
        param.addParam(ParamName.AppAmt, ctx.getParameter(ParamName.AppAmt));
        param.addParam(ParamName.AppBeginDate, ctx.getParameter(ParamName.AppBeginDate));
        param.addParam(ParamName.AppEndDate, ctx.getParameter(ParamName.AppEndDate));
        param.addParam(ParamName.BMActType, ctx.getParameter(ParamName.BMActType));

        param.addParam(ParamName.BMTransNo, ctx.getParameter(ParamName.BMTransNo) != null ? new Integer(ctx.getParameter(ParamName.BMTransNo)) : null);
        param.addParam(ParamName.BMType, ctx.getParameter(ParamName.BMType) != null ? new Integer(ctx.getParameter(ParamName.BMType)) : null);
        param.addParam(ParamName.BrhID, ctx.getParameter(ParamName.BrhID));
        param.addParam(ParamName.CheckPoint, ctx.getParameter(ParamName.CheckPoint));
        param.addParam(ParamName.ClientName, ctx.getParameter(ParamName.ClientName));

        param.addParam(ParamName.CLientNo, ctx.getParameter(ParamName.CLientNo));
        param.addParam(ParamName.CurNo, ctx.getParameter(ParamName.CurNo));
        param.addParam(ParamName.Flag, ctx.getParameter(ParamName.Flag));
        param.addParam(ParamName.OrigAccNo, ctx.getParameter(ParamName.OrigAccNo));
        param.addParam(ParamName.OrigBMNo, ctx.getParameter(ParamName.OrigBMNo));

        param.addParam(ParamName.OrigDueBillNo, ctx.getParameter(ParamName.OrigDueBillNo));
        ctx.setAttribute(ParamName.ParamName, param);
        return param;
    }


    public static Param getParamByCtx(HttpServletRequest ctx) {
        Param param = new Param();
        param.addParam(ParamName.BMNo, ctx.getParameter(ParamName.BMNo));
        param.addParam(ParamName.AppAmt, ctx.getParameter(ParamName.AppAmt));
        param.addParam(ParamName.AppBeginDate, ctx.getParameter(ParamName.AppBeginDate));
        param.addParam(ParamName.AppEndDate, ctx.getParameter(ParamName.AppEndDate));
        param.addParam(ParamName.BMActType, ctx.getParameter(ParamName.BMActType));

        param.addParam(ParamName.BMTransNo, ctx.getParameter(ParamName.BMTransNo) != null ? new Integer(ctx.getParameter(ParamName.BMTransNo)) : null);
        param.addParam(ParamName.BMType, ctx.getParameter(ParamName.BMType) != null ? new Integer(ctx.getParameter(ParamName.BMType)) : null);
        param.addParam(ParamName.BrhID, ctx.getParameter(ParamName.BrhID));
        param.addParam(ParamName.CheckPoint, ctx.getParameter(ParamName.CheckPoint));
        param.addParam(ParamName.ClientName, ctx.getParameter(ParamName.ClientName));

        param.addParam(ParamName.CLientNo, ctx.getParameter(ParamName.CLientNo));
        param.addParam(ParamName.CurNo, ctx.getParameter(ParamName.CurNo));
        param.addParam(ParamName.Flag, ctx.getParameter(ParamName.Flag));
        param.addParam(ParamName.OrigAccNo, ctx.getParameter(ParamName.OrigAccNo));
        param.addParam(ParamName.OrigBMNo, ctx.getParameter(ParamName.OrigBMNo));

        param.addParam(ParamName.OrigDueBillNo, ctx.getParameter(ParamName.OrigDueBillNo));
        ctx.setAttribute(ParamName.ParamName, param);
        return param;
    }


    public String generateParameter() {
        StringBuffer sb = new StringBuffer();
        for (Iterator iter = this.data.keySet().iterator(); iter.hasNext();) {
            String item = (String) iter.next();
            sb.append(item + "=" + this.data.get(item) + "&");
        }
        return sb.toString();
    }

    /**
     * The main program for the Param class
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        Param data = new Param();
        System.out.println(data.addParam(ParamName.BMNo, "DFEFDFFD sdfsd"));

        System.out.println(data.addParam(ParamName.AppAmt, new BigDecimal(242432432.234)));
        System.out.println(data.getParam(ParamName.BMNo));
        System.out.println(data.getParam(ParamName.AppAmt));
        System.out.println(data.ifExist(ParamName.BMNo));
        System.out.println(data.ifExist(ParamName.AppAmt));
        data.deleteParam(ParamName.AppAmt);

        System.out.println(data.ifExist(ParamName.BMNo));
        System.out.println(data.ifExist(ParamName.AppAmt));

    }

    public HashMap getParams() {
        return data;
    }

}
