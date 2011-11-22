package screencapture;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PictureTakerThread extends Thread
{
    private ConcurrentLinkedQueue<PicNode> data; 
    private boolean running;
    private boolean paused;
    
    /**
     * Default Constructor
     * Creates a new ConcurrentLinkedQueue to hold data taken from the PictureTakerThread.
     * Sets running to true; This makes our loop in run tick.
     */
    public PictureTakerThread(ConcurrentLinkedQueue<PicNode> data)
    {
        this.data = data;
        this.running = true;
        this.paused = false;
    }
    
    /**
     * Takes pictures of the desktop screen.
     */
    @Override
    public void run()
    {
        try 
        {
            // TODO: DEBUG prints out that the PictureTakerThread has started.
            System.out.println("PictureTaker has started.");
            Robot robot = new Robot();
            Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage bufferedImage;
            int counter = 0;
            while(running)
            { 
                while(paused){
                    this.yield();
                }
                
                // Sync Screen More For Linux/Mac
                //Toolkit.getDefaultToolkit().sync();
                // Create a buffered image of the screen.
                bufferedImage = robot.createScreenCapture(captureSize);
                // Add the image data to the ConcurrentLinkedQueue
                data.add(new PicNode(bufferedImage, counter + ".jpg"));
                // TODO: DEBUG: print out the current image count
                //System.out.println("Picture: " + counter);
                // Increase the image counter.
                counter++; 
                // Sleep for a bit
                //this.yield();
                try{this.sleep(ScreenCapture.SLEEP_TIME);}catch(Exception ex){System.out.println("Error Sleeping.");}
            }
            // TODO: DEBUG prints out that the PictureTakerThread has ended.
            System.out.println("PictureTakerThread has ended.");
        }
        catch(AWTException ex)
        { 
            System.out.println("AWT-EXCEPTION");
        }    
    }
    
    /**
     * Ends the main loop within the run method.
     */
    public synchronized void kill()
    {
        this.running = false;
    }
    
    public synchronized void pause(){        
        this.paused = !this.paused;
        //TODO: DEBUG - prints out if pictureTakerThread was paused
        System.out.println("PT thread is paused: " + this.paused);
    }
}
