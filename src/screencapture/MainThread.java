package screencapture;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MainThread extends Thread 
{
    private boolean running;
    private PictureTakerThread pt;
    private PictureWriterThread pw;
    private Timer timer;
    
    public MainThread()
    {
        this.running = false;
        this.pt = new PictureTakerThread();
        this.pw = new PictureWriterThread();
        this.timer = new Timer(1, "seconds"); // sets the timer to one second per update
    }
    
    @Override
    public void run()
    {
        running = true;
        // Start Child Threads
        pt.start();
        pw.start();
        // Create Location to hold data
        ConcurrentLinkedQueue<PicNode> data;
        // Start Loop
        while(running)
        {
            if(timer.isTime())
            {
                // Get Data from the picture Taker
                data = pt.getData();
                pw.addData(data);
                data.clear();
                // Reset the timer
                timer.went();
                // Force Garbage Collect
                System.gc();
            }
            else
            {
                // Sleep For awhile
                this.yield();
            }
        }
        
        try 
        {
            // Kill the PictureTakerThread
            pt.kill();
            // Join the PictureTakerThread
            pt.join();
            // Get final data from picture taker
            data = pt.getData();
            // Give Final Data to picture writer
            pw.addData(data);
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
}
