package com.zt.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

// com.sun.image.codec.jpeg package is included in sun and ibm sdk 1.3
import com.sun.image.codec.jpeg.*;

/**
 * Use this class to size images.
 *
 * @author Dan Becker
 */
public class ImageSizer {
    public static final MediaTracker tracker = new MediaTracker( new Component() {} );
   /** Adjusts the size of the image to the given coordinates.
     * If width or height is -1, the image aspect ration is maintained.
     */
   public static Image setSize( Image image, int width, int height ) {
      return setSize( image, width, height, java.awt.Image.SCALE_DEFAULT );
   } // setSize

   /** Adjusts the size of the image to the given coordinates.
     * If width or height is -1, the image aspect ration is maintained.
     * <p>
     * Hints are one of SCALE_DEFAULT, SCALE_FAST, SCALE_SMOOTH,
     * SCALE_REPLICATE, SCALE_AREA_AVERAGING as defined in java.awt.Image.
     */
   public static Image setSize( Image image, int width, int height, int hints ) {
      return image.getScaledInstance( width, height, hints );
   } // setSize

   /** Checks the given image for valid width and height. */
   public static void checkImage( Image image ) {
      waitForImage( image );
      int imageWidth = image.getWidth( null );
      if ( imageWidth < 1 )
         throw new IllegalArgumentException( "image width " + imageWidth + " is out of range" );
      int imageHeight = image.getHeight( null );
      if ( imageHeight < 1 )
         throw new IllegalArgumentException( "image height " + imageHeight + " is out of range" );
      // System.out.println( "Image size=" + imageWidth + "x" + imageHeight );
   } // checkImage

   /** Waits for given image to load. Use before querying image height/width/colors. */
   public static void waitForImage( Image image ) {
      try {
         tracker.addImage( image, 0 );
         tracker.waitForID( 0 );
         // loadStatus = tracker.statusID( 0, false );
         tracker.removeImage(image, 0);
      } catch( InterruptedException e ) { e.printStackTrace(); }
   } // waitForImage

   /** Encodes the given image at the given quality to the output stream. */
   public static void encodeJPEG( OutputStream outputStream, Image outputImage, float outputQuality )
      throws java.io.IOException {
      int outputWidth  = outputImage.getWidth( null );
      if ( outputWidth < 1 )
         throw new IllegalArgumentException( "output image width " + outputWidth + " is out of range" );
      int outputHeight = outputImage.getHeight( null );
      if ( outputHeight < 1 )
         throw new IllegalArgumentException( "output image height " + outputHeight + " is out of range" );

      // Get a buffered image from the image.
      BufferedImage bi = new BufferedImage( outputWidth, outputHeight,
         BufferedImage.TYPE_INT_RGB );
      Graphics2D biContext = bi.createGraphics();
      biContext.drawImage( outputImage, 0, 0, null );
      // Note that additional drawing such as watermarks or logos can be placed here.

      // com.sun.image.codec.jpeg package is included in sun and ibm sdk 1.3
      JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( outputStream );
      // The default quality is 0.75.
      JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam( bi );
      jep.setQuality( outputQuality, true );
      encoder.encode( bi, jep );
      // encoder.encode( bi );
      outputStream.flush();
   } // encodeImage

   /** Test program */
   public static void main( String [] args )
      throws java.io.IOException {
      if (args.length < 4) {
         System.out.println( "ImageSizer - changes the size of an image." );
         System.out.println( "use: java ImageSizer inputFile outputFile outputWidth outputQuality" );
         return;
      }
      // Gather input arguments.
      String inputFileName = args[ 0 ];
      String outputFileName = args[ 1 ];
      int outputWidth = Integer.parseInt( args[ 2 ] );
      if ( outputWidth < 1 )
         throw new IllegalArgumentException( "output width \"" + args[ 2 ] +"\" out of range" );
      float outputQuality = Float.parseFloat( args[ 3 ] );
      if (( outputQuality < 0.0F ) || ( outputQuality > 1.0F ))
         throw new IllegalArgumentException( "output quality \"" + args[ 3 ] +"\" out of range" );

      // Get input image
      Image inputImage = Toolkit.getDefaultToolkit().getImage( inputFileName );
      checkImage( inputImage );
      // System.out.println( "Input image size=" + inputImage.getWidth( null ) + "x" + inputImage.getHeight( null ) );

      // Create output image.
      Image outputImage = ImageSizer.setSize( inputImage, outputWidth, -1 );
      checkImage( outputImage );
      // System.out.println( "Output image size=" + outputImage.getWidth( null ) + "x" + outputImage.getHeight( null ) );

      // Encode JPEG file.
      FileOutputStream fos = new FileOutputStream( outputFileName );
      encodeJPEG( fos, outputImage, outputQuality );
      fos.close();
      // For some reason, the MediaTracker/ImageProducer likes to hang on.
      System.exit( 0 );
   } // main
} // class ImageSizer
