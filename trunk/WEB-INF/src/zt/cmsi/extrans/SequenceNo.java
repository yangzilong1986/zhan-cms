package zt.cmsi.extrans;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class SequenceNo {
    public static long tagCounter;

    /**
     * @roseuid 3E50E89400A7
     */
    public SequenceNo() {
        tagCounter = 1;
    }


    /**
     * @return com.zt.plugflow.iopool.IOTag
     * @roseuid 3E50D8E900E6
     */
    public static synchronized long nextSeqNo() {
        tagCounter++;
        return tagCounter;
    }

}