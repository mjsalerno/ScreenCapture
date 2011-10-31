package screencapture;

import java.util.Scanner;

public class ScreenCapture 
{
    public static void main(String[] args) 
    {
        Scanner kb = new Scanner(System.in);
        
        MainThread mt = new MainThread();
        RecorderThread r = new RecorderThread("audio.wav");
        
        r.startRecording();
        mt.start();   
        
        System.out.println("everything is running \n");        
        System.out.println("Press any key to stop recording.");
        
        kb.nextLine();        
        
        r.stopRecording();
        mt.kill();
        
    }
}
