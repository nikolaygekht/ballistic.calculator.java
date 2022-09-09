package gehtsoft.ballisticcalculator;

import javax.measure.Quantity;
import javax.measure.Unit;

import gehtsoft.ballisticcalculator.Units.UnitUtils;
import tech.units.indriya.quantity.Quantities;

/** 3D vector for calculations */
class QuantityVector<T extends Quantity<T>> {
    private Quantity<T> mX, mY, mZ;

    /** Returns X coordinate */
    public Quantity<T> getX() {
        return mX;
    }

    /** Returns Y coordinate */
    public Quantity<T> getY() {
        return mY;
    }

    /** Returns Z coordinate */
    public Quantity<T> getZ() {
        return mZ;
    }
    
    /**
     * Constructor
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public QuantityVector(Quantity<T> x, Quantity<T> y, Quantity<T> z) {
        mX = x;
        mY = y;
        mZ = z;
    }

    /** Returns magnitude of the vector */
    public Quantity<T> getMagnitude() {
        Unit<T> t = mX.getUnit();

        return Quantities.getQuantity(Math.sqrt(
            Math.pow(UnitUtils.in(mX, t), 2) +
            Math.pow(UnitUtils.in(mY, t), 2) +
            Math.pow(UnitUtils.in(mZ, t), 2)), t);
    }

    private static <T extends Quantity<T>> Quantity<T> add(Quantity<T> a, Quantity<T> b) {
        Unit<T> t = a.getUnit();
        if (b.getUnit() != t) {
            return Quantities.getQuantity(a.getValue().doubleValue() +
                                   UnitUtils.in(b, t), t);
        } else {
            return Quantities.getQuantity(a.getValue().doubleValue() +
                                   b.getValue().doubleValue(), t);        
        }
    }

    /** Adds a vector */
    public QuantityVector<T> add(QuantityVector<T> v) {
        return new QuantityVector<T>(add(mX, v.mX), add(mY, v.mY), add(mZ, v.mZ));
    }

    /** Subtract a vector */
    public QuantityVector<T> subtract(QuantityVector<T> v) {
        return new QuantityVector<T>(add(mX, v.mX.negate()), add(mY, v.mY.negate()), add(mZ, v.mZ.negate()));
    }

    /** Gets distance between two vectors */
    public Quantity<T> getDistance(QuantityVector<T> x) {
        return this.subtract(x).getMagnitude();
    }

    /** Multiple vector by a constant */
    public QuantityVector<T> multiply(double v) {
        return new QuantityVector<T>(mX.multiply(v), mY.multiply(v), mZ.multiply(v));
    }
}
