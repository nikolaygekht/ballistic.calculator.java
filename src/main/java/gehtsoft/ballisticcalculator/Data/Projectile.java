package gehtsoft.ballisticcalculator.Data;

import javax.measure.*;
import javax.measure.quantity.*;

import gehtsoft.ballisticcalculator.Units.UnitUtils;
import systems.uom.common.Imperial;

/**
 * The information about a projectile
 */
public class Projectile {
    private Quantity<Mass> mBulletWeight;
    private Quantity<Speed> mMuzzleVelocity;
    private BallisticCoefficient mBallisticCoefficient;
    private Quantity<Length> mBulletDiameter;
    private Quantity<Length> mBulletLength;

    /** Returns the weight of the bullet */
    public Quantity<Mass> getBulletWeight() {
        return mBulletWeight;
    }

    /** Returns the muzzle velocity */
    public Quantity<Speed> getMuzzleVelocity() {
        return mMuzzleVelocity;
    }

    /** The ballistic coefficient of a projectile */
    public BallisticCoefficient getBallisticCoefficient() {
        return mBallisticCoefficient;
    }

    /** The bullet diameter
     *
     * The bullet diameter may be null. If bullet diameter is not set,
     * the ballistic coefficient cannot be a form factor
    */
    public Quantity<Length> getBulletDiameter() {
        return mBulletDiameter;
    }

    /** The bullet length.
     *
     * The bullet length may be null. If bullet length is not set,
     * the bullet precession cannot be calculated.
     *
     */
    public Quantity<Length> getBulletLength() {
        return mBulletLength;
    }

    /**
     * Constructor (minimum information)
     */
    public Projectile(Quantity<Mass> bulletWeight, Quantity<Speed> muzzleVelocity, BallisticCoefficient bc) {
        mBulletWeight = bulletWeight;
        mMuzzleVelocity = muzzleVelocity;
        mBallisticCoefficient = bc;
    }

    /**
     * Constructor (with bullet dimensions)
     */
    public Projectile(Quantity<Mass> bulletWeight, Quantity<Speed> muzzleVelocity, BallisticCoefficient bc, Quantity<Length> bulletDiameter, Quantity<Length> bulletLength) {
        mBulletWeight = bulletWeight;
        mMuzzleVelocity = muzzleVelocity;
        mBallisticCoefficient = bc;
        mBulletDiameter = bulletDiameter;
        mBulletLength = bulletLength;
    }

    /** Get ballistic coefficient.
     *
     * If the value if a form factor, it calculates the coefficient via sectional density.
     * In this case, the bullet diameter and weight (for an etalon bullet) is required.
     */
    public double getBallisticCoefficientValue() {
        if (mBallisticCoefficient.getType() == BallisticCoefficientValueType.Coefficient) {
            return mBallisticCoefficient.getValue();
        } else {
            if (mBulletDiameter == null || mBulletDiameter.getValue().doubleValue() <= 0)
                throw new IllegalStateException("If ballistic coefficient is a form factor, the bullet diameter must be set");
            if (mBulletWeight == null || mBulletWeight.getValue().doubleValue() <= 0)
                throw new IllegalStateException("If ballistic coefficient is a form factor, the bullet weight must be set");

            double diameter = UnitUtils.in(mBulletDiameter, Imperial.INCH);
            double weight = UnitUtils.in(mBulletWeight, Imperial.POUND);

            return weight / Math.pow(diameter, 2) / mBallisticCoefficient.getValue();
        }
    }
}
