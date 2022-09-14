package gehtsoft.ballisticcalculator;

import javax.measure.*;
import javax.measure.quantity.*;

import si.uom.SI;
import tech.units.indriya.quantity.Quantities;

/**
 * One point of a calculated trajectory.
 */
public class TrajectoryPoint
{
    private Quantity<Length> mDistance;
    private Quantity<Speed> mVelocity;
    private double mMach;
    private Quantity<Length> mDrop;
    private Quantity<Length> mWindage;
    private Quantity<Energy> mEnergy;
    private Quantity<Angle> mHold;
    private Quantity<Angle> mWindageAdjustment;
    private Quantity<Time> mTime;

    /**
     * The distance from the start of the trajectory.
     */
    public Quantity<Length> getDistance() {
        return mDistance;
    }

    /**
     * The velocity of the projectile
     */
    public Quantity<Speed> getVelocity() {
        return mVelocity;
    }

    /**
     * The mach number corresponding to the velocity in the current weather conditions.
     * @return
     */
    public double getMach() {
        return mMach;
    }

    /**
     * The drop (vertical deviation) of the projectile vs the line of sight.
     * 
     * Positive value is over the line of sight, negative value is under the line of sight.
     */
    public Quantity<Length> getDrop() {
        return mDrop;
    }

    /**
     * The windage (horizontal deviation) of the projectile vs the line of sight.
     * 
     * Positive value is to the left of the line of sight, negative value is to the right of the line of sight.
     */
    public Quantity<Length> getWindage() {
        return mWindage;
    }

    /** 
     * The kinetic energy of the projectile.
     */
    public Quantity<Energy> getEnergy() {
        return mEnergy;       
    }
    
    /**
     * Sight adjustment to the drop.
     */
    public Quantity<Angle> getHold() {
        return mHold;
    }

    /**
     * Sight adjustment to the windage.
     */
    public Quantity<Angle> getWindageAdjustment() {
        return mWindageAdjustment;
    }

    /**
     * Sight adjustment to the windage.
     */
    public Quantity<Time> getFlightTime() {
        return mTime;
    }

    /** 
     * Constructor (for calculation)
     */
    public TrajectoryPoint(final Quantity<Mass> bulletWeight,
                           final Quantity<Length> distance, 
                           final Quantity<Speed> velocity, 
                           final double mach, 
                           final Quantity<Length> drop, 
                           final Quantity<Length> windage, 
                           Quantity<Time> time) {
        mDistance = distance;
        mVelocity = velocity;
        mMach = mach;
        mDrop = drop;
        mWindage = windage;
        mEnergy = calculateEnergy(bulletWeight, velocity);
        mTime = time;

        if (distance.getValue().doubleValue() > 0) {
            mHold = calculateAngle(distance, drop);
            mWindageAdjustment = calculateAngle(distance, windage);
        } else {
            mHold = null;
            mWindage = null;
        }
    }

    /** 
     * Constructor (for serialization)
     */
    public TrajectoryPoint(final Quantity<Length> distance, 
                           final Quantity<Speed> velocity, 
                           final Quantity<Energy> energy, 
                           final double mach, 
                           final Quantity<Length> drop, 
                           final Quantity<Angle> hold, 
                           final Quantity<Length> windage, 
                           final Quantity<Angle> windageAdjustment, 
                           Quantity<Time> time) {
        mDistance = distance;
        mVelocity = velocity;
        mMach = mach;
        mDrop = drop;
        mWindage = windage;
        mEnergy = energy;
        mTime = time;
        mHold = hold;
        mWindageAdjustment = windageAdjustment;
    }
    
    private static Quantity<Energy> calculateEnergy(Quantity<Mass> mass, Quantity<Speed> velocity) {
        double massInKg = mass.getUnit().getConverterTo(SI.KILOGRAM).convert(mass.getValue()).doubleValue();
        double velocityInMetersPerSecond = velocity.getUnit().getConverterTo(SI.METRE_PER_SECOND).convert(velocity.getValue()).doubleValue();
        double energy = massInKg * velocityInMetersPerSecond * velocityInMetersPerSecond / 2;
        return Quantities.getQuantity(energy, SI.JOULE);
    }

    private static Quantity<Angle> calculateAngle(Quantity<Length> distance, Quantity<Length> deviation) {
        double distanceInMeters = distance.getUnit().getConverterTo(SI.METRE).convert(distance.getValue()).doubleValue();
        double deviationInMeters = deviation.getUnit().getConverterTo(SI.METRE).convert(deviation.getValue()).doubleValue();
        double angleInRadians = Math.atan(deviationInMeters / distanceInMeters);
        return Quantities.getQuantity(angleInRadians, SI.RADIAN);
    }
}