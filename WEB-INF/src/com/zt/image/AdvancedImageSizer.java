package com.zt.image;
import javax.imageio.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public abstract class AdvancedImageSizer
{
    int width = 640;
    int height = 480;

    public static AdvancedImageSizer getInstance(int width ,int height ,String type){
        AdvancedImageSizer sizer = null;
        if(type!=null&&type.equals("scale")){
            sizer = new ScaleImageSizer();
        }else{
            sizer = new  FixedImageSizer();
        }

        if(width<0){
            throw new IllegalArgumentException("Width could not be negative!");
        }
        if(height<0){
            throw new IllegalArgumentException("Height could not be negative!");
        }
        sizer.width = width;
        sizer.height = height;
        return sizer;
    }
    public static AdvancedImageSizer getInstance(int width ,int height){
        return getInstance(width,height,"normal");
    }


    public abstract BufferedImage exportImage(Image image);


}
