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
                if(data != null && !data.isEmpty())
                {
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
        while(!data.isEmpty())
        {
            this.data.add(data.remove());
        }
    }
    
    /**
     * @return If there is still data to be written return true. 
     */
    public synchronized boolean hasData()
    {
        return (this.data.size() > 0 ? true : false);
    }
}
