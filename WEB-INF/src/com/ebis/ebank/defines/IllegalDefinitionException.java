package com.ebis.ebank.defines;

import java.util.ArrayList;
import java.util.List;

public class IllegalDefinitionException extends Exception {

    private ArrayList list = new ArrayList();
    public IllegalDefinitionException(String s) {
        super();
        list.add(s);
    }

    public void add(String s) {
        list.add(s);
    }
    public List get() {
        return list;
    }
}