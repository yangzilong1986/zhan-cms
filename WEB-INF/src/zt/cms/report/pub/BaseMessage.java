package zt.cms.report.pub;


import java.io.Serializable ;
import java.util.Enumeration ;
import java.util.Vector;
import java.util.ListIterator ;



/**
 * File Name: BaseMessage.java<br>
 * Author Ping Liu<br>
 * Date: 26-july-00<br>
 * Copyright (c) 2000 by AsiaEC.com, Inc. All Rights Reserved.<br>
 * reversion:<br>
 * This class is used as remote result package header infomation.<br>
 * if u wanna return a result from your ejb's method, better way is to return this object.even every type can be wrapped<br>
 * into this class.<br>
 * 1.You can return an object by asign the obj to m_objData.<br>
 * 2.You can return a result set by put your result set into an hash, dictionary, property or vector, whatever you like.<br>
 * remember to asign the set's enumeration interface to m_enuResults, that's the last count.<br>
 * usage :<br>
 * service side :<br>
 * 1.create a BaseMessage object.<br>
 * 2.set BaseMessge Type , RESULT_OBJECT, RESULT_SET are now on support.<br>
 * 3.attach your really result in two way<br>
 *   i)attach ur result object to m_objData;<br>
 *   ii)create a set of type Hash, Dictionary, Property or Vector.<br>
 *      add your result element into set.<br>
 *      get the enumeration interface from the set, attach the enumberation interface to m_enumresults ;<br>
 * 4.return BaseMessage object now.<br>
 * client side:<br>
 * 1.get the result from service , that is a BaseMessage Object;<br>
 * 2.switch the message type , ignore this step if you clear know what' in message object.<br>
 * 3.get result object from m_objData or get result set from m_enumResults with m_nCount plus.<br>
 * 4.that is all.<br>
 * @author Ping Liu
 */



public class BaseMessage implements Serializable

{

    /**
    * declare message type const .
    */

    public static final int UNKNOWN = 0x00000000 ;
    public static final int RESULT_OBJECT = 0x00000001 ;
    public static final int RESULT_SET = 0x00000002 ;
    public static final int RESULT_SET2 = 0x00000004 ;

    public int m_nType = UNKNOWN;//message type to be send out
    public int m_nCount = 0 ;//initially used as rowset count.
    public Object m_objData = null ;
    public Enumeration m_enumResults = null ;//result set enumeration.
    public ListIterator m_iteratorResults = null ;// result set ListIterator for bi-direction access

    /**
    * default constructure with UNKNOWN data type.
    */

    public BaseMessage()
    {
    }

    /**
    * construct an object BassMessage object with RESULT_OBJECT data type.
    * @param the object will be contained in this BaseMessage object. this parameter will be asigned to m_objData .
    */

    public BaseMessage( Object obj )
    {
        m_nType = BaseMessage.RESULT_OBJECT ;
        m_objData = obj ;
    }

    /**
    * construct an data set with RESULT_SET data type.
    * @param nCount elements count in this BaseMessage object.
    * @param e set of elements in this BaseMessage object.
    */

    public BaseMessage( int nCount , Enumeration e )
    {
        m_nType = BaseMessage.RESULT_SET ;
        m_nCount = nCount ;
        m_enumResults = e ;
    }

    /**
    * construct an data set with RESULT_SET2 data type.
    * @param nCount elements count in this BaseMessage object.
    * @param iterator set of elements in this BaseMessage object.
    */

    public BaseMessage( int nCount , ListIterator iterator )
    {
        m_nType = BaseMessage.RESULT_SET2 ;
        m_nCount = nCount ;
        m_iteratorResults = iterator ;
    }

    /**
    * @param vct vector of elements will be set to RESULT_SET2
    */

    public BaseMessage( Vector vct )
    {
        m_nType = BaseMessage.RESULT_SET2 ;
        m_nCount = vct.size() ;
        m_iteratorResults = vct.listIterator() ;
    }
}

