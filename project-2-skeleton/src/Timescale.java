import static java.lang.Math.pow;

/**
 * Timescale directly affects spawn rate and speed of spawned objects
 */
public class Timescale {

    private final static int DEFAULT_TIMESCALE = 1;
    private final static int TIMESCALE_INCREMENT = 1;
    private final static int MIN_TIMESCALE = 1;
    private final static int MAX_TIMESCALE = 5;
    private final static int EFFECT_PERCENTAGE = 50;
    private final static double EFFECT = 1 + EFFECT_PERCENTAGE/100.0;

    private int timescale;
    private double timescaleEffect;


    /**
     * Changes TimeScale i.e. increases or decreases timescale
     * @param isL for key pressed - K or L
     */
    public void changeTimescale(boolean isL){

        if(isL)
            timescale = Integer.min(MAX_TIMESCALE, timescale + TIMESCALE_INCREMENT);

        else
            timescale = Integer.max(MIN_TIMESCALE, timescale - TIMESCALE_INCREMENT);

        timescaleEffect = pow(EFFECT, timescale - 1);
    }


    /**
     * @return multiplier of spawn rate and movement speed
     */
    public double getTimescaleEffect(){ return timescaleEffect; }


    /**
     * Default constructor for timescale. Initialises timescale and its effects
     */
    Timescale(){ timescaleEffect = timescale = DEFAULT_TIMESCALE; }
}
