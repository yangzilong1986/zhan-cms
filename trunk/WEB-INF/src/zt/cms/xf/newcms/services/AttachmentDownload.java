package zt.cms.xf.newcms.services;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-11-16
 * Time: 10:24:42
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
public class AttachmentDownload {
    private String appno;

    public AttachmentDownload() {
        Map params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "未传入有效申请单参数！", "未传入有效申请单参数！"));
        } else
            appno = (String) params.get("appno");
    }

    public String getAppno() {
        return appno;
    }

    public void setAppno(String appno) {
        this.appno = appno;
    }
}
