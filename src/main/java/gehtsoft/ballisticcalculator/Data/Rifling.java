package gehtsoft.ballisticcalculator.data;

import javax.measure.Quantity;
import javax.measure.quantity.Length;

/** The information about the rifling.
 * 
 *  The data is optional and is used when 
 *  calculating the drift of the projectile.
 */
public class Rifling {
    private Quantity<Length> mRiflingStep;
    private TwistDirection mTwistDirection;

    /** Gets the step of the rifling */
    public Quantity<Length> getRiflingStep() {
        return mRiflingStep;
    }

    /** Gets the twist direction */
    public TwistDirection getTwistDirection() {
        return mTwistDirection;
    }

    /** Constructor */
    public Rifling(Quantity<Length> riflingStep, TwistDirection twistDirection) {
        mRiflingStep = riflingStep;
        mTwistDirection = twistDirection;
    }
}
