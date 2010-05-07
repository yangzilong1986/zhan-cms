package com.ebis.ebank.ap;

import java.util.*;
import com.ebis.ebank.general.*;

public class ApMeta implements java.io.Serializable {
  private Vector MetaName = new Vector();
  private Vector MetaDesc = new Vector();
  private Vector MetaType = new Vector();
  private int MetaCount = 0; //base index is 1

  public ApMeta() {
  }

  public void ClearAll() {
    this.MetaCount = 0;
    this.MetaDesc.removeAllElements();
    this.MetaName.removeAllElements();
    this.MetaType.removeAllElements();
  }

  public int AddMeta(String colname, int coltype, String coldesc) {
    String toname, todesc;
    Integer totype;
    boolean errorfound = false;

    if (colname == null) {
      return -1;
    }
    if (DataType.ValidType(coltype) == false) {
      return -1;
    }

    if (this.MetaExist(colname) == true) {
      return -1;
    }

    toname = colname;
    if (coldesc == null) {
      todesc = toname;
    }
    else {
      todesc = coldesc;

    }
    totype = new java.lang.Integer(coltype);

    try {
      this.MetaName.add(MetaCount, toname);
      this.MetaDesc.add(MetaCount, todesc);
      this.MetaType.add(MetaCount, totype);
      this.MetaCount++;
    }
    catch (Exception e1) {
      errorfound = true;
      //throw e1;
    }
    finally {
      if (errorfound == true) {
        return -1;
      }
      else {
        return 0;
      }
    }
  }

  public int getMetaSize() {
    return (this.MetaCount < 0 ? 0 : this.MetaCount);
  }

  public String getMetaName(int metapos) {
    if (this.validateIndex(metapos) == false) {
      return null;
    }
    else {
      return (String)this.MetaName.elementAt(metapos);
    }
  }

  public int MetaName2Pos(String metaname) {
    int i;

    if (metaname == null) {
      return -1;
    }

    for (i = 0; i <= this.getMetaSize() - 1; i++) {
      if ( ( (String)this.MetaName.elementAt(i)).compareTo(metaname) == 0) {
        return i;
      }
    }

    return -1;
  }

  public String getMetaDesc(int metapos) throws java.lang.IndexOutOfBoundsException {
    if (this.validateIndex(metapos) == false) {
      return null;
    }
    else {
      return (String)this.MetaDesc.elementAt(metapos);
    }
  }

  public int getMetaType(int metapos) throws java.lang.IndexOutOfBoundsException {
    if (this.validateIndex(metapos) == false) {
      return -1;
    }
    else {
      return ( (Integer)this.MetaType.elementAt(metapos)).intValue();
    }
  }

  public boolean validateIndex(int idx) {
    if (idx < 0 || idx >= this.MetaCount) {
      return false;
    }
    else {
      return true;
    }
  }

  public boolean MetaExist(String metaname) {
    if (this.MetaName2Pos(metaname) >= 0) {
      return true;
    }
    else {
      return false;
    }
  }
}
