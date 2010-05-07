package com.ebis.ebank.defines;

import org.apache.commons.digester.*;
import java.io.*;

public class ParseGateway {
    public static final String filename = "gateways.xml";
    public static final boolean VALIDXML = false;
    public static GatewayManager parse(String path) {
        Digester digester = new Digester();
        digester.setValidating(VALIDXML);
        GatewayManager manager = GatewayManager.getInstance();
        digester.push(manager);

        digester.addObjectCreate("gateways/gateway","com.ebis.ebank.defines.Gateway");
        digester.addCallMethod("gateways/gateway","setGatewayID",1);
        digester.addSetProperties("gateways/gateway","id","gatewayID");
        digester.addCallMethod("gateways/gateway/hostName","setHostName",1);
        digester.addCallParam("gateways/gateway/hostName",0);
        digester.addCallMethod("gateways/gateway/classHandler","setClassHandler",1);
        digester.addCallParam("gateways/gateway/classHandler",0);

        digester.addSetNext("gateways/gateway","add","com.ebis.ebank.defines.Gateway");

        try {
            if ( path == null )
                path = "";
            digester.parse(path+filename);
        } catch ( Exception e ) {
            Debug.debug(e.getMessage());
            return manager;
        }

        return manager;
    }
}