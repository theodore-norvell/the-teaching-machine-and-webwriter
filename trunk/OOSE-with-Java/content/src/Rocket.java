/** Represent information about a simulated rocket operating
 * in a 1 dimensional frictionless space.
 * @author theo
 *
 */
public class Rocket {
    private double position ; // In m
    private double nonFuelMass ; // In kg
    private double fuelMass ; // In kg
    private double velocity ; // In m/s
    private double lastUpdateTime ; // In s
    private static final double burnRate = 1.0 ; // In kg/s
    private static final double force = 100.0 ; // In kg m/s/s 
    
    public Rocket( double initialPosition, double nonFuelMass, double initialFuelMass ) {
        this.position = initialPosition ;
        this.nonFuelMass = nonFuelMass ;
        this.fuelMass = initialPosition ;
        this.velocity = 0.0 ;
        this.lastUpdateTime = 0.0 ;
    }
    
    /** Obtain the position of the rocket at a given time.
     * The time must be greater or equal to the last update time.
     * Sets the last update time to <code>time</code>.
     * @param time The time at which the position must be known
     * @return The position at time <code>time</code>.
     */
    public double getPosition( double time ) {
        update(time);
        return position ; }
    
    public void burn( double startTime, double duration, double direction ) {
        assert duration > 0.0 ;
        assert direction == +1.0 || direction == -1 ;
        update( startTime ) ;
        double fuelUnits = Math.max( duration * burnRate, fuelMass ) ;
        duration = fuelUnits / burnRate ;
        double m0 = nonFuelMass + fuelMass ;
        double t2 = duration * duration ;
        double t3 = t2 * duration ;
        double t4 = t3 * duration ;
        double deltaV = 1.0/force * (burnRate * t3 / 3.0 +
                                     m0 * t2 / 2.0) ;
        double deltaX = velocity * t2 / 2 
                      + direction/force * ( - burnRate * t4 / 4.0 
                                            + m0 * t3 / 3.0) ;
        velocity += direction*deltaV ;
        position += deltaX ;
        fuelMass -= fuelUnits ;
        lastUpdateTime += duration ;
    }

    private void update(double time) {
        assert time >= lastUpdateTime ;
        position += velocity * (time - lastUpdateTime) ;
        lastUpdateTime = time ;
    }
}
