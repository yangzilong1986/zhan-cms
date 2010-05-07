//Source file: e:\\java\\zt\\platform\\form\\util\\MessageUtil.java

package zt.platform.form.util;

import java.io.Serializable;

import com.zt.util.PropertyManager;
import zt.platform.form.util.event.ErrorMessage;

/**
 * 消息处理工具
 *
 * @author 王学吉
 * @version 1.0
 */
public class MessageUtil implements Serializable{

    /**
     * 将msg转换成消息串
     * <p/>
     * 根据ErrorMessage的实例，访问PropertyMessages得到转换后的消息串
     *
     * @param msg
     * @return String
     * @roseuid 3F7E3FD90063
     */
    public static String getMessage(ErrorMessage msg) {
        String temp;
        if (msg.getType() == msg.CONSTANT_TYPE) {
            temp = PropertyManager.getProperty(msg.getMessage());
            if (temp == null) temp = msg.getMessage();
            else
                temp += "(" + msg.getMessage() + ")"; //here added by JGO, so programmer can locate the error accurately
        } else {
            temp = PropertyManager.getProperty(msg.getMessage(), msg.getArguments());
            if (temp == null) temp = msg.getMessage() + "<" + msg.getArguments() + ">";
        }
        return temp;
    }
}
