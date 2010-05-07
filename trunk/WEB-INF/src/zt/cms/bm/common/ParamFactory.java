package zt.cms.bm.common;
import zt.cmsi.pub.*;
import zt.platform.form.control.*;
public class ParamFactory {
    private ParamFactory() {
    }

   public static Param getParamByCtx(SessionContext ctx){
        Param param=new Param();
        param.addParam(ParamName.BMNo,ctx.getParameter(ParamName.BMNo));
        param.addParam(ParamName.AppAmt,ctx.getParameter(ParamName.AppAmt));
        param.addParam(ParamName.AppBeginDate,ctx.getParameter(ParamName.AppBeginDate));
        param.addParam(ParamName.AppEndDate,ctx.getParameter(ParamName.AppEndDate));
        param.addParam(ParamName.BMActType,ctx.getParameter(ParamName.BMActType));

        param.addParam(ParamName.BMTransNo,ctx.getParameter(ParamName.BMTransNo)!=null?new Integer(ctx.getParameter(ParamName.BMTransNo)) :null);
        param.addParam(ParamName.BMType,ctx.getParameter(ParamName.BMType)!=null?new Integer(ctx.getParameter(ParamName.BMType)) :null);
        param.addParam(ParamName.BrhID,ctx.getParameter(ParamName.BrhID));
        param.addParam(ParamName.CheckPoint,ctx.getParameter(ParamName.CheckPoint));
        param.addParam(ParamName.ClientName,ctx.getParameter(ParamName.ClientName));

        param.addParam(ParamName.CLientNo,ctx.getParameter(ParamName.CLientNo));
        param.addParam(ParamName.CurNo,ctx.getParameter(ParamName.CurNo));
        param.addParam(ParamName.Flag,ctx.getParameter(ParamName.Flag));
        param.addParam(ParamName.OrigAccNo,ctx.getParameter(ParamName.OrigAccNo));
        param.addParam(ParamName.OrigBMNo,ctx.getParameter(ParamName.OrigBMNo));

        param.addParam(ParamName.OrigDueBillNo,ctx.getParameter(ParamName.OrigDueBillNo));
        ctx.setAttribute(ParamName.ParamName,param);
        return param;
    }

}