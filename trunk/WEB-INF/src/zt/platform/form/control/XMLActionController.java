package zt.platform.form.control;

import zt.platform.form.util.FormInstance;
import zt.platform.form.util.FormInstanceManager;
import zt.platform.form.util.SessionAttributes;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.event.EventType;
import zt.platform.utils.Debug;
import zt.platform.utils.ErrorCode;
//import org.jdom.*;
//import org.jdom.input.*;
//import org.jdom.output.*;
//import org.jdom.Document;
//import javax.servlet.ServletInputStream;
//import javax.servlet.http.HttpServletRequest;
//import org.w3c.dom.NodeList;
//import org.jdom.Element;
//import java.util.*;
//import java.io.*;
//import com.zt.util.PropertyManager;

/**
 * XML FORM processor
 * <p/>
 * author: JGO(GZL) on Aug 6,2005
 */

public class XMLActionController {
    private SessionContext ctx;
    public static final int POOL_MIN_SIZE = 20;

    /**
     * @param ctx
     * @roseuid 3F724F850093
     */
    public XMLActionController(SessionContext ctx) {
        this.ctx = ctx;
    }


    public int run() {
        try {
            //1.ctx�л��FormInstanceManager�������������²���
            FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
            if (fiManager == null) {
                return ErrorCode.FORM_INSTANCE_NOT_EXIST;
//        int size = PropertyManager.getIntProperty(SystemAttributeNames.FORM_INSTANCE_POOL_SIZE);
//        if (size < POOL_MIN_SIZE) {
//          size = POOL_MIN_SIZE;
//        }
//        fiManager = new FormInstanceManager(size);
//        ctx.setAttribute(SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME,fiManager);
            }
            //2.�����¼�������������ÿ�������һϵ���¼���
            EventManager eManager = new EventManager();
            Event firstEvent = findEvent(fiManager);//��request�л�ȡԭʼ�Ĵ����¼�
            if (firstEvent == null) {
                return ErrorCode.ERROR_NOT_EVENT;
            }
            eManager.trigger(firstEvent);//�����¼���Ϊ��һ���¼�ѹ���¼�������
            if (firstEvent.isInstance()) {//�����������Ѿ����ڵ�ʵ���������request����ʵ�����е���ֵ
                FormInstance fi = fiManager.getFormInstance(firstEvent.getInstanceid());
                if (fi != null) {
                    //fi.updateValue(ctx);
                    //this.updateParameters(fi);
                    //we don't call updateParameters because it is to be called in ControllerAssistor
                }
            }
            //4.�����¼�
            ErrorMessages errMsgs = new ErrorMessages();
            Event event = null;
            int result = 0;
            try {
                while (eManager.hasMoreEvent() && result >= 0) {
                    event = eManager.nextEvent();
                    //�����¼��������ǽ���ƽ̨�ĺ�������
                    result = ControllerAssistor.process(ctx, event, errMsgs, eManager, null);
                    if (event.getBranchType() == event.BRANCH_BREAK_TYPE) {
                        break;
                    }
                }
            }
            catch (Exception ex) {
                Debug.debug(ex);
            }
            catch (Throwable t) {
                t.printStackTrace();
            }

            //7.�γ�Form������
            String[] body = ViewController.process(ctx, event, errMsgs, result);
            if (body != null) {
                if (body.length >= 1) {
                    ctx.setBody(body[0]);
                }
                if (body.length >= 2) {
                    ctx.setSysButton(body[1]);
                }
            }
            return result;
        }
        catch (Exception e) {
            ctx.setHead("");
            ctx.setBody("");
            ctx.setTail("");
            return ErrorCode.EXCEPTION_THROWN;
        }
    }

    /**
     * ��requese�л�ȡinstanceid��formid��eventid��initParameters���������
     * ���instanceid��formid�Ĵ����Ժ�eventid�ĺϷ���
     * ���ո�����Щ���������¼�����
     */
    private Event findEvent(FormInstanceManager manager) {
        String instanceid = ctx.getParameter(SessionAttributes.REQUEST_INSATNCE_ID_NAME);
        //String formid = ctx.getParameter(SessionAttributes.REQUEST_FORM_ID_NAME);
        String eventid = ctx.getParameter(SessionAttributes.REQUEST_EVENT_ID_NAME);
        String initParameters = ctx.getParameter(SessionAttributes.REQUEST_FORM_INIT_PARAMETERS_NAME);
        instanceid = (instanceid == null ? null : instanceid.trim());
        //formid=(formid==null?null:formid.trim());
        eventid = (eventid == null ? null : eventid.trim());
        String id = "";
        int eventType = EventType.LOAD_EVENT_TYPE;
        boolean isInstance = false;

        //instanceid����
        if (instanceid != null && instanceid.length() > 0) {
            if (manager.getFormInstance(instanceid) == null) {
                ctx.setHead("");//��manager���Ҳ���insttance�����ڳ�ʱ
                ctx.setBody("");
                ctx.setTail("");
                return null;
            }
            if (eventid == null || !EventType.validate(eventid)) {
                ctx.setHead("");//��manager���Ҳ���insttance�����ڳ�ʱ
                ctx.setBody("");
                ctx.setTail("");
                return null;
            }
            id = instanceid;
            isInstance = true;
        } else {
            ctx.setHead("");//��manager���Ҳ���insttance�����ڳ�ʱ
            ctx.setBody("");
            ctx.setTail("");
            return null;
        }
        //eventType
        try {
            eventType = Integer.parseInt(eventid);
        }
        catch (Exception e) {
            return null;
        }
        //Event
        return new Event(id, eventType, Event.BRANCH_CONTINUE_TYPE, isInstance, initParameters);
    }

    /**
     * here to parse xml data from request and save then into request's parameters
     * @param fi FormInstance
     */
//  private void updateParameters(FormInstance fi)
//  {
//    if(fi == null) return;
//    HttpServletRequest req = null;
//    ServletInputStream is = null;
//    Document doc = null;
//    String namestr,valuestr;
//
//    try
//    {
//      req = ctx.getRequest();
//      if(req == null) return;
//      //req.setCharacterEncoding(PropertyManager.getProperty(SystemAttributeNames.WEB_SERVER_ENCODING));
//      is = req.getInputStream();
//
//      if(is == null) return;
//
//      String str = "";
//      byte[] bt = new byte[req.getContentLength()+1];
//      while(is.readLine(bt,0,req.getContentLength()) >= 0)
//      {
//        str += new String(bt);
//      }
//      if(str.trim().length() <= 0) return;
//      str = str.trim();
//
//      System.out.println("str="+str+"]");
//
//      Reader reader = new StringReader(str);
//
//      org.jdom.input.SAXBuilder sb = new org.jdom.input.SAXBuilder();
//      doc = sb.build(is);
//      Element el = doc.getRootElement();
//      List list = el.getChildren();
//      Iterator it = list.iterator();
//      while(it.hasNext())
//      {
//        el = (Element)it.next();
//        namestr = el.getChildText("name");
//        valuestr = el.getChildText("value");
//        if(namestr != null) namestr = namestr.trim();
//        if(namestr != null && namestr.length() > 0 && valuestr != null)
//        {
//          //System.out.println("XML LINE="+namestr+"="+valuestr);
//          ctx.setRequestAtrribute(namestr,valuestr);
//
//        }
//      }
//
//    }
//    catch(Exception e)
//    {
//      Debug.debug(e);
//    }
//
//  }
}
