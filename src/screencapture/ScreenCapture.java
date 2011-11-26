package screencapture;

public class ScreenCapture 
{
    public final static int DEBUG = 0; // 0 = false; 1 = true
    
    /**
     * Main entrance to the program.
     * @param args 
     */
    public static void main(String[] args) 
    {
        ScreenCaptureGui gui = new ScreenCaptureGui("OGS");
        gui.setVisible(true); // Make the gui visible
    }
}
