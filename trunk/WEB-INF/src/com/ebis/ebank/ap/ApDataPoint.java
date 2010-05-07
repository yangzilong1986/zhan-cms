package com.ebis.ebank.ap;

import java.util.*;
import com.ebis.ebank.general.*;

public class ApDataPoint implements java.io.Serializable {
  private Vector ApDataAry = new Vector(); //ApDataExch object
  private Vector ApBlock = new Vector(); //block name, string
  private int AryCount = 0; //block size
  private int CurrentBlock = 0; //current block

  static String TempBlockName = "TEMP_BLOCK_NAME_1_2";
  private ApDataSpec apDataSpec = null;
  private boolean RequestSuccessed = false;

  public void setRequestSuccessed(boolean successed) {
    this.RequestSuccessed = successed;
  }

  public boolean getRequestSuccessed() {
    return this.RequestSuccessed;
  }

  public ApDataPoint(ApDataSpec dataspec) {
    if (dataspec != null) {
      this.apDataSpec = dataspec;
    }
  }

  public ApDataSpec getApDataSpec() {
    return this.apDataSpec;
  }

  public boolean blockExist(String blkname) {
    if (this.getBlockNumber(blkname) >= 0) {
      return true;
    }
    else {
      return false;
    }
  }

  public int addBlock(String blkname) throws java.lang.IllegalArgumentException {
    ApDataExch exch;
    boolean errorfound = false;
    if (blkname == null) {
      return -1;
    }
    if (this.blockExist(blkname) == true) {
      return -1;
    }
    try {
      exch = new ApDataExch();
      this.ApBlock.add(this.AryCount, blkname);
      this.ApDataAry.add(this.AryCount, exch);
      this.AryCount++;
      this.CurrentBlock = this.AryCount - 1;
    }
    catch (Exception e) {
      errorfound = true;
    }
    if (errorfound == true) {
      return -1;
    }
    else {
      return 0;
    }
  }

  public int setCurrentBlock(String blkname) {
    int blkno;
    if (blkname == null) {
      if (this.AryCount > 0) {
        this.CurrentBlock = 0;
        return 0;
      }
      else {
        return -1;
      }
    }

    blkno = this.getBlockNumber(blkname);
    if (blkno >= 0) {
      this.CurrentBlock = blkno;
      return 0;
    }
    else {
      return -1;
    }
  }

  private ApDataExch returnCurrentBlock() {
    if (this.getBlockSize() == 0) {
      this.addBlock(this.TempBlockName);
      this.setCurrentBlock(0);
    }

    if (this.CurrentBlock < 0 || this.CurrentBlock >= this.AryCount) {
      throw new java.lang.ArrayIndexOutOfBoundsException();
    }
    else {
      return (ApDataExch)this.ApDataAry.elementAt(this.CurrentBlock);
    }
  }

  public int setCurrentBlock(int blkno) {
    if (blkno < 0 || blkno >= this.AryCount) {
      return -1;
    }
    else {
      this.CurrentBlock = blkno;
      return 0;
    }
  }

  public int getBlockSize() {
    return this.AryCount < 0 ? 0 : this.AryCount;
  }

  public String getBlockName(int blkno) {
    if (blkno < 0 || blkno >= this.AryCount) {
      return null;
    }
    else {
      return (String)this.ApBlock.elementAt(blkno);
    }
  }

  public int getBlockNumber(String blkname) {
    int i;
    if (blkname == null) {
      return -1;
    }

    for (i = 0; i <= this.getBlockSize() - 1; i++) {
      if ( ( (String)this.ApBlock.elementAt(i)).compareTo(blkname) == 0) {
        return i;
      }
    }

    return -1;

  }

  /**
      @param name
      @param value
      @return int
      @roseuid 3DDE612101CD
   */
  public int setData(String name, Object value) {
    return this.returnCurrentBlock().setData(name, value);
  }

  public int setData(int ColSeqNo, Object value) {
    return this.returnCurrentBlock().setData(ColSeqNo, value);
  }

  public int addMeta(String metaname, int metatype, String metadesc) {
    return this.returnCurrentBlock().addMeta(metaname, metatype, metadesc);
  }

  public int getMetaSize() {
    return this.returnCurrentBlock().getMetaSize();
  }

  public String getMetaName(int metapos) {
    return this.returnCurrentBlock().getMetaName(metapos);
  }

  public int getMetaPos(String metaname) {
    return this.returnCurrentBlock().getMetaPos(metaname);
  }

  public String getMetaDesc(int metapos) {
    return this.returnCurrentBlock().getMetaDesc(metapos);
  }

  public int getMetaType(int metapos) {
    return this.returnCurrentBlock().getMetaType(metapos);
  }

  public int getDataSize() {
    return this.returnCurrentBlock().getDataSize();
  }

  public void clearCurrentBlockAll() {
    this.returnCurrentBlock().clearAll();
  }

  public void clearAll() {
    int i;
    for (i = 0; i <= this.getBlockSize() - 1; i++) {
      ( (ApDataExch)this.ApDataAry.elementAt(i)).clearAll();
    }
    this.ApBlock.removeAllElements();
    this.AryCount = 0; //block size
    this.CurrentBlock = 0; //current block
    this.setRequestSuccessed(false);
  }

  public void clearCurrentBlockData() {
    this.returnCurrentBlock().clearData();
  }

  public int addRow() {
    return this.returnCurrentBlock().addRow();
  }

  public Object getData(String metaname) {
    return this.returnCurrentBlock().getData(metaname);
  }

  public Object getData(int metapos) throws java.lang.IllegalArgumentException {
    return this.returnCurrentBlock().getData(metapos);
  }

  public boolean initRead() {
    return this.returnCurrentBlock().initRead();
  }

  public boolean moveNext() {
    return this.returnCurrentBlock().moveNext();
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < getMetaSize(); i++) {
      initRead();
      if (!eof()) {
        sb.append("["+zt.cmsi.biz.util.fixLenString(getMetaName(i),20) + "][" + checkNull(getData(i)) + "]\n");
      }
    }
    return sb.toString();
  }

  private String checkNull(Object object) {
    if (object == null) {
      return "null";
    }
    else {
      return object.toString();
    }
  }

  public static void toString(ApDataPoint ap) {
    if (ap == null) {
      return;
    }
    int count, i;
    System.out.println("request flag is:" + ap.getRequestSuccessed());
    for (count = 0; count < ap.getBlockSize(); count++) {
      ap.setCurrentBlock(count);
      System.out.println("Current block is:" + ap.getBlockName(count));
      for (i = 0; i <= (ap.getMetaSize() - 1); i++) {
        System.out.print(ap.getMetaName(i) + "(" + ap.getMetaDesc(i) + "/" + ap.getMetaType(i) + ")" + " | ");
      }
      ap.initRead();
      //ap.setCurrentBlock("ML00");
      for (; !ap.eof(); ) {
        for (i = 0; i <= (ap.getMetaSize() - 1); i++) {
          System.out.print(ap.getData(i) + " | ");
        }
        System.out.println(" ");
        ap.moveNext();
      }

    }
  }

  public boolean eof() {
    return this.returnCurrentBlock().eof();
  }

  public boolean metaExist(String metaname) {
    return this.returnCurrentBlock().MetaExist(metaname);
  }

  public static void main(String argv[]) {
    int i, j;
    ApDataSpec dataspec = new ApDataSpec("apid", "casubject", true);
    ApDataPoint exch = new ApDataPoint(null);

    java.util.Date date1 = new java.util.Date(234234);
    java.util.Date date2 = new java.util.Date(234234);
    Double float1 = new java.lang.Double(123.423);
    Double float2 = new java.lang.Double(67455.423);
    String string1 = new String("jgo eyes");
    String string2 = new String("sadf eyes");

    exch.addBlock("block1");
    exch.addBlock("block2");

    exch.setCurrentBlock("block1");
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

    exch.setCurrentBlock("block2");
    exch.addMeta("col1", DataType.TypeDate, "column1");
    exch.addMeta("col2", DataType.TypeNumber, "column2");
    exch.addMeta("col3", DataType.TypeString, null);

    exch.addRow();
    exch.setData(0, date1);
    exch.setData("col2", float1);
    exch.setData("col3", string1);

    exch.setCurrentBlock(0);
    System.out.println(exch.getBlockName(0));
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

    exch.setCurrentBlock(1);
    System.out.println(exch.getBlockName(1));
    for (i = 0; i <= (exch.getMetaSize() - 1); i++) {
      System.out.print(exch.getMetaName(i) + "(" + exch.getMetaDesc(i) + "/" + exch.getMetaType(i) + ")" + " | ");
    }

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
