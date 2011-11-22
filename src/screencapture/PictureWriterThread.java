package screencapture;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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
            File file = new File("test.dat");
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            // Byte Buffer Vars
            byte[] buff = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            
            // TODO: DEBUG variable holds timer information.
            long before = 0;
            while(running)
            {
                writeBinaryData(out, pn, buff, bos, before);
                try{this.sleep(ScreenCapture.SLEEP_TIME);}catch(Exception ex){System.out.println("Error Sleeping.");}
            }
            // Write out everything left in the buffer.
            while(!data.isEmpty())
            {
                writeBinaryData(out, pn, buff, bos, before);
                this.yield();
            }
            // Close the stream's
            bos.close();
            out.close();
            // TODO: DEBUG prints out that the PictureWriterThread has ended.
            System.out.println("PictureWriterThread has ended.");
        }
        catch(IOException ex)
        { 
            System.out.println("Unable to create Binary stream.");
        }
    }
    
    /**
     * Creates a .jpg image of the next file in the ConcurrentLinkedQueue data.
     * @param pn A PictureNode used to hold the data removed from the ConcurrentLinkedQueue.
     */
    private synchronized void write(PicNode pn, long before) throws IOException
    {
        if(data != null && !data.isEmpty())
        {
            // Debug Timing
            before = System.currentTimeMillis();
            pn = data.remove();
            ImageIO.write(pn.IMAGE, "jpg", new File(pn.FILE_NAME));
            System.out.println("Write DT: " + (System.currentTimeMillis() - before));
        }
    }
    
    /**
     * Writes PicNode object out to a binary file to be manipulated later.
     * @param out DataOutputStream to binary file.
     * @param pn PicNode containing the next image on the Queue.
     */
    private synchronized void writeBinaryData(DataOutputStream out, PicNode pn, byte[] buff, ByteArrayOutputStream bos, long before) throws IOException
    {
        // New Code writes pictures to Binary file to be processed later.
        if(data != null && !data.isEmpty())
        {
            pn = data.remove();
            // Debug Timing
            before = System.currentTimeMillis();    
            out.writeUTF(pn.FILE_NAME);
            //out.write(pn.getImageBytes(buff, bos));
            System.out.println("Write DT: " + (System.currentTimeMillis() - before));
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
