package com.ebis.ebank.defines;

import org.apache.commons.digester.*;
import java.io.*;

public class ParseTransaction {
    public static final String filename = "transactions.xml";
    public static final boolean VALIDXML = false;
    public static TransactionManager parse(String path) {
        Digester digester = new Digester();
        digester.setValidating(VALIDXML);
        TransactionManager manager = TransactionManager.getInstance();
        digester.push(manager);

        digester.addObjectCreate("transactions/transaction","com.ebis.ebank.defines.Transaction");
        digester.addCallMethod("transactions/transaction","setId",1);
        digester.addSetProperties("transactions/transaction","id","id");

        digester.addCallMethod("transactions/transaction/hostTP","setHostTP",1);
        digester.addCallParam("transactions/transaction/hostTP",0);

        digester.addCallMethod("transactions/transaction/gatewayID","setGatewayID",1);
        digester.addCallParam("transactions/transaction/gatewayID",0);

        digester.addCallMethod("transactions/transaction/hostTransID","setHostTransID",1);
        digester.addCallParam("transactions/transaction/hostTransID",0);

        digester.addCallMethod("transactions/transaction/transOutID","setTransOutID",1);
        digester.addCallParam("transactions/transaction/transOutID",0);

        digester.addCallMethod("transactions/transaction/serviceClass","setServiceClass",1);
        digester.addCallParam("transactions/transaction/serviceClass",0);


        digester.addSetNext("transactions/transaction","add","com.ebis.ebank.defines.Transaction");

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