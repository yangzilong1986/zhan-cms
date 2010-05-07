package zt.platform.form.util;

import java.io.Serializable;

public class FormFieldValue implements Serializable{

    public final static int TYPE_VALUE = 0;
    public final static int TYPE_READONLY = 1;
    public final static int TYPE_DISABLE = 2;
    public final static int TYPE_COLOR = 3;
    public final static int TYPE_MESSAGE = 4;
    public final static int TYPE_HIDDEN = 5;
    public final static int TYPE_FOCUS = 6;

    private String name = null;
    private String value = null;
    private int valuetype = 0;

    /**
     * FormFieldValue
     */
    public FormFieldValue(String name1, String value1, int type1) {
        this.name = name1;
        this.value = value1;
        this.valuetype = type1;
    }

    public void setFldName(String name1) {
        if (name1 != null) this.name = name1;
    }

    public String getFldName() {
        return this.name;
    }

    public void setFldValue(String value1) {
        if (value1 != null) {
            this.value = value1;
        }
    }

    public Object getFldValue() {
        return this.value;
    }

    public int getFldType() {
        return this.valuetype;
    }

    public void setFldType(int type1) {
        this.valuetype = type1;
    }

}
