package gehtsoft.ballisticcalculator;

/** 3D vector for calculations */
class Vector {
    private double mX, mY, mZ;

    /** Returns X coordinate */
    public double getX() {
        return mX;
    }

    /** Returns Y coordinate */
    public double getY() {
        return mY;
    }

    /** Returns Z coordinate */
    public double getZ() {
        return mZ;
    }
    
    /**
     * Constructor
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public Vector(double x, double y, double z) {
        mX = x;
        mY = y;
        mZ = z;
    }

    /** Returns magnitude of the vector */
    public double getMagnitude() {
        return Math.sqrt(mX * mX + mY * mY + mZ * mZ);
    }

    /** Adds a vector */
    public Vector add(Vector v) {
        return new Vector(mX + v.mX, mY + v.mY, mZ + v.mZ);
    }

    /** Subtract a vector */
    public Vector subtract(Vector v) {
        return new Vector(mX - v.mX, mY - v.mY, mZ - v.mZ);
    }

    /** Gets distance between two vectors */
    public double getDistance(Vector x) {
        return this.subtract(x).getMagnitude();
    }

    /** Normalize vector */
    public Vector normalize() {
        double magnitude = getMagnitude();
        if (magnitude < 1e-10) {
            return this;
        }

        return new Vector(mX / magnitude, mY / magnitude, mZ / magnitude);
    }

    /** Multiple vector by a constant */
    public Vector mul(double v) {
        return new Vector(mX * v, mY* v, mZ * v);
    }
}
