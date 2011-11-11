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
            }
            // Sleep For awhile
            this.yield();
        }
        // Kill child Threads
        pt.kill();
        pw.kill();
    }
    
    public synchronized void kill()
    {
        this.running = false;
    }
}
