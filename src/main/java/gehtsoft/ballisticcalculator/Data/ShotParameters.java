package gehtsoft.ballisticcalculator.data;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;

/** The shot parameters */
public class ShotParameters {
    private Quantity<Length> mMaximumDistance;
    private Quantity<Length> mStep;
    
    private Quantity<Angle> mSightAngle;
    private Quantity<Angle> mShotAngle;
    private Quantity<Angle> mCantAngle;
    private Quantity<Angle> mBarrelAzimuth;

    /** Returns the maximum distance */
    public Quantity<Length> getMaximumDistance() {
        return mMaximumDistance;
    }

    /** Returns the calculation step */
    public Quantity<Length> getStep() {
        return mStep;
    }

    /** Returns the sight angle */
    public Quantity<Angle> getSightAngle() {
        return mSightAngle;
    }
    
    /** Returns the shot angle. 
     * 
     * The shot angle is optional. 
     *
     * Please note that shot angle is used only for calculation of the trajectory.
     * 
     * The sight angle is calculated from zero information for a flat shot. 
     */
    public Quantity<Angle> getShotAngle() {
        return mShotAngle;
    }

    /** Returns the weapon cant angle 
     * 
     * The value is optional
    */
    public Quantity<Angle> getCantAngle() {
        return mCantAngle;
    }

    /** Returns the azimuth of the barrel */
    public Quantity<Angle> getBarrelAzimuth() {
        return mBarrelAzimuth;
    }

    /** Minimum constructor 
     * 
     * @param maximumDistance The maximum distance to calculate the trajectory
     * @param step The calculation step
     * @param sightAngle The sight angle. Use trajectory calculator to calculate the sight angle from the zeroing information.
    */
    public ShotParameters(Quantity<Length> maximumDistance, Quantity<Length> step, Quantity<Angle> sightAngle) {
        this(maximumDistance, step, sightAngle, null, null, null);
    }

    /** Full constructor 
     * 
     * @param maximumDistance The maximum distance to calculate the trajectory
     * @param step The calculation step
     * @param sightAngle The sight angle. Use trajectory calculator to calculate the sight angle from the zeroing information.
     * @param shotAngle The shot angle. The parameter is optional
     * @param cantAngle The weapon cant angle. The parameter is optional
     * @param barrelAzimuth The azimuth of the barrel. The parameter is optional
    */
    public ShotParameters(Quantity<Length> maximumDistance, Quantity<Length> step, Quantity<Angle> sightAngle, Quantity<Angle> shotAngle, Quantity<Angle> cantAngle, Quantity<Angle> barrelAzimuth) {
        mMaximumDistance = maximumDistance;
        mStep = step;
        mSightAngle = sightAngle;
        mShotAngle = shotAngle;
        mCantAngle = cantAngle;
        mBarrelAzimuth = barrelAzimuth;
    }
}
