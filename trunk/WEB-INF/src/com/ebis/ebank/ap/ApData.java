package com.ebis.ebank.ap;

import java.util.*;

public class ApData implements java.io.Serializable {
  private Vector ApDataSet = new Vector();

  public ApData() {
  }

  public int setData(int pos, Object obj) {
    if (obj == null) {
      return -1;
    }
    if (pos < 0) {
      return -1;
    }
    if (pos < this.ApDataSet.size()) {
      this.ApDataSet.set(pos, obj);
    }
    else {
      int i, j;
      j = pos - this.ApDataSet.size();
      for (i = 1; i <= j; i++) {
        this.ApDataSet.add(null);
      }
      this.ApDataSet.add(obj);
    }
    return 0;
  }

  public Object getData(int pos) {
    if (pos < 0 || pos >= this.ApDataSet.size()) {
      return null;
    }
    else {
      return this.ApDataSet.elementAt(pos);
    }
  }

  public int ClearAll() {
    this.ApDataSet.removeAllElements();
    return 0;
  }
}
