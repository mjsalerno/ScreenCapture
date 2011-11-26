package screencapture;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author paul
 */
public class ScreenCaptureGui extends JFrame implements ActionListener 
{
    // Threads
    private MainThread mt;
    //JPanel
    private JPanel mainPanel;
    // Buttons
    private JButton btnRecord;
    // label
    protected JLabel lblQueueSize;
    // Gui Properties
    private String title;
    
    /**
     * Complete Constructor
     * @param title Takes base title for the gui. 
     */
    public ScreenCaptureGui(String title)
    {
        super(title);
        // Gui
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(300, 300);
        this.setResizable(false);
        this.mainPanel = new JPanel();
        this.btnRecord = new JButton("RECORD");
        this.lblQueueSize = new JLabel("Queue Size: 0");
        this.btnRecord.addActionListener(this);
        this.mainPanel.add(lblQueueSize);
        this.mainPanel.add(btnRecord);
        this.add(mainPanel);
        // Threads
        this.mt = null;
        // Properties
        this.title = title;
    }
    
    /**
     * Checks for what actions are performed in the gui.
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        // If the record button is pressed
        if(e.getSource() == btnRecord)
        {
            if(mt == null)
            {
                // Create a new Thread
                mt = new MainThread(this);
                mt.start();
                // Change the title and button text 
                this.setTitle(title + " - Recording");
                btnRecord.setText("STOP");
                // TODO: DEBUG prints out Starting MainThread
                System.out.println("Starting the MainThread.");

            }
            else
            {
                // Change the title and button text to indicate that work is still being done.
                btnRecord.setText("WRITING DATA");
                btnRecord.setEnabled(false);
                this.setTitle(title + " - WRITING DATA");
                // Force a repaint of gui; this.repaint() doesnt seem to work on linux.
                this.paint(this.getGraphics());
                // TODO: DEBUG prints out that we are killing the main thread.
                System.out.println("Killing Main Thread.");
                // End the MainThread
                mt.kill();
                // Join the Main Thread
                try
                {
                    // TODO: DEBUG prints out that the main thread has been joined.
                    System.out.println("Main Thread Joined.");
                    mt.join();
                }
                catch(Exception ex)
                { 
                    System.out.println("Main Thread Join Failed.");
                }
                // TODO: DEBUG prints out when control from the thread join is handed back to the gui.
                System.out.println("GUI MT DONE");
                // Once the join is completed set everything back to normal.
                btnRecord.setEnabled(true);
                this.setTitle(title);
                btnRecord.setText("RECORD");
                // Set MainThread to null
                mt = null;
                // Recomend a Garbage Collect incase things are still hanging around.
                System.gc();
            }
        }
    }
}
