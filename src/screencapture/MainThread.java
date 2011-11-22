package screencapture;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MainThread extends Thread 
{
    private boolean running;
    // Create Location to hold data
    private ConcurrentLinkedQueue<PicNode> data;
    // Gui
    private ScreenCaptureGui gui;
    private PictureTakerThread pt;
    private PictureWriterThread pw;
    
    public MainThread(ScreenCaptureGui gui)
    {
        this.gui = gui;
        this.running = false;
        this.timer = new Timer(2, "seconds"); // sets the timer to one second per update
        this.data = new ConcurrentLinkedQueue<PicNode>();
        // Initalize Worker Threads
        pt = new PictureTakerThread(data);
        pt.setName("PictureTakerThread");
        pw = new PictureWriterThread(data);
        pw.setName("PictureWriterThread");
        // Set this threads name
        this.setName("Thread Manager");
    }
    
    @Override
    public void run()
    {
        // Set the running to true
        this.running = true;
        // Start Worker Threads
        pt.start();
        pw.start();
                // Adjust QueueSize
                this.gui.lblQueueSize.setText("Queue Size : " + data.size());
    }
            // Always sleep for awhile
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
        catch(Exception ex)
        {
            System.out.println("A problem occurred ending the thread.");
        }
    }
    /**
     * Sets lblQueueSize to the label in gui.
     */
}
