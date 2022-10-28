package gehtsoft.ballisticcalculator.drag;

/** 
 * The data point of the source drag table
 */
public class DragTableDataPoint {
    private double mMach;
    private double mDragCoefficient;

    /** Gets mach speed */
    public double getMach() {
        return mMach;
    }

    /** Gets drag coefficient */
    public double getDragCoefficient() {
        return mDragCoefficient;
    }

    /**
     * Constructor
     * @param mach Mach speed
     * @param dragCoefficient Drag coefficient
     */
    public DragTableDataPoint(double mach, double dragCoefficient) {
        mMach = mach;
        mDragCoefficient = dragCoefficient;
    }
}
