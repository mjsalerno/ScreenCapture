package screencapture;

public class ScreenCapture 
{
    public final static long SLEEP_TIME = 200; 
    public final static int DEBUG = 0; // 0 = false; 1 = true
    
    /**
     * Main entrance to the program.
     * @param args 
     */
    public static void main(String[] args) 
    {
        /*
        MainThread mt = new MainThread();
        mt.start();
         */
        ScreenCaptureGui gui = new ScreenCaptureGui("OGS");
        gui.setVisible(true); // Make the gui visible
    }
}
