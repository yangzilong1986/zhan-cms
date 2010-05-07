//Source file: e:\\java\\zt\\platform\\form\\util\\event\\ErrorMessages.java

package zt.platform.form.util.event;

import java.util.ArrayList;
import java.util.List;

/**
 * 错误信息集合
 *
 * @author 王学吉
 * @version 1.0
 */
public class ErrorMessages {
    private List messages = new ArrayList();

    /**
     * @param msg
     * @roseuid 3F722DE60248
     */
    public void add(String msg) {
        this.messages.add(new ErrorMessage(0, msg));
    }

    /**
     * @roseuid 3F722E0802A1
     */
    public void clear() {
        this.messages.clear();
    }

    /**
     * @return List
     * @roseuid 3F722E3803C2
     */
    public List getAll() {
        return this.messages;
    }

    public String  getAllMessages() {
        if (this.size() ==0) {
            return " ";
        }
        String message = " ";
        for (int i = 0; i < this.size(); i++) {
            message += this.get(i).getMessage();
        }
        return message;
    }


    /**
     * @param idx
     * @roseuid 3F722E430300
     */
    public ErrorMessage get(int idx) {
        return (ErrorMessage) this.messages.get(idx);
    }

    /**
     * @return int
     * @roseuid 3F722E4B0121
     */
    public int size() {
        return this.messages.size();
    }
}
