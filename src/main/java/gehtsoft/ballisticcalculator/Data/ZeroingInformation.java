package gehtsoft.ballisticcalculator.Data;

import javax.measure.Quantity;
import javax.measure.quantity.Length;

/** The information about weapon zeroing */
public class ZeroingInformation {
    private Quantity<Length> mSightHeight;
    private Quantity<Length> mZeroDistance;

    /** Returns the sight height */
    public Quantity<Length> getSightHeight() {
        return mSightHeight;
    }

    /** Returns the zeroing distance */
    public Quantity<Length> getZeroDistance() {
        return mZeroDistance;
    }

    /** Constructor */
    public ZeroingInformation(Quantity<Length> sightHeight, Quantity<Length> zeroDistance) {
        mSightHeight = sightHeight;
        mZeroDistance = zeroDistance;
    }
}
