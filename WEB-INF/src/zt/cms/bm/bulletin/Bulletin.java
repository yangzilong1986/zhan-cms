package zt.cms.bm.bulletin;

import java.text.*;
import java.util.*;
/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004Äê2ÔÂ13ÈÕ
 */
public class Bulletin {
    private int seqno;
    private int bulletinType;
    private String publishedBy;
    private String caption;
    private boolean link;
    private String content;
    private java.util.Calendar createDate;
    private String fullName;


    /**
     *  Constructor for the bulletin object
     */
    public Bulletin() { }


    /**
     *  Gets the seqno attribute of the bulletin object
     *
     *@return    The seqno value
     */
    public int getSeqno() {
        return seqno;
    }


    /**
     *  Sets the seqno attribute of the bulletin object
     *
     *@param  seqno  The new seqno value
     */
    public void setSeqno(int seqno) {
        this.seqno = seqno;
    }


    /**
     *  Gets the bullitinType attribute of the bulletin object
     *
     *@return    The bullitinType value
     */
    public int getBulletinType() {
        return bulletinType;
    }


    /**
     *  Sets the bullitinType attribute of the bulletin object
     *
     *@param  bulletinType  The new bulletinType value
     */
    public void setBulletinType(int bulletinType) {
        this.bulletinType = bulletinType;
    }


    /**
     *  Gets the publishedBy attribute of the bulletin object
     *
     *@return    The publishedBy value
     */
    public String getPublishedBy() {
        return publishedBy;
    }




    /**
     *  Sets the publishedBy attribute of the bulletin object
     *
     *@param  publishedBy  The new publishedBy value
     */
    public void setPublishedBy(String publishedBy) {
        this.publishedBy = publishedBy;
    }




    /**
     *  Gets the caption attribute of the bulletin object
     *
     *@return    The caption value
     */
    public String getCaption() {
        return caption;
    }


    /**
     *  Sets the caption attribute of the bulletin object
     *
     *@param  caption  The new caption value
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }


    /**
     *  Gets the link attribute of the bulletin object
     *
     *@return    The link value
     */
    public boolean isLink() {
        return link;
    }


    /**
     *  Sets the link attribute of the bulletin object
     *
     *@param  link  The new link value
     */
    public void setLink(boolean link) {
        this.link = link;
    }


    /**
     *  Gets the content attribute of the bulletin object
     *
     *@return    The content value
     */
    public String getContent() {
        return content;
    }



    /**
 *  Gets the content attribute of the bulletin object
 *
 *@return    The content value
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


    public String getTrimedCaption(){
        if (caption.length() >12) {
            return getCaption().substring(0, 12) + "...";
        }
        else {
            return getCaption();
        }
    }



    /**
     *  Sets the content attribute of the bulletin object
     *
     *@param  content  The new content value
     */
    public void setContent(String content) {
        this.content = content;
    }


    /**
     *  Gets the createDate attribute of the bulletin object
     *
     *@return    The createDate value
     */
    public String getCreateDateString() {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(this.getCreateDate().getTime().getTime()));
    }

    /**
 *  Gets the createDate attribute of the bulletin object
 *
 *@return    The createDate value
 */
public java.util.Calendar getCreateDate() {
    return createDate;
}



    /**
     *  Sets the createDate attribute of the bulletin object
     *
     *@param  createDate  The new createDate value
     */
    public void setCreateDate(java.util.Calendar createDate) {
        this.createDate = createDate;
    }

    public String toString(){
        StringBuffer sb=new StringBuffer();
        sb.append("\ngetBulletinType="+getBulletinType());
        sb.append("\ngetCaption="+getCaption());
        sb.append("\ngetContent="+getContent());
        sb.append("\ngetCreateDateString="+getCreateDateString());
        sb.append("\ngetPublishedBy="+getPublishedBy());
        sb.append("\ngetSeqno="+getSeqno());
        sb.append("\nisRule="+isRule());
        return sb.toString();
    }

    public boolean isRule(){
        if(this.getBulletinType()==2){
            return true;
        }else{
            return false;
        }
    }

    public String getURL(){
        return getContent();
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }



}
