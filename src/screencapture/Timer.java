package screencapture;

/**
 * @author Michael
 */
public class Timer 
{
    private long startTime;
    private long endTime;
    private final long TRIGGER;
    // Conversion units
    private final long SECONDS_TO_NANO = 1000000000;
    private final long MILI_TO_NANO = 1000000;
    
    /** Default constructor.
     * Defaults trigger to 1000000000 nanoseconds; 1 second.
     */
    public Timer()
    {
        this.TRIGGER = 1000000000; // 1 second in nano time
        this.startTime = System.nanoTime();
    }
    
    /** Complete constructor.
     * @param trigger - number of nanoseconds to pause.
     */
    public Timer(long trigger)
    {
        this.TRIGGER = trigger;
        this.startTime = System.nanoTime();
    }
    
    /**
     * Takes a long value with a string containing the name of the desired units.
     * If the string contains an invalid unit, we default to one second.
     * Possible Units:
     *  seconds<br />
     *  milliseconds<br />
     *  nanoseconds<br />
     * @param trigger Amount of units you want for the timer.
     * @param units Type of units you want for the timer.
     */
    public Timer(long trigger, String units)
    {
        if(units.equalsIgnoreCase("seconds"))
        {
            this.TRIGGER = trigger * this.SECONDS_TO_NANO;
        }
        else if(units.equalsIgnoreCase("milliseconds"))
        {
            this.TRIGGER = trigger * this.MILI_TO_NANO;
        }
        else if(units.equalsIgnoreCase("nanoseconds"))
        {
            this.TRIGGER = trigger;
        }
        else
        {
            System.out.println("Invalid units defaulting to 1 second.");
            this.TRIGGER = this.SECONDS_TO_NANO; // Sets the timer to 1 second.
        }
    }
    
    /**
     * Tells the timer that the action happened 
     * and to reset the startTime variable.
     */
    public void went()
    {
        this.startTime = System.nanoTime();
    }
    
    /**
     * ask the timer if enough time has gone by between the last went() 
     * call and now.
     * @return true  - if it is time to run the action again. <br>
     * @return false - if not enough time has passed.
     */
    public boolean isTime()
    {
        this.endTime = System.nanoTime();
        return ((this.endTime - this.startTime) >= this.TRIGGER);
    }
}
