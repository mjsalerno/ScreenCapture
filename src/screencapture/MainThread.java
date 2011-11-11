package screencapture;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            }
            // Sleep For awhile
            this.yield();
        }
        
        try 
        {
            // Kill Writer Thread
            pt.kill();
            // Join thtread
            pt.join();
            // Get final data from picture taker
            data = pt.getData();
            // Give Final Data to picture writer
            pw.addData(data);
            // Join the Picture writer thread
            while(pw.hasData()) {this.yield();} // Wait or this thread to finish its work.
            // Once it is done working kill the thread.
            pw.kill();
            // Join the thread.
            pw.join();
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
