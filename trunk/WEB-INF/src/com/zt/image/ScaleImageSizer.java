package com.zt.image;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.math.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ScaleImageSizer extends AdvancedImageSizer
{
    private static final MediaTracker tracker = new MediaTracker( new Component() {} );
    public BufferedImage exportImage(Image image)
    {
        //wait for image, enable the getwidth and getheight method
        waitForImage(image);

        int formerWidth = image.getWidth(null);
        int formerHeight = image.getHeight(null);

        float formerRate = (float)formerHeight/formerWidth;
        float expectedRate = (float)this.height/this.width;

        int finalWidth = this.width;
        int finalHeight = this.height;

        if(Math.abs(formerRate-expectedRate)>=0.00001){
            if(formerRate<expectedRate){
                finalHeight = formerHeight * width / formerWidth ;
            }else{
                finalWidth = formerWidth * height / formerHeight ;
            }
        }
        BufferedImage bi = new BufferedImage(finalWidth, finalHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphic = bi.createGraphics();
        graphic.drawImage(image.getScaledInstance(finalWidth,finalHeight,java.awt.Image.SCALE_DEFAULT), 0, 0, null);
        return bi;
    }

    private static void waitForImage( Image image ) {
      try {
         tracker.addImage( image, 0 );
         tracker.waitForID( 0 );
         tracker.removeImage(image, 0);
      } catch( InterruptedException e ) {
          e.printStackTrace();
      }
   }


}
