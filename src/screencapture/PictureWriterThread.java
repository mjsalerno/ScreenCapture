package screencapture;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
            // Vars for Java.NIO
            File outFile = new File("test.dat");
            FileOutputStream fos = new FileOutputStream(outFile);
            FileChannel fc = fos.getChannel();
            ByteBuffer buffer = null;
            // TODO: DEBUG variable holds timer information.
            long before = 0;
            while(running)
            {
                buffer = ByteBuffer.wrap(data.remove().getImageBytes());
                //writeBinary(out, pn, before);
                writeBinaryData2(buffer, fc);
                //write(pn, before);
                this.yield();
            }
            // Write out everything left in the buffer.
            while(!data.isEmpty())
            {
                buffer = ByteBuffer.wrap(data.remove().getImageBytes());
                //writeBinary(out, pn, before);
                writeBinaryData2(buffer, fc);
                //write(pn, before);
                this.yield();
            }
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
    private synchronized void writeBinary(DataOutputStream out, PicNode pn, long before) throws IOException
    {
        // New Code writes pictures to Binary file to be processed later.
        if(data != null && !data.isEmpty())
        {
            pn = data.remove();
            // Debug Timing
            before = System.currentTimeMillis();    
            out.writeUTF(pn.FILE_NAME);
            out.write(pn.getImageBytes());
            System.out.println("Write DT: " + (System.currentTimeMillis() - before));
        }
    }
    
    /**
     * Uses java nio to write out binary files.
     */
    private void writeBinaryData2(ByteBuffer buffer, FileChannel fc) throws IOException
    {
        buffer.flip();
        fc.write(buffer);
        buffer.clear();        
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
