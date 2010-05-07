package zt.cms.bm.bulletin;

import java.text.*;
import java.util.*;
/**
 *  Description of the Class
 *
 *@author     rock
 *@created    2004Äê2ÔÂ19ÈÕ
 */
public class Msg {
    private int msgNo;
    private java.util.Calendar createDate;
    private String fromUser;
    private String toUser;
    private String content;
    private boolean readFlag;
    private String fullName;


    /**
     *  Constructor for the Msg object
     */
    public Msg() { }


    /**
     *  Gets the msgNo attribute of the Msg object
     *
     *@return    The msgNo value
     */
    public int getMsgNo() {
        return msgNo;
    }


    /**
     *  Sets the msgNo attribute of the Msg object
     *
     *@param  msgNo  The new msgNo value
     */
    public void setMsgNo(int msgNo) {
        this.msgNo = msgNo;
    }


    /**
     *  Gets the createDate attribute of the Msg object
     *
     *@return    The createDate value
     */
    public java.util.Calendar getCreateDate() {
        return createDate;
    }


    /**
     *  Sets the createDate attribute of the Msg object
     *
     *@param  createDate  The new createDate value
     */
    public void setCreateDate(java.util.Calendar createDate) {
        this.createDate = createDate;
    }


    /**
     *  Gets the fromUser attribute of the Msg object
     *
     *@return    The fromUser value
     */
    public String getFromUser() {
        return fromUser;
    }


    /**
     *  Sets the fromUser attribute of the Msg object
     *
     *@param  fromUser  The new fromUser value
     */
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getTrimedContent(){
    if(content!=null){
        if (content.length() > 12) {
            return getFilteredContent().substring(0, 12) + "...";
        }
        else {
            return getFilteredContent();
        }
    }else{
        return "";
    }
}



    /**
     *  Gets the toUser attribute of the Msg object
     *
     *@return    The toUser value
     */
    public String getToUser() {
        return toUser;
    }


    /**
     *  Sets the toUser attribute of the Msg object
     *
     *@param  toUser  The new toUser value
     */
    public void setToUser(String toUser) {
        this.toUser = toUser;
    }


    /**
     *  Gets the content attribute of the Msg object
     *
     *@return    The content value
     */
    public String getContent() {
        return content;
    }


    /**
     *  Gets the filteredContent attribute of the Msg object
     *
     *@return    The filteredContent value
     */
    public String getFilteredContent() {
        if(content!=null){
            return content.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\\r\\n",
                "<br>").replaceAll("\\s", "&nbsp;")
                .replaceAll("\\\"", "&quot;");
        }else{
            return "";
        }
    }


    /**
     *  Sets the content attribute of the Msg object
     *
     *@param  content  The new content value
     */
    public void setContent(String content) {
        this.content = content;
    }


    /**
     *  Gets the readFlag attribute of the Msg object
     *
     *@return    The readFlag value
     */
    public boolean isReadFlag() {
        return readFlag;
    }


    /**
     *  Sets the readFlag attribute of the Msg object
     *
     *@param  readFlag  The new readFlag value
     */
    public void setReadFlag(boolean readFlag) {
        this.readFlag = readFlag;
    }


    /**
     *  Gets the createDateString attribute of the Msg object
     *
     *@return    The createDateString value
     */
    public String getCreateDateString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(this.getCreateDate().getTime().getTime()));
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\ngetContent:" + getContent());
        sb.append("\ngetCreateDateString:" + getCreateDateString());
        sb.append("\ngetFromUser:" + getFromUser());
        sb.append("\ngetMsgNo:" + getMsgNo());
        sb.append("\ngetToUser:" + getToUser());
        return sb.toString();
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
