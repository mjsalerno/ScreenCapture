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
    private Timer timer;
    
    
    public PictureTakerThread()
    {
        this.data = new ConcurrentLinkedQueue<PicNode>();
        this.running = false;
        this.timer = new Timer();
    }

    public PictureTakerThread(long time) {
        this.data = new ConcurrentLinkedQueue<PicNode>();
        this.running = false;
        this.timer = new Timer(time);
    }
    
    @Override
    public void run()
    {
        try 
        {
            Robot robot = new Robot();
            Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage bufferedImage;
            running = true;
            int counter = 0;
            System.out.println("PictureTaker has started.");
            while(running)
            {    
                if(timer.isTime()){
                    timer.went();
                    // Sync Screen More For Linux/Mac
                    //Toolkit.getDefaultToolkit().sync();
                    bufferedImage = robot.createScreenCapture(captureSize);
                    data.add(new PicNode(bufferedImage, counter + ".jpg"));
                    //System.out.println("Picture: " + counter);
                    counter++;
                    //this.yield();
                }
            }
        }
        catch(AWTException ex)
        { 
            System.out.println("AWT-EXCEPTION");
        }    
    }
    
    public synchronized void kill()
    {
        this.running = false;
    }
    
    public synchronized ConcurrentLinkedQueue<PicNode> getData()
    {
        ConcurrentLinkedQueue<PicNode> temp = data;
        data = new ConcurrentLinkedQueue<PicNode>();
        return temp;
    }    
}
