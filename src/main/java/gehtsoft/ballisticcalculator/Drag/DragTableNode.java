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
    public double CalculateDrag(double mach) {
        return mC + mach * (mB + mA * mach);
    }

    /** Checks whether the specified velocity is withing the node range */
    public Boolean In(double mach) {
        if (mach < mMach)
            return false;
        if (mNext == null)
            return true;
        return mach < mNext.getMach();
    }

    private void setNext(DragTableNode next) {
        mNext = next;
    }

    public DragTableNode(double mach, double dragCoefficient, double a, double b, double c, DragTableNode previous) {
        mMach = mach;
        mDragCoefficient = dragCoefficient;
        mPrevious = previous;
        mNext = null;
        if (previous != null)
            previous.setNext(this);
        
    }
}
