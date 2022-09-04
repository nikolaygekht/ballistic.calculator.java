package gehtsoft.ballisticcalculator.Drag;

/**
 * A node of a drag table for polynomial table approximation
 */
class DragTableNode implements IDragTableNode {
    private double mMach;
    private double mDragCoefficient;
    private double mA, mB, mC;
    private IDragTableNode mNext;
    private IDragTableNode mPrevious;

    /** Gets mach speed */
    public double getMach() {
        return mMach;
    }
    /** Gets drag coefficient */
    public double getDragCoefficient() {
        return mDragCoefficient;
    }

    /** Gets the next node */
    public IDragTableNode getNext() {
        return mNext;
    }

    /** Gets the previous node */
    public IDragTableNode getPrevious() {
        return mPrevious;
    }

    /** Calculates for the specified velocity */
    public double calculateDrag(double mach) {
        return mC + mach * (mB + mA * mach);
    }

    /** Checks whether the specified velocity is withing the node range */
    public Boolean in(double mach) {
        if (mach < mMach)
            return false;
        if (mNext == null)
            return true;
        return mach < mNext.getMach();
    }

    private void setNext(DragTableNode next) {
        mNext = next;
    }

    /** Constructor
     * @param mach the minimum mach velocity covered by the node
     * @param dragCoefficient the drag coefficient at the minimum velocity
     * @param a the first coefficient of the polynomial
     * @param b the second coefficient of the polynomial
     * @param c the third coefficient of the polynomial
     * @param previous the previous node
     */
    public DragTableNode(double mach, double dragCoefficient, double a, double b, double c, DragTableNode previous) {
        if (previous == null) {
            if (mach > 1e-7)
                throw new IllegalArgumentException("The first node must have mach = 0");
        } else {
            if (mach <= previous.getMach())
                throw new IllegalArgumentException("The mach velocity must be greater than the previous node");
        }
        
        mMach = mach;
        mDragCoefficient = dragCoefficient;
        mPrevious = previous;
        mA = a;
        mB = b;
        mC = c;
        mNext = null;
        if (previous != null)
            previous.setNext(this);
        
    }
}
