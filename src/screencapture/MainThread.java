package screencapture;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MainThread extends Thread 
{
    private boolean running;
    private PictureTakerThread pt;
    private PictureWriterThread pw;
    
    public MainThread()
    {
        this.running = false;
        this.pt = new PictureTakerThread();
        this.pw = new PictureWriterThread();
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
            // Get Data from the picture Taker
            data = pt.getData();
            pw.addData(data);
            data.clear();
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
