//Source file: e:\\java\\zt\\cmsi\\biz\\CriCheckResult.java

package zt.cmsi.biz;

import zt.cmsi.pub.cenum.EnumValue;

public class CriCheckResult {
    public int alertType;
    public String message;

    public int getAlertType() {
        return alertType;
    }

    public void setAlertType(int alertType) {
        this.alertType = alertType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "Alert Type=" + getAlertType() + "\nMessage=" + getMessage();
    }

    public String getAlertTypeName() {
        if (EnumValue.AlertType_BuJianCha == getAlertType()) {
            return "不检查";
        } else if (EnumValue.AlertType_JingGao == getAlertType()) {
            return "警告";
        } else if (EnumValue.AlertType_LanJie == getAlertType()) {
            return "拦截";
        } else {
            return "不可知警告类型===" + getAlertType();
        }
    }

    public boolean isLanjie() {
        if (EnumValue.AlertType_LanJie == getAlertType()) {
            return true;
        } else {
            return false;
        }

    }

}
