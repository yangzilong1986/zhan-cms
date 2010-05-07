package zt.cms.report.pub.equition;
import java.util.*;
/**
 * <p>Title: 信贷管理系统</p>
 * <p>Description: 潍坊信贷</p>
 * <p>Copyright: Copyright (c) 2003  中天信息技术有限公司</p>
 * <p>Company: 中天信息技术有限公司</p>
 * @author Yusg
 * @version 1.0
 */

public class Equition
{
    private String key;
    private Collection values;
    boolean changed = false;

    public Equition(String key,String values)throws IllegalArgumentException{
        this.key = key;
        this.values=new Vector();
        String theValue[]=values.split("\\+");
        for (int i = 0; i < theValue.length; i++) {
            try {
                Integer.parseInt(theValue[i]);
            }
            catch (Exception ex) {
                throw new IllegalArgumentException(values+", must be all Integers");
            }
            this.values.add(theValue[i]);
        }
    }

    public String getKey()
    {
        return key;
    }
    public void setKey(String key)
    {
        this.key = key;
    }
    public java.util.Collection getValues()
    {
        return values;
    }
    public void setValues(java.util.Collection values)
    {
        this.values = values;
    }

    public boolean addValues(Equition e)throws IllegalArgumentException{
        Collection deleteValues = new Vector();

        for (Iterator iter = this.values.iterator(); iter.hasNext(); ) {
            String value = (String)iter.next();
            if(value.equals(e.getKey())){
                if(e.valueHasKey(this.key)){
                    throw new IllegalArgumentException(this+" is wrong with "+e);
                }
                deleteValues.add(value);
            }
        }

        for (Iterator iter = deleteValues.iterator(); iter.hasNext(); ) {
            String item = (String)iter.next();
            this.values.remove(item);
            for (Iterator iter2 = e.getValues().iterator(); iter2.hasNext(); ) {
                String newValue = (String)iter2.next();
                this.values.add(new String(newValue));
            }
        }

        if(deleteValues.size()==0){
            return false;
        }else{
            return true;
        }
    }

    private boolean valueHasKey(String key){
        for (Iterator iter = this.getValues().iterator(); iter.hasNext(); ) {
            String item = (String)iter.next();
            if(key.equals(item)){
                return true;
            }
        }
        return false;
    }


    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append(this.key+"=");
        for (Iterator iter = this.values.iterator(); iter.hasNext(); ) {
            String item = (String)iter.next();
            sb.append(item+"+");
        }
        String result = sb.toString();
        return result.substring(0,result.length()-1);
    }




}
