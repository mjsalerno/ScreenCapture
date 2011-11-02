
package screencapture;

/**
 *
 * @author Michael
 */
public class Timer {
    
    private long startTime;
    private long endTime;
    private final long TRIGGER;
    
    /** Default constructor.
     * Defaults trigger to 10000.
     */
    public Timer(){
        this.TRIGGER = 1000000000;
        this.startTime = System.nanoTime();
    }
    
    /** Complete constructor.
     * 
     * @param trigger - number of nanoseconds to pause.
     */
    public Timer(long trigger){
        this.TRIGGER = trigger;
        this.startTime = System.nanoTime();
    }
    
    /**
     * Tells the timer that the action happened 
     * and to reset the startTime variable.
     */
    public void went(){
        this.startTime = System.nanoTime();
    }
    
    /**
     * ask the timer if enough time has gone by between the last went() 
     * call and now.
     * @return true  - if it is time to run the action again. <br>
     * @return false - if not enough time has passed.
     */
    public boolean isTime(){
        this.endTime = System.nanoTime();
        return ((this.endTime - this.startTime) >= this.TRIGGER);
    }
    
}
