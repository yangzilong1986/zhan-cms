package com.ebis.ebank.ap;

import java.util.*;

import com.ebis.ebank.general.*;

public class ApDataExch implements java.io.Serializable {
  private Vector ApDataSet = new Vector();
  private ApMeta MetaData = new ApMeta();
  private int CurrentCursor = -1;
  private int RowCount = 0;

  public ApDataExch() {
  }

  public int setData(String name, Object value) {
    int col = this.getMetaPos(name);
    if (col >= 0) {
      return this.setData(col, value);
    }
    else {
      return -1;
    }
  }

  public int setData(int ColSeqNo, Object value) {
    //if(ColSeqNo < 0 || ColSeqNo >= this.MetaData.getMetaSize())
    if (this.MetaData.validateIndex(ColSeqNo) == false) {
      return -1;
    }
    if (this.ApDataSet.size() <= 0) {
      return -1;
    }
    if (this.CurrentCursor < 0 || this.CurrentCursor > (this.RowCount - 1)) {
      return -1;
    }

    return ( (ApData)this.ApDataSet.elementAt(this.CurrentCursor)).setData(ColSeqNo, value);
  }

  public int addMeta(String metaname, int metatype, String metadesc) {
    return this.MetaData.AddMeta(metaname, metatype, metadesc);
  }

  public int getMetaSize() {
    return this.MetaData.getMetaSize();
  }

  public String getMetaName(int metapos) {
    return this.MetaData.getMetaName(metapos);
  }

  public int getMetaPos(String metaname) {
    return this.MetaData.MetaName2Pos(metaname);
  }

  public String getMetaDesc(int metapos) {
    return this.MetaData.getMetaDesc(metapos);
  }

  public int getMetaType(int metapos) {
    return this.MetaData.getMetaType(metapos);
  }

  public int getDataSize() {
    return (this.RowCount < 0 ? 0 : this.RowCount);
  }

  public void clearAll() {
    this.clearData();
    this.MetaData.ClearAll();

  }

  public void clearData() {
    this.ApDataSet.removeAllElements();
    this.RowCount = 0;
    this.CurrentCursor = -1;
  }

  public int addRow() {
    ApData pData = new ApData();
    this.ApDataSet.add(pData);
    this.RowCount++;
    this.CurrentCursor = this.RowCount - 1;
    return 0;
  }

  public Object getData(String metaname) {
    int col;
    col = getMetaPos(metaname);
    if (col >= 0) {
      return this.getData(col);
    }
    else {
      return null;
    }
  }

  public Object getData(int metapos) {
    if (this.MetaData.validateIndex(metapos) == false) {
      return null;
    }
    if (this.ApDataSet.size() <= 0) {
      return null;
    }
    if (this.CurrentCursor < 0 || this.CurrentCursor > (this.RowCount - 1)) {
      return null;
    }

    return ( (ApData)this.ApDataSet.elementAt(this.CurrentCursor)).getData(metapos);
  }

  public boolean initRead() {
    if (this.RowCount <= 0) {
      this.CurrentCursor = -1;
    }
    else {
      this.CurrentCursor = 0;
    }

    return true;
  }

  public boolean moveNext() {
    if (this.CurrentCursor >= 0 && (this.CurrentCursor) < this.RowCount) {
      this.CurrentCursor++;
      return true;
    }
    else {
      return false;
    }
  }

  public boolean MetaExist(String metaname) {
    return this.MetaData.MetaExist(metaname);
  }

  public boolean eof() {
    if (this.CurrentCursor >= 0 && (this.CurrentCursor) < this.RowCount) {
      return false;
    }
    else {
      return true;
    }
  }

  static public void main(String argv[]) {
    int i, j;
    ApDataExch exch = new ApDataExch();
    java.util.Date date1 = new java.util.Date(234234);
    java.util.Date date2 = new java.util.Date(234234);
    Double float1 = new java.lang.Double(123.423);
    Double float2 = new java.lang.Double(67455.423);
    String string1 = new String("jgo eyes");
    String string2 = new String("sadf eyes");

    exch.addMeta("col1", DataType.TypeDate, "column1");
    exch.addMeta("col2", DataType.TypeNumber, "column2");
    exch.addMeta("col3", DataType.TypeString, null);

    exch.addRow();
    exch.setData(0, date1);
    exch.setData("col2", float1);
    exch.setData("col3", string1);

    exch.addRow();
    exch.setData(0, date2);
    exch.setData("col2", float2);
    exch.setData("col3", string2);

    for (i = 0; i <= (exch.getMetaSize() - 1); i++) {
      System.out.print(exch.getMetaName(i) + "(" + exch.getMetaDesc(i) + "/" + exch.getMetaType(i) + ")" + " | ");
    }
    System.out.println(" ");
    System.out.println("===================================");

    exch.initRead();
    for (; !exch.eof(); ) {
      for (i = 0; i <= (exch.getMetaSize() - 1); i++) {
        System.out.print(exch.getData(i) + " | ");
      }
      System.out.println(" ");
      exch.moveNext();
    }

    exch.clearAll();
  }
}
