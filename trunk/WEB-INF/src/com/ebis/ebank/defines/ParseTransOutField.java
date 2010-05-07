package com.ebis.ebank.defines;

import org.apache.commons.digester.*;
import java.io.*;

public class ParseTransOutField {
    public static final String filename = "formoutfields.xml";
    public static final boolean VALIDXML = false;
    public static TransOutFieldManager parse(String path) {
        Digester digester = new Digester();
        digester.setValidating(VALIDXML);
        TransOutFieldManager manager = TransOutFieldManager.getInstance();
        digester.push(manager);

        digester.addCallMethod("forms/form","setTransID",1);
        digester.addSetProperties("forms/form","id","transID");

        digester.addObjectCreate("forms/form","java.util.ArrayList");

        digester.addObjectCreate("forms/form/outfield","com.ebis.ebank.defines.TransactionOutField");

//        digester.addCallMethod("transactions/transaction/field","setID",1);
//        digester.addSetProperties("transactions/transaction/field","id","id");

        digester.addCallMethod("forms/form/outfield/seqno","setSeqno",1);
        digester.addCallParam("forms/form/outfield/seqno",0);

        digester.addCallMethod("forms/form/outfield/name","setName",1);
        digester.addCallParam("forms/form/outfield/name",0);

        digester.addCallMethod("forms/form/outfield/len","setLen",1);
        digester.addCallParam("forms/form/outfield/len",0);

        digester.addCallMethod("forms/form/outfield/type","setType",1);
        digester.addCallParam("forms/form/outfield/type",0);

        digester.addCallMethod("forms/form/outfield/format","setFormat",1);
        digester.addCallParam("forms/form/outfield/format",0);

        digester.addCallMethod("forms/form/outfield/desc","setDesc",1);
        digester.addCallParam("forms/form/outfield/desc",0);

        digester.addCallMethod("forms/form/outfield/constant","isConstant",1);
        digester.addCallParam("forms/form/outfield/constant",0);

        digester.addCallMethod("forms/form/outfield/value","setValue",1);
        digester.addCallParam("forms/form/outfield/value",0);


        digester.addSetNext("forms/form/outfield","add","com.ebis.ebank.defines.TransactionOutField");

        digester.addSetNext("forms/form","add","java.util.ArrayList");

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