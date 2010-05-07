package com.zt.image;

import java.awt.image.BufferedImage;
import java.awt.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class FixedImageSizer extends AdvancedImageSizer
{
    public BufferedImage exportImage(Image image)
    {
        BufferedImage bi = new BufferedImage( this.width, this.height, BufferedImage.TYPE_INT_RGB );
        Graphics2D graphic = bi.createGraphics();
        graphic.drawImage( image.getScaledInstance(this.width,this.height,java.awt.Image.SCALE_DEFAULT), 0, 0, null );
        return bi;
    }
}
