package screencapture;

import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JLabel;

public class MainThread extends Thread 
{
    private boolean running;
    private Timer timer;
    // Create Location to hold data
    private ConcurrentLinkedQueue<PicNode> data;
    private JLabel lblQueueSize;
    
    public MainThread()
    {
        this.lblQueueSize = null;
        this.data = new ConcurrentLinkedQueue<PicNode>();
        this.running = false;
        this.timer = new Timer(1, "seconds"); // sets the timer to one second per update
    }
    
    @Override
    public void run()
    {
        // Create Threads
        PictureTakerThread pt = new PictureTakerThread(data);
        PictureWriterThread pw = new PictureWriterThread(data);
        // Start Child Threads
        pt.start();
        pw.start();
        // Set the running to true
        this.running = true;
        // Start Loop
        while(running)
        {
            if(timer.isTime())
            {
                // Reset the timer
                timer.went();
                // TODO: DEBUG prints out current size of the queue.
                this.lblQueueSize.setText("Queue Size: " + data.size());
                // Suggest Garbage Collect
                System.gc();
            }
            
            try
            {
                this.sleep(1000); // Sleep For awhile
            }
            catch(Exception ex)
            { 
                System.out.println("Error sleeping.");
            }
        }
        
        try 
        {
            // Kill the PictureTakerThread
            pt.kill();
            // Join the PictureTakerThread
            pt.join();
            // Kill the PictureWriterThread
            pw.kill();
            // Join the PictureWriterThread.
            pw.join();
            // TODO: DEBUG prints out main thread is done.
            System.out.println("Main Thread is done.");
        } 
        catch (InterruptedException ex) 
        { 
            System.out.println("Unable to join thread.");
        }
    }
    
    /**
     * Returns if the thread is running or not.
     */
    public synchronized boolean isRunning()
    {
        return this.running;
    }
    
    /**
     * Forces the thread to stop
     */
    public synchronized void kill()
    {
        this.running = false;
    }
    
    /**
     * Sets lblQueueSize to the label in gui.
     */
    public synchronized void setQCounter(JLabel qCounter)
    {
        this.lblQueueSize = qCounter;
    }
}
