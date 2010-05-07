package com.zt.image;
import javax.imageio.*;
import javax.imageio.stream.*;
import java.io.*;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.util.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class JpegImageWriter
{
    public void writeImage(RenderedImage in,File outFile,float compressionQuality){
        ImageWriter writer = (ImageWriter)ImageIO.getImageWritersByFormatName("jpg").next();

        ImageOutputStream ios = null;
        try {
            ios = ImageIO.createImageOutputStream(outFile);
            writer.setOutput(ios);
            ImageWriteParam param = new JPEGImageWriteParam(Locale.getDefault());
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT) ;
            param.setCompressionQuality(compressionQuality);

            writer.write(null, new IIOImage(in, null, null), param);

            ios.flush();
            writer.dispose();
            ios.close();

        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
