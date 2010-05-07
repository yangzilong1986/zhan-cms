package com.ebis.ebank.defines;

import org.apache.commons.digester.*;
import java.io.*;

public class ParseTransactionField {
    public static final String filename = "transactionfields.xml";
    public static final boolean VALIDXML = false;
    public static TransFieldManager parse(String path) {
        Digester digester = new Digester();
        digester.setValidating(VALIDXML);
        TransFieldManager manager = TransFieldManager.getInstance();
        digester.push(manager);

        digester.addCallMethod("transactions/transaction","setTransID",1);
        digester.addSetProperties("transactions/transaction","id","transID");

        digester.addObjectCreate("transactions/transaction","java.util.ArrayList");

        digester.addObjectCreate("transactions/transaction/field","com.ebis.ebank.defines.TransactionField");

//        digester.addCallMethod("transactions/transaction/field","setID",1);
//        digester.addSetProperties("transactions/transaction/field","id","id");

        digester.addCallMethod("transactions/transaction/field/seqno","setSeqno",1);
        digester.addCallParam("transactions/transaction/field/seqno",0);

        digester.addCallMethod("transactions/transaction/field/name","setName",1);
        digester.addCallParam("transactions/transaction/field/name",0);

        digester.addCallMethod("transactions/transaction/field/len","setLen",1);
        digester.addCallParam("transactions/transaction/field/len",0);

        digester.addCallMethod("transactions/transaction/field/type","setType",1);
        digester.addCallParam("transactions/transaction/field/type",0);

        digester.addCallMethod("transactions/transaction/field/format","setFormat",1);
        digester.addCallParam("transactions/transaction/field/format",0);

        digester.addCallMethod("transactions/transaction/field/ismac","setIsMac",1);
        digester.addCallParam("transactions/transaction/field/ismac",0);

        digester.addSetNext("transactions/transaction/field","add","com.ebis.ebank.defines.TransactionField");

        digester.addSetNext("transactions/transaction","add","java.util.ArrayList");

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