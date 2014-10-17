/****************************************
 * 
 * 
 * @author ARUN
 *
 *Created on 28-4-2007 
 */

package arun.puyo.v1_1;

//import java.awt.image.BufferedImage;
import java.awt.MediaTracker;
import java.awt.Image;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java .util.HashMap;
import javax.imageio.ImageIO;
import java.net.URL;
import java.awt.Toolkit;

public class ImageHolder {

	//A hashmap holding the images
	private static HashMap images = new HashMap();
	private static HashMap imageNames = new HashMap();

	private int waitedTime=0;
	
	static//Hope this block gets executed - its really amazing java providing such a functionality
	{
		imageNames.put(AppConsts.PUYO_GREEN,AppConsts.FILE_PUYO_GREEN);
		imageNames.put(AppConsts.PUYO_BLUE,AppConsts.FILE_PUYO_BLUE);
		imageNames.put(AppConsts.PUYO_RED,AppConsts.PUYO_RED);
		imageNames.put(AppConsts.PUYO_YELLOW,AppConsts.FILE_PUYO_YELLOW);		
	}
	
	/*
	 * This methos loads the image with the specified filename, using the ImageIO methid
	 * Unlike the getImage method, i think this is not an asynchronous method
	 * I hate asynchronous methods, whats the point if a method returns if it is still not finished
	 */
	public /*static*/ void loadImage(String key, String filename, GamePanel gp)
	{
		/*try {*/
			
			//URL url=getClass().getClassLoader().getResource(filename);
			//this one doesnt seem to work on some jdk, real struggle
			/*BufferedImage img=ImageIO.read(new File(filename));*/
			//BufferedImage img=ImageIO.read(url);	
			Toolkit kit = Toolkit.getDefaultToolkit();
			Image img=kit.getImage(filename);
			//Wait for the images to get loaded, dont know what else to do
			//this may lead to infinite loop, so trying to add timeout
			waitedTime=0;
//			giving a 2sec time for each image
		     while (waitForImage(img,gp)==false)
		     { 
					try {
						Thread.currentThread().sleep(500);	
					} catch (InterruptedException e) {}
					waitedTime+=500;
					if(waitedTime>2000){System.out.println("FATAL ERROR! Images not Found!");System.exit(-1);}	 
		     }	
		     
		     
			images.put(key,img);
		/*} catch (IOException e) {
			MyLogger.log(filename +" not found");
			//e.printStackTrace();
		}*/
		
	}
	
	public /*static*/ Image getImage(String key)
	{
		Image img = (Image) images.get(key);
		if (img==null)
		{
			//Quite a polite comment
			/*MyLogger.log("Hello Mr.Arun, It looks like you are trying to access an unloaded image");
			MyLogger.log("Ok! Let me try to load it for you again");
			loadImage(key,(String)imageNames.get(key));*/
			MyLogger.log("Critical Error! Image not Loaded!!");
			System.exit(0);
		}
		return img;
		
	}
	
	//THis is a specific method that allows to load the puyo images
	public /*static*/ void loadPuyoImages(GamePanel gp)
	{
		loadImage(AppConsts.PUYO_GREEN,AppConsts.FILE_PUYO_GREEN,gp);
		loadImage(AppConsts.PUYO_BLUE,AppConsts.FILE_PUYO_BLUE,gp);
		loadImage(AppConsts.PUYO_RED,AppConsts.FILE_PUYO_RED,gp);
		loadImage(AppConsts.PUYO_YELLOW,AppConsts.FILE_PUYO_YELLOW,gp);
	}
	
	public static int getWidth(String key, GamePanel gp)
	{
		return ((Image)images.get(key)).getWidth(gp);
		
	}
	
	public static int getHeight(String key, GamePanel gp)
	{
		return ((Image)images.get(key)).getHeight(gp);
		
	}	
	
	public static int getPuyoWidth(GamePanel gp)
	{
		//For the time being iam doing this, later this must be modified
		String key=AppConsts.PUYO_GREEN;
		return ((Image)images.get(key)).getWidth(gp);
		
	}
	
	public static int getPuyoHeight(GamePanel gp)
	{
		//For the time being iam doing this, later this must be modified
		String key=AppConsts.PUYO_GREEN;		
		return ((Image)images.get(key)).getHeight(gp);
		
	}		
	
  private boolean waitForImage(Image image, Component c) {
    MediaTracker tracker = new MediaTracker(c);
    tracker.addImage(image, 0);
    try {
      tracker.waitForAll();
    } catch(InterruptedException ie) {}
    //Need to find out what is the logic behind this
    return(!tracker.isErrorAny());
  }	
}
