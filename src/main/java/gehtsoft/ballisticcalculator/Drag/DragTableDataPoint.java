package gehtsoft.ballisticcalculator.Drag;

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
     * @param DragCoefficient Drag coefficient
     */
    public DragTableDataPoint(double mach, double DragCoefficient) {
        mMach = mach;
        mDragCoefficient = DragCoefficient;
    }
}
