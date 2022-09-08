package gehtsoft.ballisticcalculator.Data;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Speed;

/** The wind information */
public class Wind {
    public Quantity<Speed> mSpeed;
    public Quantity<Angle> mDirection;

    /** Returns the wind speed */
    public Quantity<Speed> getSpeed() {
        return mSpeed;
    }

    /** Returns the wind direction 
     * 
     * The wind direction is interpreted as following:
     * - 0 degrees - the wind toward the shooter
     * - 90 degrees - the wind from left of the shooter
     * - 180 degrees - the wind toward the target
     * - 270/-90 degrees - the wind from right of the shooter
     */
    public Quantity<Angle> getDirection() {
        return mDirection;
    }

    /** Constructor 
     * 
     * @param speed the wind speed
     * @param direction the wind direction
     * 
     * The wind direction is interpreted as following:
     * - 0 degrees - the wind toward the shooter
     * - 90 degrees - the wind from left of the shooter
     * - 180 degrees - the wind toward the target
     * - 270/-90 degrees - the wind from right of the shooter
    */
    public Wind(Quantity<Speed> speed, Quantity<Angle> direction) {
        mSpeed = speed;
        mDirection = direction;
    }
}
