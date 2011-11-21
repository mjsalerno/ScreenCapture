package screencapture;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.imageio.ImageIO;

public class PictureWriterThread extends Thread
{
    private ConcurrentLinkedQueue<PicNode> data; 
    private boolean running;
    
    /**
     * Default Constructor
     * Creates a new ConcurrentLinkedQueue to hold data taken from the PictureTakerThread.
     * Sets running to true; This makes our loop in run tick.
     */
    public PictureWriterThread(ConcurrentLinkedQueue<PicNode> data)
    {
        this.data = data;
        this.running = true;
    }
    
    /**
     * Main operation of the PictureWriterThread.
     * Converts the data from the PictureTakerThread and turns them into images.
     */
    @Override
    public void run()
    {
        // TODO: DEBUG prints out that the PictureWriterThread has started.
        System.out.println("PictureWriter has started.");
        // Sets a node that is going to be reused many times to null.
        PicNode pn = null;
        
        try
        {
            // Binary Stream Data
            File file = new File("Test.dat");
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            // Debug Timing
            long before = 0;
            while(running)
            {
                long before = System.currentTimeMillis();
                writeBinary(out, pn);
                System.out.println("Write DT: " + (System.currentTimeMillis() - before));
                this.yield();
            }
            // Write out everything left in the buffer.
            while(!data.isEmpty())
            {
                writeBinary(out, pn);
                this.yield();
            }
            // Close the Stream
            out.close();
            // TODO: DEBUG prints out that the PictureWriterThread has ended.
            System.out.println("PictureWriterThread has ended.");
        }
        catch(Exception ex)
        { 
            System.out.println("Unable to create Binary stream.");
        }
    }
    
    /**
     * Creates a .jpg image of the next file in the ConcurrentLinkedQueue data.
     * @param pn A PictureNode used to hold the data removed from the ConcurrentLinkedQueue.
     */
    private synchronized void write(PicNode pn)
    {
        try
        {
            if(data != null && !data.isEmpty())
            {
                pn = data.remove();
                ImageIO.write(pn.getImage(), "jpg", new File(pn.getFilePath()));
            }
        }
        catch(Exception ex){}
    }
    
    /**
     * Writes PicNode object out to a binary file to be manipulated later.
     * @param out DataOutputStream to binary file.
     * @param pn PicNode containing the next image on the Queue.
     */
    private synchronized void writeBinary(DataOutputStream out, PicNode pn) throws IOException
    {
        // New Code writes pictures to Binary file to be processed later.
        if(data != null && !data.isEmpty())
        {
            pn = data.remove();
            out.writeUTF(pn.getFilePath());
            out.write(pn.getImageBytes());
        }
    }
    
    /**
     * Ends the main loop in the run method.
     */
    public synchronized void kill()
    {
        this.running = false;
    }
    
    /**
     * @return If there is still data in the ConcurrentLinkedQueue return true. 
     */
    public synchronized boolean hasData()
    {
        return (this.data.size() > 0 ? true : false);
    }
}
