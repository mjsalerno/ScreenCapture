package screencapture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.imageio.ImageIO;

public class PictureWriterThread extends Thread
{
    private ConcurrentLinkedQueue<PicNode> data; 
    private boolean running;
    
    public PictureWriterThread()
    {
        data = new ConcurrentLinkedQueue<PicNode>();
        this.running = false;
    }
    
    @Override
    public void run()
    {
        running = true;
        PicNode pn;
        System.out.println("PictureWriter has started.");
        try
        {
            while(running)
            {
                pn = data.poll();
                if(pn != null)
                {
                    System.out.println("wrote");
                    pn = data.remove();
                    ImageIO.write(pn.getImage(), "jpg", new File(pn.getFilePath()));
                }
                else
                {
                    // Sleep for alittle bit
                    this.yield();
                }
              }
        }
        catch(Exception ex){}
    }
    
    public synchronized void kill()
    {
        this.running = false;
    }
    
    public synchronized void addData(ConcurrentLinkedQueue<PicNode> data)
    {
        this.data.addAll(data);
    }
}
