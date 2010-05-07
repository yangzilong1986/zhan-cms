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

/******************************************
 *
 *    打平公式的处理
 *
 *****************************************/


public class EquitionPool
{


    private java.util.Map equitions;
    public Equition getEquition(String key){
        return null;
    }
    public java.util.Map getEquitions()
    {
        return equitions;
    }
    public void setEquitions(java.util.Map equitions)
    {
        this.equitions = equitions;
    }

    public static EquitionPool buildEquitionPool(String source)throws IllegalArgumentException{
        EquitionPool pool = new EquitionPool();
        String[] es = source.split(",");
        Map map =new HashMap();
        for (int i = 0; i < es.length; i++) {
           String[] keys = es[i].split("=");
           if(keys.length!=2||keys[1].trim().equals("")||keys[0].trim().equals("")){
               throw new IllegalArgumentException(es[i]+" is not right!");
           }
           map.put(keys[0],new Equition(keys[0],keys[1]));
        }
        pool.setEquitions(map);
        return pool;
    }

    public String generateOut()throws IllegalArgumentException{
        boolean hasChanged = true;
        while(hasChanged){
            hasChanged = false;
            for (Iterator iter = this.equitions.keySet().iterator(); iter.hasNext(); ) {
                String key = (String) iter.next();
                Equition e = (Equition)this.equitions.get(key);
                for (Iterator iteEquition = this.equitions.values().iterator(); iteEquition.hasNext(); ) {
                    Equition item = (Equition) iteEquition.next();
                    if(e.addValues(item)){
                       hasChanged = true;
                   }
                }
            }
        }
       return this.toString();
    }


    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        for (Iterator iter = this.equitions.values().iterator(); iter.hasNext(); ) {
            Equition item = (Equition) iter.next();
            sb.append(item.toString() + ",");
        }
        return sb.toString().substring(0,sb.toString().length()-1);
    }

    public static void main(String[] args) {
        String source = "1=2+3+112,2=3+7,3=6+7+2,2=12+1+23";
        try {
            EquitionPool pool = EquitionPool.buildEquitionPool(source);
            System.out.println("---------------"+pool.generateOut()+"--------------");
            //System.out.println("-------------"+pool+"---------------");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            //ex.printStackTrace();
        }
    }
}
