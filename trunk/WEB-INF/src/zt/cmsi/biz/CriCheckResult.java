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
            return "�����";
        } else if (EnumValue.AlertType_JingGao == getAlertType()) {
            return "����";
        } else if (EnumValue.AlertType_LanJie == getAlertType()) {
            return "����";
        } else {
            return "����֪��������===" + getAlertType();
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
