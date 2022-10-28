package gehtsoft.ballisticcalculator.drag;

/**
 * Interface to a drag table
 */
public interface IDragTable {
    /**
     * Get the identifier of the drag table
     */
    public DragTableId getID();

    /**
     * Finds node by velocity
     */
    public IDragTableNode find(double mach);

    /**
     * Gets count of the data points
     */
    public int length();

    /**
     * Gets data point by index.
     */
    public IDragTableNode get(int index);
}
