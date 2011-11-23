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
public class ScreenCaptureGui extends JFrame implements ActionListener {

    private String title;
    // Threads
    private MainThread mt;
    //JPanel
    private JPanel mainPanel;
    // Buttons
    private JButton btnRecord;
    private JButton btnPause;
    // label
    public JLabel lblQueueSize;

    /**
     * Complete Constructor
     * @param title Takes base title for the gui. 
     */
    public ScreenCaptureGui(String title) {
        super(title);
        this.title = title;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(300, 300);
        this.setResizable(false);

        this.mainPanel = new JPanel();
        this.btnRecord = new JButton("RECORD");
        this.btnPause = new JButton("Pause");
        this.lblQueueSize = new JLabel("queue size");
        this.btnRecord.addActionListener(this);

        this.btnPause.addActionListener(this);

        this.mainPanel.add(lblQueueSize);
        this.mainPanel.add(btnRecord);
        this.btnPause.addActionListener(this);

        this.mainPanel.add(btnPause);
        this.add(mainPanel);

        this.mt = new MainThread(this);
    }

    /**
     * Checks for what actions are performed in the gui.
     * @param e 
     */
    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        // If the record button is pressed
        if (e.getSource() == btnRecord) {
            if (!mt.isRunning()) {
                mt = new MainThread(this);
                this.setTitle(title + " - Recording");
                btnRecord.setText("STOP");
                mt.setQCounter(lblQueueSize);
                mt.start();
                // TODO: DEBUG prints out Starting MainThread
                System.out.println("Starting the MainThread.");
            } else {
                this.mt.setPictureTakerPaused(false);
                btnRecord.setText("WRITING DATA");
                btnRecord.setEnabled(false);
                this.setTitle(title + " - WRITING DATA");
                this.paint(this.getGraphics());

                // TODO: DEBUG prints out that we are killing the main thread.
                System.out.println("Killing Main Thread.");
                // End the MainThread
                mt.kill();
                try {
                    // TODO: DEBUG prints out that the main thread has been joined.
                    System.out.println("Main Thread Joined.");
                    mt.join();
                } catch (Exception ex) {
                    System.out.println("Main Thread Join Failed.");
                }
                // TODO: DEBUG prints out when control from the thread join is handed back to the gui.
                System.out.println("GUI MT DONE");
                // Once the join is completed set everything back to normal.
                btnRecord.setEnabled(true);
                this.setTitle(title);
                btnRecord.setText("RECORD");
            }
        } else if (e.getSource() == btnPause) {
            mt.pausePictureTaker();
        }
    }
}
